package org.proteored.miapeapi.xml.gi.util;

import org.proteored.miapeapi.xml.gi.autogenerated.FuGECommonOntologyParamType;
import org.proteored.miapeapi.xml.gi.autogenerated.FuGECommonOntologyPropertyValue;
import org.proteored.miapeapi.xml.gi.autogenerated.ParamType;

 
public class Utils {
	public static String writeParam(FuGECommonOntologyParamType param) {
		StringBuilder sb = new StringBuilder();
		if (param == null) return null;
		if (param.getValue() == null) {
			sb.append(param.getName());
		} else {
			sb.append(param.getName());
			sb.append(": ");
			sb.append(param.getValue());
		}
		if (param.getUnitName() != null) {
			sb.append(" " + param.getUnitName());
		}
		return sb.toString();
	}

	public static String writeParam(ParamType paramType) {
		StringBuilder sb = new StringBuilder();
		if (paramType.getCvParam() != null) {
			sb.append(writeParam(paramType.getCvParam()));
		}
		
		if (paramType.getUserParam() != null) {
			if (paramType.getCvParam() != null) {
				sb.append(";");
			}
			sb.append(writeParam(paramType.getUserParam()));
		}

		return sb.toString();
	}

	public static String writeValue(
			FuGECommonOntologyPropertyValue param) {
		StringBuilder sb = new StringBuilder();
		if (param == null) return null;
		if (param.getValue() != null) {
			sb.append(param.getValue());
		}
		
		return sb.toString();
	}
	
	public static String writeUnit(
			FuGECommonOntologyPropertyValue param) {
		StringBuilder sb = new StringBuilder();
		if (param == null) return null;
		
		if (param.getUnitName() != null) {
			sb.append(param.getUnitName());
		}
		return sb.toString();
	}
}
