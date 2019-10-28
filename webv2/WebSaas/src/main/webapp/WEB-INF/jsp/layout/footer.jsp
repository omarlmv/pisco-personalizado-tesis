<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- footer -->
<footer id="footer-waypoint">
	<section class="limite">
		<h2 class="cssOcultarCompras">Productos y Servicios:</h2>
		<nav class="productos-servicios cssOcultarCompras">
			<a href="${pageContext.request.contextPath}/categorias">Compras</a> <a href="${pageContext.request.contextPath}/viajes">Viajes</a>
		</nav>
		<div class="acceso-rapido">
			<nav>
				<ul class="principal">
					<li><a href="${pageContext.request.contextPath}/que-es-interbank-benefit">Qu&eacute; es Interbank Benefit </a></li>
<%-- 					<li><a href="${pageContext.request.contextPath}/preguntas-frecuentes">Preguntas frecuentes</a></li> --%>
<%-- 					<li><a href="${pageContext.request.contextPath}/conocer-uso-de-millas">Conocer sobre el uso de Millas Benefit </a></li> --%>
<%-- 					<li><a href="${prop['config.url.recursos.base.web']}static/que-es-bim/pdf/canje_en_establecimientos_new.pdf" target="_blank" class="btn__ibkbenefit gonow__btn">Canje en tienda</a></li> --%>
				</ul>
				<ul class="principal left">
					<li><a href="${pageContext.request.contextPath}/terminos-y-condiciones">T&eacute;rminos y condiciones</a></li>
					<li><a href="https://www.interbank.com.pe/formulario-contacto" target="_blank"><span class="ico-reclamaciones"></span> Libro de reclamaciones</a></li>
				</ul>
			</nav>
		</div>
		<div class="telefono">
			<p>Si tienes dudas o consultas ll&aacute;manos a los siguientes números</p>
			<ul>
				<li>(01) 311- 9020</li>
				<li><span>(Lima)</span></li>
			</ul>
			<p class="horario-atencion">Horario de atenci&oacute;n</p>
			<p class="hora">8:00 am - 8:00 pm</p>
<!-- 			<ul> -->
<!-- 				<li>0801-00802</li> -->
<!-- 				<li><span>(Provincias)</span></li> -->
<!-- 			</ul> -->
		</div>
		<ul class="logo-footer">
			<li>
				<a href="${pageContext.request.contextPath}/"> <img width="200" height="32" alt="Logo Footer Interbank Benefit" src="${prop['config.url.recursos.base.web']}static/images/logo-footer.png"/></a>
			</li>
			<li class="copyright">© Copyright ${prop['session_anio_copyright']} - Todos los derechos reservados</li>
		</ul>
	</section>
	<div style="display: none;" id="success-logout-ibk"></div>
	<c:set var="cssModalBienvenida" value=""></c:set>
	<c:if test="${sessionScope.sesionBienvenida eq 'active'}">
	<c:set var="cssModalBienvenida" value="active"></c:set>
	</c:if>
	<section class="bienvenida-modal  ${cssModalBienvenida}">
		<div class="bienvenida-modal-cell">
			<div class="bienvenida-modal-overlay"></div>
			<div class="bienvenida-modal-wancho">
				<div class="bienvenida-modal-cerrar"></div>
				<div class="bienvenida-modal-box">
					<h3>¡Bienvenido a Interbank Benefit!</h3>
					<p>Conoce el nuevo programa de recompensas, exclusivo para clientes Interbank, que une los Puntos Interbank y Membership Rewards en <strong>Millas Benefit</strong>.</p>
					<p>¿Estás listo para disfrutarlo?</p>
					<div class="bienvenida-modal-btn">
						<a href="${pageContext.request.contextPath}/que-es-interbank-benefit">¡Descubre c&oacute;mo aqu&iacute;!</a>
					</div>
				</div>
			</div>
		</div>
	</section>
</footer>
<div id="idFooterEvento">
	
</div>				
<!-- /footer -->
<!-- BOTON TOP -->
<!-- <a href="#" class="back-top icon-down-2"></a> -->
<!-- modal sesion -->

<!-- <div class="background-login"> -->
<%-- 	<div class="cont-login" data-url='${prop["config.oauth.api.url.login"]}?id=${prop["config.oauth.clientId"]}&amp;redir=${prop["config.oauth.login.redirectUrl"]}&amp;error=${prop["config.oauth.login.redirectUrl.error"]}'> --%>
<!-- 		<span class="cargar-login"></span> -->
<!-- 		<i class="close-login material-icons">close</i> -->
<!-- 		<iframe id="iframe-login" width="100%" height="100%" scrolling="auto" frameborder="0" onload="onIframeLoad();"></iframe> -->
<!-- 	</div> -->
<!-- </div> -->

<div class="pop-login" style="display: none" data-url='${prop["config.oauth.api.url.login"]}?id=${prop["config.oauth.clientId"]}&amp;redir=${prop["config.oauth.login.redirectUrl"]}&amp;error=${prop["config.oauth.login.redirectUrl.error"]}'>
	<span class="cargar-login"></span>
	<div class="pop-login-overlay"></div>
	<div class="pop-login-box">
		<div class="pop-login-ctn">
			<div class="pop-login-cerrar"></div>
			<iframe width="100%" height="100%" scrolling="auto" frameborder="0" onload="onIframeLoad();"></iframe>
		</div>
	</div>
</div>