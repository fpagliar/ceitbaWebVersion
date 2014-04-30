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
		<div class="jumbotron">
			<div class="container-fluid">
				<div class="row-fluid">
				<c:if test="${error!=null}">
					<div class="error alert alert-error"> Por favor inicie sesion para continuar </div>
				</c:if>
					<form:form class="form-signin" method="post" action="login"
						commandName="loginForm">
						<h2 class="form-signin-heading">Sign in</h2>
						<div class="control-group">
							<form:errors class="error alert alert-error" path="*" />
						</div>
						<div class="control-group">
							<label class="control-label" for="inputEmail">Username</label>
							<div class="controls">
								<form:input name="username" type="text"
									class="input-block-level" id="inputEmail"
									placeholder="Username" path="username" />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="inputPassword">Password</label>
							<div class="controls">
								<form:password name="password" type="password"
									class="input-block-level" id="inputPassword"
									placeholder="Password" path="password" />
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<form:input name="path" type="hidden"
									class="input-block-level" id="inputEmail"
									placeholder="Username" path="path" value="${pos}"/>
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<button type="submit" class="btn btn-large btn-primary">Iniciar sesion</button>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>