<div class="shadow-sm p-0">
	<div class="row m-0  bg-light ">
		<div class="col-3 p-0 ">
			<h6 class=" bg-light p-1 text-underline">Summary</h6>
		</div>
		<div class="col-9 p-1 text-danger text-right"><%="Subtotal(" + loTranOrder.getTot_qty() + " Items): " + loTranOrder.getTot_selling_price()%></div>
	</div>
	<div class="row m-0 p-0">
		<div class="col-sm col text-nowrap  pl-1 pr-1">
			<span>Order#: </span><span class="text-muted font-weight-bold"><%=loTranOrder.getTranOrderKey().getOrder_no()%></span>
		</div>
		<div class="col-sm col text-nowrap  pl-1 pr-1">
			<span>Date: </span><span class="text-muted"><%=AppUtils.getFormattedDate(loTranOrder.getOrder_date())%></span>
		</div>

		<div class="col-sm col text-nowrap  pl-1 pr-1">
			<span>Status: </span> <span id="status" class="text-muted"><%=ReferenceUtils.getOptionValue(session, AppConstants.GROUP_ORDER_STATUS, loTranOrder.getStatus())%></span>
		</div>

		<div class="col-sm col text-nowrap  pl-1 pr-1">
			<%
				if (loTranOrder.getTranOrderPayment().get(0).getStatus().equals(AppConstants.STATUS_PAYMENT_PENDING)
						&& loTranOrder.getStatus().equals(AppConstants.STATUS_ORDER_SUBMITTED)) {
			%>
			<button order_no="<%=loTranOrder.getTranOrderKey().getOrder_no()%>"
				status="CA" attr_status="status"
				class="updateorderstatus btn btn-outline-info btn-sm font-xs">Cancel</button>
			<%
				}
			%>
		</div>


	</div>
	<div class="row m-0 p-0">
		<div class="col-sm col pl-1 pr-1">
			<span>Name: </span><span class="text-muted"><%=loTranOrder.getCustomer_name()%></span>
		</div>
		<div class="col-sm col text-nowrap pl-1 pr-1">
			<span>Contact: </span><span class="text-muted"><%=loTranOrder.getMobile()%></span>
		</div>
		<div class="col-sm col text-nowrap pl-1 pr-1">
			<span>email: </span><span class="text-muted"><%=loTranOrder.getEmail()%></span>
		</div>
		<div class="col-12 pl-1 pr-1">
			<span>Address: </span><span class="text-muted"><%=loTranOrder.getDelivery_address()%></span>
		</div>
	</div>


	<div class="row m-0 p-0">
		<%
			if (loTranOrder.getTranOrderPayment().size() != 0) {
		%>
		<div class="col-sm col text-nowrap  pl-1 pr-1">
			<span>Payment Mode: </span><span class="text-muted"><%=ReferenceUtils.getOptionValue(session, AppConstants.GROUP_PAYMENT_MODE,
						loTranOrder.getTranOrderPayment().get(0).getPayment_mode())%></span>
		</div>
		<div class="col-sm col text-nowrap  pl-1 pr-1">
			<span>Payment Status: </span><span class="text-muted"><%=ReferenceUtils.getOptionValue(session, AppConstants.GROUP_PAYMENT_STATUS,
						loTranOrder.getTranOrderPayment().get(0).getStatus())%></span>
		</div>
		<%
			}
		%>

		<div class="col-sm col text-nowrap  bv col pl-1 pr-1">
			<span>BV: </span><span class="text-muted"><%=loTranOrder.getTot_bv()%></span>
		</div>
		<div class="col-sm col text-nowrap  discount pl-1 pr-1">
			<span>Discount: </span><span class="text-muted"><%=loTranOrder.getTot_disc()%></span>
		</div>
	</div>
</div>

<div class="shadow-sm">
	<h6 class="bg-light p-1 text-underline">Details</h6>

	<%
		List<TranOrderDetail> loTranOrderDetailList = loTranOrder.getTranOrderDetail();
		if (loTranOrderDetailList != null) {
			int i = 0;
			int liCol = 0;
			String lsProductCode = "";
			String lsVendorCode = "";
			BigDecimal lsSellingPrice = BigDecimal.ZERO;
			for (TranOrderDetail loDetailRow : loTranOrderDetailList) {
				i++;
				lsProductCode = loDetailRow.getTranOrderDetailKey().getProduct_code();
				lsVendorCode = loDetailRow.getTranOrderDetailKey().getVendor_code();
				lsSellingPrice = loDetailRow.getTranOrderDetailKey().getSelling_price();
	%>
	<div class="shadow-sm row m-0 p-1 font-sm">

		<div class="row col-12 m-0 s-0">
			<div class="col-1 p-0">
				<img class="productdetail img-fluid rounded "
					product_code="<%=lsProductCode%>" vendor_code="<%=lsVendorCode%>"
					style="height: 25px;"
					src="<%=request.getContextPath()%>/app<%=msProductImg%><%="1"%>.jpg"
					onerror="this.src='<%=request.getContextPath()%>/img/noimage.png?ver=1.03'">
			</div>
			<div class="col-11 p-0"><%=loDetailRow.getProduct_name()%>
			</div>
			<div class="col-12 p-0">
				<div class="row m-0 p-0">
					<div class="col text-right p-0">
						<%="Rate: " + lsSellingPrice%>
					</div>
					<div class="col text-right p-0">
						<%="Quantity: " + loDetailRow.getOrder_qty()%>
					</div>
					<div class="col-sm text-right p-0">
						<%="Item Total: " + lsSellingPrice.multiply(loDetailRow.getOrder_qty())%>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%
		}
		}
	%>

</div>