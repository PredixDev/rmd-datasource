package com.ge.predix.solsvc.experience.datasource.datagrid.dto;

/**
 * 
 * @author 212421693
 *
 */
public class BaseKpiDataGrid {

	private String tag;
	private String tagUri;
	private Boolean tag_isKpi;
	private Boolean tag_isPM;

	private Boolean alertStatus = Boolean.FALSE;

	private String alertStatusType;

	private Double currentValue;
	private String unit;
	private Long lastTagReading;
	/**
	 * @return the tag
	 */
	public String getTag() {
		return this.tag;
	}
	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	/**
	 * @return the tagUri
	 */
	public String getTagUri() {
		return this.tagUri;
	}
	/**
	 * @param tagUri the tagUri to set
	 */
	public void setTagUri(String tagUri) {
		this.tagUri = tagUri;
	}
	/**
	 * @return the tag_isKpi
	 */
	public Boolean getTag_isKpi() {
		return this.tag_isKpi;
	}
	/**
	 * @param tag_isKpi the tag_isKpi to set
	 */
	public void setTag_isKpi(Boolean tag_isKpi) {
		this.tag_isKpi = tag_isKpi;
	}
	/**
	 * @return the tag_isPM
	 */
	public Boolean getTag_isPM() {
		return this.tag_isPM;
	}
	/**
	 * @param tag_isPM the tag_isPM to set
	 */
	public void setTag_isPM(Boolean tag_isPM) {
		this.tag_isPM = tag_isPM;
	}
	/**
	 * @return the alertStatus
	 */
	public Boolean getAlertStatus() {
		return this.alertStatus;
	}
	/**
	 * @param alertStatus the alertStatus to set
	 */
	public void setAlertStatus(Boolean alertStatus) {
		this.alertStatus = alertStatus;
	}
	/**
	 * @return the alertStatusType
	 */
	public String getAlertStatusType() {
		return this.alertStatusType;
	}
	/**
	 * @param alertStatusType the alertStatusType to set
	 */
	public void setAlertStatusType(String alertStatusType) {
		this.alertStatusType = alertStatusType;
	}
	/**
	 * @return the currentValue
	 */
	public Double getCurrentValue() {
		return this.currentValue;
	}
	/**
	 * @param currentValue the currentValue to set
	 */
	public void setCurrentValue(Double currentValue) {
		this.currentValue = currentValue;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return this.unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the lastTagReading
	 */
	public Long getLastTagReading() {
		return this.lastTagReading;
	}
	/**
	 * @param lastTagReading the lastTagReading to set
	 */
	public void setLastTagReading(Long lastTagReading) {
		this.lastTagReading = lastTagReading;
	}

	

}
