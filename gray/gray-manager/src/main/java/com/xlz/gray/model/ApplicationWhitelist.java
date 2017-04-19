package com.xlz.gray.model;

import com.xlz.commons.base.model.BaseDomain;
import com.xlz.commons.config.Level;

public class ApplicationWhitelist extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 1为用户号，2为用户名，3为电话号码 */
	private Integer whiteListId;  
	/** 服务或者系统id */
	private Integer linkId;  
	/** 1系统2服务 */
	private Level level;  
  	
	public Integer getWhiteListId() {
		return whiteListId;
	}

	public void setWhiteListId(Integer whiteListId) {
		this.whiteListId = whiteListId;
	}
	public Integer getLinkId() {
		return linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

}
