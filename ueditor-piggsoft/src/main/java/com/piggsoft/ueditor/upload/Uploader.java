package com.piggsoft.ueditor.upload;

import javax.servlet.http.HttpServletRequest;

import com.piggsoft.ueditor.context.Configuration;
import com.piggsoft.ueditor.context.Context;
import com.piggsoft.ueditor.define.State;

public class Uploader{
	protected Base64Uploader base64Uploader;
	protected BinaryUploader binaryUploader;
	
	public final State exec() {
		Context context = Context.getInstance();
		return doExec(context.getRequest(), context.getConfiguration());
	}
	
	public final State doExec(HttpServletRequest request, Configuration configuration) {
		String filedName = configuration.getFieldName();
		State state = null;
		if ("true".equals(configuration.getIsBase64())) {
			state = base64Uploader.save(request.getParameter(filedName));
		} else {
			state = binaryUploader.save(request);
		}
		return state;
	}

	public Base64Uploader getBase64Uploader() {
		return base64Uploader;
	}

	public void setBase64Uploader(Base64Uploader base64Uploader) {
		this.base64Uploader = base64Uploader;
	}

	public BinaryUploader getBinaryUploader() {
		return binaryUploader;
	}

	public void setBinaryUploader(BinaryUploader binaryUploader) {
		this.binaryUploader = binaryUploader;
	}
}
