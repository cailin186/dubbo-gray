package com.xlz.gray.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.base.mapper.PageQuery;
import com.xlz.commons.base.service.impl.BaseServiceImpl;
import com.xlz.commons.config.Level;
import com.xlz.commons.utils.PageInfo;
import com.xlz.gray.mapper.WhiteListMapper;
import com.xlz.gray.model.WhiteList;
import com.xlz.gray.service.WhiteListService;

@Service
public class WhiteListServiceImpl extends BaseServiceImpl<WhiteList> implements WhiteListService {

	@Autowired
	private WhiteListMapper whiteListMapper;
	@Override
	protected BaseMapper<WhiteList> getDAO() {
		return whiteListMapper;
	}
	@Override
	public List<WhiteList> findWhitelistByLinkId(Long id,Level level) {
		return whiteListMapper.findWhitelistByLinkId(id,level);
	}
	@Override
	public PageInfo<WhiteList> findSelectedByPage(List<FilterRule> filterRules, PageQuery pageQuery) {
		PageInfo<WhiteList> pageInfo = new PageInfo<WhiteList>(pageQuery.getStart(), pageQuery.getPageSize(), "", "");
    	pageQuery.setTotalCount(whiteListMapper.getSelectedTotalCount(filterRules));
    	if(pageQuery.getTotalCount() <= 0){
    		pageInfo.setRows(new ArrayList<WhiteList>());
            pageInfo.setTotal(pageQuery.getTotalCount());
    	}else{
    	List<WhiteList> rows = whiteListMapper.findSelectedByPage(filterRules, pageQuery);
	    	pageInfo.setRows(rows);
	        pageInfo.setTotal(pageQuery.getTotalCount());
    	}
        
    	return pageInfo;
	}
	
}
