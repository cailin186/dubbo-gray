package com.xlz.commons.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务异常类
 * @author zhangll
 *
 */
public class BusinessException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6971716908203238516L;

	/**
	 * 自定义属性
	 */
	private Map<String, Object> resultMap = new HashMap<String, Object>();
	
	public BusinessException(String message) {
		super(message);
	}
	
	public BusinessException(Throwable cause) {
		super(cause);
	}
	
	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BusinessException(String message, Map<String, Object> resultMap) {
		super(message);
		this.resultMap = resultMap;
	}
	
	public BusinessException(String message, Map<String, Object> resultMap, Throwable cause) {
		super(message, cause);
		this.resultMap = resultMap;
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

}
