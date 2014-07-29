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
							<li class="active"><a href="#">Nuevo Usuario</a></li>
							<li ><a href="update">Mi Perfil</a></li>
						</ul>
					</div>
					<div class="span5">
						<c:if test="${new==true}">
							<div class="alert alert-success">${newmsg}</div>
						</c:if>
						<form:form method="post" action="register"
							commandName="registerUserForm" class="form-horizontal">

							<div class="control-group">
								<label class="control-label disabled" for="username">Nombre de usuario</label>
								<div class="controls">
									<form:input name="username" path="username" type="text"
										value="" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="username" class="error alert alert-error" />
							</div>

							<div class="control-group">
								<label class="control-label" for="password">Password</label>
								<div class="controls">
									<form:input name="password" path="password" type="password"
										value="" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="password" class="error alert alert-error" />
							</div>

							<div class="control-group">
								<label class="control-label" for="rePassword"> Repetir password</label>
								<div class="controls">
									<form:input name="rePassword" path="rePassword" type="password"/>
								</div>
							</div>
							<div class="control-group">
								<form:errors path="rePassword" class="error alert alert-error" />
							</div>

							<div class="control-group">
								<label class="control-label" for="level">Rol</label>
								<form:select name="level" path="level">
									<c:forEach var="a_level" items="${levels}"
										varStatus="rowCounter">
										<option value="${a_level}">${a_level}</option>
									</c:forEach>
								</form:select>
							</div>

							<div class="control-group">
								<form:errors path="level" class="error alert alert-error" />
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
			<p>CEITBA 2014</p>
		</div>

	</div>
</body>
</html>