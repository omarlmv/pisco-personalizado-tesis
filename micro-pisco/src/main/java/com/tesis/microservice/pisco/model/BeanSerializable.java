package com.tesis.microservice.pisco.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Proyecto: Domain
 * 
 * @date : 26/7/2017
 * @time : 14:39:46
 * @author : Erick vb.
 */
public class BeanSerializable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
