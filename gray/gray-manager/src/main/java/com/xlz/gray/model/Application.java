package com.xlz.gray.model;

import java.util.HashMap;
import java.util.List;

import com.xlz.commons.base.model.BaseDomain;

public class Application extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 名称 */
	private String name;  
	/** group_id */
	private String applicationId;  
	/** 部门id */
	private String organizationId;  
	/** 可以制定多台机器作为灰度机，多个机器用;区分，区间制定用-隔开 */
	private String grayIp;  
	/** 线上部署应用的ip */
	private String deployIp;  
	/** 灰度参数名称*/
	private String param;  
	/** 可以按照用户特征等将其分组 */
	private String remark;  
	
	private String nginxs;
	
	private Object strategy = new HashMap<>(); 
	private Object instance = new HashMap<>(); 
  	
	private Object whitelists;
	private List<ApplicationService> services;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getGrayIp() {
		return grayIp;
	}

	public void setGrayIp(String grayIp) {
		this.grayIp = grayIp;
	}
	public String getDeployIp() {
		return deployIp;
	}

	public void setDeployIp(String deployIp) {
		this.deployIp = deployIp;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getNginxs() {
		return nginxs;
	}

	public void setNginxs(String nginxs) {
		this.nginxs = nginxs;
	}

	public Object getStrategy() {
		return strategy;
	}

	public void setStrategy(Object strategy) {
		this.strategy = strategy;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public Object getWhitelists() {
		return whitelists;
	}

	public void setWhitelists(Object whitelists) {
		this.whitelists = whitelists;
	}

	public List<ApplicationService> getServices() {
		return services;
	}

	public void setServices(List<ApplicationService> services) {
		this.services = services;
	}

	public Object getInstance() {
		return instance;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}

}
