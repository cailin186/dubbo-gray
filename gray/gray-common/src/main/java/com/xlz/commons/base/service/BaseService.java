package com.xlz.commons.base.service;

import java.util.List;

import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.base.mapper.PageQuery;
import com.xlz.commons.base.model.BaseDomain;
import com.xlz.commons.utils.PageInfo;

public interface BaseService<T extends BaseDomain> {
    /**
     * 根据id找到对应的DO对象
     * 
     * @param id
     * @return
     */
    T findById(Long id);

    /**
     * 更新数据
     * 
     * @param t
     */
    void update(T t);

    /**
     * 创建新纪录,返回对应的id
     * 
     * @param entity
     * @return
     */
    Long save(T entity);

    /**
     * 根据id删除记录
     * 
     * @param ids 多个id以半角英文逗号分隔 如;1,2,3
     */
    void delete(String ids);

    /**
     * 查找指定的记录列表
     * 
     * @param filterRuleList 需要过滤的查询条件
     * @return
     */
    List<T> findAll(List<FilterRule> filterRuleList);
    
    /**
     * 查找指定的记录列表
     * 
     * @param filterRuleList 需要过滤的查询条件
     * @param pageQuery
     * @return
     */
    PageInfo<T> findByPage(List<FilterRule> filterRuleList, PageQuery pageQuery);

    /**
     * 查找指定的记录列表的总数
     * 
     * @param filterRuleList
     * @return
     */
    Integer getTotalCount(List<FilterRule> filterRuleList);

}
