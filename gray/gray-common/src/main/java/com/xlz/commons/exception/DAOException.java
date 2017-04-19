package com.xlz.commons.exception;

/**
 * DAO异常
 * 
 * @author zhangll
 *
 */
public class DAOException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6971716908203238516L;
	
	public DAOException(String message) {
		super(message);
	}
	
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

}
