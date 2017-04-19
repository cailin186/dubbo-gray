package com.xlz.gray.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.base.service.impl.BaseServiceImpl;
import com.xlz.commons.config.Type;
import com.xlz.gray.mapper.ApplicationInstanceMapper;
import com.xlz.gray.model.Application;
import com.xlz.gray.model.ApplicationInstance;
import com.xlz.gray.service.ApplicationInstanceService;

@Service
public class ApplicationInstanceServiceImpl extends BaseServiceImpl<ApplicationInstance> implements ApplicationInstanceService {

	@Autowired
	private ApplicationInstanceMapper applicationInstanceMapper;
	@Override
	protected BaseMapper<ApplicationInstance> getDAO() {
		return applicationInstanceMapper;
	}
	@Override
	public void updateInstance(HttpServletRequest request, Application application) {
		List<FilterRule> filterRuleList = new ArrayList<>();
    	filterRuleList.add(new FilterRule("application_id","=",application.getId()));
    	List<ApplicationInstance>  list = applicationInstanceMapper.findAll(filterRuleList);
    	
    	Map<Type,ApplicationInstance> set = new HashMap<>();
    	for(ApplicationInstance model :  list){
    		set.put(model.getType(),model);
    	}
    	for (Type type : Type.values()) {  
    		String deployInstance = request.getParameter("deploy_" + type.name());
    		String grayInstance = request.getParameter("gray_" + type.name());
    		ApplicationInstance entity = new ApplicationInstance();
    	    if(!set.containsKey(type)){
    			entity.setType(type);
    			entity.setStatus(1);
    			entity.setDeployInstance(deployInstance);
    			entity.setGrayInstance(grayInstance);
    			entity.setApplicationId(application.getId());
    			entity.setCreateUser(application.getCreateUser());
    			applicationInstanceMapper.save(entity );
			}else {
				entity.setId(set.get(type).getId());
				entity.setStatus(1);
				entity.setDeployInstance(deployInstance);
    			entity.setGrayInstance(grayInstance);
    			entity.setUpdateUser(application.getUpdateUser());
    			applicationInstanceMapper.update(entity );
			}
    	} 
	}
	
}
