package com.piggsoft.ueditor.hunter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.piggsoft.ueditor.PathFormat;
import com.piggsoft.ueditor.context.Configuration;
import com.piggsoft.ueditor.context.Context;
import com.piggsoft.ueditor.define.AppInfo;
import com.piggsoft.ueditor.define.BaseState;
import com.piggsoft.ueditor.define.MIMEType;
import com.piggsoft.ueditor.define.MultiState;
import com.piggsoft.ueditor.define.State;
import com.piggsoft.ueditor.upload.StorageManager;
import com.piggsoft.ueditor.utils.Constants;

/**
 * 图片抓取器
 * @author hancong03@baidu.com
 *
 */
public class ImageHunter{

	private StorageManager storageManager;
	
	public State capture () {
		Context context = Context.getInstance();
		Configuration configuration = context.getConfiguration();
		String[] list = context.getRequest().getParameterValues((String) context.getConfiguration().getFieldName());
		String filename = configuration.getFilename();
		String savePath = configuration.getSavePath();
		String rootPath = configuration.getRootPath();
		long maxSize = configuration.getMaxSize();
		List<String> allowTypes = Arrays.asList(configuration.getAllowFiles());
		List<String> filters = Arrays.asList(configuration.getFilter());
		return capture(list, filename, savePath, rootPath, maxSize, allowTypes, filters);
	}
	
	public State capture (String[] list, String filename, String savePath,
			String rootPath, long maxSize, List<String> allowTypes,
			List<String> filters) {
		
		MultiState state = new MultiState( true );
		
		for ( String source : list ) {
			state.addState( captureRemoteData( source, filename, savePath, rootPath, maxSize, allowTypes, filters) );
		}
		return state;
	}


	public State captureRemoteData (String urlStr, String filename,
			String savePath, String rootPath, long maxSize,
			List<String> allowTypes, List<String> filters) {
		
		HttpURLConnection connection = null;
		URL url = null;
		String suffix = null;
		
		try {
			url = new URL( urlStr );

			if ( !validHost(url.getHost(), filters) ) {
				return new BaseState( false, AppInfo.PREVENT_HOST );
			}
			
			connection = (HttpURLConnection) url.openConnection();
		
			connection.setInstanceFollowRedirects( true );
			connection.setUseCaches( true );
		
			if ( !validContentState( connection.getResponseCode() ) ) {
				return new BaseState( false, AppInfo.CONNECTION_ERROR );
			}
			
			suffix = MIMEType.getSuffix( connection.getContentType() );
			
			if ( !validFileType(suffix, allowTypes) ) {
				return new BaseState( false, AppInfo.NOT_ALLOW_FILE_TYPE );
			}
			
			if ( !validFileSize(connection.getContentLength(), maxSize) ) {
				return new BaseState( false, AppInfo.MAX_SIZE );
			}
			
			String _savePath = this.getPath(savePath, filename, suffix );
			String physicalPath = rootPath + _savePath;

			State state = storageManager.saveFileByInputStream( connection.getInputStream(), physicalPath );
			
			if ( state.isSuccess() ) {
				state.putInfo(Constants.STATE_URL, PathFormat.format( _savePath ) );
				state.putInfo(Constants.STATE_SOURCE, urlStr );
			}
			
			return state;
			
		} catch ( Exception e ) {
			return new BaseState( false, AppInfo.REMOTE_FAIL );
		}
		
	}
	
	private String getPath ( String savePath, String filename, String suffix  ) {
		
		return PathFormat.parse( savePath + suffix, filename );
		
	}
	
	private boolean validHost (String hostname, List<String> filters) {
		
		return !filters.contains( hostname );
		
	}
	
	private boolean validContentState (int code) {
		
		return HttpURLConnection.HTTP_OK == code;
		
	}
	
	private boolean validFileType (String type, List<String> allowTypes) {
		
		return allowTypes.contains( type );
		
	}
	
	private boolean validFileSize (int size, long maxSize) {
		return size < maxSize;
	}

	public StorageManager getStorageManager() {
		return storageManager;
	}

	public void setStorageManager(StorageManager storageManager) {
		this.storageManager = storageManager;
	}
	
}
