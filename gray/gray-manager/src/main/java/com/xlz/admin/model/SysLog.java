package com.xlz.admin.model;

import java.util.Date;

import com.xlz.commons.base.model.BaseDomain;
import com.xlz.commons.utils.JsonUtils;

/**
 *
 * 系统日志
 *
 */
public class SysLog extends BaseDomain  {

	private static final long serialVersionUID = 1L;

	/** 登陆名 */
	private String loginName;

	/** 角色名 */
	private String roleName;

	/** 内容 */
	private String optContent;

	/** 客户端ip */
	private String clientIp;

	/** 创建时间 */
	private Date createTime;

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getOptContent() {
		return this.optContent;
	}

	public void setOptContent(String optContent) {
		this.optContent = optContent;
	}

	public String getClientIp() {
		return this.clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
