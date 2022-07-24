package com.compsoft.shop.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.compsoft.shop.model.MstCustomer;
import com.compsoft.shop.model.MstProduct;
import com.compsoft.shop.model.TranOrder;
import com.compsoft.shop.service.MstCustomerService;
import com.compsoft.shop.service.MstProductService;
import com.compsoft.shop.service.TranOrderService;
import com.framework.controller.SuperController;
import com.framework.reference.ImportExport;
import com.framework.reference.ReferenceUtils;
import com.framework.security.Security;
import com.framework.utils.AlertUtils;
import com.framework.utils.AppUtils;
import com.framework.utils.FrameworkConstants;
import com.framework.utils.GlobalValues;



/**
 * @author Pradeep Chawadkar
 *
 */
/**
 * @author Pradeep
 *
 */
@Controller
public class DataProcessController extends SuperController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MstCustomerService MstCustomerService;

	@Autowired
	private MstProductService MstProductService;

	@Autowired
	private TranOrderService TranOrderService;

	@Autowired
	private SessionFactory sessionFactory;

	@RequestMapping(value = "/ExportOrder", method = RequestMethod.GET)
	public @ResponseBody JSONObject exportOrder(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String data) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		try {
			JSONParser parser = new JSONParser();
			JSONObject loInputData = (JSONObject) parser.parse(data);
			TranOrder loTranOrder = moGson.fromJson(loInputData.toJSONString(), TranOrder.class);
			loTranOrder.getTranOrderKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			List<Map<String, Object>> loAllOrders = TranOrderService.getOrderDetails(loTranOrder);
			List<Map<String, Object>> loParams = ReferenceUtils.getImportExportParams(foHttpSession, jdbcTemplate,
					"EXPORT_ORDER");

			JSONObject loReturnFile = ImportExport.exportData(loAllOrders, loParams, true);
			if (((Integer) loReturnFile.get("status")).intValue() == 1) {
				String lsRootPath = ReferenceUtils.getCnfigParamValue(foHttpSession, "UPLOAD_FOLDER");
				String lsFile = lsRootPath + "OrderList.xlsx";
				File loOutFile = new File(lsFile);
				FileOutputStream loFileStrem = new FileOutputStream(loOutFile);
				((XSSFWorkbook) loReturnFile.get("excelfile")).write(loFileStrem);
				loFileStrem.close();
				loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
				loReturn.put("file", "OrderList.xlsx");
			} else {
				loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("log", loReturnFile.get("log"));
			}

			return loReturn;
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			loReturn.put("log", "Error in downloading data. Please contact system administrator." + ex.getMessage());
			return loReturn;
		}
	}

	@RequestMapping(value = "/ExportProduct", method = RequestMethod.GET)
	public @ResponseBody JSONObject exportProduct(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		try {

			JSONParser parser = new JSONParser();
			MstProduct loProductParam = new MstProduct();
			loProductParam.getMstProductKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loProductParam.getMstProductKey().setVendor_code(GlobalValues.getVendorCode(foHttpSession));
			List<Map<String, Object>> loAllProducts = MstProductService.getProductList(loProductParam);
			List<Map<String, Object>> loParams = ReferenceUtils.getImportExportParams(foHttpSession, jdbcTemplate,
					"EXPORT_PRODUCT");

			JSONObject loReturnFile = ImportExport.exportData(loAllProducts, loParams, true);
			if (((Integer) loReturnFile.get("status")).intValue() == 1) {
				String lsRootPath = ReferenceUtils.getCnfigParamValue(foHttpSession, "UPLOAD_FOLDER");
				String lsFile = lsRootPath + "ProductList.xlsx";
				File loOutFile = new File(lsFile);
				FileOutputStream loFileStrem = new FileOutputStream(loOutFile);
				((XSSFWorkbook) loReturnFile.get("excelfile")).write(loFileStrem);
				loFileStrem.close();
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
				loReturn.put("file", "ProductList.xlsx");
			} else {
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("response_error", loReturnFile.get("log"));
			}

			return loReturn;
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			loReturn.put("response_error", "Error in downloading data. Please contact system administrator." + ex.getMessage());
			return loReturn;
		}
	}

	@RequestMapping(value = "/ExportCustomer", method = RequestMethod.GET)
	public @ResponseBody JSONObject exportCustomer(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		try {

			JSONParser parser = new JSONParser();
			MstCustomer loCustomerParam = new MstCustomer();
			loCustomerParam.getMstCustomerKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			List<Map<String, Object>> loAllCustomer = MstCustomerService.getCustomerList(loCustomerParam);
			List<Map<String, Object>> loParams = ReferenceUtils.getImportExportParams(foHttpSession, jdbcTemplate,
					"EXPORT_CUSTOMER");

			JSONObject loReturnFile = ImportExport.exportData(loAllCustomer, loParams, true);
			if (((Integer) loReturnFile.get("status")).intValue() == 1) {
				String lsRootPath = ReferenceUtils.getCnfigParamValue(foHttpSession, "UPLOAD_FOLDER");
				String lsFile = lsRootPath + "CustomerList.xlsx";
				File loOutFile = new File(lsFile);
				FileOutputStream loFileStrem = new FileOutputStream(loOutFile);
				((XSSFWorkbook) loReturnFile.get("excelfile")).write(loFileStrem);
				loFileStrem.close();
				loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
				loReturn.put("file", "CustomerList.xlsx");
			} else {
				loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("log", loReturnFile.get("log"));
			}

			return loReturn;
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			loReturn.put("log", "Error in downloading data. Please contact system administrator." + ex.getMessage());
			return loReturn;
		}
	}
	
	@RequestMapping(value = "/UploadImage", method = RequestMethod.POST)
	public @ResponseBody JSONObject uploadImage(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam("imagefile") MultipartFile data) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		try {
			String lsSelectFileName = data.getName();
			lsSelectFileName = data.getOriginalFilename();
			if (AppUtils.isValueEmpty(lsSelectFileName)) {
				loReturn.put("status", "failed");
				loReturn.put("error", "Please select file");
				return loReturn;
			}
			String lsSelectFileExt = lsSelectFileName.substring(lsSelectFileName.indexOf(".") + 1,
					lsSelectFileName.length());
			String lsSelectFilePre = lsSelectFileName.substring(0, lsSelectFileName.indexOf("."));
			
			String lsSubFolder = "productimg";
			if (!("jpg".equals(lsSelectFileExt) || "JPG".equals(lsSelectFileExt))) {
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("error", "File type must be .jpg");
				return loReturn;
			}

			if (ImportExport.uploadFile(foHttpSession, data, lsSelectFileName, lsSubFolder)) {
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
				loReturn.put("response_success", "File uploaded successfully");
				loReturn.put("image_name", lsSelectFilePre);
				
			} else {
				loReturn.put("response_status",FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("response_error", "Upload failed");
			}
			return loReturn;

		} catch (Exception ex) {
			ex.printStackTrace();
			loReturn.put("status", "failed");
			loReturn.put("error", "Upload failed");
			return loReturn;
		}
	}

	@RequestMapping(value = "/UploadProducts", method = RequestMethod.POST)
	public @ResponseBody JSONObject uploadProducts(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam("file") MultipartFile inputfile) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		try {
			String lsSelectFileName = inputfile.getOriginalFilename();
			if (AppUtils.isValueEmpty(lsSelectFileName)) {
				loReturn.put("response_status",FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("response_error", "Please select file");
				return loReturn;
			}
			String lsSelectFileExt = lsSelectFileName.substring(lsSelectFileName.indexOf(".") + 1,
					lsSelectFileName.length());
			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");
			
			String lsSubFolder = "";
			if (!(lsSelectFileExt.equals("xlsx"))) {
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("response_error", "File type must be .xlsx");
				return loReturn;
			}
			String lsUploadFileName = "ProductList_" + Calendar.getInstance().getTime().getTime() + ".xlsx";
			ImportExport.uploadFile(foHttpSession, inputfile, lsUploadFileName, "");
			
			File lsTempFile = new File("currentfile.xlsx");
			inputfile.transferTo(lsTempFile);
			
			FileInputStream fis = new FileInputStream(lsTempFile); // obtaining bytes
			XSSFWorkbook loSheet = new XSSFWorkbook(fis);
			List<Map<String, Object>> loParams = ReferenceUtils.getImportExportParams(foHttpSession, jdbcTemplate,
					"IMPORT_PRODUCT");
			JSONObject loReturnJSON = ConvertSheetToJson(loSheet , loParams, "MstProductKey");
			if (((String)loReturnJSON.get("response_status")).equals("success"))
			{
				loReturnJSON = loadSheetToTable_Product((JSONArray)loReturnJSON.get("data"), foHttpSession);
			}
			
			return loReturnJSON;

		} catch (Exception ex) {
			logException(ex, foHttpSession);
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			loReturn.put("response_error", "Error in uploading file.");
			return loReturn;
			
		}
		/*
		 * model.put("uploadresponse", loReturn); return new
		 * ModelAndView("/Shop/Main_DataMgmt", model);
		 */

	}
	
	@RequestMapping(value = "/UploadCustomers", method = RequestMethod.POST)
	public @ResponseBody JSONObject uploadCustomer(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam("file") MultipartFile inputfile) {
		AlertUtils.resetMessage(foHttpSession);
		Map<String, Object> model = new HashMap<String, Object>();
		JSONObject loReturn = new JSONObject();
		try {
			
			
			Session loSession = startTransation(sessionFactory);
			String lsSelectFileName = inputfile.getOriginalFilename();
			if (AppUtils.isValueEmpty(lsSelectFileName)) {
				loReturn.put("status", "failed");
				loReturn.put("error", "Please select file");
				return loReturn;
			}
			
			String lsSelectFileExt = lsSelectFileName.substring(lsSelectFileName.indexOf(".") + 1,
					lsSelectFileName.length());
			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");
			
			String lsSubFolder = "";
			
			if (!(lsSelectFileExt.equals("xlsx"))) {
				loReturn.put("status", "failed");
				loReturn.put("error", "File type must be .xlsx");
				return loReturn;
			}
			String lsUploadFileName = "CustomerList_" + Calendar.getInstance().getTime().getTime() + ".xlsx";
			ImportExport.uploadFile(foHttpSession, inputfile, lsUploadFileName, "");
		
			byte[] bytes = inputfile.getBytes();
			BufferedOutputStream stream ;
			File lsTempFile = new File("currentfile.xlsx");
			inputfile.transferTo(lsTempFile);
			
			FileInputStream fis = new FileInputStream(lsTempFile); // obtaining bytes
			XSSFWorkbook loSheet = new XSSFWorkbook(fis);
			List<Map<String, Object>> loParams = ReferenceUtils.getImportExportParams(foHttpSession, jdbcTemplate,
					"IMPORT_CUSTOMER");
			JSONObject loReturnJSON = ConvertSheetToJson(loSheet , loParams, "MstCustomerKey");
			if (((String)loReturnJSON.get("status")).equals("success"))
			{
				loReturnJSON = loadSheetToTable_Customer((JSONArray)loReturnJSON.get("data"), loCustomer);
			}
			
			return loReturnJSON;

		} catch (Exception ex) {
			logException(ex, foHttpSession);
			loReturn.put("status", "failed");
			loReturn.put("error", "Error in uploading file.");
			return loReturn;
			
		}
	}
	
	public JSONObject ConvertSheetToJson(XSSFWorkbook foSheet, List<Map<String, Object>> foParams, String fsKey) {
		JSONObject loJObj = new JSONObject();
		int r = 1;
		int c = 0;
		String error = FrameworkConstants.EMPTY;
		try {
			if (foParams == null) {
				loJObj.put("response_status", "failed");
				loJObj.put("response_error", "Parameter not setup.");
				loJObj.put("data", null);
				return loJObj;
			}
			
			XSSFSheet sheet = foSheet.getSheetAt(0); // creating a Sheet object to
			int liNumberofRows = sheet.getPhysicalNumberOfRows();
			
			if (liNumberofRows == 0) {
				loJObj.put("response_status", "failed");
				loJObj.put("response_error", "Sheet is empty");
				loJObj.put("data", null);
				return loJObj;
			}
			String lsAllErrors = "";
			String lsAllSuccess = "";
			JSONArray loJSONData = new JSONArray();
			for (r = 1; r < liNumberofRows; r++) {
				JSONObject loRow = new JSONObject();
				JSONObject loRowKey = new JSONObject();
				if (sheet.getRow(r).getCell(0) == null
						|| AppUtils.isValueEmpty(sheet.getRow(r).getCell(0).toString())) {
					break;
				}
				for (c = 0; c < foParams.size(); c++) {
					Cell loCell = sheet.getRow(r).getCell(c);
					if (loCell == null || AppUtils.isValueEmpty(loCell.toString())) {
						continue;
					}
					Map<String, Object> loParam = foParams.get(c);
					System.out.println("loParam " + loParam.toString());
					String lsValue = null;
					error = (String) loParam.get("PROCESS_NAME") + "-" + (String) loParam.get("ERROR");
					if (((String) loParam.get("DATA_TYPE")).equals("DECIMAL")) {
						lsValue = new BigDecimal(String.valueOf(loCell.getNumericCellValue())).setScale(2, BigDecimal.ROUND_CEILING) + "";
					} else if (((String) loParam.get("DATA_TYPE")).equals("NUMBER")) {
						lsValue = new BigDecimal(String.valueOf(loCell.getNumericCellValue())).longValue()+ "";
					} else if (((String) loParam.get("DATA_TYPE")).equals("DATE")) {
						lsValue = AppUtils.getFormattedTimestamp(loCell.getDateCellValue());
					} else {
						lsValue = loCell.toString();
						if (loCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							lsValue = (new BigDecimal(loCell.toString())).longValue() + "";
						}
					}
					if (loParam.get("KEY") != null && ((String) loParam.get("KEY")).equals("Y")) {
						loRowKey.put((String) loParam.get("APP_ATTRIBUTE"), AppUtils.convertNullToEmpty(lsValue));
					} else {
						loRow.put((String) loParam.get("APP_ATTRIBUTE"), AppUtils.convertNullToEmpty(lsValue));
					}
				}
				loRow.put(fsKey, loRowKey);
				loJSONData.add(loRow);
			}
			loJObj.put("response_status", "success");
			loJObj.put("response_success", "Data successfully converted");
			loJObj.put("data", loJSONData);
			return loJObj;

		} catch (Exception ex) {
			ex.printStackTrace();
			loJObj.put("response_status", "failed");
			loJObj.put("response_error", "Error in converting data Row#"+ r + "Column#" + c + " Error: " + error + "/" + ex.getMessage());
			return loJObj;
		}
		
	}
	
	public JSONObject loadSheetToTable_Product(JSONArray foJSon, HttpSession foHttpSession) {
		JSONObject loReturn = new JSONObject();
		try {
			String lsAllErrors = FrameworkConstants.EMPTY;
			String lsAllSuccess = FrameworkConstants.EMPTY;
			Session loSession = startTransation(sessionFactory);
			for (int i = 0; i < foJSon.size(); i++) {
				JSONObject loObject = (JSONObject) foJSon.get(i);
				MstProduct loProduct = moGson.fromJson(loObject.toJSONString(), MstProduct.class);
				loProduct.getMstProductKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
				loProduct.getMstProductKey().setVendor_code(GlobalValues.getVendorCode(foHttpSession));
				
				String lsval = loProduct.validate();
				if (!AppUtils.isValueEmpty(lsval)) {
					lsval = "<li class='text-danger' style='list-style: none;font-weight:bold;'> Product Code:"
							+ loProduct.getMstProductKey().getProduct_code() + " (Row #" + (i + 1) + ") " + "</li>"
							+ lsval;
					lsAllErrors = lsAllErrors + lsval;
				} else {
					lsAllSuccess = lsAllSuccess + "<li class='text-success' style='list-style: none;'> Product Code:"
							+ loProduct.getMstProductKey().getProduct_code() + " (Row #" + (i + 1)
							+ ") successfully uploaded/updated</li>";
					MstProductService.addProduct(loSession, loProduct);
				}

			}
			if (commitNoMsg(loSession.getTransaction(), loSession)) {
				if (AppUtils.isValueEmpty(lsAllErrors)) {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
					loReturn.put("response_success", "All data loaded successfully. Please see log.");
					loReturn.put("log", lsAllSuccess);
				} else {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
					loReturn.put("response_success", "Data partially loaded successfully. Please see log.");
					loReturn.put("log", lsAllErrors + lsAllSuccess);
				}
			} else {
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("response_error", lsAllErrors + lsAllSuccess);
			}
			return loReturn;

		} catch (Exception ex) {
			ex.printStackTrace();
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			loReturn.put("response_error", "Error in loading product data.");
			loReturn.put("log", ex.getMessage());
			return loReturn;
		}
		
	}
	
	public JSONObject loadSheetToTable_Customer(JSONArray foJSon, MstCustomer foCustomer) {
		JSONObject loReturn = new JSONObject();
		try {
			String lsAllErrors = FrameworkConstants.EMPTY;
			String lsAllSuccess = FrameworkConstants.EMPTY;
			Session loSession = startTransation(sessionFactory);
			for (int i = 0; i < foJSon.size(); i++) {
				JSONObject loObject = (JSONObject) foJSon.get(i);
				MstCustomer loMstCustomer = moGson.fromJson(loObject.toJSONString(), MstCustomer.class);
				loMstCustomer.getMstCustomerKey().setCompany_code(foCustomer.getMstCustomerKey().getCompany_code());
				if (AppUtils.isValueEmpty(loMstCustomer.getPassword()))
				{
					loMstCustomer.setPassword(Security.decrypt(FrameworkConstants.DEFAULT_PASSWORD));
				}
				
				String lsval = loMstCustomer.validate();
				//loMstCustomer.setPassword(FrameworkConstants.DEFAULT_PASSWORD);
				if (!AppUtils.isValueEmpty(lsval)) {
					lsval = "<li class='text-danger' style='list-style: none;font-weight:bold;'> Customer Code:"
							+ loMstCustomer.getMstCustomerKey().getCustomer_code() + " (Row #" + (i + 1) + ") " + "</li>"
							+ lsval;
					lsAllErrors = lsAllErrors + lsval;
				} else {
					lsAllSuccess = lsAllSuccess + "<li class='text-success' style='list-style: none;'> Customer Code:"
							+ loMstCustomer.getMstCustomerKey().getCustomer_code() + " (Row #" + (i + 1)
							+ ") successfully uploaded/updated</li>";
					MstCustomerService.addCustomer(loSession, loMstCustomer);
				}
			}
			
			if (commitNoMsg(loSession.getTransaction(), loSession)) {
				if (AppUtils.isValueEmpty(lsAllErrors)) {
					loReturn.put("status", "success");
					loReturn.put("success", "All data loaded successfully. Please see log.");
					loReturn.put("log", lsAllSuccess);
				} else {
					loReturn.put("status", "success");
					loReturn.put("success", "Data partially loaded successfully. Please see log.");
					loReturn.put("log",  lsAllErrors + lsAllSuccess);
				}
			} else {
				loReturn.put("status", "failed");
				loReturn.put("error", lsAllErrors + lsAllSuccess);
			}
			return loReturn;

		} catch (Exception ex) {

			loReturn.put("status", "failed");
			loReturn.put("error", "Error in loading customer data.");
			loReturn.put("log", ex.getMessage());
			return loReturn;
		}
	}
	@RequestMapping(value = "/DeleteProducts", method = RequestMethod.GET)
	public @ResponseBody JSONObject deleteProducts(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestData) {
		JSONObject loReturn = new JSONObject();
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();

			
			Session loSession = startTransation(sessionFactory);
			JSONParser parser = new JSONParser();
			JSONObject requestJson = (JSONObject) parser.parse(requestData);
			String product_code = (String) requestJson.get("product_code");
			String vendor_code = (String) requestJson.get("vendor_code");
			BigDecimal selling_price = (BigDecimal) requestJson.get("selling_price");
			
			if (!AppUtils.isValueEmpty(product_code)) {
				MstProduct loProduct = new MstProduct();
				loProduct.getMstProductKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
				loProduct.getMstProductKey().setProduct_code(product_code);
				loProduct.getMstProductKey().setVendor_code(vendor_code);
				loProduct.getMstProductKey().setSelling_price(selling_price);
					loSession.delete(loProduct);
			} else {
				MstProduct loProductParam = new MstProduct();
				loProductParam.getMstProductKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
				loProductParam.getMstProductKey().setVendor_code(GlobalValues.getVendorCode(foHttpSession));
				List<Map<String, Object>> loAllProducts = MstProductService.getProductList(loProductParam);

				if (loAllProducts != null) {
					int i = 0;
					for (Map<String, Object> loProductRow : loAllProducts) {
						MstProduct loProduct = new MstProduct();
						loProduct.getMstProductKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
						loProduct.getMstProductKey().setProduct_code((String) loProductRow.get("product_code"));
						loProduct.getMstProductKey().setVendor_code((String) loProductRow.get("vendor_code"));
						loProduct.getMstProductKey().setSelling_price((BigDecimal) loProductRow.get("selling_price"));
									loSession.delete(loProduct);
					}
				}
			}

			if (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
				loReturn.put("response_success", "Products deleted successfully.");
			} else {
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("response_error", "Error in deleted product. Please contact system administrator.");
			}

			return loReturn;
		} catch (Exception ex) {
			ex.printStackTrace();
			logException(ex, foHttpSession);
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			loReturn.put("response_error", ex.getMessage() + " Error in deleted product. Please contact system administrator.");
			return loReturn;
		}
	}

}
