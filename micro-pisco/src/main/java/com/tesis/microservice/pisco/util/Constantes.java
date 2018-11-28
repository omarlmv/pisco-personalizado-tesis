package com.tesis.microservice.pisco.util;

public class Constantes {	
	public static final String SCHEMA_NAME = "sch_general";	
	public static final String SP_CATEGORIA_ARBOL_LISTAR_BY_NIVEL_TIPO = "sp_categoria_arbol_listar_by_nivel_tipo";
	public static final String SP_CATEGORIA_ARBOL_LISTAR_HIJOS = "sp_categoria_arbol_listar_hijos";
	public static final String SP_FACT_PRODUCTO_LISTAR_DESTACADOS = "sp_fact_producto_listar_destacados";
	public static final String SP_FACT_PRODUCTO_CONTAR_LISTAR_DESTACADOS = "sp_fact_producto_contar_listar_destacados";

	public static final String SP_FACT_PRODUCTO_LISTAR_MALLA = "sp_fact_producto_listar_malla";

	public static final String SP_EVENTO_SEGMENTO_LISTAR = "SP_EVENTO_SEGMENTO_LISTAR";
	public static final String SP_CATEGORIA_ARBOL_LISTAR_TODOS_HIJOS = "sp_categoria_listar_todos_hijos";
	
	public static final Integer TOTAL_DESTACADOS_PAGINA=10;
	public static final String SP_CATEGORIA_ARBOL_LISTAR_TODOS = "sp_categoria_arbol_listar_todos";
	public static final String SP_FACT_PRODUCTO_CONTAR_MALLA = "sp_fact_producto_contar_malla";
	public static final String SP_FACT_PRODUCTO_CONTADOR_X_CATEGORIA_NIVEL = "sp_fact_producto_contador_x_categoria_nivel";
	public static final String SP_FACT_PRODUCTO_CONTADOR_RANGO_MILLAS = "sp_fact_producto_contador_rango_millas";
	public static final String SP_AGRUPADOR_LISTA_WEB = "sp_agrupador_listar_web";
	public static final String SP_FACT_PRODUCTO_CONTADOR_X_CATEGORIA_DESTACADOS = "sp_fact_producto_contador_x_categoria_destacados";
	public static final String SP_FACT_PRODUCTO_CONTADOR_RANGO_MILLAS_DESTACADOS = "sp_fact_producto_contador_rango_millas_destacados";
	public static final String SP_FACT_PRODUCTO_DESTACADOS_LISTAR_HOME = "sp_fact_producto_destacados_listar_home";
	
	public static final String SP_EVENTO_LISTAR_X_TIPO = "sp_evento_listar_x_tipo";
	public static final String SP_FACT_PRODUCTO_LISTAR_PARA_BUSQUEDA = "sp_fact_producto_listar_para_busqueda";
	public static final String SP_FACT_PRODUCTO_CONTAR_X_CATEGORIA = "sp_fact_producto_contar_x_categoria";


	
	
	public static final String SP_PEDIDOS_DISTRIBUIDOS = "sp_pedidos_distribucion";
	public static final String SP_ACTUALIZAR_ESTADO_PEDIDO = "sp_actualizar_estado_pedido";
	public static final String SP_REGISTRAR_OBSERVACION_PEDIDO = "sp_registrar_observacion_pedido";
	
	
	//WorkFlow
	public static final String SP_REGISTRAR_WORKFLOW = "sp_registrar_workflow";
	public static final String SP_BUSCAR_ESTADO_WORKFLOW = "sp_buscar_estado_workflow";
	public static final String SP_BUSCAR_ESTADO_PEDIDO = "sp_buscar_estado_pedido";
	
}
