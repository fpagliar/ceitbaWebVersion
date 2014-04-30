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
							<c:if test="${profile == true}">
								<li class="active"><a href="#">Mi Perfil</a></li>
							</c:if>
							<c:if test="${profile != true}">
								<li class="active"><a href="#">Editar</a></li>
							</c:if>
						</ul>
					</div>
					<div class="span5">
						<c:if test="${successMsg!=null}">
							<div class="alert alert-success">${successMsg}</div>
						</c:if>
 						<form:form method="post" action="update"
							commandName="updateUserForm" class="form-horizontal">

							<div class="control-group">
								<div class="controls">
									<form:input name="currentUsername" path="currentUsername" type="text" class="hidden"
										value="${user.username}" />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label disabled" for="username">Nombre de usuario</label>
								<div class="controls">
									<form:input name="username" path="username" type="text"
										value="${user.username}" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="username" class="error alert alert-error" />
							</div>

							<div class="control-group">
								<label class="control-label" for="oldPassword">Password anterior</label>
								<div class="controls">
									<form:input name="oldPassword" path="oldPassword" type="password"
										value="" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="oldPassword" class="error alert alert-error" />
							</div>

							<div class="control-group">
								<label class="control-label" for="newPassword">Password nueva</label>
								<div class="controls">
									<form:input name="newPassword" path="newPassword" type="password"/>
								</div>
							</div>
							<div class="control-group">
								<form:errors path="newPassword" class="error alert alert-error" />
							</div>

							<div class="control-group">
								<label class="control-label" for="reNewPassword">Repetir password nueva</label>
								<div class="controls">
									<form:input name="reNewPassword" path="reNewPassword" type="password"/>
								</div>
							</div>
							<div class="control-group">
								<form:errors path="reNewPassword" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<div class="controls">
									<button type="submit" class="btn">Confirmar</button>
								</div>
							</div>
						</form:form>
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