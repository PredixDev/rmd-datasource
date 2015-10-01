package com.ge.predix.solsvc.experience.datasource.datagrid;


import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.ge.predix.solsvc.experience.datasource.boot.DatasourceApplication;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.AssetKpiDataGrid;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.DatagridResponse;
import com.ge.predix.solsvc.restclient.config.OauthRestConfig;
import com.ge.predix.solsvc.restclient.impl.CxfAwareRestClient;
import com.ge.predix.solsvc.restclient.impl.Token;

/**
 * 
 * @author 212421693
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DatasourceApplication.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class AssetDatagridServiceIT {
    
    @Value("${local.server.port}")
    private int localServerPort;

	private URL base;
	
	private RestTemplate template;
	
	
	@Autowired
	private OauthRestConfig restConfig;
	
	   @Autowired
	private
	CxfAwareRestClient restClient;
	
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	

	/**
	 * @throws Exception -
	 */
	@Before
	public void setUp() throws Exception {
		this.template = new TestRestTemplate();
	}

	/**
	 * 
	 * @throws Exception -
	 */
	@SuppressWarnings("nls")
    @Test
	public void ping() throws Exception {
		this.base = new URL("http://localhost:" + this.localServerPort + "/services/experience/datasource/datagrid/asset/ping");
		ResponseEntity<String> response = this.template.getForEntity(this.base.toString(), String.class);
		assertThat(response.getBody(), startsWith("SUCCESS"));
	}
	
	//@Path("/{entityType}/{id}/")
	/**
	 * @throws Exception : exception
	 */
	@Test
	public void getGroupAssetKpi() throws Exception {
		// get Token for the secure service 

		URL dataSourceURl = new URL("http://localhost:" + this.localServerPort + "/services/experience/datasource/datagrid/asset/compressor-2015");  //$NON-NLS-1$//$NON-NLS-2$
		
		Token requestToken = this.restClient.requestToken( this.restConfig.getOauthUserName(), this.restConfig.getOauthUserPassword(), this.restConfig.isOauthEncodeUserPassword());
		
		HttpHeaders headers = new HttpHeaders();
		headers.put("Authorization", Collections.singletonList(requestToken.getToken().replace("BEARER", "Bearer"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		//log.debug("Authorization-->"+headers.getFirst("Authorization")); //$NON-NLS-1$ //$NON-NLS-2$
		
		ResponseEntity<DatagridResponse> response = this.template.exchange(dataSourceURl.toString(), HttpMethod.GET, new HttpEntity<byte[]>(headers), DatagridResponse.class);
		//log.debug(response.getStatusCode());
		//assert(response.getStatusCode()== HttpStatus.OK);
		
		DatagridResponse dataGridResponse = response.getBody();
		 List<Map<String, Object>> responseList = dataGridResponse.getTableData();
		
		List<AssetKpiDataGrid> assetKpiDataGridRows = new ArrayList <AssetKpiDataGrid>();
		for(Map<String, Object> mapResponse : responseList){
			assetKpiDataGridRows.add(this.objectMapper.convertValue(mapResponse, AssetKpiDataGrid.class));
		}
		assertNotNull(assetKpiDataGridRows);
//		assertTrue(assetKpiDataGridRows.size() > 0);
	}


}
