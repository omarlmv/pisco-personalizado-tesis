<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <link rel="stylesheet" href="${prop['config.url.recursos.base.web']}static/que-es-bim/css/main.css?${prop['config.web.release']}">
    <link rel="shortcut icon" href="${prop['config.url.recursos.base.web']}static/que-es-bim/images/favicon.ico">
    <script>
      var submitted=false;
    </script>
    <style>
    .interbankbenefit_page .millaslist__text{
    	line-height:19px;
    }
    </style>
    
    <div class="page interbankbenefit_page" data-menu="mostrar">
      <div class="block header">
        <div class="bg">
          <div class="content banner__ibkbenefit">
            <div class="banner__container">
              <h3 class="banner__title">¡Ya no tienes que esperar<span>           
                  para disfrutar!</span></h3>
              <div class="banner__logo"></div>
            </div>
          </div>
        </div>
      </div>
      <div class="block menu">
        <div class="content menu__container">
          <ul class="list__container menu_bim clearfix">
            <li class="list__block"><a href="javascript:;" data-content="quees" class="list__text">Bienvenido</a></li>
            <li class="list__block"><a href="javascript:;" data-content="canje" class="list__text">¿Cómo canjeo?</a></li>
            <li class="list__block"><a href="javascript:;" data-content="beneficios" class="list__text">Beneficios</a></li>
            <c:choose>
	            <c:when test="${sessionScope.sessionCliente eq null }">
            <li class="list__block"><a href="javascript:;" data-content="millas__space" class="list__text">Millas Benefit</a></li>            
	        <li class="list__block list__block--last"><a href="javascript:;" data-content="registro" class="list__text">¿Cómo me registro?</a></li>			
				</c:when>
				<c:otherwise>
			<li class="list__block list__block--last"><a href="javascript:;" data-content="millas__space" class="list__text">Millas Benefit</a></li>
				</c:otherwise>
			</c:choose>
            <div class="clearfix"></div>
          </ul>
        </div>
      </div>
      <div class="block quees">
        <div class="content--space">
          <h3 class="prologue__title">¡Bienvenido a Interbank Benefit!</h3>
          <div class="prologue__block">
            <div class="prologue__bgimage">
<!--             	<iframe src="https://www.youtube.com/embed/BjdGfQbFdfk"></iframe> -->
            </div>
          </div>
          <div class="prologue__block">
            <div class="prologue__container">
              <p class="prologue__text"><strong>Interbank Benefit </strong>es tu nuevo <strong>programa de recompensas </strong>con beneficios renovados para que empieces a <strong>disfrutar sin tener que esperar. </strong>Ahora, acumularás Millas Benefit por los consumos y operaciones que realices con tus <strong>Tarjetas de Crédito Interbank </strong>afiliadas al programa.</p>
              <p class="prologue__text">Tus puntos se han convertido automáticamente en <strong>Millas Benefit:</strong></p>
              <p class="prologue__text prologue__text--millas"><strong><span>1 Punto Interbank</span> = <span>1 Milla Benefit</span></strong><br><strong><span>1 Punto Membership Rewards</span> = <span>1 Milla Benefit</span></strong></p>
              <p class="prologue__text">Disfruta hoy canjeando <strong>viajes cuando quieras, a cualquier destino y en la aerolínea que elijas </strong>y, cientos de <strong>productos a precios especiales </strong> ¡en solo un clic! <a href="${pageContext.request.contextPath}/" class="btn__ibkbenefit prologue__btn"> Canjea ahora ></a></p>
            </div>
          </div>
          <div class="clearfix"></div>
        </div>
      </div>
      <div class="block canje">
        <div class="content--space">
          <div class="canje__content">
            <h3 class="canje__title">¿Cómo canjeo?</h3>
            <p class="canje__text">Puedes elegir cualquiera de estas <strong>tres opciones de canje:</strong></p>
            <div class="canje__container">
              <div class="canje__block">
                <div class="icon__block icon__block--card"></div>
                <P class="canje__text">Canjea tus <br><strong>Millas Benefit acumuladas</strong></P>
              </div>
              <div class="canje__block">
                <div class="icon__block icon__block--pluscard"></div>
                <P class="canje__text">Si no te alcanzan tus millas,<br><strong>paga la diferencia </strong><br>con tu Tarjeta de Crédito</P>
              </div>
              <div class="canje__block">
                <div class="icon__block icon__block--plus"></div>
                <P class="canje__text"> <strong>O compra sin usar <br> tus millas </strong>solo con tu<br>Tarjeta de Crédito </P>
              </div>
              <div class="clearfix"></div>
            </div>
          </div>
        </div>
      </div>
      <div class="block beneficios">
        <div class="content--space">
          <div class="ibkbenefit__content">
            <h3 class="ibkbenefit__title">Beneficios</h3>
            <div class="ibkbenefit__container">
              <div class="ibkbenefit__block">
                <div class="article__image article__image--viajes"></div>
                <h5 class="article__title">¡Viaja cuando quieras!</h5>
                <p class="article__text">Viaja todo el año en cualquier aerolínea ¡Tú eliges el destino!</p>
              </div>
              <div class="ibkbenefit__block ibkbenefit__block--last">
                <div class="article__image article__image--prod"></div>
                <h5 class="article__title">¡Productos a precios especiales!</h5>
                <p class="article__text">Canjea el producto que más te guste con solo un clic y te lo llevamos al lugar que elijas</p>
              </div>
              <div class="clearfix"></div>
            </div>
          </div>
        </div>
      </div>
      <div class="block millas__space millas--left">
        <div id="millas_block"></div>
        <div class="millas__content millas__content--left">
          <h3 class="millas__title millas__title--left">Millas Benefit</h3>
          <ul class="millaslist">
            <li class="millaslist__block">
              <div class="icon__block icon__millas icon__millas--money"></div>
              <p class="millaslist__text"> <strong>¿Qué es? <br></strong>Es la moneda que acumulas por cada consumo que realices con tus Tarjetas de Crédito Interbank afiliadas al programa Interbank Benefit.</p>
            </li>
            <li class="millaslist__block">
              <div class="icon__block icon__millas icon__millas--points"></div>
              <p class="millaslist__text"> <strong>¿Mis Millas Benefit vencen?<br></strong>Tus Millas Benefit no vencen, siempre y cuando realices consumos todos los meses con tu Tarjeta de Crédito Interbank.</p>
            </li>
            <li class="millaslist__block">
              <div class="icon__block icon__millas icon__millas--shopping"></div>
              <p class="millaslist__text"> <strong>¿Y si tengo más de una tarjeta afiliada al programa?<br></strong>En este caso, con ambas acumularás Millas Benefit que se sumarán en una sola bolsa de millas.</p>
            </li>
            <li class="millaslist__block">
              <div class="icon__block icon__millas icon__millas--denied"></div>
              <p class="millaslist__text">No acumulan millas los pagos de servicios realizados en Tiendas Interbank, los consumos en casinos, Disposiciones de Efectivo, Extracash ni Compra de Deuda.</p>
            </li>
            <div class="clearfix"></div>
          </ul>
        </div>
      </div>
      <div class="block millas__space millas--right">
        <div class="millas__content millas__content--right">
          <h3 class="millas__title">Además, puedes utilizar tus <strong>Millas Benefit </strong>como dinero en los establecimientos afiliados</h3>
          <p class="millas__text">¡Descarga la relación de todos los establecimientos afiliados aquí!</p><a href="${prop['config.url.recursos.base.web']}static/que-es-bim/pdf/canje_en_establecimientos_new.pdf" target="_blank" class="btn__ibkbenefit gonow__btn">Descarga ahora ></a>
        </div>
        <div class="container__images"></div>
      </div>
      <div class="clearfix"></div>
      <c:if test="${sessionScope.sessionCliente eq null }">
      <div class="block registro">
        <div class="content">
          <h3 class="registro__title">¿Cómo me registro?</h3>
          <div class="registro__content">
            <div class="registro__block">
              <div class="registro__block--image"></div>
            </div>
            <div class="registro__block registro__block--right">
              <ul class="steps__block">
                <li class="steps__list">
                  <p class="steps__text"> <strong>Paso 1: </strong>Entra a www.interbankbenefit.pe y haz clic en <strong>Ingresar.</strong></p>
                </li>
                <li class="steps__list">
                  <p class="steps__text"> <strong>Paso 2: </strong>Haz clic en <strong>Regístrate </strong>y completa tus datos.</p>
                </li>
                <li class="steps__list">
                  <p class="steps__text"> <strong>Paso 3: </strong>Crea y confirma tu contraseña.</p>
                </li>
                <li class="steps__list">
                  <p class="steps__text"> <strong>Paso 4: </strong>Te enviaremos un código de activación a tu e-mail registrado en el banco. Ingrésalo para completar el registro ¡Y listo!</p>
                </li>
              </ul><a href="javascript:void(0)" class="registrar link btn__ibkbenefit registro__btn">Regístrate aquí ></a>
            </div>
            <div class="clearfix"></div>
          </div>
        </div>
      </div>
      </c:if>
    </div>

<script>


$(function() {
	
	
	$(".list__text").click(function(){
		
		var content = $(this).data("content");
		var elemento = "."+content;
		console.log(content);
		$('html, body').animate({
	        scrollTop: $(elemento).offset().top
	    }, 1000);
	});
	
	$(".conocer-mas").click(function() {
	    $('html, body').animate({
	        scrollTop: $("#conocer-mas").offset().top
	    }, 2000);
	});
	
	$("#programa-bim .ingresar").on("click",function(){
		$( ".btnLoginAll" ).trigger( "click" );		
	});
	
});


</script>
 
<style>

.page.interbankbenefit_page *{
	box-sizing:border-box;
}

</style>