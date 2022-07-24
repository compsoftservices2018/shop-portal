<%@ include file="../common/header.jsp"%>

<title><%=session.getAttribute("TITLE")%></title>


<script>
	$(document).ready(function() {
		showProgress("");
		$('#ListTable').DataTable({
			"paging" : false,
			"info" : false,
			scrollY : 'calc(100vh - 450px)',
			scrollX : true
		});
	});

	


	$(document).on('change', '#product_code', function() {
		showProgress("Getting product information.");
		var element = $(this).attr("");
		$.get('GetProductInfoFresh', {
			requestData : JSON.stringify({
				product_code : $(this).attr('product_code'),
				vendor_code : $(this).attr('vendor_code')
			})
		}, function(responseText) {
			hideProgress();
			if (responseText.status == "success") {
				$("#modalInformationError").html("Product already exists with this code.");
				$("#modalInformation").show();

			}

		});

	});


	$(document).on('click', '.proddetailsadmin', function() {
		showProgress("Getting product information.");
		var element = $(this).attr("");
		$.get('GetProductInfoFresh', {
			requestData : JSON.stringify({
				product_code : $(this).attr('product_code'),
				vendor_code : $(this).attr('vendor_code')
			})
		}, function(responseText) {
			hideProgress();
			if (responseText.status == "success") {

				/* if (responseText.MstProductKey.product_code != undefined)
					{
					$("#ProductEntryForm #product_code").attr('readonly', 'readonly');
					$("#ProductEntryForm #product_code").attr('disabled', 'disabled');
					$("#ProductEntryForm #product_code").attr('tabindex', '-1');
					$("#ProductEntryForm #product_code").css('background-color', '#f5f5f5');
					$("#ProductEntryForm #product_code").css('opacity', '1');
					
					} */
				$.each(responseText, function(key, value) {
					$("#ProductEntryForm #" + key).val(value);
				});
				$("#ProductEntryForm #product_code").val(responseText.MstProductKey.product_code);
				$("#ProductEntryForm #span_product_code").html(responseText.MstProductKey.product_code);
				$("#ProductEntryForm #product_code").removeClass("show");
				$("#ProductEntryForm #product_code").addClass("hide");
				$("#ProductEntryForm #span_product_code").removeClass("hide");
				$("#ProductEntryForm #span_product_code").addClass("show");

				$("#prod_image_admin_name").html(responseText.image_name);

				$("#prod_image_admin").attr("src", "<%=request.getContextPath()%>/app<%=msProductImg%>/" + responseText.image_name + ".jpg");
				$("#ProductEntryForm #image_name").val(responseText.image_name);

				$("#ProductEntryForm #save_mode").val("E");
			}

		});

	});

	$(document).on(
		'click',
		'.newproduct',
		function() {
			$("#ProductEntryForm").trigger("reset");
			$("#ProductEntryForm #product_code").removeClass("hide");
			$("#ProductEntryForm #product_code").addClass("show");
			$("#ProductEntryForm #span_product_code").removeClass("show");
			$("#ProductEntryForm #span_product_code").addClass("hide");
			$("#prod_image_admin").attr("src", "");
			$("#prod_image_admin_name").html("");
			$("#ProductEntryForm #save_mode").val("N");

		});

	$(document).on('click', '.saveproduct', function() {
		showProgress("Saving product information");
		var form = $("#ProductEntryForm");
		var serialized = form.serializeArray();
		var s = '';
		var data2 = {};
		for (s in serialized) {
			data2[serialized[s]['name']] = serialized[s]['value']
		}
		data2 = removeEmptyOrNull(data2);

		var data3 = JSON.stringify(data2);

		$.post('SaveProduct', {
			data : data3
		}, function(responseText) {
			hideProgress();
			if (responseText.status == "success") {

				$("#modalInformationError").html(responseText.msg);
				$("#modalInformation").show();
			} else {
				$("#modalInformationError").html(responseText.error);
				$("#modalInformation").show();
			}

		});

	});

	$(document).on('change', '#imagefile', function(e) {
		showProgress("Uploading files.")
		e.preventDefault();

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
				if (responseText.status == "success") {
					$("#prod_image_admin").attr("src", "<%=request.getContextPath()%>/app<%=msProductImg%>/" + responseText.image_name + ".jpg");
					$("#ProductEntryForm #image_name").val(responseText.image_name);
					$("#prod_image_admin_name").html(responseText.image_name);
					$("#modalInformationError").html(responseText.success);
					$("#modalInformation").show();
				} else {
					$("#modalInformationError").html(responseText.error);
					$("#modalInformation").show();
				}
			}
		});
	});


	$(document).on('click', '#btnUploadProduct', function(e) {
		//alert("AA");
		//hideProgress();
		/* $("#modalConfirmYesNo #lblInfoConfirmYesNo").removeClass("hide");
		$("#modalConfirmYesNo #lblInfoConfirmYesNo").addClass("show");  */
		//alert("BB");
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
				if (responseText.status == "success") {
					// $("#MainFormSearchProduct").submit();
					$("#modalInformationError").html(responseText.success);
					$("#processlog").html(responseText.log);
					$("#modalInformation").show();
				} else {
					$("#modalInformationError").html(responseText.error);
					$("#modalInformation").show();
				}
			}
		});

	});


	$(document).on('click', '#btnUploadCustomer', function(e) {
		showProgress("Uploading customer data.")
		var form_data = new FormData($('#FormUpload')[0]);
		$.ajax({
			type : 'POST',
			url : 'UploadCustomers',
			processData : false,
			contentType : false,
			async : false,
			cache : false,
			data : form_data,
			success : function(responseText) {
				hideProgress();
				if (responseText.status == "success") {
					$("#modalInformationError").html(responseText.success);
					$("#processlog").html(responseText.log);
					$("#modalInformation").show();
				} else {
					$("#modalInformationError").html(responseText.error);
					$("#modalInformation").show();
				}
			}
		});

	});


	$(document).on('click', '#exportcustomer', function() {
		showProgress("Exporting customer list.");
		//var group_code = $(this).attr("group_code");
		$.get('ExportCustomer', {
			data : JSON.stringify({})
		}, function(responseText) {
			hideProgress();
			//alert(responseText);
			if (responseText.status == 1) {
				window.open('<%=request.getContextPath()%>/app/<%=GlobalValues.getCompanyCode(session)%>/' + responseText.file, '_self');
			} else {
				$("#modalInformationError").html(responseText.log);
				$("#modalInformation").show();
			}

		});

	});
</script>



<body>
	<div class="col-md-12 p-1">

		<div class="col-md-8"
			style="border-left: 1px solid #6aa8cc; border-right: 1px solid #6aa8cc;">
			<div class="col-md-12 p-1">
				<fieldset>
					<legend style="padding-top: 20px;">Data Processing</legend>
					<div class="col-md-8 p-1">
						<form id="FormUpload" style="display: inline-table;"
							class="p-1 form-horizontal" enctype="multipart/form-data">
							<div style="padding: 0px; margin: 0px;"
								class="form-group form-group-sm">
								<input class="form-control" type="file" id="file" name="file"
									style="padding: 0px; height: 33px; font-size: 17px; margin: 0px;">

							</div>

						</form>
						<div style="display: inline-table; padding-top: 5px"
							class="col-md-12 p-1">
							<a id="btnUploadProduct" class="hide"></a> <a
								class="btn btn-info"
								onclick="ConfirmYesNo('Confirmation!!!', 'Are you sure you want to upload product data? Please wait till upload finishes after clicking on Yes', 'btnUploadProduct');"
								style="" id="btnUploadProductDummy">Upload Product File</a> <a
								id="exportproduct" class="btn btn-info">Download Product
								List</a> <a id="deleteproduct" class="hide"></a> <a style=""
								onclick="ConfirmYesNo('Confirmation!!!', 'Are you sure you want to delete cart?', 'deleteproduct');"
								class="btn btn-info">Delete All Products </a>

							<%-- <%
									if (ReferenceUtils.getCnfigParamValue(session, "REG_CUSTOMER").equals("N")) {
								%>
									
								<a id="btnUploadCustomer" class="hide"></a> 
								<a class="btn btn-info" 
								onclick="ConfirmYesNo('Confirmation!!!', 'Are you sure you want to upload customer data? Please wait till upload finishes after clicking on Yes', 'btnUploadCustomer');"
									style="" id="btnUploadCustomerDummy" >Upload Customer
									File</a>
									
								<a id="exportcustomer"
									class="btn btn-info">Download Customer List</a>
										
								<%
									}
								%> --%>

							<a id="btnUploadImage" class="hide"></a>
							<!-- <a class="btn btn-info" 
								onclick="ConfirmYesNo('Confirmation!!!', 'Are you sure you want to upload image? Please wait till upload finishes after clicking on Yes', 'btnUploadImage');"
									style="" id="btnUploadImageDummy" >Upload Image</a> -->


						</div>
					</div>
					<!-- <div class="col-md-4 p-1" style="text-align: -webkit-center;">
							<div  class="col-md-12 p-1">
								<span id="prod_image_admin_name"> </span>
							</div>
							<div  class="col-md-12 p-1">
								<img class="img-responsive" id="prod_image_admin"
										onerror="this.src='/Shoponline/img/noimage.png?ver=1.03'"
										style="height: 116px; border-radius: 10px;margin-top: 5px;">
							</div>
						</div> -->

				</fieldset>
			</div>
			<div>
				<a data-toggle="collapse" href="#collapse1">Upload Product
					Process Logs</a><b class="caret"></b>
			</div>
			<div id="collapse1" class="panel-collapse collapse">
				<fieldset>
					<legend style="padding-top: 20px;">Process Log</legend>
					<div class="" id="processlog"></div>
				</fieldset>
			</div>




			<div class="col-md-12 p-1" style="">
				<fieldset>
					<legend style="padding-top: 20px;">Product List</legend>

					<form id="MainFormSearchProduct" method="get"
						action="LoadProdDataMgmt">
						<button accesskey="s" type="submit" class="btn btn-info"
							style="margin-top: 5px;" onclick="showProgress('');"
							id="searchproducts">Refresh/Search</button>
					</form>

					<table id="ListTable" class="gridtable" style="width: 100%;">
						<thead>
							<tr>
								<th>Product Code</th>
								<th>Status</th>
								<th>Product Name</th>
								<th>Group Name</th>
								<th>GST Percentage</th>
								<th>Cess Percentage</th>
								<th>HSN Code</th>
								<th>Selling Price</th>
								<th>MRP</th>
								<th>Image Name</th>
								<th>Start date</th>
								<th>End Date</th>
								<th>Home Page View</th>
								<th>Stock</th>


							</tr>
						</thead>
						<script>
							showProgress("Uploading files.");
						</script>
						<tbody>
							<%
								List<Map<String, Object>> loProductList = (List<Map<String, Object>>) request.getAttribute("AllProducts");
								if (loProductList != null) {
									for (Map<String, Object> loProductRow : loProductList) {
							%>
							<tr>
								<td><button class="link proddetailsadmin" type="submit"
										product_code="<%=(String) loProductRow.get("product_code")%>"
										vendor_code="<%=(String) loProductRow.get("vendor_code")%>"><%=(String) loProductRow.get("product_code")%></button></td>

								<td class="text-danger"><%=loProductRow.get("active") != null && ((String) loProductRow.get("active")).equals("I")
							? "Inactive"
							: ""%></td>
								<td><%=(String) loProductRow.get("product_name")%></td>
								<td><%=(String) loProductRow.get("group_name")%></td>
								<td class="text-right"><%=AppUtils.convertNullToEmpty(((BigDecimal) loProductRow.get("gst_percentage")).setScale(2))%></td>
								<td class="text-right"><%=AppUtils
							.convertNullToEmpty(((BigDecimal) loProductRow.get("cess_percentage")).setScale(2))%></td>
								<td><%=(String) loProductRow.get("hsn_code")%></td>
								<td class="text-right"><%=AppUtils.convertNullToEmpty(((BigDecimal) loProductRow.get("selling_price")).setScale(2))%></td>
								<td class="text-right"><%=AppUtils.convertNullToEmpty(((BigDecimal) loProductRow.get("cal_mrp")).setScale(2))%></td>
								<td><%=(String) loProductRow.get("image_name")%></td>
								<td><%=AppUtils.getFormattedDate((Timestamp) loProductRow.get("start_date"))%></td>
								<td><%=AppUtils.getFormattedDate((Timestamp) loProductRow.get("end_date"))%></td>
								<td><%=(String) loProductRow.get("home_page_view")%></td>
								<td class="text-right"><%=(BigDecimal) loProductRow.get("cal_stock")%></td>



							</tr>
							<%
								}
								}
							%>

						</tbody>
						<script>
							hideProgress();
						</script>
					</table>
				</fieldset>
			</div>
		</div>

		<div class="col-md-4"
			style="border-left: 1px solid #6aa8cc; border-right: 1px solid #6aa8cc;">

			<!-- <div class="col-md-12 p-1 " style="text-align: -webkit-center;">
			<fieldset>
					<legend style="padding-top: 20px;">Images</legend>
				<form method="POST" action="UploadImage" id="FormUploadImage"
					class="p-1 form-horizontal" enctype="multipart/form-data">
					<div style="padding: 0px; margin: 0px;"
						class="form-group form-group-sm">
						<input multiple="muliple" class="form-control" type="file"
							id="imagefile" name="imagefile"
							style="padding: 0px; height: 33px; font-size: 17px; margin: 0px;">
						<div style="display: flex;" class="col-md-6 p-1"></div>
					</div>
				</form>
				<button accesskey="s" class="btn btn-info"
					style="margin-top: 5px;float:left;" id="btnUploadImage" data-toggle="tooltip"
					title="Upload file">Upload Images</button>
				<img class="img-responsive" id="prod_image_admin"
					onerror="this.src='/Shoponline/img/noimage.png?ver=1.03'"
					style="height: 150px; border-radius: 10px;margin-top: 5px;">
			</fieldset>
			</div> -->



			<div class="col-md-12 p-1" style="padding-bottom: 20px;">
				<fieldset>
					<legend style="padding-top: 20px;">Product Details</legend>
					<div class="col-md-12 p-1" style="display: inline-table;">
						<div class="col-md-8 p-1" style="display: flex;">

							<a style="margin: 5px 0px 0px 5px;"
								class="newproduct btn btn-info">New</a> <a
								style="margin: 5px 0px 0px 5px;"
								class="saveproduct btn btn-info">Save</a>
						</div>
						<!-- <div class="col-md-4 p-1">
						<img class="img-responsive" id="prod_image_admin_selected"
									onerror="this.src='/Shoponline/img/noimage.png?ver=1.03'"
									style="height: 75px; border-radius: 10px;margin-top: 5px;float: right;">
						</div> -->
					</div>

					<form id="ProductEntryForm" class="form-horizontal" method="post">
						<div class="form-group form-group-sm">
							<input id="save_mode" name="save_mode" type="hidden"
								class="form-control" value="N" /> <label
								class="control-label col-sm-12" style="text-align: left;"
								labelrequired="true" for="inputsm">Code:</label>
							<div class="col-sm-12">
								<input id="product_code" name="product_code" type="text"
									required="true" autofocus="true" class="form-control"
									value="${Product_code}" /> <span id="span_product_code"
									class="hide"></span>
							</div>

							<label class="control-label col-sm-12" style="text-align: left;"
								labelrequired="true" for="inputsm">Name:</label>
							<div class="col-sm-12">
								<input id="product_name" name="product_name" type="text"
									class="uppercase form-control" required="true" value="" />
							</div>

							<label class="control-label col-sm-12" style="text-align: left;"
								for="inputsm">Additional Information:</label>
							<div class="col-sm-12">
								<input id="add_information" name="add_information" type="text"
									class="form-control" required="true" value="" />
							</div>

							<label class="control-label col-sm-12" style="text-align: left;"
								for="inputsm">Group Name:</label>
							<div class="col-sm-12">
								<input id="group_name" name="group_name" type="text"
									class="uppercase form-control" required="true"
									value="${group_name}" />
							</div>

							<label class="col-sm-12 control-label" labelrequired="true"
								for="inputsm" style="text-align: left;">Start Date:</label>
							<div class="col-sm-12">
								<input id="start_date" id="start_date" name="start_date"
									required="true" type="text"
									class="form-control formatdatecalendar" value="${start_date}" />
							</div>

							<label class="col-sm-12 control-label" for="inputsm"
								style="text-align: left;">End Date:</label>
							<div class="col-sm-12">
								<input id="end_date" id="end_date" name="end_date" type="text"
									class="form-control formatdatecalendar" value="${end_date}" />
							</div>

							<label class="control-label col-sm-12" style="text-align: left;"
								for="inputsm">MRP:</label>

							<div class="col-sm-12">
								<input id="mrp" name="mrp" type="number" required="true"
									class="form-control amount" value="${mrp}" />
							</div>

							<label class="control-label col-sm-12" style="text-align: left;"
								for="inputsm">Landing Cost:</label>

							<div class="col-sm-12">
								<input id="landing_cost" name="landing_cost" type="number"
									required="true" class="form-control amount"
									value="${landing_cost}" />
							</div>

							<label class="control-label discount col-sm-12"
								style="text-align: left;" for="inputsm">Discount%:</label>

							<div class="col-sm-12 discount">
								<input id="disc_per" name="disc_per" type="number"
									required="true" class="form-control amount" value="${disc_per}" />
							</div>

							<label class="control-label bv col-sm-12"
								style="text-align: left;" for="inputsm">BV%:</label>

							<div class="col-sm-12 bv">
								<input id="bv_per" name="bv_per" type="number" required="true"
									class="form-control amount" value="${bv_per}" />
							</div>

							<label class="control-label discount col-sm-12"
								style="text-align: left;" for="inputsm">Discount:</label>

							<div class="col-sm-12 discount">
								<input id="discount" name="discount" type="number"
									required="true" class="form-control amount" value="${discount}" />
							</div>

							<label class="control-label bv col-sm-12"
								style="text-align: left;" for="inputsm">BV:</label>

							<div class="col-sm-12 bv">
								<input id="bv" name="bv" type="number" required="true"
									class="form-control amount" value="${bv}" />
							</div>

							<label class="control-label col-sm-12" style="text-align: left;"
								labelrequired="true" for="inputsm">Selling Price:</label>

							<div class="col-sm-12">
								<input id="selling_price" name="selling_price" type="number"
									required="true" class="form-control amount" id="selling_price"
									value="${selling_price}" />

							</div>

							<label class="control-label col-sm-12" for="inputsm"
								style="text-align: left;">HSN Code:</label>
							<div class="col-sm-12">
								<input id="hsn_code" name="hsn_code" type="number"
									class="form-control" required="true" value="${hsn_code}" />
							</div>

							<label class="control-label col-sm-12" style="text-align: left;"
								for="inputsm">Stock:</label>

							<div class="col-sm-12">
								<input id="stock" name="stock" type="number" required="true"
									class="form-control number" id="stock" value="${stock}" />
							</div>



							<label class="control-label col-sm-12" style="text-align: left;"
								for="inputsm">GST%:</label>

							<div class="col-sm-12">
								<input id="gst_percentage" name="gst_percentage" type="number"
									required="true" class="form-control percentage"
									id="gst_percentage" value="${gst_percentage}" />
							</div>

							<label class="control-label col-sm-12" style="text-align: left;"
								for="inputsm">CESS%:</label>

							<div class="col-sm-12">
								<input id="cess_percentage" name="cess_percentage" type="number"
									required="true" class="form-control percentage"
									id="cess_percentage" value="${cess_percentage}" />
							</div>


							<label class="control-label col-sm-12" style="text-align: left;"
								for="inputsm">Image Name:</label>

							<div class="col-sm-12">
								<input id="image_name" name="image_name" type="text"
									class="uppercase form-control" required="true" id="image_name"
									value="${image_name}" /> <a id="btnUploadImage" class="hide"></a>
								<a class="btn btn-info" onclick="$('#imagefile').click();"
									style="" id="btnUploadImageDummy">Upload Image</a>
							</div>

							<div class="col-md-12">
								<img class="img-responsive" id="prod_image_admin"
									onerror="this.src='/Shoponline/img/noimage.png?ver=1.03'"
									style="height: 116px; border-radius: 10px; margin-top: 5px;">
							</div>


							<label class="control-label col-sm-12" style="text-align: left;"
								for="inputsm">Home Page Position:</label>
							<div class="col-sm-12">
								<select id="home_page_view" name="home_page_view" type="text"
									class="form-control">
									<%=ReferenceUtils.buildOptions(session, "PLOCN", null, true)%>
								</select>
							</div>


						</div>
					</form>
				</fieldset>
			</div>

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
