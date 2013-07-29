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
							<li><a href="listAll?list=active">Listar Activos</a></li>
							<li><a href="listAll?list=inactive">Listar Inactivos</a></li>
							<li><a href="register">Nuevo Servicio</a></li>
							<li class="active">Editar</li>
						</ul>
					</div>
					<div class="span10">
					<c:if test="${success==true}">
					<div class="alert alert-success">${successmsg}</div>
					</c:if>
					<c:if test="${new==true}">
					<div class="alert alert-success">${newmsg}</div>
					</c:if>
						<form:form method="post" action="update" commandName="updateServiceForm" class="form-horizontal">
							<div class="control-group">
								<label class="control-label" for="inputEmail">Nombre</label>
								<div class="controls">
										<form:input name="name" path="name" type="text" value="${service.name}"/>
								</div>
							</div>
							<div class="control-group">
 										<form:errors path="name" class="error alert alert-error"/>
 							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Tipo</label>
								<div class="controls">
									<form:select name="type" path="type">
										<c:if test="${service.type == 'SPORT'}">
											<form:option value="SPORT" selected="selected"/>
											<form:option value="COURSE"/>
											<form:option value="LOCKER"/>
											<form:option value="OTHER"/>
										</c:if>
										<c:if test="${service.type == 'COURSE'}">
											<form:option value="SPORT"/>
											<form:option value="COURSE" selected="selected"/>
											<form:option value="LOCKER"/>
											<form:option value="OTHER"/>
										</c:if>
										<c:if test="${service.type == 'LOCKER'}">
											<form:option value="SPORT"/>
											<form:option value="COURSE"/>
											<form:option value="LOCKER" selected="selected"/>
											<form:option value="OTHER"/>
										</c:if>
										<c:if test="${service.type != 'LOCKER' && service.type != 'COURSE' && service.type != 'SPORT'}">
											<form:option value="SPORT"/>
											<form:option value="COURSE"/>
											<form:option value="LOCKER"/>
											<form:option value="OTHER" selected="selected"/>
										</c:if>
									</form:select>
								</div>
							</div>
							<div class="control-group">
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Precio</label>
								<div class="controls">
									<form:input name="value" path="value" type="text" placeholder="Precio" value="${service.value}"/>
								</div>
							</div>
							<div class="control-group">
									<form:errors path="value" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Duracion</label>
								<div class="controls">
									<form:input name="monthsDuration" path="monthsDuration" type="text" placeholder="Duracion" value="${service.monthsDuration}"/>
								</div>
							</div>
							<div class="control-group">
									<form:errors path="monthsDuration" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Status</label>
								<div class="controls">
									<form:select name="status" path="status">
										<c:if test="${service.status == 'ACTIVE'}">
											<form:option value="ACTIVE" selected="selected"/>
											<form:option value="INACTIVE"/>								
										</c:if>
										<c:if test="${service.status == 'INACTIVE'}">
											<form:option value="ACTIVE"/>
											<form:option value="INACTIVE" selected="selected"/>								
										</c:if>
									</form:select>
								</div>
							</div>
							<div class="control-group">
							</div>
							<form:input name="id" type="hidden" value="${service.id}" path="id"/>
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