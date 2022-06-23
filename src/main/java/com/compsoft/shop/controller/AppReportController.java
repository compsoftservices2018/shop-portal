package com.compsoft.shop.controller;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.framework.controller.SuperController;
import com.framework.reference.ReferenceUtils;
import com.framework.utils.FrameworkConstants;
import com.framework.utils.GlobalValues;

/**
 * @author Vaishali Chawadkar
 *
 */

@Controller
public class AppReportController extends SuperController {

	/*@RequestMapping(value = "/PrintBill", method = RequestMethod.POST)
	public @ResponseBody JSONObject downloadCustomerInvoice(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestData) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		try {

			JSONParser parser = new JSONParser();
			JSONObject loInputData = (JSONObject) parser.parse(requestData);
			String lsBillNo = (String) loInputData.get("bill_no");
			String lsFileName = "bill_" 
					+ lsBillNo + ".pdf";
			String lsOutputFileName = 
					ReferenceUtils.getCnfigParamValue(foHttpSession, "REPORT_OUTPUT_ROOT")
					+ lsFileName;
					
			String lsDesign = ReferenceUtils.getCnfigParamValue(foHttpSession, "REPORT_DESIGN_ROOT")
					+ "kirana_bill.rptdesign";
			String lsFormat = "pdf";
			Map<String, String> loParams = new HashMap<String, String>();
			loParams.put("company_code", GlobalValues.getCompanyCode(foHttpSession));
			loParams.put("bill_no", (String) loInputData.get("bill_no"));
			ExecuteReport loNewReport = new ExecuteReport();
			loNewReport.generateReport(foHttpSession, lsDesign, lsOutputFileName, lsFormat, loParams);
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
			loReturn.put("bill_file", lsFileName);
			return loReturn;
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
			loReturn.put("response_error", "Error in generating invoice. Please contact system administrator. " +  ex.getMessage());
			return loReturn;
		}
	}*/
	
	@RequestMapping(value = "/PrintBill", method = RequestMethod.POST)
	public @ResponseBody JSONObject printBill(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestData) {
		// AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		IReportEngine engine = null;
		EngineConfig config = null;
		try {
			
			JSONParser parser = new JSONParser();
			JSONObject loInputData = (JSONObject) parser.parse(requestData);
			String lsBillNo = (String) loInputData.get("bill_no");
			
			config = new EngineConfig();
			config.setLogConfig(ReferenceUtils.getCnfigParamValue(foHttpSession, "REPORT_OUTPUT_ROOT") + "logs",
					Level.SEVERE);
			Platform.startup(config);
			final IReportEngineFactory FACTORY = (IReportEngineFactory) Platform
					.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			engine = FACTORY.createReportEngine(config);
			IReportRunnable design = null;
			String lsDesign = ReferenceUtils.getCnfigParamValue(foHttpSession, "REPORT_DESIGN_ROOT")
					+ "kirana_bill.rptdesign";

			design = engine.openReportDesign(lsDesign);
			IRunAndRenderTask task = engine.createRunAndRenderTask(design);

			PDFRenderOption PDF_OPTIONS = new PDFRenderOption();
			PDF_OPTIONS.setOutputFormat("pdf");
			String lsRootFolder = ReferenceUtils.getCnfigParamValue(foHttpSession, "REPORT_OUTPUT_ROOT");

			String lsFileName = "bill_" + lsBillNo + ".pdf";;
			String lsOutputFileName = lsRootFolder + lsFileName;
			PDF_OPTIONS.setOutputFileName(lsOutputFileName);
			task.setRenderOption(PDF_OPTIONS);
			task.setParameterValue("company_code", GlobalValues.getCompanyCode(foHttpSession));
			task.setParameterValue("bill_no", (String) loInputData.get("bill_no"));
			task.run();
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
			loReturn.put("bill_file", lsFileName);
			return loReturn;

		} catch (Exception ex) {
			ex.printStackTrace();
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			loReturn.put("response_error", "Error in generating invoice. Please contact system administrator. " +  ex.getMessage());
			return loReturn;
		} finally {
			Platform.shutdown();
			if (engine != null)
				engine.destroy();
		}
	}
	
}
