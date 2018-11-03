package com.tesis.microservice.services.util;

public class WebConfigPropiedad {

	public enum URI {
		SERVICE_AMQ_REGISTRAR_NETSUITE_PEDIDO("uri.service.amq.registrarPedidoNetSuite"),
		SERVICE_COSTAMAR_PAQUETE_ACTUALIZAR_STOCK("uri.service.costamar.paquete.actualizar.stock"), 
		SERVICE_AMQ_ACTUALIZAR_NETSUITE_PEDIDO("uri.service.amq.actualizarPedidoNetSuite"),
		SERVICE_AMQ_SINCRONIZAR_STOCK_PRODUCTO("uri.service.amq.sincronizar.stock"),
		SERVICE_AMQ_CORREO_CONFIRMACION_RESERVA_PAQUETE("uri.service.correo.confirmacion.reserva.paquete"),		
		SERVICE_AMQ_MAIL_ENVIAR("uri.service.email.enviar"),
		SERVICE_AMQ_BUSCAR_VUELOS_ASINCRONA("uri.service.amq.buscar.vuelos.asincrona"),
		SERVICE_AMQ_RESERVA_VUELOS_ASINCRONA("uri.service.amq.reserva.vuelos.asincrona");
		
		private final String uri;

		public String getUri() {
			return uri;
		}

		URI(String valor) {
			this.uri = valor;
		}

	}

	public enum URL_BASE {
		SERVICIOS_AMQ("config.app.servicios.amq"),
		SERVICIOS_ADMIN("config.app.servicios.admin"),
		MICROSERVICE_CUPON("config.app.microservice.cupon");

		private final String url;

		URL_BASE(String url) {
			this.url = url;

		}

		public String getUrl() {
			return url;
		}
	}

}
