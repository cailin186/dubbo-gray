package com.xlz.commons.result;

import java.io.Serializable;

/**
 * 返回结果
 * 
 * @author zhangll
 * 
 */
public class Result<T> implements Serializable {

    public static final int SUCCESS = 1;
    public static final int FAILURE = -1;

    private static final long serialVersionUID = 5576237395711742681L;

    /**
	 * 成功标志
	 */
    private boolean success = false;

    /**
	 * 结果码
	 */
	private String code;
	
	/**
	 * 描述
	 */
    private String msg = "";

    private T obj = null;

    public Result() {
	}
    
    public Result(ResultCode resultCode) {
    	this.success = resultCode.isSuccess();
		this.code = resultCode.getCode();
		this.msg = resultCode.getDescription();
	}

	public Result(ResultCode resultCode, boolean success) {
		this.code = resultCode.getCode();
		this.msg = resultCode.getDescription();
		this.success = success;
	}
	
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
