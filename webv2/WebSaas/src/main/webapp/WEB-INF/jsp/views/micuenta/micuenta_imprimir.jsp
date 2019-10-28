<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- informacion -->
		<div id="estado-cuenta" class="limite">
			
			<section class="puntos-actuales">
				<h2 class="subtitulo"><c:out value="${tituloEstadoCuenta}"/></h2>
				
				<section class="cliente-print">
				<div class="cliente-datos">
					<ul>
						<li><spring:message code="label.cliente"/>:</li>
						<li>${sessionCliente.nombre}</li>
					</ul>
					
					<c:if test="${tipoEstadoCuenta<3}">
					<ul>
						<li>Saldo de Millas Benefit:</li>
						<li>${saldoPuntos} </li>
					</ul>
					</c:if>
					
				</div>
				<div class="cliente-datos">	
					<p> ${fechaImpresion}</p>
				</div>	
				
			</section>
			 <div style="border:1px solid #B3C3E3;overflow: hidden; padding: 20px;">
				<div class="resumen-print">
					<div  class="detalle" id="detalle-canjes" >
					</div>
					<div class="grafico-chart-pie" style="width: 99%">
						<c:if test="${tipoEstadoCuenta!=3}">
						<p><c:out value="${tituloResumen}" escapeXml="true" /> <label class="txt-label-periodo"> </label></p>
						<br/>
						</c:if>
						<fieldset><legend><c:out value="${leyendaResumen}" escapeXml="true" /> <label class="txt-label-periodo"> </label></legend>
						<div  id="legenddiv" style="width: 48%; float: left;padding-top: 40px;"></div>
						<div id="chartdiv"  style="width: 50%; height: 360px; float:right;"></div>
						</fieldset>	 
					</div>
					
				</div>
			</div>
			
			<p class="botonera non-printable" style="border-top: none !important">
					<input type=submit value="Imprimir"  id="btnDoImprimir" onclick="printContent('estado-cuenta');"/>
					<a target="_blank" href="${pageContext.request.contextPath}/cliente/generar/pdf?periodo=${requestScope.periodo}&tipoEstadoCuenta=${requestScope.tipoEstadoCuenta}" id="linkGuardarPdf"><input type=submit value="Guardar pdf"  id="btnGuardarPdf"/></a>
			</p>
					<!-- <a target="_blank" id="hrefDescargar" href="${pageContext.request.contextPath}/cliente/generar/pdf?periodo=${requestScope.periodo}&tipoEstadoCuenta=${requestScope.tipoEstadoCuenta}" >Guardar pdf</a> -->
			</section>
		</div>
<!-- /informacion -->


<script src="${prop['config.url.recursos.base.shared']}amcharts/amcharts.js"></script>
<script src="${prop['config.url.recursos.base.shared']}amcharts/pie.js?${prop['config.web.release']}"></script>

<script src="${prop['config.url.recursos.base.web']}static/js/modulos/cliente.js?${prop['config.web.release']}"></script>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
var tipo_estado_cuenta ='${requestScope.tipoEstadoCuenta}';
var mi_periodo  ='${requestScope.periodo}';

$(document).on('ready',function() {
	 var cliente = new Cliente();
	 cliente.instanciaOrigen="impresion";
     cliente.handler();
     cliente.selectEstadoCuenta(tipo_estado_cuenta,mi_periodo);
     cliente.setProperties(mi_periodo,tipo_estado_cuenta);
     
});    
     
      
      
</script>


<script id="template-canje-descuentos" type="text/x-handlebars-template">

<p><spring:message code="label.canje.descuentos.utilizados"/> <label class="txt-label-periodo"></label></p>
<br/>
<table>
<tr>
<th colspan="4" style="text-align:right;color:#686868;font-weight: normal;">Fecha de finalización del período actual: ${fechaFinPeriodo}</th>
</tr>
<tr>
	<th>Descuento</th>
	<th>Nro. de usos del mismo descuento</th>
	<th>Usos que te quedan disponibles en el período</th>
	<th>Fecha de vigencia</th>
</tr>
{{#items}}
<tr class="{{trClass}}">
	<td class="descripcion">
	<span class="ico-cuadro {{colorIco}}"></span><span class="nombre">{{descuento}}</span></td>
	<td><label class="{{tdClass}}">{{usados}} de {{inicio}}</label></td>
	<td><label class="{{tdClass}}">{{disponibles}}</label></td>
	<td>{{vigencia}}</td>
</tr>
{{/items}}

</table>
<div>La fecha de finalizaci&oacute;n del per&iacute;odo actual para el uso de descuentos es el ${fechaFinPeriodo}</div>
								
</script>

<script  id="template-canje-productos" type="text/x-handlebars-template">
<p ><spring:message code="label.canjes.realizados.detalle"/> <label class="txt-label-periodo"></label></p>
<br/>
<table>
<tr>
<th colspan="3" style="text-align:left;color:#686868;font-weight: normal;">{{totalCanjes}} canjes realizados en el período consultado</th>
</tr>

	<tr>
		<th>Descripción</th>
		<th>Fecha</th>
		<th>Millas Benefit Canjeados</th>
	</tr>
{{#items}}
	<tr class="{{trClass}}">
		<td class="descripcion"><span class="ico-cuadro {{colorIco}}"></span><span class="nombre">{{descripcion}}</span></td>
		<td>{{fecha}}</td>
		<td><label class="{{tdClass}}">{{puntosUsados}}</label></td>
	</tr>
{{/items}}
</table>
</script>



