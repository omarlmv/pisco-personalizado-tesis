<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		<div id="contenedor">
			<section class="miga">
				<div class="detalle">
					<a href="${pageContext.request.contextPath}/">Todos los beneficios</a>
					<span>&gt;</span>
					<a href="${pageContext.request.contextPath}/compras">Compras</a>
					<span>&gt;</span>
					<p class="activo">Oportunidad</p>
				</div>
				<div class="fondo"></div>
			</section>
			<div id="oportunidades">
				<!-- inicio - oportunidad 1 -->
				<div id="oportunidad-1">
					<!-- imagen 1 -->
					<div class="img-fondo">
						<section class="para-clientes">
						 	<c:if test="${sessionScope.sessionCliente ne null }">
							 <h3>Por ser cliente Interbank</h3>
							</c:if>
							<table class="titulo-soles">
								<tr>
									<td class="titulo-separar"><p class="titulo alta">${productoDetalle.titulo}</p></td>
									<td>
										<ul class="conversion">
											<li class="soles">${simboloMoneda} ${productoDetalle.formatPrecioCatalogo}</li>
											<li class="decision">ó</li>
											<li class="puntos">${productoDetalle.formatPrecioPuntos}<br />Millas Benefit</li>
										</ul>
									</td>
								</tr>
							</table>
							<p class="precio">(Precio regular <span>${simboloMoneda} ${productoDetalle.producto.formatPrecioCompra}</span>)</p>
							<p class="accion">
								<c:if test="${sessionScope.sessionCliente ne null}">
									<span>Tienes ${cliente.totalPuntos} Millas Benefit<br />
									${labelPuntos}</span>
								</c:if>
								<a href="${pageContext.request.contextPath}/canjeProducto/paso1/${productoDetalle.keyItem}" class="boton">${boton}</a>
							</p>
						</section>
						<input type="hidden" id="imgPortada" value="${prop['url.imagen.producto']}${productoDetalle.producto.imagen1}" >
						<nav class="acceso-oportunidad">
							<a href="${pageContext.request.contextPath}/producto/oportunidad/${productoDetalle.keyItem}" class="activo"><span></span><span>LA OPORTUNIDAD</span></a>
							<a href="${pageContext.request.contextPath}/producto/${productoDetalle.keyItem}"><span></span><span>DETALLE</span></a>
<%-- 							<a href="${pageContext.request.contextPath}/producto/fotos/${productoDetalle.keyItem}"><span></span><span>FOTOS</span></a> --%>
						</nav>

						<img src="${prop['url.imagen.producto']}${productoDetalle.producto.imagen1}" class="central" alt="${productoDetalle.titulo}"/>
					</div>
				</div>
            </div>
		</div>
		
<script type="text/javascript">	
	$(document).ready(function(){
		activarMenu("compras");
	});
</script>		