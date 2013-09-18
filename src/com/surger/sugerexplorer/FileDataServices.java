package com.surger.sugerexplorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public  class FileDataServices {

	private static final String TAG = "SugerExplorer";

	private File currentPath;
	
	
	// listview 布局文件
	private static final int RESOURCE_LAYOUT = R.layout.file_list_item_4;

	private static final String ICON = "icon", FILENAME = "filename",
			ATTR = "attribute", CHECKED = "checked";

	private static final String[] FROM = { ICON, FILENAME, ATTR, CHECKED };

	private static final int[] TO = { R.id.fileIconIv, R.id.fileNameTv,
			R.id.fileAttrTv, R.id.fileCb };

	public static final String[][] commSuffix = {
			{ "txt", "c", "cpp", "java", "html", "css", "js" },
			{ "jpg", "jpeg", "png", "gif", "bmp" },
			{ "mp3", "wav", "ape", "flac" },
			{ "mp4", "avi", "rmvb", "rm", "mkv" },
			{ "doc", "docx", "xls", "xlsx", "ppt", "pptx" } ,
			{ "zip", "gz", "rar" , "tar" , "7z"}
	};

	public static final int TEXTINDEX = 0;
	public static final int PICINDEX = 1;
	public static final int MUSICINDEX = 2;
	public static final int VIEDOINDEX = 3;
	public static final int ZIPINDEX = 4;

		
	
	/**
	 * @param fileList
	 */
	public  List<Map<String, Object>> loadFileList(Context context,
			File filePath) {
		
		boolean hasParent = true;
		if(filePath.getParentFile().equals(null)){
			hasParent = true;
		}
		
		setCurrentPath(filePath);
		
		List<Map<String, Object>> pageData = new ArrayList<Map<String, Object>>();
		
		
		// 设置图标
		// 设置名称
		// 设置信息
		//设置上级目录
		
		//怎么隐藏该条目上的 checkbox？ 怎么获取 checkbox？
		
		if(hasParent){
			
			Map<String, Object> firstItemData = new HashMap<String, Object>();
			firstItemData.put(ICON, R.drawable.folder);
			firstItemData.put(FILENAME, new String("."));
			firstItemData.put(ATTR, new String("parent folder"));
			firstItemData.put(CHECKED, false);
			pageData.add(firstItemData);
		}
		
		if(filePath.isDirectory()){
			File[] fileList = filePath.listFiles();
		
			for (File item : fileList) {
				// 读取文件列表
				// 读取文件信息 设置图标 设置信息
				// 绑定到视图--显示
				Map<String, Object> oneItemData = new HashMap<String, Object>();
				// 设置图标
				// 设置名称
				// 设置信息 etc...
				oneItemData.put(ICON, getFileIcon(item));
				oneItemData.put(FILENAME, getFileName(item));
				oneItemData.put(ATTR, getFileAttribution(item));
				oneItemData.put(CHECKED, false);
				pageData.add(oneItemData);
			}
		}
		return pageData;

	}

	public  SimpleAdapter getAdapter(Context context,
			List<Map<String, Object>> pageData, ListView listView) {

		
		
		
		return new SimpleAdapter(context, pageData, RESOURCE_LAYOUT, FROM, TO); 
		
	}

	private  String getFileAttribution(File item) {

		char[] mod = { '-', '-', '-', '-' };
		if (item.isDirectory()) {
			mod[0] = 'd';
		}
		if (item.canRead()) {
			mod[1] = 'r';
		}
		if (item.canWrite()) {
			mod[2] = 'w';
		}
		if (item.canExecute()) {
			mod[3] = 'x';
		}

		Date modifyDate = new Date(item.lastModified());

		return new String(mod) + " " + modifyDate.toGMTString();

	}

	private  String getFileName(File item) {
		return item.getName().toString();
	}

	private  int getFileIcon(File item) {
		
		
		int iconId = R.drawable.default2;
		if(item.isDirectory()){
			 return iconId = R.drawable.folder;
		}
		
		
		
		String[] suffixes = null;
		//suffixes[1].e
		
		try {
			suffixes = item.getName().split("\\."); // 正则表达式
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d("TAG" , item.getName() +" " +suffixes.length);
		for (int i = 0; i< suffixes.length ; i++){
			//Log.d("TAG", suffixes[i]);
		}
		if (suffixes.length > 0 ) {
			String suffix = suffixes[suffixes.length -1 ];
			//Log.d("TAG","suffix = " + suffix);
			int i = getFileIconType(suffix);
			switch (i) {
			case TEXTINDEX:
				iconId = R.drawable.notes;
				break;
			case PICINDEX:
				iconId = R.drawable.gallery;
				break;
			case MUSICINDEX:
				iconId = R.drawable.music;
				break;
			case VIEDOINDEX:
				iconId = R.drawable.video_player;
				break;
			case ZIPINDEX:
				iconId = R.drawable.zip_icon; //没效果？ 
				break;
			default :
				break;
			}
		}
		return iconId;

	}

	/**
	 * @param suffix
	 * @return
	 */
	private  int getFileIconType(String suffix) {
		int type = 0;
		for (int index = 0 ; index < commSuffix.length ; index++) {
			for ( String typeString : commSuffix[index] ) {
				if (typeString.equalsIgnoreCase(suffix)) {
					type = index;
					break;
				}
				break;
			}
		}
		return type;
	}

	public File getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(File currentPath) {
		this.currentPath = currentPath;
	}

}
