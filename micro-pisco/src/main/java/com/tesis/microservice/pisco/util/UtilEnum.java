package com.tesis.microservice.pisco.util;

public class UtilEnum {
	public static enum EXCEPTION {
	       
		EXCEPTION(-1, "Error en el servidor", "msg.exception.error.generico"), 
		EXEPTION_TOKEN_NO_VALIDO(-2,"Token No V�lido","msg.exception.token_invalido"),
		EXCEPTION_LISTA_VACIA(-3,"Resultado vacio","msg.exception.lista_vacia"),
		EXCEPTION_PEDIDO_REPETIDO(-4,"El pedido ya existe","msg.exception.pedido_repetido"), 
		EXCEPTION_PROGRAMA_INCORRECTO(-5,"La solicitud no corresponde al programa","msg.exception.programa_incorrecto"), 
		EXCEPTION_PEDIDO_SIN_STOCK(-6,"No hay stock para procesar el pedido", "msg.exception.sin_stock"),
		EXCEPTION_PEDIDO_NO_EXISTE(-7,"El pedido no existe o ya ha sido procesado","msg.exception.pedido_inexistente"),
		EXCEPTION_DETALLE_PEDIDO_PENDIENTE(-8,"Detalle pedido pendiente","msg.exception.detalle_pedido.pendiente"),
		EXCEPTION_ANULACION_PEDIDO_PROCESADO(-9,"El pedido ya se encuentra procesado y no puede anularlo","msg.exception.anulacion_pedido_procesado"),
		EXCEPTION_ANULACION_PEDIDO_NO_EXISTE(-10,"El pedido no existe","msg.exception.anulacion_pedido_inexistente"),
		EXCEPTION_ANULACION_PEDIDO_REALIZADO(-11,"Se realiz� la anulaci�n del pedido anteriormente","msg.exception.anulacion_pedido_realizado");

		private final Integer codigo;
		private final String mensaje;
		private final String key;

		private EXCEPTION(Integer codigo, String mensaje, String key) {
			this.codigo = codigo;
			this.mensaje = mensaje;
			this.key = key;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getMensaje() {
			return mensaje;
		}

		public String getKey() {
			return key;
		}

	}

	public static EXCEPTION obtenerError(Integer codigo) {
		for (EXCEPTION error : EXCEPTION.values()) {
			if (error.getCodigo().equals(codigo)) {
				return error;
			}
		}
		return null;
	}	   
	   
	public static enum TIPO_ENTREGA {
		
		DIGITAL (1,"DIGITAL"),
		FISICO (2,"FISICO");

		private Integer codigo;
		private String texto;

		private TIPO_ENTREGA(Integer codigo, String texto) {
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
	
public static enum CANAL_TRANSACCION {
		
		WEB (1,"WEB"),
		POS (2,"POS");

		private Integer codigo;
		private String texto;

		private CANAL_TRANSACCION(Integer codigo, String texto) {
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
public static enum RESULTADO_OPERACION {
	OK(1,"OK"),
	ERROR(-1,"ERROR"),;
	
	private Integer codigo;
	private String texto;
	
	private RESULTADO_OPERACION(Integer codigo, String texto){
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

public  enum FASE_WORKFLOW{
	NEGOCIACION(300, "NEGOCIACION","NEGOCIACION"), 
	PRODUCCION(301, "PRODUCCION","PRODUCCION"), 
	DISTRIBUCION(302, "DISTRIBUCION","DISTRIBUCION");

	private final int codigo;
	private final String  texto;
	private final String keyMsg;
	private FASE_WORKFLOW(int codigo, String texto, String keyMsg){
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
	
	public static FASE_WORKFLOW getFaseWorkFlowByID(Integer id){
		FASE_WORKFLOW[] valores = FASE_WORKFLOW.values();
		for(int i=0; i<valores.length; i++){
			if(valores[i].getCodigo() == id){
				return  valores[i];
			}
		}
		return null;
	}
	
	public static String getFaseWorkFlowKeyMsg(Integer estado){
		String descripcion =  null;
		for(UtilEnum.FASE_WORKFLOW item : UtilEnum.FASE_WORKFLOW.values() ){
			if(item.getCodigo() ==estado){
				descripcion = item.getKeyMsg();
				break;
			}
			
		}
		return descripcion;
	}
}



public  enum ESTADO_FLUJO_TRABAJO{
	CREACION_FLUJO (100,"CREACION_FLUJO","CREACION_FLUJO"),
	APROBACION_FLUJO (101,"APROBACION_FLUJO","APROBACION_FLUJO"),
	ABASTECIMIENTO_FLUJO (102,"ABASTECIMIENTO_FLUJO","ABASTECIMIENTO_FLUJO"),
	
	PRODUCCION_FLUJO (103,"PRODUCCION_FLUJO","PRODUCCION_FLUJO"),
	CONTROL_CALIDAD_FLUJO (104,"CONTROL_CALIDAD_FLUJO","CONTROL_CALIDAD_FLUJO"),
	PRE_DISTRIBUCION_FLUJO (105,"PRE_DISTRIBUCION_FLUJO","PRE_DISTRIBUCION_FLUJO"),
	
	INCIO_ENTREGA_FLUJO (106,"INCIO_ENTREGA_FLUJO","INCIO_ENTREGA_FLUJO"),
	INCIDENCIA_FLUJO (107,"INCIDENCIA_FLUJO","INCIDENCIA_FLUJO"),

	FIN_ENTREGA_FLUJO (108,"FIN_ENTREGA_FLUJO","FIN_ENTREGA_FLUJO");

	private final int codigo;
	private final String  texto;
	private final String keyMsg;
	
	private ESTADO_FLUJO_TRABAJO(int codigo, String texto, String keyMsg){
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
	
	public static ESTADO_FLUJO_TRABAJO getEstadoWorkFlowByID(Integer id){
		ESTADO_FLUJO_TRABAJO[] valores = ESTADO_FLUJO_TRABAJO.values();
		for(int i=0; i<valores.length; i++){
			if(valores[i].getCodigo() == id){
				return  valores[i];
			}
		}
		return null;
	}
	
	public static String getEstadoWorkFlowKeyMsg(Integer estado){
		String descripcion =  null;
		for(UtilEnum.ESTADO_FLUJO_TRABAJO item : UtilEnum.ESTADO_FLUJO_TRABAJO.values() ){
			if(item.getCodigo() ==estado){
				descripcion = item.getKeyMsg();
				break;
			}
			
		}
		return descripcion;
	}
}

	
public  enum ESTADO_PEDIDO{
	CREADO_PENDIENTE_APROBACION_PEDIDO (200,"CREADO_PENDIENTE_APROBACION_PEDIDO","CREADO_PENDIENTE_APROBACION_PEDIDO"),
	APROBADO_PENDIENTE_ABASTECIMIENTO_PEDIDO (201,"APROBADO_PENDIENTE_ABASTECIMIENTO_PEDIDO","APROBADO_PENDIENTE_ABASTECIMIENTO_PEDIDO"),
	ABASTECIMIENTO_PENDIENTE_PRODUCCION_PEDIDO (202,"ABASTECIMIENTO_PENDIENTE_PRODUCCION_PEDIDO","ABASTECIMIENTO_PENDIENTE_PRODUCCION_PEDIDO"),
	
	PRODUCIDO_PENDIENTE_CONTROL_CALIDAD_PEDIDO (203,"PRODUCIDO_PENDIENTE_CONTROL_CALIDAD_PEDIDO","PRODUCIDO_PENDIENTE_CONTROL_CALIDAD_PEDIDO"),
	CONTROLADO_PENDIENTE_PRE_DISTRIBUCION_PEDIDO (204,"CONTROLADO_PENDIENTE_PRE_DISTRIBUCION_PEDIDO","CONTROLADO_PENDIENTE_PRE_DISTRIBUCION_PEDIDO"),
	PRE_DISTRIBUIDO_PEDIDO (205,"PRE_DISTRIBUIDO_PEDIDO","PRE_DISTRIBUIDO_PEDIDO"),

	INCIDENCIA_PEDIDO (206,"INCIDENCIA_PEDIDO","INCIDENCIA_PEDIDO"),
	DISTRIBUIDO_INICIADO_PEDIDO (207,"DISTRIBUIDO_INICIADO_PEDIDO","DISTRIBUIDO_INICIADO_PEDIDO"),
	
	DISTRIBUIDO_FINALIZADO_PEDIDO (208,"DISTRIBUIDO_FINALIZADO_PEDIDO","DISTRIBUIDO_FINALIZADO_PEDIDO");

	private final int codigo;
	private final String  texto;
	private final String keyMsg;
	
	private ESTADO_PEDIDO (int codigo, String texto, String keyMsg){
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
	
	public static ESTADO_PEDIDO getEstadoPedidoByID(Integer id){
		ESTADO_PEDIDO[] valores = ESTADO_PEDIDO.values();
		for(int i=0; i<valores.length; i++){
			if(valores[i].getCodigo() == id){
				return  valores[i];
			}
		}
		return null;
	}
	
	public static String getEstadoPedidoKeyMsg(Integer estado){
		String descripcion =  null;
		for(UtilEnum.ESTADO_PEDIDO item : UtilEnum.ESTADO_PEDIDO.values() ){
			if(item.getCodigo() ==estado){
				descripcion = item.getKeyMsg();
				break;
			}
			
		}
		return descripcion;
	}
}

}
