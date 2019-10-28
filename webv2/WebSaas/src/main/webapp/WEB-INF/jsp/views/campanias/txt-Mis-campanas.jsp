<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- informacion -->
<section id="campanas" class="limite">
	<div class="titulo-general">
		<h1>Mis campañas</h1>
		<span></span>
	</div>
	<div class="felicitaciones" style="display: none"></div>
	<div class="detalle">
	
	<input style="display: none;" value="${sessionScope.cliente.getIdCliente}" id="idCliente" >
	
		<c:forEach items="${requestScope.campaniaForm.listaPromociones}"
			var="promocion">
			<article>
				<div class="unidad">
					<img src="${prop['url.imagen.promocion']}/galeria/${promocion.imagen1}" />
					<div >
<%-- 					<img src="${prop['url.imagen.promocion']}/galeria/${promocion.imagen1}" /> --%>
						<p class="texto">${promocion.tituloPromocion}</p>
						<p class="fondo"></p>
					</div>
				</div>
				<c:choose>
				<c:when test="${promocion.inscrito}">
					<span style="font-family: wingdings; font-size: 200%;">&#10004;</span>
					<label>Ya estas inscrito en esta campaña.</label>
				</c:when>
				<c:otherwise>
					<div id="interesaBoton1" descripcion="${promocion.descripcion}"
						titulo="${promocion.tituloPromocion}"
						imagen="${prop['url.imagen.promocion']}/galeria/${promocion.imagen1}"
						condicion="${promocion.condiciones}"
						prm="${promocion.idPromocion}"
						available="${promocion.inscripcionActiva}"
						terminos="${promocion.terminos}">
						<a href="#cam-1" class="fancybox">Me interesa</a>
					</div>
				</c:otherwise>
				</c:choose>

			</article>
		</c:forEach>
	</div>
</section>
<!-- /informacion -->


<div id="cam-1" class="lightbox-campana" style="display: none">
	<input type="hidden" id="tipoBoton" />
	<p class="titulo" id="tituloPromoDiv" ></p>
	<p class="valida" id="condicionPromoDiv"></p>
	<div class="detalle">
		<div id="campana-1" class="campana">
			<img id="img-modal-detalle" src="" />
			<div>
				<p class="texto" id="tituloImagenDiv" ></p>
				<p class="fondo"></p>
			</div>
		</div>
		<div class="descripcion">
			<p id="descripcionDiv"></p>
		</div>
		<input style="display: none;" id="prm" >
		<input style="display: none;" id="available" >
	</div>
	<p class="terminos">Términos y condiciones</p>
	<div class="caja">
		<p id="terminosCondiciones"></p>
	</div>
	<div id="finInscripcion" >
	<label class="acepto"><input type="checkbox"
		id="terminoAceptar">He leído y acepto los términos y
		condiciones</label> <a href="#" id="idInscribir">Inscribirme en esta
		campaña</a>
	</div>
</div>

<script>
	
	//Seleccion de campana
	$('body').on('click', '.fancybox', function() {
		var parent = $(this).parent();
		var titulo = parent.attr('titulo');
		var descripcion = parent.attr('descripcion');
		var imagen = parent.attr('imagen');
		var condicion = parent.attr('condicion');
		var prm =  parent.attr('prm');
		var available =  parent.attr('available');
		var terminos =  parent.attr('terminos');
		
		$("#tituloPromoDiv").text(titulo);
		$("#condicionPromoDiv").text(terminos);
		$("#tituloImagenDiv").text(titulo);
		$('#img-modal-detalle').attr('src', imagen);
		$("#descripcionDiv").text(descripcion);
		$("#prm").val(prm);
		$("#available").val(available);
		$("#terminosCondiciones").text(condicion);
		
		if(parseInt($("#available").val()) == 2){
			$("#finInscripcion").hide();
		}
		
	});
	
	$('#idInscribir').click(function() {
				if ($("#terminoAceptar").is(':checked')) {
					var CONTEXT_PATH = "${pageContext.request.contextPath}/";
					//Falta poner parametro para que obtenga IdCliente dinamicamente
					$.get(CONTEXT_PATH+"/campania/guardar/1/" + $("#prm").val(), function(result){
						if(result.estado == 0){
							console.log('es exito');
							if(result.codigo  > 0){
								console.log('Exito');
								$(".fancybox-overlay").css("display", "none"); //cierromodal
								$('#interesaBoton' + $('#tipoBoton').val()).empty(); //vaio los elemento que esta en el boton
								$('#interesaBoton' + $('#tipoBoton').val()).html('<p class="ya">Ya estoy inscrito en esta campaña</p>'); //agrego el nuevo contenido
								$('.felicitaciones').css('display', 'block') //desbloquero mensaje oculto
								$('.felicitaciones').html(result.mensaje); //escribo en el mensaje oculto
							}else{
								console.log('error');
								console.log(result.codigo);
								console.log(result.mensaje);
	 							$('.felicitaciones').css('display', 'block') //desbloquero mensaje oculto
								$('.felicitaciones').html(result.mensaje); //escribo en el mensaje oculto
							}
						}
						else{
							console.log('error');
	 						$('.felicitaciones').css('display', 'block') //desbloquero mensaje oculto
							$('.felicitaciones').html(result.mensaje); //escribo en el mensaje oculto
						}
					});
				}
			});
	
</script>