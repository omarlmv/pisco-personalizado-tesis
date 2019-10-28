<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!-- informacion -->
		<section id="campanas" class="limite">
			<div class="titulo-general">
				<h1>Mis campañas</h1>
				<span></span>
			</div>
			<div class="detalle">
		<c:forEach items="${requestScope.promocionForm.listaPromociones}">
			<article>
				<div id="campana-1" class="unidad">
					<div>
<%-- 						<p class="texto" style="background: url('${}');">Quintuplica tus puntos en Junio</p> --%>
						<p class="fondo"></p>
					</div>
				</div>
				<a href="#">Me interesa</a>
			</article>
		</c:forEach>
		<!-- 				<article> -->
<!-- 					<div id="campana-2" class="unidad"> -->
<!-- 						<div> -->
<!-- 							<p class="texto">Tarjeta Premia: elige tu rubro de devolución</p> -->
<!-- 							<p class="fondo"></p> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<a href="#">Me interesa</a> -->
<!-- 				</article> -->
<!-- 				<article> -->
<!-- 					<div id="campana-3" class="unidad"> -->
<!-- 						<div> -->
<!-- 							<p class="texto">Tarjeta Cashback: sólo en Junio, 5% de devolución en restaurantes</p> -->
<!-- 							<p class="fondo"></p> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<a href="#">Me interesa</a> -->
<!-- 				</article> -->
			</div>
		</section>
		<!-- /informacion -->