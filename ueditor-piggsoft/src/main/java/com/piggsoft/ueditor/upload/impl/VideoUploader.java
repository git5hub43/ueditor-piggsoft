package com.piggsoft.ueditor.upload.impl;

import org.springframework.beans.factory.InitializingBean;

import com.piggsoft.ueditor.define.ActionMap;
import com.piggsoft.ueditor.upload.Uploader;

public class VideoUploader extends Uploader implements InitializingBean{

	@Override
	public void afterPropertiesSet() throws Exception {
		setConf(getConfigManager().getConfig(ActionMap.UPLOAD_VIDEO));
	}
	
}
