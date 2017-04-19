package com.xlz.admin.model;

import com.xlz.commons.base.model.BaseDomain;
import com.xlz.commons.utils.JsonUtils;

/**
 *
 * 角色资源
 *
 */
public class RoleResource extends BaseDomain  {

	private static final long serialVersionUID = 1L;

	/** 角色id */
	private Long roleId;

	/** 资源id */
	private Long resourceId;

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
