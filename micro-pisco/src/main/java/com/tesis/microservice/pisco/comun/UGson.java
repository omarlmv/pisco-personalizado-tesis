
package com.tesis.microservice.pisco.comun;


import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;



 /**
  * Proyecto	: AppAdminCuponium
  * @date	: Mar 19, 2012
  * @time	: 6:51:01 PM
  * @author	: Julio Novoa
 */
public class UGson {

	private static Logger logger = LoggerFactory.getLogger(UGson.class);
	
	private UGson(){
		
	}
	
	private static Gson getObjetoGson(){
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(Timestamp.class, new JsonDeserializer<Timestamp>() {
	        @Override
	        public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	                throws JsonParseException{
	        		long time = Long.parseLong(json.getAsString());
				    return new Timestamp(time);
	        }
	    });
		
		gsonb.registerTypeAdapter(Timestamp.class, new JsonSerializer<Timestamp>() {
			@Override
			public JsonElement serialize(Timestamp paramT, Type paramType,
					JsonSerializationContext paramJsonSerializationContext) {
				try {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					Date fecha = df.parse(paramT.toString());
	        		
	        		return paramJsonSerializationContext.serialize(fecha.getTime());
				} catch (Exception e) {
					logger.info(e + "");
				}
				return null;
			}
		});
	
		
		
		
		return gsonb.create();
	}
	
	public static String convertObjectToJSon(Object obj){
		
		Gson gson = getObjetoGson();    
		
		return gson.toJson(obj); 
	}
	
	/**
	 * Convierte una cadena JSon en un objeto de tipo @param type
	 * @param cadJSon
	 * @param type
	 * @return
	 */
	public static Object convertJSonToObject(String cadJSon, Class type) throws JsonSyntaxException{
		Gson gson = getObjetoGson();     
		
		return  gson.fromJson(cadJSon, type);
	}
	
	/**
	 * Convierte una cadena JSon en un objeto de tipo BResult 
	 * @param cadJSon
	 * @param type El tipo de objeto que devuelve en el resultado el objeto Result
	 * @return Devuelve el objeto Bresult
	 */
	public static ResultGson convertJSonToResultGson(String cadJSon){
		Gson gson = getObjetoGson();     
	
		return (ResultGson)gson.fromJson(cadJSon, ResultGson.class); 
	}
	

	public static Object convertToBean(Object objJSON, Class clase){
		
		try {
			 String cadJSon = convertObjectToJSon(objJSON);
				Object obj = 	convertJSonToObject(cadJSon, clase);
			
				return obj;
			
			 
		} catch (Exception e) {
			logger.info(e + "");
		}
		return null;
		
	}
	
	
	public static Object convertToBean(ResultGson resultGson, Class clase){
		JSONObject dato;
		if(resultGson!=null){
			String cadJSon = convertObjectToJSon(resultGson);
			try {
				dato = new JSONObject(cadJSon);
				if(dato.existkey(UConstantes.ELEMENT_RESULT)){
				
					return convertJSonToObject(dato.get(UConstantes.ELEMENT_RESULT).toString(), clase);
				}
				else{
					return null; 
				}
				 
			} catch (JSONException e) {
				logger.info(e + "");
			}
		}
		return null;

		
	}
	public static List<Object> convertToList(ResultGson resultGson, Class clase){
		List<Object> listaResult = new ArrayList<>();
		if(resultGson!=null){
			String cadJSon = convertObjectToJSon(resultGson);
			JSONObject dato;
			try {
				dato = new JSONObject(cadJSon);
				if(dato.existkey(UConstantes.ELEMENT_RESULT)){
					Object objectoLista = dato.get(UConstantes.ELEMENT_RESULT);
					if(objectoLista instanceof JSONArray){
						JSONArray obj2 =  (JSONArray)objectoLista;
						if(obj2!=null){
							int tamanio = obj2.length(); 
							for(int i=0;i<tamanio;i++){
								JSONObject elemento= obj2.getJSONObject(i);
								if(elemento!=null){
									Object objElemento =  convertJSonToObject(elemento.toString(), clase);
									listaResult.add(objElemento);
								}
							}
						}
					}	
				}
				else{
					return null; 
				}
				
			} catch (JSONException e) {
				logger.info(e + "");
			}
		}
		return listaResult; 
	}
	
	@SuppressWarnings("unchecked")
	public static List<Object> convertJSonToArray(String cadJSon, Class type){
		List<Object> result = new ArrayList<>();
		try {
			Gson gson = getObjetoGson();     
			
			JsonParser parser = new JsonParser();
			JsonArray array = parser.parse(cadJSon).getAsJsonArray();
			
			for(int i=0;i<array.size();i++){
				Object obj = gson.fromJson(array.get(i), type);
				result.add(obj);
			}
			
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		return result;
	}	
	
	 /**
	  * @param lista
	  * @param type
	  * @return	: List<Object>
	  * @date	: 18/06/2012
	  * @time	: 15:22:51
	  * @author	: Gary Ayala Rojas
	  * @descripcion : Convierte un objeto Lista JSON a Lista de un tipo	
	 */
	public static List<Object> convertToArray(List lista,Class type){
		ResultGson resultGson = new ResultGson();
		resultGson.setResult(lista);
		
		return convertToList(resultGson, type);
	}
	
	 /**
	  * @param cadenaJson
	  * @return	: List<Object>
	  * @date	: 24/9/2015
	  * @time	: 16:54:07
	  * @author	: Diego A.
	  * @descripcion : Convierte cadena Json a ArrayList de Object	
	 */
	public static List<Object> fromJsonToList(String cadenaJson){
		List<Object> items = null;
		if(null==cadenaJson){
			return items;
		}
		return  new Gson().fromJson(cadenaJson, new TypeToken<List<Object>>(){}.getType());
	}
	
	public static <T> List<T> jsonToList(String data, T t ){
		return new Gson().fromJson(data,  new TypeToken<List<T>>(){}.getType());
	}
	
}
