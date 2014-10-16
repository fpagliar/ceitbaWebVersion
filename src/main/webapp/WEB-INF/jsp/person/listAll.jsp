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
							<c:if test="${search==true}">
								<li><a href="listAll">Listar Todos</a></li>
								<li><a href="register">Nuevo Usuario</a></li>
								<li class="active"><a href="#">Busqueda</a></li>
							</c:if>
							<c:if test="${search!=true}">
								<li class="active"><a href="listAll">Listar Todos</a></li>
								<li><a href="register">Nuevo Usuario</a></li>
							</c:if>
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
									<th>Legajo</th>
									<th>Nombre</th>
									<th>Apellido</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="person" items="${persons.elements}"
									varStatus="rowCounter">
									<tr>
										<td>${person.legacy}</td>
										<td>${person.firstName}</td>
										<td>${person.lastName}</td>
										<td><a href="update?id=${person.id}"><i
												class="icon-edit"></i></a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<div class="row-fluid">
							<div class="span4">
								<c:if test="${persons.actualPage > 1}">
									<a href="listAll?page=${persons.actualPage-1}&search=${searchParam}"> 
										<button class="btn-large">Anterior</button> 
									</a>
								</c:if>
							</div>
							<div class="span4">
								<h3> Pagina ${persons.actualPage} de ${persons.totalPage} </h3>
							</div>
							<div class="span4">
								<c:if test="${persons.actualPage < persons.totalPage}">
									<a href="listAll?page=${persons.actualPage+1}&search=${searchParam}"> 
										<button class="btn-large">Siguiente</button> 
									</a>
								</c:if>
							</div>
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

