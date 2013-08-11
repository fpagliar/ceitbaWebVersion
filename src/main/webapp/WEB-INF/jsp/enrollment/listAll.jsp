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
							<c:if test="${history==true}">
								<li><a href="listAll">Listar Activas</a></li>
								<li class="active"><a href="listAll?list=history">Listar
										Inactivas</a></li>
								<li><a href="register">Nueva subscripcion</a></li>
							</c:if>
							<c:if test="${service!=null}">
								<li><a href="listAll">Listar Activas</a></li>
								<li><a href="listAll?list=history">Listar Inactivas</a></li>
								<li class="active"><a href="#">${service}</a></li>
								<li><a href="register">Nueva subscripcion</a></li>
							</c:if>
							<c:if test="${search==true}">
								<li><a href="listAll">Listar Activas</a></li>
								<li><a href="listAll?list=history">Listar Inactivas</a></li>
								<li class="active"><a href="#">Busqueda</a></li>
								<li><a href="register">Nueva subscripcion</a></li>
							</c:if>
							<c:if test="${search!=true&&service==null&&history!=true}">
								<li class="active"><a href="listAll">Listar Activas</a></li>
								<li><a href="listAll?list=history">Listar Inactivas</a></li>
								<li><a href="register">Nueva subscripcion</a></li>
							</c:if>
						</ul>
					</div>
					<div class="span10">
						<h2 class="text-center">Filtrar Busqueda</h2>
						<form action="listAll" method="get" name="search">
							<input type="text" class="form-control"
								placeholder="Nombre, legajo o servicio" name="search">
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
									<th>Servicio</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="enrollment" items="${enrollments}"
									varStatus="rowCounter">
									<tr>
										<td>${enrollment.person.legacy}</td>
										<td>${enrollment.person.firstName}</td>
										<td>${enrollment.person.lastName}</td>
										<td>${enrollment.service.name}</td>
										<td><a href="show?id=${enrollment.id}"><i
												class="icon-edit"></i></a></td>
									</tr>
								</c:forEach>
								<c:if test="${serviceEnrollments!=null}">
										<tr>
											<td></td>
											<td></td>
											<td><strong>Servicios</strong></td>
											<td></td>
											<td></td>
										</tr>
									<c:forEach var="enrollment" items="${serviceEnrollments}"
										varStatus="rowCounter">
										<tr>
											<td>${enrollment.person.legacy}</td>
											<td>${enrollment.person.firstName}</td>
											<td>${enrollment.person.lastName}</td>
											<td>${enrollment.service.name}</td>
											<td><a href="show?id=${enrollment.id}"><i
													class="icon-edit"></i></a></td>
										</tr>
									</c:forEach>
										<tr>
											<td></td>
											<td></td>
											<td><strong>Usuarios</strong></td>
											<td></td>
											<td></td>
										</tr>
									<c:forEach var="enrollment" items="${personEnrollments}"
										varStatus="rowCounter">
										<tr>
											<td>${enrollment.person.legacy}</td>
											<td>${enrollment.person.firstName}</td>
											<td>${enrollment.person.lastName}</td>
											<td>${enrollment.service.name}</td>
											<td><a href="show?id=${enrollment.id}"><i
													class="icon-edit"></i></a></td>
										</tr>
									</c:forEach>
								</c:if>
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