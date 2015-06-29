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
							<li><a href="listAll">Listar Activas</a></li>
							<li><a href="listAll?list=history">Historial</a></li>
							<li><a href="register">Nueva Subscripcion</a></li>
							<li class="active">Ver Subscripcion</li>
						</ul>
					</div>
					<div class="span10">
						<c:if test="${neww==true}">
							<div class="alert alert-success">${newmsg}</div>
						</c:if>
						<form class="form-horizontal" action="listAll" method="get"
							name="search">
							<div class="control-group">
								<label class="control-label" for="inputEmail">Legajo</label>
								<div class="controls">
									<input name="legacy" path="legacy" type="text"
										value="${enrollment.person.legacy}" disabled />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label" for="inputEmail">Servicio</label>
								<div class="controls">
									<input name="legacy" path="legacy" type="text"
										value="${enrollment.service.name}" disabled />
								</div>
							</div>
						</form>
						<form class="form" action="delete" method="post" name="search">
						<input name="person" type="hidden" value="${enrollment.person.id}" path="person"/>
						<input name="service" type="hidden" value="${enrollment.service.id}" path="service"/>
							<c:if test="${isActive}">
								<div class="control-group">
									<div class="controls">
										<button type="submit" class="btn">Cancelar</button>
									</div>
								</div>
							</c:if>
						</form>
						<c:if test="${!isActive}">
							<div class="alert alert-warning">Subscripcion cancelada!</div>
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