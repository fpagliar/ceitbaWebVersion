<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<!--  TODO: fix relative paths problem in header!!! -->
<%@ include file="../template/headGeneral.jsp"%>
</head>

<body>

	<div class="container">
		<%@ include file="upperMenu.jsp"%>

		<!-- Jumbotron -->
		<div class="jumbotron">
			<div class="container-fluid">
				<div class="row-fluid">
					<div class="span2">
						<ul class="nav nav-list pull-left">
							<li class="nav-header">Opciones</li>
							<li><a href="listAll">Listar Todos</a></li>
							<li class="active"><a href="#">Nuevo Usuario</a></li>
						</ul>
					</div>
					<div class="span10">
						<form:form method="post" action="register" commandName="registerPersonForm" class="form-horizontal">
							<div class="control-group">
								<label class="control-label" for="inputEmail">Legajo</label>
								<div class="controls">
										<form:input name="legacy" path="legacy" type="text" value="${person.legacy}"/>
								</div>
							</div>
							<div class="control-group">
 										<form:errors path="legacy" class="error alert alert-error"/>
 							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Nombre</label>
								<div class="controls">
										<form:input name="firstName" path="firstName" type="text" placeholder="Nombre" value="${person.firstName}"/>
								</div>
							</div>
							<div class="control-group">
									   <form:errors path="firstName" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Apellido</label>
								<div class="controls">
									<form:input name="lastName" path="lastName" type="text" placeholder="Apellido" value="${person.lastName}"/>
								</div>
							</div>
							<div class="control-group">
									<form:errors path="lastName" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Email</label>
								<div class="controls">
									<form:input name="email" path="email" type="text" placeholder="Email" value="${person.email}"/>
								</div>
							</div>
							<div class="control-group">
									<form:errors path="email" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Telefono</label>
								<div class="controls">
									<form:input name="phone" path="phone" type="text" placeholder="Telefono" value="${person.phone}"/>
								</div>
							</div>
							<div class="control-group">
									<form:errors path="phone" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Celular</label>
								<div class="controls">
									<form:input name="cellphone" path="cellphone" type="text" placeholder="Celular" value="${person.cellphone}"/>
								</div>
							</div>
							<div class="control-group">
									<form:errors path="cellphone" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">DNI</label>
								<div class="controls">
									<form:input name="dni" path="dni" type="text" placeholder="DNI" value="${person.dni}"/>
								</div>
							</div>
							<div class="control-group">
									<form:errors path="dni" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Email2</label>
								<div class="controls">
									<form:input name="email2" path="email2" type="text" placeholder="Email2" value="${person.email2}"/>
								</div>
							</div>
							<div class="control-group">
									<form:errors path="email2" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="paymentMethod">Medio de pago</label>
								<div class="controls">
									<form:select name="paymentMethod" path="paymentMethod">
										<form:option value="CASH"> Efectivo </form:option>
										<form:option value="BILL" selected="selected"> Factura </form:option>
									</form:select>
								</div>
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

		<div class="footer">
			<p>CEITBA 2014</p>
		</div>

	</div>
</body>
</html>