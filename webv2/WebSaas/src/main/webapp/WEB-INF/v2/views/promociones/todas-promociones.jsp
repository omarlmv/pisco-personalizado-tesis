<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="todas-promociones">
	<section class="miga miga-inferior">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los beneficios</a><span>&gt;</span>
			<a href="${pageContext.request.contextPath}/promociones">Promociones</a>
			<span>&gt;</span>
			<p class="activo">Todas las promociones</p>
		</div>
	</section>
</div>

<div id="color-banner">
	<div class="container">
		<h1>Promociones</h1>
		<h4>Descuentos y beneficios exclusivos para los clientes Interbank.</h4>
	</div>
</div>

<div class="filtro-responsive">Editar filtros <i class="icon-down"></i></div>
<section id="filtros" class="container">
	<article class="filtro">
		<span>Promociones</span>
		<div class="dropdown">
			<a href="#" class="drop-toggle" id="filtroPromociones" ><span>Todos</span><span class="icon-down"></span></a>
			<ul class="drop-list" id="select-promociones">
				<c:forEach var="filtroPromo" items="${filtrosPromociones.mapgeneral.promociones}">
					<li class="drop-item toggle-valor-promociones" id="${filtroPromo.valorBusqueda}" data-promocion="${filtroPromo.valorBusqueda}"><c:out value="${filtroPromo.valor}"></c:out></li>
				</c:forEach>
			</ul>
		</div>
	</article>
	<article class="filtro">
		<span>Rubros</span>
		<div class="dropdown">
			<a href="#" class="drop-toggle" id="filtroRubros" ><span>Todos</span><span class="icon-down"></span></a>
			<ul class="drop-list" id="select-rubros">
				<c:forEach var="filtroRubro" items="${filtrosPromociones.mapgeneral.rubro}">
					<li class="drop-item toggle-valor-promociones" id="${filtroRubro.valorBusqueda}" data-rubro="${filtroRubro.valorBusqueda}"><c:out value="${filtroRubro.valor}"></c:out></li>
				</c:forEach>
			</ul>
		</div>
	</article>
	<article class="filtro">
		<span>Marca</span>
		<ul class="filtro-marca">
			<li><img src="${prop['config.url.recursos.base.web']}static/images/promociones/american.png" id="american-express" data-filtromarca="american-express" alt="American Express"></li>
			<li><img src="${prop['config.url.recursos.base.web']}static/images/promociones/visa.png" id="visa" data-filtromarca="visa"  alt="Visa"></li>
			<li><img src="${prop['config.url.recursos.base.web']}static/images/promociones/mastercard.png" id="mastercard" data-filtromarca="mastercard"  alt="MasterCard"></li>
		</ul>
	</article>
</section>

<section id="list-promociones" class="container">
	<div class="lista"></div>
	<div class="paginator">
		<a href="javascript:void(0)" id="botonAnterior" class="buttonesPromociones">Anterior</a>
		<div class="pages" id="paginasPromociones">
		</div>
		<a href="javascript:void(0)" id="botonSiguiente" class="buttonesPromociones">Siguiente</a>
	</div>
	<div class="responsive-paginator">
		<a href="javascript:void(0)" id="botonAnteriorRes" class="buttonesPromociones"><i class="icon-previous"></i></a>
		<div class="dropdown pages" id="paginasPromocionesRes">
			<a href="#" class="drop-toggle"><span>Pag. 1</span><i class="icon-down"></i></a>
			<ul class="drop-list"></ul>
		</div>
		<a href="javascript:void(0)" id="botonSiguienteRes" class="buttonesPromociones"><i class="icon-next"></i></a>
	</div>
</section>

<script id="template-lista-promociones" type="text/x-handlebars-template">
	<div>
		<article>
			{{#if descuento}}
				<a class="cont-flag">
					<div class="flag">{{descuento}}</div>
				</a>
			{{/if}}
			<a href="${pageContext.request.contextPath}/promocion{{urlDetallePromocion}}"><img src="${prop['config.halcon.recursos.base.web']}{{imagenCategoria}}" alt="{{heroTitulo}}"/></a>
			<div class="detalle-promo">
				<a class="title" href="${pageContext.request.contextPath}/promocion{{urlDetallePromocion}}"><h3>{{heroTitulo}}</h3></a>
				<p>{{heroDescripcion}}</p>
				<a href="${pageContext.request.contextPath}/promocion{{urlDetallePromocion}}" class="btn-azul">Descubrir</a>
				<div class="clearfix"></div>
			</div>
		</article>
	</div>
</script>

<script type="text/javascript">
	$(document).ready(function(){
		activarMenu('promociones');
		var promocionesPortada = new PromocionesPortada();
		promocionesPortada.cantidadXVista = "${prop['config.halcon.cantidad.promociones.vista']}";
		promocionesPortada.init();
	});
</script>