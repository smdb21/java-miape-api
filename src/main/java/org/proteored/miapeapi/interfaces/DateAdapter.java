package org.proteored.miapeapi.interfaces;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;

public class DateAdapter implements Adapter<XMLGregorianCalendar> {
	 
	private final Date date;
	private DatatypeFactory dataTypeFactory; 
	
	public DateAdapter(Date date) {
		this.date = date;
	}
	public DateAdapter(String date) {
		this.date = transformToDate(date);	
	}
	private Date transformToDate(String date) {
		Date result = null;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
		try {
			result = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IllegalMiapeArgumentException(e);
		}
		return result;
	}


	@Override
	public XMLGregorianCalendar adapt() {
		try {
			this.dataTypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			this.dataTypeFactory = null;
		}

		if (dataTypeFactory == null) return null;
		if (date== null) return null;

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
		 

		
		return  dataTypeFactory.newXMLGregorianCalendar((GregorianCalendar)calendar);
	}
}
