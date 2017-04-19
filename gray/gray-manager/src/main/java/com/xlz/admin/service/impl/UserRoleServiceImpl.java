package com.xlz.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlz.admin.mapper.UserRoleMapper;
import com.xlz.admin.model.UserRole;
import com.xlz.admin.service.UserRoleService;
import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.service.impl.BaseServiceImpl;

/**
 *
 * UserRole 表数据服务层接口实现类
 *
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl< UserRole> implements UserRoleService {

	@Autowired
    private UserRoleMapper userRoleMapper;
	
	@Override
	protected BaseMapper<UserRole> getDAO() {
		return userRoleMapper;
	}

}