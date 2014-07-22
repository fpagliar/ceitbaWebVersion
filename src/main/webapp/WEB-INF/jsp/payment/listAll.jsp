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
							<li class="active"><a href="listAll">Listar todos</a></li>
							<li><a href="register?service_id=${service.id}">Nueva asistencia</a></li>
						</ul>
					</div>
					<div class="span10">
						<h2 class="text-center">Filtrar Busqueda</h2>
						<form action="listAll" method="get" name="listAll">
							<input type="text" class="form-control"
								placeholder="Inicio formato dd/mm/aaaa" name="start">
							<input type="text" class="form-control"
								placeholder="Fin formato dd/mm/aaaa" name="end">
								<button type="submit">
									<i class="icon-search"></i>
								</button>
						</form>
						<table class="table table-striped pull-right">
							<thead>
								<tr>
									<th>Legajo</th>
									<th>Nombre</th>
									<th>Apellido</th>
									<th>Monto</th>
									<th>Fecha de deuda</th>
									<th>Fecha de pago</th>
									<th>Detalle de usuario</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="payment" items="${payments}"
									varStatus="rowCounter">
									<tr>
										<td>${payment.person.legacy}</td>
										<td>${payment.person.firstName}</td>
										<td>${payment.person.lastName}</td>
										<td>${payment.amount}</td>
										<td>${payment.formatedPaymentDate}</td>
										<td>${payment.formatedDebtDate}</td>
										<td><a href="listAll?person=${payment.person.id}"><i
												class="icon-edit"></i></a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
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