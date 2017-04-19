package com.xlz.admin.mapper;

import org.apache.ibatis.annotations.Param;

import com.xlz.admin.model.User;
import com.xlz.commons.base.mapper.BaseMapper;

/**
 *
 * User 表数据库控制层接口
 *
 */
public interface UserMapper extends BaseMapper<User> {

	User selectUserVoById(@Param("id") Long id);

}