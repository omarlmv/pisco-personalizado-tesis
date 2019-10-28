package com.tesis.microservice.pisco.comun;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





 /**
  * Proyecto: SaasppCommon
  * @date	: 29/5/2015
  * @time	: 12:23:34
  * @author	: Erick Diaz.
 */
public class UDate {
	private static Logger logger = LoggerFactory.getLogger(UDate.class);
	private static String msgParse ="#parse date:";
	private static String msgParseMes = "#diaNombreMes:";
	private UDate(){
		
	}
	public static String toDateString(Date d) {
		if (d != null) {

			SimpleDateFormat dateFormat = new SimpleDateFormat(UConstantes.FORMATO_DATE_NORMAL);
			return dateFormat.format(d);
		}
		return "";
	}

	public static String getFechaActual() {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(UConstantes.FORMATO_DATE_NORMAL);
		Date fechaDate = new Date();
		return dateFormat.format(fechaDate);
	}

	public static String getFechaActual(String formato) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
		Date fechaDate = new Date();
		return dateFormat.format(fechaDate);
	}

	public static String getUltimoMes(String formato) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
		Date fechaDate = new Date();
		return dateFormat.format(sumarDias(fechaDate, -1));
	}
	
	public static String getMesAnio(String formato) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
		Date fechaDate = new Date();
		return dateFormat.format(fechaDate);
	}
	
	public static Date getDate(String fecha) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(UConstantes.FORMATO_DATE_NORMAL);
		
		try {
			return dateFormat.parse(fecha);
		} catch (ParseException e) {
			logger.error(msgParse,e);
		}
		return null;
	}

	public static Date toDate(String fecha, String formato) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
		
		try {
			return dateFormat.parse(fecha);
		} catch (ParseException e) {
			logger.error(msgParse,e);
		}
		return null;
	}
	
	public static String formatDate(Date date, String formato) {
	    if (date == null)
	        return null;
		SimpleDateFormat df1 = new SimpleDateFormat(formato);
		return df1.format(date);
	  }
	
	public static boolean fechaEnRango(Date fecha, Date rangoInicio, Date rangoFin) {
		if (fecha == null || rangoInicio == null || rangoFin == null) {
			return false;
		}

		long f = fecha.getTime();
		long ri = rangoInicio.getTime();
		long rf = rangoFin.getTime();

		if (f - ri >= 0 && rf - f >= 0) {
			return true;
		}
		return false;
	}

	public static boolean fechaMayorOIgualQue(Date fecha, Date aComparar) {
		long f = fecha.getTime();
		long ri = aComparar.getTime();

		if (f - ri >= 0) {
			return true;
		}
		return false;
	}

	public static boolean fechaIgualQue(Date fecha, Date aComparar) {
		long f = fecha.getTime();
		long ri = aComparar.getTime();

		if (f - ri == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean fechaMayorQue(Date fecha, Date aComparar) {
		long f = fecha.getTime();
		long ri = aComparar.getTime();

		if (f - ri > 0) {
			return true;
		}
		return false;
	}

	public static boolean horaEnRango(String hora, String horaInicio, String horaFin) throws ParseException {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("H:m");
		
		Long horaAux = dateFormat.parse(hora).getTime();
		Long horaInicioAux = dateFormat.parse(horaInicio).getTime();
		Long horaFinAux = dateFormat.parse(horaFin).getTime();

		if (horaAux >= horaInicioAux && horaAux <= horaFinAux) {
			return true;
		}
		return false;
	}

	public static String getFechaYHoraActual() {


		SimpleDateFormat dateFormat = new SimpleDateFormat("H:m:s dd/MM/yyyy");
		Date fecha = new Date();
		return dateFormat.format(fecha);
	}

	public static Date fechaMas(Date fch, int dias) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(fch.getTime());
		cal.add(Calendar.DATE, dias);
		return new Date(cal.getTimeInMillis());
	}

	public static Date fechaMenos(Date fch, int dias) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(fch.getTime());
		cal.add(Calendar.DATE, -dias);
		return new Date(cal.getTimeInMillis());
	}

	 /**
	  * @param d
	  * @return	: Date
	  * @description : Devuelve la fecha pasada por parametros pero sin horas ni minutos (fecha en hora cero)
		 el dia de hoy sin horas ni nada.
	  * @date	: 2/2/2017
	  * @time	: 14:37:10
	  * @author	: Erick vb.  	
	 */
	public static Date getToday(Date d) 
	{   
		GregorianCalendar ddate = new GregorianCalendar();
		ddate.setTime(d);
		GregorianCalendar ddateday = new GregorianCalendar(ddate.get(GregorianCalendar.YEAR), ddate.get(GregorianCalendar.MONTH), ddate.get(GregorianCalendar.DAY_OF_MONTH));
		return ddateday.getTime();
	}

	public static int getYearFromDate(Date date) {
		int result = -1;
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			result = cal.get(Calendar.YEAR);
		}
		return result;
	}

	 /**
	  * @param inicio 
	  * @param fin
	  * @return	: Integer
	  * @descripcion : 
	  * @date	: 4/12/2015
	  * @time	: 17:23:47
	  * @author	: Erick vb.  	
	 */
	public static Integer getDiferenciaDias(Date inicio, Date fin) {
		
		Long a = inicio.getTime();
		Long b = fin.getTime();

		Long dif = b - a;
		Long dias = 24L * 60L * 60L * 1000L;
		Double rst = Math.floor(dif / dias);
		return rst.intValue();
	}

	public static Integer getDias(Date inicio) {
		Long a = inicio.getTime();
		Long dias = 24L * 60L * 60L * 1000L;
		Double rst = Math.floor(a / dias);
		return rst.intValue();
	}

	public static Integer getDiferenciaHoras(Date inicio, Date fin) {
		Long a = inicio.getTime();
		Long b = fin.getTime();

		Long dif = b - a;
		Long horas = 60L * 60L * 1000L;
		Double rst = Math.floor(dif / horas);
		return rst.intValue();
	}

	public static Integer getHoras(Date fecha) {
		Long a = fecha.getTime();

		Long horas = 60L * 60L * 1000L;
		Long rst = a / horas;

		return rst.intValue();
	}

	public static Integer getMinutos(Date fecha) {
		Long a = fecha.getTime();

		Long minutos = 60L * 1000L;
		Long rst = a / minutos;

		return rst.intValue();
	}
	
	public static Date addMeses(Date date, int meses)
	{	Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.MONTH, meses);
	    return cal.getTime();
	}
	
	public static Date addAnios(Date date, int anios)
	{	Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.YEAR, anios);
	    return cal.getTime();
	}

	public static Date addDias(Date fecha, Integer sum) {
		Calendar c = Calendar.getInstance(); 
		c.setTime(fecha); 
		c.add(Calendar.DATE, sum);
		return c.getTime();
	}

	public static Date addHoras(Date fecha, Integer sum) {
		Long a = fecha.getTime();

		Long horas = 60L * 60L * 1000L;
		
		return  new Date(a + horas + sum);
	}

	public static Date addMinutos(Date fecha, Integer sum) {
		Long a = fecha.getTime();

		Long minutos = 60L * 1000L;
		
		return  new Date(a + minutos + sum);
	}
	
	 /**
	  * @param xmlGC
	  * @return	: java.util.Date
	  * @date	: 21/8/2015
	  * @time	: 15:26:26
	  * @author	: Erick vb.
	  * @descripcion : 	Converts an XMLGregorianCalendar to an instance of java.util.Date
	 */
	public static java.util.Date asDate(XMLGregorianCalendar xmlGC) {
		if(xmlGC == null) {
			return null;
		} else {
			return xmlGC.toGregorianCalendar().getTime();
		}
	}
	
	
	 /**
	  * @param date
	  * @return	: XMLGregorianCalendar
	  * @date	: 21/8/2015
	  * @time	: 15:26:12
	  * @author	: Erick vb.
	  * @descripcion : 	Converts a java.util.Date into an instance of XMLGregorianCalendar
	 */
	public static XMLGregorianCalendar asXMLGregorianCalendar(java.util.Date date) {
		 DatatypeFactory df = null;
		try {
			df = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			logger.error("#Exception DatatypeConfigurationException", e);
		}
		
		if(date == null) {
			return null;
		} else {
			if(null!=df){
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTimeInMillis(date.getTime());

				
				XMLGregorianCalendar start = df.newXMLGregorianCalendar(gc);
				start.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
				return start;
			}
			
		}
		
		return null;
	}
	
	public static String toDateUrl(Date d) {
		if (d != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(UConstantes.FORMATO_DATE_YYYY_MM_DD);
			return dateFormat.format(d);
		}
		return "";
	}

	public static String nombremes(int mes) {
		try {
			String[] meses = { "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO",
					"JUNIO", "JULIO", "AGOSTO", "SETIEMBRE", "OCTUBRE",
					"NOVIEMBRE", "DICIEMBRE" };
			return meses[mes - 1];
		} catch (Exception e) {
			logger.error("#Exception ex", e);
			
		}
		return "";
	}
	
	
	public static String diaSemana (int dia, int mes, int ano)
    {
        String diaSemana;
        TimeZone timezone = TimeZone.getDefault();
        Calendar calendar = new GregorianCalendar(timezone);
        calendar.set(ano, mes-1, dia);
        int nD=calendar.get(Calendar.DAY_OF_WEEK);
        switch (nD){
            case 1: diaSemana = "domingo";
                break;
            case 2: diaSemana = "lunes";
                break;
            case 3: diaSemana = "martes";
                break;
            case 4: diaSemana = "miercoles";
                break;
            case 5: diaSemana = "jueves";
                break;
            case 6: diaSemana = "viernes";
                break;
            case 7: diaSemana = "sabado";
                break;
			default:
				diaSemana = "";
				break;
        }

        return diaSemana;
    }
	
	public static String datePickerToDatePostgre(String cadena) {
		String retorno = "";
		SimpleDateFormat originalFormat = new SimpleDateFormat(
				UConstantes.FORMATO_DATE_NORMAL);
		SimpleDateFormat targetFormat = new SimpleDateFormat(
				UConstantes.FORMATO_DATE_YYYY_MM_DD);
		Date date;
		try {
		
			date = originalFormat.parse(cadena);
			retorno = targetFormat.format(date);

		} catch (ParseException ex) {
			logger.error(msgParse,ex);
		}
		return retorno;
	}
	
	public  static Date sumarDias(Date fecha, int dias){
	

		return addDias(fecha, dias);
	}

	public static String getHoraActual(){
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
		return sdf.format(new Date());
	}
	
	public static String diaNombreMes(Date fecha){
		String formato = null;
		Integer mes = null;
		String dia = null;
		try {
			dia = formatDate(fecha,"dd");
			mes = Integer.parseInt(formatDate(fecha,"MM"));
			formato = nombremes(mes).toLowerCase();
			return String.format("%s de %s", dia, formato);
		} catch (Exception ex) {
			logger.error(msgParseMes,ex);
			return null;
		}
		
	}
	
	
	public static String diaNombreMesCapitalize(Date fecha){
		String formato = null;
		Integer mes = null;
		String dia = null;
		try {
			dia = formatDate(fecha,"dd");
			mes = Integer.parseInt(formatDate(fecha,"MM"));
			formato = StringUtils.capitalize(nombremes(mes).toLowerCase());
			return String.format("%s de %s", dia, formato);
		} catch (Exception ex) {
			logger.error(msgParseMes,ex);
			return null;
		}
	}
	
	
	public static String nombreMesCapitalize(Date fecha){
		String formato = null;
		Integer mes = null;
		try {
			mes = Integer.parseInt(formatDate(fecha,"MM"));
			formato = StringUtils.capitalize(nombremes(mes).toLowerCase());
			return formato;
		} catch (Exception ex) {
			logger.error(msgParseMes,ex);
			return null;
		}
	}
	
	
	public static String diaNombreCapitalize(Date fecha){
		String formato = null;
		Integer anio = null;
		Integer mes = null;
		Integer dia = null;
		String diaCompleto = null;
		try {
			dia = Integer.parseInt(formatDate(fecha,"dd"));
			mes = Integer.parseInt(formatDate(fecha,"MM"));
			anio = Integer.parseInt(formatDate(fecha,"YYYY"));
			diaCompleto = ((diaSemana(dia, mes, anio).toLowerCase())+" "+dia).toString();
			
			formato = StringUtils.capitalize(nombremes(mes).toLowerCase());
			return String.format("%s de %s", diaCompleto, formato);
		} catch (Exception ex) {
			logger.error(msgParseMes,ex);
			return null;
		}
	}
	
	
	public static String diaNombreMesAnioCapitalize(Date fecha){
		String formato = null;
		String anio = null;
		Integer mes = null;
		String dia = null;
		try {
			dia = formatDate(fecha,"dd");
			mes = Integer.parseInt(formatDate(fecha,"MM"));
			anio = formatDate(fecha, "YYYY");
			formato = StringUtils.capitalize(nombremes(mes).toLowerCase())+" del "+anio;
			return String.format("%s de %s", dia, formato);
		} catch (Exception ex) {
			logger.error(msgParseMes,ex);
			return null;
		}
	}
	
	 public static Date convertUtcToLocal(String fechaUtc,  String toTimeZone) {
		 DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		 utcFormat.setTimeZone(TimeZone.getTimeZone(toTimeZone));

		 Date date = null;
		try {
			date = utcFormat.parse(fechaUtc);
		} catch (ParseException e) {
			logger.error(msgParseMes,e);
		}

		DateFormat pstFormat = new SimpleDateFormat(UConstantes.FORMATO_DATE_YYYY_MM_DD_HMS_24);
		return UDate.toDate(pstFormat.format(date),UConstantes.FORMATO_DATE_YYYY_MM_DD_HMS_24 );
	 }
	 
	 public static String fechaPaqueteCostamar(Date fecha){
			String formato = null;
			Integer mes = null;
			String dia = null;
			String anio = null;
			try {
				dia = formatDate(fecha,"dd");
				mes = Integer.parseInt(formatDate(fecha,"MM"));
				anio = String.valueOf(formatDate(fecha,"YYYY")).substring(2,4);
				formato = nombremes(mes).substring(0,1) + nombremes(mes).toLowerCase().substring(1, 3);
				return String.format("%s %s' %s", dia, formato,anio);
			} catch (Exception ex) {
				logger.error(msgParseMes,ex);
				return null;
			}
			
	}
	 
	 public static Date truncateTime (Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime( date);
	    cal.set( Calendar.HOUR_OF_DAY, 0);
	    cal.set( Calendar.MINUTE, 0);
	    cal.set( Calendar.SECOND, 0);
	    cal.set( Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}
		
	 
	public static String diferenciaFechasHoras(Date dinicio, Date dfinal){
		 
       
        Calendar cinicio = Calendar.getInstance();
        Calendar cfinal = Calendar.getInstance();

        cinicio.setTime(dinicio);
        cfinal.setTime(dfinal);

        long milis1 = cinicio.getTimeInMillis();
        long milis2 = cfinal.getTimeInMillis();

        long diff = milis2-milis1;

        long diffMinutos =  Math.abs (diff / (60 * 1000));
 
        long restominutos = diffMinutos%60;

        long diffHoras =   diff / (60 * 60 * 1000);

        String devolver = "";
        
        if(diffHoras > 0 && restominutos > 0) {

        	devolver = String.valueOf(diffHoras + "h y " + restominutos + "min");
        	
        }else if(diffHoras == 0 && restominutos > 0) {
        	devolver = String.valueOf(restominutos + "min");
        }else if(diffHoras > 0 && restominutos == 0) {
        	devolver = String.valueOf(diffHoras + "h");
        }
        return devolver;
	}
	
	
}
