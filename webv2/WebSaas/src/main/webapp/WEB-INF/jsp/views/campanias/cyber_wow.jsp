<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700,900" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/cyber_wow.css?${prop['config.web.release']}">

<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>
<c:set var="isSession" value="false"></c:set>
<c:if test="${sessionScope.sessionCliente ne null }">
	<c:set var="isSession" value="true"></c:set>
</c:if>

<div id="cyberwow-page"></div>

<section id="banner-cyber">
	<div class="cont-slides"></div>
	<div class="handlers"></div>
</section>

<section id="productos" class="container">
	<div>
		<h3>&iexcl;Nuestras mejores ofertas!</h3>
		<a class= "ver-todos-normal"href="${pageContext.request.contextPath}/compras">Ver todos (0)</a>
	</div>
	<div id="carousel-productos">
		<div class="cont-carousel"></div>
	</div>
	<span id="prev" class="handler material-icons">navigate_before</span>
	<span id="next" class="handler material-icons">navigate_next</span>
	
	<a class="ver-todos" href="${pageContext.request.contextPath}/compras">Ver todos (0)</a>
</section>

<script id="template-producto" type="text/x-handlebars-template">
	<article class="{{#ifconditional stockDisponible value=0}}producto-agotado{{/ifconditional}}">
		<a href="${pageContext.request.contextPath}/producto/{{keyItem}}">			
			{{flagDescuentoWow esOferta tipoOferta colorImagenFlag porcentajeDescuento '${prop['url.imagen.producto']}' }}			
			<img src="${prop['url.imagen.producto']}{{imagenDestacado}}" alt="{{nombreCategoria}}"/>
		</a>
		<div class="detalle-producto">
			<h4><a href="${pageContext.request.contextPath}/producto/{{keyItem}}">{{recortarTitulo titulo 40}}</a></h4>
			{{flagDescuentoDetalleWow esOferta verPrecioRegular precioPuntosRegular}}			
			<p class="mb-actual">{{formatPrecioPuntos}} Millas Benefit</p>			
			<p class="precio-soles">o ${prop['config.moneda.simbolo.default']} {{formatPrecioCatalogo}}</p>
			<div class="zone-buttons">
				<a href="${pageContext.request.contextPath}/producto/{{keyItem}}" class="detalle">Ver detalle</a>
				{{#ifconditional stockDisponible value=0}}
					<a href="${pageContext.request.contextPath}/producto/{{keyItem}}" class="canjear agotado">Agotado</a>
				{{else}}
					<a href="${pageContext.request.contextPath}/producto/{{keyItem}}" class="canjear">Canjear</a>
				{{/ifconditional}}
			</div>
			{{stockWow stockDisponible verCantidadStock verProductoAgotado verPrecioProducto}}
		</div>
	</article>
</script>

<section id="categorias" class="container">
	<h3>Descubre Interbank Benefit</h3>
	<div class="lista-categorias">
		<article class="todas">
			<a href="" class="todos-producto-categoria">Ver todos los productos (0)</a>
		</article>
	</div>
</section>

<script id="template-categorias" type="text/x-handlebars-template">
	<article>
		<a href="${pageContext.request.contextPath}/compras{{filtroCategoria}}">
			<div class="mask"></div>
			<img src="${prop['config.url.recursos.base.web']}static/images/cyber-wow/categorias/{{image}}.png"/>
			<h4>{{name}}</h4>
		</a>
	</article>
</script>

<div id="vuelos" class="container">
	<h3></h3>
	<div>
		<a href="${pageContext.request.contextPath}/viajes"><img src="" alt="Vuelos"/></a>
		<div class="select"></div>
	</div>
</div>

<c:if test="${empty sessionCliente}">
<div id="registro">
	<div class="container">
		<div>
			<h4>&iquest;A&uacute;n no est&aacute;s registrado en Interbank Benefit?</h4>
			<p>Empieza a disfrutar del programa de recompensas exclusivo para clientes Interbank.</p>
		</div>
		<a href="javascript:void(0)" class="btn-azul registrar link btn__ibkbenefit registro__btn">Reg&iacute;strate</a>
	</div>
</div>
</c:if>
<script type="text/javascript">
	$(document).ready(function(){
		var cyberWow = new CyberWow();
		cyberWow.attributeFechaActual = '${cyberWowFechaActual}';		
		cyberWow.attributeFechaInicial = '${cyberWowFechaInicial}';				
		cyberWow.init();		
	});
</script>