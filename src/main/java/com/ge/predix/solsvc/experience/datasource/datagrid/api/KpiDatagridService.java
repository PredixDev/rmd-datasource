package com.ge.predix.solsvc.experience.datasource.datagrid.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author 212421693
 *
 */
@Consumes({ "application/json", "application/xml" })
@Produces({ "application/json", "application/xml" })
@Path("/experience/datasource/kpidatagrid")
@Api("KpiDataGridService")
public interface KpiDatagridService {

	
	
	
	/**
	 * @param entityType -
	 * @param id -
	 * @param authorization -
	 * @return -
	 * @throws Throwable -
	 */
	@GET
	@Path("/{entityType}/{id}/")
	@ApiOperation(value="API for the DataGrid")
	public Response getDatagrid(@PathParam("entityType") String entityType,@PathParam("id") String id,
			@HeaderParam(value="authorization") String authorization) throws Throwable;
	
	/**
	 * 
	 * @return String
	 */
	@GET
	@Path("/ping")
	@ApiOperation(value="API Status check")
	public String ping();
}
