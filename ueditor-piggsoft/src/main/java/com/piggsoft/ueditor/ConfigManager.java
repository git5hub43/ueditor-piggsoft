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

public class ConfigManager implements InitializingBean{

	private Resource originalPath;

	private JSONObject jsonConfig;

	private String rootPath;
	
	// 涂鸦上传filename定义
	private final static String SCRAWL_FILE_NAME = "scrawl";
	// 远程图片抓取filename定义
	private final static String REMOTE_FILE_NAME = "remote";

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
			conf.put("isBase64", "false");
			conf.put("maxSize", this.jsonConfig.getLong("fileMaxSize"));
			conf.put("allowFiles", this.getArray("fileAllowFiles"));
			conf.put("fieldName", this.jsonConfig.getString("fileFieldName"));
			savePath = this.jsonConfig.getString("filePathFormat");
			break;

		case ActionMap.UPLOAD_IMAGE:
			conf.put("isBase64", "false");
			conf.put("maxSize", this.jsonConfig.getLong("imageMaxSize"));
			conf.put("allowFiles", this.getArray("imageAllowFiles"));
			conf.put("fieldName", this.jsonConfig.getString("imageFieldName"));
			savePath = this.jsonConfig.getString("imagePathFormat");
			break;

		case ActionMap.UPLOAD_VIDEO:
			conf.put("maxSize", this.jsonConfig.getLong("videoMaxSize"));
			conf.put("allowFiles", this.getArray("videoAllowFiles"));
			conf.put("fieldName", this.jsonConfig.getString("videoFieldName"));
			savePath = this.jsonConfig.getString("videoPathFormat");
			break;

		case ActionMap.UPLOAD_SCRAWL:
			conf.put("filename", ConfigManager.SCRAWL_FILE_NAME);
			conf.put("maxSize", this.jsonConfig.getLong("scrawlMaxSize"));
			conf.put("fieldName", this.jsonConfig.getString("scrawlFieldName"));
			conf.put("isBase64", "true");
			savePath = this.jsonConfig.getString("scrawlPathFormat");
			break;

		case ActionMap.CATCH_IMAGE:
			conf.put("filename", ConfigManager.REMOTE_FILE_NAME);
			conf.put("filter", this.getArray("catcherLocalDomain"));
			conf.put("maxSize", this.jsonConfig.getLong("catcherMaxSize"));
			conf.put("allowFiles", this.getArray("catcherAllowFiles"));
			conf.put("fieldName", this.jsonConfig.getString("catcherFieldName")
					+ "[]");
			savePath = this.jsonConfig.getString("catcherPathFormat");
			break;

		case ActionMap.LIST_IMAGE:
			conf.put("allowFiles", this.getArray("imageManagerAllowFiles"));
			conf.put("dir", this.jsonConfig.getString("imageManagerListPath"));
			conf.put("count", this.jsonConfig.getInt("imageManagerListSize"));
			break;

		case ActionMap.LIST_FILE:
			conf.put("allowFiles", this.getArray("fileManagerAllowFiles"));
			conf.put("dir", this.jsonConfig.getString("fileManagerListPath"));
			conf.put("count", this.jsonConfig.getInt("fileManagerListSize"));
			break;

		}

		conf.put("savePath", savePath);
		conf.put("rootPath", this.rootPath);

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
		private String filter ( String input ) {
			
			return input.replaceAll( "/\\*[\\s\\S]*?\\*/", "" );
			
		}
}