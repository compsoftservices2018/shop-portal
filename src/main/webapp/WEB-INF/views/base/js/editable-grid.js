$(document).ready(function() {

	$(document).on('click', '.saverow', function() {
		saveRow($(this), "N");
	});

	$(document).on('focusout', '.saveandaddrow', function() {
		saveRow($(this), "N");
	});


	$(document).on('keydown', '.addemptyrow', function(e) {
		var keyCode = e.keyCode || e.which;
		//alert(keyCode);
		if (e.keyCode == 9) {
			var loNodeTable = document.getElementById($(this).closest('tr').parent().parent().attr('id'));
			var loNodeLastRow = loNodeTable.rows[loNodeTable.rows.length - 1];
			var loNodeCurrentRow = document.querySelectorAll('[name="' + $(this).closest('tr').attr('name') + '"]')[0];
			if (loNodeLastRow.id == loNodeCurrentRow.id) {
				addNewRow($(this), false);
			}
		}
	});

	$(document).on('click', '.addrow', function() {
		addNewRow($(this), false);
	});

	$(document).on('click', '.approverow', function() {
		approverow($(this));
	});

	$(document).on('click', '.unapproverow', function() {
		unapproverow($(this));

	});


	$(document).on('click', '.deleterow', function() {
		saveRow($(this), "Y");
	});

	$(document).on('click', '.deleterownosave', function() {
		$(this).closest('tr').addClass("hide");
		$(this).closest('tr').addClass("deleted");

	});

	$(document).on('click', '.copyrow', function() {
		addNewRow($(this), true);
	});

	/*$(document).on('change', '.editable', function() {
		var rowId = $(this).closest('tr').attr('id');
		var element = $(this).closest('tr').find('.saverow');
		element.removeClass("hide");
		element.addClass("show");
	});*/

	$(document).on('keydown', '.editable', function(e) {
		var keyCode = e.keyCode || e.which;

		//alert("Key " + keyCode);
		//("row " + $(this).closest('tr').attr('id'));
		if (e.keyCode == 113) {
			e.preventDefault();
			addNewRow($(this), false);
		}
		if (e.keyCode == 114) {
			e.preventDefault();
			addNewRow($(this), true);
		}
		if (e.keyCode == 115) {
			e.preventDefault();
			saveRow($(this), "Y");
		}
		if (e.keyCode == 116) {
			approverow($(this));
			e.preventDefault();
		}
		if (e.keyCode == 117) {
			e.preventDefault();
			unapproverow($(this));
		}
		if (e.keyCode == 123) {
			e.preventDefault();
			saveRow($(this), "N");
		}
	});

});

function unapproverow(foCurrentRow) {
	var element = document.getElementById(foCurrentRow.closest('tr').attr('id'));
	document.getElementById('auth_by' + element.id).value = "";
	document.getElementById('auth_date' + element.id).value = "";
	document.getElementById('status' + element.id).value = "N";
	saveRow(foCurrentRow, "N");
}

function approverow(foCurrentRow) {
	var element = document.getElementById(foCurrentRow.closest('tr').attr('id'));
	document.getElementById('auth_by' + element.id).value = "<%=GlobalValues.getUserCode(session)%>";
	document.getElementById('auth_date' + element.id).value = "<%=AppUtils.getCurrentDateTime()%>";
	document.getElementById('status' + element.id).value = "A";
	saveRow(foCurrentRow, "N");
}

function paintRowOnResponse(foCurrentRow, fsDelete, responseText) {
	var loNodeTable = document.getElementById(foCurrentRow.closest('tr').parent().parent().attr('id'));
	var loNodeLastRow = loNodeTable.rows[loNodeTable.rows.length - 1];
	//var loNodeCurrentRow = document.getElementById(foCurrentRow.closest('tr').attr('id'));
	var loNodeCurrentRow = document.querySelectorAll('[name="' + foCurrentRow.closest('tr').attr('name') + '"]')[0];
	//alert("responseText" + responseText);
	if (responseText == "Success") {
		foCurrentRow.closest('tr').find('.saverow').removeClass("show");
		foCurrentRow.closest('tr').find('.saverow').addClass("hide");
		if (loNodeLastRow.id == loNodeCurrentRow.id && (fsDelete == "N")) {
			addNewRow(foCurrentRow, false);
		}
		if (fsDelete == "Y") {
			foCurrentRow.closest('tr').addClass("hide");
		}
		// alert(loNodeCurrentRow.cells[20].firstChild.value);
		if (loNodeCurrentRow.cells[20].firstChild.value != "") {
			// alert("In of");
			loNodeCurrentRow.cells[0].style.backgroundColor = "#adff2f";
		} else {
			loNodeCurrentRow.cells[0].style.backgroundColor = "#ffffff";
		}
	} else {
		foCurrentRow.closest('tr').find('.saverow').removeClass("hide");
		foCurrentRow.closest('tr').find('.saverow').addClass("show");
		$("#modalInformationError").html(responseText);
		$("#modalInformation").show();
	}
}

function sleep(miliseconds) {
	var currentTime = new Date().getTime();
	while (currentTime + miliseconds >= new Date().getTime()) {
	}
}

function addNewRow(foCurrentRow, fboolCopyData) {
	var loNodeBody = document.getElementById(foCurrentRow.closest('tr')
		.parent().attr('id'));
	//alert(loNodeBody.id);
	var loNodeTable = document.getElementById(foCurrentRow.closest('tr')
		.parent().parent().attr('id'));
	//alert(loNodeTable.id);
	//alert(foCurrentRow.closest('tr').attr('idname'));
	var loNodeLastRow = loNodeTable.rows[loNodeTable.rows.length - 1];
	//alert(loNodeLastRow);
	//var loNodeCurrentRow = document.getElementById(foCurrentRow.closest('tr')
	//		.attr('id'));

	var loNodeCurrentRow = document.querySelectorAll('[name="' + foCurrentRow.closest('tr').attr('name') + '"]')[0];

	var nameattr = document.createAttribute("name"); // Create a "class" attribute
	// Set the value of the class attribute

	var liNewRowId = parseInt(loNodeLastRow.id) + 1;
	var loNodeNewRow = loNodeCurrentRow.cloneNode(true);
	loNodeNewRow.id = liNewRowId;
	//alert(loNodeNewRow.name + "/" + loNodeTable.id + liNewRowId);
	//loNodeNewRow.attr("name", fsPrefix + liNewRowId);
	nameattr.value = loNodeTable.id + liNewRowId;
	loNodeNewRow.setAttributeNode(nameattr);
	loNodeNewRow.cells[0].style.backgroundColor = "#ffffff";

	for (var c = 0; c < loNodeNewRow.cells.length; c++) {

		// alert(loNodeNewRow.cells[c].firstChild.name == "auth_by");
		if (loNodeNewRow.cells[c] != undefined
			&& loNodeNewRow.cells[c].firstChild != undefined) {

			if (loNodeNewRow.cells[c].firstChild.getAttribute("object") != undefined) {

				if (loNodeNewRow.cells[c].firstChild.getAttribute("key_name") != undefined) {
					loNodeNewRow.cells[c].firstChild.id = loNodeNewRow.cells[c].firstChild.getAttribute("object")
					+ liNewRowId + "."
					+ loNodeNewRow.cells[c].firstChild.getAttribute("key_name") + "."
					+ loNodeNewRow.cells[c].firstChild.getAttribute("attr_name");

					loNodeNewRow.cells[c].firstChild.name = loNodeNewRow.cells[c].firstChild.getAttribute("object")
					+ "[" + liNewRowId + "]."
					+ loNodeNewRow.cells[c].firstChild.getAttribute("key_name") + "."
					+ loNodeNewRow.cells[c].firstChild.getAttribute("attr_name");
				} else {
					loNodeNewRow.cells[c].firstChild.id = loNodeNewRow.cells[c].firstChild.getAttribute("object")
					+ liNewRowId + "."
					+ loNodeNewRow.cells[c].firstChild.getAttribute("attr_name");

					loNodeNewRow.cells[c].firstChild.name = loNodeNewRow.cells[c].firstChild.getAttribute("object")
					+ "[" + liNewRowId + "]."
					+ loNodeNewRow.cells[c].firstChild.getAttribute("attr_name");
				}
				loNodeNewRow.cells[c].firstChild.removeAttribute("readonly");
				loNodeNewRow.cells[c].firstChild.removeAttribute("tabindex");
				loNodeNewRow.cells[c].firstChild.style.background = "#ffffff";

				if (loNodeNewRow.cells[c].firstChild.getAttribute("ref_name") != undefined) {
					//alert(loNodeNewRow.cells[c].firstChild.getAttribute("lov_source"))
					loNodeNewRow.cells[c].firstChild
						.setAttribute("ref_name", liNewRowId + "_"
							+ loNodeNewRow.cells[c].firstChild.getAttribute("attr_name"));
				}
				loNodeNewRow.cells[c].firstChild
				.setAttribute("index", liNewRowId);

			} else {
				loNodeNewRow.cells[c].firstChild.id = loNodeNewRow.cells[c].firstChild.getAttribute("attrname") + liNewRowId;
			}

			if (!fboolCopyData) {
				loNodeNewRow.cells[c].firstChild.value = "";
				if (loNodeNewRow.cells[c].firstChild.getAttribute("default_value") != undefined) {
					loNodeNewRow.cells[c].firstChild.value = loNodeNewRow.cells[c].firstChild.getAttribute("default_value");

				}
			}
		}
	}
	loNodeBody.append(loNodeNewRow);
	setMaskFix();
	//sleep(500);
	return loNodeNewRow;
	loNodeNewRow.cells[1].firstChild.focus();

}

function tableToJson(loTableid) {
	var json = '';
	var otArr = [];
	$('.deleted', '#' + loTableid).remove();
	var header = $('#' + loTableid + ' tbody tr').each(function(e, loTr) {

		lArrayRows = $(this).children();
		var itArr = [];
		lArrayRows.each(function(e, lArrayColumns) {

			if ($(lArrayColumns).find("input").attr('attr_name') != undefined) {
				//alert( $(lArrayColumns).find("input").attr('name') + "/" + $(lArrayColumns).find("input").val());
				var items = '';
				items += "\"" + $(lArrayColumns).find("input").attr('attr_name') + "\" : \"" + $(lArrayColumns).find("input").val()
					+ "\"";
				itArr.push(items);
			}
		});
		otArr.push('{' + itArr.join(',') + '}');
	})
	return '[' + otArr.join(",") + ']'
}

function tableRowToJson(row) {
	var lArrayRows = row;
	var itArr = [];
	lArrayCols = lArrayRows.children();
	lArrayCols.each(function(e, lArrayCol) {
		var items = '';
		if ($(lArrayCol).find("input").attr('attr_name') != undefined) {
			items += "\"" + $(lArrayCol).find("input").attr('attr_name') + "\":\"" + $(lArrayCol).find("input").val() + "\"";
			itArr.push(items);
		} else if ($(lArrayCol).find("select").attr('attr_name') != undefined) {
			items += "\"" + $(lArrayCol).find("select").attr('attr_name') + "\":\"" + $(lArrayCol).find("select").val() + "\"";
			itArr.push(items);
		}
	});
	//alert(itArr);
	return '{' + itArr + '}';
}