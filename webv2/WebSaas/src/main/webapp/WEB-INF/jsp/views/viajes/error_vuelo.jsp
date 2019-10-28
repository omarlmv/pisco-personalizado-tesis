<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ page language="java" pageEncoding="ISO-8859-1"%>

<div class="b404-wancho">
  <div class="b404-img">
    <img src="${prop['config.url.recursos.base.web']}static/images/404-avion.png" alt="">
  </div>
  <div class="b404-p">
    <h2>${mensajeError}</h2>
    <p><a href="<c:url value="/viajes"/>" class="volver">Volver</a></p>
  </div>
</div>
<script type="text/javascript">
setTimeout(function(){ location.href = CONTEXT_PATH+'/viajes'; }, 10000);

</script>