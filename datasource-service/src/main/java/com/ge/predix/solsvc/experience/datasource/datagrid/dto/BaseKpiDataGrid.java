package com.ge.predix.solsvc.experience.datasource.datagrid.dto;

/**
 * 
 * @author 212421693
 *
 */
public class BaseKpiDataGrid {

	private String meter;
	private String meterUri;
	private Boolean meter_isKpi;
	private Boolean meter_isPM;

	private Boolean alertStatus = Boolean.FALSE;

	private String alertStatusType;

	private Double currentValue;
	private String unit;
	private Long lastMeterReading;
	/**
	 * @return the meter
	 */
	public String getMeter() {
		return this.meter;
	}
	/**
	 * @param meter the meter to set
	 */
	public void setMeter(String meter) {
		this.meter = meter;
	}
	/**
	 * @return the meterUri
	 */
	public String getMeterUri() {
		return this.meterUri;
	}
	/**
	 * @param meterUri the meterUri to set
	 */
	public void setMeterUri(String meterUri) {
		this.meterUri = meterUri;
	}
	/**
	 * @return the meter_isKpi
	 */
	public Boolean getMeter_isKpi() {
		return this.meter_isKpi;
	}
	/**
	 * @param meter_isKpi the meter_isKpi to set
	 */
	public void setMeter_isKpi(Boolean meter_isKpi) {
		this.meter_isKpi = meter_isKpi;
	}
	/**
	 * @return the meter_isPM
	 */
	public Boolean getMeter_isPM() {
		return this.meter_isPM;
	}
	/**
	 * @param meter_isPM the meter_isPM to set
	 */
	public void setMeter_isPM(Boolean meter_isPM) {
		this.meter_isPM = meter_isPM;
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
	 * @return the lastMeterReading
	 */
	public Long getLastMeterReading() {
		return this.lastMeterReading;
	}
	/**
	 * @param lastMeterReading the lastMeterReading to set
	 */
	public void setLastMeterReading(Long lastMeterReading) {
		this.lastMeterReading = lastMeterReading;
	}

	

}
