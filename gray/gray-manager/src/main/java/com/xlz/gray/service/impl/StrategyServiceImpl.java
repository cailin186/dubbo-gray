package com.xlz.gray.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.service.impl.BaseServiceImpl;
import com.xlz.commons.config.Level;
import com.xlz.commons.config.Type;
import com.xlz.gray.mapper.StrategyMapper;
import com.xlz.gray.model.Strategy;
import com.xlz.gray.service.StrategyService;

@Service
public class StrategyServiceImpl extends BaseServiceImpl<Strategy> implements StrategyService {

	@Autowired
	private StrategyMapper strategyMapper;
	@Override
	protected BaseMapper<Strategy> getDAO() {
		return strategyMapper;
	}
	@Override
	public List<Strategy> findStrategyByLink(Long id, Type engineType,Level level) {
		return strategyMapper.findStrategyByLink(id,engineType,level);
	}
	
}
