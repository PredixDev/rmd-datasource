package com.ge.predix.solsvc.experience.datasource.datagrid.service;

import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.ge.predix.solsvc.experience.datasource.datagrid.api.KpiDatagridService;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.DatagridResponse;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.KpiDataGrid;

/**
 * KPI data grid integration
 * 
 * @author 212421693
 *
 */
@Component(value = "kpiDatagridService")
public class KpiDatagridServiceImpl implements KpiDatagridService {
	private final ObjectMapper mapper = new ObjectMapper(); // jackson's
															// objectmapper

	/**
	 * @return DatagridResponse
	 */
	@SuppressWarnings("unchecked")
	public DatagridResponse getDatagridResponseObject() {
		DatagridResponse response = new DatagridResponse();
		KpiDataGrid datagrid = new KpiDataGrid();
		datagrid.getSiteName().add(""); //$NON-NLS-1$
		datagrid.getCompressorEfficiency().add(new Double(25));
		datagrid.getCompressorEfficiency().add(new Double(-15));
		datagrid.getHeatRate().add(new Double(10));
		datagrid.getOutput().add(new Double(2));
		datagrid.getUnitNumber().add("UnitName"); //$NON-NLS-1$
		response.getTableData().add(
				getMapper().convertValue(datagrid, Map.class));

		return response;
	}

	@Override
	public Response getDatagrid(String entityType, String id,
			String authorization) throws Throwable {
		DatagridResponse datagrid = getDatagridResponseObject();
		return handleResult(datagrid);
	}

	/**
	 * @param entity
	 *            to be wrapped into JSON response
	 * @return JSON response with entity wrapped
	 */
	protected Response handleResult(Object entity) {
		ResponseBuilder responseBuilder = Response.status(Status.OK);
		responseBuilder.type(MediaType.APPLICATION_JSON);
		responseBuilder.entity(entity);
		return responseBuilder.build();
	}

	@Override
	public String ping() {
		return "SUCCESS"; //$NON-NLS-1$
	}

	/**
	 * @return the mapper
	 */
	public ObjectMapper getMapper() {
		return this.mapper;
	}

}
