<%@ include file="../common/header.jsp"%>
<%@ page import="com.compsoft.shop.model.TranOrder"%>
<style>
div.dataTables_wrapper div.dataTables_filter input {
	width: 150px;
}
</style>

<script>
	$(document).ready(function() {
	
		$('#orders').DataTable({
			dom : 'Blfrtip', 
			 buttons : [ 'excelHtml5' ], 
			/* scrollY : 'calc(100vh - 300px)', */
			"scrollX" : true,
			paging : true,
			"info" : true,
			/* responsive : true, */
			"pageLength" : 10
		});
	});

	


$(document).on('click', '.startbill', function() {
	
	showProgress("Generating bill.");
	var thisElement = $(this).parent().parent();
	$.post('GenerateBill', {
		requestData : JSON.stringify({
			order_no : $(this).attr('order_no'),
		})
	}, function(responseText) {
		hideProgress();
		console.log(responseText);
		if (responseText.response_status == 1) {
			//console.log(thisElement)
			thisElement.find(".lookup_bill_no").val(responseText.bill_no);
			thisElement.find(".btn_lookup_bill_no").html(responseText.bill_no);
			thisElement.find(".status").html(responseText.order_status);
					
			$('#orders').DataTable().destroy();
			$('#orders').DataTable({
				dom : 'Blfrtip', 
				 buttons : [ 'excelHtml5' ], 
				/* scrollY : 'calc(100vh - 300px)', */
				"scrollX" : true,
				paging : true,
				"info" : true,
				/* responsive : true, */
				"pageLength" : 10
			});
		} else {
			$("#modalInformationError").html(responseText.response_status);
			$("#modalInformation").modal("show");
		}
	});
});

	
	
	$(document).on('change', '.status', function() {
		showProgress("Updating order status.");
		var element = $(this).attr("status_attr");
		$.get('UpdateOrderStatus', {
			order_no : $(this).attr('order_no'),
			status : $(this).val()
		}, function(responseText) {
			hideProgress();
			if (responseText.status == "success") {
				$("#" + element).html("Status Changed to " + responseText.neworderstatus);
				//$("#modalInformationError").html(responseText.message);
				$('#orders').DataTable().destroy();
				$('#orders').DataTable({
					dom : 'Blfrtip',
					buttons : [ 'excelHtml5' ]
					/* scrollY : 'calc(100vh - 300px)',
					scrollX : true,
					"paging" : true,
					"info" : true,
					"responsive" : true,
					"pageLength" : 10 */
				});
			} else {
				$("#modalInformationError").html(responseText.error);
				$("#modalInformation").show();
			}

		});
	});



	$(document).on('click', '.processall', function() {
		//hideProgress();
		var loNodeTable = document.getElementById("OrderTableBody");

		if ($('#status').val() != "SM") {
			$("#modalInformationError").html("Status should be submitted");
			$("#modalInformation").show();
			return;
		}

		for (var i = 0, row; row = loNodeTable.rows[i]; i++) {
			showProgress("Updating order status");
			$.get('UpdateOrderStatus', {
				order_no : loNodeTable.rows[i].id,
				status : "PR"
			}, function(responseText) {
				hideProgress();
				if (responseText.status == "success") {
				} else {
					$("#modalInformationError").html(responseText.error);
					$("#modalInformation").show();
				}
			});
		}
		$("#FormSearchAdminPage").submit();
	});

	$(document).on('click', '.closeall', function() {
		//hideProgress();
		var loNodeTable = document.getElementById("OrderTableBody");
		for (var i = 0, row; row = loNodeTable.rows[i]; i++) {
			showProgress("Updating order status.");
			$.get('UpdateOrderStatus', {
				order_no : loNodeTable.rows[i].id,
				status : "CL"
			}, function(responseText) {
				hideProgress();
				if (responseText.status == "success") {
				} else {
					$("#modalInformationError").html(responseText.error);
					$("#modalInformation").show();
				}
			});
		}
		$("#FormSearchAdminPage").submit();
	});


	$(document).on('click', '.exportorder', function() {
		showProgress("Exporting order data.");
		//var group_code = $(this).attr("group_code");
		$.get('ExportOrder', {
			data : JSON.stringify(removeEmptyOrNull({
				"group_code" : $("#FormSearchAdminPage #group_code").val(),
				"distribution_month" : $("#FormSearchAdminPage #distribution_month").val(),
				"distribution_cycle" : $("#FormSearchAdminPage #distribution_cycle").val(),
				"order_date" : $("#FormSearchAdminPage #order_date").val(),
				"order_to_date" : $("#FormSearchAdminPage #order_to_date").val(),
				"status" : $("#FormSearchAdminPage #status").val()
			}))
		}, function(responseText) {
			hideProgress();

			if (responseText.status == 1) {
				window.open('<%=request.getContextPath()%>/app/<%=GlobalValues.getCompanyCode(session)%>/' + responseText.file, '_self');
			} else {
				$("#modalInformationError").html(responseText.log);
				$("#modalInformation").show();
			}

		});

	});
</script>

<%
	TranOrder loTranOrder = (TranOrder) request.getAttribute("TranOrder");
	String lsStatusSearch = loTranOrder.getStatus();
%>

<title><%=session.getAttribute("TITLE")%></title>

<body>
	<!-- <div class="col-md-2 p-1"></div> -->



	<fieldset class="mt-1 p-1">
		<legend>Search orders</legend>
		
<form:form id="FormSearchAdminPage" class="form-horizontal"
	method="post" modelAttribute="TranOrder" action="SearchOrders">
		<div class="form-row m-0 p-1">
			<div class="pr-1">
				<label>From:</label>
				<form:input path="order_date" id="order_date" name="order_date"
					type="text" class="form-control  formatdatecalendar"
					value="${order_date}" />
			</div>
			<div class="pr-1">
				<label>To:</label>	
				<form:input path="order_to_date" id="order_date" name="order_date"
					type="text" class="form-control  formatdatecalendar"
					value="${order_date}" />
			</div>
			<div class="pr-1">
				<label>Status:</label>
				<form:select path="status" type="text" class="form-control ">
					<%=ReferenceUtils.buildOptions(session, AppConstants.GROUP_ORDER_STATUS, lsStatusSearch,
							true)%>
				</form:select>
			</div>
			<div class="pr-1 align-self-end">
				<button class="btn btn-info btn-sm" type="submit">Search</button>
			</div>
	</div>
</form:form>
	</fieldset>
	<fieldset class="mt-1 p-1">
		<legend>Orders</legend>
		<div class="overflow-auto">
			<table class="table table-striped" id="orders" style="width: 100%;">
				<thead class="bg-light">
					<tr>
						<th>Order#</th>
						<th>Bill#</th>
						<th>Name</th>
						<th>Date</th>
						<th>Order Status</th>
						<th>Payment Mode</th>
						<th>Payment Status</th>
						<th class="text-right">Amount</th>
						<th></th>
					</tr>
				</thead>
				<tbody>

					<%
						List<Map<String, Object>> loOrders = (List<Map<String, Object>>) request.getAttribute("OrderList");
						if (loOrders != null) {
							int liRow = 0;
							for (Map<String, Object> loOrderList : loOrders) {
								liRow++;
								String lsCompanyCode = (String) loOrderList.get("company_code");
								String lsCustomerCode = (String) loOrderList.get("customer_code");
								String lsCustomerName = (String) loOrderList.get("customer_name");
								String lsPaymentMode = ReferenceUtils.getOptionValue(session, AppConstants.GROUP_PAYMENT_MODE,
										(String) loOrderList.get("payment_mode"));
								String lsPaymentStatus = ReferenceUtils.getOptionValue(session, AppConstants.GROUP_PAYMENT_STATUS,
										(String) loOrderList.get("payment_status"));
								String lsOrderNo = (String) loOrderList.get("order_no");
								String lsBillNo = AppUtils.convertNullToEmpty((String) loOrderList.get("bill_no"));
								String lsStatus = ReferenceUtils.getOptionValue(session, AppConstants.GROUP_ORDER_STATUS,
										(String) loOrderList.get("status"));
								String lsOrderDate = AppUtils.getFormattedDate((Timestamp) loOrderList.get("order_date"));
								String lsOrderAmt = "Subtotal(" + loOrderList.get("TOT_ORDER_QTY") + " Items) : "
										+ ((BigDecimal) loOrderList.get("TOT_SELLING_PRICE")).setScale(2);
					%>

					<tr>
						<td>
							<form id="OrderDetail" class="p-0 text-center" method="post" action="OrderDetailBill">
								<input id="order_no" name="order_no" type="hidden"
									value=<%=(String) loOrderList.get("order_no")%> />
								<button type="submit" class="btn btn-link p-1"><%=lsOrderNo%></button>
							</form>
						</td>
						<td>
							<form id="form-get" method="POST" class="p-0 text-center" action="GetBill">
								<input id="lookup_bill_no" name="lookup_bill_no" type="hidden"
									 class="lookup_bill_no"
									value=<%=(String) loOrderList.get("bill_no")%> />
								<button type="submit" id="btn_lookup_bill_no"
									class="btn p-1 btn-link btn_lookup_bill_no"><%=lsBillNo%></button>
							</form>
						</td>

						<td class="text-nowrap"><%=lsCustomerName%></td>
						<td class="text-nowrap"><%=lsOrderDate%></td>
						<td class="text-nowrap status" id="status<%=liRow%>"><%=lsStatus%></td>
						<td class="text-nowrap"><%=lsPaymentMode%></td>
						<td class="text-nowrap"><%=lsPaymentStatus%></td>
						<td class="text-right text-danger text-nowrap"><%=lsOrderAmt%></td>
						<%
							if (((String) loOrderList.get("status")).equals(AppConstants.STATUS_ORDER_SUBMITTED)) {
						%>
						<td class="text-nowrap pl-1">
							<button order_no="<%=lsOrderNo%>" status="CA"
								customer_code="<%=lsCustomerCode%>"
								attr_status="status<%=liRow%>"
								class="updateorderstatus btn btn-outline-info btn-sm font-xs">Cancel</button>
							<button order_no="<%=lsOrderNo%>"
								customer_code="<%=lsCustomerCode%>"
								class="ml-1 startbill btn btn-outline-info btn-sm font-xs">Start
								Billing</button>
						</td>
						<%
							} else {
						%>
						<td class="readonly"></td>
						<%
							}
						%>
					</tr>
					<%
						}
						}
					%>
				</tbody>
			</table>
		</div>
	</fieldset>
	<%-- <fieldset class="p-1">
    <legend class="h5 text-underline">Orders</legend>
		<%
			List<Map<String, Object>> loOrders = (List<Map<String, Object>>) request.getAttribute("OrderList");
			if (loOrders != null) {
				int liRow = 0;
				for (Map<String, Object> loOrderList : loOrders) {
					liRow++;
					String lsCompanyCode = (String) loOrderList.get("company_code");
					String lsCustomerCode = (String) loOrderList.get("customer_code");
					String lsCustomerName = (String) loOrderList.get("customer_name");
					String lsPaymentMode = ReferenceUtils.getOptionValue(session, AppConstants.GROUP_PAYMENT_MODE,(String) loOrderList.get("payment_mode"));
					String lsPaymentStatus = ReferenceUtils.getOptionValue(session, AppConstants.GROUP_PAYMENT_STATUS,(String) loOrderList.get("payment_status"));
								String lsOrderNo = (String) loOrderList.get("order_no");
					String lsStatus = (String) loOrderList.get("status");
					String lsOrderDate = AppUtils.getFormattedDate((Timestamp) loOrderList.get("order_date"));
					String lsOrderDistributionMonth = AppUtils
							.getFormattedDateTime((Timestamp) loOrderList.get("distribution_month"));
		%>
		<div class="font-sm">
			<div class="row shadow-sm m-0 rounded">
				<div class="row col-sm m-0 p-0">
					<div class="col-sm col p-1">
						<img class="icon-sm rounded img-fluid"
							src="<%=request.getContextPath()%>/app<%=msProductImg%>/<%=loOrderList.get("FIRST_PRODUCT")%>.jpg"
							onerror="this.src='<%=request.getContextPath()%>/img/noimage.png?ver=1.03'">
					</div>
					<div class="col-sm col p-1  text-right text-nowrap">
						<form id="OrderDetail" class="" method="get" action="OrderDetail">
							<input id="were23wer" name="were23wer" type="hidden"
								value=<%=Security.encrypt((String) loOrderList.get("order_no"))%> />
							<button type="submit" class="btn text-info btn-sm  link"
								style="font-weight: normal; padding: 0px 10px 0px 10px;"><%="Order#: " + lsOrderNo%></button>
						</form>
					</div>

					<div class="col-sm col p-1  text-right text-nowrap">
						<%="Date: " + lsOrderDate%>
					</div>
					
					<div class="col-sm col p-1  text-right text-nowrap">
						Status: <span id="status<%=liRow%>"><%="Status: " + ReferenceUtils.getOptionValue(session, AppConstants.GROUP_ORDER_STATUS, lsStatus)%></span>
					</div>
					
					<div class="col-sm col p-1 text-danger text-right text-nowrap">
						<%="Subtotal(" + loOrderList.get("TOT_ORDER_QTY") + " Items) : "
							+ ((BigDecimal) loOrderList.get("TOT_SELLING_PRICE")).setScale(2)%>
					</div>
					<div class="col-sm p-1">
						<%
							if (lsStatus.equals(AppConstants.STATUS_ORDER_SUBMITTED)) {
						%>
						<div class="row m-0 ">
							<div class="col-12 p=0 d-inline-flex">
								<button order_no="<%=lsOrderNo%>" status="CA" 
									customer_code="<%=lsCustomerCode%>"
									attr_status="status<%=liRow%>"
									class="updateorderstatus btn btn-outline-info btn-sm font-xs">Cancel</button>
								<button order_no="<%=lsOrderNo%>" customer_code="<%=lsCustomerCode%>"
									class="ml-1 startbill btn btn-outline-info btn-sm font-xs">Start Billing</button>
							</div>
						</div>
						<%
							}
						%>
					</div>
				</div>
			</div>
		</div>
		<%
			}
			}
		%>
	</fieldset> --%>
	<%-- <hr>
	<fieldset class="p-1">
		<legend class="h5 text-underline">Delivery setup</legend>
		<%@ include file="../Snippet/Snippet_Delivery.jsp"%>
	</fieldset> --%>

</body>
<%@ include file="../common/footer.jsp"%>
