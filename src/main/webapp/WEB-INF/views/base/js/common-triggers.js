$(document).on('focus', '.amount, .number', function(e){
		$(this).select();
});

$(document).on('click', '.home', function() {
	window.open('<%=request.getContextPath()%>/Home', '_self');
});


$(document).on('click', '.comingsoon', function() {
	$("#comingsoon").modal("show");

});

$(document).on('click', '.resetpassword', function() {
	window.open('<%=request.getContextPath()%>/PageResetPassword', '_self');
});


$(document).on('click', '.appsignoff', function() {
	//$("#logout").submit();
	window.open('<%=request.getContextPath()%>/AppSignOut', '_self')
});

$(document).on('click', '.showadminlogin', function() {
	//$('#modalAdminSignIn').modal("show");
	window.open('<%=request.getContextPath()%>/PageAdminSignIn', '_self')
});

$(document).on('click', '.showapplogin', function() {
	//$('#modalAppSignIn').modal("show");
	window.open('<%=request.getContextPath()%>/PageAppSignIn', '_self')
});



$(document).on('keydown', '.lov', function(e) {
	var keyCode = e.keyCode || e.which;
	if (e.keyCode == 112) {
		e.preventDefault();
		$("#progresswindow").show();
	
		$("#ModalSelectList_modal-body #source").val($(this).attr('ref_name'))

		$.get('GetLOV', {
			requestData : JSON.stringify({
				"lov_type" : $(this).attr('lov')
			})
		}, function(response) {
			$('#SelectListTable').DataTable().clear().destroy();
			$("#SelectListBody").html(response);
			$('#ModalSelectList').modal("show");
			$('input[type=search]').focus();
			$('#ModalSelectList_modal-body #source').focus();
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
			$("#progresswindow").hide();
		});
	}
});

$(document).on('click', '#SelectListTable .selectvalue', function() {
	$('input[ref_name =' + $('#ModalSelectList_modal-body #source').val() + ']')
		.val($(this).attr('value'));
	$('#ModalSelectList').modal("hide");
	$('input[ref_name =' + $('#ModalSelectList_modal-body #source').val() + ']')
		.focus();
	var data_key = JSON.parse($(this).parent().parent().find("#lvl_datakey").html());

	$.each(data_key, function(key, val) {
		var newattr = document.createAttribute(key);
		newattr.value = val;
		document.getElementById($('#ModalSelectList_modal-body #source').val())
			.setAttributeNode(newattr);
	});

	$('input[ref_name =' + $('#ModalSelectList_modal-body #source').val() + ']')
	.change();

	$.each(data_key, function(key, val) {
		var newattr = document.createAttribute(key);
		newattr.value = "";
		document.getElementById($('#ModalSelectList_modal-body #source').val())
			.setAttributeNode(newattr);
	});


});

$(document).on('click', '#modalConfirmYesNo #btnYes', function() {
	$("#modalConfirmYesNo").modal("hide");
	$("#" + $(this).attr("action")).click();
});

$(document).on('click', '.form-new', function() {
	$('#form-new').submit();
});

$(document).on('click', '.form-close', function() {
	$('#form-close').submit();
});

$(document).on('focus', '.amount, .number', function(e){
	$(this).select();
});

