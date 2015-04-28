package com.piggsoft.ueditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import com.piggsoft.ueditor.define.ActionMap;
import com.piggsoft.ueditor.utils.Constants;

public class ConfigManager implements InitializingBean {

	private Resource originalPath;

	private JSONObject jsonConfig;

	private String rootPath;
	
	private void initEnv() throws FileNotFoundException, IOException {
		File file = originalPath.getFile();
		String configContent = FileCopyUtils.copyToString(new FileReader(file));
		configContent = filter(configContent);
		try {
			JSONObject jsonConfig = new JSONObject(configContent);
			this.jsonConfig = jsonConfig;
		} catch (Exception e) {
			this.jsonConfig = null;
		}
	}

	public Map<String, Object> getConfig(int type) {

		Map<String, Object> conf = new HashMap<String, Object>();
		String savePath = null;

		switch (type) {

		case ActionMap.UPLOAD_FILE:
			conf.put(Constants.ParamConf.IS_BASE64, "false");
			conf.put(Constants.ParamConf.MAX_SIZE, this.jsonConfig.getLong(Constants.ParamJsonConfig.FILE_MAX_SIZE));
			conf.put(Constants.ParamConf.ALLOW_FILES, this.getArray(Constants.ParamJsonConfig.FILE_ALLOW_FILES));
			conf.put(Constants.ParamConf.FIELD_NAME, this.jsonConfig.getString(Constants.ParamJsonConfig.FILE_FIELD_NAME));
			savePath = this.jsonConfig.getString(Constants.ParamJsonConfig.FILE_PATH_FORMAT);
			break;

		case ActionMap.UPLOAD_IMAGE:
			conf.put(Constants.ParamConf.IS_BASE64, "false");
			conf.put(Constants.ParamConf.MAX_SIZE, this.jsonConfig.getLong(Constants.ParamJsonConfig.IMAGE_MAX_SIZE));
			conf.put(Constants.ParamConf.ALLOW_FILES, this.getArray(Constants.ParamJsonConfig.IMAGE_ALLOW_FILES));
			conf.put(Constants.ParamConf.FIELD_NAME, this.jsonConfig.getString(Constants.ParamJsonConfig.IMAGE_FIELD_NAME));
			savePath = this.jsonConfig.getString(Constants.ParamJsonConfig.IMAGE_PATH_FORMAT);
			break;

		case ActionMap.UPLOAD_VIDEO:
			conf.put(Constants.ParamConf.IS_BASE64, "false");
			conf.put(Constants.ParamConf.MAX_SIZE, this.jsonConfig.getLong(Constants.ParamJsonConfig.VIDEO_MAX_SIZE));
			conf.put(Constants.ParamConf.ALLOW_FILES, this.getArray(Constants.ParamJsonConfig.VIDEO_ALLOW_FILES));
			conf.put(Constants.ParamConf.FIELD_NAME, this.jsonConfig.getString(Constants.ParamJsonConfig.VIDEO_FIELD_NAME));
			savePath = this.jsonConfig.getString(Constants.ParamJsonConfig.VIDEO_PATH_FORMAT);
			break;

		case ActionMap.UPLOAD_SCRAWL:
			conf.put(Constants.ParamConf.IS_BASE64, "true");
			conf.put(Constants.ParamConf.FILE_NAME, Constants.SCRAWL_FILE_NAME);
			conf.put(Constants.ParamConf.MAX_SIZE, this.jsonConfig.getLong(Constants.ParamJsonConfig.SCRAWL_MAX_SIZE));
			conf.put(Constants.ParamConf.FIELD_NAME, this.jsonConfig.getString(Constants.ParamJsonConfig.SCRAWL_FIELD_NAME));
			savePath = this.jsonConfig.getString(Constants.ParamJsonConfig.SCRAWL_PATH_FORMAT);
			break;

		case ActionMap.CATCH_IMAGE:
			conf.put(Constants.ParamConf.FILE_NAME, Constants.REMOTE_FILE_NAME);
			conf.put(Constants.ParamConf.FILTER, this.getArray(Constants.ParamJsonConfig.CATCHER_LOCAL_DOMAIN));
			conf.put(Constants.ParamConf.MAX_SIZE, this.jsonConfig.getLong(Constants.ParamJsonConfig.CATCHER_MAX_SIZE));
			conf.put(Constants.ParamConf.ALLOW_FILES, this.getArray(Constants.ParamJsonConfig.CATCHER_ALLOW_FILES));
			conf.put(Constants.ParamConf.FIELD_NAME, this.jsonConfig.getString(Constants.ParamJsonConfig.CATCHER_FIELD_NAME)
					+ "[]");
			savePath = this.jsonConfig.getString(Constants.ParamJsonConfig.CATCHER_PATH_FORMAT);
			break;

		case ActionMap.LIST_IMAGE:
			conf.put(Constants.ParamConf.ALLOW_FILES, this.getArray(Constants.ParamJsonConfig.IMAGE_MANAGER_ALLOW_FILES));
			conf.put(Constants.ParamConf.DIR, this.jsonConfig.getString(Constants.ParamJsonConfig.IMAGE_MANAGER_LIST_PATH));
			conf.put(Constants.ParamConf.COUNT, this.jsonConfig.getInt(Constants.ParamJsonConfig.IMAGE_MANAGER_LIST_SIZE));
			break;

		case ActionMap.LIST_FILE:
			conf.put(Constants.ParamConf.ALLOW_FILES, this.getArray(Constants.ParamJsonConfig.FILE_MANAGER_ALLOW_FILES));
			conf.put(Constants.ParamConf.DIR, this.jsonConfig.getString(Constants.ParamJsonConfig.FILE_MANAGER_LIST_PATH));
			conf.put(Constants.ParamConf.COUNT, this.jsonConfig.getInt(Constants.ParamJsonConfig.FILE_MANAGER_LIST_SIZE));
			break;

		}

		conf.put(Constants.ParamConf.SAVE_PATH, savePath);
		conf.put(Constants.ParamConf.ROOT_PATH, this.rootPath);

		return conf;

	}

	// 验证配置文件加载是否正确
	public boolean valid() {
		return this.jsonConfig != null;
	}

	public JSONObject getAllConfig() {

		return this.jsonConfig;

	}

	private String[] getArray(String key) {

		JSONArray jsonArray = this.jsonConfig.getJSONArray(key);
		String[] result = new String[jsonArray.length()];

		for (int i = 0, len = jsonArray.length(); i < len; i++) {
			result[i] = jsonArray.getString(i);
		}

		return result;

	}

	public Resource getOriginalPath() {
		return originalPath;
	}

	public void setOriginalPath(Resource originalPath) {
		this.originalPath = originalPath;
	}

	public JSONObject getJsonConfig() {
		return jsonConfig;
	}

	public void setJsonConfig(JSONObject jsonConfig) {
		this.jsonConfig = jsonConfig;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initEnv();
	}

	// 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
	private String filter(String input) {
		return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");
	}
}
