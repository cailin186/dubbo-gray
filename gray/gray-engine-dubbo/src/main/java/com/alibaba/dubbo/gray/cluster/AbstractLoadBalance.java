package com.alibaba.dubbo.gray.cluster;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.gray.data.Application;
import com.alibaba.dubbo.gray.data.ApplicationService;
import com.alibaba.dubbo.gray.data.GrayDataCache;
import com.alibaba.dubbo.gray.data.Strategy;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.LoadBalance;
import com.xlz.commons.config.Way;
import com.xlz.commons.utils.ReflectUtils;

/**
 * AbstractLoadBalance
 * 
 * @author william.liangf
 */
public abstract class AbstractLoadBalance implements LoadBalance {
	
	private final ConcurrentMap<String, AtomicLong> totalCount = new ConcurrentHashMap<String, AtomicLong>();

    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        if (invokers == null || invokers.size() == 0)
            return null;
        if (invokers.size() == 1)
            return invokers.get(0);
        //定义新的Invoker列表
        List<Invoker<T>> newInvokers = new ArrayList<Invoker<T>>();
        newInvokers.addAll(invokers);
        //如果预请求的provider的服务方法或系统已经开启灰度，则判断灰度逻辑，如果满足直接走灰度，否则继续向下选择
        URL providerUrl = (URL) ReflectUtils.getInstanceFieldValue(invokers.get(0), "providerUrl");
        String applicationId = providerUrl.getParameter(Constants.APPLICATION_KEY);
        
        Application application = GrayDataCache.getInstance().getApplication(applicationId);
        if(application != null && application.getStatus() != null && application.getStatus().intValue() != 0){
        	//灰度已经开启，将灰度机器从Invoker列表中去掉
        	Invoker<T> grayInvoker = null;
        	for(Invoker<T> invoker : invokers){
        		String grayInstance = invoker.getUrl().getHost() + ":" + invoker.getUrl().getPort();
        		if(application.getGrayInstance().contains(grayInstance)){
        			newInvokers.remove(invoker);
        			grayInvoker = invoker;
        		}
        	}
        	if(grayInvoker != null){
        		//获取灰度参数所对应的参数索引
        		String serviceName = url.getServiceInterface() + "." + invocation.getMethodName();
        		if(application.getStatus().intValue() == 1){
        			//判断当前接口服务是否开启灰度
        			ApplicationService applicationService= application.getServices().get(serviceName);
        			if(applicationService!=null && applicationService.getStatus().intValue() == 1){
	            		if(isGray(application.getApplicationId()+serviceName,applicationService.getWhitelists(),applicationService.getStrategy(), invocation,application.getParam())){
	            			return grayInvoker;
	            		}
        			}else{
	            		//开启应用级别灰度
	            		if(isGray(application.getApplicationId(),application.getWhitelists(),application.getStrategy(), invocation,application.getParam())){
	            			return grayInvoker;
	            		}
        			}
            	}else if(application.getStatus().intValue() == 2){
            		//开启服务级别灰度
            		ApplicationService applicationService= application.getServices().get(serviceName);
            		if(isGray(application.getApplicationId()+serviceName,applicationService.getWhitelists(),applicationService.getStrategy(), invocation,application.getParam())){
            			return grayInvoker;
            		}
            	}
        	}
        }
        return doSelect(newInvokers, url, invocation);
    }

    private boolean isGray(String prefix,Set<String> whitelists,
    		Strategy strategy,Invocation invocation,String paramName){
    	if(strategy.getWay() == Way.whitelist){
    		//白名单匹配处理
    		for(Object obj : invocation.getArguments()){
    			String paramValue = (String) ReflectUtils.getInstanceFieldValue(obj,paramName);
	    		if(paramValue!= null && whitelists.contains(paramName+":"+paramValue)){
	    			return true;
	    		}
    		}
    	}else if(strategy.getWay() == Way.flowTatio){
    		int flowTatio = strategy.getFlowTatio();
    		AtomicLong  total = totalCount.get(prefix);
    		if(total == null){
    			totalCount.put(prefix, new AtomicLong());
    		}
    		long newCount = totalCount.get(prefix).incrementAndGet();
    		if(newCount % 100 <= flowTatio){
    			return true;
    		}
    	}else if(strategy.getWay() == Way.ip){
    	}else if(strategy.getWay() == Way.weight){
    	}else if(strategy.getWay() == Way.business){
    	}else if(strategy.getWay() == Way.regular){
    		for(Object obj : invocation.getArguments()){
	    		String paramValue = (String) ReflectUtils.getInstanceFieldValue(obj,paramName);
	    	    if(paramValue!= null){
		    		Matcher matcher = strategy.getRegular().matcher(paramValue);
		    	    // 字符串是否与正则表达式相匹配
		    	    boolean rs = matcher.find();
		    	    if(rs){
		    	    	return true;
		    	    }
	    	    }
    		}
    	}
    	return false;
    }
    
    protected abstract <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation);

    protected int getWeight(Invoker<?> invoker, Invocation invocation) {
        int weight = invoker.getUrl().getMethodParameter(invocation.getMethodName(), Constants.WEIGHT_KEY, Constants.DEFAULT_WEIGHT);
        if (weight > 0) {
	        long timestamp = invoker.getUrl().getParameter(Constants.TIMESTAMP_KEY, 0L);
	    	if (timestamp > 0L) {
	    		int uptime = (int) (System.currentTimeMillis() - timestamp);
	    		int warmup = invoker.getUrl().getParameter(Constants.WARMUP_KEY, Constants.DEFAULT_WARMUP);
	    		if (uptime > 0 && uptime < warmup) {
	    			weight = calculateWarmupWeight(uptime, warmup, weight);
	    		}
	    	}
        }
    	return weight;
    }
    
    static int calculateWarmupWeight(int uptime, int warmup, int weight) {
    	int ww = (int) ( (float) uptime / ( (float) warmup / (float) weight ) );
    	return ww < 1 ? 1 : (ww > weight ? weight : ww);
    }

}