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
							<li><a href="register">Nuevo Usuario</a></li>
							<li class="active"><a href="#">Editar</a></li>
						</ul>
					</div>
					<div class="span5">
						<c:if test="${success==true}">
							<div class="alert alert-success">${successmsg}</div>
						</c:if>
						<c:if test="${neww==true}">
							<div class="alert alert-success">${newmsg}</div>
						</c:if>
						<form:form method="post" action="update" commandName="updatePersonForm" class="form-horizontal">
							<div class="control-group">
								<label class="control-label" for="inputEmail">Legajo</label>
								<div class="controls">
									<form:input name="legacy" path="legacy" type="text" value="${person.legacy}" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="legacy" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Nombre</label>
								<div class="controls">
									<form:input name="firstName" path="firstName" type="text"
										placeholder="Nombre" value="${person.firstName}" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="firstName" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Apellido</label>
								<div class="controls">
									<form:input name="lastName" path="lastName" type="text"
										placeholder="Apellido" value="${person.lastName}" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="lastName" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Email</label>
								<div class="controls">
									<form:input name="email" path="email" type="text"
										placeholder="Email" value="${person.email}" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="email" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Telefono</label>
								<div class="controls">
									<form:input name="phone" path="phone" type="text"
										placeholder="Telefono" value="${person.phone}" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="phone" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Celular</label>
								<div class="controls">
									<form:input name="cellphone" path="cellphone" type="text"
										placeholder="Celular" value="${person.cellphone}" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="cellphone" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">DNI</label>
								<div class="controls">
									<form:input name="dni" path="dni" type="text" placeholder="DNI"
										value="${person.dni}" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="dni" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Email2</label>
								<div class="controls">
									<form:input name="email2" path="email2" type="text"
										placeholder="Email2" value="${person.email2}" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="email2" class="error alert alert-error" />
							</div>
							<form:input name="id" type="hidden" value="${person.id}"
								path="id" />
							<div class="control-group">
								<label class="control-label" for="paymentMethod">Medio de pago</label>
								<div class="controls">
									<form:select name="paymentMethod" path="paymentMethod">
										<c:if test="${isCash}">
											<form:option value="CASH" selected="selected"> Efectivo </form:option>
											<form:option value="BILL"> Factura </form:option>
										</c:if>
										<c:if test="${not isCash}">
											<form:option value="CASH"> Efectivo </form:option>
											<form:option value="BILL" selected="selected"> Factura </form:option>
										</c:if>
									</form:select>
								</div>
							</div>
							<div class="control-group">
								<div class="controls">
									<button type="submit" class="btn">Confirmar</button>
								</div>
							</div>
						</form:form>
					</div>
					<div class="span3 pull-right">
						<h2>Subscripciones</h2>
						<hr/>
						<c:if test="${fn:length(enrollments) == 0}">
							<h5>El usuario no tiene subscripciones vigentes</h5>
						</c:if>
						<table class="table table-striped pull-right">
							<thead>
								<tr>
									<th>Servicio</th>
									<th>Inicio</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="enrollment" items="${enrollments}"
									varStatus="rowCounter">
									<tr>
										<td>${enrollment.service.name}</td>
										<td>${enrollment.formatedStartDate}</td>
										<td><a href="../enrollment/show?id=${enrollment.id}"><i class="icon-remove"></i></a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="span3 pull-right">
						<h2>Historial</h2>
						<hr/>
						<c:if test="${fn:length(history) == 0}">
							<h5>El usuario no tiene subscripciones canceladas</h5>
						</c:if>
						<table class="table table-striped pull-right">
							<thead>
								<tr>
									<th>Servicio</th>
									<th>Inicio</th>
									<th>Fin</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="enrollment" items="${history}"
									varStatus="rowCounter">
									<tr>
										<td>${enrollment.service.name}</td>
										<td>${enrollment.formatedStartDate}</td>
										<td>${enrollment.formatedEndDate}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="span3 pull-right">
						<h2>Productos</h2>
						<hr/>
						<c:if test="${fn:length(purchases) == 0}">
							<h5>El usuario no tiene productos comprados </h5>
						</c:if>
						<table class="table table-striped pull-right">
							<thead>
								<tr>
									<th>Producto</th>
									<th>Fecha</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="purchase" items="${purchases}" varStatus="rowCounter">
									<tr>
										<td>${purchase.product.name}</td>
										<td>${purchase.formatedDate}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
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