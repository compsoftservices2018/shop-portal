<%@ include file="../common/header.jsp"%>
<%@ page import="com.compsoft.shop.model.TranOrder"%>

<title><%=session.getAttribute("TITLE")%></title>


<script>
	$(document).ready(function() {
		/* $('#ListTable').DataTable({
			"searching" : true,
			"paging" : true,
			"info" : true,
			scrollY : 'calc(100vh - 350px)',
			scrollX : true
		});
		 */
		$('#ListTable').DataTable({
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

	<%-- $(document).on('click', '.form-new', function() {
		$("#ProductEntryForm").trigger("reset");
		$("#page_mode").val("N");
	});

	$(document).on('click', '.saveproduct', function() {
		showProgress("Saving product information");
		var form_data = new FormData($('#ProductEntryForm')[0]);
		$.ajax({
			type : 'POST',
			url : 'SaveProduct',
			processData : false,
			contentType : false,
			async : false,
			cache : false,
			data : form_data,
			success : function(responseText) {
				hideProgress();
				if (responseText.response_status == 1) {
					showSuccess(responseText.response_success);
				} else {
					showError(responseText.response_error);
				}
			}
		});
	});

	$(document).on('click', '.proddetailsadmin', function() {
		showProgress("Getting product information.");
		var element = $(this).attr("");
		$.get('GetProductInfo', {
			requestData : JSON.stringify({
				product_code : $(this).attr('product_code'),
				vendor_code : $(this).attr('vendor_code'),
				selling_price : $(this).attr('selling_price')
			})
		}, function(responseText) {
			hideProgress();
			if (responseText.response_status == 1) {
				console.log(responseText);
				$.each(responseText, function(key, value) {
					$("#ProductEntryForm #" + key.toLowerCase()).val(value);
				});
				$("#ProductEntryForm .product_code").val(responseText.PRODUCT_CODE);
				$("#ProductEntryForm .vendor_code").val(responseText.VENDOR_CODE);
				$("#ProductEntryForm .selling_price").val(responseText.SELLING_PRICE);
				$("#prod_image_admin").attr("src", "<%=request.getContextPath()%>/app<%=msProductImg%>/" + responseText.IMAGE_NAME + ".jpg");
				$("#ProductEntryForm #page_mode").val("U");
			}
		});
	});

	$(document).on('change', '#imagefile', function(e) {
		showProgress("Uploading files.")
		var form_data = new FormData($('#FormUploadImage')[0]);
		$.ajax({
			type : 'POST',
			url : 'UploadImage',
			processData : false,
			contentType : false,
			async : false,
			cache : false,
			data : form_data,
			success : function(responseText) {
				hideProgress();
				if (responseText.response_status == "success") {
					$("#prod_image_admin").attr("src", "<%=request.getContextPath()%>/app<%=msProductImg%>/" + responseText.image_name + ".jpg");
					$("#ProductEntryForm #image_name").val(responseText.image_name);
					showSuccess(responseText.response_success);
				} else {
					showError(responseText.response_error);
				}
			}
		});
	});
 --%>

	$(document).on('click', '#exportproduct', function() {
		showProgress("Exporting product list.");
		$.get('ExportProduct', {
			data : JSON.stringify({})
		}, function(responseText) {
			hideProgress();
			if (responseText.response_status == 1) {
				window.open('<%=request.getContextPath()%>/app/<%=GlobalValues.getCompanyCode(session)%>/' + responseText.file, '_self');
			} else {
				showError(responseText.response_error);
			}
		});
	});

	$(document).on('click', '.deleteproduct', function() {
		showProgress("Deleting product");
		var element = $(this).attr("");
		$.get('DeleteProducts', {
			requestData : JSON.stringify({})
		}, function(responseText) {
			hideProgress();
			if (responseText.response_status == 1) {
				// $("#MainFormSearchProduct").submit();
				showSuccess(responseText.response_success);
			} else {
				showError(responseText.response_error);
			}

		});

	});

	$(document).on('click', '.btnUploadProduct', function(e) {
		e.preventDefault();
		 $("#modalConfirmYesNo").modal("hide");
		showProgress("Uploding products..");
		var form_data = new FormData($('#FormUpload')[0]);
		$.ajax({
			type : 'POST',
			url : 'UploadProducts',
			processData : false,
			contentType : false,
			async : false,
			cache : false,
			data : form_data,
			success : function(responseText) {
				hideProgress();
				if (responseText.response_status == 1) {
					showSuccess(responseText.response_success);
					$("#processlog").html(responseText.log);
				} else {
					showError(responseText.response_error);
				}
			}
		});
		$('#modalConfirmYesNo').modal('hide');
	});
</script>

<body>
	<div class="">
		<%-- <div class="col-sm-4 bg-light d-none"
			style="position: relative; overflow: auto; width: 100%; height: calc(100vh - 70px);">
			<fieldset class="mt-1">
				<legend>Product Details</legend>
				<div class="row m-0">
					<div class="col p-0">
						<a class="form-new btn btn-info btn-sm btn-block">New</a>
					</div>
					<div class="col p-0">
						<a
							class="saveproduct btn btn-info btn-sm btn-block btn-default-action">Save</a>
					</div>
				</div>
				<form:form id="ProductEntryForm" class="form"
					modelAttribute="MstProduct" method="post">
					<form:input path="page_mode" type="hidden" value="${page_mode}" />
					<div>
						<label class="col-form-label-sm">Image Name:</label>
						<div class="input-group-prepend input-group-sm	">
							<input id="image_name" name="image_name" type="text"
								class="input-group-prepend uppercase form-control form-control-sm"
								value="" placeholder="Required"> <a
								class="input-group-append btn btn-info btn-sm text-nowrap"
								onclick="$('#imagefile').click();" id="btnUploadImageDummy">Upload
								Image</a>
						</div>
						<a id="btnUploadImage" class="hide"></a> <img
							class="img-responsive" id="prod_image_admin"
							onerror="this.src='<%=request.getContextPath()%>/img/noimage.png?ver=1.03'"
							style="height: 116px; border-radius: 10px; margin-top: 5px;">
					</div>
					<div class="form-group form-group-sm">
						<label class="col-form-label-sm">Code:</label>
						<form:input path="MstProductKey.product_code" type="text"
							class="form-control form-control-sm product_code"
							value="${MstProductKey.product_code}" />
						<form:input path="MstProductKey.vendor_code" type="hidden"
							class="form-control form-control-sm vendor_code"
							value="${MstProductKey.vendor_code}" />

						<label class="col-form-label-sm">Name:</label>
						<form:input path="product_name" type="text"
							class="uppercase form-control form-control-sm" value="" />
						<label class="col-form-label-sm">Additional Information:</label>
						<form:input path="add_information" type="text"
							class="form-control form-control-sm" value="" />
						<label class="col-form-label-sm">Category:</label>
						<form:input path="group_name" type="text"
							class="uppercase form-control form-control-sm"
							value="${group_name}" />
						<label class="col-form-label-sm">Start Date:</label>
						<form:input path="start_date" type="text"
							class="form-control formatdate form-control-sm"
							value="${start_date}" />
						<label class="col-form-label-sm">End Date:</label>
						<form:input path="end_date" type="text"
							class="form-control formatdate form-control-sm"
							value="${end_date}" />
						<label class="col-form-label-sm">MRP:</label>
						<form:input path="mrp" type="number"
							class="form-control amount form-control-sm" value="${mrp}" />
						<label class="col-form-label-sm">Landing Cost:</label>
						<form:input path="landing_cost" type="number" required="true"
							class="form-control amount form-control-sm"
							value="${landing_cost}" />
						<label class="col-form-label-sm">Discount%:</label>
						<form:input path="disc_per" type="number" required="true"
							class="form-control amount form-control-sm" value="${disc_per}" />
						<label class="col-form-label-sm">BV%:</label>
						<form:input path="bv_per" type="number" required="true"
							class="form-control amount form-control-sm" value="${bv_per}" />
						<label class="col-form-label-sm">Discount:</label>
						<form:input path="discount" type="number" required="true"
							class="form-control amount form-control-sm" value="${discount}" />
						<label class="col-form-label-sm">BV:</label>
						<form:input path="bv" type="number" required="true"
							class="form-control amount form-control-sm" value="${bv}" />
						<label class="col-form-label-sm">Selling Price:</label>
						<form:input path="MstProductKey.selling_price" type="number"
							class="form-control amount form-control-sm selling_price"
							value="${MstProductKey.selling_price}" />
						<label class="col-form-label-sm">HSN Code:</label>
						<form:input path="hsn_code" type="number"
							class="form-control form-control-sm" value="${hsn_code}" />
						<label class="col-form-label-sm">Stock:</label>
						<form:input path="stock" type="number"
							class="form-control number form-control-sm" id="stock"
							value="${stock}" />
						<label class="col-form-label-sm">GST%:</label>
						<form:input path="gst_percentage" type="number"
							class="form-control form-control-sm amount" id="gst_percentage"
							value="${gst_percentage}" />
						<label class="col-form-label-sm">CESS%:</label>
						<form:input path="cess_percentage" type="number"
							class="form-control form-control-sm amount" id="cess_percentage"
							value="${cess_percentage}" />
						<label class="col-form-label-sm">Home Page Position:</label>
						<form:select path="home_page_view"
							class="form-control  form-control-sm">
							<%=ReferenceUtils.buildOptions(session, "PLOCN", null, true)%>
						</form:select>
					</div>
				</form:form>
			</fieldset>

		</div> --%>
		<div>
			<fieldset class="mt-1">
				<legend>Data Processing</legend>
				<div class="row m-0 p-1">
					<div class="col-sm-8 row m-0 p-1">
						<div class="col-sm p-1">
							<form id="FormUpload" style="display: inline-table; width: 100%"
								class="nopad form-horizontal" enctype="multipart/form-data">
								<div style="padding: 0px; margin: 0px;"
									class="form-group form-group-sm">
									<input class="form-control" type="file" id="file" name="file"
										style="padding: 0px; height: 33px; font-size: 17px; margin: 0px;">
								</div>
							</form>
						</div>
						<div class="col-sm-2 p-1">
							<a class="btn btn-info btn-sm"
								onclick="ConfirmYesNo('Confirmation!!!', 'Are you sure you want to upload product data? Please wait till upload finishes after clicking on Yes', 'btnUploadProduct');"
								style="" id="btnUploadProductDummy">Upload Product File</a>
						</div>
						<div class="col-sm-2 p-1">
							<a data-toggle="collapse" href="#processlogdata"
								class="btn btn-info btn-sm">Process Logs</a><b class="caret"></b>
						</div>

						<div class="col-sm-2 p-1">
							<form id="MainFormSearchProduct" method="get"
								action="GetProducts">
								<button accesskey="s" type="submit" class="btn btn-info btn-sm"
									id="searchproducts">Refresh/Search</button>
							</form>
						</div>

					</div>

					<div class="col-sm text-nowrap text-right p-2">
						<a id="exportproduct" class="btn btn-info btn-sm">Download
							Product List</a> <a
							onclick="ConfirmYesNo('Confirmation!!!', 'Are you sure you want to delete cart?', 'deleteproduct');"
							class="btn btn-info btn-sm">Delete All Products </a> <a
							id="btnUploadImage" class="hide"></a>
					</div>
				</div>
			</fieldset>
		</div>
		<div id="processlogdata" class="mt-1 p-1 panel-collapse collapse">
			<fieldset>
				<legend>Process Log</legend>
				<div class="" id="processlog"></div>
			</fieldset>
		</div>
		<div>
			<fieldset class="mt-1 p-1">
				<legend>Product List</legend>
				<div class="overflow-auto">
					<table id="ListTable" class="table" style="width: 100%;">
						<thead>
							<tr>
								<th>Product Code</th>
								<th>Product Name</th>
								<th>Bar Code</th>
								<th>Image Name</th>
								<th>Group Name</th>
								<th>GST Percentage</th>
								<th>Cess Percentage</th>
								<th>HSN Code</th>
								<th>Discount%</th>
								<th>BV%</th>
								<th>Landing Cost%</th>
								<th>MRP</th>
								<th>Discount</th>
								<th>BV</th>
								<th>Selling Price</th>
								<th>Start date</th>
								<th>End Date</th>
								<th>Home Page View</th>
								<th>Additional Info</th>
							</tr>
						</thead>
						<tbody>
							<%
								List<Map<String, Object>> loProductList = (List<Map<String, Object>>) request.getAttribute("AllProducts");
								if (loProductList != null) {
									for (Map<String, Object> loProductRow : loProductList) {
							%>
							<tr
								class="<%=loProductRow.get("active") != null && ((String) loProductRow.get("active")).equals("I")
							? "text-line-through" : ""%>">
								<td>
									<%-- <button class="p-0 btn btn-link proddetailsadmin"
									type="submit"
									product_code="<%=(String) loProductRow.get("product_code")%>"
									vendor_code="<%=(String) loProductRow.get("vendor_code")%>"
									selling_price="<%=(BigDecimal) loProductRow.get("selling_price")%>"><%=(String) loProductRow.get("product_code")%></button> --%>
									<%=(String) loProductRow.get("product_code")%>
								</td>

								<td><%=(String) loProductRow.get("product_name")%></td>
								<td><%=AppUtils.convertNullToEmpty((String) loProductRow.get("alt_product_code"))%></td>
								<td><%=AppUtils.convertNullToEmpty((String) loProductRow.get("image_name"))%></td>
								<td><%=(String) loProductRow.get("group_name")%></td>
								<td class="text-right"><%=AppUtils.convertNullToEmpty(((BigDecimal) loProductRow.get("gst_percentage")).setScale(2))%></td>
								<td class="text-right"><%=AppUtils
							.convertNullToEmpty(((BigDecimal) loProductRow.get("cess_percentage")).setScale(2))%></td>
								<td><%=AppUtils.convertNullToEmpty((String) loProductRow.get("hsn_code"))%></td>
								<td class="text-right"><%=AppUtils.convertNullToEmpty(((BigDecimal) loProductRow.get("disc_per")).setScale(2))%></td>
								<td class="text-right"><%=AppUtils.convertNullToEmpty(((BigDecimal) loProductRow.get("bv_per")).setScale(2))%></td>
								<td class="text-right"><%=AppUtils.convertNullToEmpty(((BigDecimal) loProductRow.get("landing_cost")).setScale(2))%></td>
								<td class="text-right"><%=AppUtils.convertNullToEmpty(((BigDecimal) loProductRow.get("mrp")).setScale(2))%></td>
								<td class="text-right"><%=AppUtils.convertNullToEmpty(((BigDecimal) loProductRow.get("discount")).setScale(2))%></td>
								<td class="text-right"><%=AppUtils.convertNullToEmpty(((BigDecimal) loProductRow.get("bv")).setScale(2))%></td>
								<td class="text-right"><%=AppUtils.convertNullToEmpty(((BigDecimal) loProductRow.get("selling_price")).setScale(2))%></td>
								<td><%=AppUtils.getFormattedDate((Timestamp) loProductRow.get("start_date"))%></td>
								<td><%=AppUtils.getFormattedDate((Timestamp) loProductRow.get("end_date"))%></td>
								<td><%=AppUtils.convertNullToEmpty((String) loProductRow.get("add_information"))%></td>
								<td><%=AppUtils.convertNullToEmpty((String) loProductRow.get("home_page_view"))%></td>
							</tr>
							<%
								}
								}
							%>

						</tbody>
					</table>
				</div>
			</fieldset>
		</div>
	</div>

	<form id="FormUploadImage" style="display: none;"
		class="p-1 form-horizontal" enctype="multipart/form-data">
		<div style="padding: 0px; margin: 0px;"
			class="form-group form-group-sm">
			<input class="form-control" type="file" id="imagefile"
				name="imagefile"
				style="padding: 0px; height: 33px; font-size: 17px; margin: 0px;">
		</div>
	</form>
</body>
<%@ include file="../common/footer.jsp"%>
