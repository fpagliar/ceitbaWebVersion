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
						</div>
						<div class="span5">
							<h2>Detalle</h2>
						</div>
						<div class="span5">
							<h2>Anotados</h2>
						</div>
					</div>
				<div class="row-fluid">
					<div class="span2">
						<ul class="nav nav-list pull-left">
							<li class="nav-header">Opciones</li>
							<li><a href="listAll">Listar Todos</a></li>
							<li><a href="listAll?list=active">Listar Activos</a></li>
							<li><a href="listAll?list=inactive">Listar Inactivos</a></li>
							<li><a href="register">Nuevo Servicio</a></li>
							<li class="active"><a href="#">Editar</a></li>
						</ul>
					</div>
					<div class="span5" text-align= "center">
						<c:if test="${success==true}">
							<div class="alert alert-success">${successmsg}</div>
						</c:if>
						<c:if test="${neww==true}">
							<div class="alert alert-success">${newmsg}</div>
						</c:if>
						<form:form method="post" action="update"
							commandName="updateServiceForm" class="form-horizontal">
							<div class="control-group">
								<label class="control-label" for="inputEmail">Nombre</label>
								<div class="controls">
									<form:input name="name" path="name" type="text" value="${service.name}" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="name" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Tipo</label>
								<div class="controls">
									<form:select name="type" path="type">
										<c:if
											test="${service.type == 'SUBSCRIBABLE'}">
											<form:option value="SUBSCRIBABLE" selected="selected" />
											<form:option value="CONSUMABLE"/>
										</c:if>
										<c:if
											test="${service.type != 'SUBSCRIBABLE'}">
											<form:option value="SUBSCRIBABLE" />
											<form:option value="CONSUMABLE" selected="selected" />
										</c:if>
									</form:select>
								</div>
							</div>
							<div class="control-group"></div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Precio</label>
								<div class="controls">
									<form:input name="value" path="value" type="text"
										placeholder="Precio" value="${service.value}" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="value" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Duracion</label>
								<div class="controls">
									<form:input name="monthsDuration" path="monthsDuration"
										type="text" placeholder="Duracion (meses)"
										value="${service.monthsDuration}" />
								</div>
							</div>
							<div class="control-group">
								<form:errors path="monthsDuration"
									class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Status</label>
								<div class="controls">
									<form:select name="status" path="status">
										<c:if test="${service.status == 'ACTIVE'}">
											<form:option value="ACTIVE" selected="selected" />
											<form:option value="INACTIVE" />
										</c:if>
										<c:if test="${service.status == 'INACTIVE'}">
											<form:option value="ACTIVE" />
											<form:option value="INACTIVE" selected="selected" />
										</c:if>
									</form:select>
								</div>
							</div>
							<div class="control-group"></div>
							<form:input name="id" type="hidden" value="${service.id}"
								path="id" />
							<div class="control-group">
								<div class="controls">
									<button type="submit" class="btn">Confirmar</button>
								</div>
							</div>
						</form:form>
					</div>
					<div class="span5">
						<c:if test="${fn:length(enrollments.elements) == 0}">
							<h5>El servicio no tiene subscripciones vigentes</h5>
						</c:if>
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
								<c:forEach var="enrollment" items="${enrollments.elements}"
									varStatus="rowCounter">
									<tr>
										<td>${enrollment.person.legacy}</td>
										<td>${enrollment.person.firstName}</td>
										<td>${enrollment.person.lastName}</td>
										<td><a href="../enrollment/show?id=${enrollment.id}"><i class="icon-remove"></i></a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<div class="row-fluid">
							<div class="span4">
								<c:if test="${enrollments.actualPage > 1}">
									<a href="update?id=${service.id}&page=${enrollments.actualPage-1}"> 
										<button class="btn-large">Anterior</button> 
									</a>
								</c:if>
							</div>
							<div class="span4">
								<h4> Pagina ${enrollments.actualPage} de ${enrollments.totalPage} </h4>
							</div>
							<div class="span4">
								<c:if test="${enrollments.actualPage < enrollments.totalPage}">
									<a href="update?id=${service.id}&page=${enrollments.actualPage+1}"> 
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