<div class="span3 pull-right">
	<c:if test="${user.username == null}">
		<a href="/sibadac/data/user/login"><span class="pull-right">Iniciar sesion</span></a>
	</c:if>
	<c:if test="${user.username != null}">
		<span class="c">Welcome: <a href="../user/update">${user.username}</a> </span>
              <a href="/sibadac/data/user/logout"><span class="pull-right">Cerrar sesion</span></a>
	</c:if>
</div>
