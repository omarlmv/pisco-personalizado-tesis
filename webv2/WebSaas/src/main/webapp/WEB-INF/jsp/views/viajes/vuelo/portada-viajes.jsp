<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>
<c:set var="isSession" value="false"></c:set>
<c:if test="${sessionScope.sessionCliente ne null }">
	<c:set var="isSession" value="true"></c:set>
</c:if>
<style>
.loadinggif {
	background:
		url("${prop['config.url.recursos.base.web']}static/images/ajax/ajax-loader.gif")
		no-repeat right center;
}
</style>
<!-- central -->
<div id="contenedor" class="flotante-explora fondo-desplegado" data-menu="mostrar">
	<!-- miga -->
	<section class="miga">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los
				beneficios</a> <span>></span>
			<p class="activo">Viajes</p>
		</div>
		<div class="fondo"></div>
	</section>
	<div class="cont-top-avisos ocultar-div"></div>
	<div class="img-fondo img-fondo-viajes">
		<!--                    contenido fijo -->
		<div id="slider-viajes">
			<div class="cont-slides">
				<!-- 				<span id="slider-viajes-1" class="slides slider-viajes-1 slide-active"> -->
				<%-- 					<span class="overflow"><img src="${prop['config.url.recursos.base.web']}static/images/banner-viajes/banner-viajes-2/Benefit-vitrina-viajes-machupicchu.png" alt="" /></span> --%>
				<!-- 				</span> -->
				<!-- 				<span id="slider-viajes-2" class="slides slider-viajes-2"> -->
				<%-- 					<span class="overflow"><img src="${prop['config.url.recursos.base.web']}static/images/banner-viajes/banner-viajes-2/Benefit-vitrina-viajes-playa.png" alt="" /></span> --%>
				<!-- 				</span> -->
				<!-- 				<span id="slider-viajes-3" class="slides slider-viajes-3"> -->
				<%-- 					<span class="overflow"><img src="${prop['config.url.recursos.base.web']}static/images/banner-viajes/banner-viajes-2/Benefit-vitrina-viajes-Miami.png" alt="" /></span> --%>
				<!-- 				</span> -->
			</div>
			<div class="slider-viajes-manejadores">
				<!-- 				<a href="#" id="manejador-slide-1" data-orden="1" class="sliderfijoViajes manejador-active"><span class="manejador"></span></a> -->
				<!-- 				<a href="#" id="manejador-slide-2" data-orden="2" class="sliderfijoViajes"><span class="manejador"></span></a> -->
				<!-- 				<a href="#" id="manejador-slide-3" data-orden="3" class="sliderfijoViajes"><span class="manejador"></span></a> -->
			</div>
			<div id="motor-viajes" class="container">
				<form:form id="frmBuscarVuelos"
					action="${pageContext.request.contextPath}/viajes/vuelos/consulta"
					method="get" autocomplete="off" commandName="busquedaVuelosForm">
					<div class="frm-buttons">
						<button type="button" id="btnVuelos">
							<i class="material-icons">airplanemode_active</i>Vuelos
						</button>
						<button type="button" id="btnPaquetes">Paquetes</button>
					</div>
					<div class="content">
						<div class="origen-destino cnt-error">
							<label for="origen"> <i class="material-icons">room</i> <form:input
									id="origen" path="descripcionOrigen" maxlength="200"
									type="text" placeholder="Origen (ciudad o aeropuerto)" /> <form:input
									type="hidden" path="origen" id="origen" class="idOrigen" />
							</label> <label for="destino"> <i class="material-icons">room</i>
								<form:input id="destino" path="descripcionDestino"
									maxlength="200" type="text"
									placeholder="Destino (ciudad o aeropuerto)" /> <form:input
									type="hidden" path="destino" id="destino" class="idDestino" />
							</label>
						</div>
						<div class="tipo-fecha">
							<div id="tipo">
								<label for="chkT2" id="ida-vuelta" class="radio-label">
									<form:radiobutton name="tipo-vuelo" class="tipoVuelo"
										checked="checked" path="tipoVuelo" id="chkT2" value="2" /> <span
									class="radio-input"> <i class=""></i>
								</span> <span>Ida y vuelta</span>
								</label> <label for="chkT1" id="solo-ida" class="radio-label"> <form:radiobutton
										name="tipo-vuelo" class="tipoVuelo" path="tipoVuelo"
										id="chkT1" value="1" /> <span class="radio-input"> <i
										class="hidden"></i>
								</span> <span>Solo ida</span>
								</label>
							</div>
							<div id="fecha" class="cnt-error">
								<i class="material-icons">today</i> <label
									for="calendario-busqueda-ida"> <i
									class="material-icons">today</i> <form:input path="fechaIda"
										id="calendario-busqueda-ida" type="text"
										placeholder="Fecha de ida" maxlength="10" />
								</label> <label for="calendario-busqueda-regreso"> <i
									class="material-icons">event</i> <form:input
										path="fechaRegreso" id="calendario-busqueda-regreso"
										type="text" placeholder="Fecha de vuelta" maxlength="10" />
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
										<button type="button">
											<span>1 Adulto, 0 Ni&ntilde;os, 0 Beb&eacute;s</span><i
												class="material-icons">expand_more</i>
										</button>
										<div class="drop-list" id="cant-pasajeros">
											<div>
												<p>
													<span>Adultos</span><small>Mayor de 12 a&ntilde;os</small>
												</p>
												<ul id="select-adulto">
													<li class="material-icons remover disabled">remove</li>
													<li class="num">1</li>
													<li class="material-icons agregar">add</li>
												</ul>
											</div>
											<div>
												<p>
													<span>Ni&ntilde;os</span><small>Entre 2 a 12
														a&ntilde;os</small>
												</p>
												<ul id="select-ninio">
													<li class="material-icons remover disabled">remove</li>
													<li class="num">0</li>
													<li class="material-icons agregar">add</li>
												</ul>
											</div>
											<div>
												<p>
													<span>Beb&eacute;s</span><small>Entre 0 a 23 meses</small>
												</p>
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
										<button type="button">
											<span>Todas las categorías</span><i class="material-icons">expand_more</i>
										</button>
										<form:select placeholder="Destino" class="select hidden"
											path="clase">
											<form:option value="A" selected="selected">Todas las categorías</form:option>
											<form:option value="M">Economic</form:option>
											<form:option value="W">Economic Premium</form:option>
											<form:option value="F">First</form:option>
											<form:option value="C">Business</form:option>
										</form:select>
										<ul class="drop-list" id="select-clases">
											<li class="drop-item" data-value="A"><span>Todas
													las categorías</span><i class="material-icons">done</i></li>
											<li class="drop-item" data-value="M"><span>Economic</span><i
												class="material-icons"></i></li>
											<li class="drop-item" data-value="W"><span>Economic
													Premium</span><i class="material-icons"></i></li>
											<li class="drop-item" data-value="F"><span>First</span><i
												class="material-icons"></i></li>
											<li class="drop-item" data-value="C"><span>Business</span><i
												class="material-icons"></i></li>
										</ul>
									</div>
								</div>
							</div>
							<button id="buscar">Buscar vuelos</button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
	<div id="slider-3-descuento" class="slider-paquete-destacado"></div>
	<c:if test="${sessionCliente ne null}">
		<section class='flotante-principal'>
			<div class='resaltar'>
				<ul>
					<li><span class="oportunidades-exclusivas"></span></li>
					<li><a href="${pageContext.request.contextPath}/">Ofertas
							exclusivas para ti</a></li>
				</ul>
			</div>
			<div id="racionalPaquete"></div>
		</section>
	</c:if>
</div>
<!-- viajes -->
<div class="limite-viajes">




	<div class="limiteCompras2">
		<div class="limiteCompras cf">
			<!-- 			<p>M&aacute;s oportunidades destacadas en vuelos y paquetes de viajes (<span id="cantidadTotalVuelos"></span>)</p> -->
			<!-- 			<span class="separar centrar"></span> -->

			<div id="lista-ofertas">
				<div class="limiteCompras cf">
					<p>
						Destinos m&aacute;s buscados (<span id="cantidadVuelosRanking"></span>)
					</p>
					<span class="separar centrar"></span>
				</div>
				<section id="lista-paquetes-ranking">
					<div id="main-ranking"></div>
					<div class="num-ofertas"></div>
					<div class="handler-ranking">
						<span class="handler handler-left"></span> <span
							class="handler handler-right"></span>
					</div>
				</section>
				<br />
			</div>
		</div>
		<section id="lista-paquetes">
			<div class="limiteCompras cf">
				<div class="title-paquete">
					<p>
						Oportunidades en paquetes tur&iacute;sticos (<span
							id="cantidadPaquetesDisponibles"></span>)
					</p>
					<span class="separar centrar"></span>
				</div>
				<div class="select">
					<select class="filtro-ordenar comprar" id="cboFiltroPaquete">
						<option value="0">Ordenar por</option>
						<option value="1">Precio más bajo</option>
						<option value="2">Precio más alto</option>
						<option value="3">Nombre (A a Z)</option>
						<option value="4">Nombre (Z a A)</option>
					</select>
				</div>
			</div>
			<div id="main"></div>
		</section>
		<br />
		<section class="sectionVerMas">
			<div class="boton-infinite">
				<a href="javascript:;" id="verMas" class="btn-infinite">Ver
					m&aacute;s <span class="ico-down"></span>
				</a>
			</div>
		</section>
		<nav class="menu-abajo" id="menu-abajo">
			<a href="#" class="retroceder"></a>
			<div id="paginasTotal"></div>
			<a href="#" class="avanzar-activo"></a>
		</nav>
	</div>


</div>
<div id="espacio"></div>
<jsp:include page="modalMensajes.jsp" />

<script id="templatePaqueteDestacado" type="text/x-handlebars-template">
<!-- se comento esto para ponerle slider fijo --> 
<!-- <div class="slider-cnt full"> -->
<!--   <div id="destacadoSlider1" class="img-fondo img-cnt slider-grupo activo"> -->
<!--     <section class="detalle-oportunidad-two" style="background-image: url('{{imageBanner}}');"> -->
<!--       <section id="" class="programar block2-programar"> -->
<!--              <div class="para-clientes-port" style="display:none"> -->
<!--                           <div class="titulo"> -->
<!--                               {{titulo}} -->
<!--                           </div> -->
<!--                     <div class="group-part"> -->
<!--                           <div class="parte1"> -->
<!--                             <h4>{{formatPrecioPuntos}} <strong class="b">Millas Benefit&reg;</strong></h4> -->
<!--                             <p>ó US$ {{formatPrecioDolares}}</p> -->
<!--                           </div> -->
<!--                           <div class="parte1" style="display:none"> -->
<%--                               <a class="link-blue" href="${pageContext.request.contextPath}/viajes/compra/paquetes/{{keyItem}}">{{button}}</a> --%>
<%--                               <p class="subrayado"><a href="${pageContext.request.contextPath}/viajes/compra/paquetes/{{keyItem}}">Ver Detalles</a></p> --%>
<!--                           </div> -->
<!--                     </div> -->
<!--             </div> -->
<!--      </section> -->
<!--     </section> -->
<!--   </div> -->
<!-- </div> -->
</script>
<script id="templateListarPaquete" type="text/x-handlebars-template">
	<article class="grid-item oportunidad" ordenar="{{precioPuntos}}" titulo="{{titulo}}">
		<a href="${pageContext.request.contextPath}/viajes/paquete/{{keyItem}}">
			<div class="cont-flag">
				{{flagDescuento esOferta tipoOferta colorImagenFlag porcentajeDescuento '${prop['url.imagen.producto']}' }}
			</div>
		</a>
		<div class="imagen">
			<a href="${pageContext.request.contextPath}/viajes/paquete/{{keyItem}}">
				<img class="imagen" src="{{imageSmall}}" height="208" width="316" alt="{{titulo}}">
			</a>
		</div>
		<div class="descripcion">
			<div class="nombre" style="word-wrap: break-word;">
				<a href="${pageContext.request.contextPath}/viajes/paquete/{{keyItem}}">{{recortarTitulo titulo 40}}</a>
			</div>
			<div class="precio" style="">
				<div class="puntos"><span>{{formatPrecioPuntos}}</span> Millas Benefit por persona</div>
				&oacute; {{simboloMonedaDolar}} {{formatPrecioDolares}} | {{simboloMonedaSoles}} {{formatPrecioSoles}}
			</div>
			<div class="detalle-compra" style="">
				<a href="${pageContext.request.contextPath}/viajes/paquete/{{keyItem}}" class="ver-detalle">Ver detalle</a>
				<a href="javascript:;" data-paquete="{{keyItem}}" class="boton comprar-paquete">{{button}}</a>
			</div>
		</div>
	</article>
</script>
<script id="templateRacionalPaquete" type="text/x-handlebars-template">
	<div class='resumen' style='display:none'>
		<p class='arrow-explora'></p>
		<div class='detalle'>
			<div class='explora'><a href="#lista-paquetes">
				<p>Explora todos los beneficios</p>
				<p>(<span>{{cantidadPaquetesDestacados}}</span>) en Paquetes</p></a>
			</div>
		</div>
	</div>
</script>


<script id="templateEventos" type="text/x-handlebars-template">
	<div class='{{classEvento}}' style='background-color:{{colorEvento}}'>
		{{#if tipoUrlEvento}}
    	<a href="{{urlEvento}}" target="{{targetEvento}}">
			<p>{{{mensajeEvento}}}</p>
		</a>
		{{else}}
			<p>{{{mensajeEvento}}}</p>
		{{/if}}
		<span class="close-aviso"></span>
	</div>
</script>


<script id="templateListarRanking" type="text/x-handlebars-template">
	<article class="grid-item oportunidad ranking" ordenar="{{precioPuntos}}" titulo="{{titulo}}">
		<div class="imagen">
			<a class="descubrirViaje" id="{{idVueloProcesoAsincrono}}" style="cursor: pointer;">
				<img class="imagen" id="{{iataImagen}}" data-url="${prop['dominio.resources.costamar.iata']}" src="${prop['dominio.resources.costamar.iata']}{{iataImagen}}" height="208" width="316" alt="{{titulo}}">
			</a>
		</div>
		<div class="descripcion">
			<div class="nombre descubrirViaje {{fechaVuelta}}" style="word-wrap: break-word; cursor: pointer;" id="{{idVueloProcesoAsincrono}}">
				{{#if fechaVuelta}} 
					{{ciudadOrigen}} - {{ciudadDestino}} - {{ciudadOrigen}}
				{{else}} 
					{{ciudadOrigen}} - {{ciudadDestino}}
				{{/if}}
			</div>
			<div style="font-weight: bold;">Desde</div>
			<div class="precio" style="">
				<div class="puntos"><span>{{formatPrecioPuntos}}</span> Millas Benefit por persona</div>
				&oacute; {{simboloMonedaDolar}} {{formatPrecioDolares}} | {{simboloMonedaSoles}} {{formatPrecioSoles}}
			</div>
			<div class="detalle-compra" style="">
				<button type="button" class="boton descubrirViaje" id="{{idVueloProcesoAsincrono}}" >Descubrir</button>
			</div>
		</div>
	</article>
</script>

<script type="text/javascript">
	$(document).on('ready',
					function() {
						var portadaViajes = new PortadaViajes();
						portadaViajes.loginCliente = CLIENTE_IS_LOGIN;
						portadaViajes.LIMITE_X_PAGINA = parseInt(
								'${totalXPagina}', 10);
						portadaViajes.valAdulto = 1;
						portadaViajes.valNinio = 0;
						portadaViajes.valInfante = 0;
						portadaViajes.timerBusquedaRespuesta = parseInt(
								'${timerVuelosBusquedaRespuesta}', 10);
						portadaViajes.timerBusquedaMaxEspera = parseInt(
								'${timerVuelosBusquedaMaxEspera}', 10);
						portadaViajes.init();
					});
</script>