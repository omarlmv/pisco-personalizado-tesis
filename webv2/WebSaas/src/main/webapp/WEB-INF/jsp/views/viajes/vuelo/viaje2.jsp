<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<style>
.botonera .continuar-fin {
	background-color: #69be28;
}

body {
	background-color: #f2f1f1;
}

.error, .error-condiciones {
	color: #ff0000;
}

.blockUI {
	background-color: none !important;
}
</style>

<!-- estado -->
<p class="separar"></p>
<section class="estado">
	<p class="barra"></p>
	<div class="limite">
		<section class="flotante-secundario">
			<div class="mis-canjes">
				<div class="cabec-canjespro"></div>
				<div class="mis-canjes-cnt">
					<p>Mis canjes</p>
					<div class="paquete">
						<div class="paquete-destino">
							${busquedaVuelosForm.origen} - ${busquedaVuelosForm.destino} <c:if test="${busquedaVuelosForm.tipoVuelo == 2}">- ${busquedaVuelosForm.origen}</c:if>
						</div>
						<div class="paquete-detalle">
							<ul>
								<li>N&uacute;mero de pasajeros</li>
								<li>${adulto + ninio + infante}</li>
							</ul>
							<ul>
								<li>Ida</li>
								<li>${fechaSalida}</li>
							</ul>
							<c:if test="${not empty fechaRegreso}">
								<ul>
									<li>Vuelta</li>
									<li>${fechaRegreso}</li>
								</ul>
							</c:if>
							<hr>
							<div class="paquete-puntos">
								<ul>
									<li>Total en Millas Benefit</li>
									<li>${formatValorPuntos}</li>
								</ul>
							</div>
						</div>

					</div>
				</div>
				<div class="blockdown-zonaracional">
					<c:if test="${valorPuntos <= totalPuntos}">
						<div class="check-alcance">
							<p>
								<span class="icon-check"></span>Te alcanza con tus Millas Benefit
							</p>
							<p>
								Puntos disponibles tras el canje: <span class="puntos-disponibles-canje">${totalPuntos-valorPuntos}</span>
							</p>
						</div>
					</c:if>
					<div class="miscanjes-mixto-paso2">
						<form>
							<p>
								<span class="color">Si usas</span> <input id="puntos" onkeypress='return isNumberKey(event)' maxlength="9" type="text" value="${valorPuntos}" placeholder="0" /> <input type="hidden"
									id="precio" value="${recomendacion.dolares.total}"></input>
								<input type="hidden" id="precio-soles" value="${recomendacion.soles.total}"></input> <input type="hidden" id="precio-dolares" value="${recomendacion.dolares.total}"></input> <input
									type="hidden" id="totalPuntos" value="${totalPuntos}"></input> <span class="color">Millas Benefit</span>
								<button id="btnActualizarConbinacion" type="button">Actualizar</button>
							</p>
						</form>
						<p>
							<span class="color">Te quedan por cubrir:</span><span class="blue precio-por-cubrir"></span>
						</p>

					</div>
				</div>
			</div>
			<!--mis canjes-->
		</section>
		<!-- /flotante -->
		<p class="titulo-canje">Reserva de vuelo</p>
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
<div class="limite acomodar-racional">
	<div id="informacion-pasajeros" class="paso2-paquetes informacion">
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
						<span class="icon-ico-salida"></span> <strong>Ida:</strong> ${recomendacion.fechaSalidaGeneral}
					</p>
				</div>
				<c:forEach var="segmento" items="${vueloSalida.segmentos}">
					<div class="pasajeros-idadetalle">
						<div class="idadetalle-blok">
							<p class="icon-airlines">
								<img style="height: auto; width: 82px;" alt="${vueloSalida.aereolinea.nombre}" src="${urlIconoCostamar}${segmento.aereolinea.codigoAereoLinea}.png" />
							</p>
							<p>Vuelo ${segmento.numeroVuelo}</p>
							<p>Operado por:${segmento.aerolineaNombreOperadora}</p>
						</div>
						<div class="idadetalle-blok">
							<p>
								Partida:<em class="itinerario-respon">${segmento.formatoCortoFechaSalida}</em>
							</p>
							<p>${segmento.horaSalida}</p>
							<p>${segmento.aereopuertoOrigen.codigoIata}</p>
						</div>
						<div class="idadetalle-blok">
							<p>
								Llegada: <em class="itinerario-respon">${segmento.formatoCortoFechaLlegada}</em>
							</p>
							<p>${segmento.horaLlegada}</p>
							<p>${segmento.aereopuertoDestino.codigoIata}</p>
						</div>
					</div>
				</c:forEach>
				<c:if test="${recomendacion.fechaRegresoGeneral != null}">
					<div class="pasajeros-vuelta">
						<p>
							<span class="icon-ico-regreso"></span> <strong>Vuelta:</strong> ${recomendacion.fechaRegresoGeneral}
						</p>
					</div>
					<c:forEach var="segmento" items="${vueloRegreso.segmentos}">
						<div class="pasajeros-vueltadetalle">
							<div class="vueltadetalle-blok">
								<p class="icon-airlines">
									<img style="height: auto; width: 82px;" alt="${vueloRegreso.aereolinea.nombre}" src="${urlIconoCostamar}${segmento.aereolinea.codigoAereoLinea}.png" />
								</p>
								<p>Vuelo ${segmento.numeroVuelo}</p>
								<p>Operado por:${segmento.aerolineaNombreOperadora}</p>
							</div>
							<div class="vueltadetalle-blok">
								<p>
									Partida:<em class="itinerario-respon">${segmento.formatoCortoFechaSalida}</em>
								</p>
								<p>${segmento.horaSalida}</p>
								<p>${segmento.aereopuertoOrigen.codigoIata}</p>
							</div>
							<div class="vueltadetalle-blok">
								<p>
									Llegada: <em class="itinerario-respon">${segmento.formatoCortoFechaLlegada}</em>
								</p>
								<p>${segmento.horaLlegada}</p>
								<p>${segmento.aereopuertoDestino.codigoIata}</p>
							</div>
						</div>
					</c:forEach>
				</c:if>
			</div>
		</div>
		<form:form id="frmPasajero" action="${pageContext.request.contextPath}/viajes/compra/vuelos/reserva/guardarInformacion" method="POST" autocomplete="off">
			<div class="info-contacto-pasajeros">
				<div class="titulotwo-tabladetalle-paso3">
					<p>Información de contacto</p>
				</div>
				<div class="form-contactoinfo">
					<div class="groupone-info-formpasaj">
						<div class="groupone-infoinput-pasajos">
							<label>Primer nombre: </label><br> <input type="text" id="nombreContacto" name="nombreContacto" maxlength="100" value="${contacto.nombreContacto}">
						</div>
						<div class="groupone-infoinput-pasajos">
							<label>Primer apellido: </label><br> <input type="text" id="apellidoContacto" name="apellidoContacto" maxlength="100" value="${contacto.apellidoContacto}">
						</div>
					</div>
					<div class="groupotwo-info-formpasaj">
						<div class="groupotwo-infoinput-pasajos">
							<label>E-mail: </label><br> <input type="text" id="correoContacto" name="correoContacto" maxlength="100" value="${contacto.correoContacto}">
						</div>
						<div class="groupotwo-infoinput-pasajos">
							<label>Celular: </label><br> <input type="text" id="celularContacto" name="celularContacto" value="${contacto.celularContacto}" maxlength="12">
						</div>
					</div>
					<div class="button-info-formpasaj">
						<a class="" id="buttonContacto">Siguiente</a>
					</div>
				</div>
			</div>
			<div id="divForms" class="forms-regs-pasajeros border-regs-pasajeros">
				<!-- adulto -->
				  <input type="hidden" value="false" id="terminosCondiciones" name="terminosCondiciones"></input> 
			<c:set var="counterPasajero" value="0"></c:set>

				<c:forEach var="i" begin="0" end="${adulto -1}" varStatus="status">
				<c:set var="counterPasajero" value="${i+1}"></c:set>
					<div class="pasajerofirst-form-reg">
						<div class="titulothree-trabladetalle-paso3 off" id="pasajero-${counterPasajero}">
							<p>Pasajero ${i+1}(adulto)</p>
							<p class="subrayado ps2-editar" id="openEditar${counterPasajero}">Editar</p>
						</div>
						<div class="form-pasajerofirstinfo datos-multiple" id="datos-${counterPasajero}">
							<c:if test="${status.first}">
								<div class="checkbox-use">
									<label><input type="checkbox" id="chkClonarDatos"> Usar la informaci&oacute;n de persona de contacto</label>
								</div>
							</c:if>
							<div class="groupone-pasajerofirst">
								<div class="groupone-pasajerofirst-inputone">
									<label>Primer nombre: </label> <input type="hidden" value="ADT" name="adultos[${i}].tipo" /> <input type="hidden" value="${i}" name="adultos[${i}].numeroReferencia" /> <br> 
									<input type="text" id="nombre-persona${counterPasajero}" class="adulto-nombre" name="adultos[${i}].nombre" maxlength="100" value="${adultos[i].nombre}">
								</div>
								<div class="groupone-pasajerofirst-inputone">
									<label>Primer apellido: </label><br> <input type="text" id="apellido-persona${counterPasajero}" name="adultos[${i}].apellido" maxlength="100" value="${adultos[i].apellido}">
								</div>
							</div>
							<div class="groupone-pasajeroseconds">
								<div class="groupone-pasajerofirst-inputtwo tipodocumento">
									<div class="groupone-pasajeofirst-inputtwoone">
										<label>Tipo de documento: </label><br> <select id="tipo_documento-${counterPasajero}" class="tipo_documento" name="adultos[${i}].tipoDocumento" id="${counterPasajero}">
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
										<label>Número de documento: </label><br> <input type="text" id="numero_documento${counterPasajero}" documento="${counterPasajero}" name="adultos[${i}].numeroDocumento"
											value="${adultos[i].numeroDocumento}" maxlength="12" />
									</div>
								</div>
								<div class="groupone-pasajerofirst-inputtwo">
									<label>Fecha de nacimiento: </label><br> <input type="text" readonly="readonly" id="nacimiento-persona${counterPasajero}" name="adultos[${i}].fechaNacimiento"
										class="calendario-nacimiento-adulto" placeholder="dd/mm/aaaa" value="${adultos[i].fechaNacimiento}" /> <span class="icon-ico-calendario"></span>
								</div>
							</div>
							<div class="groupone-pasajerosthird">
								<div class="groupone-pasajerofirst-inputthree">
									<label>Nacionalidad:</label><br> <input type="text" class="nacionalidad" id="nacionalidad-persona${counterPasajero}" name="adultos[${i}].nacionalidad" maxlength="50" value="${adultos[i].nacionalidad}">
								</div>
								<div class="groupone-pasajerofirst-inputthree">
									<label>G&eacute;nero:</label><br> <select name="adultos[${i}].genero" id="genero${counterPasajero}">
										<c:choose>
											<c:when test="${adultos[i].genero eq 'M'}">
												<option value="">-Seleccione-</option>
												<option value="M" selected="selected">Masculino</option>
												<option value="F">Femenino</option>
											</c:when>
											<c:when test="${adultos[i].genero eq 'F'}">
												<option value="">-Seleccione-</option>
												<option value="M">Masculino</option>
												<option value="F" selected="selected">Femenino</option>
											</c:when>
											<c:otherwise>
												<option value="" selected="selected">-Seleccione-</option>
												<option value="M">Masculino</option>
												<option value="F">Femenino</option>
											</c:otherwise>
										</c:choose>
									</select>
								</div>
							</div>
							<div class="groupone-pasajerosthird">
								<div class="pas2-pasaj-frec">
									<label for="pasajero-frecuente${i}"><input type="checkbox" id="pasajero-frecuente${counterPasajero}" class="pasajero-frecuente-check">Pasajero frecuente</label>
								</div>
							</div>
							<div class="groupone-pasajerosthird pasajero-frecuente" style="display: none">
								<div class="groupone-pasajerofirst-inputthree">
									<label>Aerol&iacute;nea:</label><br> <select name="adultos[${i}].aereolinea" id="aereolinea${counterPasajero}" class="selAerolinea">
										<c:forEach items="${mapaAerolineas}" var="aerolinea">
											<c:if test="${aerolinea.key == adultos[i].aereolinea}">
												<option value="${aerolinea.key}" selected="selected">${aerolinea.value}</option>
											</c:if>
											<c:if test="${aerolinea.key != adultos[i].aereolinea}">
												<option value="${aerolinea.key}">${aerolinea.value}</option>
											</c:if>
										</c:forEach>
									</select>
								</div>
								<div class="groupone-pasajerofirst-inputthree">
									<label>Número:</label><br> <input type="text" name="adultos[${i}].numero" id="numero${counterPasajero}" maxlength="20" value="${adultos[i].numero}" class="txtNumero">
								</div>
							</div>
							<c:choose>
								<c:when test="${counterPasajero == adulto+ninio+infante}">
									<div class="button-info-formpasaj">
										<button name="buttonContinuar-${counterPasajeroi}" id="buttonContinuar-${counterPasajero}" class="continuar">Siguiente</button>
									</div>
								</c:when>
								<c:otherwise>
									<div class="button-info-formpasaj">
										<button name="buttonContinuar-${counterPasajero}" id="buttonContinuar-${counterPasajero}" class="continuar">Siguiente pasajero</button>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</c:forEach>
				<!-- ninios -->
				<c:if test="${ninio > 0}">
				<c:forEach var="i" begin="0" end="${ninio-1}">
				<c:set var="counterPasajero" value="${adulto+i+1}"></c:set>
					<div class="pasajerofirst-form-reg">
						<div class="titulothree-trabladetalle-paso3" id="pasajero-${counterPasajero}" data-datos="datos-${counterPasajero}">
							<p>Pasajero ${i+adulto+1}(niño)</p>
							<p class="subrayado ps2-editar">Editar</p>
						</div>
						<div class="form-pasajerofirstinfo datos-multiple" id="datos-${counterPasajero}">

							<div class="groupone-pasajerofirst">
								<div class="groupone-pasajerofirst-inputone">
									<label>Primer nombre: </label> <input type="hidden" value="CHD" name="ninios[${i}].tipo"></input>
									 <input type="text" id="nombre-persona${counterPasajero}" name="ninios[${i}].nombre" maxlength="100" value="${ninios[i].nombre}" /> 
									<input type="hidden" value="${i}" name="ninios[${i}].numeroReferencia" />
								</div>
								<div class="groupone-pasajerofirst-inputone">
									<label>Primer apellido: </label><br> <input type="text" id="apellido-persona${counterPasajero}" name="ninios[${i}].apellido" maxlength="100" value="${ninios[i].apellido}" />
								</div>
							</div>
							<div class="groupone-pasajeroseconds">
								<div class="groupone-pasajerofirst-inputtwo tipodocumento">
									<div class="groupone-pasajeofirst-inputtwoone">
										<label>Tipo de documento: </label><br> <select id="tipo_documento-${counterPasajero}" class="tipo_documento" name="ninios[${i}].tipoDocumento">
											<c:forEach var="tipo" items="${listaTipos}">
												<c:if test="${tipo.id == ninios[i].tipoDocumento}">
													<option value="${tipo.id}" selected="selected">${tipo.descripcion}</option>
												</c:if>
												<c:if test="${tipo.id != ninios[i].tipoDocumento}">
													<option value="${tipo.id}">${tipo.descripcion}</option>
												</c:if>
											</c:forEach>
										</select>
									</div>
									<div class="groupone-pasajeofirst-inputtwoone">
										<label>N&uacute;mero de documento: </label><br> <input type="text" id="numero_documento${counterPasajero}" documento="${counterPasajero}" value="${ninios[i].numeroDocumento}"
											name="ninios[${i}].numeroDocumento" maxlength="12" />
									</div>
								</div>
								<div class="groupone-pasajerofirst-inputtwo">
									<label>Fecha de nacimiento: </label><br> <input type="text" readonly="readonly" value="${ninios[i].fechaNacimiento}" id="nacimiento-persona${counterPasajero}"
										name="ninios[${i}].fechaNacimiento" class="calendario-nacimiento-ninio" placeholder="dd/mm/aaaa" data-tipopasajero="CHD"/> <span class="icon-ico-calendario"></span>
								</div>
							</div>
							<div class="groupone-pasajerosthird">
								<div class="groupone-pasajerofirst-inputthree">
									<label>Nacionalidad:</label><br> <input type="text" class="nacionalidad" id="nacionalidad-persona${counterPasajero}" name="ninios[${i}].nacionalidad" maxlength="50" value="${ninios[i].nacionalidad}" />
								</div>
								<div class="groupone-pasajerofirst-inputthree">
									<label>G&eacute;nero:</label><br> <select name="ninios[${i}].genero" id="genero${counterPasajero}">
										<c:choose>
											<c:when test="${ninios[i].genero eq 'M'}">
												<option value="">- Seleccione -</option>
												<option value="M" selected="selected">Masculino</option>
												<option value="F">Femenino</option>
											</c:when>
											<c:when test="${ninios[i].genero eq 'F'}">
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
							<div class="groupone-pasajerosthird">
								<div class="pas2-pasaj-frec">
									<label><input type="checkbox" class="pasajero-frecuente-check" id="pasajero-frecuente${counterPasajero}">Pasajero frecuente</label>
								</div>
							</div>
							<div class="groupone-pasajerosthird pasajero-frecuente" style="display: none">
								<div class="groupone-pasajerofirst-inputthree">
									<label>Aerolínea:</label><br> <select name="ninios[${i}].aereolinea" id="aereolinea${counterPasajero}" class="selAerolinea">
										<c:forEach items="${vueloSalida.segmentos}" var="segmento" end="0">
											<c:if test="${segmento.aereolinea.codigoAereoLinea == ninios[i-adulto].aereolinea}">
												<option value="${segmento.aereolinea.codigoAereoLinea}" selected="selected">${segmento.aereolinea.nombre}</option>
											</c:if>
											<c:if test="${segmento.aereolinea.codigoAereoLinea != ninios[i-adulto].aereolinea}">
												<option value="${segmento.aereolinea.codigoAereoLinea}">${segmento.aereolinea.nombre}</option>
											</c:if>
										</c:forEach>
									</select>
								</div>
								<div class="groupone-pasajerofirst-inputthree">
									<label>N&uacute;mero:</label><br> <input type="text" name="ninios[${i}].numero" id="numero${counterPasajero}" maxlength="20" value="${ninios[i].numero}" class="txtNumero" />
								</div>
							</div>
							<c:choose>
								<c:when test="${counterPasajero == adulto+ninio+infante}">
									<div class="button-info-formpasaj">
										<button name="buttonContinuar-${counterPasajero}" id="buttonContinuar-${counterPasajero}" class="continuar">Siguiente</button>
									</div>
								</c:when>
								<c:otherwise>
									<div class="button-info-formpasaj">
										<button name="buttonContinuar-${counterPasajero}" id="buttonContinuar-${counterPasajero}" class="continuar">Siguiente pasajero</button>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</c:forEach>
				</c:if>
				<!-- infantes -->
				<c:if test="${infante > 0}">
				<c:forEach var="i" begin="0" end="${infante-1}">
				<c:set var="counterPasajero" value="${adulto+ninio+i+1}"></c:set>
					<div class="pasajerofirst-form-reg">
						<div class="titulothree-trabladetalle-paso3" id="pasajero-${counterPasajero}">
							<p>Pasajero ${adulto+ninio+i+1}(infante)</p>
							<p class="subrayado ps2-editar">Editar</p>
						</div>
						<div class="form-pasajerofirstinfo datos-multiple" id="datos-${counterPasajero}">
							<div class="groupone-pasajerofirst">
								<div class="groupone-pasajerofirst-inputone">
									<label>Primer nombre: </label> <input type="hidden" value="INF" name="infantes[${i}].tipo" /> <input type="text" id="nombre-persona${counterPasajero}" name="infantes[${i}].nombre" maxlength="100"
										value="${infantes[i].nombre}" />
								</div>
								<div class="groupone-pasajerofirst-inputone">
									<label>Primer apellido: </label><br> <input type="text" id="apellido-persona${counterPasajero}" name="infantes[${i}].apellido" maxlength="100" value="${infantes[i].apellido}" />
								</div>
							</div>
							<div class="groupone-pasajeroseconds">
								<div class="groupone-pasajerofirst-inputtwo tipodocumento">
									<div class="groupone-pasajeofirst-inputtwoone">
										<label>Tipo de documento: </label><br> <select id="tipo_documento-${counterPasajero}" name="infantes[${i}].tipoDocumento">
											<c:forEach var="tipo" items="${listaTipos}">
												<c:if test="${tipo.id == infantes[i].tipoDocumento}">
													<option value="${tipo.id}" selected="selected">${tipo.descripcion}</option>
												</c:if>

												<c:if test="${tipo.id != infantes[i].tipoDocumento}">
													<option value="${tipo.id}">${tipo.descripcion}</option>
												</c:if>
											</c:forEach>
										</select>
									</div>
									<div class="groupone-pasajeofirst-inputtwoone">
										<label>Número de documento: </label><br> <input id="numero_documento${counterPasajero}" type="text" documento="${counterPasajero}" value="${infantes[i].numeroDocumento}"
											name="infantes[${i}].numeroDocumento" maxlength="12" />
									</div>
								</div>
								<div class="groupone-pasajerofirst-inputtwo">
									<label>Fecha de nacimiento: </label><br> <input type="text" readonly="readonly" id="nacimiento-persona${counterPasajero}" value="${infantes[i].fechaNacimiento}"
										name="infantes[${i}].fechaNacimiento" class="calendario-nacimiento-infante" placeholder="dd/mm/aaaa" data-tipopasajero="INF"/> <span class="icon-ico-calendario"></span>
								</div>
							</div>
							<div class="groupone-pasajerosthird">
								<div class="groupone-pasajerofirst-inputthree">
									<label>Nacionalidad:</label><br> <input type="text" class="nacionalidad" id="nacionalidad-persona${counterPasajero}" name="infantes[${i}].nacionalidad" maxlength="50"
										value="${infantes[i].nacionalidad}" />
								</div>
								<div class="groupone-pasajerofirst-inputthree">
									<label>G&eacute;nero:</label><br> <select name="infantes[${i}].genero" id="genero${counterPasajero}">
										<c:choose>
											<c:when test="${infantes[i].genero eq 'M'}">
												<option value="">- Seleccione -</option>
												<option value="M" selected="selected">Masculino</option>
												<option value="F">Femenino</option>
											</c:when>
											<c:when test="${infantes[i].genero eq 'F'}">
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
							<div class="groupone-pasajerosthird">
								<div class="groupone-pasajerofirst-inputthree">
									<label>Adulto responsable:</label><br>
									 <select name="infantes[${i}].numeroReferencia" class="slt-responsable" id="slt-responsable${counterPasajero}">
										<option value="0">-Seleccione-</option>
										<c:forEach var="j" begin="1" end="${adulto}">
											<c:if test="${infantes[i].numeroReferencia ==j}">
												<option value="${j}" selected="selected">Adulto ${j}</option>
											</c:if>
											<c:if test="${infantes[i].numeroReferencia !=j}">
												<option value="${j}">Adulto ${j}</option>
											</c:if>
										</c:forEach>
									</select>
								</div>
								<div class="groupone-pasajerofirst-inputthree"></div>
							</div>
							<input type="hidden" id="aereolinea${counterPasajero}" /> <input type="hidden" id="numero${counterPasajero}" />
							<c:choose>
								<c:when test="${counterPasajero == adulto+ninio+infante}">
									<div class="button-info-formpasaj">
										<button name="buttonContinuar-${counterPasajero}" id="buttonContinuar-${counterPasajero}" class="continuar">Siguiente</button>
									</div>
								</c:when>
								<c:otherwise>
									<div class="button-info-formpasaj">
										<button name="buttonContinuar-${counterPasajero}" id="buttonContinuar-${counterPasajero}" class="continuar">Siguiente pasajero</button>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</c:forEach>
				</c:if>
			</div>
			<!-- divForms -->
			<div class="terminos tem-condition" id="divTerminosCondiciones" style="padding-bottom: 35px">
				<label><input type="checkbox" class="terminos-condiciones"><span class="text-tem-condition">He leído y aceptado los <span class="term-modal-lanza"><input
							type="submit" id="condiciones-costamar" name="condiciones-costamar" rel="0" value="t&eacute;rminos y condiciones" /></span> <span class="term-modal-lanza"><input type="submit"
							id="condiciones-aerolinea" name="condiciones-aerolinea" value="de la aerol&iacute;nea" rel="0"></span> y de la <a target="_blank"
						href="https://costamar.com.pe/terminos-condiciones">agencia de viajes</a></span></label> <label style="display: none" id="labelTerminosCondiciones"><span class="error-condiciones"></span></label>
			</div>
			<div class="botonera">
				<a href="<c:url value="/viajes/vuelos/busqueda-vuelos"/>" class="volver">Volver</a> <input type="submit" name="btn-enviar-all" id="btn-enviar-all" value="Siguiente" rel="1"
					class="continuar-fin" disabled="disabled">
			</div>
		</form:form>
	</div>
</div>
<!-- /informacion -->
<!-- POP UP -->
<section class="term-modal">
	<div class="term-modal-cell">
		<div class="term-modal-overlay"></div>
		<div class="term-modal-wancho">
			<div class="term-modal-cerrar"></div>
			<div class="term-modal-box">
				<h3>Términos y condiciones de la aerolínea</h3>
				<div class="term-box">
					<p></p>
					<p id="body-terminosaerolinea" style="display:none;"></p>
				</div>
				<div class="term-close">
					<div class="ter-clos">Cerrar</div>
				</div>
			</div>
		</div>
	</div>
</section>
<!--  -->
<div id="lightbox-procesando" class="lightbox" style="display: none">
	<p class="texto">
		<spring:message code="msg.espere.procesando" htmlEscape="false" />
	</p>
	<span class="cargador"></span>
</div>

<jsp:include page="../../modalMensajes.jsp"/>

<script type="text/javascript">
$(document).on('ready',function() {
	var pasajeros = new Pasajeros();
	cantidadPuntosMaximo = '${requestScope.totalPuntos}';
	pasajeros.simboloMonedaDolar = '${requestScope.simboloMonedaDolar}';
	
	pasajeros.rangoAniosAdulto = ${rangoAniosAdulto};
	pasajeros.fechaMinAdulto = '${fechaMinAdulto}';
	pasajeros.fechaMaxAdulto = '${fechaMaxAdulto}';
	pasajeros.fechaMinNinio = '${fechaMinNinio}';
	pasajeros.fechaMaxNinio = '${fechaMaxNinio}';
	pasajeros.fechaInfante = '${fechaInfante}';
	pasajeros.fechaMaxima = '${fechaMaxima}';
	
	pasajeros.fechaIdaVuelo = '${fechaSalida}';
	pasajeros.fechaRegresoVuelo = '${fechaRegreso}';
	
	pasajeros.inicioinfante = '${adulto + ninio}';
	pasajeros.caninfante = '${infante}';
	pasajeros.totalFormularios = parseInt('${adulto+ninio+infante}');
	pasajeros.htmlLoading = $("#lightbox-procesando");
	pasajeros.init();
});
</script>