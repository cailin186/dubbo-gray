package com.xlz.admin.model;

import com.xlz.commons.base.model.BaseDomain;
import com.xlz.commons.utils.JsonUtils;

/**
 *
 * 用户角色
 *
 */
public class UserRole extends BaseDomain {

	private static final long serialVersionUID = 1L;

	/** 用户id */
	private Long userId;

	/** 角色id */
	private Long roleId;

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
