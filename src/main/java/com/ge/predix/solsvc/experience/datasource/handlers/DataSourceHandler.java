package com.ge.predix.solsvc.experience.datasource.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math.util.MathUtils;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import com.ge.predix.entity.asset.Asset;
import com.ge.predix.entity.asset.AssetTag;
import com.ge.predix.entity.timeseries.datapoints.queryrequest.DatapointsQuery;
import com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.DatapointsLatestQuery;
import com.ge.predix.entity.timeseries.datapoints.queryresponse.DatapointsResponse;
import com.ge.predix.solsvc.bootstrap.ams.common.AssetConfig;
import com.ge.predix.solsvc.bootstrap.ams.dto.Attribute;
import com.ge.predix.solsvc.bootstrap.ams.dto.Tag;
import com.ge.predix.solsvc.bootstrap.ams.factories.AssetClientImpl;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.AssetKpiDataGrid;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.BaseKpiDataGrid;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.SummaryKpiColumn;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.SummaryKpiDataGridResponse;
import com.ge.predix.solsvc.restclient.impl.RestClient;
import com.ge.predix.solsvc.timeseries.bootstrap.client.TimeseriesClient;
import com.ge.predix.solsvc.timeseries.bootstrap.config.DefaultTimeseriesConfig;

/**
 * 
 * @author 212421693
 *
 */

public abstract class DataSourceHandler {

	private static Logger log = LoggerFactory.getLogger(DataSourceHandler.class);

	private Map<String, Object> assetMap = new HashMap<String, Object>();

	/**
	 * 
	 */
	@SuppressWarnings("nls")
    public static final String GROUP_FILTER = "GROUP"; 

	/**
	 * 
	 */
	@SuppressWarnings("nls")
    public static final String PARENT_FILTER = "parent"; 

	/**
	 * 
	 */
	@SuppressWarnings("nls")
    public static final String IS_KPI_METER = "isKpi=true"; 

	/**
	 * 
	 */
	@SuppressWarnings("nls")
    public static final String MAC_URL = "machineUrl"; 

	/**
	 * 
	 */
	@SuppressWarnings("nls")
    public static final String SUMMARY_ASSET_ATT = "attributes.summary.value"; 

	/**
	 * 
	 */
	@SuppressWarnings("nls")
    public static final String SUMMARY_METERS = "summaryTag"; 

	/**
	 * 
	 */
	@SuppressWarnings("nls")
    public static final String MACHINE_URI = "machineUri"; 

	/**
	 * 
	 */
	protected static final int DATAPOINT_TS = 0;

	/**
	 * 
	 */
	protected static final int DATAPOINT_VALUE = 1;

	/**
	 * 
	 */
	protected static final int DATAPOINT_QLTY = 2;

	/**
	 * 
	 */
	@Autowired
	protected RestClient restClient;

	/**
	 * 
	 */
	@Autowired
	protected AssetConfig assetConfig;

	@Autowired
	private TimeseriesClient timeseriesClient;
	
	@Autowired
	@Qualifier("AssetClient")
	protected AssetClientImpl assetClient;

//	@Autowired
//	private AssetFactory assetFactory;
//
//	/**
//	 * 
//	 */
//	@Autowired
//	protected GroupFactory groupFactory;
//
//	@Autowired
//	private TagFactory tagFactory;

	@Autowired
	private DefaultTimeseriesConfig timeseriesConfig;

	@Value("${predix.asset.cache.enabled:false}")
	private String cacheEnabled;

	/**
	 * @return -
	 */
	public Boolean isCacheEnabled() {
		return Boolean.valueOf(this.cacheEnabled);
	}

	/**
	 * 
	 * @param id -
	 * @param start_time - 
	 * @param end_time - 
	 * @param authorization -
	 * @return -
	 */

	public abstract List<BaseKpiDataGrid> getWidgetData(String id, String start_time, String end_time,
			String authorization);

	
	/**
	 * @param tagName
	 *            -
	 * @param assetTag
	 *            -
	 * @param authorization
	 *            -
	 * @return -
	 */
	@SuppressWarnings({"nls" })
	protected AssetKpiDataGrid getAnalyticsDrivenAssetDataGrid(String tagName, AssetTag assetTag,
			String authorization) {
		AssetKpiDataGrid assetKpiDataGrid = null;
		// check if datasource has the tagExtenstionsUri
		String tagExtensionUrl = null;
		if (assetTag.getAlertStatusUri() != null
				&& !(assetTag.getAlertStatusUri().isEmpty())) {
			tagExtensionUrl = assetTag.getAlertStatusUri();
		} else {
			return null;
		}

		Asset asset = getAsset(tagExtensionUrl.replace("/asset/", ""), authorization); 
		String alertStatus = getValueFromAttributes(asset, "alertStatus");
		String alertType = getValueFromAttributes(asset, "alertType");
		String alertLevel = getValueFromAttributes(asset, "alertLevel");
		String thresholdDiff = getValueFromAttributes(asset, "deltaThreshold");
		String alarmLevelValue = getValueFromAttributes(asset, "alertLevelValue");
		String alarmLevelValueTime = getValueFromAttributes(asset, "alertTime");
		String sourceType = getValueFromAttributes(asset, "sourceType");
		log.debug("***Analytics Tag information**** for " + tagName + " for " + tagExtensionUrl); 
		log.debug("alertStatus = " + alertStatus);
		log.debug("alertType = " + alertType);
		log.debug("alertLevel = " + alertLevel);
		log.debug("thresholdDiff = " + thresholdDiff);
		log.debug("alarmLevelValue = " + alarmLevelValue);

		if (!org.springframework.util.StringUtils.isEmpty(sourceType)
				&& !org.springframework.util.StringUtils.isEmpty(alertStatus)
				&& !org.springframework.util.StringUtils.isEmpty(alertType)
				&& !org.springframework.util.StringUtils.isEmpty(alertLevel)
				&& !org.springframework.util.StringUtils.isEmpty(thresholdDiff)
				&& !org.springframework.util.StringUtils.isEmpty(alarmLevelValue)
				&& !org.springframework.util.StringUtils.isEmpty(alarmLevelValueTime)) {

			assetKpiDataGrid = new AssetKpiDataGrid();
			assetKpiDataGrid.setAlertStatus(Boolean.valueOf(alertStatus));
			assetKpiDataGrid.setAlertStatusType(alertType);
			assetKpiDataGrid.setCurrentValue(new Double(alarmLevelValue));
			assetKpiDataGrid.setDeltaThreshold(new Double(thresholdDiff));
			assetKpiDataGrid.setDeltaThresholdLevel(alarmLevelValue);
			assetKpiDataGrid.setLastTagReading(new Long(alarmLevelValueTime));
		}

		return assetKpiDataGrid;
	}
	
	/**
	 * Calls Time series End point to fetch the Tag last value
	 * @param assetTagName - String Asset Tag Name 
	 * @param assetTag - Asset Tag object
	 * @param authorization - 
	 * @return -
	 */
	@SuppressWarnings("nls")
    protected List<Double> getCurrentValue(String assetTagName, AssetTag assetTag, String authorization) {

		List<Header> headers = new ArrayList<Header>();
		this.restClient.addSecureTokenToHeaders(headers, authorization);
		this.restClient.addZoneToHeaders(headers, this.timeseriesConfig.getZoneId());

        if (log.isTraceEnabled()) {
            log.trace("assetTag= " + assetTag);
        }

        DatapointsLatestQuery latestDatapoints = new DatapointsLatestQuery();
		com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.Tag tag = new com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.Tag();
		tag.setName(assetTag.getTimeseriesDatasource().getTag());
		List<com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.Tag> tags = new ArrayList<com.ge.predix.entity.timeseries.datapoints.queryrequest.latest.Tag>();
		tags.add(tag);
		latestDatapoints.setTags(tags);
		Long startTime = System.currentTimeMillis();
		if (log.isTraceEnabled()) {
			log.trace("Callling TS Current Value " + this.timeseriesConfig.getQueryUrl() + " "  
					+ latestDatapoints.toString());
		}

		DatapointsResponse response = this.getTimeseriesClient()
				.queryForLatestDatapoint(latestDatapoints, headers);
		if (log.isTraceEnabled()) {
			log.trace("Total time spend is sec " + (System.currentTimeMillis() - startTime) / 1000); 
			log.trace("Callling TS Current Value " + this.timeseriesConfig.getQueryUrl() + " " 
					+ latestDatapoints.toString());
		}
		List<Double> currentValueList = createCurrentValueList(response);

		return currentValueList;
	}

	/**
	 * @param response
	 *            Response object from the Time Series API call
	 * @return -
	 */
	public static List<Double> createCurrentValueList(DatapointsResponse response) {
		if (response == null || CollectionUtils.isEmpty(response.getTags())
				|| CollectionUtils.isEmpty(response.getTags().get(0).getResults())
				|| CollectionUtils.isEmpty(response.getTags().get(0).getResults().get(0).getValues())) {
			return null;
		}

		@SuppressWarnings("unchecked")
		List<Object> responseValues = (List<Object>) response.getTags().get(0).getResults().get(0).getValues().get(0);
		List<Double> currentValueList = new ArrayList<Double>();
		if (!CollectionUtils.isEmpty(responseValues) && responseValues.size() > 2) {
			for (Object obj : responseValues) {
				if (obj instanceof Long) {
					currentValueList.add(((Long) obj).doubleValue());
				} else if (obj instanceof Integer) {
					currentValueList.add(((Integer) obj).doubleValue());
				} else {
					currentValueList.add((Double) obj);
				}
			}
		}
		return currentValueList;
	}

	/**
	 * 
	 * @param tagUri
	 *            : tagUri
	 * @param authorization
	 *            : authorization
	 * @return Tag
	 */
	@SuppressWarnings("nls")
    protected Tag getTag(String tagUri, String authorization) {
		// GET Tag
		List<Header> headers = new ArrayList<Header>();
		this.restClient.addSecureTokenToHeaders(headers, authorization);
		this.restClient.addZoneToHeaders(headers, this.assetConfig.getZoneId());
		Tag tag = null;
		if (isCacheEnabled()) {
			this.assetMap.get(tagUri);
		}
		
		//if (tag == null)
//			tag = this.getTagFactory().getTag(tagUri.replace("/tag/", ""),  
//					headers);
			
		
		
		List<Tag> myTags = this.assetClient.getModels( tagUri, Tag.class, headers);	
//		if (tag != null) {
//			this.assetMap.put(tagUri, tag);
//		}
		
		if(myTags!= null && myTags.size() > 0)
		{
			//We are expecting one Tag object here as per the above logic tagUri string replacement
			this.assetMap.put(tagUri,myTags.get(0));
			tag = myTags.get(0);
		}
		
		return tag;
	}

	/**
	 * 
	 * @param currentValue
	 *            : currentValue
	 * @param assetTag
	 *            : assetTag
	 * @return Boolean
	 */
	protected Boolean getTagAlertStatus(Double currentValue, AssetTag assetTag) {

		if (currentValue > assetTag.getLoAlarmThreshold() && currentValue < assetTag.getHiAlarmThreshold()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;

	}

	/**
	 * @param asset
	 *            -
	 * @return -
	 */
	@SuppressWarnings("nls")
    protected boolean hasKpi(Asset asset) {
		LinkedHashMap<String, AssetTag> tags = asset.getAssetTag();
		if (tags != null) {
			for (Entry<String, AssetTag> entry : tags.entrySet()) {
				AssetTag assetTag = entry.getValue();
				if (assetTag.getIsKpi() != null
						&& StringUtils.containsIgnoreCase(assetTag.getIsKpi().toString(), "TRUE")) { 
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * 
	 * @param id
	 *            : id
	 * @param startTime
	 *            : start_time
	 * @param endTime
	 *            : end_time
	 * @param authorization
	 *            : authorization
	 * @return SummaryKpiDataGridResponse
	 */
	@SuppressWarnings("nls")
    public SummaryKpiDataGridResponse getSummary(String id, String startTime, String endTime, String authorization) {

		SummaryKpiDataGridResponse summaryKpiDataGrid = new SummaryKpiDataGridResponse();

		Asset asset = getSummaryAsset(id, authorization);

		setSummaryBasedOnTags(summaryKpiDataGrid, asset, authorization, startTime, endTime);

		Double health = getHealth(id, authorization, startTime, endTime); // ??
																						// startTime
																						// and
																						// endTime
																						// are
																						// not
																						// used
																						// in
																						// getHealth
																						// method

		Double reliability = new Double(95);
		Double availability = new Double(99.4);

		addkpi(summaryKpiDataGrid, MathUtils.round(reliability, 3), "Reliability", "%");  
		addkpi(summaryKpiDataGrid, MathUtils.round(availability, 3), "Availability", "%"); 
		summaryKpiDataGrid.getOverall().setTitle("KPI Health");
		summaryKpiDataGrid.getOverall().setSubtitle(id);
		summaryKpiDataGrid.getOverall().setPercentage(health);

		return summaryKpiDataGrid;
	}

	/**
	 * 
	 * @param summaryKpiDataGrid
	 *            : summaryKpiDataGrid
	 * @param asset
	 *            : asset
	 * @param authorization
	 *            : authorization
	 * @param startTime
	 *            : startTime
	 * @param endTime
	 *            : endTime
	 */
	@SuppressWarnings("nls")
    protected void setSummaryBasedOnTags(SummaryKpiDataGridResponse summaryKpiDataGrid, Asset asset,
			String authorization, String startTime, String endTime) {

		AssetTag outputTag = null;
		AssetTag summary2Tag = null;
		if (asset != null && asset.getAttributes() != null && asset.getAttributes().containsKey(SUMMARY_METERS)) {
			String totalOutputTag = null;
			String summary2OutputTag = null;
			Attribute listAttributes = (Attribute) asset.getAttributes().get(SUMMARY_METERS);
			if (listAttributes.getValue() != null && listAttributes.getValue().size() >= 2) {
				totalOutputTag = (String) listAttributes.getValue().get(0);
				summary2OutputTag = (String) listAttributes.getValue().get(1);
			} else if (listAttributes.getValue() != null && listAttributes.getValue().size() > 1) {
				totalOutputTag = (String) listAttributes.getValue().get(0);
			}

			LinkedHashMap<String, AssetTag> tags = asset.getAssetTag();
			if (tags != null) {
				for (Entry<String, AssetTag> entry : tags.entrySet()) {
					entry.getKey();

					if ((entry.getKey()).equalsIgnoreCase(totalOutputTag)) {
						outputTag = entry.getValue();
					}
					if ((entry.getKey()).equalsIgnoreCase(summary2OutputTag)) {
						summary2Tag = entry.getValue();
					}
				}
			}
			Double average = 0d;
			Tag tag = null;
			if (outputTag != null) {
				tag = getTag(outputTag.getTagUri(), authorization);
				if (tag != null) {
					average = getSummaryOutput(outputTag.getTagUri().replace("/tag/", ""),  
							outputTag, authorization, summaryKpiDataGrid, startTime, endTime);
					if (average == null || average == 0d) {
						average = 10d;
					}
					addkpi(summaryKpiDataGrid, MathUtils.round(average, 3), "Output (avg)", tag.getUom());
				}

			} else {
				addkpi(summaryKpiDataGrid, MathUtils.round(10d, 3), "Output (avg)", ""); 
			}

			if (summary2Tag != null) {
				tag = getTag(summary2Tag.getTagUri(), authorization);
				if (tag != null) {
					average = getSummaryOutput(summary2Tag.getTagUri().replace("/tag/", ""),  
							summary2Tag, authorization, summaryKpiDataGrid, startTime, endTime);
					if (average == null || average == 0d) {
						average = 10d;
					}
					addkpi(summaryKpiDataGrid, MathUtils.round(average, 3), tag.getName() + " (avg)", tag.getUom());
				}
			} else {

				addkpi(summaryKpiDataGrid, MathUtils.round(10d, 3), "Output (avg)", "");  
			}
		}

	}

	/**
	 * 
	 * @param id
	 *            : id
	 * @param authorization
	 *            : authorization
	 * @param start_time
	 *            : start_time
	 * @param end_time
	 *            : end_time
	 * @return Double
	 */
	@SuppressWarnings("nls")
    protected Double getHealth(String id, String authorization, String start_time, String end_time) {
		Double availability = 0d;

		int tagsNotInAlarmState = 0;
		List<BaseKpiDataGrid> groupList = getWidgetData(id, start_time, end_time, authorization);

		for (BaseKpiDataGrid basekpi : groupList) {
			if (basekpi.getAlertStatus() == null || !basekpi.getAlertStatus()) {
				tagsNotInAlarmState++;
			}
		}

		if (groupList != null && tagsNotInAlarmState > 0) {
			availability = (double) ((tagsNotInAlarmState * 100) / groupList.size());
			log.debug("getHealth" + availability + "= (" + tagsNotInAlarmState + "* 100 ) / " + groupList.size());  //$NON-NLS-3$
		}
		return availability;
	}

	/**
	 * 
	 * @param id
	 *            : id
	 * @param authorization
	 *            : authorization
	 * @return Asset
	 */
	@SuppressWarnings({
            "unchecked", "nls"
    })
	public Asset getSummaryAsset(String id, String authorization) {

		// GET asset
		List<Header> headers = new ArrayList<Header>();
		this.restClient.addSecureTokenToHeaders(headers, authorization);
		this.restClient.addZoneToHeaders(headers, this.assetConfig.getZoneId());

		List<Asset> assets = null;

		if (isCacheEnabled()) {
			assets = ((List<Asset>) this.assetMap.get(SUMMARY_ASSET_ATT + "/" + GROUP_FILTER.toLowerCase() + "/" + id)); 
		}
		if (assets == null || assets.size() == 0) {
			Long startTime = System.currentTimeMillis();
			if (log.isTraceEnabled()) {
				log.trace("Callling Asset Summary" + SUMMARY_ASSET_ATT, "/" + GROUP_FILTER.toLowerCase() + "/" + id);   //$NON-NLS-3$
			}

			log.debug("cache missed,going to asset now "); 
			
			log.debug("Callling Asset Summary:" + SUMMARY_ASSET_ATT, "/" + GROUP_FILTER.toLowerCase() + "/" + id);   //$NON-NLS-3$

			
//			log.debug("Sushma....VALUES TEST: " + SUMMARY_ASSET_ATT);
//				log.debug("VALUES 2: " +	"/" + GROUP_FILTER.toLowerCase() + "/" );
				
			
			
//			assets = this.getAssetFactory().getAssetsByFilter(null, SUMMARY_ASSET_ATT,
//					"/" + GROUP_FILTER.toLowerCase() + "/" + id,  
//					headers);
						
			assets = this.assetClient.getModelsByFilter(Asset.class, null, SUMMARY_ASSET_ATT,
				"/"	+ GROUP_FILTER.toLowerCase() + "/" + id, headers);
			
			this.assetMap.put(SUMMARY_ASSET_ATT + "/" + GROUP_FILTER.toLowerCase() + "/" 
					+ id, assets);
			if (log.isTraceEnabled()) {
				log.trace("Total time spend is sec " + (System.currentTimeMillis() - startTime) / 1000); 
				log.trace("END Asset Summary" + SUMMARY_ASSET_ATT, "/" + GROUP_FILTER.toLowerCase() + "/" + id);   //$NON-NLS-3$
			}
		} else {
			log.debug("cache hit,Loading asset from cache "); 
		}

		if (assets != null && assets.size() >= 1) {
			return assets.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @param summaryKpiDataGrid
	 *            : summaryKpiDataGrid
	 * @param output
	 *            : output
	 * @param label
	 *            : label
	 * @param uom
	 *            : uom
	 */
	protected void addkpi(SummaryKpiDataGridResponse summaryKpiDataGrid, Double output, String label, String uom) {
		summaryKpiDataGrid.getKpis().add(new SummaryKpiColumn(output.toString(), label, uom));

	}

	/**
	 * @param assetTagName
	 *            : assetTagName
	 * @param assetTag
	 *            : assetTag
	 * @param authorization
	 *            : authorization
	 * @param summaryKpiDataGrid
	 *            : summaryKpiDataGrid
	 * @param startTime
	 *            : startTime
	 * @param endTime
	 *            :endTime
	 * @return Double
	 */

	@SuppressWarnings("nls")
    protected Double getSummaryOutput(String assetTagName, AssetTag assetTag, String authorization,
			SummaryKpiDataGridResponse summaryKpiDataGrid, String startTime, String endTime) {

		DatapointsQuery datapointsQuery = buildDatapointsQueryRequest(assetTag, startTime, endTime);

		List<Header> headers = new ArrayList<Header>();
		this.restClient.addSecureTokenToHeaders(headers, authorization);
		this.restClient.addZoneToHeaders(headers, this.timeseriesConfig.getZoneId());

		Long logStartTime = System.currentTimeMillis();
		if (log.isTraceEnabled()) {
			log.trace("Callling TS Query" + this.timeseriesConfig.getQueryUrl() + " " + datapointsQuery.toString());  
		}
		DatapointsResponse response = this.getTimeseriesClient()
				.queryForDatapoints(datapointsQuery, headers);
		log.debug(response.toString());
		if (log.isTraceEnabled()) {
			log.trace("Total time spend is sec " + (System.currentTimeMillis() - logStartTime) / 1000); 
			log.trace("Callling TS Query"); 
		}

		Double avg = getAverageFromDatapointsResponse(response);
		return avg;
	}

	/**
	 * @param response
	 *            : DatapointsResponse
	 * @return -
	 */
	@SuppressWarnings("unchecked")
	public static Double getAverageFromDatapointsResponse(DatapointsResponse response) {
		if (response == null || CollectionUtils.isEmpty(response.getTags())
				|| CollectionUtils.isEmpty(response.getTags().get(0).getResults())
				|| CollectionUtils.isEmpty(response.getTags().get(0).getResults().get(0).getValues())) {
			return null;
		}
		List<Object> averages = response.getTags().get(0).getResults().get(0).getValues();
		List<Object> average = (List<Object>) averages.get(0); // taking the
																// first element
																// in the list
																// of values
		Double avg = null;
		if (average != null && average.get(1) != null) {
			// indicates the avg
			avg = Double.valueOf(average.get(1).toString());

		} else {
			avg = 0d;
		}
		return avg;
	}

	@SuppressWarnings("nls")
    private DatapointsQuery buildDatapointsQueryRequest(AssetTag assetTag, String startTime, String endTime) {
		DatapointsQuery datapointsQuery = new DatapointsQuery();
		if (startTime == null) {
			datapointsQuery.setStart("5d-ago"); 
		} else {
			datapointsQuery.setStart(startTime);
		}
		if (endTime != null) {
			datapointsQuery.setEnd(endTime);
		}
		com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag tag = new com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag();
		tag.setName(assetTag.getTimeseriesDatasource().getTag());

		com.ge.predix.entity.timeseries.datapoints.queryrequest.Aggregation aggregation = new com.ge.predix.entity.timeseries.datapoints.queryrequest.Aggregation();
		aggregation.setType("avg"); 
		aggregation.setInterval("5d"); 
		List<com.ge.predix.entity.timeseries.datapoints.queryrequest.Aggregation> aggregations = new ArrayList<com.ge.predix.entity.timeseries.datapoints.queryrequest.Aggregation>();
		aggregations.add(aggregation);
		tag.setAggregations(aggregations);

		List<com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag> tags = new ArrayList<com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag>();
		tags.add(tag);
		datapointsQuery.setTags(tags);
		return datapointsQuery;
	}

	@SuppressWarnings("nls")
    	private String getValueFromAttributes(Asset asset, String attributeName) {
		String attValue = null;
		Attribute attribute = (Attribute) asset.getAttributes().get(attributeName);

		if (attribute != null && !org.springframework.util.StringUtils.isEmpty(attribute.getValue())
				&& attribute.getValue().size() > 0) {
			if (!org.springframework.util.StringUtils.isEmpty(attribute.getValue().get(0))) {
				attValue = attribute.getValue().get(0).toString();
				log.debug("attributeName" + attributeName + "=" + attValue); 
			}
		}

		return attValue;
	}

	/**
	 * @param id
	 *            -
	 * @param authorization
	 *            -
	 * @return -
	 */
	@SuppressWarnings("nls")
    public Asset getAsset(final String id, final String authorization) {
		Asset asset = null;
		
		if (authorization == null) {
			// Note in your app you may want to throw an exception rather than
			// use the info in the property file
			List<Header> headers = this.restClient.getSecureTokenForClientId();
			this.restClient.addZoneToHeaders(headers, this.assetConfig.getZoneId());
		} else {
			List<Header> headers = new ArrayList<Header>();
			this.restClient.addSecureTokenToHeaders(headers, authorization);
			this.restClient.addZoneToHeaders(headers, this.assetConfig.getZoneId());
			if (isCacheEnabled()) {
				asset = (Asset) this.assetMap.get(id);
			}
			if (asset == null) {
				log.debug("Cache missed,going to asset" + id); 
				Long startTime = System.currentTimeMillis();
				if (log.isTraceEnabled()) {
					log.trace("Callling getAsset"); 
				}
				
				//asset = this.getAssetFactory().getAsset(id, headers);
				//Calling with the new API signature
				
				List<Asset> assetList = this.assetClient.getModels("/asset/"+ id, Asset.class, headers);
				
//				if (asset != null) {
//					this.assetMap.put(id, asset);
//				}
				
				if(assetList != null && assetList.size() >= 1)
				{
					asset = assetList.get(0);
					this.assetMap.put(id, asset);
				}
				
				if (log.isTraceEnabled()) {
					log.trace("Total time spend is sec " + (System.currentTimeMillis() - startTime) / 1000); 
					log.trace("Callling getAsset"); 
				}
			} else {
				log.debug("Cache hit,loading from cache" + id); 
			}
		}
		return asset;
		
	}

	/**
	 * @return the restClient
	 */
	public RestClient getRestClient() {
		return this.restClient;
	}

	/**
	 * @param restClient
	 *            the restClient to set
	 */
	public void setRestClient(RestClient restClient) {
		this.restClient = restClient;
	}

	/**
	 * @return the restConfig
	 */
	public AssetConfig getAssetRestConfig() {
		return this.assetConfig;
	}

	/**
	 * @return the timeseriesFactory
	 */
	public TimeseriesClient getTimeseriesClient() {
		return this.timeseriesClient;
	}

	
	/**
	 * @return the assetMap
	 */
	public Map<String, Object> getAssetMap() {
		return this.assetMap;
	}

	/**
	 * @param assetMap
	 *            the assetMap to set
	 */
	public void setAssetMap(Map<String, Object> assetMap) {
		this.assetMap = assetMap;
	}

	
}
