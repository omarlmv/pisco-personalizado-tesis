<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- estado -->
<p class="separar"></p>
<section class="estado">
<%-- 	<input type="hidden" name="complementos" value="${complementos}" /> --%>
<%-- 	<input type="hidden" name="idCatalogoProducto" value="${productoDetalle.idCatalogoProducto}" /> --%>
	<p class="barra"></p>
	<div class="limite">
	<div class="error"></div>

	<!-- flotante -->
	<section class="flotante-secundario" id="flotante-secundario">
	
	<div class="mis-canjespro">
		    <div class="cabec-canjespro">
		    	<p><p>
		    </div>
			<div class="mis-canjespro-cnt">
			     <p>Mis canjes</p> 
			  <div class="zonaracional">
			      <div id="lista-complementos">
		      
			      </div>
			       
			   	  <div class="zonaracional-detalle">
                     

                     
			   	     <div class="zonaracional-puntos">
			   	  	   <ul>
			   	  	   	<li>Total en Millas Benefit</li>
			   	  	   	<li id="total-en-puntos">0</li>
			   	  	   </ul>
			   	 	 </div>
			   	  </div>

			   </div>
			   </div>
		</div>
	
	
	</section>
				<!-- /flotante -->
						

		<p class="titulo-canje">Canje de producto</p>
		
	</div>
</section>
<!-- /estado -->
<!-- informacion -->

<div class="limite">
<div class="informacion">
	<div class="producto-especifico">
		<section class="message info"><!-- producto -->
				<p class="mensaje-texto">Usted, no tiene ning&uacute;n canje pendiente </p><!-- subtitulo <h2> -->
			 	
		</section>				
	</div>
	
	<div class="botonera">
		<a href="${pageContext.request.contextPath}/categorias" class="continuar">Volver a  compras</a>
	</div>
</div>
	
	
</div>
			
