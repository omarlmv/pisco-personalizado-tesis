<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<!-- Dialogo -->
<div class="lista-carrito-compras">
	<c:if test="${sessionCliente ne null}">
		<section id="carrito" class="flotante-principal carrito-general" <c:if test="${sesionCarritoCompras ne null && fn:length(sesionCarritoCompras.detalles) gt 0}"> style="display: block !important" </c:if>>
			<a href="${pageContext.request.contextPath}/canjeProducto/pendientes" class="espera">
				<span>${fn:length(sesionCarritoCompras.detalles)}</span> 
				producto<c:if test="${fn:length(sessionScope.sesionCarritoCompras.detalles) > 1}">s</c:if> 
				guardado<c:if test="${fn:length(sessionScope.sesionCarritoCompras.detalles) > 1}">s</c:if> 
				en 'Mis canjes'
			</a>
			<div class="resaltar" style="display: none">
				<ul>
					<li><span class="oportunidades-exclusivas"></span></li>
					<li><a href="${pageContext.request.contextPath}/compra/destacados/oportunidad">Oportunidades exclusivas para ti</a></li>
					<li><a href="${pageContext.request.contextPath}/">Ofertas exclusivas para ti</a></li>
				</ul>
			</div>
			<div class="resumen" style="display: none">
				<div>
					<p class="arrow-explora"></p>
					<div class="detalle">
						<div class="explora">
							<a href="#lista-compras">
								<p>Explora todos los beneficios</p>
								<p>(<span class="total-beneficios"></span>) en 'Compras'</p>
							</a>
						</div>
					</div>
				</div>
			</div>
		</section>
	</c:if>
</div>
<div id="contenedor" class="flotante-explora slider-foto">
<!-- 	<div id="slider1" class="img-fondo slider-grupo activo"></div> -->
<!-- 	<div id="slider2" class="img-fondo slider-grupo "></div> -->
<!-- 	<div id="slider3" class="img-fondo slider-grupo"></div> -->
	<!-- menu slider -->
	<section id="slidersHead">
		<nav class="slider-menu">
<!-- 			<a href="javascript:void(0);" data-slider-descuento="slider1" class="activo" id="sli1"></a> -->
<!-- 			<a href="javascript:void(0);" data-slider-descuento="slider2" id="sli2"></a> -->
<!-- 			<a href="javascript:void(0);" data-slider-descuento="slider3" id="sli3"></a> -->
		</nav>
	</section>
	<!-- /menu slider -->

	<!-- miga -->
	<section class="miga miga-inferior">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los beneficios</a> <span>&gt;</span>
			<c:choose>
				<c:when test="${idCategoria eq 0 }">
					<p class="activo">Compras</p>
				</c:when>
				<c:otherwise>
					<a href="${pageContext.request.contextPath}/compras">Compras</a>
					<span>&gt;</span>
					<p class="activo">${nombreCategoria}</p>
				</c:otherwise>
			</c:choose>
		</div>
	</section>
	<div class="cont-top-avisos">
	</div>
	<script id="templateSlider" type="text/x-handlebars-template">
	<div class="slider-cnt {{calcularAncho ancho}}">
		<div class="img-cnt">
			
			<!-- Inicio Flag de descuento -->
			<a href="${pageContext.request.contextPath}/producto/{{keyItem}}">
				<div class="cont-flag">
					{{flagDescuento esOferta tipoOferta colorImagen porcentajeDescuento '${prop['url.imagen.producto']}' }}
				</div>
			</a>
			<!-- Fin Flag de descuento -->

			<a href="${pageContext.request.contextPath}/producto/{{keyItem}}" style="display:block">	
			    {{mostrarProductoAgotadoBanner stockDisponible verCantidadStock verProductoAgotado verPrecioProducto}}
				<img src="${prop['url.imagen.producto']}{{imagenOportunidad}}" class="central" alt="{{titulo}}">
			</a>
		</div>
		<section class="detalle-oportunidad detalle-compras">
       		<div class="do-titulo">
          		<h2>{{nombre}}</h2>
       		</div>
       		<div class="do-left {{opacarPorcentajeDecuento stockDisponible verProductoAgotado verPrecioProducto}}">
<!-- 		Inicio flag -->
				{{flagDescuentoInfo esOferta verPrecioRegular tipoOferta precioPuntosRegular colorImagen porcentajeDescuento}}
<!-- 		Fin flag -->
							
           		<div class="do-millas"><span class="nro">{{formatPrecioPuntos}}</span> Millas Benefit</div>
           		<div class="do-precio">
               		o ${prop['config.moneda.simbolo.default']} {{formatearDecimal formatPrecioCatalogo}}
           		</div>
       		</div>
       		<div class="do-right">
           		<div class="do-comprar">
					{{mostrarProductoBajoStock stockDisponible verCantidadStock}}					
               		<a href="${pageContext.request.contextPath}/producto/{{keyItem}}" class="do-boton {{mostrarBotonComprar stockDisponible verProductoAgotado}}">{{button}}</a>
           		</div>
           		<div class="ver-detalle">
               		<a href="${pageContext.request.contextPath}/producto/{{keyItem}}">Ver detalles</a>
           		</div>
       		</div>
       		<c:if test="${empty sessionCliente}">
       		<div class="cliente">
               ¿No eres cliente? <a href="https://interbank.pe/solicitar/tarjeta/credito/inicio/?mcid=benefit:referral:09-2018_TC100:organico:link_hero_categoria_compras" target="_blank" class="">Hazte cliente ahora</a>
       		</div>
       		</c:if>
   		</section>
	</div>
	</script>
	<!-- /flotante -->
</div>
<!-- /central -->
<!-- ofertas -->
<form id="fromselect" method="get">
	<div class="buscador-ancho" id="scrollToHere">
		<div class="titulo-general">
			<h1>M&aacute;s oportunidades en compras (<span class="oportunidades-compras"></span>)</h1>
			<span class="separar centrar"></span>
		</div>
		<div class="cont-busqueda">
			<label> <input type="text" name="buscar" id="palabraClave" placeholder="Buscar"> <input type="button" class="ico-lupa" value="" id="botonSubmit">
			</label>
			<c:choose>
				<c:when test="${idCategoria eq 0}">
					<div class="filtro-categorias-compras">
						<h4>Filtra por categor&iacute;a</h4>
					</div>
					<div class="despliegue">
						<c:forEach items="${listaCategoriasTotalItems}" var="lista">
							<label><input type="checkbox" name="filtroCategoria" id="checkCategoria-${lista.idCategoria}" class="filter-categoria" value="${lista.idCategoria}">${lista.nombreCategoria}
								(${lista.totalProductosCategoria})</label>
						</c:forEach>
						<div class="salv1">a</div>
					</div>
				</c:when>
				<c:when test="${codigoAgrupador ne null}">
				      <div class="filtro-categorias-compras">
						<h4>Filtra por categor&iacute;a</h4>
					</div>
					<div class="despliegue">
						<c:forEach items="${listaCategoriasTotalItems}" var="lista">
							<label><input type="checkbox" name="filtroSubCategoria" id="checkCategoria-${lista.idCategoria}" class="filter-categoria" value="${lista.idCategoria}">${lista.nombreCategoria}
								(${lista.totalProductosCategoria})</label>
						</c:forEach>
						<div class="salv1">a</div>
					</div>
				</c:when>	
				<c:otherwise>
					<div class="filtro-categorias-compras">
						<h4>Filtra por subcategor&iacute;a</h4>
					</div>
					<div class="despliegue">
						<c:forEach items="${listaCategoriasTotalItems}" var="lista">
							<c:choose>
								<c:when test="${lista.idCategoria eq idCategoria}">
									<c:forEach items="${lista.listaSubCategoria}" var="listaSub">
										<c:if test="${listaSub.totalProductosSubCategoria > 0}">
											<label><input type="checkbox" name="filtroSubCategoria" id="checkCategoria-${listaSub.idSubcategoria}" class="filter-categoria" value="${listaSub.idSubcategoria}">${listaSub.nombreSubcategoria}
												(${listaSub.totalProductosSubCategoria})</label>
										</c:if>
									</c:forEach>
								</c:when>
							</c:choose>
						</c:forEach>
						<div class="salv2"> </div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
		<span class="div-busqueda"></span>
	</div>
	<div class="compras-content cf">
		<select id="cbo-filtro-ordenar" name="ordenar" class="filtro-ordenar comprar">
			<option value="all">Ordenar por</option>
			<option value="PRECIO_CATALOGO_ASC">De menor a mayor precio</option>
			<option value="PRECIO_CATALOGO_DESC">De mayor a menor precio</option>
			<option value="NOMBRE_CATALOGO_ASC">Nombre (A a Z)</option>
			<option value="NOMBRE_CATALOGO_DESC">Nombre (Z a A)</option>
			<c:if test="${sessionCliente ne null}">
				<option value="ALCANZA_CATALOGO_MILLAS">Me alcanza con mis Millas Benefit</option>
			</c:if>
		</select>
<!-- 		</select> <input type="hidden" name="principal" id="productoSlider" /> -->
	</div>
</form>
<div class="limiteCompras" id="limiteCompras">
	<div align="center">
	<script id="templateBody" type="text/x-handlebars-template">
		<article class="tarjeta tarjeta-producto {{#ifconditional stockDisponible value=0}}producto-agotado{{/ifconditional}}">
			<a href="${pageContext.request.contextPath}/producto/{{keyItem}}">
				{{flagDescuento esOferta tipoOferta colorImagen porcentajeDescuento '${prop['url.imagen.producto']}' }}
				<img src="${prop['url.imagen.producto']}{{imagenDestacado}}" alt="{{titulo}}"/>
				{{mostrarDisponibilidadProducto stockDisponible verCantidadStock verProductoAgotado verPrecioProducto}}
			</a>
			<div class="detalle-tarjeta">
				<h3><a href="${pageContext.request.contextPath}/producto/{{keyItem}}">{{nombre}}</a></h3>
				{{#ifmenor stockDisponible value=1}}
					{{#if verPrecioProducto}}
						{{flagDescuentoDetalleProducto esOferta verPrecioRegular tipoOferta precioPuntosRegular colorImagen porcentajeDescuento}}
						<p class="precio-final">{{formatPrecioPuntos}} Millas Benefit</p>
						<p class="precio-soles">o por ${prop['config.moneda.simbolo.default']}{{formatearDecimal formatPrecioCatalogo}}</p>
					{{/if}}
				{{else}}
					{{flagDescuentoDetalleProducto esOferta verPrecioRegular tipoOferta precioPuntosRegular colorImagen porcentajeDescuento}}
					<p class="precio-final">{{formatPrecioPuntos}} Millas Benefit</p>
					<p class="precio-soles">o por ${prop['config.moneda.simbolo.default']}{{formatearDecimal formatPrecioCatalogo}}</p>
				{{/ifmenor}}
				<div class="button-options">
					{{#ifconditional stockDisponible value=0}}
						<!-- <a href="" class="disabled material-icons">add_shopping_cart</a> -->
						<a href="" class="disabled agotado" style="width:100%;font-size:14px;margin:0;">Agotado</a>
					{{else}}
						<a data-href="${pageContext.request.contextPath}/canjeProducto/carrito/{{keyItem}}" class="material-icons btn-carrito-compras">add_shopping_cart</a>
						<a href="${pageContext.request.contextPath}/producto/{{keyItem}}" class="canjear">{{button}}</a>
					{{/ifconditional}}
				</div>
			</div>
		</article>
	</script>
	</div>
	<section id="lista-compras">
		<div id="main" class="lista-productos"></div>
	</section>
	<br />
	<section align="center" class="sectionVerMas">
		<div class="boton-infinite">
			<a href="javascript:;" id="verMas" class="btn-infinite">Ver m&aacute;s<span class="ico-down"></span></a>
		</div>
	</section>
</div>

<div id="modalCarritoCompras">
<!-- 	<div> -->
<!-- 		<i class="material-icons">done</i> -->
<!-- 		<img class="imgCarrito" src="http://s3.amazonaws.com/bim.batch.files/public/web/images/producto/ficha-tecnica/106914-ficha-tecnica.jpg" alt="{{alt}}" /> -->
<!-- 		<h3>¡Producto añadido al carrito!</h3> -->
<%-- 		<a href='${pageContext.request.contextPath}/canjeProducto/pendientes' class='btn-azul btn-ir-carrito'>Ir al carrito</a> --%>
<!-- 		<button type="button" id="btn-seguir-comprando">Seguir comprando</button> -->
<!-- 	</div> -->
</div> 

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
		var compras = new Compras();
		var data = '${displayModal}';
		compras.esCategoria = ${esCategoria};
		compras.q_ordenar = '${ordenar}';
		compras.q_reset = '${reset}';
		compras.puntosCliente = '${cliente.totalPuntos}';
		compras.idCategoria = '${idCategoria}';
		compras.q_buscar = '${buscar}';
		compras.q_categoria = ${categoria};
// 		compras.q_listaSlider = ${listaSlider};
		compras.q_subcategoria = ${subcategoria};
		compras.LIMITE_X_PAGINA = parseInt('${totalXPagina}', 10);
		compras.codigoAgrupador = '${codigoAgrupador}';
		compras.init();
		
		if(data!="") {
						
			var templateHead = $("#template-carrito-modal-compras");
			var sourceTemplateHead  = templateHead.html();
			console.log(sourceTemplateHead);
			var templateHead  = Handlebars.compile(sourceTemplateHead);
			var context = null;
	 		var dataParse = eval("(" + data + ")");
			
			if(dataParse.estado==0) {
					context={"estado": dataParse.estado,"mensaje": dataParse.mensaje,
					    "src": dataParse.result[0],"alt": dataParse.result[1]};
			} else {
					context={"estado": dataParse.estado,"mensaje": dataParse.mensaje,
						    "src": dataParse.result[0],"alt": dataParse.result[1]};
			}
						
			var theCompiledHtml = templateHead(context);
						
			$('#modalCarritoCompras').html(theCompiledHtml);
			$("#modalCarritoCompras").fadeIn('fast').css('display', 'flex');
						
			$('body').on('click', '#btn-seguir-comprando', function(event) {
				$('#modalCarritoCompras').html('');
				$("#modalCarritoCompras").fadeOut('fast');
			});
		}		
	});
</script>
<script id="template-carrito-modal-compras" type="text/x-handlebars-template">
		<div>
			<i class="material-icons close">close</i>
			<i class="material-icons">done</i>
			<img class="imgCarrito" src="{{src}}" alt="{{alt}}" />
			<h3>{{mensaje}}</h3>
			<a href='${pageContext.request.contextPath}/canjeProducto/pendientes' class='btn-azul btn-ir-carrito'>Ir al carrito</a>
			<button type="button" id="btn-seguir-comprando">Seguir comprando</button>
		</div>
</script>