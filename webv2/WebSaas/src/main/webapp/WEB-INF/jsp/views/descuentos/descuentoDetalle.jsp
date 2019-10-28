<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script>(function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1";
    fjs.parentNode.insertBefore(js, fjs);
  }(document, 'script', 'facebook-jssdk'));</script>
<div id="contenedor">

	<section class="miga">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los
				beneficios</a> <span>&gt;</span> <a
				href="${pageContext.request.contextPath}/descuento">Descuentos</a> <span>&gt;</span>
			<p class="activo"></p>
			<p class="activo" id="recortar">${descuentoDetalle.marca.nombre}</p>

		</div>
		<div class="fondo"></div>
	</section>
	
	<div class="volver"><a href="javascript:history.back();" automation="volverDescuentos"><span class="icon-previous"></span>Volver</a></div>
	<div id="detalle-descuento">
		<h3 class="detalle-titulo-responsive" id="recortar" >${descuentoDetalle.titulo}</h3>
		<p class="tipo-local-responsive" automation="categoriaDescuento">${descuentoDetalle.marca.nomCategoria}</p>
		<section class="detalle-desc-img">
			<img src="${prop['url.imagen.descuento']}${descuentoDetalle.imagen}" alt="${descuentoDetalle.titulo}" />
			<div id="share-container">
				<div class="btn-share fb-share-button" data-href="https://developers.facebook.com/docs/plugins/" data-layout="button" data-size="small" data-mobile-iframe="false">
					<a class="fb-xfbml-parse-ignore" target="_blank" href="https://www.facebook.com/sharer/sharer.php?u=${fbUrl}&amp;src=sdkpreparse"><i class="fa fa-facebook-square"></i></a>
				</div>
				<div class="btn-share">
					<a class="twitter-share-button" target="_blank" href="https://twitter.com/intent/tweet?" data-url="${fbUrl}" data-text="${fbTitle}"><i class="fa fa-twitter-square"></i></a>
				</div>
				<div class="btn-share">
					<a href="" class="mail-share-button"><i class="fa fa-envelope-square"></i></a>
				</div>
				<div class="btn-share">
					<a class="whatsapp-share-button" href="whatsapp://send?text=" data-url="${fbUrl}" data-text="${fbTitle}"><i class="fa fa-whatsapp"></i></a>
				</div>
			</div> 
		</section>
		<section class="detalle-desc-info">
			<h3 automation="tituloDescuento">${descuentoDetalle.titulo}</h3>
			<p class="tipo-local" automation="categoriaDescuento">${descuentoDetalle.marca.nomCategoria}</p>
<!-- 			<section class="shared-panel"> -->
<!-- 				<p>Compartir con:</p> -->
<!-- 				<div> -->
<!-- 					<a href="javascript:;" target="_blank" class="shared fb" title="Facebook"> -->
<%-- 						<img src="${prop['config.url.recursos.base.web']}static/css/images/facebook.png"/> --%>
<!-- 					</a> -->
<!-- 				</div> -->
<!-- 			</section> -->
			<div class="porcentaje-descuento">
				<h4 automation="porcentajeDescuento">${descuentoDetalle.porcentajeDescuentoFormat}% dscto.</h4>
				<p automation="descripcionDescuento">${descuentoDetalle.descripcion}</p>
			</div>
			<div class="datos-descuento">
				<ul>
					<!--<li>Ubicación: Lima.</li>-->
					<!--<li>Válido: Todos los días.</li>-->
					<!--<li>Disponible: ${descuentoDetalle.maximoUso} por cliente del 10/04 al 11/07.</li>-->
					<!--<li>Descuento máximo: S/.${descuentoDetalle.consumoMaximoDescuentoFormat}</li>-->
				</ul>
			</div>
			<div class="tabla-info">
				<div class="info-title">
					<span id="title1" class="title1 title-active" automation="labelTerminoDescuento">T&eacute;rminos y <br />condiciones</span>
					<span id="title2" class="title2" automation="labelLocalesDescuento">Locales</span>
				</div>
				<div id="info-body" class="info-body">
					<div id="info-body-1" class="info-body-1 info-body-active">
						<p style="display:none;" automation="terminoDescuento">${descuentoDetalle.terminosCondiciones}</p>
						<ul></ul>
					</div>
					<div id="info-body-2" class="info-body-2" automation="localesDescuento">
						<div id="ciudad-despliegue-lima" class="ciudad-despliegue" style="display:none;">
							<strong class="ciudad">Lima</strong>
							<br />
						</div>
						<br />
						<div id="ciudad-despliegue-provincias" class="ciudad-despliegue" style="display:none;">
							<strong class="ciudad">Provincias</strong>
							<br />
						</div>
					</div>
				</div>
			</div>
			
		
		</section>
		
	</div>
	
<jsp:include page="../modalMensajes.jsp" />
	       
</div>
<script id="templateLocales" type="text/x-handlebars-template">
	<div>
		<ul>
			<li><span class="distrito">{{ubigeo.nombreUbigeo}}</span> - {{direccion}}</li>
		</ul>
	</div>
</script>

<script type="text/javascript">
	$(document).ready(function() {
		var apiKeyG =  '${prop["config.google.api.key.shortLink"]}';
		$(".cargador").hide();
		activarMenu("descuentos");

		var descuentoDetalle = new DescuentoDetalle();
		descuentoDetalle.idDescuento = "${descuentoDetalle.idDescuento}";
		descuentoDetalle.titulo = '${descuentoDetalle.titulo}';
		descuentoDetalle.init();
// 		tablaInfo();

		generateShortLinkTwitter(apiKeyG,'${fbUrl}');
	});
</script>