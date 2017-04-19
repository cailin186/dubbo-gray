package com.xlz.gray.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.service.impl.BaseServiceImpl;
import com.xlz.gray.mapper.ApplicationNginxMapper;
import com.xlz.gray.model.ApplicationNginx;
import com.xlz.gray.service.ApplicationNginxService;

@Service
public class ApplicationNginxServiceImpl extends BaseServiceImpl<ApplicationNginx> implements ApplicationNginxService {

	@Autowired
	private ApplicationNginxMapper applicationNginxMapper;
	@Override
	protected BaseMapper<ApplicationNginx> getDAO() {
		return applicationNginxMapper;
	}
	
}
