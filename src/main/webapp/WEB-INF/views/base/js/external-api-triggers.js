
$(document).on('click', '.inquiry', function() {
	var formData = formToJSON($("#submitinquiry"));
	console.log(formData);
	showProgress("Submitting request");
	$.ajax({
		type : 'POST',
		url : '<%=ReferenceUtils.getCnfigParamValue(session, "COMPSOFT_API")%>/api-inquiry?callback=',
		dataType : 'jsonp', //use jsonp data type in order to perform cross domain ajax
		contentType : "application/json; charset=utf-8",
		crossDomain : true,
		data : {
			requestData : formData
		},
		success : function(data) {
			hideProgress();
			$("#modalInformation #modalInformationError").html("Inquiry submitted successfully");
			$('#modalInformation').modal("show");
		},
		error : function(data) {
			hideProgress();
			if (data.status == 200) {
				$("#modalInformation #modalInformationError").html("Inquiry submitted successfully");
				$('#modalInformation').modal("show");
			} else {
				$("#modalInformation #modalInformationError").html("Inquiry submitted successfully");
				$("#modalInformation #modalInformationError").html(
						"Inquiry NOT submitted successfully. Please try later.");
				$('#modalInformation').modal("show");
			}
		}
	});

});