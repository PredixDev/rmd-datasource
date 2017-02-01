/*
 * Copyright (c) 2016 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.predix.solsvc.experience.datasource.datagrid.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ge.predix.entity.asset.Asset;
import com.ge.predix.solsvc.bootstrap.ams.dto.Group;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.BaseKpiDataGrid;
import com.ge.predix.solsvc.experience.datasource.handlers.DataSourceHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author 212421693
 */
@Consumes({ "application/json" })
@Produces({ "application/json" })
@Path("/experience")
@Component(value = "assetService")
@Api("AssetService")
public class AssetService extends DataSourceHandler {

	private static Logger log = LoggerFactory.getLogger(AssetService.class);

	/**
	 * 
	 * @param authorization - 
	 * @param filter - 
	 * @return -
	 * @throws Throwable -
	 */
	@GET
	@Path("/group")
	@ApiOperation(value="API list of Cached Asset by groups")
	public Response getCacheGroup(@HeaderParam(value = "authorization") String authorization,
			@QueryParam("filter") String filter) throws Throwable {

		String groupIdentifier = "/group/?filter=" + filter; //$NON-NLS-1$
		if (this.getAssetMap().get(groupIdentifier) == null) {
			List<Header> headers = null;
			if (authorization == null) {
				// Note in your app you may want to throw an exception rather
				// than
				// use the info in the property file
				headers = this.restClient.getSecureTokenForClientId();
				this.restClient.addZoneToHeaders(headers, this.assetConfig.getZoneId());
			} else {
				headers = new ArrayList<Header>();
				this.restClient.addSecureTokenToHeaders(headers, authorization);

				this.restClient.addZoneToHeaders(headers, this.assetConfig.getZoneId());
			}
			Long startTime = System.currentTimeMillis();
			if (log.isDebugEnabled()) {
				log.debug("Callling getAssetGroup from getCache " + groupIdentifier); //$NON-NLS-1$
			}
			// /group?filter=parent=/group/root
			String[] filtervalue = filter.split("="); //$NON-NLS-1$
			List<Group> groups = this.groupFactory.getGroupsByFilter(null, filtervalue[0], filtervalue[1], headers);
			this.getAssetMap().put(groupIdentifier, handleResult(groups,MediaType.APPLICATION_JSON_TYPE));
			if (log.isTraceEnabled()) {
				log.debug("Total Group call time = " + (System.currentTimeMillis() - startTime) / 1000); //$NON-NLS-1$
				log.debug("END getAssetGroup from getCache " + groupIdentifier); //$NON-NLS-1$

			}
		}
		return (Response) this.getAssetMap().get(groupIdentifier);

	}

	
	/**
	 * 
	 * @param authorization - 
	 * @param filter - 
	 * @return - 
	 * @throws Throwable -
	 */
	@GET
	@Path("/asset")
	@ApiOperation(value="API to returned Cached Asset")
	public Response getCacheAsset(@HeaderParam(value = "authorization") String authorization,
			@QueryParam("filter") String filter) throws Throwable {

		String assetIdentifier = "/asset/?filter=" + filter; //$NON-NLS-1$
		if (this.getAssetMap().get(assetIdentifier) == null) {
			List<Header> headers = null;
			if (authorization == null) {
				// Note in your app you may want to throw an exception rather
				// than
				// use the info in the property file
				headers = this.restClient.getSecureTokenForClientId();
				this.restClient.addZoneToHeaders(headers, this.assetConfig.getZoneId());
			} else {
				headers = new ArrayList<Header>();
				this.restClient.addSecureTokenToHeaders(headers, authorization);

				this.restClient.addZoneToHeaders(headers, this.assetConfig.getZoneId());

			}
			Long startTime = System.currentTimeMillis();
			if (log.isDebugEnabled()) {
				log.debug("Callling getAssetGroup from getCache " + assetIdentifier); //$NON-NLS-1$
			}
			// /group?filter=parent=/group/root
			String[] filtervalue = filter.split("="); //$NON-NLS-1$
			List<Asset> assets = this.getAssetFactory().getAssetsByFilter(null, filtervalue[0], filtervalue[1],
					headers);
			this.getAssetMap().put(assetIdentifier, handleResult(assets, MediaType.APPLICATION_JSON_TYPE));
			if (log.isTraceEnabled()) {
				log.debug("Total Group call time = " + (System.currentTimeMillis() - startTime) / 1000); //$NON-NLS-1$
				log.debug("END getAssetGroup from getCache " + assetIdentifier); //$NON-NLS-1$

			}

		}
		return (Response) this.getAssetMap().get(assetIdentifier);

	}

	/**
     * @param entity
     *            to be wrapped into JSON response
     * @param textPlainType
     *            -
     * @return JSON response with entity wrapped
     */
    protected Response handleResult(Object entity, MediaType textPlainType) {
        ResponseBuilder responseBuilder = Response.status(Status.OK);
        responseBuilder.type(textPlainType);
        responseBuilder.entity(entity);
        return responseBuilder.build();
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ge.predix.solsvc.experience.datasource.handlers.DataSourceHandler
	 * #getWidgetData(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<BaseKpiDataGrid> getWidgetData(String id, String start_time, String end_time, String authorization) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
     * -
     * 
     * @return string
     */
    @SuppressWarnings("nls")
    @GET
    @Path("/health")
    @ApiOperation(value = "/health")
    public Response greetings() {
        return handleResult("{\"status\":\"up\", \"date\": \" " + new Date() + "\"}", MediaType.TEXT_PLAIN_TYPE);
    }

}
