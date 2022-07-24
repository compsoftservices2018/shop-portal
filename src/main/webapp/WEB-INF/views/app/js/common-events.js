$(document).ready(function() {
		
	<%
	if (ReferenceUtils.getCnfigParamValue(session, "PAYMENT_GATEWAY").equals("Y")) {
	%>
		$('.payment').removeClass("d-none"); 
		$('.payment').addClass("d-block"); 
	<%
	}
	%>
	
	
	designMenu();
});


	