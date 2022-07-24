

<div id="ModalSelectList" class="modal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Select Options</h4>
			</div>

			<div class="modal-body" id="ModalSelectList_modal-body">

				<input type="hidden" name="source" id="source" value="" />

				<table id="SelectListTable" class="table" style="width: 100%;">
					<thead>
						<tr>
							<th>Select</th>
							<th>Description</th>
							<th class="d-none">key</th>
						</tr>
					</thead>
					<tbody id="SelectListBody">

					</tbody>
				</table>
			</div>
			<div class="modal-footer">

				<button id="ModalSelectListClose" type="button"
					class="btn btn-info" data-dismiss="modal"
					onclick="$('#ModalSelectList').hide();$('#progresswindow').hide();">Close</button>

			</div>
		</div>
	</div>
</div>
