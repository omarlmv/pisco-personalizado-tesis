package com.plazapoints.saas.web.form;

import java.util.Date;
import java.util.List;

import com.piscos.domain.BeanBase;
import com.piscos.domain.Categoria;
import com.piscos.domain.util.Paquete;
import com.piscos.domain.util.ParametroContacto;
import com.piscos.domain.util.ReservaPaquete;

public class ReservaPaqueteSession extends BeanBase{

	private static final long serialVersionUID = 1L;

	private String nombre;
	private String apellido;
	private Integer idNacionalidad;
	private String nacionalidad;
	private String tipoDocumento;
	private String numeroDocumento;
	private Date fechaNacimiento;
	private String genero;
	private String tipo;
	private Integer cantAdulto;
	private Integer cantidadPasajeros;
	private Integer cantidadPaquetes;
	private Integer costoEnPuntos;
	private Double costoDolarPorCubrir;
	private String codigoAerolinea;
	private String aerolinea;
	private String codigoIataOrigen;
	private String codigoIataDestino;
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
	private Integer puntosUsados;
	private String fechaActual;
	private String horaActual;
	private String fechaIniVigencia;
	private String fechaFinVigencia;
	private Boolean htmlScape = true;
	private String precioDolaresFormateado;
	private Paquete paquete;
	private List<Categoria> productos;
	private String hashGenerado;
	private ReservaPaquete reservaPaquete;
	private ParametroContacto parametroContacto; 
	private Double importeDescuentoCupon;
	
	public ParametroContacto getParametroContacto() {
		return parametroContacto;
	}
	public void setParametroContacto(ParametroContacto parametroContacto) {
		this.parametroContacto = parametroContacto;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
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
	public String getNacionalidad() {
		return nacionalidad;
	}
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
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
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Integer getCantAdulto() {
		return cantAdulto;
	}
	public void setCantAdulto(Integer cantAdulto) {
		this.cantAdulto = cantAdulto;
	}
	public Integer getCostoEnPuntos() {
		return costoEnPuntos;
	}
	public void setCostoEnPuntos(Integer costoEnPuntos) {
		this.costoEnPuntos = costoEnPuntos;
	}
	public Double getCostoDolarPorCubrir() {
		return costoDolarPorCubrir;
	}
	public void setCostoDolarPorCubrir(Double costoDolarPorCubrir) {
		this.costoDolarPorCubrir = costoDolarPorCubrir;
	}
	public String getCodigoAerolinea() {
		return codigoAerolinea;
	}
	public void setCodigoAerolinea(String codigoAerolinea) {
		this.codigoAerolinea = codigoAerolinea;
	}
	public String getAerolinea() {
		return aerolinea;
	}
	public void setAerolinea(String aerolinea) {
		this.aerolinea = aerolinea;
	}
	public String getCodigoIataOrigen() {
		return codigoIataOrigen;
	}
	public void setCodigoIataOrigen(String codigoIataOrigen) {
		this.codigoIataOrigen = codigoIataOrigen;
	}
	public String getCodigoIataDestino() {
		return codigoIataDestino;
	}
	public void setCodigoIataDestino(String codigoIataDestino) {
		this.codigoIataDestino = codigoIataDestino;
	}
	public String getCodigoReserva() {
		return codigoReserva;
	}
	public void setCodigoReserva(String codigoReserva) {
		this.codigoReserva = codigoReserva;
	}
	public String getPrecioDolares() {
		return precioDolares;
	}
	public void setPrecioDolares(String precioDolares) {
		this.precioDolares = precioDolares;
	}
	public String getPrecioSoles() {
		return precioSoles;
	}
	public void setPrecioSoles(String precioSoles) {
		this.precioSoles = precioSoles;
	}
	public String getPrecioSolesReferencia() {
		return precioSolesReferencia;
	}
	public void setPrecioSolesReferencia(String precioSolesReferencia) {
		this.precioSolesReferencia = precioSolesReferencia;
	}
	public String getPrecioDolaresReferencia() {
		return precioDolaresReferencia;
	}
	public void setPrecioDolaresReferencia(String precioDolaresReferencia) {
		this.precioDolaresReferencia = precioDolaresReferencia;
	}
	public String getTituloPaquete() {
		return tituloPaquete;
	}
	public void setTituloPaquete(String tituloPaquete) {
		this.tituloPaquete = tituloPaquete;
	}
	public String getRequerimientoAdicional() {
		return requerimientoAdicional;
	}
	public void setRequerimientoAdicional(String requerimientoAdicional) {
		this.requerimientoAdicional = requerimientoAdicional;
	}
	public String getOrigenViaje() {
		return origenViaje;
	}
	public void setOrigenViaje(String origenViaje) {
		this.origenViaje = origenViaje;
	}
	public String getDestinoViaje() {
		return destinoViaje;
	}
	public void setDestinoViaje(String destinoViaje) {
		this.destinoViaje = destinoViaje;
	}
	public Integer getHabitacion() {
		return habitacion;
	}
	public void setHabitacion(Integer habitacion) {
		this.habitacion = habitacion;
	}
	public String getCodigoPaquete() {
		return codigoPaquete;
	}
	public void setCodigoPaquete(String codigoPaquete) {
		this.codigoPaquete = codigoPaquete;
	}
	public String getSmallSampleImage() {
		return smallSampleImage;
	}
	public void setSmallSampleImage(String smallSampleImage) {
		this.smallSampleImage = smallSampleImage;
	}
	public String getMainBannerImage() {
		return mainBannerImage;
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
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCodigoItinerario() {
		return codigoItinerario;
	}
	public void setCodigoItinerario(String codigoItinerario) {
		this.codigoItinerario = codigoItinerario;
	}
	public String getFecSalidaRegresoVuelo() {
		return fecSalidaRegresoVuelo;
	}
	public void setFecSalidaRegresoVuelo(String fecSalidaRegresoVuelo) {
		this.fecSalidaRegresoVuelo = fecSalidaRegresoVuelo;
	}
	public String getFechaVueloSalida() {
		return fechaVueloSalida;
	}
	public void setFechaVueloSalida(String fechaVueloSalida) {
		this.fechaVueloSalida = fechaVueloSalida;
	}
	public String getFechaVueloRegreso() {
		return fechaVueloRegreso;
	}
	public void setFechaVueloRegreso(String fechaVueloRegreso) {
		this.fechaVueloRegreso = fechaVueloRegreso;
	}
	public String getHoraVueloSalida() {
		return horaVueloSalida;
	}
	public void setHoraVueloSalida(String horaVueloSalida) {
		this.horaVueloSalida = horaVueloSalida;
	}
	public String getHoraVueloRegreso() {
		return horaVueloRegreso;
	}
	public void setHoraVueloRegreso(String horaVueloRegreso) {
		this.horaVueloRegreso = horaVueloRegreso;
	}
	public Integer getPuntosUsados() {
		return puntosUsados;
	}
	public void setPuntosUsados(Integer puntosUsados) {
		this.puntosUsados = puntosUsados;
	}
	public String getFechaActual() {
		return fechaActual;
	}
	public void setFechaActual(String fechaActual) {
		this.fechaActual = fechaActual;
	}
	public String getHoraActual() {
		return horaActual;
	}
	public void setHoraActual(String horaActual) {
		this.horaActual = horaActual;
	}
	public String getFechaIniVigencia() {
		return fechaIniVigencia;
	}
	public void setFechaIniVigencia(String fechaIniVigencia) {
		this.fechaIniVigencia = fechaIniVigencia;
	}
	public String getFechaFinVigencia() {
		return fechaFinVigencia;
	}
	public void setFechaFinVigencia(String fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
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
	public Paquete getPaquete() {
		return paquete;
	}
	public void setPaquete(Paquete paquete) {
		this.paquete = paquete;
	}
	public String getHashGenerado() {
		return hashGenerado;
	}
	public void setHashGenerado(String hashGenerado) {
		this.hashGenerado = hashGenerado;
	}
	public Integer getCantidadPaquetes() {
		return cantidadPaquetes;
	}
	public void setCantidadPaquetes(Integer cantidadPaquetes) {
		this.cantidadPaquetes = cantidadPaquetes;
	}
	public List<Categoria> getProductos() {
		return productos;
	}
	public void setProductos(List<Categoria> productos) {
		this.productos = productos;
	}
	public ReservaPaquete getReservaPaquete() {
		return reservaPaquete;
	}
	public void setReservaPaquete(ReservaPaquete reservaPaquete) {
		this.reservaPaquete = reservaPaquete;
	}
	public Integer getCantidadPasajeros() {
		return cantidadPasajeros;
	}
	public void setCantidadPasajeros(Integer cantidadPasajeros) {
		this.cantidadPasajeros = cantidadPasajeros;
	}
	public Double getImporteDescuentoCupon() {
		return importeDescuentoCupon;
	}
	public void setImporteDescuentoCupon(Double importeDescuentoCupon) {
		this.importeDescuentoCupon = importeDescuentoCupon;
	}
	
}
