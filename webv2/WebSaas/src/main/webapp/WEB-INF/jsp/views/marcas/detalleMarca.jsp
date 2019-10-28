<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<div id="contenedor" class="detalleMarcasPage flotante-explora slider-foto">
	<section class="miga miga-inferior">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los beneficios</a> <span>&gt;</span>
			<a href="${pageContext.request.contextPath}/marca">Canje en locales afiliados</a> <span>&gt;</span>
			<p class="activo">${canjeLocal.nombreMarca}</p>
		</div>
	</section>
	<div class="opciones-detalle">
		<div class="btnVolver">
			<a href="javascript:window.history.back()" ><i class="material-icons">arrow_back</i>Regresar</a>
		</div>
		<div class="btnCompartir">
			<span class="show-share">Compartir</span>
			<div class="share-icons">
				<span class="hide-share">Cerrar</span> 
				<i class="icon-next"></i>
				<div class="btn-share fb-share-button" data-href="https://developers.facebook.com/docs/plugins/" data-layout="button" data-size="small" data-mobile-iframe="false">
					<a class="fb-xfbml-parse-ignore afb-share-button" data-url="${fbUrl}" data-text="${fbTitle}">
						<i class="icon-ico-facebook"></i>
					</a>
				</div>
				<div class="btn-share">
					<a class="twitter-share-button-cc" target="_blank" href="https://twitter.com/intent/tweet?" data-url="${fbUrl}" data-text="${fbTitle}">
						<i class="icon-ico-twitter"></i>
					</a>
				</div>
				<div class="btn-share btn-whatsapp">
					<a class="whatsapp-share-button" href="whatsapp://send?text=" data-url="${fbUrl}" data-text="${fbTitle}">
						<i class="icon-ico-whatsapp"></i>
					</a>
				</div>
			</div>
			<i class="fa fa-share-alt pointer"></i>
		</div>
	</div>
	
	<div class="cont-detalle-marca">
		<img src="${prop['url.imagen.marca']}${canjeLocal.imagenMediumMarca}" alt="${canjeLocal.nombreMarca}"/>
		<div class="info-marca">
			<div class="titulo">
				<h3>${canjeLocal.nombreMarca}</h3>
			</div>
			<p class="rubro-marca">${canjeLocal.nombreCategoria}</p>
			<p class="desc-marca">${canjeLocal.descripcionMarca}</p>
			<div class="cont-locales">
				<div class="titulo">Locales</div>
<!-- 				<div class="contenido"> -->
				<div class="contenido-locales">
					<div class="contenido">
						<div id="ciudad-despliegue-lima" class="ciudad-despliegue">
							<strong class="ciudad">Lima</strong>
							<br /><br />
						</div><br />
						<div id="ciudad-despliegue-provincias" class="ciudad-despliegue">
							<strong class="ciudad">Provincia</strong>
							<br /><br />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
</div>

<script id="templateListaLocales" type="text/x-handlebars-template">
	<ul>
		<li><span class="ciudad">{{nombre}}</span> - {{direccion}}</li>
	</ul><br />
</script>

<script type="text/javascript">
	$(document).on('ready', function(){
		var apiKeyG =  '${prop["config.google.api.key.shortLink"]}';
		handlerShareButton();
		generateShortLinkTwitter(apiKeyG,'${fbUrl}');
		
		var detalleMarca = new DetalleMarca();
		detalleMarca.idsLocales = '${canjeLocal.idsLocales}';
		detalleMarca.init();
		activarMenu("marcas");
	});
</script>
