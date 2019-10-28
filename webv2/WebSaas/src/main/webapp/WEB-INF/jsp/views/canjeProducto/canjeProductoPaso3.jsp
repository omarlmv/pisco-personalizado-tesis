<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style>
.cssOcultarCompras{display:none;}

body{
	background: #f2f1f1;
}

</style>
<!-- estado -->
<section class="estado">
	<p class="barra"></p>
	<div class="limite">
		<!-- flotante -->
				<section class="flotante-secundario">
	
		<div class="mis-canjespro">
		    <div class="cabec-canjespro">
		    	<p><p>
		    </div>
			<div class="mis-canjespro-cnt">
			     <p>Mis canjes</p> 
			  <div class="zonaracional">
			      <div id="lista-complementos">
<%-- 			      	<c:set var="totalSoles" value="0" />  --%>
<%-- 			      	<c:set var="totalPuntosUsar" value="0" /> --%>
			      	<c:forEach items="${listaProductos}" var="item" varStatus="cont">
			      	
			      		<c:choose>
			      			<c:when test="${cont.count eq 1}">
			      			 <div class="zonaracional-preciouno">
			                     <div class="zonracional-preciodos-one">
			                     	 <p><c:if test="${item.cantidad>1}">(<c:out value="${item.cantidad}"></c:out>)</c:if> ${item.catalogoProducto.titulo}</p>
			                     </div>
			                     <div class="zonaracional-preciodos-two">
			                     	 <p>${item.formatSubTotalImportePuntos}</p>
			                     </div>
						      </div>
			      			</c:when>
			      			<c:when test="${item.catalogoProducto.idCatalogoProducto eq -1}">
			      			 <div class="zonaracional-preciodos">
			                     <div class="zonracional-preciouno-one">
			                     	 <p><c:if test="${item.cantidad>1}">(<c:out value="${item.cantidad}"></c:out>)</c:if> ${item.catalogoProducto.titulo}</p>
			                     </div>
			                     <div class="zonaracional-preciouno-two">
			                     	<p>${item.formatSubTotalImportePuntos}</p>
			                     </div>
						      </div>
			      			</c:when>
			      			<c:otherwise>
	 							<div class="zonaracional-preciouno">
			                     <div class="zonracional-preciouno-one">
			                     	 <p><c:if test="${item.cantidad>1}">(<c:out value="${item.cantidad}"></c:out>)</c:if> ${item.catalogoProducto.titulo}</p>
			                     </div>
			                     <div class="zonaracional-preciouno-two">
			                     	<p>${item.formatSubTotalImportePuntos}</p>
			                     </div>
						      </div>
			      			</c:otherwise>
			      		</c:choose>
<%-- 			      		<c:set var="totalSoles" value="${totalSoles+(item.catalogoProducto.precioCatalogo*item.cantidad)}" /> --%>
<%-- 			      		<c:set var="totalPuntosUsar" value="${totalPuntosUsar+(item.catalogoProducto.precioPuntos*item.cantidad)}" /> --%>
			      		<c:set var="conteo" value="${conteo+1}" />
			      	</c:forEach>
			      </div>
			       
			   	  <div class="zonaracional-detalle">
                     
<!--                      <div class="extra-producto"> -->

<!--                      	<p class="extra-productouno">Extra entrega express</p> -->
<!--                      	<p class="extra-productodos">100</p> -->

<!--                      </div> -->
                     
			   	     <div class="zonaracional-puntos">
			   	  	   <ul>
			   	  	   	<li>Total en Millas Benefit</li>
			   	  	   	<li id="total-en-puntos">${formatTotalPuntos}</li>
			   	  	   </ul>
			   	 	 </div>
			   	  </div>

			   </div>
			   </div>
			   <div class="blockdown-zonaracional">
			   		
			   		<c:if test="${totalPuntosUsar <= cliente.totalPuntos}">
<!-- 			   		<div class="check-alcance" > -->
<!-- 			   			<p><span class="icon-ico_check2"></span> Te alcanza con tus Millas Benefit</p> -->
<%-- 			   			<p>Puntos disponibles tras el canje: <span class="puntos-disponibles-canje" >${cliente.totalPuntos-totalPuntos}</span></p> --%>
<!-- 			   		</div> -->
			   		</c:if>
			   		
					<div class="miscanjes-mixto-paso2">
			   		<ul class="puntos-efectivo">
			   			<li class="pe-left green">Millas Benefit por canjear:</li>
			   			<li class="pe-right green canje-puntos-cubrir">0</li>
			   			<li class="pe-left steel">Total a pagar:</li>
			   			<li class="pe-right steel"><span>${simboloMoneda}</span> <span class="canje-precio-cubrir-efectivo">${formatMontoPorCubrir}</span></li>
			   		</ul>
		   		
				   </div>
			   </div>
		</div>
	
	
	</section>
				<!-- /flotante -->


		<p class="titulo-canje">Canje de producto</p>
		<ul class="estado-4pasos estado-operacion">
			<li class="paso-1 pasado"><span>1</span>
				<p></p></li>
			<li class="paso-2 pasado"><span>2</span>
				<p></p></li>
			<li class="paso-3 presente"><span>3</span>
				<p></p></li>
			<li class="paso-4 completo-off"><span></span>
				<p></p></li>
		</ul>
	</div>
</section>
<!-- /estado -->
<!-- informacion -->
<div class="limite acomodar-racional medioCanjeProductos">
	
	<div class="tit-mediocanje-pas3">
		<p>Medio de canje</p>
	</div>
		  	
	<div id="medio-canje" class="informacion">
		<form:form id="formCanjePaso3" cssClass="canjeP3-form" action="${pageContext.request.contextPath}/canjeProducto/finalizar" method="POST" autocomplete="off" commandName="canjeProductoForm">
			<div id="valoresVenta">
			</div>
			<div class="canjeP3-box">
				<div class="canjeP3-wancho">
					<div class="canjeP3-left">
						<span class="canjeP3-h4 cP3-h4-hide">
							<c:if test="${fn:length(listaProductos) == 1}">
  									<h4>Producto:</h4>
							</c:if>
							<c:if test="${fn:length(listaProductos) > 1}">
  									<h4>Productos:</h4>
							</c:if>
						</span>
						<div class="canjeP3-listado">
						 	<c:forEach items="${listaProductos}" var="item" varStatus="cont">
								<p><span></span> ${item.cantidad} ${item.catalogoProducto.titulo}<c:if test="${!cont.last}">,</c:if></p>
						 	</c:forEach>

											 	
						</div>
					</div>
					<div class="canjeP3-right">
						<p>Datos de env&iacute;o:<br>
						<c:if test="${not empty delivery.nombreDireccion}">	
						   <span style="font-style: italic;">${delivery.nombreDireccion} (guardada)</span><br> 
						</c:if>
						 <span class="capitalize">${delivery.direccion}</span> ${not empty delivery.direccionNumero? ' N°':''}${delivery.direccionNumero} ${not empty delivery.direccionInterior? ' Int ':''}${delivery.direccionInterior}${not empty delivery.direccionLote? ' Lt ':''}${delivery.direccionLote}${not empty delivery.direccionManzana? ' Mz ':''}${delivery.direccionManzana},
															 ${delivery.distrito}, ${delivery.ciudad}, ${delivery.departamento}</p> 
						<p>Tu pedido llegará hasta el ${diaEntrega}</p>
						<p>Recibe el producto: <span class="capitalize">${nombreContacto}</span></p>
					</div>
				</div>
			</div>
			<div class="canjeP3-box color-celeste">
				<div class="canjeP3-wancho sin-wancho">
					<div class="canjeP3-left">
						<p>Mi total en Millas Benefit:</p>
					</div>
					<div class="canjeP3-right">
					<c:choose>
						<c:when test="${sessionScope.sessionCliente.formatTotalPuntos ne null }"><p>${sessionScope.sessionCliente.formatTotalPuntos}</p></c:when>
						<c:otherwise><p>0</p></c:otherwise>
					</c:choose>
					</div>
					<div class="canjeP3-left">
						<p>Valor del producto en Millas Benefit:</p>
					</div>
					<div class="canjeP3-right">
						<p>${formatTotalPuntos}</p>
					</div>
					
					<div class="separador"></div>
					
					<div class="canjeP3-left montoTotal-aplicate">
						<p>Monto total:</p>
					</div>
					<div class="canjeP3-right cnt-cupon montoTotal-aplicate">
						<p>${simboloMoneda} ${formatTotalPrecio}</p>
						<c:if test="${listaCupones ne null}">
						<span id="show-cupon" class="btn-cupon">&iexcl;Tienes cupones disponibles!</span>
						</c:if>
						
					</div>
					<div id="resumen-cupon-dscto" >
						<div class="canjeP3-left dscto-cupon">
							<p>Monto con cup&oacute;n:</p>
						</div>
						<div id="monto-con-cupon" class="canjeP3-right dscto-cupon">
						</div>
						
						<div class="canjeP3-left dscto-cupon rojo" style="vertical-align: top !important;">
							<p>Descuento por cup&oacute;n:</p>
						</div>
						<div id="dscto-aplicado" class="canjeP3-right dscto-cupon rojo">
						</div>
						<div class="canjeP3-left dscto-cupon dscto-cupon montoSinCupon-aplicate">
							<p>Monto sin cup&oacute;n:</p>
						</div>
						<div id="monto-sin-cupon" class="canjeP3-right dscto-cupon montoSinCupon-aplicate">
						</div>
						<div class="canjeP3-left dscto-cupon delivery-aplicate">
							<p>Comisi&oacute;n por delivery:</p>
						</div>
						<div id="comision-delivery" class="canjeP3-right dscto-cupon delivery-aplicate">
						</div>
						
						<div class="canjeP3-left dscto-cupon">
							<p><strong>Nuevo monto total:</strong></p>
						</div>
						<div id="nuevo-monto-total" class="canjeP3-right cnt-cupon dscto-cupon">
						
					</div>
					
					</div>
					<div class="cupon-detail">
						<div class="separador"></div>
						<div class="canjeP3-left line-cupon">
							<p>Agregar cup&oacute;n:</p>
						</div>
						<div class="canjeP3-right cnt-cupon contenedor-cupon">
							<div>
								<form:select class="list-cupones" path="cboCupones" id="cboCupones">
								 <form:option  value="0">Seleccionar cup&oacute;n</form:option>
								  <c:forEach items="${listaCupones}" var="cupon">
								  <c:if test="${cupon.tipoCupon==1}">
								  <form:option value="${cupon.codigo}">${cupon.label} (${cupon.simboloTipo}${cupon.monto}) </form:option>
								  </c:if>
								   <c:if test="${cupon.tipoCupon==2}">
								     <form:option value="${cupon.codigo}">${cupon.label} (${cupon.monto}${cupon.simboloTipo}) </form:option>
								   </c:if>
								  </c:forEach>
									
								</form:select>
								
								<div class="cupon-selected"></div>
								<span id="apply-cupon" class="btn-cupon">Aplicar</span>
								<span id="cancel-cupon">Cancelar</span>
								<span class="icon-check" id="check-cupon"><a id="cambiar-cupon" href="#">Cambiar</a>&nbsp;<a id="quitar-cupon" href="#">Quitar</a></span>
								<span class="error-cupon"></span>
								<span class="load-cupon"><img src="${prop['config.url.recursos.base.web']}static/images/pre-carga.gif"/>Validando tu c&oacute;digo...</span>
							</div>
						</div>
						
					</div>
				</div>
			</div>
			
				

<!-- 			<section> -->
<!-- 				<div class="valor"> -->
<!-- 					<ul class="elemento-horizontal"> -->
<!-- 						<li>Valor del producto:</li> -->
<%-- 						<li><span class="canje-precio">${simboloMoneda}  ${formatTotalPrecio}</span> ó <span class="canje-puntos">${formatTotalPuntos} </span>Millas Benefit <sup>&reg;</sup></li> --%>
						

<!-- 					</ul> -->
<!-- 				</div>	 -->
<!-- 			</section> -->
			
			<section class="valor-diferencia">
				<c:if test="${cliente.totalPuntos > 0}" > 
				<div class="valor canjeP3-box">
					<div class="puntos-usar canjeP3-wancho sin-wancho">
						<div class="canjeP3-left">¿Cu&aacute;ntas de tus Millas Benefit quieres usar?: </div>
						<div class="canjeP3-right">
							<div class="actualizar" style="padding: 0; margin: 0;">
								<form:input required="required" type="text" value="" path="puntosUsados" class="puntos" title="Campo requerido." />
								
								<a href="javascript:void(0)" id="btnActualizar">Actualizar</a>
							</div>
						</div>
						
						
						<div class="canjeP3-left"></div>
						<div class="canjeP3-right p3barra">
							<input id="num-range" type="text" name="num-range" style="display:block !important;visibility:hidden;"/>
						</div>
					</div>
					
					<div class="canjeP3-box p3Bold">
						<div class="canjeP3-wancho sin-wancho">
							<div class="canjeP3-left" style="vertical-align:middle;">
								<p>Diferencia por cubrir:</p>
							</div>
							<div class="canjeP3-right" style="vertical-align:middle;">
								<p><span class="canje-precio-cubrir">${simboloMoneda}</span> <span id="li-diferencia-cubrir"  class="canje-precio-cubrir-efectivo canje-precio-cubrir">${formatMontoPorCubrir}</span></p>
							</div>
						</div>
					</div>
					
				</div>
				

				
				
				</c:if>
				
				<c:set var="cssDisplayBoleta" value="none"></c:set>
			
					    <c:if test="${displayFormBoleta}">
					      <c:set var="cssDisplayBoleta" value="block"></c:set>
					   </c:if>
				
				
				<div class="elemento-horizontal diferencia datos-boleta-texto hidden" style="display:${displayFormBoleta}" id="section-form-boleta1"> <!-- section-form-boleta --> 
				</div>
				
				<div class="botonera">
					<a href="javascript:history.back(1)" class="volver">Volver</a> 
					<input type="submit" value="Siguiente" class="continuar" />
				</div>
			</section>			
		</form:form>
	</div>
	
	<div class="informacion">
			<div class="medio-canje off" id="content-iframe-medio-canje">
			  <div id="loadIframePasarela"></div>
			  <iframe id="iframe-medio-canje" width="720" onload="ocultarLoad()">
			  
			  </iframe>
			</div>
	</div>
	
	
</div>
<div id="lightbox-send-form" class="lightbox" style="display:none"><p class="texto"><spring:message code="msg.espere.procesando.canje" htmlEscape="false"/></p><span class="cargador"></span></div>
<div id="lightbox-procesando" class="lightbox" style="display:none"><p class="texto"><spring:message code="msg.espere.procesando" htmlEscape="false"/></p><span class="cargador"></span></div>
<script id="template-stock-excedido" type="text/x-handlebars-template">

<div id="div-stock-excedido"  class="lightbox-mensaje lightbox" style="cursor: default;"> 
        <p class="texto">{{msg}}</p>       
	<div class="botonera">
		 {{#if stockDisponible}}
 		<a href="javascript:void(0)"  id="btn-actualizar-cantidad" data="{{idItem}}|{{stockDisponible}}" class="canjear">Continuar</a> 
		{{/if}}
        <a href="javascript:void(0)"  id="btn-sacar-mis-canjes" class="eliminar">Eliminar</a>
	</div>
</div> 
</script>

<script id="template-cupon-error" type="text/x-handlebars-template">



<div id="lightbox-falta-datos" class="" style="display:block;">
	<div class="general-modal">
		<div class="close-modal"></div>
		<div class="message">
			<p>Aquí va el mensaje</p>
		</div>
		<div class="buttons">
			<input class="btn-success" type="button" value="Aceptar"/>
		</div>
	</div>
</div>
</script>

<!-- /informacion -->
<script type="text/javascript">
activarMenu("compras");

var htmlProcess =null;
var htmlFinalizaProcess = null;
$(document).ready(function(){
	var totalPuntosCliente ='${cliente.totalPuntos}';
	var totalImportePuntos = '${totalPuntosUsar}';
	var simboloMoneda = '${simboloMoneda}'; 
	$('.alineacion').find('a').removeClass('activo');
	
	htmlProcess = $("#lightbox-procesando");
	htmlFinalizaProcess =  $("#lightbox-send-form");
	
	var p = new CotizacionPuntos();
	p.totalImporte = parseFloat('${totalPrecio}');
	p.diferenciaXCubrir =parseFloat('${montoPorCubrir}');
	p.importeMaximoCompraSinBoleta = parseInt('${maximoCompraSinBoleta}',10);
	p.urlPasarela='${urlPasarelaPago}';
	
	p.totalImportePuntos="${totalPuntosUsar}";
	p.totalPuntosCliente="${cliente.totalPuntos}";
	
	$('.canjeP3-h4').clone().appendTo('.canjeP3-listado p:first-child span').removeClass('cP3-h4-hide');
		
	p.init(totalPuntosCliente,totalImportePuntos, simboloMoneda);
	
	
	var myDelivery = new Delivery();
	
// 	myDelivery.init(totalPuntosCliente,totalImportePuntos,'${listaItemsCarrito}');
	myDelivery.init(totalPuntosCliente,totalImportePuntos);
	myDelivery.calcularPuntosDisponibles();
	
	
	try{
		var ado = new AdobeAnalytic();
		ado.checkoutMedioCanje('${listaItemsCarrito}');
	}catch(err){
		console.error(err);
	}
})


</script>

<jsp:include page="../modalMensajes.jsp" />