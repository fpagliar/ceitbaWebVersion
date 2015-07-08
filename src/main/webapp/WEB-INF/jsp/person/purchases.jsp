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
		<!-- Jumbotron -->
		<div class="jumbotron">
			<div class="container-fluid">
				<div class="row-fluid">
					<div class="span2">
						<%@ include file="sideMenu.jsp"%>
					</div>
					<div class="span8">
						<h2>Productos pendientes</h2>
						<hr/>
						<c:if test="${fn:length(pendingPurchases) == 0}">
							<h5>El usuario no tiene productos pendientes</h5>
						</c:if>
						<c:if test="${fn:length(pendingPurchases) != 0}">
							<table class="table table-striped pull-right">
								<thead>
									<tr>
										<th>Nombre</th>
										<th>Fecha</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="purchase" items="${pendingPurchases}" varStatus="rowCounter">
										<tr>
											<td>${purchase.product.name}</td>
											<td>${purchase.formattedDate}</td>
											<td>
												<form action="reimburse" method="post">
													<input name="id" path="id" type="hidden" value="${purchase.id}" />
													<button type="submit" class="btn btn-default" 
													    onClick='return confirm("Esta seguro que desea eliminar esta compra?")'
													    style="border:none; background:none; padding:0;">
														<i class="icon-remove"></i>
													</button>
												<form>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>

						<h2>Historial de productos</h2>
						<hr/>
						<c:if test="${fn:length(billedPurchases) == 0}">
							<h5>El usuario no tiene productos historicos</h5>
						</c:if>
						<c:if test="${fn:length(billedPurchases) != 0}">
							<table class="table table-striped pull-right">
								<thead>
									<tr>
										<th>Elemento</th>
										<th>Fecha</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="purchase" items="${billedPurchases}" varStatus="rowCounter">
										<tr>
											<td>${purchase.product.name}</td>
											<td>${purchase.formatedDate}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>
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