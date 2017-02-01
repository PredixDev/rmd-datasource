package com.ge.predix.solsvc.experience.datasource.datagrid.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ge.predix.solsvc.experience.datasource.datagrid.dto.BaseKpiDataGrid;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.DatagridResponse;
import com.ge.predix.solsvc.experience.datasource.handlers.DataSourceHandler;

/**
 * DatagridService implementation to fetch data for the widgets
 * 
 * @author 212421693
 *
 */
@Component(value = "datagridService")
public abstract class DatagridServiceImpl
{

    @SuppressWarnings("unused")
    private static Logger      log          = LoggerFactory.getLogger(DatagridServiceImpl.class);

    private final ObjectMapper objectMapper = new ObjectMapper();                                 // jackson's
                                                                                                  // objectmapper

    /**
     * @return ; DatagridResponse
     */
    public DatagridResponse getDatagridResponseObject()
    {
        return new DatagridResponse();
    }

    /**
     * @param id
     *            : id
     * @param appId
     *            : appid
     * @param authorization
     *            : authorization
     * @param start_time
     *            : start_time
     * @param end_time
     *            : end_time
     * @return Response
     */
    @SuppressWarnings("unchecked")
    public Response getResponse(String id, String appId, String authorization, String start_time, String end_time)
    {

        DatagridResponse response = getDatagridResponseObject();
        List<BaseKpiDataGrid> kpiList = getDataSourceHandler().getWidgetData(id, start_time, end_time, authorization);
        for (BaseKpiDataGrid groupKpi : kpiList)
        {
            response.getTableData().add(this.getObjectMapper().convertValue(getKpiDataGrid(groupKpi), Map.class));
        }

        return handleResult(response);

    }

    /**
     * @param groupKpi
     *            : groupKpi
     * @return Object
     */
    public abstract Object getKpiDataGrid(BaseKpiDataGrid groupKpi);

    /**
     * @param entity
     *            to be wrapped into JSON response
     * @return JSON response with entity wrapped
     */
    protected Response handleResult(Object entity)
    {
        ResponseBuilder responseBuilder = Response.status(Status.OK);
        responseBuilder.type(MediaType.APPLICATION_JSON);
        responseBuilder.entity(entity);
        return responseBuilder.build();
    }

    /**
     * @return String
     */
    public String ping()
    {
        return "SUCCESS"; //$NON-NLS-1$
    }

    /**
     * @return -
     */

    public abstract DataSourceHandler getDataSourceHandler();

    /**
     * @return the objectMapper
     */
    public ObjectMapper getObjectMapper()
    {
        return this.objectMapper;
    }

}
