function setMaskFix(){
	$(".pin").mask("000000", {placeholder: "Enter 6 digits."});
	$(".cell").mask("0000000000", {placeholder: "Enter 10 digits. e.g 1234567890"});
	$(".phone").mask("000000000000", {placeholder: "Enter digits only"});
	$(".website").attr('placeholder', 'e.g. http://www.websitename.com');
	$(".email").attr('placeholder', 'e.g. johnsmith@domain.com');
	$(".formatdatecalendar").mask("00/00/0000", {placeholder: "DD/MM/YYYY"});
	$(".formatdatecalendar").css('text-align','center');
	$(".formatdatecalendar").css('width','175px');
	$(".formatdatecalendar").datepicker().val();

	//$(".number").mask("000000000000");
	$(".formatdate").mask("00/00/0000", {placeholder: "DD/MM/YYYY"});
	//$(".formatdate").datepicker().val();
	$(".formatdate").css('text-align','center');
	//$(".formatdate").css('width','110px');
	
	
	$(".number").mask("999999999999", {placeholder: "0"});
	$(".number").css('text-align','right');
	$(".number").css('min-width','100px');
	$(".amount").mask("999999999999.99", {placeholder: "0.00"});
	$(".amount").css('text-align','right');
	$(".amount").css('min-width','100px');
	$(".amount_3").mask("999999999999.999", {placeholder: "0.000"});
	$(".amount_3").css('text-align','right');
	$(".amount_3").css('min-width','100px');
	$(".list").css('min-width','100px');
	$(".desc").css('width','300px');
	$(".lov").attr("placeholder", "Press F1 for List");
	$('[required="true"]').attr("placeholder", "Required");
	$('.readonly').attr('readonly', 'readonly');
	$('.readonly').addClass('bg-light');
	$('.readonly').attr('tabindex', '-1');
	$('.readonly').css('opacity', '1');
	$('[readonly="readonly"]').css('background-color', '#eeeeee !important');

	/*var jsRequiredAttrValue = $(this).attr("required");
	if (typeof jsRequiredAttrValue != 'undefined') {
		$(this).css("border-color", "#f6b03d")
		// $(this).after("*");
	}*/
	//$('[readonly="readonly"]').attr('disabled', 'disabled');
	//$('[readonly="readonly"]').attr('tabindex', '-1');
	//$('[readonly="readonly"]').css('opacity', '1');
	//$('.readonly').attr('disabled', 'disabled');
	//$('[readonly="readonly"]').css('color', '#555555');
	/*var jsRequiredAttrValue = $(this).attr("required");
	if (typeof jsRequiredAttrValue != 'undefined') {
		$(this).css("border-color", "#f6b03d")
		// $(this).after("*");
	}*/
	//required
}



/*
function ConfirmYesNo(title, msg, clickelement) {
    var $confirm = $("#modalConfirmYesNo");
    $confirm.modal('show');
    $("#lblTitleConfirmYesNo").html(title);
    $("#lblMsgConfirmYesNo").html(msg);
    jQuery('#btnYesConfirmYesNo').click(function() {
		//alert("Yes");
    	 $confirm.modal("hide");
		//	alert("No");
    	 $("#" + clickelement ).click();
		
	       
    });
    
    jQuery('#btnNoConfirmYesNo').click(function() {
		//alert("No");
		 $confirm.modal("hide");
	       
    });
}*/

function ConfirmYesNo(title, msg, yes_element) {
	//alert(yes_element);
    $("#modalConfirmYesNo").modal("show");
    $("#confirm-title").html(title);
    $("#confirm-msg").html(msg);
    $('#modalConfirmYesNo #btnYes').addClass(yes_element);
    $('#modalConfirmYesNo #btnYes').attr("action", yes_element);
   /* var action_attr = document.createAttribute("action"); 
    action_attr.value = yes_element;
    $('#modalConfirmYesNo #btnYes').setAttributeNode(action_attr);*/
    
}

function showProgress(msg) {
	$('#progresswindow #process-msg').html(msg);
    $("#progresswindow").modal("show");
 }

function hideProgress() {
    $("#progresswindow").modal("hide");
 }

const removeEmptyOrNull = (obj) => {
    Object.keys(obj).forEach(k =>
      (obj[k] && typeof obj[k] === 'object') && removeEmptyOrNull(obj[k]) ||
      (!obj[k] && obj[k] !== undefined) && delete obj[k]
    );
    return obj;
};


function formToJSON(form){
	var serialized = form.serializeArray();
	var s = '';
	var data2 = {};
	for (s in serialized) {
		data2[serialized[s]['name']] = serialized[s]['value']
	}
	return JSON.stringify(removeEmptyOrNull(data2));
}

function showError(response){
	hideProgress();
	$("#modalInformationError").html(response);
	$("#modalInformation").modal("show");
}

function showSuccess(response){
	hideProgress();
	$("#modalInformationError").html(response);
	$("#modalInformation").modal("show");
}
  	
function designMenu() {
	var loAllElements = document.getElementsByName("outeachelement");
	var i;
	var j;
	var eleArr = new Array();
	for (j = 0; j < loAllElements.length; j++) {
		eleArr[j] = loAllElements[j].id;
	}
	for (i = 0; i < eleArr.length; i++) {
		// alert(eleArr[i]);
		var elementhtml = document.getElementById(eleArr[i]);
		var level = elementhtml.getAttribute("level");
		// alert(level);
		var parentelementhtml;
		if (level < 3) {
			parentelementhtml = document.getElementById(elementhtml
					.getAttribute("parentid"));
		} else {
			parentelementhtml = document.getElementById("ul_"
					+ elementhtml.getAttribute("parentid"));
		}
		// alert("parentelementhtml" + parentelementhtml);
		if (parentelementhtml != null) {
			// alert("In if");
			parentelementhtml.appendChild(elementhtml);
		}
	}

	var loAllElementsChild = document.getElementsByName("eachelementchild");
	var k;

	var eleArr = new Array();
	for (k = 0; k < loAllElementsChild.length; k++) {
		var elementhtml = loAllElementsChild[k];
		var parentelementhtml;
		parentelementhtml = document.getElementById("ul_"
				+ elementhtml.getAttribute("parentid"));
		// alert("parentelementhtml" + parentelementhtml);
		if (parentelementhtml != null) {
			// alert("In if");
			parentelementhtml.appendChild(elementhtml);
		}
	}
}


function processCheckbox(fsDBField) {
	if (document.getElementById("dummy_" + fsDBField).checked) {
		document.getElementById(fsDBField).value = "Y";
	} else {
		document.getElementById(fsDBField).value = "N";
	}
}
