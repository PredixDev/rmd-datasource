package com.ge.predix.solsvc.experience.datasource.datagrid.dto;

/**
 * 
 * @author 212421693
 *
 */
public class SummaryKpiColumn {
	
	private String value;
	private String label;
	private String uom;
	
	/**
	 * 
	 */
	public SummaryKpiColumn() {
		super();
	}
	
	
	/**
	 * @param value : value 
	 * @param label : label 
	 * @param uom2 : uom2
	 */
	public SummaryKpiColumn(String value, String label, String uom2) {
		super();
		this.value = value;
		this.label = label;
		this.uom = uom2;
	}


	/**
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}


	/**
	 * @return the label
	 */
	public String getLabel() {
		return this.label;
	}


	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}


	/**
	 * @return the uom
	 */
	public String getUom() {
		return this.uom;
	}


	/**
	 * @param uom the uom to set
	 */
	public void setUom(String uom) {
		this.uom = uom;
	}

	
	
	
}
