<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<div id="vales-paso2">
	<div class="separar"></div>
	<div id="pasos">
		<div class="container">
			<p>Canje de vale digital</p>
			<ul>
				<li class="paso-anterior"><span>1</span></li>
				<li class="paso-activo"><span>2</span></li>
				<li><span class="icon-check"></span></li>
			</ul>
			<div class="resumen-paso2">
				<div class="lista-canjes">
					<h4>Mis canjes</h4>
					<ul class="lista-items"></ul>
					<ul class="resumen"></ul>
				</div>
				<div class="cont-resumen">
					<!-- <div class="puntos-disponibles">
						<i class="icon-check"></i>
						<p>Te alcanza con tus Millas Benefit<br>Puntos disponibles tras el canje: 5,839,999</p>
					</div> -->
					<div class="uso-millas total-pagar">
						<p><span>Millas benefit por canjear:</span><span class="canje-puntos-cubrir">0.0</span></p>
						<p><span>Total a pagar: </span> <span ><span>S/</span><span class="canje-precio-cubrir-efectivo">0.00</span></span></p>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="back-option container">
		<a href="javascript:window.history.back()"><i class="material-icons">arrow_back</i>Regresar</a>
	</div>
	
	<div class="container">
		<div class="info-paso2">
			<h1>Resumen de la compra</h1>
			<section id="vales-por-canjear"></section>
			<script id="template-resumen-vales" type="text/x-handlebars-template">
				<article>
					<div>
						<div>
							<div class="cnt-image"><img alt="Imagen de vale" src="${prop['url.imagen.evales']}{{item.evales.imagenFormat}}"/></div>
							<div>

								<div>
									<h2>{{item.evales.titulo}}</h2>
									<p>por {{item.evales.precioPuntosFormat}} Millas Benefit</p>
									<p>o ${simboloMoneda}{{item.evales.precioCatalogoFormat}}</p>
								</div>
								<div class="envio-cantidad">
									<p>Cantidad</p>
									<p class="num">{{item.cantidad}}</p>
								</div>

							</div>
						</div>
						{{#ifmayor item.cantidadRegalos  value=0}}
							<p class="multiple-envio">Enviar&aacute; {{item.cantidadRegalos}} de {{item.cantidad}} vales digitales como regalo</p>
							<button type="button" class="detalles">Ver detalles</button>
						
						{{/ifmayor}}
						{{#ifconditional item.cantidadRegalos value = 0}}
							<p class="unico-envio">
								<strong><i class="material-icons">mail_outline</i>Env&iacute;o:</strong>
								<span>${cliente.email}</span>
							</p>
						{{/ifconditional}}
					</div>
			{{#ifmayor item.cantidadRegalos  value=0}}
					<section id="detalle-multiple-envio">
							<p>Cantidad</p>
							<div class="clearfix"><div>
							<div id="detalle-desglose-{{indice}}">
								
							</div>

							<button type="button" class="ocultar">Ocultar detalles</button>
						</section>
					{{/ifmayor}}
				</article>
			</script>
		</div>
	</div>
	
	<div class="zona-canje container">
		<div>
			<h2>Medio de canje</h2>
			<div id="canje-vales">
			<form:form id="formCanjeEvales" action="${pageContext.request.contextPath}/vales-digitales/finalizar" method="POST" autocomplete="off" commandName="canjeEvalesForm">
		
				<ul>
					<li>
						<span>Mi total en Millas Benefit:</span>
						<span> ${formatPuntosCliente} </span>
					</li>
					<li>
						<span>Valor del producto en Millas Benefit:</span>
						<span>${formatTotalPuntos}</span>
					</li>
					<li>
						<span>Monto total:</span>
						<span>${simboloMoneda} ${formatTotalPrecio}</span>
					</li>
				</ul>
				<c:if test="${cliente.totalPuntos > 0}" > 
				<label>
					<span>&iquest;Cu&aacute;ntas Millas Benefit quieres usar?</span>
					<div>
						<form:input required="required" type="text" value="" path="puntosUsados"   />
						<button type="button" id="btnActualizarPuntos">Actualizar</button>
					</div>
				</label>
				<div class="p3barra">
					<input id="num-range" type="text" name="num-range" style="display:block !important;visibility:hidden;"/>
				</div>
				
				<p id="cubrir-diferencia">
				    <span>Diferencia por cubrir: </span>
				    <span class="moneda">${simboloMoneda}<span class="canje-precio-cubrir-efectivo">${formatMontoPorCubrir}</span></span>
				</p>
				
				</c:if>
				
				<div class="botonera">
					<a href="javascript:history.back(1)" class="volver">Volver</a> 
					<input type="submit" value="Siguiente" class="continuar">
				</div>
				</form:form>
			</div>
		</div>
<!-- 		<iframe id="iframe-medio-canje" width="720" onload="ocultarLoad()"></iframe> -->
		<div class="informacion">
			<div class="medio-canje off" id="content-iframe-medio-canje">
			  <div id="loadIframePasarela"></div>
			  <iframe id="iframe-medio-canje" width="720" onload="ocultarLoad()">
			  
			  </iframe>
			</div>
		</div>
	
	
		
	</div>
</div>
<div id="lightbox-send-form" class="lightbox" style="display:none"><p class="texto"><spring:message code="msg.espere.procesando.canje" htmlEscape="false"/></p><span class="cargador"></span>

</div>
<div id="lightbox-procesando" class="lightbox" style="display:none"><p class="texto"><spring:message code="msg.espere.procesando" htmlEscape="false"/></p><span class="cargador"></span></div>

<script id="template-resumen-vales-desglose" type="text/x-handlebars-template">
<article class="for-gift">
								<div>
									<div>
										<p>
											
											<span><strong>{{#if grupoRegalo.esRegalo}}Regalo {{counterRegalo}} {{else}} Para mí {{/if}} <span class="point">·</span></strong></span>

											<span> {{evale.titulo}}</span>
										</p>
									{{#if grupoRegalo.esRegalo}}
										<p class="name-pc">
											<span>Nombre </span>
											<span class="name">{{grupoRegalo.nombreContacto}}</span>
										</p>
									{{/if}}

										<p class="mail-pc">
											<span>E-mail </span>
											<span class="email">{{grupoRegalo.emailContacto}}</span>
										</p>
									</div>
									<p class="cantidad">{{grupoRegalo.cantidad}}</p>
								</div>
							{{#if grupoRegalo.esRegalo}}

								<p class="name-responsive">
									<span>Nombre </span>
									<span class="name">{{grupoRegalo.nombreContacto}}</span>
								</p>
							{{/if}}
								<p class="mail-responsive">
									<span>E-mail </span>
									<span class="email">{{grupoRegalo.emailContacto}}</span>
								</p>
							</article>
</script>	

<script id="template-stock-excedido" type="text/x-handlebars-template">

<div id="div-stock-excedido"  class="lightbox-mensaje lightbox" style="cursor: default;"> 
        <p class="texto">{{mensaje}}</p>       
	<div class="botonera">
		<a href="${pageContext.request.contextPath}/vales-digitales/validarStock"  class="canjear">Regresar</a>
	</div>
</div> 
</script>						
<script type="text/javascript">

	$(document).ready(function(){
		var paso2 = new ValesDigitalesPaso2();
		paso2.carrito = <c:out value='${carritoEvales}' escapeXml='false'></c:out>;
		paso2.totalPuntosCliente="${cliente.totalPuntos}";
		paso2.totalImportePuntos="${totalPuntosUsar}";
		paso2.nameCsrf = "${_csrf.headerName}";
		paso2.valueCsrf = "${_csrf.token}";
		paso2.htmlProcess= $("#lightbox-procesando");
		paso2.htmlFinalizaProcess =  $("#lightbox-send-form");
		paso2.urlPasarela= '${urlPasarelaPago}';
		paso2.init();
	});
</script>











