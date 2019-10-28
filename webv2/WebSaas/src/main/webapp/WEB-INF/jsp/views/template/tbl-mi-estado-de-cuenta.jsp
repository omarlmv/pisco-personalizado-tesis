
		<!-- informacion -->
		<div id="estado-cuenta" class="limite">
			<div class="titulo-general">
				<h1>Mi estado de cuenta</h1>
				<span></span>
			</div>
			<section class="cliente">
				<ul>
					<li>Cliente:</li>
					<li>Mar�a Iturralde Reyes</li>
				</ul>
				<p>�ltima actualizaci�n: 18/07/2015 10:30 AM</p>
			</section>
			<section class="puntos-actuales">
				<h2 class="subtitulo">Puntos actuales</h2>
				<form>
					<div>
						<div class="puntos">
							<p class="texto">Saldo de puntos:</p>
							<p class="numero">25,000</p>
						</div>
						<div class="resumen">
							<div class="mostrar-periodo">
								<ul class="elemento">
									<li>Mostrar:</li>
									<li>
										<select>
											<option value="">Puntos acumulados</option>
										</select>
									</li>
								</ul>
								<ul class="elemento">
									<li>Per�odo:</li>
									<li>
										<select>
											<option value="">�ltimos 30 d�as</option>
										</select>
									</li>
								</ul>
							</div>
							<div class="leyenda">
								<div>
									<p>RESUMEN DE PUNTOS ACUMULADOS EN LOS �LTIMOS 30 D�AS</p>
									<ul>
										<li><span class="ico-cuadro color-1"></span><span>Compras y uso de tus productos Interbank 15,000</span></li>
										<li><span class="ico-cuadro color-2"></span><span>Promoci�n 5,000</span></li>
										<li><span class="ico-cuadro color-3"></span><span>Devoluci�n o anulaci�n de canjes 5,000</span></li>
									</ul>
								</div>
								<div class="pie"><img src="${prop['config.url.recursos.base.web']}/static/images/pie.png" height="207" width="211" alt="" /></div>
							</div>
						</div>
					</div>
					<p class="botonera">
						<input type="submit" value="GUARDAR / IMPRIMIR ESTADO DE CUENTA" />
					</p>
				</form>
			</section>
		</div>
		<!-- /informacion -->