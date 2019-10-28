<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div id="fb-root"></div>
<script>
	(function(d, s, id) {
		var api_key_face = '${prop["config.facebook.api.key"]}';
		var js, fjs = d.getElementsByTagName(s)[0];
		if (d.getElementById(id))
			return;
		js = d.createElement(s);
		js.id = id;
		js.src = "//connect.facebook.net/es_ES/sdk.js#xfbml=0&version=v2.10&appId="+ api_key_face;
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));
</script>

<div id="detalle-producto-page" data-menu="mostrar">
	<section class="miga miga-inferior">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los beneficios</a> <span>&gt;</span> 
			<a href="${pageContext.request.contextPath}/categorias">Productos</a> <span>&gt;</span>
			<p class="activo">${productoDetalle.producto.descripcionMarca}</p>
		</div>
	</section>

	<div class="back-option container">
		<a href="javascript:window.history.back()"><i class="material-icons">arrow_back</i>Regresar</a>
	</div>

	<div class="container">
		<div class="image-zone pull-left">
			<div id="image-producto">
				<div class="swipe">
					<img src="${prop['url.imagen.producto']}${productoDetalle.producto.imagen1}" alt="${productoDetalle.nombre}" onerror="this.src='${prop['url.imagen.producto']}/default/no-found-image.png'" />
				</div>
				<div class="controls"></div>
			</div>
			<div id="image-carousel">
				<div class="carousel_contenedor" style="width: calc(100% + 24px);">
					<div class="active" data-type="image" data-src="${prop['url.imagen.producto']}${productoDetalle.producto.imagen1}">
						<img onerror="this.src='${prop['url.imagen.producto']}/default/no-found-image.png'" id="img-0" src="${prop['url.imagen.producto']}${productoDetalle.producto.imagen1}" alt="${productoDetalle.nombre}" />
					</div>
					<div data-type="image" data-src="" class="imagenSecundaria">
						<img id="img-1" src="" />
					</div>
					<div data-type="image" data-src="" class="imagenSecundaria">
						<img id="img-2" src="" />
					</div>
					<div data-type="image" data-src="" class="imagenSecundaria">
						<img id="img-3" src="" />
					</div>
					<div data-type="image" data-src="" class="imagenSecundaria">
						<img id="img-4" src="" />
					</div>
					<div data-type="image" data-src="" class="imagenSecundaria">
						<img id="img-5" src="" />
					</div>
				</div>
				<span class="control control-next material-icons">keyboard_arrow_right</span>
				<span class="control control-prev material-icons">keyboard_arrow_left</span>
			</div>
		</div>

		<div class="titulo-producto pull-right">
			<h2>${productoDetalle.producto.descripcionMarca} </h2>
			<h1>${productoDetalle.nombre}</h1>
			<div class="divisor"></div>
			<ul class="social-media media-agotado">
				<li class="icon btn-share fb-share-button"><a class="fb-xfbml-parse-ignore afb-share-button fa fa-facebook-square" target="_blank" data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-ico-red-facebook"></i></a></li>
				<li class="icon btn-share"><a class="twitter-share-button-cc fa fa-twitter" target="_blank" href="https://twitter.com/intent/tweet?" data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-ico-red-twitter"></i></a></li>
				<li class="icon btn-share ws-share-button"><a class="fa fa-whatsapp whatsapp-share-button" href="whatsapp://send?text=" data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-icon-whatsapp"></i></a></li>
			</ul>
		</div>

		<div class="custom-product pull-right">
			<!-- <p class="descripcion">${productoDetalle.descripcion}</p> -->
			<section id="lista-atributos"></section>
			<c:if test="${productoDetalle.producto.stockDisponible > 0}">
				<a data-href="${pageContext.request.contextPath}/canjeProducto/carrito/${productoDetalle.keyItem}" class="btn-carrito"><i class="material-icons">add_shopping_cart</i>Agregar al carrito</a>
			</c:if>
			<script id="template-atributos" type="text/x-handlebars-template">
				<article>
					{{#ifconditional tipoConfiguracionParametroDetalle value=1}}
						<h3>{{descripcionConfiguracionParametroDetalle}}:</h3>
						<div class="sub-atributos">
							<div class="lista-subatributos">
							{{#each valorConfiguracionParametroDetalle}}
								<label class="sub-atributo-simple">
									<span class="sinstock {{#ifmayor catalogoProductoCustom.stockDisponible value=0}}hidden{{/ifmayor}}"></span>
									<button class="open-tooltip" data-atributo="{{valorAtributo}}" data-id="{{catalogoProductoCustom.idCatalogoProducto}}">{{valorAtributo}}</button>
									<span class="hidden">{{descripcionAtributo}}</span>
								</label>
							{{/each}}
							</div>
						</div>
					{{/ifconditional}}
					{{#ifconditional tipoConfiguracionParametroDetalle value=2}}
						<h3>{{descripcionConfiguracionParametroDetalle}}: <span class="labelColor"><span></h3>
						<div class="sub-atributos">
							<div class="lista-subatributos">
							{{#each valorConfiguracionParametroDetalle}}
								<label class="sub-atributo-color">
									<span class="sinstock {{#ifmayor catalogoProductoCustom.stockDisponible value=0}}hidden{{/ifmayor}}"></span>
									<button class="open-tooltip" style="background-image: url('{{catalogoProductoCustom.imagenProducto}}')" data-atributo="{{valorAtributo}}" data-id="{{catalogoProductoCustom.idCatalogoProducto}}"></button>
									<span class="hidden">{{descripcionAtributo}}</span>
								</label>
							{{/each}}
							</div>
						</div>
					{{/ifconditional}}
				</article>
			</script>
			<div class="inicio-canje">
				<p class="precio-sin-dscto">
				<c:choose>
					<c:when test="${productoDetalle.producto.stockDisponible > 0}">
						<c:choose>
							<c:when test="${esOferta and verPrecioRegular and tipoOferta eq 1}">
								<span class="tachado">${precioPuntosRegular} Millas Benefit</span>
								<span class="color-dscto" style="background-color:${colorImagen}">-${porcentajeDescuento}%</span>
							</c:when>
							<c:when test="${esOferta and verPrecioRegular and tipoOferta eq 2}">
								<span class="tachado">${precioPuntosRegular} Millas Benefit</span>
								<span class="color-dscto"></span>
							</c:when>
							<c:otherwise>
								<span class="tachado hidden"></span>
								<span class="color-dscto hidden"></span>
							</c:otherwise>
						</c:choose>
						<p class="millas">
							<span>${productoDetalle.formatPrecioPuntos}</span> <small>Millas Benefit</small>
						</p>
						<p class="soles">
							<span class="simbolo">o ${simboloMoneda}</span> <span class="formatPrecioC">${productoDetalle.formatPrecioCatalogo}</span>
						</p>

					</c:when>
					<c:when test="${productoDetalle.producto.stockDisponible < 1}">
						<c:choose>
							<c:when test="${productoDetalle.verPrecioProducto}">
								<c:choose>
									<c:when test="${esOferta and verPrecioRegular and tipoOferta eq 1}">
										<span class="tachado">${precioPuntosRegular} Millas Benefit</span>
										<span class="color-dscto" style="background-color:${colorImagen}">-${porcentajeDescuento}%</span>
									</c:when>
									<c:when test="${esOferta and verPrecioRegular and tipoOferta eq 2}">
										<span class="tachado">${precioPuntosRegular} Millas Benefit</span>
										<span class="color-dscto"></span>
									</c:when>
									<c:otherwise>
										<span class="tachado hidden"></span>
										<span class="color-dscto hidden"></span>
									</c:otherwise>
								</c:choose>
								<p class="millas">
									<span>${productoDetalle.formatPrecioPuntos}</span> <small>Millas Benefit</small>
								</p>
								<p class="soles">
									<span class="simbolo">o ${simboloMoneda}</span> <span class="formatPrecioC">${productoDetalle.formatPrecioCatalogo}</span>
								</p>
							</c:when>
							<c:otherwise>
								<span class="tachado hidden"></span>
								<span class="color-dscto hidden"></span>
								<p class="millas hidden">
									<span></span><small></small>
								</p>
								<p class="soles hidden">
									<span class="simbolo"></span> <span class="formatPrecioC"></span>
								</p>
							</c:otherwise>
						</c:choose>
					</c:when>
				</c:choose>
				<!-- <c:if test="${productoDetalle.producto.stockDisponible > 1}">
					<p class="txt-cantidad">Cantidad</p>
				</c:if>
				<c:if test="${productoDetalle.producto.stockDisponible == 0}">
					<p class="txt-cantidad" style="display: none">Cantidad</p>
				</c:if> -->
				<div>
					<c:if test="${productoDetalle.producto.stockDisponible > 1}">
						<div class="contador">
							<c:set var="stockMaximo"
								value="${productoDetalle.producto.stockDisponible}"></c:set>
							<c:if test="${stockMaximo > maximoStockVisible}">
								<c:set var="stockMaximo" value="${maximoStockVisible}"></c:set>
							</c:if>
							<span class="remove-cantidad material-icons disabled">remove</span>
							<span class="select-cantidad select-web" max-stock="${stockMaximo}"> <span>1</span> <i class="icon-down"></i>
								<ul class="dropdown-number">
									<c:forEach begin="1" end="${stockMaximo}" var="i">
										<li class="dropnum-item" value="${i}"><c:out value="${i}" /></li>
									</c:forEach>
								</ul>
							</span> <span class="add-cantidad material-icons active">add</span>
						</div>
					</c:if>
					
					<c:if test="${productoDetalle.producto.stockDisponible == 1}">
						<div class="contador">
							<c:set var="stockMaximo"
								value="${productoDetalle.producto.stockDisponible}"></c:set>
							<c:if test="${stockMaximo > maximoStockVisible}">
								<c:set var="stockMaximo" value="${maximoStockVisible}"></c:set>
							</c:if>
							<span class="remove-cantidad material-icons disabled">remove</span>
							<span class="select-cantidad select-web" max-stock="${stockMaximo}"> <span>1</span> <i class="icon-down"></i>
								<ul class="dropdown-number">
									<c:forEach begin="1" end="${stockMaximo}" var="i">
										<li class="dropnum-item" value="${i}"><c:out value="${i}" /></li>
									</c:forEach>
								</ul>
							</span> <span class="add-cantidad material-icons disabled">add</span>
						</div>
					</c:if>
					
					<c:if test="${productoDetalle.producto.stockDisponible < 1}">
						<div class="contador hidden">
							<span class="remove-cantidad material-icons disabled">remove</span>
							<span class="select-cantidad select-web" max-stock="${stockMaximo}"> <span>1</span> <i class="icon-down"></i>
								<ul class="dropdown-number"></ul>
							</span> <span class="add-cantidad material-icons disabled">add</span>
						</div>
					</c:if>

					<c:if test="${productoDetalle.producto.stockDisponible < 1}">
						<a href="" class="btn-azul btn-canje disabled" style="pointer-events: none;">Iniciar canje</a>
					</c:if>
					<c:if test="${productoDetalle.producto.stockDisponible > 0}">
						<a href="${pageContext.request.contextPath}/canjeProducto/paso1/${productoDetalle.keyItem}" class="btn-azul btn-canje">Iniciar canje</a>
					</c:if>
				</div>
				
<%-- 				<c:if test="${productoDetalle.producto.stockDisponible < 1}"> --%>
<!-- 					<a href="" class="btn-carrito disabled" style="pointer-events: none;">Agregar al carrito</a> -->
<%-- 				</c:if> --%>

				<c:if test="${productoDetalle.producto.stockDisponible > 0}">
					<a data-href="${pageContext.request.contextPath}/canjeProducto/carrito/${productoDetalle.keyItem}" class="btn-carrito"><i class="material-icons">add_shopping_cart</i>Agregar al carrito</a>
				</c:if>
				
				<p class="cantidad-stock">
					<c:if test="${productoDetalle.verCantidadStock >= productoDetalle.producto.stockDisponible and productoDetalle.producto.stockDisponible > 1}">
						<span>&iexcl;Solo ${productoDetalle.producto.stockDisponible} productos en stock!</span>
					</c:if>
					<c:if test="${productoDetalle.verCantidadStock >= productoDetalle.producto.stockDisponible and productoDetalle.producto.stockDisponible eq 1}">
						<span>&iexcl;Solo ${productoDetalle.producto.stockDisponible} productos en stock!</span>
					</c:if>
					<c:if test="${productoDetalle.producto.stockDisponible < 1}">
						<span class="agotado">&iexcl;Producto agotado!</span>
					</c:if>
				</p>

			</div>
			<div class="beneficios-producto">
<!-- 				<h3>Tipos de entrega disponibles</h3> -->
				<ul>
					<c:if test="${productoDetalle.esExpreso}">
						<li>
							<span class="material-icons">flash_on</span>
							<div>
								<h4>Delivery Express</h4>
								<p>24hr&middot;S&oacute;lo para Lima Metropolitana</p>
							</div>
						</li>
					</c:if>
					<li>
						<span class="material-icons">local_shipping</span>
						<div>
							<h4>Delivery Regular</h4>
							<p>3 d&iacute;as h&aacute;biles a Lima Metropolitana<br>3 a 7 d&iacute;as h&aacute;biles a Provincias</p>
						</div>
					</li>
					<li>
						<span class="material-icons">shopping_basket</span>
						<div>
							<h4>Opciones de pago</h4>
							<p>Con Millas Benefit, tarjeta de cr&eacute;dito o ambas.</p>
						</div>
					</li>
					<li>
						<span class="material-icons">security</span>
						<div>
							<h4>Compra segura</h4>
							<p>Tus compras son 100% seguras y respaldadas por Interbank.</p>
						</div>
					</li>
				</ul>
			</div>
			<ul class="social-media">
				<li class="icon btn-share fb-share-button"><a class="fb-xfbml-parse-ignore afb-share-button fa fa-facebook-square" target="_blank" data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-ico-red-facebook"></i></a></li>
				<li class="icon btn-share"><a class="twitter-share-button-cc fa fa-twitter" target="_blank" href="https://twitter.com/intent/tweet?" data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-ico-red-twitter"></i></a></li>
				<li class="icon btn-share ws-share-button"><a class="fa fa-whatsapp whatsapp-share-button" href="whatsapp://send?text=" data-url="${fbUrl}" data-text="${fbTitle}"><i class="icon-icon-whatsapp"></i></a></li>
			</ul>
		</div>

		<div class="pull-left">
			<div class='info-producto seccion <c:if test="${empty productoDetalle.producto.informacionProducto}">hidden</c:if>'>
				<h3>Informaci&oacute;n del producto</h3>
				<p>${productoDetalle.producto.informacionProducto}</p>
			</div>

			<div class='especificaciones seccion <c:if test="${empty productoDetalle.producto.especificacionesProducto}">hidden</c:if>'>
				<h3>Especificaciones</h3>
				<p>${productoDetalle.producto.especificacionesProducto}</p>
			</div>
			<div class='modo-uso seccion <c:if test="${empty productoDetalle.producto.modoUsoProducto}">hidden</c:if>'>
				<h3>Modo de uso</h3>
				<p>${productoDetalle.producto.modoUsoProducto}</p>
			</div>
			<div class="seccion resenia-producto">
				<div class="contenedor-resumen-estrellas">
					<h3 class="titulo-resenia"></h3>
					<div class="estrellas"></div>
				</div>
				<div class="main-resenia">
					<div class="paginador-comentarios">
						<span class="prev disabled">Anterior</span>
						<div class="pages">
							<span class="numero-pagina active">1</span>
						</div>
						<span class="next disabled">Siguiente</span>
					</div>
				</div>
			</div>
			<div class="seccion nuevo-comentario hidden">
				<div>
					<h2>&#191;Quieres dejar un comentario?</h2>
					<div style="margin-left: 15px;">
						<p class="clasificacion">
						  <input id="radio1" type="radio" name="estrellas" value="5">
						  <label for="radio1" class="material-icons">star_border</label>
						  <input id="radio2" type="radio" name="estrellas" value="4">
						  <label for="radio2" class="material-icons">star_border</label>
						  <input id="radio3" type="radio" name="estrellas" value="3">
						  <label for="radio3" class="material-icons">star_border</label>
						  <input id="radio4" type="radio" name="estrellas" value="2">
						  <label for="radio4" class="material-icons">star_border</label>
						  <input id="radio5" type="radio" name="estrellas" value="1">
						  <label for="radio5" class="material-icons">star_border</label>
						</p>
					</div>
				</div>

				<div class="">
					<form:form id="form-envio-comentario" class="contenedor-modal" autocomplete="off" method="post" action="${pageContext.request.contextPath}/producto/registrarComentario">
						<div style="position:relative;">
							<textarea rows="8" id="comment" data-limit="false" onkeypress="disableKeypress(event)" maxlength="120"></textarea>
							<span class="cant-caracteres">120/120</span>
						</div>
						<div style="margin: 10px 0;">
							<span>Tu evaluaci&oacute;n se publicar&aacute; con el nombre registrado en Interbank Benefit.</span>
						</div>
<!--						<div id="contenedor-recaptcha">
	      					<div class="g-recaptcha" data-callback="verificarReCaptcha" data-expired-callback="recaptchaExpired" data-sitekey="${prop['config.api.recaptcha.site.key']}"></div>
	      				</div> -->
	      				<br/>
	      				<div>
							<button id="btn-enviar-comentario" type="button"  class="btn-azul">Enviar comentario</button>
						</div>
						<span class="respuesta-envio hidden"></span>
  					</form:form>
				</div>
			</div>
		</div>

		<div class="clearfix"></div>
	</div>
</div>

<div class="cnt-scrollnav">
	<div id="scrollnav">
		<a data-scroll="titulo-producto" href="#">Resumen</a>
		
		<c:choose>
			<c:when test="${not empty productoDetalle.producto.informacionProducto}">
				<span>&middot;</span>
				<a data-scroll="info-producto" href="#">Detalles</a>
			</c:when>
			<c:otherwise>
				<span class="hidden">&middot;</span>
				<a data-scroll="info-producto" class="hidden" href="#">Detalles</a>
			</c:otherwise>
		</c:choose>
		
		<c:choose>
			<c:when test="${not empty productoDetalle.producto.especificacionesProducto}">
				<span>&middot;</span>
				<a data-scroll="especificaciones" href="#">Especificaciones</a>
			</c:when>
			<c:otherwise>
				<span class="hidden">&middot;</span>
				<a data-scroll="especificaciones" class="hidden" href="#">Especificaciones</a>
			</c:otherwise>
		</c:choose>
		
		<c:choose>
			<c:when test="${not empty productoDetalle.producto.modoUsoProducto}">
				<span>&middot;</span>
				<a data-scroll="modo-uso" href="#">Modo de uso</a>
			</c:when>
			<c:otherwise>
				<span class="hidden">&middot;</span>
				<a data-scroll="modo-uso" class="hidden" href="#">Modo de uso</a>
			</c:otherwise>
		</c:choose>
		<span>&middot;</span>
		<a data-scroll="resenia-producto" href="#">Comentarios</a>
	</div>
</div>

<div id="canje-flotante" class="<c:if test="${productoDetalle.producto.stockDisponible < 1}">producto-agotado</c:if>">
	<c:choose>
		<c:when test="${productoDetalle.producto.stockDisponible > 0}">
			<div>
				<div>
					<c:choose>
						<c:when test="${esOferta and verPrecioRegular and tipoOferta eq 1}">
							<p class="dscto">
								<span>${precioPuntosRegular} Millas Benefit</span> <span class="color-dscto" style="background-color:${colorImagen}">-${porcentajeDescuento}%</span>
							</p>
							<p class="millas">${productoDetalle.formatPrecioPuntos} <small>Millas Benefit</small></p>
							<p class="labelMillas hidden">Millas Benefit</p>
						</c:when>
						<c:when test="${esOferta and verPrecioRegular and tipoOferta eq 2}">
							<p class="dscto">
								<span>${precioPuntosRegular} Millas Benefit</span> <span class="color-dscto"></span>
							</p>
							<p class="millas">${productoDetalle.formatPrecioPuntos} <small>Millas Benefit</small></p>
							<p class="labelMillas hidden">Millas Benefit</p>
						</c:when>
						<c:otherwise>
							<p class="dscto hidden">
								<span></span> <span class="color-dscto"></span>
							</p>
							<p class="millas">${productoDetalle.formatPrecioPuntos}</p>
							<p class="labelMillas">Millas Benefit</p>
						</c:otherwise>
					</c:choose>

					<p class="soles">
						<span class="simbolo">o ${simboloMoneda}</span> <span class="formatPrecioC">${productoDetalle.formatPrecioCatalogo}</span>
					</p>

				</div>

				<div>
					<div class="contador">
						<c:set var="stockMaximo" value="${productoDetalle.producto.stockDisponible}"></c:set>
						<c:if test="${stockMaximo > maximoStockVisible}">
							<c:set var="stockMaximo" value="${maximoStockVisible}"></c:set>
						</c:if>
						<span class="remove-cantidad material-icons disabled">remove</span>
						<span class="select-cantidad select-app" max-stock="${stockMaximo}">
							<span>1</span> <i class="icon-down"></i>
							<ul class="dropdown-number">
								<c:forEach begin="1" end="${stockMaximo}" var="i">
									<li class="dropnum-item" value="${i}"><c:out value="${i}" /></li>
								</c:forEach>
							</ul>
						</span> <span class="add-cantidad material-icons <c:if test="${stockMaximo eq 1}">disabled</c:if><c:if test="${stockMaximo > 1}">active</c:if>">add</span>
					</div>
					<p class="cantidad-stock">
						<c:if test="${productoDetalle.verCantidadStock >= productoDetalle.producto.stockDisponible and productoDetalle.producto.stockDisponible > 1}">
							<span>&iexcl;Solo ${productoDetalle.producto.stockDisponible} productos en stock!</span>
						</c:if>
						<c:if test="${productoDetalle.verCantidadStock >= productoDetalle.producto.stockDisponible and productoDetalle.producto.stockDisponible eq 1}">
							<span>&iexcl;Solo ${productoDetalle.producto.stockDisponible} productos en stock!</span>
						</c:if>
						<c:if test="${productoDetalle.producto.stockDisponible < 1}">
							<span class="agotado">&iexcl;Producto agotado!</span>
						</c:if>
					</p>
				</div>
			</div>
		</c:when>


		<c:when test="${productoDetalle.producto.stockDisponible < 1}">
			<c:choose>
				<c:when test="${productoDetalle.verPrecioProducto}">
					<div>
						<div>
							<c:choose>
								<c:when test="${esOferta and verPrecioRegular and tipoOferta eq 1}">
									<p class="dscto">
										<span>${precioPuntosRegular} Millas Benefit</span> <span class="color-dscto" style="background-color:${colorImagen}">-${porcentajeDescuento}%</span>
									</p>
									<p class="millas">${productoDetalle.formatPrecioPuntos} <small>Millas Benefit</small></p>
									<p class="labelMillas hidden">Millas Benefit</p>
									<p class="soles">
										<span class="simbolo">o ${simboloMoneda}</span> <span
											class="formatPrecioC">${productoDetalle.formatPrecioCatalogo}</span>
									</p>
								</c:when>
								<c:when test="${esOferta and verPrecioRegular and tipoOferta eq 2}">
									<p class="dscto">
										<span>${precioPuntosRegular} Millas Benefit</span> <span class="color-dscto" style="background-color:${colorImagen}">-${porcentajeDescuento}%</span>
									</p>
									<p class="millas">${productoDetalle.formatPrecioPuntos} <small>Millas Benefit</small></p>
									<p class="labelMillas hidden">Millas Benefit</p>
									<p class="soles">
										<span class="simbolo">o ${simboloMoneda}</span> <span
											class="formatPrecioC">${productoDetalle.formatPrecioCatalogo}</span>
									</p>
								</c:when>
								<c:otherwise>
									<p class="dscto hidden">
										<span></span> <span class="color-dscto"></span>
									</p>
									<p class="millas">${productoDetalle.formatPrecioPuntos}</p>
									<p class="labelMillas">Millas Benefit</p>
									<p class="soles">
										<span class="simbolo">o ${simboloMoneda}</span> <span class="formatPrecioC">${productoDetalle.formatPrecioCatalogo}</span>
									</p>
								</c:otherwise>
							</c:choose>
							<%-- 								<p class="millas">${productoDetalle.formatPrecioPuntos}</p> --%>
							<!-- 								<p class="millas">Millas Benefit</p> -->
							<%-- 								<p class="soles"><span class="simbolo">o ${simboloMoneda}</span> <span class="formatPrecioC">${productoDetalle.formatPrecioCatalogo}</span></p> --%>
						</div>

						<div>
							<div class="contador hidden">
								<c:set var="stockMaximo" value=""></c:set>
								<c:if test="${stockMaximo > maximoStockVisible}">
									<c:set var="stockMaximo" value="${maximoStockVisible}"></c:set>
								</c:if>
								<span class="remove-cantidad material-icons disabled">remove</span>
								<span class="select-cantidad select-app" max-stock="${stockMaximo}"> <span>1</span> <i class="icon-down"></i>
									<ul class="dropdown-number">
										<c:forEach begin="1" end="${stockMaximo}" var="i">
											<li class="dropnum-item" value="${i}"><c:out value="${i}" /></li>
										</c:forEach>
									</ul>
								</span> <span class="add-cantidad material-icons disabled">add</span>
							</div>
							<p class="cantidad-stock">
								<c:if test="${productoDetalle.verCantidadStock >= productoDetalle.producto.stockDisponible and productoDetalle.producto.stockDisponible > 1}">
									<span>&iexcl;Solo ${productoDetalle.producto.stockDisponible} productos en stock!</span>
								</c:if>
								<c:if test="${productoDetalle.verCantidadStock >= productoDetalle.producto.stockDisponible and productoDetalle.producto.stockDisponible eq 1}">
									<span>&iexcl;Solo ${productoDetalle.producto.stockDisponible} productos en stock!</span>
								</c:if>
								<c:if test="${productoDetalle.producto.stockDisponible < 1}">
									<span class="agotado hidden">&iexcl;Producto agotado!</span>
								</c:if>
							</p>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="hidden">
						<div>
							<p class="dscto hidden">
								<span></span> <span class="color-dscto hidden"></span>
							<p class="millas hidden"></p>
							<p class="labelMillas hidden">Millas Benefit</p>
							<p class="soles hidden">
								<span class="simbolo"></span> <span class="formatPrecioC"></span>
							</p>
						</div>

						<div class="contador hidden">
							<c:set var="stockMaximo" value=""></c:set>
							<c:if test="${stockMaximo > maximoStockVisible}">
								<c:set var="stockMaximo" value="${maximoStockVisible}"></c:set>
							</c:if>
							<span class="remove-cantidad material-icons disabled">remove</span>
							<span class="select-cantidad select-app" max-stock="${stockMaximo}"> <span>1</span> <i class="icon-down"></i>
								<ul class="dropdown-number">
									<c:forEach begin="1" end="${stockMaximo}" var="i">
										<li class="dropnum-item" value="${i}"><c:out value="${i}" /></li>
									</c:forEach>
								</ul>
							</span> <span class="add-cantidad material-icons disabled">add</span>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</c:when>
	</c:choose>


	<c:if test="${productoDetalle.producto.stockDisponible < 1}">
		<c:choose>
			<c:when test="${productoDetalle.verPrecioProducto}">
				<a href="" class="btn-iniciar disabled" style="pointer-events: none;">Agotado</a>
			</c:when>
			<c:otherwise>
				<a href="" class="btn-iniciar disabled" style="border: none; line-height: 50px; max-width: 100%; font-size: 17px;pointer-events: none;">Producto agotado</a>
			</c:otherwise>
		</c:choose>
	</c:if>
	<c:if test="${productoDetalle.producto.stockDisponible > 0}">
		<a href="${pageContext.request.contextPath}/canjeProducto/paso1/${productoDetalle.keyItem}" class="btn-iniciar">Iniciar canje</a>
	</c:if>


<script id="template-carrito-modal" type="text/x-handlebars-template">
	<div>
			<i class="material-icons close">close</i>
			<i class="material-icons">done</i>
			<img class="imgCarrito" onerror="this.src='${prop['url.imagen.producto']}/default/no-found-image.png'" src="{{src}}" alt="{{alt}}" />
			<h3>{{mensaje}}</h3>
			<a href='${pageContext.request.contextPath}/canjeProducto/pendientes' class='btn-azul btn-ir-carrito'>Ir al carrito</a>
		<button type="button" id="btn-seguir-comprando">Seguir comprando</button>
	</div>
</script>

</div>
<script id="template-resenia-producto" type="text/x-handlebars-template">
	<div class="comentario">
		<div class="comentario-datos">
			<div style="display:flex;">
				<h3>{{nombresCliente}} {{apellidosCliente}}</h3>
				<div class="contenedor-estrellas">
				 	{{#starsPositive valorComentario}}
	    		 	{{/starsPositive}}
				 	{{#starsNegative valorComentario}}
	    		 	{{/starsNegative}}
				</div>
			</div>
			<div class="contenedor-fecha">
				{{fechaRegistro}}
			</div>
		</div>
		<div class="contenedor-descripcion">
			<p>{{descripcionComentario}}</p>
		</div>
	</div>
</script>

<div id="modalCarrito">
<!-- 	<div> -->
<!-- 		<i class="material-icons">done</i> -->
<!-- 		<img class="imgCarrito" src="http://s3.amazonaws.com/bim.batch.files/public/web/images/producto/ficha-tecnica/106914-ficha-tecnica.jpg" alt="{{alt}}"/> -->
<!-- 		<h3>&iexcl;Producto aadido al carrito!</h3> -->
<%-- 		<a href='${pageContext.request.contextPath}/canjeProducto/pendientes' class='btn-azul btn-ir-carrito'>Ir al carrito</a> --%>
<!-- 		<button type="button" id="btn-seguir-comprando">Seguir comprando</button> -->
<!-- 	</div> -->
</div>


<script id="template-beneficios-producto" type="text/x-handlebars-template">
	{{#if esExpreso}}
		<li>
			<span class="material-icons">flash_on</span>
			<div>
				<h4>Delivery Express</h4>
				<p>24hr&middot;S&oacute;lo para Lima Metropolitana</p>
			</div>
		</li>
	{{/if}}
	<li>
		<span class="material-icons">local_shipping</span>
		<div>
			<h4>Delivery Regular</h4>
			<p>3 d&iacute;as h&aacute;biles a Lima Metropolitana<br>3 a 7 d&iacute;as h&aacute;biles a Provincias</p>
		</div>
	</li>
	<li>
		<span class="material-icons">shopping_basket</span>
		<div>
			<h4>Opciones de pago</h4>
			<p>Con Millas Benefit, tarjeta de cr&eacute;dito o ambas.</p>
		</div>
	</li>
	<li>
		<span class="material-icons">security</span>
		<div>
			<h4>Compra segura</h4>
			<p>Tus compras son 100% seguras y respaldadas por Interbank.</p>
		</div>
	</li>
</script>

<script type="text/javascript">
	$(document).ready(function() {
		var apiKeyG = '${prop["config.google.api.key.shortLink"]}';
		activarMenu("compras");
		var prod = new ProductoDetalle();
		var data = '${displayModal}';
		if(data!="") {
			
			var templateHead = $("#template-carrito-modal");
			var sourceTemplateHead  = templateHead.html();
			console.log(sourceTemplateHead);
			var templateHead  = Handlebars.compile(sourceTemplateHead);
			var context = null;
	 		var dataParse = eval("(" + data + ")");
			
			if(dataParse.estado==0) {
				context={"estado": dataParse.estado,"mensaje": dataParse.mensaje,
					    "src": dataParse.result[0],"alt": dataParse.result[1]};
			} else {
					context={"estado": dataParse.estado,"mensaje": dataParse.mensaje,
						    "src": dataParse.result[0],"alt": dataParse.result[1]};
			}
						
			var theCompiledHtml = templateHead(context);
						
			$('#modalCarrito').html(theCompiledHtml);
			$("#modalCarrito").fadeIn('fast').css('display', 'flex');
						
			$('body').on('click', '#btn-seguir-comprando', function(event) {
				$('#modalCarrito').html('');
				$("#modalCarrito").fadeOut('fast');
			});
		}

		prod.codigoGrupoCaracteristica = '${codigoGrupo}';
		prod.idCatalogoProducto = '${productoDetalle.idCatalogoProducto}';
		prod.imagenCatalogoProducto = '${productoDetalle.producto.imagen1}';
		prod.videoProducto = '${productoDetalle.producto.videoProducto}';
		prod.stockDisponible = '${productoDetalle.producto.stockDisponible}';
		prod.verPrecioProducto = '${productoDetalle.verPrecioProducto}';
		prod.nameCsrf = "${_csrf.headerName}";
		prod.valueCsrf = "${_csrf.token}";
		prod.cantidadComentariosXPagina = '${prop["config.cantidad.comentario.x.pagina"]}';
		
		prod.init();
		
		handlerShareButton();
		generateShortLinkTwitter(apiKeyG, '${fbUrl}');
				
	    var objCatalogoProducto = <c:out value='${productoDetalleJson}' escapeXml='false'></c:out>;
// 		console.log("objCatalogoProducto DETALLE :"+JSON.stringify(objCatalogoProducto));
		//DATA PARA ADOBE 
	 	var objAdobeAnalytic = new AdobeAnalytic();
	 	objAdobeAnalytic.fullPath = window.location.href;
	 	objAdobeAnalytic.pathURL = window.location.pathname;
	 	objAdobeAnalytic.tipoIntegracion = ADOBE_ANALYTIC.DETALLE_PRODUCTO;
	 	objAdobeAnalytic.dataAnalizar = objCatalogoProducto;
	 	var objPageBody = objAdobeAnalytic.sendDataAdobe();
// 	 	console.log("Body Page: " + JSON.stringify(objPageBody));	 	
	 	
	 	digitalData.push(objPageBody);

	 	digitalData.push({"event":"virtualPage"});
	});
	
</script>