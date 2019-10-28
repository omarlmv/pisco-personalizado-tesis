<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script type="text/javascript" src="${prop['config.url.recursos.base.admin']}js/handlebars-v3.0.3.js"></script>
<script>
var urlBaseWeb = '<c:url value="/" />';
</script>
<!-- estado -->
<p class="separar"></p>
<section class="estado">
	<p class="barra"></p>
	<div class="limite">

		<!-- flotante -->
		<section class="flotante-secundario">
			<p class="tienes">
				<span class="tienes-texto">Tienes</span> <span class="tienes-puntos">${cliente.totalPuntos}</span>
			</p>
			<div class="resumen resumen-producto">
				<p class="arrow-resumen"></p>
				<div class="detalle">
					<ul>
						<li>${productoDetalle.titulo}</li>
						<li class="numero">${productoDetalle.precioPuntos}</li>
					</ul>
					<div id="textoComplementos">
					<c:forEach items="${listaComplementos}" var="item">
						<c:if test="${item.clase eq 'activo'}">
						<span>+</span>
						<ul>
							<li>${item.titulo}</li>
							<li class="numero">${item.precioPuntos}</li>
						</ul>
						</c:if>
					</c:forEach>
					</div>
				</div>
				<div class="total">
					<ul>
						<li>Total en puntos:</li>
						<div id="totalPuntos">
							<li class="numero">${totalPuntos}</li>
						</div>
					</ul>
				</div>
			</div>
			<div class="cotizando">
				<ul class="enunciado compra">
					<li>Mis canjes</li>
					<li><p>Te alcanza con tus puntos</p></li>
				</ul>
				<ul class="disponibles">
					<li>Puntos disponibles tras el canje:</li>
					<li><div id="puntosCliente"></div></li>
				</ul>
				<div class="combinacion">
					<p>Prueba combinaciones posibles:</p>
					<form>
						<ul>
							<li>Si usas estos<br />puntos:
							</li>
							<li id="calculo"><input id="puntosSeleccionados" type="text" value="${productoDetalle.precioPuntos}" /></li>
							<li><input type="button" onclick="javascript:actualizar();" value="ACTUALIZAR" /></li>
						</ul>
					</form>
					<p>Te quedan por cubrir: S/. <span id="porCubrir"></span></p>
				</div>
			</div>
			<div class="apoyo">
				<p>Apoyo en lï¿½nea</p>
				<ul>
					<li><span class="ico-telefono"></span></li>
					<li>311 9000 (Lima)<br />0801 00802 (Provincias)
					</li>
				</ul>
			</div>
		</section>
		<!-- /flotante -->

		<p class="titulo-canje">Canje de producto</p>
		<ul class="estado-4pasos estado-operacion">
			<li class="paso-1 presente"><span>1</span>
			<p></p></li>
			<li class="paso-2"><span>2</span>
			<p></p></li>
			<li class="paso-3"><span>3</span>
			<p></p></li>
			<li class="paso-4 completo-off"><span></span>
			<p></p></li>
		</ul>
	</div>
</section>
<!-- /estado -->
<!-- informacion -->
<div class="limite">
	<div id="detalles-producto" class="informacion">
		<div class="titulo-general">
			<h1>Detalles del producto</h1>
			<span></span>
		</div>
		<form:form id="formCanjePaso1" action="${pageContext.request.contextPath}/canjeProducto/paso2" method="POST" autocomplete="off" commandName="canjeProductoForm">
			<div id="complementos"><input type="hidden" name="complementos" value="${complementos}" /></div>
			<input type="hidden" name="idCatalogoProducto" value="${productoDetalle.idCatalogoProducto}" />
			<section class="producto">
				<h2 class="subtitulo">${productoDetalle.titulo}</h2>
				<img src="${prop['url.imagen.producto']}${productoDetalle.producto.imagen1}" height="270" width="448" alt="" />
				<!-- <div class="elemento-grupo">
					<ul class="elemento">
						<li>Cantidad</li>
						<li><select>
								<option value="">1</option>
						</select></li>
					</ul>
					<ul class="elemento">
						<li>Aro</li>
						<li><select>
								<option value="">26</option>
						</select></li>
					</ul>
				</div> -->
			</section>
			<section class="agregar">
				<p>¿Te gustaría agregar?</p>
				<div id="comp">
					<c:forEach items="${listaComplementos}" var="item">
						<a href="javascript:seleccionar('${item.idCatalogoProducto}');" id="${item.idCatalogoProducto}" class="${item.clase}">
							<article>
								<div class="seleccionar" class="${item.clase}"></div>
								<img src="${prop['url.imagen.producto']}${item.producto.imagen1}" height="65" width="65" alt=""/>
								<p>${item.titulo}
									<br />
									<span>${item.precioPuntos}</span> puntos
								</p>
							</article>
						</a>
					</c:forEach>
				</div>
			</section>
			<div class="botonera">
				<input type="submit" value="CONTINUAR">
				<div>
					<span class="texto">GUARDAR Y SEGUIR NAVEGANDO</span> <span class="ico-seguir"></span>
				</div>
			</div>
		</form:form>
	</div>
</div>
<script>
	$('.alineacion').find('a').removeClass('activo');
	$('#compras').addClass('activo');

	var complementos = [];
	var total = '${productoDetalle.precioPuntos}';
	var puntosCliente = '${cliente.totalPuntos}';

	function seleccionar(val) {
		$("#textoComplementos").empty();
		if(complementos.indexOf(val) == -1){
			complementos.push(val);
		}else{
			removeArrayItem(complementos, val);			
		}
		
		$("#complementos").empty();
		$('#complementos').append("<input type='hidden' name='complementos' value="+complementos+" />");

		$.ajax({
					url : '${pageContext.request.contextPath}/canjeProducto/paso1/items',
					type : 'post',
					dataType : 'json',
					data : $('#formCanjePaso1').serialize(),
					success : function(data) {
						total = '${productoDetalle.precioPuntos}';
						$.each(data, function(index, value) {
							$('#textoComplementos').append("<span>+</span>");
							$('#textoComplementos').append("<ul><li>"+value.titulo+"</li><li class='numero'>"+value.precioPuntos+"</li></ul>");
							total = parseInt(total) + parseInt(value.precioPuntos);
						});
					$("#totalPuntos").empty();
					$('#totalPuntos').append("<li class='numero'>"+total+"</li>");
						if(puntosCliente >total){
							$("#puntosCliente").empty();
							$('#puntosCliente').append(puntosCliente - total);
						}else{
							$("#puntosCliente").empty();
							$('#puntosCliente').append(0);
						}
							actualizar();
					}
				});
	}

	function removeArrayItem(array, itemToRemove) {
		var removeCounter = 0;
		for (var index = 0; index < array.length; index++) {
			if (array[index] === itemToRemove) {
				array.splice(index, 1);
				removeCounter++;
				index--;
			}
		}
		return removeCounter;
	}
	
	$(document).ready(function() {
		actualizar();
	});
	
	function actualizar() {
	var puntosPorConvertir;
	
	if($('#puntosSeleccionados').val()>total){
		$('#puntosSeleccionados').val(total);
	}
	
		puntosPorConvertir = total - $('#puntosSeleccionados').val();
	
		$.ajax({
					url : urlBaseWeb + 'common/puntosASoles/'+puntosPorConvertir,
					type : 'get',
					dataType : 'json',
					data : $('#formCanjePaso3').serialize(),
					success : function(data) {
						console.log(data);
						$('#porCubrir').empty();
						$('#porCubrir').append(data);
					}
				});
	}
	
	$(document).ready(function() {
		if(puntosCliente >total){
			$("#puntosCliente").empty();
			$('#puntosCliente').append(puntosCliente - total);
		}else{
			$("#puntosCliente").empty();
			$('#puntosCliente').append(0);
		}
	});
	
</script>