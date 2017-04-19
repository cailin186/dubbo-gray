package com.xlz.gray.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xlz.commons.config.Level;
import com.xlz.commons.config.Type;
import com.xlz.commons.result.Result;
import com.xlz.commons.result.ResultCode;
import com.xlz.commons.utils.StringUtils;
import com.xlz.gray.model.Application;
import com.xlz.gray.model.ApplicationService;

/**
 * @description：被灰度系统服务管理
 * @author：zhagnll
 * 0:灰度已关闭1:系统灰度中2:服务灰度中3:nginx部分灰度;
 */
@Controller
@RequestMapping("/serviceGray")
public class GrayServiceController extends GrayBaseController {
    
    /**
     * 灰度发布管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "gray/gray_publish/service_gray_publish";
    }
    
    /**
     * 系统服务灰度管理
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
	    		ApplicationService applicationServiceEntity = applicationServiceService.findById(new Long(id));
	    		//获取系统服务
	    		Application application = applicationService.findById(applicationServiceEntity.getApplicationId());
	    		message.append("===============系统【"+application.getApplicationId()+"】服务【"+applicationServiceEntity.getName()+"】操作指令===============").append("<br/>");
	    		if(application.getStatus().intValue() ==0 && cmd.startsWith("reload")){
	    			message.append("系统【"+application.getApplicationId()+"】未启动灰度，不能执行相应指令").append("<br/>");
	    			continue ;
	    		}
	    		if(application.getStatus().intValue() ==0 && cmd.startsWith("reload")){
	    			message.append("服务【"+applicationServiceEntity.getName()+"】未启动灰度，不能重新加载白名单").append("<br/>");
	    			continue ;
	    		}
	    		if("startServiceGray".equals(cmd) && applicationServiceEntity.getStatus().intValue() ==1 ){
		    			message.append("系统服务【"+applicationServiceEntity.getName()+"】处于启动状态，不能在启动").append("<br/>");
		    			continue ;
	    		}
	    		if("stopServiceGray".equals(cmd) && applicationServiceEntity.getStatus().intValue() ==0 ){
	    			message.append("系统服务【"+applicationServiceEntity.getName()+"】处于停止状态，不能在停止").append("<br/>");
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
	        	applicationEntity.setId(application.getId());
	        	ApplicationService serviceEntity = new ApplicationService();
        		serviceEntity.setId(new Long(id));
	    		if("stopServiceGray".equals(cmd)){
	    			//更新启动状态
		        	status = 0;
		        	serviceEntity.setStatus(status);
		    		applicationServiceService.update(serviceEntity);
	    		}else if("startServiceGray".equals(cmd)){
	        		serviceEntity.setStatus(status);
		    		applicationServiceService.update(serviceEntity);
		    		
	    			applicationEntity.setStatus(2);
	    			applicationService.update(applicationEntity);
	    		}
	    		
	    		String publishResponse = publish(applicationServiceEntity.getId(), application.getId(), application.getApplicationId()
	        			, cmd, Level.service);
	    		message.append(publishResponse);
	        }
    	}
    	return renderSuccess(message.toString());
    }
    
}
