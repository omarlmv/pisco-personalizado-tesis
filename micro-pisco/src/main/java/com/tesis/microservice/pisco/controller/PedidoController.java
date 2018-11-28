package com.tesis.microservice.pisco.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesis.microservice.pisco.model.Pedido;
import com.tesis.microservice.pisco.model.WorkFlow;
import com.tesis.microservice.pisco.service.DistribucionService;
import com.tesis.microservice.pisco.service.WorkFlowService;
import com.tesis.microservice.pisco.util.Result;
import com.tesis.microservice.pisco.util.UtilEnum;
import com.tesis.microservice.pisco.util.UtilEnum.ESTADO_PEDIDO;
import com.tesis.microservice.pisco.util.UtilEnum.ESTADO_FLUJO_TRABAJO;
import com.tesis.microservice.pisco.util.UtilEnum.RESULTADO_OPERACION;

@RestController
@RequestMapping("pedido")
public class PedidoController {

	private static Logger logger = LoggerFactory.getLogger(PedidoController.class);

	@Autowired
	private WorkFlowService workFlowService;
	
	@Autowired
	private DistribucionService distribucionService;	
	
//	@Autowired
//	private ProcesoUtil procesoUtil;
	
	// @AUTOWIREsD
	// PRIVATE PROPIEDADWEB PROPIEDADWEB;

	@RequestMapping(value = "status", method = RequestMethod.GET)
	public String status() {
		return "OK";
	}	
	
	
//	@RequestMapping(value = "listar/{usuario}", method = RequestMethod.GET)
//	public List<Pedido> listarPedidoDistribucion(@PathVariable("usuario") Integer IdUsuario) {
//		logger.info("DistribucionController.listarPedidoDistribucion");
//		List<Pedido> listaPedidos = null;
//		try {
//			listaPedidos = distribucionService.listarPedidoDistribucion(IdUsuario);
//			
//		} catch (Exception e) {
//			logger.error("Exception listarPedidoDistribucion :", e);
//		}
//		return listaPedidos;
//	}

	
	@RequestMapping(value = "actualizarEstadoPedido", method = RequestMethod.POST)
	public Result actualizarEstadoPedido(@RequestBody Pedido pedido) {
		logger.info("PedidoController.actualizarEstadoPedido");
		Result resultado = new Result();
		ESTADO_PEDIDO estadoParametro = null;
		ESTADO_PEDIDO estado = null;
		try {
			int codigo_fase = 0;
			boolean nuevoRegistro = false;
			boolean validaEstado = true;
			
			logger.info(pedido.toString());
			
			int estadoActual = distribucionService.buscarEstadoPedido(pedido);
			estadoParametro = UtilEnum.ESTADO_PEDIDO.getEstadoPedidoByID(pedido.getEstado());

			if(estadoParametro==null) {
				resultado.setEstado(RESULTADO_OPERACION.ERROR.getCodigo());
				resultado.setMensaje("El estado " + pedido.getEstado() + " enviado no existe ");
				return resultado;
			}
			
			if(estadoActual==-1) {
				nuevoRegistro = true;
				estadoParametro = UtilEnum.ESTADO_PEDIDO.getEstadoPedidoByID(UtilEnum.ESTADO_PEDIDO.CREADO_PENDIENTE_APROBACION_PEDIDO.getCodigo());
			} else {
				estado = UtilEnum.ESTADO_PEDIDO.getEstadoPedidoByID(estadoActual);
			}
			
			/*El estado actual necesita ser mayor al que viene como parametro*/
			if(nuevoRegistro==false && estadoActual>=estadoParametro.getCodigo()) {
				validaEstado = false;
				resultado.setEstado(RESULTADO_OPERACION.ERROR.getCodigo());
				resultado.setMensaje("El estado pedido enviado " + estadoParametro.getCodigo() + " - "+ estadoParametro.getTexto() + 
				" no procede ya que actualmente se encuentra con estado " + estado.getTexto() );	
				return resultado;
			}

			if(validaEstado) {
				pedido.setEstado(estadoParametro.getCodigo());
//				pedido.setFase(codigo_fase);

				/*Registra Flujo*/
				WorkFlow flujo = new WorkFlow();

				UtilEnum.ESTADO_PEDIDO pedidoEstado = UtilEnum.ESTADO_PEDIDO.getEstadoPedidoByID(pedido.getEstado());
				Integer estadoFlujo = null;
				
				switch (pedidoEstado) {
				case CREADO_PENDIENTE_APROBACION_PEDIDO:
					estadoFlujo = ESTADO_FLUJO_TRABAJO.CREACION_FLUJO.getCodigo();
					break;
				case APROBADO_PENDIENTE_ABASTECIMIENTO_PEDIDO:
					estadoFlujo = ESTADO_FLUJO_TRABAJO.APROBACION_FLUJO.getCodigo();
					break;
				case ABASTECIMIENTO_PENDIENTE_PRODUCCION_PEDIDO:
					estadoFlujo = ESTADO_FLUJO_TRABAJO.ABASTECIMIENTO_FLUJO.getCodigo();
					break;
				case PRODUCIDO_PENDIENTE_CONTROL_CALIDAD_PEDIDO:
					estadoFlujo = ESTADO_FLUJO_TRABAJO.PRODUCCION_FLUJO.getCodigo();
					break;
				case CONTROLADO_PENDIENTE_PRE_DISTRIBUCION_PEDIDO:
					estadoFlujo = ESTADO_FLUJO_TRABAJO.CONTROL_CALIDAD_FLUJO.getCodigo();
					break;
//				case PRE_DISTRIBUIDO_PEDIDO:
//					estadoFlujo = ESTADO_FLUJO_TRABAJO.PRE_DISTRIBUCION_FLUJO.getCodigo();
//					break;
				case PRE_DISTRIBUIDO_PEDIDO:
					estadoFlujo = ESTADO_FLUJO_TRABAJO.INCIO_ENTREGA_FLUJO.getCodigo();
					break;
				case INCIDENCIA_PEDIDO:
					/*Si es incidencia vuelve a pre distribuido solo en el pedido*/
					pedido.setEstado(UtilEnum.ESTADO_PEDIDO.PRE_DISTRIBUIDO_PEDIDO.getCodigo());
					estadoFlujo = ESTADO_FLUJO_TRABAJO.INCIDENCIA_FLUJO.getCodigo();
					break;
				case DISTRIBUIDO_FINALIZADO_PEDIDO:
					estadoFlujo = ESTADO_FLUJO_TRABAJO.FIN_ENTREGA_FLUJO.getCodigo();
					break;
					
				default:
					break;
				}
				
				resultado.setEstado(RESULTADO_OPERACION.OK.getCodigo());
				resultado.setMensaje("Pedido exitoso " + pedido.getIdPedido() +
				" Estado actualizado : " + estadoParametro.getCodigo() + " - " + estadoParametro.getTexto()+" | ");
				Integer registro = distribucionService.actualizarEstadoPedido(pedido.getIdPedido(), pedido.getEstado());
				resultado.setResult(registro);
				
				
				flujo.setIdPedido(pedido.getIdPedido());
				flujo.setEstado(estadoFlujo);
				flujo.setIdUsuario(pedido.getIdUsuario());
				
				Result resultadoFLujo = registrarFlujo(flujo);				
				resultado.setMensaje(resultado.getMensaje() + (((resultadoFLujo.getMensaje() != null) ? resultadoFLujo.getMensaje() : "")));
				
				distribucionService.registrarObservacionPedido(pedido.getIdPedido(), pedido.getObservacion(), pedido.getEstado(), pedido.getIdUsuario());
				
				return resultado;
			}
			
		} catch (Exception e) {
			logger.error("Exception actualizarEstadoPedido :", e);
		}
		return resultado;
	}
	
	
	@RequestMapping(value = "registrarFlujo", method = RequestMethod.POST)
	public Result registrarFlujo(@RequestBody WorkFlow workFlow) {
		logger.info("DistribucionController.registrarFlujo");
		Result resultado = new Result();
		ESTADO_FLUJO_TRABAJO estadoParametro = null;
		ESTADO_FLUJO_TRABAJO estado = null;
		try {
			int codigo_fase = 0;
			boolean nuevoRegistro = false;
			boolean validaEstado = true;
			
			int estadoActual = workFlowService.buscarEstadoWorkFlow(workFlow);
			estadoParametro = UtilEnum.ESTADO_FLUJO_TRABAJO.getEstadoWorkFlowByID(workFlow.getEstado());

			if(estadoParametro==null) {
				resultado.setEstado(RESULTADO_OPERACION.ERROR.getCodigo());
				resultado.setMensaje("El estado " + workFlow.getEstado() + " enviado no existe ");
				return resultado;
			}
			
			if(estadoActual==-1) {
				nuevoRegistro = true;
				estadoParametro = UtilEnum.ESTADO_FLUJO_TRABAJO.getEstadoWorkFlowByID(UtilEnum.ESTADO_FLUJO_TRABAJO.CREACION_FLUJO.getCodigo());
			} else {
				estado = UtilEnum.ESTADO_FLUJO_TRABAJO.getEstadoWorkFlowByID(estadoActual);
			}
			/*validacion omituda para registrar las incidencias*/
//			/*El estado actual necesita ser mayor al que viene como parametro*/
//			if(nuevoRegistro==false && estadoActual>=estadoParametro.getCodigo()) {
//				validaEstado = false;
//				resultado.setEstado(RESULTADO_OPERACION.ERROR.getCodigo());
//				resultado.setMensaje("El estado flujo trabajo enviado " + estadoParametro.getCodigo() + " - "+ estadoParametro.getTexto() + 
//				" no procede ya que actualmente se encuentra con estado " + estado.getTexto() );	
//				return resultado;
//			}

			if(validaEstado) {
				switch (estadoParametro) {
				case CREACION_FLUJO:
				case APROBACION_FLUJO:
				case ABASTECIMIENTO_FLUJO:
					codigo_fase = UtilEnum.FASE_WORKFLOW.NEGOCIACION.getCodigo();
					break;

				case PRODUCCION_FLUJO:
				case CONTROL_CALIDAD_FLUJO:
				case PRE_DISTRIBUCION_FLUJO:
					codigo_fase = UtilEnum.FASE_WORKFLOW.PRODUCCION.getCodigo();
					break;
					
				case INCIO_ENTREGA_FLUJO:
				case FIN_ENTREGA_FLUJO:
					codigo_fase = UtilEnum.FASE_WORKFLOW.DISTRIBUCION.getCodigo();
					break;				
					
				default:
					break;
				}
				workFlow.setEstado(estadoParametro.getCodigo());
				workFlow.setFase(codigo_fase);

				resultado.setEstado(RESULTADO_OPERACION.OK.getCodigo());
				resultado.setMensaje(" Flujo de Pedido con ID: " + workFlow .getIdPedido() +
				" Estado registrado: " + estadoParametro.getCodigo() + " - " + estadoParametro.getTexto());
				Integer registro = workFlowService.registrarWorkFlow(workFlow);
				resultado.setResult(registro);
				return resultado;
			}
			
		} catch (Exception e) {
			logger.error("Exception registrarFlujo :", e);
		}
		return resultado;
	}
	
//	@RequestMapping(value = "registrarIncidencia/", method = RequestMethod.POST)
//	public Integer registrarIncidencia(@RequestBody Pedido pedido) {
//		logger.info("DistribucionController.registrarIncidencia");
//		Integer resultado = null;
//		try {
//			resultado = distribucionService.registrarIncidencia(pedido.getIdPedido(), pedido.getObservacion());
//		} catch (Exception e) {
//			logger.error("Exception registrarIncidencia :", e);
//		}
//		return resultado;
//	}
	
	
}
