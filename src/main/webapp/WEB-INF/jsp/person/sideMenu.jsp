						<ul class="nav nav-list pull-left">
							<li class="nav-header">Opciones</li>
							<c:if test="${menu=='listAll'}">
								<li class="active"><a href="listAll">Listar Todos</a></li>
							</c:if>
							<c:if test="${menu!='listAll'}">
								<li><a href="listAll">Listar Todos</a></li>
							</c:if>

							<c:if test="${menu=='register'}">
								<li class="active"><a href="register">Nuevo Usuario</a></li>
							</c:if>
							<c:if test="${menu!='register'}">
								<li><a href="register">Nuevo Usuario</a></li>
							</c:if>

							<c:if test="${menu!='register' && menu!='listAll'}">
								<li class="active"><a href="#">Detalles</a></li>
							</c:if>
						</ul>
						<c:if test="${menu!='register' && menu!='listAll'}">
							<ul class="nav nav-list pull-left">
								<li class="nav-header">&nbsp;</li>
							</ul>
							<ul class="nav nav-list pull-left">
								<li class="nav-header">Detalles</li>

								<c:if test="${menu=='state'}">
									<li class="active"><a href="state?id=${person.id}">Estado</a></li>
								</c:if>
								<c:if test="${menu!='state'}">
									<li><a href="state?id=${person.id}">Estado</a></li>
								</c:if>

								<c:if test="${menu=='update'}">
									<li class="active"><a href="update?id=${person.id}">Editar</a></li>
								</c:if>
								<c:if test="${menu!='update'}">
									<li><a href="update?id=${person.id}">Editar</a></li>
								</c:if>
								
								<c:if test="${menu=='history'}">
									<li class="active"><a href="history?id=${person.id}">Historial</a></li>
								</c:if>
								<c:if test="${menu!='history'}">
									<li><a href="history?id=${person.id}">Historial</a></li>
								</c:if>

								<c:if test="${menu=='subscribe'}">
									<li class="active"><a href="subscribe?id=${person.id}">Nueva Subscripcion</a></li>
								</c:if>
								<c:if test="${menu!='subscribe'}">
									<li><a href="subscribe?id=${person.id}">Nueva Subscripcion</a></li>
								</c:if>

								<c:if test="${menu=='purchases'}">
									<li class="active"><a href="purchases?id=${person.id}">Productos</a></li>
								</c:if>
								<c:if test="${menu!='purchases'}">
									<li><a href="purchases?id=${person.id}">Productos</a></li>
								</c:if>

								<c:if test="${menu=='purchase'}">
									<li class="active"><a href="purchase?id=${person.id}">Nuevo Producto</a></li>
								</c:if>
								<c:if test="${menu!='purchase'}">
									<li><a href="purchase?id=${person.id}">Nuevo Producto</a></li>
								</c:if>
							</ul>
						</c:if>
							