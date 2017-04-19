package com.xlz.gray.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.service.impl.BaseServiceImpl;
import com.xlz.gray.mapper.ApplicationServiceMapper;
import com.xlz.gray.model.ApplicationService;
import com.xlz.gray.service.ApplicationServiceService;

@Service
public class ApplicationServiceServiceImpl extends BaseServiceImpl<ApplicationService> implements ApplicationServiceService {

	@Autowired
	private ApplicationServiceMapper applicationServiceMapper;
	@Override
	protected BaseMapper<ApplicationService> getDAO() {
		return applicationServiceMapper;
	}
	
	
}
