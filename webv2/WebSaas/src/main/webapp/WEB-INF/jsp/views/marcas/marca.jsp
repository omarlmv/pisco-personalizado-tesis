<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="contenedor" class="marcasPage flotante-explora slider-foto">
	<section class="miga miga-inferior">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los beneficios</a> <span>&gt;</span>
			<p class="activo">Canje en locales afiliados</p>
		</div>
	</section>
	
<!-- 	<div id="marcas-destacado"> -->

<!-- 		<div class="marca-destacados-manejadores" id="marca-destacados-manejadores"></div> -->
<!-- 	</div> -->
	<section id="slidersHead">
		<nav class="slider-menu">
		</nav>
	</section>
	
</div>
<div class="container-marcas">
	<div class="titulo-general">
		<h1>M&aacute;s oportunidades para canjear tus Millas Benefit en locales (0)</h1>
		<span class="separar centrar"></span>
	</div>
</div>
<div class="menu-marcas">
	<ul class="container-marcas" id="marca-ubicacion">
		<li style="cursor: pointer;"><a id="menu1" class="buscar-marca active"><span class="icon-buscar_por_marca_icono"></span><span>Buscar por categor&iacute;a</span></a></li>
		<li style="cursor: pointer;"><a id="menu2" class="buscar-marca"><span class="icon-buscar_por_ubicacion_icono"></span><span>Buscar por ubicaci&oacute;n</span></a></li>
	</ul>
</div>
<section id="buscarMarca" class="container-marcas">
	<ul class="filtros-marca">
		<li class="filtro-categoria filtro-activo" id="0"><a>Todas<span class="fa fa-plus"></span></a></li>
	</ul>
	<div class="filtros-marca-responsive">
		<label>
			<input type="checkbox" checked class="check-activo" id="0-2"/>
			<span>Todas</span>
		</label>
	</div>
	<div class="select">
	<form id="formFiltros" method="get">
		<select id="cboFiltroMarca" name="ordenar" class="filtro-ordenar comprar" automation="cboOrdenarMarcas">
			<option value="ID">Ordenar por</option>
			<option value="NOMBRE_ASC">Nombre (A a Z)</option>
			<option value="NOMBRE_DESC">Nombre (Z a A)</option>
		</select>
	</form>
	</div>
	<div class="clear"></div>
	<div id="lista-marcas"></div>
	<div id="main"></div>
	<section align="center" class="sectionVerMas" style="display: none;">
		<div class="boton-infinite">
			<a href="javascript:;" id="verMas" class="btn-infinite">Ver m&aacute;s<span class="ico-down"></span></a>
		</div>
	</section>
	
</section>

<section id="ubicacionMarca" class="container-marcas">
	<div>
		<label>Ingresa el nombre de una calle o distrito:</label>
		<div class="txtUbicacion">
			<div class="txtUbicacionShadow"></div>
			<input id="txtUbicacion" type="text" placeholder="Ingresar calle o distrito"/>
			<span class="borrar-ubicacion fa fa-close"></span>
			<p id="sin-ubicacion">No hay resultados</p>
		</div>
		<a id="ubicacionDispositivo" href="#">Usar la ubicación de mi dispositivo</a>
	</div>
	<div id="map"></div>
</section>


<script id="templateListarCategorias" type="text/x-handlebars-template">
	<li class="filtro-categoria" id={{idCategoria}}>
		<a>{{nombre}}<span class="fa fa-plus"></span></a>
	</li>
</script>

<script id="templateListarCategoriasResponsive" type="text/x-handlebars-template">
	<label>
		<input type="checkbox" id="{{idCategoria}}-2"/>
		<span>{{nombre}}</span>
	</label>
</script>

<script id="templateListarMarcas" type="text/x-handlebars-template">
	<div class="marca">
		<a href="${pageContext.request.contextPath}/marca/detalle/{{codigo}}" style="position: relative">
			<img src="${prop['url.imagen.marca']}{{imagenSmallMarca}}" alt="{{marca.nombre}}"/>
		</a>
	</div>
</script>

<script id="templateListarMarcasDestacadas" type="text/x-handlebars-template">
	
		<div class="slider-cnt">
			<div class="img-cnt">
				<a href="#" style="display:block">
					<img src="${prop['url.imagen.marca']}{{imagenLargeMarca}}" class="central" alt="{{marca.nombre}}">
				</a>
			</div>
			<section class="detalle-oportunidad detalle-compras activo">
				<div class="do-titulo {{marca.descripcion}}">
					<h2>{{marca.nombre}}</h2>
				</div>
				<div class="do-left ">
					<p>{{marca.descripcion}}</p>
				</div>
				<div class="do-right {{marca.nombre}}">
					<div class="ver-detalle">
						<a href="${pageContext.request.contextPath}/marca/detalle/{{codigo}}">Ver detalles</a>
					</div>
				</div>
			</section>
		</div>

</script>

<script type="text/javascript">
	$(document).on('ready', function(){
		var portadaMarca = new PortadaMarca();
		portadaMarca.init();
		activarMenu("marcas");
	});
</script>
<!-- Google Maps Api -->
<script async defer src='https://maps.googleapis.com/maps/api/js?key=${prop["config.google.api.key.map.autocomplete"]}&libraries=places&callback=initMapa'></script>
