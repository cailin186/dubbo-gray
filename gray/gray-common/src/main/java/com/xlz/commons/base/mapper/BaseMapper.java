package com.xlz.commons.base.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xlz.commons.base.model.BaseDomain;

/**
 * @author zhangll
 *
 */
public interface BaseMapper<T extends BaseDomain> {

    Integer save(T t);

    int update(T t);

    void delete(Long id);

    T findById(Long id);
    
    List<T> findAll(@Param("filterRules")List<FilterRule> filterRules);
    
    List<T> findByPage(@Param("filterRules") List<FilterRule> filterRules, @Param("pageQuery") PageQuery pageQuery);

    Integer getTotalCount(@Param("filterRules") List<FilterRule> filterRules);
}
