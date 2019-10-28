<%@ page contentType="text/html;charset=UTF-8" language="java"
	trimDirectiveWhitespaces="true" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" pageEncoding="ISO-8859-1"%>

<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>
<c:set var="isSession" value="false"></c:set>
<c:if test="${sessionScope.sessionCliente ne null }">
	<c:set var="isSession" value="true"></c:set>
</c:if>

<div id="evales-portada" data-menu="mostrar">
	<section class="miga miga-inferior">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}">Todos los beneficios</a>
			<span>&gt;</span>
			<c:choose>
				<c:when test="${idCategoriaEvales eq 0}">
					<p class="activo">Vales digitales</p>
				</c:when>
				<c:otherwise>
					<a href="${pageContext.request.contextPath}/vales-digitales">Vales
						digitales</a>
					<span>&gt;</span>
					<p class="activo">${nombreCategoria}</p>
				</c:otherwise>
			</c:choose>
		</div>
	</section>

	<section id="banner-evales" class="banner-generico">
<%-- 		<c:if --%>
<%-- 			test="${sessionCarritoEvales ne null && fn:length(sessionCarritoEvales.detalles) gt 0}"> --%>
<!-- 			<a -->
<%-- 				href="${pageContext.request.contextPath}/vales-digitales/pendientes" --%>
<%-- 				id="vales-guardados"> <span class="num-vales">${fn:length(sessionCarritoEvales.detalles)}</span> --%>
<%-- 				<span>Vale<c:if --%>
<%-- 						test="${fn:length(sessionCarritoEvales.detalles) > 1}">s</c:if> --%>
<%-- 					digital<c:if test="${fn:length(sessionCarritoEvales.detalles) > 1}">es</c:if> --%>
<%-- 					guardado<c:if --%>
<%-- 						test="${fn:length(sessionCarritoEvales.detalles) > 1}">s</c:if> en --%>
<!-- 					mis 'canjes' -->
<!-- 			</span> -->
<!-- 			</a> -->
<%-- 		</c:if> --%>
		<div class="dots-banner"></div>
	</section>

	<form id="fromselect" method="get">
		<div id="scrollToHere">
			<h2 class="titulo-h2">
				M&aacute;s oportunidades en Vales Digitales (<span
					class="vales-digitales-cantidad">${cantidadCategoriasTotalItems}</span>)
			</h2>
			<div class="zona-filtros">
				<div class="container">
					<div class="header">
						<c:if test="${idCategoriaEvales eq 0}">
							<span>Filtrar por categoría</span>
						</c:if>
						<c:if test="${idCategoriaEvales ne 0}">
							<span style="visibility: hidden;">Filtrar por categoría</span>
						</c:if>
						<label> <input type="text" placeholder="Buscar"
							id="palabraClave" name="buscar"> <span
							class="fa fa-search" id="botonSubmit"></span>
						</label>
					</div>
					<c:if test="${idCategoriaEvales eq 0}">
						<!-- <form class="lista-categorias" id="categoria-evales">-->
						<div class="lista-categorias" id="categoria-evales">
							<c:forEach items="${listaCategoriasTotalItems}" var="lista">
								<label class=""> <input type="checkbox"
									name="filtroCategoria" id="checkCategoria-${lista.idCategoria}"
									class="filter-categoria" value="${lista.idCategoria}">
									<!-- <span class="ch-input checkbox-${lista.idCategoria}">
							<i class="hidden"></i>
						</span>
						 --> <span class="ch-label">${lista.nombreCategoria}
										(${lista.totalProductosCategoria})</span>
								</label>
							</c:forEach>
						</div>
						<!-- </form>-->
					</c:if>
				</div>
			</div>
		</div>
		<div class="container">
			<select id="cbo-filtro-ordenar" name="ordenar"
				class="filtro-ordenar comprar">
				<option value="all">Ordenar por</option>
				<option value="PRECIO_CATALOGO_ASC">De menor a mayor precio</option>
				<option value="PRECIO_CATALOGO_DESC">De mayor a menor
					precio</option>
				<option value="NOMBRE_CATALOGO_ASC">Nombre (A a Z)</option>
				<option value="NOMBRE_CATALOGO_DESC">Nombre (Z a A)</option>
				<c:if test="${sessionCliente ne null}">
					<option value="ALCANZA_CATALOGO_MILLAS">Me alcanza con mis
						Millas Benefit</option>
				</c:if>
			</select>
		</div>
	</form>


	<div class="clearfix"></div>

	<!-- 	<section id="lista-evales" class="lista-articulos container"><div id="main" style = "margin: 20px auto"></div></section>	 -->
	<section id="lista-evales" class="lista-articulos container"></section>
	<br />
	<section class="sectionVerMas">
		<div class="boton-infinite">
			<a href="javascript:;" id="verMas" class="btn-infinite">Ver
				m&aacute;s <span class="ico-down"></span>
			</a>
		</div>
	</section>

	<script id="template-evales" type="text/x-handlebars-template">
		<article>
			<div>			
				<a href="${pageContext.request.contextPath}/vales-digitales/detalle/{{keyItem}}">
					<!--DESCUENTO BARRA O IMAGEN-->
					<div class="cont-flag {{mostrarFlagDescuentoYPrecioProductoEvales stock verProductoAgotado verPrecioProducto}}">						
						{{flagDescuentoEvales esOferta tipoOferta colorImagen porcentajeDescuento '${prop['url.imagen.evales']}' }}
					</div>
					<!--PRODUCTO AGOTADO O STOCK DISPONIBLE-->
					{{mostrarProductoAgotadoEvales stock verCantidadStock verProductoAgotado verPrecioProducto}}					
					<img src="${prop['url.imagen.evales']}{{imagenGaleria}}" alt="Imagen del E-Vale">
				</a>
				<div class="detalle-articulo">
					<a href="${pageContext.request.contextPath}/vales-digitales/detalle/{{keyItem}}"><h2>{{recortarTitulo titulo 27}}</h2></a>
					<!--PRECIO SIN DESCUENTO-->
					<div class = "{{mostrarFlagDescuentoYPrecioProductoEvales stock verProductoAgotado verPrecioProducto}}">
						{{flagDescuentoDetalleEvales esOferta verPrecioRegular tipoOferta precioPuntosRegular colorImagen porcentajeDescuento}}
					</div>												
					<div class = "{{mostrarFlagDescuentoYPrecioProductoEvales stock verProductoAgotado verPrecioProducto}}">
						{{#ifconditional cantidadEvalesPorMarca value=1}}
	    					<p class="precio-millas">{{formatPrecioPuntosMinimo}} <span class="millas">Millas Benefit</span></p>
						{{else}}		
							<p class="precio-millas">Desde {{formatPrecioPuntosMinimo}} <span class="millas">Millas Benefit</span></p>
						{{/ifconditional}}																						
						<p class="precio-soles">o ${prop['config.moneda.simbolo.default']} {{formatearDecimal formatPrecioCatalogoMinimo}}</p>
					</div>						

					<div class="botones">
							<a class="ver-detalle-articulo" href="${pageContext.request.contextPath}/vales-digitales/detalle/{{keyItem}}">Ver detalle</a>
							<div class = "{{mostrarBotonComprarEvales stock verProductoAgotado}}">
								{{#ifconditional cantidadEvalesPorMarca value=1}}
									<a id="canjear-articulo" href="${pageContext.request.contextPath}/vales-digitales/paso1/agregar/{{keyItem}}" class="btn-azul">{{button}}</a>	
								{{else}}
									<a id="canjear-articulo" href="${pageContext.request.contextPath}/vales-digitales/detalle/{{keyItem}}" class="btn-azul">{{button}}</a>	
								{{/ifconditional}}
							</div> 										
					</div>
				</div>
			</div>
		</article>
	</script>

	<script id="template-slider" type="text/x-handlebars-template">
		<article>

			<a href="${pageContext.request.contextPath}/vales-digitales/detalle/{{keyItem}}">
					<div class="cont-flag {{mostrarFlagDescuentoYPrecioProductoEvales stock verProductoAgotado verPrecioProducto}}">
						{{flagDescuentoEvales esOferta tipoOferta colorImagen porcentajeDescuento '${prop['url.imagen.evales']}' }}
					</div>
			</a>
			<a href="${pageContext.request.contextPath}/vales-digitales/detalle/{{keyItem}}">
				<img alt="Banner evales" src="${prop['url.imagen.evales']}{{imagenOportunidad}}">
			</a>
			<div class="detalle-banner">
				<h2>{{titulo}}</h2>
				<div>				
					<div class="precio">
					{{flagDescuentoInfoEvales esOferta verPrecioRegular tipoOferta precioPuntosRegular colorImagen porcentajeDescuento}}								
					{{#ifconditional cantidadEvalesPorMarca value=1}}
						<p class="precio-millas">{{formatPrecioPuntosMinimo}} <span class="millas">Millas Benefit</span></p>
					{{else}}
						<p class="precio-millas">Desde {{formatPrecioPuntosMinimo}} <span class="millas">Millas Benefit</span></p>
					{{/ifconditional}}
						<p class="precio-soles">o ${prop['config.moneda.simbolo.default']} {{formatearDecimal formatPrecioCatalogoMinimo}}</p>
					</div>
					<div class="botones">
						{{mostrarProductoBajoStockEvales stock verCantidadStock}}
						{{#ifconditional cantidadEvalesPorMarca value=1}}
							<a class="comprar-evale btn-azul" href="${pageContext.request.contextPath}/vales-digitales/paso1/agregar/{{keyItem}}">{{button}}</a>
						{{else}}
							<a class="comprar-evale btn-azul" href="${pageContext.request.contextPath}/vales-digitales/detalle/{{keyItem}}">{{button}}</a>	
						{{/ifconditional}}
						<a class="detalle-evale" href="${pageContext.request.contextPath}/vales-digitales/detalle/{{keyItem}}">Ver detalles</a>
					</div>
				</div>
				<c:if test="${empty sessionCliente}">
					<div class="no-cliente">
						&iquest;No eres cliente? <a href="https://interbank.pe/solicitar/tarjeta/credito/inicio/?mcid=benefit:referral:09-2018_TC100:organico:link_hero_categoria_valesdigitales" target="_blank" class="">Hazte cliente ahora</a>
					</div>
				</c:if>
			</div>
		</article>
	</script>

</div>

<script type="text/javascript">
	$(document).ready(function() {
		var valesPortada = new ValesDigitalesPortada();
		valesPortada.idCategoria = '${idCategoriaEvales}';

		valesPortada.q_buscar = '${buscar}';
		valesPortada.q_ordenar = '${ordenar}';
		valesPortada.q_reset = '${reset}';//como valor
		valesPortada.q_categoria = ${categoria};//como objeto		
		valesPortada.LIMITE_X_PAGINA = parseInt('${totalXPagina}', 10);
		valesPortada.init();
	});
</script>