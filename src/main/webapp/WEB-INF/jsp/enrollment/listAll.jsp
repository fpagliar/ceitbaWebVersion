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
								<li class="active"><a href="listAll?list=history">Historial</a></li>
								<li><a href="register">Nueva subscripcion</a></li>
							</c:if>
							<c:if test="${service!=null}">
								<li><a href="listAll">Listar Activas</a></li>
								<li><a href="listAll?list=history">Historial</a></li>
								<li class="active"><a href="#">${service}</a></li>
								<li><a href="register">Nueva subscripcion</a></li>
							</c:if>
							<c:if test="${search==true}">
								<li><a href="listAll">Listar Activas</a></li>
								<li><a href="listAll?list=history">Historial</a></li>
								<li class="active"><a href="#">Busqueda</a></li>
								<li><a href="register">Nueva subscripcion</a></li>
							</c:if>
							<c:if test="${search!=true&&service==null&&history!=true}">
								<li class="active"><a href="listAll">Listar Activas</a></li>
								<li><a href="listAll?list=history">Historial</a></li>
								<li><a href="register">Nueva subscripcion</a></li>
							</c:if>
						</ul>
					</div>
					<div class="span10">
						<!--
						<h2 class="text-center">Filtrar Busqueda</h2>
						<form action="listAll" method="get" name="search">
							<c:if test="${history==true}">
								<input type="hidden" class="form-control"
									name="list" value="history">
							</c:if>
							<input type="text" class="form-control"
								placeholder="Nombre, legajo o servicio" name="search">
								<button type="submit">
									<i class="icon-search"></i>
								</button>
						</form>
						-->
						<table class="table table-striped pull-right">
							<thead>
								<tr>
									<th>Legajo</th>
									<th>Nombre</th>
									<th>Apellido</th>
									<th>Servicio</th>
									<th>Alta</th>
									<th>Baja</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${historyEnrollments!=null}">
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td><strong>Historial</strong></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
									<c:forEach var="enrollment" items="${historyEnrollments.elements}"
										varStatus="rowCounter">
										<tr>
											<td>${enrollment.person.legacy}</td>
											<td>${enrollment.person.firstName}</td>
											<td>${enrollment.person.lastName}</td>
											<td>${enrollment.service.name}</td>
											<td>${enrollment.formatedStartDate}</td>
											<td>${enrollment.formatedEndDate}</td>
											<td><a href="show?id=${enrollment.id}"><i
													class="icon-edit"></i></a></td>
										</tr>
									</c:forEach>
									<div class="row-fluid">
										<div class="span4">
											<c:if test="${historyEnrollments.actualPage > 1}">
												<a href="listAll?page=${historyEnrollments.actualPage-1}"> 
													<button class="btn-large">Anterior</button> 
												</a>
											</c:if>
										</div>
										<div class="span4">
											<h3> Pagina ${historyEnrollments.actualPage} de ${historyEnrollments.totalPage} </h3>
										</div>
										<div class="span4">
											<c:if test="${historyEnrollments.actualPage < historyEnrollments.totalPage}">
												<a href="listAll?page=${historyEnrollments.actualPage+1}"> 
													<button class="btn-large">Siguiente</button> 
												</a>
											</c:if>
										</div>
									</div>
								</c:if>
								<c:forEach var="enrollment" items="${enrollments.elements}"
									varStatus="rowCounter">
									<tr>
										<td>${enrollment.person.legacy}</td>
										<td>${enrollment.person.firstName}</td>
										<td>${enrollment.person.lastName}</td>
										<td>${enrollment.service.name}</td>
										<td>${enrollment.formatedStartDate}</td>
										<td>${enrollment.formatedEndDate}</td>
										<td><a href="show?id=${enrollment.id}"><i
												class="icon-edit"></i></a></td>
									</tr>
								</c:forEach>
								<!--
								<c:if test="${serviceEnrollments!=null}">
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td><strong>Servicios</strong></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
									<c:forEach var="enrollment" items="${serviceEnrollments.elements}"
										varStatus="rowCounter">
										<tr>
											<td>${enrollment.person.legacy}</td>
											<td>${enrollment.person.firstName}</td>
											<td>${enrollment.person.lastName}</td>
											<td>${enrollment.service.name}</td>
											<td>${enrollment.formatedStartDate}</td>
											<td>${enrollment.formatedEndDate}</td>
											<td><a href="show?id=${enrollment.id}"><i
													class="icon-edit"></i></a></td>
										</tr>
									</c:forEach>
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td><strong>Usuarios</strong></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
									<c:forEach var="enrollment" items="${personEnrollments.elements}"
										varStatus="rowCounter">
										<tr>
											<td>${enrollment.person.legacy}</td>
											<td>${enrollment.person.firstName}</td>
											<td>${enrollment.person.lastName}</td>
											<td>${enrollment.service.name}</td>
											<td>${enrollment.formatedStartDate}</td>
											<td>${enrollment.formatedEndDate}</td>
											<td><a href="show?id=${enrollment.id}"><i
													class="icon-edit"></i></a></td>
										</tr>
									</c:forEach>
								</c:if>
								-->
							</tbody>
						</table>
						<c:if test="${enrollments!=null}">
							<div class="row-fluid">
								<div class="span4">
									<c:if test="${enrollments.actualPage > 1}">
										<a href="listAll?page=${enrollments.actualPage-1}"> 
											<button class="btn-large">Anterior</button> 
										</a>
									</c:if>
								</div>
								<div class="span4">
									<h3> Pagina ${enrollments.actualPage} de ${enrollments.totalPage} </h3>
								</div>
								<div class="span4">
									<c:if test="${enrollments.actualPage < enrollments.totalPage}">
										<a href="listAll?page=${enrollments.actualPage+1}"> 
											<button class="btn-large">Siguiente</button> 
										</a>
									</c:if>
								</div>
							</div>
						</c:if>		
						<c:if test="${historyEnrollments!=null}">
							<div class="row-fluid">
								<div class="span4">
									<c:if test="${historyEnrollments.actualPage > 1}">
										<a href="listAll?page=${historyEnrollments.actualPage-1}"> 
											<button class="btn-large">Anterior</button> 
										</a>
									</c:if>
								</div>
								<div class="span4">
									<h3> Pagina ${historyEnrollments.actualPage} de ${historyEnrollments.totalPage} </h3>
								</div>
								<div class="span4">
									<c:if test="${historyEnrollments.actualPage < historyEnrollments.totalPage}">
										<a href="listAll?page=${historyEnrollments.actualPage+1}"> 
											<button class="btn-large">Siguiente</button> 
										</a>
									</c:if>
								</div>
							</div>
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