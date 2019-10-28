<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>

.detalle .enunciado{
	text-transform:capitalize;
}

.detalle .enunciado.capital{
	text-transform:capitalize;
}

</style>
<!-- informacion -->
		<div id="seguimiento" class="limite" data-menu="mostrar">
			<div class="titulo-general">
				<h1 class="titulo">Seguimiento a mis compras</h1>
			</div>
			<div class="search" >
				<div  class="search-line">
				N&uacute;mero de orden:
				</div>
				
				<div  class="search-line input">
					<input type="text" name="textBuscarCanje" class="buscar-canje" id="textBuscarCanje" size="40"  maxlength="20" style="height: 36px;"/> 
				</div>
				
				<div  class="search-line"> <a  id="btnBuscarCanje"  class="boton-buscar" href="void:javascript(0)">Buscar</a>
				</div> 
			</div>
				
			<section id="lista-result-canjes">
				
				
			</section>
		</div>
		<!-- /informacion -->
<script type="text/javascript">

 
 
$(document).on('ready',function() {
	 var cliente = new Cliente();
     cliente.handler();
	 cliente.buscarCanjes();
	 activarMenu("none");
});    
</script>



<script  id="template-canje-detalle-estado" type="text/x-handlebars-template">
<ul class="seguimiento-{{total_items}}estados">
{{#items}}

 {{#if estadoInicial}}
  <li class="estacion-inicial">
	<span class="esfera recibido {{cssActivo}}"></span>
	<span class="enunciado capital enunciado-recibido {{cssActivo}}-texto">{{descEstado}}</span>
  </li>

 {{/if}}

 {{#if estadoNext}}
	<li class="estacion {{cssActivo}}">
   		<span class="esfera"></span>
		<p></p>
	   <span class="enunciado">{{{splitForPalote descEstado}}}</span>
    </li>
{{/if}}

 {{#if reprogramado}}
	<li class="estacion {{cssActivo}}">
   		<span class="esfera"></span>
		<p></p>
	 <div class="enunciado reprogramado"><a href="#cam-1" class="modal-detalle" id-estado="{{idEstado}}" >{{descEstado}}</a></div>
	</li>
{{/if}}

 {{#if cancelado}}
	<li class="estacion {{cssActivo}}">
   		<span class="esfera"></span>
		<p></p>
	 <div class="enunciado cancel-anulado"><a href="#cam-1" class="modal-detalle" id-estado="{{idEstado}}" >{{descEstado}}</a></div>
	</li>
{{/if}}

 {{#if entregado}}
	<li class="estacion {{cssActivo}}">
   		<span class="esfera finalizado"></span>
		<p></p>
	 <div class="enunciado reprogramado"><a href="#cam-1" class="modal-detalle" id-estado="{{idEstado}}" >{{descEstado}}</a></div>
	</li>
{{/if}}

	
{{/items}}

</ul>
</script>

<div id="cam-1" class="pd-modal">
</div>

<script  id="template-modal-detalle-estado" type="text/x-handlebars-template">
		<div class="pd-modal-cell">
			<div class="pd-modal-overlay"></div>
			<div class="pd-modal-wancho">
					<div class="pd-modal-cerrar"></div>
					<div class="pd-modal-box">
					 <h3>Detalle {{descTipoDetalle}}</h3>
					 <h4>Fecha: {{fecha}}</h4>
					 <h5>Hora: {{hora}}</h5>
					 <h6>Motivo:</h6>
					 <p>{{motivo}} </p>
					 <p>{{descripcion}}</p>
					  {{#longitud reprogramacion}}
					   <p>{{reprogramacion}}</p>
					  {{/longitud}}
					  <p>{{notaOpcional}}</p>
					  <div class="pd-modal-btn">
						<p>Cerrar</p>
					  </div>
				</div>
			</div>
		</div>
</script>

 
<script  id="template-canje-cliente" type="text/x-handlebars-template">
<p class="titulo">Estado de canjes realizados</p>
				<table>
					<tr>
						<th class="producto">Producto</th>
						<th class="numero">Número de orden</th>
						<th class="fecha">Fecha de canje</th>
						<th class="datos">Datos del despacho</th>
						<th class="situacion">Estado</th>
						<th class="detalles">&nbsp;</th>
					</tr>
{{#items}}
					<tr class="trDetalle">
						<td>{{nombre}}</td>
						<td>{{nroOrden}}</td>
						<td>{{fechaCanje}}</td>
						<td>{{despachos}}</td>
						<td class="situacion"><span>{{descEstado}}{{#if entregado}}<span class="icon-check-2"></span>{{/if}}</span></td>
						<td><a href="javascript:void(0)" id-pedido="{{nroOrden}}" timeEntrega="{{tiempoEntrega}}" class="show-detalle-pedido">Ver detalles</a></td>
					</tr>
					<tr class="detalle detalle_reset">
						<td colspan="6" td-id-detalle="{{nroOrden}}">
						</td>
					</tr>
{{/items}}
					
				</table>
</script>
