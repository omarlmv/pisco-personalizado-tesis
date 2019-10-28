package com.tesis.microservice.pisco.comun;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UUtil {
	private static Logger logger = LoggerFactory.getLogger(UUtil.class);
	private UUtil() {

	}

	/**
	 * Metodo que retorna null si no soporta
	 * 
	 * @param searchOper
	 * @param searchField
	 * @param searchString
	 * @return Object[] o null
	 */
	public static Object[] tipoConsAndVariable(String searchOper,
			String searchField, String searchString) {		
		Object[] var = null;
		if (searchOper != null && searchString != null) {
			String variable ;
			Integer var_all ;
			var = new Object[2];
			switch (searchOper) {
			case "eq":
				var_all = 3;
				variable = searchString;
				var[0] = var_all;
				var[1] = variable;
				break;
			case "bw":
				var_all = 4;
				variable = new StringBuilder().append("%").append(searchString)
						.toString();
				var[0] = var_all;
				var[1] = variable;
				break;
			case "bn":
				var = null;
				break;
			case "ew":
				var_all = 4;
				variable = new StringBuilder().append(searchString).toString();
				var[0] = var_all;
				var[1] = variable;
				break;
			case "en":
				var = null;
				break;
			case "cn":
				var_all = 4;
				variable = new StringBuilder().append("%").append(searchString).append("%").toString();
				var[0] = var_all;
				var[1] = variable;
				break;
			case "nc":
				var = null;
				break;
			case "nu":
				var = null;
				break;
			case "nn":
				var = null;
				break;
			case "in":
				var = null;
				break;
			case "ni":
				var = null;
				break;
			default:
				var = null;
				break;
			}

		}
		return var;
	}

	public static Integer[] calculaInicioTotalPag(Integer total, Integer page,
			Integer limit) {
		Integer totalPages = 1;
		if (total > 0) {
			double myTotal = Double.parseDouble(total.toString());
			double myLimit = Double.parseDouble(limit.toString());

			totalPages = (int) Math
					.ceil(myTotal / (myLimit <= 0 ? 1 : myLimit));

			if (totalPages == 0)
				totalPages = 1;
		}

		if (page > totalPages)
			page = totalPages;
		Integer start = limit * page - limit;
		Integer[] inicioAndTotalPages = { start, totalPages };
		return inicioAndTotalPages;
	}

	public static BigDecimal formateaDecimal(BigDecimal numero,
			Integer decimales) {
		if (numero != null) {
			return numero.setScale(decimales, BigDecimal.ROUND_HALF_EVEN);
		}

		return numero;
	}

	public static String str(Object obj) {
		if (obj != null) {
			return obj.toString().trim();
		}
		return StringUtils.EMPTY;
	}

	public static String extraeTildes(String cadena) {
		if (cadena != null) {
			return StringUtils.stripAccents(cadena);
		}
		return StringUtils.EMPTY;
	}
	
	public static Integer entero(Object obj) {
		if (obj != null) {
			try {
				if (obj instanceof Integer) {
					int val = ((Integer) obj).intValue();
					if (val >= 0)
						return val;
				} else {
					if (obj instanceof String) {
						return Integer.parseInt(obj.toString());
					} else if (obj instanceof BigDecimal) {
						return Integer.parseInt(obj.toString());
					} else if (obj instanceof Long) {
						return (int) Long.parseLong(obj.toString());
					} else if (obj instanceof Double) {
						return (int) Double.parseDouble(obj.toString());
					} else if (obj instanceof Float) {
						return (int) Double.parseDouble(obj.toString());

					} else {
						return Integer.parseInt(obj.toString());
					}

				}
			} catch (NumberFormatException e) {
				return -1;
			}
		}
		return -1;
	}

	public static BigInteger bigInt(Object obj) {
		String sNum ;
		String sNumFin ;
		if (obj != null && !obj.toString().equals(StringUtils.EMPTY)) {
			if (obj instanceof BigInteger) {
				sNum = ((BigInteger) obj).toString();
			} else {
				sNum = obj.toString();
			}
			sNumFin = sNum;
			try {
				return new BigInteger(sNumFin);
			} catch (Exception e) {
				logger.error("exception", e);
				
			}
		}
		return null;
	}

	public static Long lng(Object obj) {
		if (obj != null) {
			try {
				if (!obj.toString().trim().equals(StringUtils.EMPTY))
					return new Long(obj.toString().trim());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String removeBlankSpaces(String param) {
		if (param != null)
			return param.toString().replaceAll(" ", StringUtils.EMPTY);
		else
			return param;
	}

	public static String trim(Object obj) {
		if (obj != null)
			return obj.toString().trim();
		return null;
	}

	public static boolean eq(Object obj1, Object obj2) {
		if (obj1 == obj2)
			return true;
		if (obj1 == null || obj2 == null) {
			String s1 = obj1 != null ? obj1.toString().trim() : obj2.toString()
					.trim();
			return s1.equals(StringUtils.EMPTY);
		}
		String sObj1 = obj1.toString();
		String sObj2 = obj2.toString();
		while (sObj1.indexOf("  ") > -1)
			sObj1 = sObj1.replaceAll("  ", " ");
		while (sObj2.indexOf("  ") > -1)
			sObj2 = sObj2.replaceAll("  ", " ");
		if (sObj1.trim().equals(sObj2.trim()))
			return true;
		if (obj1.equals(obj2))
			return true;
		return false;
	}

	// public static String formatNumber(Object obj, int decimales) {
	// String sNum = null;
	// String sNumFin = null;
	// if (obj != null && !obj.toString().equals(StringUtils.EMPTY)) {
	// if (obj instanceof BigDecimal) {
	// sNum = ((BigDecimal)obj).toPlainString();
	// } else {
	// sNum = obj.toString();
	// }
	// sNumFin = recortaNumero(sNum);
	// try {
	// return sNumFin;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return null;
	// }

	public static Object validaNull(Object objeto) {
		String cad = StringUtils.EMPTY;
		Object obj = cad;
		if (objeto != null) {
			obj = objeto;
		}
		return obj;
	}

	public static BigDecimal validaNull(BigDecimal objeto) {
		BigDecimal obj;
		if (objeto != null) {
			obj = objeto;
		} else {
			obj = new BigDecimal(0);
		}
		return obj;
	}

	public static String validaNull(String objeto) {
		String cad;
		if (objeto != null) {
			cad = objeto;
		} else {
			cad = StringUtils.EMPTY;
		}
		return cad;
	}

	public static String leerTagXml(Element element, String tagXml, int i) {
		NodeList title = element.getElementsByTagName(tagXml);
		Element line = (Element) title.item(i);
		Node child = line.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return null;
	}

	public static boolean validarSchemaXml(String rutaXml, String rutaXsd) {
		try {
			URL schemaFile = new URL(rutaXsd);
			Source xmlFile = new StreamSource(new File(rutaXml));
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			try {
				validator.validate(xmlFile);
				return true;
			} catch (SAXException e) {
				return false;
			}
		} catch (Exception e) {
			logger.error("exception", e);
			return false;
		}
	}

	public static void eliminarArchivo(URL url, String nomFile) {
		File f = new File(url.getPath());
		String[] listaArchivos = f.list();
		for (int i = 0; i < listaArchivos.length; i++) {
			File fAux = new File(url.getPath() + listaArchivos[i]);
			if (!listaArchivos[i].equals(nomFile)) {
				fAux.delete();
			}
		}
	}

	/**
	 * Metodo que genera un arreglo de strings a partir de un caracter separador
	 * 
	 * @param linea
	 *            : trama a leer
	 * @param separador
	 *            : separador de trama
	 * @return cLinea : arreglo de strings
	 */
	public static String[] toArray(String linea, String separador) {
		StringTokenizer Parser = new StringTokenizer(linea, separador, false);
		int tamanio = Parser.countTokens();
		String[] cLinea = new String[tamanio];
		for (int i = 0; i < tamanio; i++)
			cLinea[i] = Parser.nextToken();
		return cLinea;
	}

	/**
	 * Resumen Metodo que redondea los decimales de los numeros.
	 *
	 * @param cantidad
	 *            : Numero que se desea redondear, tipo double.
	 * @param dec
	 *            : Cantidad de decimales que se desea tener en el numero, tipo
	 *            int.
	 * @return cantRedon : Retorna el numero redondeado, tipo double.
	 */
	public static Double redondear(Double number, int dec) {
		//double cantRedon = 0;
		
//		 if (String.valueOf(cantidad).toLowerCase().contains("e")) {
//			 
//			 cantidad = cantidad.
//		 }
		/*
		 BigDecimal aDecimal = new BigDecimal(number);
		 BigDecimal another = aDecimal.setScale(dec, aDecimal.ROUND_HALF_UP);*/
		    
//		cantRedon = 
		return Math.round(number * Math.pow(10, dec))/ Math.pow(10, dec);
	}
	
	public static boolean isNumericRE(String cadena) {
		return cadena.matches("-?\\d+(\\.\\d+)?");
	}

	public static boolean isNumeric(String cadena) {
		try {
			if(NumberUtils.isNumber(cadena)){
				return true;
			}
			
		} catch (NumberFormatException nfe) {
			return false;
		}
		
		return false;
	}
	
	public static boolean isDigits(String cadena) {
		try {
			if(NumberUtils.isDigits(cadena)){
				return true;
			}
			
		} catch (NumberFormatException nfe) {
			return false;
		}
		
		return false;
	}

	public static boolean isDouble(String cadena) {
		try {
			Double.parseDouble(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public static Character stringToChar(String string) {
		if (string != null) {
			return string.charAt(0);
		}
		return null;
	}

	public static String formatearDireccion(String departamento, String distrito, String direccion,String direccionInterior,String manzana,String lote,String numero){
		String direccionTotal = "";
		//Av. Los cedros 122, Miraflores, Lima, Lima. 
		direccionTotal = (manzana == null ? "manzana": " Mz. " + manzana) + 
						 (manzana == null ? "departamento": " Dept. " + departamento)+
						 (manzana == null ? "distrito": " Dist. " + distrito)+
						 (manzana == null ? "direccion": " Dir. " + direccion)+
						 (manzana == null ? "direccionInterior": " DirI. " + direccionInterior)+
						 (manzana == null ? "lote": " Lt. " + lote)+
						 (manzana == null ? "numero": " Nro. " + numero);
		
		return direccionTotal;
	}
	
	public static String formatearMoneda(String forma, Double numero) {
		if (null == forma) {
			return String.valueOf(numero);
		}
		if(null == numero){
			return "0";
		}
		char separadorDecimal = forma.charAt(0);
		char separadorMiles = forma.charAt(1);
		String formato = forma.substring(2, forma.length());
		DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
		unusualSymbols.setDecimalSeparator(separadorDecimal);
		unusualSymbols.setGroupingSeparator(separadorMiles);
		
		String strange = formato;
		DecimalFormat weirdFormatter = new DecimalFormat(strange,unusualSymbols);
		return weirdFormatter.format(numero);
	}
	
	public static String formatearPuntos(Integer numero) {
		if (null == UConstantes.FORMATO_PUNTOS) {
			return String.valueOf(numero);
		}
		if(null == numero){
			return "0";
		}
		char separadorDecimal = UConstantes.FORMATO_PUNTOS.charAt(0);
		char separadorMiles = UConstantes.FORMATO_PUNTOS.charAt(1);
		String formato = UConstantes.FORMATO_PUNTOS.substring(2, UConstantes.FORMATO_PUNTOS.length());
		DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
		unusualSymbols.setDecimalSeparator(separadorDecimal);
		unusualSymbols.setGroupingSeparator(separadorMiles);
		String strange = formato;
		DecimalFormat weirdFormatter = new DecimalFormat(strange,unusualSymbols);
		return weirdFormatter.format(numero);
	}

	public static String formatearMoneda(String forma, BigDecimal numero) {
		if (null == forma) {
			return String.valueOf(numero);

		}
		char separadorDecimal = forma.charAt(0);
		char separadorMiles = forma.charAt(1);
		String formato = forma.substring(2, forma.length());
		DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
		unusualSymbols.setDecimalSeparator(separadorDecimal);
		unusualSymbols.setGroupingSeparator(separadorMiles);
		String strange = formato;
		DecimalFormat weirdFormatter = new DecimalFormat(strange,
				unusualSymbols);

		return weirdFormatter.format(numero.doubleValue());
	}

	/**
	 * @param urlList
	 * @param url
	 * @return : boolean
	 * @date : Mar 6, 2012
	 * @time : 3:50:12 PM
	 * @author : Julio Novoa
	 * @descripcion : Verifica si la cadena existe en la lista de strings
	 */
	public static boolean contiene(List<String> urlList, String url) {
		if (urlList != null) {
			Iterator<String> listIterator = urlList.iterator();
			while (listIterator.hasNext()) {
				String urlActual = (String) listIterator.next();
				if (url.contains(urlActual)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param data
	 * @param separador
	 * @return : String
	 * @date : May 3, 2012
	 * @time : 3:48:59 PM
	 * @author : erick vb
	 * @descripcion :
	 */
	public static String formatArregloToCadena(String data[], String separador) {

		int i = 0;
		int total = data.length;

		String format = null;
		StringBuilder cadena = new StringBuilder();
		for (i = 0; i < total; i++) {
			cadena.append(data[i]);
			if (i < (total - 1)) {
				cadena.append(separador);
			}
		}
		format = cadena.toString();
		return format;

	}

	public static String formatListaToCadena(List<String> data,
			String separador) {
		int i = 0;
		int total = data.size();

		String format = null;
		StringBuilder cadena = new StringBuilder();
		for (i = 0; i < total; i++) {
			cadena.append(data.get(i));
			if (i < (total - 1)) {
				cadena.append(separador);
			}
		}
		format = cadena.toString();
		return format;
	}
	
	public static String formatListaToCadena(List<Integer> data,
			String separador, boolean enteros) {
		int i = 0;
		int total = data.size();

		String format = null;
		StringBuilder cadena = new StringBuilder();
		for (i = 0; i < total; i++) {
			cadena.append(data.get(i));
			if (i < (total - 1)) {
				cadena.append(separador);
			}
		}
		format = cadena.toString();
		return format;
	}
	

	public static String normalizarTexto(String url) {
		// Descomposicion canonica
		if(url==null){
			return StringUtils.EMPTY;
		}
		String normalized = Normalizer.normalize(url.trim(), Normalizer.Form.NFD);
		// Nos quedamos unicamente con los caracteres ASCII
		Pattern pattern = Pattern.compile("\\P{ASCII}");
		return pattern.matcher(normalized).replaceAll("");
	}

	public static String reemplazarEspacio(String palabra, String replace) {
		return palabra.replaceAll(" ", replace);
	}

	/**
	 * @param sNum
	 * @param decimales
	 * @return : String
	 * @date : 07/08/2012
	 * @time : 12:49:55
	 * @author : Edwin Q.
	 * @descripcion : Completa ceros a la derecha de un decimal
	 */
	public static String completarCeros(String sNum, Integer decimales) {
		int pos = sNum.indexOf(".");
		if (pos != -1) {
			String dec = sNum.substring(pos + 1);
			dec = "0." + dec;
			BigDecimal sDec = new BigDecimal(dec);
			sNum = sNum.substring(0, pos + 1)
					+ sDec.toPlainString().substring(2);
			StringBuffer ini = new StringBuffer();
			ini.append(sDec.toPlainString().substring(2));

			int complete = decimales - ini.length();
			if (complete > 0) {
				for (int i = 1; i <= complete; i++) {
					ini.append("0");
				}
			}
			sNum = sNum.substring(0, pos + 1) + ini.toString();
		}
		return sNum;
	}

	public static Boolean isEmail(String correo) {
		Pattern pat = null;
		Matcher mat = null;
		String patter = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		// "^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$"
		pat = Pattern.compile(patter);
		mat = pat.matcher(correo);
		if (mat.find()) {
			return true;
		} else {
			return false;
		}
	}

	public static String reemplazarCaracteresExtras(String cadena) {
		String vacio = StringUtils.EMPTY;
		String expresion = "[^A-Za-z0-9\\.\\@_\\-~#]+";
		Pattern pattern = Pattern.compile(expresion);
		Matcher matcher = pattern.matcher(cadena);

		StringBuffer sb = new StringBuffer();
		boolean resultado = matcher.find();
		while (resultado) {
			matcher.appendReplacement(sb, vacio);
			resultado = matcher.find();
		}

		matcher.appendTail(sb);
		cadena = sb.toString();
		cadena = cadena.replace("#", vacio);
		cadena = cadena.replace(".", "-");
		return cadena;
	}

	/**
	 * @param string
	 * @return : String
	 * @date : 21/11/2012
	 * @time : 12:38:58
	 * @author : Gary Ayala Rojas
	 * @descripcion :
	 */
	public static String toCapitalize(String string) {
		string = string.trim();
		String[] strings = null;
		String stringToCapitalize = "";
		if (string.indexOf(" ") > 0) {
			strings = string.split(" ");
		} else {
			strings = new String[1];
			strings[0] = string;
		}
		for (String cadena : strings) {
			String tmp = cadena.trim().toUpperCase();
			if (tmp.length() > 1) {
				tmp = tmp.replace(tmp.substring(1), tmp.substring(1)
						.toLowerCase());
			}
			stringToCapitalize += tmp + " ";
		}
		return stringToCapitalize.trim();
	}

	public static String toUrlAmigable(String texto) {
		String cadena = reemplazarCaracteres(normalizarTexto(texto));
		return cadena;
	}

	private static String reemplazarCaracteres(String cadena) {
		//String vacio = StringUtils.EMPTY;
		String expresion = "[^A-Za-z0-9\\.\\@_\\~#]+";
		Pattern pattern = Pattern.compile(expresion);
		Matcher matcher = pattern.matcher(cadena);

		StringBuffer sb = new StringBuffer();
		boolean resultado = matcher.find();
		while (resultado) {
			matcher.appendReplacement(sb, "-");
			resultado = matcher.find();
		}

		matcher.appendTail(sb);
		cadena = sb.toString();
		cadena = cadena.replace("#", "-");
		return cadena;
	}

	public static String encriptCodigo(String codigo, String palabra) {

		String prefijo = palabra.substring(4, 8);
		Integer prefijoSuma = UUtil.entero(prefijo);
		Integer nuevoCodigo = prefijoSuma + UUtil.entero(codigo);
		Integer multiplo = nuevoCodigo * 11 + 792;
		String generado = prefijo + multiplo;
		return generado;
	}

	public static Integer decriptCodigo(String palabra) {

		// String palabra = UUtil.str(restar);
		String prefijo = palabra.substring(0, 4);
		String codigo = palabra.substring(4);
		Integer myEntero = UUtil.entero(codigo);
		if (myEntero == 0) {
			return 0;
		}

		Integer generado = Math.abs(myEntero - 792);
		if (generado % 11 == 0) {
			Integer multiplo = generado / 11;

			return Math.abs(multiplo - UUtil.entero(prefijo));
		} else {
			return 0;
		}
	}

	

	public static String getCodigoVenta(String idTransaccion) {
		return idTransaccion.substring(2);
	}

	/**
	 * @param email
	 * @return : Boolean
	 * @date : 29/10/2013
	 * @time : 20:48:56
	 * @author : Gary Ayala Rojas
	 * @descripcion : Valida un email
	 */
	public static Boolean validEmail(String email) {
		String  emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		return email.matches(emailPattern);
	}

	public static Boolean validarFiltroIn(String filtro) {
		String ruler = "[0-9.,]+";
		Pattern patternLocal = Pattern.compile(ruler);
		Matcher matcher = patternLocal.matcher(filtro);
		return matcher.matches();
	}

	public static Boolean validarNombreColumna(String nombre) {
		String ruler = "^(-)?^[a-zA-Z0-9\\._]+$";
		Pattern patternLocal = Pattern.compile(ruler);
		Matcher matcher = patternLocal.matcher(nombre);
		return matcher.matches();
	}

	private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

	public static String removeTagsHtml(String string) {
		if (string == null || string.length() == 0) {
			return string;
		}

		Matcher m = REMOVE_TAGS.matcher(string);
		return m.replaceAll("");
	}


	public static String toString(String data[], String separador) {
		return formatArregloToCadena(data, separador);
	}


	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,100})";
	private static final String CARACATER_ESPECIAL_PATTERN = "[^\\w\\s]";//[`~!@#$%^&*()_+[\\]\\\\;\',./{}|:\"<>?]

	/**
	 * Validate password with regular expression
	 * 
	 * @param password
	 *            password for validation
	 * @return true valid password, false invalid password
	 */
	public static boolean validarPasswordSeguro(final String password) {
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher;

		// pattern = Pattern.compile(PASSWORD_PATTERN);
		// ((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[-\\.@#$%]).{8,100})
		matcher = pattern.matcher(password);
		return matcher.matches();
	}

	
	public static String safeData(String unsafe, Boolean htmlScape){
		String safe = htmlScape?Jsoup.clean( (unsafe==null?"":unsafe) , Whitelist.basic()):unsafe;
		return safe;
	}
	
	public static boolean isCharacterOrNumber(String cadena){
		
		String patter = "^\\w*$";
		Pattern pat = Pattern.compile(patter);
		Matcher mat = pat.matcher(cadena);
		if (mat.find()) {
			return true;
		} else {
			return false;
		}		
		
	}
	
	
     /**
      * @param s
      * @return	: String
      * @description : convert from internal Java String format -> UTF-8
      * @date	: 18/5/2016
      * @time	: 17:35:38
      * @author	: Erick vb.  	
     */
    public static String convertToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
  
     /**
      * @param s
      * @return	: String
      * @description : convert from UTF-8 -> internal Java String format
      * @date	: 18/5/2016
      * @time	: 17:35:35
      * @author	: Erick vb.  	
     */
    public static String convertFromUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("ISO-8859-1"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
    
  
    public static String convertISO_8859_15(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), "ISO-8859-15");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
  
    

	public static void main(String argg[]){
//		Double numero = 121100454540.52;
//				
//		System.out.println(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, numero));
//		
//		System.out.println(UUtil.formatearPuntos(10000000));
//		String cadena = "7.062018073E9";
//		System.out.println(UUtil.isDigits(cadena));
//		System.out.println(UUtil.isNumeric(cadena));
//		System.out.println(UUtil.isDouble(cadena));		
		//System.out.println(UUtil.reemplazarCaracteresExtras(cadena));
		//System.out.println(UUtil.toUrlAmigable(cadena));
		//System.out.println(UUtil.reemplazarCaracteresExtras(UUtil.toUrlAmigable(cadena)));
//		System.out.println(UUtil.normalizarTexto(cadena));
//		Pattern patternCEspeciales = Pattern.compile(CARACATER_ESPECIAL_PATTERN);
//		Matcher matcherCEspeciales;
//		matcherCEspeciales = patternCEspeciales.matcher("1adasasd");
//		System.out.println("result: " + matcherCEspeciales.find());
		
//		if(!validarPasswordSeguro("Afasasa[34124141")){
//		}else{
//		}
		
		//System.out.println(formatearMoneda(UConstantes.FORMATO_MONEDA,12.56));
		System.out.println(isCharacterOrNumber("xx21454"));
	}
	
	public static int tipoVariable(Object obj){
		if(obj instanceof Double){
			return 1;
		}else if(obj instanceof Integer){
			return 2;
		}else if(obj instanceof Long){
			return 3;
		}else{
			return 0;
		}
	}
	
	public static List<Integer> cleanListInteger(String[] listInteger){
		List<Integer> listInt = new ArrayList<>();
		for (String integer : Arrays.asList(listInteger)) {
	    	Integer valid = UUtil.entero(integer);
	    	if(valid>0){
	    		listInt.add(valid);
	    	}
		}
		return listInt;
	}

	public static String limpiarBusquedaUrl(String busqueda){
		try {
			return busqueda.replaceAll("[^a-zA-Z0-9-éèëêáàäâåíìïîóòöôúùüûñç.]"," ").trim();
		}catch (Exception e) {
			return null;
		}
	}
	
	public static String formatKeyBusquedaParaFiltrado(String keyBusqueda){
		StringBuilder sbkeyBusqueda = null;
		
		try {
			String[] k = UUtil.limpiarBusquedaUrl(keyBusqueda).split(" ");
			sbkeyBusqueda = new StringBuilder();
			for (int i = 0; i < k.length; i++) {				
				if(k[i].length()>2){
					sbkeyBusqueda.append("%").append(k[i]).append("%");
					if(k.length > i+1 ){
						sbkeyBusqueda.append(",");
					}
				}
			}
			
			return sbkeyBusqueda.toString();
		}catch (Exception e) {
			return null;
		}
	}
	
	
	
	public static String formatearEspaciosCadena(String string){
		String cadena = string.trim();
		cadena = cadena.replaceAll(" +", " ");
		return cadena;
	}
	
	
	public static String obtenerDesdePosicion(String frase,String pattern) {
		String palabra = StringUtils.EMPTY;
		if(!StringUtils.isEmpty(frase)) {
			Integer posicion = frase.indexOf(pattern);
			if(posicion>-1) {
				palabra = frase.substring(posicion).toLowerCase();
			}
		}
		return palabra;
	}

	
	public static String fromNumberToLetter(Integer num){
		 String[] unidades = {"cero", "uno", "dos" ,"tres" ,"cuatro" ,"cinco" ,
		            "seis" ,"siete" ,"ocho" ,"nueve","diez"};
		        String[] especiales = {"once", "doce","trece","catorce", "quince", 
		            "diezciseis", "diecisiete", "dieciocho", "diecinueve"};
		        String[] decenas =  {"veinte", "treinta","cuarenta","cincuenta", "sesenta",
		            "setenta", "ochenta", "noventa"};
		String letra = null;  
		        if(num>=0 && num<11)
		        	letra = unidades[num];         
		        else if(num<20)
		        	letra =especiales[num-11];        
		         else if(num<100){
		            int unid = num % 10;
		            int dec = num/10;
		            if(unid == 0)
		            	letra = decenas[dec-2];                
		            else
		            	letra = decenas[dec-2] + " y " + unidades[unid];
		        }
		        else            
		        	letra = num+"";
		return letra;        
	}
	 /**
	  * @param precioVenta
	  * @param precioRegular
	  * @param precioCatalogo
	  * @return	: String
	  * @descripcion : Calcula porcentaje de descuento de producto
	  * @date	: 23 ene. 2019
	  * @time	: 17:20:01
	  * @author	: Omar mv.  	
	 */
	public static String calcularPorcentajeDescuentoProducto(Double precioVenta, Double precioRegular,Double precioCatalogo) {
		String porcentajeFinal = "";
		if ((precioVenta != null) && (precioRegular != null)) {
			Double percent = (UUtil.redondear(((precioRegular - precioCatalogo) / precioRegular) * 100, 0));
			porcentajeFinal = Integer.valueOf(percent.intValue()).toString();
		}
		return porcentajeFinal;
	}
	
}