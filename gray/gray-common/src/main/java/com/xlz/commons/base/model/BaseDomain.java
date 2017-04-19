package com.xlz.commons.base.model;
import java.io.Serializable;
import java.util.Date;

public class BaseDomain implements Serializable {

	private static final long serialVersionUID = -4462320355563185956L;
	
	/** 主键id */
	protected Long id;

	/** 创建人 */
	protected String createUser;

	/** 创建时间 */
	protected Date createTime;

	/** 更新人 */
	protected String updateUser;

	/** 更新时间 */
	protected Date updateTime;

	/** 状态  */
	protected Integer status;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
