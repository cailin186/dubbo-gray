package com.alibaba.dubbo.gray.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Application  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;  
	private String applicationId;  
	//ip:port
	private Set<String> grayInstance = new HashSet<String>();  
	private String param;  
	private Integer status;
	private Strategy strategy;
	private Map<String,ApplicationService> services = new HashMap<String,ApplicationService>();
	private Set<String> whitelists = new HashSet<String>();
	
	public Application() {
		super();
	}

	public Application(String name, String applicationId, String param, Integer status) {
		super();
		this.name = name;
		this.applicationId = applicationId;
		this.param = param;
		this.status = status;
	}

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
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public Map<String, ApplicationService> getServices() {
		return services;
	}

	public void setServices(Map<String, ApplicationService> services) {
		this.services = services;
	}

	public Set<String> getWhitelists() {
		return whitelists;
	}

	public void setWhitelists(Set<String> whitelists) {
		this.whitelists = whitelists;
	}

	public Set<String> getGrayInstance() {
		return grayInstance;
	}

	public void setGrayInstance(Set<String> grayInstance) {
		this.grayInstance = grayInstance;
	}

	@Override
	public String toString() {
		return "Application [name=" + name + ", applicationId=" + applicationId + ", grayInstance=" + grayInstance
				+ ", param=" + param + ", status=" + status + ", strategy=" + strategy + ", services=" + services
				+ ", whitelists=" + whitelists + "]";
	}

}
