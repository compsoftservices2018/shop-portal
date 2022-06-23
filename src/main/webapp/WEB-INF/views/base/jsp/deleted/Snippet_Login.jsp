
<div style="text-align: center;">
	<img
		style="height: 80px; padding: 2px; border: 1px outset; border-radius: 20px;"
		alt="image" src="<%=request.getContextPath()%>/img/logo.png?ver=1.03" />
</div>
<div
	style="color: #ffc107; font-size: 20px; font-weight: bold; text-align: center;">
	<%=ReferenceUtils.getGlobalConfigParamValue(session, "APP_NAME")%>
</div>

<div class="col-sm-12"
	style="text-align: center; font-weight: bold; padding-bottom: 10px; font-size: 20px;">
	Login</div>
<div class="form-group form-group-sm">
	<div class="col-sm-12">
		<label class="control-label" for="inputsm" style="font-size: 14px;"
			style="text-align: left;">Login Id</label>
	</div>
	<div class="col-sm-12">
		<input name="login_id" type="text" class="uppercase form-control"
			style="height: 40px; font-size: 14px;" id="login_id" placeholder=""
			value="" />
	</div>
</div>
<div class="form-group form-group-sm">
	<div class="col-sm-12">
		<label class="control-label" for="inputsm" style="font-size: 14px;"
			style="text-align: left;">Password</label>
	</div>
	<div class="col-sm-12" style="display: flex;">
		<input name="password" type="password" class="form-control"
			style="height: 40px; font-size: 14px;" id="password"
			placeholder="Enter password" value="" /> <a id="showpassword"
			accesskey="x" class="buttonimage" data-toggle="tooltip"
			style="margin: 0px;" data-placement="top" title="Show/hide Password"
			role="button"><img alt="image"
			style="height: 40px; border: 1px solid #ccc; margin-left: -10px;"
			src="<%=request.getContextPath()%>/img/showpassword.png?ver=1.03" />
		</a>
	</div>
</div>

<div style="text-align: center;">
	<div>
		<button type="submit" class="btn btn-default">Login</button>
		<button type="button" class="btn btn-default" data-dismiss="modal"
			onclick="$('#modallogin').hide();">Cancel</button>
	</div>
</div>
