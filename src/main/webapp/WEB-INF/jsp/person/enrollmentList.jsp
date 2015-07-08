						<h2>Subscripciones</h2>
						<hr/>
						<c:if test="${fn:length(enrollments) == 0}">
							<h5>El usuario no tiene subscripciones vigentes</h5>
						</c:if>
						<c:if test="${fn:length(enrollments) != 0}">
							<table class="table table-striped pull-right">
								<thead>
									<tr>
										<th>Servicio</th>
										<th>Inicio</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="enrollment" items="${enrollments}" varStatus="rowCounter">
										<tr>
											<td>${enrollment.service.name}</td>
											<td>${enrollment.formatedStartDate}</td>
											<td>
												<form action="unsubscribe" method="post">
													<input name="id" path="id" type="hidden" value="${person.id}" />
													<input name="service" path="service" type="hidden" value="${enrollment.service.id}" />
													<button type="submit" class="btn btn-default" 
													    onClick='return confirm("Esta seguro que desea eliminar esta subscripcion?")'
													    style="border:none; background:none; padding:0;">
														<i class="icon-remove"></i>
													</button>
												<form>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>
