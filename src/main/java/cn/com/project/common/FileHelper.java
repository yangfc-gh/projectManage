package cn.com.project.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


public class FileHelper extends org.apache.commons.io.FileUtils {
	
	public static String os = System.getProperty("os.name").toLowerCase();
	/**
	 * 常用（ 保存|上传|下载） 路径
	 */
	public static String FILE_SAVE_PATH = os.startsWith("win") ? "D:/annes/" : "/usr/annes/";
	
	/**
	 * 上传单个文件自定义文件夹
	 * @param file 前台上传文件流
	 * @param fileName 自定义文件名字
	 * @param filePath 本地文件保存（最后带/）
	 * @return
	 * @throws Exception
	 */
	public static String uploadSingleFile (MultipartFile file, String fileName, String filePath) throws Exception {
		if(StringUtils.isBlank(filePath)){
			filePath = FILE_SAVE_PATH;
		}
		if(StringUtils.isBlank(fileName)){
			fileName = file.getOriginalFilename();
		}
		if (!new File(filePath).exists()) {
			new File(filePath).mkdirs();
		}
		String path = filePath + fileName;
		File newfile = new File(path);
		try {
			file.transferTo(newfile);
		} catch (IOException e) {
//			throw new Exception("上传文件失败！");
			return "file upload failed";
		}
		return path;
	}
}
