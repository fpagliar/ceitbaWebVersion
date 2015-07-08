						<ul class="nav nav-list pull-left">
							<li class="nav-header">Opciones</li>

							<c:if test="${menu=='listAll'}">
								<li class="active">
									<a href="listAll">Listar Todos</a>
								</li>
							</c:if>
							<c:if test="${menu!='listAll'}">
								<li>
								<a href="listAll">Listar Todos</a>
								</li>
							</c:if>

							<c:if test="${menu=='listActive'}">
								<li class="active">
									<a href="listAll?list=active">Listar Activos</a>
								</li>
							</c:if>
							<c:if test="${menu!='listActive'}">
								<li>
									<a href="listAll?list=active">Listar Activos</a>
								</li>
							</c:if>

							<c:if test="${menu=='listInactive'}">
								<li class="active">
									<a href="listAll?list=inactive">Listar Inactivos</a>
								</li>
							</c:if>
							<c:if test="${menu!='listInactive'}">
								<li>
									<a href="listAll?list=inactive">Listar Inactivos</a>
								</li>
							</c:if>

							<c:if test="${menu=='newService'}">
								<li class="active">
									<a href="register">Nuevo Servicio</a>
								</li>
							</c:if>
							<c:if test="${menu!='newService'}">
								<li>
									<a href="register">Nuevo Servicio</a>
								</li>
							</c:if>
							
							<c:if test="${menu=='update'}">
								<li class="active">
									<a href="#">Editar</a>
								</li>
							</c:if>
						</ul>
