package com.xlz.gray.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.gray.service.Cmd;
import com.alibaba.dubbo.gray.service.GrayCmdService;
import com.xlz.commons.base.BaseController;
import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.config.Level;
import com.xlz.commons.config.Type;
import com.xlz.commons.result.Result;
import com.xlz.commons.result.ResultCode;
import com.xlz.commons.utils.HttpUtil;
import com.xlz.commons.utils.StringUtils;
import com.xlz.event.RegistryContainer;
import com.xlz.gray.model.Application;
import com.xlz.gray.model.ApplicationInstance;
import com.xlz.gray.model.ApplicationNginx;
import com.xlz.gray.model.ApplicationService;
import com.xlz.gray.model.ApplicationStrategy;
import com.xlz.gray.model.Strategy;
import com.xlz.gray.service.ApplicationInstanceService;
import com.xlz.gray.service.ApplicationNginxService;
import com.xlz.gray.service.ApplicationServiceService;
import com.xlz.gray.service.ApplicationStrategyService;

public class GrayBaseController extends BaseController{

    @Autowired
	private ApplicationNginxService applicationNginxService;
    @Autowired
    private ApplicationStrategyService applicationStrategyService;
	@Autowired
    private ApplicationInstanceService applicationInstanceService;
    @Autowired
    private RegistryContainer registryContainer;
    @Autowired
    protected ApplicationServiceService applicationServiceService;
    @Autowired
    protected com.xlz.gray.service.ApplicationService applicationService;
    
    protected String publish(Long likeId,Long applicationId,
    		String applicationCode,String cmd,Level level){
    	StringBuilder sb = new StringBuilder();
    	//获取策略
    	List<FilterRule> filterRuleList = new ArrayList<>();
    	filterRuleList.add(new FilterRule("link_id","=",likeId));
    	filterRuleList.add(new FilterRule("level","=",level));
    	List<ApplicationStrategy>  applicationStrategyList = applicationStrategyService.findAll(filterRuleList);
		
    	//获取nginx
    	filterRuleList.clear();
    	filterRuleList.add(new FilterRule("application_id","=",applicationId));
    	List<ApplicationNginx>  nginxList = applicationNginxService.findAll(filterRuleList);
    	for(ApplicationStrategy applicationStrategy : applicationStrategyList){
    		//nginx+lua引擎
			if(applicationStrategy.getType() == Type.nginx){
				filterRuleList.clear();
	        	filterRuleList.add(new FilterRule("application_id","=",applicationId));
	        	filterRuleList.add(new FilterRule("type","=",Type.dubbo));
	        	List<ApplicationInstance>  applicationInstanceList = applicationInstanceService.findAll(filterRuleList);
				if(applicationInstanceList.isEmpty()){
					sb.append("dubbo的应用实例未配置<br/>");
				}
				sb.append("nginx类型策略的灰度命令执行结果如下：<br/>");
				//循环nginx列表，启动nginx上的引擎拉取服务配置，进行灰度启动
				for(ApplicationNginx nginx : nginxList){
					try {
						String url = "http://" + nginx.getIp() + ":" + nginx.getPort() + "/gray";
						String result = HttpUtil.sendPostUrl(url, "cmd=" + cmd + "&applicationId=" + applicationCode,
								"UTF-8");
						ApplicationNginx nginxEntity = new ApplicationNginx();
						nginxEntity.setId(nginx.getId());
						nginxEntity.setUpdateUser(getStaffName());
						if ("success".equals(result)) {
							nginxEntity.setStatus(1);
						} else {
							nginxEntity.setStatus(0);
							applicationNginxService.update(nginxEntity);
						}
						applicationNginxService.update(nginxEntity);
						sb.append(nginx.getIp()).append(":").append(nginx.getPort()).append("操作结果：").append(result)
								.append("<br/>");
					} catch (Exception e) {
						sb.append(nginx.getIp()).append(":").append(nginx.getPort()).append("操作结果：执行失败").append(e.getMessage())
						.append("<br/>");
					}
				}
			}else 
			//dubbo引擎
			if(applicationStrategy.getType() == Type.dubbo){
				filterRuleList.clear();
	        	filterRuleList.add(new FilterRule("application_id","=",applicationId));
	        	filterRuleList.add(new FilterRule("type","=",Type.dubbo));
	        	List<ApplicationInstance>  applicationInstanceList = applicationInstanceService.findAll(filterRuleList);
				if(applicationInstanceList.isEmpty()){
					sb.append("dubbo的应用实例未配置<br/>");
				}
	        	//获取到当前服务对应到的consumer的应用
	        	//将命令推送到被灰度系统的上层应用
				Set<String> applications = registryContainer.getDependencies(applicationCode,true) ;
				if(!applications.isEmpty())
					sb.append("dubbo类型策略的灰度命令执行结果如下：<br/>");
				for(String application : applications){
					System.out.println("==========>"+applicationId);
					GrayCmdService grayCmdService = registryContainer.getGrayCmdService( application);
					Result r = grayCmdService.acceptCmd(new Cmd(cmd,applicationCode));
					sb.append("consumer应用 ").append(application).append(" 操作结果：").append(r.getMsg())
					.append("<br/>");
				}
			}
		}
    	return sb.toString();
    }
    
    /**
     * 校验灰度数据完整性
     * @param application
     * @return
     */
    protected Result<Application> publishCompleteCheck(String applicationId,Type [] types) {
		Result<Application> result = new Result<Application> ();
		//TODO 获取已经开通的策略类型，对其进行参数校验
		for (Type type : types) {  
			Result<Application> data = applicationService.publishConfig(applicationId, type);
			if(!result.getCode().equals(ResultCode.COMMON_SUCCESS.getCode())){
				result.setMsg(result.getMsg()+"<br/>");
				result.setCode(data.getCode());
				break ;
			}else{
				Application application = data.getObj();
				//按照不同引擎和策略分别校验
				Strategy strategy = (Strategy)application.getStrategy();
				//控制台未勾选，不检查
				if(strategy.getStatus().intValue() != 1){
					continue;
				}
				//应用的配置校校验
				if(StringUtils.isBlank(application.getGrayIp())){
					result.setCode(ResultCode.COMMON_BUSINESS_EXCEPTION.getCode());
					result.setMsg("指定的灰度部署实例不能为空，请检查后重试");
					return result;
				}else{
					//TODO 正则表达式进行格式校验
					
				}
				if(StringUtils.isBlank(application.getDeployIp())){
					result.setCode(ResultCode.COMMON_BUSINESS_EXCEPTION.getCode());
					result.setMsg("除灰度机之外的其他部署实例为空，请检查后重试");
					return result;
				}else{
					//TODO 正则表达式进行格式校验
					
				}
				//按照灰度方式进行校验
				result = applicationStrategyService.publishCheckByWay(strategy,application.getParam());
				if(!result.getCode().equals(ResultCode.COMMON_SUCCESS.getCode())){
					return result;
				}
				
				//服务的配置校验
				for(ApplicationService service : application.getServices()){
					//按照灰度方式进行校验
					strategy = (Strategy)service.getStrategy();
					result = applicationStrategyService.publishCheckByWay(strategy,service.getParam());
					if(!result.getCode().equals(ResultCode.COMMON_SUCCESS.getCode())){
						return result;
					}
				}
			}
		}
		return result;
	}
	
}
