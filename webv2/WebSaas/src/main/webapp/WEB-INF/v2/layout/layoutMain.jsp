<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
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
<link href="${prop['config.url.recursos.base.web']}static/images/favicon.ico" rel="Shortcut Icon"/>
<jsp:include page="../inc/variablesJS.jsp" />
<c:choose><c:when test="${(prop['config.ambiente.deploy'] eq 'desarrollo') || (prop['config.ambiente.deploy'] eq 'site')}">
	<link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}v2/css/normalize.css?${prop['config.web.release']}">
	<!-- <link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/jquery-ui.css?${prop['config.web.release']}"> -->
	<!-- <link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/jquery.fancybox.css?${prop['config.web.release']}"> -->
	<!-- <link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/rangeslider.css?${prop['config.web.release']}" /> -->
	<link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}v2/css/font.css?${prop['config.web.release']}">
	<!-- <link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/modal.css?${prop['config.web.release']}"> -->
	<link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}v2/css/general.css?${prop['config.web.release']}">
	<!-- <link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/gif.css?${prop['config.web.release']}" /> -->
	<!-- <link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/custom.css?${prop['config.web.release']}"> -->
	<link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}v2/css/font-awesome.css?${prop['config.web.release']}"/>
	<!-- <link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/nouislider.css?${prop['config.web.release']}"/> -->

	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}v2/js/plugins/jquery-2.2.4.min.js"></script>
	<!-- <script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/jquery.fancybox.min.js?${prop['config.web.release']}"></script> -->
	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}v2/js/plugins/handlebars-v3.0.3.min.js"></script>
	<!-- <script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/jquery-ui.min.js"></script> -->
	<!-- <script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/jquery.validate.min.js?${prop['config.web.release']}"></script> -->
	<!-- <script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/additional-methods.min.js?${prop['config.web.release']}"></script> -->
	<!-- <script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/jquery.blockUI.min.js"></script> -->
	<!-- <script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/jquery.form.min.js"></script> -->
	<!-- <script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/isotope.pkgd.min.js"></script> -->
	<!-- <script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/imagesloaded.pkgd.min.js"></script> -->
	<!-- <script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/packery-mode.pkgd.min.js"></script> -->
	<!-- <script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/rangeslider.min.js"></script> -->
	<!-- <script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/sticky.min.js?${prop['config.web.release']}"></script> -->
	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/DigitalDataHelper.js"></script>
	<!-- <script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/nouislider.min.js"></script> -->
	
	<!-- MODULOS -->
	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}v2/js/modulos/handlebars.helper.js?${prop['config.web.release']}"></script>
	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}v2/js/modulos/promociones.js?${prop['config.web.release']}"></script>
	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}v2/js/modulos/util-bim.js?${prop['config.web.release']}"></script>
	
</c:when><c:when test="${(prop['config.ambiente.deploy'] eq 'produccion')}">
	<link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/all-style.css?${prop['config.web.release']}">
	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/all-plugins.min.js?${prop['config.web.release']}"></script>
	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/all-modules.min.js?${prop['config.web.release']}"></script>
	<script type="text/javascript" src="${prop['config.ibk.url.adobe.analytics']}"></script>
</c:when><c:otherwise>
	<link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/all-style.css?${prop['config.web.release']}">
	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/all-plugins.min.js?${prop['config.web.release']}"></script>
	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/modulos/all-modules.min.js?${prop['config.web.release']}"></script>
	<script type="text/javascript" src="${prop['config.ibk.url.adobe.analytics']}"></script>
</c:otherwise></c:choose>

	<script>
	    (function(m,e,t,r,i,c,s){
	    m[i]=function(c,s){if(s.event!=undefined){_satellite.track(s.event)}};
	    m[t]=m[t]||[];m[r]=m[r]||new DigitalDataHelper(m[t],m[i],true);
	    })(window,document,'digitalData','dataHelper', 'dataListener')
	    
	    var estructuraPageHeader = '${estructuraMapping}';	
	    if(estructuraPageHeader!= ""){
		    var dataHeader = formatHeaderPage(estructuraPageHeader);
		    console.log("Header page: " + JSON.stringify(dataHeader));
		    digitalData.push(dataHeader);
	    }	    
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
		<tiles:insertAttribute name="content" />
		<tiles:insertAttribute name="footer" />
	</div>
	<tiles:insertAttribute name="pixelRemarketingGoogle" />
	<c:choose><c:when test="${prop['config.ambiente.deploy'] eq 'produccion' || prop['config.ambiente.deploy'] eq 'uat'}"><script type="text/javascript">_satellite.pageBottom();</script></c:when><c:otherwise></c:otherwise></c:choose>
</body>
</html>