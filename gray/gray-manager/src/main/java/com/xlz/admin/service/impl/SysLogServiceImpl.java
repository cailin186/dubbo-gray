package com.xlz.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlz.admin.mapper.SysLogMapper;
import com.xlz.admin.model.SysLog;
import com.xlz.admin.service.SysLogService;
import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.service.impl.BaseServiceImpl;

/**
 *
 * SysLog 表数据服务层接口实现类
 *
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl< SysLog> implements SysLogService {
    
	@Autowired
    private SysLogMapper sysLogMapper;

	@Override
	protected BaseMapper<SysLog> getDAO() {
		return sysLogMapper;
	}
    
}