<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
<c:when test="${(prop['config.ambiente.deploy'] ne 'desarrollo')}">
<script>
!function(f,b,e,v,n,t,s){if(f.fbq)return;n=f.fbq=function(){n.callMethod?
n.callMethod.apply(n,arguments):n.queue.push(arguments)};if(!f._fbq)f._fbq=n;
n.push=n;n.loaded=!0;n.version='2.0';n.queue=[];t=b.createElement(e);t.async=!0;
t.src=v;s=b.getElementsByTagName(e)[0];s.parentNode.insertBefore(t,s)}(window,
document,'script','https://connect.facebook.net/en_US/fbevents.js');
fbq('init', '768296169980827'); // Insert your pixel ID here.
fbq('track', 'PageView');
//fbq('track', 'Purchase');
fbq('track', 'Purchase', {
	value: '${pixelFBMontoUsado}',
	currency: '${pixelFBMonedaUsado}'
	});
</script>
<noscript><img height="1" width="1" style="display:none"
src="https://www.facebook.com/tr?id=768296169980827&ev=PageView&noscript=1"
/></noscript>
<!-- DO NOT MODIFY -->
<!-- End Facebook Pixel Code -->
</c:when>
</c:choose>