<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<%@ include file="../template/headGeneral.jsp"%>
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
							<li><a href="listAll">Listar Todas</a></li>
							<li class="active"><a href="#">Nueva asistencia</a></li>
						</ul>
					</div>
					<div class="span10">
						<div id="content">
							<div class="page">
								<form:form method="post" action="register"
									commandName="registerAssistanceForm" class="form-horizontal">
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
										<form:input name="serviceName" path="serviceName" type="hidden"
											 value="${service.name}"/>
									</div>
									<div class="control-group">
										<form:errors path="serviceName" class="error alert alert-error" />
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