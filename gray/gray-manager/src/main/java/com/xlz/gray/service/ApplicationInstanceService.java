package com.xlz.gray.service;

import javax.servlet.http.HttpServletRequest;

import com.xlz.commons.base.service.BaseService;
import com.xlz.gray.model.Application;
import com.xlz.gray.model.ApplicationInstance;

public interface ApplicationInstanceService extends BaseService<ApplicationInstance> {

	void updateInstance(HttpServletRequest request,  Application application);
	
}

