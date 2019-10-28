<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<style>
body {
	background-color: #f2f1f1;
}

.blockUI {
	background-color: none !important;
}
</style>
<!-- estado -->
<p class="separar"></p>
<section class="estado">
	<p class="barra"></p>
	<div class="limite">
		<!-- flotante -->
		<section class="flotante-secundario">
			<div class="mis-canjes">
				<div class="cabec-canjespro"></div>
				<div class="mis-canjes-cnt">
					<p>Mis canjes</p>
					<div class="paquete">
						<div class="paquete-destino">
							${salida.aereopuertoOrigen.codigoIata} - ${salida.aereopuertoDestino.codigoIata}
							<c:if test="${not empty vueloRegreso}">- ${regreso.aereopuertoDestino.codigoIata}</c:if>
						</div>
						<div class="paquete-detalle">
							<ul>
								<li>N&uacute;mero de pasajeros</li>
								<li>${adulto + ninio + infante}</li>
							</ul>
							<ul>
								<li>Ida</li>
								<li>${fechaSalida}</li>
							</ul>
							<c:if test="${not empty fechaRegreso}">
								<ul>
									<li>Vuelta</li>
									<li>${fechaRegreso}</li>
								</ul>
							</c:if>
							<hr>

							<div class="paquete-puntos">
								<ul>
									<li>Total en Millas Benefit</li>
									<li class="formatValorPuntos">${formatImportePuntos}</li>
								</ul>
							</div>

						</div>

					</div>
				</div>
				<div class="blockdown-zonaracional">
					<c:if test="${importePuntos <= totalPuntosCliente}">
					</c:if>
				</div>
				<div class="miscanjes-mixto-paso2">
					<ul class="puntos-efectivo">
						<li class="pe-left green">Millas Benefit por canjear:</li>
						<li class="pe-right green canje-puntos-cubrir">0</li>
						<li class="pe-left steel">Total a pagar:</li>
						<li class="pe-right steel"><span class="canje-precio-cubrir-efectivo">${simboloMonedaDolar} ${formatPrecioDolares}</span></li>
					</ul>
				</div>

			</div>
			<!--mis canjes-->
		</section>
		<!-- /flotante -->
		<p class="titulo-canje">Reserva de vuelo</p>
		<ul class="estado-4pasos estado-operacion">
			<li class="paso-1 pasado"><span>1</span>
			<p></p></li>
			<li class="paso-2 pasado"><span>2</span>
			<p></p></li>
			<li class="paso-3 presente"><span>3</span>
			<p></p></li>
			<li class="paso-4 completo-off"><span></span>
			<p></p></li>
		</ul>
	</div>
</section>
<!-- /estado -->

<!-- informacion -->
<div class="limite acomodar-racional" id="viajes-paso-3">
	<div class="medio-canje informacion" id="medio-canje">
		<div class="tit-mediocanje-pas3">
			<p>Medio de canje</p>
		</div>
		<div class="canjeP3-form">
			<form:form id="frmPasarela" action="${pageContext.request.contextPath}/viajes/compra/vuelos/procesar" method="POST" autocomplete="off" commandName="pasarelaPagoForm">
				<input type="hidden" class="urlImagenLoad" value="${prop['config.url.recursos.base.web']}static/images/ajax/cargando.gif" />
<%-- 				<form:input type="hidden" value="${salida.hashGenerado}" path="vueloSalida" /> --%>
<%-- 				<form:input type="hidden" value="${regreso.hashGenerado}" path="vueloRegreso" /> --%>
				<input type="hidden" value="D" class="indicador-moneda"></input>
				<div class="canjeP3-box">
					<div class="canjeP3-wancho">
						<div class="canjeP3-left">
							<p>
								Vuelo ida: <span class="z-salto-linea"> ${salida.aereopuertoOrigen.codigoIata} - ${salida.aereopuertoDestino.codigoIata} </span>
							</p>
						</div>
						<div class="canjeP3-right">
							<c:forEach var="segmento" items="${salida.segmentos}" varStatus="stat">
								<c:if test="${stat.first}">
									<p>${segmento.aereolinea.nombre} N° ${segmento.numeroVuelo}</p>
									<p>Partida: ${segmento.aereopuertoOrigen.nombre} ${segmento.formatoCortoFechaSalida} ${segmento.horaSalida}</p>
								</c:if>
								<c:if test="${stat.last}">
									<p>Llegada a: ${segmento.aereopuertoDestino.nombre} ${segmento.formatoCortoFechaLlegada} ${segmento.horaLlegada}</p>
									<p>
										Pasajero${adulto+ninio+infante>1?'s':''}:
										<c:if test="${adulto>0}">${adulto}&nbsp;Adulto${adulto<2?'':'s'}</c:if>
										<c:if test="${ninio>0}">, ${ninio}&nbsp;Niño${ninio<2?'':'s'}</c:if>
										<c:if test="${infante>0}">, ${infante}&nbsp;Bebé${infante<2?'':'s'}</c:if>
									</p>
								</c:if>
							</c:forEach>
						</div>
						<c:if test="${not empty fechaRegreso}">
							<div class="canjeP3-left">
								<p>
									Vuelo regreso: <span class="z-salto-linea"> ${salida.aereopuertoDestino.codigoIata} - ${regreso.aereopuertoDestino.codigoIata}</span>
								</p>
							</div>
							<div class="canjeP3-right">
								<c:forEach var="segmento" items="${regreso.segmentos}" varStatus="stat">
									<c:if test="${stat.first}">
										<p>${segmento.aereolinea.nombre} N° ${segmento.numeroVuelo}</p>
										<p>Partida: ${segmento.aereopuertoOrigen.nombre} ${segmento.formatoCortoFechaSalida} ${segmento.horaSalida}</p>
									</c:if>
									<c:if test="${stat.last}">
										<p>Llegada a: ${segmento.aereopuertoDestino.nombre} ${segmento.formatoCortoFechaLlegada} ${segmento.horaLlegada}</p>
										<p>
											Pasajero${adulto+ninio+infante>1?'s':''}:
											<c:if test="${adulto>0}">${adulto}&nbsp;Adulto${adulto<2?'':'s'}</c:if>
											<c:if test="${ninio>0}">, ${ninio}&nbsp;Niño${ninio<2?'':'s'}</c:if>
											<c:if test="${infante>0}">, ${infante}&nbsp;Bebé${infante<2?'':'s'}</c:if>
										</p>
									</c:if>
								</c:forEach>
							</div>
						</c:if>
						<div>
							<c:set var="contar" value="1" />
							<table class="elemento-horizontal">
								<c:forEach var="pasajero" items="${adultos}">
									<tr class="lPasajeros">
										<td>Pasajero ${contar}:</td>
										<td>${pasajero.nombre} ${pasajero.apellido}</td>
									</tr>
									<c:set var="contar" value="${contar + 1}" />
								</c:forEach>
								<c:forEach var="pasajero" items="${ninios}">
									<tr class="lPasajeros">
										<td>Pasajero ${contar}:</td>
										<td>${pasajero.nombre} ${pasajero.apellido}</td>
									</tr>
									<c:set var="contar" value="${contar + 1}" />
								</c:forEach>
								<c:forEach var="pasajero" items="${infantes}">
									<tr class="lPasajeros">
										<td>Pasajero ${contar}:</td>
										<td>${pasajero.nombre} ${pasajero.apellido}</td>
									</tr>
									<c:set var="contar" value="${contar + 1}" />
								</c:forEach>

							</table>
						</div>
					</div>
				</div>
				<div class="canjeP3-box color-celeste">
					<div class="canjeP3-wancho sin-wancho">
						<div class="canjeP3-left">
							<p>Mi total de Millas Benefit:</p>
						</div>
						<div class="canjeP3-right">
							<p>${sessionScope.sessionCliente.formatTotalPuntos}</p>
						</div>

						<div class="canjeP3-left">
							<p>Valor del producto en Millas Benefit</p>
						</div>
						<div class="canjeP3-right">
							<p>${formatImportePuntos}</p>
						</div>

						<div class="canjeP3-left">
							<p>Monto total:</p>
						</div>
						<div class="canjeP3-right cnt-cupon">
							<p>${simboloMonedaDolar}${formatPrecioDolares}</p>
							<c:if test="${listaCupones ne null}">
							<span id="show-cupon" class="btn-cupon">&iexcl;Tienes cupones disponibles!</span>
							</c:if>
						</div>
						<div class="canjeP3-left dscto-cupon rojo" style="vertical-align: top !important;">
							<p>Descuento:</p>
						</div>
						<div id="dscto-aplicado"  class="canjeP3-right dscto-cupon rojo">
							
						</div>
						<div class="canjeP3-left dscto-cupon">
							<p>Nuevo monto total:</p>
						</div>
						<div id="nuevo-monto-total" class="canjeP3-right cnt-cupon dscto-cupon">
							
						</div>
						<div class="cupon-detail">
							<div class="separador"></div>
							<div class="canjeP3-left line-cupon">
								<p>Agregar cup&oacute;n:</p>
							</div>
							<div class="canjeP3-right cnt-cupon contenedor-cupon">
								<p>
								
								<form:select class="list-cupones" path="cboCupones" id="cboCupones">
								 <form:option  value="0">Seleccionar cup&oacute;n</form:option>
								  <c:forEach items="${listaCupones}" var="cupon">
								  <c:if test="${cupon.tipoCupon==1}">
								  <form:option value="${cupon.codigo}">${cupon.label} (${cupon.simboloTipo}${cupon.monto}) </form:option>
								  </c:if>
								   <c:if test="${cupon.tipoCupon==2}">
								     <form:option value="${cupon.codigo}">${cupon.label} (${cupon.monto}${cupon.simboloTipo}) </form:option>
								   </c:if>
								  </c:forEach>
									
								</form:select>
								
									<div class="cupon-selected"></div>
									<span id="apply-cupon" class="btn-cupon">Aplicar</span>
									<span id="cancel-cupon">Cancelar</span>
									<span class="icon-check" id="check-cupon"><a id="cambiar-cupon" href="#">Cambiar</a>&nbsp;<a id="quitar-cupon" href="#">Quitar</a></span>
									<span class="error-cupon"></span>
									<span class="load-cupon"><img src="${prop['config.url.recursos.base.web']}static/images/pre-carga.gif"/>Validando tu c&oacute;digo...</span>
								</p>
							</div>
						</div>
					</div>
				</div>
				<div class="valor canjeP3-box p3Bold">
					<c:if test="${totalPuntosCliente > 0}">
						<div class="puntos-usar canjeP3-wancho sin-wancho">
							<div class="canjeP3-left">
								<p class="millas-benefit-usar">¿Cuántas de tus Millas Benefit quieres usar?</p>
							</div>
							<div class="canjeP3-right">
								<div class="actualizar">
									<form:input type="text" required="required" value="0" path="puntosUsados" class="puntos" />
									<a href="javascript:void(0)" id="btnActualizar">Actualizar</a>
								</div>
							</div>
							<div class="canjeP3-left"></div>
							<div class="canjeP3-right p3barra">
								<input id="num-range" type="text" name="num-range" style="display: block !important; visibility: hidden;" />
							</div>
						</div>
					</c:if>
				</div>
				<div class="canjeP3-box p3Bold">
					<div class="canjeP3-wancho sin-wancho">
						<div class="canjeP3-left" style="vertical-align: middle;">
							<p>Diferencia por cubrir:</p>
						</div>
						<div class="canjeP3-right" style="vertical-align: middle;">
							<p>
								<span class="simbolo-divisa"></span><span class="precio-por-cubrir">${simboloMonedaDolar} ${formatPrecioDolares}</span>
							</p>
						</div>
					</div>
				</div>
				<div class="boton-medio-canje">
					<div class="botonone-medio-canje">
						<a href="<c:url value="/viajes/compra/vuelos/pasajeros/${vueloSalidaParam}/${vueloRegresoParam}"/>" class="volver">Volver</a>
					</div>
					<div class="botonone-medio-canje">
						<input type="submit" value="Siguiente" class="continuar" />
					</div>
				</div>
			</form:form>
		</div>
		<div class="capa-viajes"></div>
	</div>
	<!-- div informacion -->
	<div class="informacion iframe-main-content-medio-canje">
		<div class="medio-canje off" id="content-iframe-medio-canje" style="display: none">
			<iframe id="iframe-medio-canje" width="720" height="400" frameborder="0" onload="ocultarLoad()"> </iframe>
		</div>
	</div>
</div>
<!-- /informacion -->



<jsp:include page="../../modalMensajes.jsp" />

<script type="text/javascript">
$(document).on('ready',function() {
	var medioCanje = new MedioCanje();
	medioCanje.cantidadPuntosMaximo = parseInt('${totalMaximoPuntosUsar}', 10);
	
	medioCanje.precioDolares = parseFloat('${precioDolares}');
	medioCanje.simboloMonedaDolar = '${simboloMonedaDolar}';
	medioCanje.htmlProcess = $("#lightbox-procesando-reserva");
	medioCanje.htmlFinalizaProcess = $("#lightbox-send-form");
	medioCanje.urlPasarela = '${urlPasarelaPago}';
	
	medioCanje.totalImportePuntos = parseInt('${importePuntos}');
	medioCanje.totalPuntosCliente = parseInt('${totalPuntosCliente}');
	
	medioCanje.timerReservaRespuestaAsync= parseInt('${timerVuelosReservaRespuesta}',10);
	medioCanje.timerReservaMaxEsperaAsync= parseInt('${timerVuelosReservaMaxEspera}',10);
	
	medioCanje.timerBusquedaRespuesta = parseInt('${timerVuelosBusquedaRespuesta}',10);
	medioCanje.timerBusquedaMaxEspera =  parseInt('${timerVuelosBusquedaMaxEspera}',10);
	
	medioCanje.jsonFormBusqueda = '${jsonFormBusqueda}';
	medioCanje.init();
	
});
</script>