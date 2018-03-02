package org.proteored.miapeapi.text.tsv.msi;

public enum TableTextFileSeparator {
	TAB("\t"), COMMA(","), SEMICOLON(";"), COLON(":");
	private final String symbol;

	private TableTextFileSeparator(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public static String valuesString() {
		StringBuilder sb = new StringBuilder();
		for (TableTextFileSeparator separator : TableTextFileSeparator.values()) {
			if (!"".equals(sb.toString())) {
				sb.append(", ");
			}
			if (separator == TAB) {
				sb.append("'<i>tab</i>'");
			} else {
				sb.append("'" + separator.getSymbol() + "'");
			}
		}
		return sb.toString();
	}

	public static TableTextFileSeparator[] valuesWithBlank() {
		TableTextFileSeparator[] ret = new TableTextFileSeparator[values().length + 1];
		ret[0] = null;
		for (int i = 0; i < values().length; i++) {
			ret[i + 1] = values()[i];
		}
		return ret;
	}
}
