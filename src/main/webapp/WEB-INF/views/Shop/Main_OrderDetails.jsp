<%@ include file="../common/header.jsp"%>
<%@ page import="com.compsoft.shop.model.TranOrder"%>
<%@ page import="com.compsoft.shop.model.TranOrderDetail"%>
<%@ page import="com.compsoft.shop.model.TranOrderPayment"%>
<%
	TranOrder loTranOrder = (TranOrder) request.getAttribute("order");
%>
<title><%=session.getAttribute("TITLE")%></title>
<body>

	<div class="row m-0">
		<div class="col-sm-1"></div>
		<div class="col-sm-10">
			<%@ include file="../Snippet/Snippet_OrderDetail.jsp"%>
		</div>
		<div class="col-sm-1"></div>
	</div>
</body>
<%@ include file="../common/footer.jsp"%>