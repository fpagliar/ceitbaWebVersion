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
							<li><a href="listNewEnrollments">Altas</a></li>
							<li><a href="listCancelledEnrollments">Bajas</a></li>
							<li class="active"><a href="#">Anotados</a></li>
							<li><a href="pendingPurchases">Compras Pendientes</a></li>
							<li><a href="listDebts">Deudas en efectivo</a></li>
						</ul>
					</div>
					<div class="span9">
						<h2 class="text-center">Filtrar Busqueda</h2>
						<form action="listEnrolled" method="get" name="listEnrolled" class="form-horizontal">
							<div class="control-group">
								<label class="control-label" for="start">Inicio</label>
								<div class="controls">
									<input type="text" class="form-control" placeholder="formato dd/mm/aaaa" name="start">
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="end">Fin</label>
								<div class="controls">
									<input type="text" class="form-control" placeholder="formato dd/mm/aaaa" name="end">
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="service_id">Servicio</label>
								<div class="controls">
									<select name="service_id">
										<c:forEach var="service" items="${services}" varStatus="rowCounter">
											<option value="${service.id}"> ${service.name} </option>
										</c:forEach>
									</select>
								</div>
							</div>
							<button type="submit" class="btn">Listar</button>
						</form>
						<h4 class="text-center">Busqueda: ${query} </h4>
						<table class="table table-striped pull-right">
							<thead>
								<tr>
									<th>Legajo</th>
									<th>Nombre</th>
									<th>Apellido</th>
									<th>Fecha de inscripcion</th>
									<th>Servicio</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="enrollment" items="${enrolled}"
									varStatus="rowCounter">
									<tr>
										<td>${enrollment.person.legacy}</td>
										<td>${enrollment.person.firstName}</td>
										<td>${enrollment.person.lastName}</td>
										<td>${enrollment.formatedStartDate}</td>
										<td>${enrollment.service.name}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="span1">
						<a href="downloadBilling">
							<button type="submit" class="btn"> Descargar a Excel </button>
						</a>
					</div>
					<div>
						&nbsp;
					</div>
					<div class="span1">
						<form action="billCashPayments" method="post">
							<button type="submit" class="btn" onClick='return confirm("Esta accion va facturar a todos las personas que estan anotadas en un servicio y abonan en efectivo. Desea seguir?")'>Facturar efectivo</button>
						<form>
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