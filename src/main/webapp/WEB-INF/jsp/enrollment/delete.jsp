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
						<div class="alert alert-success">
							<h4>La subscripcion fue borrada con exito</h4>
						</div>
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