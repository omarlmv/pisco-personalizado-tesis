package com.plazapoints.saas.web.util;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import org.apache.axis.MessageContext;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.encoding.ser.BeanSerializer;
import org.apache.axis.server.AxisServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.helpers.AttributesImpl;


public class AxisObjectUtil {
	private static Logger logger = LoggerFactory.getLogger(AxisObjectUtil.class);
	public String serializeAxisObject(Object obj) throws IOException  {
	    if (obj == null) {
	        return null;
	    }
	   
	    	
	    StringWriter outStr = new StringWriter();
	    TypeDesc typeDesc = null;
		try {
			typeDesc = getAxisTypeDesc(obj);
		} catch (CustomException e) {
			logger.error("error", e);
		}
		if(null==typeDesc){
			return null;
		}
	    QName qname = typeDesc.getXmlType();
	    String lname = qname.getLocalPart();
	    if (lname.startsWith(">") && lname.length() > 1)
	        lname = lname.substring(1);
 
	    
	    qname =new QName(lname);
	    
	    AxisServer server = new AxisServer();
	    BeanSerializer ser = new BeanSerializer(obj.getClass(), qname, typeDesc);
	    SerializationContext ctx = new SerializationContext(outStr,
	            new MessageContext(server));
	    ctx.setSendDecl(false);
	    ctx.setDoMultiRefs(false);
	    ctx.setPretty(true);
	    ser.serialize(qname, new AttributesImpl(), obj, ctx);
	    
	    String xml = outStr.toString();
	    xml = xml.replaceAll(" xmlns[:=].*?\".*?\"", "").replaceAll(" xsi:type=\".*?\"", "");
	    return xml;
	  
	    
	}
 
	  
	private TypeDesc getAxisTypeDesc(Object obj) throws CustomException {
	    final Class<? extends Object> objClass = obj.getClass();
	  
	        Method methodGetTypeDesc;
			try {
				methodGetTypeDesc = objClass.getMethod("getTypeDesc",new Class[] {});
				 return (TypeDesc) methodGetTypeDesc.invoke(obj,    new Object[] {});
				 
			} catch (NoSuchMethodException | SecurityException |IllegalAccessException | IllegalArgumentException |InvocationTargetException e) {
				throw new CustomException("No parse metodo axis",e);
			}
	}

}

