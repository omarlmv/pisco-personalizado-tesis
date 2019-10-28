<div id="promocion-detalle-a-page">
	<section class="miga miga-inferior">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los beneficios</a><span>&gt;</span>
			<a href="${pageContext.request.contextPath}/promociones">Promociones</a>
			<span>&gt;</span>
			<p class="activo">${detallePromocion.heroTitulo}</p>
		</div>
	</section>
</div>

<div id="color-banner">
	<div class="container">
<%-- 		<h4>${detallePromocion.sheetsCategoria}</h4> --%>
		<h1>${detallePromocion.heroTitulo}</h1>
		<p></p>
	</div>
</div>

<div id="tab-promocion" class="container">
	<div class="image">
		<img src="${prop['config.halcon.recursos.base.web']}${detallePromocion.imagenCategoria}" alt="">
		<div class="redes-sociales">
			<a class="facebook"><i class="fa fa-facebook-square"></i></a>
			<a class="twitter"><i class="fa fa-twitter"></i></a>
		</div>
	</div>
	<div class="detalle">
		<h2>${detallePromocion.heroTitulo}</h2>
		<p>${detallePromocion.detalleCategoria}</p>
		<div>${detallePromocion.sheetsCategoria}</div>
	</div>
</div>

<div id="tab-condiciones" class="container">
<!-- 	<h2>Condiciones</h2> -->
	${detallePromocion.descripcionCondiciones}
	${detallePromocion.textoLegal}
</div>

<script type="text/javascript">
	$(document).ready(function(){
		activarMenu('promociones');
		var promocionesDetalle = new PromocionesDetalle();
		promocionesDetalle.selectTab();
	});
</script>