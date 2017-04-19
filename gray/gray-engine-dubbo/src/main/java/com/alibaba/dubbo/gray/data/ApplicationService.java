package com.alibaba.dubbo.gray.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ApplicationService  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;  
	private String version;  
	private String param;  
	private Integer status;
	
	private Strategy strategy;
	private Set<String> whitelists = new HashSet<String>();
	
	public ApplicationService(String name, String version, String param, Integer status) {
		super();
		this.name = name;
		this.version = version;
		this.param = param;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Set<String> getWhitelists() {
		return whitelists;
	}

	public void setWhitelists(Set<String> whitelists) {
		this.whitelists = whitelists;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public String toString() {
		return "ApplicationService [name=" + name + ", version=" + version + ", param=" + param + ", status=" + status
				+ ", strategy=" + strategy + ", whitelists=" + whitelists + "]";
	}

}
