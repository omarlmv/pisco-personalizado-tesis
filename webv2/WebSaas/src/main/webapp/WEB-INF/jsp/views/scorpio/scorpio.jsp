<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!doctype html>
<html lang="es">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">
	<meta name="google-site-verification" content="ekpeDq0fiC2zN8p5U4c8rhFtfPh41QfIekgwJHa3LaA" />
	
	<meta name="csrfToken_" content="${_csrf.token}">
    <meta name="headerName_" content="${_csrf.headerName}">
    
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
	
	<title>Interbank Benefit</title>
	
	<c:choose>
		<c:when test="${(prop['config.ambiente.deploy'] eq 'desarrollo') || (prop['config.ambiente.deploy'] eq 'site') || prop['config.ambiente.deploy'] eq 'uat'}">
			<base href="/WebSaas">
		</c:when>
		<c:otherwise>
			<base href="/">
		</c:otherwise>
	</c:choose>
	<c:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 10000) %></c:set>
	<link rel="icon preload" type="image/x-icon" href="${prop['config.url.recursos.base.web']}static/scorpio/favicon.ico">	
	
	<c:choose>
		<c:when test="${(prop['config.ambiente.deploy'] eq 'desarrollo')}">
			<script type="text/javascript" src="${prop['config.app.web']}/configProvided.js?release=1.${rand}"></script>
		</c:when>
		<c:otherwise>
			<script type="text/javascript" src="${prop['config.domain.web']}/configProvided.js?release=1.${rand}"></script>
		</c:otherwise>
	</c:choose>
	<link rel="stylesheet" href="${prop['config.url.recursos.base.web']}static/scorpio/styles.css?${prop['config.web.release']}">
</head>
<body>
	<app-root></app-root>
	
	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/scorpio/runtime.js?${prop['config.web.release']}"></script>
	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/scorpio/polyfills.js?${prop['config.web.release']}"></script>
	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/scorpio/scripts.js?${prop['config.web.release']}"></script>
	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/scorpio/main.js?${prop['config.web.release']}"></script>	
	
	<c:if test="${prop['config.ambiente.deploy'] eq 'produccion' || prop['config.ambiente.deploy'] eq 'uat'}">
		<script type="text/javascript" src="${prop['config.ibk.url.adobe.analytics']}"></script>
	</c:if>
	
	<script type="text/javascript">
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
	
	<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/min/general-components.min.js"></script>
	<script async defer src='https://maps.googleapis.com/maps/api/js?key=${prop["config.google.api.key.map.autocomplete"]}&libraries=places'></script>
	<tiles:insertAttribute name="pixelRemarketingGoogle" />
	<c:if test="${prop['config.ambiente.deploy'] eq 'produccion' || prop['config.ambiente.deploy'] eq 'uat'}">
	<script type="text/javascript">_satellite.pageBottom();</script>
	</c:if>
	
</body>
</html>
