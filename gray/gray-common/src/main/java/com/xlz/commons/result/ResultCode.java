package com.xlz.commons.result;

/**
 * 结果码枚举
 * 
 * @author zhangll
 * 
 */
public enum ResultCode {
	
	/**
	 * 通用的返回码
	 */
	COMMON_SUCCESS(true,"J000000","成功"),
	COMMON_BUSINESS_EXCEPTION(false,"J000997","业务异常"),
	COMMON_SYSTEM_EXCEPTION(false,"J000998","系统异常"),
	COMMON_SYSTEM_ERROR(false,"J000999","系统错误");

	/**
	 * 状态
	 */
	private boolean success;
	/**
	 * 结果码
	 */
	private String code;

	/**
	 * 描述
	 */
	private String description;

	private ResultCode(boolean success,String code, String description) {
		this.code = code;
		this.description = description;
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public boolean isSuccess() {
		return success;
	}

}
