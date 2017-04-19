package com.xlz.gray.model;

import com.xlz.commons.base.model.BaseDomain;

public class WhiteListGroup extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 组名 */
	private String name;  
	/** 可以按照用户特征等将其分组 */
	private String remark;  
  	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
