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
						<%@ include file="sideMenu.jsp"%>
					</div>
					<div class="span5">
						<h2> Comprar </h2>
						<form class="form-horizontal" action="purchase" method="post">
							<input name="id" path="id" type="hidden" value="${person.id}" />
							<select name="product" path="product" class="form-control">
								<c:forEach var="product" items="${products}" varStatus="rowCounter">
									<option value="${product.id}">${product.name}</option>
								</c:forEach>
							</select>
					    	<button type="submit" class="btn btn-default">Comprar</button>
						</form>
					</div>
					<div class="span5">
						<h2> Nuevo Producto </h2>
						<form:form method="post" action="newProduct" commandName="productForm" class="form-horizontal">
							<form:input name="personId" path="personId" type="hidden" value="${person.id}"/>
							<div class="control-group">
								<label class="control-label" for="inputName">Nombre</label>
								<div class="controls">
										<form:input name="name" path="name" type="text"/>
								</div>
							</div>
							<div class="control-group">
 								<form:errors path="name" class="error alert alert-error"/>
 							</div>
							<div class="control-group">
								<label class="control-label" for="inputValue">Precio</label>
								<div class="controls">
										<form:input name="value" path="value" type="text"/>
								</div>
							</div>
							<div class="control-group">
								<form:errors path="value" class="error alert alert-error" />
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