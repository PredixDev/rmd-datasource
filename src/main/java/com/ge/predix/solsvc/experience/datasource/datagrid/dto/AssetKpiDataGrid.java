package com.ge.predix.solsvc.experience.datasource.datagrid.dto;


/**
 * 
 * @author 212421693
 *
 */
public class AssetKpiDataGrid extends BaseKpiDataGrid {
	
	private Double thresholdMin ;
	private Double thresholdMax ;
	private Double deltaThreshold ;
	private String deltaThresholdColor ;
	private String deltaThresholdLevel ;
	/**
	 * @return the thresholdMin
	 */
	public Double getThresholdMin() {
		return this.thresholdMin;
	}
	/**
	 * @param thresholdMin the thresholdMin to set
	 */
	public void setThresholdMin(Double thresholdMin) {
		this.thresholdMin = thresholdMin;
	}
	/**
	 * @return the thresholdMax
	 */
	public Double getThresholdMax() {
		return this.thresholdMax;
	}
	/**
	 * @param thresholdMax the thresholdMax to set
	 */
	public void setThresholdMax(Double thresholdMax) {
		this.thresholdMax = thresholdMax;
	}
	/**
	 * @return the deltaThreshold
	 */
	public Double getDeltaThreshold() {
		return this.deltaThreshold;
	}
	/**
	 * @param deltaThreshold the deltaThreshold to set
	 */
	public void setDeltaThreshold(Double deltaThreshold) {
		this.deltaThreshold = deltaThreshold;
	}
	/**
	 * @return the deltaThresholdColor
	 */
	public String getDeltaThresholdColor() {
		return this.deltaThresholdColor;
	}
	/**
	 * @param deltaThresholdColor the deltaThresholdColor to set
	 */
	public void setDeltaThresholdColor(String deltaThresholdColor) {
		this.deltaThresholdColor = deltaThresholdColor;
	}
	/**
	 * @return the deltaThresholdLevel
	 */
	public String getDeltaThresholdLevel() {
		return this.deltaThresholdLevel;
	}
	/**
	 * @param deltaThresholdLevel the deltaThresholdLevel to set
	 */
	public void setDeltaThresholdLevel(String deltaThresholdLevel) {
		this.deltaThresholdLevel = deltaThresholdLevel;
	}
	
	

}
