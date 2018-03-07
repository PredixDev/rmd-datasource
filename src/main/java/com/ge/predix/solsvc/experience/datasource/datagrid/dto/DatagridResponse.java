package com.ge.predix.solsvc.experience.datasource.datagrid.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * 
 * 
 * @author 212421693
 */
public class DatagridResponse {

	private List<Map<String, Object>> tableData = new ArrayList<Map<String, Object>>();
	private List<Column> columns = new ArrayList<Column>();

	/**
	 * 
	 * @return The tableData
	 */
	public List<Map<String, Object>> getTableData() {
		return this.tableData;
	}

	/**
	 * 
	 * @param tableData
	 *            The tableData
	 */
	public void setTableData(List<Map<String, Object>> tableData) {
		this.tableData = tableData;
	}

	/**
	 * 
	 * @return The columns
	 */
	public List<Column> getColumns() {
		return this.columns;
	}

	/**
	 * 
	 * @param columns
	 *            The columns
	 */
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

}
