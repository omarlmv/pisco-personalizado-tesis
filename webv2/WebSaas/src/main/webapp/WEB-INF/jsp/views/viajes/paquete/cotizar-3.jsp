<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<style>
body {
	background-color: #f2f1f1;
}

.botonera input[type="button"], .botonera a {
	behavior: url("../static/css/PIE.htc");
	border-radius: 3px;
	-moz-border-radius: 3px;
	-webkit-border-radius: 3px;
	color: #fff;
	display: inline-block;
	font-family: 'omnesmedium', Arial;
	font-size: 0.938em;
	height: 50px;
	line-height: 50px;
	margin: 0 4px;
	padding: 0 40px;
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
						<div class="paquete-destino">${reservaPaquete.paquete.titulo}</div>
						<div class="paquete-detalle">
							<ul>
								<li>Número de pasajeros</li>
								<li class="cantidadAdultos">${reservaPaqueteForm.cantAdulto}</li>
							</ul>
							<ul>
								<li>Ida</li>
								<li>${reservaPaqueteForm.fechaVueloSalida}</li>
							</ul>
							<ul>
								<li>Vuelta</li>
								<li>${reservaPaqueteForm.fechaVueloRegreso}</li>
							</ul>
							<hr>
							<div class="paquete-puntos">
								<ul>
									<li>Total en Millas Benefit</li>
									<li class="costoEnPuntos">${formatCostoEnPuntos}</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<div class="miscanjes-mixto-paso2">
					<ul class="puntos-efectivo">
						<li class="pe-left green">Millas Benefit por canjear:</li>
						<li class="pe-right green canje-puntos-cubrir">0</li>
						<li class="pe-left steel">Total a pagar:</li>
						<li class="pe-right steel"><span class="canje-precio-cubrir-efectivo" id="canje-precio-cubrir-efectivo">${simboloMonedaDolar} ${formatPrecioDolares}</span></li>
					</ul>
				</div>
			</div>
			<!--mis canjes-->
		</section>

		<p class="titulo-canje">Canje de paquete</p>
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
<div class="limite" id="paquete-paso-3">
	<div id="medio-canje" class="medio-canje informacion viajes-paso3-limit">
		<div class="tit-mediocanje-pas3">
			<p>Medio de canje</p>
		</div>
		<div class="canjeP3-form">
			<div class="canjeP3-box">
				<div class="canjeP3-wancho">
					<div class="canjeP3-left">
						<p>
							Vuelo ida: <span>${vueloOrigen[0].aereopuertoOrigen.codigoIata} - ${vueloDestino[0].aereopuertoOrigen.codigoIata}</span>
						</p>
					</div>
					<div class="canjeP3-right">
						<c:forEach var="segmento" items="${vueloOrigen}" varStatus="stat">
							<c:if test="${stat.first}">
								<p>${segmento.aereolinea.nombre} N° ${segmento.numeroVuelo}</p>
								<p>Partida: ${segmento.aereopuertoOrigen.nombre} ${segmento.formatoCortoFechaSalida} ${segmento.horaSalida}</p>
							</c:if>
							<c:if test="${stat.last}">
								<p>Llegada a: ${segmento.aereopuertoDestino.nombre} ${segmento.formatoCortoFechaLlegada} ${segmento.horaLlegada}</p>
								<p>Pasajeros : ${reservaPaqueteForm.cantAdulto} adultos</p>
							</c:if>
						</c:forEach>
					</div>
					<div class="canjeP3-left">
						<p>
							Vuelo regreso: <span>${vueloDestino[0].aereopuertoOrigen.codigoIata} - ${vueloOrigen[0].aereopuertoOrigen.codigoIata}</span>
						</p>
					</div>
					<div class="canjeP3-right">
						<c:forEach var="segmento" items="${vueloDestino}" varStatus="stat">
							<c:if test="${stat.first}">
								<p>${segmento.aereolinea.nombre} N° ${segmento.numeroVuelo}</p>
								<p>Partida: ${segmento.aereopuertoOrigen.nombre} ${segmento.formatoCortoFechaSalida} ${segmento.horaSalida}</p>
							</c:if>
							<c:if test="${stat.last}">
								<p>Llegada a: ${segmento.aereopuertoDestino.nombre} ${segmento.formatoCortoFechaLlegada} ${segmento.horaLlegada}</p>
								<p>Pasajeros : ${reservaPaqueteForm.cantAdulto} adultos</p>
							</c:if>
						</c:forEach>
					</div>
					<div class="canjeP3-left">
						<p>Hotel:</p>
					</div>
					<div class="canjeP3-right">
						<p>${reservaPaquete.paquete.detallePaquete.hotel.nombre} ${reservaPaquete.paquete.detallePaquete.hotel.ratingAward}</p>
					</div>
					<c:forEach items="${pasajeros}" var="pasajero" varStatus="count">
						<div class="canjeP3-left">
							<p>Pasajero ${count.index+1}:</p>
						</div>
						<div class="canjeP3-right">
							<p>${pasajero.nombre} ${pasajero.apellido}</p>
						</div>
					</c:forEach>
				</div>
			</div>
			<form:form id="frmPasarela" action="${pageContext.request.contextPath}/viajes/compra/paquetes/enviarPasarelaPago/${hashPaquete}" method="POST" autocomplete="off" commandName="reservaPaqueteForm">
				<div class="canjeP3-box color-celeste">
					<div class="canjeP3-wancho sin-wancho">
						<div class="canjeP3-left">
							<p>Mi total de Millas Benefit:</p>
						</div>
						<div class="canjeP3-right">
							<p>${sessionScope.sessionCliente.formatTotalPuntos}</p>
						</div>
						<div class="canjeP3-left">
							<p>Valor del producto en Millas Benefit:</p>
						</div>
						<div class="canjeP3-right">
							<p class="costoEnPuntos">${formatCostoEnPuntos}</p>
						</div>
						<div class="canjeP3-left">
							<p>Monto total:</p>
						</div>
						<div class="canjeP3-right cnt-cupon">
							<p>${simboloMonedaDolar} ${montoFinalDolares}</p>
							<c:if test="${listaCupones ne null}">
								<span id="show-cupon" class="btn-cupon">&iexcl;Tienes cupones disponibles!</span>
							</c:if>
						</div>
						
						<div class="canjeP3-left dscto-cupon rojo" style="vertical-align: top !important;">
						<p>Descuento:</p>
					</div>
					<div id="dscto-aplicado" class="canjeP3-right dscto-cupon rojo">
						
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
							<div>
								<form:select class="list-cupones" path="cboCupones" id="cboCupones">
								 <form:option  value="0">Seleccionar cup&oacute;n</form:option>
								  <c:forEach items="${listaCupones}" var="cupon">
								  <form:option value="${cupon.codigo}">${cupon.label} (${cupon.monto}${cupon.simboloTipo}) </form:option>
								  </c:forEach>
									
								</form:select>
								
								<div class="cupon-selected"></div>
								<span id="apply-cupon" class="btn-cupon">Aplicar</span>
								<span id="cancel-cupon">Cancelar</span>
								<span class="icon-check" id="check-cupon"><a id="cambiar-cupon" href="#">Cambiar</a>&nbsp;<a id="quitar-cupon" href="#">Quitar</a></span>
								<span class="error-cupon"></span>
								<span class="load-cupon"><img src="${prop['config.url.recursos.base.web']}static/images/pre-carga.gif"/>Validando tu c&oacute;digo...</span>
							</div>
						</div>
						
					</div>
					
					</div>
				</div>
				<div class="canjeP3-box p3Bold">
					<div class="puntos-usar canjeP3-wancho sin-wancho">
						<c:if test="${puntos > 0}">
							<div class="canjeP3-left">
								<p>¿Cuántas de tus Millas Benefit quieres usar?</p>
							</div>
							<div class="canjeP3-right">
								<div class="actualizar">
									<form:input type="text" required="required" value="${totalMaximoPuntosUsar}" path="puntosUsados" class="puntos" />
									<a href="javascript:void(0)" id="btn-actualizar-puntos">Actualizar</a>
								</div>
							</div>
							<div class="canjeP3-left"></div>
							<div class="canjeP3-right p3barra">
								<div>
									<input id="num-range" type="text" name="num-range" style="display: block !important; visibility: hidden;" />
								</div>
							</div>
						</c:if>
						<form:hidden value="" path="costoDolarPorCubrir" />
					</div>
				</div>
				<div class="canjeP3-box p3Bold">
					<div class="canjeP3-wancho sin-wancho">
						<div class="canjeP3-left" style="vertical-align: middle;">
							<p>Diferencia por cubrir:</p>
						</div>
						<div class="canjeP3-right" style="vertical-align: middle;">
							<p>
								<span class="simbolo-divisa"></span><span class="precio-por-cubrir" id="precio-por-cubrir">${formatPrecioDolares}</span>
							</p>
						</div>
					</div>
				</div>
				
				<div class="boton-medio-canje">
					<div class="botonone-medio-canje">
						<a href="<c:url value="/viajes/compra/paquetes/pasajeros/${hashPaquete}"/>" class="volver">Volver</a>
					</div>
					<div class="botonone-medio-canje">
						<input type="submit" class="continuar" value="Siguiente" />
					</div>
				</div>
			</form:form>
		</div>
	</div>
	<div class="informacion iframe-main-content-medio-canje">
		<div class="medio-canje off" id="content-iframe-medio-canje" style="display: none">
			<iframe id="iframe-medio-canje" width="720" height="400" frameborder="0"> </iframe>
		</div>
	</div>
</div>


<jsp:include page="../../modalMensajes.jsp" />

<script type="text/javascript">
$(document).on('ready',function() {
	var cotizarPaquete_3 = new CotizarPaquete_3();
	cotizarPaquete_3.cantidadPuntosMaximo = parseInt('${totalMaximoPuntosUsar}', 10);
	cotizarPaquete_3.montoPorCubrirFinal = '${formatPrecioDolares}';
	cotizarPaquete_3.precioDolar = '${reservaPaqueteForm.precioDolares}';
	cotizarPaquete_3.costoPuntos = '${formatCostoEnPuntos}';
	cotizarPaquete_3.totalPuntosUsuario = parseInt('${puntos}');
	cotizarPaquete_3.precioBasePuntos = parseInt('${precioBasePuntos}');
	cotizarPaquete_3.precioDolares = parseFloat(cotizarPaquete_3.montoPorCubrirFinal);
	cotizarPaquete_3.simboloMonedaDolar = '${simboloMonedaDolar}';
	cotizarPaquete_3.urlPasarela = '${urlPasarelaPago}';
	cotizarPaquete_3.htmlProcess = $("#lightbox-procesando-reserva");
	cotizarPaquete_3.htmlFinalizaProcess = $("#lightbox-send-form-paquete");
	cotizarPaquete_3.hashPaquete ='${hashPaquete}';
	
	cotizarPaquete_3.totalImportePuntos = parseInt('${importePuntos}');
	cotizarPaquete_3.totalPuntosCliente = parseInt('${totalPuntosCliente}');
	
	cotizarPaquete_3.init();
});
</script>

