<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="galeria">
	<section class="miga">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los beneficios</a>
					<span>&gt;</span>
					<a href="${pageContext.request.contextPath}/compras">Compras</a>
					<span>&gt;</span>
					<p class="activo">Fotos</p>
		</div>
		<div class="fondo"></div>
	</section>
	<!-- <div id="oportunidades"> -->
		<!-- <div id="oportunidad-1"> -->
			<div class="promocion-detalle">
				<div class="titulo-general">
					<p class="titulo">${productoDetalle.titulo}</p>
				</div>
				<div class="galeria">
					<div class="imagen-cargada">
						<div>
							<p class="texto">
								<span></span> <span></span>
							</p>
							<div class="fondo"></div>
						</div>
						<a href="#" class="ico-retroceder-foto ico-foto" onclick="return false;" ></a> <a href="#" onclick="return false;"  class="ico-avanzar-foto ico-foto"></a> <img src="${prop['url.imagen.producto']}${productoDetalle.producto.imagen1}" style="height: 238px; width: 410px;" />
					</div>
					<nav>
						<c:forEach var="imagen" items="${imagenes}">
							<a onclick="return false;"  href="#" data-img-g="<c:out value="${prop['url.imagen.producto']}${imagen}"/>" data-texto=""> <span></span> <img src="<c:out value="${prop['url.imagen.producto']}${imagen}"/>" height="100" width="115" alt="" style="width: 115px; height: 100px;" />
							</a>
						</c:forEach>
					</nav>
				</div>
			</div>
			<section class="para-clientes">
						<table class="titulo-soles">
								<tr>
									<td class="titulo-separar"><p class="titulo alta">${productoDetalle.titulo}</p></td>
									<td>
										<ul class="conversion">
											<li class="soles">${simboloMoneda} ${productoDetalle.formatPrecioCatalogo}</li>
											<li class="decision">&oacute;</li>
											<li class="puntos">${productoDetalle.formatPrecioPuntos}<br />Millas Benefit></li>
										</ul>
									</td>
								</tr>
							</table>
							<p class="precio">(Precio regular <span>${simboloMoneda} ${productoDetalle.producto.formatPrecioCompra} </span>)</p>
							<p class="accion">
								<c:if test="${sessionScope.sessionCliente ne null }">
									<span>Tienes ${cliente.totalPuntos} Millas Benefit><br />${labelPuntos}</span>
								</c:if>
								<a href="${pageContext.request.contextPath}/canjeProducto/paso1/${productoDetalle.keyItem}" class="boton">${boton}</a>
							</p>
						</section>
			<nav id="acceso">
				<a href="${pageContext.request.contextPath}/producto/oportunidad/${productoDetalle.keyItem}"><span></span><span>LA OPORTUNIDAD</span></a>
						<a href="${pageContext.request.contextPath}/producto/${productoDetalle.keyItem}"><span></span><span>DETALLE</span></a>
						<a href="${pageContext.request.contextPath}/producto/fotos/${productoDetalle.keyItem}" class="activo"><span></span><span>FOTOS</span></a>
			</nav>
		<!-- </div> -->
	<!-- </div>-- -->
</div>

<script type="text/javascript">
$(document).ready(function(){
	activarMenu("compras");
})
</script>
