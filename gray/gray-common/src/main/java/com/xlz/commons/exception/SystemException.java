package com.xlz.commons.exception;


/**
 * 系统异常类
 * @author zhangll
 *
 */
public class SystemException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6971716908203238516L;
	
	public SystemException(String message) {
		super(message);
	}
	
	public SystemException(Throwable cause) {
		super(cause);
	}
	
	public SystemException(String message, Throwable cause) {
		super(message, cause);
	}

}
