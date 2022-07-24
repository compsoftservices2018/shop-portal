<%@ include file="../common/header.jsp"%>
<%@ page import="com.compsoft.shop.model.TranBill"%>
<%@ page import="com.compsoft.shop.model.TranBillDetail"%>
<%@ page import="com.compsoft.shop.model.TranBillPayment"%>

<%
	TranBill loBill = (TranBill) request.getAttribute("TranBill");
%>


<script>

	function calculate() {
		var tot_bill_qty = 0;
		var tot_mrp = 0;
		var tot_discount = 0;
		var tot_bv = 0;
		var tot_selling_price = 0;
		var tot_payment_amt = 0;
		var lsBvAmt = 0;
		var lsDiscount = 0;
		$(".data_row_product").each(function(index) {
			var lsSellingPrice = $(this).find(".selling_price").val();
			var lsBvAmt = $(this).find(".bv_amt").val();
			var lsDiscount = $(this).find(".discount").val();
			$(this).find(".tot_bv_amt").css("text-decoration", "none");
			$(this).find(".selling_price").css("text-decoration", "none");
			$(this).find(".tot_discount").css("text-decoration", "none");
			$(this).find(".mrp").css("text-decoration", "line-through");
			if ($("#flag_bill_type").is(':checked')) {
				$("#bill_type").val("M");
				$(this).find(".tot_bv_amt").css("text-decoration", "line-through");
				$(this).find(".selling_price").css("text-decoration", "line-through");
				$(this).find(".tot_discount").css("text-decoration", "line-through");
				$(this).find(".mrp").css("text-decoration", "none");
				var lsSellingPrice = $(this).find(".mrp").val();
			//var lsBvAmt = 0;
			//var lsDiscount = 0;
			}

			tot_bill_qty += parseFloat($(this).find(".bill_qty").val());
			tot_mrp += parseFloat($(this).find(".mrp").val() * $(this).find(".bill_qty").val());
			tot_discount += parseFloat(lsDiscount * $(this).find(".bill_qty").val());
			tot_bv += parseFloat(lsBvAmt * $(this).find(".bill_qty").val());
			$(this).find(".item_total").val(parseFloat(($(this).find(".bill_qty").val() - $(this).find(".returned_qty").val()) * lsSellingPrice).toFixed(2));
			$(this).find(".tot_bv_amt").val(parseFloat(($(this).find(".bill_qty").val() - $(this).find(".returned_qty").val()) * lsBvAmt).toFixed(2));
			$(this).find(".tot_discount").val(parseFloat(($(this).find(".bill_qty").val() - $(this).find(".returned_qty").val()) * lsDiscount).toFixed(2));
			tot_selling_price += parseFloat($(this).find(".item_total").val());
		});
		$("#tot_bill_qty").val(tot_bill_qty);
		$("#tot_mrp").val(tot_mrp.toFixed(2));
		$("#tot_selling_price").val(tot_selling_price.toFixed(2));
		if ($("#flag_bill_type").is(':checked')) {
			$("#tot_discount").val(0);
			$("#tot_bv").val(0);
		} else {
			$("#tot_discount").val(tot_discount.toFixed(2));
			$("#tot_bv").val(tot_bv);
		}

		calculatePayment();
	}

	function calculatePayment() {
		var tot_payment_amt = 0;
		$(".data_row_payment").each(function(index) {
			console.log("Mode" + $(this).find(".payment_mode").val())
			if ($(this).find(".status").val() == "PD") {
				if ($(this).find(".payment_mode").val() == "RF") {
					tot_payment_amt -= parseFloat($(this).find(".payment_amt").val());
				} else {
					tot_payment_amt += parseFloat($(this).find(".payment_amt").val());
				}
				if ($(this).find(".sub_status").val() == "SM") {
					$(this).find(".rzr_order_id").parent().parent()
						.find('input, textarea, button, select').attr('readonly', 'readonly');
					$(this).find(".rzr_order_id").parent().parent()
						.find('input, textarea, button, select').css('background-color', '#eeeeee !important');
				}
			}

		});
		$("#tot_due_amt").val((parseFloat($("#tot_selling_price").val()) - tot_payment_amt).toFixed(2));
	}

	$(document).on('click', '.printbill', function(e) {
		showProgress("Preparing Invoice.");
		$.post('PrintBill', {
			requestData : JSON.stringify({
				"bill_no" : $('input[ref_name=TranBill_bill_no]').val()
			})
		}, function(responseText) {
			//alert(responseText.response_status);
			if (responseText.response_status == 1) {
				showSuccess("Invoice prepared.");
				window.open('<%=request.getContextPath()%>/app/<%=msReport%>/' + responseText.bill_file, '_self');
				hideProgress();
			} else {
				hideProgress();
				showError(responseText.response_error);
			}

		});
	});

	$(document).on('click', '.savebill, .submitreturn', function(e) {
		$('#status').val("SM")
		saveBill()
	});

	$(document).on('click', '.saveforlater', function(e) {
		$('#status').val("DR")
		saveBill();
	});

	$(document).on('click', '.reopenbill', function(e) {
		$('#status').val("DR")
		saveBill();
	});

	$(document).on('change', '.bill_qty, .returned_qty', function(e) {
		calculate();
	});

	$(document).on('change', '.payment_amt, .payment_mode, .status', function(e) {
		calculatePayment();
	});



	$(document).on('click', '.form-delete-row-product', function(e) {

		if ($(".data_row_product").length == 1) {
			return false;
		}
		$(this).parent().parent().remove();

		$.get('DeleteBillDetail', {
			requestData : tableRowToJson($(this).parents('tr'))
		}, function(responseText) {
			//alert(response.status);
			if (responseText.response_status == 1) {
				//alert("hiding");
				//$(this).closest('tr').hide();
				$("#progresswindow").hide();
			}

		});
		calculate();
	});

	$(document).on('click', '.form-delete-row-payment', function(e) {
		if ($(".data_row_payment").length == 1) {
			return false;
		}
		$(this).parent().parent().deleteRow();
		//jsTable.clear().draw();
		
	
		
		
		$.get('DeleteBillPayment', {
			requestData : tableRowToJson($(this).parents('tr'))
		}, function(responseText) {
			//alert(response.status);
			if (responseText.response_status == 1) {
				//alert("hiding");
				//$(this).closest('tr').hide();
				$("#progresswindow").hide();
			}

		});
		calculate();

	});

	$(document).on('click', '.form-new', function(e) {
		window.open('${pageContext.request.contextPath}/NewBill', '_self');
	});


	$(document).on('change', '#lookup_bill_no', function(e) {
		$('#form-get').submit();
	});

	$(document).on('change', '#lookup_bill_no_return', function(e) {
		$('#form-return-bill').submit();
	});


	$(document).on('click', '.add_payment', function(a) {
		addNewRow($('#PaymentTable tr:last'), false);
	});


	$(document).on('change', '#flag_bill_type', function(a) {
		$("#bill_type").val("");
		calculate();
	});



	$(document).ready(function() {

		/* if ($('#authorise_flag').val() == 'A') {
			$("#ReturnRecForm :input").attr('readonly', 'readonly');
			$("#ReturnRecForm :input").css('background-color', '#eeeeee !important');
			$("#ReturnRecForm :input").removeClass("addemptyrow")
			$(".form-delete-row").hide();
			$(".form-save").hide();
			$(".form-approve").hide();
			$(".form-unapprove").removeClass("hide");
			;
		}
 */
		console.log("aa" + "${TranBill.bill_type}");
		if ("${TranBill.bill_type}" == 'M') {
			$("#flag_bill_type").click();
		}

		if ("${tran_return}" == 'Y') {
			$(".bill_qty").addClass("readonly")
			$(".returned_qty").removeClass("readonly");
			$(".returned_qty").removeAttr("readonly");
				$(".returned_qty").removeClass("bg-light");
				$(".submitreturn").removeClass("d-none");
			$(".form-return-bill").removeClass("d-none");
			$(".col-del-payment").removeClass("d-none");
		} else {
			if ("${TranBill.status}" == ""
				|| "${TranBill.status}" == 'DR') {
				$(".saveforlater").removeClass("d-none");
				$(".savebill").removeClass("d-none");
				$(".col-del-payment").removeClass("d-none");
				$(".col-del-product").removeClass("d-none");
				$(".lookup_product_code_div").removeClass("d-none");
				$(".form-get").removeClass("d-none");
				$(".form-new").removeClass("d-none");
			} else if ("${TranBill.status}" == 'SM') {
				$(".reopenbill").removeClass("d-none");
				$(".printbill").removeClass("d-none");
				$(".bill_qty").addClass("readonly")
				$(".form-get").removeClass("d-none");
				$('#section-header').find('input, textarea, button, select').attr('disabled', 'disabled');
				$('#section-payment').find('input, textarea, button, select').attr('disabled', 'disabled');
				$('#section-product').find('input, textarea, button, select').attr('disabled', 'disabled');
			}
		}

		console.log("Order:" + "${TranBill.order_no}");

		if ("${TranBill.order_no}" != '') {
			$(".order_qty_col").removeClass("d-none");
		}


		var jsTable = $('#ProductTable').DataTable({
			/* dom : 'Blfrtip',
			buttons : [], */
			scrollY : 'calc(100vh - 410px)',
			"scrollX" : true,
			paging : false,
			"info" : true,
			"searching" : false,
			ordering : false
		/* "pageLength" : 10, */
		});
		var jsTablePayment = $('#PaymentTable').DataTable({
			/* dom : 'Blfrtip',
			buttons : [], */
			/* "scrollX" : true, */
			paging : false,
			"info" : false,
			"searching" : false,
			ordering : false
		/* "pageLength" : 10, */
		});



		setMaskFix();
		calculate();
		calculatePayment();
	//console.log("${TranBill}");
	});


	function saveBill() {
		var form_data = new FormData($('#TranBillForm')[0]);
		$.ajax({
			type : 'POST',
			url : 'SaveBill',
			processData : false,
			contentType : false,
			async : false,
			cache : false,
			data : form_data,
			success : function(responseText) {
				console.log(responseText);
				if (responseText.response_status == 1) {
					$("#lookup_bill_no").val(responseText.bill_no);
					$('#lookup_bill_no').change();
				} else {
					showError(responseText.response_error);
				}
				$('input[ref_name=TranBill_bill_no]').val(responseText.bill_no);
			}
		});
	}
</script>


<title><%=session.getAttribute("TITLE")%></title>
<body>
	<input type="hidden" id="tran_return" name="tran_return"
		value="${tran_return}">
	<!-- <div class="page-header">Lookup</div> -->
	<!-- <div class="row m-0 bg-info">

		<div class="col-sm form-group m-0 p-1"></div>
	</div> -->
	<div class="row m-0 border-bottom">
		<div class="col-sm p-1">
			<a id="btnNew" accesskey="n"
				class="access-add btn btn-info btn-sm form-new d-none "
				role="button"> New</a> <a id="btnSave" accesskey="n"
				class="d-none access-update ml-1 btn btn-info btn-sm saveforlater"
				role="button">Save for Later</a> <a id="" accesskey="x"
				class="access-approve d-none text-nowrap printbill ml-1 btn btn-info btn-sm float-right"
				role="button">Print</a> <a id="" accesskey="x"
				class="access-update d-none  text-nowrap savebill ml-1 btn btn-info btn-sm  btn-default-action float-right"
				role="button">Submit Bill</a> <a id="" accesskey="x"
				class="access-update d-none text-nowrap submitreturn ml-1 btn btn-info btn-sm form-close btn-default-action float-right"
				role="button">Submit Return</a>
		</div>
		<div class="col-sm-2 p-1">
			<form id="form-get" method="POST" action="GetBill"
				class="d-none form-get">
				<input type="text" placeholder="Search Bill#" class="form-control "
					id="lookup_bill_no" name="lookup_bill_no" ref_name="lookup_bill_no"
					value="">
			</form>
			<form method="POST" action="ReturnBill" id="form-return-bill"
				class="d-none form-return-bill">
				<input type="text" placeholder="Return Bill#" class="form-control "
					id="lookup_bill_no_return" name="lookup_bill_no_return"
					ref_name="lookup_bill_no_return" value="">
			</form>
		</div>
		<div
			class="d-none col-sm-4 lookup_product_code_div row form-group m-0 p-1 bg-info text-light">
			<label>Scan/Enter Product:</label> <input type="text"
				placeholder="Search product" lov="PRODUCTS"
				class=" col-sm  float-right lov lookup_product_code form-control  "
				id="lookup_product_code" ref_name="lookup_product_code" value="">

		</div>
	</div>

	<form:form id="TranBillForm" class="form p-1" method="POST"
		modelAttribute="TranBill" action="SaveBill">
		<!-- hidden elements -->
		<form:input type="hidden" path="status" value="${status}" />
		<form:input type="hidden" path="order_no" value="${order_no}" />

		<div class="form-row p-1">
			<div class="col-sm-9 p-0" id="section-header">
				<div class="row m-0 p-1">
					<div class="col-sm-2 col p-0">
						<label>Bill No:</label>
						<form:input type="text" class="form-control readonly bill_no"
							path="TranBillKey.bill_no" ref_name="TranBill_bill_no"
							value="${TranBillKey.bill_no}" />
					</div>
					<div class="col-sm-2 p-0">
						<label>Customer Code:</label>
						<form:input type="text"
							class="uppercase form-control lov customer_info" lov="CUSTOMERS"
							path="customer_code" ref_name="customer_code"
							value="${customer_code}" />
					</div>
					<div class="col-sm-3 p-0">
						<label>Customer Name:</label>
						<form:input type="text" class="form-control  readonly"
							path="customer_name" ref_name="customer_name"
							value="${customer_name}" />
					</div>

					<div class="col-sm-2 col p-0">
						<label>Mobile:</label>
						<form:input type="text" class="cell form-control " path="mobile"
							ref_name="mobile" placehonder="Mobile#" value="${mobile}" />
					</div>
					<div class="col-sm-3 col p-0">
						<label>e-mail:</label>
						<form:input type="text" class="email form-control " path="email"
							ref_name="email" placehonder="e-mail id" value="${email}" />
					</div>

				</div>
				<div class="row m-0 p-1">
					<div class="col-sm-5 col p-0">
						<label>Delivery Address:</label>
						<form:input type="text" class="form-control "
							path="delivery_address" ref_name="delivery_address"
							value="${delivery_address}"></form:input>
					</div>
					<div class="col-sm-2 col p-0">
						<label>Pin:</label>
						<form:input type="text" class="pin form-control " path="pin"
							ref_name="pin" value="${pin}" />
					</div>
					<div class="col-sm-3 p-0">
						<label>Remark:</label>
						<form:input type="text" class="form-control  "
							placeholder="Add note" path="remark" value="${remark}" />
					</div>
					<div class="col-sm-2 col p-0">
						<label>Date:</label>
						<form:input type="text" class="formatdate form-control  readonly "
							path="bill_date" value="${bill_date}" />
					</div>
				</div>

			</div>

			<div class="row col-sm-3 p-1 m-0 shadow">
				<div class="row w-100">
					<div class="col col-sm p-0 text-nowrap">
						<span class="h-100 text-underline">Summary</span>
					</div>
					<div class="col col-sm p-0 text-danger">
						<label class="form-check-label" for="bill_type">MRP Bill?</label>
						<input type="checkbox" class="mt-2 ml-2 form-check-input"
							id="flag_bill_type" />
						<form:input type="hidden" path="bill_type" value="${bill_type}" />
					</div>
				</div>
				<div class="col col-sm p-0 text-nowrap d-flex">
					<span class="h-100">MRP:</span>
					<form:input type="text"
						class="amount p-0 pr-2 h-100 text-right form-control form-control-plaintext"
						path="tot_mrp" value="${tot_mrp}" />
				</div>
				<div class="col col-sm p-0 text-nowrap d-flex">
					<span class="h-100">Discount:</span>
					<form:input type="text"
						class="amount h-100 p-0 pr-2 text-right form-control form-control-plaintext"
						path="tot_discount" value="${tot_discount}" />
				</div>
				<div class="col col-sm p-0  text-nowrap d-flex">
					<span class="h-100">BV:</span>
					<form:input type="text"
						class="amount h-100 p-0 pr-2 text-right form-control form-control-plaintext"
						path="tot_bv" value="${tot_bv}" />
				</div>
				<div class="col col-sm p-0  text-nowrap d-flex">
					<span class="h-100">Qty:</span>
					<form:input type="text"
						class="amount h-100 p-0 pr-2 text-right form-control form-control-plaintext"
						path="tot_bill_qty" value="${tot_bill_qty}" />
				</div>
				<div class="col-12 p-0  text-nowrap d-flex">
					<span class="h-100">Bill Amount:</span>
					<form:input type="text"
						class="amount h-100 p-0 pr-2 text-right font-weight-bold form-control form-control-plaintext"
						path="tot_selling_price" value="${tot_selling_price}" />
				</div>
				<div class="col-12 p-0  text-nowrap d-flex ">
					<span class="text-danger h-100">Due Amount:</span>
					<form:input type="text"
						class="h-100 p-0 pr-2 text-right font-weight-bold text-danger form-control form-control-plaintext"
						path="tot_due_amt" value="${tot_due_amt}" />
				</div>

			</div>

		</div>

		<div class="overflow-auto" id="section-payment">
			<table id="PaymentTable" class="table" style="width: 100%;">
				<thead>
					<tr>
						<th class="d-none"></th>
						<th class="d-none"></th>
						<th class="d-none"></th>
						<th class="d-none"></th>
						<th class="d-none"></th>
						<th class="d-none"></th>
						<th class="d-none"></th>
						<th class="d-none"></th>
						<th style="width: 25px;" class="col-del-payment p-0 d-none"><a
							class="btn btn-info btn-sm btn-block add_payment">+</a></th>
						<th>Payment Mode</th>
						<th>Status</th>
						<th>Details</th>
						<th>Amount</th>
					</tr>
				</thead>
				<tbody id="PaymentTableBody">

					<c:forEach items="${TranBillPaymentRows}" var="TranBillPaymentR"
						varStatus="loopCounter">
						<%
							TranBillPayment loPaymentRow = (TranBillPayment) pageContext.getAttribute("TranBillPaymentR");
									System.out.println("In Loop");
						%>
						<tr id="${loopCounter.index}" class="data_row_payment"
							name="PaymentTable${loopCounter.index}">
							<td class="d-none"><form:input
									path="TranBillPaymentList[${loopCounter.index}].TranBillPaymentKey.bill_no"
									type="text" class="form-control readonly"
									object="TranBillPaymentList" key_name="TranBillPaymentKey"
									attr_name="bill_no" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_bill_no"
									value="${TranBillPaymentKey.bill_no }" /></td>
							<td class="d-none"><form:input
									path="TranBillPaymentList[${loopCounter.index}].sub_status"
									type="text" class="form-control readonly sub_status"
									object="TranBillPaymentList" key_name="" attr_name="sub_status"
									index="${loopCounter.index}"
									ref_name="${loopCounter.index}_sub_status"
									value="${sub_status}" /></td>

							<td class="d-none"><form:input
									path="TranBillPaymentList[${loopCounter.index}].rzr_order_id"
									type="text" class="form-control readonly rzr_order_id"
									object="TranBillPaymentList" key_name=""
									attr_name="rzr_order_id" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_rzr_order_id"
									value="${rzr_order_id}" /></td>
							<td class="d-none"><form:input
									path="TranBillPaymentList[${loopCounter.index}].rzr_order_status"
									type="text" class="form-control readonly"
									object="TranBillPaymentList" key_name=""
									attr_name="rzr_order_status" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_rzr_order_status"
									value="${rzr_order_status}" /></td>
							<td class="d-none"><form:input
									path="TranBillPaymentList[${loopCounter.index}].rzr_payment_id"
									type="text" class="form-control readonly"
									object="TranBillPaymentList" key_name=""
									attr_name="rzr_payment_id" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_rzr_payment_id"
									value="${rzr_payment_id}" /></td>
							<td class="d-none"><form:input
									path="TranBillPaymentList[${loopCounter.index}].rzr_sign"
									type="text" class="form-control readonly"
									object="TranBillPaymentList" key_name="" attr_name="rzr_sign"
									index="${loopCounter.index}"
									ref_name="${loopCounter.index}_rzr_sign" value="${rzr_sign}" /></td>

							<td class="d-none"><form:input
									path="TranBillPaymentList[${loopCounter.index}].TranBillPaymentKey.payment_id"
									type="text" class="form-control readonly"
									object="TranBillPaymentList" key_name="TranBillPaymentKey"
									attr_name="payment_id" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_payment_id"
									value="${TranBillPaymentKey.payment_id }" /></td>
							<td class="d-none"><form:input
									path="TranBillPaymentList[${loopCounter.index}].payment_date"
									type="text" class="form-control formatdate readonly"
									object="TranBillPaymentList" attr_name="payment_date"
									index="${loopCounter.index}"
									ref_name="${loopCounter.index}_payment_date"
									value="${payment_date }" /></td>

							<td style="width: 25px;" class="col-del-payment d-none"><a
								class="form-delete-row-payment"><img class="icon-sm"
									src="<%=request.getContextPath()%>/img/delete.png" /></a></td>
							<!-- hidden elements -->
							<td><form:select
									path="TranBillPaymentList[${loopCounter.index}].payment_mode"
									object="TranBillPaymentList" default_value="CH" key_name=""
									attr_name="payment_mode" type="text"
									ref_name="${loopCounter.index}_payment_mode"
									index="${loopCounter.index}" class="form-control payment_mode">
									<%=ReferenceUtils.buildOptions(session, "PAYMD", loPaymentRow.getPayment_mode(), true)%>
								</form:select></td>
							<td><form:select
									path="TranBillPaymentList[${loopCounter.index}].status"
									object="TranBillPaymentList" default_value="PN" key_name=""
									attr_name="status" type="text" index="${loopCounter.index}"
									value="PN" ref_name="${loopCounter.index}_status"
									class="form-control status">
									<%=ReferenceUtils.buildOptions(session, "PAYST", loPaymentRow.getStatus(), true)%>
								</form:select></td>
							<td><form:input
									path="TranBillPaymentList[${loopCounter.index}].remark"
									type="text" class="form-control" object="TranBillPaymentList"
									attr_name="remark" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_remark" value="${remark }" /></td>
							<td><form:input
									path="TranBillPaymentList[${loopCounter.index}].payment_amt"
									type="text" class="form-control amount text-danger payment_amt"
									object="TranBillPaymentList" attr_name="payment_amt"
									default_value="0" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_payment_amt"
									value="${payment_amt }" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="overflow-auto p-0 m-0" id="section-product">
			<table id="ProductTable" class="table" style="width: 100%;">
				<thead>
					<tr>
						<th class="d-none"></th>
						<th class="d-none"></th>
						<th class="d-none"></th>
						<th class="d-none"></th>
						<th class="d-none"></th>
						<th class="d-none"></th>
						<th class="d-none"></th>
						<th class="d-none"></th>
						<th style="width: 25px;" class="col-del-product d-none"></th>
						<th>Product Code</th>
						<th class="desc">Product Name</th>
						<th>MRP</th>
						<th>Selling Price</th>
						<th class="order_qty_col d-none">Ordered Qty</th>
						<th class="bill_qty_col">Bill Qty</th>
						<th class="returned_qty_col">Returned Qty</th>
						<th>Item Total</th>
						<th>Discount</th>
						<th>Bv</th>

					</tr>
				</thead>
				<tbody id="ProductTableTableBody">

					<c:forEach items="${TranBillDetailRows}" var="TranBillDetailR"
						varStatus="loopCounter">
						<%
							System.out.println("In Loop");
						%>
						<tr id="${loopCounter.index}" class="data_row_product"
							name="ProductTable${loopCounter.index}">
							<td class="d-none"><form:input
									path="TranBillDetailList[${loopCounter.index}].TranBillDetailKey.bill_no"
									type="text" class="bill_no amount readonly"
									object="TranBillDetailList" default_value="0"
									key_name="TranBillDetailKey" attr_name="bill_no"
									index="${loopCounter.index}"
									ref_name="${loopCounter.index}_bill_no"
									value="${TranBillDetailKey.bill_no}" /></td>

							<td class="d-none"><form:input
									path="TranBillDetailList[${loopCounter.index}].cess_percentage"
									type="text" class="lov" lov="PRODUCTS"
									object="TranBillDetailList" key_name=""
									attr_name="cess_percentage" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_cess_percentage"
									value="${cess_percentage }" /></td>
							<td class="d-none"><form:input
									path="TranBillDetailList[${loopCounter.index}].gst_percentage"
									type="text" class="lov" lov="PRODUCTS"
									object="TranBillDetailList" key_name=""
									attr_name="gst_percentage" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_gst_percentage"
									value="${gst_percentage }" /></td>
							<td class="d-none"><form:input
									path="TranBillDetailList[${loopCounter.index}].hsn_code"
									type="text" class="lov" lov="PRODUCTS"
									object="TranBillDetailList" key_name="" attr_name="hsn_code"
									index="${loopCounter.index}"
									ref_name="${loopCounter.index}_hsn_code" value="${hsn_code }" /></td>
							<td class="d-none"><form:input
									path="TranBillDetailList[${loopCounter.index}].landing_cost"
									type="text" class="lov" lov="PRODUCTS"
									object="TranBillDetailList" key_name=""
									attr_name="landing_cost" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_landing_cost"
									value="${landing_cost}" /></td>
							<td class="d-none"><form:input
									path="TranBillDetailList[${loopCounter.index}].disc_per"
									type="text" class="lov" lov="PRODUCTS"
									object="TranBillDetailList" key_name="" attr_name="disc_per"
									index="${loopCounter.index}"
									ref_name="${loopCounter.index}_disc_per" value="${disc_per}" /></td>
							<td class="d-none"><form:input
									path="TranBillDetailList[${loopCounter.index}].bv_per"
									type="text" class="lov" lov="PRODUCTS"
									object="TranBillDetailList" key_name="" attr_name="bv_per"
									index="${loopCounter.index}"
									ref_name="${loopCounter.index}_bv_per" value="${bv_per}" /></td>

							<td class="d-none"><form:input
									path="TranBillDetailList[${loopCounter.index}].TranBillDetailKey.vendor_code"
									type="text" class="lov" lov="PRODUCTS"
									object="TranBillDetailList" key_name="TranBillDetailKey"
									attr_name="vendor_code" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_vendor_code"
									value="${TranBillDetailKey.vendor_code }" /></td>

							<td style="width: 25px;" class="col-del-product d-none"><a
								class="form-delete-row-product"><img class="icon-sm"
									src="<%=request.getContextPath()%>/img/delete.png" /></a></td>


							<td><form:input
									path="TranBillDetailList[${loopCounter.index}].TranBillDetailKey.product_code"
									type="text" class="lov product_info_row readonly"
									lov="products" object="TranBillDetailList"
									key_name="TranBillDetailKey" attr_name="product_code"
									index="${loopCounter.index}"
									ref_name="${loopCounter.index}_product_code"
									value="${TranBillDetailKey.product_code }" /></td>
							<td class="desc"><form:input
									path="TranBillDetailList[${loopCounter.index}].product_name"
									type="text" class="form-control readonly"
									object="TranBillDetailList" attr_name="product_name"
									index="${loopCounter.index}"
									ref_name="${loopCounter.index}_product_name"
									value="${product_name}" /></td>
							<td><form:input
									path="TranBillDetailList[${loopCounter.index}].mrp" type="text"
									class="mrp amount readonly" object="TranBillDetailList"
									default_value="0" attr_name="mrp" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_mrp" value="${mrp}" /></td>
							<td><form:input
									path="TranBillDetailList[${loopCounter.index}].TranBillDetailKey.selling_price"
									type="text" class="selling_price amount readonly"
									object="TranBillDetailList" default_value="0"
									key_name="TranBillDetailKey" attr_name="selling_price"
									index="${loopCounter.index}"
									ref_name="${loopCounter.index}_selling_price"
									value="${TranBillDetailKey.selling_price}" /></td>
							<td class="order_qty_col d-none"><form:input
									path="TranBillDetailList[${loopCounter.index}].order_qty"
									type="text" class="order_qty_row readonly number"
									object="TranBillDetailList" index="${loopCounter.index}"
									attr_name="order_qty" value="${order_qty}"
									ref_name="${loopCounter.index}_order_qty" /></td>
							<td class="bill_qty_col"><form:input tabindex="-1"
									path="TranBillDetailList[${loopCounter.index}].bill_qty"
									type="text" class="number bill_qty" object="TranBillDetailList"
									index="${loopCounter.index}" attr_name="bill_qty"
									value="${bill_qty}" ref_name="${loopCounter.index}_bill_qty" /></td>
							<td class="returned_qty_col"><form:input tabindex="-1"
									path="TranBillDetailList[${loopCounter.index}].returned_qty"
									type="text" class="number returned_qty readonly"
									object="TranBillDetailList" index="${loopCounter.index}"
									attr_name="returned_qty" value="${returned_qty}"
									ref_name="${loopCounter.index}_returned_qty" /></td>
							<td><form:input
									path="TranBillDetailList[${loopCounter.index}].item_total"
									type="text" class="text-danger amount readonly item_total"
									object="TranBillDetailList" index="${loopCounter.index}"
									attr_name="item_total" value="${item_total}"
									ref_name="${loopCounter.index}_item_total" /></td>

							<td><form:input
									path="TranBillDetailList[${loopCounter.index}].discount"
									type="text" class="discount amount readonly d-none"
									object="TranBillDetailList" default_value="0"
									attr_name="discount" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_discount" value="${discount}" />
							<input
									id="TranBillDetailList[${loopCounter.index}].tot_discount"
									type="text" class="tot_discount amount readonly"
									object="TranBillDetailList" default_value="0"
									attr_name="tot_discount" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_tot_discount" value="" /></td>
							<td><form:input
									path="TranBillDetailList[${loopCounter.index}].bv" type="text"
									class="bv_amt number readonly d-none" object="TranBillDetailList"
									default_value="0" attr_name="bv" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_bv" value="${bv}" />
									<input
									id="TranBillDetailList[${loopCounter.index}].tot_bv" type="text"
									class="tot_bv_amt number readonly" object="TranBillDetailList"
									default_value="0" attr_name="tot_bv" index="${loopCounter.index}"
									ref_name="${loopCounter.index}_tot_bv" value="" />
									</td>



						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</form:form>

	<%@ include file="../base/jsp/lov.jsp"%>

</body>
<%@ include file="../common/footer.jsp"%>