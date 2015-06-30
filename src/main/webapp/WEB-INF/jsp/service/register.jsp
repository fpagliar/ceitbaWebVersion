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
							<li class="active"><a href="register">Nuevo Servicio</a></li>
						</ul>
					</div>
					<div class="span10">
						<form:form method="post" action="register" commandName="registerServiceForm" class="form-horizontal">
							<div class="control-group">
								<label class="control-label" for="inputEmail">Nombre</label>
								<div class="controls">
										<form:input name="name" path="name" type="text" placeHolder="Nombre"/>
								</div>
							</div>
							<div class="control-group">
 										<form:errors path="name" class="error alert alert-error"/>
 							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Tipo</label>
								<div class="controls">
									<form:select name="type" path="type">
										<form:option value="SUBSCRIBABLE"> Estandar </form:option>
										<form:option value="CONSUMABLE"> Consumible </form:option>
									</form:select>
								</div>
							</div>
							<div class="control-group">
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Precio</label>
								<div class="controls">
									<form:input name="value" path="value" type="text" placeholder="Precio"/>
								</div>
							</div>
							<div class="control-group">
									<form:errors path="value" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Duracion(meses)</label>
								<div class="controls">
									<form:input name="monthsDuration" path="monthsDuration" type="text" placeholder="Duracion" value="0"/>
								</div>
							</div>
							<div class="control-group">
									<form:errors path="monthsDuration" class="error alert alert-error" />
							</div>
							<div class="control-group">
								<label class="control-label" for="inputEmail">Status</label>
								<div class="controls">
									<form:select name="status" path="status">
											<form:option value="ACTIVE" selected="selected"/>
											<form:option value="INACTIVE"/>								
									</form:select>
								</div>
							</div>
							<div class="control-group">
							</div>
							<div class="control-group">
								<div class="controls">
									<button type="submit" class="btn">Crear</button>
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