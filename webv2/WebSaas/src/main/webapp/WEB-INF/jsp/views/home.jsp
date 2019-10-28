<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.plazapoints.saas.web.util.Constantes"%>
		<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>
		<div class="lista-carrito-compras">
					<c:if test="${sessionCliente ne null}">
					<c:if test="${sessionScope.sesionCarritoCompras ne null &&   fn:length(sessionScope.sesionCarritoCompras.detalles) gt 0}">
					<section id="carrito" class="flotante-principal carrito-general" style="display:block !important">

						   <a href="${pageContext.request.contextPath}/canjeProducto/pendientes" class="espera"><span>${fn:length(sessionScope.sesionCarritoCompras.detalles)}</span>
						    producto<c:if test="${fn:length(sessionScope.sesionCarritoCompras.detalles) > 1}">s</c:if> guardado<c:if test="${fn:length(sessionScope.sesionCarritoCompras.detalles) > 1}">s</c:if> en 'Mis canjes'</a>

						<div class="resaltar" style="display:none">
							<ul>
								<li><span class="oportunidades-exclusivas"></span></li>
								<li><a href="${pageContext.request.contextPath}/compra/destacados/oportunidad">Oportunidades exclusivas para ti</a></li>
								<li><a href="${pageContext.request.contextPath}/">Ofertas exclusivas para ti</a></li>
								
							</ul>
						</div>
						<div class="resumen" style="display:none">
							<div>
								<p class="arrow-explora"></p>
								<div class="detalle">
									<div class="explora">
									<a href="#lista-compras">
										<p>Explora todos los beneficios</p>
										<p>(<span class="total-beneficios"></span>) en 'Compras'</p></a>
									</div>
								</div>
							</div>
						</div>
					</section>
					</c:if>
					</c:if>
		</div>
<div id="contenedor" class="flotante-explora slider-foto contenedor-banner"><!--  style="display: none;" -->
	<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>
	<div class="banner-home" id="banner-home">
		<a class="slide-home" href="${pageContext.request.contextPath}/que-es-interbank-benefit" id="banner-que-es">
			<div class="banner-destacados banner-destacados-active">
				<h2>Bienvenido a</h2>
				<img class="banner-logo" src="${prop['config.url.recursos.base.web']}static/images/banner-home/LogoIBK-slider.png" alt="Logo Interbak Benefit Slider" />
				<p>El nuevo programa de recompensas de tu tarjeta de crédito que une tus Puntos Interbank y Membership Rewards en Millas Benefit. 
				<strong>&iexcl;No esperes m&aacute;s y empieza a disfrutar!</strong></p>
				<span class="boton-banner">Conoce m&aacute;s</span>
			</div>
		</a>
		<a class="slide-home" href="${pageContext.request.contextPath}/viajes" id="banner-viajes">
			<div class="banner-destacados">
				<h2>&iexcl;Viaja cuando quieras!</h2>
				<p>En cualquier aerol&iacute;nea &iexcl;T&uacute; eliges a d&oacute;nde ir y cu&aacute;ntas Millas Benefit usar!</p>
				<span class="boton-banner">Buscar vuelos</span>
			</div>
		</a>
		<a class="slide-home" href="${pageContext.request.contextPath}/compras" id="banner-compras">
			<div class="banner-destacados">
				<h2>&iexcl;Productos a precios especiales!</h2>
				<p>Canjea el producto que m&aacute;s te guste combinando tus Millas Benefit y tu tarjeta de cr&eacute;dito.</p>
				<span class="boton-banner">Canjear ahora</span>
			</div>
		</a>
		<div class="banner-home-manejadores" id="banner-home-manejadores"></div>
	</div>
	
	<script id="templateDescuento" type="text/x-handlebars-template">

	<div class="slider-cnt">	

	<a href="${pageContext.request.contextPath}/descuento/detalle/{{keyItem}}" class="img-cnt">
		<img src="${prop['url.imagen.descuento']}{{imagenOportunidad}}" class="central" alt="{{titulo}}">
	</a>

	<section class="detalle-oportunidad">
       <div class="do-titulo">
           <a href="${pageContext.request.contextPath}/descuento/detalle/{{keyItem}}"><h2>{{titulo}}</h2></a>
       </div>
       <div class="do-left">
          
           <div class="do-millas"><span class="nro">{{formatPorcentajeDescuento}}%</span> Millas Benefit</div>
            <div class="do-precio">
               ó {{simboloMoneda}} {{formatearDecimal formatPrecioCatalogo}}
           </div>
       </div>
       
       <div class="do-right">
           <!--<div class="do-comprar">
               <a href="" class="do-boton">Comprar</a>
           </div>-->
            <div class="ver-detalle">
                <a href="${pageContext.request.contextPath}/descuento/detalle/{{keyItem}}">Ver detalles</a>
            </div>
       </div>
       <c:if test="${empty sessionCliente}">
       <div class="cliente">
               ¿No eres cliente? <a href="http://interbank.solicitudes.pe/tarjetas-credito?utm_source=interbankbenefit" class="">Hazte cliente ahora</a>
       </div>
	   </c:if>
   </section>
    </div>
	</script>
	<script id="templateProducto" type="text/x-handlebars-template">
	<div class="slider-cnt">

	<a href="${pageContext.request.contextPath}/producto/{{keyItem}}" class="img-cnt">
		<img src="${prop['url.imagen.producto']}{{imagenOportunidad}}" class="central" alt="{{titulo}}">
	</a>

	<section class="detalle-oportunidad">
       <div class="do-titulo">
           <h2>{{titulo}}</h2>
       </div>
       <div class="do-left">
          
           <div class="do-millas"><span class="nro">{{formatPrecioPuntos}}</span> Millas Benefit</div>
            <div class="do-precio">
               ó {{simboloMoneda}} {{formatearDecimal formatPrecioCatalogo}}
           </div>
       </div>
       
       <div class="do-right">
           <div class="do-comprar">
               <a href="${pageContext.request.contextPath}/canjeProducto/paso1/{{keyItem}}" class="do-boton">{{button}}</a>
           </div>
            <div class="ver-detalle">				
                <a href="${pageContext.request.contextPath}/producto/{{keyItem}}">Ver detalles</a>
            </div>
       </div>
       <c:if test="${empty sessionCliente}">
       <div class="cliente">
               ¿No eres cliente? <a href="http://interbank.solicitudes.pe/tarjetas-credito?utm_source=interbankbenefit" class="">Hazte cliente ahora</a>
       </div>
       </c:if>
   </section>
   </div>
	</script>
	<script id="templatePaquete" type="text/x-handlebars-template">

	<div class="slider-cnt full"> 

	<a href="${pageContext.request.contextPath}/viajes/paquete/{{keyItem}}" class="img-cnt">
		<img src="{{imagenOportunidad}}" class="central" alt="{{titulo}}" />
	</a>
	<section class="detalle-oportunidad">
       <div class="do-titulo">
           <h2>{{recortarTitulo titulo 6}}</h2>
       </div>
       <div class="do-left">
          
           <div class="do-millas"><span class="nro">{{formatoMiles formatPrecioPuntos}}</span> Millas Benefit</div>
            <div class="do-precio" >
               ó {{simboloMoneda}} {{formatearDecimal formatPrecioCatalogo}}
           </div>
       </div>
       
       <div class="do-right" style="display:none">
           <div class="do-comprar">
				<a href="javascript:;" data-paquete="{{keyItem}}" class="boton comprar-paquete">{{button}}</a>
           </div>
            <div class="ver-detalle">
                <a href="${pageContext.request.contextPath}/viajes/paquete/{{keyItem}}">Ver detalles</a>
            </div>
       </div>
       <c:if test="${empty sessionCliente}">
       <div class="cliente">
               ¿No eres cliente? <a href="http://interbank.solicitudes.pe/tarjetas-credito?utm_source=interbankbenefit" class="">Hazte cliente ahora</a>
       </div>
       </c:if>
   </section>
	</div>
	</script>
</div>
<div id="contenido_dinamico"></div>
<div id="publica">
		<div id="destacados">
		<div class="limiteOfertas homeNuevos">
			<div class="titulo-general centrado">
				<h1 class="titulo">Destacados</h1>				
			</div>
				<script id="articleProducto" type="text/x-handlebars-template">
					<article class="grid-item producto elementoOculto">
							<div class="imagen">
								{{mostrarProductoAgotado stockDisponible verCantidadStock verProductoAgotado verPrecioProducto}}
								<a href="${pageContext.request.contextPath}/producto/{{keyItem}}">
									<div class="cont-flag {{mostrarFlagDescuentoYPrecioProducto stockDisponible verProductoAgotado verPrecioProducto}}">
										{{flagDescuento esOferta tipoOferta colorImagenFlag porcentajeDescuento '${prop['url.imagen.producto']}' }}
									</div>
								</a>
								<a href="${pageContext.request.contextPath}/producto/{{keyItem}}">															
								<img class="imagen" src="${prop['url.imagen.producto']}{{imagenDestacado}}" height="270" width="235" alt="{{titulo}}">
<!-- 		Inicio flag -->
			
<!-- 		Fin flag -->
								</a>
							</div>
							<div class="descripcion">
								<div class="nombre"><a href="${pageContext.request.contextPath}/producto/{{keyItem}}">{{recortarTitulo titulo 17}}</a></div>
								<div class="precio {{opacarPorcentajeDecuento stockDisponible verProductoAgotado verPrecioProducto}} {{mostrarFlagDescuentoYPrecioProducto stockDisponible verProductoAgotado verPrecioProducto}}">
							<!-- 		Inicio flag -->
								{{flagDescuentoDetalle esOferta verPrecioRegular tipoOferta precioPuntosRegular colorImagenFlag porcentajeDescuento}}
							<!-- 		Fin flag -->
									<div class="puntos">{{formatPrecioPuntos}} <span class="mb">Millas Benefit</span></div>
									<div class="soles">ó {{simboloMoneda}} {{formatearDecimal formatPrecioCatalogo}}</div>
								</div>
								<div class="detalle-compra">									
									<a class="ver-detalle" href="${pageContext.request.contextPath}/producto/{{keyItem}}">Ver detalles</a>
									<a href="${pageContext.request.contextPath}/canjeProducto/paso1/{{keyItem}}" class="boton {{mostrarBotonComprar stockDisponible verProductoAgotado}}">{{button}}</a>
								</div>
								
							</div>
					</article>					
				</script>
				<script id="articleDescuento" type="text/x-handlebars-template">
				<article class="grid-item simple descuento elementoOculto">
							<div class="imagen">
								<a href="${pageContext.request.contextPath}/descuento/detalle/{{keyItem}}"><img class="imagen" src="${prop['url.imagen.descuento']}{{imagenDestacado}}" height="270" width="235" alt="{{titulo}}"></a>
							</div>
							<div class="descripcion">
								<div class="nombre"><a href="${pageContext.request.contextPath}/descuento/detalle/{{keyItem}}">{{recortarTitulo titulo 28}}</a></div>
								<div class="porcentaje">
									<div><span>{{formatPorcentajeDescuento}}%</span> Dscto.</div>
								</div>
								<p class="vigencia">{{labelVigenciaDescuento}}</p>
								<div class="detalle-compra">
									<a href="${pageContext.request.contextPath}/descuento/detalle/{{keyItem}}" class="ver-detalle">Ver detalles</a>
								</div>
								
							</div>
					</article>
				</script>
				<script id="articlePaquete" type="text/x-handlebars-template">
					<article class="grid-item paquete elementoOculto">
						<div class="color-prox-vj in-home"></div>
						<div class="text-prox-vj in-home">
							<p>Próximamente</p>
						</div>
							<div class="imagen">
								<a href="${pageContext.request.contextPath}/viajes/paquete/{{keyItem}}">
									<img class="imagen" src="{{imagenDestacado}}" height="270" width="480" alt="{{titulo}}">
								</a>
							</div>
							<div class="descripcion">
								<div class="nombre"><a href="${pageContext.request.contextPath}/viajes/paquete/{{keyItem}}">{{recortarTitulo titulo}}</a></div>
								<div class="precio">
									<div class="puntos">{{formatoMiles formatPrecioPuntos}} <span class="mb">Millas Benefit por persona</span></div>
								</div>
								<div class="do-precio" >
               						ó {{simboloMonedaDolar}} {{formatearDecimal formatPrecioCatalogoDolares}} | {{simboloMoneda}} {{formatearDecimal formatPrecioCatalogo}}
           						</div>
								<div class="detalle-compra" style="">
									<a href="${pageContext.request.contextPath}/viajes/paquete/{{keyItem}}" class="ver-detalle">Ver detalles</a>
									<a href="javascript:;" data-paquete="{{keyItem}}" class="boton comprar-paquete">{{button}}</a>
								</div>
								
							</div>
					</article>

				 </script> 
				<section id="lista-home">
					<div id="main" class="grid"></div>
				</section>
				<section align="center" class="sectionVerMas">
					<div class="boton-infinite">
					<a href="javascript:;" id="verMas" class="btn-infinite">Ver m&aacute;s <span class="ico-down"></span></a>
					</div>
				</section>
		</div>
		</div>
		</div>
	<jsp:include page="conoce_bim.jsp" />

<link  rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/cms/css/main.css?${prop['config.web.release']}" />
<script src="${prop['config.url.recursos.base.web']}static/cms/js/main.js?${prop['config.web.release']}"></script>
<script type="text/javascript">

var urlListarDestacados = CONTEXT_PATH+"${prop['uri.web.destacados.listar']}";
var urlDestacado = "${prop['url.imagen.producto']}";
var puntosCliente = '${cliente.totalPuntos}';
var boton = "comprar";
	
$(document).ready(function(){
	var destacado = new Destacados();
	destacado.LIMITE_X_PAGINA = parseInt("${prop['config.total.registros.pagina']}",10);
	destacado.init();
	destacado.loadBanner();
	if("${triggerLoginCliente}"=="true"){
		loginHandler();
	}
});
</script>

<script id="templateBanner" type="text/x-handlebars-template">
	<a class="slide-home" href="{{urlEvento}}" target="{{targetEvento}}">
		<div class="banner-destacados banner-destacados-oferta">
			<div class="contenido">
				<h2>{{customJson.titulo}}</h2>
				<p>{{customJson.subTitulo}}</p>
				<span class="boton-banner" >{{mensajeEvento}}</span>
			</div>
			<img class="slide-promo-img" src="{{imagenEvento}}" alt="{{customJson.titulo}}" />
		</div>
	</a>
</script>
	