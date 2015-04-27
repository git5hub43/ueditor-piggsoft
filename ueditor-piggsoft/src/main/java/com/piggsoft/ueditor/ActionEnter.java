package com.piggsoft.ueditor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.piggsoft.ueditor.define.ActionMap;
import com.piggsoft.ueditor.define.AppInfo;
import com.piggsoft.ueditor.define.BaseState;
import com.piggsoft.ueditor.define.State;
import com.piggsoft.ueditor.hunter.ImageHunter;
import com.piggsoft.ueditor.hunter.impl.FileListManager;
import com.piggsoft.ueditor.hunter.impl.ImageListManager;
import com.piggsoft.ueditor.upload.impl.FileUploader;
import com.piggsoft.ueditor.upload.impl.ImageUploader;
import com.piggsoft.ueditor.upload.impl.ScrawlUploader;
import com.piggsoft.ueditor.upload.impl.VideoUploader;

public class ActionEnter {
	private ConfigManager configManager;

	private FileListManager fileListManager;

	private ImageListManager imageListManager;

	private ImageHunter imageHunter;

	private FileUploader fileUploader;

	private ImageUploader imageUploader;

	private ScrawlUploader scrawlUploader;

	private VideoUploader videoUploader;

	public String exec(HttpServletRequest request) {
		String callbackName = request.getParameter("callback");
		if (callbackName != null) {
			if (!validCallbackName(callbackName)) {
				return new BaseState(false, AppInfo.ILLEGAL).toJSONString();
			}
			return callbackName + "(" + this.invoke(request) + ");";
		} else {
			return this.invoke(request);
		}
	}

	public String invoke(HttpServletRequest request) {
		String actionType = request.getParameter("action");
		if (actionType == null || !ActionMap.mapping.containsKey(actionType)) {
			return new BaseState(false, AppInfo.INVALID_ACTION).toJSONString();
		}
		if (this.configManager == null || !this.configManager.valid()) {
			return new BaseState(false, AppInfo.CONFIG_ERROR).toJSONString();
		}
		State state = null;
		int actionCode = ActionMap.getType(actionType);
		Map<String, Object> conf = null;
		int start = 0;
		switch (actionCode) {
		case ActionMap.CONFIG:
			return this.configManager.getAllConfig().toString();
		case ActionMap.UPLOAD_IMAGE:
			state = imageUploader.doExec(request);
			break;
		case ActionMap.UPLOAD_SCRAWL:
			state = scrawlUploader.doExec(request);
			break;
		case ActionMap.UPLOAD_VIDEO:
			state = videoUploader.doExec(request);
			break;
		case ActionMap.UPLOAD_FILE:
			state = fileUploader.doExec(request);
			break;
		case ActionMap.CATCH_IMAGE:
			conf = configManager.getConfig(actionCode);
			String[] list = request.getParameterValues((String) conf
					.get("fieldName"));
			state = imageHunter.capture(list);
			break;
		case ActionMap.LIST_IMAGE:
			start = this.getStartIndex(request);
			state = imageListManager.listFile(start);
			break;
		case ActionMap.LIST_FILE:
			start = this.getStartIndex(request);
			state = fileListManager.listFile(start);
			break;
		}

		return state.toJSONString();

	}

	public int getStartIndex(HttpServletRequest request) {

		String start = request.getParameter("start");

		try {
			return Integer.parseInt(start);
		} catch (Exception e) {
			return 0;
		}

	}

	/**
	 * callback参数验证
	 */
	public boolean validCallbackName(String name) {

		if (name.matches("^[a-zA-Z_]+[\\w0-9_]*$")) {
			return true;
		}

		return false;

	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public void setConfigManager(ConfigManager configManager) {
		this.configManager = configManager;
	}

	public ImageHunter getImageHunter() {
		return imageHunter;
	}

	public void setImageHunter(ImageHunter imageHunter) {
		this.imageHunter = imageHunter;
	}

	public ImageUploader getImageUploader() {
		return imageUploader;
	}

	public void setImageUploader(ImageUploader imageUploader) {
		this.imageUploader = imageUploader;
	}

	public ScrawlUploader getScrawlUploader() {
		return scrawlUploader;
	}

	public void setScrawlUploader(ScrawlUploader scrawlUploader) {
		this.scrawlUploader = scrawlUploader;
	}

	public VideoUploader getVideoUploader() {
		return videoUploader;
	}

	public void setVideoUploader(VideoUploader videoUploader) {
		this.videoUploader = videoUploader;
	}

	public FileUploader getFileUploader() {
		return fileUploader;
	}

	public void setFileUploader(FileUploader fileUploader) {
		this.fileUploader = fileUploader;
	}

	public FileListManager getFileListManager() {
		return fileListManager;
	}

	public void setFileListManager(FileListManager fileListManager) {
		this.fileListManager = fileListManager;
	}

	public ImageListManager getImageListManager() {
		return imageListManager;
	}

	public void setImageListManager(ImageListManager imageListManager) {
		this.imageListManager = imageListManager;
	}
}
