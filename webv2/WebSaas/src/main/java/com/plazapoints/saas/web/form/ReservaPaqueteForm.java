package com.plazapoints.saas.web.form;

import java.util.Date;
import java.util.List;

import com.piscos.common.util.UUtil;
import com.piscos.domain.BeanBase;
import com.piscos.domain.Categoria;
import com.piscos.domain.util.ParametroContacto;
import com.piscos.domain.util.Pasajero;

public class ReservaPaqueteForm extends BeanBase{
	
	 /**
	  * @date	: 19/10/2015
	  * @time	: 10:50:51
	  * @author	: Arly Fernandez.
	 */
	private static final long serialVersionUID = 1L;

	/*Datos del Pasajero*/
	private String nombre;
	
	private String apellido;
		
	private Integer idNacionalidad;
	
	private String nacionalidad;
	
	private String tipoDocumento;
	
	private String numeroDocumento;
	
	private Date fechaNacimiento;
	
	private String genero;
	
	private String tipo; //'adt’ para adulto, 'chd’ para niño y 'inf’ para infante
	
	private Integer cantAdulto;
	
	private Integer cantNino;
	
	private Integer cantInfante;
	
	/*Datos del producto */
	private Integer costoEnPuntos;
	
	private Double costoDolarPorCubrir;
	
	/*Datos del vuelo*/
	private String codigoAerolinea;
	
	private String aerolinea;
	
	private String codigoIataOrigen;
	
	private String codigoIataDestino;
	
	/*Datos del paquete*/
	private String codigoReserva;
	
	private String precioDolares;
	
	private String precioSoles;
	
	private String precioSolesReferencia;
	
	private String precioDolaresReferencia;
	
	private String tituloPaquete;
	
	private String requerimientoAdicional;
	
	private String origenViaje;
	
	private String destinoViaje;
	
	private Integer habitacion; //tipo doble --1 , tipo matrimonial 2

	private String codigoPaquete;
	
	/*Imagenes*/
	private String smallSampleImage; //Imagen referencial para el listado de paquete
	
	private String mainBannerImage; //Imagen de tipo banner
	
	private String highlightsImage; //Imagende tipo destacado para el listado de paquete
	
	private String image; //Imagen para el listado y busqueda de paquete
	
	private String codigoItinerario;
	
	private String fecSalidaRegresoVuelo;
	
	private String fechaVueloSalida;
	
	private String fechaVueloRegreso;
	
	private String horaVueloSalida;
	
	private String horaVueloRegreso;
	
	private List<Pasajero> adultos;
	
	private List<Pasajero> ninios;
	
	private List<Pasajero> infantes;
	
	private Integer puntosUsados;
	
	private String fechaActual;
	
	private String horaActual;
	
	private String fechaIniVigencia;
	
	private String fechaFinVigencia;
	
	private List<Categoria> productos;
	
	private Boolean htmlScape = true;
	
	private String precioDolaresFormateado;
	
	private ParametroContacto parametroContacto; 
	
	private String cboCupones;
	
	public ReservaPaqueteForm() {}
	
	public ReservaPaqueteForm(ReservaPaqueteSession reservaPaqueteSession) {
		this.nombre = reservaPaqueteSession.getNombre();
		this.apellido = reservaPaqueteSession.getApellido();
		this.idNacionalidad = reservaPaqueteSession.getIdNacionalidad();
		this.nacionalidad = reservaPaqueteSession.getNacionalidad();
		this.tipoDocumento = reservaPaqueteSession.getTipoDocumento();
		this.numeroDocumento = reservaPaqueteSession.getNumeroDocumento();
		this.fechaNacimiento = reservaPaqueteSession.getFechaNacimiento();
		this.genero = reservaPaqueteSession.getGenero();
		this.tipo = reservaPaqueteSession.getTipo();
		this.cantAdulto = reservaPaqueteSession.getCantAdulto();
		this.costoEnPuntos = reservaPaqueteSession.getCostoEnPuntos();
		this.costoDolarPorCubrir = reservaPaqueteSession.getCostoDolarPorCubrir();
		this.codigoAerolinea = reservaPaqueteSession.getCodigoAerolinea();
		this.aerolinea = reservaPaqueteSession.getAerolinea();
		this.codigoIataOrigen = reservaPaqueteSession.getCodigoIataOrigen();
		this.codigoIataDestino = reservaPaqueteSession.getCodigoIataOrigen();
		this.codigoReserva = reservaPaqueteSession.getCodigoReserva();
		this.precioDolares = reservaPaqueteSession.getPrecioDolares();
		this.precioSoles = reservaPaqueteSession.getPrecioSoles();
		this.precioSolesReferencia = reservaPaqueteSession.getPrecioSolesReferencia();
		this.precioDolaresReferencia = reservaPaqueteSession.getPrecioDolaresReferencia();
		this.tituloPaquete = reservaPaqueteSession.getTituloPaquete();
		this.requerimientoAdicional = reservaPaqueteSession.getRequerimientoAdicional();
		this.origenViaje = reservaPaqueteSession.getOrigenViaje();
		this.destinoViaje = reservaPaqueteSession.getDestinoViaje();
		this.habitacion = reservaPaqueteSession.getHabitacion();
		this.codigoPaquete = reservaPaqueteSession.getCodigoPaquete();
		this.smallSampleImage = reservaPaqueteSession.getSmallSampleImage();
		this.mainBannerImage = reservaPaqueteSession.getMainBannerImage();
		this.highlightsImage = reservaPaqueteSession.getHighlightsImage();
		this.image = reservaPaqueteSession.getImage();
		this.codigoItinerario = reservaPaqueteSession.getCodigoItinerario();
		this.fecSalidaRegresoVuelo = reservaPaqueteSession.getFecSalidaRegresoVuelo();
		this.fechaVueloSalida = reservaPaqueteSession.getFechaVueloSalida();
		this.fechaVueloRegreso = reservaPaqueteSession.getFechaVueloRegreso();
		this.horaVueloSalida = reservaPaqueteSession.getHoraVueloSalida();
		this.horaVueloRegreso = reservaPaqueteSession.getHoraVueloRegreso();
		this.adultos = null==reservaPaqueteSession.getReservaPaquete()?null:reservaPaqueteSession.getReservaPaquete().getAdultos();
		this.puntosUsados = reservaPaqueteSession.getPuntosUsados();
		this.fechaActual = reservaPaqueteSession.getFechaActual();
		this.horaActual = reservaPaqueteSession.getHoraActual();
		this.fechaIniVigencia = reservaPaqueteSession.getFechaIniVigencia();
		this.fechaFinVigencia = reservaPaqueteSession.getFechaFinVigencia();
		this.htmlScape = reservaPaqueteSession.getHtmlScape();
		this.precioDolaresFormateado = reservaPaqueteSession.getPrecioDolaresFormateado();
		this.parametroContacto = reservaPaqueteSession.getParametroContacto();
		this.productos = reservaPaqueteSession.getProductos();
	}

	public String getNombre() {
		return UUtil.safeData(nombre, htmlScape);
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return UUtil.safeData(apellido, htmlScape);
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public Integer getIdNacionalidad() {
		return idNacionalidad;
	}

	public void setIdNacionalidad(Integer idNacionalidad) {
		this.idNacionalidad = idNacionalidad;
	}

	public String getNumeroDocumento() {
		return UUtil.safeData(numeroDocumento, htmlScape);
	}

	public String getTipoDocumento() {
		return UUtil.safeData(tipoDocumento, htmlScape);
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}



	public String getGenero() {
		return UUtil.safeData(genero, htmlScape);
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}



	public String getNacionalidad() {
		return UUtil.safeData(nacionalidad, htmlScape);
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getTipo() {
		return UUtil.safeData(tipo, htmlScape);
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getHabitacion() {
		return habitacion;
	}

	public void setHabitacion(Integer habitacion) {
		this.habitacion = habitacion;
	}

	public Integer getCantAdulto() {
		return cantAdulto;
	}

	public void setCantAdulto(Integer cantAdulto) {
		this.cantAdulto = cantAdulto;
	}

	public Integer getCantNino() {
		return cantNino;
	}

	public void setCantNino(Integer cantNino) {
		this.cantNino = cantNino;
	}

	public Integer getCantInfante() {
		return cantInfante;
	}

	public void setCantInfante(Integer cantInfante) {
		this.cantInfante = cantInfante;
	}

	public String getCodigoPaquete() {
		return UUtil.safeData(codigoPaquete, htmlScape);
	}

	public void setCodigoPaquete(String codigoPaquete) {
		this.codigoPaquete = codigoPaquete;
	}

	public void setFecSalidaRegresoVuelo(String fecSalidaRegresoVuelo) {
		this.fecSalidaRegresoVuelo = fecSalidaRegresoVuelo;
	}
	
	public String getFecSalidaRegresoVuelo() {
		return UUtil.safeData(fecSalidaRegresoVuelo, htmlScape);
	}

	public String getFechaVueloSalida() {
		return UUtil.safeData(fechaVueloSalida, htmlScape);
	}

	public void setFechaVueloSalida(String fechaVueloSalida) {
		this.fechaVueloSalida = fechaVueloSalida;
	}

	public String getFechaVueloRegreso() {
		return UUtil.safeData(fechaVueloRegreso, htmlScape);
	}

	public void setFechaVueloRegreso(String fechaVueloRegreso) {
		this.fechaVueloRegreso = fechaVueloRegreso;
	}

	public List<Pasajero> getAdultos() {
		return adultos;
	}

	public void setAdultos(List<Pasajero> adultos) {
		this.adultos = adultos;
	}

	public List<Pasajero> getNinios() {
		return ninios;
	}

	public void setNinios(List<Pasajero> ninios) {
		this.ninios = ninios;
	}

	public List<Pasajero> getInfantes() {
		return infantes;
	}

	public void setInfantes(List<Pasajero> infantes) {
		this.infantes = infantes;
	}

	public String getOrigenViaje() {
		return UUtil.safeData(origenViaje, htmlScape);
	}

	public void setOrigenViaje(String origenViaje) {
		this.origenViaje = origenViaje;
	}

	public String getDestinoViaje() {
		return UUtil.safeData(destinoViaje, htmlScape);
	}

	public void setDestinoViaje(String destinoViaje) {
		this.destinoViaje = destinoViaje;
	}

	public String getTituloPaquete() {
		return UUtil.safeData(tituloPaquete, htmlScape);
	}

	public void setTituloPaquete(String tituloPaquete) {
		this.tituloPaquete = tituloPaquete;
	}

	public String getRequerimientoAdicional() {
		return UUtil.safeData(requerimientoAdicional, htmlScape);
	}

	public void setRequerimientoAdicional(String requerimientoAdicional) {
		this.requerimientoAdicional = requerimientoAdicional;
	}

	public Integer getPuntosUsados() {
		return puntosUsados;
	}

	public void setPuntosUsados(Integer puntosUsados) {
		this.puntosUsados = puntosUsados;
	}

	public String getPrecioDolares() {
		return UUtil.safeData(precioDolares, htmlScape);
	}

	public void setPrecioDolares(String precioDolares) {
		this.precioDolares = precioDolares;
	}

	public String getCodigoItinerario() {
		return UUtil.safeData(codigoItinerario, htmlScape);
	}

	public void setCodigoItinerario(String codigoItinerario) {
		this.codigoItinerario = codigoItinerario;
	}

	public String getFechaActual() {
		return UUtil.safeData(fechaActual, htmlScape);
	}

	public void setFechaActual(String fechaActual) {
		this.fechaActual = fechaActual;
	}

	public String getHoraActual() {
		return UUtil.safeData(horaActual, htmlScape);
	}

	public void setHoraActual(String horaActual) {
		this.horaActual = horaActual;
	}

	public String getCodigoReserva() {
		return UUtil.safeData(codigoReserva, htmlScape);
	}

	public void setCodigoReserva(String codigoReserva) {
		this.codigoReserva = codigoReserva;
	}
	
	public Integer getCostoEnPuntos() {
		return costoEnPuntos;
	}

	public void setCostoEnPuntos(Integer costoEnPuntos) {
		this.costoEnPuntos = costoEnPuntos;
	}
	
	public String getPrecioSoles() {
		return UUtil.safeData(precioSoles, htmlScape);
	}

	public void setPrecioSoles(String precioSoles) {
		this.precioSoles = precioSoles;
	}
	
	public List<Categoria> getProductos() {
		return productos;
	}

	public void setProductos(List<Categoria> productos) {
		this.productos = productos;
	}
	

	public String getFechaIniVigencia() {
		return UUtil.safeData(fechaIniVigencia, htmlScape);
	}

	public void setFechaIniVigencia(String fechaIniVigencia) {
		this.fechaIniVigencia = fechaIniVigencia;
	}

	public String getFechaFinVigencia() {
		return UUtil.safeData(fechaFinVigencia, htmlScape);
	}

	public void setFechaFinVigencia(String fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
	}

	public String getAerolinea() {
		return UUtil.safeData(aerolinea, htmlScape);
	}

	public void setAerolinea(String aerolinea) {
		this.aerolinea = aerolinea;
	}
	
	public String getHoraVueloSalida() {
		return UUtil.safeData(horaVueloSalida, htmlScape);
	}

	public void setHoraVueloSalida(String horaVueloSalida) {
		this.horaVueloSalida = horaVueloSalida;
	}

	public String getHoraVueloRegreso() {
		return UUtil.safeData(horaVueloRegreso, htmlScape);
	}

	public void setHoraVueloRegreso(String horaVueloRegreso) {
		this.horaVueloRegreso = horaVueloRegreso;
	}
	
	public Double getCostoDolarPorCubrir() {
		return costoDolarPorCubrir;
	}

	public void setCostoDolarPorCubrir(Double costoDolarPorCubrir) {
		this.costoDolarPorCubrir = costoDolarPorCubrir;
	}
	
	public String getSmallSampleImage() {
		return UUtil.safeData(smallSampleImage, htmlScape);
	}

	public void setSmallSampleImage(String smallSampleImage) {
		this.smallSampleImage = smallSampleImage;
	}

	public String getMainBannerImage() {
		return UUtil.safeData(mainBannerImage, htmlScape);
	}

	public void setMainBannerImage(String mainBannerImage) {
		this.mainBannerImage = mainBannerImage;
	}

	public String getHighlightsImage() {
		return highlightsImage;
	}

	public void setHighlightsImage(String highlightsImage) {
		this.highlightsImage = highlightsImage;
	}

	public String getImage() {
		return UUtil.safeData(image, htmlScape);
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getPrecioSolesReferencia() {
		return UUtil.safeData(precioSolesReferencia, htmlScape);
	}

	public void setPrecioSolesReferencia(String precioSolesReferencia) {
		this.precioSolesReferencia = precioSolesReferencia;
	}

	public String getPrecioDolaresReferencia() {
		return UUtil.safeData(precioDolaresReferencia, htmlScape);
	}

	public void setPrecioDolaresReferencia(String precioDolaresReferencia) {
		this.precioDolaresReferencia = precioDolaresReferencia;
	}
	
	public String getCodigoIataOrigen() {
		return UUtil.safeData(codigoIataOrigen, htmlScape);
	}

	public void setCodigoIataOrigen(String codigoIataOrigen) {
		this.codigoIataOrigen = codigoIataOrigen;
	}

	public String getCodigoIataDestino() {
		return UUtil.safeData(codigoIataDestino, htmlScape);
	}

	public void setCodigoIataDestino(String codigoIataDestino) {
		this.codigoIataDestino = codigoIataDestino;
	}

	public Boolean getHtmlScape() {
		return htmlScape;
	}

	public void setHtmlScape(Boolean htmlScape) {
		this.htmlScape = htmlScape;
	}

	public String getPrecioDolaresFormateado() {
		return precioDolaresFormateado;
	}

	public void setPrecioDolaresFormateado(String precioDolaresFormateado) {
		this.precioDolaresFormateado = precioDolaresFormateado;
	}

	public String getCodigoAerolinea() {
		return codigoAerolinea;
	}

	public void setCodigoAerolinea(String codigoAerolinea) {
		this.codigoAerolinea = codigoAerolinea;
	}

	public ParametroContacto getParametroContacto() {
		return parametroContacto;
	}

	public void setParametroContacto(ParametroContacto parametroContacto) {
		this.parametroContacto = parametroContacto;
	}

	public String getCboCupones() {
		return cboCupones;
	}

	public void setCboCupones(String cboCupones) {
		this.cboCupones = cboCupones;
	}
}
