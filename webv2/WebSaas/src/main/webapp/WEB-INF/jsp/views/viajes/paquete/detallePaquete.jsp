<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>

<!-- inicio - modificación 29/09/2015 -->
		<div id="contenedor">
<!-- fin - modificación 29/09/2015 -->

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
			<c:if test="${contenido != ''}">
				<div>${contenido}</div>
			</c:if>
			
			
			<!-- inicio - modificación 29/09/2015 -->
			<div id="oportunidades">
			<!-- fin - modificación 29/09/2015 -->
				<!-- inicio - modificación 29/09/2015 -->
					<!-- inicio - oportunidad 1 -->
					<div id="oportunidad-1">
						<!-- imagen 1 -->
						<div class="img-fondo">
							<!-- fin - modificación 29/09/2015 -->
							<!-- resaltar -->
							<section class="para-clientes">
								<c:if test="${sessionScope.sessionCliente ne null }">								
								<h3>Por ser cliente Interbank</h3>
								</c:if>
								<table class="titulo-soles">
									<tr>
										<td class="titulo-separar"><p class="titulo alta">${detallePaquete.tituloPaquete} </p></td>
										<td>
											<ul class="conversion">
												<li class="soles"><span>${simboloMonedaDolar} ${formatPrecioDolares} &#124; ${simboloMonedaSoles} ${formatPrecioSoles}</span></li>
												<c:if test="${sessionCliente ne null}">
													<li class="decision">&oacute;</li>
													<li class="puntos">${formatCostoEnPuntos}<br />Millas Benefit</li>
												</c:if>
											</ul>
										</td>
									</tr>
								</table>
								<p class="duracion">${detallePaquete.tituloPaquete}</p>
								<!-- <p class="precio">(Precio regular <span>US$ ${detallePaquete.precioDolaresReferencia} &#124; S/.${detallePaquete.precioSolesReferencia}</span>)</p> -->
								<p class="precio">(Precio regular <span>${simboloMonedaDolar} ${formatPrecioDolaresReferencia} &#124; ${simboloMonedaSoles} ${formatPrecioSolesReferencia}</span>)</p>
								<p class="accion">
									<c:if test="${sessionCliente ne null}">
										<span>Tienes ${formatPuntos} Millas Benefit<br />${labelPuntos}</span>
							 		</c:if>
							 		<a href="${pageContext.request.contextPath}/viajes/compra/paquetes/cotizar/${keyItem}" class="boton">${button}</a>
								</p>
							</section>
							<!-- /resaltar -->
							<!-- acceso -->
							<nav class="acceso-oportunidad">
								<c:if test="${tipoPantalla == 'Oportunidad'}">
									<a href="${pageContext.request.contextPath}/viaje/paquete/obtener/destacados/${keyItem}" class="activo"><span></span><span>LA OPORTUNIDAD</span></a>
									<a href="${pageContext.request.contextPath}/viaje/paquete/destino/obtener/destacados/${keyItem}"><span></span><span>EL DESTINO</span></a>
									<a href="${pageContext.request.contextPath}/viaje/paquete/incluye/obtener/destacados/${keyItem}"><span></span><span>QUÉ INCLUYE</span></a>
								</c:if>
								<c:if test="${tipoPantalla == ''}">
									<a href="${pageContext.request.contextPath}/viajes/paquete/${keyItem}" class="activo"><span></span><span>LA OPORTUNIDAD</span></a>
									<a href="${pageContext.request.contextPath}/viajes/paquete/destino/${keyItem}"><span></span><span>EL DESTINO</span></a>
									<a href="${pageContext.request.contextPath}/viajes/paquete/${keyItem}"><span></span><span>QUÉ INCLUYE</span></a>
								</c:if>
							</nav>
							<!-- /acceso -->
							<span class="cargador"></span>
						<!-- inicio - modificación 29/09/2015 -->
							<img src="<c:out value="${detallePaquete.mainBannerImage}" escapeXml="false"/>" class="central" alt="${detallePaquete.tituloPaquete}" />
						</div>
						<!-- /imagen 1 -->
					</div>
            </div>			
			
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
		
		document.location.href = CONTEXT_PATH + "/viaje/paquete/obtener/destacados/" + $(".codigo-paquete"+key).val();
		
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
		
		document.location.href = CONTEXT_PATH + "/viaje/paquete/obtener/destacados/" + $(".codigo-paquete"+key).val();
		
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
