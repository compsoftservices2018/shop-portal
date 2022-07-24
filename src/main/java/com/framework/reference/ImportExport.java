package com.framework.reference;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.framework.utils.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public class ImportExport {

	public static JSONObject exportData(List<Map<String, Object>> loList, List<Map<String, Object>> loParams,
			boolean fboolHeader) {
		JSONObject loReturn = new JSONObject();
		try {

			if (loParams == null) {
				loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("log", "Import parameter not setup.");
				return loReturn;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			workbook.createSheet();
			XSSFSheet sheet = workbook.getSheetAt(0);
			int liStartRow = 0;
			if (fboolHeader) {
				sheet.createRow(liStartRow);
				XSSFRow firstrow = sheet.getRow(0);
				for (int c = 0; c < loParams.size(); c++) {
					firstrow.createCell(c);
					Cell cell = firstrow.getCell(c);
					cell.setCellValue(String.valueOf((String) loParams.get(c).get("sheet_column")));

				}
				liStartRow++;
			}
			if (loList != null) {
				int i = 0;
				for (i = 0; i < loList.size(); i++) {
					Map<String, Object> loDataRow = loList.get(i);
					sheet.createRow(liStartRow);
					XSSFRow row = sheet.getRow(liStartRow);
					liStartRow++;
					for (int c = 0; c < loParams.size(); c++) {
						row.createCell(c);
						Cell cell = row.getCell(c);
						Object loDataObject = loDataRow.get((String) loParams.get(c).get("app_attribute"));
						if (null != cell && null != loDataObject) {
							if (loParams.get(c).get("data_type").equals("NUMBER")
									|| loParams.get(c).get("data_type").equals("DECIMAL")) {
								cell.setCellValue(((BigDecimal) loDataObject).doubleValue());
							} else if (loParams.get(c).get("data_type").equals("DATE")) {
								CellStyle cellStyle = workbook.createCellStyle();
								CreationHelper createHelper = workbook.getCreationHelper();
								cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
								cell.setCellValue((Timestamp) loDataObject);
								cell.setCellStyle(cellStyle);
							} else {
								cell.setCellValue(String.valueOf(loDataObject));
							}

						}
					}
				}
				loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
				loReturn.put("excelfile", workbook);
				loReturn.put("log", "File created successfully " + i);
			} else {
				loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
				loReturn.put("excelfile", workbook);
				loReturn.put("log", "No data selected for export.");
			}

			return loReturn;
		} catch (Exception E) {
			E.printStackTrace();
			loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			loReturn.put("log", "Error in exporting data " + E.getMessage());
			return loReturn;
		}
	}

	public static boolean uploadFile(HttpSession foHttpSession, MultipartFile foFile, String fsUploadFileName, String fsSubFolder)
	{
		BufferedOutputStream stream ;
		File lsServerFile;
		if (!foFile.isEmpty()) {
			try {
				byte[] bytes = foFile.getBytes();
	
				// Creating the directory to store file
				String lsRootPath = ReferenceUtils.getCnfigParamValue(foHttpSession, "UPLOAD_FOLDER");
				File dir = new File(lsRootPath + File.separator + fsSubFolder );
				if (!dir.exists())
					dir.mkdirs();
	
				// Create the file on server
				 lsServerFile = new File(dir.getAbsolutePath() + File.separator + fsUploadFileName);
				 //foFile.transferTo(lsServerFile);
				 stream = new BufferedOutputStream(
						new FileOutputStream(lsServerFile));
				stream.write(bytes);
				stream.flush();
				stream.close();
				return true;
			} catch (FileNotFoundException e) {
				AlertUtils.addError("Unable to find file", foHttpSession);
				e.printStackTrace();
				return false;
			}
			catch(IOException e){
				AlertUtils.addError("Not able to upload file.", foHttpSession);
				e.printStackTrace();
				return false;
			}
		} else {
			AlertUtils.addError("File is empty.", foHttpSession);
			return false;
		}
	}

}
