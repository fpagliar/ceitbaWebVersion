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
							<li><a href="listAll">Listar Todos</a></li>
							<li><a href="register">Nuevo Usuario</a></li>
							<li><a href="update">Mi perfil</a></li>
							<li class="active"><a href="#">Listar acciones</a></li>
						</ul>
					</div>
					<div class="span10">
						<h2 class="text-center">Filtrar Busqueda</h2>
						<form action="listActions" method="get" name="listActions" class="form-horizontal">
							<div class="control-group">
								<label class="control-label" for="start">Inicio</label>
								<div class="controls">
									<input type="text" class="form-control"
										placeholder="formato dd/mm/aaaa" name="start">
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="end">Fin</label>
								<div class="controls">
									<input type="text" class="form-control"
										placeholder="formato dd/mm/aaaa" name="end">
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="user">Usuario</label>
								<div class="controls">
									<select name="user">
										<option value=""> Todos </option>
										<c:forEach var="user" items="${users}" varStatus="rowCounter">
											<option value="${user.id}"> ${user.username} </option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="controller">Controller</label>
								<div class="controls">
									<select name="controller">
										<option value=""> Todos </option>
										<c:forEach var="controller" items="${controllers}" varStatus="rowCounter">
											<option value="${controller}"> ${controller} </option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="action">Tipo de accion</label>
								<div class="controls">
									<select name="action">
										<option value=""> Todas </option>
										<c:forEach var="action" items="${userActions}" varStatus="rowCounter">
											<option value="${action}"> ${action} </option>
										</c:forEach>
									</select>
								</div>
							</div>
							<button type="submit" class="btn">Listar</button>
						</form>
						<table class="table table-striped pull-right">
							<thead>
								<tr>
									<th>Usuario</th>
									<th>Controller</th>
									<th>Accion</th>
									<th>Clase</th>
									<th>Fecha</th>
									<th>Anterior</th>
									<th>Posterior</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="action" items="${actions}"
									varStatus="rowCounter">
									<tr>
										<td>${action.user.username}</td>
										<td>${action.controller}</td>
										<td>${action.action}</td>
										<td>${action.className}</td>
										<td>${action.formattedDate}</td>
										<td>${action.previous}</td>
										<td>${action.subsequent}</td>
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
