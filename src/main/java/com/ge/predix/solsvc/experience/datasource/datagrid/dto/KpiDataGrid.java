package com.ge.predix.solsvc.experience.datasource.datagrid.dto;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * 
 * @author 212421693
 */
public class KpiDataGrid {

	private List<String> siteName = new ArrayList<String>();
	private List<String> unitNumber = new ArrayList<String>();
	private List<Double> output = new ArrayList<Double>();
	private List<Double> heatRate = new ArrayList<Double>();
	private List<Double> compressorEfficiency = new ArrayList<Double>();
	
	/**
	 * 
	 * @return The siteName
	 */
	public List<String> getSiteName() {
		return this.siteName;
	}

	/**
	 * 
	 * @param siteName
	 *            The siteName
	 */
	public void setSiteName(List<String> siteName) {
		this.siteName = siteName;
	}

	/**
	 * 
	 * @return The unitNumber
	 */
	public List<String> getUnitNumber() {
		return this.unitNumber;
	}

	/**
	 * 
	 * @param unitNumber
	 *            The unitNumber
	 */
	public void setUnitNumber(List<String> unitNumber) {
		this.unitNumber = unitNumber;
	}

	/**
	 * 
	 * @return The output
	 */
	public List<Double> getOutput() {
		return this.output;
	}

	/**
	 * 
	 * @param output
	 *            The output
	 */
	public void setOutput(List<Double> output) {
		this.output = output;
	}

	/**
	 * 
	 * @return The heatRate
	 */
	public List<Double> getHeatRate() {
		return this.heatRate;
	}

	/**
	 * 
	 * @param heatRate
	 *            The heatRate
	 */
	public void setHeatRate(List<Double> heatRate) {
		this.heatRate = heatRate;
	}

	/**
	 * 
	 * @return The compressorEfficiency
	 */
	public List<Double> getCompressorEfficiency() {
		return this.compressorEfficiency;
	}

	/**
	 * 
	 * @param compressorEfficiency
	 *            The compressorEfficiency
	 */
	public void setCompressorEfficiency(List<Double> compressorEfficiency) {
		this.compressorEfficiency = compressorEfficiency;
	}
}
