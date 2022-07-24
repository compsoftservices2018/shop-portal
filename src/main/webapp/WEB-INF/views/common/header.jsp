<%@ include file="imports.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1" />

<script>


</script>


<%

String msDownloads = null;
String msProductImg = null;
String msReport = null;
String msExternalReport = null;
String msImg =  null;


String msLocationUI="";
String msCustNameUI="";
String msWelcomeMsgUI="";
MstCompany moCompanyUI = null;
MstUser moUserUI = null;
String msTitleUI="";
String msUserNameUI="";
String msCompanyShortName="";

moCompanyUI = (MstCompany) session.getAttribute("COMPANYOBJ");
moUserUI = (MstUser) session.getAttribute("USEROBJ");
msDownloads = ReferenceUtils.getCnfigParamValue(session, "RELATIVEPATH_DOWNLOADS");
msProductImg = ReferenceUtils.getCnfigParamValue(session, "RELATIVEPATH_PRODUCT_IMG");
msReport = ReferenceUtils.getCnfigParamValue(session, "RELATIVEPATH_REPORT_OUTPUT");
msExternalReport = ReferenceUtils.getCnfigParamValue(session, "RELATIVEPATH_EXTERNAL_REPORT_OUTPUT");
msImg = ReferenceUtils.getCnfigParamValue(session, "RELATIVEPATH_IMG");

if (moCompanyUI!=null){
	msTitleUI = moCompanyUI.getCompany_name();
	msCompanyShortName = moCompanyUI.getShort_name();
}
if (moUserUI!=null)
	msUserNameUI = moUserUI.getUser_name();
	
	
%>
<%@ include file="libraries.jsp"%>

<%@ include file="modals.jsp"%>
<%@ include file="NavBar.jsp"%>

</head>

