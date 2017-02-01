/*
 * Copyright (c) 2016 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.predix.solsvc.experience.datasource.datagrid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ge.predix.entity.asset.Asset;
import com.ge.predix.solsvc.bootstrap.ams.dto.Group;
import com.ge.predix.solsvc.experience.datasource.boot.DatasourceApplication;
import com.ge.predix.solsvc.ext.util.JsonMapper;
import com.ge.predix.solsvc.restclient.impl.RestClient;

/**
 * 
 * @author 212421693
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DatasourceApplication.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class AssetServiceIT {

	@Value("${local.server.port}")
	private int localServerPort;

	@Autowired
	private RestClient restClient;

	@Autowired
    private JsonMapper jsonMapper;

	/**
	 * @throws Exception
	 *             -
	 */
	@Before
	public void setUp() throws Exception {
	    //
	}

	/**
	 * 
	 * @throws Exception
	 *             -
	 */
	@SuppressWarnings("nls")
	@Test
	public void getGroupRoot() throws Exception {
		URL groupUrl = new URL(
				"http://localhost:" + this.localServerPort + "/services/experience/group?filter=parent=/group/root");
		List<Group> groups = getGroup(groupUrl);
		assertNotNull(groups);
		assertFalse(groups.size() <= 0);
	}

	/**
	 * 
	 * @throws Exception
	 *             -
	 */
	@SuppressWarnings("nls")
	@Test
	public void getGroupLevel() throws Exception {
		URL groupUrl = new URL("http://localhost:" + this.localServerPort
				+ "/services/experience/group?filter=parent=/group/enterprise-predix");
		List<Group> groups = getGroup(groupUrl);
		assertNotNull(groups);

		groupUrl = new URL("http://localhost:" + this.localServerPort
				+ "/services/experience/group?filter=parent=/group/site-richmond");
		groups = getGroup(groupUrl);
		assertNotNull(groups);
	}

	/**
	 * @throws Exception
	 *             -
	 */
	@SuppressWarnings("nls")
	@Test
	public void getAssetLevel() throws Exception {

		URL assetUrl = new URL("http://localhost:" + this.localServerPort
				+ "/services/experience/asset?filter=group=/group/plant-richmond-refinery");

		List<Asset> assets = getAsset(assetUrl);
		assertNotNull(assets);

		assetUrl = new URL("http://localhost:" + this.localServerPort
				+ "/services/experience/asset?filter=parent=/asset/compressor-2015");
		assets = getAsset(assetUrl);

		assertNotNull(assets);
	}

	/**
	 * @param groupUrl -
	 * @return -
	 * @throws Exception
	 *             -
	 */
	public List<Group> getGroup(URL groupUrl) throws Exception {

        List<Header> headers = this.restClient.getSecureTokenForClientId();
        try (CloseableHttpResponse response = this.restClient.get(groupUrl.toString(), headers))
        {
            assert (response.getStatusLine().getStatusCode() == 200);
            String body = EntityUtils.toString(response.getEntity());
            Assert.assertNotNull(body);
            List<Group> groups = this.jsonMapper.fromJsonArray(body, Group.class);
            return groups;
        }
	}

	/**
	 * @param assetUrl
	 *            -
	 * @return -
	 * @throws Exception
	 *             -
	 */
	public List<Asset> getAsset(URL assetUrl) throws Exception {

        List<Header> headers = this.restClient.getSecureTokenForClientId();
        try (CloseableHttpResponse response = this.restClient.get(assetUrl.toString(), headers))
        {
            assert (response.getStatusLine().getStatusCode() == 200);
            String body = EntityUtils.toString(response.getEntity());
            Assert.assertNotNull(body);
            List<Asset> assets = this.jsonMapper.fromJsonArray(body, Asset.class);
            return assets;
        }
	}

}
