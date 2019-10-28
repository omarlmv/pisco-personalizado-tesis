<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/jquery-ui.js"></script>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
		<div id="ficha-tecnica">
			<!-- flotante -->
			<section class="flotante-principal">
				<div class="resumen">
					<p class="arrow-oportunidad"></p>
					<div class="detalle">
						<p><span class="cantidad">8</span> <a href="#" class="oportunidades">Oportunidades exclusivas para ti</a></p>
						<ul>
							<li><a href="#" class="ico-retroceder-oferta ico-oferta"></a></li>
							<li>
								<p>${productoDetalle.titulo}</p>
								<p>1 / 8</p>
							</li>
							<li><a href="#" class="ico-avanzar-oferta ico-oferta"></a></li>
						</ul>
					</div>
				</div>
				<div class="resaltar">
					<ul>
						<li>7</li>
						<li><a href="#">Descuentos en lugares cercanos a tu casa</a></li>
					</ul>
					<p><a href="#">Explora todos los productos, servicios y beneficios</a></p>
				</div>
			</section>
			<!-- /flotante -->
			<!-- miga -->
			<div id="detalleProducto" >
			<section class="miga">
				<div class="detalle">
				</div>
			</section>
			<!-- /miga -->
			<div class="ficha-tecnica-detalle">
				<div class="titulo-general">
					<h1>${productoDetalle.tituloLargo}</h1>
					<span></span>
				</div>
				<section class="garantias-incluye">
					<!-- <img src="${prop['config.url.recursos.base.web']}/static/images/img-ficha-tecnica.jpg" height="236" width="300" alt="" /> -->
					<img src="${prop['url.imagen.producto']}${productoDetalle.producto.imagen2}" height="236" width="300" alt="" style="max-width: 300px; max-height: 236px" />
					<h4>Condiciones</h4>
					<p class="garantias">
					${productoDetalle.condiciones}					
					<!--
					Marco acero r&iacute;gido 10 años<br />Marco acero doble suspensi&oacute;n 3 años<br />Marco aluminio 3 años<br />Horquillas r&iacute;gidas 10 años<br />Horquillas suspensi&oacute;n 6 meses
					-->
					</p>
					<!-- <p class="incluye">Precio incluye despacho normal (6 a 9 d&iacute;as)</p>  -->
				</section>
				<section>
					<h5>Ficha t&eacute;cnica</h5>
					<table>
						<tr>
							<th>Marca</th>
							<th>${productoDetalle.producto.marcaCatalogo.nombre}</th>
						</tr>
						<tr>
							<td>Nombre</td>
							<td>${productoDetalle.nombre}</td>
						</tr>
						<tr>
							<td>Descripci&oacute;n</td>
							<td>${productoDetalle.descripcion}</td>
						</tr>
						<!--  
						<tr>
							<td>Titulo</td>
							<td>${productoDetalle.titulo}</td>
						</tr>
						<tr>
							<td>Titulo Largo</td>
							<td>${productoDetalle.tituloLargo}</td>
						</tr>
						-->
						<tr>
							<td>Categoria</td>
							<td>${productoDetalle.producto.categoria.nombreCategoria}</td>
						</tr>
						<!-- 
						<tr>
							<td>Precio Catalogo</td>
							<td>${productoDetalle.precioCatalogo}</td>
						</tr>
						<tr>
							<td>Precio Puntos</td>
							<td>${productoDetalle.precioPuntos}</td>
						</tr>
						<tr>
							<td>Precio Compra</td>
							<td>${productoDetalle.producto.precioCompra}</td>
						</tr>
						<tr>
							<td>Imagen 1</td>
							<td>${productoDetalle.producto.imagen1}</td>
						</tr>
						<tr>
							<td>Imagen 2</td>
							<td>${productoDetalle.producto.imagen2}</td>
						</tr>
						<tr>
							<td>Imagen 3</td>
							<td>${productoDetalle.producto.imagen3}</td>
						</tr>
						 -->
					</table>
				</section>
			</div>
			
			<!-- resaltar -->
			<section class="para-cliente">
				<h3>Para clientes Interbank</h3>
				<table class="titulo-soles">
								<tr>
									<td class="titulo-separar"><p class="titulo baja">dsds </p></td>
									<td>
										<ul class="solo-soles">
											<li class="soles-base">S/. dsdsd</li>
											<li class="decision">ó</li>
											<li class="puntos">63,000 puntos</li>
										</ul>
									</td>
								</tr>
				</table>
				<p class="precio">(Precio regular <span>S/. dsds</span>)</p>
							<p class="accion">
								<span>Tienes 25,000 puntos<br />Si los usas, pagas desde US$ 425</span>
								<a href="" class="boton">Boton</a>
				            </p>
							<a href="#" class="detalles">Ver detalles</a>
				<!-- <div class="detalle">
					<h3>Para clientes Interbank</h3>
					<div class="nombre-precio">
						<div class="nombre">
							<p class="titulo">${productoDetalle.titulo}</p>
							<ul>
								<li>(Precio regular <span>${simboloMoneda} ${productoDetalle.producto.precioCompra}</span>)</li>
							</ul>
						</div>
						<div class="precio">
							<ul>
								<li class="dinero">${simboloMoneda} ${productoDetalle.precioCatalogo}</li>
								<li class="decision">&oacute;</li>
								<li class="puntos">${productoDetalle.precioPuntos} puntos</li>
							</ul>
						</div>
					</div>
					<p class="accion">
						<span>Tienes 25,000 puntos<br />Te alcanza para este producto</span>
						<a href="${pageContext.request.contextPath}/canjeProducto/paso1/${productoDetalle.idCatalogoProducto}" class="boton">COTIZAR CANJE</a>
					</p>
				</div>-->
				<div class="fondo"></div>
			</section>
			</div>
			
			<div id="oportunidadProducto" hidden="true">
					<div style="height: 550px; background: url('${prop['url.imagen.producto']}${productoDetalle.producto.imagen1}')50% no-repeat; background-size:cover;">
				<section class="resaltar-basico">
				<div class="detalle">
					<h3>Para clientes Interbank</h3>
					<div class="nombre-precio">
						<div class="nombre">
							<p class="titulo">${productoDetalle.titulo}</p>
							<ul>
								<li>(Precio regular <span>${simboloMoneda} ${productoDetalle.producto.precioCompra}</span>)</li>
							</ul>
						</div>
						<div class="precio">
							<ul>
								<li class="dinero">${simboloMoneda} ${productoDetalle.precioCatalogo}</li>
								<li class="decision">&oacute;</li>
								<li class="puntos">${productoDetalle.precioPuntos} puntos</li>
							</ul>
						</div>
					</div>
					<p class="accion">
						<span>Tienes 25,000 puntos<br />Te alcanza para este producto</span>
						<a href="${pageContext.request.contextPath}/canjeProducto/paso1/${productoDetalle.idCatalogoProducto}" class="boton">COTIZAR CANJE</a>
					</p>
				</div>
				<div class="fondo"></div>
			</section>
				</div>
			
			</div>
			
			<div id="foto" hidden="true">
			<!-- flotante -->
			<section class="flotante-principal">
				<div class="resumen">
					<p class="arrow-oportunidad"></p>
					<div class="detalle">
						<p><span class="cantidad">2</span> <a href="#" class="oportunidades">Oportunidades exclusivas para ti</a></p>
						<ul>
							<li><a href="#" class="ico-retroceder-oferta ico-oferta"></a></li>
							<li>
								<p>${productoDetalle.titulo}</p>
								<p>2 / 9</p>	
							</li>
							<li><a href="#" class="ico-avanzar-oferta ico-oferta"></a></li>
						</ul>
					</div>
				</div>
				<div class="resaltar">
					<ul>
						<li>8</li>
						<li><a href="#">Descuentos en lugares cercanos a tu casa</a></li>
					</ul>
					<p><a href="#">Explora todos los productos, servicios y beneficios</a></p>
				</div>
			</section>
			<!-- /flotante -->
			<!-- miga -->
			<section class="miga">
			</section>
			<!-- /miga -->
			<div class="promocion-detalle">
				<div class="titulo-general">
					<p>${productoDetalle.titulo}</p>
					<span class="linea"></span>
				</div>
				<div class="galeria">
					<div class="imagen-cargada" style="max-height: 360px; max-width: 600px;">
						<div>
							<p class="texto"><span></span><span>${productoDetalle.descripcion}</span></p>
							<div class="fondo" ></div>
						</div>
						<a href="#" class="ico-retroceder-foto ico-foto"></a>
						<a href="#" class="ico-avanzar-foto ico-foto"></a>
						<img src="${prop['config.url.recursos.base.web']}/static/images/galeria-img-1-g.jpg" style="max-height: 360px; max-width: 600px;" alt="" style="display: inline;" />	
					</div>
					<nav>
						<a href="#" data-img-g="${prop['url.imagen.producto']}${productoDetalle.producto.imagen1}" data-texto="">
							<span></span>
							<img src="${prop['url.imagen.producto']}${productoDetalle.producto.imagen1}" style="max-width: 115px; max-height: 100px"  alt="" />
						</a>
						<a href="#" data-img-g="${prop['url.imagen.producto']}${productoDetalle.producto.imagen2}" data-texto="">
							<span></span>
							<img src="${prop['url.imagen.producto']}${productoDetalle.producto.imagen2}" style="max-width: 115px; max-height: 100px"  alt="" />
						</a>
						<a href="#" data-img-g="${prop['url.imagen.producto']}${productoDetalle.producto.imagen3}" data-texto="">
							<span></span>
							<img src="${prop['url.imagen.producto']}${productoDetalle.producto.imagen3}" style="max-width: 115px; max-height: 100px"  alt="" />
						</a>
					</nav>
				</div>
			</div>
			<!-- resaltar -->
			<section class="resaltar-basico">
				<div class="detalle">
					<h3>Para clientes Interbank</h3>
					<div class="nombre-precio">
						<div class="nombre">	
							<p class="titulo">${productoDetalle.titulo}</p>
							<ul>
								<li>(Precio regular <span>${simboloMoneda} ${productoDetalle.producto.precioCompra}</span>)</li>
							</ul>
						</div>
						<div class="precio">
							<ul>
								<li class="dinero">${simboloMoneda} ${productoDetalle.precioCatalogo}</li>
								<li class="decision">&oacute;</li>
								<li class="puntos">${productoDetalle.precioPuntos} puntos</li>
							</ul>
						</div>
					</div>
					<p class="accion">
						<span>Tienes 25,000 puntos<br />Te alcanza para este producto</span>
						<a href="${pageContext.request.contextPath}/canjeProducto/paso1/${productoDetalle.idCatalogoProducto}" class="boton">COTIZAR CANJE</a>
					</p>
				</div>
				<div class="fondo"></div>
			</section>
			<!-- /acceso -->
		</div>
			
			<!-- /resaltar -->
			<!-- acceso -->
			<nav id="acceso">
				<a id="btnOportunidad" href="#">LA OPORTUNIDAD</a>
				<a id="btnDetalle" href="#">DETALLE</a>
				<%-- <a href="${pageContext.request.contextPath}/compra/producto/fotos/${productoDetalle.idCatalogoProducto}">FOTOS</a> --%>
				<a id="btnFotos" href="#">FOTOS</a>
			</nav>
			<!-- /acceso -->
		</div>
		

<!-- 
<strong>Producto</strong> <br />
idCatalogoProducto => ${productoDetalle.idCatalogoProducto} <br />
Nombre => ${productoDetalle.nombre} <br />
Descripcion => ${productoDetalle.descripcion} <br />
Titulo => ${productoDetalle.titulo} <br />
Titulo Largo => ${productoDetalle.tituloLargo} <br />
Marca => ${productoDetalle.producto.marcaCatalogo.nombre} <br />
Categoria => ${productoDetalle.producto.categoria.nombre} <br />

Precio Catalogo => ${productoDetalle.precioCatalogo} <br />
Precio Puntos => ${productoDetalle.precioPuntos} <br />

Precio Compra => ${productoDetalle.producto.precioCompra} <br />
Imagen 1 => ${productoDetalle.producto.imagen1} <br />
Imagen 2 => ${productoDetalle.producto.imagen2} <br />
Imagen 3 => ${productoDetalle.producto.imagen3} <br />
 -->
 
 <script>
 $("#btnOportunidad").click(function(){
	    $("#oportunidadProducto").show();
	    $("#detalleProducto").hide();
	    $("#foto").hide();
	    $("#btnOportunidad").addClass("activo");
	    $("#btnDetalle").removeClass("activo");
	    $("#btnFotos").removeClass("activo");
	    
	});

 $("#btnDetalle").click(function(){
	    $("#oportunidadProducto").hide();
	    $("#detalleProducto").show();
	    $("#foto").hide();
	    $("#btnOportunidad").removeClass("activo");
	    $("#btnDetalle").addClass("activo");
	    $("#btnFotos").removeClass("activo");
	});

 $("#btnFotos").click(function(){
	    $("#oportunidadProducto").hide();
	    $("#detalleProducto").hide();
	    $("#foto").show();
	    $("#btnOportunidad").removeClass("activo");
	    $("#btnDetalle").removeClass("activo");
	    $("#btnFotos").addClass("activo");
	});

 $('.alineacion').find('a').removeClass('activo');
 $('#compras').addClass('activo');
 
 $(document).ready(function(){
	 $("#btnDetalle").addClass("activo");
 });
 
 
 </script>