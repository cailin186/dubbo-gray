package com.xlz.gray.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.config.Level;
import com.xlz.commons.config.Type;
import com.xlz.gray.model.Strategy;

/**
 *
 * Strategy 表数据库控制层接口
 *
 */
public interface StrategyMapper extends BaseMapper<Strategy> {

	List<Strategy> findStrategyByLink(@Param("id")Long id, 
			@Param("engineType")Type engineType,@Param("level")Level level);

}