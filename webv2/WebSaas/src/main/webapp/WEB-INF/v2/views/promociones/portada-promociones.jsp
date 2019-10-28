<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>
<c:set var="isSession" value="false"></c:set>
<c:if test="${sessionScope.sessionCliente ne null }">
	<c:set var="isSession" value="true"></c:set>
</c:if>

<div id="portada-promociones-page">
	<section class="miga miga-inferior">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los beneficios</a> <span>&gt;</span>
			<p class="activo">Promociones</p>
		</div>
	</section>
</div>

<section id="banner">
	<article>
		<div class="layer"></div>
		<img src="${prop['config.url.recursos.base.web']}static/images/promociones/banner-home.jpg" alt="Promociones exclusivas">
		<div class="container">
			<div>
			<c:choose>
				<c:when test="${isSession eq false}">
					<h2>${promociones.body.hero.titulo}</h2>
					<h3>${promociones.body.hero.subtitulo}</h3>
					<a onclick="loginHandler()" href="javascript:void(0)" class="btn-verde">Ver m&aacute;s</a>
				</c:when>
				<c:otherwise>
					<h2>${promociones.body.hero.saludo}</h2>
					<h3>${promociones.body.hero.subtitulo}</h3>
				</c:otherwise>
			</c:choose>
			</div>
		</div>
	</article>
</section>

<section id="mejores-ofertas" class="container">
	<div class="title-zone">
		<h3>${promociones.body.exclusiva.subtituloStatic}</h3>
		<h4>${promociones.body.exclusiva.tituloStatic}</h4>
	</div>
	<div id="cnt-exclusiva1">
		<div class="ofertas-responsive-one" id="exclusiva1" data-typeslide="dot"><div class="slide"></div></div>
		<div class="dots"></div>
	</div>
	<div id="cnt-exclusiva2">
		<div class="ofertas-responsive-two" id="exclusiva2" data-typeslide="dot"><div class="slide"></div></div>
		<div class="dots"></div>
	</div>
</section>

<script id="template-mejores-ofertas" type="text/x-handlebars-template">
	<article class="promocion-{{tipo}}">
		<a href="{{exclusiva.urlImagen}}">
			<div class="mask"></div>
			<img src="{{exclusiva.imagenDos}}" alt="{{exclusiva.titulo}}" />
		</a>
		<div class="detalle-promo">
			<div>
				<h5>{{exclusiva.etiqueta}}</h5>
				<a href="{{exclusiva.urlImagen}}"><h3>{{exclusiva.titulo}}</h3></a>
<!-- 				{{#tituloPromocionesExclusivas tipo exclusiva.titulo exclusiva.urlImagen}} -->
<!-- 				{{/tituloPromocionesExclusivas}} -->
			</div>
			<div class="clearfix"></div>
		</div>
	</article>
</script>

<c:if test="${not empty sessionCliente}">
	<section class="container">
		<c:if test="${not empty promociones.body.meta.urlImagen}">
			<div id="cupones">
				<div>
					<c:choose>
						<c:when test="${not empty promociones.body.meta.tituloPasoTres}">
							<div class="cupon-success">
								<h2>${promociones.body.meta.tituloPasoTres}</h2>
								<h3>${promociones.body.meta.tituloSeccionPasoTres}</h3>
								<p>${promociones.body.meta.descripcionPasoTres}</p>
							</div>
						</c:when>
						<c:otherwise>
							<form id="form-cupon">
								<h3>${promociones.body.meta.tituloPasoUno}</h3>
								<h2>${promociones.body.meta.tituloSeccionPasoUno}</h2>
								<p>${promociones.body.meta.descripcionPasoUno}</p>
								<input type="hidden" id="metas" value="${promociones.body.meta.codigoMeta}">
								<label class="checkbox-label">
									<input type="checkbox" id="cuponTyc">
									<span class="ch-input">
										<i class="hidden"></i>
										<span class="acepte-tyc hidden">Acepte los t&eacute;rminos y condiciones</span>
									</span>
									<span class="ch-label">Acepto los <a href="#" id="open-tyc-cupon">t&eacute;rminos y condiciones</a></span>
								</label>
								<a href="#" class="btn-azul" id="enviar-cupones">${promociones.body.meta.nombreBotonPasoUno}</a>
							</form>
							<div class="cupon-success hidden">
								<h2>${promociones.body.meta.tituloPasoTres}</h2>
								<h3>${promociones.body.meta.tituloSeccionPasoTres}</h3>
								<p>${promociones.body.meta.descripcionPasoTres}</p>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
				<div class="cnt-image">
					<img src="${prop['config.halcon.recursos.base.web']}${promociones.body.meta.urlImagen}" alt="${promociones.body.meta.tituloSeccionPasoUno}"/>
				</div>
			</div>
		</c:if>
	</section>
	<div id="tyc-cupones" class="override-modal">
		<div class="general-modal">
			<div class="close-modal"></div>
			${promociones.body.meta.contenidoTerminos}
		</div>
	</div>
</c:if>

<section id="categorias" class="container hidden" >
	<div class="title-zone">
		<h3>${promociones.body.categoria.descripcion}</h3>
		<h4>${promociones.body.categoria.titulo}</h4>
	</div>
	<div class="list-categorias"></div>
	<div class="mas-categorias"><a href="#" class="btn-azul">Todas las categor&iacute;as</a></div>
	<c:if test="${promociones.body.categoria.botonTodasPromociones ne null}">
		<div class="mas-promociones">
			<a class="btn-verde" href="${promociones.body.categoria.botonTodasPromociones}">Todas las promociones</a>
		</div>
	</c:if>
</section>

<script id="template-categorias" type="text/x-handlebars-template">
	<div class="cnt-article">
		<article>
			<a href="{{urlImagen}}">
				<img src="{{imagen}}" alt="{{titulo}}" />
				<span class="categoria">{{titulo}}</span>
			</a>
		</article>
	</div>
</script>

<%-- <c:if test="${not empty sessionCliente}"> --%>
	<div id="cuenta-sueldo" class="container">
		<div>
			<div>
				<img src="${promociones.body.cuentaSueldo.imagenStatic}" alt="Cuenta sueldo">
			</div>
			<div>
				<c:choose>
					<c:when test="${not empty sessionCliente}">
						<h4>${promociones.body.cuentaSueldo.subTitulo}</h4>
						<h3>${promociones.body.cuentaSueldo.nombre}</h3>
					</c:when>
					<c:otherwise>
						<h4>${promociones.body.cuentaSueldo.copy}</h4>
						<h3>${promociones.body.cuentaSueldo.tituloZona}</h3>
					</c:otherwise>
				</c:choose>
				<div class="list-promo" id="lista-promo-cuenta-sueldo"></div>
				<div class="more">
					<a href="${promociones.body.cuentaSueldo.urlVerTodas}">Ver todas<i class="icon-next"></i></a>
					<div class="paginator">
						<span id="prev" class="icon-previous"></span>
						<span class="pages"></span>
						<span id="next" class="icon-next"></span>
					</div>
				</div>
				<div class="beneficios">
					<h3>Conoce todos tus beneficios en el APP de Cuenta Sueldo</h3>
					<p>Encuentra tus descuentos ordenados por rubros &iexcl;y conoce los m&aacute;s cercanos a ti!</p>
					<ul>
						<li>
							<a href="https://play.google.com/store/apps/details?id=pe.com.interbank.mobilebanking" target="_blank">
								<img alt="PlayStore" src="${prop['config.url.recursos.base.web']}static/images/promociones/playstore-icon.jpg">
								<span>Desc&aacute;rgala gratis<br>en <strong>PlayStore</strong></span>
							</a>
						</li>
						<li>
							<a href="https://itunes.apple.com/app/id378649517" target="_blank">
								<img alt="AppStore" src="${prop['config.url.recursos.base.web']}static/images/promociones/applestore-icon.jpg">
								<span>Desc&aacute;rgala gratis<br>en <strong>AppStore</strong></span>
							</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	
	<div id="templateTarjetasCliente"></div>
	
<%-- </c:if> --%>

<script id="template-tarjetas-cliente" type="text/x-handlebars-template">
	<div id="tarjetas-cliente-{{codigoTarjeta}}" class="tarjetas-cliente container">
		<div>
			<h4>Siempre tienes mas con</h4>
			<h3>{{tarjeta.nombre}}</h3>
			<img src="{{tarjeta.imagen}}" alt="{{tarjeta.nombre}}">
		</div>
		<div>
			<div id="tarjeta-{{codigoTarjeta}}" class="list-promo list-promo-{{codigoTarjeta}}"><div class="slide"></div></div>
			<div class="more">
				<a href="{{tarjeta.linkVerTodas}}">Ver todas<i class="icon-next"></i></a>
				<div class="paginator">
					<span id="prev" class="icon-previous"></span>
					<span class="pages"></span>
					<span id="next" class="icon-next"></span>
				</div>
			</div>
		</div>
	</div>
</script>

<script id="template-tarjeta-rubros" type="text/x-handlebars-template">
	<div class="rubro">
		{{#if rubro.rubrosTypGrupo1}}
			<div class="rubro-paso1 hidden">
				<h3>{{rubro.titulo}}</h3>
				<p>{{rubro.descripcion}}</p>
				<button class="btn-azul" id="elegir-rubro">{{rubro.textoBoton}}</button>
			</div>
			<div class="rubro-paso2 hidden">
				<p>Elegir un rubro del siguiente grupo:</p>
				<ul>
					{{#each rubro.rubrosTypGrupo1 as |rubroGrupo|}}
						<li data-rubro1="{{rubroGrupo.codigoGrupo1}}">
							<span class="cnt-image"><img src="${prop['config.halcon.recursos.base.web']}{{rubroGrupo.urlImageGrupo1}}" alt="{{rubroGrupo.textGrupo1}}"/></span>
							<span>{{rubroGrupo.textGrupo1}}</span>
						</li>
					{{/each}}
				</ul>
				<button class="btn-azul disabled">Siguiente</button>
				<p class="num-pasos">Paso 1 de 2</p>
			</div>
			<div class="rubro-paso3 hidden">
				<p>Elegir un rubro del siguiente grupo:</p>
				<ul>
					{{#each rubro.rubrosTypGrupo2 as |rubroGrupo|}}
						<li data-rubro2="{{rubroGrupo.codigoGrupo2}}">
							<span class="cnt-image"><img src="${prop['config.halcon.recursos.base.web']}{{rubroGrupo.urlImageGrupo2}}" alt="{{rubroGrupo.textGrupo2}}"/></span>
							<span>{{rubroGrupo.textGrupo2}}</span>
						</li>
					{{/each}}
				</ul>
				<button class="btn-azul disabled">Listo</button>
				<p class="num-pasos">Paso 2 de 2</p>
			</div>
		{{/if}}
		{{#if rubro.exists}}
			<div class="existe-rubro">
				<h3>{{parseHtml rubro.tituloTyp}}</h3>
				<p>{{rubro.descripcionTyp}}</p>
				<ul class="rubro-seleccionado"></ul>
				<a href="#" class="nuevo-rubro">Elegir nuevos rubros<i class="icon-next"></i></a>
			<div>
		{{/if}}
	</div>
</script>

<script id="template-descuento-premia" type="text/x-handlebars-template">
	{{#if premia}}
	<div class="premia">
		<div class="premia-paso1">
			<h3>{{premia.titulo}}</h3>
			<p>{{premia.descripcion}}</p>
			<button class="btn-azul" id="activar-premia" data-porcentaje="{{premia.porcentajePremia}}">{{premia.textoBoton}}</button>
		</div>
		<div class="premia-paso2 hidden">
 			<h3>{{premia.tituloTyp}}</h3>
 			<p>{{premia.descripcionTyp}}</p>
		</div>
	</div>
	{{/if}}
</script>

<script id="template-retos" type="text/x-handlebars-template">
	<form id="{{codigo}}" class="{{#if exists}}{{else}}no-participando{{/if}}">
		<label for="participar" class="checkbox-label">
			<input type="checkbox" id="participar-{{codigo}}">
			<span class="ch-input" data-check="participar1">
				<i class="hidden"></i>
			</span>
			<span class="ch-label" id="titulo-retos-{{codigo}}"></span>
		</label>
		<p>{{descripcion}}</p>
		<span class="select-reto"></span>
	</form>
</script>

<c:choose>
	<c:when test="${not empty sessionCliente}">
		<div id="tyc-retos" class="override-modal">
			<div class="general-modal">
				<div class="close-modal"></div>
				${promociones.body.reto.contenidoTerminos}
			</div>
		</div>
		<section id="retos" class="container hidden">
			<div>
				<h3>${promociones.body.reto.tituloPasoUno}</h3>
				<p>${promociones.body.reto.tituloSeccionPasoUno}</p>
			</div>
			<div>
				<div class="lista-retos"></div>
				<div id="seccionEnviarRetos">
					<a id="enviar-reto" class="btn-azul" href="">${promociones.body.reto.nombreBotonDos}</a>
					<label class="checkbox-label">
						<input type="checkbox" id="retoTyc">
						<span class="ch-input">
							<i class="hidden"></i>
							<span class="acepte-tyc hidden">Acepte los t&eacute;rminos y condiciones</span>
						</span>
						<span class="ch-label">Acepto los <a href="#" id="open-tyc-retos">t&eacute;rminos y condiciones</a></span>
					</label>
				</div>
			</div>
		</section>
	</c:when>
	<c:otherwise>
		<section id="select-tarjeta" class="container">
			<div class="here">
				<h4 >Imag&iacute;nate con una</h4>
				<div class="dropdown">
					<a href="#" class="drop-toggle" data-tipotarjeta=""><span></span><span class="icon-down"></span></a>
					<ul class="drop-list" id="toggle-select-tarjeta">
					</ul>
				</div>
				<div class="list-promo" id="lista-promo-tarjeta" data-typeslide="number">
					<div class="slide"></div>
				</div>
				<div class="more">
					<a  id="verTodasPromociones">Ver todas<i class="icon-next"></i></a>
					<div class="paginator">
						<span id="prev" class="icon-previous"></span>
						<span class="pages"></span>
						<span id="next" class="icon-next"></span>
					</div>
				</div>
			</div>
			<div><img id="imagen-seleccion-tarjeta" src="" alt="Selecci&oacute;n de tarjeta"></div>
		</section>
		
		<div id="cuenta-sueldo" class="cuenta-sueldo-nolog container hidden" >
			<h4>${promociones.body.cuentaSueldo.copy}</h4>
			<h3>${promociones.body.cuentaSueldo.tituloZona}</h3>
			<div>
				<div>
					<img src="${promociones.body.cuentaSueldo.imagenStatic}" alt="Cuenta sueldo">
				</div>
				<div>
					<div class="list-promo" id="lista-promo-cuenta-sueldo" data-typeslide="number"></div>
					<div class="more">
						<a href="${promociones.body.cuentaSueldo.urlVerTodas}">Ver todas<i class="icon-next"></i></a>
						<div class="paginator">
							<span id="prev" class="icon-previous"></span>
							<span class="pages">1 de 2</span>
							<span id="next" class="icon-next"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div id="encuentra-tarjeta" class="container">
			<div>
				<div class="cnt-image"><img alt="Encuentra tarjeta" src="${prop['config.halcon.recursos.base.web']}${promociones.body.entryPoint.imagen}"></div>
				<div>
					<h3>${promociones.body.entryPoint.titulo}</h3>
					<p>${promociones.body.entryPoint.descripcion}</p>
				</div>
				<a href="${prop['config.halcon.recursos.base.web']}${promociones.body.entryPoint.botonLink}" class="btn-verde">${promociones.body.entryPoint.botonTitulo}</a>
			</div>
		</div>
	</c:otherwise>
</c:choose>

<script id="template-promo-tarjeta" type="text/x-handlebars-template">
	<div>
		<article>
			<a href="{{urlImagen}}"><img src="{{imagen}}" alt="{{titulo}}"/></a>
			<div class="detail-promo">
				<h4>{{categoria}}</h4>
				<a href="{{urlImagen}}"><h3>{{titulo}}</h3></a>
			</div>
		</article>
	</div>
</script>

<script id="template-promo-tarjeta-cliente" type="text/x-handlebars-template">
	<div>
		<article>
			<a href="{{url}}"><img src="{{imagen}}" alt="{{titulo}}"/></a>
			<div class="detail-promo">
				<h4>{{categoria}}</h4>
				<a href="{{url}}"><h3>{{recortarTitulo titulo 46}}</h3></a>
			</div>
		</article>
	</div>
</script>

<script type="text/javascript">
	$(document).ready(function(){
		activarMenu('promociones');
		var promocionesHome = new PromocionesHome();
		promocionesHome.cliente = ${promocionesHalconCliente};
		promocionesHome.urlBase = "${prop['config.halcon.recursos.base.web']}";
		promocionesHome.init();
	});
</script>