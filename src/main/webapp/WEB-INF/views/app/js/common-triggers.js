$(document).on('click', '.signoff', function() {
	//$("#logout").submit();
	window.open('<%=request.getContextPath()%>/PortalSignOut', '_self')
});

$(document).on('change', '.lookup_product_code', function(a) {
		var jsProductCode = $(this).val();
		var jsSellingPrice = $(this).attr('selling_price');
		var jsVendorCode = $(this).attr('vendor_code');
		$.get('GetBillProductInfo', {
			requestData : JSON.stringify({
				"product_code" : jsProductCode,
				"selling_price" :  jsSellingPrice,
				"vendor_code" :  jsVendorCode
			})
		}, function(response) {
			if (response.response_status == -1) {
				$("#ModalSelectList_modal-body #source").val('lookup_product_code');
				$('#SelectListTable').DataTable().clear().destroy();
				$("#SelectListBody").html(response.product_list);
				$('#ModalSelectList').modal("show");
				/*$('#SelectListTable').DataTable({
					dom : 'Blfrtip', 
					 buttons : [ 'excelHtml5' ], 
					 scrollY : 'calc(100vh - 300px)', 
					"scrollX" : true,
					paging : true,
					"info" : true,
					 responsive : true, 
					"pageLength" : 10
				});*/
			} else if (response.response_status == 1) {
				$(".lookup_product_code").val("");
				var jsRowFound = false;
				$(".data_row_product").each(function(index) {
					if ($(this).find(".product_info_row").val() == jsProductCode
							&& (jsSellingPrice == undefined 
									||jsSellingPrice=='' 
										|| $(this).find(".selling_price").val() == jsSellingPrice)) 
					{

						$(this).find(".bill_qty")
							.val(parseFloat($(this).find(".bill_qty").val()) + 1);
						$(this).find(".bill_qty").change();
						jsRowFound = true;
					}
				});
				if (!jsRowFound) {
					if ($('#ProductTable tr:last').find(".product_info_row").val() != '') {
						console.log("new record");
						addNewRow($('#ProductTable tr:last'), false);
					}
					$('#ProductTable tr:last').find(".product_info_row").val(response.PRODUCT_CODE);
					$('#ProductTable tr:last').find('input[attr_name = vendor_code]').val(response.VENDOR_CODE);
					$('#ProductTable tr:last').find('input[attr_name = product_name]').val(response.PRODUCT_NAME);
					$('#ProductTable tr:last').find('input[attr_name = mrp]').val(response.MRP);
					$('#ProductTable tr:last').find('input[attr_name = discount]').val(response.DISCOUNT);
					$('#ProductTable tr:last').find('input[attr_name = bv]').val(response.BV);
					$('#ProductTable tr:last').find('input[attr_name = selling_price]').val(response.SELLING_PRICE);
					$('#ProductTable tr:last').find('input[attr_name = cess_percentage]').val(response.CESS_PERCENTAGE);
					$('#ProductTable tr:last').find('input[attr_name = gst_percentage]').val(response.GST_PERCENTAGE);
					$('#ProductTable tr:last').find('input[attr_name = hsn_code]').val(response.HSN_CODE);
					$('#ProductTable tr:last').find('input[attr_name = landing_cost]').val(response.LANDING_COST);
					$('#ProductTable tr:last').find('input[attr_name = disc_per]').val(response.DISC_PER);
					$('#ProductTable tr:last').find('input[attr_name = bv_per]').val(response.BV_PER);
					$('#ProductTable tr:last').find('input[attr_name = bill_qty]').val(1);
					$('#ProductTable tr:last').find('input[attr_name = bill_qty]').change();
					$('#ProductTable tr:last').find(".bill_qty").val(1);
					$('#ProductTable tr:last').find(".bill_qty").change();
				}
			} else {
				showError(response.response_error);
			}
			$(".lookup_product_code").focus();
		});
	});
	

$(document).on('change', '.customer_info', function(a) {
	var index = $(this).attr("index");
	$.get('GetCustomerInfo', {
		requestData : JSON.stringify({
			"customer_code" : $(this).val()
		})
	}, function(response) {
		console.log(response);
		$('input[ref_name = customer_name]').val(response.CUSTOMER_NAME);
		$('input[ref_name = mobile]').val(response.MOBILE);
		$('input[ref_name = email]').val(response.EMAIL);
		$('input[ref_name = delivery_address]').val(response.ADDRESS);
		$('input[ref_name = pin]').val(response.PIN);
	});
});





