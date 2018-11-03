package com.tesis.microservice.services.util;

/**
 * Proyecto: SaasppCommon
 * 
 * @date : 15/04/2015
 * @time : 11:45:39
 * @author : Erick vb.
 */
public class UConstantes {
	public static final String FORMATO_DATE_NOMBRE_MES = "dd 'de' MMMM";
	
	public static final String FORMATO_DATE_NORMAL = "dd/MM/yyyy";
	public static final String FORMATO_DATE_NORMAL_HORA = "dd/MM/yyyy HH:mm:ss";
	public static final String FORMATO_DATE_NORMAL_HORA_AM_PM = "dd/MM/yyyy hh:mm:ss a";
	public static final String FORMATO_DATE_HH_MIN_SS = "HH:mm:ss";
	public static final String FORMATE_DATE_HH_MIN = "HH:mm";
	public static final String FORMATO_DATE_HH_MIN_SS_A = "hh:mm:ss a";
	
	public static final String FORMATO_DATE_YYYY_MM_DD_HMS = "yyyy-MM-dd hh:mm:ss a";
	public static final String FORMATO_DATE_YYYY_MM_DD_HMS_24 = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMATO_DATE_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String FORMATO_DATE_DD_MM_YYYY = "dd-MM-yyyy";
	public static final String FORMATO_DATE_DD_MM_YYYY_HMS = "dd-MM-yyyy hh:mm:ss";
	public static final String FORMATO_DATE_MM_YYYY = "MM-yyyy";
	public static final String FORMATO_DATE_YYYY_MM = "yyyy-MM";
	
	
	public static final String LOCALE_PE = "es_PE";
	public static final String TIME_ZONE = "GMT";
	
	public static final String  ELEMENT_RESULT="result";
	public static final String MAXIMO_DIAS_DELIVERY = "config.maximo.dias.delivery";
	public static final int MAXIMO_DIAS_DELIVERY_DEFAULT = 3;
	
	public static final int VENTA_ESTADO_NO_ENCONTRADO=-1;
	public static final int VENTA_ESTADO_ELIMINADO=0;
	public static final int VENTA_ESTADO_PENDIENTE=1;
	public static final int VENTA_ESTADO_RECHAZADO=2;
	public static final int VENTA_ESTADO_CONFIRMADO=3;
	
	
	public static final String FORMATO_MONEDA = ".,###,##0.00";
	public static final String FORMATO_PUNTOS = ".,###,###.##"; 
	public static final String SEPARADOR_COMA =",";
	public static final String SEPARADOR_PUNTO = ".";
	public static final String SEPARADOR_ESPACIO = " ";
	public static final String SEPARADOR_GUION_MEDIO = "-";
	public static final String SEPARADOR_PALOTE = "|";
	public static final String SEPARADOR_DOBLE_PIPE = "||";	
	public static final String SEPARADOR_ABRE_PARENTESIS = "(";
	public static final String SEPARADOR_CIERRA_PARENTESIS = ")";
	public static final String SEPARADOR_SIGNO_MAS = "+";
	public static final String DESPACHO_EN = "Despacho en ";
	public static final String DIAS_HABILES = "días hábiles";
	public static final String LO_RECIBIRA = "Lo recibirá";
	public static final String SEPARADOR_DOS_PUNTOS =":";
	
	public static final String CONFIG_BIM_ADMIN_EMAIL = "config.bim.admin.email";
	public static final String CONFIG_BIM_TELF_LIMA = "config.bim.telefono.lima";
	public static final String CONFIG_BIM_TELF_PROV = "config.bim.telefono.provincia";
	public static final String CONFIG_BIM_ADMIN_NOMBRE = "config.bim.admin.nombre";
	public static final String CONFIG_URL_RECURSOS_BASE_WEB ="config.url.recursos.base.web";
	public static final String CONFIG_BIM_ROL_COMERCIAL_BANCO ="config.bim.rol.comercial.banco";
	public static final String CONFIG_BIM_ROL_COMERCIAL_PP ="config.bim.rol.comercial.pp";
	public static final String CONFIG_BIM_EMAIL_CONSULTA_RECLAMO = "config.bim.email.consulta.reclamo";
	public static final String CONFIG_DOMAIN_ADMIN = "config.domain.admin";
	public static final String CONFIG_DOMAIN_WEB = "config.domain.web";
	public static final String CONFIG_PUNTOS_PORCENTAJE_CONVERSION = "config.puntos.porcentaje.conversion";
	public static final String CONFIG_TIPO_CAMBIO_FORMULA = "config.tipocambio.base";
	public static final String PRECIO_FLETE = "config.precio.flete.base";
	public static final String CONFIG_COMISION_PORCENTAJE_USO_PUNTOS = "config.comision.porcentaje.usoPuntos";
	
	
	public static final String CONFIG_RUTA_RECURSOS = "config.app.path.resources";
	public static final String CONTEXT_PATH_WEB = "config.app.web.contextPath";
	public static final String CONTEXT_PATH_HALCON = "config.halcon.recursos.base.web";
	
	public static final Integer EXCEPTION_SERVICE_IBK_GENERIC =1;
	public static final Integer EXCEPTION_SERVICE_IBK_URL =2;
	public static final Integer EXCEPTION_SERVICE_IBK_BUS =3;
	public static final int MONEDA_CANTIDAD_DECIMAL = 2;
	public static final int CANTIDAD_DECIMAL_CUATRO = 4;
	public static final String CONFIG_AMBIENTE_DEPLOY ="config.ambiente.deploy";
	public static final String CONFIG_AMBIENTE_PROXY_IP_REVERSE= "config.ambiente.proxyip.remote";
	public static final String SEPARADOR_SLASH = "/";
	public static final String SEPARADOR_SALTO_LINEA = "\n";
	public static final String SEPARADOR_SALTO_LINEA_HTML = "</br>";
	
	public static final String CONFIG_URL_OAUTH_ESTADO_CUENTA_PUNTOS ="config.oauth.api.url.estadoCuentaPuntos";
	public static final String CONFIG_URL_OAUTH_SALDO_PUNTOS ="config.oauth.api.url.saldoPuntos";
	public static final String CONFIG_URL_OAUTH_LISTA_BENEFICIOS ="config.oauth.api.url.listaBeneficios";
	public static final String CONFIG_URL_OAUTH_HISTORICO_CANJES ="config.oauth.api.url.historicoCanjes";
	public static final String CONFIG_URL_OAUTH_REGISTRO_PREMIO ="config.oauth.api.url.registroPremio";
	
	public static final String PAGO_IBK_ESTADO_OK = "OK";
	public static final String PAGO_IBK_ESTADO_NOK = "NOK";
	public static final String COSTAMAR_CODIGO_RESERVA_ACTUALIZAR_OK = "OK";
	public static final String COSTAMAR_CODIGO_RESERVA_ACTUALIZAR_NO_OK = "NO_OK";
	
	public static final boolean BOTON_DESACTIVADO_LIQUIDACIONES = false;
	public static final boolean BOTON_ACTIVADO_LIQUIDACIONES = true;
	
	public static final int LISTA_LIQUIDACIONES_PENDIENTES = 0;
	public static final int LISTA_LIQUIDACIONES_POR_PROCESAR = 1;
	
	public static final int LIQUIDACION_WEB_Y_TELEFONO_ITEMS = 1;
	public static final int LIQUIDACION_TELEFONO_CONSOLIDADO = 2;
	
	public static final String NOMBRE_ITEM_DELIVERY = "Delivery";
	public static final String CARPETA_IMAGEN_WEB = "static/images/";
	public static final String CONFIG_EMAIL_SOPORTE_SISTEMAS = "config.email.soporte.sistemas";
	
	public static final Integer IMAGEN_OFERTA_CONSTANTE = 500;
	public static final Integer CATALOGO_STATUS_APROBADO = 1;
	public static final String CONFIG_URL_RECURSOS_BASE_ADMIN = "config.url.recursos.base.admin";
	public static final String CONFIG_URL_RECURSOS_BASE_ADMIN_LOCAL = "config.url.recursos.base.admin.pc";
	public static final CharSequence SEPARADOR_GUION_BAJO = "_";
	
	public static final String SSL_TRUST_STORE_LOCATION="config.ssl.truststore.location";
	public static final String SSL_TRUST_STORE_PASSWORD="config.ssl.truststore.password";
	public static final Integer CODIGO_ITEM_DELIVERY = -100;
	public static final String CONFIG_SERVER_CACHE_ENDPOINTS = "config.server.cache.endpoints";
	public static final String CONFIG_SERVER_CACHE_PORTS = "config.server.cache.ports";


	/*Properties Halcon*/
	public static final String CONFIG_HALCON_SECURITY_VECTOR = "config.halcon.security.vector";
	public static final String CONFIG_HALCON_SECURITY_LLAVE = "config.halcon.security.llave";
	public static final String CONFIG_HALCON_SECURITY_BASIC_AUTH_USUARIO = "config.halcon.security.basic.auth.usuario";
	public static final String CONFIG_HALCON_SECURITY_BASIC_AUTH_PASSWORD = "config.halcon.security.basic.auth.password";
	public static final String CONFIG_HALCON_API_URL_HOME = "config.halcon.api.url.home";
	public static final String CONFIG_HALCON_API_URL_OBTENER_DETALLE = "config.halcon.api.url.obtener.detalle";
	public static final String CONFIG_HALCON_API_URL_OBTENER_FILTROS = "config.halcon.api.url.obtener.filtro";

	public static final String CONFIG_HALCON_API_URL_USUARIO = "config.halcon.api.url.usuario";
	public static final String CONFIG_HALCON_API_URL_USUARIO_RUBRO = "config.halcon.api.url.usuario.rubro";
	public static final String CONFIG_HALCON_API_URL_USUARIO_RETOS = "config.halcon.api.url.usuario.retos";
	public static final String CONFIG_HALCON_API_URL_USUARIO_METAS = "config.halcon.api.url.usuario.metas";
	
	public static final String URL_IMAGEN_EVALES = "url.imagen.evales";
	public static final String URL_AWS_CACHE_LIBERAR = "url.aws.cache.liberar";
	
	//BUSQUEDA VUELOS
	public static final String COSTAMAR_CODIGO_LATAM = "LA";
	public static final String COSTAMAR_AEREOPUERTO_PERU = "PE";
	public static final String COSTAMAR_CLASE_TODOS = "A";
	
	//REVAMPING
	public static final Integer MAXIMO_CARACTERES_DESCRIPCION=2500;
	public static final String RESPUESTA_POSITIVA = "SI";
	public static final String RESPUESTA_NEGATIVA = "NO";
	private UConstantes(){
		
	}
	
}

