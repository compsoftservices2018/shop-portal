package com.framework.utils;

import java.io.BufferedWriter;
import com.framework.reference.*;


import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.framework.model.MstUser;
import java.net.InetAddress; 
/**
 * @author Pradeep Chawadkar
 *
 */

public class AppUtils
{	
	
	public static String buildOptions(HttpSession session, String fsGroup, String fsValue, boolean fsEmptyValue)
	{
		Map<String, String> loMapAll = (Map<String, String>) session.getAttribute("REFERENCE");
		Iterator<Map.Entry<String, String>> itr = loMapAll.entrySet().iterator();
		String lsReturnOptions = FrameworkConstants.EMPTY;
		
		if (fsEmptyValue)
		{
			lsReturnOptions = "<option value=''></option>";
		}

		while (itr.hasNext())
		{
			Map.Entry<String, String> entry = itr.next();
			if (fsGroup.equals(entry.getKey().substring(0, 5)))
			{
				if (fsValue != null && fsValue.equals(entry.getKey().substring(5, entry.getKey().length())))
				{
					lsReturnOptions = lsReturnOptions + "<option value='"
							+ entry.getKey().substring(5, entry.getKey().length()) + "' selected>" 
							+ entry.getValue() + " (" + entry.getKey().substring(5, entry.getKey().length()) + ")"
							+ "</option>";
				} else
				{
					lsReturnOptions = lsReturnOptions + "<option value='"
							+ entry.getKey().substring(5, entry.getKey().length()) + "'>" 
							+ entry.getValue() + " (" + entry.getKey().substring(5, entry.getKey().length()) + ")"
							+ "</option>";
				}
			}
		}
		return lsReturnOptions;
	}

	public static String getCompanyCode(HttpSession fsSession)
	{
		return (String) fsSession.getAttribute("COMPANY");

	}

	public static String getLocationCode(HttpSession fsSession)
	{
		return (String) fsSession.getAttribute("LOCATION");
	}

	public static String getLoggedInUser(HttpSession fsSession)
	{
		MstUser loUser = (MstUser)fsSession.getAttribute("USEROBJ");
		return loUser.getMstUserKey().getUser_code();
	}

	public static String validateModel(Object foObject )
	{
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set inputErrors = validator.validate(foObject);
		Iterator iterator = inputErrors.iterator();
		String lsMessages = FrameworkConstants.EMPTY;
	
		while (iterator.hasNext())
		{
			ConstraintViolation setElement = (ConstraintViolation) iterator.next();
			lsMessages = lsMessages + "<li class='text-danger' style='list-style: none;'>" + setElement.getMessage() + "</li>";
		}

		return lsMessages;
	}


	

	public static boolean isValueEmpty(String fsValue)
	{
		if (fsValue == null || fsValue.equals(FrameworkConstants.EMPTY))
		{
			return true;
		}
		return false;
	}

	public static java.sql.Timestamp getCurrentDate()
	{
		return new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
	}
	
	public static java.sql.Date getCurrentSqlDate()
	{
		
		Date date = new Date(Calendar.getInstance().getTime().getTime());
		return (java.sql.Date) date;
	}
	
	public static java.sql.Time getCurrentTime()
	{
		return new java.sql.Time(Calendar.getInstance().getTime().getTime());
	}
	
	public static java.sql.Timestamp getCurrentTimestamp()
	{
		long time = System.currentTimeMillis();
		return new java.sql.Timestamp(time);
	}
	

	public static java.sql.Date getFormattedDate(String lsDate)
	{
		if (AppUtils.isValueEmpty(lsDate))
		{
			return null;
		}

		try
		{
			SimpleDateFormat loDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date date = loDateFormat.parse(lsDate);
			return new Date(date.getTime());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	
	public static java.sql.Timestamp getFormattedTimestamp(String lsDate)
	{
		
		if (AppUtils.isValueEmpty(lsDate))
		{
			return null;
		}

		try
		{
			SimpleDateFormat loDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date date = loDateFormat.parse(lsDate);
			return new java.sql.Timestamp(date.getTime());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public static String getPrintDate(String lsDate)
	{
		if (AppUtils.isValueEmpty(lsDate))
		{
			return FrameworkConstants.EMPTY;
		}

		try
		{
			return lsDate.substring(8, 10) + "/" + lsDate.substring(5, 7) + "/" + lsDate.substring(0, 4);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return FrameworkConstants.EMPTY;
	}

	
		
	public static String displayErrors(HttpSession foHttpSession)
	{
		String lsMessages = FrameworkConstants.EMPTY;
		
		ArrayList<String> loErrors = (ArrayList<String>)foHttpSession.getAttribute("ERRORS");
		if (loErrors == null)
		{
			return lsMessages;
		}
		for (int i=0; i<loErrors .size(); i++) {
			lsMessages = lsMessages + "<li>" + (String)loErrors.get(i) + "</li>";
		}
		return lsMessages;
		
	}
	
	public static String displayMassages(HttpSession foHttpSession)
	{
		String lsMessages = FrameworkConstants.EMPTY;
		
		ArrayList<String> loErrors = (ArrayList<String>)foHttpSession.getAttribute("ERRORS");
		if (loErrors == null)
		{
			return lsMessages;
		}
		for (int i=0; i<loErrors .size(); i++) {
			lsMessages = lsMessages + "<li>" + (String)loErrors.get(i) + "</li>";
		}
		return lsMessages;
		
	}
	
	public static String getDateForQuery(Timestamp loDate)
	{
		
		if (loDate == null)
		{
			return FrameworkConstants.EMPTY;
		}
		try
		{
			return new SimpleDateFormat("dd-MMM-yyyy").format(loDate);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return FrameworkConstants.EMPTY;
	}
	
	public static String getFormattedDateTime(Timestamp loDate)
	{
		
		if (loDate == null)
		{
			return FrameworkConstants.EMPTY;
		}
		try
		{
			return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(loDate);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return FrameworkConstants.EMPTY;
	}
	
	public static String getFormattedDateTimeMMYYYY(Timestamp loDate)
	{
		
		if (loDate == null)
		{
			return FrameworkConstants.EMPTY;
		}
		try
		{
			return new SimpleDateFormat("MMM-yyyy").format(loDate);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return FrameworkConstants.EMPTY;
	}
	
	public static String getFormattedDate(Timestamp loDate)
	{
		
		if (loDate == null)
		{
			return FrameworkConstants.EMPTY;
		}
		try
		{
			return new SimpleDateFormat("dd/MM/yyyy").format(loDate);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return FrameworkConstants.EMPTY;
	}
	
	public static String getFormattedDate(java.util.Date loDate)
	{
		if (loDate == null)
		{
			return FrameworkConstants.EMPTY;
		}
		try
		{
			return new SimpleDateFormat("dd/MM/yyyy").format(loDate);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return FrameworkConstants.EMPTY;
	}
	
	public static String convertNullToEmpty(Object foValue)
	{
		if (foValue == null )
		{
			return FrameworkConstants.EMPTY;
		}
		return foValue.toString();
	}

	public static BigDecimal convertNullZero(BigDecimal foValue)
	{
		if (foValue == null || AppUtils.isValueEmpty(foValue.toString()))
		{
			return new BigDecimal("0.00");
		}
		return foValue.setScale(2);
	}
	
	public static String convertNullToSpace(String foValue)
	{
		if (foValue == null || isValueEmpty( foValue ))
		{ 
			foValue = FrameworkConstants.SPACE;
			return FrameworkConstants.SPACE;
		}
		return foValue;
	}
	
	public static String convertNullToSpace(Object foValue)
	{
		if (foValue == null || isValueEmpty( foValue.toString() ))
		{ 
			foValue = FrameworkConstants.SPACE;
			return FrameworkConstants.SPACE;
		}
		return foValue.toString();
	}

	public static int daysBetween(java.util.Date foStartDate, java.util.Date foEndDate){
           return (int)( (foEndDate.getTime() - foStartDate.getTime()) / (1000 * 60 * 60 * 24));
   }
	
	public static java.util.Date getRelativeMonthDate(java.util.Date fdtLastPaymentDate, int fiMonths)
	{
		Calendar myCal = Calendar.getInstance();
		myCal.setTime(fdtLastPaymentDate);
		myCal.add(Calendar.MONTH, +fiMonths);
		return myCal.getTime();
	}

	public static java.util.Date getRelativeDaysDate(java.util.Date fdtLastPaymentDate, int fiDays)
	{
		Calendar myCal = Calendar.getInstance();
		myCal.setTime(fdtLastPaymentDate);
		myCal.add(Calendar.DAY_OF_MONTH, +fiDays);
		return myCal.getTime();
	}

	public static java.sql.Timestamp getRelativeDaysDateTime(java.sql.Timestamp loDateTime, int fiDays)
	{
				Calendar cal = Calendar.getInstance();
				cal.setTime(loDateTime);
				cal.add(Calendar.DAY_OF_MONTH, +fiDays);
				loDateTime.setTime(cal.getTime().getTime()); // or
				return new Timestamp(cal.getTime().getTime());
	}
	
	public static int getLateDaysForPendingInstallment(int liPendingInstallment)
	{
		int liLateDays = 0;
		for (int i=liPendingInstallment;i>0;i--)
		{
			liLateDays = liLateDays + i*30;
		}
		return liLateDays;
	}
	
	public static String getCurrentDateTime()
	{
		return AppUtils.getPrintDate(AppUtils.getCurrentTimestamp().toString()) +  " " + AppUtils.getCurrentTime();
	}

	public static String getFormattedTimestamp(java.util.Date ldtDate)
	{
		if (ldtDate == null)
		{
			return FrameworkConstants.EMPTY;
		}

		return  new SimpleDateFormat("dd/MM/yyyy").format(ldtDate);
	}
	
	
		public static int getIntValue(String lsVal)
	{
		int liNewVal = 0;
		try
		{
			liNewVal = Integer.parseInt(lsVal);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			liNewVal = 0;
		}
		return liNewVal;
	}

	
	public static void logInFile(BufferedWriter foBr, String lsMsg) throws IOException
	{
		foBr.write(lsMsg + System.getProperty("line.separator"));
	}
	
	public static String getMMYYYFirstDateForQuery(String lsDate)
	{
		if (lsDate.equals(FrameworkConstants.SPACE))
		{
			return FrameworkConstants.SPACE;
		}
		if (AppUtils.isValueEmpty(lsDate))
		{
			return FrameworkConstants.EMPTY;
		}
		
		try
		{
			lsDate =  "01/" + lsDate;
			return new SimpleDateFormat("dd-MMM-yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse(lsDate));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return FrameworkConstants.EMPTY;
	}
	public static String getMMYYYLastDateForQuery(String lsDate)
	{
		if (lsDate.equals(FrameworkConstants.SPACE))
		{
			return FrameworkConstants.SPACE;
		}
		if (AppUtils.isValueEmpty(lsDate))
		{
			return FrameworkConstants.EMPTY;
		}
		
		try
		{
			
			java.util.Date loDate  = new SimpleDateFormat("dd/MM/yyyy").parse("01/" + lsDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(loDate);
			lsDate =  calendar.getActualMaximum(Calendar.DATE) +  "/" + lsDate;
			return new SimpleDateFormat("dd-MMM-yyyy").format(new SimpleDateFormat("dd/MM/yyyy").parse(lsDate));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return FrameworkConstants.EMPTY;
	}
	
	public static String pw(int n, String ch)
	{
		String lsOut = FrameworkConstants.EMPTY;
		String one[] =
		{ " ", " One", " Two", " Three", " Four", " Five", " Six", " Seven", " Eight", " Nine", " Ten", " Eleven",
				" Telve", " Thirteen", " Fourteen", " Fifteen", " Sixteen", " Seventeen", " Eighteen", " Nineteen" };

		String ten[] =
		{ " ", " ", " Twenty", " Thirty", " Forty", " Fifty", " Sixty", "Seventy", " Eighty", " Ninety" };

		if (n > 19)
		{
			//System.out.print(ten[n / 10] + " " + one[n % 10]);
			lsOut = lsOut + ten[n / 10] + "" + one[n % 10];
		} else
		{
			//System.out.print(one[n]);
			lsOut = lsOut + one[n];
		}
		if (n > 0)
		{
			//System.out.print(ch);
			lsOut = lsOut + ch;
		}
		return lsOut;
		}

	public static String ConvertNumberToWord(int fiInput)
	{
		int n = fiInput;
		
		String lsReturn = FrameworkConstants.EMPTY;
		
		if (n <= 0)
		{
			return "Zero";
		} else
		{
			//NumberToWord a = new NumberToWord();
			lsReturn = lsReturn + pw((n / 1000000000), " Hundred");
			lsReturn = lsReturn + pw((n / 10000000) % 100, " Crore");
			lsReturn = lsReturn + pw(((n / 100000) % 100), " Lakh");
			lsReturn = lsReturn + pw(((n / 1000) % 100), " Thousand");
			lsReturn = lsReturn + pw(((n / 100) % 10), " Hundred");
			lsReturn = lsReturn + pw((n % 100), " ");
		}
		return lsReturn;
	}
	
}
