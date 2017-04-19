package com.xlz.gray.mapper;

import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.gray.model.ApplicationWhitelist;

/**
 *
 * ApplicationWhitelist 表数据库控制层接口
 *
 */
public interface ApplicationWhitelistMapper extends BaseMapper<ApplicationWhitelist> {

	void delete(ApplicationWhitelist applicationWhitelist);
	
}