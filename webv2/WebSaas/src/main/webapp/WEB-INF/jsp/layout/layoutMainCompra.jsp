<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!DOCTYPE html>
<html lang="es">
	<head>
		
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

		<title>BIM - ${title}</title>

		<link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/normalize.css">
		<link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/general.css?${prop['config.web.release']}">
		<link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/jquery.fancybox.css">
		<link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/font.css">
		<link rel="stylesheet" type="text/css" href="${prop['config.url.recursos.base.web']}static/css/jquery-ui.css">
		
		<jsp:include page="../inc/variablesJS.jsp" />
		
		<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/jquery1.8.3.min.js"></script>
		<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/jquery.validate.js"></script>
		<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/jquery.fancybox.js"></script>	
		<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/main.js?${prop['config.web.release']}"></script>
		<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/jquery-ui.js"></script>
		<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/jquery.blockUI.js"></script>
		
		<!--[if lt IE 9]><script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->

	</head>
	<body>
		<tiles:insertAttribute name="header" />
				
		<tiles:insertAttribute name="content" />
		
		<tiles:insertAttribute name="footer" />
		
		<script type="text/javascript" src="${prop['config.url.recursos.base.web']}static/js/jquery.form.js"></script>

		
	</body>
</html>