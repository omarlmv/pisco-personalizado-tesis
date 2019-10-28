<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div id="lightbox-procesando" class="buscar-vuelo" style="display: none;">
	<div class="buscar-vuelo-box">
		<p>
			<spring:message code="msg.espere.procesando.busqueda.vuelos"
				htmlEscape="false" />
		</p>
		<h4>
			Vuelo: &nbsp;<span id="spn-resumenvuelo"></span>
		</h4>

		<div class="pop-busqueda-both">
			<div class="pop-busqueda-itine">
				<p>
					Ida: &nbsp;<span id="spn-fida"></span>
				</p>
			</div>
			<div class="pop-busqueda-itine pop-busqueda-vuelta">
				<p>
					Vuelta: &nbsp;<span id="spn-fvuela"></span>
				</p>
			</div>
		</div>
		<div class="gif-avion">
			<div class="gif-avion-puntos">
				<div class="gif-avion-puntos2">
					<span></span><span></span> <span></span><span></span>
				</div>
			</div>
			<div class="gif-avion-img">
				<img
					src="${prop['config.url.recursos.base.web']}static/images/avion-n.png"
					alt="">
			</div>
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
				<input  type="hidden" name="tipo" value="${tipoEntidad}"/>
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

<div id="lightbox-borrar-direccion" class="override-modal">
	<div class="general-modal">
		<div class="close-modal"></div>
		<div class="message">
			<p><spring:message code="msg.canje.pase.dos.borrar.direccion" htmlEscape="false "/></p>
		</div>
		<div class="buttons">
			<input class="btn-cancel" type="button" value="Cancelar"/>
			<input id="eliminarDireccion" class="btn-success" type="button" value="Eliminar"/>
		</div>
	</div>
</div>

<div id="lightbox-falta-datos" class="override-modal">
	<div class="general-modal">
		<div class="close-modal"></div>
		<div class="message">
			<p>Aquí va el mensaje</p>
		</div>
		<div class="buttons">
			<input class="btn-success btn-reload-app" type="button" value="Aceptar"/>
		</div>
	</div>
</div>

<div id="lightbox-guardar-direccion" class="override-modal">
	<div class="general-modal">
		<div class="close-modal"></div>
		<div class="message">
			<p><spring:message code="msg.canje.pase.dos.guardar.direccion" htmlEscape="false" /></p>
		</div>
		<form id="form-guardar-direccion" >
			<div class="nombre-direccion">
				<p>&iquest;Con qu&eacute; nombre quieres guardar la direcci&oacute;n?</p>
				<input id="direccionNombre" type="text" placeholder='Ejemplo: "Casa de padres"' name="direccionNombre" />
			</div>
			<div class="buttons">
				<input id="cancelar-guardar" class="btn-cancel" type="button" value="No, gracias"/>
				<input id="aceptar-guardar" class="btn-success" type="button" value="S&iacute;"/>
				<input id="cancelar" class="btn-cancel hidden" type="button" value="Cancelar"/>
				<input id="confirmar-guardar" class="btn-success hidden" type="button" value="Guardar"/>
			</div>
		</form>
	</div>
</div>

<div id="lightbox-direccion-grabada" class="override-modal">
	<div class="general-modal">
<!-- 		<div class="close-modal"></div> -->
		<h3 class="message"><spring:message code="msg.canje.pase.dos.direccion.grabada" htmlEscape="false"/></h3>
		<p class="message"><spring:message code="msg.canje.pase.dos.direccion.grabada.dos" htmlEscape="false"/></p>
		<div><img alt="Gif timer" src="${prop['config.url.recursos.base.web']}static/images/pre-carga.gif"></div>
	</div>
</div>

<!-- popup reserva -->
<div id="lightbox-reserva" class="buscar-vuelo" style="display: none;">
	<div class="buscar-vuelo-box">
		<span class="close"></span>
		<img src="" alt="Icono pendiente" />
		<spring:message code="msg.reserva.vuelos.reserva" htmlEscape="false" />
		<div class="button-options">
			<button>S&iacute;</button>
			<button>No</button>
		</div>
	</div>
</div>
<!-- /popup reserva -->

<!-- popup perdida de vuelo -->
<div id="lightbox-perdida" class="buscar-vuelo" style="display: none;">
	<div class="buscar-vuelo-box">
		<span class="close"></span>
		<spring:message code="msg.reserva.vuelos.perdida" htmlEscape="false" />
		<div class="button-options">
			<button class="nueva-busqueda">Buscar de nuevo</button>
			<button class="cambiar-busqueda">Modificar b&uacute;squeda</button>
		</div>
	</div>
</div>
<!-- /popup perdida de vuelo -->

<div id="lightbox-send-form" class="lightbox" style="display: none">
	<p class="texto">
		<spring:message code="msg.espere.procesando.viaje" htmlEscape="false" />
	</p>
	<span class="cargador-airplane"></span>
</div>
<div id="lightbox-procesando-reserva" class="lightbox" style="display: none">
	<p class="texto">
		<spring:message code="msg.espere.procesando" htmlEscape="false" />
	</p>
</div>

<div id="lightbox-no-diponible" class="buscar-vuelo" style="display: none;">
	<div class="buscar-vuelo-box">
		<p>
			<spring:message code="msg.vuelo.procesando.nodisponible" htmlEscape="false" />
		</p>
		<div class="end-gif" style="display:block;">
			<a href="javascript:;" class="nueva-busqueda">Buscar de nuevo</a>
			<a href="javascript:;" class="cambiar-busqueda ">Modificar busqueda</a>
		</div>
	</div>
</div>

<div id="lightbox-send-form-paquete" class="lightbox" style="display: none">
	<p class="texto">
		<spring:message code="msg.espere.procesando.canje" htmlEscape="false" />
	</p>
	<span class="cargador-airplane"></span>
</div>

<!-- Modal niño infante -->
<div id="lightbox-ninio-infante" class="override-modal">
	<div class="general-modal" style="max-width: 360px;">
		<div class="close-modal" style="top: 20px; right: 20px;"></div>
		<p><spring:message code="msg.vuelo.ninio.infante.desc" htmlEscape="false"/></p>
		<a class="btn-success" id="modiciar-vuelo-pasajero">Modificar tipo de pasajero</a>
	</div>
</div>

<div id="lightbox-espera" class="buscar-vuelo" style="display: none;">
	<div class="buscar-vuelo-box">
		<p>
			<spring:message code="msg.espere.procesando.busqueda.nuevo" htmlEscape="false" />
		</p>
		<div class="timer-gif" style="display:block;">
			<span class="gif-timer"><img src="" alt=""  data="${prop['config.url.recursos.base.web']}static/images/benni-timer.gif?codeGen="/></span>
			<a href="javascript:;" class="cambiar-busqueda">Cambiar búsqueda original </a>
		</div>
		<div class="end-gif" style="display:none;">
			<span class="close-modal"></span>
			<a href="javascript:;" class="nueva-busqueda">Buscar de nuevo</a>
			<a href="javascript:;" class="cambiar-busqueda ">Cambiar búsqueda original </a>
		</div>
	</div>
</div>

<div id="lightbox-msg-susto" class="override-modal">
	<div class="general-modal">
		<i class="material-icons warning-icon">playlist_add_check</i>
		<div>
			<h3>&iquest;Todo listo para continuar?</h3>
			<p>Revisa los datos de tu vuelo, dado que si luego retrocedes podr&iacute;as perder tu pre-reserva y es posible que las tarifas cambien.</p>
			<div class="button-options">
				<a class="btn-success" id="btn-susto-quedarse">Revisar datos</a>
				<a class="btn-success" id="btn-susto-continuar">Continuar</a>
			</div>
		</div>
	</div>
</div>


<script type="text/javascript">
$(document).ready(function(){
	sendMailSharedOferta();
});
</script>