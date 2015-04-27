package com.piggsoft.ueditor.hunter.impl;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.piggsoft.ueditor.define.ActionMap;
import com.piggsoft.ueditor.hunter.ListManager;

public class FileListManager extends ListManager implements InitializingBean {
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, Object> conf = configManager.getConfig(ActionMap.LIST_FILE);
		this.rootPath = (String) conf.get("rootPath");
		this.dir = this.rootPath + (String) conf.get("dir");
		this.allowFiles = this.getAllowFiles(conf.get("allowFiles"));
		this.count = (Integer) conf.get("count");
	}
}
