package com.xlz.gray.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.service.impl.BaseServiceImpl;
import com.xlz.gray.mapper.WhiteListGroupMapper;
import com.xlz.gray.model.WhiteListGroup;
import com.xlz.gray.service.WhiteListGroupService;

@Service
public class WhiteListGroupServiceImpl extends BaseServiceImpl<WhiteListGroup> implements WhiteListGroupService {

	@Autowired
	private WhiteListGroupMapper whiteListGroupMapper;
	@Override
	protected BaseMapper<WhiteListGroup> getDAO() {
		return whiteListGroupMapper;
	}
	
}
