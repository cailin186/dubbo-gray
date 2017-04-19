package com.xlz.gray.restful;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xlz.commons.config.Type;
import com.xlz.commons.result.Result;
import com.xlz.commons.result.ResultCode;
import com.xlz.event.RegistryContainer;
import com.xlz.gray.service.ApplicationService;

@RequestMapping("/pullApplicationConfig")
@Controller
public class PullApplicationConfig {

	@Autowired
	private ApplicationService applicationService;
	@Autowired
    private RegistryContainer registryContainer;
	/**
	 * 
	 * @return
	 */
	@RequestMapping("/applicationConfig")
	@ResponseBody
	public Object applicationConfig(String applicationId,Type engineType) {
		return applicationService.publishConfig(applicationId, engineType);
	}
	/**
	 * 获取dubbo应用的依赖应用
	 * @return
	 */
	@RequestMapping("/getDependencies")
	@ResponseBody
	public Object getDependencies(String applicationId) {
		Result<Set<String>> result = new Result<Set<String>>();
		
		if (StringUtils.isBlank(applicationId)) {
			result.setCode(ResultCode.COMMON_BUSINESS_EXCEPTION.getCode());
			result.setMsg("参数中[applicationId]不能为空");
			return result;
		}
		Set<String> dependencies = registryContainer.getDependencies(applicationId, false);
		
		result = new Result<Set<String>>(ResultCode.COMMON_SUCCESS);
		result.setObj(dependencies);
		return result;
	}

}