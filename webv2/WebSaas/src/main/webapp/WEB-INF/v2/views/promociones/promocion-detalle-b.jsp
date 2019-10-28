<div id="promocion-detalle-b-page"></div>

<div id="breadcrumbs" class="container">
	<a href="${pageContext.request.contextPath}/">Inicio</a>
	<span class="icon-right"></span>
	<a href="${pageContext.request.contextPath}/promociones">Promociones</a>
	<span class="icon-right"></span>
	<span class="active">Promoci&oacute;n B</span>
</div>

<div id="select-tabs" class="container">
	<span data-tab="promocion" class="active">Mi Promoci&oacute;n</span>
	<span data-tab="condiciones">Condiciones</span>
</div>

<div id="tab-promocion" class="container">
	<div class="header-promo">
		<h3>Tu diversi&oacute;n est&aacute; asegurada con los descuentos exclusivos de tus Tarjetas Blue y D&eacute;bito Joven.</h3>
	</div>
	<div class="clearfix"></div>
	<section class="promo-categoria"></section>
</div>

<script id="template-promo-b" type="text/x-handlebars-template">
	<h2>{{categoria}}</h2>
	<div class="clearfix"></div>
	{{#each articles}}
	<article>
		<div class="image">
			<img src="${prop['config.url.recursos.base.web']}v2/images/promociones/{{image}}" alt="{{titulo}}">
		</div>
		<div class="detalle">
		<h3>{{titulo}}</h3>
		<ul>
			{{#each lista}}
				<li><p><strong>{{porcentaje}}</strong>{{texto}}</p></li>
			{{/each}}
		</ul>
		<p>{{restricciones}}</p>
		<p><strong>{{direccion.strong}}</strong>{{direccion.texto}}</p>
		</div>
	</article>
	{{/each}}
</script>

<div id="tab-condiciones" class="container">
  <h2>T&eacute;rminos y Condiciones generales</h2>
  <p>Promociones v&aacute;lidas del 01/09/17 al 31/12/17 pagando con Tarjeta Blue o D&eacute;bito Joven de Interbank. No son acumulables con otras promociones y/o descuentos. M&aacute;ximo 1 promoci&oacute;n por cliente. Las promociones de cumplea&ntilde;os solo aplican al titular, quien deber&aacute; presentar su DNI. Cada local tiene restricciones propias que se pueden ver en la descripci&oacute;n de cada promoci&oacute;n. Interbank no se responsabiliza por los productos y/o servicios brindados por los establecimientos. Consulta tasas de inter&eacute;s, comisiones, gastos y penalidades en el Tarifario, en Tiendas Interbank o en <a href="https://www.interbank.pe" target="_blank">www.interbank.pe</a>, conforme a la normativa de Transparencia.</p>
</div>

<script type="text/javascript">
  $(document).ready(function(){
    fnActiveMenu('promociones');
    var promocionesDetalle = new PromocionesDetalle();
    promocionesDetalle.selectTabB();
    promocionesDetalle.listaPromoB();
  });
</script>