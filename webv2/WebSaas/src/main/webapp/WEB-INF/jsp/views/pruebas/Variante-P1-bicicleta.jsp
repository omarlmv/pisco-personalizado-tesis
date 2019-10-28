		<!-- estado -->
		<p class="separar"></p>
		<section class="estado">
			<p class="barra"></p>
			<div class="limite">

				<!-- flotante -->
				<section class="flotante-secundario">
					<p class="tienes"><span class="tienes-texto">Tienes</span> <span class="tienes-puntos">25,000 puntos</span></p>
					<div class="resumen resumen-producto">
						<p class="arrow-resumen"></p>
						<div class="detalle">
							<ul class="producto1">
								<li>1 Bicicleta KTM 12345</li>
								<li class="numero">21,000</li>
							</ul>
							<span class="producto1">+</span>
							<ul class="producto2">
								<li>1 Bicicleta KTM 12345</li>
								<li class="numero">21,000</li>
							</ul>
						</div>
						<div class="total">
							<ul>
								<li>Total en puntos:</li>
								<li class="numero"> 42,000</li>
							</ul>
						</div>
					</div>
					<div class="cotizando">
						<ul class="enunciado compra">
							<li>Mis canjes</li>
							<li><p>Te alcanza con tus puntos</p></li>
						</ul>
						<ul class="disponibles">
							<li>Puntos disponibles tras el canje:</li>
							<li>2,500</li>
						</ul>
						<div class="combinacion">
							<p>Prueba combinaciones posibles:</p>
							<form>
								<ul>
									<li>Si usas estos<br />puntos:</li>
									<li><input type="text" placeholder="25,000" /></li>
									<li><input type="submit" value="ACTUALIZAR" /></li>
								</ul>
							</form>
							<p>Te quedan por cubrir: S/. 465</p>
						</div>
					</div>
					<div class="apoyo">
						<p>Apoyo en línea</p>
						<ul>
							<li><span class="ico-telefono"></span></li>
							<li>311 9020 (Lima)<br />0801 00802 (Provincias)</li>
						</ul>
					</div>
				</section>
				<!-- /flotante -->

				<p class="titulo-canje">Canje de producto</p>
				<ul class="estado-4pasos estado-operacion">
					<li class="paso-1 presente"><span>1</span><p></p></li>
					<li class="paso-2"><span>2</span><p></p></li>
					<li class="paso-3"><span>3</span><p></p></li>
					<li class="paso-4 completo-off"><span></span><p></p></li>
				</ul>
			</div>
		</section>
		<!-- /estado -->
		<!-- informacion -->
		<div class="limite">
			<div id="detalles-producto" class="informacion">
				<form>
					<div class="producto-especifico producto1">
						<a href="#" class="sacar" codigo="1">Sacar de Mis canjes</a>
						<div class="tooltip-sacar tooltip-sacar1" style="display:none;">
							<span class="ico-tooltip-sacar"></span>
							<div>
								<a href="#" class="ico-cerrar"></a>
								<p>¿Seguro deseas eliminar este producto de tus canjes?</p>
								<a href="#" class="boton botonSacar" producto="1">Eliminar</a>	
							</div>
						</div>
						<div class="titulo-general">
							<p>Detalles del producto 1</p>
							<span></span>
						</div>
						<section class="producto">
							<h2 class="subtitulo">Bicicleta KTM 123</h2>
							<img src="${prop['url.imagen.producto']}detalle-bicicleta.jpg" height="270" width="448" alt="" />
							<div class="elemento-grupo">
								<ul class="elemento">
									<li>Cantidad</li>
									<li>
										<select>
											<option value="">1</option>
										</select>
									</li>
								</ul>
								
							</div>
						</section>
						<section class="agregar">
							<p>¿Te gustaría agregar?</p>
							<div>
								<a href="#" class="activo">
									<article>
										<div class="seleccionar"></div>
										<img src="${prop['config.url.recursos.base.web']}static/images/agregar-1.jpg" height="65" width="65" alt="" />
										<p>Casco<br />KTM 567<br />+ <span>1,500</span> puntos</p>
									</article>
								</a>
								<a href="#">
									<article>
										<div class="seleccionar"></div>
										<img src="${prop['config.url.recursos.base.web']}static/images/agregar-2.jpg" height="65" width="65" alt="" />
										<p>Casco<br />KTM 567<br />+ <span>1,500</span> puntos</p>
									</article>
								</a>
								<a href="#">
									<article>
										<div class="seleccionar"></div>
										<img src="${prop['config.url.recursos.base.web']}static/images/agregar-3.jpg" height="65" width="65" alt="" />
										<p>Casco<br />KTM 567<br />+ <span>1,500</span> puntos</p>
									</article>
								</a>
							</div>
						</section>
					</div>
					<div class="producto-especifico producto2">
						<a href="#" class="sacar" codigo="2">Sacar de Mis canjes</a>
						<div class="tooltip-sacar tooltip-sacar2" style="display:none;">>
							<span class="ico-tooltip-sacar"></span>
							<div>
								<a href="#" class="ico-cerrar"></a>
								<p>¿Seguro deseas eliminar este producto de tus canjes?</p>
								<a href="#" class="boton botonSacar" producto="2">Eliminar</a>	
							</div>
						</div>
						<div class="titulo-general">
							<p>Detalles del producto 2</p>
							<span></span>
						</div>
						<section class="producto">
							<h2 class="subtitulo">Bicicleta KTM 123</h2>
							<img src="${prop['url.imagen.producto']}detalle-bicicleta.jpg" height="270" width="448" alt="" />
							<div class="elemento-grupo">
								<ul class="elemento">
									<li>Cantidad</li>
									<li>
										<select>
											<option value="">1</option>
										</select>
									</li>
								</ul>
								
							</div>
						</section>
					</div>
					<div class="botonera">
						<input type="submit" value="CONTINUAR">
						<div>
							<span class="texto">GUARDAR Y SEGUIR NAVEGANDO</span>
							<span class="ico-seguir"></span>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!-- /informacion -->
<script>
$('.botonSacar').click(function(){
	var producto = $(this).attr('producto');
	$('.producto'+producto).remove();
	
});

$('.sacar').click(function(){
	var producto = $(this).attr('codigo');
	$('.tooltip-sacar'+producto).css('display','block');
});

</script>