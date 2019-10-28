<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" pageEncoding="ISO-8859-1"%>

<style type="text/css">
.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}
 
.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}
 
#login-box {
	width: 300px;
	padding: 20px;
	margin: 100px auto;
	background: #fff;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border: 1px solid #000;
}
</style>
	
	<div id="login-box">
 
		<h2>Acceso Cliente</h2>
 
		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>

 
		<form name="loginForm" id="loginForm" action="<c:url value='/login'/>" method="POST" autocomplete="off">
 
		    <table>
			<tr>
				<td>Usuario:</td>
				<td><input type="text" name="username"   autocomplete="off" id="username" /></td>
			</tr>
			<tr>
				<td>Contrase&ntilde;a:</td>
				<td><input type="password" name="password"  autocomplete="off" id="password"/></td>
			</tr>
			<tr>
			    <td colspan="2">
			    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			    <input name="btnSendform" type="submit" value="enviar" />
                </td>
			</tr>
		   </table>
 
		   
		</form>
	</div>
<script  type="text/javascript">
activarMenu(0);
</script>	
	
	