<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
#legenddiv{margin-top:70px;width: 240px;}
#chartdiv{width: 300px;}
#legenddiv,#chartdiv{float:left;vertical-align:top;}
#legenddiv svg{position:relative !important;}

</style>
		<!-- informacion -->
		<div id="estado-cuenta" class="pantalla limite">
			<div class="titulo-general">
				<h1 class="titulo">Mi estado de cuenta</h1>
			</div>
			<section class="cliente">
				<ul>
					<li>Cliente:</li>
					<li>${sessionCliente.nombre} ${sessionCliente.apellidos}</li>
				</ul>
				<p>&Uacute;ltima actualizaci&oacute;n: ${ultimaActualizacion}</p>
			</section>
			<section class="puntos-actuales">
				<!-- <h2 class="subtitulo">Millas Benefit &reg; actuales</h2> -->
				<form>
					<div>
						<div class="puntos">
							<p class="texto">Saldo de Millas Benefit:</p>
							<p class="numero" id="txt-saldo-puntos"></p>
						</div>
						<div class="resumen">
							<div class="mostrar-periodo">
							
							
			
<!-- 								<ul class="elemento"> -->
<!-- 									<li>Mostrar:</li> -->
<!-- 									<li> -->
<!-- 										<select id="cboSelectEstadoCuenta" name="cboSelectEstadoCuenta"> -->
<%-- 											<option value="1"><spring:message code="select.movimiento.puntos" text="Movimiento puntos"/></option> --%>
<%-- 											<option value="2"><spring:message code="select.canjer.realizados" text="Canjes realizados"/></option> --%>
<%-- 											<option value="3"><spring:message code="select.descuentos.utilizados" text="Descuentos utilizados"/></option> --%>
<!-- 										</select> -->
<!-- 									</li> -->
<!-- 								</ul> -->


								<ul class="elemento" id="ul-perido-estado">
									<li>Per&iacute;odo:</li>
									<li>
										<select id="cboSelectFiltroPeriodo">
										
										</select>
									</li>
								</ul>
								<ul class="enlaces">
									<li><a target="_blank" href="javascript:;" id="linkGuardarPdf">Descargar</a></li>
									<li><a href="javascript:;" onclick="printContentCanjes('detalle-canjes');">Imprimir</a></li>
								</ul>
							</div>
							<div  class="detalle" id="detalle-canjes" >
							</div>
							
							<div class="leyenda grafico-chart-pie" >
							<p id="text-leyenda-titulo"></p>
							</div>	
						   <div  id="legenddiv" class="grafico-chart-pie" style=""></div>
						   <div id="chartdiv" class=" grafico-chart-pie" style="height: 360px;"></div>
						   
						</div>
					</div>
				</form>
			</section>
		</div>
		
		<div id="estado-cuenta" class="impresion limite">
			
			<section class="puntos-actuales">
				<h2 class="subtitulo"><c:out value="${tituloEstadoCuenta}"/></h2>
				
				<section class="cliente-print">
				<div class="cliente-datos">
					<ul>
						<li><spring:message code="label.cliente"/>:</li>
						<li>${sessionCliente.nombre} ${sessionCliente.apellidos}</li>
					</ul>
									
					<ul class="ulMillasBenefit">
						<li>Saldo de Millas Benefit:</li>
						<li>${sessionScope.sessionCliente.formatTotalPuntos}</li>
					</ul>
										
				</div>
				<div class="cliente-datos">	
					<p> </p>
				</div>	
				
			</section>
			 <div style="border:1px solid #B3C3E3;overflow: hidden; padding: 20px;">
				<div class="resumen-print">
															
				</div>
			</div>
			</section>
		</div>


<script src="${prop['config.url.recursos.base.shared']}amcharts/amcharts.js"></script>
<script src="${prop['config.url.recursos.base.shared']}amcharts/pie.js?${prop['config.web.release']}"></script>

<!-- <script type="text/javascript" src="https://www.google.com/jsapi"></script> -->
<script type="text/javascript">
	var TITULO_CANJES = '<spring:message code="label.titulo.ec.canjes" />';
	var MSG_MOVI_PUNTOS ='<spring:message code="label.movimiento.puntos" />';
	var MSG_CANJES_PRO ='<spring:message code="label.canjes.realizados.detalle" />';
	var MSG_CANJES_DESC ='<spring:message code="label.canje.descuentos.utilizados" />';
	var FECHA_CANJE = "${ultimaActualizacion}";//Fecha Canje
		
      var TIPO_ESTADO_CUENTA = 2;//Estado cuenta CANJE
      
      $(document).ready(function(){
    	  
    	  var cliente = new Cliente();
          cliente.init();
      
	      $(".estado-cuenta .subtitulo").text(TITULO_CANJES);

      	  $(".cliente-datos p").text(FECHA_CANJE);
      	  $(".impresion").css("display","none");
      	  
      	 
      });
      
</script>


 <script id="template-canje-descuentos" type="text/x-handlebars-template">
<!--<p class="text-leyenda-titulo">DETALLE DE CANJE DE DESCUENTOS PERSONALIZADOS</p>-->
<table>
<tr>
	<th>Descuento</th>
	<th>Nro. de usos del mismo descuento</th>
	<th>Usos que te quedan disponibles en el período</th>
	<th>Fecha de vigencia</th>
</tr>
{{#items}}
<tr class="{{trClass}}">
	<td class="descripcion"><!--<span class="ico-cuadro {{colorIco}}"></span><span class="nombre">-->{{descuento}}<!--</span>--></td>
	<td><label class="{{tdClass}}">{{usados}} de {{inicio}}</label></td>
	<td><label class="{{tdClass}}">{{disponibles}}</label></td>
	<td>{{vigencia}}</td>
</tr>
{{/items}}

</table>
<div>* Recuerda que sólo puedes utilizar cada descuento personalizado hasta la fecha de vigencia indicada.</div>
								
</script>

<script  id="template-canje-productos" type="text/x-handlebars-template">
<!--<p class="text-leyenda-titulo"></p>-->
<!--<div>{{totalCanjes}} canjes realizados en el período consultado</div>-->

<table>
	<tr>
		<th>Descripción</th>
		<th>Fecha</th>
		<th>Millas Benefit</th>
	</tr>
{{#items}}
	<tr class="{{trClass}}">
		<td class="descripcion"><!--<span class="ico-cuadro {{colorIco}}"></span><span class="nombre">-->{{descripcion}}<!--</span>--></td>
		<td>{{fecha}}</td>
		<td><label class="{{tdClass}}">{{puntos}}</label></td>
	</tr>
{{/items}}
</table>
</script>



