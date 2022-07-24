<%-- 
<%
	if (session.getAttribute("USEROBJ") != null && ((MstUser) session.getAttribute("USEROBJ")).getUser_type()
			.equals(FrameworkConstants.USER_TYPE_SUPRADMIN)) {
%>

<li><a class="" data-toggle="dropdown"
	href="<%=request.getContextPath()%>/ContactUs">System Administration</a>

	<ul class="dropdown-menu" style="font-size: 12px; margin-top: 0px;">
		<li><a href="<%=request.getContextPath()%>/AddCompany">Manage
				Companies</a></li>
		<li><a href="<%=request.getContextPath()%>/AddAdminUser">Manage
				Admin Users</a></li>
		<li><a href="<%=request.getContextPath()%>/UserAccess">Manage
				Admin User's Permissions</a></li>
	
	</ul></li>

<%
	}
%>

<%
	if (session.getAttribute("USEROBJ") != null && (((MstUser) session.getAttribute("USEROBJ")).getUser_type()
			.equals(FrameworkConstants.USER_TYPE_ADMIN) || (((MstUser) session.getAttribute("USEROBJ")).getUser_type()
					.equals(FrameworkConstants.USER_TYPE_SUPRADMIN)) )  ) {
%>

<li><a class="" data-toggle="dropdown"
	href="<%=request.getContextPath()%>/ContactUs">Company Administration</a>

	<ul class="dropdown-menu" style="font-size: 12px; margin-top: 0px;">
		<li><a href="<%=request.getContextPath()%>/AddUser">Manage
				Users</a></li>
		<li><a href="<%=request.getContextPath()%>/UserAccess">Manage
				User's Permissions</a></li>

	</ul></li>

<%
	}
%>

<%
	if (session.getAttribute("USEROBJ") != null ) {
%>

<li><a class="" data-toggle="dropdown"
	href="<%=request.getContextPath()%>/ContactUs">Masters</a>

	<ul class="dropdown-menu" style="font-size: 12px; margin-top: 0px;">
		<li><a href="<%=request.getContextPath()%>/">Products
		</a></li>

	</ul></li>
	
	<%
	}
%>
 --%>

<ul class="d-flex font-sm" style="list-style:none;">
				

<%
	List<Map<String, Object>> loModuleList = (List<Map<String, Object>>) session.getAttribute("MODULES");
	if (loModuleList != null) {
		for (Map<String, Object> loModuleRow : loModuleList) {
			String lsLeaf = ((BigDecimal) loModuleRow.get("leaf")).toString();
%>

<%
	if (lsLeaf.equals("0")) {
%>

<li level="<%=loModuleRow.get("level")%>" name="outeachelement"
	class="dropdown <%=((BigDecimal) loModuleRow.get("level")).intValue() > 2 ? "dropdown-submenu" : ""%>"
	id="<%=loModuleRow.get("module_code")%>" 
	parentid="<%=loModuleRow.get("parent_module_code")%>"><a class="pr-2 mr-2 text-white border-right"
	data-toggle="dropdown"  href="<%=request.getContextPath()%>/ContactUs"><%=loModuleRow.get("module_name")%></a>
	<ul id="<%="ul_" + loModuleRow.get("module_code")%>"
		parentid="<%="ul_" + loModuleRow.get("parent_module_code")%>"
		class="p-0 bg-info dropdown-menu" >
	</ul></li>


<%
	} else {
%>

<li name="eachelementchild" id="<%=loModuleRow.get("module_code")%>" class="bg-white dropdown-item"
	parentid="<%=loModuleRow.get("parent_module_code")%>"><a class="pr-1 mr-1 text-secondary bg-white"
	href="<%=request.getContextPath() + "/" + loModuleRow.get("url")%>"><%=loModuleRow.get("module_name")%></a>
</li>

<%
	}
		}
	}
%>
</ul>

