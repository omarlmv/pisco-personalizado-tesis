<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>
<c:set var="isSession" value="false"></c:set>
<c:if test="${sessionScope.sessionCliente ne null }">
	<c:set var="isSession" value="true"></c:set>
</c:if>

<div id="promociones-categorias-page">
	<section class="miga">
		<div class="detalle">
			<a href="${pageContext.request.contextPath}/">Todos los beneficios</a> <span>></span>
			<a href="${pageContext.request.contextPath}/promociones">Promociones</a> <span>&gt;</span>
			<p class="activo">Todas las promociones</p>
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
	</div>
	
	<div id="tab-promocion" class="tab-cnt">
		<ul class="tabs tab-promo limite">
			<li class="tab-active">Buscar por categor&iacute;a</li>
			<li>Buscar por estilo de vida</li>
		</ul>
	</div>
	
	<div id="tab-categoria-promocion" class="limite">
		<ul class="lista-categorias">
			<li id="todas" class="categoria categoria-activa">Todas<span class="fa fa-plus"></span></li>
			<li class="categoria">Conciertos<span class="fa fa-plus"></span></li>
			<li class="categoria">Restaurantes y gourmet<span class="fa fa-plus"></span></li>
			<li class="categoria">Moda y accesorios<span class="fa fa-plus"></span></li>
			<li class="categoria">Fragancias y belleza<span class="fa fa-plus"></span></li>
			<li class="categoria">Entretenimiento<span class="fa fa-plus"></span></li>
			<li class="categoria">Regalos y detalles<span class="fa fa-plus"></span></li>
		</ul>
		<div id="lista-categorias-responsive">
			<label><input type="checkbox" id="todas"/><span>Todas</span></label>
			<label><input type="checkbox" /><span>Conciertos</span></label>
			<label><input type="checkbox" /><span>Restaurantes y gourmet</span></label>
			<label><input type="checkbox" /><span>Moda y accesorios</span></label>
			<label><input type="checkbox" /><span>Fragancias y belleza</span></label>
			<label><input type="checkbox" /><span>Entretenimiento</span></label>
			<label><input type="checkbox" /><span>Regalos y detalles</span></label>
		</div>
	</div>
	
	<div class="select limite">
		<form id="formFiltros" method="get">
			<select id="cboFiltroPromocion" name="ordenar" class="filtro-ordenar comprar" automation="cboOrdenarPromociones">
				<option value="ID">Ordenar por</option>
				<option value="NOMBRE_ASC">Nombre (A a Z)</option>
				<option value="NOMBRE_DESC">Nombre (Z a A)</option>
			</select>
		</form>
	</div>
	
	<div id="section-promociones" class="limite">
		<section id="promociones-categoria" class="limite"></section>
		<div class="handler-article">
			<div class="handler-next icon-ico-bullet-right"></div>
			<div class="handler-prev icon-ico-bullet-left"></div>
		</div>
	</div>
	
	<section class="sectionVerMas">
		<div class="boton-infinite">
			<a href="javascript:;" id="verMas" class="btn-infinite">Ver m&aacute;s <span class="ico-down"></span></a>
		</div>
	</section>
	
	<c:if test="${empty sessionCliente}">
		
		<div id="tarjeta-disfruta" class="limite">
			<h3>
				<span>Disfruta de estos y otros beneficion con:</span>
				<i class="sub-h3"></i>
			</h3>
			<section id="lista-tarjetas"></section>
			<div class="handler-article">
				<div class="handler-next icon-ico-bullet-right"></div>
				<div class="handler-prev icon-ico-bullet-left"></div>
			</div>
		</div>
		
		<jsp:include page="../conoce_bim.jsp" />
	</c:if>
	
</div>

<script id="template-promocion-categoria" type="text/x-handlebars-template">
	<article>
		<a href=""><img src="" alt="Imagen de promoción por categoria" /></a>
		<div class="descripcion">
			<h3>{{titulo}}</h3>
			<p>{{descripcion}}</p>
		</div>
	</article>
</script>

<script id="template-tarjeta-disfruta" type="text/x-handlebars-template">
	<article>
		<div class="img-tarjeta bloque"><img src="${prop['config.url.recursos.base.web']}images/promociones/{{image}}" alt="{{nombre}}" /></div>
		<div class="detalle">
			<h3>{{nombre}}</h3>
			<p>{{descripcion}}</p>
		</div>
	</article>
</script>

<script type="text/javascript">
	$(document).on('ready', function(){
		var promocionCategoria = new PromocionCategoria();
		promocionCategoria.init();
		activarMenu("promociones");
	});
</script>