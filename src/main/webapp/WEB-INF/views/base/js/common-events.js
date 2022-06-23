$(document).ready(function() {
	$('#maintable').DataTable({
		dom : 'Blfrtip', 
		 buttons : [ 'excelHtml5' ], 
		/* scrollY : 'calc(100vh - 300px)', */
		"scrollX" : true,
		paging : true,
		"info" : true,
		/* responsive : true, */
		"pageLength" : 10
	});
	
	$('#SelectListTable').DataTable({
		dom : 'Blfrtip', 
		 buttons : [ 'excelHtml5' ], 
		/* scrollY : 'calc(100vh - 300px)', */
		"scrollX" : true,
		paging : true,
		"info" : true,
		/* responsive : true, */
		"pageLength" : 10
	});
	
	/*
	$('#').DataTable({
		dom : 'Bfrtip',
		buttons : [],
		"pageLength" : 10
	});*/
	
	<% if (moUserUI != null) 
	{
	%>
		$('.signoff').removeClass("d-none");
		$('.signoff').addClass("d-block");
	<%
	} else {
	%>
		$('.showadminlogin').removeClass("d-none");
		$('.showadminlogin').addClass("d-block");
		$('.showapplogin').removeClass("d-none");
		$('.showapplogin').addClass("d-block");
	<%}%>
	hideProgress();
});

$(window).on("load", function() {
	
			<%if (AlertUtils.hasAppSuccess(session)) {%>
			$('#modalInformation').modal("show");
				$('#d-blockerrors').css('display', 'none');
				$('#d-blockinfo').css('display', 'none');
				$('#d-blocksuccess').css('display', 'block');
			<%} else if (AlertUtils.hasAppInfo(session)) {%>
			$('#modalInformation').modal("show");
				$('#d-blockerrors').css('display', 'none');
				$('#d-blockinfo').css('display', 'block');
				$('#d-blocksuccess').css('display', 'none');
			<%} else if (AlertUtils.hasAppErrors(session)) {%>
			$('#modalInformation').modal("show");
				$('#d-blockerrors').css('display', 'block');
				$('#d-blockinfo').css('display', 'none');
				$('#d-blocksuccess').css('display', 'none');
			<%} else {%>
			$('#d-blockerrors').css('display', 'none');
				$('#d-blockinfo').css('display', 'none');
				$('#d-blocksuccess').css('display', 'none');
			<%}%>
		$('[data-toggle="tooltip"]').tooltip();  
		
		setMaskFix();
	
		
});

	