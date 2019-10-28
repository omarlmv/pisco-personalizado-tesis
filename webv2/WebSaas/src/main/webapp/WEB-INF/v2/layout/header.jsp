<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>
<c:set var="isSession" value="false"></c:set>
<c:if test="${sessionScope.sessionCliente ne null }">
	<c:set var="isSession" value="true"></c:set>
</c:if>

<header>
	<div class="outContainer">
		<a href="${pageContext.request.contextPath}/"><img src="${prop['config.url.recursos.base.web']}v2/images/layout/logo-benni.png" alt="Interbank Benefit"></a>
		<div id="log-reg">
			<a href="#" id="btn-responsive-menu"><i class="fa fa-bars"></i><span>Men&uacute;</span></a>
			<c:choose>
				<c:when test="${not empty sessionCliente}">
					<div class="log">
						<a><i class="icon-plane"></i> Tienes ${sessionScope.sessionCliente.formatTotalPuntos} Millas Benefit</a>
						<a id="show-opciones"><i class="icon-user"></i> ${sessionCliente.primerNombre} ${sessionCliente.apellidos} <i class="icon-down"></i></a>
						<ul id="menu-usuario-opciones">
							<li><a href="${pageContext.request.contextPath}/cliente/perfil" class="perfil"><span>Mi perfil</span></a></li>
							<li><a href="${pageContext.request.contextPath}/cliente/seguimientoCompras"><span>Seguimiento a mis compras</span></a></li>
							<li class="cerrarSesion"><a href="javascript:;"><span><i class="icon-close"></i>Cerrar sesión</span></a></li>
						</ul>
					</div>
				</c:when>
				<c:otherwise>
					<div class="no-log">
						<a href="#" id="registro">Reg&iacute;strate</a>
						<a href="#" id="login">Iniciar Sesi&oacute;n</a>
					</div>
				</c:otherwise>
			</c:choose>
			<ul id="responsive-menu">
				<li><a data-menu="home" href="${pageContext.request.contextPath}/">Inicio</a></li>
				<li><a data-menu="viajes" href="${pageContext.request.contextPath}/viajes">Viajes</a></li>
				<li><a data-menu="compras" href="${pageContext.request.contextPath}/compras">Compras</a></li>
				<li><a data-menu="promociones" href="${pageContext.request.contextPath}/promociones-home">Promociones</a></li>
				<li><a data-menu="canje-locales" href="${pageContext.request.contextPath}/canje-en-locales-afiliados">Canje en locales afiliados</a></li>
			</ul>
		</div>
	</div>
	<div>
		<ul id="menu" class="outContainer">
			<li><a id="home" href="${pageContext.request.contextPath}/">Inicio</a></li>
			<li><a id="viajes" href="${pageContext.request.contextPath}/viajes">Viajes</a></li>
			<li><a id="compras" href="${pageContext.request.contextPath}/compras">Compras</a></li>
			<li><a id="promociones" href="${pageContext.request.contextPath}/promociones-home">Promociones</a></li>
			<li><a id="canje-locales" href="${pageContext.request.contextPath}/canje-en-locales-afiliados">Canje en locales afiliados</a></li>
		</ul>
	</div>
</header>