 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <div id="contenedor">
 <input type="hidden" id="totalRegistros" value="${total}"/>
			<!-- flotante -->
			<section class="flotante-principal">
				<div class="resumen">
					<p class="arrow-oportunidad"></p>
					<div class="detalle">
						<p><span class="cantidad">${total}</span> <span>Oportunidades exclusivas para ti</span></p>
						<ul id="minslide">
							<li><a href="#" class="ico-retroceder-oferta ico-oferta"></a></li>
							<li class="relative">
								<c:forEach items="${oportunidades}" var="oportunidad">
									<div key="${oportunidad.item}">${oportunidad.descripcion}<br>${oportunidad.item}/${total}</div>
									<input type="hidden" id="id-destacado" class="id-destacado${oportunidad.item}" value="${oportunidad.tipoDestacado}-${oportunidad.idDestacado}"></input>
								</c:forEach>
							</li>
							<li><a href="#" class="ico-avanzar-oferta ico-oferta"></a></li>
						</ul>
					</div>
				</div>
				<div class="resaltar">
					<a href="${pageContext.request.contextPath}/descuento/descuento">Explora todos los beneficios</a>
				</div>
			</section>
			<!-- /flotante -->
			<!-- miga -->
			<section class="miga">
				<div class="detalle">
					<p class="activo">Oportunidades exclusivas para ti</p>
				</div>
				<div class="fondo"></div>
			</section>
			<!-- /miga -->
			
			<div id="oportunidades">
			    <c:forEach items="${oportunidades}" var="oportunidad">
				<div id="oportunidad-${oportunidad.item}" style="display: none;">
					<c:if test="${oportunidad.tipoDestacado == 1}">
					<div class="img-fondo">
						<section class="para-clientes">
							<h3>Por ser cliente Interbank</h3>
							<table class="titulo-soles">
								<tr>
									<td class="titulo-separar"><p class="titulo alta">${oportunidad.descripcion}</p></td>
									<input type="hidden" class="id-seleccionado" value="${oportunidad.tipoDestacado}-${oportunidad.idDestacado}"></input>
									<td>
										<ul class="conversion">
											<li class="soles">${simboloMoneda} ${oportunidad.catalogoProducto.precioCatalogo}</li>
											<li class="decision">&oacute;</li>
											<li class="puntos">${oportunidad.catalogoProducto.precioPuntos}<br />Millas Benefit</li>
										</ul>
									</td>
								</tr>
							</table>
							<p class="precio">(Precio regular <span>${simboloMoneda} ${oportunidad.catalogoProducto.producto.precioCompra} </span>)</p>
							<p class="accion">
								<span>Tienes ${cliente.totalPuntos} puntos<br />Si los usas, pagas desde ${simboloMoneda} ${oportunidad.porCubrir}</span>
								<a href="${pageContext.request.contextPath}/canjeProducto/paso1/${oportunidad.catalogoProducto.idCatalogoProducto}" class="boton">${oportunidad.button}</a>
							</p>
						</section>
						<nav class="acceso-oportunidad">
							<a href="#" class="activo"><span></span><span>LA OPORTUNIDAD</span></a>
							<a href="#" onclick="verCondiciones();"><span></span><span>CONDICIONESS</span></a>
						</nav>
						<span class="cargador"></span>
						<img src="${prop['url.imagen.producto']}${oportunidad.catalogoProducto.producto.imagen1}" class="central">
						</div>
					</c:if>
						<c:if test="${oportunidad.tipoDestacado == 2}">
					<div class="img-fondo">
						<section class="para-clientes">
							<h3>Por ser cliente Interbank</h3>
							<table class="titulo-soles">
								<tr>
									<td class="titulo-separar"><p class="titulo alta">${oportunidad.descripcion}</p>
									<input type="hidden" class="id-seleccionado" value="${oportunidad.tipoDestacado}-${oportunidad.idDestacado}"></input>
									</td>
									<td>
										<ul class="conversion">
											<li class="soles">${oportunidad.descuento.descuento}%</li>
											<li class="decision">Descuento</li>
										</ul>
									</td>
								</tr>
							</table>
							<p class="duracion">${oportunidad.descripcion}</p>
						</section>
						<nav class="acceso-oportunidad">
							<a href="#" class="activo"><span></span><span>LA OPORTUNIDAD</span></a>
							<a href="#" onclick="verCondiciones();"><span></span><span>CONDICIONES</span></a>
						</nav>
						<span class="cargador"></span>
						<img src="${prop['url.imagen.descuento']}${oportunidad.descuento.imagen}" class="central">
						</div>
						</c:if>
						<c:if test="${oportunidad.tipoDestacado == 3}">
						<div class="img-fondo">
							<section class="para-clientes">
								<h3><spring:message code="label.home.clientes.interbank" text="Para clientes Interbank" /></h3>
								<table class="titulo-soles">
									<tr>
										<td class="titulo-separar">
					 						<p class="titulo alta">${oportunidad.descripcion} US$ ${oportunidad.paquete.precioDolares}</p>
					 						<input type="hidden" class="id-seleccionado" value="${oportunidad.tipoDestacado}-${oportunidad.idDestacado}"></input>
										</td>
										<td>
											<ul>
												<li class="soles">${simboloMoneda} ${oportunidad.paquete.precioSoles}</li>
												<li class="decision">ó</li>
												<li class="puntos">${oportunidad.paquete.precioPuntos}<br />Millas Benefit</li>
											</ul>
										</td>
									</tr>
								</table>
								<p class="duracion"></p>
								<p class="precio">(Precio regular <span>US$ ${oportunidad.paquete.precioDolaresReferencia} &#124; ${simboloMoneda} ${oportunidad.paquete.precioSolesReferencia})</span></p>
								<p class="accion">
									<span>Tienes ${cliente.totalPuntos} puntos<br />Si los usas, pagas desde US$ ${oportunidad.porCubrir}</span>
									<a href="${pageContext.request.contextPath}/viajes/compra/paquetes/cotizar/2/${oportunidad.paquete.codigo}" class="boton">${oportunidad.button}</a>
								</p>
								<a href="${pageContext.request.contextPath}/viajes/paquete/detalle/obtener/sincronizados/${oportunidad.paquete.codigo}">Ver detalles</a>
							</section>
							<nav class="acceso-oportunidad">
							<a href="#" class="activo"><span></span><span>LA OPORTUNIDAD</span></a>
							<a href="#" onclick="verCondiciones();"><span></span><span>CONDICIONES</span></a>
							</nav>
							<span class="cargador"></span>
							<img src="${oportunidad.paquete.mainBannerImage}" class="central">
						</div>
						</c:if>
				</div>
				</c:forEach>
            </div>
		</div>
<script type="text/javascript">
$(document).ready(function(){
	$(".cargador").hide();
	$('#minslide').simpleFade();
	$('#oportunidad-'+1).css('display','block');
});
$.fn.simpleFade = function(options) {
	var defaults = { 
		'prev':$('.ico-retroceder-oferta'), 
		'next':$('.ico-avanzar-oferta'),
		'absolute':true
	}
	
	var opts   = $.extend(defaults, options);	
	var div    =  this.find('.relative').children('div');
	var maxslide  = div.length - 1;
	var here   = 0;
	
	opts.next.click(function(e){
		e.preventDefault();		 
		if(here < maxslide){						
			here++;	
		}else{
			here = 0;
		}
		animateFade();
	});
	
	opts.prev.click(function(e){
		e.preventDefault();
		if(here > 0){
			here--;					
		}else{
			here = maxslide;
		}
		animateFade();
	});
	
	function animateFade(){
		//alert("key");
		var key = div.eq(here).attr('key');
		var idDestacado=$(".id-destacado"+key).val();
		$(".id-seleccionado").val(idDestacado);
		div.eq(here).siblings().fadeOut();
		div.eq(here).fadeIn();
		
		if(opts.absolute){
			var opt = $('#oportunidad-'+key);
			opt.siblings().fadeOut();
			opt.fadeIn();
		}else{
			var opt = $('.oportunidad-'+key);
			opt.siblings().css('display','none');
			opt.css('display','block');
		}
	}
};

function verCondiciones(){
	
	var idSeleccionado=$(".id-seleccionado").val();
	location.href=CONTEXT_PATH +"/destacados/oportunidad-condiciones/"+idSeleccionado;
	
}
</script>