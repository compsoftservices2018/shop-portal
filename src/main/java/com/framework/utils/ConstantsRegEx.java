package com.framework.utils;

public class ConstantsRegEx
{
	public static final String EMAIL = "^$|^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
	public static final String EMAIL_MSG = "Invalid e-mail address. Example - johnsmith@domain.com";
	public static final String WEBSITE = "^$|^(https?:\\/\\/)?(www\\.)?([a-zA-Z0-9]+(-?[a-zA-Z0-9])*\\.)+[\\w]{2,}(\\/\\S*)?$";
	public static final String WEBSITE_MSG = "Invalid web address. Example - http://www.websitename.com";
	public static final String PHONE = "^$|^\\d*$";
	public static final String PHONE_MSG = "Phone number should be numeric.";
	public static final String MOBILE = "^$|^\\d{10}$";
	public static final String MOBILE_MSG = "Mobile number should be of 10 digit.";
	public static final String FAX = "^$|^\\d*$";
	public static final String FAX_MSG = "Fax number should be numeric.";
	public static final String PAN = "^$|^[A-Za-z]{5}\\d{4}[A-Za-z]{1}$";
	public static final String PAN_MSG = "Invalid PAN number format. Example - XXXXX9999X.";
	public static final String CODE = "^$|^[a-zA-Z0-9]{5}$";
	public static final String CODE_MSG = "Code should be of 5 character.";
	public static final String PIN = "^$|^\\d{6}$";
	public static final String PIN_MSG = "PIN should be of 6 digit.";
	

}