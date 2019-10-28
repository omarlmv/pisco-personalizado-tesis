<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" isErrorPage="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
 <c:set var="sesionBienvenida" value="" scope="session"  />
<div class="b404-wancho">
  <div class="b404-img">
    <img src="${prop['config.url.recursos.base.web']}static/images/404-avion.png" alt="">
  </div>
  <div class="b404-p">
    <h2>Estamos en mantenimiento.</h2>
  </div>
</div>
<script type="text/javascript">

$(document).ready(function() {
	$('#home').removeClass('activo');
	$('#viajes').addClass('activo');
});
</script>