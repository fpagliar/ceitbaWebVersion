						<h2>Subscripciones</h2>
						<hr/>
						<c:if test="${fn:length(enrollments) == 0}">
							<h5>El usuario no tiene subscripciones vigentes</h5>
						</c:if>
						<c:if test="${fn:length(enrollments) != 0}">
							<form id="unsubscribe-table-form" action="unsubscribe" method="post">
								<input name="id" path="id" type="hidden" value="${person.id}" />
								<input id="unsubscribe-table-service" name="service" path="service" type="hidden"/>
							</form>
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
												<button class="btn btn-default unsubscribe-table-button" 
													data-value="${enrollment.service.id}"
												    style="border:none; background:none; padding:0;">
													<i class="icon-remove"></i>
												</button>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>
