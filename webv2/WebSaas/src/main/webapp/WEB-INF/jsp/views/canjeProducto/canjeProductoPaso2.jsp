<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script type="text/javascript">
var urlBaseWeb = '<c:url value="/" />';
setTimeout(function(){$('html,body').scrollTop(0);},100);
</script>
<style>
ul li{
	font-weight:normal;
	font-family: 'omnes_regular',arial;
	color: #686868;
}
</style>
<!-- estado -->
		<p class="separar"></p>
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
			      	<c:forEach items="${carritoCompra.detalles}" var="item" varStatus="cont">			      	
			      		<c:choose>
			      			<c:when test="${cont.count != 1}">
			      			 <div class="zonaracional-preciouno">
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
			                     <div class="zonracional-preciodos-one">
			                     	 <p><c:if test="${item.cantidad>1}">(<c:out value="${item.cantidad}"></c:out>)</c:if> ${item.catalogoProducto.titulo}</p>
			                     </div>
			                     <div class="zonaracional-preciodos-two">
			                     	 <p>${item.formatSubTotalImportePuntos}</p>
			                     </div>
						      </div>
			      			</c:otherwise>
			      		</c:choose>
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
<%-- 			   		<c:if test="${totalPuntosUsar <= cliente.totalPuntos}"> --%>
			   		<div class="check-alcance" style="display: none" >
			   			<p><span class="icon-check"></span> Te alcanza con tus Millas Benefit</p>
			   			<p>Puntos disponibles tras el canje: <span class="puntos-disponibles-canje">${cliente.totalPuntos-totalPuntosUsar}</span></p>
			   		</div>
<%-- 			   		</c:if> --%>
					<div class="miscanjes-mixto-paso2">
			   		
				   		<p>						   		
					   		<span class="color">Si usas</span>
					   		
							<input type="text" onkeypress="return isNumber(event)" maxlength="9" id="importe-puntos-usar" class="puntos" value="0" />
							<span class="color">Millas Benefit</span><button id="btnActualizarConbinacion" type="button">Actualizar</button>
						</p>
				   		<p><span class="color">Te quedan por cubrir:</span>
				   			<span id="montoPorCubrir" class="blue precio-por-cubrir">${requestScope.simboloMonedaDolar} ${obtenerDetalle.precioDolares}</span></p>
				   		
				   </div>
			   </div>
		</div>
	
	
	</section>
				
				<!-- /flotante -->

				<p class="titulo-canje">Canje de producto</p>
				<ul class="estado-4pasos estado-operacion">
					<li class="paso-1 pasado"><span>1</span><p></p></li>
					<li class="paso-2 presente"><span>2</span><p></p></li>
					<li class="paso-3"><span>3</span><p></p></li>
					<li class="paso-4 completo-off"><span></span><p></p></li>
				</ul>
			</div>
		</section>
		<!-- /estado -->
		
		
<!-- informacion -->
		<div class="limite acomodar-racional">
			<div id="datos-despacho" class="informacion">
				<div class="titulo-general">
					<h1 class="titulo">Datos de env&iacute;o</h1>
				</div>
				<div class="error">${error}</div>
				<p class="campos-obligatorios">Los campos marcados con * son obligatorios</p>
				<form:form id="frmDelivery" action="${pageContext.request.contextPath}/canjeProducto/delivery/guardar" method="POST" autocomplete="off">
				<input type="hidden" name="precioDelivery" value="0"/>
					<section>
						<article class="direccion-despacho">
							<div>
							<input type="hidden" name="idDireccionDelivery" value="${cliente.direccionDelivery.idDireccionDelivery}" />
								<div class="zona-direcciones cont-subtitulo">
									<div>
										<h2 class="subtitulo">Dirección de despacho</h2>
										<span class="" id="agregar-direccion"><i class="fa fa-plus"></i> Agregar direcci&oacute;n</span> <!-- Quitar clase ocultar-div -->
									</div>
									<div class="cont-elemento grupo-nueva-direct"> <!-- Quitar clase ocultar-div -->
										<select name="direccionesGuardadas" id="direccionesGuardadas">							
										</select>
										<p id="eliminar-direccion"><i class="fa fa-minus"></i> Eliminar esta direcci&oacute;n</p>
										<label for="nueva-direccion" class="nombreNuevaDireccion ocultar-div">
											<span>
												<span>Nombre de direcci&oacute;n:</span>
												<span class="cancel-nuevaDireccion">Cancelar</span>
											</span>
<!-- 											<input id="nombreDireccionPrimario" name="nombreDireccionPrimario" type="text" placeholder='Ejemplo: "Direcci&oacute;n de mam&aacute;"'/> -->
										</label>
									</div>
								</div>
								<ul class="elemento">
									<li>Departamento:</li>
									<li class="requerido">
										<select id="cbo_departamento" title="Campo requerido." required="required" name="departamento"></select>
									</li>
								</ul>
								<ul class="elemento">
									<li>Provincia:</li>
									<li class="requerido">
										<select id="cbo_provincia" title="Campo requerido." required="required" name="provincia"></select>
									</li>
								</ul>
								<ul class="elemento">
									<li>Distrito:</li>
									<li class="requerido">
										<select id="cbo_distrito" title="Campo requerido." required="required" name="distrito"></select>
									</li>
								</ul>
								<ul class="elemento">
									<li>Urbanización:</li>
									<li><input type="text" name="urbanizacion" maxlength="100" title="Campo requerido." placeholder="" value="${cliente.direccionDelivery.urbanizacion}" /></li>
								</ul>
								<ul class="elemento">
									<li>Tipo de vía:</li>
<!-- 									<li class="requerido"> -->
<!-- 										<select name="tipoCalle" title="Campo requerido." required="required"> -->
<!-- 											<option value=""> - Seleccionar - </option> -->
<%-- 											<c:forEach var="item" items="${listaTipoCalle}"> --%>
<%-- 												<option value="${item}" <c:if test="${item == cliente.direccionDelivery.tipoDireccion}">selected</c:if>>${item}</option> --%>
<%-- 											</c:forEach> --%>
<!-- 										</select> -->
<!-- 									</li> -->
									<li class="requerido">									
									<input type="hidden" id="tipoCalle" value="" name="tipoCalle" />
										<ul class="option-group grupo-tipo-via">
											<c:forEach var="item" items="${listaTipoCalle}">
												<li class="option-item <c:if test="${item == cliente.direccionDelivery.tipoDireccion}">activo</c:if>" rel="${item}">${item}</li>
											</c:forEach>
										</ul>
									</li>
								</ul>
								<ul class="elemento">
									<li>Direcci&oacute;n:</li>
									<li class="requerido"><input type="text" required="required" name="nombreVia" maxlength="100" placeholder="" value="${cliente.direccionDelivery.direccion}" title="Campo requerido." class="capitalize"/></li>
								</ul>
								<div class="numero-interior">
									<ul class="elemento numero">
										<li>N&uacute;mero:</li>
										<li class="requerido"><input type="text" required="required" name="numero" maxlength="25" placeholder="" value="${cliente.direccionDelivery.direccionNumero}" title="Campo requerido."/></li>
									</ul>
									<ul class="elemento interior">
										<li>Interior:</li>
										<li><input type="text" name="interior" maxlength="25" placeholder="" value="${cliente.direccionDelivery.direccionInterior}" /></li>
									</ul>
								</div>
								<div class="lote-manzana">
									<c:choose>
										<c:when test="${manzanaLote eq true}">								
											<label><input type="checkbox" name="chk_lote_manzana" value="1" id="chk_lote_manzana" checked="true" />Indicar lote y manzana</label>
											<div class="elemento-grupo" style="display:block">
										</c:when>
										
										<c:otherwise>
										
											<label><input type="checkbox" name="chk_lote_manzana" value="1" id="chk_lote_manzana">Indicar lote y manzana</label>
											<div class="elemento-grupo" style="display:none">
										
										</c:otherwise>
									</c:choose>
<!-- 									<label><input type="checkbox" name="chk_lote_manzana" value="1" id="chk_lote_manzana">Indicar lote y manzana</label> -->
<!-- 									<div class="elemento-grupo" style="display:none"> -->
										
										
										<ul class="elemento numero">
											<li>Lote:</li>
											<li><input type="text" id="lote" name="lote" placeholder="" value="${cliente.direccionDelivery.direccionLote}" /></li>
										</ul>
										<ul class="elemento interior">
											<li>Manzana:</li>
											<li><input type="text" id="manzana" name="manzana" placeholder="" value="${cliente.direccionDelivery.direccionManzana}" /></li>
										</ul>
<!-- 									</div> -->
								</div>
								<ul class="elemento">
									<li>Referencia:</li>
									<li><input type="text" maxlength="100" name="referencia" id="referencia" placeholder="" value="${cliente.direccionDelivery.referenciaDireccion}" /></li>
								</ul>
								
							</div>
						</article>
						<article class="datos-contacto">
							<div>
								<h2 class="subtitulo">Datos de contacto</h2>
								<p>Persona que recibe el producto:</p>
								<div class="cont-elemento">
									<ul class="elemento">
										<li>
										<input type="hidden" id="tipoDestinatario" value="" name="tipoDestinatario" />
											<ul class="grupo-contacto option-group ocultar-div">
												<li id="titular" rel="1" class="option-item">Titular de la cuenta</li>
												<li id="otro" rel="2" class="option-item">Otra persona</li>
											</ul>
										</li>
									</ul>
									<div class="opcion-contacto">
										<h4 id="labelTitular">Titular de la cuenta:</h4>
										<p id="agregar-persona"><i class="fa fa-user-plus"></i> Agregar otra persona</p>
									</div>							
									<div class="grupo-contacto-titular">								
										<ul class="elemento">
											<li id="docTitularLabel">Documento de identidad:</li>
											<li class="bloque requerido">
												<select id="tipoDocumentoBoleta" name="tipoDocumentoBoleta" class="enableValidator">
													<c:forEach items="${enumTipoDocumento}" var="lista">
														<option value="${lista.codigo}">${lista.texto}</option>
													</c:forEach>
												</select>
												<input type="text" title="Campo requerido" name="nroDocumentoBoleta" id="nroDocumentoBoleta"/>
											</li>
										</ul>
										<ul class="elemento">
											<li id="telefonoTitularLabel">Tel&eacute;fono:</li>
											<li class="bloqueo requerido">
												<input type="text" title="Campo requerido." required="required" name="telefonoTitular" id="telefonoTitular" placeholder="" value="${cliente.direccionDelivery.telefono}" maxlength="12" />
											</li>
										</ul>
										<ul class="elemento">
											<li id="emailTitularLabel">E-mail:</li>
											<li class="bloqueo requerido">
												<input type="text" maxlength="80" title="Campo requerido." required="required" name="emailTitular" id="emailTitular" placeholder="" value="${cliente.direccionDelivery.email}" />
											</li>
										</ul>
									</div>
									<div class="grupo-cantacto-otro" style="display:none">
										<ul class="elemento">
											<li id="nombreComplentoLabel">Nombre completo:</li>
											<li class="bloqueo requerido">
												<input type="text" maxlength="160" id="contactoNombre" required="required" name="contactoNombre" placeholder="" value="${cliente.direccionDelivery.nombreContacto}" class="capitalize" />
											</li>
										</ul>
										<ul class="elemento">
											<li id="telefonoLabel">Tel&eacute;fono:</li>
											<li class="bloqueo requerido">
												<input type="text" title="Campo requerido." required="required" name="contactoTelefono" id="contactoTelefono" placeholder="" value="${cliente.direccionDelivery.telefonoContacto}" maxlength="12" />
											</li>
										</ul>
<!-- 										<ul class="elemento"> -->
<!-- 											<li id="emailLabel">E-mail:</li> -->
<!-- 											<li class="bloqueo requerido"> -->
<%-- 												<input type="text" maxlength="80" title="Campo requerido." required="required" name="contactoEmail" id="contactoEmail" placeholder="" value="${cliente.direccionDelivery.emailContacto}" /> --%>
<!-- 											</li> -->
<!-- 										</ul> -->
									</div>
								</div>
							</div>
						</article>
						<article class="plazo-entrega">
							<div>
								<h2 class="subtitulo">Plazo de entrega</h2>
								<ul class="elemento">
									<li>Tipo de entrega:</li>
									<li>
									<input type="hidden" id="inputTipoEntrega" value="" name="tipoEntrega" />
										<ul class="grupo-tipoentrega option-group" id="tipoEntrega" item-select="${cliente.direccionDelivery.tipoEntrega}">
										</ul>
										<p class="nota" id="costoDelivery"></p><br />
										<p class="nota" id="diasDelivery"></p>
									</li>
								</ul>
<!-- 								<p>Tiempo estimado de entrega: 3 d&iacute;as h&aacute;biles</p> -->
							</div>
						</article>
						<div class="clear"></div>
<!-- 						<article class="guardar-direccion ocultar-div"> -->
<!-- 							<label> -->
<!-- 								<input id="check-nueva-direc" type="checkbox" value="1"/> -->
<!-- 								<span>Guardar esta direcci&oacute;n para usarla de nuevo</span> -->
<!-- 							</label> -->
<!-- 							<label for="direccionPorGuardar" class="ocultar-div"> -->
<!-- 								<span>Nombre de la direcci&oacute;n:</span> -->
<!-- 								<input id="nombreDireccionSecundario" name="nombreDireccionSecundario" type="text" placeholder='Ejemplo: "Direcci&oacute;n de mam&aacute;"'/> -->
<!-- 							</label> -->
<!-- 						</article> -->
					</section>
					<div class="botonera">
						<a  class="unselectable" href="${pageContext.request.contextPath}/canjeProducto/pendientes">Volver</a>
<!-- 						<input type="submit" value="Siguiente" id="guardarDelivery" style="display:none"> -->
						<input type="button" value="Siguiente" id="mostrarConfirmacion">
					</div>
				</form:form>
				
			</div>
		</div>
<div id="lightbox-procesando" class="lightbox" style="display:none"><p class="texto"><spring:message code="msg.espere.procesando" htmlEscape="false"/></p><span class="cargador"></span></div>
<jsp:include page="../modalMensajes.jsp" />
<!-- /informacion -->
<script type="text/javascript">

activarMenu("compras");

var separador = '<c:out value="${separador}"/>';

var dirDepartamento = '${cliente.direccionDelivery.departamento}';
var dirCiudad = '${cliente.direccionDelivery.ciudad}';
var dirDistrito = '${cliente.direccionDelivery.distrito}';


var codigoPaisDefault ='${codigoPaisDefault}';
var codigoNivelDepartamento = '${codigoNivelDepartamento}';
var codigoNivelProvincia = '${codigoNivelProvincia}';
var codigoNivelDistrito = '${codigoNivelDistrito}';

var htmlProcess =null;
$(document).ready(function() {
	
	htmlProcess = $("#lightbox-procesando");
	var puntosCliente = '${cliente.totalPuntos}';
	var totalPuntos = '${totalPuntos}';
	var todosExpress = '${todosExpress}';
	
	if(todosExpress=='true'){
		todosExpress = true;
	}else{
		todosExpress = false;
	}
	
	var myDelivery = new Delivery();
	
	myDelivery.nombreContacto =  '${cliente.direccionDelivery.nombreContacto}';
	
	if(('${cliente.direccionDelivery.email}').length == 0){
		myDelivery.emailCliente =  '${cliente.email}';
	}else{
		myDelivery.emailCliente =  '${cliente.direccionDelivery.email}';
	}
	myDelivery.emailClienteSession = '${cliente.email}';
	myDelivery.todosSonExpress =  todosExpress;
	myDelivery.simboloMoneda = '${simboloMoneda}';
	myDelivery.totalImportePuntos="${totalPuntosUsar}";
	myDelivery.totalPuntosCliente="${cliente.totalPuntos}";
	myDelivery.totalImporte = parseFloat('${totalSoles}');	
	myDelivery.nombreDireccionActual =  '<c:out value="${cliente.direccionDelivery.nombreDireccion}" escapeXml="true"></c:out>';
	myDelivery.dirManzanaLote = '${manzanaLote}';
	
// 	myDelivery.init(puntosCliente,totalPuntos,'${listaItemsCarrito}');
	myDelivery.init(puntosCliente,totalPuntos);
	myDelivery.calcularPuntosDisponibles();
	myDelivery.loadDepartamento();
	
	try{
		var ado = new AdobeAnalytic();
		ado.checkoutCart('${listaItemsCarrito}');

		
	}catch(err){
		console.error(err);
	}

	
});

</script>