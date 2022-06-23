<%@ include file="../common/header.jsp"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="com.compsoft.shop.model.TranBill"%>
<style>
div.dataTables_wrapper div.dataTables_filter input {
	width: 150px;
}
</style>

<script>
	$(document).ready(function() {

		$('#bills').DataTable({
			dom : 'Blfrtip',
			buttons : [ 'excelHtml5' ],
			/* scrollY : 'calc(100vh - 300px)', */
			"scrollX" : true,
			paging : true,
			"info" : true,
			/* responsive : true, */
			"pageLength" : 10
		});
		setMaskFix();
	});


	$(document).on('change', '.updatebillheader', function(e) {
		showProgress("Updating..");
		var parentRow = $(this).parent().parent();
		console.log(parentRow);

		$.post('UpdateBillHeader', {
			requestData : JSON.stringify({
				value : $(this).val(),
				attr_name : $(this).attr("attr_name"),
				bill_no : $(this).attr("bill_no")
			})
		}, function(responseText) {
			hideProgress();
			if (responseText.response_status == 0) {
				showError(response_error);
			}

		});

	});

	$(document).on('click', '.updatebillheaderonclick', function(e) {
		showProgress("Updating..");
		var parentRow = $(this).parent().parent();
		console.log(parentRow);

		$.post('UpdateBillHeader', {
			requestData : JSON.stringify({
				value : $(this).val(),
				attr_name : $(this).attr("attr_name"),
				bill_no : $(this).attr("bill_no")
			})
		}, function(responseText) {
			hideProgress();
			if (responseText.response_status == 0) {
				showError(response_error);
			}

		});

	});
</script>


<%
	TranBill loTranBill = (TranBill) request.getAttribute("TranBill");
	String lsStatusSearch = loTranBill.getStatus();
%>

<title><%=session.getAttribute("TITLE")%></title>

<body>



	<fieldset class="mt-1 p-1">
		<legend>Search Bills</legend>
		<form:form id="FormSearchBills" class="form-horizontal" method="post"
			modelAttribute="TranBill" action="SearchBills">
			<div class="form-row m-0 p-1">
				<div class="pr-1">
					<label>From:</label>
					<form:input path="bill_date" type="text"
						class="form-control  formatdatecalendar"
						value="${bill_date}" />
				</div>
				<div class="pr-1">
					<label>To:</label>
					<form:input path="bill_to_date" type="text"
						class="form-control  formatdatecalendar"
						value="${bill_to_date}" />
				</div>
				<div class="pr-1">
					<label>Status:</label>
					<form:select path="status" class="form-control ">
						<%=ReferenceUtils.buildOptions(session, AppConstants.GROUP_BILL_STATUS, lsStatusSearch, true)%>
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
			<table class="table table-striped" id="bills" style="width: 100%;">
				<thead class="bg-light">
					<tr>
						<th class="d-none"></th>
						<th>Bill#</th>
						<th>Order#</th>
						<th>Code</th>
						<th>Name</th>
						<th>Date</th>
						<th>Bill Amt</th>
						<th>Status</th>
						<th>Assign Delivery To</th>
						<th>Scheduled Delivery Date</th>
						<th></th>

					</tr>
				</thead>
				<tbody>
					<%
						List<Map<String, Object>> loBillList = (List<Map<String, Object>>) request.getAttribute("BillList");
						if (loBillList != null) {
							int liCount = 0;
							int liCol = 0;
							for (Map<String, Object> row : loBillList) {
					%>
					<tr>
						<td class="d-none"><input id="bill_no"
							class="bill_no form-control formatdate" value="${bill_no }" /></td>
						<td>
							<form id="form-get" method="POST" class="p-0 text-center"
								action="GetBill">
								<input id="lookup_bill_no" name="lookup_bill_no" type="hidden"
									class="lookup_bill_no" value=<%=(String) row.get("bill_no")%> />
								<button type="submit" id="btn_lookup_bill_no"
									class="btn p-1 btn-link btn_lookup_bill_no"><%=(String) row.get("bill_no")%></button>
							</form>
						</td>
						<td>
							<form id="OrderDetail" class="p-0 text-center" method="POST"
								action="OrderDetailBill">
								<input id="order_no" name="order_no" type="hidden"
									value=<%=Security.encrypt((String) row.get("order_no"))%> />
								<button type="submit" class="btn btn-link p-1"><%=AppUtils.convertNullToEmpty((String) row.get("order_no"))%></button>
							</form>
						</td>
						<td><%=(String) row.get("customer_code")%></td>
						<td><%=(String) row.get("customer_name")%></td>
						<td><%=AppUtils.getFormattedDate((Timestamp) row.get("bill_date"))%></td>
						<td class="text-right"><%=(((BigDecimal) row.get("mrp")).subtract((BigDecimal) row.get("discount"))).setScale(2)%></td>
						<td><%=ReferenceUtils.getOptionValue(session, AppConstants.GROUP_BILL_STATUS,
							(String) row.get("status"))%></td>

						<%
							if (((String) row.get("status")).equals(AppConstants.STATUS_BILL_SUBMITTED)) {
						%>
						<td><input id="delivered_by" type="text" attr_name="delivered_by"
							bill_no=<%=(String) row.get("bill_no")%>
							class="updatebillheader form-control" value="<%=AppUtils.convertNullToEmpty((String) row.get("delivered_by")) %>" /></td>
						<td><input id="scheduled_delivery_date"
							bill_no=<%=(String) row.get("bill_no")%>
							attr_name="scheduled_delivery_date"
							class="updatebillheader form-control formatdatecalendar"
							value="<%=AppUtils.getFormattedDate((Timestamp) row.get("scheduled_delivery_date"))%>" /></td>
						<td>
							<button bill_no=<%=(String) row.get("bill_no")%> value="DR"
								attr_name="status"
								class="updatebillheaderonclick btn btn-outline-info btn-sm font-xs">Re-Open</button>
							<button bill_no=<%=(String) row.get("bill_no")%> value="CA"
								attr_name="status"
								class="updatebillheaderonclick btn btn-outline-info btn-sm font-xs">Cancel</button>

						</td>
						<%
							} else {
						%>
						<td class="readonly"></td>
						<td class="readonly"></td>
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

</body>
<%@ include file="../common/footer.jsp"%>
