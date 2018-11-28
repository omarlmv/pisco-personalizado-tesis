package com.tesis.microservice.pisco.util;

import java.util.Date;

public class UtilEnum2 {
	private UtilEnum2(){
		
	}

	public  enum ESTADO_OPERACION{
		EXITO(0,"EXITO"),
		ERROR(1,"ERROR"),
		TRANSACCION_SIN_REGISTROS(20,"TRANSACCION SIN REGISTROS"),
		EXCEPTION(10,"EXCEPTION"),
		TRANSACCION_NO_PROCESADO(50,"TRANSACCION NO PROCESADO"),
		TRANSACCION_YA_EXISTE(51,"TRANSACCION YA EXISTE"),
		TRANSACCION_CON_DEPENDENCIA(52,"TRANSACCION CON DEPENDENCIA EN OTRAS ENTIDADES"),
		TRANSACCION_NO_AUTORIZADO(55,"TRANSACCION NO AUTORIZADO"),
		TRANSACCION_INCOMPLETO(56,"TRANSACCION INCOMPLETO"),
		TRANSACCION_NO_PENDIENTE(57,"TRANSACCION NO PENDIENTE"),
		TRANSACCION_PENDIENTE(58,"TRANSACCION PENDIENTE"),
		CODIGO_OPERACION_NO_ACTUALIZADO(60,"CODIGO OPERACION NO ACTUALIZADO"),
		RESERVA_VUELO_NO_REALIZADA(70,"RESERVA VUELO NO REALIZADA"),
		RESERVA_PAQUETE_NO_REALIZADA(90,"RESERVA PAQUETE NO REALIZADA"),//paquete_no_realizada
		REGISTRO_VENTA_NO_REALIZADO(80,"REGISTRO VENTA NO REALIZADO"),
		DESCUENTO_NO_APLICA(91,"DESCUENTO NO APLICA"),
		COMERCIO_NO_ASIGNADO_A_POS(101,"COMERCIO NO ASIGNADO A POS"), 
		SEGMENTO_NO_ASIGNADO(121,"SEGMENTO NO ASIGNADO"),
		PERIODO_ANUAL_YA_EXISTE(131,"PERIODO ANUAL YA EXISTE"),
		USUARIO_NO_AUTORIZADO(201,"USUARIO NO AUTORIZADO"),
		USUARIO_SIN_LOGIN(203,"USUARIO SIN LOGIN"),
		USUARIO_TOKEN_NEW_EXPIRADO(204,"USUARIO SIN LOGIN"),
		USUARIO_TOKEN_EXPIRADO(205,"USUARIO SIN LOGIN"),
		PUNTOS_INSUFICIENTES(211,"PUNTOS INSUFICIENTES"),
		STOCK_INSUFICIENTE(221,"STOCK INSUFICIENTE"),
		ERROR_DATOS_FORMULARIO(499,"ERROR DATOS FORMULARIO"),
		ERROR_REGISTRO_CON_HIJO(500,"ERROR REGISTRO CON HIJO");

		private final int codigo;
		private final String texto;
		private ESTADO_OPERACION(int estado,String texto){
			this.codigo = estado;
			this.texto = texto;
		}
		public int getCodigo() {
			return codigo;
		}
		
		public String getTexto() {
			return texto;
		}
	}

	public  enum ESTADO_REGISTRO_BASE{
		//transicion eliminado 
		ELIMINADO(0,"Eliminado","msg.estado.eliminado"),

		DESACTIVADO(1,"DESACTIVADO","msg.estado.desactivado"),

		//transicion rechazado o anulado
		RECHAZADO(2,"RECHAZADO","msg.estado.rechazado"),
		CADUCADO(3,"CADUCADO","msg.estado.caducado"),
		RECHAZADO_RESERVA_COSTAMAR(5,"DENEGADO RECHAZADO RESERVA","msg.estado.rechazado"),
		BLOQUEADO(6,"BLOQUEADO","msg.estado.bloqueado"),

		//transicion pendiente en confirmacion 
		PENDIENTE(51,"PENDIENTE","msg.estado.pendiente"),
		PENDIENTE_ABIERTO(52,"ABIERTO","msg.estado.abierto"),
		PENDIENTE_RESERVA_COSTAMAR(55,"PENDIENTE RESERVA","msg.estado.pendiente"),

		//transicion confirmado cerrado
		CONFIRMADO(101,"CONFIRMADO","msg.estado.confirmado"),
		CONFIRMADO_CERRADO(102,"CERRADO","msg.estado.cerrado"),
		CONFIRMADO_ACCESO(103,"CONFIRMADO ACCESO","msg.estado.confirmado.acceso"),
		CONFIRMADO_RESERVA_COSTAMAR(105,"CONFIRMADO RESERVA","msg.estado.confirmado"),

		//transicion activo GO!!!!! 
		ACTIVO(201,"ACTIVO","msg.estado.activo"),
		ACTIVO_APROBADO(202,"","msg.estado.aprobado"),
		//ACTIVO_DEVUELTO(203,"","msg.estado.devuelto"),
		ACTIVO_RESERVA_COSTAMAR(205,"ACTIVO RESERVA","msg.estado.activo");

		private final int codigo;
		private final String  texto;
		private final String keyMsg;

		private ESTADO_REGISTRO_BASE(int estado, String texto, String keyMsg){
			this.codigo = estado;
			this.texto=texto;
			this.keyMsg = keyMsg;
		}
		public int getCodigo() {
			return codigo;
		}
		public String getTexto() {
			return texto;
		}
		public String getKeyMsg() {
			return keyMsg;
		}

		public static ESTADO_REGISTRO_BASE getEstadoRegistroByID(Integer id){
			UtilEnum2.ESTADO_REGISTRO_BASE[] valores = ESTADO_REGISTRO_BASE.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo() == id){
					return  valores[i];
				}
			}
			return null;
		}
		
		/**
		  * @param estado
		  * @return	: String
		  * @date	: 5/6/2015
		  * @time	: 14:42:35
		  * @author	: Erick vb.
		  * @descripcion : 	retorna el estado x defecto del enum
		 */
		public static String getEstadoTexto(Integer estado){
			String descripcion =  null;
			for(ESTADO_REGISTRO_BASE item : ESTADO_REGISTRO_BASE.values() ){
				if(item.getCodigo() ==estado){
					descripcion = item.getTexto();
					break;
				}
				
			}
			return descripcion;
		}
		
		public static String getEstadoKeyMsg(Integer estado){
			String codeMsg =  null;
			for(UtilEnum2.ESTADO_REGISTRO_BASE item : UtilEnum2.ESTADO_REGISTRO_BASE.values() ){
				if(item.getCodigo() ==estado){
					codeMsg = item.getKeyMsg();
					break;
				}
				
			}
			return codeMsg;
		}
		
		public static ESTADO_TRANSICION getEstadoTransicion(Integer estado){
			ESTADO_TRANSICION estadoFind = null;
			
			if(estado<0){
				estadoFind= ESTADO_TRANSICION.INDEFINIDO;
			}else if(estado==0){
				estadoFind=  ESTADO_TRANSICION.ELIMINADO;
			}else if(estado>0 && estado<50){
				estadoFind=  ESTADO_TRANSICION.RECHAZADO_ANULADO;
			}else if(estado>50 && estado<100){
				estadoFind=  ESTADO_TRANSICION.PENDIENTE;
			}else if(estado>100 && estado<200){
				estadoFind=  ESTADO_TRANSICION.CONFIRMADO;
			}else if(estado>200 && estado<300){
				estadoFind=  ESTADO_TRANSICION.ACTIVO;
			}
			else{
				estadoFind= ESTADO_TRANSICION.INDEFINIDO;
			}
			return estadoFind;
		}
	}
	
	public  enum ESTADO_TRANSICION{
		INDEFINIDO,
		ELIMINADO,
		RECHAZADO_ANULADO,
		PENDIENTE,
		CONFIRMADO,
		ACTIVO
	}

	public  enum EXTENSION_FICHERO{
		PDF(".pdf"),
		EXCEL_97_2003(".xls"),
		EXCEL(".xlsx"),
		CSV(".csv"),
		TXT(".txt");

		private final String  extension;

		private EXTENSION_FICHERO(String extension){
			this.extension = extension;
		}

		public String getExtension() {
			return extension;
		}
	}

	public  enum ACCION_MANTENIMIENTO{
		REGISTRAR(1),
		ACTUALIZAR(2),
		DESACTIVAR(3),
		ELIMINAR(0),
		ACTUALIZAR_ESTADO(4),
		ACTUALIZAR_IMAGEN(5),
		ACTUALIZAR_PARCIAL(6),
		REPLICAR(7);
		private final int codigo;

		private ACCION_MANTENIMIENTO(int estado){
			this.codigo = estado;
		}
		public int getCodigo() {
			return codigo;
		}
	}

	public  enum EXCEPTION_DB{
		NO_PROCESADO(-1),
		PRIMARY_KEY_VIOLATION(-2),
		FOREING_KEY_VIOLATION(-3),
		UNIQUE_KEY_VIOLATION(-4),
		ERROR(-10),
		DUPLICATE_VALUE(-11),
		EXCEDE_MONTO(-12),
		SIN_STOCK(-13);
		
		private final int codigo;

		public int getCodigo() {
			return codigo;
		}

		private EXCEPTION_DB(int codigo) {
			this.codigo = codigo;
		}
	}

	public  enum TIPO_BENEFICIO{
		DESCUENTO(1,"DESCUENTO","msg.catalogo.descuentos"),
		CATALAGO(2,"CATALOGO","msg.catalogo.productos"),
		CAMPANIA(3,"CAMPANIA","msg.catalogo.campania"),
		EXPERIENCIA(4,"EXPERIENCIA","msg.catalogo.experiencia");

		private final int codigo;
		private final String  texto;
		private final String keyMsg;
		private TIPO_BENEFICIO(int codigo, String texto, String keyMsg){
			this.codigo = codigo;
			this.texto = texto;
			this.keyMsg= keyMsg;
		}
		public int getCodigo() {
			return codigo;
		}
		public String getTexto() {
			return texto;
		}
		public String getKeyMsg() {
			return keyMsg;
		}
		
		public static TIPO_BENEFICIO getTipoBeneficioByID(Integer id){
			TIPO_BENEFICIO[] valores = TIPO_BENEFICIO.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo() == id){
					return  valores[i];
				}
			}
			return null;
		}
		
		public static String getTipoBeneficioKeyMsg(Integer estado){
			String descripcion =  null;
			for(UtilEnum2.TIPO_BENEFICIO item : UtilEnum2.TIPO_BENEFICIO.values() ){
				if(item.getCodigo() ==estado){
					descripcion = item.getKeyMsg();
					break;
				}
				
			}
			return descripcion;
		}
	}

	public  enum TIPO_WISHLIST{
		CATALOGO(1,"CATALOGO");		
		private final int codigo;
		private final String  texto;
		private TIPO_WISHLIST(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}
		public int getCodigo() {
			return codigo;
		}
		public String getTexto() {
			return texto;
		}
	}
	public  enum TIPO_SEGMENTO{
		PERIODO(1,"Periodo"),
		CAMPANIA(2,"Campaña"),
		BASE(11,"Base");

		private final int codigo;
		private final String  texto;
		private TIPO_SEGMENTO(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}
		public int getCodigo() {
			return codigo;
		}
		public String getTexto() {
			return texto;
		}
		
		public static String getTipoSegmentoTexto(Integer estado){
			String descripcion =  null;
			for(UtilEnum2.TIPO_SEGMENTO item : UtilEnum2.TIPO_SEGMENTO.values() ){
				if(item.getCodigo() ==estado){
					descripcion = item.getTexto();
					break;
				}
				
			}
			return descripcion;
		}
	}
	
//	public static enum CATALAGO_ESTADO_PEDIDO{
//
//		//RECIBIDO(1,"_pendingApproval","Pendiente Pedido");
//		//CONFIRMACION_STOCK(2,"_pendingFulfillment","Pedido parcialmente aprobado")
//		//Guia remision
//		//Listo para despacho -en ruta
//		//reprogramado
//		//Cancelacion pedido  
//		//entregado
//        REGISTRADO(0,"registrado","Pedido registrado","msg.estado.pedido.registrado",0), //PEDIDO_RECIBIDO
//		RECIBIDO(1,"_pendingApproval","Aprobación pendiente","msg.estado.pedido.recibido",1), //PENDIENTE_APROBACION (evaluan el precio del producto)
//		CONFIRMACION_STOCK(2,"_pendingFulfillment","Ejecución del pedido pendiente","msg.estado.pedido.confirmado",2), // 1ro NS RECIBIDO (recibido por ns, fue confirmado) APROBADO
//		GUIA_REMISION(3,"_partiallyFulfilled","Parcialmente completado","msg.estado.pedido.listadespacho",3), //(Se emitio una guia parcial parcial (no se usa xk no esta habilitado en ns)) GUIA_REMISION_PARCIAL
//		
//		LISTO_DESPACHO_EN_RUTA(4,"_pendingBillingPartFulfilled","Facturación pendiente/parcialmente ejecutada","msg.estado.pedido.enruta",4),// GUIA_REMISION_PARCIAL (si es igual q este estado buscar en NS)
//		REPROGRAMADO(5,"_pendingBilling","Facturación pendiente","msg.estado.pedido.reprogramado",5),//(Se emitio la guia con todos los productos) GUIA_REMISION_TOTAL  (si es igual q este estado buscar en NS)
//		// (canjeado en efectivo ) _fullyBilled GUIA_REMISION_TOTAL (si es igual q este estado buscar en NS)
//		
//	
//		//itemFullFillment aqui estan los estados de la guia pcking shipp 
//		
//		//DESPACHO : SO esta 
//			//_pendingBillingPartFulfilled y picking
//			//_pendingBilling y picking
//		
//		//En RUTA : 
//			//Visita1 ó shipped
//			//Visita2 ó shipped
//			//Visita3 ó shipped
//		
//		//Estados de tracking
//		DERIVADO_A_BANCO(6,"_derivadoBanco","Derivado al banco ibk","msg.estado.pedido.derivado",6),//(no tiene SO, es estado de la guia) REPROGRAMADO
//		NO_ENTREGADO(7,"_noEntregado","No entregado","msg.estado.pedido.noentregado",7),//(no tiene SO) NO_ENTREGADO
//		CANCELADO(8,"_cancelled","Cancelado","msg.estado.pedido.cancelado",8), //( _cancelled estado del SO)ANULADO
//		ENTREGADO(9,"_closed","Entregado","msg.estado.pedido.entregado",9); //ENTREGADO
//
//		private final int id;
//		private final String codigo;
//		private final String texto;
//		private final String keyMsg;
//		private final int ordenDefault;
//
//		private CATALAGO_ESTADO_PEDIDO(Integer id,String codigo,String texto, String keyMsg, int ordenDefault){
//			this.id=id;
//			this.codigo=codigo;
//			this.texto=texto;
//			this.keyMsg = keyMsg;
//			this.ordenDefault = ordenDefault; 
//		}
//
//		public static Integer getIdByCodigo(String codigo){
//			Integer strCodigoReturn=null;
//			UtilEnum2.CATALAGO_ESTADO_PEDIDO[] valores = CATALAGO_ESTADO_PEDIDO.values();
//			for(int i=0; i<valores.length; i++){
//				if(valores[i].getCodigo().equalsIgnoreCase(codigo)){
//					strCodigoReturn=valores[i].getId();
//					break;
//				}
//			}
//			return strCodigoReturn;
//		}
//		
//		public static Integer getIdByTexto(String texto){
//			Integer strCodigoReturn=null;
//			UtilEnum2.CATALAGO_ESTADO_PEDIDO[] valores = CATALAGO_ESTADO_PEDIDO.values();
//			for(int i=0; i<valores.length; i++){
//				if(valores[i].getTexto().equals(texto)){
//					strCodigoReturn=valores[i].getId();
//					break;
//				}
//			}
//			return strCodigoReturn;
//		}
//
//		public static String getCodigoByTexto(String texto){
//			String strCodigoReturn=null;
//			UtilEnum2.CATALAGO_ESTADO_PEDIDO[] valores = CATALAGO_ESTADO_PEDIDO.values();
//			for(int i=0; i<valores.length; i++){
//				if(valores[i].getTexto().equals(texto)){
//					strCodigoReturn=valores[i].getCodigo();
//					break;
//				}
//			}
//			return strCodigoReturn;
//		}
//
//
//		public static CATALAGO_ESTADO_PEDIDO getEstadoPedidoByID(Integer id){
//			UtilEnum2.CATALAGO_ESTADO_PEDIDO[] valores = CATALAGO_ESTADO_PEDIDO.values();
//			for(int i=0; i<valores.length; i++){
//				if(valores[i].getId() == id){
//					return  valores[i];
//				}
//			}
//			return null;
//		}
//		
//		public static String getTextoByCodigo(String codigo){
//			String strCodigoReturn=null;
//			UtilEnum2.CATALAGO_ESTADO_PEDIDO[] valores = CATALAGO_ESTADO_PEDIDO.values();
//			for(int i=0; i<valores.length; i++){
//				if(valores[i].getCodigo().equals(codigo)){
//					strCodigoReturn=valores[i].getTexto();
//					break;
//				}
//			}
//			return strCodigoReturn;
//		}
//
//		public int getId() {
//			return id;
//		}
//
//		public String getCodigo() {
//			return codigo;
//		}
//
//		public String getTexto() {
//			return texto;
//		}
//
//		public String getKeyMsg() {
//			return keyMsg;
//		}
//
//		public int getOrdenDefault() {
//			return ordenDefault;
//		}
//
//	}

	public  enum TIPO_OPERACION_PUNTOS {
		CATALOGO(1, "CATALOGO"), DESCUENTO(2, "DESCUENTO");
		private final int codigo;
		private final String texto;

		private TIPO_OPERACION_PUNTOS(int codigo, String texto) {
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
	}

	public  enum TIPO_CATEGORIA {
		PRODUCTO(1, "PRODUCTO"),
		DESCUENTO(2, "DESCUENTO"),
		MARCA(3, "MARCA"),
		PAQUETE(4, "PAQUETE"),
		ENTRETENIMIENTO(5, "ENTRETENIMIENTO"),
		PROMOCION(6, "PROMOCION"),
		EVALES(7,"EVALES"),
		TODOS(0, "TODOS");
		private final Integer codigo;
		private final String texto;

		private TIPO_CATEGORIA(Integer codigo, String texto) {
			this.codigo = codigo;
			this.texto = texto;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		
		public static TIPO_CATEGORIA obtenerTipoCategoriaByCodigo(Integer codigo){
		  TIPO_CATEGORIA[] valores = TIPO_CATEGORIA.values();
		  for(int i=0; i< valores.length; i++){
			  if(valores[i].getCodigo().equals(codigo)){
					return  valores[i];
			  }
		  }
		  return null;
		}
	}

	public  enum TIPO_DOCUMENTO {
//		DNI(1, "DNI","ID"),PASAPORTE (2, "Pasaporte","PASSPORT"), CARNET_EXTRAJERIA(3,"CEX","CE");		
		DNI(1, "DNI","ID","2","DNI",1), PASAPORTE(2, "Pasaporte","PASSPORT","4","PASSPORT",5), CARNET_EXTRAJERIA(3,"CE","CE","1","CE",3);
		
		private final Integer codigo;
		private final String texto;
		private final String costamar;
		private final String netSuite;
		private final String costamarPaquete;
		private final Integer codigoBIM;
		
		private TIPO_DOCUMENTO(Integer codigo, String texto,String costamar, String netSuite, String costamarPaquete,Integer codigoBIM) {
			this.codigo = codigo;
			this.texto = texto;
			this.costamar=costamar;
			this.netSuite= netSuite;
			this.costamarPaquete = costamarPaquete;
			this.codigoBIM = codigoBIM;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}

		public String getCostamar() {
			return costamar;
		}
		public String getNetSuite() {
			return netSuite;
		}
		public String getCostamarPaquete() {
			return costamarPaquete;
		}
		
		public Integer getCodigoBIM() {
			return codigoBIM;
		}
		
		public static TIPO_DOCUMENTO obtenerTipoDocumentoByCodigo(Integer codigo){
		  TIPO_DOCUMENTO[] valores = TIPO_DOCUMENTO.values();
		  for(int i=0; i< valores.length; i++){
			  if(valores[i].getCodigo().equals(codigo)){
					return  valores[i];
			  }
		  }
		  return null;
		}
		
		public static TIPO_DOCUMENTO obtenerTipoDocumentoByCodigoBIM(Integer codigo){
			  TIPO_DOCUMENTO[] valores = TIPO_DOCUMENTO.values();
			  for(int i=0; i< valores.length; i++){
				  if(valores[i].getCodigoBIM().equals(codigo)){
						return  valores[i];
				  }
			  }
			  return null;
			}
	}
	
	
//	public static enum TIPO_DOCUMENTO_VUELOS {
////		DNI(1, "DNI","ID"),PASAPORTE (2, "Pasaporte","PASSPORT"), CARNET_EXTRAJERIA(3,"CEX","CE");
//		DNI(1, "DNI","ID"),PASAPORTE (2, "Pasaporte","PASSPORT"), CARNET_EXTRAJERIA(3,"CEX","CE");
//		private final Integer codigo;
//		private final String texto;
//		private final String costamar;
//
//		private TIPO_DOCUMENTO_VUELOS(Integer codigo, String texto,String costamar) {
//			this.codigo = codigo;
//			this.texto = texto;
//			this.costamar=costamar;
//		}
//
//		public Integer getCodigo() {
//			return codigo;
//		}
//
//		public String getTexto() {
//			return texto;
//		}
//
//		public String getCostamar() {
//			return costamar;
//		}
//	}

	public  enum GENERO {
		MASCULINO("M", "Masculino"),FEMENINO ("F", "Femenino ");
		private final String codigo;
		private final String texto;

		private GENERO(String codigo, String texto) {
			this.codigo = codigo;
			this.texto = texto;
		}

		public String getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
	}

	public  enum TIPO_VENTA{
		CATALOGO_PRODUCTO(1,"CATALOGO PRODUCTO", TIPO_PROVEEDOR_IBK.PLAZAPOINTS),
		DESCUENTO(2,"DESCUENTO",TIPO_PROVEEDOR_IBK.PLAZAPOINTS),
		VUELO(3,"VUELOS",TIPO_PROVEEDOR_IBK.COSTAMAR),
		PAQUETE(4,"PAQUETE",TIPO_PROVEEDOR_IBK.COSTAMAR),
		EVALES(5,"EVALES",TIPO_PROVEEDOR_IBK.ZONACARDS);

		private final int codigo;
		private final String texto;
		private final TIPO_PROVEEDOR_IBK tipoProvedor;

		private TIPO_VENTA(int codigo, String texto, TIPO_PROVEEDOR_IBK tipoProvedor){
			this.codigo = codigo;
			this.texto = texto;
			this.tipoProvedor = tipoProvedor;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		
		public static TIPO_VENTA getTipoVenta(Integer codigo){
			UtilEnum2.TIPO_VENTA[] valores = TIPO_VENTA.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return  valores[i];
				}
			}
			return null;
		}

		public TIPO_PROVEEDOR_IBK getTipoProvedor() {
			return tipoProvedor;
		}
	}

	public  enum TIPO_TRACKING{
		CATALOGO(1,"CATALOGO"),
		DESCUENTO(2,"DESCUENTO"),
		VUELO(3,"VUELOS"),
		PAQUETE(4,"PAQUETE"),
		EVALES(5,"EVALES"),
		PRODUCTO(6, "PRODUCTO"),
		CATEGORIA(11,"CATEGORIA"),
		ESTADO_CUENTA_CLIENTE(12,"ESTADO CUENTA CLIENTE"),
		MENSAJE_BIENVENIDA(21,"MENSAJE BIENVENIDA");

		private final int codigo;
		private final String texto;

		private TIPO_TRACKING(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static TIPO_TRACKING getTipoTracking(int codigo){
			UtilEnum2.TIPO_TRACKING[] valores = TIPO_TRACKING.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return  valores[i];
				}
			}
			return null;
		}

	}

	/*Tipo de descuento*/

	public  enum TIPO_DESCUENTO{
		SUBVENCIONADO(1,"SUBVENCIONADO"),
		NOSUBVENCIONADO(2,"NO SUBVENCIONADO");

		private final int codigo;
		private final String texto;

		private TIPO_DESCUENTO(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		

		 /**
		  * @param tipo
		  * @return	: String
		  * @date	: 11/8/2015
		  * @time	: 11:08:30
		  * @author	: Diego A.
		  * @descripcion : Obtener el texto del tipo descuento	
		 */
		public static String getTipoDescuentoTexto(Integer tipo){
			String descripcion =  null;
			for(TIPO_DESCUENTO item : TIPO_DESCUENTO.values() ){
				if(item.getCodigo() ==tipo){
					descripcion = item.getTexto();
					break;
				}
				
			}
			return descripcion;
		}
	}

	public  enum ESTADO_VENTA{
		ELIMINADO(0,"Eliminado","msg.estado.venta.eliminado"),

		//desde 101 -299
		PENDIENTE_PRODUCTO(101,"PENDIENTE_PRODUCTO","msg.estado.venta.pendiente"),
		PENDIENTE_VUELOS(121,"PENDIENTE_VUELOS","msg.estado.venta.pendiente"),
		PENDIENTE_PAQUETE(131,"PENDIENTE_PAQUETE","msg.estado.venta.pendiente"),
		PENDIENTE_DESCUENTO(141,"PENDIENTE_DESCUENTO","msg.estado.venta.pendiente"),
		PENDIENTE_EVALES(142,"PENDIENTE_EVALES","msg.estado.venta.pendiente"),
		//desde 301 hasta 499
		CONFIRMADO_PRODUCTO(301,"CONFIRMADO_PRODUCTO","msg.estado.venta.confirmado"),
		CONFIRMADO_VUELOS(321,"CONFIRMADO_VUELOS","msg.estado.venta.confirmado"),
		CONFIRMADO_PAQUETE(331,"CONFIRMADO_PAQUETE","msg.estado.venta.confirmado"),
		CONFIRMADO_DESCUENTO(341,"CONFIRMADO_DESCUENTO","msg.estado.venta.confirmado"),
		CONFIRMADO_EVALES(342,"CONFIRMADO_EVALES","msg.estado.venta.confirmado"),

		//desde 501-799
		ANULADO_PRODUCTO(501,"ANULADO_PRODUCTO","msg.estado.venta.anulado"),
		ANULADO_VUELOS(521,"ANULADO_VUELOS","msg.estado.venta.anulado"),
		ANULADO_PAQUETE(531,"ANULADO_PAQUETE","msg.estado.venta.anulado"),
		ANULADO_DESCUENTO(541,"ANULADO_DESCUENTO","msg.estado.venta.anulado"),
		ANULADO_EVALES(542,"ANULADO_EVALES","msg.estado.venta.anulado"),
		ANULADO_NO_CONCRETADO(561,"ANULADO_NO_CONCRETADO","msg.estado.venta.anulado.noConcretado");

		private final int codigo;
		private final String texto;
		private final String message;

		private ESTADO_VENTA(int codigo, String texto, String message){
			this.codigo = codigo;
			this.texto = texto;
			this.message = message;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}

		public String getMessage() {
			return message;
		}

		public static ESTADO_VENTA getEstadoVentaByID(Integer id){
			UtilEnum2.ESTADO_VENTA[] valores = ESTADO_VENTA.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo() == id){
					return  valores[i];
				}
			}
			return null;
		}

	}

	public  enum ORIGEN_REGISTRO_CLIENTE{
		WEB(1,"Web"),
		FACEBOOK(2,"Facebook"),
		YOUTUBE(3,"Youtube"),
		GOOGLE(4,"Google"),
		APPMOVIL_POSVIRTUAL(6,"App Movil Pos Virtual"),
		APPMOVIL_IOS(7,"App Movil IOS"),
		API_MERCHAN(8,"Api Merchan"),
		API_WEB_SERVICE(9,"Api Web Service"),
		APPIPAD_CINEMARK(11,"App Ipad Cinemark"),
		POS_COMERCIO(12,"Pos Comercio");

		private final int codigo;
		private final String texto;

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		private ORIGEN_REGISTRO_CLIENTE(int codigo, String texto) {
			this.codigo = codigo;
			this.texto =  texto;
		}
	}

	public  enum UBIGEO_NIVEL{
		PAIS(1),
		DEPARTAMENTO(2),
		PROVINCIA(3),
		DISTRITO(4),
		ZONA(5);

		private final int codigo;

		public int getCodigo() {
			return codigo;
		}

		private UBIGEO_NIVEL(int codigo) {
			this.codigo = codigo;
		}

	}		
	public  enum USUARIO_APLICACION{
		NETSUITE(1,"Usuario NetSuite"),
		CLIENTE_WEB(2,"Cliente Web"),
		ADMIN_WEB(3,"Admin Web"),
		PORTAL_WEB(4,"Portal Web"),
		SERVICE_IBK(5,"Service IBK"),
		SERVICE_COSTAMAR(6,"Service Costamar"),
		SERVICE_SAAS(7,"Service Saas"),
		SERVICES_AMQ(8,"Service AMQ"),
		SERVICE_ADMIN(9,"Service Admin");

		private Integer codigo;
		private String descripcion;
		
		
		public Integer getCodigo() {
			return codigo;
		}

		public void setCodigo(Integer codigo) {
			this.codigo = codigo;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}

		private USUARIO_APLICACION(Integer codigo,String descripcion) {
			this.codigo=codigo;
			this.descripcion=descripcion;
		}
		
		public static String obtenerAliasUsuario(String cadena){
			String[] nombre = cadena.split(":");
			String respuesta = "";
			
			if(nombre.length > 2){
				respuesta = nombre[2];
			}
			
			return respuesta;
		}
		
	}

	
	
	public  enum CATALOGO_PRODUCTO_ORDERBY {
		MENOR_PRECIO("PRECIO_CATALOGO_ASC", "Menor Precio"),
		MAYOR_PRECIO("PRECIO_CATALOGO_DESC", "Mayor Precio"),
		NOMBRE_A_Z ("NOMBRE_CATALOGO_ASC", "Nombre A-Z"),
		NOMBRE_Z_A ("NOMBRE_CATALOGO_DESC", "Nombre Z-A"),
		MILLAS_BENEFIT ("ALCANZA_CATALOGO_MILLAS", "Me alcanza con mis Millas Benefit"),
		DESTACADOS("DESTACADO_DESC","Destacado");

		private final String codigo;
		private final String texto;

		private CATALOGO_PRODUCTO_ORDERBY(String codigo, String texto) {
			this.codigo = codigo;
			this.texto = texto;
		}

		public String getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static CATALOGO_PRODUCTO_ORDERBY getCatalogoOrdenByID(String id){
			CATALOGO_PRODUCTO_ORDERBY[] valores = CATALOGO_PRODUCTO_ORDERBY.values();
		
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo().equals(id)){
					return  valores[i];
				}
			}
			return null;
		}
	}
	
	public  enum VALES_DIGITALES_ORDERBY {
		MENOR_PRECIO("PRECIO_CATALOGO_ASC", "Menor Precio"),
		MAYOR_PRECIO("PRECIO_CATALOGO_DESC", "Mayor Precio"),
		NOMBRE_A_Z ("NOMBRE_CATALOGO_ASC", "Nombre A-Z"),
		NOMBRE_Z_A ("NOMBRE_CATALOGO_DESC", "Nombre Z-A"),
		MILLAS_BENEFIT ("ALCANZA_CATALOGO_MILLAS", "Me alcanza con mis Millas Benefit"),
		MENOR_PRECIO_PUNTOS("PRECIO_PUNTOS_ASC","Menor Precio Puntos");

		private final String codigo;
		private final String texto;

		private VALES_DIGITALES_ORDERBY(String codigo, String texto) {
			this.codigo = codigo;
			this.texto = texto;
		}

		public String getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static VALES_DIGITALES_ORDERBY getValesDigitalesOrdenByID(String id){
			VALES_DIGITALES_ORDERBY[] valores = VALES_DIGITALES_ORDERBY.values();
		
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo().equals(id)){
					return  valores[i];
				}
			}
			return null;
		}
	}

	public  enum DELIVERY_TIPO_CALLE{
		AVENIDA("Avenida"),
		CALLE("Calle"),
		JIRON("Jirón"),
		OTRO("Otros");

		private final String codigo;

		private DELIVERY_TIPO_CALLE(String codigo){
			this.codigo = codigo;
		}
		public String getCodigo() {
			return codigo;
		}
	}

	public  enum DELIVERY_HORARIO_ENTREGA{
		MANHANA("Durante la mañana"),
		TARDE("Durante la tarde");

		private final String codigo;

		private DELIVERY_HORARIO_ENTREGA(String codigo){
			this.codigo = codigo;
		}
		public String getCodigo() {
			return codigo;
		}
	}

	public  enum DELIVERY_TIPO_ENTREGA{		
		REGULAR(1,"Regular"),
		EXPRESS(2, "Express");

		private final int codigo;
		private final String descripcion;

		private DELIVERY_TIPO_ENTREGA(int codigo, String descripcion){
			this.codigo = codigo;
			this.descripcion = descripcion;
		}
		public int getCodigo() {
			return codigo;
		}
		public String getDescripcion(){
			return descripcion;
		}
		
		public static DELIVERY_TIPO_ENTREGA getTipoEntrega(Integer id){
			DELIVERY_TIPO_ENTREGA[] valores = DELIVERY_TIPO_ENTREGA.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo() == id){
					return  valores[i];
				}
			}
			return null;
		}
		
		public static DELIVERY_TIPO_ENTREGA getTipoEntrega(String texto){
			DELIVERY_TIPO_ENTREGA[] valores = DELIVERY_TIPO_ENTREGA.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getDescripcion().equals(texto)){
					return  valores[i];
				}
			}
			return null;
		}
	}

	public  enum TIPO_MONEDA{

		PERU("pen","soles","S/",1),

		EEUU("usd","dolares","US$",2);
		private final String codigo;
		private final String descripcion;
		private final String simbolo;
		private final int id;

		private TIPO_MONEDA(String codigo,String descripcion, String simbolo, int id){
			this.codigo=codigo;
			this.descripcion=descripcion;
			this.simbolo = simbolo;
			this.id = id;
		}

		public String getCodigo() {
			return codigo;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public String getSimbolo() {
			return simbolo;
		}

		public int getId() {
			return id;
		}
		
		public static TIPO_MONEDA getTipoMoneda(Integer codigo){
			TIPO_MONEDA[] valores = TIPO_MONEDA.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getId()==codigo){
					return valores[i];
					
				}
			}
			return null;
		}

	}
	
	
	public  enum TIPO_PROVEEDOR_IBK{

		PLAZAPOINTS(1,"PLAZAPOINTS"),
		COSTAMAR(2,"COSTAMAR"),
		ZONACARDS(3,"ZONACARDS");

		private final int codigo;
		private final String descripcion;
	
		private TIPO_PROVEEDOR_IBK(int codigo, String descripcion){
			this.codigo=codigo;
			this.descripcion = descripcion;
		}


		public Integer getCodigo() {
			return codigo;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public static TIPO_PROVEEDOR_IBK getTipoProveedor(Integer codigo){
			TIPO_PROVEEDOR_IBK[] valores = TIPO_PROVEEDOR_IBK.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return valores[i];
				}
			}
			return null;
		}

	}

	
	public  enum TIPO_PERIODO{
		ANUAL(1,"PERIODO ANUAL"),
		CAMPANIA(2,"PERIODO CAMPANIA");
		private final int codigo;
		private final String  texto;
		private TIPO_PERIODO(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}
		public int getCodigo() {
			return codigo;
		}
		public String getTexto() {
			return texto;
		}
		public static TIPO_PERIODO getPeriodo(Integer codigo){
			TIPO_PERIODO[] valores = TIPO_PERIODO.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return valores[i];
				}
			}
			return null;
		}
	}

	public  enum ESTADO_RECLAMO{
		ABIERTO(1,"ABIERTO","abierto reclamo abierto abiertos open en proceso","#d91e18"),
		PENDIENTE(2,"PENDIENTE","pendiente reclamo pendiente pending  esperando tu respuesta","#e87e04"),
		RESUELTO(3,"RESUELTO","resuelto reclamo resuelto resolved este ticket se resolvio","#f7ca18"),
		CERRADO(4,"CERRADO","cerrado reclamo cerrado closed este ticket se cerro","#26c281"),
		ESPERA_CLIENTE(5,"EN ESPERA CLIENTE","en espera de cliente waiting on customer awaiting your reply","#3598dc"),
		ESPERA_TERCERO(6,"EN ESPERA DE TERCERO","en espera de tercero waiting on third party being processed","#32c5d2");
		

		private final Integer codigo;
		private final String  texto;
		private final String diccionarioApi;
		private final String color;
		
		private ESTADO_RECLAMO(Integer codigo, String texto, String diccionarioApi, String color){
			this.codigo = codigo;
			this.texto = texto;
			this.diccionarioApi = diccionarioApi;
			this.color = color;
		}
		public Integer getCodigo() {
			return codigo;
		}
		public String getTexto() {
			return texto;
		}

		public String getDiccionarioApi() {
			return diccionarioApi;
		}
		public String getColor() {
			return color;
		}
		/*public static String obtenerNombreEstado(Integer id){
			String strCodigoReturn=null;
			UtilEnum2.ESTADO_RECLAMO[] valores = ESTADO_RECLAMO.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo().equals(id)){
					strCodigoReturn=valores[i].getTexto();
					return strCodigoReturn;
				}
			}
			return "";
		}
		
		public static String getEstadoReclamoTexto(Integer estado){
			String descripcion =  null;
			for(UtilEnum2.ESTADO_RECLAMO item : UtilEnum2.ESTADO_RECLAMO.values() ){
				if(item.getCodigo().equals(estado)){
					descripcion = item.getTexto();
					break;
				}
				
			}
			return descripcion;
		}*/
		
		public static ESTADO_RECLAMO getEstadoReclamo(Integer estado){
			
			for(UtilEnum2.ESTADO_RECLAMO item : UtilEnum2.ESTADO_RECLAMO.values() ){
				if(item.getCodigo().equals(estado)){
					return item;
				}
				
			}
			return null;
		}
		
	}
	public  enum RESPUESTA_SERVICIO{
		EXITO(1,"EXITO"),
		ERROR_INTERNO_CONSUMO_SERVICIO(2,"ERROR_INTERNO_CONSUMO_SERVICIO"),
		ERROR_LLAMADO(3,"ERROR_EN_EL_LLAMADO_AL_SERVICIO"),
		ERROR_URL_NO_ENCONTRADA(4,"ERROR_URL_NO_ENCONTRADA");


		private final Integer codigo;
		private final String texto;

		private RESPUESTA_SERVICIO(Integer codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static RESPUESTA_SERVICIO getRespuestaServicio(Integer codigo){
			RESPUESTA_SERVICIO[] valores = RESPUESTA_SERVICIO.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return valores[i];
				}
			}
			return null;
		}

	}


	public  enum TIPO_VUELO_COSTAMAR{
		VUELO_IDA(0,"VUELO_IDA"),

		VUELO_REGRESO(1,"VUELO_REGRESO");

		private final Integer codigo;
		private final String texto;

		private TIPO_VUELO_COSTAMAR(Integer codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}

	}
	public  enum TIPO_COMENTARIO{
		RESUMEN(1,"RESUMEN","msg.resumen"),
		PERIODO(2,"PERIODO","msg.periodo"),
		SEGMENTO(3,"SEGMENTO","msg.segmento"),
		CATALOGO_PRODUCTO(4,"CATALOGO PRODUCTO","msg.catalogo.producto"),
		CATALOGO_DESCUENTO(5,"CATALOGO DESCUENTO","msg.catalogo.descuento");

		private final int codigo;
		private final String  texto;
		private final String keyMsg;
		private TIPO_COMENTARIO(int codigo, String texto, String keyMsg){
			this.codigo = codigo;
			this.texto = texto;
			this.keyMsg= keyMsg;
		}
		public int getCodigo() {
			return codigo;
		}
		public String getTexto() {
			return texto;
		}
		public String getKeyMsg() {
			return keyMsg;
		}
	}

	public  enum TIPO_AGRUPADOR {
		PRODUCTO(1, "PRODUCTO");

		private final int codigo;
		private final String texto;

		private TIPO_AGRUPADOR(int codigo, String texto) {
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
	}

	public  enum TIPO_USUARIO{
		PLAZAPOINTS(1,"PlazaPoints"),
		BGM(2,"BGM"),
		INTERBANK(3,"Interbank"),
		COSTAMAR(4,"Costamar"),
		CLIENTE(5,"Cliente");
		private final int codigo;
		private final String texto;

		private TIPO_USUARIO(int codigo, String texto) {
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static TIPO_USUARIO getTipoUsuario(Integer idTipo){
			TIPO_USUARIO[] valores = TIPO_USUARIO.values();
			TIPO_USUARIO tipoUsuario=null;
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo() == idTipo){
					tipoUsuario =  valores[i];
					return tipoUsuario;
				}
			}
			
			return null;
		}
	}

	public  enum RESPUESTA_RESERVA_COSTAMAR{
		SEGMENTO_CANCELADO(0,"SEGMENTO_CANCELADO","msg.reserva.vuelos.error0"),		
		RESERVADO(1,"RESERVADO","msg.reserva.vuelos.error1"),
		DATOS_INCORRECTOS(2,"DATOS_INCORRECTOS","msg.reserva.vuelos.error2"),
		RESERVA_DUPLICADA(3,"RESERVA_DUPLICADA","msg.reserva.vuelos.error3"),
		PROBLEMAS_CONEXION_PROVEEDOR(6,"PROBLEMAS_CONEXION_PROVEEDOR","msg.reserva.vuelos.error6"),
		ERROR_PROCESO_RESERVA(7,"ERROR_PROCESO_RESERVA","msg.reserva.vuelos.error7"),
		LA_TARIFA_HA_CAMBIADO(9,"LA_TARIFA_HA_CAMBIADO","msg.reserva.vuelos.error9"),
		EXCEPTION(ESTADO_OPERACION.EXCEPTION.getCodigo(),ESTADO_OPERACION.EXCEPTION.getTexto(),"msg.reserva.vuelos.error10");


		private final Integer codigo;
		private final String texto;
		private final String keyMsg;

		private RESPUESTA_RESERVA_COSTAMAR(Integer codigo, String texto, String keyMsg){
			this.codigo = codigo;
			this.texto = texto;
			this.keyMsg = keyMsg;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		public String getKeyMsg() {
			return keyMsg;
		}
		
		public static RESPUESTA_RESERVA_COSTAMAR getRespuestaReseva(int codigo){
			RESPUESTA_RESERVA_COSTAMAR[] valores = RESPUESTA_RESERVA_COSTAMAR.values();
			RESPUESTA_RESERVA_COSTAMAR respuesta=null;
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo() == codigo){
					respuesta =  valores[i];
					return respuesta;
				}
			}
			
			return null;
		}
		
	}

	public  enum TIPO_POS{
		VISA(1,"Visa"),
		MASTERCARD(2,"MC Procesos");
		private final Integer codigo;
		private final String texto;

		private TIPO_POS(int codigo, String texto) {
			this.codigo = codigo;
			this.texto = texto;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static String getTipoPos(Integer estado){
			String descripcion =  null;
			for(UtilEnum2.TIPO_POS item : UtilEnum2.TIPO_POS.values() ){
				if(item.getCodigo().equals(estado)){
					descripcion = item.getTexto();
					break;
				}
				
			}
			return descripcion;
		}
		
	}
	public  enum TIPO_ENTIDAD{
		NONE(0,"NONE"),
		USUARIO(1,"USUARIO"),
		CLIENTE(2,"CLIENTE"),
		VENTA(3,"VENTA"),
		PERIODO_ANUAL(4,"PERIODO ANUAL"),
		SEGMENTO(5,"SEGMENTO"),
		PROMOCIONES(6,"PROMOCION"), 
		CATEGORIA(7,"CATEGORIA"),
		MARCA(8,"MARCA"), 
		EMPRESA(9,"EMPRESA"), 
		LOCAL(10,"LOCAL"),
		CATALOGO_PRODUCTO(11,"CATALOGO PRODUCTO"),
		DESCUENTO(12,"DESCUENTO"),
		PAQUETE(13,"PAQUETE"), 
		ROL(14,"ROL"),
		DESCUENTOS(18,"DESCUENTOS"),
		BENEFICIO(21,"BENEFICIO"),
		EVALES(22,"EVALES"),
		COMPROBANTE_PAGO(25,"COMPROBANTE PAGO")
		;
		private final int codigo;
		private final String texto;

		private TIPO_ENTIDAD(int codigo, String texto) {
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
	}

	public  enum CUENTA_EMAIL{
		INVITACION(1),
		FEEDBACK(2),
		INFO(3),
		EMPRESA(4),
		CONTACTO(5)
		;
		private final int codigo;

		public int getCodigo() {
			return codigo;
		}
		private CUENTA_EMAIL(int codigo) {
			this.codigo = codigo;
		}

	}

	public  enum PLANTILLA_EMAIL{
		//CONSTANCIA_CANJE(1,"CONS-mail-canje.ftl","config.ruta.plantilla.email"),
		CONSTANCIA_PAQUETE(2,"CONS-mail-reserva-paquete.ftl","config.ruta.plantilla.email.reserva.paquete"),		
		CONSTANCIA_VIAJE(3,"CONS-mail-viajes.ftl","config.ruta.plantilla.email"),
		CONSTANCIA_OPORTUNIDAD(4,"CONS-mail-oportunidad.ftl","config.ruta.plantilla.email"),		
		CONSTANCIA_CANJE_PRODUCTO(5,"CONS-mail-canje_producto.ftl","config.ruta.plantilla.email"),
		CORREO_INFORMACION_CONSULTA(6,"consulta-mail.ftl","config.ruta.plantilla.email"),
		CORREO_RESTABLECER_CONTRASENIA(7,"PlantillaRestableceClave.ftl","config.ruta.plantilla.email"),
		CORREO_GENERAL(8,"plantilla-mail-general.ftl","config.ruta.plantilla.email"),
		CONSTANCIA_CANCELACION_CANJE(9,"CONS-mail-cancelacion_canje.ftl","config.ruta.plantilla.email"),
		CONSTANCIA_LIQUIDACION_OBSERVACION(10,"CONS-mail-observacion-liquidacion.ftl","config.ruta.plantilla.email"),
		CONSTANCIA_CANJE_EVALES(11,"CONS-mail-canje_vale.ftl","config.ruta.plantilla.email"),
		CONSTANCIA_GIFTCARD_DIGITAL(12,"mail_giftcard_digital.ftl","config.ruta.plantilla.email"),
		CONSTANCIA_GIFTCARD_DIGITAL_REGALO(13,"mail_giftcard_digital_regalo.ftl","config.ruta.plantilla.email"),
		CONSTANCIA_GIFTCARD_CONFIRMACION_REGALO(14,"mail_giftcard_confirmacion_regalo.ftl","config.ruta.plantilla.email"),
		
		;		
				
		private final int codigo;
		private final String  nombre;
		private final String  ruta;

		private PLANTILLA_EMAIL(int codigo, String nombre, String ruta){
			this.codigo = codigo;
			this.nombre = nombre;
			this.ruta = ruta;
		}

		public int getCodigo() {
			return codigo;
		}
		public String getNombre (){
			return nombre;
		}
		public String getRuta(){
			return ruta;
		}

	}


	public  enum ASUNTO_EMAIL{
		CONFIRMACION_PEDIDO(1,"Confirmación de pedido"),
		CONFIRMACION_RESERVA_PAQUETE(2,"Confirmación de reserva de paquete"),
		CONFIRMACION_CANJE_PRODUCTO(3,"Confirmación de compra de producto"),
		RECLAMO_CLIENTE(4,"Consulta de reclamo con ID "),
		RETABLECER_CONTRASENIA(5,"Restablecer Contraseña"),
		CONFIRMACION_RESERVA_VUELO(6,"Confirmación de reserva de vuelo"),
		GRUPO_BENEFICIO_APROBACION(7,"Aprobación de grupo beneficios"),
		REPROGRAMACION_CANJE(8,"Reprogramacion canje"),
		ANULACION(9,"Anulación de "),
		GENERACION_REPORTE(10,"Generación de reporte");
		

		private final int codigo;
		private final String  texto;

		private ASUNTO_EMAIL(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}
		public String getTexto (){
			return texto;
		}
	}

	public  enum ESTADO_REGISTRO_PEDIDO_NETSUITE {
		EXITO(0,""),
		NO_EXISTE(-1,"Registro no existe"),
		REGISTRO_DUPLICADO(-2,"Registro duplicado"),
		EXCEPTION(-4,"Excepcion");

		private final int codigo;
		private final String texto;

		public int getCodigo() {
			return codigo;
		}
		public String getTexto(){
			return texto;
		}
		private ESTADO_REGISTRO_PEDIDO_NETSUITE(int codigo, String texto) {
			this.codigo = codigo;
			this.texto=texto;
		}

	}

	public  enum TIPO_HABITACION_PAQUETE{
		DOBLE(2,"Doble"),
		MATRIMONIAL(3,"Matrimonial");

		private final int codigo;
		private final String texto;

		private TIPO_HABITACION_PAQUETE(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}

	}

	public  enum CANAL{
		WEB(1,"WEB"),
		TELEFONO(2,"TELEFONO"),
		TV(3,"TV");

		private final int codigo;
		private final String texto;

		private CANAL(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static CANAL getCanal(Integer codigo){
			UtilEnum2.CANAL[] valores = CANAL.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return valores[i];
					
				}
			}
			return null;
		}
		
	}
	
	public  enum ENVIAR_RESERVA_PAQUETE_COSTAMAR{
		INITIAL(1,"Initial"),
		BOOKING(2,"Booking"),
		COMPLETED_PAYMENT(3,"Completed Payment"),
		DENIED_PAYMENT(4,"Denied Payment"),
		CANCELED_PAYMENT(5,"Canceled Payment"),
		TICKETING(6,"Ticketing"),
		EXPIRED_PAYMENT(7,"Expired Payment"),
		CANCELED_BOOKING(8,"Canceled Booking");

		private final Integer codigo;
		private final String texto;

		private ENVIAR_RESERVA_PAQUETE_COSTAMAR(Integer codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
	}

	public  enum RESPUESTA_RESERVA_PAQUETE_COSTAMAR{
		SEGMENTO_CANCELADO(0,"Segmento cancelado"),		
		RESERVADO(1,"Reservado"),
		CONEXION_AMADEUS(2,"Conexión amadeus"),
		RESERVA_DUPLICADA(3,"Reserva duplicada"),
		PAGO_EXITOSO(4,"Reserva completada y pago exitoso con TC completado"),
		PAGO_DENEGADO(5,"Reserva completada y pago dengado con TC completado"),
		RESERVA_NO_REALIZADA(6,"No se pudo realizar la reserva");

		private final Integer codigo;
		private final String texto;

		private RESPUESTA_RESERVA_PAQUETE_COSTAMAR(Integer codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
	}
	
	public  enum RESPUESTA_RESERVA_VUELO_COSTAMAR{
		PENDIENTE (2,"pendiente"),
		SUCCESS (3,"success"), /*completado*/	
		DENIED (4,"denied"),
		CANCELED (8,"canceled"); //antes 5

		private final Integer codigo;
		private final String texto;

		private RESPUESTA_RESERVA_VUELO_COSTAMAR(Integer codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
	}
	
	public static enum PROMOCION_ERROR{
		EXCEPTION(0, "Ocurrio un error al guardar los datos"),
		YA_INSCRITO(-1,"Ya te encuentras inscrito en esta promocion"),
		NO_HABILITADO(-2,"Promocion no habilitada para inscripcion");

		private final Integer codigo;
		private final String mensaje;
		private PROMOCION_ERROR(Integer codigo, String mensaje) {
			this.codigo = codigo;
			this.mensaje = mensaje;
		}
		public Integer getCodigo() {
			return codigo;
		}
		public String getMensaje() {
			return mensaje;
		}

	}
//
//	public static enum TIPO_OBSERVACION{
//
//		PRODUCTO_MAL_ESTADO(1,2,"Pedido en mal estado"),
//		DATOS_INCORRECTOS(2,1,"Datos incorrectos del cliente"),		
//		NO_ENCONTRADO(3,1,"Cliente no encontrado"),
//		DEMORA_DELIVERY(4,2,"Pedido reprogramado");
//
//		private final int codigo;
//		private final Integer codigoTipoMotivo;
//		private final String mensaje;
//
//		private TIPO_OBSERVACION(int codigo, Integer codigoTipoMotivo,String mensaje) {
//			this.codigo = codigo;
//			this.mensaje = mensaje;
//			this.codigoTipoMotivo = codigoTipoMotivo;
//		}
//		public int getCodigo() {
//			return codigo;
//		}
//		public String getMensaje() {
//			return mensaje;
//		}
//		public Integer getCodigoTipoMotivo() {
//			return codigoTipoMotivo;
//		}
//
//		public static Integer obtenerIdTipoMotivo(Integer codigoObservacion){
//			UtilEnum2.TIPO_OBSERVACION[] valores = TIPO_OBSERVACION.values();
//			for (int i = 0; i < valores.length; i++) {
//				if(valores[i].getCodigo()==(codigoObservacion)){
//					return valores[i].getCodigoTipoMotivo();
//				}
//			}
//			return null;
//		}
//
//		public static String obtenerTextoMotivo(Integer codigoObservacion){
//			UtilEnum2.TIPO_OBSERVACION[] valores = TIPO_OBSERVACION.values();
//			for (int i = 0; i < valores.length; i++) {
//				if(valores[i].getCodigo()==(codigoObservacion)){
//					return valores[i].getMensaje();
//				}
//			}
//			return null;
//		}
//	}

	public  enum TIPO_SEGMENTACION{

		LINEAL(1,"Lineal"),
		MATRIZ(2,"Matriz");

		private final int codigo;
		private final String  texto;

		private TIPO_SEGMENTACION(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
	}

	public  enum ESTADO_CONSULTA{

		PENDIENTE(1,"Pendiente","Respuesta pendiente"),		
		RESPONDIDA(2,"Respondida","Respondida"),
		CERRADO(3,"Cerrado","Cerrado");

		private final Integer codigo;
		private final String  texto;
		private final String mensaje;

		private ESTADO_CONSULTA(Integer codigo, String texto, String mensaje){
			this.codigo = codigo;
			this.texto = texto;
			this.mensaje = mensaje;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}

		public String getMensaje() {
			return mensaje;
		}
		
		
		public static ESTADO_CONSULTA obtenerConsulta(Integer codigoPrioridad){
			UtilEnum2.ESTADO_CONSULTA[] valores = ESTADO_CONSULTA.values();
			for (int i = 0; i < valores.length; i++) {
				if(valores[i].getCodigo().equals(codigoPrioridad)){
					return valores[i];
				}
			}
			return null;
		}

	}
	
	
	

	public  enum PRIORIDAD_CONSULTA_RECLAMO{

		ALTA(1,"Alta"),
		MEDIA(2,"Media"),
		BAJA(3,"Baja");

		private final int codigo;
		private final String texto;

		PRIORIDAD_CONSULTA_RECLAMO(int codigo,String texto){
			this.codigo=codigo;
			this.texto=texto;
		}

		public int getCodigo() {
			return codigo;
		}
		public String getTexto() {
			return texto;
		}

		public static String obtenerTextoPrioridad(Integer codigoPrioridad){
			UtilEnum2.PRIORIDAD_CONSULTA_RECLAMO[] valores = PRIORIDAD_CONSULTA_RECLAMO.values();
			for (int i = 0; i < valores.length; i++) {
				if(valores[i].getCodigo()==(codigoPrioridad)){
					return valores[i].getTexto();
				}
			}
			return null;
		}

	}

	 /**
	  * Proyecto: SaasppCommon
	  * @date	: 5/8/2016
	  * @time	: 11:56:12
	  * @author	: Erick vb.
	  * @Description: Aplica a liuidacion y liquidacion detalle
	 */
	public  enum ESTADO_COBRO_LIQUIDACION{		
		PENDIENTE_CONFIRMACION(1,"Pendiente confirmacion", "Borrador"),
		NO_PAGADO(8,"No pagado","Aceptado"), //0 ERA
		PAGADO(11,"Pagado","Pagado"),//1 ERA
		OBSERVADO(2,"Observado","Observado"),//2 ERA
		ELIMINADO(0,"Eliminado","Eliminado");//3 ERA
		
		private final int codigo;
		private final String  texto;
		private final String filtro;
		
		private ESTADO_COBRO_LIQUIDACION(int codigo, String texto, String filtro){
			this.codigo = codigo;
			this.texto=texto;
			this.filtro = filtro;
		}
		public int getCodigo() {
			return codigo;
		}
		public String getTexto() {
			return texto;
		}
		public String getFiltro() {
			return filtro;
		}
		public static ESTADO_COBRO_LIQUIDACION getEstadoCobroLiquidacion(Integer codigo){
			
			UtilEnum2.ESTADO_COBRO_LIQUIDACION[] valores = ESTADO_COBRO_LIQUIDACION.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return valores[i];
				}
			}
			return null;
		}
		
	}

	public  enum TIPO_COMENTARIO_VENTA{		
		OBSERVACION(1,"Observar"),
		RESPUESTA(2,"Responder"),
		APROBAR(3,"Aprobar"); //0 ERA
		
		private final int codigo;
		private final String  texto;
		
		private TIPO_COMENTARIO_VENTA(int codigo, String texto){
			this.codigo = codigo;
			this.texto=texto;
		}
		
		public int getCodigo() {
			return codigo;
		}
		
		public String getTexto() {
			return texto;
		}	
		
		public static TIPO_COMENTARIO_VENTA getTipoComentario(Integer codigoComentario){
			UtilEnum2.TIPO_COMENTARIO_VENTA[] valores = TIPO_COMENTARIO_VENTA.values();
			for (int i = 0; i < valores.length; i++) {
				if(valores[i].getCodigo() == codigoComentario){
					return valores[i];
				}
			}
			return null;
		}
	}
	
	public  enum ESTADO_RESPUESTA{
		ABIERTO(1,"Abierto"),
		CERRADO(2,"Cerrado");

		private final Integer codigo;
		private final String  texto;
		private ESTADO_RESPUESTA(Integer codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}
		public Integer getCodigo() {
			return codigo;
		}
		public String getTexto() {
			return texto;
		}

		public static String obtenerNombreEstado(Integer id){
			String strCodigoReturn=null;
			UtilEnum2.ESTADO_RESPUESTA[] valores = ESTADO_RESPUESTA.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo().equals(id)){
					strCodigoReturn=valores[i].getTexto();
					break;
				}
			}
			return strCodigoReturn;
		}
		
		public static String getEstadoReclamoTexto(Integer estado){
			String descripcion =  null;
			for(UtilEnum2.ESTADO_RESPUESTA item : UtilEnum2.ESTADO_RESPUESTA.values() ){
				if(item.getCodigo().equals(estado)){
					descripcion = item.getTexto();
					break;
				}
				
			}
			return descripcion;
		}

	}

	public  enum MOTIVO_DEVOLUCION{

		PRODUCTO_MAL_ESTADO(1,"Producto en mal estado"),
		ERROR_DE_ENVIO(2,"Envio fuera de fecha"),
		CARACTERISTICAS_INCORRECTAS(3,"Caracteristicas incorrectas del producto"),
		PRODUCTO_INCORRECTO(4,"Producto equivocado");

		private final int codigo;
		private final String  texto;

		private MOTIVO_DEVOLUCION(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
				
	}


	public  enum TIPO_LIQUIDACION{
		PUNTOS_PLAZAPOINTS(1,"Puntos Plazapoints","PP","puntosPlazapoints",5),
		PUNTOS_COSTAMAR(2,"Puntos Costamar","Costamar","puntosCostamar",6),
		COMISIONES(3,"Comisiones","","comision",7), //solo se cobra por uso puntos de productos
		DEVOLUCIONES(4,"Devoluciones","","devolucion",8),
		FLETE_VALE(5,"Flete de vale","","fletevale",9),
		CUPONES_PLAZAPOINTS(6,"Cupones plazapoints","","cupon",11),
		CUPONES_COSTAMAR(7,"Cupones costamar","","cupon",12);

		private final int codigo;
		private final String texto;
		private final String abreviatura;
		private final String urlDetalle;
		private final Integer idReporte;

		private TIPO_LIQUIDACION(int codigo, String texto,String abreviatura,String urlDetalle, Integer idReporte){
			this.codigo = codigo;
			this.texto = texto;
			this.abreviatura = abreviatura;
			this.urlDetalle = urlDetalle;
			this.idReporte = idReporte;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public String getAbreviatura(){
			return abreviatura;
		}
		
		public String getUrlDetalle(){
			return urlDetalle;
		}
		
		public Integer getIdReporte(){
			return idReporte;
		}
		
		public static TIPO_LIQUIDACION getTipoLiquidacion(Integer codigoPrioridad){
			UtilEnum2.TIPO_LIQUIDACION[] valores = TIPO_LIQUIDACION.values();
			for (int i = 0; i < valores.length; i++) {
				if(valores[i].getCodigo() == codigoPrioridad){
					return valores[i];
				}
			}
			return null;
		}
	}	

	public  enum TIPO_IMAGEN{
		DESTACADO(1,"destacado"),
		FICHA_TECNICA(2,"ficha-tecnica"),
		GALERIA(3,"galeria"),
		OPORTUNIDAD(4,"oportunidad"),
		BACKGROUND(5, "background"),
		BANNER_EVENTO(6, "banner"),
		CATEGORIA_NODO(7, "nodo");
		


		private final int codigo;
		private final String carpeta;

		private TIPO_IMAGEN(int codigo, String carpeta){
			this.codigo = codigo;
			this.carpeta = carpeta;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getCarpeta() {
			return carpeta;
		}
		public static TIPO_IMAGEN getTipoImagen(Integer codigo){
			TIPO_IMAGEN[] valores = TIPO_IMAGEN.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return valores[i];
					
				}
			}
			return null;
		}
	}
	
	
	public  enum TIPO_IMAGEN_MARCA{
		SMALL(1,"small"),
		MEDIUM(2,"medium"),
		LARGE(3,"large");

		private final int codigo;
		private final String carpeta;

		private TIPO_IMAGEN_MARCA(int codigo, String carpeta){
			this.codigo = codigo;
			this.carpeta = carpeta;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getCarpeta() {
			return carpeta;
		}
		public static TIPO_IMAGEN_MARCA getTipoImagen(Integer codigo){
			TIPO_IMAGEN_MARCA[] valores = TIPO_IMAGEN_MARCA.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return valores[i];
					
				}
			}
			return null;
		}
	}

	public static String getTipoLiquidacion(String codigo){
		String retorno = null;
		UtilEnum2.TIPO_LIQUIDACION[] valores = TIPO_LIQUIDACION.values();
		for(int i=0; i<valores.length; i++){
			if(valores[i].getCodigo()==Integer.parseInt(codigo)){
				retorno=valores[i].getTexto();
				break;
			}
		}
		return retorno;
	}

	



	public static enum CATEGORIA_ENTRETENIMIENTO{
		CINE(1,"cines","Cines",TIPO_BENEFICIO.CATALAGO.getCodigo()),// jala de productos
		RESTAURANTES(2,"restaurantes","Restaurantes",TIPO_BENEFICIO.DESCUENTO.getCodigo()),// jala de descuento
		CONCIERTOS(3,"conciertos-y-espectaculos","Conciertos y Espectaculos",TIPO_BENEFICIO.CATALAGO.getCodigo()),// jala de productos
		EXPERIENCIAS(4,"experiencias","Experiencias",TIPO_BENEFICIO.DESCUENTO.getCodigo());// jala de descuento

		private final int id;
		private final String codigo;
		private final String etiqueta;
		private final int tipoBenficio;

		private CATEGORIA_ENTRETENIMIENTO(int id, String codigo, String etiqueta, int tipoBeneficio){
			this.id = id;
			this.codigo = codigo;
			this.etiqueta = etiqueta;
			this.tipoBenficio = tipoBeneficio;
		}

		public int getId() {
			return id;
		}
		public String getCodigo() {
			return codigo;
		}
		public String getEtiqueta() {
			return etiqueta;
		}
		public int getTipoBeneficio() {
			return tipoBenficio;
		}
		
		public static CATEGORIA_ENTRETENIMIENTO getCategoriaXCodigo(String codigoCategoria){
			UtilEnum2.CATEGORIA_ENTRETENIMIENTO[] valores = CATEGORIA_ENTRETENIMIENTO.values();
			for (int i = 0; i < valores.length; i++) {
				if(valores[i].getCodigo().equalsIgnoreCase(codigoCategoria)){
					return valores[i];
				}
			}
			return null;
		}
		
	}
	
public static enum PAQUETE_ORDERBY {
		
//		<option value="0">Ordenar por</option>
//        <option value="1">Precio más bajo</option>
//        <option value="2">Nacional</option>
//        <option value="3">Internacional</option>
		
		DESTACADOS(0, "Destacados"),
		MENOR_PRECIO(1, "Menor Precio"),
		NACIONAL(2, "Nacional"),
		INTERNACIONAL(3,"Internacional");

		private final int codigo;
		private final String texto;

		private PAQUETE_ORDERBY(int codigo, String texto) {
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static PAQUETE_ORDERBY getPaqueteOrdenByID(Integer id){
			PAQUETE_ORDERBY[] valores = PAQUETE_ORDERBY.values();
		
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo() == id ){
					return  valores[i];
				}
			}
			return null;
		}
	}

	public static enum TIPO_OPERACION_RESERVA_PRODUCTO {
		RESERVAR(1, "reservar"), LIBERAR(2, "liberar");
		
		private final int		codigo;
		private final String	carpeta;
		
		private TIPO_OPERACION_RESERVA_PRODUCTO(int codigo, String carpeta) {
			this.codigo = codigo;
			this.carpeta = carpeta;
		}
		
		public int getCodigo() {
			return codigo;
		}
		
		public String getCarpeta() {
			return carpeta;
		}
}
	
	public static enum ACCION_ACTUALIZAR_STOCK_PAQUETE{
		RESTAR(0),
		RESTAURAR(1);
		
		private final int codigo;
	
		private ACCION_ACTUALIZAR_STOCK_PAQUETE(int estado){
			this.codigo = estado;
		}
		public int getCodigo() {
			return codigo;
		}
	}
	
	public static enum PERMISO_S3{
		PUBLICO(0),
		PRIVADO(1);
		
		private final int codigo;
	
		private PERMISO_S3(int estado){
			this.codigo = estado;
		}
		public int getCodigo() {
			return codigo;
		}
	}
	
	public static enum TIPO_SINCRONIZACION_STOCK{
		SYNCHRONOUS(0),
		ASYNCHRONOUS(1);
		
		private final int codigo;
	
		private TIPO_SINCRONIZACION_STOCK(int estado){
			this.codigo = estado;
		}
		public int getCodigo() {
			return codigo;
		}
	}
	
	public static enum TIPO_SFC{
		CATALOGO_PRODUCTO("CATP"),
		DESCUENTO("DSCT"),
		DELIVERY_ZONA("DLZN"),
		DELIVERY_ZONA_EXPRESS("DLZE");
		
		private final String tipo;
		
		private TIPO_SFC(String tipo){
			this.tipo=tipo;
		}

		public String getTipo() {
			return tipo;
		}
	}
	
	public static enum TIPO_PRODUCTO{
		PRODUCTO(1,"PRODUCTO"),
		VALE(2,"VALE");

		private final int codigo;
		private final String texto;

		private TIPO_PRODUCTO(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		public static TIPO_PRODUCTO obtenerTipoProductoByTexto(String texto){
			TIPO_PRODUCTO[] valores = TIPO_PRODUCTO.values();
			TIPO_PRODUCTO tipoProducto=null;
			for(int i=0; i<valores.length; i++){
				if(valores[i].getTexto().equalsIgnoreCase(texto)){
					tipoProducto =  valores[i];
				}
			}
			return tipoProducto;
		}
		public static TIPO_PRODUCTO obtenerTipoProducto(int codigo){
			TIPO_PRODUCTO[] valores = TIPO_PRODUCTO.values();
			TIPO_PRODUCTO tipoProducto=null;
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					tipoProducto =  valores[i];
				}
			}
			return tipoProducto;
		}
	}
	
	
	public static enum TIPO_PESO{
		VOLUMETRICO(1,"PV","PESO VOLUMEN"),
		LINEAL(2,"PK","PESO KILO");
		
		private final Integer codigo;
		private final String texto;
		private final String descripcion;
		
		private TIPO_PESO(Integer codigo, String texto,String descripcion){
			this.codigo = codigo;
			this.texto = texto;
			this.descripcion = descripcion;
		}
		
		public Integer getCodigo() {
			return codigo;
		}
		
		public String getTexto() {
			return texto;
		}
		
		public String getDescripcion() {
			return descripcion;
		}
		public static TIPO_PESO obtenerTipoPesoByTexto(String texto){
			TIPO_PESO[] valores = TIPO_PESO.values();
			TIPO_PESO tipoPesos=null;
			for(int i=0; i<valores.length; i++){
				if(valores[i].getTexto().equalsIgnoreCase(texto)){
					tipoPesos =  valores[i];
				}
			}
			return tipoPesos;
		}
		public static TIPO_PESO obtenerTipoPeso(Integer codigo){
			TIPO_PESO[] valores = TIPO_PESO.values();
			TIPO_PESO tipoPesos=null;
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo().intValue() == codigo){
					tipoPesos =  valores[i];
				}
			}
			return tipoPesos;
		}
	}
	
	
	public static enum TIPO_VENTA_PRODUCTO{
		SUBVENCIONADO(1,"SUBV","SUBVENCIONADO"),
		NO_SUBVENCIONADO(2,"NSUBV","NO SUBVENCIONADO");
		
		private final Integer codigo;
		private final String texto;
		private final String descripcion;
		
		private TIPO_VENTA_PRODUCTO(Integer codigo, String texto,String descripcion){
			this.codigo = codigo;
			this.texto = texto;
			this.descripcion = descripcion;
		}
		
		public Integer getCodigo() {
			return codigo;
		}
		
		public String getTexto() {
			return texto;
		}
		
		public String getDescripcion() {
			return descripcion;
		}

		public static TIPO_VENTA_PRODUCTO obtenerTipoVentaProductoByTexto(String texto){
			TIPO_VENTA_PRODUCTO[] valores = TIPO_VENTA_PRODUCTO.values();
			TIPO_VENTA_PRODUCTO tipoVentaProducto=null;
			for(int i=0; i<valores.length; i++){
				if(valores[i].getTexto().equalsIgnoreCase(texto)){
					tipoVentaProducto =  valores[i];
				}
			}
			return tipoVentaProducto;
		}
		public static TIPO_VENTA_PRODUCTO obtenerTipoVentaProducto(Integer codigo){
			TIPO_VENTA_PRODUCTO[] valores = TIPO_VENTA_PRODUCTO.values();
			TIPO_VENTA_PRODUCTO tipoVentaProducto=null;
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo().intValue()==codigo){
					tipoVentaProducto =  valores[i];
				}
			}
			return tipoVentaProducto;
		}
	}
	
	
	public static enum AMBIENTE_DEPLOY{
		DESARROLLO(1,"desarrollo"),
		SITE(2,"site"),
		UAT(3,"uat"),
		PRODUCCION(4,"produccion");
		
		private final Integer codigo;
		private final String tipo;
		
		private AMBIENTE_DEPLOY(Integer codigo, String tipo){
			this.codigo=codigo;
			this.tipo=tipo;
		}
		
		public Integer getCodigo() {
			return codigo;
		}

		public String getTipo() {
			return tipo;
		}
		
		public static Integer obtenerCodigoByTipo(String tipo){
			AMBIENTE_DEPLOY[] ambientes = AMBIENTE_DEPLOY.values();
			Integer codigo=0;
			for(int i=0; i<ambientes.length; i++){
				if(ambientes[i].getTipo().equalsIgnoreCase(tipo)){
					codigo =  ambientes[i].getCodigo();
				}
			}
			return codigo;
		}
	}
	
	public static enum TIPO_ACCION_LOGIN{
		LOGIN_OK(1,"LOGIN OK"),
		LOGOUT(2,"LOGOUT"),
		INTENTO_FALLIDO(3,"INTENTO FALLIDO"),
		BLOQUEADO(4,"LOGIN BLOQUEADO");
		
		private final Integer codigo;
		private final String texto;
		
		private TIPO_ACCION_LOGIN(Integer codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}
		
		public Integer getCodigo() {
			return codigo;
		}
		
		public String getTexto() {
			return texto;
		}
		
	}
	
	 /**
	  * Proyecto: SaasppCommon
	  * @date	: 20/6/2016
	  * @time	: 19:04:52
	  * @author	: Erick vb.
	  * @description: solo describe los no entrega en netsuite, 
	  * para los demas estados llamar a la entidad pedido estado observacion
	 */
	public static enum MOTIVO_NO_ENTREGA{

		DESTINATARIO_AUSENTE(1,"DESTINATARIO_AUSENTE","El destinatario no se encuentra en la dirección consignada","msg.motivo.ausente"),
		DIRECCION_INCOMPLETA(2,"DIRECCION_INCOMPLETA","La dirección proporcionada por el cliente es incompleta","msg.motivo.dir.incompleto"),
		DIRECCION_NO_EXISTE(3,"DIRECCION_NO_EXISTE","La dirección no se encuentra", "msg.motivo.dir.noexiste"),
		DIRECCION_DESCONOCIDA(4,"DIRECCION_DESCONOCIDA","La dirección es desconocida","msg.motivo.dir.desconicida"),
		DIRECCION_INACCESIBLE(5,"DIRECCION_INACCESIBLE","No se puede ingresar en la dirección proporcionada","msg.motivo.dir.inacccesible"),
		DESTINATARIO_NO_ACEPTA_PRODUCTO(6,"DESTINATARIO_NO_ACEPTA_PRODUCTO","El destinatario se niega a aceptar el producto","msg.motivo.destino.noacepta"),
		SE_MUDO_DE_CASA(7,"SE_MUDO_DE_CASA","El destinatario se mudó de casa","msg.motivo.mudo.casa"),
		SIN_DOCUMENTOS_PARA_RECEPCION(8,"SIN_DOCUMENTOS_PARA_RECEPCION","El destinatario no posee documentos para la recepción del producto","msg.motivo.sindocumentos"),
		ERROR_EN_PEDIDO(9,"ERROR_EN_PEDIDO","Error en el pedido","msg.motivo.error.pedido"),
		EN_RUTA(10,"EN_RUTA","En ruta","msg.motivo.enruta"),
		OTRO(11,"OTRO","Otro","msg.motivo.otro");

		private final int codigo;
		private final String  texto;
		private final String  descripcion;
		private final String keyMsg;

		private MOTIVO_NO_ENTREGA(int codigo, String texto, String descripcion, String keyMsg){
			this.codigo = codigo;
			this.texto = texto;
			this.descripcion = descripcion;
			this.keyMsg = keyMsg;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}

		public String getDescripcion() {
			return descripcion;
		}
		
		public static MOTIVO_NO_ENTREGA obtenerMotivoEntrega(Integer codigo){
			MOTIVO_NO_ENTREGA[] valores = MOTIVO_NO_ENTREGA.values();
			MOTIVO_NO_ENTREGA motivo=null;
			
			if(codigo==null){return null;}
			
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo() == codigo ){
					motivo =  valores[i];
				}
			}
			return motivo;
		}

		public String getKeyMsg() {
			return keyMsg;
		}
		
	}
	
	public static enum ESTADO_TRACKING_GUIA_REMISION_NETSUITE{
		
		GUIADO(2,"Guiado"), //en ruta -> antes VISITA_1(2,"Visita 1")
		VISITA(3,"Visita"), //en ruta, despues de reprogramado -> antes VISITA_2(3,"Visita 2")
		VISITA_3(4,"Visita 3"), //en ruta, despues de reprogramado//yanoseusa 02052018
		VISITA_4(9,"Visita 4"), //en ruta, despues de reprogramado//yanoseusa 02052018
		DEVOLUCION_CURRIER(5,"Devolucion currier"), //Proximo a eliminar, solo en provincia (ANULADO)
		EN_CONSULTA(6,"En consulta"), //no entregado prox a visita 2..3  con ejecutivo comercial segundo (REPROGRAMADO)
		RECOORDINACION(8,"Recoordinacion"),//no entregado prox a visita 2..3 call center primero en reprogramacion (REPROGRAMADO)
		NO_ENTREGADO(10,"No Entregado"),
		REPROGRAMADO(11,"Reprogramado"),
		ENTREGADO_COURIER(7,"Entregado al courier"), //entregado por el currier, solo en provincia (en ruta)
		ENTREGADO(1,"Entregado");// (entregado)

		private final Integer codigo;
		private final String texto;

		private ESTADO_TRACKING_GUIA_REMISION_NETSUITE(Integer codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
	}
	
	public static enum ESTADO_GUIA_REMISION_NETSUITE{
		
		ENVIADO("_shipped","Recordinando en Call Center"),
		RECOGIDO("_picked","Entregado al courier"),
		EMBALADO("_packed","Entregado");

		private final String codigo;
		private final String texto;

		private ESTADO_GUIA_REMISION_NETSUITE(String codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public String getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
	}
	
	public static enum ESTADO_PEDIDO_NETSUITE{
		
		PENDIENTE_APROBACION("_pendingApproval","Aprobación pendiente"),
		APROBADO("_pendingFulfillment","Ejecución del pedido pendiente"),
		GUIA_REMISION_PARCIAL("_pendingBillingPartFulfilled","Facturación pendiente/parcialmente ejecutada"),
		GUIA_REMISION_TOTAL("_pendingBilling","Facturación pendiente"),
		GUIA_REMISION_TOTAL_FACTURADO("_fullyBilled","Facturado"),
		ANULADO("_cancelled","Cancelado"),
		ENTREGADO("_closed","Entregado"),
		CERRADO("_closed","Cerrado");
		
		private final String codigo;
		private final String texto;
		
		private ESTADO_PEDIDO_NETSUITE(String codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}
		
		public String getCodigo() {
			return codigo;
		}
		
		public String getTexto() {
			return texto;
		}
		
	}
	
	 /**
	  * @descripcion	: Enum para mostrar en pantalla
	 */
	public static enum CATALAGO_ESTADO_PEDIDO {

		REGISTRADO(0, "Pedido registrado", "msg.estado.pedido.registrado", 0,"Registrado"),

		RECIBIDO(1, "Pedido recibido por NetSuite", "msg.estado.pedido.recibido", 1,"Pedido Recibido"),

		DESPACHO(3, "Pedido despachado por NetSuite", "msg.estado.pedido.despachado", 2,"Almacen"),

		RUTA(4, "Pedido en ruta por NetSuite", "msg.estado.pedido.enruta", 3,"En ruta"),

		NO_ENTREGADO(7, "Pedido no entregado por NetSuite", "msg.estado.pedido.noentregado", 4,"No entregado"),

		DERIVADO_A_BANCO(6, "Pedido derivado a banco por NetSuite", "msg.estado.pedido.derivado.banco", 5,"Derivado a banco"),

		REPROGRAMADO(8, "Pedido reprogramado 1 por NetSuite", "msg.estado.pedido.reprogramado1", 6,"Reprogramado 1"),

		REPROGRAMADO_1(11, "Pedido reprogramado 1 por NetSuite", "msg.estado.pedido.reprogramado1", 7,"Reprogramado 1"),//deprecated

		REPROGRAMADO_2(12, "Pedido reprogramado 2 por NetSuite", "msg.estado.pedido.reprogramado2", 8,"Reprogramado 2"),//deprecated

		REPROGRAMADO_3(13, "Pedido reprogramado 3 por NetSuite", "msg.estado.pedido.reprogramado3",	9,"Reprogramado 3"),//deprecated

		ANULADO(21, "Pedido anulado por NetSuite", "msg.estado.pedido.anulado", 10,"Anulado"),

		ENTREGADO(22, "Pedido entregado por NetSuite", "msg.estado.pedido.entregado", 11,"Entregado");

		private CATALAGO_ESTADO_PEDIDO(int id, String texto, String keyMsg,
				int ordenDefault, String shortLabel) {
			this.id = id;
			this.texto = texto;
			this.keyMsg = keyMsg;
			this.ordenDefault = ordenDefault;
			this.shortLabel = shortLabel;
			
		}

		private final int id;
		private final String texto;
		private final String keyMsg;
		private final int ordenDefault;
		private final String shortLabel;
		public int getId() {
			return id;
		}

		public String getTexto() {
			return texto;
		}

		public String getKeyMsg() {
			return keyMsg;
		}

		public int getOrdenDefault() {
			return ordenDefault;
		}
		public String getShortLabel() {
			return shortLabel;
		}
	public static Integer getIdByTexto(String texto){
		Integer strCodigoReturn=null;
		UtilEnum2.CATALAGO_ESTADO_PEDIDO[] valores = CATALAGO_ESTADO_PEDIDO.values();
		for(int i=0; i<valores.length; i++){
			if(valores[i].getTexto().equalsIgnoreCase(texto)){
				strCodigoReturn=valores[i].getId();
				break;
			}
		}
		return strCodigoReturn;
	}

	public static CATALAGO_ESTADO_PEDIDO getEstadoPedidoByID(Integer id){
		UtilEnum2.CATALAGO_ESTADO_PEDIDO[] valores = CATALAGO_ESTADO_PEDIDO.values();
		for(int i=0; i<valores.length; i++){
			if(valores[i].getId() == id){
				return  valores[i];
			}
		}
		return null;
	}
	
	public static String getTextoPedidoByID(Integer id){
		UtilEnum2.CATALAGO_ESTADO_PEDIDO[] valores = CATALAGO_ESTADO_PEDIDO.values();
		for(int i=0; i<valores.length; i++){
			if(valores[i].getId() == id){
				return  valores[i].getTexto();
			}
		}
		return null;
	}

	

	}
	
	public static enum TIPO_PROCESO{
		
		EMAIL_CLIENTE(1,"Enviar email al cliente"),
		CONFIRMAR_PAGO(2,"Confirmar Pago");
		
		private final Integer codigo;
		private final String tipo;
		
		private TIPO_PROCESO(Integer codigo, String tipo) {
			this.codigo = codigo;
			this.tipo = tipo;
		}

		public String getTipo() {
			return tipo;
		}

		public Integer getCodigo() {
			return codigo;
		}
		
	}
	
	public static enum ORIGEN_REGISTRO_PEDIDO_ESTADO{
		
		NETSUITE_BACK(1,"Proceso sincronizacion Netsuite"),
		ADMIN_RECORDINAR(2,"Proceso recordinar entrega administrador");
		
		private final int codigo;
		private final String texto;
		
		private ORIGEN_REGISTRO_PEDIDO_ESTADO(int codigo, String texto) {
			this.codigo = codigo;
			this.texto = texto;
		}

		public String getTexto() {
			return texto;
		}

		public Integer getCodigo() {
			return codigo;
		}
		
	}
	
	public static enum TIPO_ANCHO{
		DEFECTO(1,"1400px"),
		COMPLETO(2,"100%");
		
		private final Integer codigo;
		private final String descripcion;
		
		private TIPO_ANCHO(Integer codigo,String descripcion){
			this.codigo = codigo;
			this.descripcion = descripcion;
		}
		
		public Integer getCodigo() {
			return codigo;
		}
		
		public String getDescripcion() {
			return descripcion;
		}
		public static TIPO_ANCHO obtenerTipoAnchoByDescripcion(String descripcion){
			TIPO_ANCHO[] valores = TIPO_ANCHO.values();
			TIPO_ANCHO tipoAnchos=null;
			for(int i=0; i<valores.length; i++){
				if(valores[i].getDescripcion().equalsIgnoreCase(descripcion)){
					tipoAnchos =  valores[i];
				}
			}
			return tipoAnchos;
		}
	}
	
	public static enum PROCESO_COLA{
		ANULAR_VENTA_LIBERAR_STOCK("COLA-LIBERAR-STOCK"),
		ACTUALIZAR_ESTADO_PERIODO("COLA-ACTUALIZAR-ESTADO-PERIODO"),
		DESBLOQUEAR_USER_ADMIN("COLA-DESBLOQUEAR-USER-ADMIN"),
		SINCRONIZAR_CODIGO_SFC_DELIVERY("COLA-SINCRONIZAR-CODIGO-SFC-DELIVERY"),
		GENERAR_DEVOLUCION_VENTA_PENDIENTE("COLA-GENERAR-DEVOLUCION-VENTA"),
		NOTIFICACION_PEDIDO_NO_REGISTRADO("COLA-NOTIFICACION-PEDIDO-NO-REGISTRADO"),
		INACTIVAR_USUARIOS("COLA-INACTIVAR-USUARIOS"),
		NOTIFICACION_EVALE_NO_CONFIRMADO("COLA-NOTIFICACION-EVALE-NO-CONFIRMADO"),
		SINCRONIZAR_CODIGO_SFC_CATALOGO("COLA-SINCRONIZAR-CODIGO-SFC-CATALOGO"),
		SINCRONIZAR_MIGRACION_JOINNUS("COLA-SINCRONIZAR-MIGRACION-JOINNUS")
		;
		
		private final String nombre;
		
		private PROCESO_COLA(String nombre){
			this.nombre = nombre;
			
		}

		public String getNombre() {
			return nombre;
		}
		
	}
	
	public static enum REPORTE_COLA{
		LIQUIDACION_DELIVERY(0,"COLA-REPORTES-PROCESAR-EXCEL","REPORTE LIQUIDACIONES DELIVERY","Liquidacion_Delivery_"),
		ENTREGAS_POR_FECHA(1,"COLA-REPORTES-PROCESAR-EXCEL","REPORTE TRACKING ENTREGADOS","Reporte_Tracking_Entregados_"),
		REPORTE_TRACKING(2,"COLA-REPORTES-PROCESAR-EXCEL","REPORTE TRACKING","Reporte_Tracking_Entregados_"),
		TIEMPO_ENTREGA(3,"COLA-REPORTES-PROCESAR-EXCEL","REPORTE TRACKING TIEMPOS DE ENTREGA","Reporte_Tracking_Tiempo_Espera_"),
		MOTIVOS_NO_ENTREGA(4,"COLA-REPORTES-PROCESAR-EXCEL","REPORTE TRACKING MOTIVOS NO ENTREGA","Reporte-Tracking-Motivos-No-Entrega-"),
		
		PUNTOS_PLAZAPOINTS(5,"COLA-REPORTES-PROCESAR-EXCEL","LIQUIDACIONES PUNTOS PLAZAPOINTS","Liquidacion_Puntos_Plazapoints_"),
		PUNTOS_COSTAMAR(6,"COLA-REPORTES-PROCESAR-EXCEL","LIQUIDACIONES PUNTOS COSTAMAR","Liquidacion_Puntos_Costamar_"),
		COMISION_PLAZAPOINTS(7,"COLA-REPORTES-PROCESAR-EXCEL","LIQUIDACIONES COMISION PLAZAPOINTS","Liquidacion_Comision_Plazapoints_"),
		DEVOLUCIONES(8,"COLA-REPORTES-PROCESAR-EXCEL","LIQUIDACIONES DEVOLUCIONES","Liquidacion_Devoluciones_"),
		FLETES_VALES(9,"COLA-REPORTES-PROCESAR-EXCEL","LIQUIDACIONES FLETES DE VALES","Liquidacion_Fletes_Vales_"),
		CUPONES_PLAZAPOINTS(11,"COLA-REPORTES-PROCESAR-EXCEL","LIQUIDACIONES CUPONES PLAZAPOINTS","Liquidacion-cupones-plazapoints-"),
		CUPONES_COSTAMAR(12,"COLA-REPORTES-PROCESAR-EXCEL","LIQUIDACIONES CUPONES COSTAMAR","Liquidacion-cupones-costamar-");
		
		private final int idReporte;
		private final String nombreCola;
		private final String titulo;
		private final String fichero;
		
		private REPORTE_COLA(int idReporte,String nombreCola,String titulo,String fichero){
			this.idReporte = idReporte;
			this.nombreCola = nombreCola;
			this.titulo = titulo;
			this.fichero = fichero;
			
		}
		
		public int getIdReporte() {
			return idReporte;
		}

		public String getNombreCola() {
			return nombreCola;
		}
		
		public String getTitulo() {
			return titulo;
		}
		
//		public String getFichero() {
////			return fichero+UDate.toDateUrl(new Date());
//		}
		
		public static REPORTE_COLA getReporteXId(int idReporte){
			UtilEnum2.REPORTE_COLA[] valores = REPORTE_COLA.values();
			for (int i = 0; i < valores.length; i++) {
				if(valores[i].getIdReporte() == idReporte){
					return valores[i];
				}
			}
			return null;
		}
		
	}
		
	public static enum DIA_SEMANA{
		
		DOMINGO(1),
		LUNES(2),
		MARTES(3),
		MIERCOLES(4),
		JUEVES(5),
		VIERNES(6),
		SABADO(7);
		
		private final int codigo;
		
		private DIA_SEMANA(int codigo) {
			this.codigo = codigo;
		}

		public Integer getCodigo() {
			return codigo;
		}
		
	}
	
	//VUELOS 
	public static enum TIPO_VENTA_PASAJERO{
		CONTACTO(0,"contacto"),
		INFANTE(1,"inf"),
		NINO(2,"chd"),
		ADULTO(3,"adt"),
		DESCONOCIDO(4,"desconocido");
		
		private final int codigo;
		private final String nombre;
		
		private TIPO_VENTA_PASAJERO(int codigo, String nombre){
			this.codigo = codigo;
			this.nombre = nombre;
		}
		
		public Integer getCodigo() {
			return codigo;
		}
		
		public String getNombre() {
			return nombre;
		}
		
		public static TIPO_VENTA_PASAJERO obtenerTipoAnchoByDescripcion(String nombre){
			TIPO_VENTA_PASAJERO[] valores = TIPO_VENTA_PASAJERO.values();
			TIPO_VENTA_PASAJERO tipoPasajero=null;
			for(int i=0; i<valores.length; i++){
				if(valores[i].getNombre().equalsIgnoreCase(nombre)){
					tipoPasajero =  valores[i];
				}
			}
			
			if(null == tipoPasajero){
				tipoPasajero = TIPO_VENTA_PASAJERO.DESCONOCIDO;
			}
			
			return tipoPasajero;
		}
	}
	
	public static enum TIPO_HABITACION{
		DOBLE(1),
		MATRIMONIAL(2);
		
		private final Integer codigo;
		
		private TIPO_HABITACION(Integer codigo){
			this.codigo = codigo;
		}
		
		public Integer getCodigo() {
			return codigo;
		}
		
	}
	
	public static enum TIPO_CANJE{
		PUNTOS(1,"Puntos"),
		DINERO(2,"Dinero"),
		MIXTO(3,"Puntos + Dinero");		
		
		private final int codigo;
		private final String nombre;
		
		private TIPO_CANJE(int codigo, String nombre){
			this.codigo = codigo;
			this.nombre = nombre;
		}
		
		public Integer getCodigo() {
			return codigo;
		}
		
		public String getNombre() {
			return nombre;
		}
	}
	
	public static enum TIPO_CONCILIACION{
		CONCILIACION_PLAZAPOINTS(1,"plazaPoints"),
		CONCILIACION_COSTAMAR(2,"costamar");

		private final int codigo;
		private final String texto;

		private TIPO_CONCILIACION(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static TIPO_CONCILIACION getTipoConciliacion(String codigoPrioridad){
			UtilEnum2.TIPO_CONCILIACION[] valores = TIPO_CONCILIACION.values();
			for (int i = 0; i < valores.length; i++) {
				if(valores[i].getTexto().equals(codigoPrioridad)){
					return valores[i];
				}
			}
			return null;
		}
	}
	
	public static enum TIPO_TX_REFERENCIA{
		NINGUNO(0,"NINGUNO"),
		ID_VENTA(1,"ID VENTA"),
		CODIGO_PNR(2,"CODIGO PNR"),
		CODIGO_SFC(3,"CODIGO SFC"),
		CODIGO_BIM_CLIENTE(4,"CODIGO BIM CLIENTE"),
		VUELO_ORIGEN_DESTINO(5,"VUELO ORIGEN DESTINO"),
		ID_PAQUETE(6,"ID PAQUETE");

		private final int codigo;
		private final String texto;

		private TIPO_TX_REFERENCIA(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static TIPO_TX_REFERENCIA getTipoTxReferencia(String codigoPrioridad){
			UtilEnum2.TIPO_TX_REFERENCIA[] valores = TIPO_TX_REFERENCIA.values();
			for (int i = 0; i < valores.length; i++) {
				if(valores[i].getTexto().equals(codigoPrioridad)){
					return valores[i];
				}
			}
			return null;
		}
	}
	
	public static enum ESTADO_PARA_LIQUIDAR{
		TODOS(0,"Todos",0,""),
		POR_LIQUIDAR(1,"Por Liquidar",1,"Observar"),
		OBSERVADAS_IBK(2,"Observadas IBK",2,"Responder"),
		RESPONDIDAS_CLIENTES(3,"Respondidas",3,"Aprobar"),
		CERRADAS(4,"Cerradas",1,"Observar"),
		PENDIENTES(5,"Pendientes",1,"Observar");

		private final int codigo;
		private final String descripcion;
		private final int codigoBoton;
		private final String descripcionBoton;
		
		private ESTADO_PARA_LIQUIDAR(int codigo, String descripcion,int codigoBoton, String descripcionBoton){
			this.codigo = codigo;
			this.descripcion = descripcion;
			this.codigoBoton = codigoBoton;
			this.descripcionBoton = descripcionBoton;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getDescripcion() {
			return descripcion;
		}
		
		public int getCodigoBoton() {
			return codigoBoton;
		}
		
		public String getDescripcionBoton() {
			return descripcionBoton;
		}
		
		public static ESTADO_PARA_LIQUIDAR getDescripcionTipoEstado(int codigoEstado){
			ESTADO_PARA_LIQUIDAR[] valores = ESTADO_PARA_LIQUIDAR.values();
			for (int i = 0; i < valores.length; i++) {
				if(valores[i].getCodigo() == codigoEstado){
					return valores[i];
				}
			}
			return null;
		}
		
		public static ESTADO_PARA_LIQUIDAR getSiguienteEstadoPorCodigo(int codigoEstado){
			ESTADO_PARA_LIQUIDAR[] valores = ESTADO_PARA_LIQUIDAR.values();
			for (int i = 0; i < valores.length; i++) {
				if(valores[i].getCodigo() == 4){
					return valores[i];
				}else if(valores[i].getCodigo() == codigoEstado){
					return valores[i+1];
				}
			}
			return null;
		}
		
		public static ESTADO_PARA_LIQUIDAR getAnteriorEstadoPorCodigo(int codigoEstado){
			ESTADO_PARA_LIQUIDAR[] valores = ESTADO_PARA_LIQUIDAR.values();
			for (int i = 0; i < valores.length; i++) {
				if(valores[i].getCodigo() == 4){
					return valores[i];
				}else if(valores[i].getCodigo() == codigoEstado){
					return valores[i-1];
				}
			}
			return null;
		}
	}
	
	public static enum TIPO_MOTIVO_LIQUIDACION{
		SIN_MOTIVO(0,"Seleccione"),
		MOTIVO_1(1,"No coincide el monto"),
		MOTIVO_2(2,"El producto fue cancelado o devuelto"),
		MOTIVO_3(3,"Se pagó en una liquidación anterior");

		private final int codigo;
		private final String descripcion;
		
		private TIPO_MOTIVO_LIQUIDACION(int codigo,String descripcion){
			this.codigo = codigo;
			this.descripcion = descripcion;
		}
		
		public int getCodigo() {
			return codigo;
		}

		public String getDescripcion() {
			return descripcion;
		}
		
		public static TIPO_MOTIVO_LIQUIDACION getMotivoPorCodigo(Integer codigo){
			UtilEnum2.TIPO_MOTIVO_LIQUIDACION[] valores = TIPO_MOTIVO_LIQUIDACION.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo() == codigo){
					return  valores[i];
				}
			}
			return null;
		}
	}	
	
	
	
	public static enum TIPO_EVENTO{
		CATALOGO(1,"CATALOGO"),
		DESCUENTO(2,"DESCUENTO"),
		VUELO(3,"VUELOS"),
		PAQUETE(4,"PAQUETE"),
		CATEGORIA(11,"CATEGORIA"),
		ESTADO_CUENTA_CLIENTE(12,"ESTADO CUENTA CLIENTE"),
		MENSAJE_BIENVENIDA(21,"MENSAJE BIENVENIDA"),
		BANNER_HOME(22,"BANNER HOME"),
		HEADER(23,"HEADER"),
		FOOTER(24,"FOOTER"),
		CATEGORIA_ARBOL(25,"FOOTER");

		private final Integer codigo;
		private final String texto;

		private TIPO_EVENTO(Integer codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}
		
		public static TIPO_EVENTO getIdEvento(Integer codigo){
			UtilEnum2.TIPO_EVENTO[] valores = TIPO_EVENTO.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return  valores[i];
				}
			}
			return null;
		}		

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}

	}
	
	public static enum CODIGO_TIPO_CATEGORIA{
		CATEGORIA_ARBOL(0,"Categoría Arbol","C", "categoria"),
		AGRUPADOR(0,"Agrupador","A", "agrupador"),
		STATIC_DESTACADO(-1,"Destacado","SD", "destacados"),
		STATIC_MALLA(-2,"Malla","SM", "todos"),
		STATIC_BUSCADOR(-3,"Buscador","SB", "busqueda");

		private final int codigo;
		private final String nombre;
		private final String alias;
		private final String codigoReferencia;

		private CODIGO_TIPO_CATEGORIA(int codigo,String nombre, String alias, String codigoReferencia){
			this.codigo = codigo;
			this.nombre = nombre;
			this.alias = alias;
			this.codigoReferencia = codigoReferencia;
		}
		
		public static CODIGO_TIPO_CATEGORIA getCodigoTipoCategoria(String alias){
			UtilEnum2.CODIGO_TIPO_CATEGORIA[] valores = CODIGO_TIPO_CATEGORIA.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getAlias().equals(alias)){
					return  valores[i];
				}
			}
			return null;
		}
		
		public static CODIGO_TIPO_CATEGORIA getTipoCategoria(String codigo){
			UtilEnum2.CODIGO_TIPO_CATEGORIA[] valores = CODIGO_TIPO_CATEGORIA.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigoReferencia().equals(codigo)){
					return  valores[i];
				}
			}
			return null;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getNombre() {
			return nombre;
		}
		
		public String getAlias() {
			return alias;
		}
		
		public String getCodigoReferencia() {
			return codigoReferencia;
		}
	}	
	
	public static enum OPERACION_VUELO_ASINCRONO{
		BUSQUEDA(1,"BUSQUEDA"),
		RESERVA(2,"RESERVA");
		private final int codigo;
		private final String texto;

		private OPERACION_VUELO_ASINCRONO(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}

	}
	
	public static enum ESTADO_VALIDACION_STOCK{
		SIN_STOCK(1,"SIN STOCK"),
		FALTA_STOCK(2,"FALTO STOCK"),
		CON_STOCK(3,"CON STOCK");
		
		private final int codigo;
		private final String texto;

		private ESTADO_VALIDACION_STOCK(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}

	}
	
	
	public static enum CLIENTE_DESBLOQUEO{
		EXITO(0,"EXITO"),
		ERROR(1,"ERROR"),
		EXCEPTION(10,"EXCEPTION");
		
		private final int codigo;
		private final String texto;

		private CLIENTE_DESBLOQUEO(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}
		public int getCodigo() {
			return codigo;
		}
		public String getTexto() {
			return texto;
		}

	}
	
	
	
	public static enum REPORTE_LIQUIDACION_COLA{
		PUNTOS_PLAZAPOINTS(1,"COLA-REPORTES-PROCESAR-EXCEL","LIQUIDACIONES PUNTOS PLAZAPOINTS","Liquidacion_Puntos_Plazapoints_"),
		PUNTOS_COSTAMAR(2,"COLA-REPORTES-PROCESAR-EXCEL","LIQUIDACIONES PUNTOS COSTAMAR","Liquidacion_Puntos_Costamar_"),
		COMISION_PLAZAPOINTS(3,"COLA-REPORTES-PROCESAR-EXCEL","LIQUIDACIONES COMISION PLAZAPOINTS","Liquidacion_Comision_Plazapoints_"),
		DEVOLUCIONES(4,"COLA-REPORTES-PROCESAR-EXCEL","LIQUIDACIONES DEVOLUCIONES","Liquidacion_Devoluciones_"),
		FLETES_VALES(5,"COLA-REPORTES-PROCESAR-EXCEL","LIQUIDACIONES FLETES DE VALES","Liquidacion_Fletes_Vales_");

		private final int idReporteLiquidacion;
		private final String nombreLiquidacion;
		private final String titulo;
		private final String fichero;
		
		private REPORTE_LIQUIDACION_COLA(int idReporteLiquidacion,String nombreLiquidacion,String titulo,String fichero){
			this.idReporteLiquidacion = idReporteLiquidacion;
			this.nombreLiquidacion = nombreLiquidacion;
			this.titulo = titulo;
			this.fichero = fichero;
			
		}
		
		public int getIdReporteLiquidacion() {
			return idReporteLiquidacion;
		}

		public String getNombreLiquidacion() {
			return nombreLiquidacion;
		}
		
		public String getTitulo() {
			return titulo;
		}
		
//		public String getFichero() {
//			return fichero+UDate.toDateUrl(new Date());
//		}
		
		public static REPORTE_LIQUIDACION_COLA getReporteXId(int idReporteLiquidacion){
			UtilEnum2.REPORTE_LIQUIDACION_COLA[] valores = REPORTE_LIQUIDACION_COLA.values();
			for (int i = 0; i < valores.length; i++) {
				if(valores[i].getIdReporteLiquidacion() == idReporteLiquidacion){
					return valores[i];
				}
			}
			return null;
		}
		
	}
	
	public static enum TIPO_INTEGRACION_MAPPING{
		
		DETALLE_PRODUCTO(1,"Detalle Producto"),
		LISTA_PRODUCTO(2,"Lista producto"),
		SOLO_CABECERA(3,"Datos de Pagina"),
		HOME(4,"Home");
		
		private final int codigo;
		private final String texto;

		private TIPO_INTEGRACION_MAPPING(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
	}
	
	public static enum TIPO_CUPON_DESCUENTO{
		NOMINAL(1,"Nominal"),
		PORCENTUAL(2,"Porcentual");
		private final int codigo;
		private final String texto;
		private TIPO_CUPON_DESCUENTO(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;
		}
		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static TIPO_CUPON_DESCUENTO getTipoCuponDescuento(Integer codigo){
			UtilEnum2.TIPO_CUPON_DESCUENTO[] valores = TIPO_CUPON_DESCUENTO.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return  valores[i];
				}
			}
			return null;
		}
		
	}
	 /**
	  * Proyecto: SaasppCommon
	  * @date	: 8/1/2018
	  * @time	: 17:09:56
	  * @author	: Erick vb.
	  * tipoGrupo en mongo db
	 */
	public static enum GRUPO_CUPON_DESCUENTO{
		
		PRODUCTO(1,"CATALOGO PRODUCTO","productos"),
		DESCUENTO(2,"DESCUENTO","descuentos"),
		VUELO(3,"VUELOS","vuelos"),
		PAQUETE(4,"PAQUETES","paquetes"),
		TODOS(10,"TODOS","todos");
		
		private final int codigo;
		private final String texto;
		private final String codigoIbk;

		private GRUPO_CUPON_DESCUENTO(int codigo, String texto, String codigoIbk){
			this.codigo = codigo;
			this.texto = texto;
			this.codigoIbk = codigoIbk;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public String getCodigoIbk() {
			return codigoIbk;
		}
		
		public static GRUPO_CUPON_DESCUENTO getGrupoCupon(Integer codigo){
			UtilEnum2.GRUPO_CUPON_DESCUENTO[] valores = GRUPO_CUPON_DESCUENTO.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return  valores[i];
				}
			}
			return null;
		}
	}
	
	public static enum TOKEN_PROCESO_IBK{
		LOGIN(1,"LOGIN","001"),
		PASARELA(2,"PASARELA","002"),
		PERFIL(3,"PERFIL","003"),
		PUNTOS(4,"PUNTOS","004");
		
		private final int codigo;
		private final String texto;
		private final String codigoIbk;

		private TOKEN_PROCESO_IBK(int codigo, String texto, String codigoIbk){
			this.codigo = codigo;
			this.texto = texto;
			this.codigoIbk = codigoIbk;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public String getCodigoIbk() {
			return codigoIbk;
		}
	}
	
	public static enum TARJETAS_HALCON{
		AMERICANEXPRESS(1,"AmericanExpress"),
		VISA(2,"Mastercard"),
		MASTERCARD(3,"Visa");
		
		private final int orden;
		private final String codigo;
		
		private TARJETAS_HALCON(int orden, String codigo){
			this.orden = orden;
			this.codigo = codigo;
		}
		
		public int getOrden() {
			return orden;
		}

		public String getCodigo() {
			return codigo;
		}
	}
	
	public static enum STATIC_PROMOCIONES_HALCON{
		IMAGEN_CUENTA_SUELDO("static/images/promociones/cuenta-sueldo.png"),
		SUB_TITULO_STATIC("CON TUS TARJETAS INTERBANK"),
		TITULO_STATIC("Disfruta de promociones exclusivas");
		
		private final String texto;
		
		private STATIC_PROMOCIONES_HALCON(String texto) {
			this.texto = texto;
		}

		public String getTexto() {
			return texto;
		}
	}
	
	public static enum EVENTO_REDIRECCION{
		NO(0,"No",false,"#"),
		LOGIN(1,"Login",false,"javascript:loginHandler();"),
		REGISTRATE(2,"Registrarte",false,"javascript:registerHandler();"),
		CUPON(3,"Cupon",true,"%s/cliente/perfil"),
		COMPRAS(4,"Compras",true,"%s/compras"),
		PRODUCTO(20,"Producto",true,"%s/producto/"),
		CATEGORIA(21,"Categoria",true,"%s/compras/"),
		OTRO(50,"Otros",true,"");
		
		private final Integer idEvento;
		private final String descripcion;
		private final Boolean extra;
		private final String evento;
		
		private EVENTO_REDIRECCION(Integer idEvento, String descripcion, Boolean extra, String evento) {
			this.idEvento = idEvento;
			this.descripcion = descripcion;
			this.extra =extra;
			this.evento = evento;
		}
		
		public static EVENTO_REDIRECCION getIdEvento(Integer codigo){
			UtilEnum2.EVENTO_REDIRECCION[] valores = EVENTO_REDIRECCION.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getIdEvento()==codigo){
					return  valores[i];
				}
			}
			return null;
		}

		public Integer getIdEvento() {
			return idEvento;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public String getEvento() {
			return evento;
		}

		public Boolean getExtra() {
			return extra;
		}
		
	}
	
	public static enum TIPO_EVALES{
		
		MARCA(1,"Marca"),
		PRODUCTO(2,"Producto"),
		SERVICIO(3,"Servicio");
		
		private final int codigo;
		private final String texto;		

		private TIPO_EVALES(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;			
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static TIPO_EVALES getTipoEvales(Integer codigo){
			UtilEnum2.TIPO_EVALES[] valores = TIPO_EVALES.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return  valores[i];
				}
			}
			return null;
		}
	}
	
	public static enum TOKEN_TIPO_PROCESO{
		
		CARGA_IMAGEN(1,"Carga de imagen");		
		
		private final int codigo;
		private final String texto;		

		private TOKEN_TIPO_PROCESO(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;			
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static TOKEN_TIPO_PROCESO getTokenTipoProceso(Integer codigo){
			UtilEnum2.TOKEN_TIPO_PROCESO[] valores = TOKEN_TIPO_PROCESO.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return  valores[i];
				}
			}
			return null;
		}
	}

	public static enum TIPO_DIRECTORIO{
		
		PRODUCTO(1,"producto"),
		MARCA(2,"marca"),
		EVALES(3,"evales");		
		
		private final int codigo;
		private final String texto;		

		private TIPO_DIRECTORIO(int codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;			
		}

		public int getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static TIPO_DIRECTORIO getTipoDirectorio(Integer codigo){
			UtilEnum2.TIPO_DIRECTORIO[] valores = TIPO_DIRECTORIO.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return  valores[i];
				}
			}
			return null;
		}
	}
	
	
	public static enum TIPO_TOP_PRODUCTOS {
		MAS_VENDIDOS(-5, "mas-vendidos", "VENDIDOS"),
		MAS_VISTOS(-6, "mas-visitados", "VISTOS");

		private final Integer id;
		private final String codigo;
		private final String texto;

		private TIPO_TOP_PRODUCTOS(Integer id, String codigo, String texto) {
			
			this.id = id;
			this.codigo = codigo;
			this.texto = texto;
		}
		
		public Integer getId() {
			return id;
		}

		public String getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static TIPO_TOP_PRODUCTOS getCodigoTopProductos(String codigo){
			UtilEnum2.TIPO_TOP_PRODUCTOS[] valores = TIPO_TOP_PRODUCTOS.values();
			for(int i = 0; i < valores.length; i++){
				if(valores[i].getCodigo().equals(codigo)){
					return  valores[i];
				}
			}
			return null;
		}
	}
	
	public static enum TIPO_VUELO {
		
		FIRST("F","First"),
		BUSINESS("C","Business"),
		ECONOMY("Y","Economy");		
		
		private final String codigo;
		private final String texto;		

		private TIPO_VUELO(String codigo, String texto){
			this.codigo = codigo;
			this.texto = texto;			
		}

		public String getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public static TIPO_VUELO getTipoClaseVuelo(String codigo){
			UtilEnum2.TIPO_VUELO[] valores = TIPO_VUELO.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo().equals(codigo)){
					return  valores[i];
				}
			}
			return null;
		}
	}
	
	public static enum EXCEL_BASE_CAMPOS {
		
		CODIGO_NETSUITE(1,"id_netsuite",EXCEL_BASE_VALIDACION.DIGITO,EXCEL_BASE_VALIDACION.NO_VACIO),
		CODIGO_PRODUCTO(2,"cod_producto",EXCEL_BASE_VALIDACION.DIGITO, EXCEL_BASE_VALIDACION.NO_VACIO),
		TITULO_PRODUCTO(3,"titulo_producto",EXCEL_BASE_VALIDACION.CADENA, EXCEL_BASE_VALIDACION.NO_VACIO),
		NOMBRE_PRODUCTO(4,"nombre_producto",EXCEL_BASE_VALIDACION.CADENA, EXCEL_BASE_VALIDACION.NO_VACIO),
//		DESCRIPCION(5,"descripcion",EXCEL_BASE_VALIDACION.CADENA,EXCEL_BASE_VALIDACION.NO_VACIO,
//				EXCEL_BASE_VALIDACION.MAXIMA_CANTIDAD_DESCRIPCION),
		CODIGO_PROVEEDOR(6,"cod_proveedor",EXCEL_BASE_VALIDACION.CADENA, EXCEL_BASE_VALIDACION.NO_VACIO),		
		COSTO_CUPONIUM(7,"costo_cuponium",EXCEL_BASE_VALIDACION.DOUBLE, EXCEL_BASE_VALIDACION.NO_VACIO),
		FLETE_LU(8,"flete_lu",EXCEL_BASE_VALIDACION.DOUBLE, EXCEL_BASE_VALIDACION.NO_VACIO),
		PSV(9,"psv",EXCEL_BASE_VALIDACION.DOUBLE, EXCEL_BASE_VALIDACION.NO_VACIO),
		PRECIO_REGULAR(10,"precio_regular",EXCEL_BASE_VALIDACION.DOUBLE, EXCEL_BASE_VALIDACION.NO_VACIO),
		PRECIO_IBK(11,"precio_ibk",EXCEL_BASE_VALIDACION.DOUBLE, EXCEL_BASE_VALIDACION.NO_VACIO),
//		DELIVERY_EXPRESS(12,"delivery_express",EXCEL_BASE_VALIDACION.BOOLEAN, EXCEL_BASE_VALIDACION.NO_VACIO),		
		IMAGEN_BIM(13,"imagen_bim",EXCEL_BASE_VALIDACION.CADENA, EXCEL_BASE_VALIDACION.NO_VACIO),
		CODIGO_CATEGORIA_BIM(14,"codigo_categoria_bim",EXCEL_BASE_VALIDACION.DIGITO, EXCEL_BASE_VALIDACION.NO_VACIO),
		CODIGO_SUBCATEGORIA_BIM(15,"codigo_subcategoria_bim",EXCEL_BASE_VALIDACION.DIGITO, EXCEL_BASE_VALIDACION.NO_VACIO),
//		VALIDA_EN_NETSUITE(16,"valida_en_netsuit",EXCEL_BASE_VALIDACION.BOOLEAN, EXCEL_BASE_VALIDACION.NO_VACIO),
		CODIGO_BODEGA(17,"cod_bodega",EXCEL_BASE_VALIDACION.DIGITO, EXCEL_BASE_VALIDACION.NO_VACIO),
		TIPO_PRODUCTO(18,"tipo_producto",EXCEL_BASE_VALIDACION.CADENA, EXCEL_BASE_VALIDACION.NO_VACIO),
		TIPO_PESO(19,"tipo_peso",EXCEL_BASE_VALIDACION.PAR_TIPO_PESO, EXCEL_BASE_VALIDACION.NO_VACIO),
		PESO_VOLUMETRICO(20,"peso_volumetrico",EXCEL_BASE_VALIDACION.DOUBLE),
		PESO_LINEAL(21,"peso_lineal",EXCEL_BASE_VALIDACION.DOUBLE),
		ANCHO(22,"ancho",EXCEL_BASE_VALIDACION.DOUBLE, EXCEL_BASE_VALIDACION.NO_VACIO),
		ALTO(23,"alto",EXCEL_BASE_VALIDACION.DOUBLE, EXCEL_BASE_VALIDACION.NO_VACIO),
		LARGO(24,"largo",EXCEL_BASE_VALIDACION.DOUBLE, EXCEL_BASE_VALIDACION.NO_VACIO),
		TIPO_VENTA(25,"tipo_venta",EXCEL_BASE_VALIDACION.PAR_TIPO_VENTA, EXCEL_BASE_VALIDACION.NO_VACIO),
		MARCA_O_BRAND(26,"marca_brand",EXCEL_BASE_VALIDACION.CADENA, EXCEL_BASE_VALIDACION.NO_VACIO),
//		INCLUYE_IGV(27,"incluye_igv",EXCEL_BASE_VALIDACION.BOOLEAN, EXCEL_BASE_VALIDACION.NO_VACIO),
		MOSTRAR_CANTIDAD_STOCK(28,"mostrar_cantidad_stock",EXCEL_BASE_VALIDACION.DIGITO, EXCEL_BASE_VALIDACION.NO_VACIO),
//		MOSTRAR_AGOTADO(29,"mostrar_agotado",EXCEL_BASE_VALIDACION.BOOLEAN, EXCEL_BASE_VALIDACION.NO_VACIO),
		MENSAJE_PARA_STOCK(30,"mensaje_para_stock",EXCEL_BASE_VALIDACION.CADENA, EXCEL_BASE_VALIDACION.NO_VACIO),
//		MOSTRAR_PRECIO_PRODUCTO(31,"mostrar_precio_producto",EXCEL_BASE_VALIDACION.BOOLEAN, EXCEL_BASE_VALIDACION.NO_VACIO),
		CODIGO_AGRUPADOR(32,"codigo_agrupador",EXCEL_BASE_VALIDACION.CADENA),
		CODIGO_GRUPO_CARACTERISTICAS(33,"codigo_grupo_caracteristicas",EXCEL_BASE_VALIDACION.CADENA),
		INFORMACION_PRODUCTO(34,"informacion_producto",EXCEL_BASE_VALIDACION.CADENA),
		ESPECIFICACIONES_PRODUCTO(35,"especificaciones_producto",EXCEL_BASE_VALIDACION.CADENA),
		MODO_USO_PRODUCTO(36,"modo_uso_producto",EXCEL_BASE_VALIDACION.CADENA),
		VIDEO_PRODUCTO(37,"video_producto",EXCEL_BASE_VALIDACION.CADENA),
		DESTACADO(38,"destacado",EXCEL_BASE_VALIDACION.DOUBLE,EXCEL_BASE_VALIDACION.NO_VACIO),
		ARBOL_CATEGORIA(39,"arbol_categoria",EXCEL_BASE_VALIDACION.DIGITO)
		;
		
		private final Integer codigo;
		private final String nombre;
		private final EXCEL_BASE_VALIDACION[] validacion;		

		private EXCEL_BASE_CAMPOS(Integer codigo, String nombre, EXCEL_BASE_VALIDACION ... validacion){
			this.codigo = codigo;
			this.nombre = nombre;
			this.validacion = validacion;			
		}	
		
		public Integer getCodigo() {
			return codigo;
		}

		public String getNombre() {
			return nombre;
		}

		public EXCEL_BASE_VALIDACION[] getValidacion() {
			return validacion;
		}
		
		public static EXCEL_BASE_CAMPOS getExcelBaseCampo(String nombre){
			UtilEnum2.EXCEL_BASE_CAMPOS[] valores = EXCEL_BASE_CAMPOS.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getNombre().equals(nombre)){
					return  valores[i];
				}
			}
			return null;
		}
	}
	
	public static enum EXCEL_BASE_VALIDACION {
		
		DIGITO(1,"deben ser dígitos del 0 al 9",null),
		NO_VACIO(2,"no debe estar vacio",null),
		CADENA(3,"debe ser del tipo cadena",null),
		DOUBLE(4,"debe ser del tipo númerico",null),
//		MAXIMA_CANTIDAD_DESCRIPCION(5,"debe tener como maximo "+UConstantes.MAXIMO_CARACTERES_DESCRIPCION+" caracteres", UConstantes.MAXIMO_CARACTERES_DESCRIPCION),
//		BOOLEAN(6,"debe ser "+UConstantes.RESPUESTA_POSITIVA+" o "+UConstantes.RESPUESTA_NEGATIVA,null),		
		PAR_TIPO_PESO(7,"debe tomar valores de "+UtilEnum2.TIPO_PESO.VOLUMETRICO.getTexto()+" o "+UtilEnum2.TIPO_PESO.LINEAL.getTexto(),null),
		PAR_TIPO_VENTA(8,"debe tomar valores de "+UtilEnum2.TIPO_VENTA_PRODUCTO.SUBVENCIONADO.getTexto()+" o "+UtilEnum2.TIPO_VENTA_PRODUCTO.NO_SUBVENCIONADO.getTexto(),null);
		private final Integer codigo;
		private final String  texto;			
		private final Integer valor;	
		
		private EXCEL_BASE_VALIDACION(Integer codigo,String texto, Integer valor){
			this.codigo = codigo;
			this.texto = texto;
			this.valor = valor;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}
		
		public Integer getValor() {
			return valor;
		}
	}
	
	public static enum TIPO_CARGA_FILE{
		CARGA_CATALOGO(1,"Carga hacia catalogo"),
		CARGA_PRODUCTO_BASE(2,"Carga hacia el producto base");
		
		private final Integer codigo;
		private final String texto;
		
		private TIPO_CARGA_FILE(Integer codigo, String texto) {
			this.codigo = codigo;
			this.texto = texto;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}		
	}
	
	public static enum TIPO_DATOS_CARGA_FILE
	{
		CREACION_PRODUCTOS(1,"Creacion de nuevos productos"),
		ACTUALIZACION_PRODUCTOS(2,"Actualizacion de productos");
		
		private final Integer codigo;
		private final String texto;
		
		private TIPO_DATOS_CARGA_FILE(Integer codigo, String texto) {
			this.codigo = codigo;
			this.texto = texto;
		}
		
		public static TIPO_DATOS_CARGA_FILE getTipoDatosCargaFile(Integer codigo){
			UtilEnum2.TIPO_DATOS_CARGA_FILE[] valores = TIPO_DATOS_CARGA_FILE.values();
			for(int i=0; i<valores.length; i++){
				if(valores[i].getCodigo()==codigo){
					return  valores[i];
				}
			}
			return null;
		}
		
		public Integer getCodigo() {
			return codigo;
		}

		public String getTexto() {
			return texto;
		}		
	}
	
	public enum NIVEL_CATEGORIA_PRODUCTO{
		DEPARTAMENTO(1),
		CATEGORIA(2),		
		SUBCATEGORIA(3),
		SUBSUBCATEGORIA(4);
		private final int codigo;

		private NIVEL_CATEGORIA_PRODUCTO(int estado){
			this.codigo = estado;
		}
		public int getCodigo() {
			return codigo;
		}
	}

	public enum SEGMENTO_BIM{
		TIPO_DOCUMENTO("TIPDOC"),
		SEGMENTO_GEOGRAFICO("SEG_GEOG"),
		SEGMENTO_PRODUCTO("SEG_PROD"),
		SEGMENTO_VALOR("SEG_VALOR"),
		SEGMENTO_BANCO("SEG_BANCO"),
		SEGMENTO_EDAD("SEG_EDAD"),
		SEGMENTO_GENERO("SEG_GEN"),
		SEGMENTO_INGRESOS("SEG_INGR"),
		SEGMENTO_CONS("SEG_CONS");
		
		private final String codigo;

		private SEGMENTO_BIM(String codigo){
			this.codigo = codigo;
		}
		public String getCodigo() {
			return codigo;
		}
	}
}