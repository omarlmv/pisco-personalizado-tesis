<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script type="text/javascript">
	var URL_RESOURCES = '${prop['config.url.recursos.base.web']}';
</script>
<style>
	.blockUI{background-color:none !important;}
</style>
		
		


				<div class="elegir">
						<div class="titulo-general">
							<p>Tienes ${cantidad} opciones para elegir:</p>
							<span></span>
						</div>
						<select class="filtro-ordenar">
							<option value="">Ordenar por</option>
							<option value="1">Precio Descendente</option>	
							<option value="2">Precio Ascendente</option>																				
						</select>
					</div>
				<!-- inicio for -->
		<c:forEach var="vuelosDisponible" items="${lista}" varStatus="status">
				<!-- Inicio section aereolinea -->
					<section class="aerolinea">
						<div class="dinero-puntos">
							<div>
								<h3>${vuelosDisponible.aereoLineaPrincipal}</h3>
								<ul class="dinero">
									<li>
										<span class="alternativa">Desde</span>
										<span class="dinero-numero">${simboloMonedaDolar} ${vuelosDisponible.dolares.formatTotal}</span>
										<span class="alternativa">ó</span>
									</li>
								</ul>
								<ul class="puntos">
									<li>
										<span class="puntos-numero">${vuelosDisponible.dolares.formatEquivalentePuntos}</span>
										<span class="puntos-texto">Millas Benefit</span>
									</li>
									<li class="puntos-detalle">Total incluidos<br />impuestos y cargos</li>
								</ul>
							</div>
						</div>
						<div class="salida-regreso" id="formulario${status.count}">
							<div>
							 <form:form id="frmContinuar" action="${pageContext.request.contextPath}/viajes/compra/vuelos/pasajeros" method="GET" autocomplete="off" commandName="vuelosSeleccionadosForm">
									<article>
										<div class="general">
											<p class="titulo ico-salida">Salida:</p>
											<ul>
												<li><span class="iniciales">${origen}</span></li>
												<li class="ico-arrow"></li>
												<li><span class="iniciales">${destino}</span></li>
											</ul>
											<p class="fecha-opciones">${vuelosDisponible.fechaSalidaGeneral}</p>
										</div>
									<c:forEach var="vuelo" items="${vuelosDisponible.vuelos}">
										<c:if test="${vuelo.indicador == '0'}">		
										<label class="seleccion">
											<input type="radio" name="vueloSalidaParam" id="vueloSalidaParam" checked="checked" value="${vuelo.idVuelo}"></input>
											<span class="sale">Sale: ${vuelo.horaSalida}</span>
											<span class="llega">Llega: ${vuelo.horaLlegada}</span>
											<span class="duracion">${vuelo.duracion}</span>
											<span class="escala">
												<a href="javascript:void(0);" class="abrir-escala">${vuelo.numeroEscalas} Escala</a>
												<div class="resultado">
<!-- 													<span class="ico-escala"></span> -->
													<div>
													  <c:forEach var="segmento" items="${vuelo.segmentos}">
														<div class="tramo">
															<!-- <p class="titulo">Tramo 1</p> -->
															<ul>
																<li>
<!-- 																	<p class="ico-copa"></p> -->
																	<span style="width: 80px;">
																		<img style="height: 24px; width: 82px;" alt="${vuelo.aereolinea.nombre}" src="${prop['dominio.resources.costamar']}${vuelo.aereolinea.codigoAereoLinea}.png">
																	</span>
																	<p>${segmento.aereolinea.nombre}<br />Vuelo ${segmento.numeroVuelo}<br /></p>
																</li>
																<li>Partida: ${segmento.formatoCortoFechaSalida}<br />${segmento.horaSalida}<br />${segmento.aereopuertoOrigen.codigoIata}</li>
																<li>Llegada: ${segmento.formatoCortoFechaLlegada}<br />${segmento.horaLlegada}<br />${segmento.aereopuertoDestino.codigoIata}</li>
															</ul>
														</div>
														<!-- Fin tramo -->
													</c:forEach>
													</div>
												</div>
											</span>
											<!-- <span class="logo-copa"></span>-->
<!-- 											<span style="width: 80px;"> -->
<%-- 												<img style="height: 20px; width: 100%;" alt="${vuelo.aereolinea.nombre}" src=" http://media.costamar.com/Costamar/airlines/${vuelo.aereolinea.codigoAereoLinea}.png"> --%>
<!-- 											</span> -->
												<span style="width: 80px;">
													<img style="height: 24px; width: 82px;" alt="${vuelo.aereolinea.nombre}" src="${prop['dominio.resources.costamar']}${vuelo.aereolinea.codigoAereoLinea}.png">
												</span>
												
										</label>
										<!-- fin seleccion -->
									 		</c:if>
										</c:forEach>
							  		</article>
									
									<article>
										<div class="general">
										  <c:if test="${not empty fechaRegreso}">
											<p class="titulo ico-regreso">Regreso:</p>
											<ul>
												<li><span class="iniciales">${destino}</span></li>
												<li class="ico-arrow"></li>
												<li><span class="iniciales">${origen}</span></li>
											</ul>
											<p class="fecha-opciones">${vuelosDisponible.fechaRegresoGeneral}</p>
										  </c:if>
										</div>
									 <c:forEach var="vuelo" items="${vuelosDisponible.vuelos}">
								 		<c:if test="${vuelo.indicador == '1'}">	
										<label class="seleccion">
											<input type="radio" name="vueloRegresoParam" id="vueloRegresoParam" checked="checked" value="${vuelo.idVuelo}"></input>
											<span class="sale">Sale: ${vuelo.horaSalida}</span>
											<span class="llega">Llega: ${vuelo.horaLlegada}</span>
											<span class="duracion">${vuelo.duracion}</span>
											<span class="escala">
												<a href="javascript:void(0);" class="abrir-escala">${vuelo.numeroEscalas} Escala</a>
												<div class="resultado">
<!-- 													<span class="ico-escala"></span> -->
													<div>
													  <c:forEach var="segmento" items="${vuelo.segmentos}">
														<div class="tramo">
															<ul>
																<li>
<!-- 																	<p class="ico-copa"></p> -->
																	<span style="width: 80px;">
																		<img style="height: 24px; width: 82px;" alt="${vuelo.aereolinea.nombre}" src="${prop['dominio.resources.costamar']}${vuelo.aereolinea.codigoAereoLinea}.png">
																	</span>
																	<p>${segmento.aereolinea.nombre}<br />Vuelo ${segmento.numeroVuelo}<br /></p>
																</li>
																<li>Partida: ${segmento.formatoCortoFechaSalida}<br />${segmento.horaSalida}<br />${segmento.aereopuertoOrigen.codigoIata}</li>
																<li>Llegada: ${segmento.formatoCortoFechaLlegada}<br />${segmento.horaLlegada}<br />${segmento.aereopuertoDestino.codigoIata}</li>
															</ul>
														</div>
														<!-- Fin tramo -->
													</c:forEach>
													</div>
												</div>
											</span>
											<!-- <span class="logo-copa"></span> -->
<!-- 											<span style="width: 80px;"> -->
<%-- 												<img style="height: 20px; width: 100%;" alt="${vuelo.aereolinea.nombre}" src=" http://media.costamar.com/Costamar/airlines/${vuelo.aereolinea.codigoAereoLinea}.png"> --%>
<!-- 											</span> -->
												<span style="width: 80px;">
													<img style="height: 24px; width: 82px;" alt="${vuelo.aereolinea.nombre}" src="${prop['dominio.resources.costamar']}${vuelo.aereolinea.codigoAereoLinea}.png">
												</span>

										</label>
										<!-- Fin selection -->
									  </c:if>
									</c:forEach>
									</article>
									<p class="botonera">
<!-- 										<input type="submit" class="continuar" value="CONTINUAR"> -->
											<input type="submit" class="continuar" value="CONTINUAR" id="${status.count}" >	
									</p>
								</form:form>
							</div>
						</div>
					</section>
					<!-- Fin section aereolinea -->
				  </c:forEach>