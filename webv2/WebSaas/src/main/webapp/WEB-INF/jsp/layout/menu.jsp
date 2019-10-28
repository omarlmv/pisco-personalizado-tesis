<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- menu principal -->
<div id="menu-principal">
	<a href="#" class="menu"><span>Men&uacute;</span></a>
	<div id="menu-principal-opciones">
		<nav class="alineacion">
			<ul class="principal">
				<li class="menu-item"><a href="${pageContext.request.contextPath}/" id="home" class="menu-link activo" automation="inicio"><span>Inicio</span></a></li>
				<li class="menu-item"><a href="${pageContext.request.contextPath}/viajes" id="viajes" class="menu-link" automation="viajes"><span>Viajes</span></a></li>
				<li class="menu-item">
					<a href="${pageContext.request.contextPath}/compras" id="compras" class="menu-link" automation="compras"><span>Compras</span></a>
					<ul class="submenu submenu-compras">
						<c:forEach items="${listaCategorias}" var="lista">
							<c:if test="${lista.esAgrupador != true}">
								<c:if test="${idCategoria eq lista.idCategoria}">
									<li>
										<a id="${lista.idCategoria}" href="${pageContext.request.contextPath}/compras/${lista.codigo}" class="activo" automation="${lista.codigo}">${lista.nombreCategoria}</a>
									</li>
								</c:if>
								<c:if test="${idCategoria ne lista.idCategoria}">
									<li>
										<a id="${lista.idCategoria}" href='${pageContext.request.contextPath}/compras/${lista.codigo}' automation="${lista.codigo}">${lista.nombreCategoria}</a>
									</li>
								</c:if>
							</c:if>
						</c:forEach>
						
						
						<li class="agrupadores">
							<ul>
								<li></li>
								<c:forEach items="${listaCategorias}" var="lista">
									<c:if test="${lista.esAgrupador == true}">
										<c:if test="${idCategoria eq lista.idCategoria}">
											<li>
												<a id="${lista.idCategoria}" href="${pageContext.request.contextPath}/compras/${lista.codigo}" class="activo" automation="${lista.codigo}">${lista.nombreCategoria}</a>
											</li>
										</c:if>
										<c:if test="${idCategoria ne lista.idCategoria}">
											<li>
												<a id="${lista.idCategoria}" href='${pageContext.request.contextPath}/compras/${lista.codigo}' automation="${lista.codigo}">${lista.nombreCategoria}</a>
											</li>
										</c:if>
									</c:if>
								</c:forEach>
							</ul>
						</li>
					</ul>
				</li>
				<li class="menu-item">
					<a href="${pageContext.request.contextPath}/descuento" id="descuentos" class="menu-link" automation="descuentos"><span>Descuentos</span></a>
					<ul class="submenu">
						<c:forEach items="${listaCategoriasDescuento }" var="listaD">
							<c:if test="${idCategoria eq listaD.idCategoria}">
								<li>
									<a id="${listaD.idCategoria}" href="${pageContext.request.contextPath}/descuento/categoria/${listaD.codigo}" class="activo" automation="${listaD.codigo}">${listaD.nombreCategoria}</a>
								</li>
							</c:if>
							<c:if test="${idCategoria ne listaD.idCategoria}">
								<li>
									<a id="${listaD.idCategoria}" href='${pageContext.request.contextPath}/descuento/categoria/${listaD.codigo}' automation="${listaD.codigo}">${listaD.nombreCategoria}</a>
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</li>

				<li class="menu-item">				
				 	<a href="${pageContext.request.contextPath}/promociones" class="menu-link" id="promociones" automation="promociones"><span>Promociones</span></a> 
				</li>

				<li class="menu-item">
					<a href="${pageContext.request.contextPath}/vales" class="menu-link" id="vales" automation="vales-digitales"><span>Zona vales</span></a>
					
				</li>
				<li class="menu-item">				
					<a href="${pageContext.request.contextPath}/canje-en-locales-afiliados" class="menu-link" id="marcas" automation="marca"><span>Canje en locales afiliados</span></a>
				</li>
			</ul>
		</nav>
	</div>
</div>
<!-- /menu principal -->
<c:if test="${flashMensajeError ne null}">
<div style="background-color: #f2dede;border: 2px solid #ebccd1; height: 40px;position: relative;TEXT-ALIGN:CENTER;PADDING-TOP:20PX;COLOR:#a94442;"><c:out value="${flashMensajeError}" escapeXml="false"></c:out></div>
</c:if>
