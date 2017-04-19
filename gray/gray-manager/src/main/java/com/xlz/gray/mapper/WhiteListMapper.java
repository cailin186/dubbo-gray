package com.xlz.gray.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.base.mapper.PageQuery;
import com.xlz.commons.config.Level;
import com.xlz.gray.model.WhiteList;

/**
 *
 * WhiteList 表数据库控制层接口
 *
 */
public interface WhiteListMapper extends BaseMapper<WhiteList> {
	
	List<WhiteList> findWhitelistByLinkId(@Param("id")Long id,@Param("level")Level level);
	
	List<WhiteList> findSelectedByPage(@Param("filterRules") List<FilterRule> filterRules,
			@Param("pageQuery") PageQuery pageQuery);

	int getSelectedTotalCount(@Param("filterRules") List<FilterRule> filterRules);
}