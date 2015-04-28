package com.piggsoft.ueditor.context;

import java.util.Map;

import com.alibaba.fastjson.JSON;

public class Configuration {
	private String fieldName;
	private String isBase64;
	private String[] allowFiles;
	private String filename;
	private String[] filter;
	private String dir;
	private int count;
	private String savePath;
	private String rootPath;
	private Long maxSize;
	
	public static Configuration newInstance(Map<String, Object> conf) {
		String json = JSON.toJSONString(conf);
		Configuration configuration = JSON.parseObject(json, Configuration.class);
		return configuration;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getIsBase64() {
		return isBase64;
	}
	public void setIsBase64(String isBase64) {
		this.isBase64 = isBase64;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	public String[] getAllowFiles() {
		return allowFiles;
	}

	public void setAllowFiles(String[] allowFiles) {
		this.allowFiles = allowFiles;
	}

	public String[] getFilter() {
		return filter;
	}

	public void setFilter(String[] filter) {
		this.filter = filter;
	}

	public Long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Long maxSize) {
		this.maxSize = maxSize;
	}
}
