package com.xlz.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlz.admin.mapper.RoleResourceMapper;
import com.xlz.admin.model.RoleResource;
import com.xlz.admin.service.RoleResourceService;
import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.service.impl.BaseServiceImpl;

/**
 *
 * RoleResource 表数据服务层接口实现类
 *
 */
@Service
public class RoleResourceServiceImpl extends BaseServiceImpl<RoleResource> implements RoleResourceService {

	@Autowired
    private RoleResourceMapper roleResourceMapper;
	
	@Override
	protected BaseMapper<RoleResource> getDAO() {
		return roleResourceMapper;
	}


}