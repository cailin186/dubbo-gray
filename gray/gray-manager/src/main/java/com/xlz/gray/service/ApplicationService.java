package com.xlz.gray.service;

import com.xlz.commons.base.service.BaseService;
import com.xlz.commons.config.Type;
import com.xlz.commons.result.Result;
import com.xlz.gray.model.Application;

public interface ApplicationService extends BaseService<Application> {
	
	Result<Application> publishConfig(String applicationId,Type engineType);
}

