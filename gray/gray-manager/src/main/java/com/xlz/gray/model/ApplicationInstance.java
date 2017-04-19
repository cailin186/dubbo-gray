package com.xlz.gray.model;

import com.xlz.commons.base.model.BaseDomain;
import com.xlz.commons.config.Type;

public class ApplicationInstance extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** application_id */
	private Long applicationId;  
	/** 类型 */
	private Type type;  
	/** 可以制定多台机器作为灰度机，多个机器用;区分，区间制定用-隔开 */
	private String grayInstance;  
	/** 线上部署应用的ip */
	private String deployInstance;  
	/** 可以按照用户特征等将其分组 */
	private String remark;  
  	
	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	public String getGrayInstance() {
		return grayInstance;
	}

	public void setGrayInstance(String grayInstance) {
		this.grayInstance = grayInstance;
	}
	public String getDeployInstance() {
		return deployInstance;
	}

	public void setDeployInstance(String deployInstance) {
		this.deployInstance = deployInstance;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
