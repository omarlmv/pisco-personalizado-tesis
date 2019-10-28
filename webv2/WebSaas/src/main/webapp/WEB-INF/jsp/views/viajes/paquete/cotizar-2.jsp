<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<style>
.pasajeros-ida {
	margin-top: 5px;
	margin-bottom: 5px;
}

.botonera .continuar-fin {
	background-color: #69be28;
}

body {
	background-color: #f2f1f1;
}

.error, .error-condiciones {
	color: #ff0000;
}
</style>
<!-- estado -->
<p class="separar"></p>
<section class="estado">
	<p class="barra"></p>
	<div class="limite">
		<!-- flotante -->
		<section class="flotante-secundario">
			<div class="mis-canjes">
				<div class="cabec-canjespro"></div>
				<div class="mis-canjes-cnt">
					<p>Mis canjes</p>
					<div class="paquete">
						<div class="paquete-destino">${reservaPaqueteForm.tituloPaquete}</div>
						<div class="paquete-detalle">
							<ul>
								<li>N&uacute;mero de pasajeros</li>
								<li class="cantidadAdultos">${reservaPaqueteForm.cantAdulto}</li>
							</ul>
							<ul>
								<li>Ida</li>
								<li>${reservaPaqueteForm.fechaVueloSalida}</li>
							</ul>
							<ul>
								<li>Vuelta</li>
								<li>${reservaPaqueteForm.fechaVueloRegreso}</li>
							</ul>
							<hr>
							<div class="paquete-puntos">
								<ul>
									<li>Total en Millas Benefit</li>
									<li id="costoEnPuntos">${formatCostoEnPuntos}</li>
								</ul>
							</div>
						</div>

					</div>
				</div>
				<c:if test="${reservaPaqueteForm.costoEnPuntos <= puntos}">
					<div class="check-alcance">
						<p>
							<span class="icon-ico_check2"></span>Te alcanza con tus Millas Benefit
						</p>
						<p>
							Puntos disponibles tras el canje: <span class="puntos-disponibles-canje">${puntosDspCanje}</span>
						</p>
					</div>
				</c:if>
				<div class="miscanjes-mixto-paso2">

					<p>
						<span class="color">Si usas</span> <input type="text" onkeypress="return isNumber(event)" maxlength="9" class="puntos" value="0" />
							<input type="hidden" id="costoSoles" value="${reservaPaqueteForm.precioSoles}" />
							<input type="hidden" id="costoDolares" value="${reservaPaqueteForm.precioDolares}" />
						<span class="color">Millas Benefit</span>
						<button id="btnActualizarCalculadora" type="button">Actualizar</button>
					</p>
					<p>
						<span class="color">Te quedan por cubrir:</span> <span id="montoPorCubrir" class="blue">${requestScope.simboloMonedaDolar} ${reservaPaqueteForm.precioDolares}</span>
					</p>

				</div>
			</div>
			<!--mis canjes-->
		</section>
		<p class="titulo-canje">Canje de paquete</p>
		<ul class="estado-4pasos estado-operacion">
			<li class="paso-1 pasado"><span>1</span>
				<p></p></li>
			<li class="paso-2 presente"><span>2</span>
				<p></p></li>
			<li class="paso-3"><span>3</span>
				<p></p></li>
			<li class="paso-4 completo-off"><span></span>
				<p></p></li>
		</ul>
	</div>
</section>
<!-- /estado -->
<!-- informacion -->
<c:if test="${msgAlerta ne null}">
 <div style="background-color: #f2dede;border: 2px solid #ebccd1; position: relative;text-align:center; padding-top:20PX; padding-bottom: 20px;color:#a94442;margin-top:10px;width:68%; margin-bottom: 12%;">
		<c:out value="${msgAlerta}" escapeXml="false"></c:out>
	</div>
</c:if>

<c:if test="${msgAlerta eq null}">

<div class="limite">
	<div id="informacion-pasajeros" class="informacion paso2-paquetes">
		<div class="titulo-pasaje-detalleform">
			<p>Ingresa los datos de los pasajeros</p>
		</div>
		<div class="pasajeros">
			<div class="table-detalle">
				<div class="titulo-tabladetalle-paso3">
					<p>Itinerario</p>
				</div>
				<div class="pasajeros-ida">
					<p>
						<span class="icon-ico-salida"></span> <strong>Ida:</strong> ${vueloOrigen[0].fechaSalida}
					</p>
				</div>
				<c:forEach var="segmento" items="${vueloOrigen}">
				<div class="pasajeros-idadetalle">
					<div class="idadetalle-blok">
						<p class="icon-airlines">
							<img style="height: 24px; width: 82px;" alt="${segmento.aereolinea.nombre}" src="${dominioCostamarResources}${segmento.aereolinea.codigoAereoLinea}.png" />
						</p>
						<p>Vuelo ${segmento.numeroVuelo}</p>
						<p>Operado por: ${segmento.aereolinea.nombre}</p>
					</div>
					<div class="idadetalle-blok">
						<p>Partida: ${segmento.formatoCortoFechaSalida}</p>
						<p>${segmento.horaSalida}</p>
						<p>${segmento.aereopuertoOrigen.codigoIata}</p>
					</div>
					<div class="idadetalle-blok">
						<p>Llegada: ${segmento.formatoCortoFechaLlegada}</p>
						<p>${segmento.horaLlegada}</p>
						<p>${segmento.aereopuertoDestino.codigoIata}</p>
					</div>
				</div>
				</c:forEach>
				<c:if test="${not empty vueloDestino[0].fechaLlegada}">
					<div class="pasajeros-vuelta">
						<p>
							<span class="icon-ico-regreso"></span> <strong>Vuelta:</strong> ${vueloDestino[0].fechaSalida}
						</p>
					</div>
					<c:forEach var="segmento" items="${vueloDestino}">
					<div class="pasajeros-idadetalle">
						<div class="idadetalle-blok">
							<p class="icon-airlines">
								<img style="height: 24px; width: 82px;" alt="${segmento.aereolinea.nombre}" src="${dominioCostamarResources}${segmento.aereolinea.codigoAereoLinea}.png" />
							</p>
							<p>Vuelo ${segmento.numeroVuelo}</p>
							<p>Operado por :${segmento.aereolinea.nombre}</p>
						</div>
						<div class="idadetalle-blok">
							<p>Partida: ${segmento.formatoCortoFechaSalida}</p>
							<p>${segmento.horaSalida}</p>
							<p>${segmento.aereopuertoOrigen.codigoIata}</p>
						</div>
						<div class="idadetalle-blok">
							<p>Llegada: ${segmento.formatoCortoFechaLlegada}</p>
							<p>${segmento.horaLlegada}</p>
							<p>${segmento.aereopuertoDestino.codigoIata}</p>
						</div>
					</div>
					</c:forEach>
				</c:if>
				<div class="titulo-cotizar-2">${reservaPaqueteForm.tituloPaquete}</div>
			</div>
		</div>
		<!-- pasajeros -->
		<!-- INICIO PASAJEROS -->
		<form:form id="frmViajesParte2" action="${pageContext.request.contextPath}/viajes/compra/paquetes/reserva/guardarInformacion/${hashPaquete}" method="POST" autocomplete="off">
			<div class="info-contacto-pasajeros">
				<div class="titulotwo-tabladetalle-paso3">
					<p>Información de contacto</p>
				</div>
				<div class="form-contactoinfo" id="datapasajeros">
					<div class="groupone-info-formpasaj">
						<div class="groupone-infoinput-pasajos">
							<label>Nombre: </label><br> <input type="text" id="nombreContacto" name="parametroContacto.nombreContacto" maxlength="100" value="${parametroContacto.nombreContacto}">
						</div>
						<div class="groupone-infoinput-pasajos">
							<label>Apellidos: </label><br> <input type="text" id="apellidoContacto" name="parametroContacto.apellidoContacto" maxlength="100" value="${parametroContacto.apellidoContacto}">
						</div>
					</div>
					<div class="groupotwo-info-formpasaj">
						<div class="groupotwo-infoinput-pasajos">
							<label>E-mail: </label><br> <input type="text" id="correoContacto" name="parametroContacto.correoContacto" maxlength="100" value="${parametroContacto.correoContacto}">
						</div>
						<div class="groupotwo-infoinput-pasajos">
							<label>Celular: </label><br> <input type="text" id="celularContacto" maxlength="12" name="parametroContacto.celularContacto" value="${parametroContacto.celularContacto}">
						</div>
					</div>
					<div class="button-info-formpasaj">
						<button name="buttonContinuar-0" id="buttonContacto">Siguiente</button>
					</div>
				</div>
			</div>

			<div class="forms-regs-pasajeros" id="divForms" data-cantidad-pasajeros="${reservaPaqueteForm.cantAdulto}">
				<c:forEach var="i" begin="0" end="${reservaPaqueteForm.cantAdulto -1}" varStatus="status">

					<div class="pasajerofirst-form-reg">
						<div class="titulothree-trabladetalle-paso3 off" id="pasajero-${i}" data-datos="datos-${i}" >
							<p>Pasajero ${i+1}</p>
							<p class="subrayado ps2-editar" id="openEditar${i}">Editar</p>
						</div>
						<div id="datos-${i}" class="form-pasajerofirstinfo datos-multiple">
							<c:if test="${status.first}">
								<div class="checkbox-use">
									<label> <input type="checkbox" id="chkClonarDatos"> Usar la información de persona de contacto
									</label>
								</div>
							</c:if>
							<div class="groupone-pasajerofirst">
								<div class="groupone-pasajerofirst-inputone">
									<label>Nombre: </label><br> <input type="hidden" value="adt" name="adultos[${i}].tipo"> <input type="text" id="nombre-persona${i}" name="adultos[${i}].nombre"
										maxlength="100" value="${adultos[i].nombre}">
								</div>
								<div class="groupone-pasajerofirst-inputone">
									<label>Apellidos: </label><br> <input type="text" id="apellido-persona${i}" name="adultos[${i}].apellido" maxlength="100" value="${adultos[i].apellido}">
								</div>
							</div>
							<div class="groupone-pasajeroseconds">
								<div class="groupone-pasajerofirst-inputtwo tipodocumento">
									<div class="groupone-pasajeofirst-inputtwoone">
										<label>Tipo de documento: </label><br> <select id="tipo_documento-${i}" class="tipo_documento" name="adultos[${i}].tipoDocumento" id="${i}">
											<c:forEach var="tipo" items="${listaTipos}">
												<c:if test="${tipo.id == adultos[i].tipoDocumento}">
													<option value="${tipo.id}" selected="selected">${tipo.descripcion}</option>
												</c:if>

												<c:if test="${tipo.id != adultos[i].tipoDocumento}">
													<option value="${tipo.id}">${tipo.descripcion}</option>
												</c:if>
											</c:forEach>
										</select>
									</div>
									<div class="groupone-pasajeofirst-inputtwoone">
										<label>Número de documento: </label><br> <input type="text" id="numero_documento${i}" documento="${i}" name="adultos[${i}].numeroDocumento"
											value="${adultos[i].numeroDocumento}" maxlength="12" />
									</div>
								</div>
								<div class="groupone-pasajerofirst-inputtwo">
									<label>Fecha de nacimiento: </label><br> <input type="text" readonly="readonly" id="nacimiento-persona${i}" name="adultos[${i}].fechaNacimiento"
										class="calendario-nacimiento-adulto" placeholder="dd/mm/aaaa" value="${adultos[i].fechaNacimiento}" /> <span class="icon-ico-calendario"></span>
								</div>
							</div>
							<div class="groupone-pasajerosthird">
								<div class="groupone-pasajerofirst-inputthree">
									<label>Nacionalidad:</label><br> <input type="text" class="nacionalidad" id="nacionalidad-persona${i}" name="adultos[${i}].nacionalidad" maxlength="50" value="${adultos[i].nacionalidad}">
								</div>
								<div class="groupone-pasajerofirst-inputthree">
									<label>G&eacute;nero:</label><br> <select name="adultos[${i}].genero" id="genero${i}">
										<c:choose>
											<c:when test="${adultos[i].genero eq 'M'}">
												<option value="">- Seleccione -</option>
												<option value="M" selected="selected">Masculino</option>
												<option value="F">Femenino</option>
											</c:when>
											<c:when test="${adultos[i].genero eq 'F'}">
												<option value="">- Seleccione -</option>
												<option value="M">Masculino</option>
												<option value="F" selected="selected">Femenino</option>
											</c:when>
											<c:otherwise>
												<option value="" selected="selected">- Seleccione -</option>
												<option value="M">Masculino</option>
												<option value="F">Femenino</option>
											</c:otherwise>
										</c:choose>
									</select>
								</div>
							</div>
							<c:if test="${reservaPaqueteForm.cantAdulto > (i+1)}">
								<div class="button-info-formpasaj">
									<button id="buttonContinuar-${i}" id="buttonContinuar-${i}" class="continuar">Siguiente Pasajero</button>
								</div>
							</c:if>
							<c:if test="${reservaPaqueteForm.cantAdulto <= (i+1)}">
								<div class="button-info-formpasaj">
									<button id="buttonContinuar-${i}" id="buttonContinuar-${i}" class="continuar">Siguiente</button>
								</div>
							</c:if>
						</div>
					</div>
				</c:forEach>
			</div>
			<!-- FIN PASAJEROS -->
			<div class="terminos tem-condition" id="divTerminosCondiciones" style="padding-bottom: 35px">
				<label><input type="checkbox" class="terminos-condiciones"><span class="text-tem-condition">He le&iacute;do y acepto los <a href="javascript:void(0);" id="terminosCostamar">t&eacute;rminos y condiciones del servicio</a></span></label> <label style="display: none" id="labelTerminosCondiciones"><span class="error-condiciones"></span></label>
			</div>
			<div class="botonera">
				<a href="javascript:void(0)" class="volver" id="volver-p1">Volver</a> <input type="submit" name="btn-enviar-all" id="btn-enviar-all" value="Siguiente" class="continuar-fin"
					disabled="disabled">
			</div>
		</form:form>
	</div>
	<!-- informacion -->
</div>

</c:if>
<!-- /informacion -->
<!-- POP UP -->
<section class="term-modal">
	<div class="term-modal-cell">
		<div class="term-modal-overlay"></div>
		<div class="term-modal-wancho">
			<div class="term-modal-cerrar"></div>
			<div class="term-modal-box">
				<h3>T&eacute;rminos y condiciones del servicio</h3>
				<div class="term-box">
					<div id="body-terminosaerolinea">
						<div class="terms responsabilidad">
							<h4>Responsabilidad</h4>
							<ol>
								<li>Costamar Travel S.A.C declara expresamente que act&uacute;a en la condici&oacute;n exclusiva de intermediaria en la reserva o contrataci&oacute;n de los distintos servicios
									tur&iacute;sticos y su responsabilidad ser&aacute; determinada &uacute;nicamente en tal condici&oacute;n conforme a las disposiciones legales sobre la materia.</li>
								<li>Costamar Travel S.A.C. no se responsabiliza por los hechos que se produzcan por caso fortuito o fuerza mayor, fen&oacute;menos clim&aacute;ticos o hechos de la naturaleza
									sobrenaturales, por incendios, fallos en maquinarias y/o equipos, acciones de gobierno, autoridades, guerras, hostilidades, huelgas, revueltas, epidemias, etc. que acontezcan antes
									o durante el desarrollo de los servicios que impidan, demoren o de cualquier modo obstaculicen la ejecuci&oacute;n total o parcial de las prestaciones contratadas. No obstante lo
									declarado en el p&aacute;rrafo que antecede.</li>
								<li>Costamar Travel S.A.C. brindar&aacute; atenci&oacute;n al cliente, en su condici&oacute;n de intermediaria, para la mejor soluci&oacute;n del problema y de corresponder,
									solicitar los reembolsos respectivos, de acuerdo a las pol&iacute;ticas de los operadores.</li>
								<li>Costamar Travel S.A.C no se responsabiliza por las alteraciones de horarios, postergaciones y/o cancelaciones, las comodidades, equipos utilizados o cualquier otra
									modificaci&oacute;n realizada por las empresas transportadoras, ni por cualquier otro hecho de tercero, no imputable a Costamar Travel S.A.C., que afecten la prestaci&oacute;n del
									servicio.</li>
							</ol>
						</div>
						<div class="terms documentacion">
							<h4>Documentaci&oacute;n</h4>
							<ol>
								<li>Es responsabilidad del Cliente informarse sobre los requisitos que exigen las autoridades migratorias, aduaneras y sanitarias de los destinos, siendo responsabilidad
									exclusiva del Cliente contar con la documentaci&oacute;n personal que exijan las autoridades mencionadas. La agencia Costamar Travel S.A.C no ser&aacute; responsable por
									inconvenientes que sufriera el Cliente que carezca de documentos en las condiciones necesarias ya sea al salir o al entrar en este u otro pa&iacute;s. Debe tenerse en cuenta que la
									obtenci&oacute;n de las visas en los pa&iacute;ses a visitar por el Cliente e incluso para las conexiones a&eacute;reas, corren por exclusiva responsabilidad de &eacute;ste, como
									as&iacute; tambi&eacute;n la presentaci&oacute;n de la documentaci&oacute;n necesaria para el ingreso a los pa&iacute;ses de destino. Para mayor informaci&oacute;n contactarse al
									tel&eacute;fono (+51-1) 616-6850 (Opci&oacute;n 1).</li>
							</ol>
						</div>
						<div class="terms servicios">
							<h4>Servicios (Paquete)</h4>
							<ol>
								<li>Los paquetes ofrecidos por Costamar Travel S.A.C NO SON REEMBOLSABLES NO PERMITEN CAMBIOS o MODIFICACIONES y NO PUEDEN SER TRANSFERIDOS A OTROS PASAJEROS. [Incluyen Boleto
									a&eacute;reo, servicios de hospedaje y traslados *] (*No aplican traslados para todos los paquetes). En caso de contar con traslados estos son compartidos. Los boletos y vouchers de
									servicio ser&aacute;n entregados como m&aacute;ximo hasta 2 d&iacute;as (48 horas) antes a la salida del viaje.</li>
								<li>TARIFAS SUJETAS A DISPONIBILIDAD O HASTA AGOTAR STOCK de acuerdo a cada destino. Aplica solo para pagos con tarjetas Interbank. PRECIOS INCLUYEN COSTOS ADMINISTRATIVOS. Los
									servicios en el extranjero est&aacute;n inafectos al IGV. Solo aplica para personas naturales, No aplica para empresas.</li>
								<li>En caso de no tomar el paquete debe asumir la penalidad 100% por No-Show (no presentarse) corresponde al total de la estad&iacute;a incluyendo el cargo por impuestos y
									servicios.</li>
								<li>Precios y tasas de embarque est&aacute;n sujetos a cambios por parte de la l&iacute;nea a&eacute;rea y de los gobiernos respectivos, sin previo aviso. La aceptaci&oacute;n
									de esta oferta est&aacute; sujeta a confirmaci&oacute;n que ser&aacute; mediante una comunicaci&oacute;n de Costamar Travel.</li>
								<li>Precios por persona m&iacute;nimo viajando 02 personas juntas en habitaci&oacute;n doble (La acomodaci&oacute;n puede ser 1 cama o 2 camas separadas de acuerdo a
									disponibilidad del hotel y solo se confirma en el ingreso al hotel (Check-In). Los programas aplican restricciones de viaje sujetas a regulaciones de compra anticipada,
									estad&iacute;a m&iacute;nima y m&aacute;xima, vuelos espec&iacute;ficos en cada paquete, disponibilidad a&eacute;rea. Incluyen impuestos y queues obligatorios para la emisi&oacute;n
									de los boletos a&eacute;reos.</li>
								<li>INGRESOS Y SALIDAS: Verificar los voucher de servicios donde se establecen los horarios de ingreso al hotel (Check-In) y salida de hotel (Check-Out) del servicio de
									alojamiento.</li>
								<li>BOLETOS A&Eacute;REOS: No aplican para acumulaci&oacute;n Millas o Kil&oacute;metros.</li>
								<li>HORARIOS: Los Horarios otorgados son en formato de 24hrs.</li>
								<li>TASAS &oacute; CARGOS: En algunos pa&iacute;ses existe un impuesto local denominado &quot;Tasa de Estancia&quot; o &quot;Tasa Tur&iacute;stica&quot; o &quot;Resort Fee&quot;
									o &quot;Impuestos ecol&oacute;gicos&quot; o &quot;Salida Anticipada&quot; que deber&aacute; ser abonado directamente por el usuario final en el hotel.</li>
								<li>FECHAS: El paquete solo aplica para fechas espec&iacute;ficas, seg&uacute;n el destino. NO PERMITE CAMBIO DE FECHA.</li>
								<li>EMBARAZADAS: Aplica restricciones en caso de mujer gestante seg&uacute;n regulaciones de la l&iacute;nea a&eacute;reas y/o servicios hoteleros.</li>
								<li>TRASLADOS: Todos los traslados son compartidos (Este servicio es una forma de trasladarse compartiendo el veh&iacute;culo con otros viajeros que viajan en la misma
									situaci&oacute;n por lo que disfrutar&aacute; de un ambiente agradable e incluso se puede hacer amistad o compartir planes tur&iacute;sticos. Es posible que se deba esperar por
									otros clientes, que compartir&aacute;n el traslado). El cliente deber&aacute; verificar la informaci&oacute;n de su voucher debiendo confirmar a los tel&eacute;fonos otorgados y
									coordinar su traslado con el operador en destino. El cliente debe tener en cuenta que existir una demora del transfer, deber&aacute; llamar al tel&eacute;fono de emergencia que
									figura en el voucher de servicio (Indicaciones se encuentra en el voucher de traslado). * El Cliente debe tener en cuenta que ya sea para los traslados, deber&aacute;n esperar al
									transportista en el lugar y horario establecido por el operador una vez que est&eacute; en el destino. S&iacute; el cliente NO cumple con los horarios, el transportista no
									est&aacute; en la obligaci&oacute;n de esperar o buscar al pasajero. Este continuar&aacute; con su ruta programada. Por tanto, si el pasajero incumple con los horarios y no accede
									al servicio, no se aceptar&aacute;n reclamaciones ni tampoco reembolsos hacia la entidad prestadora de servicio.</li>
								<li>
									<p>SISTEMA DE ALIMENTACI&Oacute;N EN HOTELES:</p> <br />
									<ul>
										<li>Sin sistema de alimentaci&oacute;n solo hospedaje.</li>
										<li>Solo desayunos.</li>
										<li>Sistema Todo Incluido: Todas las comidas, desayunos y almuerzos tipo buffet, Refrescos ilimitados.</li>
									</ul>
									<p>Debe confirmar con nuestras ejecutivas al tel&eacute;fono (+51-1) 616-6850 (Opci&oacute;n 1).</p>
								</li>
								<li>RESTRICCIONES: Por disposici&oacute;n de la industria aeron&aacute;utica y tur&iacute;stica las restricciones de los vuelos o servicios se presentan en ingl&eacute;s.
									Cualquier duda contacte a una de nuestras ejecutivas al tel&eacute;fono (+51-1) 616-6850 (Opci&oacute;n 1). Si usted desea traducir estas restricciones puede tambi&eacute;n hacerlos
									en <a href="https://www.google.com.pe/" target="_blank">www.google.com</a> (https://www.google.com.pe/). Costamar Travel S.A.C no se hace responsable por cualquier error de
									contextualizaci&oacute;n o imprecisi&oacute;n en las palabras traducidas.
								</li>
								<li>USUARIO FINAL: El tarjeta habiente se obliga a comunicar y/o trasmitir la presente informaci&oacute;n al titular beneficiado o usuario final del servicio que adquiere y
									asumir&aacute; la responsabilidad de transmitir la informaci&oacute;n brindada y la entrega de la documentaci&oacute;n proporcionada al pasajero Titular. Es de responsabilidad del
									cliente contar con DNI vigente, pasaporte vigente (M&iacute;nimo 6 meses de vigencia al finalizar el viaje) y todos los documentos necesarios que requiere para realzar el viaje.</li>
								<li>CAMBIO DE FECHA: NO PERMITE CAMBIO DE FECHA, el paquete solo aplica para fechas espec&iacute;ficas seg&uacute;n el destino.</li>
								<li>CANCELACIONES - REEMBOLSOS: El paquete NO SON REEMBOLSABLES NO PERMITEN CAMBIOS o MODIFICACIONES y NO PUEDEN SER TRANSFERIDOS A OTROS PASAJEROS. <br />
									<p>Su reserva ser&aacute; confirmada dentro de las pr&oacute;ximas 24 horas, se enviar&aacute; un documento de cobranza confirmando la reserva, caso contrario se
										realizar&aacute; el extorno en su tarjeta de cr&eacute;dito en un plazo no mayor a los cinco (7) d&iacute;as h&aacute;biles</p> <br />
									<ul>
										<li>En caso de desistir voluntariamente de la compra por parte del tarjeta habiente a la agencia Costamar Travel S.A.C, las cancelaciones estar&aacute;n sujetas a las
											condiciones contractuales bajo las cuales estos &uacute;ltimos presten sus servicios (operadores), Aplica la solicitud solo el mismo d&iacute;a de la compra hasta las 23:59 hrs,
											el tarjeta habiente deber&aacute; de enviar un email a <a href="mailto:reservasinterbankbenefit@costamar.com.pe" target="_blank">reservasinterbankbenefit@costamar.com.pe</a>. Es
											obligatorio adjuntar copia DNI o pasaporte del pasajero(s). De no enviar dicha informaci&oacute;n NO se proceder&aacute; con la solicitud y el tarjeta habiente asumir&aacute; toda
											responsabilidad. Solicitudes solo aplican en los horarios de atenci&oacute;n especificados. Es posible que existan cargos administrativos por el proceso. Para mayor
											informaci&oacute;n debe contactar al (+51-1) 616-6850 (Opci&oacute;n 1).
										</li>
										<li>Costamar Travel S.A.C no es responsable por los servicios que no fueran tomados por el Cliente en tiempo y forma convenidos, ni por las consecuencias que de ello emanen,
											como la cancelaci&oacute;n del servicio por parte del prestador, ni por el reintegro, reembolso o devoluci&oacute;n del importe abonado por dicho servicio, en ese caso, la agencia
											podr&aacute; retener del monto a reembolsar, los gastos incurridos m&aacute;s la comisi&oacute;n de los servicios contratados con terceros.</li>
										<li>En caso de reclamo y/o reembolso por causas de fuerza mayor queda sujeto a las modalidades, condiciones y procedimientos de los distintos prestadores de los servicios.
											Pudiendo el cliente solicitar como m&aacute;ximo 48 hrs antes de iniciar el viaje, acompa&ntilde;ando la documentaci&oacute;n sustentator&iacute;a m&eacute;dica, dicha la
											informaci&oacute;n ser&aacute; enviada a los operadores para su evaluaci&oacute;n. La solicitud ser&aacute; remitirla al proveedor o proveedores quienes evaluar&aacute;n la
											procedencia de la solicitud. El cliente debe aceptar que la decisi&oacute;n ser&aacute; del proveedor no teniendo nada que reclamar a Costamar Travel, quedando directamente a
											criterio de los proveedores seg&uacute;n su evaluaci&oacute;n.</li>
										<li>En ning&uacute;n caso, Costamar Travel S.A.C costear&aacute; los eventuales costos y/o cargos y/o impuestos y/o grav&aacute;menes derivados de realizar cualquier tipo de
											devoluci&oacute;n y/o reembolso y/o reintegro en caso de proceder alguno.</li>
										<li>En los casos de servicios de vuelos regulares, las condiciones son impuestas por las transportadoras con quien el Cliente deber&aacute; entenderse directamente y reclamar
											en su caso por cualquier inconveniente o perjuicio sufrido como consecuencia del viaje. Costamar Travel S.A.C no tiene control sobre estas condiciones y ofrecer&aacute; todo apoyo
											a su alcance para llevarlos a cabo.</li>
									</ul>
								</li>
							</ol>
						</div>
						<div class="terms no-incluidos">
							<h4>Servicios no incluidos</h4>
							<ol>
								<li>Propinas: Las propinas no est&aacute;n incluidas en ning&uacute;n servicio que Costamar Travel ofrece. Si el cliente requiere servicios adicionales como maleteros las
									propinas si ser&aacute;n obligatorias.</li>
								<li>Otros: S&oacute;lo debe considerarse incluido en los precios aquello que est&aacute; expresa y literalmente detallado en la documentaci&oacute;n de viaje, de lo que se
									desprende que no est&aacute; incluido en los precios: visados, certificados de vacunaci&oacute;n, tasas de embarque o de aeropuertos, extras en hoteles (caf&eacute;s, licores, aguas
									minerales, lavado y planchado, propinas, comunicaciones, etc.), tasas sobre servicios, impuestos, gastos e intereses de operaciones financieras, excursiones y visitas opcionales,
									alimentaci&oacute;n extra en ruta, comidas y/o gastos adicionales, o perjuicios producidos por cancelaciones, demoras en las salidas o llegadas de los medios de transporte, o por
									razones imprevistas ajenas a Costamar Travel S.A.C, entre otros.</li>
							</ol>
						</div>
						<div class="terms seguros">
							<h4>Seguros</h4>
							<ol>
								<li>Le aconsejamos que viaje siempre convenientemente asegurado. En ciertos casos, le ofrecemos la posibilidad de a&ntilde;adir un seguro a su viaje en ciertos destinos.
									Adicionalmente nuestros agentes le proporcionar&aacute;n toda la informaci&oacute;n que pueda necesitar para decidir si quiere contratar el seguro propuesto. Tenga en cuenta que los
									seguros no permiten cancelaciones, por tanto no son reembolsables. A menos que su visado haya sido denegado, en cuyo caso debe contactarse con nosotros para su devoluci&oacute;n
									previa aplicaci&oacute;n de la penalidad correspondiente de acuerdo a las pol&iacute;ticas del prestador del servicio.</li>
								<li>En caso de requerir asistencia, el cliente deber&aacute; contactar a los tel&eacute;fonos de la aseguradora, tel&eacute;fonos que se encuentran en su voucher. *Existen
									excepciones en la cobertura de la p&oacute;liza en caso de tener enfermedades preexistentes.</li>
								<li>En caso no se tome la tarjeta de asistencia se libera de toda responsabilidad a Costamar Travel S.A.C, de los problemas que puedan existir antes, durante y despu&eacute;s
									del viaje, no teniendo derecho a efectuar reclamo o reembolso por gastos derivados de un percance o accidente.</li>
							</ol>
						</div>
						<div class="terms equipaje">
							<h4>Equipaje</h4>
							<ol>
								<li>Costamar Travel S.A.C no se responsabiliza por el deterioro y/o extrav&iacute;o y/o hurto y/o robo y/o p&eacute;rdida del equipaje y dem&aacute;s efectos personales del
									Cliente sea por servicios contratados a trav&eacute;s de la intermediaci&oacute;n de Costamar Travel S.A.C o tomados individualmente por el Cliente. Se recomienda al Cliente
									contratar un seguro de viaje para cubrir parte de los riesgos mencionados.</li>
								<li>Las Regulaciones de equipaje depender&aacute;n de las pol&iacute;ticas de la l&iacute;nea a&eacute;rea (Equipaje de mano y bodega permitido). Para mayor informaci&oacute;n
									debe contactar al (+51-1) 616-6850 (Opci&oacute;n 1).</li>
							</ol>
						</div>
						<div class="terms atencion-cliente">
							<h4>Atenci&oacute;n al cliente</h4>
							<ol>
								<li>Las presentes condiciones generales, junto con la restante documentaci&oacute;n que se entrega al Cliente, conforman el Contrato de Viaje. Estas condiciones son entregadas
									por Costamar Travel S.A.C al Cliente al momento de realizar la reserva de los servicios. El Cliente declara conocer y aceptar las presentes condiciones generales de
									contrataci&oacute;n y dicha aceptaci&oacute;n queda ratificada por medio de uno cualquiera de los siguientes actos:</li>
								<li>La reserva y/o de los servicios contratados antes del inicio del viaje, por cualquier forma o modalidad.</li>
								<li>La aceptaci&oacute;n de los documentos de los servicios contratados.</li>
								<li>Mediante el uso de una porci&oacute;n cualquiera de los servicios contratados.</li>
							</ol>
						</div>
						<div class="terms reclamos">
							<h4>Reclamos y/o quejas</h4>
							<br />
							<p>En Per&uacute; Costamar cuenta con un Libro de Reclamaciones disponible de manera virtual y en sus diferentes locales de atenci&oacute;n al p&uacute;blico, de acuerdo a lo
								indicado en el C&oacute;digo de Protecci&oacute;n y Defensa del Consumidor de Per&uacute;, as&iacute; mismo posee con una cuenta de email para la Atenci&oacute;n al Cliente que es
								atencionalcliente@costamar.com.pe o al tel&eacute;fono (+51-1) 616-6850 (Opci&oacute;n 2).</p>
						</div>
						<div class="terms importante">
							<h4>Importante</h4>
							<ul>
								<li>Las categor&iacute;as que reflejan los hoteles han sido proporcionadas por los propios establecimientos y siempre obedeciendo a las normativas espec&iacute;ficas que rigen
									en cada pa&iacute;s, por tanto no puede ser similar en prestaciones y calidad de un hotel en un pa&iacute;s a otro hotel de otro pa&iacute;s aun teniendo la misma categor&iacute;a.
									Por lo tanto la categorizaci&oacute;n de estrellas en hoteles en el exterior no se rige al Reglamento de Establecimiento de Hospedaje del Per&uacute;. (Decreto Supremo NÂ°
									029-2004-MINCETUR).</li>
								<li>La informaci&oacute;n sobre los hoteles proporcionada es lo m&aacute;s ajustada posible a las indicaciones del propio establecimiento. Costamar Travel no se hace responsable
									de la falta de fidelidad a la realidad de la informaci&oacute;n facilitada por sus proveedores.</li>
								<li>Costamar Travel S.A.C proporcionara la informaci&oacute;n que el Hotel le provee acerca de la existencia y duraci&oacute;n de obras de reforma o renovaci&oacute;n en el
									mismo. Costamar Travel S.A.C no aceptar&aacute; reclamaciones por obras de las que no estuviera informado o que se prolonguen m&aacute;s all&aacute; de la fecha comunicada de
									t&eacute;rmino de las mismas.</li>
								<li>Costamar Travel y CTM Tours declaran expresamente que cumple con una labor de empresa intermediaria al participar en la reserva o contrataci&oacute;n de los distintos
									servicios tur&iacute;sticos provistos por sus socios estrat&eacute;gicos. Por tal motivo, Costamar Travel y CTM Tours no responsabilizan por las alteraciones de horarios,
									postergaciones y/o cancelaciones o cualquier otra modificaci&oacute;n realizada a las condiciones que rigen los servicios contratados a terceros proveedores, no somos responsables
									de perjuicio o retraso alguno derivado de circunstancias ajenas a nuestro control (ya sean causas fortuitas, de fuerza mayor y cualquier p&eacute;rdida, da&ntilde;o, accidente o
									alguna otra irregularidad que pudiera ocurrirle al usuario final o cliente), son responsables &uacute;nicamente por la organizaci&oacute;n de los tours adquiridos.</li>
							</ul>
						</div>
						<div class="terms recomendaciones">
							<h4>Recomendaciones</h4>
							<ol>
								<li>Recuerda revisar la documentaci&oacute;n antes de viajar (documento de identidad, visas, permisos notariales, vacunas, etc.).</li>
								<li>El pasaporte debe tener una vigencia m&iacute;nima de seis (6) meses. Verificar que la visa est&eacute; dentro de la fecha de vigencia.</li>
								<li>En caso que una de las pasajeras se encuentre Embarazada, le recordamos Aplica restricciones en caso de mujer gestante seg&uacute;n regulaciones de la l&iacute;nea
									a&eacute;reas y/o servicios.</li>
								<li>Para viajes Nacionales estar en el aeropuerto m&iacute;nimo dos (02) horas antes de la salida de vuelo. Para viajes internacionales estar en el aeropuerto m&iacute;nimo tres
									(03) horas antes de la salida de su vuelo.</li>
							</ol>
						</div>
						<div class="terms contacto">
							<h4>Contactos</h4>
							<br />
							<p>Costamar Travel pone a su disposici&oacute;n los siguientes n&uacute;meros de tel&eacute;fonos de Lima:</p>
							<ul>
								<li>(+51-1) 616-6850 (Opci&oacute;n 1)</li>
								<li>RPC (+51) 989-046-508. Emergencias las 24hrs. / WhatsApp (+51) 989-046-508.</li>
								<li>Para consultas por email: <a href="mailto:reservasinterbankbenefit@costamar.com.pe" target="_blank">reservasinterbankbenefit@costamar.com.pe</a></li>
							</ul>
						</div>
						<div class="terms final">
							<br />
							<p>En caso se presente un Retraso A&eacute;reo favor de contactarse con los n&uacute;meros de emergencia que se encontrar&aacute;n en su voucher de hotel y/o traslados.</p>
						</div>
					</div>
				</div>
				<div class="term-close">
					<div class="ter-clos">Cerrar</div>
				</div>
			</div>
		</div>
	</div>
</section>
<!--  -->
<!-- limite -->
<div id="lightbox-procesando" class="lightbox" style="display: none">
	<p class="texto">
		<spring:message code="msg.espere.procesando" htmlEscape="false" />
	</p>
	<span class="cargador"></span>
</div>
<!-- /informacion -->
<script type="text/javascript">
$(document).on('ready',function() {
	var cotizarPaquete_2 = new CotizarPaquete_2();
	cotizarPaquete_2.simboloMonedaDolar = '${requestScope.simboloMonedaDolar}';
	cotizarPaquete_2.htmlLoading = $("#lightbox-procesando");
	cotizarPaquete_2.montoPorCubrirFinal = '${formatCostoDolarPorCubrir}';
	cotizarPaquete_2.costoPuntos = '${formatPuntosVista}';
	cotizarPaquete_2.stockPaquete = "${stockPaquete}";
	cotizarPaquete_2.keyItem = "${keyItem}";
	cotizarPaquete_2.tokenPaquete="${hashPaquete}";
	cotizarPaquete_2.costoEnPuntos = "${costoEnPuntos}";
	cotizarPaquete_2.puntos = "${puntos}";
	cotizarPaquete_2.init();
});
</script>