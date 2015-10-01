package com.ge.predix.solsvc.experience.datasource.datagrid.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * 
 * @author 212421693
 *
 */
@Consumes({ "application/json", "application/xml" })
@Produces({ "application/json", "application/xml" })
@Path("/experience/datasource/datagrid/asset")
public interface AssetDatagridService {

	/**
	 * API for the Asset GroupDataGrid to construct the widget
	 * 
	 * @param id : id
	 * @param tenantId : tenantId
	 * @param appId : appId
	 * @param solutionId : solutionId
	 * @param authorization : authorization
	 * @param start_time : start_time
	 * @param end_time : end_time
	 * @return Response 
	 * @throws Throwable throwable
	 */
	@GET
	@Path("/{id}/")
	public Response getDatagrid(@PathParam("id") String id,
			@QueryParam("appId") String appId,
			@HeaderParam(value = "authorization") String authorization)
			throws Throwable;

	/**
	 * 
	 * @return String
	 */
	@GET
	@Path("/ping")
	public String ping();
	
	
	/**
	 * @param id : id
	 * @param appId : appId
	 * @param authorization : authorization
	 * @param start_time : start_time
	 * @param end_time : end_time
	 * @return Response
	 * @throws Throwable : Throwable
	 */
	@GET
	@Path("/{id}/summary")
	public Response getDatagridSummary(@PathParam("id") String id,
	        @QueryParam("appId") String appId,
            @HeaderParam(value="authorization") String authorization ,
            @QueryParam("start_time") String start_time , @QueryParam("end_time") String end_time) throws Throwable;

}
