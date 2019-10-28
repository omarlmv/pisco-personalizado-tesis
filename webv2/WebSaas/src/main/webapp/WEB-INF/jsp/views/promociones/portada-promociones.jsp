<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>
<c:set var="isSession" value="false"></c:set>
<c:if test="${sessionScope.sessionCliente ne null }">
	<c:set var="isSession" value="true"></c:set>
</c:if>

<div id="promociones-page">
	<section class="miga">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los beneficios</a> <span>></span>
			<p class="activo">Promociones</p>
		</div>
		<div class="fondo"></div>
	</section>

	<jsp:include page="./banner-promociones.jsp" />
	
	<div id="promo-exclusivas" class="limite">
		<h3>
			<span>Con una tarjeta Interbank</span><br>
			<span>Disfruta de promociones exclusivas</span>
			<i class="sub-h3"></i>
		</h3>
		
		<section class="lista-promociones"></section>
		<section class="lista-promociones-responsive"></section>
		<div class="handler-article handler-article-responsive">
			<div class="handler-next icon-ico-bullet-right"></div>
			<div class="handler-prev icon-ico-bullet-left"></div>
		</div>
		
		<section class="sectionVerMas">
			<div class="boton-infinite">
				<a href="javascript:;" id="verMas" class="btn-infinite">Ver m&aacute;s <span class="ico-down"></span></a>
			</div>
		</section>
	</div>
	
	<c:if test="${not empty sessionCliente}">
		<div id="visa-premia" class="limite">
			<section class="lista-promo-tarjeta">
				<article class="promo-image">
					<div>
						<p>Siempre tienes m&aacute;s con</p>
						<h3>Visa Premia</h3>
					</div>
					<div>
						<img src="${prop['config.url.recursos.base.web']}images/promociones/visa-premia.png" alt="Promoción de tarjeta" />
					</div>
				</article>
			</section>
			<div class="handler-article">
				<div class="handler-next icon-ico-bullet-right"></div>
				<div class="handler-prev icon-ico-bullet-left"></div>
			</div>
		</div>
		
		<div id="cuenta-sueldo" class="limite">
			<section class="lista-promo-tarjeta">
				<article class="promo-image">
					<div>
						<p>Disfruta al m&aacute;ximo con tu</p>
						<h3>Cuenta Sueldo Interbank</h3>
					</div>
					<div>
						<img src="${prop['config.url.recursos.base.web']}images/promociones/cuenta-sueldo.png" alt="Promoción de tarjeta" />
					</div>
				</article>
			</section>
			<div class="handler-article">
				<div class="handler-next icon-ico-bullet-right"></div>
				<div class="handler-prev icon-ico-bullet-left"></div>
			</div>
		</div>
	</c:if>
	
	<div id="promo-experiencias" class="limite">
		<h3>
			<span>y siempre</span><br>
			<span>Encuentra el mejor momento para vivir la<br>mejor experiencia</span>
			<i class="sub-h3"
			></i>
		</h3>
		
		<section class="lista-experiencias"></section>
		
		<a href="${pageContext.request.contextPath}/promociones/categorias" class="boton-todas">Todas las promociones</a>
	</div>
	
	<c:choose>
		<c:when test="${not empty sessionCliente}">
			<div id="parte-benefit" class="limite">
				<img src="${prop['config.url.recursos.base.web']}images/promociones/parte-benefit.jpg" alt="Encuentra tu tarjeta" />
				<div>
					<div class="detalle-tarjeta">
						<h3>Eres parte de Benefit</h3>
						<p>Usa tu tarjeta, acumula millas por tus compras y cámbialas por viajes y productos exclusivos.</p>
					</div>
					<a href="https://interbank.solicitudes.pe/tarjeta-credito-benefit" target="_blank">Conoce m&aacute;s</a>
				</div>
			</div>
			<div id="bono-benefit" class="limite">
				<div class="detalle-bono">
					<span>No desaproveches esta oportunidad!</span>
					<h3>Ll&eacute;vate un bono de 1000 Millas Benefit</h3>
					<p>Participa usando tus tarjetas Interbank en tiendas por departamento, consumiendo S/ 50 los s&aacute;bados y domingos de octubre con tus Tarjetas Interbank.</p>
					<a href="">Quiero participar</a>
				</div>
				<img src="${prop['config.url.recursos.base.web']}images/promociones/bono-benefit.jpg" alt="Bono de Millas Benefit" />
			</div>
		</c:when>
		<c:otherwise>
			<div id="promocion-tarjeta" class="limite">
				<p>Imag&iacute;nate con una</p>
				<select id="cboImaginate">
					<option>American Express</option>
					<option>Visa</option>
					<option>Mastercard</option>
				</select>
				
				<section class="lista-promo-tarjeta">
					<article class="promo-image">
						<img src="${prop['config.url.recursos.base.web']}images/promociones/promo-tarjeta.png" alt="Promoción de tarjeta" />
					</article>
				</section>
				<div class="handler-article">
					<div class="handler-next icon-ico-bullet-right"></div>
					<div class="handler-prev icon-ico-bullet-left"></div>
				</div>
			</div>
			
			<jsp:include page="../conoce_bim.jsp" />
		</c:otherwise>
	</c:choose>
	
</div>

<script id="template-promocion" type="text/x-handlebars-template">
	<article class="promo-simple">
		<a href=""><img src="" alt="Imagen promocion" /></a>
		<div class="descripcion">
			<p class="categoria">{{recortarTitulo categoria 20}}</p>
			<a href=""><h3>{{recortarTitulo titulo 32}}</h3></a>
		</div>
	</article>
</script>

<script id="template-promocion-doble" type="text/x-handlebars-template">
	<article class="promo-doble">
		<a href=""><img src="" alt="Imagen promocion doble" /></a>
		<div class="descripcion">
			<p class="categoria">{{categoria}}</p>
			<a href=""><h3>{{titulo}}</h3></a>
		</div>
	</article>
</script>

<script id="template-experiencias" type="text/x-handlebars-template">
	<article>
		<a href="">
			<img src="{{imageSource}}" alt="{{titulo}}" />
			<h3>{{titulo}}</h3>
		</a>
	</article>
</script>

<script id="template-promo-tarjeta" type="text/x-handlebars-template">
	<article>
		<a href=""><img src="" alt="{{titulo}}" /></a>
		<div class="detalle">
			<p class="categoria">{{categoria}}</p>
			<a href=""><h3>{{recortarTitulo titulo 36}}</h3></a>
		</div>
	</article>
</script>

<script type="text/javascript">
	$(document).on('ready', function(){
		var promociones = new Promociones();
		promociones.init();
		activarMenu("promociones");
	});
</script>