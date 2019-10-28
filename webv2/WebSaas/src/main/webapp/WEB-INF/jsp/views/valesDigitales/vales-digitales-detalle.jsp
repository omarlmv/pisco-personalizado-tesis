<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="evales-detalle" data-menu="mostrar">
	<section class="miga miga-inferior">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los beneficios</a> <span>&gt;</span>
			<a href="${pageContext.request.contextPath}/vales-digitales">Vales digitales</a> <span>&gt;</span>
			<p class="activo">${evalesDetalle.nombreMarca}</p>
		</div>
	</section>
	
	<div class="back-option container">
		<a href="javascript:window.history.back()"><i class="material-icons">arrow_back</i>Regresar</a>
	</div>
	
	<div id="detalle-vale" class="container">
		<div class="pull-right">									
			<h3>${evalesDetalle.categoria.nombreCategoria}</h3>			
			<h1>${evalesDetalle.titulo}</h1>			
			<div class = "informacion-general">
				<c:if test="${evalesDetalle.parTipoEvales eq 2 || evalesDetalle.parTipoEvales eq 3}">										
					<c:if test="${evalesDetalle.esOferta && evalesDetalle.verPrecioRegular && evalesDetalle.tipoOferta.codigo eq 1}">
						<div class="oferta">
							<span>${evalesDetalle.precioPuntosRegularFormat} Millas Benefit</span>
							<span class="porc-oferta" style='background-color: ${evalesDetalle.colorImagenFlag}'>-${evalesDetalle.porcentajeDescuento}%</span>					
						</div>																
					</c:if>
					
					<c:if test="${evalesDetalle.esOferta && evalesDetalle.verPrecioRegular && evalesDetalle.tipoOferta.codigo eq 2}">
						<div class="oferta">				
							<span>${evalesDetalle.precioPuntosRegularFormat} Millas Benefit</span>									
						</div>																
					</c:if>	
					
					<p class="millas">${evalesDetalle.precioPuntosFormat} <span>Millas Benefit</span></p>
					<p class="soles">o ${simboloMoneda}${evalesDetalle.precioCatalogoFormat}</p>
				</c:if>			
			</div>
			<p class="vale-descripcion">${evalesDetalle.descripcion}</p>
			<div class="separador"></div>
		</div>
		<div class="pull-left">
			<img alt="Detalle de vale" src="${prop['url.imagen.evales']}${evalesDetalle.imagenFormat}">
			<ul class="social-media">
				<li  class="icon btn-share fb-share-button" data-href="https://developers.facebook.com/docs/plugins/" data-layout="button" data-size="small" data-mobile-iframe="false">
					<a class="fb-xfbml-parse-ignore afb-share-button fa fa-facebook-square" data-url="${fbUrl}" data-text="${fbTitle}">
					</a>
				</li>
				<li class="icon">
				<a class="fa fa-twitter twitter-share-button-cc" target="_blank" href="https://twitter.com/intent/tweet?" data-url="${fbUrl}" data-text="${fbTitle}">
				</a>
				</li>
				<li class="icon">
					<a class="fa fa-whatsapp whatsapp-share-button" href="whatsapp://send?text=" data-url="${fbUrl}" data-text="${fbTitle}">
					</a>
				</li>
			</ul>
		</div>
		<div class="pull-right">				
			<div class="header-opciones">
				<span>Vales Digitales</span>
				<span>Cantidad</span>
			</div>
							
			<section class="opciones-vales">
				<a href="javascript:;" id="canje-vale" class="btn-azul">Iniciar canje</a>
				 
				<c:if test="${(evalesDetalle.parTipoEvales eq 2 || evalesDetalle.parTipoEvales eq 3) 
				&& evalesDetalle.verCantidadStock ne null && evalesDetalle.stock > 0 
				&& evalesDetalle.stock <= evalesDetalle.verCantidadStock}">
					<c:if test="${evalesDetalle.stock eq 1}">
						<p class="err">¡Solo ${evalesDetalle.stock} vale digital en stock!</p>
					</c:if>
					<c:if test="${evalesDetalle.stock > 1}">
						<p class="err">¡Solo ${evalesDetalle.stock} vales digitales en stock!</p>
					</c:if>					
						
				</c:if>					
				 
				<p class="err-container err"></p>
			</section>
			<div id="beneficios-producto">
				<h3>Tipos de entrega</h3>
				<ul>
					<li>
						<span class="material-icons">flash_on</span>
						<div>
							<h4>Entrega inmediata</h4>
							<p>Al instante en tu e-mail</p>
						</div>
					</li>
					<li>
						<span class="material-icons">card_giftcard</span>
						<div>
							<h4>Env&iacute;o como regalo</h4>
							<p>Env&iacute;a el Vale Digital v&iacute;a e-mail</p>
						</div>
					</li>
				</ul>
			</div>
		</div>
		<script id="template-opciones-vale" type="text/x-handlebars-template">
			<article>
				<div>
					<h3>Vale digital de ${simboloMoneda}{{evale.precioCatalogoFormat}}</h3>
					<!-- 
 					<div class = "{{mostrarFlagDescuentoYPrecioProductoEvales evale.stock evale.verProductoAgotado evale.verPrecioProducto}}">
						{{flagDescuentoDetalleEvales evale.esOferta evale.verPrecioRegular evale.tipoOferta.codigo evale.precioPuntosRegularFormat evale.colorImagenFlag evale.porcentajeDescuento}}
					</div>
					-->
					<p>por {{evale.precioPuntosFormat}} Millas Benefit</p>
					<p>o ${simboloMoneda}{{evale.precioCatalogoFormat}}</p>
				</div>
				<div>
					{{#ifmayor evale.stock value=0}}
						<div class="contador">
							<span class="remove-cant material-icons {{classDefault}}">remove</span>
							<span class="select-cantidad" idkey="{{evale.keyItem}}" max-stock="{{evale.stock}}">
							 	<span>{{cantidadDefault}}</span>
								<i class="icon-down"></i>
								<ul class="dropdown-number">
									
									 <li class="dropnum-item">0</li>
									{{listaStockEvales evale.stock}}
									
								</ul>
							</span>
							<span class="add-cant material-icons active">add</span>
						</div>
						
						{{mostrarProductoBajoStockEvalesDetalle evale.stock evale.verCantidadStock evale.parTipoEvales}}
						
					{{/ifmayor}}
					{{#ifmenor evale.stock value=1}}
						<div class="contador-agotado">Agotado</div>
					{{/ifmenor}}
				</div>
			</article>
		</script>
		<div class="pull-left">
			<div id="vale-condicion">
				<h2 class="subtitle">Condiciones</h2>
				<p>${evalesDetalle.condiciones}</p>
			</div>
			<div id="vale-locales">
				<h2 class="subtitle">Locales</h2>
				<ul id="lista-locales"></ul>
				<a href="#" id="ver-locales">Ver m&aacute;s</a>
				<script id="template-locales" type="text/x-handlebars-template">
					<li>
						<div>
							<h4>{{#abrirMapaEvalesTitulo nombreLocal latitud longitud}}{{/abrirMapaEvalesTitulo}}</h4>
							<p>{{direccion}}</p>
							<p>{{descripcion}}</p>
							<p>{{ciudad}}</p>
						</div>
						{{#abrirMapaEvales latitud longitud}}
	
						{{/abrirMapaEvales}}
						<!--<a href="#" class="como-llegar">&iquest;Como llegar?</a>-->
					</li>
				</script>
			</div>
		</div>
		<div class="clearfix"></div>
	</div>
	
</div>

<div id="modal-locales" class="background-modal" data-close="true">
	<div class="contenedor-modal">
		<span class="close material-icons">clear</span>
		<h2>Locales</h2>
		<ul id="lista-locales-modal"></ul>
	</div>
</div>

<div id="modal-como-llegar" class="override-modal">
	<div class="general-modal">
		<div class="cerrar-modal">
			<span>Abrir con</span>
			<span class="fa fa-times"></span>
		</div>
		<div>
			<a id="go-google-maps" target="_blank" href="">Google Maps</a>
			<a id="go-waze" target="_blank" href="">Waze</a>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function(){
		activarMenu('vales');
		var valesDetalle = new ValesDigitalesDetalle();
		var locales = <c:out value='${evalesLocales}' escapeXml="false"></c:out>;
		var valesXMarca = <c:out value='${listaEvalesXMarca}' escapeXml="false"></c:out>;  
		var apiKeyG =  '${prop["config.google.api.key.shortLink"]}';
		valesDetalle.init();
		valesDetalle.opcionesDetalle(valesXMarca);
		valesDetalle.listaLocales(locales);
		
		handlerShareButton();
		generateShortLinkTwitter(apiKeyG,'${fbUrl}');
		
		
	});
</script>
<div id="fb-root"></div>
<script>
(function(d, s, id) {
	var api_key_face = '${prop["config.facebook.api.key"]}';
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/es_ES/sdk.js#xfbml=0&version=v2.10&appId="+api_key_face;
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

</script>
