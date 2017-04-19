package com.xlz.gray.model;

import com.xlz.commons.base.model.BaseDomain;

public class WhiteList extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 1为用户号，2为用户名，3为电话号码
            json格式存储，kv */
	private String param;  
	/** 白名单分组 */
	private Integer groupId;  
	/** 备注 */
	private String remark;  
  	
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		if(param != null){
			param = param.replaceAll("&quot;", "\"");
		}
		this.param = param;
	}
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
