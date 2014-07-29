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
							<li><a href="listAll">Listar pagos</a></li>
							<li class="active"><a href="debts">Listar deudas</a></li>
						</ul>
					</div>
					<c:if test="${error==true}">
						<div class="alert alert-danger"> Ha ocurrido un error en el pago </div>
					</c:if>
					<div class="span10">
						<h2 class="text-center">Filtrar Busqueda</h2>
						<form action="listAll" method="get" name="listAll">
							<input type="text" class="form-control"
								placeholder="Inicio formato dd/mm/aaaa" name="start">
							<input type="text" class="form-control"
								placeholder="Fin formato dd/mm/aaaa" name="end">
							<input type="text" class="form-control"
								placeholder="Legajo" name="legacy">
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
									<th>Motivo</th>
									<th>Detalle de usuario</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="debt" items="${debts}"
									varStatus="rowCounter">
									<tr>
										<td>${debt.person.legacy}</td>
										<td>${debt.person.firstName}</td>
										<td>${debt.person.lastName}</td>
										<td>${debt.amount}</td>
										<td>${debt.formatedDate}</td>
										<td>${debt.reason}</td>
										<td>
											<form:form method="post" action="pay"
										commandName="createPaymentForm" class="form-horizontal">
												<form:input name="debtId" path="debtId" type="hidden"
												value="${debt.id}" />
												<button type="submit" class="btn">Pagar</button>
											</form:form>
										</td>
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