package com.piggsoft.ueditor;

import java.util.List;

public class Message {
	private String message;
	private List<Message> list;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Message> getList() {
		return list;
	}
	public void setList(List<Message> list) {
		this.list = list;
	}
}
