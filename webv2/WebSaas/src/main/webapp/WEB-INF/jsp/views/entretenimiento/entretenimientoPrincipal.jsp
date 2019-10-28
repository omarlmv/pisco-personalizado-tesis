<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="sessionCliente" value="${sessionScope.sessionCliente}"></c:set>
<c:set var="isSession" value="false" ></c:set>
<c:if test="${sessionScope.sessionCliente ne null }">
	<c:set var="isSession" value="true" ></c:set>
</c:if>

		
		<nav id="subopciones-menu">
			<a id="0" href="${pageContext.request.contextPath}/entretenimiento" <c:if test="${idCategoria == 0}">class="activo"</c:if> ><span></span><span>Destacados</span></a>
				<c:forEach items="${entretenimiento.categoriaEntretenimiento}"  var="lista" >
				     <a id="${lista.id}"  href='${pageContext.request.contextPath}/entretenimiento/${lista.codigo}' 
				     <c:if test="${categoria ne null && categoria.codigo eq lista.codigo}">class="activo"</c:if>><span></span><span>${lista.etiqueta}</span></a>
				     					
				</c:forEach>
		</nav>
		<!-- /subopciones -->

		<!-- central -->
		<div id="contenedor" class="flotante-explora slider-foto">

			<!-- menu slider -->
			<section id="slidersHead">
			<nav class="slider-menu">
			<a href="javascript:void(0);" data-slider-descuento="slider1" class="activo" style="display: none;" id="sli1"></a> 
			<a href="javascript:void(0);" data-slider-descuento="slider2" style="display: none;" id="sli2"></a> 
			<a href="javascript:void(0);" data-slider-descuento="slider3" style="display: none;" id="sli3"></a>
			</nav>
			</section>
			<!-- /menu slider -->

			<!-- miga -->
			<section class="miga miga-inferior">
				<div class="detalle">
					<a href="${pageContext.request.contextPath}/">Todos los beneficios</a>
					<span>&gt;</span>
					<c:if test="${categoria ne null}">
					
					  <a href="${pageContext.request.contextPath}/entretenimiento">Entretenimiento</a>
					 <span>&gt;</span>
						<p class="activo"><p class="activo">${categoria.nombreCategoria}</p>
					</c:if>
					<c:if test="${idCategoria eq 0}">
						<p class="activo">Entretenimiento</p>
					</c:if>
				</div>
			</section>
			<!-- /miga -->
			<div id="slider1" class="slider-grupo img-fondo">
			</div>
			<div id="slider2" class="slider-grupo img-fondo">
			</div>
			<div id="slider3" class="slider-grupo img-fondo">
			</div>
			

	
	<c:if test="${sessionCliente ne null}">
			<section class="flotante-principal">
				<div class="resaltar">
					<ul>
						<li><span class="oportunidades-exclusivas"></span></li>
						<!-- <li><a href="#">Oportunidades exclusivas para ti</a></li> -->
						<li><a href="${pageContext.request.contextPath}/">Ofertas exclusivas para ti</a></li>
						
					</ul>
				</div>
				<div class="resumen">
					<div>
						<p class="arrow-explora"></p>
						<div class="detalle">
							<div class="explora">
								<a href="#lista-entretenimientos"><p>Explora todos los beneficios</p>
								<p>(<span class="total-beneficios"></span>) en 'Entretenimiento y Restaurantes'</p></a>
							</div>
						</div>
					</div>
				</div>
			</section>
		</c:if>
			<!-- /flotante -->

		</div>
		<!-- /central -->

		<!-- ofertas -->
		<div class="limite oferta">
			<!-- <div class="titulo-general">
			<script id="cabecera" type="text/x-handlebars-template"> 
				<h1>M&aacute;s oportunidades en compras ({{cantLista}})</h1>
			</script>
			</div>-->
			<div class="entretenimiento-content cf">
				<div class="titulo-general">
				<h1>M&aacute;s ofertas en entretenimientos (<span class="oportunidades-entretenimientos"></span>)</h1>
				<span></span>	
				</div>
				<select id="cbo-filtro-ordenar" class="filtro-ordenar compra">
						<option value="none">Ordenar por</option>
						<option value="PRECIO_CATALOGO_ASC">Menor Precio</option>
						<option value="NOMBRE_CATALOGO_ASC">Nombre A-Z</option>
				</select>
			</div>
			
			<div align="center"> 
			
				
			</div>
			<section id="lista-entretenimientos">
			<div id="main" ></div>
			</section>
			<div>
			</div>
		</div>



	<script id="templateDescuento" type="text/x-handlebars-template">
		
			<section class="para-clientes">
		<c:choose>
   		<c:when test="${sessionCliente ne null}">
      		<h3><spring:message code="label.home.clientes.interbank" text="Por ser cliente Interbank" /></h3>
   		</c:when>
    	<c:otherwise>
    	 <h3></h3>
   		</c:otherwise>
	</c:choose>	

					<table class="titulo-soles">
						<tr>
							<td class="titulo-separar">
								<p class="titulo baja">{{titulo}}</p>
							</td>
							<td>
								<ul class="oferta">
									<li class="descuento">{{formatPorcentajeDescuento}}%</li>
									<li class="puntos">Descuento</li>
								</ul>
							</td>
						</tr>
					</table>
					
					<p class="accion">
					<a href="${pageContext.request.contextPath}/descuento/detalle/{{keyItem}}" class="detalles">Ver detalles</a>
					
					</p>
				</section>

			<img src="${prop['url.imagen.descuento']}{{imagenOportunidad}}" class="central" alt="{{titulo}}"/>
       
	</script>
	<script id="templateProducto" type="text/x-handlebars-template">
	
			<section class="para-clientes">
	<c:choose>
   		<c:when test="${sessionCliente ne null}">
      		<h3><spring:message code="label.home.clientes.interbank" text="Por ser cliente Interbank" /></h3>
   		</c:when>
    	<c:otherwise>
    	 <h3></h3>
   		</c:otherwise>
	</c:choose>	
					
		<table class="titulo-soles">
						<tr>
							<td class="titulo-separar">
								<p class="titulo alta">{{titulo}}</p>
							</td>
							<td>
								<ul class="solo-soles">
									<li class="soles-base"><span> ${simboloMoneda} {{formatPrecioCatalogo}}</span></li>
							<c:if test="${sessionCliente ne null}">
									<li class="decision">&oacute;</li>
								<li class="puntos">{{formatPrecioPuntos}}<br />Millas Benefit</li>
							</c:if>
								</ul>
							</td>
						</tr>
					</table>
					<p class="precio">(Precio regular <span> ${simboloMoneda} {{formatPrecioBase}}</span>)</p>
					<p class="accion">
				<c:if test="${sessionCliente ne null}">
					<span>Tienes ${cliente.formatTotalPuntos} Millas Benefit<br />Si los usas, pagas desde ${simboloMoneda} {{porCubrir}}</span>
				</c:if>
				<c:if test="${sessionCliente ne null}">
					<span><a href="${pageContext.request.contextPath}/canjeProducto/paso1/{{keyItem}}" class="boton">{{button}}</a>
				</c:if>
				<c:if test="${empty sessionCliente}">
					<br><a href="${pageContext.request.contextPath}/producto/oportunidad/{{keyItem}}" class="detalles">Ver detalles</a><br>
					</span>
				</c:if>
				<c:if test="${sessionCliente eq null}">
					<span>¿No eres cliente?<br /><a href="#" class="enlace">Conoce nuestras tarjetas</a></span>
					</p>
				</c:if>
				</section>

			<img src="${prop['url.imagen.producto']}{{imagenOportunidad}}" class="central" alt="{{titulo}}"/>
        <!-- </div>-->
</script>
			
<script id="articleProducto" type="text/x-handlebars-template">

<article class="descuento-basico">
	<div>
		<div class="nombre">
			
				<h3>{{titulo}} </h3>
				<span></span>
		</div>
		
		<div class="solo-precio">
		 ${prop['config.moneda.simbolo.default']}{{formatPrecioCatalogo}}
		</div>

		<p class="detalle-compra">
			<a href="${pageContext.request.contextPath}/producto/oportunidad/{{keyItem}}" class="enlace">Ver detalles</a>

			<c:if test="${sessionCliente ne null}">
			<a href="${pageContext.request.contextPath}/canjeProducto/paso1/{{keyItem}}" class="boton">{{button}}</a>
            </c:if>
		</p>
	</div>
		<img src="${prop['url.imagen.producto']}{{imagenDestacado}}" style="max-height : 100%;" width="235" height="270" alt="{{titulo}}" />

</script>

<script id="articleDescuento" type="text/x-handlebars-template">
<a href="${pageContext.request.contextPath}/descuento/oportunidad/{{keyItem}}">
<article class="descuento-basico">
	<div>
	<div class="nombre">
		<h3>{{titulo}}</h3>
		<span></span>
	</div>
	
	<div class="descuento">
		<ul>
			<li>{{formatPorcentajeDescuento}}</li>
			<li class="porcentaje">%</li>
		</ul>
			<p>Dscto.</p>
	</div>
	
	</div>
	<img src="${prop['url.imagen.descuento']}{{imagenDestacado}}" height="269" width="235" alt="{{titulo}}" />
</article>
</a>
</script>
				
				
<script type="text/javascript">	
	$(document).ready(function(){
		var puntosCliente = '${cliente.totalPuntos}';
		var tipoBeneficio = '${tipoBeneficio}';
		var idCategoria = '${idCategoria}';	
		var entretenimiento = new Entretenimiento();
		var isSession = ${isSession};
		entretenimiento.loginCliente=isSession;
		entretenimiento.init(puntosCliente,tipoBeneficio,idCategoria);
		
	});
</script>