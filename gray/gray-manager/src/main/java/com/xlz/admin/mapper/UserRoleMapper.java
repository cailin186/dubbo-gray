package com.xlz.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xlz.admin.model.UserRole;
import com.xlz.commons.base.mapper.BaseMapper;

/**
 *
 * UserRole 表数据库控制层接口
 *
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<UserRole> selectByUserId(@Param("userId") Long userId);

    List<Long> selectRoleIdListByUserId(@Param("userId") Long userId);

}