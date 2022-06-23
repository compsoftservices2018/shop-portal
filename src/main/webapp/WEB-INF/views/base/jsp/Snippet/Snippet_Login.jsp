	<div class="row pt-5 m-0 ">
		<div class="col-sm"></div>

		<div class="col-sm-6">
			<div class="row shadow-lg m-0">
				<div class="col-sm text-center">
					<div class="row m-0">
						<div class="col-12 p-2">
							<img style="height: 37px;" class="rounded"
								src="<%=request.getContextPath()%>/app<%=msImg%>/logo-sm.png?ver=1.03" />
						</div>
						<div class="col-12 p-0 pb-2">
							<h5>Login</h5>
						</div>
						<div class="col-12 p-0  pb-2">
							<input name="login_id" type="text" class="form-control rounded-0"
								id="login_id" placeholder="Login Id" value="" /> <input
								name="password" type="password" class="form-control rounded-0 "
								id="password" placeholder="Password" value="" /> <input
								name="circle" type="hidden" class="form-control" id=""
								placeholder="Enter password"
								value="<%=GlobalValues.getCompanyCode(session)%>" />
						</div>
						<div class="col-12 p-0 pb-2">
							<div class="row m-0">
								<div class="col p-0">
									<button type="submit" class="btn btn-info btn-block">Login</button>
								</div>
								<div class="col p-0">
									<button type="button" class="home btn btn-secondary btn-block">Cancel</button>
								</div>
							</div>
						</div>

						<div class="sysuser col-12 hide p-0 pb-2">
							<a class="link float-left newcustomer">Sign up</a> <a
								class="link float-right resetpassword">Forgot Password? </a>
						</div>
						<%-- <div class="comingsoon col-12 pb-2">
							<img class="img-fluid shadow-sm rounded" style="height: 30px;"
								src="<%=request.getContextPath()%>/img/signingoogle.png?ver=1.03">
						</div> --%>
						<div class="col-12 p-0 pb-2 text-danger">
							${user_error}
						</div>
					</div>

				</div>



				<div class="col-sm p-0">
					<img class="w-100 h-100"
						src="<%=request.getContextPath()%>/app<%=msImg%>/login-banner.png?ver=1.03">
				</div>
			</div>
		</div>

		<div class="col-sm"></div>
	</div>



