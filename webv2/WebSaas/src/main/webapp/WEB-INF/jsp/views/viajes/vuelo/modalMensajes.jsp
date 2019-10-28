<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div id="lightbox-generico" class="buscar-vuelo" style="display: none;">
	<div class="box-generico">
		<p>Estamos generando la reserva de tu vuelo. Por favor espera...</p>
	</div>
</div>

<div id="lightbox-error" class="lightbox" style="display:none">
	<span class="close-modal"></span>
	<img src="${prop['config.url.recursos.base.web']}static/images/pin.png" alt="Pin error" />
</div>

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
<div id="lightbox-congestion" class="buscar-vuelo" style="display: none;">
	<div class="buscar-vuelo-box">
		<span class="close-modal"></span>
		<img src="${prop['config.url.recursos.base.web']}static/images/tiempo-excesivo.png" alt="Pin error" />
		<p class="congestion-title">
			<spring:message code="msg.espere.procesando.busqueda.nuevo" htmlEscape="false"/>
		</p>
		<p>
			<spring:message code="msg.espere.procesando.congestion" htmlEscape="false" />
		</p>
		<button>Cerrar</button>
	</div>
</div>