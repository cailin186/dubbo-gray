package com.xlz.commons.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.base.mapper.PageQuery;
import com.xlz.commons.base.model.BaseDomain;
import com.xlz.commons.base.service.BaseService;
import com.xlz.commons.utils.PageInfo;

public abstract class BaseServiceImpl<T extends BaseDomain> implements BaseService<T>{
	/**
	 * 日志
	 */
	protected final Logger LOG = LoggerFactory.getLogger(getClass());
	
	protected abstract BaseMapper<T> getDAO();
	protected void beforeSave(T entity){}
	protected void beforeUpdate(T entity){};
	protected void beforeDelete(String ids){};
	
	protected void afterSave(T entity){}
	protected void afterUpdate(T entity){};
	protected void afterDelete(String ids){};

	public T findById(Long id) {
		return getDAO().findById(id);
	}
	
	public void update(T t) {
        beforeUpdate(t);
        getDAO().update(t);
        afterUpdate(t);
	}

    @Override
    public Long save(T entity) {
        beforeSave(entity);
        getDAO().save(entity);
        afterSave(entity);
        return entity.getId();
    }

    @Override
    public void delete(String ids) {
        beforeDelete(ids);
        String[] strs = ids.split(",");
        for(String str : strs){
            getDAO().delete(new Long(str));
        }
        afterDelete(ids);
    }

    @Override
    public List<T> findAll(List<FilterRule> filterRuleList) {
        return getDAO().findAll(filterRuleList);
    }
    
    @Override
    public PageInfo<T> findByPage(List<FilterRule> filterRuleList, PageQuery pageQuery) {
    	PageInfo<T> pageInfo = new PageInfo<T>(pageQuery.getStart(), pageQuery.getPageSize(), "", "");
    	pageQuery.setTotalCount(getDAO().getTotalCount(filterRuleList));
    	if(pageQuery.getTotalCount() <= 0){
    		pageInfo.setRows(new ArrayList<T>());
            pageInfo.setTotal(pageQuery.getTotalCount());
    	}else{
    	List<T> rows = getDAO().findByPage(filterRuleList, pageQuery);
	    	pageInfo.setRows(rows);
	        pageInfo.setTotal(pageQuery.getTotalCount());
    	}
        
    	return pageInfo;
    }

    @Override
    public Integer getTotalCount(List<FilterRule> filterRuleList) {
        return getDAO().getTotalCount(filterRuleList);
    }
}
