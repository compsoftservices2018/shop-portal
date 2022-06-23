package com.framework.report;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.HttpSession;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

import com.framework.reference.ReferenceUtils;

public class ExecuteReport {
	public static void executeReport() {// throws EngineException {

		IReportEngine engine = null;
		EngineConfig config = null;

		try {
			config = new EngineConfig();
			config.setBIRTHome("C:\\Dev\\dependencies\\birt-runtime\\ReportEngine");
			config.setLogConfig("c:\\temp\\test", Level.FINEST);
			Platform.startup(config);
			final IReportEngineFactory FACTORY = (IReportEngineFactory) Platform
					.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			engine = FACTORY.createReportEngine(config);

			// Open the report design
			IReportRunnable design = null;
			design = engine.openReportDesign("D:\\MGPAppResources\\resources\\app\\shop\\10001\\system_reports\\designs\\sangh_invoice.rptdesign");
			IRunAndRenderTask task = engine.createRunAndRenderTask(design);
			// task.setParameterValue("Top Count", (new Integer(5)));
			// task.validateParameters();

			final HTMLRenderOption HTML_OPTIONS = new HTMLRenderOption();
			// HTML_OPTIONS.setOutputFileName("D:\\dev-server\\Reports\\Shop\\ORDER_FORMAT.html");
			// HTML_OPTIONS.setOutputFormat("html");
			// HTML_OPTIONS.setHtmlRtLFlag(false);
			// HTML_OPTIONS.setEmbeddable(false);
			// HTML_OPTIONS.setImageDirectory("C:\\test\\images");

			PDFRenderOption PDF_OPTIONS = new PDFRenderOption();
			PDF_OPTIONS.setOutputFileName("D:\\MGPAppResources\\resources\\app\\shop\\10001\\system_reports\\output\\sangh_invoice\\INVOICE.pdf");
			PDF_OPTIONS.setOutputFormat("pdf");

			// task.setRenderOption(HTML_OPTIONS);
			task.setRenderOption(PDF_OPTIONS);

			task.setParameterValue("COMPANY_CODE", "10001");
			task.setParameterValue("GODOWN", null);
			task.setParameterValue("MONTH", "May-2021");
			task.setParameterValue("CYCLE", null);
			task.setParameterValue("TRIP_DATE", null);
			task.setParameterValue("SANGH", "2137");
					task.run();
			task.close();
			engine.destroy();

		} catch (final Exception EX) {
			EX.printStackTrace();
		} finally {
			Platform.shutdown();
		}

	}

	public static void test(final String[] ARGUMENTS) {
		try {
			executeReport();
			/*String lsOutputFileName =  "test.pdf";
			String lsDesign = "ORDER_FORMAT.rptdesign";
			String lsFormat = "pdf";
			Map<String,String> loParams = new HashMap<String,String> ();
			loParams.put("Company_code", "90002");
			loParams.put("Order_No", "00021");*/
			
			//generateReport(lsDesign,lsOutputFileName,lsFormat,loParams );
		} catch (final Exception EX) {
			EX.printStackTrace();
		}
	}
	
	public void generateReport(HttpSession foHttpSession, String lsDesignFileName, String lsOutputFileName,
			String lsFormat,Map<String,String> loParams) {

		IReportEngine engine = null;
		EngineConfig config = null;

		try {
			config = new EngineConfig();
			//config.setBIRTHome("C:\\Dev\\dependencies\\birt-runtime\\ReportEngine");
			config.setLogConfig(ReferenceUtils.getCnfigParamValue(foHttpSession, "REPORT_OUTPUT_ROOT") + "logs", Level.FINEST);
			Platform.startup(config);
			final IReportEngineFactory FACTORY = (IReportEngineFactory) Platform
					.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			//FACTORY
			engine = FACTORY.createReportEngine(config);

			// Open the report design
			IReportRunnable design = null;
			design = engine.openReportDesign(lsDesignFileName);
			IRunAndRenderTask task = engine.createRunAndRenderTask(design);
			// task.setParameterValue("Top Count", (new Integer(5)));
			// task.validateParameters();

			final HTMLRenderOption HTML_OPTIONS = new HTMLRenderOption();
			//HTML_OPTIONS.setOutputFileName("D:\\dev-server\\Reports\\Shop\\ORDER_FORMAT.html");
			//HTML_OPTIONS.setOutputFormat("html");
			// HTML_OPTIONS.setHtmlRtLFlag(false);
			// HTML_OPTIONS.setEmbeddable(false);
			// HTML_OPTIONS.setImageDirectory("C:\\test\\images");

			 PDFRenderOption PDF_OPTIONS = new PDFRenderOption();
			 PDF_OPTIONS.setOutputFileName(lsOutputFileName);
			 PDF_OPTIONS.setOutputFormat(lsFormat);

			 //task.setRenderOption(HTML_OPTIONS);
			 task.setRenderOption(PDF_OPTIONS);
				
			 for (Map.Entry<String,String> entry : loParams.entrySet()) //using map.entrySet() for iteration  
			 {  
				 task.setParameterValue(entry.getKey(), entry.getValue());
			 }   
			 
			/* task.setParameterValue("Company_code", "90002");
			task.setParameterValue("Order_No", "00021");*/
				task.run();
				
			task.close();
			//engine.destroy();
			
		} catch (final Exception EX) {
			EX.printStackTrace();
		} finally {
			Platform.shutdown();
		}

	}
	
	
	
}
