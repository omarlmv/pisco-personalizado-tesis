<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="ficha-tecnica">
	<!-- flotante -->
	<section class="flotante-principal">
		<div class="resumen">
			<p class="arrow-oportunidad"></p>
			<div class="detalle">
				<p>
					<span class="cantidad">3</span> <span>Oportunidades exclusivas para ti</span>
				</p>
				<ul id="minslide">
					<li><a href="${pageContext.request.contextPath}/destacados/oportunidad" class="ico-retroceder-oferta ico-oferta"></a></li>
					<li class="relative"><c:forEach items="${oportunidades}" var="oportunidad">
							<div key="${oportunidad.item}">${oportunidad.descripcion}<br>1/${total}</div>
						</c:forEach></li>
					<li><a href="${pageContext.request.contextPath}/destacados/oportunidad" class="ico-avanzar-oferta ico-oferta"></a></li>
				</ul>
			</div>
		</div>
		<div class="resaltar">
			<a href="#">Explora todos los beneficios</a>
		</div>
	</section>
	<section class="miga">
		<div class="detalle">
			<p class="activo">Oportunidades exclusivas para ti</p>
		</div>
		<div class="fondo"></div>
	</section>
	<div id="oportunidades">
		<c:if test="${destacadoDetalle.tipoDestacado == 1 }">
			<div class="oportunidad-1">
				<div class="ficha-tecnica-detalle">
					<div class="titulo-general">
						<h1 class="titulo">${destacadoDetalle.catalogoProducto.titulo}</h1>
					</div>
					<section class="caracteristicas">
						<img src="${prop['url.imagen.producto']}${destacadoDetalle.catalogoProducto.producto.imagen1}" style="width: 300px; height: 236px; !important" alt="" /> <span style="font-family: 'Arial Negrita', 'Arial'; font-weight: 700; font-size: 16px; color: #333333;"></span>
						<p>
							<span style="font-family: 'Arial Normal', 'Arial'; font-weight: 400; font-size: 13px; color: #333333;"></span>
						<p class="incluye"></p>
						<p class="incluye"></p>
						<p class="incluye"></p>
						<p class="incluye"></p>
					</section>
					<h4>Ficha t&eacute;cnica</h4>
					<table>
						<tr>
							<td>Marca</td>
							<td>${destacadoDetalle.catalogoProducto.producto.marcaCatalogo.nombre}</td>
						</tr>
						<tr>
							<td>Nombre</td>
							<td>${destacadoDetalle.catalogoProducto.nombre}</td>
						</tr>
						<tr>
							<td>Descripci&oacute;n</td>
							<td>${destacadoDetalle.catalogoProducto.descripcion}</td>
						</tr>
						<tr>
							<td>Categoria</td>
							<td>${destacadoDetalle.catalogoProducto.producto.categoria.nombreCategoria}</td>
						</tr>
					</table>
				</div>
				<section class="para-clientes">
					<h3>Por ser cliente Interbank</h3>
					<table class="titulo-soles">
						<tr>
							<td class="titulo-separar"><p class="titulo alta">${destacadoDetalle.catalogoProducto.titulo}</p></td>
							<td>
								<ul class="conversion">
									<li class="soles">${simboloMoneda} ${destacadoDetalle.catalogoProducto.precioCatalogo}</li>
									<li class="decision">&oacute;</li>
									<li class="puntos">${destacadoDetalle.catalogoProducto.precioPuntos}<br />Millas Benefit</li>
								</ul>
							</td>
						</tr>
					</table>
					<p class="precio">
						(Precio regular <span>${simboloMoneda} ${destacadoDetalle.catalogoProducto.producto.precioCompra} </span>)
					</p>
					<p class="accion">
						<span>Tienes ${cliente.totalPuntos} Millas Benefit<br />Si los usas, pagas desde ${simboloMoneda} ${destacadoDetalle.porCubrir}
						</span> <a href="${pageContext.request.contextPath}/canjeProducto/paso1/${destacadoDetalle.catalogoProducto.idCatalogoProducto}" class="boton">${destacadoDetalle.button}</a>
					</p>
				</section>
				<!-- /resaltar -->
				<!-- acceso -->
			</div>
		</c:if>
		<c:if test="${destacadoDetalle.tipoDestacado == 2}">
			<div class="oportunidad-1">
				<div class="ficha-tecnica-detalle">
					<div class="titulo-general">
						<h1 class="titulo">${destacadoDetalle.descuento.titulo}</h1>
					</div>
					<section class="caracteristicas">
						<img src="${prop['url.imagen.producto']}${destacadoDetalle.descuento.imagen}" height="316" width="235" alt="" />
						<h4>T&eacute;rminos y condiciones</h4>
						<p>${destacadoDetalle.descuento.terminosCondiciones}</p>
					</section>
					<section>
						<h4>Qu&eacute; incluye</h4>
						<table>
							<tr>
								<td>Nombre</td>
								<td>${destacadoDetalle.descuento.nombre}</td>
							</tr>
							<tr>
								<td>Descripci&oacute;n</td>
								<td>${destacadoDetalle.descuento.descripcion}</td>
							</tr>
							<tr>
								<td>% de descuento</td>
								<td>${destacadoDetalle.descuento.descuento}</td>
							</tr>
							<tr>
								<td>Consumo m&aacute;ximo</td>
								<td>${destacadoDetalle.descuento.montoMaximoConsumo}</td>
							</tr>
							<tr>
								<td>Usos por cliente</td>
								<td>${destacadoDetalle.descuento.maximoUso}</td>
							</tr>
						</table>
					</section>
				</div>
				<!-- resaltar -->
				<section class="para-clientes">
					<h3>Para clientes Interbank</h3>
					<table class="titulo-soles">
						<tr>
							<td class="titulo-separar">
								<p class="titulo baja">${destacadoDetalle.descuento.nombre}</p>
							</td>
							<td>
								<ul class="oferta">
									<li class="descuento">${destacadoDetalle.descuento.descuento}%</li>
									<li class="puntos">Descuento</li>
								</ul>
							</td>
						</tr>
					</table>
				</section>
			</div>

		</c:if>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		//$('#minslide').simpleFade();
	});
	$.fn.simpleFade = function(options) {
		var defaults = {
			'prev' : $('.ico-retroceder-oferta'),
			'next' : $('.ico-avanzar-oferta'),
			'absolute' : true
		}
		var opts = $.extend(defaults, options);
		var div = this.find('.relative').children('div');
		var maxslide = div.length - 1;
		var here = 0;

		opts.next.click(function(e) {
			e.preventDefault();
			if (here < maxslide) {
				here++;
			} else {
				here = 0;
			}
			animateFade();
		});

		opts.prev.click(function(e) {
			e.preventDefault();
			if (here > 0) {
				here--;
			} else {
				here = maxslide;
			}
			animateFade();
		});

		function animateFade() {
			var key = div.eq(here).attr('key');
			div.eq(here).siblings().fadeOut();
			div.eq(here).fadeIn();

			if (opts.absolute) {
				var opt = $('#oportunidad-' + key);
				opt.siblings().fadeOut();
				opt.fadeIn();
			} else {
				var opt = $('.oportunidad-' + key);
				opt.siblings().css('display', 'none');
				opt.css('display', 'block');
			}

		}

	};
</script>