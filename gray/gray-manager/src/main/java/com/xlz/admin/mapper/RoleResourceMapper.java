package com.xlz.admin.mapper;

import org.apache.ibatis.annotations.Param;

import com.xlz.admin.model.RoleResource;
import com.xlz.commons.base.mapper.BaseMapper;

/**
 *
 * RoleResource 表数据库控制层接口
 *
 */
public interface RoleResourceMapper extends BaseMapper<RoleResource> {

	Long selectIdListByRoleId(@Param("id") Long id);

}