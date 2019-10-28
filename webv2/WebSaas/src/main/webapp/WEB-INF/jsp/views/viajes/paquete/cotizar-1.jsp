<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- estado -->
<p class="separar"></p>
<section class="estado">
	<p class="barra"></p>
	<div class="limite">
		<section class="flotante-secundario">
			<div class="mis-canjes">
				<div class="cabec-canjespro"></div>
				<div class="mis-canjes-cnt">
					<p>Mis canjes</p>
					<div class="paquete">
						<div class="paquete-destino">${detallePaquete.tituloPaquete}</div>
						<div class="paquete-detalle">
							<ul>
								<li>N&uacute;mero de pasajeros</li>
								<li class="numero-pasajeros">0</li>
							</ul>
							<hr>
							<div class="paquete-puntos">
								<ul>
									<li>Total en Millas Benefit</li>
									<li id="costo-puntos">${formatoCostoEnPuntosVista}</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<c:if test="${detallePaquete.costoEnPuntos <= puntos}">
					<div class="mis-canjes-mixto">
						<p>
							<span class="icon-check"></span><span class="info-miscanjes-mixto">Te alcanza con tus Millas Benefit</span>
						</p>
					</div>
				</c:if>
				<div class="miscanjes-mixto-paso2">
					<p>
						<span class="color">Si usas</span>
							<input type="text" onkeypress="return isNumber(event)" maxlength="9" id="puntos" class="puntos" value="0" />
							<input type="hidden" id="precio" value="${obtenerDetalle.precioDolares}"></input>
							<input type="hidden" id="precio-soles" value="0"></input>
							<input type="hidden" id="precio-dolares" value="${obtenerDetalle.precioDolares}"></input>
							<input type="hidden" id="totalPuntos" value="${puntos}"></input>
						<span class="color">Millas Benefit</span>
						<button id="btnActualizarConbinacion" type="button">Actualizar</button>
					</p>
					<p>
						<span class="color">Te quedan por cubrir:</span> <span id="montoPorCubrir" class="blue precio-por-cubrir">${requestScope.simboloMonedaDolar}
							${obtenerDetalle.precioDolaresFormateado}</span>
					</p>
				</div>
			</div>
			<!--mis canjes-->
		</section>
		<!-- /flotante -->
		<p class="titulo-canje">Canje de paquete</p>
		<ul class="estado-4pasos estado-operacion">
			<li class="paso-1 presente"><span>1</span>
			<p></p></li>
			<li class="paso-2"><span>2</span>
			<p></p></li>
			<li class="paso-3"><span>3</span>
			<p></p></li>
			<li class="paso-4 completo-off"><span></span>
			<p></p></li>
		</ul>
	</div>
</section>
<!-- /estado -->
<c:if test="${msgAlerta ne null}">
 <div style="background-color: #f2dede;border: 2px solid #ebccd1; position: relative;text-align:center; padding-top:20PX; padding-bottom: 20px;color:#a94442;margin-top:10px;width:68%; margin-bottom: 12%;">
	 <c:out value="${msgAlerta}" escapeXml="false"></c:out>
 </div>
</c:if>
	
	
	<c:if test="${msgAlerta eq null}">
<div class="detalle-wrapp">
	<div class="limite">
		<form:form id="frmViajesParte1" action="${pageContext.request.contextPath}/viajes/compra/paquetes/cotizar/consulta/${hashPaquete}" method="POST" autocomplete="off" commandName="reservaPaqueteForm">
			
			<div class="detalle-wancho informacion">
				<h2>Selecci&oacute;n de fechas</h2>
				<div class="detalle-caja">
					<div class="detalle-left">
						<figure>
							<img src="${detallePaquete.highlightsImage}" alt="${detallePaquete.tituloPaquete}">
						</figure>
						<div class="detalle-clonado"></div>
						<div class="detalle-inputs">
							<ul>
								<li><label for="">Fechas de viaje:</label>
									<form:select path="fecSalidaRegresoVuelo" required="true">
										<form:options items="${listHorariosDisponibles}" itemValue="itemValue" itemLabel="fechaViajeIdaRegresoMostrar" />
									</form:select></li>
								<li><label for="">Adultos <small>(+12 a&ntilde;os)</small>:</label> <form:select path="cantAdulto">
										<c:if test="${stockPaquete > 4}">
											<c:forEach begin="1" end="5" step="1" varStatus="loop">
												<form:option value="${loop.count*2}">${loop.count*2}</form:option>
											</c:forEach>
										</c:if>

										<c:if test="${stockPaquete < 5}">
											<c:forEach begin="1" end="${stockPaquete}" step="1" varStatus="loop">
												<form:option value="${loop.count*2}">${loop.count*2}</form:option>
											</c:forEach>
										</c:if>
									</form:select></li>
							</ul>
							<div class="detalle-radios">
								<div class="errorHabitacion">
									<p>Tipo de habitaci&oacute;n</p>
									<div class="detalle-radio">
										<label for="habitacion1"><form:radiobutton path="habitacion" value="1" required="true" />&nbsp;Doble</label>
									</div>
									<div class="detalle-radio">
										<label for="habitacion2"><form:radiobutton path="habitacion" value="2" />&nbsp;Matrimonial</label>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="detalle-right tabla-cotizar">
						<div class="detalle-datos">
							<h3>${detallePaquete.tituloPaquete}</h3>
							<h4></h4>
							<h5 class="p-persona">
								<strong>${formatPuntosVista}</strong> Millas Benefit por persona
							</h5>
							<p id="costoPaquete"></p>
						</div>
						<div class="cont-detalle-condic">
							<div class="titulo">
								<span class="titulo-detalle titulo-activo" data-titulo="1">Detalles</span> <span class="titulo-condicion" data-titulo="2">Condiciones</span>
							</div>
							<div class="contenido">
								<div class="contenido-detalle contenido-activo" data-contenido="1">
									<p>${obtenerDetalle.descripcion}</p>
								</div>
								<div class="contenido-condicion" data-contenido="2">
									<p>${obtenerDetalle.detallePaquete.infoAdicionalDisponibilidad}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="detalle-botones">
					<a href="javascript:window.history.back();">Volver</a>
					<button class="activo">Siguiente</button>
				</div>
			</div>
		</form:form>
	</div>
</div>
</c:if>
<!-- /informacion -->
<script type="text/javascript">
$(document).on('ready',function() {
	var cotizarPaquete_1 = new CotizarPaquete_1();
	cotizarPaquete_1.montoPorCubrirFinal = '${formatCostoDolarPorCubrir}';
	cotizarPaquete_1.simboloMonedaDolar = "${requestScope.simboloMonedaDolar}";
	cotizarPaquete_1.montoDolares = '${obtenerDetalle.precioDolares}';
	cotizarPaquete_1.montoDolaresFormat = '${formatPrecioDolares}'
	cotizarPaquete_1.millas = '${formatPuntosVista}';
	cotizarPaquete_1.cantidadPersonas = "${cantidadPersonas}";
	cotizarPaquete_1.puntos = "${puntos}";
	cotizarPaquete_1.montoSoles = '${obtenerDetalle.precioSoles}';
	cotizarPaquete_1.montoSolesFormat = '${formatPrecioSoles}';
	cotizarPaquete_1.stockPaquete = "${stockPaquete}";
	cotizarPaquete_1.costoEnPuntos = "${costoEnPuntos}";
	cotizarPaquete_1.init();
});
</script>