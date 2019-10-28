<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>
<c:set var="isSession" value="false"></c:set>
<c:if test="${sessionScope.sessionCliente ne null }">
	<c:set var="isSession" value="true"></c:set>
</c:if>
<div id="contenedor" class="portada-descuento flotante-explora slider-foto">
	<!-- menu slider -->
	<section id="slidersHead">
		<nav class="slider-menu"></nav>
	</section>
	<!-- /menu slider -->

	<!-- miga -->
	<section class="miga miga-inferior">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los beneficios</a> <span>&gt;</span>
			<c:if test="${categoria ne null}">
				<a href="${pageContext.request.contextPath}/descuento">Descuentos</a>
				<span>&gt;</span>
				<p class="activo">${categoria.nombreCategoria}</p>
			</c:if>
			<c:if test="${idCategoria eq 0}">
				<p class="activo">Descuentos</p>
			</c:if>
		</div>
	</section>
	<div class="cont-top-avisos">
	</div>
	<!-- <p class="adicional">Has ahorrado <span>S/. 500</span> usando <span>20</span> descuentos</p> -->
	<script id="templateSlider" type="text/x-handlebars-template">
	<div class="slider-cnt slider-cnt-descuento" id="slider-descuento" >
		<div class="img-cnt">
			<a href="${pageContext.request.contextPath}/descuento/detalle/{{keyItem}}" class="detalles">
				<img src="${prop['url.imagen.descuento']}{{imagen_oportunidad}}" class="central" alt="{{nombre}}" automation="{{correlativoAutomation 'imagenSliderDescuento' 'imagenSlider'}}">
			</a>
		</div>
		<section class="detalle-oportunidad" automation="{{correlativoAutomation 'sliderDestacado' 'slider'}}">
			<div class="do-titulo" style="margin-bottom:0 !important;">
           		<h2><a href="${pageContext.request.contextPath}/descuento/detalle/{{keyItem}}" automation="{{correlativoAutomation 'tituloSliderDescuento' 'tituloSlider'}}">{{recortarTitulo nombre 28}}</a></h2>
				<div class="do-millas">
					<span class="nro" automation="{{correlativoAutomation 'porcentajeSliderDescuento' 'porcentajeSlider'}}">{{porcentaje}}% </span>dscto.
					<p class="vigencia" automation="{{correlativoAutomation 'vigenciaSliderDescuento' 'vigenciaSlider'}}">{{vigencia_descuento}}</p>
				</div>
       		</div>
			<c:if test="${!empty sessionCliente}">
				<div class="do-detalles" style="justify-content: flex-end;">
					<a href="${pageContext.request.contextPath}/descuento/detalle/{{keyItem}}" class="enlace-detalle" automation="{{correlativoAutomation 'detalleSliderDescuento' 'detalleSlider'}}">Ver detalles</a>
				</div>
			</c:if>
			<c:if test="${empty sessionCliente}">
				<div class="do-detalles">
       				<div class="cliente">
               			¿No eres cliente? <a href="https://interbank.solicitudes.pe/tarjeta-credito-benefit" target="_blank" class="" automation="{{correlativoAutomation 'enlaceSliderDescuento' 'enlaceSlider'}}">Hazte cliente ahora</a>
       				</div>
					<a href="${pageContext.request.contextPath}/descuento/detalle/{{keyItem}}" class="enlace-detalle" automation="{{correlativoAutomation 'detalleSliderDescuento' 'detalleSlider'}}">Ver detalles</a>
				</div>
       		</c:if>
		</section>
	</div>
    </script>

</div>

<div class="buscador-ancho" id="scrollToHere">
	<div class="titulo-general">
		<h1>M&aacute;s descuentos imperdibles (<span class="oportunidades-descuentos" automation="cantidadDescuentos"></span>)</h1>
		<span class="separar centrar"></span>
	</div>
</div>

<div class="limite oferta" >
	<div class="compras-content descuento-content cf" >
	<form id="formFiltros" method="get">
		<select id="cbo-filtro-ordenar" name="ordenar" class="filtro-ordenar comprar" automation="cboOrdenarDescuentos">
			<option value="all">Ordenar por</option>
			<option value="PRECIO_CATALOGO_ASC">De menor a mayor descuento</option>
			<option value="PRECIO_CATALOGO_DESC">De mayor a menor descuento</option>
			<option value="NOMBRE_CATALOGO_ASC">Nombre (A a Z)</option>
			<option value="NOMBRE_CATALOGO_DESC">Nombre (Z a A)</option>
		</select>
	</form>
	</div>

	<div align="center">
		<script id="templateBody" type="text/x-handlebars-template">
			<article id="previa-descuento" class="previa-descuento" automation="{{correlativoAutomation 'descuento' 'listado'}}">
				<div class="img-descuento">
					<a href="${pageContext.request.contextPath}/descuento/detalle/{{keyItem}}" >
						<img src="${prop['url.imagen.descuento']}{{imagen_destacado}}" alt="{{nombre}}" automation="{{correlativoAutomation 'imagenDescuento' 'imagenDescuento'}}"/>
					</a>
				</div>
				<div class="descripcion-descuento">
					<h3><a href="${pageContext.request.contextPath}/descuento/detalle/{{keyItem}}" automation="{{correlativoAutomation 'tituloDescuento' 'titulo'}}">{{recortarTitulo nombre 31}}</a></h3>
					<p><span automation="{{correlativoAutomation 'porcentajeDescuento' 'porcentaje'}}">{{porcentaje}}% </span>Dscto.</p>
					<p class="vigencia" automation="{{correlativoAutomation 'vigenciaDescuento' 'vigencia'}}">{{vigencia_descuento}}</p>
					<div class="detalle-compra">
						<a href="${pageContext.request.contextPath}/descuento/detalle/{{keyItem}}" class="detalle" style="margin: 0;" automation="{{correlativoAutomation 'detalleDescuento' 'detalle'}}">Ver detalle <span class="arrow-detalle-dscto"></span></a>
					</div>
				</div>
			</article>
		</script>
	</div>
	<section id="lista-descuentos">
		<div id="main"></div>
	</section>
	<section align="center" class="sectionVerMas">
		<div class="boton-infinite">
			<a href="javascript:;" id="verMas" class="btn-infinite">Ver m&aacute;s<span class="ico-down"></span></a>
		</div>
	</section>
</div>
<!-- /ofertas -->
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
<script type="text/javascript">
	$(document).ready(function() {
		var idCategoriaDescuento = "${idCategoriaDescuento}";
		var nombreCategoria = "${nombreCategoria}";
		var isSession = ${isSession};
		var d = new Descuento();
		d.loginCliente = isSession;
		d.q_ordenar = '${ordenar}';
		d.init(idCategoriaDescuento);
	});
</script>