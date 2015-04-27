package com.piggsoft.ueditor.upload;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.piggsoft.ueditor.ConfigManager;
import com.piggsoft.ueditor.define.State;

public class Uploader{
	protected Map<String, Object> conf = null;
	protected ConfigManager configManager;
	
	protected Base64Uploader base64Uploader;
	protected BinaryUploader binaryUploader;
	
	public final State doExec(HttpServletRequest request) {
		String filedName = (String) this.conf.get("fieldName");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = base64Uploader.save(request.getParameter(filedName),
					this.conf);
		} else {
			state = binaryUploader.save(request, this.conf);
		}

		return state;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public void setConfigManager(ConfigManager configManager) {
		this.configManager = configManager;
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

	public Map<String, Object> getConf() {
		return conf;
	}

	public void setConf(Map<String, Object> conf) {
		this.conf = conf;
	}
}
