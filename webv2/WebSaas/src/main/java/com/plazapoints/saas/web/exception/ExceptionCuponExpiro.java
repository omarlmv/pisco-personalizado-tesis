package com.plazapoints.saas.web.exception;

 /**
  * Proyecto: WebSaas
  * @date	: 14/11/2017
  * @time	: 18:38:32
  * @author	: Erick vb.
 */
public class ExceptionCuponExpiro   extends Exception{
	
	private static final long serialVersionUID = 1L;

	public ExceptionCuponExpiro(String mensaje){
		super(mensaje);
	}
}
