package com.xlz.gray.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.config.Level;
import com.xlz.commons.config.Type;
import com.xlz.commons.result.Result;
import com.xlz.commons.result.ResultCode;
import com.xlz.commons.utils.StringUtils;
import com.xlz.gray.model.Application;
import com.xlz.gray.model.ApplicationService;

/**
 * @description：被灰度系统管理
 * @author：zhagnll
 * 0:灰度已关闭1:系统灰度中2:服务灰度中3:nginx部分灰度;
 */
@Controller
@RequestMapping("/applicationGray")
public class GrayApplicationController extends GrayBaseController {
    
    /**
     * 灰度发布管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "gray/publish/application_gray_publish";
    }
    
    /**
     * 系统灰度管理
     *
     * @return
     */
    @PostMapping("/managerCmd")
    @ResponseBody
    public Object managerCmd(String ids[],String cmd) {
    	StringBuilder message = new StringBuilder();
    	for(String id : ids){
	    	if (StringUtils.isNotBlank(id)) {
	    		//获取系统服务
	    		Application application = applicationService.findById(new Long(id));
	    		message.append("===============系统服务【"+application.getApplicationId()+"】操作指令===============").append("<br/>");
	    		if(application.getStatus().intValue() ==0 && cmd.startsWith("reload") ){
	    			message.append("系统服务【"+application.getApplicationId()+"】未启动灰度，不能执行相应指令").append("<br/>");
	    			continue ;
	    		}
	    		if("startApplicationGray".equals(cmd) && application.getStatus().intValue() ==1 ){
		    			message.append("系统服务【"+application.getApplicationId()+"】处于启动状态，不能在启动").append("<br/>");
		    			continue ;
	    		}
	    		if("stopApplicationGray".equals(cmd) && application.getStatus().intValue() ==0 ){
	    			message.append("系统服务【"+application.getApplicationId()+"】处于停止状态，不能在停止").append("<br/>");
	    			continue ;
	    		}else{
	    			//数据完整性校验
	    			Result<Application> result = publishCompleteCheck(application.getApplicationId(),Type.values());
	    			if(!result.getCode().equals(ResultCode.COMMON_SUCCESS.getCode())){
	    				message.append(result.getMsg()).append("<br/>");
	    				continue ;
	    			}
	    		}
	    		
	    		//更新启动状态
	    		Integer status = 1;
	        	Application applicationEntity = new Application();
	        	applicationEntity.setId(new Long(id));
	    		if("stopApplicationGray".equals(cmd)){
	    			//更新启动状态
		        	status = 0;
	    		}
	    		applicationEntity.setStatus(status);
	        	applicationService.update(applicationEntity);
	        	//更新每个服务的状态
	        	List<FilterRule> filterRuleList = new ArrayList<>();
	        	filterRuleList.add(new FilterRule("application_id","=",id));
	        	List<ApplicationService> serviceList = applicationServiceService.findAll(filterRuleList);
	        	for(ApplicationService service : serviceList){
	        		ApplicationService serviceEntity = new ApplicationService();
	        		serviceEntity.setId(service.getId());
	        		serviceEntity.setStatus(status);
					applicationServiceService.update(serviceEntity);
	        	}
	        	String publishResponse = publish(application.getId(), application.getId(), 
	        			application.getApplicationId(), cmd, Level.application);
	        	message.append(publishResponse);
	        }
    	}
    	return renderSuccess(message.toString());
    }
}
