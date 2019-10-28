<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<tiles:insertAttribute name="pixelConversionGoogle" />
<tiles:insertAttribute name="pixelFacebookBenefit" />
<style>

.estado .limite .estado-4pasos{
	float:none!important;
}

.icon-email{
	background: none;
	width: 23px;
	height: 16px;
	line-height: 1;
	display: inline-block;
	font-size: 16px;
	color: #4CAF50;
	vertical-align: bottom;
	padding-right: 5px;
}

.botonera .continuar{
	max-width :250px !important;
	width:100% !important;
}

table {
	border-collapse: inherit!important;
	border-spacing: inherit!important;
}

@media screen and (max-width: 450px) {
    .estado .limite .estado-4pasos {
        padding-top:25px;
    }
}

</style>

<!-- estado -->
		<p class="separar"></p>
		<section class="estado">
			<p class="barra"></p>
			<div class="limite" align="center">
				<p class="titulo-canje"></p>
				<ul class="estado-4pasos estado-operacion">
					<li class="paso-1 pasado"><span>1</span><p></p></li>
					<li class="paso-2 pasado"><span>2</span><p></p></li>
					<li class="paso-3 pasado"><span>3</span><p></p></li>
					<li class="paso-4 presente completo-on"><span></span><p></p></li>
				</ul>
			</div>
		</section>
		<!-- /estado -->
		<!-- informacion -->
		<div class="limite">
			<div id="procesado">
			<div class="scroll-horizontal">
				${contenido}
			</div>
				
					
				<div class="acciones">
<!-- 					<a href="#" class="enviar">Enviar por E-mail<span></span></a> -->
				</div>
				
				<c:if test="${sessionScope.sessionCliente!=null}">
					<div class="botonera" id="botonera-comprobante">
						<a href="javascript:void(0)" id="btnEnviarEmailProducto" class="botones" ><span class="icon-email"></span>Enviar por e-mail<i class="fa fa-angle-right"></i></a>
<!-- 						<a href="javascript:void(0)" id="btnImprimirProducto" class="botones" ><span class="icon-print"></span>Imprimir<i class="fa fa-angle-right"></i></a> -->
<!-- 						<a href="javascript:void(0)" id="btnDescargarProducto" class="botones" ><span class="icon-ico-acrobat"></span>Descargar como PDF<i class="fa fa-angle-right"></i></a> -->
						
<%-- 						<c:if test="${activedShared}"> --%>
<!-- 						<div class="botones"> -->
<!-- 							<a href="javascript:void(0)" id="btnCompartirProducto"><span class="fa fa-share-alt"></span>Compartir<i class="fa fa-angle-right"></i></a> -->
<!-- 							<div id="share-container"> -->
<!-- 								<div class="btn-share fb-share-button" data-href="https://developers.facebook.com/docs/plugins/" data-layout="button" data-size="small" data-mobile-iframe="false"> -->
<%-- 									<a class="fb-xfbml-parse-ignore" target="_blank" href="https://www.facebook.com/sharer/sharer.php?u=${fbUrl}&amp;src=sdkpreparse"><i class="icon-ico-red-facebook"></i></a> --%>
<!-- 								</div> -->
<!-- 								<div class="btn-share"> -->
<%-- 									<a class="twitter-share-button" target="_blank" href="https://twitter.com/intent/tweet?" data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-ico-red-twitter"></i></a> --%>
<!-- 								</div> -->
<!-- 								<div class="btn-share"> -->
<%-- 									<a href="" class="mail-share-button" data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-icon-mensaje-square"></i></a> --%>
<!-- 								</div> -->
<!-- 								<div class="btn-share"> -->
<%-- 									<a class="whatsapp-share-button" href="whatsapp://send?text=" data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-icon-whatsapp"></i></a> --%>
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</div> -->
<%-- 						</c:if> --%>
						
					</div>
					</c:if>
					
				
				<p class="botonera">
					<a href="${pageContext.request.contextPath}/" class="continuar">Ver más productos y beneficios</a>
				</p>
				
				
			</div>
		</div>
						
				<!-- 		Modal mensajes -->
				<jsp:include page="../modalMensajes.jsp" />
				<div id="lightbox-comprobante">
					<div class="capa"></div>
					<div id="lightbox-loading" class="lightbox resend-email" style="display:none"></div>
				</div>
		<!-- /informacion -->
<script type="text/javascript">
$(document).on('ready',function() {
	var apiKeyG =  '${prop["config.google.api.key.shortLink"]}';
	var codigoTx='${codigoTx}';
	var enviarEmail = new EnviarEmail();
	enviarEmail.init(codigoTx);
	//generateShortLinkTwitter(apiKeyG,'${fbUrl}');
	
	
	
	try{
		var ado = new AdobeAnalytic();
		ado.confirmationCanje('${jsonVuelo.idVenta}','${jsonVuelo.totalPuntosUsados}','${jsonVuelo.totalVenta}','${jsonVuelo.importePuntosDelivery}','${jsonVuelo.costoDelivery}','${jsonVuelo.cliente.direccionDelivery.tipoEntrega}','${jsonVuelo.montoCuponUsado}','${jsonVuelo.referenciaResumen}','${jsonVuelo.tipoVenta.codigo}');
	}catch(err){
		console.error(err);
	}
	
});
</script>
