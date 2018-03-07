package com.ge.predix.solsvc.experience.datasource.datagrid.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ge.predix.solsvc.experience.datasource.datagrid.api.AssetDatagridService;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.AssetKpiDataGrid;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.BaseKpiDataGrid;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.Column;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.DatagridResponse;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.SummaryKpiDataGridResponse;
import com.ge.predix.solsvc.experience.datasource.handlers.DataSourceHandler;

/**
 * DatagridService implementation to fetch data for the widgets
 * 
 * @author 212421693
 *
 */
@Component(value = "assetDatagridService")
public class AssetDatagridServiceImpl extends DatagridServiceImpl implements
		AssetDatagridService {

	private static Logger log = LoggerFactory
			.getLogger(AssetDatagridServiceImpl.class);

	@Autowired
	private DataSourceHandler assetDataSourceHandler;

	/**
	 * 
	 */
	@Override
	public DatagridResponse getDatagridResponseObject() {
		DatagridResponse response = new DatagridResponse();
		response.getColumns().addAll(getColumnData());
		return response;
	}

	private List<Column> getColumnData() {

		List<Column> columns = new ArrayList<Column>();
		Column column = new Column(
				"tag", "string", "text", new Double(25), "Tag Name"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		columns.add(column);
		column = new Column(
				"alertStatus", "string", "text", new Double(20), "Alert Status"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		columns.add(column);
		column = new Column(
				"currentValue", "number", "text", new Double(20), "Current value"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		columns.add(column);
		column = new Column("unit", "number", "text", new Double(10), "Units"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		columns.add(column);
		column = new Column(
				"lastTagReading", "string", "text", new Double(10), "Last Tag Reading"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		columns.add(column);
		column = new Column(
				"thresholdMin", "number", "text", new Double(10), "Threshold Minimum"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		columns.add(column);
		column = new Column(
				"thresholdMax", "number", "text", new Double(10), "Threshold Maximum"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		columns.add(column);
		column = new Column(
				"deltaThreshold", "string", "text", new Double(25), "Delta Threshold "); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		columns.add(column);

		return columns;
	}

	@Override
	public AssetKpiDataGrid getKpiDataGrid(BaseKpiDataGrid groupKpi) {
		return (AssetKpiDataGrid) groupKpi;
	}

	@Override
	public DataSourceHandler getDataSourceHandler() {
		return this.getAssetDataSourceHandler();
	}

	@SuppressWarnings("nls")
	@Override
	public Response getDatagrid(String id, String appId, String authorization) {

		log.debug("calling GetResponse " + id); //$NON-NLS-1$
		return getResponse(id, appId, authorization, null, null);

	}

	@Override
	public String ping() {
		return "SUCCESS"; //$NON-NLS-1$
	}

	@SuppressWarnings("nls")
	@Override
	public Response getDatagridSummary(String id, String appId,
			String authorization, String start_time, String end_time) {

		if (getDataSourceHandler() == null)
			throw new RuntimeException("Datasource Handler is null");
		SummaryKpiDataGridResponse summarykpi = getDataSourceHandler()
				.getSummary(id, start_time, end_time, authorization);
		return handleResult(summarykpi);

	}

	/**
	 * @return the assetDataSourceHandler
	 */
	public DataSourceHandler getAssetDataSourceHandler() {
		return this.assetDataSourceHandler;
	}

	/**
	 * @param assetDataSourceHandler
	 *            the assetDataSourceHandler to set
	 */
	public void setAssetDataSourceHandler(
			DataSourceHandler assetDataSourceHandler) {
		this.assetDataSourceHandler = assetDataSourceHandler;
	}

}
