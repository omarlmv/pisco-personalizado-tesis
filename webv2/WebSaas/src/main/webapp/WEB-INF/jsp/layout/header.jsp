<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="idHeaderEvento">
	<i class="material-icons">close</i>
</div>
<!-- header -->
<header>
	<section class="zone-navigation">
		<div class="sub-zone">
			<a href="${pageContext.request.contextPath}/" style=""><img class="logo" alt="Logo Interbank benefit" src="${prop['config.url.recursos.base.web']}static/images/logo.png" /></a>
			<c:if test="${sessionScope.sessionCliente!=null}">
				<p class="log-responsive">
					<b class="text-null">Hola, <span class="capitalize get"><c:out value="${sessionCliente.primerNombre} ${sessionCliente.apellidos}"></c:out></span></b>
					<br>
					Tienes
					<b>${sessionScope.sessionCliente.formatTotalPuntos}</b>
					<b>Millas Benefit</b>
					<a class="car-green">
						<i class="material-icons">shopping_cart</i>
						<span>()</span>
					</a>
				</p>
			</c:if>
			<c:if test="${sessionScope.sessionCliente==null}">
				<div class="buttons">
					<a href="javascript:void(0)" class="registrar link" id="btnRegister">Crear cuenta</a>
					<a href="javascript:void(0)" class="iniciar btnLoginAll" src='${prop["config.oauth.api.url.login"]}?id=${prop["config.oauth.clientId"]}&amp;redir=${prop["config.oauth.login.redirectUrl"]}&amp;error=${prop["config.oauth.login.redirectUrl.error"]}'>Ingresar</a>
					<a href="javascript:void(0)" class="cart-button"><i class="material-icons">shopping_cart</i><i class="count"></i></a>
				</div>
			</c:if>
		</div>
		<div class="buscador">
			<i class="material-icons burger-menu">menu</i>
			<label>
				<input type="text" id="searchAll" placeholder="Busca tus productos aqu&iacute;">
				<div>
					<i class="material-icons">search</i>
				</div>
				<ul id="myList"></ul>
			</label>
		</div>
		<ul class="menu">
			<li><a class="path-viajes" href="${pageContext.request.contextPath}/viajes">Viajes</a></li>
			<li>
				<a class="path-productos" href="${pageContext.request.contextPath}/categorias">Productos</a>
				<div class="submenu" id="menu-departamentos">
					<script id="templateDepartamentos" type="text/x-handlebars-template">
						<ul id="menu-departamentos" class="menu-categoria">
							{{#each departamento as |departamento|}}
								<li>
									<a class="path-productos" href="${pageContext.request.contextPath}/c/{{departamento.codigo}}">
										<span>{{departamento.nombre}}</span>
										{{#hijosDepartamento departamento.listaCategoriaArbol}}
										<i class="material-icons">keyboard_arrow_right</i>
										{{/hijosDepartamento}}
									</a>
								</li>
							{{/each}}
						</ul>
						{{#each departamento as |departamento|}}
							{{#if departamento.listaCategoriaArbol}}
								<div>
									{{#each departamento.listaCategoriaArbol as |categoria|}}
										<a href="${pageContext.request.contextPath}/c/{{categoria.codigo}}">{{categoria.nombre}}</a>
									{{/each}}
								</div>
							{{else}}
								<div></div>
							{{/if}}
						{{/each}}
					</script>
				</div>
			</li>
			<li>
				<a class="path-vales" href="${pageContext.request.contextPath}/vales">Zona vales</a>
				
			</li>
<%-- 			<li><a class="path-experiencias" href="${pageContext.request.contextPath}/eventos">Eventos</a></li> --%>
			<li><a class="path-locales" href="${pageContext.request.contextPath}/canje-en-locales-afiliados">Canje en locales afiliados</a></li>
		</ul>
	</section>
	<section class="user-zone">
		<c:if test="${sessionScope.sessionCliente!=null}">
			<div class="logued">
				<p class="millas">
					<b>Tienes ${sessionScope.sessionCliente.formatTotalPuntos}</b>
					<span><b>Millas Benefit</b></span>
				</p>
				<div class="dropdown-login">
					<p class="drop-button usuario">
						<span class="capitalize"><c:out value="${sessionCliente.primerNombre} ${sessionCliente.apellidos}"></c:out></span>
					</p>
					<p>&#47;</p>
					<div class="car-green">
						<i class="material-icons">shopping_cart</i>
						<p class="usuario">Carrito</p>
						<p class="items-shopp">()</p>
					</div>
					<ul class="drop-list">
						<li class="drop-item">
							<a href="${pageContext.request.contextPath}/cliente/perfil"><span>Mi Perf&iacute;l</span></a>
						</li>
						<li class="drop-item">
							<a href="${pageContext.request.contextPath}/mis-canjes">Mis
								canjes</a>
						</li>
						<li class="drop-item cerrarSesion">
							<a><i class="material-icons">exit_to_app</i>Cerrar sesi&oacute;n</a>
						</li>
					</ul>
				</div>
			</div>
		</c:if>
		<div class="cart">
			<div class="cart-shop">
				<div class="cart-middle" data-attr="cart">
					<i class="material-icons close">close</i>
					<p class="text-shop"><i class="material-icons">shopping_cart</i> Tus compras</p>
					<div class="cart-button">
						<a href="${pageContext.request.contextPath}/carrito" class="cart-green carritoProductos" type="button">Ir a carrito de productos <span class="cart-number-two"> (0)</span></a>
						<a class="cart-grey carritoProductos" type="button">Ir a carrito de productos <span class="cart-number"> (0)</span></a>
<%-- 						<a href="${pageContext.request.contextPath}/vales-digitales/pendientes" class="cart-green carritoEvales" type="button">Ir a carrito de vales digitales <span class="cart-number-two"> (0)</span></a> --%>
<!-- 						<a class="cart-grey carritoEvales" type="button">Ir a carrito de vales digitales <span class="cart-number"> (0)</span></a> -->
					</div>
					<div class="cart-none">
						<i class="material-icons">add_shopping_cart</i>
						<p>No tienes compras guardadas</p>
					</div>
				</div>
			</div>
		</div>
		<c:if test="${sessionScope.sessionCliente==null}">
			<div class="no-logued">
				<a href="javascript:void(0)" class="registrar link" id="btnRegister">Crear cuenta</a>
				<a href="javascript:void(0)" class="iniciar btnLoginAll" src='${prop["config.oauth.api.url.login"]}?id=${prop["config.oauth.clientId"]}&amp;redir=${prop["config.oauth.login.redirectUrl"]}&amp;error=${prop["config.oauth.login.redirectUrl.error"]}'>Ingresar</a>
				<a href="javascript:void(0)" class="cart-btn"><i class="material-icons">shopping_cart</i><i class="count"></i></a>
			</div>
		</c:if>
	</section>
	<div class="menu-responsive">
		<i class="material-icons close">close</i>
		<div class="active">
			<div class="user-zone">
				<c:if test="${sessionScope.sessionCliente==null}">
					<small>Se parte de Interbank Benefit</small>
					<div>
						<a href="javascript:void(0)" class="registrar link" id="btnRegister">Reg&iacute;strate</a>
						<a href="javascript:void(0)" class="iniciar btnLoginAll" src='${prop["config.oauth.api.url.login"]}?id=${prop["config.oauth.clientId"]}&amp;redir=${prop["config.oauth.login.redirectUrl"]}&amp;error=${prop["config.oauth.login.redirectUrl.error"]}'>Ingresar</a>
					</div>
				</c:if>
				<c:if test="${sessionScope.sessionCliente!=null}">
					<p>Hola, <span class="capitalize"><c:out value="${sessionCliente.primerNombre} ${sessionCliente.apellidos}"></c:out></span></p>
					<small>Tienes ${sessionScope.sessionCliente.formatTotalPuntos} Millas Benefit</small>
					<div>
						<a href="${pageContext.request.contextPath}/cliente/perfil">Mi perf&iacute;l</a>
						<a href="${pageContext.request.contextPath}/mis-canjes">Mis canjes</a>
					</div>
				</c:if>
			</div>
			<div class="routes">
				<ul>
					<li><a href="${pageContext.request.contextPath}/"><span>Inicio</span></a></li>
					<li><a href="${pageContext.request.contextPath}/viajes"><i class="material-icons">airplanemode_active</i><span>Viajes</span></a></li>
					<li id="fa" class="open-routes" data-route="productos">
						<a href="">
							<i class="material-icons">shopping_basket</i>
							<span>Productos</span>
							<i class="material-icons arrow">keyboard_arrow_right</i>
						</a>
					</li>
<!-- 					<li class="open-routes" data-route="vales-digitales"> -->
<%-- 						<a href="${pageContext.request.contextPath}/vales"> --%>
<!-- 							<i class="material-icons">loyalty</i> -->
<!-- 							<span>Zona vales</span> -->
<!-- 							<i class="material-icons arrow">keyboard_arrow_right</i> -->
<!-- 						</a> -->
<!-- 					</li> -->
					<li><a href="${pageContext.request.contextPath}/vales"><i class="material-icons">loyalty</i><span>Zona vales</span></a></li>
<%-- 					<li><a href="${pageContext.request.contextPath}/eventos"><i class="material-icons">local_activity</i><span>Eventos</span></a></li> --%>
					<li>
						<a href="${pageContext.request.contextPath}/canje-en-locales-afiliados">
							<i class="material-icons">store</i>
							<span>Canje en locales afiliados</span>
						</a>
					</li>
				</ul>
			</div>
			<div class="subroutes productos">
				<p class="back-routes">
					<i class="material-icons">arrow_back</i>
					Regresar
				</p>
				<p>
					<a href="${pageContext.request.contextPath}/categorias">Productos</a>
				</p>
				<ul id="menu-responsive-departamentos"></ul>
			</div>
			<div class="subroutes vales-digitales">
				<p class="back-routes">
					<i class="material-icons">arrow_back</i>
					Regresar
				</p>
				<p>
					<a href="${pageContext.request.contextPath}/vales">Zona vales</a>
				</p>
				
			</div>
			<c:if test="${sessionScope.sessionCliente!=null}">
				<a class="logout cerrarSesion"><i class="material-icons">exit_to_app</i>Cerrar sesi&oacute;n</a>
			</c:if>
		</div>
	</div>	
</header>
<div class="preload"></div>

<c:if test="${flashMensajeError ne null}">
<div style="background-color: #f2dede;border: 2px solid #ebccd1; height: 40px;position: relative;TEXT-ALIGN:CENTER;PADDING-TOP:20PX;COLOR:#a94442;"><c:out value="${flashMensajeError}" escapeXml="false"></c:out></div>
</c:if>

<script type="text/javascript">
	$(document).ready(function(){
		var header = new Header();
		header.init();
		
		var buscador = new Buscador();
		buscador.init();
	});
</script>
