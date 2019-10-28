<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<style>
.cssOcultarCompras{display:none;}
.miscanjes-mixto-paso2{
	width:100%;
}	
</style>
<!-- estado -->
<p class="separar"></p>
<section class="estado">
	<p class="barra"></p>
	<div class="limite">
	<div class="error"></div>
	<!-- flotante -->	
	<section class="flotante-secundario" id="flotante-secundario">	
	<div class="mis-canjespro">

		    <div class="cabec-canjespro">
		    	<p><p>
		    </div>
			<div class="mis-canjespro-cnt">
			     <p>Mis canjes</p>
			  <div class="zonaracional">
			      <div id="lista-complementos"> </div>			       
			   	  <div class="zonaracional-detalle">                     
                   <div class="zonaracional-puntos">
			   	  	   <ul>
			   	  	   	<li>Total en Millas Benefit</li>
			   	  	   	<li id="total-en-puntos">${formatImporteTotalPuntos}</li>
			   	  	   </ul>
			   	 	 </div>
			   	  </div>
			   </div>
			   </div>
			   <div class="blockdown-zonaracional">
			   	<c:if test="${clienteLogin}">
					<div class="check-alcance" style="display: none">
						<p><span class="icon-check"></span> Te alcanza con tus Millas Benefit</p>
			   			<p>Puntos disponibles tras el canje: <span class="puntos-disponibles-canje">${cliente.totalPuntos-importeTotalPuntos}</span></p>
			   		</div>

			   		<div class="miscanjes-mixto-paso2">			   		
				   		<p>
					   		<span class="color">Si usas</span>					   		
							<input type="text" onkeypress="return isNumber(event)" maxlength="9" id="importe-puntos-usar" class="puntos" value="0" />
							<span class="color">Millas Benefit</span><button id="btnActualizarConbinacion" class="btnActualizarConbinacion" type="button">Actualizar</button>
						</p>
				   		<p><span class="color">Te quedan por cubrir:</span>
				   			<span id="montoPorCubrir" class="blue precio-por-cubrir">${requestScope.simboloMonedaDolar} ${obtenerDetalle.precioDolares}</span></p>				   		
				   </div>
				    </c:if>
			   </div>
			   
			  
		</div>	
		
		
	</section>
		<p class="titulo-canje">Canje de producto</p>
		<ul class="estado-4pasos estado-operacion">
			<li class="paso-1 presente"><span>1</span>
			<p></p></li>
			<li class="paso-2"><span>2</span>
			<p></p></li>
			<li class="paso-3"><span>3</span>
			<p></p></li>
			<li class="paso-4 completo-off"><span></span>
			<p></p></li>
		</ul>
	</div>
</section>
<!-- /estado -->
<!-- informacion -->
<div class="limite acomodar-racional">

	<c:if test="${msgAlerta ne null}">
<!-- 	 <div style="background-color: #f2dede;border: 2px solid #ebccd1; height: 40px;position: relative;TEXT-ALIGN:CENTER;PADDING-TOP:20PX;COLOR:#a94442;margin-top:10px;width:68%"> -->
<%-- 			<c:out value="${msgAlerta}" escapeXml="false"></c:out> --%>
<!-- 		</div> -->
		<div class="cart-error-messages">
			<i class="material-icons">warning</i>
			<p>${msgAlerta}</p>
			<i class="material-icons close">close</i>
		</div>
	</c:if>
	<div class="opciones-detalle">
		<div class="btnVolver">
			<a href="javascript:window.history.back()" ><i class="material-icons">arrow_back</i>Regresar</a>
		</div>
	</div>
	<div style="display:none;" id="msgAlertSinStock" class=""> 
		<p></p>
	</div>
	
	<div id="detalles-producto" class="informacion">
		<form:form id="formCanjePaso1" action="${pageContext.request.contextPath}/canjeProducto/paso2" method="GET" autocomplete="off" commandName="canjeProductoForm">
			<c:set var="countNoComplemento" value="0" scope="page" />
			<c:forEach items="${listaItemCarrito}" var="item" varStatus="theCount">
<%-- 				<c:if test="${item.idCatalogoProductoComplemento==null || item.idCatalogoProductoComplemento==0}"> --%>
			  		<c:set var="countNoComplemento" value="${countNoComplemento + 1}" scope="page"/>
			  			<div class="producto-especifico" >
							<section class="producto" id="lista-producto-${theCount.count}" data-monto-soles="${item.catalogoProducto.formatPrecioCatalogo}">
								<div>
									<a href="javascript:;" rel="${item.catalogoProducto.idCatalogoProducto}" class="sacar-producto confirmar-sacar"></a>							
									<div class="descripcion">
										<div class="nombre">${item.catalogoProducto.titulo}</div>
										<div class="imagen">
										    <img onerror="this.src='${prop['url.imagen.producto']}/default/no-found-image.png'" src="${prop['url.imagen.producto']}${item.catalogoProducto.producto.imagen1}" width="177" alt="${item.catalogoProducto.titulo}" />
										</div>							
										<div class="precio">
											<div class="puntos">${item.catalogoProducto.formatPrecioPuntos} <span class="mb">Millas Benefit</span></div>
											<div class="soles"></div>
										</div>
									</div>
									<div style="display:none;" id="fueraStock" >
										<p>Este producto est&aacute; fuera de stock<p>
									    <a href="javascript:;" class="responsive confirmar-sacar quitarSinStock" rel="${item.catalogoProducto.idCatalogoProducto}">Eliminar</a>	
									</div>
									<div class="elemento-grupo" style="">
										<ul class="elemento">
											<li>Cantidad</li>
											<li>
											<c:set  var="stockMaximo" value="${item.stockDisponibleVisible}"></c:set>
											<c:if test="${stockMaximo > maximoStockVisible}">
												<c:set var="stockMaximo" value="${maximoStockVisible}" ></c:set>
											</c:if>	
											<!-- Artificio para soportar la vuelta desde paso 2 
											manteniendo la cantidad previa -->
											<c:if test="${item.cantidad > stockMaximo}">
												<c:set var="stockMaximo" value="${item.cantidad}" ></c:set>
											</c:if>	
																			
											<c:choose>
												<c:when test="${stockMaximo > 0}">
												<select name="cboSelectCantidad" class="cboSelectCantidad" code="${item.catalogoProducto.idCatalogoProducto}"> <!-- Agregar clase "borde-sin-stock" -->
													<c:forEach begin="1" end="${stockMaximo}"  var="i" >
														<option value="${i}"   <c:if test="${item.cantidad==i}">selected="selected"</c:if>><c:out value="${i}"/></option>
													</c:forEach>
												</select>	
												</c:when>		   	
											    <c:otherwise>
											       Sin stock
											    </c:otherwise>
											</c:choose>
												<span style="display:none;" id="faltoStok"></span>
											</li>
											<li>
												<a href="javascript:;" class="responsive confirmar-sacar" rel="${item.catalogoProducto.idCatalogoProducto}">Eliminar</a>
											    <a href="javascript:;" class="sacar" code="${item.catalogoProducto.idCatalogoProducto}">Eliminar</a>
		                						<div class="tooltip-sacar" style="display: none;" id="tooltip-sacar-${item.catalogoProducto.idCatalogoProducto}" code="${item.catalogoProducto.idCatalogoProducto}">
		                							<span class="ico-tooltip-sacar"></span>
		                							<div>
		                								<a href="javascript:;" class="ico-cerrar"></a>
		                								<p>Seguro deseas eliminar este producto de tus canjes?</p>
		                								<a href="javascript:;" class="boton confirmar-sacar">Eliminar</a>	
		                							</div>
		                						</div>
		                						<div id="iditem-${item.catalogoProducto.idCatalogoProducto}" data='{"id":"${item.catalogoProducto.idCatalogoProducto}","precio":"${item.catalogoProducto.precioCatalogo}","millas":"${item.catalogoProducto.precioPuntos}","nombre":"${item.catalogoProducto.nombre}"}' style="display: none;"></div>
											</li>
										</ul>
									</div>
								</div>
								<div class="clearfix"></div>
								<div style="display:none;" class="limiteCanjeExedido" >
								</div>
							</section>
						</div>			  

		</c:forEach>	
		</form:form>
					<div class="botonera">			
			<c:if test="${listaItemCarrito ne null}">
			    <a href="javascript:;" class="continuar" id="enviarPaso2">Siguiente</a>			    
				<div>
					<a href="${pageContext.request.contextPath}/categorias" class="texto" > Seguir comprando <span class="ico-seguir"></span></a> 
				</div>
			</c:if>			
			<c:if test="${listaItemCarrito eq null && stockDisponible ==0 }">
			 <a href="${pageContext.request.contextPath}/categorias" class="continuar">Volver</a>
			 </c:if>				
			</div>
	</div>
</div>
<div id="lightbox-procesando" class="lightbox" style="display:none"><p class="texto"><spring:message code="msg.espere.procesando"/></p><span class="cargador"></span></div>

<script id="template-stock-excedido" type="text/x-handlebars-template">
<div id="div-stock-excedido"  class="lightbox-mensaje lightbox" style="cursor: default;"> 
        <p class="texto">{{msg}}</p>       
	<div class="botonera">
		 {{#if stockDisponible}}
 		<a href="javascript:void(0)"  id="btn-actualizar-cantidad" class="canjear">Modificar</a> 
		{{/if}}
        <!--<a href="javascript:void(0)"  id="btn-sacar-mis-canjes" class="eliminar">Eliminar</a>-->
	</div>
</div> 
</script>

<script id="template-resumen-carrito" type="text/x-handlebars-template">
{{#items}}
<div class="zonaracional-preciouno">
	<div class="zonracional-preciouno-one">
		<p> {{formatCantidadCarrito cantidad}} {{titulo}}
		</p>
	</div>
	<div class="zonaracional-preciouno-two">
		<p>{{formatSubTotalPuntos}}</p>
	</div>
</div>
{{/items}}
</script>


<script type="text/javascript">
activarMenu("compras");
var htmlProcess =null;
$(document).ready(function() {
	htmlProcess = $("#lightbox-procesando");
	var puntosCliente = 0;
	var isLogin  = false;
	var triggerAutoPaso2 = false;
	if('${clienteLogin}'=='true'){
		puntosCliente = '${cliente.totalPuntos}';
		isLogin = true;
	}
	if('${triggerAutoPaso2}'=='true'){
		triggerAutoPaso2 = true;
	}
	var importePuntos = '${importeTotalPuntos}';

	
	var miCanje = new Canje();
	miCanje.totalImporte = parseFloat('${totalSoles}');
	miCanje.init(puntosCliente,importePuntos);
	miCanje.simboloMoneda = "${simboloMoneda}";
	miCanje.isLoginCliente = isLogin;

	
	
		
	$.each($(".producto-especifico .producto"),function(i,e){
		var formatoSoles = formatearDecimal("${simboloMoneda}",$(this).attr("data-monto-soles"),1);
		$(this).find(".soles").html("o "+formatoSoles);	
	});
	//
	
	if(triggerAutoPaso2){
		$("#enviarPaso2").trigger( "click" );	
	}
	
// 	if('${itemAgregadoCarrito}'=='true'){
// 		try{
// 			var ado = new AdobeAnalytic();
// 			ado.addItemCart('${cantidad}','${catalogoProducto.idCatalogoProducto}','${catalogoProducto.precioPuntos}','${catalogoProducto.precioCatalogo}');
// 		}catch(err){
// 			console.error(err);
// 		}
		
// 	}


});
</script>