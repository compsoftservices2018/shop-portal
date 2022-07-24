<form id="form-close" method="get" action="ClosePage">
</form>
	
<div id="modalConfirmYesNo" class="modal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="confirm-title">Confirmation!!!</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body text-center font-sm">
				<p id="confirm-msg"></p>
				<p id="additional-msg"></p>
			</div>
			<div class="modal-footer justify-content-center">
				<button id="btnYes" type="button" class="btn btn-info btn-sm" data-dismiss="modal">Yes</button>
				<button type="button" class="btn btn-secondary btn-sm"
					data-dismiss="modal">No</button>
			</div>
		</div>
	</div>
</div>

<div id="progresswindow" class="modal">
	<div class="modal-dialog bg-light p-2 rounded">
		<div class="d-flex justify-content-center">
			<div class="spinner-border text-warning" role="status"></div>
		</div>
		<div class="font-sm text-center text-info">
			<span>Please wait...</span>
			<p id="process-msg" style="margin: 0px;"></p>
		</div>
	</div>
</div>


<div id="modalInformation" class="modal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="confirm-title">Information</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body font-sm ">
				<%
					ArrayList<String> loErrors = (ArrayList<String>) session.getAttribute("ERRORS");
					if (loErrors != null && loErrors.size() != 0) {
				%>
				<div id="modalInformationError" class="text-danger">
					<ul class="list-inline">
						<%
							for (int i = 0; i < loErrors.size(); i++) {
						%>
						<%="<li>" + (String) loErrors.get(i) + "</li>"%>
						<%
							}
						%>
					</ul>
				</div>
				<%
					}
					ArrayList<String> loInfo = (ArrayList<String>) session.getAttribute("INFO");
					if (loInfo != null && loInfo.size() != 0) {
				%>
				<div class="text-info">
					<ul class="list-inline">
						<%
							for (int i = 0; i < loInfo.size(); i++) {
						%>
						<%="<li>" + (String) loInfo.get(i) + "</li>"%>
						<%
							}
						%>
					</ul>
				</div>
				<%
					}
					ArrayList<String> loSuccess = (ArrayList<String>) session.getAttribute("SUCCESS");
					if (loSuccess != null && loSuccess.size() != 0) {
				%>
				<div class="text-success">
					<ul class="list-inline">
						<%
							for (int i = 0; i < loSuccess.size(); i++) {
						%>
						<%="<li>" + (String) loSuccess.get(i) + "</li>"%>
						<%
							}
						%>
					</ul>
				</div>
				<%
					}

					session.setAttribute("SUCCESS", null);
					session.setAttribute("INFO", null);
					session.setAttribute("ERRORS", null);
				%>
				<div id="modalInformationError"></div>
			</div>
			<div class="modal-footer justify-content-center">
				<button type="button" class="btn btn-info btn-sm"
					data-dismiss="modal">Ok</button>
			</div>
		</div>
	</div>
</div>




<div id="comingsoon" class="modal">

	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-body text-center">
				<img alt="image"
					src="<%=request.getContextPath()%>/img/comingsoon.png?ver=1.03"
					style="width: 150px; border-radius: 8px;" />
			</div>
			<div class="modal-footer justify-content-center">
				<button type="button" class="btn btn-info btn-sm"
					data-dismiss="modal">Ok</button>
			</div>

		</div>
	</div>
</div>



