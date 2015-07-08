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
						<h2>Historial de subscripciones</h2>
						<hr/>
						<c:if test="${fn:length(enrollments) == 0}">
							<h5>El usuario no tiene subscripciones anteriores</h5>
						</c:if>
						<c:if test="${fn:length(enrollments) != 0}">
							<table class="table table-striped pull-right">
								<thead>
									<tr>
										<th>Servicio</th>
										<th>Inicio</th>
										<th>Fin</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="enrollment" items="${enrollments}" varStatus="rowCounter">
										<tr>
											<td>${enrollment.service.name}</td>
											<td>${enrollment.formatedStartDate}</td>
											<td>${enrollment.formatedEndDate}</td>
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