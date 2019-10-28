<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" href="${prop['config.url.recursos.base.web']}static/benefit-navidad/css/style.css?${prop['config.web.release']}" />

<%-- <c:choose> --%>
<%-- 	<c:when test="${(prop['config.ambiente.deploy'] eq 'produccion')}"> --%>
		<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/benefit-navidad/js/main.js?${prop['config.web.release']}"></script>
<%-- 	</c:when> --%>
<%-- 	<c:otherwise> --%>
<%-- 		<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/benefit-navidad/js/mainUat.js?${prop['config.web.release']}"></script> --%>
<%-- 	</c:otherwise> --%>
<%-- </c:choose> --%>

<div id="benefit-navidad">
	<div id="banner">
		<ul id="banner-control">
			<li class="icon-ico-bullet-left"></li>
			<li class="icon-ico-bullet-right"></li>
		</ul>
	</div>
	
<!-- 	<div id="evento-banner"> -->
<!-- 		<ul id="evento-banner-control"> -->
<!-- 			<li class="icon-ico-bullet-left"></li> -->
<!-- 			<li class="icon-ico-bullet-right"></li> -->
<!-- 		</ul> -->
<!-- 	</div> -->
		
	<section id="categorias" class="all-container">
		
	</section>
	
	<div id="mejores-ofertas" class="all-container">
		<h2 class="titulo">Nuestras mejores ofertas</h2>
		
		<div id="lista-top-navidad"></div> 
		<div id="lista-top-navidad-responsive" class="oferta-responsive"></div>
		
		<span class="arrow arrow-left"><i class="icon-ico-bullet-left"></i></span>
		<span class="arrow arrow-right"><i class="icon-ico-bullet-right"></i></span>
		<div class="dots-responsive"></div>
	</div>
	
	<div id="img-descuentos" class="all-container">
		
	</div>
	
	<div id="mejores-ofertas-dos" class="all-container">
	
		<div id="lista-ofertas-navidad"></div>
		<div id="lista-ofertas-navidad-responsive" class="oferta-responsive"></div>
		
		<span class="arrow arrow-left"><i class="icon-ico-bullet-left"></i></span>
		<span class="arrow arrow-right"><i class="icon-ico-bullet-right"></i></span>
		<div class="dots-responsive"></div>
	</div>
	
	<div id="img-descuentos-dos" class="all-container">
		
	</div>
	
	<script id="template-banner" type="text/x-handlebars-template">
		<div class="img {{classEvento}}">
			<a href="{{urlEvento}}">				
				<img class="{{classContenedorEvento}}" src="{{imagenEventoGeneral}}"/>
			</a>
		</div>
	</script>

	<script id="template-navidad-productos" type="text/x-handlebars-template">

		<article class="oferta">
			<a href="${pageContext.request.contextPath}/producto/{{keyItem}}?pcid=benefit:home:navidad:image:carrusel-footer" class="cont-flag {{mostrarFlagDescuentoYPrecioProducto stockDisponible verProductoAgotado verPrecioProducto}}" >
				{{flagDescuento esOferta tipoOferta colorImagen porcentajeDescuento '${prop['url.imagen.producto']}' }}
			</a>
			<a class="cnt-image" href="${pageContext.request.contextPath}/producto/{{keyItem}}?pcid=benefit:home:navidad:image:carrusel-footer">
				{{mostrarProductoAgotado stockDisponible verCantidadStock verProductoAgotado verPrecioProducto}}
				<img src="${prop['url.imagen.producto']}{{imagenDestacado}}" alt="{{nombreCategoria}}" />
			</a>
			<div>
				<p class="oferta-categoria">{{recortarTitulo nombreCategoria 15}}</p>
				<a href="${pageContext.request.contextPath}/producto/{{keyItem}}?pcid=benefit:home:navidad:title:carrusel-footer" class="oferta-detalle"><h3>{{recortarTitulo titulo 36}}</h3></a>

				<span class="oferta-actual">{{formatPrecioPuntos}}</span>
				<span class="oferta-millas">Millas Benefit</span>
				<span class="oferta-soles">o ${prop['config.moneda.simbolo.default']} {{formatPrecioCatalogo}}</span>
				<a href="${pageContext.request.contextPath}/producto/{{keyItem}}?pcid=benefit:home:navidad:link:carrusel-footer" class="oferta-detalle">Ver detalle  <span class="icon-ico-bullet-right"></span></a>
				{{mostrarBotonComprarLanding keyItem stockDisponible verProductoAgotado}}

			</div>
		</article>

	</script>
	
	
	<script id="template-navidad-productos-top" type="text/x-handlebars-template">

		<article class="oferta">
			<a href="${pageContext.request.contextPath}/producto/{{keyItem}}?pcid=benefit:home:navidad:image:carrusel-body" class="cont-flag {{mostrarFlagDescuentoYPrecioProducto stockDisponible verProductoAgotado verPrecioProducto}}" >
				{{flagDescuento esOferta tipoOferta colorImagenFlag porcentajeDescuento '${prop['url.imagen.producto']}' }}
			</a>
			<a class="cnt-image" href="${pageContext.request.contextPath}/producto/{{keyItem}}?pcid=benefit:home:navidad:image:carrusel-body">
				{{mostrarProductoAgotado stockDisponible verCantidadStock verProductoAgotado verPrecioProducto}}
				<img src="${prop['url.imagen.producto']}{{imagenDestacado}}" alt="{{nombreCategoria}}" />
			</a>
			<div>
				<p class="oferta-categoria">{{recortarTitulo nombreCategoria 15}}</p>
				<a href="${pageContext.request.contextPath}/producto/{{keyItem}}?pcid=benefit:home:navidad:title:carrusel-body" class="oferta-detalle"><h3>{{recortarTitulo titulo 36}}</h3></a>
				
				<span class="oferta-actual">{{formatPrecioPuntos}}</span>
				<span class="oferta-millas">Millas Benefit</span>
				<span class="oferta-soles">o ${prop['config.moneda.simbolo.default']} {{formatPrecioCatalogo}}</span>
				<a href="${pageContext.request.contextPath}/producto/{{keyItem}}?pcid=benefit:home:navidad:link:carrusel-body" class="oferta-detalle">Ver detalle  <span class="icon-ico-bullet-right"></span></a>
				{{mostrarBotonComprarLanding keyItem stockDisponible verProductoAgotado}}
			</div>
		</article>

	</script>
	
	<script id="template-top-categoria" type="text/x-handlebars-template">

		<article>
			<a href="${pageContext.request.contextPath}{{href-titulo}}">
				<img src="${prop['config.url.recursos.base.web']}static/benefit-navidad/images/categoria-{{imagen}}" alt="{{nombreImagen}}" />
			</a>
			<div>
				<span class="prev-titulo">{{prevTitulo}}</span>
				<a href="${pageContext.request.contextPath}{{href-titulo}}"><h3>{{categoria}}</h3></a>
				<!-- <span class="porc-categoria">{{descuento}}</span> -->
				<a class="ver-promo" href="${pageContext.request.contextPath}{{href-enlace}}">
					<span>Ver promociones </span>
					<span class="icon-ico-bullet-right"></span>
				</a>
			</div>
		</article>
		
	</script>
	
	<script id="template-top-descuentos" type="text/x-handlebars-template">

		<a href="${pageContext.request.contextPath}{{descuento1.href}}" class="img-principal">
			<img src="${prop['config.url.recursos.base.web']}static/benefit-navidad/images/{{descuento1.imagen}}" alt="{{descuento1.nombreDescuento}}" />
			<div>
				<span class="prev-titulo">{{descuento1.prevTitulo}}</span>
				<!-- <span class="porc">{{descuento1.porcentaje}}</span> -->
				<h3>{{descuento1.tituloPromocion}} <span class="icon-ico-bullet-right"></span></h3>
				<span class="categoria"><!-- Ver promociones <span class="icon-ico-bullet-right"></span> --></span>
			</div>
		</a>
		<div class="img-doble">
			<a href="${pageContext.request.contextPath}{{descuento2.href}}">
				<img src="${prop['config.url.recursos.base.web']}static/benefit-navidad/images/{{descuento2.imagen}}" alt="{{descuento2.nombreDescuento}}" />
				<div>
					<span>Los mejores</span>
					<span class="categoria">{{descuento2.categoria}} <span class="genero">{{descuento2.genero}}</span><span class="icon-ico-bullet-right"></span></span>
				</div>
			</a>
			<a href="${pageContext.request.contextPath}{{descuento3.href}}">
				<img src="${prop['config.url.recursos.base.web']}static/benefit-navidad/images/{{descuento3.imagen}}" alt="{{descuento3.nombreDescuento}}" />
				<div>
					<span>Los mejores</span>
					<span class="categoria">{{descuento3.categoria}} <span class="genero">{{descuento3.genero}}</span><span class="icon-ico-bullet-right"></span></span>
				</div>
			</a>
		</div>

	</script>
	
	<script id="template-bot-descuentos" type="text/x-handlebars-template">

		<div class="img-doble">
			<a href="${pageContext.request.contextPath}{{descuento1.href}}">
				<img src="${prop['config.url.recursos.base.web']}static/benefit-navidad/images/{{descuento1.imagen}}" alt="{{descuento1.nombreDescuento}}" />
				<div>
					<span class="prev-titulo"></span>
					<!-- <span class="porc">{{descuento1.porcentaje}}</span> -->
					<h3>{{descuento1.tituloPromo}}</h3>
					<span class="categoria">{{descuento1.categoria}} <span class="icon-ico-bullet-right"></span></span>
				</div>
			</a>
			<a href="${pageContext.request.contextPath}{{descuento3.href}}">
				<img src="${prop['config.url.recursos.base.web']}static/benefit-navidad/images/{{descuento3.imagen}}" alt="{{descuento3.nombreDescuento}}" />
				<div>
					<span class="prev-titulo"></span>
					<!-- <span class="porc">{{descuento3.porcentaje}}</span> -->
					<h3>{{descuento3.tituloPromo}}</h3>
					<span class="categoria">{{descuento3.categoria}} <span class="icon-ico-bullet-right"></span></span>
				</div>
			</a>
		</div>
		<div class="img-doble">
			<a href="${pageContext.request.contextPath}{{descuento2.href}}">
				<img src="${prop['config.url.recursos.base.web']}static/benefit-navidad/images/{{descuento2.imagen}}" alt="{{descuento2.nombreDescuento}}" />
			</a>
			<a href="${pageContext.request.contextPath}{{descuento4.href}}">
				<img src="${prop['config.url.recursos.base.web']}static/benefit-navidad/images/{{descuento4.imagen}}" alt="{{descuento4.nombreDescuento}}" />
			</a>
		</div>

	</script>
	
	<script type="text/javascript">
		$(document).on('ready', function(){
			var benefitNavidad = new BenefitNavidad();
			benefitNavidad.init();
		});
	</script>
	
</div>

<script type="text/javascript">

$(document).ready(function(){
	var accionPagina = "${accionPaginaInicial}";
	if(accionPagina == '/registrate'){
		registerHandler();
	}else if(accionPagina == '/ingresar'){
		loginHandler();
	}
	if("${triggerLoginCliente}"=="true"){
		loginHandler();
	}
});
</script>