package com.tesis.microservice.pisco.comun;


import org.apache.commons.lang3.StringUtils;

import com.tesis.microservice.pisco.comun.UtilEnum.TIPO_IMAGEN;


 /**
  * Proyecto: SaasppCommon
  * @date	: 28/4/2016
  * @time	: 11:01:33
  * @author	: Erick vb.
 */
public class UtilImagen {
	

	public static String nombreImagenPrefijo(String imagen,TIPO_IMAGEN tipoImagen){
		if(null==imagen || imagen.length()==0){
			return StringUtils.EMPTY;
		}
		
		String imagenNueva = String.valueOf(imagen);
		int index = String.valueOf(imagen).lastIndexOf(".");
		if(index>0){
			StringBuffer buffer = new StringBuffer();
			buffer.append(imagen.substring(0,index));
			buffer.append("-");
			buffer.append(tipoImagen.getCarpeta());
			buffer.append(imagen.substring(index));
			imagenNueva = "/"+tipoImagen.getCarpeta()+"/"+buffer.toString();
		}

		return imagenNueva;
	}
	
	 /**
	  * @param imagen
	  * @param tipoImagen
	  * @return	: String
	  * @description : agrega solo el sufijo  del directorio  al nombre de un imagen, usado para subir a un directorio especifico del s3
	  * @date	: 28/4/2016
	  * @time	: 11:03:02
	  * @author	: Erick vb.  	
	 */
	public static String formatNombreDirectorioS3(String imagen,TIPO_IMAGEN tipoImagen){
		if(null==imagen || imagen.length()==0){
			return StringUtils.EMPTY;
		}
		
		String imagenNueva = String.valueOf(imagen);
		int index = String.valueOf(imagen).lastIndexOf(".");
		if(index>0){
			StringBuffer buffer = new StringBuffer();
			buffer.append(imagen.substring(0,index));
			buffer.append("-");
			buffer.append(tipoImagen.getCarpeta());
			buffer.append(imagen.substring(index));
			imagenNueva = buffer.toString();
		}

		return imagenNueva;
	}
	
	
	public static String addSufijoNombre(String imagen,TIPO_IMAGEN tipoImagen, String sufijo){
		if(null==imagen || imagen.length()==0){
			return StringUtils.EMPTY;
		}
		
		String imagenNueva = String.valueOf(imagen);
		int index = String.valueOf(imagen).lastIndexOf(".");
		if(index>0){
			StringBuffer buffer = new StringBuffer();
			buffer.append(imagen.substring(0,index));
			buffer.append("-");
			buffer.append(sufijo);
			buffer.append(imagen.substring(index));
			imagenNueva = buffer.toString();
		}

		return imagenNueva;
	}

}
