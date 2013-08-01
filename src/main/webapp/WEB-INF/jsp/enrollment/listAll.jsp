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
			<h2>Filtrar Busqueda</h2>
			<form class="form-search" action="listAll" method="get" name="search">
				Usuarios:
				<input type="text" class="input-medium search-query" placeholder="Nombre" name="search">
					<button type="submit">
						<i class="icon-search"></i>
					</button>
			</form>
			
			<form method="get" action="listAll" class="form-horizontal">
				Cursos:
				<select>
					<c:forEach var="service" items="${courses}" varStatus="rowCounter">
						<option value="${service.name}">${service.name}</option>
					</c:forEach>
				</select>
				<button type="submit">
						<i class="icon-search"></i>
				</button>
			</form>
			<form method="get" action="listAll" class="form-horizontal">
				Deportes:
				<select>
					<c:forEach var="service" items="${sports}" varStatus="rowCounter">
						<option value="${service.name}">${service.name}</option>
					</c:forEach>
				</select>
				<button type="submit">
						<i class="icon-search"></i>
				</button>
			</form>
			<form method="get" action="listAll" class="form-horizontal">
				Lockers:
				<select>
					<c:forEach var="service" items="${lockers}" varStatus="rowCounter">
						<option value="${service.name}">${service.name}</option>
					</c:forEach>
				</select>
				<button type="submit">
						<i class="icon-search"></i>
				</button>
			</form>
			<form method="get" action="listAll" class="form-horizontal">
				Otros:
				<select>
					<c:forEach var="service" items="${others}" varStatus="rowCounter">
						<option value="${service.name}">${service.name}</option>
					</c:forEach>
				</select>
				<button type="submit">
						<i class="icon-search"></i>
				</button>
			</form>
			<div class="container-fluid">
				<div class="row-fluid">
					<div class="span2">
						<ul class="nav nav-list pull-left">
							<li class="nav-header">Opciones</li>
							<c:if test="${history==true}">
								<li><a href="listAll">Listar Activas</a></li>
								<li class="active"><a href="listAll?list=history">Listar Inactivas</a></li>
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
										<td><a href="update?id=${enrollment.id}"><i class="icon-edit"></i></a></td>
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