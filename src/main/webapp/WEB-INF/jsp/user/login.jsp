<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" >
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<head>
	<%@ include file="../template/headGeneral.jsp" %>
</head>

<body>

	<div class="container">
		<form:form class="form-signin" method="post" action="login" commandName="loginForm">
			<h2 class="form-signin-heading">Sign in</h2>
			<form:errors class="error" path="*" />
			<div class="control-group">
				<label class="control-label" for="inputEmail">Username</label>
				<div class="controls">
					<form:input name="username" type="text" class="input-block-level" id="inputEmail" placeholder="Username" path="username" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="inputPassword">Password</label>
				<div class="controls">
					<form:password name="password" type="password" class="input-block-level" id="inputPassword" placeholder="Password" path="password" />
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn btn-large btn-primary">Log In</button>
				</div>
			</div>
		</form:form>
	</div>
	<!-- /container -->

	<!-- Le javascript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<!-- 
	<script src="../../assets/js/jquery.js"></script>
	<script src="../../assets/js/bootstrap-transition.js"></script>
	<script src="../../assets/js/bootstrap-alert.js"></script>
	<script src="../../assets/js/bootstrap-modal.js"></script>
	<script src="../../assets/js/bootstrap-dropdown.js"></script>
	<script src="../../assets/js/bootstrap-scrollspy.js"></script>
	<script src="../../assets/js/bootstrap-tab.js"></script>
	<script src="../../assets/js/bootstrap-tooltip.js"></script>
	<script src="../../assets/js/bootstrap-popover.js"></script>
	<script src="../../assets/js/bootstrap-button.js"></script>
	<script src="../../assets/js/bootstrap-collapse.js"></script>
	<script src="../../assets/js/bootstrap-carousel.js"></script>
	<script src="../../assets/js/bootstrap-typeahead.js"></script>
	-->
</body>
</html>