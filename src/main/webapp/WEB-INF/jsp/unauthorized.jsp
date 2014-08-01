<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<!--  TODO: fix relative paths problem in header!!! -->
<%@ include file="template/headGeneral.jsp"%>
</head>

<body>
    <div class="container">
      <div class="masthead">
        <h3 class="muted">SiBaDaC</h3>
        <div class="navbar">
          <div class="navbar-inner">
            <div class="container">
              <ul class="nav">
                <li><a href="/sibadac/data/person/listAll">Usuarios</a></li>
                <li><a href="/sibadac/data/service/listAll">Servicios</a></li>
				<li><a href="/sibadac/data/enrollment/listAll">Subscripciones</a></li>
				<li><a href="/sibadac/data/payment/listAll">Pagos</a></li>
				<li><a href="/sibadac/data/billing/listNewEnrollments">Facturacion</a></li>
				<li><a href="/sibadac/data/user/update">Perfil</a></li>
              </ul>
            </div>
          </div>
        </div><!-- /.navbar -->
      </div>

      <!-- Jumbotron -->
      <div class="jumbotron">
        <h2>Sistema de Base de Datos de CEITBA</h2>
		<h3 class="alert alert-danger"> No tiene autorizacion para ver esta pagina! :( </h3>
      </div>

      <div class="footer">
        <p>CEITBA 2014</p>
      </div>

    </div>
</body>
</html>