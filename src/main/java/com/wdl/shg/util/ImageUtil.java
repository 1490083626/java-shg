package com.wdl.shg.util;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.wdl.shg.dto.ImageHolder;

import ch.qos.logback.classic.Logger;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
public class ImageUtil {
	//通过线程找到水印图片路径，src/mian/resources下
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r =new Random();
	//日志
	private static Logger logger = (Logger) LoggerFactory.getLogger(ImageUtil.class);
	/**
	 * 将CommonsMultipartFile转换成File类
	 * @param cFile
	 * @return
	 */
	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
		File newFile = new File(cFile.getOriginalFilename());
		
		try {
			cFile.transferTo(newFile);
		} catch (IllegalStateException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return newFile;
		
	}
	/**
	 * 处理缩略图，并返回新生成图片的相对值路径
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	public static String generateThumbnail(ImageHolder thumbnail,String targetAddr) {
		//获取不重复的随机名
		String realFileName = getRandomFileName();
		//获取文件的扩展名如png.jpg等
		String extension = getFileExtension(thumbnail.getImageName());
		//如果目标路径不存在，则自动创建
		makeDirPath(targetAddr);
		//获取文件存储的相对路径（带文件名）
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is:" + relativeAddr);
		//获取文件要保存到的目标路径
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is:" + PathUtil.getImgBasePath()+ relativeAddr);
		logger.debug("basePath is:" + basePath);
		//调用Thumbnails生成带有水印的图片
		try {
			Thumbnails.of(thumbnail.getImage()).size(200, 200)
			.sourceRegion(Positions.CENTER, 400,400)
			.outputQuality(0.5f)
			.toFile(dest);
		}catch(IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		//返回图片相对路径，以便不同系统操作
		return relativeAddr;
	}
	/**
	 * 创建目标路径所涉及到的目录，即/home/work/wdl/xxx.jpg，那么home work wdl这三个文件夹都得自动创建
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if(!dirPath.exists()) {
			dirPath.mkdirs();
		}
		
	}
	/**
	 * 获取收入文件流的扩展名
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
	/**
	 * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
	 * @return
	 */
	public static String getRandomFileName() {
		// 获取随机的五位数
		int rannum = r.nextInt(89999) + 10000;
		String nowTimeStr = sDateFormat.format(new Date());
		
		return nowTimeStr + rannum;
	}
	public static void main(String[] args) throws IOException {
		
		Thumbnails.of(new File("E:\\testImages\\test2.png"))
		.size(200, 200).watermark(Positions.BOTTOM_RIGHT,
				ImageIO.read(new File(basePath + "/publish.png")),0.25f).outputQuality(0.8f)
		.toFile("E:\\testImages\\test2new.png");
	}
	
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
		if(fileOrPath.exists()) {
			if(fileOrPath.isDirectory()) {
				File files[] = fileOrPath.listFiles();
				for(int i=0; i<files.length;i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}
	
	/**
	 * 
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	public static String generateNormalImg(ImageHolder thumbnail,String targetAddr) {
		//获取不重复的随机名
		String realFileName = getRandomFileName();
		//获取文件的扩展名如png.jpg等
		String extension = getFileExtension(thumbnail.getImageName());
		//如果目标路径不存在，则自动创建
		makeDirPath(targetAddr);
		//获取文件存储的相对路径（带文件名）
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is:" + relativeAddr);
		//获取文件要保存到的目标路径
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is:" + PathUtil.getImgBasePath()+ relativeAddr);

		//调用Thumbnails生成带有水印的图片
		try {
			Thumbnails.of(thumbnail.getImage()).size(375, 667)
			.watermark(Positions.BOTTOM_RIGHT,
					ImageIO.read(new File(basePath + "/publish.png")),0.4f)
			.outputQuality(0.8f)
			.toFile(dest);
		}catch(IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		//返回图片相对路径，以便不同系统操作
		return relativeAddr;
	}
}
