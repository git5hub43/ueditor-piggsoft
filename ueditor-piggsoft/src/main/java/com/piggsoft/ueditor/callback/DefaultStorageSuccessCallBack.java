package com.piggsoft.ueditor.callback;

import java.io.File;

import com.piggsoft.ueditor.define.BaseState;
import com.piggsoft.ueditor.define.State;

public class DefaultStorageSuccessCallBack implements StorageSuccessCallBack{

	@Override
	public State storageCallBack(File file) {
		State state = new BaseState(true);
		state.putInfo( "size", file.length() );
		state.putInfo( "title", file.getName() );
		return state;
	}

}
