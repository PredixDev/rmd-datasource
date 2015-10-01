package com.ge.predix.solsvc.experience.datasource.datagrid.dto;

/**
 * 
 * 
 * @author 212421693
 */
public class Column {
	
	private String field;

	private String type;

	private String inputType;

	private Double inputSize;

	private String label;
	
	private String uom;
	
	/**
	 * 
	 */
	public Column() {
		super();
	}
	
	/**
	 * @param field : value
	 * @param type : value
	 * @param inputType : value
	 * @param inputSize : value
	 * @param label : value
	 */
	public Column(String field, String type, String inputType,
			Double inputSize, String label) {
		super();
		this.field = field;
		this.type = type;
		this.inputType = inputType;
		this.inputSize = inputSize;
		this.label = label;
	
	}

	/**
	 * @param field : value
	 * @param type : value
	 * @param inputType : value
	 * @param inputSize : value
	 * @param label : value
	 * @param uom : value
	 */
	public Column(String field, String type, String inputType,
			Double inputSize, String label,String uom) {
		super();
		this.field = field;
		this.type = type;
		this.inputType = inputType;
		this.inputSize = inputSize;
		this.label = label;
		this.uom = uom;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return this.field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the inputType
	 */
	public String getInputType() {
		return this.inputType;
	}

	/**
	 * @param inputType the inputType to set
	 */
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	/**
	 * @return the inputSize
	 */
	public Double getInputSize() {
		return this.inputSize;
	}

	/**
	 * @param inputSize the inputSize to set
	 */
	public void setInputSize(Double inputSize) {
		this.inputSize = inputSize;
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