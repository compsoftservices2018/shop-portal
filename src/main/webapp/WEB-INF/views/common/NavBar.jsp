<header class="header-area overlay ">
	<nav
		class="navbar navbar-expand-md fixed-top bg-info border-bottom border-light">
		<div class="col-sm-12 p-1 container">

			<ul>
				<li class="nav-link pointer" data-toggle="tooltip"
					data-placement="bottom" title="Home">
					<div>
						<a class="home"> <img style="height: 37px;" class="rounded"
							src="<%=request.getContextPath()%>/app<%=msImg%>/logo-sm.png?ver=1.03" />
						</a>

					</div>
				</li>
			</ul>

			<ul>
				<li class="nav-link">
					<h5><%=msCompanyShortName%></h5>
				</li>
			</ul>

			
			<ul class="collapse navbar-collapse justify-content-end">
				<li class="customer nav-link">
					<div>
						<h5 class="nav-link navbar-collapse font-sm"><%=msUserNameUI%></h5>
					</div>
				</li>
			</ul>
			
			<ul class="showapplogin d-none justify-content-end">
				<li class="nav-link"><a
					class="btn btn-info btn-sm border-white">Sign In</a></li>
			</ul>
			<ul class="signoff d-none justify-content-end">
				<li class="nav-link"><a
					class="btn btn-info btn-sm border-white">Sign off</a></li>
			</ul>

		</div>
		
	</nav>
	<nav style="margin-top:50px;height: 30px;"
		class="navbar navbar-expand-md fixed-top bg-info border-bottom border-light">
		<div class="col-sm-12 p-1 container">
			<%@ include file="../base/jsp/menu.jsp"%>
	</div>
	</nav>
</header>






