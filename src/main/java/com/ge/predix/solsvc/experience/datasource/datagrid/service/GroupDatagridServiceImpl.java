package com.ge.predix.solsvc.experience.datasource.datagrid.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ge.predix.solsvc.experience.datasource.datagrid.api.GroupDatagridService;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.BaseKpiDataGrid;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.Column;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.DatagridResponse;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.GroupKpiDataGrid;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.SummaryKpiDataGridResponse;
import com.ge.predix.solsvc.experience.datasource.handlers.DataSourceHandler;

/**
 * DatagridService implementation to fetch data for the widgets : fetches the
 * list of tags for the asset that have KPI metrics
 * 
 * @author 212421693
 *
 */
@Component(value = "groupDatagridService")
public class GroupDatagridServiceImpl extends DatagridServiceImpl implements
		GroupDatagridService {

	private static Logger log = LoggerFactory
			.getLogger(GroupDatagridServiceImpl.class);

	@Autowired
	private
	DataSourceHandler groupDataSourceHandler;

	@Override
	public DatagridResponse getDatagridResponseObject() {
		DatagridResponse response = new DatagridResponse();
		response.getColumns().addAll(getGroupColumnData());
		return response;
	}

	private List<Column> getGroupColumnData() {

		List<Column> columns = new ArrayList<Column>();
		Column column = new Column("asset", "string", "text", new Double(25),  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				ColumnsTags.getString("GroupDatagridServiceImpl.0")); //$NON-NLS-1$
		columns.add(column);
		column = new Column(ColumnsTags.getString("GroupDatagridServiceImpl.1"), ColumnsTags.getString("GroupDatagridServiceImpl.2"), ColumnsTags.getString("GroupDatagridServiceImpl.3"), new Double(20), ColumnsTags.getString("GroupDatagridServiceImpl.4")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		columns.add(column);
		column = new Column(ColumnsTags.getString("GroupDatagridServiceImpl.5"), ColumnsTags.getString("GroupDatagridServiceImpl.6"), ColumnsTags.getString("GroupDatagridServiceImpl.7"), new Double(20), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				ColumnsTags.getString("GroupDatagridServiceImpl.8")); //$NON-NLS-1$
		columns.add(column);
		column = new Column(ColumnsTags.getString("GroupDatagridServiceImpl.9"), ColumnsTags.getString("GroupDatagridServiceImpl.10"), ColumnsTags.getString("GroupDatagridServiceImpl.11"), new Double(10), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				ColumnsTags.getString("GroupDatagridServiceImpl.12")); //$NON-NLS-1$
		columns.add(column);
		column = new Column(ColumnsTags.getString("GroupDatagridServiceImpl.13"), ColumnsTags.getString("GroupDatagridServiceImpl.14"), ColumnsTags.getString("GroupDatagridServiceImpl.15"), new Double(10), ColumnsTags.getString("GroupDatagridServiceImpl.16")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		columns.add(column);
		column = new Column(ColumnsTags.getString("GroupDatagridServiceImpl.17"), ColumnsTags.getString("GroupDatagridServiceImpl.18"), ColumnsTags.getString("GroupDatagridServiceImpl.19"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				new Double(10), ColumnsTags.getString("GroupDatagridServiceImpl.20")); //$NON-NLS-1$
		columns.add(column);

		return columns;
	}

	
	@Override
	public Response getDatagrid(String id, String appId, String authorization,
			String start_time, String end_time) throws Throwable {
		log.debug(ColumnsTags.getString("GroupDatagridServiceImpl.21")+id); //$NON-NLS-1$
		return getResponse(id, appId, authorization, start_time, end_time);
	}

	@Override
	public GroupKpiDataGrid getKpiDataGrid(BaseKpiDataGrid groupKpi) {
		return (GroupKpiDataGrid) groupKpi;
	}

	@Override
	public DataSourceHandler getDataSourceHandler() {
		return this.getGroupDataSourceHandler();
	}
	
	@Override
	public String ping() {
		return ColumnsTags.getString("GroupDatagridServiceImpl.22"); //$NON-NLS-1$
	}

	@Override
	public Response getDatagridSummary(String id, String appId,
			String authorization, String start_time, String end_time)
			throws Throwable {
		log.debug(ColumnsTags.getString("GroupDatagridServiceImpl.23")+id); //$NON-NLS-1$
		return getSummary(id,appId,authorization,start_time,end_time);

	}
	
	
	/**
	 * @param id : id 
	 * @param appId : appId
	 * @param authorization : authorization
	 * @param start_time : start_time
	 * @param end_time : end_time
	 * @return Response -
	 */
	public Response getSummary(String id,
			String appId, String authorization,
			String start_time, String end_time) {
	
		SummaryKpiDataGridResponse summarykpi = getDataSourceHandler().getSummary(id, start_time,end_time,authorization);
			return handleResult(summarykpi);
	
	}

	/**
	 * @return the groupDataSourceHandler
	 */
	public DataSourceHandler getGroupDataSourceHandler() {
		return this.groupDataSourceHandler;
	}

	/**
	 * @param groupDataSourceHandler the groupDataSourceHandler to set
	 */
	public void setGroupDataSourceHandler(DataSourceHandler groupDataSourceHandler) {
		this.groupDataSourceHandler = groupDataSourceHandler;
	}

}
