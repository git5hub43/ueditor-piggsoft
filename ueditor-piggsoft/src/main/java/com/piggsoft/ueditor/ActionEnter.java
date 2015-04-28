package com.piggsoft.ueditor;

import javax.servlet.http.HttpServletRequest;

import com.piggsoft.ueditor.context.Context;
import com.piggsoft.ueditor.define.ActionMap;
import com.piggsoft.ueditor.define.AppInfo;
import com.piggsoft.ueditor.define.BaseState;
import com.piggsoft.ueditor.define.State;
import com.piggsoft.ueditor.exception.UeditorException;
import com.piggsoft.ueditor.hunter.ImageHunter;
import com.piggsoft.ueditor.hunter.ListManager;
import com.piggsoft.ueditor.upload.Uploader;
import com.piggsoft.ueditor.utils.Constants;

public class ActionEnter {
	
	private ConfigManager configManager;

	private ListManager listManager;

	private ImageHunter imageHunter;

	private Uploader uploader;
	
	public String exec(HttpServletRequest request) {
		String callbackName = request.getParameter(Constants.ParamRequest.CALLBACK);
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
		Context context;
		try {
			context = Context.createInstance(request, configManager);
		} catch (UeditorException e) {
			return e.toJsonString();
		}
		State state = null;
		switch (context.getActionCode()) {
		case ActionMap.CONFIG:
			return this.configManager.getAllConfig().toString();
		case ActionMap.UPLOAD_IMAGE:
		case ActionMap.UPLOAD_SCRAWL:
		case ActionMap.UPLOAD_VIDEO:
		case ActionMap.UPLOAD_FILE:
			state = uploader.exec();
			break;
		case ActionMap.CATCH_IMAGE:
			String[] list = request.getParameterValues((String) context.getConf()
					.get(Constants.ParamConf.FIELD_NAME));
			state = imageHunter.capture(list);
			break;
		case ActionMap.LIST_IMAGE:
		case ActionMap.LIST_FILE:
			state = listManager.list();
			break;
		}

		return state.toJSONString();

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

	public Uploader getUploader() {
		return uploader;
	}

	public void setUploader(Uploader uploader) {
		this.uploader = uploader;
	}

	public ListManager getListManager() {
		return listManager;
	}

	public void setListManager(ListManager listManager) {
		this.listManager = listManager;
	}
}
