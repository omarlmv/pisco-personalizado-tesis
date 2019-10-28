<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>
<c:set var="isSession" value="false"></c:set>
<c:if test="${sessionScope.sessionCliente ne null }">
	<c:set var="isSession" value="true"></c:set>
</c:if>

<div id="banner-promociones" class="all-banner">
	<div class="banner"></div>
	<div class="handler"></div>
</div>

<script id="template-banner" type="text/x-handlebars-template">
	<div>
		<a href="{{href}}"><img src="{{imageSource}}" alt="{{titulo}}" /></a>
		<div class="detalle">
			<a href="{{href}}"><h3>{{titulo}}</h3></a>
			<div class="bloque">
				<div>
					<span class="porcentaje">{{porcentaje}}% dscto.</span>
					<span class="categoria">{{categoria}}</span>
				</div>
				<a class="detalles" href="{{href}}">Ver detalles</a>
			</div>
			<div class="clear"></div>
			<c:if test="${empty sessionCliente}">
				<div class="cliente">&iquest;No eres cliente? <a href="https://interbank.solicitudes.pe/tarjeta-credito-benefit" target="_blank">Hazte cliente ahora</a></div>
			</c:if>
		</div>
	</div>
</script>

<script type="text/javascript">
$(document).on('ready', function(){
	var promocionComun = new PromocionComun();
	promocionComun.llenarBanner();
});
</script>