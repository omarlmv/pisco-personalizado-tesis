<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>
		<div id="galeria">

			<!-- flotante -->
			<c:if test="${sessionCliente ne null}">
				<c:if test="${tipoPantalla == 'Oportunidad'}">
					<section class="flotante-principal">
						<div class="resumen">
							<p class="arrow-oportunidad"></p>
							<div class="detalle">
								<p><span class="cantidad">${cantidadPaquetesDestacadosPuntaje}</span> <span>Ofertas exclusivas para ti</span></p>
								<ul id="minslide">
									<li><a href="#" class="ico-retroceder-oferta ico-oferta"></a></li>
									<li class="relative" >
										
										<c:forEach items="${oportunidades}" var="oportunidad">
											<div key="${oportunidad.item}" style="display:${oportunidad.display}" >${oportunidad.nombre}<br>${oportunidad.item}/${cantidadPaquetesDestacadosPuntaje}</div>
											<input type="hidden" id="codigo-paquete" class="codigo-paquete${oportunidad.item}" value="${oportunidad.codigoPaquete}"></input>
										</c:forEach>
										
									</li>
									<li><a href="#" class="ico-avanzar-oferta ico-oferta"></a></li>
								</ul>
							</div>
						</div>
						<div class="resaltar">
							<a href="${pageContext.request.contextPath}/viajes/vuelos">Explora todos los beneficios</a>
						</div>
					</section>
				</c:if>
			</c:if>
			<!-- /flotante -->
			
			<!-- miga -->
			<section class="miga">
				<div class="detalle">
					<a href="${pageContext.request.contextPath}/">Todos los beneficios</a>
					<span>&gt;</span>
					<a href="${pageContext.request.contextPath}/viajes">Viajes</a>
					<span>&gt;</span>
					<p class="activo"><p class="activo">Paquete</p>
				</div>
				<div class="fondo"></div>
			</section>
			<!-- /miga -->
			
			<!-- <div id="oportunidades"> -->
				<!-- <div id="oportunidad-1"> -->
					<div class="promocion-detalle">
						<div class="titulo-general">
							<p class="titulo">${reservaPaqueteForm.tituloPaquete}</p>
						</div>
						<div class="galeria">
							<div class="imagen-cargada">
								<div>
								<p class="texto">
								<span></span><span></span>
								</p>
								<div class="fondo"></div>
							</div>
							<a href="#" class="ico-retroceder-foto ico-foto" onclick="return false;"></a>
							<a href="#" class="ico-avanzar-foto ico-foto" onclick="return false;"></a>
							<img src="<c:out value="${reservaPaqueteForm.mainBannerImage}" escapeXml="false"/>" style="height:238px;width:410px;" alt="${reservaPaqueteForm.tituloPaquete}" />
								
								
							</div>
							 <nav>
								<c:forEach var="imagen" items="${detalleImagen}">
								    <a href="#" onclick="return false;" data-img-g="<c:out value="${imagen}"/>" data-texto="">
										<span></span>
										<img src="<c:out value="${imagen}" escapeXml="false" />" height="100" width="115" alt="${reservaPaqueteForm.tituloPaquete}" style="width:115px;height:100px;" />
									</a>
								</c:forEach>
							</nav>
						</div>
					</div>
					<!-- resaltar -->
						<section class="para-clientes">
						<c:if test="${sessionScope.sessionCliente ne null }">	
							<h3>Por ser cliente Interbank</h3>
						</c:if>
							<table class="titulo-soles">
								<tr>
									<td class="titulo-separar"><p class="titulo alta">${reservaPaqueteForm.tituloPaquete}</p></td>
									<td>
										<ul class="conversion">
											<li class="soles">${simboloMonedaDolar} ${formatPrecioDolares} &#124; ${simboloMonedaSoles} ${formatPrecioSoles}</li>
											<c:if test="${sessionCliente ne null}">
											<li class="decision">&oacute;</li>
											<li class="puntos">${formatCostoEnPuntos}<br />Millas Benefit</li>
											</c:if>
										</ul>
									</td>
								</tr>
							</table>
							<p class="precio">(Precio regular <span>${simboloMonedaDolar} ${formatPrecioDolaresReferencia} &#124; ${simboloMonedaSoles} ${formatPrecioSolesReferencia}</span>)</p>
							<p class="accion">
								<c:if test="${sessionCliente ne null}">
									<span>Tienes ${formatPuntos} Millas Benefit<br />${labelPuntos}</span>
								</c:if>
								<a href="${pageContext.request.contextPath}/viajes/compra/paquetes/cotizar/2/${keyItem}" class="boton"> ${button} </a>
							</p>
						</section>
					<!-- /resaltar -->
					<!-- acceso -->
					<nav class="acceso-oportunidad">
								<c:if test="${tipoPantalla == 'Oportunidad'}">
									<a href="${pageContext.request.contextPath}/viaje/paquete/obtener/destacados/${detalleCodigo}" ><span></span><span>LA OPORTUNIDAD</span></a>
									<a href="${pageContext.request.contextPath}/viaje/paquete/destino/obtener/destacados/${detalleCodigo}" class="activo" ><span></span><span>EL DESTINO</span></a>
									<a href="${pageContext.request.contextPath}/viaje/paquete/incluye/obtener/destacados/${detalleCodigo}"><span></span><span>QUÉ INCLUYE</span></a>
								</c:if>
								<c:if test="${tipoPantalla == ''}">
									<a href="${pageContext.request.contextPath}/viajes/paquete/${keyItem}"><span></span><span>LA OPORTUNIDAD</span></a>
									<a href="${pageContext.request.contextPath}/viajes/paquete/destino/${keyItem}" class="activo" ><span></span><span>EL DESTINO</span></a>
									<a href="${pageContext.request.contextPath}/viajes/paquete/${keyItem}"><span></span><span>QUÉ INCLUYE</span></a>
								</c:if>
							</nav>
					<!-- /acceso -->
					<span class="cargador"></span>
					<!-- inicio - modificación 29/09/2015 -->
				<!-- </div>-->
			<!-- </div>	-->		
			
		</div>
<script type="text/javascript">
$(document).ready(function(){
	activarMenu("viajes");
	$(".cargador").hide();
	$('#minslide').simpleFade();
});
$.fn.simpleFade = function(options) {
	var defaults = { 
		'prev':$('.ico-retroceder-oferta'), 
		'next':$('.ico-avanzar-oferta'),
		'absolute':true
	}
	var opts   = $.extend(defaults, options);	
	var div    =  this.find('.relative').children('div');
	var maxDestacado = div.length;
	var maxslide  = div.length - 1;
	var here   = 0;
	
	
	opts.next.click(function(e){
		e.preventDefault();
		
		var key = parseInt(div.eq(here).attr('key')) + 1;
		
		if(key > maxDestacado){
			key = maxDestacado;
		}
		
		console.log('next-key : '+key);
		console.log('next-codigo paquete : '+$(".codigo-paquete"+key).val());
		
		document.location.href = CONTEXT_PATH + "/viajes/paquete/destino/obtener/destacados/" + $(".codigo-paquete"+key).val();
		
		if(here < maxslide){						
			here++;	
		}else{
			here = 0;
		}
		animateFade();
	});
	
	opts.prev.click(function(e){
		e.preventDefault();
		
		var key = parseInt(div.eq(here).attr('key')) - 1;
		
		if(key < 1){
			key = 1;
		}
		
		console.log('prev-key : '+key);
		console.log('prev-codigo paquete : '+$(".codigo-paquete"+key).val());
		
		document.location.href = CONTEXT_PATH + "/viajes/paquete/destino/obtener/destacados/" + $(".codigo-paquete"+key).val();
		
		if(here > 0){
			here--;					
		}else{
			here = maxslide;
		}
		animateFade();
	});
	
	function animateFade(){
		var key = div.eq(here).attr('key');
		var codigoPaquete=$(".codigo-paquete"+key).val();
		console.log('animate-cod:'+codigoPaquete);
		$(".id-seleccionado").val(codigoPaquete);
		div.eq(here).siblings().fadeOut();
		div.eq(here).fadeIn();
	}
	
};

</script>