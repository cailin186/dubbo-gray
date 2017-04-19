package com.alibaba.dubbo.gray.service;

import java.io.Serializable;

public class Cmd implements Serializable{

	private static final long serialVersionUID = 1L;
	private String cmd;
	private String applicationId;
	public Cmd() {
		super();
	}
	public Cmd(String cmd,String applicationId) {
		super();
		this.cmd = cmd;
		this.applicationId = applicationId;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	@Override
	public String toString() {
		return "Cmd [cmd=" + cmd + ", applicationId=" + applicationId + "]";
	}
	
}
