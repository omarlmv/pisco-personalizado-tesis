
<style>
html,body{height:100%;}
.divContentIframe{display: inline-block; margin: 0;padding: 0;width: 100%;}
.divContentIframe iframe{border: 0 none;display: block;margin: auto;height: 600px;width: 100%;}

/* @media and (max-width:1100px){ */
/* .divContentIframe iframe{height: 840px;overflow: hidden;} */
/* } */

.divContentIframe iframe {
   height: 1100px;
   }


@media screen and(max-with:1100px){ 
   .divContentIframe iframe {
   height: 1010px;
   }
}

@media screen and(max-with:625px){
.divContentIframe iframe {
   height: 1160px;
  }
}  
</style>

<div class="divContentIframe sss" id="mi-perfil" data-menu="mostrar">
	<div class="limite">
	<div class="titulo-general">
				<h1 class="titulo">Mi perfil</h1>
			</div>
	<iframe id="ifPerfil" src="${urlMiPerfil}" width="100%">
	
	</iframe>
	</div>
</div>
		
<script type="text/javascript">
activarMenu("none");

onResize = function() {
    ifHeight = $("#ifPerfil").contents().find("body").height();
    $("#ifPerfil").height(ifHeight+32);
}
$(window).bind("load resize", onResize);

</script> 