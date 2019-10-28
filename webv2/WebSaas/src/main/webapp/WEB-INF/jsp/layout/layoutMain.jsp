<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="google-site-verification" content="ekpeDq0fiC2zN8p5U4c8rhFtfPh41QfIekgwJHa3LaA" />
<title><c:choose><c:when test="${metaTitle eq null}">${prop['config.web.default.title.page']}</c:when><c:otherwise>${metaTitle}</c:otherwise></c:choose></title>
<c:if test="${(prop['config.ambiente.deploy'] ne 'produccion')}"><meta name="robots" content="noindex, nofollow"/><meta name="googlebot" content="noindex, nofollow" /></c:if>
<c:if test="${metaKeyWords ne null}"><meta name="keywords" content="${metaKeyWords}"></c:if>
<c:if test="${metaDescription ne null}"><meta name="description" content="${metaDescription}"></c:if>
<c:if test="${metaCanonical ne null}"><link href="${metaCanonical}" rel="canonical"/></c:if>
<c:if test="${metaFacebook}">
	<meta property="og:url" content="${fbUrl}" />
	<meta property="og:type" content="${fbType}" />
	<meta property="og:title" content="${fbTitle}" />
	<meta property="og:description" content="${fbDescription}" />
	<meta property="og:image" content="${fbImage}" />
</c:if>
<link href="${prop['config.url.recursos.base.web']}static/images/favicon.ico" rel="Shortcut Icon preload"/>
<jsp:include page="../inc/variablesJS.jsp" />
<c:choose><c:when test="${(prop['config.ambiente.deploy'] eq 'desarrollo') || (prop['config.ambiente.deploy'] eq 'site')}">
	<link rel="stylesheet preload" as="style" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/normalize.css?${prop['config.web.release']}">
	<link rel="stylesheet preload" as="style" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/jquery-ui.css?${prop['config.web.release']}">
	<link rel="stylesheet preload" as="style" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/jquery.fancybox.css?${prop['config.web.release']}">
	<link rel="stylesheet preload" as="style" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/rangeslider.css?${prop['config.web.release']}" />
	<link rel="stylesheet preload" as="style" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/font.css?${prop['config.web.release']}">
	<link rel="stylesheet preload" as="style" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/modal.css?${prop['config.web.release']}">
	<link rel="stylesheet preload" as="style" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/general.css?${prop['config.web.release']}">
	<link rel="stylesheet preload" as="style" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/gif.css?${prop['config.web.release']}" />
	<link rel="stylesheet preload" as="style" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/custom.css?${prop['config.web.release']}">
	<link rel="stylesheet preload" as="style" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/font-awesome.css?${prop['config.web.release']}"/>
	<link rel="stylesheet preload" as="style" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/nouislider.css?${prop['config.web.release']}"/>

	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/jquery1.8.3.min.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/jquery.fancybox.min.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/handlebars-v3.0.3.min.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/jquery-ui.min.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/jquery.validate.min.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/additional-methods.mincustom.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/jquery.blockUI.min.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/jquery.form.min.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/isotope.pkgd.min.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/imagesloaded.pkgd.min.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/packery-mode.pkgd.min.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/rangeslider.min.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/sticky.min.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/DigitalDataHelper.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/nouislider.min.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/jquery.imageCaching.min.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/lunr.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/general-components.min.js"></script>

	<!-- MODULOS -->	
	
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/variablesJson.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/util-bim.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/handlebars.helper.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/vuelos.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/enviarEmail.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/destacados.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/canjes.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/paquete.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/producto.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/filtro-vuelos.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/cliente.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/compras.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/descuento.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/entretenimiento.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/pasarela.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/main.js?${prop['config.web.release']}"></script>
 	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/adobe-analytic.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/terminosCondiciones.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/canjeLocal.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/promociones.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/valesDigitales.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/cyber_wow.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/header.js?${prop['config.web.release']}"></script>
<!-- 	<script src="https://www.google.com/recaptcha/api.js"></script> -->
<!-- 	<script src="https://www.google.com/recaptcha/api.js?callback=verificarReCaptcha" async defer"></script> -->

</c:when><c:when test="${(prop['config.ambiente.deploy'] eq 'produccion')}">
	<link rel="stylesheet preload" as="style" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/all-style.css?${prop['config.web.release']}">
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/all-plugins.min.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/all-modules.min.js?${prop['config.web.release']}"></script>
 	<script rel="preload" as="script" type="text/javascript" src="${prop['config.ibk.url.adobe.analytics']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/general-components.min.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/cyber_wow.js?${prop['config.web.release']}"></script>
<!-- 	<script src="https://www.google.com/recaptcha/api.js"></script> -->
<!-- 	<script src="https://www.google.com/recaptcha/api.js?callback=verificarReCaptcha" async defer"></script> -->
	
</c:when><c:otherwise>
	<link rel="stylesheet preload" as="style" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/all-style.css?${prop['config.web.release']}">
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/all-plugins.min.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/all-modules.min.js?${prop['config.web.release']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.ibk.url.adobe.analytics']}"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/general-components.min.js"></script>
	<script rel="preload" as="script" type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/cyber_wow.js?${prop['config.web.release']}"></script>
<!-- 	<script src="https://www.google.com/recaptcha/api.js"></script> -->
<!-- 	<script src="https://www.google.com/recaptcha/api.js?callback=verificarReCaptcha" async defer"></script> -->
	
</c:otherwise></c:choose>

	<script rel="preload" as="script">
	    (function(m,e,t,r,i,c,s){
	    m[i]=function(c,s){if(s.event!=undefined){_satellite.track(s.event)}};
	    m[t]=m[t]||[];m[r]=m[r]||new DigitalDataHelper(m[t],m[i],true);
	    })(window,document,'digitalData','dataHelper', 'dataListener')
	    
	    var aplicarAdobe = '${aplicarAdobeDefecto}';
	    if(aplicarAdobe == null || aplicarAdobe == '' ){aplicarAdobe = 'true';}
	    
	    var estructuraPageHeader = '${estructuraMapping}';
	    if(estructuraPageHeader != ""){
	    	if(aplicarAdobe == 'true'){
			    var dataHeader = formatHeaderPage(estructuraPageHeader);
			    console.log("Header page: " + JSON.stringify(dataHeader));
			    digitalData.push(dataHeader);
	    	}else{
	    		var dataHeader = formatHeaderPage(estructuraPageHeader);
			    console.log("Header page: " + JSON.stringify(dataHeader));
	    	}
	    }
	    
    
	</script>
	
	<c:choose>
		<c:when test="${prop['config.ambiente.deploy'] eq 'produccion' || prop['config.ambiente.deploy'] eq 'uat'}">
		<!-- Global site tag (gtag.js) - Google AdWords: 858691517 -->
			<script rel="preload" as="script" async src="https://www.googletagmanager.com/gtag/js?id=AW-858691517"></script>
		</c:when>
		<c:otherwise></c:otherwise>
	</c:choose>
	<script rel="preload" as="script">
		window.dataLayer = window.dataLayer || [];
		function gtag(){dataLayer.push(arguments);}
		gtag('js', new Date());
		
		gtag('config', 'AW-858691517');
	</script>
	
	
<!-- [if lt IE 9]> 
		<link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/ie.css">
		<![endif]-->
<!--[if lt IE 9]>
		<script src="${prop['config.url.recursos.base.web']}static/js/html5shiv.js"></script>
		<script src="${prop['config.url.recursos.base.web']}static/js/respond.js" type="text/javascript"></script>
		<![endif]-->

</head>
<body>
	<div class="fancyboxoverflow">
		<tiles:insertAttribute name="header" />
<%-- 		<tiles:insertAttribute name="menuPrincipal" /> --%>
		<tiles:insertAttribute name="content" />
		<tiles:insertAttribute name="footer" />
	</div>
	<tiles:insertAttribute name="pixelRemarketingGoogle" />
	<c:choose>
		<c:when test="${prop['config.ambiente.deploy'] eq 'produccion' || prop['config.ambiente.deploy'] eq 'uat'}">
			<script rel="preload" as="script" type="text/javascript">_satellite.pageBottom();</script>
		</c:when>
		<c:otherwise></c:otherwise>
	</c:choose>
</body>
</html>