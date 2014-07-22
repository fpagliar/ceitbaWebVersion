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
							<c:if test="${list==null}">
								<li class="active"><a href="listAll">Listar Todos</a></li>
								<li><a href="listAll?list=active">Listar Activos</a></li>
								<li><a href="listAll?list=inactive">Listar Inactivos</a></li>
								<li><a href="listAll?list=SPORT">Listar Deportes</a></li>
								<li><a href="listAll?list=COURSE">Listar Cursos</a></li>
								<li><a href="listAll?list=LOCKER">Listar Lockers</a></li>
								<li><a href="listAll?list=OTHER">Listar Otros Servicios</a></li>
							</c:if>
							<c:if test="${list=='active'}">
								<li><a href="listAll">Listar Todos</a></li>
								<li class="active"><a href="listAll?list=active">Listar
										Activos</a></li>
								<li><a href="listAll?list=inactive">Listar Inactivos</a></li>
								<li><a href="listAll?list=SPORT">Listar Deportes</a></li>
								<li><a href="listAll?list=COURSE">Listar Cursos</a></li>
								<li><a href="listAll?list=LOCKER">Listar Lockers</a></li>
								<li><a href="listAll?list=OTHER">Listar Otros Servicios</a></li>
							</c:if>
							<c:if test="${list=='inactive'}">
								<li><a href="listAll">Listar Todos</a></li>
								<li><a href="listAll?list=active">Listar Activos</a></li>
								<li class="active"><a href="listAll?list=inactive">Listar
										Inactivos</a></li>
								<li><a href="listAll?list=SPORT">Listar Deportes</a></li>
								<li><a href="listAll?list=COURSE">Listar Cursos</a></li>
								<li><a href="listAll?list=LOCKER">Listar Lockers</a></li>
								<li><a href="listAll?list=OTHER">Listar Otros Servicios</a></li>
							</c:if>
							<c:if test="${list=='SPORT'}">
								<li><a href="listAll">Listar Todos</a></li>
								<li><a href="listAll?list=active">Listar Activos</a></li>
								<li><a href="listAll?list=inactive">Listar Inactivos</a></li>
								<li class="active"><a href="listAll?list=SPORT">Listar
										Deportes</a></li>
								<li><a href="listAll?list=COURSE">Listar Cursos</a></li>
								<li><a href="listAll?list=LOCKER">Listar Lockers</a></li>
								<li><a href="listAll?list=OTHER">Listar Otros Servicios</a></li>
							</c:if>
							<c:if test="${list=='COURSE'}">
								<li><a href="listAll">Listar Todos</a></li>
								<li><a href="listAll?list=active">Listar Activos</a></li>
								<li><a href="listAll?list=inactive">Listar Inactivos</a></li>
								<li><a href="listAll?list=SPORT">Listar Deportes</a></li>
								<li class="active"><a href="listAll?list=COURSE">Listar
										Cursos</a></li>
								<li><a href="listAll?list=LOCKER">Listar Lockers</a></li>
								<li><a href="listAll?list=OTHER">Listar Otros Servicios</a></li>
							</c:if>
							<c:if test="${list=='LOCKER'}">
								<li><a href="listAll">Listar Todos</a></li>
								<li><a href="listAll?list=active">Listar Activos</a></li>
								<li><a href="listAll?list=inactive">Listar Inactivos</a></li>
								<li><a href="listAll?list=SPORT">Listar Deportes</a></li>
								<li><a href="listAll?list=COURSE">Listar Cursos</a></li>
								<li class="active"><a href="listAll?list=LOCKER">Listar
										Lockers</a></li>
								<li><a href="listAll?list=OTHER">Listar Otros Servicios</a></li>
							</c:if>
							<c:if test="${list=='OTHER'}">
								<li><a href="listAll">Listar Todos</a></li>
								<li><a href="listAll?list=active">Listar Activos</a></li>
								<li><a href="listAll?list=inactive">Listar Inactivos</a></li>
								<li><a href="listAll?list=SPORT">Listar Deportes</a></li>
								<li><a href="listAll?list=COURSE">Listar Cursos</a></li>
								<li><a href="listAll?list=LOCKER">Listar Lockers</a></li>
								<li class="active"><a href="listAll?list=OTHER">Listar
										Otros Servicios</a></li>
							</c:if>
							<li><a href="register">Nuevo Servicio</a></li>
						</ul>
					</div>
					<div class="span10">
						<form class="form-search" action="listAll" method="get"
							name="search">
							<input type="text" class="input-medium search-query"
								placeholder="Filtrar resultados" name="search">
								<button type="submit">
									<i class="icon-search"></i>
								</button>
						</form>
						<table class="table table-striped pull-right">
							<thead>
								<tr>
									<th>Nombre</th>
									<th>Tipo</th>
									<th>Precio</th>
									<th>Estado</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="service" items="${services}"
									varStatus="rowCounter">
									<tr>
										<td>${service.name}</td>
										<c:if test="${service.type == 'OTHER'}">
											<td>Otro</td>
										</c:if>
										<c:if test="${service.type == 'SPORT'}">
											<td>Deporte</td>
										</c:if>
										<c:if test="${service.type == 'COURSE'}">
											<td>Curso</td>
										</c:if>
										<c:if test="${service.type == 'LOCKER'}">
											<td>Locker</td>
										</c:if>
										<td>${service.value}</td>
										<td>${service.status}</td>
										<td><a href="update?id=${service.id}"><i
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

