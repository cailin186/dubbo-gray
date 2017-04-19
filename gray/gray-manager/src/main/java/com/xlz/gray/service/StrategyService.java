package com.xlz.gray.service;

import java.util.List;

import com.xlz.commons.base.service.BaseService;
import com.xlz.commons.config.Level;
import com.xlz.commons.config.Type;
import com.xlz.gray.model.Strategy;

public interface StrategyService extends BaseService<Strategy> {
	
	List<Strategy> findStrategyByLink(Long id, Type engineType,Level level);
	
}

