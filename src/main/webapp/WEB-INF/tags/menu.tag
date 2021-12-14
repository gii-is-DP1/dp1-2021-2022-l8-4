<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a href="<spring:url value="/" htmlEscape="true" />">
				<spring:url value="/resources/images/TITULO_DEL_JUEGO.png" htmlEscape="true" var="logoImage" />
                <img class="img-responsive" src="${logoImage}" style="width: 10rem"/>
			</a>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">

				<petclinic:menuItem active="${name eq 'home'}" url="/" title="home page">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<span>Inicio</span>
				</petclinic:menuItem>

				<petclinic:menuItem active="${name eq 'rules'}" url="/rules" title="rules">
					<span class="glyphicon glyphicon-book" aria-hidden="true"></span>
					<span>Reglas</span>
				</petclinic:menuItem>

				<petclinic:menuItem active="${name eq 'users'}" url="/users?page=1"
					title="list users">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Lista de Usuarios</span>
				</petclinic:menuItem>
			</ul>


			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Iniciar Sesion</a></li>
					<li><a href="<c:url value="/users/new" />">Registrarse</a></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">

					<petclinic:menuItem active="${name eq 'lobbiesList'}" url="/games/lobbies">
						<span>Unirse a partida</span>
					</petclinic:menuItem>
					<petclinic:menuItem active="${name eq 'newGame'}" url="/games/new">
						<span>Nueva partida</span>
					</petclinic:menuItem>
					<petclinic:menuItem active="${name eq 'newGame'}" url="/statistics">
						<span class="glyphicon glyphicon-fire" aria-hidden="true"></span>
						<span>Estadisticas</span>
					</petclinic:menuItem>

					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>
							<strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<a href="<c:url value="/users/profile/${currentUser.id}" />"
													class="btn btn-primary btn-block btn-sm">Perfil</a>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />"
													class="btn btn-primary btn-block btn-sm">Salir</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>
<!-- 							
                            <li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="#" class="btn btn-primary btn-block">My Profile</a>
											</p>
										</div>
									</div>
								</div>
							</li>
-->
						</ul></li>
				</sec:authorize>
			</ul>
		</div>



	</div>
</nav>
