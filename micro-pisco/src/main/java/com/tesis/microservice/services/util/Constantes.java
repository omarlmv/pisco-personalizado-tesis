package com.tesis.microservice.services.util;

public class Constantes {
	public static final String PAGINA_PERIODO_ANUAL = "beneficio/periodo_anual";
	public static final String PAGINA_SEGMENTO = "beneficio/segmento";
	public static final String SESION_CARRITO_COMPRAS = null;
	public static final Integer CANTIDAD_PRODUCTOS_POR_DEFECTO = 1;
	public static final String PAGINA_DESCUENTO = "descuento";
	
	public static final String PREFIJO_LISTA_SEGMENTO = "cc";
	
	
	public static final String SEPARADOR_ARRAY =",";
	public static final String SESSION_CLIENTE="SESSION_CLIENTE";
	public static final String SUBSIDIARIA_NETSUITE="18";
	public static final String LOG_METODO_PAQUETE_LISTAR = "listar paquete por categoria";
	public static final String LOG_METODO_PAQUETE_DISPONIBLES_LISTAR = "listar paquetes disponibles";
	public static final String LOG_METODO_PAQUETE_DETALLE_OBTENER = "Obtener detalle del paquete";
	public static final String LOG_METODO_PAQUETE_HORARIO_VUELO_LISTAR = "Listar horarios disponibles del paquete";
	public static final String LOG_METODO_PAQUETE_RESERVA_REGISTRAR = "Registrar Reserva de Paquete";
	public static final String LOG_METODO_PAQUETE_RESERVA_TRANSACCION_ACTUALIZAR = "Actualizar transaccion de reserva de paquete";
	public static final String LOG_METODO_VUELO_RESERVA_TRANSACCION_ACTUALIZAR = "Actualizar transaccion de reserva de vuelo";
	public static final String LOG_METODO_PAQUETE_RESERVA_CORREO_OBTENER = "Obtener correo de confirmacion de reserva de paquete";
	public static final String LOG_METODO_VUELO_RESERVA_CORREO_OBTENER = "Obtener correo de confirmacion de reserva de vuelo";
	public static final String LOG_METODO_PAQUETE_RESERVA_ESTADO_TRANSACCION_OBTENER = "Obtener estado de transaccion";
	
	public static final String OAUTH_API_URL_OBTENER_DATOS_CLIENTE = "config.oauth.api.url.obtenerDatosCliente";
	public static final String OAUTH_API_URL_OBTENER_TOKEN = "config.oauth.api.url.obtenerToken";
	public static final String OAUTH_API_URL_REGENERAR_TOKEN = "config.oauth.api.url.regenerarToken";
	public static final String OAUTH_CLIENT_ID = "config.oauth.clientId";
	public static final String OAUTH_CLIENT_SECRET = "config.oauth.clientSecret";
	
	//Tags para la auditoria de ventas
	public static final String TAG_VENTA_CREACION = "Creacion de Venta";
	public static final String TAG_VENTA_CONFIRMACION_RESERVA = "Confirmacion de Reserva";
	public static final String LOG_METODO_ESTADO_CUENTA_DESCUENTOS = "Obtener estado cuenta canjes de productos";
	public static final String LOG_METODO_ESTADO_CUENTA_PUNTOS = "Obtener estado cuenta historial puntos ";
	public static final String LOG_METODO_ESTADO_CUENTA_CANJES = "Obtener estado cuenta canjes de productos";
	public static final String LOG_METODO_ESTADO_CUENTA_REGISTRAR_PREMIO = "Estado cuenta regitrar premio";
	public static final String OAUTH_API_URL_LOGOUT = "config.oauth.api.url.logout";
	
	public static final String MSG_CANJE_DELIVERY_INVALIDO = "Error, los datos de direcci&oacute;n delivery no son  v&aacute;lidos";
	public static final String URL_IMAGEN_PRODUCTO = "url.imagen.producto";
	public static final String CONFIG_SIMBOLO_MONEDA_DEFAULT = "config.moneda.simbolo.default";
	public static final String CONFIG_URL_RECURSOS_BASE_WEB ="config.url.recursos.base.web";
	public static final String LABEL_MAIL_CANJE_DELIVERY_DIAS_ESTIMADO= "dentro de {0} d&iacute;a";
	public static final String TIPO_CAMBIO_FORMULA = "config.tipocambio.base";
	public static final String CONFIG_PUNTOS_PORCENTAJE_CONVERSION = "config.puntos.porcentaje.conversion";
	public static final String MSG_CONFIRMACION_PAGO_EXITO = "Se realiz&oacute; el canje exitosamente";
	public static final String MSG_CONFIRMACION_PAGO_ERROR = "Ocurri&oacute; un error al realizar el canje, por favor vuelve a intentarlo.";
	
	public static final String TAG_VENTA_CONFIRMAR_PAGO_IBK = "Confirmacion de Pago IBK";
	public static final String TAG_VENTA_ANULAR_PAGO_IBK = "Anular Pago IBK";
	public static final String TAG_VENTA_ANULAR_RESERVA_VUELO_INMEDIATO = "Anular Reserva vuelo inmediato";
	
	public static final String URL_PROMOCIONES_CATEGORIA = "promociones/categoria";
	public static final String URL_PROMOCION = "promocion";

}