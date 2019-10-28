<section id="retos" class="container retos-page">
  <div>
    <h3>&iexcl;Este es tu reto del mes!</h3>
    <p>Puedes cumplir m&aacute;s de un reto y ganar</p>
    <a href="#" class="btn-azul">Quiero participar</a>
  </div>
  <div class="lista-retos"></div>
</section>

<script id="template-retos" type="text/x-handlebars-template">
  <article>
    <label for="reto{{id}}" class="lbl-check">
      <input type="checkbox" id="reto{{id}}">
      <span class="ipt-check" data-check="reto{{id}}"><i class="fa"></i></span>
      <span>Cumplir&eacute; este reto</span>
    </label>
    <p>&iexcl;Pasa a cuotas tus compras con tu tarjeta de cr&eacute;dito y gana la devoluci&oacute;n de tus 3 primeras cuotas! Sorteo 28/12.</p>
  </article>
</script>

<script type="text/javascript">
  $(document).ready(function(){
    var retos = new Retos();
    retos.listaRetos();
  });
</script>