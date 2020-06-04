package com.wdl.shg.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Random;

public class PathUtil {
	//获取系统分隔符
	private static String seperator = System.getProperty("file.separator");
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss"); // 时间格式化的格式
	private static final Random r = new Random();
	//返回根路径
	public static String getImgBasePath() {
		//获取系统名称
		String os = System.getProperty("os.name");
		String basePath = "";
		if(os.toLowerCase().startsWith("win")) {//windows
			basePath = "E:/wxWork/shg/images/";
		}else{//linux等
			basePath = "/Users/baidu/work/image/";
		}
		//根据系统替换分隔符
		basePath = basePath.replace("/", seperator);
		return basePath;
	}
	
	public static String getHeadLineImagePath() {
		String headLineImagePath = "/upload/images/item/headtitle/";
		headLineImagePath = headLineImagePath.replace("/", seperator);
		return headLineImagePath;
	}
	
	//返回不同业务需求返回子路径
	public static String getUserImagePath(long userId) {
		String imagePath = "upload/item/user/" + userId + "/";
		imagePath = imagePath.replace("/", seperator);
		return imagePath;
	}
	
	public static void deleteFile(String storePath) {
		File file = new File(getImgBasePath() + storePath);
		if (file.exists()) {
			if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
			file.delete();
		}
	}

	

}
