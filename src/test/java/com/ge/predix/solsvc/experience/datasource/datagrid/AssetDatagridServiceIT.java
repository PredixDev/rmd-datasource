package com.ge.predix.solsvc.experience.datasource.datagrid;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.ge.predix.solsvc.experience.datasource.boot.DatasourceApplication;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.AssetKpiDataGrid;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.DatagridResponse;
import com.ge.predix.solsvc.ext.util.JsonMapper;
import com.ge.predix.solsvc.restclient.impl.RestClient;

/**
 * 
 * @author 212421693
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DatasourceApplication.class)
@WebAppConfiguration
@IntegrationTest(
{
        "server.port=0"
})
public class AssetDatagridServiceIT
{

    @Value("${local.server.port}")
    private int                localServerPort;

    private URL                base;

    private RestTemplate       template;

    @Autowired
    private RestClient         restClient;

    /**
     * 
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JsonMapper         jsonMapper;

    /**
     * @throws Exception -
     */
    @Before
    public void setUp()
            throws Exception
    {
        this.template = new TestRestTemplate();
    }

    /**
     * 
     * @throws Exception -
     */
    @SuppressWarnings("nls")
    @Test
    public void ping()
            throws Exception
    {
        this.base = new URL(
                "http://localhost:" + this.localServerPort + "/services/experience/datasource/datagrid/asset/ping");
        ResponseEntity<String> response = this.template.getForEntity(this.base.toString(), String.class);
        assertThat(response.getBody(), startsWith("SUCCESS"));
    }

    // @Path("/{entityType}/{id}/")
    /**
     * @throws Exception : exception
     */
    @Test
    public void getGroupAssetKpi()
            throws Exception
    {
        // get Token for the secure service
        URL dataSourceURl = new URL("http://localhost:" + this.localServerPort //$NON-NLS-1$
                + "/services/experience/datasource/datagrid/asset/compressor-2015");  //$NON-NLS-1$

        List<Header> headers = this.restClient.getSecureTokenForClientId();
        try (CloseableHttpResponse response = this.restClient.get(dataSourceURl.toString(), headers))
        {
            assert (response.getStatusLine().getStatusCode() == 200);
            String body = EntityUtils.toString(response.getEntity());
            Assert.assertNotNull(body);
            DatagridResponse dataGridResponse = this.jsonMapper.fromJson(body, DatagridResponse.class);
            List<Map<String, Object>> responseList = dataGridResponse.getTableData();

            List<AssetKpiDataGrid> assetKpiDataGridRows = new ArrayList<AssetKpiDataGrid>();
            for (Map<String, Object> mapResponse : responseList)
            {
                assetKpiDataGridRows.add(this.objectMapper.convertValue(mapResponse, AssetKpiDataGrid.class));
            }
            assertNotNull(assetKpiDataGridRows);
        }
    }

}
