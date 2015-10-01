/**
 * 
 */
package com.ge.predix.solsvc.experience.datasource.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.kairosdb.client.builder.DataFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ge.predix.solsvc.bootstrap.ams.dto.Asset;
import com.ge.predix.solsvc.bootstrap.ams.dto.AssetMeter;
import com.ge.predix.solsvc.bootstrap.ams.dto.Group;
import com.ge.predix.solsvc.bootstrap.ams.dto.Meter;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.BaseKpiDataGrid;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.GroupKpiDataGrid;

/**
 * Component to get Data for Asset Based on Group
 * 
 * @author 212421693
 *
 */
@Component
public class GroupDataSourceHandler extends DataSourceHandler {
	private static Logger log = LoggerFactory
			.getLogger(GroupDataSourceHandler.class);

	@SuppressWarnings("nls")
    @Override
	public List<BaseKpiDataGrid> getWidgetData(String id, String start_time,
			String end_time, String authorization) throws DataFormatException {
		log.debug("getWidgetData for " + id); //$NON-NLS-1$

		List<Asset> allGroupChildrenAsset = getAssetWithKpi(id, authorization);

		// call timeseries to get the currentValue

		List<BaseKpiDataGrid> groupList = new ArrayList<BaseKpiDataGrid>();

		for (Asset asset : allGroupChildrenAsset) {

			LinkedHashMap<String, AssetMeter> meters = asset.getAssetMeter();
			if (meters != null) {
				for (Entry<String, AssetMeter> entry : meters.entrySet()) {

					AssetMeter assetMeter = entry.getValue();

                    if ( assetMeter.getMeterDatasource().getIsKpi() !=null  
                    		&& StringUtils.containsIgnoreCase(assetMeter.getMeterDatasource().getIsKpi().toString(), "TRUE")){

						GroupKpiDataGrid groupKpiDataGrid = new GroupKpiDataGrid();
						groupKpiDataGrid.setAsset(asset.getDescription());
						groupKpiDataGrid.setAssetUri(asset.getUri());
						groupKpiDataGrid.setMeter(entry.getKey());
						groupKpiDataGrid.setMeterUri(assetMeter.getUri());
						groupKpiDataGrid.setMeter_isKpi(Boolean.TRUE);
						groupKpiDataGrid.setMeter_isPM(Boolean.FALSE);
	                    if ( assetMeter.getMeterDatasource().getMachineUri() !=null
	                    		&& !(assetMeter.getMeterDatasource().getMachineUri()).toString().isEmpty()){//$NON-NLS-1$
							groupKpiDataGrid.setMeter_isPM(Boolean.TRUE);
						}

						List<Double> dataPoint = getCurrentValue(
								entry.getKey(), assetMeter, authorization);
						if (dataPoint != null && dataPoint.size() == 2) {
							groupKpiDataGrid.setCurrentValue(dataPoint
									.get(DATAPOINT_VALUE));
							groupKpiDataGrid.setLastMeterReading(dataPoint.get(
									DATAPOINT_TS).longValue());
							groupKpiDataGrid
									.setAlertStatus(getMeterAlertStatus(
											dataPoint.get(DATAPOINT_VALUE),
											assetMeter));
						} else {
							groupKpiDataGrid
									.setAlertStatus(getMeterAlertStatus(
											new Double(0), assetMeter));
						}

						Meter meter = getMeter(assetMeter.getUri(),
								authorization);
						if (meter != null) {
							groupKpiDataGrid.setUnit(meter.getUom());
						}

						groupList.add(groupKpiDataGrid);
					}
				}
			}

		}

		return groupList;
	}

	/***
	 * Method to fetch Asset based on group
	 * 
	 * @param id
	 * @param authorization
	 * @return
	 */
	private List<Asset> getAssetWithKpi(String id, String authorization) {
		List<Asset> allGroupChildrenAsset = new ArrayList<Asset>();
		// http://predix-asset.grc-apps.svc.ice.ge.com/services/asset/?filter=group=/group/plant-richmond-refinery

        List<Header> headers = new ArrayList<Header>();
        List<Asset> totalAssets = new ArrayList<Asset>();
        
        this.restClient.addSecureTokenToHeaders(headers, authorization);
        this.restClient.addZoneToHeaders(headers, this.assetRestConfig.getZoneId());
		// GET Group
        List<Group> groups = groupFactory.getGroupsByFilter(null,
				PARENT_FILTER.toLowerCase(), "/" + GROUP_FILTER.toLowerCase()//$NON-NLS-1$
						+ "/" + id, headers); //$NON-NLS-1$
        
        
        if(CollectionUtils.isEmpty(groups)){
        	return allGroupChildrenAsset;
		}
        
        for (Group group:groups ) {
        	//get assets for this group
        	
        	List<Asset> groupAssets = getAssetFactory().getAssetsByFilter(null,
    				GROUP_FILTER.toLowerCase(), group.getUri(), headers);
        	
        	if(CollectionUtils.isEmpty(groupAssets)){
            	return allGroupChildrenAsset;
    		}
        	
        	for (Asset asset : groupAssets) {
        		totalAssets.add(asset);
        		totalAssets.addAll(getAssetChildren(asset, headers));
    		}
        	
        }
        
		// check for kpi
		for (Asset asset : totalAssets) {
			if (hasKpi(asset)) {
				allGroupChildrenAsset.add(asset);
			}
		}

		return allGroupChildrenAsset;
	}

	/**
	 * Recursive call to get all children assets
	 * 
	 * @param asset
	 * @param context
	 * @return
	 */
	private Collection<? extends Asset> getAssetChildren(Asset asset,
			List<Header> headers) {
		List<Asset> assetChildren = new ArrayList<Asset>();

		List<Asset> assets = getAssetFactory().getAssetsByFilter(null,
				PARENT_FILTER.toLowerCase(), asset.getUri(), headers);

		if (assets != null && assets.size() > 0) {
			assetChildren.addAll(assets);
			for (Asset child : assets) {
				assetChildren.addAll(getAssetChildren(child, headers));
			}

		} else {
			return assets;
		}
		return assetChildren;

	}

}
