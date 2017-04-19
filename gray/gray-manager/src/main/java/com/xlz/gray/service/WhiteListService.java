package com.xlz.gray.service;

import java.util.List;

import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.base.mapper.PageQuery;
import com.xlz.commons.base.service.BaseService;
import com.xlz.commons.config.Level;
import com.xlz.commons.utils.PageInfo;
import com.xlz.gray.model.WhiteList;

public interface WhiteListService extends BaseService<WhiteList> {
	
	List<WhiteList> findWhitelistByLinkId(Long id,Level level);
	
	PageInfo<WhiteList> findSelectedByPage(List<FilterRule> filterRules,PageQuery pageQuery);
}

