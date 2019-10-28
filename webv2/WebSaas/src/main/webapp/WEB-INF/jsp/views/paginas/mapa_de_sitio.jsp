<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<!-- informacion -->
		<div id="mapa" class="limite">
			<div class="titulo-general">
				<h1>Mapa del sitio</h1>
			</div>
			<ul class="primer-nivel">
<!-- 				<li><a href="#">VIAJES</a></li> -->
<!-- 				<li> -->
<!-- 					<ul class="segundo-nivel"> -->
<!-- 						<li><a href="#">Vuelos</a></li> -->
<!-- 						<li><a href="#">Paquetes turísticos</a></li> -->
<!-- 						<li><a href="#">Hoteles</a></li> -->
<!-- 						<li><a href="#">Autos</a></li> -->
<!-- 					</ul> -->
<!-- 				</li> -->
<!-- 				<li><a href="#">ENTRETENIMIENTO Y RESTAURANTES</a></li> -->
<!-- 				<li> -->
<!-- 					<ul class="segundo-nivel"> -->
<!-- 						<li><a href="#">Destacados</ a></li> -->
<!-- 						<li><a href="#">Ir al cine</a></li> -->
<!-- 						<li><a href="#">Ver un espectáculo</a></li> -->
<!-- 						<li class="ultimo"><a href="#">Ver descuentos</a></li> -->
<!-- 					</ul> -->
<!-- 				</li> --> 
				<li><a href="${pageContext.request.contextPath}/viajes">VIAJES</a></li>
				<li><a href="${pageContext.request.contextPath}/compras">COMPRAS</a></li>
<!-- 				<li><a href="#">DESCUENTOS</a></li> -->
				<li>
					<c:choose>
						<c:when test="${sessionScope.sessionCliente!=null}">
							<a href="${pageContext.request.contextPath}/cliente/perfil">DATOS DEL USUARIO</a>
						</c:when>
						<c:otherwise>
							<a href="javascript:;" onclick="loginHandler()">DATOS DEL USUARIO</a>
						</c:otherwise>
					</c:choose>
				</li>
			</ul>
		</div>		
	