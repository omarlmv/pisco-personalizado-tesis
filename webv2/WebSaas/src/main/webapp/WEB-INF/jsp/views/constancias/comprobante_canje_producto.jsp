<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<tiles:insertAttribute name="pixelConversionGoogle" />
<tiles:insertAttribute name="pixelFacebookBenefit" />
		
		
<style>

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
				<div class="comprobante-pasarela">
				
					<c:if test="${mostrarSoles==true}">
					<p class="titulo-canje">Canje de producto</p>
					</c:if>
									
					<c:if test="${mostrarSoles==false}">
					<p class="titulo-canje">Canje de vale</p>
					</c:if>
									
					<ul class="estado-4pasos estado-operacion">
						<li class="paso-1 pasado"><span>1</span><p></p></li>
						<li class="paso-2 pasado"><span>2</span><p></p></li>
						<li class="paso-3 pasado"><span>3</span><p></p></li>
						<li class="paso-4 presente completo-on"><span></span><p></p></li>
					</ul>
				</div>
			</div>
		</section>
		<!-- /estado -->
		<!-- informacion -->
		<div class="limite">
			<div id="procesado">
				<section class="cabecera">
					<h1>¡Felicitaciones!</h1>
					
					<c:if test="${mostrarSoles==true}">
					<p>Tu compra / canje de Millas Benefit se ha realizado con éxito</p>
					</c:if>
									
					<c:if test="${mostrarSoles==false}">
						<p>Tu canje de Millas Benefit se ha realizado con éxito</p>
					</c:if>
																							
				</section>
				<section class="confirmacion">
				
					<c:if test="${mostrarSoles==true}">
					<h3>Comprobante de Compra / Canje de Millas Benefit</h3>
					</c:if>
									
					<c:if test="${mostrarSoles==false}">
						<h3>Comprobante de Canje de Millas Benefit</h3>
					</c:if>
					
					<p class="operacion-fecha">Operaci&oacute;n hecha el ${fechaTx} a las ${horaTx}</p>
					<div class="codigo-detalle" id="comp-producto">
						<article class="codigo">
							<div>
								<h4 class="titulo">Tu n&uacute;mero de orden es:</h4>
								<p class="reserva">${numeroTx}</p>
								<div class="texto">
                					<c:if test="${mostrarSoles==true}">
									<p>Con este n&uacute;mero podr&aacute;s monitorear el despacho de tu producto</p>
                					</c:if>
                									
                					<c:if test="${mostrarSoles==false}">
									    <p>Con este n&uacute;mero podr&aacute;s monitorear el despacho de tu vale</p>
                					</c:if>									
									
								</div>
							</div>
						</article>
						<article class="detalle">
							<div>
							
								<c:if test="${mostrarSoles==true}">
								<h4 class="titulo">Detalle de la compra / canje:</h4>
								</c:if>
												
								<c:if test="${mostrarSoles==false}">
								<h4 class="titulo">Detalle del canje:</h4>
								</c:if>
							
								<div class="texto">
									<p style="margin: 10px 0 0 0;"><span>Datos del cliente:</span></p>
									<div>
										<p class="out-margin">${nombreCliente}<br /></p>
									</div>
									<c:if test="${aplicaDescuento}">
									
									<p style="margin: 10px 0 0 0;"><span>Monto total:</span></p>
									<div>
										<p class="out-margin">${simboloMoneda} ${montoSinDescuento}<br /></p>
									</div>
									<p style="margin: 10px 0 0 0;"><span>Descuento:</span></p>
									<div>
										<p class="out-margin">${simboloMoneda} ${montoDescontado}<br /></p>
									</div>
									</c:if>
									
									<c:if test="${mostrarSoles==true}">
									<p class="total"><span>Total Pagado:</span><br />${simboloMoneda} ${totalMontoPagado}</p>
									<p><span>Millas Benefit Canjeadas:</span><br />${totalPuntosUsados}</p>									
									</c:if>
									
									<c:if test="${mostrarSoles==false}">
									<p><span>Millas Benefit Canjeadas:</span><br />${totalPuntosUsados}</p>									
									</c:if>
								</div>
							</div>
						</article>
					</div>
					<div class="compra-canje-detalle">
					
					 <c:forEach items="${listaProductos}" var="item" varStatus="cont">
					  <article>
							<p class="titulo-detalle">${item.tituloProducto}</p>
							<img src="${item.urlImagen}" height="235" width="316" alt="" />
							<div class="sobre-el-producto">
<%-- 								<p class="titulo-producto">${item.tituloProducto}</p> --%>
								<ul>
									<li>Cantidad:</li>
									<li>${item.cantidad}</li>
								</ul>
								<ul>
									<li>Descripci&oacute;n:</li>
								</ul>
                                <p>${item.fichaTecnica}</p>								
							</div>
						</article>
					 </c:forEach>
					
						
						<div class="despacho">
							<p class="titulo-detalle">Datos de despacho</p>
							<ul>
								<li>Tipo de entrega:</li>
								<li>${tipoEntrega}, Tu pedido llegará ${tipoDelivery} ${fechaEntrega} </li>
							</ul>
							<ul>
								<li>Departamento:</li>
								<li>${departamento}</li>
							</ul>
							<ul>
								<li>Provincia:</li>
								<li>${provincia}</li>
							</ul>
							<ul>
								<li>Distrito:</li>
								<li>${distrito}</li>
							</ul>
<!-- 							<ul> -->
<!-- 								<li>Urbanizaci&oacute;n:</li> -->
<%-- 								<li>${urbanizacion}</li> --%>
<!-- 							</ul> -->
							
							<ul>
								<li>Direcci&oacute;n:</li>
								<li>${direccion}</li>
							</ul>							
<!-- 							<ul> -->
<!-- 								<li>Número:</li> -->
<%-- 								<li>${direccionNro}</li> --%>
<!-- 							</ul> -->
							<ul>
                					<c:if test="${mostrarSoles==true}">
								<li>Persona que recibe el producto:</li>
                					</c:if>
                									
                					<c:if test="${mostrarSoles==false}">
							        	<li>Persona que recibe el vale:</li>
                					</c:if>	
							
								<li>${personaRecibe}</li>
							</ul>
<!-- 							<ul> -->
<!-- 								<li>Preferencia de entrega:</li> -->
<%-- 								<li>${horarioEntrega}</li> --%>
<!-- 							</ul> -->
						</div>
					</div>
					
					<c:if test="${sessionScope.sessionCliente!=null}">
					<div class="botonera" id="botonera-comprobante">
						<a href="javascript:void(0)" id="btnEnviarEmailProducto" class="botones" ><span class="icon-email"></span>Enviar por e-mail<i class="fa fa-angle-right"></i></a>
<!-- 						<a href="javascript:void(0)" id="btnImprimirProducto" class="botones" ><span class="icon-print"></span>Imprimir<i class="fa fa-angle-right"></i></a> -->
<!-- 						<a href="javascript:void(0)" id="btnDescargarProducto" class="botones" ><span class="icon-ico-acrobat"></span>Descargar como PDF<i class="fa fa-angle-right"></i></a> -->
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
					</div>
					</c:if>
				</section>
				
				<p class="botonera">
					<c:if test="${mostrarSoles==true}">
						<a href="${pageContext.request.contextPath}/categorias" class="continuar">Ver m&aacute;s productos y beneficios</a>
					</c:if>
					<c:if test="${mostrarSoles==false}">
						<a href="${pageContext.request.contextPath}/vales" class="continuar">Ver m&aacute;s vales</a>
					</c:if>
				</p>
				
				<div id="lightbox-comprobante">
					<div class="capa"></div>
					<div id="lightbox-loading" class="lightbox resend-email" style="display:none"></div>
				</div>
			</div>
		</div>
		<jsp:include page="../modalMensajes.jsp" />
		<!-- /informacion -->
		<c:if test="${sessionScope.sessionCliente!=null}">
		<script type="text/javascript">
		$(document).on('ready',function() {
			var apiKeyG =  '${prop["config.google.api.key.shortLink"]}';
			var codigoTx='${codigoTx}';
			var enviarEmail = new EnviarEmail();
			enviarEmail.init(codigoTx);
			//generateShortLinkTwitter(apiKeyG,'${fbUrl}');
			//aca agregar el currency facebook
			
			try{
				var ado = new AdobeAnalytic();
				ado.confirmationCanje('${numeroTx}','${totalMillas}','${totalSoles}','${delMillas}','${delSoles}','${delTipo}','${montoDescontado}','${jsonProd}','${tipoVenta}');
			}catch(err){
				console.error(err);
			}
		});
		</script>
		</c:if>

