package com.piggsoft.ueditor.hunter;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;

import com.piggsoft.ueditor.PathFormat;
import com.piggsoft.ueditor.context.Context;
import com.piggsoft.ueditor.define.AppInfo;
import com.piggsoft.ueditor.define.BaseState;
import com.piggsoft.ueditor.define.MultiState;
import com.piggsoft.ueditor.define.State;

public class ListManager{

	private static final String INDEX_PARAM_NAME = "start";
	
	
	public State list() {
		Context context = Context.getInstance();
		int index = getStartIndex(context.getRequest());
		String rootPath = context.getConfiguration().getRootPath();
		String dir = rootPath + context.getConfiguration().getDir();
		String[] allowFiles = getAllowFiles(context.getConfiguration().getAllowFiles());
		int count = context.getConfiguration().getCount();
		return list(index, rootPath, dir, allowFiles, count);
	}
	
	private State list (int index, String rootPath, String dir,
			String[] allowFiles, int count) {
		
		File fileDir = new File( dir );
		State state = null;

		if ( !fileDir.exists() ) {
			return new BaseState( false, AppInfo.NOT_EXIST );
		}
		
		if ( !fileDir.isDirectory() ) {
			return new BaseState( false, AppInfo.NOT_DIRECTORY );
		}
		
		Collection<File> list = FileUtils.listFiles( fileDir, allowFiles, true );
		
		if ( index < 0 || index > list.size() ) {
			state = new MultiState( true );
		} else {
			Object[] fileList = Arrays.copyOfRange( list.toArray(), index, index + count );
			state = getState( fileList );
		}
		
		state.putInfo( "start", index );
		state.putInfo( "total", list.size() );
		
		return state;
		
	}
	
	public int getStartIndex(HttpServletRequest request) {
		String start = request.getParameter(INDEX_PARAM_NAME);
		try {
			return Integer.parseInt(start);
		} catch (Exception e) {
			return 0;
		}
	}
	
	private State getState ( Object[] files ) {
		
		MultiState state = new MultiState( true );
		BaseState fileState = null;
		
		File file = null;
		
		for ( Object obj : files ) {
			if ( obj == null ) {
				break;
			}
			file = (File)obj;
			fileState = new BaseState( true );
			fileState.putInfo( "url", PathFormat.format( getPath( file) ) );
			state.addState( fileState );
		}
		
		return state;
		
	}
	
	private String getPath ( File file ) {
		Context context = Context.getInstance();
		String rootPath = context.getConfiguration().getRootPath();
		String path = PathFormat.format( file.getAbsolutePath() );
		return path.replace( rootPath, "/" );
		
	}
	
	protected String[] getAllowFiles ( Object fileExt ) {
		
		String[] exts = null;
		String ext = null;
		
		if ( fileExt == null ) {
			return new String[ 0 ];
		}
		
		exts = (String[])fileExt;
		
		for ( int i = 0, len = exts.length; i < len; i++ ) {
			
			ext = exts[ i ];
			exts[ i ] = ext.replace( ".", "" );
			
		}
		
		return exts;
		
	}

}
