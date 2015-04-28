package com.piggsoft.ueditor.context;

import javax.servlet.http.HttpServletRequest;

import com.piggsoft.ueditor.ConfigManager;
import com.piggsoft.ueditor.define.ActionMap;
import com.piggsoft.ueditor.define.AppInfo;
import com.piggsoft.ueditor.exception.UeditorException;

public class Context{
	
	protected HttpServletRequest request;
	
	protected ConfigManager configManager;
	
	public static final String ACTION_PARAM_NAME = "action";
	
	protected int actionCode;
	
	protected Configuration configuration;
	
	private Context() {
		
	}
	
	private static ThreadLocal<Context> contextThead = new ThreadLocal<>();
	
	public static Context getInstance() {
		return contextThead.get();
	}
	
	public static Context createInstance(HttpServletRequest request, ConfigManager configManager) throws UeditorException {
		Context context = new Context();
		context.setRequest(request);
		context.setConfigManager(configManager);
		context.setActionCode(parseActionCode(request));
		checkConfig(configManager);
		context.setConfiguration(Configuration.newInstance(context.getConfigManager().getConfig(context.getActionCode())));
		contextThead.set(context);
		return context;
	}
	
	private static int parseActionCode(HttpServletRequest request) throws UeditorException {
		String actionType = request.getParameter(ACTION_PARAM_NAME);
		if (actionType == null || !ActionMap.mapping.containsKey(actionType)) {
			throw new UeditorException(AppInfo.INVALID_ACTION);
		}
		return ActionMap.getType(actionType);
	}
	
	private static void checkConfig(ConfigManager configManager) throws UeditorException {
		if (configManager == null || !configManager.valid()) {
			throw new UeditorException(AppInfo.CONFIG_ERROR);
		}
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public void setConfigManager(ConfigManager configManager) {
		this.configManager = configManager;
	}

	public void setActionCode(int actionCode) {
		this.actionCode = actionCode;
	}

	public int getActionCode() {
		return actionCode;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

}
