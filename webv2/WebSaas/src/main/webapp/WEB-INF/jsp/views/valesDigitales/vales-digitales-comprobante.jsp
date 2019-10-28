<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<tiles:insertAttribute name="pixelConversionGoogle" />
<tiles:insertAttribute name="pixelFacebookBenefit" />

<div id="vales-comprobante">
	<div class="separar"></div>
	<div id="pasos">
		<div class="container">
			<p>Canje de Vale Digital</p>
			<ul>
				<li class="paso-anterior"><span>1</span></li>
				<li class="paso-anterior"><span>2</span></li>
				<li class="paso-activo"><span class="icon-check"></span></li>
			</ul>
		</div>
	</div>
	
	<div class="container">
		<div class="cnt-comprobante">
			<div class="intro">
				<h1>&iexcl;Felicitaciones!</h1>
				<p>Tu canje de Millas Benefit se ha realizado con &eacute;xito</p>
				<p>Comprobante de Canje de Millas Benefit</p>
				<p>Operaci&oacute;n hecha el ${comprobante.fechaTx} a las ${comprobante.horaTx}</p>
			</div>
			<div class="datos-canje">
				<div class="orden-compra">
					<p>Tu orden de canje es:</p>
					<p class="num-orden">${comprobante.numeroTx}</p>
					<p>Este no es tu vale digital. Hemos enviado tu vale(s) al e-mail se&ntilde;alado.</p>
				</div>
				<div>
					<h2>Detalle del canje</h2>
					<p>Datos del cliente:</p>
					<p>${comprobante.nombreCliente}</p>
					<br>
<!-- 					<p>Total pagado:</p> -->
<%-- 					<p>${comprobante.simboloMoneda} ${comprobante.totalMontoPagado}</p> --%>
					<br>
					<p>Millas Benefit canjeadas:</p>
					<p>${comprobante.totalPuntosUsados}</p>
				</div>
			</div>
			<section class="detalle-canje">
			 <c:forEach items="${comprobante.listaEvales}" var="item" >
			 
				<article>
					<h3>${item.titulo}</h3>
					<div>
						<img src="${item.urlImagen}" alt="Imagen de comprobante"/>
						<div class="description">
							<p><strong>Cantidad: </strong>${item.cantidad}</p>
							<p>
								<strong>Descripci&oacute;n:</strong>
								<br>
								<span>${item.descripcion}</span>
							</p>
							<ul>
								<li>
									<strong>Detalles de env&iacute;o
									</strong>
<!-- 									<strong>Cantidad</strong> -->
								</li>
								 <c:forEach items="${item.grupoEnvios}" var="envio" >
								<c:if test="${envio.esRegalo eq false}">
								<li>
									<span>
<!-- 										<strong>Para m&iacute;</strong> -->
										<span>E-mail de env&iacute;o: ${envio.emailContacto}</span>
									</span>
<%-- 									<span class="cant-vales">${envio.cantidad}</span> --%>
								</li>
								</c:if>
								<c:if test="${envio.esRegalo}">
								<li>
									<span>
										<strong>${envio.ordenRegalo}. Regalo</strong>
										<span>Para ${envio.nombreContacto}</span>
										<span>E-mail de env&iacute;o: ${envio.emailContacto}</span>
									</span>
<%-- 									<span class="cant-vales">${envio.cantidad}</span> --%>
								</li>
								</c:if>
								
								</c:forEach>
							</ul>
						</div>
					</div>
				</article>
				
				</c:forEach>
				
			</section>
			<div class="envio-mail">
				<a href="javascript:void(0)" id="btnEnviarEmailProducto"><i class="envelope material-icons">mail</i><span>Enviar por e-mail </span><i class="material-icons">keyboard_arrow_right</i></a>
			</div>
		</div>
		<div class="ver-mas">
			<a class="btn-verde" href="${pageContext.request.contextPath}/vales">Ver m&aacute;s productos y beneficios</a>
		</div>
		
		<div id="lightbox-comprobante">
			<div class="capa"></div>
			<div id="lightbox-loading" class="lightbox resend-email" style="display:none"></div>
		</div>
	</div>
</div>


<div id="lightbox-enviar-correo" class="override-modal">
	<div class="general-modal">
		<div class="close-modal"></div>
		<form:form id="form-send-mail-shared" action="">
			<label class="lbl-mail" for="">
				<span>E-mail:</span>
				<input id="email-send" name="emailSend" type="text" />
				<input type="hidden" name="tipo" value="${tipoEntidad}"/>
			</label>
			<label class="lbl-message" for="">
				<span>Mensaje:</span>
				<textarea id="message-send" name="messageSend" cols="30" rows="5"></textarea>
			</label>
			<input class="btn-success send-mail-shared-oferta" type="button" value="Enviar"/>
		</form:form>
	</div>
</div>

<div id="lightbox-envio-correcto" class="override-modal">
	<div class="general-modal">
		<div class="close-modal"></div>
		<div class="message">
			<p class="message-int exito ocultar-div">
				<i class="fa fa-check check-mail"></i>
				<span><spring:message code="msg.modal.envio.correcto" htmlEscape="false" /></span>
			</p>
			<p class="message-int error ocultar-div">
				<i class="fa fa-exclamation-triangle error-mail"></i>
				<span><spring:message code="msg.modal.envio.error" htmlEscape="false" /></span>
			</p>
		</div>
		<input id="btn-send-mail" class="btn-success" type="button" value="Aceptar"/>
	</div>
</div>


<script type="text/javascript">
$(document).ready(function(){
	var codigoTx='${codigoTx}';
	var enviarEmail = new EnviarEmail();
	enviarEmail.init(codigoTx);
	
	sendMailSharedOferta();
});
</script>
