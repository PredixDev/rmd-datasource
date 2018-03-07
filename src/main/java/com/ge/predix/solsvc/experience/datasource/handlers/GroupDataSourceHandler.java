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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ge.predix.entity.asset.Asset;
import com.ge.predix.entity.asset.AssetTag;
import com.ge.predix.solsvc.bootstrap.ams.dto.Group;
import com.ge.predix.solsvc.bootstrap.ams.dto.Tag;
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
	private static Logger log = LoggerFactory.getLogger(GroupDataSourceHandler.class);

	@SuppressWarnings("nls")
	@Override
	public List<BaseKpiDataGrid> getWidgetData(String id, String start_time, String end_time, String authorization) {
		log.debug("getWidgetData for " + id); //$NON-NLS-1$

		List<Asset> allGroupChildrenAsset = getAssetWithKpi(id, authorization);

		// call time series to get the currentValue

		List<BaseKpiDataGrid> groupList = new ArrayList<BaseKpiDataGrid>();

		for (Asset asset : allGroupChildrenAsset) {

			LinkedHashMap<String, AssetTag> tags = asset.getAssetTag();
			if (tags != null) {
				for (Entry<String, AssetTag> entry : tags.entrySet()) {

					AssetTag assetTag = entry.getValue();

					if (assetTag.getIsKpi() != null
							&& StringUtils.containsIgnoreCase(assetTag.getIsKpi().toString(), "TRUE")) {

						GroupKpiDataGrid groupKpiDataGrid = new GroupKpiDataGrid();
						groupKpiDataGrid.setAsset(asset.getDescription());
						groupKpiDataGrid.setAssetUri(asset.getUri());
						groupKpiDataGrid.setTag(entry.getKey());
						groupKpiDataGrid.setTagUri(assetTag.getTagUri());
						groupKpiDataGrid.setTag_isKpi(Boolean.TRUE);
						groupKpiDataGrid.setTag_isPM(Boolean.FALSE);
						if (assetTag.getEdgeDatasource() != null
								&& !(assetTag.getEdgeDatasource().getNodeName()).toString().isEmpty()) {// $NON-NLS-1$
							groupKpiDataGrid.setTag_isPM(Boolean.TRUE);
						}

						List<Double> dataPoint = getCurrentValue(entry.getKey(), assetTag, authorization);
						if (dataPoint != null && dataPoint.size() >= 2) {
							groupKpiDataGrid.setCurrentValue(dataPoint.get(DATAPOINT_VALUE));
							groupKpiDataGrid.setLastTagReading(dataPoint.get(DATAPOINT_TS).longValue());
							groupKpiDataGrid
									.setAlertStatus(getTagAlertStatus(dataPoint.get(DATAPOINT_VALUE), assetTag));
						} else {
							groupKpiDataGrid.setAlertStatus(getTagAlertStatus(new Double(0), assetTag));
						}

						Tag tag = getTag(assetTag.getTagUri(), authorization);
						if (tag != null) {
							groupKpiDataGrid.setUnit(tag.getUom());
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

		List<Header> headers = new ArrayList<Header>();
		List<Asset> totalAssets = new ArrayList<Asset>();

		this.restClient.addSecureTokenToHeaders(headers, authorization);
		this.restClient.addZoneToHeaders(headers, this.assetConfig.getZoneId());
		
//		log.debug("Parameter id value from method: getAssetWithKpi: " + id);
		
		log.debug("Parameter filetr value from method: getAssetWithKpi: " 
		+ PARENT_FILTER.toLowerCase() + "/" + GROUP_FILTER.toLowerCase()	
		+ "/");

		List<Group> groups = this.assetClient.getModelsByFilter(Group.class, null, 
				PARENT_FILTER.toLowerCase(), "/" + GROUP_FILTER.toLowerCase()
				+ "/" + id, headers);
		if (groups == null) {
			log.debug("from getAssetWithKpi: getting parent group as null: ");
			groups = new ArrayList<Group>();
		}
		
		// get current group as well		
		List<Group> currentGroup = this.assetClient.getModels("/group/" + id, Group.class, headers);
		log.debug("from getAssetWithKpi- list of current groups:" + currentGroup.size() );
		log.debug("from getAssetWithKpi- list of current groups:" + currentGroup.toString() );
		groups.add( currentGroup.get(0));
		
		List<Asset> groupAssets = null;
		for (Group group : groups) {
			// get assets for this group
			
			//call using the new API method now
			 groupAssets = this.assetClient.getModelsByFilter(Asset.class, null,
					GROUP_FILTER.toLowerCase(), group.getUri(), headers);

		}//end for here
			
			
			if (CollectionUtils.isEmpty(groupAssets)) {
				return allGroupChildrenAsset;
			}
			
			
			for (Asset asset : groupAssets) {
				totalAssets.add(asset);
				if(getAssetChildren(asset, headers)!= null)
					{
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
	private Collection<? extends Asset> getAssetChildren(Asset asset, List<Header> headers) {
		
		List<Asset> assetChildren = new ArrayList<Asset>();

//		List<Asset> assets = getAssetFactory().getAssetsByFilter(null, PARENT_FILTER.toLowerCase(), asset.getUri(),
//				headers);
		
		List<Asset> assets = this.assetClient.getModelsByFilter(Asset.class, null, "parentUri", 
				asset.getUri(), headers);
		if(null == assets)
			log.debug("from getAssetChildren: No Child assets found");

		if (assets != null && assets.size() > 0) {
			//log.debug("from getAssetChildren: Assets size is " + assets.size());
			assetChildren.addAll(assets);
			for (Asset child : assets) {
				//Add if the children assets returned is not null
				if( getAssetChildren(child, headers) != null)
					assetChildren.addAll(getAssetChildren(child, headers));
			}

		} else {
			return assets;
		}
		return assetChildren;

	}

}
