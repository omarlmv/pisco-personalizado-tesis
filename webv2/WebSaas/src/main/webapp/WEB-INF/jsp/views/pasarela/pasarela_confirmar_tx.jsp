<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

	<div class="limite">
		<div id="error-pago">
			<div id="procesando-espera">
				<p class="titulo"><c:out value="${msgTituloProcesandoPago}" escapeXml="false"/> <br>
				</p>
						<ul class="gif-procesando">
						    <li><div class="gif-procesando-1"></div></li>
						    <li><div class="gif-procesando-2"></div></li>
						    <li><div class="gif-procesando-3"></div></li>
						    <li><div class="gif-procesando-4"></div></li>
						    <li><div class="gif-procesando-5"></div></li>
						    <li><div class="gif-procesando-6"></div></li>
						    <li><div class="gif-procesando-7"></div></li>
						    <li><div class="gif-procesando-8"></div></li>
						    <li><div class="gif-procesando-9"></div></li>
		  				</ul>
				<p class="texto"><c:out value="${msgDetalleProcesandoPago}" escapeXml="false"/></p>
				<p> <a href="${pageContext.request.contextPath}/">Continuar</a>.</p>
			</div>	
			<div id="procesando-espera-fin" style="display: none;">
				<p class="titulo" id="titulo-espera-fin"><c:out value="${msgDetalleProcesandoPagoError}" escapeXml="false"/></p>
				<p class="texto">${msgInformacionComunicate}</p>
				<p> <a href="${pageContext.request.contextPath}/">Continuar</a>.</p>
			</div>
				
		</div>
		
	</div>
	
<script type="text/javascript">

$(document).ready(function() {

	var timerMilis = '${timerMilisegundos}';
	var timerMaxExpera = '${timerMilisegundosMaxEspera}';
	var urlConsultaEstado = CONTEXT_PATH +'${uriConsultaEstadoPago}';
	var urlComprobantePago = CONTEXT_PATH +'${uriComprobantePago}';
	var codigoTx = '${codigoTx}';
	
	var miPasarela = new Pasarela(timerMilis, timerMaxExpera, codigoTx, urlConsultaEstado , urlComprobantePago);
	miPasarela.init();
});
	
</script>