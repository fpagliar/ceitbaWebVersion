<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<%@ include file="../template/headGeneral.jsp"%>
<style type="text/css">
body {
	font: 12px Arial, Helvetica, sans-serif
}

#container {
	margin: 0 auto;
	width: 780px
}

#navigation {
	margin: 0
}

#navigation li {
	list-style: none;
	display: block;
	float: left
}

#navigation li a {
	margin: 0 1px;
	text-decoration: none;
	background: #08f;
	padding: 10px;
	color: #fff;
}

#navigation li a:hover {
	background: #def
}

.page {
	padding: 10px;
	background: #def;
	height: 250px
}

#content {
	overflow: hidden;
	height: 250px
}

#footer a {
	float: right;
	color: #999
}
</style>
</head>

<body>
	<div class="container">
		<%@ include file="upperMenu.jsp"%>

		<div class="jumbotron">
			<div class="container-fluid">
				<div class="row-fluid">
					<div class="span2">
						<ul class="nav nav-list pull-left">
							<li class="nav-header">Opciones</li>
							<li><a href="listAll">Listar Todos</a></li>
							<li class="active"><a href="#">Nueva Subscripcion</a></li>
						</ul>
					</div>
					<div class="span10">
						<ul id="navigation">
							<li><a href="#Deporte">Deporte</a></li>
							<li><a href="#Curso">Curso</a></li>
							<li><a href="#Otro">Otro</a></li>
						</ul>
						<br clear="all">
							<div id="content">
								<div id="Deporte" class="page">
									<form:form method="post" action="register"
										commandName="registerEnrollmentForm" class="form-horizontal">
										<div class="control-group">
											<label class="control-label" for="inputEmail">Legajo</label>
											<div class="controls">
												<form:input name="legacy" path="legacy" type="text"
													placeHolder="Legajo" />
											</div>
										</div>
										<div class="control-group">
											<form:errors path="legacy" class="error alert alert-error" />
										</div>
										<div class="control-group">
											<label class="control-label" for="inputEmail">Deporte</label>
											<form:select name="serviceName" path="serviceName">
												<c:forEach var="service" items="${sports}"
													varStatus="rowCounter">
													<option value="${service.name}">${service.name}</option>
												</c:forEach>
											</form:select>
										</div>
										<div class="control-group">
											<div class="controls">
												<button type="submit" class="btn">Crear</button>
											</div>
										</div>
									</form:form>
								</div>
								<div id="Curso" class="page">
									<form:form method="post" action="register"
										commandName="registerEnrollmentForm" class="form-horizontal">
										<div class="control-group">
											<label class="control-label" for="inputEmail">Legajo</label>
											<div class="controls">
												<form:input name="legacy" path="legacy" type="text"
													placeHolder="Legajo" />
											</div>
										</div>
										<div class="control-group">
											<form:errors path="legacy" class="error alert alert-error" />
										</div>
										<div class="control-group">
											<label class="control-label" for="inputEmail">Cursos</label>
											<form:select name="serviceName" path="serviceName">
												<c:forEach var="service" items="${courses}"
													varStatus="rowCounter">
													<option value="${service.name}">${service.name}</option>
												</c:forEach>
											</form:select>
										</div>
										<div class="control-group">
											<div class="controls">
												<button type="submit" class="btn">Crear</button>
											</div>
										</div>
									</form:form>
								</div>
								<div id="Otro" class="page">
									<form:form method="post" action="register"
										commandName="registerEnrollmentForm" class="form-horizontal">
										<div class="control-group">
											<label class="control-label" for="inputEmail">Legajo</label>
											<div class="controls">
												<form:input name="legacy" path="legacy" type="text"
													placeHolder="Legajo" />
											</div>
										</div>
										<div class="control-group">
											<form:errors path="legacy" class="error alert alert-error" />
										</div>
										<div class="control-group">
											<label class="control-label" for="inputEmail">Otro</label>
											<form:select name="serviceName" path="serviceName">
												<c:forEach var="service" items="${others}"
													varStatus="rowCounter">
													<option value="${service.name}">${service.name}</option>
												</c:forEach>
											</form:select>
										</div>
										<div class="control-group">
											<div class="controls">
												<button type="submit" class="btn">Crear</button>
											</div>
										</div>
									</form:form>
								</div>
							</div>
					</div>
				</div>
			</div>
		</div>
		<div class="footer">
			<p>&copy; CEITBA 2013</p>
		</div>

	</div>
</body>
</html>
