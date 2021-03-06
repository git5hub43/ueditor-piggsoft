package com.piggsoft.ueditor.upload;

import org.apache.commons.codec.binary.Base64;

import com.piggsoft.ueditor.PathFormat;
import com.piggsoft.ueditor.context.Configuration;
import com.piggsoft.ueditor.context.Context;
import com.piggsoft.ueditor.define.AppInfo;
import com.piggsoft.ueditor.define.BaseState;
import com.piggsoft.ueditor.define.FileType;
import com.piggsoft.ueditor.define.State;
import com.piggsoft.ueditor.utils.Constants;

public final class Base64Uploader {

	private StorageManager storageManager;

	public State save(String content) {
		Configuration configuration = Context.getInstance().getConfiguration();
		byte[] data = decode(content);

		long maxSize = configuration.getMaxSize();

		if (!validSize(data, maxSize)) {
			return new BaseState(false, AppInfo.MAX_SIZE);
		}

		String suffix = FileType.getSuffix(FileType.JPG);

		String savePath = PathFormat.parse(configuration.getSavePath(), configuration.getFilename());

		savePath = savePath + suffix;
		String physicalPath = configuration.getRootPath() + savePath;

		State storageState = storageManager.saveBinaryFile(data, physicalPath);

		if (storageState.isSuccess()) {
			storageState.putInfo(Constants.STATE_URL, PathFormat.format(savePath));
			storageState.putInfo(Constants.STATE_TYPE, suffix);
			storageState.putInfo(Constants.STATE_ORIGINAL, "");
		}

		return storageState;
	}

	private byte[] decode(String content) {
		return Base64.decodeBase64(content);
	}

	private boolean validSize(byte[] data, long length) {
		return data.length <= length;
	}

	public StorageManager getStorageManager() {
		return storageManager;
	}

	public void setStorageManager(StorageManager storageManager) {
		this.storageManager = storageManager;
	}

}