<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style>
body {
	background-color: #f2f1f1;
}
.blockUI {
	background-color: none !important;
}
#frmBuscarVuelos input[type="radio"] {
	opacity: 1;
	position: inherit;
}
#frmBuscarVuelos .calendario.regreso {
	float: none;
}
#frmBuscarVuelos .aerolinea {
	overflow: inherit;
}
#frmBuscarVuelos .seleccion-pasajeros {
	float: none;
	margin-left: 0;
}

@media ( max-width :758px) {
	#frmBuscarVuelos .nro-pasajero .nro-pj-input {
		margin-bottom: 33px;
	}
}
</style>
<!-- estado -->

<p class="separar"></p>
<section class="estado">
	<p class="barra"></p>
	<div class="limite">
		<!-- Inicio Buscado fijo -->
		<div id="motor-fijo">
			<div class="info-vuelo">
				<div>
					<div class="origen-destino">
						<span data-name="origen"><c:out value="${busquedaVuelosForm.origen}" /></span>
<!-- 						<span class="origen"></span> -->
						<span class="ico icon-flecha-derecha"></span>
						<span data-name="destino"><c:out value="${busquedaVuelosForm.destino}" /></span>
<!-- 						<span class="destino"></span> -->
					</div>
					<div class="detalle-vuelo">
						<ul>
							<li id="fechaIdaBusqueda"></li>
							<li id="flechaRegresoBusqueda" class="ico icon-flecha-derecha"></li>
							<li id="fechaRegresoBusqueda"></li>
						</ul>
						<ul>
							<li id="tipoVueloBusqueda"></li>
							<li id="tipoClaseVueloBusqueda"></li>
							<li id="cantidadPasajerosBusqueda"></li>
						</ul>
					</div>
				</div>
				<a id="btnModificar" class="btn-success" href="#">Modificar</a>
			</div>
			<form id="modificarBusqueda" class="aerolinea">
				<div class="close-modal"></div>
				<div class="groupone-input-rowone">
					<div class="groupone-one-reg">
						<input type="text" placeholder="Origen" id="modificarOrigen" name="modificarOrigen"/>
						<input type="hidden" name="modificarOrig" id="modificarOrigen" />
					</div>
					<div class="groupone-flecha-reg">
						<span class="icon-flecha-derecha"></span>
					</div>
					<div class="groupone-one-reg">
						<input type="text" placeholder="Destino" id="modificarDestino" name="modificarDestino"/>
						<input type="hidden" name="modificarDest" id="modificarDestino" />
					</div>
				</div>
				<div class="group-input-radio">
					<div class="radiobutton-one">
						<input type="radio" name="modTipoVuelo" class="modTipoVuelo" id="modificarTipoVuelo1" value="2"/>
						<label for="modificarTipoVuelo1">Ida y regreso</label>
					</div>
					<div class="radiobutton-one">
						<input type="radio" name="modTipoVuelo" class="modTipoVuelo" id="modificarTipoVuelo2" value="1"/>
						<label for="modificarTipoVuelo2">Solo ida</label>
					</div>
				</div>
				<div class="groupone-input-rowone">
					<div class="groupone-one-reg">
						<input type="text" id="modificarFechaIda" name="modificarFechaIda"  placeholder="Fecha de ida" class="calendario ida" maxlength="10" readonly/>
						<span class="icon-ico-calendario"></span>
					</div>
					<div class="groupone-flecha-reg">
						<span class="icon-flecha-derecha"></span>
					</div>
					<div class="groupone-one-reg">
						<input type="text" id="modificarFechaRegreso" name="modificarFechaRegreso" placeholder="Fecha de regreso" class="calendario regreso" maxlength="10" readonly/>
						<span class="icon-ico-calendario"></span>
					</div>
				</div>
				<div class="groupthree-input-rowthree">
					<div class="groupone-one-reg">
						<input class="pointer" type="text" readonly id="modificarPasajeros"/>
						<input type="hidden" id="modCantAdulto"/>
						<input type="hidden" id="modCantNinio"/>
						<input type="hidden" id="modCantBebe"/>
						<ul class="select-pasajero">
							<li>
								<span>Adultos (12 a&ntilde;os o m&aacute;s)</span>
								<ul class="pj-menu pj-menu-adulto">
									<li><a href="#" class="pj-menos"></a></li>
									<li class="pj-nro"></li>
									<li><a href="#" class="pj-mas"></a></li>
								</ul>
							</li>
							<li>
								<span>Ni&ntilde;os (2 a 11 a&ntilde;os)</span>
								<ul class="pj-menu pj-menu-ninio">
									<li><a href="#" class="pj-menos"></a></li>
									<li class="pj-nro"></li>
									<li><a href="#" class="pj-mas"></a></li>
								</ul>
							</li>
							<li>
								<span>Beb&eacute;s (0 a 23 meses)</span>
								<ul class="pj-menu pj-menu-bebe">
									<li><a href="#" class="pj-menos"></a></li>
									<li class="pj-nro"></li>
									<li><a href="#" class="pj-mas"></a></li>
								</ul>
							</li>
							<li class="cerrar-pj">Cerrar</li>
						</ul>
					</div>
					<div class="nro-pasajero-select select-pas1-vuelos select-modificar">
						<div class="pj-adulto">
							<ul>
								<li class="elemento"><span>Adultos (12 a&ntilde;os o m&aacute;s)</span></li>
								<li class="elemento">
									<ul class="pj-menu">
										<li><a href="#" class="pj-menos"></a></li>
										<li class="pj-nro">${adultos}</li>
										<li><a href="#" class="pj-mas"></a></li>
									</ul>
								</li>
							</ul>
						</div>
						<div class="pj-nino">
							<ul>
								<li class="elemento"><span>Nios (2 a 11 a&ntilde;os)</span></li>
								<li class="elemento">
									<ul class="pj-menu">
										<li><a href="#" class="pj-menos"></a></li>
										<li class="pj-nro">${ninio}</li>
										<li><a href="#" class="pj-mas"></a></li>
									</ul>
								</li>
							</ul>
						</div>
						<div class="pj-bebe">
							<ul>
								<li class="elemento"><span>Bebs (0 a 23 meses)</span></li>
								<li class="elemento">
									<ul class="pj-menu">
										<li><a href="#" class="pj-menos"></a></li>
										<li class="pj-nro">${infante}</li>
										<li><a href="#" class="pj-mas"></a></li>
									</ul>
								</li>
							</ul>
						</div>
						<a class="pj-cerrar" href="#">Cerrar</a>
					</div>
					<div class="groupone-flecha-reg"></div>
					<div class="groupone-one-reg">
						<select id="modificarClaseVuelo">
							<option value="A">Todas las categorías</option>
							<option value="M">Economic</option>
							<option value="W">Economic Premium</option>
							<option value="F">First</option>
							<option value="C">Business</option>
						</select>
					</div>
				</div>
				<div class="group-button-formreg">
					<input type="submit" value="Modificar b&uacute;squeda" id="btnModificarBusqueda">
				</div>
			</form>
			<div class="filtros-vuelo">
				<p>Tienes <span id="cantidadDeBusquedas"></span> opciones para elegir</p>
				<a id="filtros-vuelo" href="#">Filtrar opciones</a>
			</div>
			<form class="detalle-filtro-vuelo">
				<div class="tipo-filtro-vuelo">
					<label for="cboFiltroEscalas">
						<span>N&uacute;mero de escalas</span>
						<select id="cboFiltroEscalas">
							<option value="todos">Todas las escalas</option>
							<option value="directo">Directo</option>
							<option value="escala">1 escala</option>
							<option value="xescala">2 escalas o m&aacute;s</option>
						</select>
					</label>
					<label for="cboFiltroAerolineas">
						<span>Aerol&iacute;neas</span>
						<select id="cboFiltroAerolineas">
							<option value="todos">Todas las Aerol&iacute;neas</option>
						</select>
					</label>
				</div>
				<div class="horario-filtro-vuelo">
					<div class="label">
						<span>Horario de salida:</span>
						<div class="range-slide" id="rangeSlideSalida"></div>
						<div class="range-text">
							<ul>
								<li>Desde:</li>
								<li class="salida-desde">0:00 hrs.</li>
							</ul>
							<ul>
								<li>Hasta:</li>
								<li class="salida-hasta">24:00 hrs.</li>
							</ul>
						</div>
					</div>
					<div class="label">
						<span>Horario de regreso:</span>
						<div class="range-slide" id="rangeSlideRegreso"></div>
						<div class="range-text">
							<ul>
								<li>Desde:</li>
								<li class="regreso-desde">0:00 hrs.</li>
							</ul>
							<ul>
								<li>Hasta:</li>
								<li class="regreso-hasta">24:00 hrs.</li>
							</ul>
						</div>
					</div>
				</div>
			</form>
		</div>
		<!-- Fin Buscado fijo -->
		<!-- flotante -->
		<section class="flotante-secundario">
			<div class="mis-canjes">
				<div class="cabec-canjespro"></div>
				<div class="mis-canjes-cnt">
					<p>Mis canjes</p>
					<div class="paquete">
						<div class="paquete-destino">
							<c:out value="${busquedaVuelosForm.origen}" /> - <c:out value="${busquedaVuelosForm.destino}" /> <c:if test="${busquedaVuelosForm.tipoVuelo == 2}">- <c:out value="${busquedaVuelosForm.origen}" /></c:if>
						</div>
						<div class="paquete-detalle">
							<ul>
								<li>N&uacute;mero de pasajeros</li>
								<li>${adulto + ninio + infante}</li>
							</ul>
							<ul>
								<li>Ida</li>
								<li>${busquedaVuelosForm.fechaIda}</li>
							</ul>
							<c:if test="${not empty busquedaVuelosForm.fechaRegreso}">
								<ul>
									<li>Vuelta</li>
									<li>${busquedaVuelosForm.fechaRegreso}</li>
								</ul>
							</c:if>
							<hr>
							<div class="paquete-puntos">
								<ul>
									<li>Total en Millas Benefit desde</li>
									<li class="formatValorPuntos">-</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<div class="blockdown-zonaracional">
					<div class="check-alcance" style="display: none">
						<p>
							<span class="icon-check"></span> Te alcanza con tus Millas Benefit
						</p>
						<p>
							Puntos disponibles tras el canje: <span class="puntos-disponibles-canje"></span>
						</p>
					</div>
				</div>
			</div>
		</section>
		<!-- /flotante -->
		<p class="titulo-canje">Reserva de vuelo</p>
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
<!-- informacion -->
<div class="limite acomodar-racional">
	<div id="informacion-pasajeros" class="informacion p1-wancho">
		<div class="header-seleccion-vuelos">
			<h3><c:out value="${tituloVueloPaso1}" escapeXml="false"></c:out></h3>
			<p>
				Vuelo <c:out value="${busquedaVuelosForm.origen}" /> - <c:out value="${busquedaVuelosForm.destino}" />
				<c:if test="${busquedaVuelosForm.tipoVuelo == 2}">- <c:out value="${busquedaVuelosForm.origen}" /></c:if>
			</p>
		</div>
		<div id="motor-viajes" class="container">
			<form:form id="frmBuscarVuelos" action="${pageContext.request.contextPath}/viajes/vuelos/consulta" method="get" autocomplete="off" commandName="busquedaVuelosForm">
				<div class="content">
					<div class="origen-destino cnt-error">
						<label for="origen">
							<i class="material-icons">room</i>
							<form:input id="origen" path="descripcionOrigen" maxlength="200" type="text" placeholder="Origen (ciudad o aeropuerto)"/>
							<form:input type="hidden" path="origen" id="origen" class="idOrigen"/>
						</label>
						<label for="destino">
							<i class="material-icons">room</i>
							<form:input id="destino" path="descripcionDestino" maxlength="200" type="text" placeholder="Destino (ciudad o aeropuerto)"/>
							<form:input type="hidden" path="destino" id="destino" class="idDestino" />
						</label>
					</div>
					<div class="tipo-fecha">
						<div id="tipo">
							<label for="chkT2" id="ida-vuelta" class="radio-label">
								<form:radiobutton name="tipo-vuelo" class="tipoVuelo" checked="checked" path="tipoVuelo" id="chkT2" value="2"/>
								<span class="radio-input">
									<i class=""></i>
								</span>
								<span>Ida y vuelta</span>
							</label>
							<label for="chkT1" id="solo-ida" class="radio-label">
								<form:radiobutton name="tipo-vuelo" class="tipoVuelo" path="tipoVuelo" id="chkT1" value="1"/>
								<span class="radio-input">
									<i class="hidden"></i>
								</span>
								<span>Solo ida</span>
							</label>
						</div>
						<div id="fecha" class="cnt-error">
							<i class="material-icons">today</i>
							<label for="calendario-busqueda-ida">
								<i class="material-icons">today</i>
								<form:input path="fechaIda" id="calendario-busqueda-ida" type="text" placeholder="Fecha de ida" maxlength="10" />
							</label>
							<label for="calendario-busqueda-regreso">
								<i class="material-icons">event</i>
								<form:input path="fechaRegreso" id="calendario-busqueda-regreso" type="text" placeholder="Fecha de vuelta" maxlength="10" />
							</label>
						</div>
					</div>
					<div class="mas-opciones">
						<div class="pasajeros-clases">
							<div class="pasajeros">
								<form:hidden path="cantidadAdultos" />
								<form:hidden path="cantidadNinios" />
								<form:hidden path="cantidadInfantes" />
								<div class="dropdown">
									<button type="button"><span>1 Adulto, 0 Ni&ntilde;os, 0 Beb&eacute;s</span><i class="material-icons">expand_more</i></button>
									<div class="drop-list" id="cant-pasajeros">
										<div>
											<p><span>Adultos</span><small>Mayor de 12 a&ntilde;os</small></p>
											<ul id="select-adulto">
												<li class="material-icons remover disabled">remove</li>
												<li class="num">1</li>
												<li class="material-icons agregar">add</li>
											</ul>
										</div>
										<div>
											<p><span>Ni&ntilde;os</span><small>Entre 2 a 12 a&ntilde;os</small></p>
											<ul id="select-ninio">
												<li class="material-icons remover disabled">remove</li>
												<li class="num">0</li>
												<li class="material-icons agregar">add</li>
											</ul>
										</div>
										<div>
											<p><span>Beb&eacute;s</span><small>Entre 0 a 23 meses</small></p>
											<ul id="select-bebe">
												<li class="material-icons remover disabled">remove</li>
												<li class="num">0</li>
												<li class="material-icons agregar">add</li>
											</ul>
										</div>
										<span class="err"></span>
										<div class="buttons">
											<button id="borrar" type="button">Borrar</button>
											<button id="guardar" type="button">Guardar</button>
										</div>
									</div>
								</div>
							</div>
							<div class="clases">
								<div class="dropdown" data-type="overpost">
									<button type="button"><span>Todas las categorías</span><i class="material-icons">expand_more</i></button>
									<form:select placeholder="Destino" class="select hidden" path="clase">
										<form:option value="A">Todas las categorías</form:option>
										<form:option value="M">Economic</form:option>
										<form:option value="W">Economic Premium</form:option>
										<form:option value="F">First</form:option>
										<form:option value="C">Business</form:option>
									</form:select>
									<ul class="drop-list" id="select-clases">
										<li class="drop-item" data-value="A"><span>Todas las categorías</span><i class="material-icons"></i></li>
										<li class="drop-item" data-value="M"><span>Economic</span><i class="material-icons"></i></li>
										<li class="drop-item" data-value="W"><span>Economic Premium</span><i class="material-icons"></i></li>
										<li class="drop-item" data-value="F"><span>First</span><i class="material-icons"></i></li>
										<li class="drop-item" data-value="C"><span>Business</span><i class="material-icons"></i></li>
									</ul>
								</div>
							</div>
						</div>
						<button id="buscar">Buscar vuelos</button>
					</div>
				</div>
			</form:form>
		</div>
		
		<input type="hidden" value="" id="precio-busqueda" />
		<div class="contenido opciones-vuelosP2" >
			<span class="cargador"></span>
			<section class="select-three-types">
				<div class="aerolinea-filtrosone" style="display: none">
					<input type="text" value="Filtros de bsqueda">
				</div>
				<div class="aerolinea-tituloopcion">
					<div class="titulo-option">
						<p>
							Tienes <span class="cantidad-vuelos" id="cantidadVuelos"></span> opciones para elegir
						</p>
					</div>
					<div class="titulo-option open-filtros">
						<a href="#"><span class="icon-down"></span>Filtrar opciones</a>
					</div>
				</div>
				
				<div id="filtro-vuelo-desplegable">
					<form class="despl-detalle-filtro-vuelo">
						<div class="tipo-filtro-vuelo">
							<label for="desplCboFiltroEscalas">
								<span>N&uacute;mero de escalas</span>
								<select id="desplCboFiltroEscalas">
									<option value="todos">Todas las escalas</option>
									<option value="directo">Directo</option>
									<option value="escala">1 escala</option>
									<option value="xescala">2 escalas o m&aacute;s</option>
								</select>
							</label>
							<label for="desplCboFiltroAerolineas">
								<span>Aerol&iacute;neas</span>
								<select id="desplCboFiltroAerolineas">
									<option value="todos">Todas las Aerol&iacute;neas</option>
								</select>
							</label>
						</div>
						<div class="horario-filtro-vuelo">
							<div class="label">
								<span>Horario de salida:</span>
								<div class="range-slide" id="desplRangeSlideSalida"></div>
								<div class="range-text">
									<ul>
										<li>Desde:</li>
										<li class="salida-desde">0:00 hrs.</li>
									</ul>
									<ul>
										<li>Hasta:</li>
										<li class="salida-hasta">24:00 hrs.</li>
									</ul>
								</div>
							</div>
							<div class="label">
								<span>Horario de regreso:</span>
								<div class="range-slide" id="desplRangeSlideRegreso"></div>
								<div class="range-text">
									<ul>
										<li>Desde:</li>
										<li class="regreso-desde">0:00 hrs.</li>
									</ul>
									<ul>
										<li>Hasta:</li>
										<li class="regreso-hasta">24:00 hrs.</li>
									</ul>
								</div>
							</div>
						</div>
					</form>
				</div>
				
				<!-- Filtro de precios por tabla  --> 
				<div id="matrizVuelos">
				
				</div>
				<script id="templateMatrizVuelo" type="text/x-handlebars-template">
					<section class="aerolinea formreg-pasone-viaje filtro-matriz">
						<div class="cont-filtro-tabla">
							<table class="filtro-por-tabla" id="escalas">
								<tr class="mostrar-todos">
									<td class="filtro-viaje1 filtro-todos" id="filtroTodasAerolineas" >Escalas<img src="${prop['config.url.recursos.base.web']}static/images/division-matriz.png" alt="" />Aerol&iacute;neas</td>
								</tr>
								<tr class="lista-lineas-todo">
									<td class="filtro-viaje1 filtro-directo"><a id="filtro-directo" href="javascript:void(0);" data-escala="0">DIRECTO desde</a><i class="close"></i></td>
								</tr>
								<tr class="lista-lineas-todo">
									<td class="filtro-viaje1 filtro-escala"><a id="filtro-escala" href="javascript:void(0);" data-escala="1">1 ESCALA desde</a><i class="close"></i></td>
								</tr>
								<tr class="lista-lineas-todo">
									<td class="filtro-viaje1 filtro-xescala"><a id="filtro-xescala" href="javascript:void(0);" data-escala="2">2 ESCALAS o m&aacute;s desde</a><i class="close"></i></td>
								</tr>
							</table>
							<table class="filtro-por-tabla" id="aerolineas">
								<tr class="lista-lineas-aero">
								
								{{#each aereolineasDisponibles as |aeroDisponible|}}
									<td class="filtro-viaje1 num-aerolineas">
										<a href="javascript:void(0);" id="{{aeroDisponible.codigoLineaPrincipal}}" data-aero="{{aeroDisponible.codigoLineaPrincipal}}" class="{{aeroDisponible.codigoLineaPrincipal}}">
											<img class="logo-aerolinea" src="${prop['dominio.resources.costamar']}{{aeroDisponible.codigoLineaPrincipal}}.png" alt="" />
										</a>
										<i class="close"></i>
									</td>
								{{/each}}
								</tr>
								<tr class="lista-lineas">
								{{#each aereolineasDisponibles as |aeroDisponible|}}
									<td class="num-directo" rel="0">
										<a href="javascript:void(0);" id="directo-{{aeroDisponible.codigoLineaPrincipal}}" data-aero="{{aeroDisponible.codigoLineaPrincipal}}" data-precio="" data-escala="" ></a>
										<i class="close"></i>
									</td>
								{{/each}}
								</tr>
								<tr class="lista-lineas">
								{{#each aereolineasDisponibles as |aeroDisponible|}}
									<td class="num-escala" rel="1">
										<a href="javascript:void(0);" id="escala-{{aeroDisponible.codigoLineaPrincipal}}" data-aero="{{aeroDisponible.codigoLineaPrincipal}}" data-precio="" data-escala="" ></a>
										<i class="close"></i>
									</td>
								{{/each}}
								</tr>
								<tr class="lista-lineas">
								{{#each aereolineasDisponibles as |aeroDisponible|}}
									<td class="num-escalas" rel="2">
										<a href="javascript:void(0);" id="xescala-{{aeroDisponible.codigoLineaPrincipal}}" data-aero="{{aeroDisponible.codigoLineaPrincipal}}" data-precio="" data-escala="" "></a>
										<i class="close"></i>
									</td>
								{{/each}}
							</table>
						</div>
						<span class="handler handler-left"><img src="${prop['config.url.recursos.base.web']}static/images/ico-retroceder-oferta-blanco.png" alt="" /></span>
						<span class="handler handler-right"><img src="${prop['config.url.recursos.base.web']}static/images/ico-avanzar-oferta-blanco.png" alt="" /></span>
						<div class="cont-handler-responsive">
							<span class="handler-responsive handler-left"><img src="${prop['config.url.recursos.base.web']}static/images/ico-retroceder-oferta-blanco.png" alt="" /></span>
							<span class="handler-responsive handler-right"><img src="${prop['config.url.recursos.base.web']}static/images/ico-avanzar-oferta-blanco.png" alt="" /></span>
						</div>
					</section>
				</script>
			<!--	Filtro de precios por tabla   -->
			</section>
			<div class="cont-cboOrdenarVuelo">
				<form id="cboOrdenarVuelo">
					<select name="fitlr-radio" id="fitlr-radio">
						<option value="1" id="filtr-one">Ordenar por menor precio</option>
						<option value="2" id="filtr-two">Ordenar por mayor precio</option>
					</select>
				</form>
			</div>
			<div id="msgUy">
				<p>&iexcl;Uy! Parece que no encontramos nada con esa combinaci&oacute;n de filtros.</p>
			</div>
			<div id="baseVuelos">
				
			</div>
			<script id="templateBaseVuelo" type="text/x-handlebars-template">
<!--				<div class="choose-flight">-->					
 				<div class="seleccion-vuelo"> 
 					<form id="formulario{{index}}" class="info-vuelo">
 						<h3>Elegir ida &middot; <span>{{base.fechaSalidaGeneral}}</span></h3>
 						<div class="opciones-vuelo">
 						{{#each base.vuelos as |vuelo|}}
 							{{#ifconditional vuelo.indicador value=0}}
 								<div>
 									<label class="radio-label">
 										<input type="radio" name="vueloSalidaParam" id="vueloSalidaParam" checked="checked" value="{{vuelo.hashGenerado}}"/>
 										<span class="radio-input">
 											<i class="hidden"></i> 
 										</span>
 									</label>
 									<div>
 										<p class="horario">{{vuelo.horaSalida}}</p>
 										<p class="gray">${origen}</p>
 										<p class="data-responsive">{{vuelo.segmentos.[0].numeroVuelo}}</p>
									</div>
									<div>
										<p class="duracion">{{vuelo.duracion}}<i class="material-icons">play_arrow</i></p>
										{{#ifmayor vuelo.numeroEscalas value=0}}
											<p class="detalle-escalas">{{vuelo.numeroEscalas}} Escala(s)</p>
											<div class="bg-escalas">
												<div class="escalas">
													<div class="close material-icons">close</div>
													<h5>Escalas</h5>
													{{#each vuelo.segmentos as |segmento|}}
													<div class="tramo">
														<h6>Tramo {{incrementar @index}}</h6>
														<p>VUELO {{segmento.numeroVuelo}} &middot; Operado por {{segmento.aerolineaNombreOperadora}}</p>
														<ul>
															<li>
																<p>{{segmento.horaSalida}} <span class="gray">{{segmento.aereopuertoOrigen.codigoIata}}</span></p>
																<p>Sale: {{segmento.fechaSalida}}</p>
																<p>{{segmento.aereopuertoOrigen.nombre}}</p>
															</li>
															<li>
																<span class="material-icons">play_arrow</span>
															</li>
															<li>
																<p>{{segmento.horaLlegada}} <span class="gray">{{segmento.aereopuertoDestino.codigoIata}}</span></p>
																<p>Llega: {{segmento.fechaLlegada}}</p>
																<p>{{segmento.aereopuertoDestino.nombre}}</p>
															</li>
														</ul>
													</div>
													{{/each}}
												</div>
											</div>
										{{else}}
											<p class="gray">Directo</p>
										{{/ifmayor}}
										<p class="data-responsive clase">{{#tipoCabinaLabel vuelo.segmentos.[0].tipoCabina}}{{/tipoCabinaLabel}}</p>
									</div>
									<div>
										<p class="horario">{{vuelo.horaLlegada}}</p>
										<p class="gray">${destino}</p>
										<p class="data-responsive">
										{{#validacionLan vuelo}}
											<button type="button" data-tarifa="{{vuelo.nombreTarifa}}" class="tipo-tarifa">Tarifa {{capitalizeTarifa vuelo.nombreTarifa}}</button>
										{{/validacionLan}}
										</p>
									</div>
									<div>
										<p class="clase">{{#tipoCabinaLabel vuelo.segmentos.[0].tipoCabina}}{{/tipoCabinaLabel}}</p>
										<p class="gray">Vuelo {{vuelo.segmentos.[0].numeroVuelo}}</p>
									</div>
									<div>
										<img src="${prop['dominio.resources.costamar']}{{vuelo.aereolinea.codigoAereoLinea}}.png"/>
											{{#validacionLan vuelo}}
												<button type="button" data-tarifa="{{vuelo.nombreTarifa}}" class="tipo-tarifa">Tarifa {{capitalizeTarifa vuelo.nombreTarifa}}</button>
											{{/validacionLan}}
									</div>
								</div>
							{{/ifconditional}}
						{{/each}}
						</div>
						{{#mostrarVueloRegreso base.vuelos}}
						<h3>Elegir vuelta &middot; <span>{{base.fechaRegresoGeneral}}</span></h3>
						<div class="opciones-vuelo">
						{{#each base.vuelos as |vuelo|}}
							{{#ifconditional vuelo.indicador value=1}}
								<div>
									<label class="radio-label">
										<input type="radio" name="vueloRegresoParam" id="vueloRegresoParam" checked="checked" value="{{vuelo.hashGenerado}}"/>
										<span class="radio-input">
											<i class="hidden"></i>
										</span>
									</label>
									<div>
										<p class="horario">{{vuelo.horaSalida}}</p>
										<p class="gray">${destino}</p>
										<p class="data-responsive">{{vuelo.segmentos.[0].numeroVuelo}}</p>
									</div>
									<div>
										<p class="duracion">{{vuelo.duracion}}<i class="material-icons">play_arrow</i></p>
										{{#ifmayor vuelo.numeroEscalas value=0}}
											<p class="detalle-escalas">{{vuelo.numeroEscalas}} Escala(s)</p>
											<div class="bg-escalas">
												<div class="escalas">
													<div class="close material-icons">close</div>
													<h5>Escalas</h5>
													{{#each vuelo.segmentos as |segmento|}}
													<div class="tramo">
														<h6>Tramo {{incrementar @index}}</h6>
														<p>VUELO {{segmento.numeroVuelo}} &middot; Operado por {{segmento.aerolineaNombreOperadora}}</p>
														<ul>
															<li>
																<p>{{segmento.horaSalida}} <span class="gray">{{segmento.aereopuertoOrigen.codigoIata}}</span></p>
																<p>Sale: {{segmento.fechaSalida}}</p>
																<p>{{segmento.aereopuertoOrigen.nombre}}</p>
															</li>
															<li>
																<span class="material-icons">play_arrow</span>
															</li>
															<li>
																<p>{{segmento.horaLlegada}} <span class="gray">{{segmento.aereopuertoDestino.codigoIata}}</span></p>
																<p>Llega: {{segmento.fechaLlegada}}</p>
																<p>{{segmento.aereopuertoDestino.nombre}}</p>
															</li>
														</ul>
													</div>
													{{/each}}
												</div>
											</div>
										{{else}}
											<p class="gray">Directo</p>
										{{/ifmayor}}
										<p class="data-responsive clase">{{#tipoCabinaLabel vuelo.segmentos.[0].tipoCabina}}{{/tipoCabinaLabel}}</p>
									</div>
									<div>
										<p class="horario">{{vuelo.horaLlegada}}</p>
										<p class="gray">${origen}</p>
										<p class="data-responsive">
										{{#validacionLan vuelo}}
											<button type="button" data-tarifa="{{vuelo.nombreTarifa}}" class="tipo-tarifa">Tarifa {{capitalizeTarifa vuelo.nombreTarifa}}</button>
										{{/validacionLan}}
										</p>
									</div>
									<div>
										<p class="clase">{{#tipoCabinaLabel vuelo.segmentos.[0].tipoCabina}}{{/tipoCabinaLabel}}</p>
										<p class="gray">Vuelo {{vuelo.segmentos.[0].numeroVuelo}}</p>
									</div>
									<div>
										<img src="${prop['dominio.resources.costamar']}{{vuelo.aereolinea.codigoAereoLinea}}.png"/>
										{{#validacionLan vuelo}}
											<button type="button" data-tarifa="{{vuelo.nombreTarifa}}" class="tipo-tarifa">Tarifa {{capitalizeTarifa vuelo.nombreTarifa}}</button>
										{{/validacionLan}}
									</div>
								</div>
							{{/ifconditional}}
						{{/each}}
						</div>
						{{/mostrarVueloRegreso}}
						<input type="button" value="Siguiente" class="btn-default btn-responsive btnViajeToPaso2" id="{{index}}"/>
					</form>
					<div class="info-precio">
						<h4>{{safeString base.aereoLineaPrincipal}}</h4>
						<h3><span>{{base.dolares.formatEquivalentePuntosAdulto}} </span><small>Millas Benefit por adulto</small></h3>
						<p class="precio-dolares">o ${requestScope.simboloMonedaDolar} {{base.dolares.formatAdulto}}</p>
						<ul class="detalle-precios">
							{{#ifconditional nroPasajeros value=1}}	
								{{#each base.listaTotalFareVuelosXpasajero as |fareVuelos|}}
								<li>
									{{#ifconditional fareVuelos.passengerType value='1 adulto'}}
										<span>Tarifa Base</span>
									{{else}}
										<span>{{capitalize fareVuelos.passengerType}}</span>
									{{/ifconditional}}
									{{#each fareVuelos.currencies as |currency|}}
										<span>{{currency.formatEquivalentePuntos}}</span>
									{{/each}}
								</li>
								{{/each}}
							{{else}}
								{{#each base.listaTotalFareVuelosXpasajero as |fareVuelos|}}
								<li>
									<span>{{capitalize fareVuelos.passengerType}}</span>
									{{#each fareVuelos.currencies as |currency|}}
										<span>{{currency.formatEquivalentePuntos}}</span>
									{{/each}}
								</li>
								{{/each}}
							{{/ifconditional}}
							<li>
								<span>Impuestos</span>
								<span>{{base.dolares.formatEquivalentePuntosTraxes}}</span>
								<span></span>
							</li>
							{{#ifconditional base.dolares.formatDiscount value=0}}
							{{else}}
								<li class="subvencion">
									<span>Descuento</span>
									<span>{{base.dolares.formatDiscount}}</span>
									<span></span>
								</li>
							{{/ifconditional}}
						</ul>
						<p class="total">
							<span>Total</span>
							<span><b>{{base.dolares.formatEquivalentePuntos}}</b> <small>Millas Benefit</small><br><small>o ${requestScope.simboloMonedaDolar} <b style="font-size:14px;">{{base.dolares.formatTotal}}</b></small></span>
						</p>
						<input type="button" value="Siguiente" class="btn-default btnViajeToPaso2" id="{{index}}"/>
					</div>
				</div>
			</script>

			<div class="botonera">
				<a href="<c:url value="/viajes"/>" class="volver">Volver</a>
			</div>
		</div>
	</div>
</div>
<div id="modal-tarifas">
	<div>
		<div class="close"></div>
		<img src="${prop['config.url.recursos.base.web']}static/images/latam/tarifa_light.png"/>
	</div>
</div>
<jsp:include page="modalMensajes.jsp" />
<script type="text/javascript">
$(document).on('ready',function() {
	var seleccionVuelo = new SeleccionVuelo();
	seleccionVuelo.totalPuntos = '${totalPuntos}';
	seleccionVuelo.preciosRacional = [];
	seleccionVuelo.recorrer = '';
	seleccionVuelo.tipoVuelo='${tipoVuelo}';
	seleccionVuelo.valAdulto='${adulto}';
	seleccionVuelo.valNinio='${ninio}';
	seleccionVuelo.valInfante='${infante}';
	seleccionVuelo.timerBusquedaRespuesta = parseInt('${timerVuelosBusquedaRespuesta}', 10);
	seleccionVuelo.timerBusquedaMaxEspera = parseInt('${timerVuelosBusquedaMaxEspera}', 10);
	seleccionVuelo.fechaIdaBusqueda = '${busquedaVuelosForm.fechaIda}';
	seleccionVuelo.fechaRegresoBusqueda = '${busquedaVuelosForm.fechaRegreso}';
	seleccionVuelo.init();
	
});
</script>