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
							<li><a href="listEnrolled">Anotados</a></li>
							<li class="active"><a href="pendingPurchases">Compras Pendientes</a></li>
							<li><a href="listDebts">Deudas en efectivo</a></li>
						</ul>
					</div>
					<div class="span9">
						<table class="table table-striped pull-right">
							<thead>
								<tr>
									<th>Legajo</th>
									<th>Nombre</th>
									<th>Apellido</th>
									<th>Fecha de Compra</th>
									<th>Monto</th>
									<th>Motivo</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>FACTURA</td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<c:forEach var="purchase" items="${billPurchases}" varStatus="rowCounter">
									<tr>
										<td>${purchase.person.legacy}</td>
										<td>${purchase.person.firstName}</td>
										<td>${purchase.person.lastName}</td>
										<td>${purchase.formattedDate}</td>
										<td>${purchase.product.value}</td>
										<td>${purchase.product.name}</td>
									</tr>
								</c:forEach>
								<tr>
									<td>EFECTIVO</td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<c:forEach var="purchase" items="${cashPurchases}" varStatus="rowCounter">
									<tr>
										<td>${purchase.person.legacy}</td>
										<td>${purchase.person.firstName}</td>
										<td>${purchase.person.lastName}</td>
										<td>${purchase.formattedDate}</td>
										<td>${purchase.product.value}</td>
										<td>${purchase.product.name}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="span1">
						<div class="row-fluid">
							<a href="downloadBilling">
								<button type="submit" class="btn"> Descargar a Excel </button>
							</a>
						</div>
						<div>
							&nbsp;
						</div>
						<div class="row-fluid">
							<form action="billCashPayments" method="post">
								<button type="submit" class="btn" onClick='return confirm("Esta accion va facturar a todos las personas que estan anotadas en un servicio y abonan en efectivo. Desea seguir?")'>Facturar efectivo</button>
							</form>
						</div>
						<div class="row-fluid">
							<form action="clearBilledPurchases" method="post">
								<button type="submit" class="btn" onClick='return confirm("Esta accion va a archivar todas las compras efectuadas excepto las compras que se abonan en efectivo. Desea seguir?")'>Archivar compras facturadas</button>
							</form>
						</div>
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