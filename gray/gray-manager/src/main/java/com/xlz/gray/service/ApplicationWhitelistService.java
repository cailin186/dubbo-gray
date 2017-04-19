package com.xlz.gray.service;

import com.xlz.commons.base.service.BaseService;
import com.xlz.commons.config.Level;
import com.xlz.gray.model.ApplicationWhitelist;

public interface ApplicationWhitelistService extends BaseService<ApplicationWhitelist> {
	/**
     * 根据id删除记录
     * 
     * @param ids 多个id以半角英文逗号分隔 如;1,2,3
     */
    void delete(String ids,Integer linkId,Level level);
}

