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
		<!-- Jumbotron -->
		<div class="jumbotron">
			<div class="container-fluid">
				<div class="row-fluid">
					<div class="span2">
						<%@ include file="sideMenu.jsp"%>
					</div>
					<div class="span6">
						<c:if test="${success!=null}">
							<div class="alert alert-success">${success}</div>
						</c:if>
						<h2>Subscribir</h2>
						<hr/>
						<form class="form-horizontal" action="subscribe" method="post">
							<input name="id" path="id" type="hidden" value="${person.id}" />
							<select name="service" path="service" class="form-control">
								<c:forEach var="service" items="${services}" varStatus="rowCounter">
									<option value="${service.id}">${service.name}</option>
								</c:forEach>
							</select>
					    	<button type="submit" class="btn btn-default">Subscribir</button>
						</form>
					</div>
					<div class="span4 pull-right">
						<%@ include file="enrollmentList.jsp"%>
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