<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <div id="fb-root"></div>
<script>
(function(d, s, id) {
	var api_key_face = '${prop["config.facebook.api.key"]}';
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/es_ES/sdk.js#xfbml=0&version=v2.10&appId="+api_key_face;
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

</script>
  
<div id="contenedor" class="cont-detalle-prod">
	<section class="miga">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los
				beneficios</a> <span>&gt;</span> <a
				href="${pageContext.request.contextPath}/compras">Compras</a> <span>&gt;</span>
			<p class="activo"></p>
			<p class="activo" id="recortar">${productoDetalle.producto.descripcionMarca}</p>

		</div>
		<div class="fondo"></div>
	</section>
	<div class="opciones-detalle">
		<div class="btnVolver">
			<a href="javascript:window.history.back()" ><i class="icon-previous"></i>Volver</a>
		</div>
		<div class="btnCompartir">
			<span class="show-share">Compartir</span>
			<div class="share-icons">
				<span class="hide-share">Cerrar</span> 
				<i class="icon-next"></i>
				<div class="btn-share fb-share-button" data-href="https://developers.facebook.com/docs/plugins/" data-layout="button" data-size="small" data-mobile-iframe="false">
					<a class="fb-xfbml-parse-ignore afb-share-button" data-url="${fbUrl}" data-text="${fbTitle}">
						<i class="icon-ico-facebook"></i>
					</a>
				</div>
				<div class="btn-share">
					<a class="twitter-share-button-cc" target="_blank" href="https://twitter.com/intent/tweet?" data-url="${fbUrl}" data-text="${fbTitle}">
						<i class="icon-ico-twitter"></i>
					</a>
				</div>
				<div class="btn-share btn-whatsapp">
					<a class="whatsapp-share-button" href="whatsapp://send?text=" data-url="${fbUrl}" data-text="${fbTitle}">
						<i class="icon-ico-whatsapp"></i>
					</a>
				</div>
			</div>
			<i class="fa fa-share-alt pointer"></i>
		</div>
	</div>
	<div id="detalle-producto" class="limite">
		<h3 class="detalle-titulo-responsive" id="recortar" >${productoDetalle.producto.descripcionMarca}</h3>
		<p class="nombreCatalogoProducto">${productoDetalle.nombre}</p>
		<section class="detalle-prod-img">
			<img src="${prop['url.imagen.producto']}${productoDetalle.producto.imagen1}" width="580" height="580" alt="${productoDetalle.nombre}" />
			<div id="share-container" class="hidden">
				<div class="btn-share fb-share-button">
					<a class="fb-xfbml-parse-ignore afb-share-button" target="_blank" data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-ico-red-facebook"></i></a>
				</div>
				<div class="btn-share">
					<a class="twitter-share-button-cc" target="_blank" href="https://twitter.com/intent/tweet?" data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-ico-red-twitter"></i></a>
				</div>
<!-- 				<div class="btn-share"> -->
<%-- 					<a href="" class="mail-share-button"  data-url="${fbUrl}" data-text="&iexcl;Mira esta oferta de Interbank Benefit&#33;"><i class="icon-icon-mensaje-square"></i></a> --%>
<!-- 				</div> -->
				<div class="btn-share">
					<a class="whatsapp-share-button" href="whatsapp://send?text=" data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-icon-whatsapp"></i></a>
				</div>
			</div>
		</section>
		<c:set  var="stockMaximo" value="${productoDetalle.producto.stockDisponible}"></c:set>
		<c:if test="${stockMaximo > maximoStockVisible}">
			<c:set var="stockMaximo" value="${maximoStockVisible}" ></c:set>
		</c:if>
		<section class="detalle-prod-info">
			<h3 class="titulo" automation="tituloProducto">${productoDetalle.producto.descripcionMarca}</h3>
			<p class="nombreCatalogoProducto">${productoDetalle.nombre}</p>
				<c:if test="${stockMaximo == 0 && productoDetalle.verProductoAgotado && !productoDetalle.verPrecioProducto}">	
					<c:set  var="mostrarPrecio" value="display:none"></c:set>					
				</c:if>	
				<c:if test="${stockMaximo == 0 && productoDetalle.verProductoAgotado && productoDetalle.verPrecioProducto}">
					<c:set  var="opacarOferta" value="opacity: 0.3"></c:set>
				</c:if>
				
			<div class="do-precio" style="${mostrarPrecio};">
					<c:choose>
						<c:when test="${esOferta and verPrecioRegular and tipoOferta eq 1}">
							<div class="oferta">${precioPuntosRegular} Millas Benefit <span class="porc-oferta" style="background-color:${colorImagen};${opacarOferta};">-${porcentajeDescuento}%</span></div>
						</c:when>
						<c:when test="${esOferta and verPrecioRegular and tipoOferta eq 2}">
							<div class="oferta">${precioPuntosRegular} Millas Benefit</div>
						</c:when>
					</c:choose>
					<span class="millas">${productoDetalle.formatPrecioPuntos} <span class="mb">Millas Benefit </span></span><br />
					<span class="soles">o ${simboloMoneda} ${productoDetalle.formatPrecioCatalogo}</span>				
			</div>
			
			<div class="do-cantidad">		
				<c:choose>
					<c:when test="${stockMaximo > 0}">
						<p>Cantidad:</p>
						<select name="cboSelectCantidad" id="cboSelectCantidad">
							<c:forEach begin="1" end="${stockMaximo}"  var="i" >
								<option value="${i}" ><c:out value="${i}"/></option>
							</c:forEach>
						</select>
						<div class="canje-stock">
						<c:if test="${productoDetalle.verCantidadStock >= productoDetalle.producto.stockDisponible}">
							<span>¡Solo ${productoDetalle.producto.stockDisponible} en stock!</span>
						</c:if>	
							<a href="${pageContext.request.contextPath}/canjeProducto/paso1/${productoDetalle.keyItem}"  class="btn-canje">Iniciar canje</a>
						</div>
					</c:when>
					<c:when test="${stockMaximo == 0 && productoDetalle.verProductoAgotado}">
					   <div class="canje-stock">
							<div class="detalle-agotado">¡Producto agotado!</div>
							<a href="#"  class="btn-canje btn-disabled">Iniciar canje</a>
						</div>
					</c:when>
					<c:otherwise>
						Sin stock
					</c:otherwise>
				</c:choose>
			</div>
			<div class="tabla-info">
				<div class="info-title">
					<span id="title1" class="title1 title-active" automation="labelDetalleProducto">Detalles</span>
				</div>
				<div id="info-body" class="info-body">
					<div id="info-body-1" class="info-body-1 info-body-active">
						<strong>Descripci&oacute;n:</strong>
						<p class="descripcionCatalogoProducto">${productoDetalle.descripcion}</p>
						<br />
						<strong>Categor&iacute;a:</strong>
						<p class="nombreCategoria">${productoDetalle.producto.categoria.nombreCategoria}</p>
					</div>
				</div>
			</div>
			<div class="caracteristicas-ficha">
				<nav id="mainFicha">
								
				</nav>
			</div>
		</section>
	</div>
</div>

<jsp:include page="../modalMensajes.jsp" />


<script id="template-ficha-producto" type="text/x-handlebars-template">
	<a href="#">
		<span class="arrow-preguntas"></span>
	</a>
	<div class="contenidos" id="{{keyConfiguracionParametroDetalle}}">{{descripcionConfiguracionParametroDetalle}}
		<section></section>
	</div>
</script>

<script id="template-atributos-color" type="text/x-handlebars-template">
	<button type="button" class="botonAtributo boton btn-primary" style="width: 50px; border:none; background-color:{{valorAtributo}}" title="{{descripcionAtributo}}" data-atributo="{{valorAtributo}}" data-id="{{catalogoProductoCustom.idCatalogoProducto}}"></button>
</script>

<script id="template-atributos-simple" type="text/x-handlebars-template">
	<button type="button" class="botonAtributo boton btn-primary" style="width: 50px; border:none;" title="{{descripcionAtributo}}" data-atributo="{{valorAtributo}}" data-id="{{catalogoProductoCustom.idCatalogoProducto}}">{{valorAtributo}}</button>
</script>

<script type="text/javascript">
$(document).ready(function(){
	var apiKeyG =  '${prop["config.google.api.key.shortLink"]}';
	activarMenu("compras");
	
	var prod = new ProductoDetalle();
	prod.codigoGrupoCaracteristica = '${codigoGrupo}';
	prod.idCatalogoProducto = '${productoDetalle.idCatalogoProducto}';
	prod.init();
	
	handlerShareButton();
	generateShortLinkTwitter(apiKeyG,'${fbUrl}');
	

//     var objCatalogoProducto = JSON.parse('${productoDetalleJson}');	
	
	//DATA PARA ADOBE
// 	var objAdobeAnalytic = new AdobeAnalytic();
// 	objAdobeAnalytic.fullPath = window.location.href;
// 	objAdobeAnalytic.pathURL = window.location.pathname;
// 	objAdobeAnalytic.tipoIntegracion = ADOBE_ANALYTIC.DETALLE_PRODUCTO;
// 	objAdobeAnalytic.dataAnalizar = objCatalogoProducto;			
// 	var objPageBody = objAdobeAnalytic.sendDataAdobe();
// 	console.log("Body Page: " + JSON.stringify(objPageBody));
// 	digitalData.push(objPageBody);
	
	
})
</script>

