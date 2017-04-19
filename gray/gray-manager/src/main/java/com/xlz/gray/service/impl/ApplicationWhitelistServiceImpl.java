package com.xlz.gray.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.service.impl.BaseServiceImpl;
import com.xlz.commons.config.Level;
import com.xlz.gray.mapper.ApplicationWhitelistMapper;
import com.xlz.gray.model.ApplicationWhitelist;
import com.xlz.gray.service.ApplicationWhitelistService;

@Service
public class ApplicationWhitelistServiceImpl extends BaseServiceImpl<ApplicationWhitelist> implements ApplicationWhitelistService {

	@Autowired
	private ApplicationWhitelistMapper applicationWhitelistMapper;
	@Override
	protected BaseMapper<ApplicationWhitelist> getDAO() {
		return applicationWhitelistMapper;
	}
	
	@Override
	public void delete(String ids, Integer linkId, Level level) {
		 String[] strs = ids.split(",");
	        for(String str : strs){
	        	ApplicationWhitelist applicationWhitelist = new ApplicationWhitelist();
	        	applicationWhitelist.setLinkId(linkId);
	        	applicationWhitelist.setLevel(level);
	        	applicationWhitelist.setId(new Long(str));
	        	applicationWhitelistMapper.delete(applicationWhitelist);
	        }
	}
	
}
