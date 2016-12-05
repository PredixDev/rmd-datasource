package com.ge.predix.solsvc.experience.datasource.datagrid.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 212421693
 *
 */
public class SummaryKpiDataGridResponse {
	
	private List<SummaryKpiColumn> kpis= new ArrayList<SummaryKpiColumn>();
	private SiteKpiHealth overall = new SiteKpiHealth();
	/**
	 * @return the kpis
	 */
	public List<SummaryKpiColumn> getKpis() {
		return this.kpis;
	}
	/**
	 * @param kpis the kpis to set
	 */
	public void setKpis(List<SummaryKpiColumn> kpis) {
		this.kpis = kpis;
	}
	/**
	 * @return the overall
	 */
	public SiteKpiHealth getOverall() {
		return this.overall;
	}
	/**
	 * @param overall the overall to set
	 */
	public void setOverall(SiteKpiHealth overall) {
		this.overall = overall;
	}
	
	
	
}
