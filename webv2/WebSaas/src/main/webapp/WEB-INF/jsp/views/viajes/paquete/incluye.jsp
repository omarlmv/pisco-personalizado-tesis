<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
(function(d, s, id) {
	var api_key_face = '${prop["config.facebook.api.key"]}';
  
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/es_ES/sdk.js#xfbml=1&version=v2.10&appId="+api_key_face;
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

</script>


<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>
<!-- /menu secundario -->
<section class="miga miga-detalle-paquete">
	<div class="detalle">
		<a href="${pageContext.request.contextPath}/">Todos los beneficios</a> <span>&gt;</span> <a href="${pageContext.request.contextPath}/viajes">Viajes</a> <span>&gt;</span>
		<p class="activo">
		<p class="activo">Paquete</p>
	</div>
	<div class="fondo"></div>
</section>
<div class="opciones-detalle" data-menu="mostrar">
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

<div class="cont-detalle-paquete limite">
	<div class="detalle-paquete-info-responsive">
		<div class="titulo">
			<h3>${reservaPaqueteForm.tituloPaquete}</h3>
		</div>
	</div>
	<div class="detalle-paquete-img">
		<img src="<c:out value="${imagenDestacado}" escapeXml="false" />" alt="${reservaPaqueteForm.tituloPaquete}" />
<!-- 		<div id="share-container"> -->
<!-- 			<div class="btn-share fb-share-button" data-href="https://developers.facebook.com/docs/plugins/" data-layout="button" data-size="small" data-mobile-iframe="false"> -->
<%-- 				<a class="fb-xfbml-parse-ignore" target="_blank" href="https://www.facebook.com/sharer/sharer.php?u=${fbUrl}&amp;src=sdkpreparse"><i class="icon-ico-red-facebook"></i></a> --%> 
<%-- 				<a class="fb-xfbml-parse-ignore afb-share-button"   data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-ico-red-facebook"></i></a> --%>
<!-- 			</div> -->
<!-- 			<div class="btn-share"> -->
<%-- 				<a class="twitter-share-button-cc" target="_blank" href="https://twitter.com/intent/tweet?" data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-ico-red-twitter"></i></a> --%>
<!-- 		</div> -->
<!-- 			<div class="btn-share"> -->
<%-- 				<a href="" class="mail-share-button"  data-url="${fbUrl}" data-text="&iexcl;Mira este paquete de Interbank Benefit&#33;"><i class="icon-icon-mensaje-square"></i></a> --%>
<!-- 			</div> -->
<!-- 			<div class="btn-share"> -->
<%-- 				<a class="whatsapp-share-button" href="whatsapp://send?text=" data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-icon-whatsapp"></i></a> --%>
<!-- 			</div> -->
<!-- 		</div> -->
	</div>
	<div class="detalle-paquete-info">
		<div class="titulo">
			<h3>${reservaPaqueteForm.tituloPaquete}</h3>
		</div>
		<div class="costo">
			<p>
				<span class="costo-millas">${formatCostoEnPuntos} </span>Millas Benefit por persona
			</p>
			<p class="costo-soles" id="costoPaquete"></p>
		</div>
		<div class="pasajeros">
			<div id="pasajeros-div">
				<span>Adultos <small>(+12 años)</small>:</span> <select class="cboPasajerosPaquete" name="" id="cboPasajerosPaquete">
					<c:if test="${stockPaquete > 4}">
						<c:forEach begin="1" end="5" step="1" varStatus="loop">
							<option value="${loop.count*2}">${loop.count*2}</option>
						</c:forEach>
					</c:if>

					<c:if test="${stockPaquete < 5}">
						<c:forEach begin="1" end="${stockPaquete}" step="1" varStatus="loop">
							<option value="${loop.count*2}">${loop.count*2}</option>
						</c:forEach>
					</c:if>
				</select>
			</div>
			<a href="javascript:void(0)" class="btnCanjearPaquete" id="btnCanjearPaquete" style="display: block;">Canjear</a>
			<p id="sinStock" style="display: none;">Sin Stock</p>
		</div>
		<div class="cont-detalle-condic">
			<div class="titulo">
				<span class="titulo-detalle titulo-activo" data-titulo="1">Detalles</span> <span class="titulo-condicion" data-titulo="2">Condiciones</span>
			</div>
			<div class="contenido">
				<div class="contenido-detalle contenido-activo" data-contenido="1">
					<p>${obtenerDetalle.descripcion}</p>
				</div>
				<div class="contenido-condicion" data-contenido="2">
					<p>${obtenerDetalle.detallePaquete.infoAdicionalDisponibilidad}</p>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="../../modalMensajes.jsp" />

<script type="text/javascript">
$(document).ready(function() {
	var apiKeyG =  '${prop["config.google.api.key.shortLink"]}';
	var paqueteComun = new PaqueteComun();
	paqueteComun.formatoSoles = formatearDecimal('S/ ','${reservaPaqueteForm.precioSoles}', 2);
	paqueteComun.formatoDolares = formatearDecimal('US$ ','${formatPrecioDolares}', 2);
	paqueteComun.keyItem = "${keyItem}";
	paqueteComun.stockPaquete = "${stockPaquete}";
	paqueteComun.init();
	paqueteComun.funcTabla();
	paqueteComun.incluye();
	handlerShareButton();
	generateShortLinkTwitter(apiKeyG,'${fbUrl}');
});
</script>