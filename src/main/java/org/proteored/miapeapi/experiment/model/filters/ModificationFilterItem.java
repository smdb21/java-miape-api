package org.proteored.miapeapi.experiment.model.filters;

public class ModificationFilterItem {
	public static final String CONTAINING = "containing";
	public static final String NOT_CONTAINING = "do not containing";
	private LogicOperator operator;
	private String modifName;
	private boolean contain;
	private Integer number;

	public ModificationFilterItem(LogicOperator operator, String modifName, boolean contain,
			Integer number) {
		this.operator = operator;
		this.modifName = modifName;
		this.contain = contain;
		this.number = number;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ModificationFilterItem) {
			ModificationFilterItem item = (ModificationFilterItem) obj;
			if (this.modifName == null && item.modifName != null)
				return false;
			if (this.modifName != null && item.modifName == null)
				return false;
			if (this.modifName != null) {
				if (!modifName.equals(item.modifName))
					return false;
			}
			if (this.operator != null) {
				if (!this.operator.equals(item.operator))
					return false;
			} else if (item.operator != null) {
				return false;
			}
			if (this.contain && !item.contain)
				return false;
			if (!this.contain && item.contain)
				return false;
			if (this.number != item.number)
				return false;
			return true;
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		String ret = " ";
		String contain = "";
		if (this.contain)
			contain = CONTAINING;
		else
			contain = NOT_CONTAINING;

		if (number != null)
			ret += contain + " (" + number + " " + modifName + ")";
		else
			ret += contain + " (" + modifName + " one or more times)";

		return ret;
	}

	public LogicOperator getOperator() {
		return operator;
	}

	public void setOperator(LogicOperator operator) {
		this.operator = operator;
	}

	public String getModifName() {
		return modifName;
	}

	public void setModifName(String modifName) {
		this.modifName = modifName;
	}

	public boolean isContain() {
		return contain;
	}

	public void setContain(boolean contain) {
		this.contain = contain;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

}
