package com.piggsoft.ueditor.callback;

import java.io.File;

import com.piggsoft.ueditor.define.State;

public interface StorageSuccessCallBack {
	State storageCallBack(File file);
}
