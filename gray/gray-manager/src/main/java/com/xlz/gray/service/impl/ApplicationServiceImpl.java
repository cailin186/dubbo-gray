package com.xlz.gray.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.base.service.impl.BaseServiceImpl;
import com.xlz.commons.config.Level;
import com.xlz.commons.config.Type;
import com.xlz.commons.config.Way;
import com.xlz.commons.result.Result;
import com.xlz.commons.result.ResultCode;
import com.xlz.event.RegistryContainer;
import com.xlz.gray.mapper.ApplicationMapper;
import com.xlz.gray.model.Application;
import com.xlz.gray.model.ApplicationInstance;
import com.xlz.gray.model.Strategy;
import com.xlz.gray.model.WhiteList;
import com.xlz.gray.service.ApplicationInstanceService;
import com.xlz.gray.service.ApplicationService;
import com.xlz.gray.service.ApplicationServiceService;
import com.xlz.gray.service.StrategyService;
import com.xlz.gray.service.WhiteListService;

@Service
public class ApplicationServiceImpl extends BaseServiceImpl<Application> implements ApplicationService {

	@Autowired
	private ApplicationMapper applicationMapper;
	@Autowired
	private StrategyService strategyService;
	@Autowired
	private WhiteListService whiteListService;
	@Autowired
	private ApplicationServiceService applicationServiceService;
	@Autowired
    private ApplicationInstanceService applicationInstanceService;
	
	@Override
	protected BaseMapper<Application> getDAO() {
		return applicationMapper;
	}
	@Override
	public Result<Application> publishConfig(String applicationId, Type engineType) {
		Result<Application> result = new Result<Application>();

		if (StringUtils.isBlank(applicationId)) {
			result.setCode(ResultCode.COMMON_BUSINESS_EXCEPTION.getCode());
			result.setMsg("参数中[applicationId]不能为空");
			return result;
		}
		if (!(engineType instanceof Type)) {
			result.setCode(ResultCode.COMMON_BUSINESS_EXCEPTION.getCode());
			result.setMsg("参数中[engineType]不能为空");
			return result;
		}

		// 获取系统信息，并且判断系统是否启用灰度
		List<FilterRule> filterRuleList = new ArrayList<>();
		filterRuleList.add(new FilterRule("application_id", "=", applicationId));
		List<Application> applicationList = this.findAll(filterRuleList);
		Application application;
		if (!applicationList.isEmpty()) {
			application = applicationList.get(0);
		} else {
			result.setCode(ResultCode.COMMON_BUSINESS_EXCEPTION.getCode());
			result.setMsg("根据标识在灰度管控系统中未查找到此系统");
			return result;
		}
		// 获取系统级的灰度策略和其他信息
		Map<String, Object> returnMap = getStrategyAndOther(engineType, application.getId(), Level.application);
		if (returnMap == null) {
			result.setCode(ResultCode.COMMON_BUSINESS_EXCEPTION.getCode());
			result.setMsg("此系统所绑定的策略中不存在类型为[" + engineType + "]的策略");
			return result;
		}

		//设置实例
		filterRuleList.clear();
    	filterRuleList.add(new FilterRule("application_id","=",application.getId()));
    	filterRuleList.add(new FilterRule("type","=",engineType));
    	List<ApplicationInstance>  applicationInstanceList = applicationInstanceService.findAll(filterRuleList);
    	ApplicationInstance applicationInstance = applicationInstanceList.get(0);
    	application.setDeployIp(applicationInstance.getDeployInstance());
    	application.setGrayIp(applicationInstance.getGrayInstance());
    	
		application.setWhitelists(returnMap.get("whitelists"));
		application.setStrategy(returnMap.get("strategy"));
		
		filterRuleList.clear();
		filterRuleList.add(new FilterRule("application_id", "=", application.getId()));
		filterRuleList.add(new FilterRule("type", "=", engineType));
		//获取系统下的服务包含按照引擎类型获取
		List<com.xlz.gray.model.ApplicationService> applicationServiceList = applicationServiceService.findAll(filterRuleList);
		
		List<com.xlz.gray.model.ApplicationService> applicationServices = new ArrayList<>();
		for(com.xlz.gray.model.ApplicationService applicationService : applicationServiceList) {
			// 获取服务级的灰度策略和其他信息
			returnMap = getStrategyAndOther(engineType, applicationService.getId(), Level.service);
			if (returnMap != null) {
				applicationServices.add(applicationService);
				applicationService.setStrategy(returnMap.get("strategy"));
				applicationService.setWhiteLists(returnMap.get("whitelists"));
			}
		}
		
		application.setServices(applicationServices);
		
		result = new Result<Application>(ResultCode.COMMON_SUCCESS);
		result.setObj(application);
		return result;
	}
	
	/**
	 * 根据参数获取策略信息
	 * 
	 * @param engineType
	 * @param linkId
	 * @param level
	 * @return
	 */
	private Map<String, Object> getStrategyAndOther(Type engineType, Long linkId, Level level) {
		// 找到系统并且系统已经启用灰度，继续获取系统所绑定的策略,系统在启用灰度之前必须保障存在可用策略
		List<Strategy> strategyList = strategyService.findStrategyByLink(linkId,engineType, level);
		if (strategyList.size() != 1) {
			return null;
		}
		Map<String, Object> resultMap = new HashMap<>();
		Strategy strategy = strategyList.get(0);
		// 根据不同策略获取不同信息，比如白名单策略需要获取
		if (strategy.getWay() == Way.whitelist) {
			// 获取白名单
			List<WhiteList> whitelists = whiteListService.findWhitelistByLinkId(strategy.getId(),level);
			resultMap.put("whitelists", whitelists);
		}
		resultMap.put("strategy", strategy);
		
		return resultMap;
	}
}
