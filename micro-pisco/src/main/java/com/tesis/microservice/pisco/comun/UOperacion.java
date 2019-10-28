
package com.tesis.microservice.pisco.comun;
/**
  * Proyecto: SaasppCommon
  * @date	: 28/5/2015
  * @time	: 10:44:06
  * @author	: Erick vb.
 */
public class UOperacion {
	 
	 /**
	  * @param importeTotal
	  * @param cambioSoles
	  * @param cambioPuntos
	  * @return	: Integer
	  * @date	: 29/5/2015
	  * @time	: 12:27:38
	  * @author	: Erick vb.
	  * @descripcion : 	retorna el equivalente un puntos
	 */
//	public static Integer solesAPuntos(Double importeTotal, Double cambioSoles, Double cambioPuntos){
//		Double enPuntos =(importeTotal* cambioPuntos/cambioSoles);
//		Integer totalPuntos = (int) Math.floor(enPuntos);
//		return totalPuntos;
//	}
		
	 
	public static Integer conversionDeSolesAPuntos(Double tipoCambioFormula, Double porcentajeConversion, Double precioSoles){
		
		Double precioPuntos=precioSoles/tipoCambioFormula;
		Integer totalPuntos=(int)Math.round(precioPuntos/(porcentajeConversion/100));		
		return totalPuntos;
	}
	
	public static Integer conversionDeDolaresAPuntos(Double porcentajeConversion, Double precioDolares){
		Integer totalPuntos=(int)Math.round(precioDolares/(porcentajeConversion/100));		
		return totalPuntos;
	}
	
	public static Double conversionDePuntoASoles(Double tipoCambioFormula, Double porcentajeConversion, Integer totalPuntos){
		Double PorcentajePuntosACobrar=totalPuntos*(porcentajeConversion*0.01);
		Double totalSoles=PorcentajePuntosACobrar*tipoCambioFormula;
		return totalSoles;
	}
	
	public static Double calcularTipoCambio(Double precioSoles,Double precioDolares){ 
		Double tipoCambio=precioSoles/precioDolares;
		
		return tipoCambio;
		
	}
	
	 /**
	  * @param porcentajeConversion
	  * @param totalPuntos
	  * @return	: Double
	  * @date	: 5/1/2016
	  * @time	: 14:18:43
	  * @author	: Arly Fernandez
	  * @descripcion :
	 */
	public static Double conversionDePuntoADolares(Double porcentajeConversion, Integer totalPuntos){
		Double totalDolares=(totalPuntos*(porcentajeConversion*0.01)) ;
		return totalDolares;	
	}

	public static boolean verificarDisponibilidadPuntos(Integer totalPuntos,Integer puntosCliente){
		
		if(totalPuntos > puntosCliente){
			return false;
		}else{
			return true;
		}
	}

	public static int estadoVenta(int estado){
		int estadoFinal = UConstantes.VENTA_ESTADO_NO_ENCONTRADO;
		if(estado<0){
			estadoFinal = UConstantes.VENTA_ESTADO_NO_ENCONTRADO;
		}else if(estado==0){
			estadoFinal = UConstantes.VENTA_ESTADO_ELIMINADO;
		}else if(estado>100 && estado<=299){
			estadoFinal = UConstantes.VENTA_ESTADO_PENDIENTE;
		}else if(estado>300 && estado<=499){
			estadoFinal = UConstantes.VENTA_ESTADO_CONFIRMADO;
		}else if(estado>500 && estado<=799){
			estadoFinal = UConstantes.VENTA_ESTADO_RECHAZADO;
		}else{
			estadoFinal = UConstantes.VENTA_ESTADO_NO_ENCONTRADO;
		}
		return estadoFinal;
	}
	
	public static int obtenerEstadoAprobacionVenta(Integer tipoVenta){
		int estadoConfirmacion=0;
		if(tipoVenta==UtilEnum.TIPO_VENTA.CATALOGO_PRODUCTO.getCodigo()){
			
			estadoConfirmacion = UtilEnum.ESTADO_VENTA.CONFIRMADO_PRODUCTO.getCodigo();
			
		}else if(tipoVenta==UtilEnum.TIPO_VENTA.VUELO.getCodigo()){
			
			estadoConfirmacion = UtilEnum.ESTADO_VENTA.CONFIRMADO_VUELOS.getCodigo();
			
		}else if(tipoVenta==UtilEnum.TIPO_VENTA.PAQUETE.getCodigo()){
			
			estadoConfirmacion = UtilEnum.ESTADO_VENTA.CONFIRMADO_PAQUETE.getCodigo();
		}else if(tipoVenta== UtilEnum.TIPO_VENTA.EVALES.getCodigo()){
			estadoConfirmacion = UtilEnum.ESTADO_VENTA.CONFIRMADO_EVALES.getCodigo();
		}
		else{
			estadoConfirmacion = UtilEnum.ESTADO_VENTA.ELIMINADO.getCodigo();
		}
			
		return estadoConfirmacion;
	}
	
	public static int obtenerEstadoCancelacionVenta(Integer tipoVenta){
		int estadoConfirmacion=0;
		if(tipoVenta==UtilEnum.TIPO_VENTA.CATALOGO_PRODUCTO.getCodigo()){
			estadoConfirmacion = UtilEnum.ESTADO_VENTA.ANULADO_PRODUCTO.getCodigo();
		}else if(tipoVenta==UtilEnum.TIPO_VENTA.VUELO.getCodigo()){
			estadoConfirmacion = UtilEnum.ESTADO_VENTA.ANULADO_VUELOS.getCodigo();
		}else if(tipoVenta==UtilEnum.TIPO_VENTA.PAQUETE.getCodigo()){
			estadoConfirmacion = UtilEnum.ESTADO_VENTA.ANULADO_PAQUETE.getCodigo();
		}else if(tipoVenta==UtilEnum.TIPO_VENTA.EVALES.getCodigo()){
			estadoConfirmacion = UtilEnum.ESTADO_VENTA.ANULADO_EVALES.getCodigo();
		}
		else{
			estadoConfirmacion = UtilEnum.ESTADO_VENTA.ELIMINADO.getCodigo();
		}
			
		return estadoConfirmacion;
	}

	 /**
	  * @param porcentajeComisionPuntos
	  * @param totalPuntosUsados
	  * @return	: Integer
	  * @descripcion : Retorna importe en puntos
	  * @date	: 16/2/2016
	  * @time	: 14:56:46
	  * @author	: Erick vb.  	
	 */
	public static Integer calcularComisionUsoPuntos(Double porcentajeComisionPuntos, Integer totalPuntosUsados) {
		return (int) Math.ceil((porcentajeComisionPuntos* totalPuntosUsados) / 100.00);
	}
	
	 /**
	  * @param tipoCambioFormula
	  * @param porcentajeConversion
	  * @param totalPuntos
	  * @param porcentajeComisionPuntos
	  * @return	: Double
	  * @descripcion : retorna el calculo de uso de puntos convertido a costo comision soles
	  * @date	: 19/2/2016
	  * @time	: 13:02:17
	  * @author	: Erick vb.  	
	 */
	public static Double conversionDePuntoAComisionSoles(Double tipoCambioFormula, Double porcentajeConversion, Integer totalPuntos,
			Double porcentajeComisionPuntos){
		//calcaulado por partes hay perdida y ganancia
		
		//return conversionDePuntoASoles(tipoCambioFormula, porcentajeConversion, totalPuntos)*porcentajeComisionPuntos*0.01;
		//Integer totalComisionPuntos = calcularComisionUsoPuntos(porcentajeComisionPuntos, totalPuntos);
		//return conversionDePuntoASoles(tipoCambioFormula, porcentajeConversion, totalComisionPuntos);
	
		//comision de puntos a soles actualmente calculando matematicamente
		Double totalComisionPuntos = ((porcentajeComisionPuntos* totalPuntos) / 100.00);
		Double PorcentajePuntosACobrar= totalComisionPuntos*(porcentajeConversion*0.01);
		Double totalSoles = PorcentajePuntosACobrar*tipoCambioFormula;
		return totalSoles;
	}
	
	
public static boolean esPosibleReprogramarEntrega(Integer estadoFinal){
		
		if(estadoFinal == UtilEnum.CATALAGO_ESTADO_PEDIDO.NO_ENTREGADO.getId()
		|| estadoFinal == UtilEnum.CATALAGO_ESTADO_PEDIDO.REPROGRAMADO_1.getId()){
			return true;
		}else{
			return false;
		}
		
	}
	
	public static boolean esPosibleReenvioCorreo(Integer estadoFinal){
		
		if(estadoFinal != UtilEnum.CATALAGO_ESTADO_PEDIDO.ANULADO.getId()){
			return true;
		}else{
			return false;
		}
		
	}
	
	public static boolean esPosibleSolicitaDevolucion(Integer estadoFinal){
		
		if(estadoFinal == UtilEnum.CATALAGO_ESTADO_PEDIDO.ENTREGADO.getId()){
			return true;
		}else{
			return false;
		}
		
	}
	
	public static boolean esPosibleGenerarReclamo(Integer estadoFinal){
		
		if(estadoFinal != UtilEnum.CATALAGO_ESTADO_PEDIDO.ANULADO.getId()){
			return true;
		}else{
			return false;
		}
	}
	
	public static UtilEnum.ESTADO_PARA_LIQUIDAR tipoEstadoLiquidacionXperfil(UtilEnum.TIPO_USUARIO tipoUsuario){
		if(tipoUsuario.getCodigo() == UtilEnum.TIPO_USUARIO.PLAZAPOINTS.getCodigo() || tipoUsuario.getCodigo() == UtilEnum.TIPO_USUARIO.COSTAMAR.getCodigo()){
			return UtilEnum.ESTADO_PARA_LIQUIDAR.OBSERVADAS_IBK;
		}else{
			return UtilEnum.ESTADO_PARA_LIQUIDAR.TODOS;
		}
	}

	public static boolean estadoBotonLiquidacion(UtilEnum.TIPO_USUARIO tipoUsuario, Integer estadoXLiquidar, UtilEnum.TIPO_LIQUIDACION tipoLiquidacion) {
		boolean plazaPoints = (tipoUsuario.equals(UtilEnum.TIPO_USUARIO.PLAZAPOINTS)) && !tipoLiquidacion.equals(UtilEnum.TIPO_LIQUIDACION.PUNTOS_COSTAMAR);
		boolean costamar = (tipoUsuario.equals(UtilEnum.TIPO_USUARIO.COSTAMAR)) && tipoLiquidacion.equals(UtilEnum.TIPO_LIQUIDACION.PUNTOS_COSTAMAR);
		boolean observadosIbk = UtilEnum.ESTADO_PARA_LIQUIDAR.OBSERVADAS_IBK.getCodigo() == estadoXLiquidar;
		
		if(tipoUsuario.equals(UtilEnum.TIPO_USUARIO.INTERBANK) && !observadosIbk){
			return UConstantes.BOTON_ACTIVADO_LIQUIDACIONES;
		}else if((plazaPoints || costamar) && observadosIbk){
			return UConstantes.BOTON_ACTIVADO_LIQUIDACIONES;
		}else{
			return UConstantes.BOTON_DESACTIVADO_LIQUIDACIONES;
		}
	}
	
	public static boolean estadoBotonLiquidacion(UtilEnum.TIPO_USUARIO tipoUsuario, Integer estadoXLiquidar) {
		boolean plazaPoints = (tipoUsuario.equals(UtilEnum.TIPO_USUARIO.PLAZAPOINTS));
		boolean costamar = (tipoUsuario.equals(UtilEnum.TIPO_USUARIO.COSTAMAR));
		boolean observadosIbk = UtilEnum.ESTADO_PARA_LIQUIDAR.OBSERVADAS_IBK.getCodigo() == estadoXLiquidar;
		
		if(tipoUsuario.equals(UtilEnum.TIPO_USUARIO.INTERBANK) && !observadosIbk){
			return UConstantes.BOTON_ACTIVADO_LIQUIDACIONES;
		}else if((plazaPoints || costamar) && observadosIbk){
			return UConstantes.BOTON_ACTIVADO_LIQUIDACIONES;
		}else{
			return UConstantes.BOTON_DESACTIVADO_LIQUIDACIONES;
		}
	}
	
	
	public static boolean estadoBotonCobrar(UtilEnum.TIPO_USUARIO tipoUsuario,UtilEnum.TIPO_LIQUIDACION tipoLiquidacion) {
		boolean plazaPoints = tipoUsuario.equals(UtilEnum.TIPO_USUARIO.PLAZAPOINTS) && !tipoLiquidacion.equals(UtilEnum.TIPO_LIQUIDACION.PUNTOS_COSTAMAR);
		boolean costamar = tipoUsuario.equals(UtilEnum.TIPO_USUARIO.COSTAMAR) && tipoLiquidacion.equals(UtilEnum.TIPO_LIQUIDACION.PUNTOS_COSTAMAR);
		
		if(plazaPoints || costamar){
			return UConstantes.BOTON_ACTIVADO_LIQUIDACIONES;
		}else{
			return UConstantes.BOTON_DESACTIVADO_LIQUIDACIONES;
		}
	}
	
	public static boolean estadoBotonCobrar(UtilEnum.TIPO_USUARIO tipoUsuario) {
		boolean plazaPoints = tipoUsuario.equals(UtilEnum.TIPO_USUARIO.PLAZAPOINTS) ;
		boolean costamar = tipoUsuario.equals(UtilEnum.TIPO_USUARIO.COSTAMAR);
		
		if(plazaPoints || costamar){
			return UConstantes.BOTON_ACTIVADO_LIQUIDACIONES;
		}else{
			return UConstantes.BOTON_DESACTIVADO_LIQUIDACIONES;
		}
	}
	public static  boolean deliveryPagadoEnPuntos(Double totalVenta, Double costoDelivery){
		if(totalVenta -costoDelivery >=0){
			/*se paga delivery todo con soles*/
			return false;
		}else{
			/*se pago todo con puntos*/
			return true;
		}
		
	}
}
