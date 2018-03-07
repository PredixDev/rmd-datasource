package com.ge.predix.solsvc.experience.datasource.datagrid.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
@Path("/experience/datasource/datagrid/asset")
@Api("AssetDataGridService")
public interface AssetDatagridService {

	/**
	 * API for the Asset GroupDataGrid to construct the widget
	 * 
	 * @param id : id
	 * @param appId : appId
	 * @param authorization : authorization
	 * @return Response 
	 * @throws Throwable throwable
	 */
	@GET
	@Path("/{id}/")
	@ApiOperation(value="API for the Asset level DataGrid")
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
	@ApiOperation(value="API for the Status check")
	public String ping();
	
	
	/**
	 * @param id : id
	 * @param appId : appId
	 * @param authorization : authorization
	 * @param start_time : start_time (Format: "1y-ago" )
	 * @param end_time : end_time (Format: "1y-ago")
	 * @return Response
	 * @throws Throwable : Throwable
	 */
	@GET
	@Path("/{id}/summary")
	@ApiOperation(value="API for the Asset level DataGrid Summary")
	public Response getDatagridSummary(@PathParam("id") String id,
	        @QueryParam("appId") String appId,
            @HeaderParam(value="authorization") String authorization ,
            @QueryParam("start_time") String start_time , @QueryParam("end_time") String end_time) throws Throwable;

}
