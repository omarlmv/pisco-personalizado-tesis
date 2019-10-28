<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div id="vales-paso1">
	<div class="separar"></div>
	<div id="pasos">
		<div class="container">
			<p>Canje de vale digital</p>
			<ul class="cart-fill">
				<li class="paso-activo"><span>1</span></li>
				<li><span>2</span></li>
				<li><span class="icon-check"></span></li>
			</ul>
			<div class="resumen-paso1">
				<div class="lista-canjes">
					<h4>Mis canjes</h4>
					<ul class="lista-items"></ul>
					<ul class="resumen"></ul>
				</div>
				<div class="cont-resumen">
					<div class="puntos-disponibles check-alcance-ev" style="display: none">
						<i class="icon-check"></i>
						<p>Te alcanza con tus Millas Benefit<br>Puntos disponibles tras el canje: <span class="puntos-disponibles-canje"></span></p>
					</div>
					<div class="uso-millas">
						<label>Si usas <input type="text" value="0" id="importe-puntos-usar" onkeypress="return isNumber(event)" maxlength="9"/> Millas Benefit <input type="button" value="Actualizar" id="btnActualizarConbinacion"/></label>
						<p>Te quedan por cubrir: <span id="montoPorCubrir">0</span></p>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<!-- con datos -->
	<div class="back-option container">
		<a href="${evalesBackHistory}"><i class="material-icons">arrow_back</i>Regresar</a>
	</div>
	
	<div class="container cart-fill">
		<div class="info-paso1">
		<c:if test="${evaleInSegmento eq false}">
			<div class="vales-low-stock ">
				<p><c:out  value="${evaleNoSegmento}" escapeXml="false"/></p>
			</div>
			</c:if>
			<div class="vales-low-stock hidden">
				<p>&iexcl;Lo sentimos! Mientras esperabas, algunos vales digitales dejaron de estar disponbiles</p>
			</div>
			
			
			<section id="vales-por-canjear"></section>
			<div class="actions">
				<a id="siguiente" href="javascript:;" class="btn-verde">Siguiente</a>
				<a id="go-vales" href="${pageContext.request.contextPath}/vales-digitales">Seguir comprando <span class="fa fa-angle-right"></span></a>
			</div>
		</div>
	</div>
	<!-- sin datos -->
	<div class="container cart-empty" style="display: none;">
		<div class="info-paso1">
			<div class="sin-canje-pendiente">
			 <c:choose>
         
		         <c:when test = "${evaleInSegmento eq false}">
		           <p><c:out  value="${evaleNoSegmento}" escapeXml="false"/></p>
		         </c:when>
		          <c:otherwise>
		           <p>Usted no tiene ning&uacute;n vale digital pendiente</p>
		         </c:otherwise>
		      </c:choose>
				
				<div>
					<a class="btn-verde" href="${pageContext.request.contextPath}/vales-digitales">Volver a vales digitales</a>
				</div>
			</div>
		</div>
		
	</div>
	
</div>

<div id="modal-envio-vales" class="background-modal" data-close="false">
	<form:form id="form-envio-vales" class="contenedor-modal" autocomplete="off" method="post" action="${pageContext.request.contextPath}/vales-digitales/carrito/datosEnvio/agregar">
		<span class="close material-icons">clear</span>
		<h2>Enviar vales digitales</h2>
		<p>Ind&iacutecanos si el vale digital es de regalo y agrega los datos de la persona que lo recibir&aacute; por correo electr&oacute;nico.</p>
		<section id="vales-regalo"></section>
		<div class="zone-button"><button type="button" id="guardar-vale" class="btn-verde" style="display: none;">Guardar</button></div>
		
	</form:form>
</div>
<script id="template-vales-por-canjear" type="text/x-handlebars-template">
			<article id="{{evales.keyItem}}">
				<div>
					<div class="cnt-image"><img alt="Imagen de vale" src="${prop['url.imagen.evales']}{{evales.imagenFormat}}"/></div>
					<div>
						<h2>Vale Digital de  ${simboloMoneda}{{evales.valorNominalFormat}}</h2>
						<p>por {{evales.precioPuntosFormat}} Millas Benefit</p>
						<p>o ${simboloMoneda}{{evales.precioCatalogoFormat}}</p>
						<button type="button" class="enviar-regalo"  idkey="{{evales.keyItem}}" id="btn-enviar-{{evales.keyItem}}">&iquest;Enviar como regalo?</button><a href="#"></a>

					</div>
				</div>
				<div>
					<p>Cantidad</p>
					<div class="contador-agotado hidden">Agotado</div>
					<div class="contador"  idkey="{{evales.keyItem}}">
						<span class="remove-cant material-icons {{#ifconditional cantidad value = 1}}disabled{{/ifconditional}}
						{{#ifmayor cantidad value = 1}}active{{/ifmayor}}">remove</span>
						<span class="select-cantidad" max-stock="{{evales.stock}}">
							<span>{{cantidad}}</span>
							<i class="icon-down"></i>
							<ul class="dropdown-number">
								{{listaStockEvales evales.stock}}
							</ul>

						</span>
						<span class="add-cant material-icons  {{#ifmayor evales.stock value = cantidad}}active{{/ifmayor}} 
{{#ifconditional  evales.stock  value = cantidad }}disabled{{/ifconditional}} 
">add</span>
					</div>
					<div class="warning bajo-stock hidden">Solo quedan 2 unidades</div>
					<div class="warning sin-stock hidden">Este vale digital esta fuera de stock</div>
					<div class="drop-zone">
						<button type="button"  class="delete-item-ev">Eliminar</button>
						<div class="drop-modal">
							<div class="material-icons">clear</div>
							<p>&iquest;Seguro que deseas eliminar este vale digital?</p>
							<button idkey="{{evales.keyItem}}">Eliminar</button>
						</div>
					</div>
				</div>
			</article>
		</script>
<script id="template-modal-vale" type="text/x-handlebars-template">
			<article>
				<div class="detalle-vale">
					<div class="cnt-image"><img alt="Imagen de vale" src="${prop['url.imagen.evales']}{{evale.imagenFormat}}"/></div>
					<div>
						<h4>{{evale.titulo}}</h4>
						<p>por {{evale.precioPuntosFormat}} Millas Benefit</p>
						<p>o ${simboloMoneda}{{evale.precioCatalogoFormat}}</p>
						<div class="opciones-envio">
							<label class="radio-label">
								<input class="personal" type="radio" {{#if datosEnvio.esRegalo}}{{else}} checked="checked" {{/if}}  name="tipo-envio{{index}}" value="false" index="{{index}}"/>
								<span class="radio-input">
									<i class="{{#if datosEnvio.esRegalo}}hidden{{/if}}"></i>
								</span>
								<span>Para m&iacute;</span>
							</label>
							<label class="radio-label">
								<input class="regalo" type="radio" {{#if datosEnvio.esRegalo}} checked="checked" {{else}}{{/if}}" name="tipo-envio{{index}}" value="true" index="{{index}}"/>
								<span class="radio-input">

									<i class="{{#if datosEnvio.esRegalo}}{{else}}hidden{{/if}}"></i>

								</span>
								<span>Regalo</span>
							</label>
						</div>
					</div>
				</div>
				<div class="datos-envio">
					<div>
						<h4>Datos de env&iacute;o <span {{#if guardado}}style="display: inline;"{{/if}} > {{#if esMobil}} &excl;Guardado!{{else}}&excl;Vale digital guardado!{{/if}} </span></h4>
						{{#ifmayor index value=1}}
							<button class="copiar-datos" type="button" disabled>Copiar datos previos <span class="material-icons">content_copy</span></button>
						{{/ifmayor}}
					</div>
					<div>
						<div>
							<label>
								<div>
									<span>Nombre</span>
									<input id="nameVale{{index}}" data-validate="false" name="name" type="text" value="{{datosEnvio.nombreContacto}}" onkeypress="onKeyPressNombre(event)"
 data-es-regalo="{{datosEnvio.esRegalo}}" 
{{#if datosEnvio.esRegalo}}{{else}}disabled{{/if}} />
									<i class="check material-icons">done</i>
								</div>
								<span class="error"></span>
								<div class="clearfix"></div>
							</label>
							<label>
								<div>
									<span>E-mail</span>
									<input id="emailVale{{index}}" 
									data-validate="false"
								name="email" type="text" value="{{datosEnvio.emailContacto}}" 
								{{#if datosEnvio.esRegalo}}{{else}}disabled{{/if}}
								   data-es-regalo="{{datosEnvio.esRegalo}}"
								 />
								
									<i class="check material-icons">done</i>
								</div>
								<span class="error"></span>
								<div class="clearfix"></div>
							</label>
						</div>
						<label>
							<div>
								<span>Mensaje</span>

								<textarea id="messageVale{{index}}" rows="3" placeholder="(Opcional)" onkeypress="onKeyPressNombre(event)" 

{{#if datosEnvio.esRegalo}}{{else}}disabled{{/if}} >{{datosEnvio.mensajeContacto}}</textarea>
								<span class="cant-caracteres">Caracteres disponibles 120</span>
							</div>
						</label>
					</div>
				</div>
			</article>
		</script>
				
<script type="text/javascript">
	$(document).ready(function(){
		activarMenu('vales');
		var valesPaso1 = new ValesDigitalesPaso1();
		valesPaso1.carrito = <c:out value='${carritoEvales}' escapeXml='false'></c:out>;
		valesPaso1.totalPuntosCliente="${cliente.totalPuntos}";
		valesPaso1.nameCsrf = "${_csrf.headerName}";
		valesPaso1.valueCsrf = "${_csrf.token}";
		valesPaso1.clienteEmail = "${cliente.email}";
		valesPaso1.clienteNombres = "${cliente.nombre} ${cliente.apellidos}";
		valesPaso1.simboloMoneda= "${simboloMoneda}";
		if("${activarValidarStock}"=="true"){
			valesPaso1.activarValidarStock =true; 
		}else{
			valesPaso1.activarValidarStock =false;
		}
		valesPaso1.init();
	});
</script>

