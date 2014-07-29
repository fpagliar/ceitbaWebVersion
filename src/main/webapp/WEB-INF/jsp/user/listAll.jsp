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
							<li class="active"><a href="#">Listar Todos</a></li>
							<li><a href="register">Nuevo Usuario</a></li>
							<li><a href="update">Mi perfil</a></li>
						</ul>
					</div>
					<div class="span10">
						<c:if test="${successMsg != null}">
							<div class="alert alert-success">${successMsg}</div>
						</c:if>
						<table class="table table-striped pull-right">
							<thead>
								<tr>
									<th>Usuario</th>
									<th>Rol</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="user" items="${users}"
									varStatus="rowCounter">
									<tr>
										<td>${user.username}</td>
										<c:if test="${user.level == 'ADMINISTRATOR'}"> 
											<td>Administrador</td>
										</c:if>
										<c:if test="${user.level == 'MODERATOR'}"> 
											<td>Moderador</td>
										</c:if>
										<c:if test="${user.level == 'REGULAR'}"> 
											<td>Regular</td>
										</c:if>
										<c:if test="${admin}">
											<td>
												<a href="update?id=${user.id}">Editar</a>
											</td>
										</c:if>
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

