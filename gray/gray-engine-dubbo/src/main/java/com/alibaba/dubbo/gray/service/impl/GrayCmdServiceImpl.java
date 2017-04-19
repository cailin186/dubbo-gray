package com.alibaba.dubbo.gray.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.ParseException;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.gray.data.Application;
import com.alibaba.dubbo.gray.data.ApplicationService;
import com.alibaba.dubbo.gray.data.GrayDataCache;
import com.alibaba.dubbo.gray.data.Strategy;
import com.alibaba.dubbo.gray.service.Cmd;
import com.alibaba.dubbo.gray.service.GrayCmdService;
import com.xlz.commons.config.Way;
import com.xlz.commons.result.Result;
import com.xlz.commons.result.ResultCode;
import com.xlz.commons.utils.HttpUtil;

public class GrayCmdServiceImpl implements GrayCmdService,ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LogManager.getLogger(getClass());
	@Autowired
    private ApplicationConfig applicationConfig;
	private String url = "http://100.66.153.186/gray/pullApplicationConfig/";
	
	public void setUrl(String url) {
		this.url = url;
	}

	public void setApplicationConfig(ApplicationConfig applicationConfig) {
		this.applicationConfig = applicationConfig;
	}

	public Result acceptCmd(Cmd cmd) {
		logger.info("收到服务端指令===>"+cmd);
		Result result = new Result();
		try {
			String uri = "applicationConfig?engineType=dubbo&applicationId=" + cmd.getApplicationId();
			String json = HttpUtil.sendGet( url+uri,  "UTF-8");
			Result<Map<?,?>> grayConfig = JSON.parse(json, Result.class);
			if(grayConfig.isSuccess() && grayConfig.getCode().equals(ResultCode.COMMON_SUCCESS.getCode())){
				String applicationId = grayDataParse(cmd,result,grayConfig.getObj());
				Application application = GrayDataCache.getInstance().getApplication(applicationId);
				logger.info("服务端指令执行结果===>"+application);
				//System.out.println(application);
				result.setCode(ResultCode.COMMON_SUCCESS.getCode());
				result.setMsg("灰度已经成功开启");
			}else{
				result.setCode(ResultCode.COMMON_BUSINESS_EXCEPTION.getCode());
				result.setMsg("获取服务端数据失败");
				return result;
			}
		} catch (ParseException e) {
			result.setCode(ResultCode.COMMON_SYSTEM_EXCEPTION.getCode());
			result.setMsg("获取到服务端数据解析失败");
			result.setObj(e);
			return result;
		} catch (Exception e) {
			result.setCode(ResultCode.COMMON_SYSTEM_EXCEPTION.getCode());
			result.setMsg("获取服务端数据失败");
			result.setObj(e);
			return result;
		}
		return result;
	}
	
	private String grayDataParse(Cmd cmd,Result result,Map<?,?> data){
		Application application = new Application(data.get("name").toString(),
				data.get("applicationId").toString(), 
				data.get("param").toString(), 
				new Integer(data.get("status").toString()) );
		if("stopApplicationGray".equals(cmd.getCmd()) || application.getStatus().intValue() == 0){
			GrayDataCache.getInstance().removeApplication(application);
		}else{
			if(!"startApplicationGray".equals(cmd.getCmd())){
				application = GrayDataCache.getInstance().getApplication(application.getApplicationId());
			}else{
				String instances[] = data.get("grayIp").toString().split(";");
				for(String instance : instances){
					application.getGrayInstance().add(instance);
				}
			}
			if("startApplicationGray".equals(cmd.getCmd()) || "reloadApplicationStrategy".equals(cmd.getCmd())){
				application.setStrategy(buildStrategy(data));
				if(application.getStatus() == 1 && application.getStrategy() == null){
					return null;
				}
			}
			List<Map<?,?>> servicesMap = (List<Map<?,?>>)data.get("services");
			for(Map<?,?> service : servicesMap){
				Integer status = new Integer(service.get("status").toString());
				if(status == 0){
					continue;
				}
				ApplicationService applicationService = new ApplicationService (service.get("name").toString(), 
						service.get("version").toString() , 
						service.get("param").toString() , 
						new Integer(service.get("status").toString()) );
				if(!cmd.getCmd().startsWith("start")){
					applicationService = application.getServices().get(applicationService.getName());
				}
				if(cmd.getCmd().startsWith("start") || 
						(cmd.getCmd().startsWith("reload") && cmd.getCmd().endsWith("Strategy") )){
					applicationService.setStrategy(buildStrategy(service));
				}
				if(cmd.getCmd().startsWith("start") || 
						(cmd.getCmd().startsWith("reload") && cmd.getCmd().endsWith("Whitelist") )
						|| applicationService.getStrategy().getWay() == Way.whitelist){
					applicationService.setWhitelists( buildWhitelists(applicationService.getParam(),service));
				}
				if(cmd.getCmd().startsWith("start") ){
					application.getServices().put(applicationService.getName(), applicationService);
				}
			}
			if("startApplicationGray".equals(cmd.getCmd() ) || application.getStrategy().getWay() == Way.whitelist ||
					"reloadApplicationWhitelist".equals(cmd.getCmd())){
				application.setWhitelists( buildWhitelists(application.getParam(),data));
			}
			if("startApplicationGray".equals(cmd.getCmd())){
				GrayDataCache.getInstance().putApplication(application);
			}
		}
		return application.getApplicationId();
	}
	
	private Strategy buildStrategy(Map<?,?> data){
		Map<?,?> strategyMap = (Map<?,?>)data.get("strategy");
		Integer status = new Integer(strategyMap.get("status").toString());
		if(status == 0){
			return null;
		}
		Strategy strategy = new Strategy();
		if(strategyMap.get("name")!= null){
			strategy.setName(strategyMap.get("name").toString());
		}
		if(strategyMap.get("forwardReverse")!= null){
			strategy.setForwardReverse(new Integer(strategyMap.get("forwardReverse").toString()));
		}
		if(strategyMap.get("flowTatio")!= null){
			strategy.setFlowTatio(new Integer(strategyMap.get("flowTatio").toString()));
		}
		if(strategyMap.get("regular")!= null){
			strategy.setRegular(Pattern.compile(strategyMap.get("regular").toString()));
		}
		if(strategyMap.get("way")!= null){
			strategy.setWay(Way.valueOf(strategyMap.get("way").toString()));
		}
		return strategy;
	}
	
	private Set<String> buildWhitelists(String paramName,Map<?,?> data){
		List<Map<?,?>> whitelistsMap = (List<Map<?,?>>) data.get("whitelists");
		Set<String> whitelists = new HashSet<String>();
		if(whitelistsMap == null){
			return whitelists;
		}
		for(Map<?,?> whitelist : whitelistsMap){
			Integer status = new Integer(whitelist.get("status").toString());
			if(status == 0){
				continue;
			}
			String param = whitelist.get("param").toString();
			try {
				Map<String,String> map = (Map<String,String>)JSON.parse(param, Map.class);
				for(Map.Entry<String,String> entry : map.entrySet()){
					if(paramName.equals(entry.getKey()))
						whitelists.add(entry.getKey()+":"+entry.getValue());
				}
			} catch (ParseException e) {
			}
		}
		return whitelists;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent cre) {
		applicationConfig = cre.getApplicationContext().getBean(ApplicationConfig.class);
		try {
			//获取当前应用所依赖的应用id
			String uri = "getDependencies?applicationId=" + applicationConfig.getName();
			String json = HttpUtil.sendGet( url+uri,  "UTF-8");
			Result<List<String>> result = JSON.parse(json, Result.class);
			if(result.isSuccess() && result.getCode().equals(ResultCode.COMMON_SUCCESS.getCode())){
				for(String id : result.getObj()){
					acceptCmd(new Cmd("startApplicationGray",id)) ;
				}
			}else{
				logger.error(ResultCode.COMMON_BUSINESS_EXCEPTION.getCode(),"获取服务端数据失败");
			}
		} catch (InterruptedException e) {
		} catch (Exception e) {
			logger.error(e.getMessage(),"初始化灰度数据失败！");
		}
	}
}
