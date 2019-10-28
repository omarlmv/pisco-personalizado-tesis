<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<style>
.botonera .continuar{
	max-width :250px !important;
	width:100% !important;
}
</style>
<!-- estado -->
		<p class="separar"></p>
		<section class="estado">
			<p class="barra"></p>
			<div class="limite">
				<p class="titulo-canje">Canje de paquete</p>
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
				<section class="cabecera">
					<h1>¡Felicitaciones!</h1>
					<p>Tu compra / canje de Millas Benefit se ha realizado con éxito</p>
				</section>
				<section class="confirmacion">
					<h3>Comprobante de Compra / Canje de Millas Benefit</h3>
					<div class="codigo-detalle">
						<article class="codigo">
							<div>
								<h4 class="titulo">Tu c&oacute;digo de reserva es:</h4>
								<p class="reserva">${codigoReserva}</p>
							</div>
						</article>
					</div>
				</section>
				<c:if test="${sessionScope.sessionCliente!=null}">
				<p class="botonera">
					<a href="#" id="btnEnviarEmail" class="continuar email">Enviar por E-mail</a>
				</p>
				</c:if>
				<p class="botonera">
					<a href="${pageContext.request.contextPath}/categorias" class="continuar">VER M&Aacute;S PRODUCTOS Y BENEFICIOS</a>
				</p>
			</div>
		</div>
<!-- 		Modal mensajes -->
		<jsp:include page="../modalMensajes.jsp" />
		<!-- /informacion -->
		<c:if test="${sessionScope.sessionCliente!=null}">
		<script type="text/javascript">
		$(document).on('ready',function() {
			var codigoTx='${codigoTx}';
			var enviarEmail = new EnviarEmail();
			enviarEmail.init(codigoTx);
		});
		</script>
		</c:if>
<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/enviarEmail.js"></script>