<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" isErrorPage="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" pageEncoding="ISO-8859-1"%>

		<!-- informacion -->
	
	<c:choose>
		<c:when test="${mensajeError ne null && fn:length(mensajeError)>0}">
	      	<div class="limite">
				<div id="error-pago">
					<p class="titulo"><c:out value="${mensajeError}" escapeXml="false"/></p>
					<p class="texto">Para mayor información comunícate al (01)311-9020 (Lima) o al 0801-00802 (Provincias)</p>
					<p>Intentelo otra vez, <a href="<c:url value="/"/>">Home</a>.</p>
				</div>
			</div>
		</c:when>
		
		<c:otherwise>
			<div class="limite">
				<div id="error-pago">
					<p class="titulo">Tu operaci&oacute;n no se ha podido realizar. Por favor int&eacute;ntalo de nuevo.</p><br>
					<p class="texto">Para mayor información comunícate al: 311-9020 (Lima) o al  0801-00802 (Provincias)</p><br>
					<p>Intentelo otra vez, <a href="<c:url value="/"/>">Clic Aqui</a>.</p>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
	
		<!-- /informacion -->
	
		
	</body>
</html>