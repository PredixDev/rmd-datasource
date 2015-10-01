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
@Path("/experience/datasource/datagrid/group")
public interface GroupDatagridService {
	
	/**
	 * API for the Asset GroupDataGrid
	 * @param id : id 
	 * @param tenantId : tenant id 
	 * @param appId : appid 
	 * @param solutionId : solution Id 
	 * @param authorization : authorization
	 * @param start_time  :start time
	 * @param end_time : end time
	 * @return Response
	 * @throws Throwable Throwable
	 */
	@GET
	@Path("/{id}")
	public Response getDatagrid(@PathParam("id") String id,
	        @QueryParam("appId") String appId,
            @HeaderParam(value="authorization") String authorization ,
            @QueryParam("start_time") String start_time , @QueryParam("end_time") String end_time) throws Throwable;
	
	/**
	 * 
	 * @return String
	 */
	@GET
	@Path("/ping")
	public String ping();
	
	/**
	 * 
	 * @param id : id 
	 * @param appId : application Id 
	 * @param authorization : authorization 
	 * @param start_time : start time 
	 * @param end_time : end _time
	 * @return Response
	 * @throws Throwable Throwable
	 */
	@GET
	@Path("/{id}/summary")
	public Response getDatagridSummary(@PathParam("id") String id,
	        @QueryParam("appId") String appId,
            @HeaderParam(value="authorization") String authorization ,
            @QueryParam("start_time") String start_time , @QueryParam("end_time") String end_time) throws Throwable;
}
