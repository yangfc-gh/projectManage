package cn.com.project.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ExcelUtils {
	
	private static final Log log = LogFactory.getLog(ExcelUtils.class);

	private static boolean doExport(HttpServletRequest request, HttpServletResponse response, String fileName, XSSFWorkbook wb){
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			response.reset();  
			response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("gb2312"), "iso8859-1")+".xlsx");  
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			wb.write(output);//写入文件
			return true;
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
		} finally{
			if (null != wb) {
				try {
					wb.close();
				} catch (IOException e) {
				}
			}
			if (null != output) {
				try {
					output.close();
				} catch (IOException e) {
				}
			}
			try {
				response.flushBuffer();
			} catch (IOException e) {
			}
		}
		return false;
	}
	
	private static boolean doExportZip(HttpServletRequest request, HttpServletResponse response, String fileName, List<Map<String, Object>> wbs){
		OutputStream output = null;
		ZipOutputStream zipos = null;
		try {
			output = response.getOutputStream();
			response.reset();  
			response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("gb2312"), "iso8859-1")+".zip");  
			response.setContentType("application/zip;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			
			ZipOutputStream zos = new ZipOutputStream(output);
			for(Map<String, Object> tmp : wbs){
				zos.putNextEntry(new ZipEntry(tmp.get("fname").toString()));
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				((XSSFWorkbook)tmp.get("wb")).write(bos);
				bos.writeTo(zos);
				zos.closeEntry();
            }
			zos.close();
			return true;
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
		} finally{
			if (null != output) {
				try {
					output.close();
				} catch (IOException e) {
				}
			}
			try {
				response.flushBuffer();
			} catch (IOException e) {
			}
		}
		return false;
	}

	/**
	 * 写excel，xls格式
	 * @param sheetNum 工作簿索引
	 * @param sheetTitle 工作簿名字
	 * @param headers 标题行
	 * @param result 数据（二维）
	 * @throws Exception
	 */
	public static String writeExcel(int sheetNum, String sheetTitle, String[] headers, List<List<Object>> result) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(sheetNum, sheetTitle);
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth((short) 20);
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell((short) i);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text.toString());
		}
		// 遍历集合数据，产生数据行
		if (result != null) {
			int index = 1;
			for (List<Object> m : result) {
				row = sheet.createRow(index);
				int cellIndex = 0;
				for (Object str : m) {
					HSSFCell cell = row.createCell((short) cellIndex);
					cell.setCellValue(null != str ? str.toString() : "");
					cellIndex++;
				}
				index++;
			}
		}
		File file= new File(FileHelper.FILE_SAVE_PATH);
		if (!file.exists()){
			file.mkdir();
		}
		file= new File(FileHelper.FILE_SAVE_PATH + "fordownload/");
		if (!file.exists()){
			file.mkdir();
		}

		String path = FileHelper.FILE_SAVE_PATH + "fordownload/" + CommonUtils.createUUID() + "_download.xls";
		file = new File(path);
		OutputStream out = new FileOutputStream(file);
		workbook.write(out);
		workbook.close();
		out.flush();
		out.close();
		return path;
	}
}
