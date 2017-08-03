package org.proteored.miapeapi.interfaces;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;

/**
 * Class that formats the date in yyy-MM-dd format
 * 
 * @author Salvador
 * 
 */
public class MiapeDate {
	private String date;
	private Date asDate;

	public MiapeDate(String year, String month, String day) {
		this.date = validateDate(day, month, year);
	}

	/**
	 * Time in millliseconds
	 * 
	 * @param time
	 */
	public MiapeDate(long time) {
		this(new Date(time));
	}

	/**
	 * 
	 * @param date
	 *            in format "yyyy-MM-dd" or "yyyy-MM-dd HH:mm:ss:S"
	 */
	public MiapeDate(String date) {
		String day;
		String month;
		String year;
		String date1;
		String date2;
		String hour;
		String minute;
		String second;
		String regexp;
		String regexp2;
		String regexp3;
		try {
			regexp = "^(\\d+)-(\\d\\d)-(\\d\\d).*(\\d\\d):(\\d\\d):(\\d\\d).*";
			regexp2 = "^(\\d+)-(\\d\\d)-(\\d\\d).*";

			regexp3 = "^(\\d+)-(\\d\\d)-(\\d\\d)T(\\d\\d):(\\d\\d):(\\d\\d).*";
			if (Pattern.matches(regexp, date)) {
				Pattern p = Pattern.compile(regexp);
				Matcher m = p.matcher(date);
				if (m.find()) {
					year = m.group(1);
					month = m.group(2);
					day = m.group(3);
					hour = m.group(4);
					minute = m.group(5);
					second = m.group(6);

					this.date = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String cadena = this.date;
					this.asDate = formatter.parse(cadena);
				}
			} else if (Pattern.matches(regexp2, date)) {
				Pattern p = Pattern.compile(regexp2);
				Matcher m = p.matcher(date);
				if (m.find()) {
					year = m.group(1);
					month = m.group(2);
					day = m.group(3);

					this.date = year + "-" + month + "-" + day;
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String cadena = this.date;
					this.asDate = formatter.parse(cadena);

				}
			} else if (Pattern.matches(regexp3, date)) {
				Pattern p = Pattern.compile(regexp3);
				Matcher m = p.matcher(date);
				if (m.find()) {
					year = m.group(1);
					month = m.group(2);
					day = m.group(3);
					hour = m.group(4);
					minute = m.group(5);
					second = m.group(6);

					this.date = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String cadena = this.date;
					this.asDate = formatter.parse(cadena);
				}
			} else {

				// "yyyy-MM-dd"
				if (date.length() > 10) {
					date = date.substring(0, 10);

				}
				day = date.substring(8);
				month = date.substring(5, 7);
				year = date.substring(0, 4);
				this.date = validateDate(day, month, year);
				return;

			}
		} catch (ParseException e) {
			throw new IllegalMiapeArgumentException(e.getMessage());
		} catch (IllegalMiapeArgumentException e) {
			throw e;
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalMiapeArgumentException("The format yyyy-MM-dd");
		}
	}

	public MiapeDate(Date date) {
		if (date != null) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(date);
			this.date = dateString;
			this.asDate = date;
		} else {
			this.date = null;
			this.asDate = null;
		}

	}

	private String validateDate(String day, String month, String year) {
		if (year == null || month == null || day == null) {
			throw new IllegalMiapeArgumentException("Some of the parameters is null");
		}
		String result = "";
		if (year.length() != 4) {
			throw new IllegalMiapeArgumentException("The year must have the format YYYY");
		}
		if (month.length() != 2) {
			throw new IllegalMiapeArgumentException("The month must have the format MM");
		}
		if (day.length() != 2) {
			throw new IllegalMiapeArgumentException("The day must have the format DD");
		}

		try {
			Integer.parseInt(year);
			int monthInteger = Integer.parseInt(month);
			int dayInteger = Integer.parseInt(day);

			if (monthInteger > 12) {
				throw new IllegalMiapeArgumentException("The month has a value higher than 12");
			}

			if (dayInteger > 31) {
				throw new IllegalMiapeArgumentException("The day has a value higher than 31");
			}

			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			result = year + "-" + month + "-" + day;
			asDate = formatter.parse(result);

		} catch (NumberFormatException e) {
			throw new IllegalMiapeArgumentException("The date must have numerical values");
		} catch (ParseException e) {
			throw new IllegalMiapeArgumentException(e);
		}
		return result;
	}

	public String getValue() {
		return date;
	}

	public Date toDate() {
		return asDate;
	}

	@Override
	public String toString() {
		return "MiapeDate [date=" + date + "]";
	}

	public XMLGregorianCalendar toXMLGregorianCalendar() throws DatatypeConfigurationException {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(asDate);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	}

}
