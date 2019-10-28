<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript">
	var CONTEXT_PATH = "${pageContext.request.contextPath}";
	var URL_RESOURCES = '${prop['config.url.recursos.base.web']}';
	var URL_RESOURCES_HALCON = '${prop['config.halcon.recursos.base.web']}';
	/*Variables para portada-paquete.js */
	var URL_AJAX = "${prop['config.url.recursos.base.web']}static/images/ajax/";
	var PRIMERA_IMAGEN = "${prop['config.url.recursos.base.web']}static/images/central-descuento-2.jpg";
	/*Variables para pasarela de pago*/
	var URL_PASARELA_RESUMEN = "${prop['uri.web.pasarela.resumen']}/";
	var CLIENTE_IS_LOGIN = false;
	var MSG_ERROR_EXCEPTION = '<spring:message code="msg.operacion.exception" text=""  htmlEscape="false" />';
	var MSG_ERROR_EXCEPTION_VUELO_BUS='<spring:message code="msg.vuelo.busqueda.error" text=""  htmlEscape="false" />';
	var MSG_ERROR_EXCEPTION_VUELO_RES='<spring:message code="msg.vuelo.reserva.error" text=""   />';
	
	//console.log('<<web  ID_CLIENTE : '+'${sessionScope.sessionCliente.idCliente}');
	if ('${sessionScope.sessionCliente.idCliente}' == null	|| '${sessionScope.sessionCliente.idCliente}' == "" || ('${sessionScope.sessionCliente.idCliente}').length == 0) {
		CLIENTE_IS_LOGIN = false;
	} else {
		CLIENTE_IS_LOGIN = true;
	}
	var MSG_ERROR_REQUERIDO = '<spring:message code="msg.validacion.form.requerido" text=""  htmlEscape="false" />';
	var MSG_ERROR_MIN = '<spring:message code="msg.validacion.form.min" text=""  htmlEscape="false" />';
	var MSG_ERROR_MAX = '<spring:message code="msg.validacion.form.max" text=""  htmlEscape="false" />';
	var MSG_ERROR_DIGITOS = '<spring:message code="msg.validacion.form.digitos" text=""  htmlEscape="false" />';
	var MSG_SIN_REGISTROS = '<spring:message code="msg.operacion.sin.regristros" text=""  htmlEscape="false" />';
	var MSG_OPERACION_CONSULTANDO = '<spring:message code="msg.operacion.consultando" text=""  htmlEscape="false" />';
	var MSG_OPERACION_CONSULTANDO_NUMERO_ORDEN = '<spring:message code="msg.operacion.consultando.numero.orden" text=""  htmlEscape="false" />';
	var MSG_OPERACION_PROCESANDO = '<spring:message code=" msg.operacion.procesando" text=""  htmlEscape="false" />';
	var MSG_ERROR_EMAIL = '<spring:message code="msg.validacion.form.email"   htmlEscape="false" />';
	var urlLoguot = "${prop['config.oauth.api.url.logout']}";
	var URL_REGISTRO_CLIENTE = "${prop['config.ibk.url.registroCliente']}";
	var URL_RECUPERAR_CUENTA = "${prop['config.ibk.url.recuperarCuenta']}";
	var ENABLE_CONSOLE_LOG = true;
	if ("${prop['config.ambiente.deploy']}" == "produccion") {
		ENABLE_CONSOLE_LOG = false;
	}
	var ACTIVE_MSG_BV = '${sessionScope.sesionBienvenida}';
	var AMBIENTE_DEPLOY = 1; 	
	if("${prop['config.ambiente.deploy']}" == "produccion"){
		AMBIENTE_DEPLOY = 2;
	}
	/* Expresiones regulares */
	var REGEX_NAME = /^[^'"´]{0,50}$/i;
	var REGEX_EMAIL = /^[-\w.%+]{1,64}@(?:[A-Z0-9-]{1,63}\.){1,125}[A-Z]{2,63}$/i;
</script>