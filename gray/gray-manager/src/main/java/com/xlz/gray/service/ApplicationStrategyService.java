package com.xlz.gray.service;

import javax.servlet.http.HttpServletRequest;

import com.xlz.commons.base.model.BaseDomain;
import com.xlz.commons.base.service.BaseService;
import com.xlz.commons.config.Level;
import com.xlz.commons.result.Result;
import com.xlz.gray.model.Application;
import com.xlz.gray.model.ApplicationStrategy;
import com.xlz.gray.model.Strategy;

public interface ApplicationStrategyService extends BaseService<ApplicationStrategy> {
	
	/**
     * 删除重新插入
     * @param nginxs
     */
    void updateApplicationStrategy(HttpServletRequest request,BaseDomain domain,
    		Level level);
    
    /**
     * 按照灰度方式进行策略校验
     * @param strategy
     * @param param
     * @return
     */
    Result<Application> publishCheckByWay(Strategy strategy,String param);
    
    /**
     * 校验策略配置
     * @param request
     * @return
     */
    Result<Application> checkStrategy(HttpServletRequest request,String param);
}

