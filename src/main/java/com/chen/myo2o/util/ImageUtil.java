package com.chen.myo2o.util;

import com.chen.myo2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r = new Random();
	public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
		String realFileName =getRandomFileName();
		String extension = getFileExtension(thumbnail.getImageName());
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage()).size(200, 200).outputQuality(0.25f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}

	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
	public static void main(String[] args) throws IOException {
		String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		Thumbnails.of(new File("E:\\image\\916492631372800465.jpg")).size(1280,960)
				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath+"/watermark.png")),0.8f).outputQuality(0.8f)
				.toFile("F:\\IdeaProjects\\myo2o\\target\\test-classes\\upload\\item\\shop\\9\\916492631372800466.jpg");

	}

	public static String getRandomFileName() {
		//获取随机五位数
		int rannum = r.nextInt(89999)+10000;
		String nowTimeStr = sdf.format(new Date());
		return nowTimeStr + rannum;
	}
	/*
  storePath 是文件的路径还是目录路径
  如果是文件路径则删除文件
  如果是目录则删除目录
   */
	public static void deleteFile(String storePath){
		File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
		if(fileOrPath.exists()) {
			if (fileOrPath.isDirectory()) {
				File[] files = fileOrPath.listFiles();
				for (int i = 0; i < files.length; i++)
					files[i].delete();
			}
			fileOrPath.delete();
		}
	}

	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		// 获取不重复的随机名
		String realFileName = getRandomFileName();
		// 获取文件的拓展名
		String extension = getFileExtension(thumbnail.getImageName());
		//如果目标路径不存在，则自动创建
		makeDirPath(targetAddr);
		//获取文件存储的相对路径(带文件名)
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is" + relativeAddr);
		//获取文件要保存到的目标路径
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is :" + PathUtil.getImgBasePath() + relativeAddr);
		//调用Thumbnails生成有水印的图片
		try {
			Thumbnails.of(thumbnail.getImage()).size(337, 640).outputQuality(0.5f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}

}
