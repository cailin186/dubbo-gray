package com.xlz.gray.model;

import com.xlz.commons.base.model.BaseDomain;

public class ApplicationNginx extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** ip */
	private String ip;  
	/** port */
	private String port;  
	/** 可以按照用户特征等将其分组 */
	private String remark;  
	/** 所属系统 */
	private Long applicationId;  
  	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

}
