package com.piggsoft.ueditor.exception;

import com.piggsoft.ueditor.define.BaseState;

public class UeditorException extends Exception{
	
	private int code;
	
	public UeditorException(int code) {
		super();
		this.code = code;
	}

	public UeditorException() {
		super();
	}

	public UeditorException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UeditorException(String message, Throwable cause) {
		super(message, cause);
	}

	public UeditorException(String message) {
		super(message);
	}

	public UeditorException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6097087382501781154L;
	
	public String toJsonString() {
		return new BaseState(false, getCode()).toJSONString();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
