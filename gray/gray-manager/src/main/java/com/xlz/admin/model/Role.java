package com.xlz.admin.model;

import com.xlz.commons.base.model.BaseDomain;
import com.xlz.commons.utils.JsonUtils;

/**
 *
 * 角色
 *
 */
public class Role extends BaseDomain  {

	private static final long serialVersionUID = 1L;

	/** 角色名 */
	private String name;

	/** 排序号 */
	private Integer seq;

	/** 简介 */
	private String description;

	/** 状态 */
	private Integer status;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
