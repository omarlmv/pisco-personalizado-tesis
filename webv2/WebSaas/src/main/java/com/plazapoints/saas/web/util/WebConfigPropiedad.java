package com.plazapoints.saas.web.util;

public class WebConfigPropiedad {

	public enum URI {
		// SERVICE_EMPRESA_OBTENER_DATOS("uri.service.empresa.obtener.empresa"),
		SERVICE_DESCUENTO_OBTENER("uri.service.beneficio.descuento.obtener"),
		SERVICE_CLIENTE_TRACKING_ACTUALIZAR("uri.service.cliente.tracking.actualizar"),
		SERVICE_CLIENTE_TRACKING_OBTENER("uri.service.cliente.tracking.obtener"),
		
		SERVICE_PAQUETE_COSTAMAR_LISTAR_CATEGORIA("uri.service.costamar.categoria.listar.tipo"),
		SERVICE_PAQUETE_COSTAMAR_LISTAR_POR_CATEGORIA("uri.service.costamar.paquete.listar.categoria"),
		SERVICE_PAQUETE_COSTAMAR_LISTAR_DISPONIBLES("uri.service.costamar.paquete.disponibles.listar"),
		SERVICE_PAQUETE_COSTAMAR_DETALLE_WS("uri.service.costamar.paquete.detalle.ws"),
		SERVICE_PAQUETE_COSTAMAR_PAQUETE_HORARIO_VUELO_LISTAR("uri.service.costamar.paquete.horarioVuelo.listar"),
		//SERVICE_PAQUETE_COSTAMAR_BASEDATOS_OBTENER_DETALLE("uri.service.costamar.paquete.detalle.basedatos.obtener"),
		SERVICE_PAQUETE_COSTAMAR_DESTACADO_LISTAR("uri.service.costamar.paquete.destacado.listar"),
		SERVICE_PAQUETE_COSTAMAR_ORDEN_LISTAR("uri.service.costamar.paquete.orden.listar"),
		SERVICE_PAQUETE_COSTAMAR_LISTAR_TOTAL("uri.service.costamar.paquete.listar.total"),
		SERVICE_PAQUETE_COSTAMAR_VALIDAR_POR_SEGMENTO("uri.service.costamar.paquete.validar.por.segmento"),
		SERVICE_PAQUETE_COSTAMAR_RESERVA("uri.service.costamar.paquete.reserva"),
		SERVICE_PAQUETE_TRANSACCION_ACTUALIZAR("uri.service.costamar.paquete.transaccion.actualizar"),
		SERVICE_PAQUETE_CORREO_OBTENER("uri.service.costamar.paquete.correo.obtener"),
//		SERVICE_PAQUETE_ESTADO_TRANSACCION_OBTENER("uri.service.costamar.paquete.transaccion.obtener"),
		SERVICE_PAQUETE_COSTAMAR_LOG_SERVICIOS_REGISTRAR("uri.service.costamar.paquete.registrar.logServicios"),
		
		SERVICE_VUELOS_COSTAMAR_DISPONIBILIDAD("uri.service.costamar.vuelos.disponibilidad"),
		SERVICE_VUELOS_COSTAMAR_REGULACIONES("uri.service.costamar.vuelos.regulaciones"),
		SERVICE_VUELOS_COSTAMAR_RESERVA("uri.service.costamar.vuelos.reserva"),
		SERVICE_VUELOS_COSTAMAR_ACTUALIZAR_ESTADO("uri.service.costamar.actualizar.estado"),
		SERVICE_VUELOS_COSTAMAR_OBTENER_VENTA("uri.service.costamar.obtener.venta"),
		SERVICE_VUELOS_COSTAMAR_TRANSACCION_ACTUALIZAR("uri.service.costamar.transaccion.actualizar"),
		SERVICE_VUELOS_COSTAMAR_CORREO_OBTENER("uri.service.costamar.correo.obtener"),
		SERVICE_VUELOS_BUSQUEDA_ASYNC_RESULTADO("uri.service.vuelos.busqueda.async.resultado"),
				
		SERVICE_REGISTRAR_VENTA("uri.service.venta.registrar"), 
		SERVICE_CARRITO_COMPRAS_OBTENER("uri.service.compra.carrito.obtener"), 
		SERVICE_WISHLIST_GUARDAR("uri.service.catalogo.wishlist.guardar"), 
		SERVICE_WISHLIST_LISTAR("uri.service.catalogo.wishlist.listar"), 
		SERVICE_CATALOGO_PEDIDO_REGISTRAR("uri.service.catalogo.pedido.registrar"),
		SERVICE_CATALOGO_MARCAS_LISTAR("uri.service.catalogo.marcas.listar"),
		SERVICE_CATALOGO_PRODUCTO_LISTAR("uri.service.catalogo.producto.listar"),
		SERVICE_CATALOGO_PRODUCTO_TOTAL("uri.service.catalogo.producto.total"),
		SERVICE_CATALOGO_PRODUCTO_DETALLE("uri.service.catalogo.producto.detalle"),
		SERVICE_CATALOGO_PRODUCTO_DETALLE_POR_CODIGO_NETSUITE("uri.service.catalogo.producto.detalle.por.codigoNetsuite"),
		SERVICE_CARRITO_CATALOGO_PRODUCTO_OBTENER("uri.service.carrito.catalogo.producto.obtener"), 
		SERVICE_CARRITO_PRODUCTO_AGREGAR("uri.service.carrito.producto.agregar"),
		SERVICE_CARRITO_COMPRAS_CREAR("uri.service.carrito.compras.crear"),
		SERVICE_CARRITO_COMPRAS_DETALLE("uri.service.carrito.compras.detalle"), 
		SERVICE_CARRITO_COMPRAS_PRODUCTO_ACTUALIZAR("uri.service.carrito.compras.producto.actualizar"),
		SERVICE_CARRITO_COMPRAS_VER("uri.service.carrito.compras.ver"),
		SERVICE_DESCUENTO_CONSULTAR_CANJE("uri.service.descuento.consultar.canje"),
		SERVICE_VENTA_ACTUALIZAR_MONTOS_PAGADOS("uri.service.venta.actualizar.montosPagados"),
		SERVICE_DESCUENTO_VALIDAR_CANJE("uri.service.descuento.validar.canje"),
		SERVICE_LISTAR_DESCUENTOS("uri.service.descuento.listar"),
		SERVICE_CLIENTE_DIRECCION_DELIVERY("uri.service.cliente.direccion.delivery"),
		SERVICE_CLIENTE_ACTUALIZAR_DIRECCIONDELIVERY("uri.service.cliente.actualizarDireccionDelivery"),
		SERVICE_VENTA_OBTENER("uri.service.venta.obtener"),
		SERVICE_VENTA_ENVIAR_CORREO_CANCELACION_CANJE("uri.service.venta.enviar.correoCancelacionCanje"),
		SERVICE_CATALOGO_PRODUCTO_BY_ENTIDAD("uri.service.catalogo.productoByEntidad"),
		SERVICE_CATALOGO_REGISTRAR_NETSUITE_PEDIDO("uri.service.catalogo.registrarPedidoNetSuite"),
		SERVICE_ENVIAR_VENTA_NETSUITE("uri.controller.venta.enviar.netsuite"),
		SERVICE_ENVIAR_VENTA_CONFIRMAR_PAGO("uri.service.venta.confirmar.pago.ibk"),		
		SERVICE_AMQ_REGISTRAR_NETSUITE_PEDIDO("uri.service.amq.registrarPedidoNetSuite"),
		SERVICE_AMQ_SINCRONIZAR_STOCK_NETSUITE("uri.service.amq.producto.sincroniza.stock"),
		
		SERVICE_AMQ_CONSULTAR_NETSUITE_PEDIDO_VIA_WS("uri.service.amq.obtenerPedidoViaWS"),
		SERVICE_VENTA_SINCRONIZAR_PEDIDO_NETSUITE("uri.service.venta.sincronizar.pedido.netSuite"),
		SERVICE_AMQ_ACTUALIZAR_NETSUITE_PEDIDO("uri.service.amq.actualizarPedidoNetSuite"),
		SERVICE_PAQUETE_COSTAMAR_BASEDATOS_LISTAR_TODO("uri.service.costamar.paquete.basedatos.listar"),
		SERVICE_PAQUETE_COSTAMAR_DETALLE_DB("uri.service.costamar.paquete.detalle.db"),
		
		SERVICE_SEGMENTO_OBTENER_IDS("uri.service.segmento.obtener.ids"),
		SERVICE_SEGMENTO_OBTENER_BASE("uri.service.segmento.obtener.base"),
		SERVICE_BENEFICIO_OBTENER_GRUPO("uri.service.beneficio.obtener.grupo"),
		SERVICE_CATALOGO_PRODUCTO_DESTACADOS_LISTAR("uri.service.catalogo.producto.destacados.listar"),
		SERVICE_PERIODO_LISTAR_VIGENTES("uri.service.periodo.listar.vigentes"),
		
		
		SERVICE_UBIGEO_LISTAR_UBIGEO("uri.service.ubigeo.listarUbigeo"),
		SERVICE_VUELOS_COSTAMAR_DISPONIBLES("uri.service.costamar.vuelos.disponibilidad"),
		SERVICE_VUELOS_COSTAMAR_RESERVAR("uri.service.costamar.vuelos.reserva"), 
		SERVICE_VUELOS_COSTAMAR_AIRPORT_LISTAR("uri.service.costamar.vuelos.airport.listar"),
		
		SERVICE_VUELOS_RESERVA_INICIAR_ASYNC("uri.service.vuelos.reserva.iniciar.async"),
		SERVICE_VUELOS_RESERVAR_ASYNCRONA("uri.service.vuelos.reservar.asyncrona"),
		SERVICE_VUELOS_RESERVA_RESULTADO_ASYNC("uri.service.vuelos.reserva.resultado.async"),
		
		SERVICE_DESCUENTO_LISTAR_DESTACADOS("uri.service.beneficio.descuento.listar.destacados"),
		SERVICE_CATALOGO_PRODUCTO_COMPLEMENTOS("uri.service.catalogo.producto.complemento"),
		SERVICE_CLIENTE_OBTENER_SEGMENTOS("uri.service.cliente.obtenerSegmentos"),
		SERVICE_CAMPANIA_OBTENER_X_CLIENTE("uri.service.campania.obtenerxCliente"),
		SERVICE_CAMPANIA_GUARDAR_ELECCION_CLIENTE("uri.service.campania.guardarEleccion"),
		SERVICE_CAMPANIA_OBTENER_PROMOCION_CLIENTE("uri.service.campania.obtener.promociones.cliente"),
		SERVICE_AMQ_MAIL_ENVIAR("uri.service.email.enviar"),
		SERVICE_AMQ_CORREO_CONFIRMACION_RESERVA_PAQUETE("uri.service.correo.confirmacion.reserva.paquete"),
		SERVICE_AMQ_AUDITORIA_ENTIDAD_ACTUALIZAR("uri.service.amq.auditoriaEntidad.actualizar"),
		SERVICE_BIM_CLIENTE_CANJES_PRODUCTOS("uri.service.bim.cliente.canjes.productos"), 

		SERVICE_BIM_CLIENTE_CANJES_DESCUENTOS("uri.service.bim.cliente.canjes.descuentos"),
		SERVICE_BENEFICIO_DESCUENTO_PRINCIPAL_LISTAR("uri.service.beneficio.descuento.principal.listar"), 
		SERVICE_BENEFICIO_DESCUENTO_CATEGORIAS_LISTAR("uri.service.beneficio.descuento.categorias.listar"), 
		SERVICE_BENEFICIO_DESCUENTO_FILTRO_LISTAR("uri.service.beneficio.descuento.filtro.listar"), 
		SERVICE_BENEFICIO_DESCUENTO_FILTRO_CLAVE_LISTAR("uri.service.beneficio.descuento.filtro.clave.listar"), 
		SERVICE_BENEFICIO_DESCUENTO_FILTRO_COMBO_LISTAR("uri.service.beneficio.descuento.filtro.combo.listar"), 
		SERVICE_BENEFICIO_DESCUENTO_DESTACADOS_LISTAR("uri.service.beneficio.descuento.destacados.listar"), 
		SERVICE_BENEFICIO_DESCUENTO_DETALLE("uri.service.beneficio.descuento.detalle"), 
		SERVICE_LISTAR_VENTAS_PENDIENTES_NETSUITE("uri.service.get.ventasPendientes"),
		SERVICE_BENEFICIO_DESCUENTO_CANJEAR("uri.service.beneficio.descuento.canjear"),
		SERVICE_BIM_CLIENTE_SALDO_PUNTOS("uri.service.bim.cliente.saldoPuntos"), 
		SERVICE_VENTA_CANJES_LISTAR_BY_CLIENTE("uri.service.venta.canjes.listarXCliente"), 
		SERVICE_CATEGORIA_PARTIPO_LISTAR("uri.service.categoria.parTipo.listar"),
		SERVICE_CATEGORIA_LISTAR_X_TIPO("uri.service.categoria.listarXTipo"),
		SERVICE_CATEGORIA_LISTAR_TOTAL_ITEMS("uri.service.categoria.listarConTotalItems"),
		SERVICE_CATEGORIA_LISTAR_TOTAL_ITEMS_BY_SEGMENTOS("uri.service.categoria.listarConTotalItemsBySegmentos"),		
		SERVICE_SUBCATEGORIA_LISTAR_TOTAL_ITEMS("uri.service.subcategoria.listarConTotalItems"),
		SERVICE_SUBCATEGORIA_LISTAR_SUBCATE_BY_CATEGORIA("uri.service.subcategoria.listarSubCateByCategoria"),
		//SERVICE_CATEGORIA_CANTIDAD_PARTIPO_LISTAR("uri.service.categoria.parTipoCantidad.listar"),
		SERVICE_ZONA_DELIVERY("uri.service.common.zonas.listar"),
		SERVICE_ZONA_DELIVERYBYUBIGEO("uri.service.common.zonas.listarByUbigeo"),
		SERVICE_VENTA_PEDIDO_LISTAR_ESTADOS("uri.service.venta.pedido.listarEstados"), 
		SERVICE_VENTA_PEDIDO_OBTENER_OBSERVACION("uri.service.venta.pedido.obtenerObservacion"), 
		SERVICE_ACCESO_USUARIO_OBTENER_LOGIN("uri.service.acceso.usuario.obtenerLogin"), 
		SERVICE_ACCESO_IBK_OBTENER_TOKEN("uri.service.acceso.ibk.obtenerToken"), 
		SERVICE_ACCESO_IBK_OBTENER_DATOS_CLIENTE("uri.service.acceso.ibk.obtenerDatosCliente"),
		SERVICE_ACCESO_IBK_REGENERAR_TOKEN("uri.service.acceso.ibk.regenerarToken"),
		SERVICE_VUELOS_COSTAMAR_AEREOLINEAS_LISTAR("uri.service.costamar.vuelos.aereolineas.listar"),
		SERVICE_LISTAR_PRODUCTOS_ACTIVOS("uri.service.amq.producto.listar.activos"),
		SERVICE_LISTAR_PRODUCTOS_ACTIVOS_CON_STOCK_CERO("uri.service.amq.producto.activos.stock.cero"), 
		SERVICE_AMQ_SINCRONIZAR_STOCK_PRODUCTO("uri.service.amq.sincronizar.stock"), 
		SERVICE_ACCESO_CLIENTE_ACTUALIZAR_LOGIN("uri.service.acceso.cliente.actualizarLogin"), 
		SERVICE_BIM_PASARELA_PROCESAR("uri.service.bim.pasarela.procesar"), 
		SERVICE_BIM_PASARELA_OBTENER_RESUMEN("uri.service.bim.pasarela.obtenerResumen"), 
		SERVICE_VENTA_CONFIRMAR_PAGO("uri.service.venta.confirmar.pago"),
		SERVICE_VENTA_CANCELAR_PAGO("uri.service.venta.cancelar.pago"),
		SERVICE_DELIVERY_OBTENER_FECHA_ENTREGA("uri.service.common.delivery.obtenerFechaEntrega"), 
		SERVICE_BENEFICIO_DESCUENTO_TODOS_DESTACADOS_LISTAR("uri.service.beneficio.descuento.todos.destacados.listar"), 
		SERVICE_BENEFICIO_DESCUENTO_FILTRO_DEPARTAMENTO_LISTAR("uri.service.beneficio.descuento.filtro.departamento.listar"),
		//SERVICE_OBTENER_PRODUCTOS_ENTRETENIMIENTO("uri.service.producto.obtener.entretenimiento"),
		SERVICE_OBTENER_DESCUENTOS_ENTRETENIMIENTO("uri.service.descuento.obtener.entretenimiento"),  
		SERVICE_BENEFICIO_DESCUENTO_POR_CATEGORIA_LISTAR("uri.service.beneficio.descuento.categoria.listar"), 
		SERVICE_BENEFICIO_DESCUENTO_MOSTRAR("uri.service.beneficio.descuento.categoria.listar"), 
		SERVICE_BENEFICIO_DESCUENTO_MOSTRAR_FILTRO("uri.service.beneficio.descuento.mostrar.filtro"), 
		SERVICE_DESCUENTO_LISTAR("uri.service.beneficio.descuento.listar"), 
		SERVICE_DESCUENTO_TOTAL_REGISTRO("uri.service.beneficio.descuento.totalRegistro"), 
		SERVICE_CATALOGO_PRODUCTO_TOTAL_REGISTROS("uri.service.catalogo.producto.totalRegistro"), 
		SERVICE_DESCUENTO_BY_DEPARTAMENTO_LISTAR("uri.service.beneficio.descuento.departamento.listar"), 
		SERVICE_PAQUETE_OBTENER_CANTIDAD_REGISTROS("uri.service.paquete.obtener.cantidad.registros"), 
		SERVICE_VENTA_ACTUALIZAR_CODIGO_TX_IBK("uri.service.venta.actualizar.codigoTxIBK"),
		SERVICE_VENTA_DIRECCION_DELIVERY_OBTENER("uri.service.venta.direccion.delivery.obtener"),
		SERVICE_DIRECCION_DELIVERY_LISTAR_POR_CLIENTE("uri.service.direccion.delivery.listar.por.cliente"),
		SERVICE_DIRECCION_DELIVERY_ELIMINAR_POR_NOMBRE("uri.service.direccion.delivery.eliminar.por.nombre"),
		SERVICE_CATALOGO_PRODUCTO_LISTAR_SIMILAR_NETSUITE("uri.service.catalogo.producto.listar.similarCodigoNetsuite"), 
		SERVICE_CATALOGO_PRODUCTO_VALIDAR_PERTENECE_A_BENEFICIO("uri.service.catalogo.producto.perteneceABeneficio"), 
		SERVICE_DESCUENTO_LISTAR_POR_BENEFICIOS("uri.service.beneficio.descuento.beneficios.listar"), 
		
		SERVICE_WEB_CREAR_CARRITO("uri.service.web.crear.carrito"),
		SERVICE_WEB_OBTENER_CARRITO("uri.service.web.obtener.carrito"),
		SERVICE_WEB_CREAR_CARRITO_ULTIMO("uri.service.web.crear.carrito.ultimo"),
		
		SERVICE_WEB_AGREGAR_ITEM_CARRITO("uri.service.web.agregar.item.carrito"),
		SERVICE_WEB_AGREGAR_ITEM_PRECIO_DELIVERY_CARRITO("uri.service.web.agregar.item.precio.delivery.carrito"),
		SERVICE_WEB_ACTUALIZAR_ITEM_CARRITO("uri.service.web.actualizar.item.carrito"),
		SERVICE_WEB_ELIMINAR_ITEM_CARRITO("uri.service.web.eliminar.item.carrito"),
		SERVICE_WEB_OBTENER_ITEM_CARRITO("uri.service.web.obtener.item.carrito"),
		SERVICE_WEB_ACTUALIZAR_ESTADO_ITEM_CARRITO("uri.service.web.actualizar.estado.item.carrito"),
		SERVICE_COMPRA_LINEA_RESERVAR_STOCK("uri.service.compra.reservar.stock"),
		SERVICE_COMPRA_ACTUALIZAR_ESTADO("uri.service.compra.actualizar.estado"),
		SERVICE_COMPRA_LINEA_LIBERAR_STOCK("uri.service.compra.liberar.stock"),
		SERVICE_CATALOGO_LIBERAR_STOCK_BY_VENTA("uri.service.catalogo.liberar.stock.byVenta"),
		
		SERVICE_BIM_CLIENTE_ESTADO_CUENTA_PUNTOS("uri.service.bim.cliente.estadoCuenta.puntos"), 
		SERVICE_CLIENTE_ACTUALIZAR_DATOS("uri.service.cliente.actualizar.datos"),
		SERVICE_VENTA_CANCELAR_RESERVAS_BY_CLIENTE("uri.service.venta.cancelar.reserva.cliente"),
		
		SERVICE_WEB_LISTAR_CONFIGURACION_FILTRO("uri.service.web.listar.configuracion.filtro"), 
		SERVICE_ACCESO_IBK_CLOSE_SESSION("uri.service.acceso.ibk.close.session"), 
		SERVICE_CATALOGO_PRODUCTO_LISTAR_BY_ID("uri.service.catalogo.producto.listar.by.id"),
		
		SERVICE_INTEGRACION_ADOBE_DATA("uri.service.integracion.adobe.data"),
		
		SERVICE_EVENTO_WEB_LISTAR("uri.service.evento.web.listar"),
		SERVICE_VIAJE_RANKING_LISTAR("uri.service.viaje.ranking.listar"), 
		SERVICE_VUELOS_COSTAMAR_BUSQUEDA_ASINCRONA("uri.service.costamar.vuelo.busqueda.asincrona"),
		SERVICE_COMMON_BANNER_LISTAR("uri.service.common.banner.listar"),
		SERVICE_DESCUENTO_LOCALES_LISTAR("uri.service.web.descuento.locales.listar"),
		
		SERVICE_OBTENER_VUELO_ASINCRONO("uri.service.obtener.vuelo.asincrono"),
		SERVICE_TERMINO_CONDICION_LISTAR("uri.service.termino.condicion.listar"),
		
		SERVICE_CANJE_LOCAL_LISTAR("uri.service.canje.local.listar"),
		SERVICE_CANJE_LOCAL_DETALLE("uri.service.canje.local.detalle"),
		SERVICE_CANJE_LOCAL_LISTAR_LOCALES("uri.service.canje.local.listar.locales"),
		SERVICE_CANJE_LOCAL_OBTENER_ARRAY_LOCALES("uri.service.canje.local.obtener.array.locales"),
		SERVICE_CANJE_LOCAL_DESTACADOS_LISTAR("uri.service.canje.local.destacados.listar"),
		SERVICE_CATALOGO_PRODUCTO_DESTACADOS_FILTRO("uri.service.catalogo.producto.destacados.filtro"),
		SERVICE_CANJE_LOCAL_LISTAR_LOCALES_RANGO("uri.service.canje.local.listar.locales.rango"), 
		SERVICE_COMMON_AGRUPADOR_CAT_TOTAL_ITEMS("uri.common.agrupador.categoria.totalItems"),
		SERVICE_AGRUPADOR_AUTOMATICO_CAT_TOTAL_ITEMS("uri.common.agrupador.automatico.categoria.totalItems"),

		SERVICE_PROMOCIONES_OBTENER_HOME("uri.service.promociones.obtener.home"),
		SERVICE_PROMOCIONES_OBTENER_CLIENTE("uri.service.promociones.obtener.cliente"),
		SERVICE_PROMOCIONES_DETALLE("uri.service.promociones.detalle"),
		SERVICE_PROMOCIONES_FILTRO("uri.service.promociones.filtro"),
		SERVICE_PROMOCIONES_RUBROS("uri.service.promociones.rubros"),
		SERVICE_PROMOCIONES_RETOS("uri.service.promociones.retos"),
		SERVICE_PROMOCIONES_METAS("uri.service.promociones.metas"),
		SERVICE_CANJE_LOCAL_LISTAR_SIN_FILTRO("uri.service.canje.local.listar.sin.filtro"),
		SERVICE_CANJE_LOCAL_DESTACADOS_COMPLEMENTO_LISTAR("uri.service.canje.local.destacados.complementos.listar"),		
		SERVICE_CATALOGO_PRODUCTO_GRUPO_CARACTERISTICA_LISTAR("uri.service.catalogo.producto.grupo.caracteristica.listar"),
		SERVICE_VENTA_EVALES_ENVIO_LISTAR("uri.service.venta.evales.envio.listar"), 
		SERVICE_AMQ_SINCRONIZAR_STOCK_EVALES("uri.service.amq.sincronizar.stock.evales"),
		
		SERVICE_CATALOGO_PRODUCTO_LISTA_DESTACADOS("uri.service.catalogo.producto.lista.destacados"),
		SERVICE_CATALOGO_PRODUCTO_TOP_LISTA_DESTACADOS("uri.service.catalogo.producto.top.lista.destacados"),
		SERVICE_CATALOGO_PRODUCTO_TOP_LISTAR("uri.service.catalogo.producto.top.listar"), 
		SERVICE_VENTA_VALIDACION_CANJE_X_DIA("uri.service.venta.validacion.canje.xdia"),
		SERVICE_COMENTARIO_PRODUCTO_PROMEDIAR("uri.service.comentario.producto.promediar"),
		SERVICE_COMENTARIO_PRODUCTO_LISTAR("uri.service.comentario.producto.listar"),
		SERVICE_COMENTARIO_PRODUCTO_REGISTRAR("uri.service.comentario.producto.registrar"),
		
		SERVICE_CATALOGO_PRODUCTO_CONTAR("uri.service.catalogo.producto.contar"),
		SERVICE_CARRITO_PENDIENTES_CANJE("uri.service.carrito.pendientes.canje"),
		
		SERVICE_MILLAS_VARIABLE_LISTAR("uri.service.millas.variable.listar"),
		SERVICE_VENTA_MILLAS_VARIABLE_REGISTRAR("uri.service.venta.millas.variable.registrar"), 
		SERVICE_COMMON_DELIVERY_COSTOS("uri.service.common.delivery.costos"), 
		SERVICE_COMMON_DELIVERY_COSTO_PESO("uri.service.common.delivery.costoXPeso"),
		SERVICE_UBIGEO_LISTAR_X_DELIVERY("uri.service.common.ubigeo.listarXDelivery"),
		SERVICE_AMQ_CONSULTAR_ESTADO_PAGO_IBK("uri.service.amq.consultarEstadoPagoIbk"),
		SERVICE_VENTA_LISTAR_FERIADOS("uri.service.venta.feriados.listar"),
		SERVICE_DELIVERY_VALIDAR_FERIADO("uri.service.common.delivery.validarFeriado");
		private final String uriService;
		URI(String valor) {
			this.uriService = valor;
		}
		public String getUri() {
			return uriService;
		}

		
	}

	public enum URL_BASE {
		SERVICIOS_WEB("config.app.servicios.web"),
//		SERVICIOS_ADMIN("config.app.servicios.admin"),
		SERVICIOS_AMQ("config.app.servicios.amq"), 
		MICROSERVICE("config.app.microservice"),
		MICROSERVICE_CUPON("config.app.microservice.cupon"),
		MICROSERVICE_EVALES("config.app.microservice.evales"),
		MICROSERVICE_PRODUCTO("config.app.microservice.producto"),
		MICROSERVICE_JOINNUS("config.app.microservice.joinnus");
		
		private final String url;

		URL_BASE(String url) {
			this.url = url;

		}

		public String getUrl() {
			return url;
		}
	}
	
	
	public enum URI_MICROSERVICE {
		CUPON("config.app.microservice.cupon.initial"),
		CUPON_LISTAR("uri.microservice.cupon.listar"),
		CUPON_USAR("uri.microservice.cupon.usar"),
		EVALES_DETALLE_ITEM("uri.microservice.evales.detalle.item"), 
		EVALES_DETALLE_MARCA("uri.microservice.evales.detalle.marca"),
		EVALES_DETALLE_IDEVALES("uri.microservice.evales.detalle.idEvales"),
		EVALES_FILTRO_X_IDS("uri.microservice.evales.xids"),
		EVALES_HOME_LISTAR("uri.microservice.evales.home.listar"),
		EVALES_SLIDER_HOME_LISTAR("uri.microservice.evales.slider.home.listar"),
		PRODUCTO_DEPARTAMENTOS("uri.microservice.producto.departamento.listar"),
		PRODUCTO_AGRUPADORES("uri.microservice.producto.agrupadores.listar"),
		PRODUCTO_DESTACADOS_POR_PRODUCTO("uri.microservice.producto.destacado.producto"),	
		PRODUCTO_DESTACADOS_POR_CATEGORIA("uri.microservice.producto.destacado.categoria"),				
		EVENTO_LISTAR_TIPO("uri.microservice.evento.listar.tipoevento"),
		CATEGORIAS_LISTADO_TODOS("uri.microservice.producto.categoria.listarTodos"),
		PRODUCTOS_MALLA("uri.microservice.producto.listarMalla"), 
		PRODUCTOS_MALLA_CONTAR("uri.microservice.producto.contarMalla"), 
		PRODUCTOS_CONTADOR_POR_CATEGORIAS("uri.microservice.producto.contadorXCategoria"),
		PRODUCTOS_CONTADOR_POR_RANGO_MILLAS("uri.microservice.producto.contadorXrangoMillas"),
		PRODUCTO_DESTACADOS_LISTAR_HOME("uri.microservice.producto.destacados.listar"),
		
		CATEGORIA_LISTAR_NODOS("uri.microservice.categoria.listar.nodos"),
		PRODUCTO_LISTADO_BUSQUEDA("uri.microservice.producto.listado.busqueda"),
		
		PRODUCTO_CATEGORIA_REDIRECT("uri.microservice.producto.redirect"),
		JOINNUS_EVENTOS_CATEGORIAS_LISTAR("uri.microservice.joinnus.evento.categoria.listar"),
//		JOINNUS_EVENTOS_CATEGORIAS_CANTIDAD("uri.microservice.joinnus.evento.categoria.cantidad"),		
		JOINNUS_EVENTOS_HOME_CATEGORIA_LISTAR("uri.microservice.joinnus.evento.categorias.home.listar"),
		JOINNUS_EVENTOS_FICHA("uri.microservice.joinnus.evento.ficha"),
		JOINNUS_EVENTOS_FICHA_X_ID_EVENTO("uri.microservice.joinnus.evento.fichaxIdEvento"),
		JOINNUS_EVENTO_BANNER("uri.microservice.joinnus.evento.banner"),
		JOINNUS_GENERAR_RESERVA_EVENTO_JOINNUS("uri.microservice.joinnus.evento.generarTicketReservaJoinnus"),
		JOINNUS_CONSULTA_EVENTOS_CANJEADOS("uri.microservice.joinnus.consultarEventosCanjeados"),
		JOINNUS_CONSULTA_DETALLE_EVENTOS_CANJEADOS("uri.microservice.joinnus.consultarEventosDetalleCanjeado"),
		JOINNUS_REENVIO_ENTRADA_EVENTOS_CANJEADOS("uri.microservice.joinnus.reenvioEntradasCanjeadas"),
		
		SERVICE_VALE_CATEGORIA_POR_PRODUCTO("uri.microservice.producto.categoria.vale.marca"),	
		SERVICE_VALE_POR_PRODUCTO("uri.microservice.producto.vale.catalogoProducto"),
		JOINNUS_ANULAR_RESERVA("uri.microservice.joinnus.anularReserva")
		
		;

		
		private final String url;
		
		URI_MICROSERVICE(String url) {
			this.url = url;
			
		}
		
		public String getUrl() {
			return url;
		}
	}


}
 