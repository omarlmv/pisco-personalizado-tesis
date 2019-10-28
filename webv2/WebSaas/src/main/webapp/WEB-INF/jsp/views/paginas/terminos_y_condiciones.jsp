		<div class="relativo" data-menu="mostrar">
			<section class="miga miga-inferior">
				<div class="detalle">
					<a href="${pageContext.request.contextPath}/">Inicio</a>
					<span>&gt;</span>
					<p class="activo">T&eacute;rminos y condiciones de Interbank Benefit</p>
					</div>
			</section>
		</div>
		
		<div id="terminos-condiciones" class="limite">
			<div class="titulo-general">
				<h1 class="titulo">T&eacute;rminos y condiciones del programa de recompensas Interbank Benefit</h1>
			</div>
			<div class="terminos-condiciones-detalle">
				<nav id="main-terminos-condiciones">
								
				</nav>
			</div>
		</div>

<script type="text/javascript">
$(document).on('ready',function() {
	var	terminosCondiciones = new TerminosCondiciones();
	terminosCondiciones.init();
});
</script>


<script>
$(window).on("orientationchange",function(event){
	$.each($(".terminos-condiciones-detalle .contenidos"),function(i,e){
		if($(this).css("display")=="block"){			
			alto = $(this).height();
			console.log("Alto: "+alto);
			setTimeout(function(){$(".terminos-condiciones-detalle").css('height', alto);},300);
		}
	});
});
</script>

<script id="template-terminos-condiciones" type="text/x-handlebars-template">
	<a href="#" data-contenido="contenido-{{idTerminoCondicion}}">
		<p>{{titulo}}</p>
		<span class="arrow-preguntas"></span>
	</a>
	<div class="contenidos" id="contenido-{{idTerminoCondicion}}">
		<section>
			<h2>{{nombre}}</h2>

			{{safeString descripcion}}

			<center><a class="close-tab" href="javascript:void(0)">cerrar x</a></center>
		</section>
	</div>
</script>