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

					if (assetTag.getTagDatasource().getIsKpi() != null && StringUtils
							.containsIgnoreCase(assetTag.getTagDatasource().getIsKpi().toString(), "TRUE")) {

						GroupKpiDataGrid groupKpiDataGrid = new GroupKpiDataGrid();
						groupKpiDataGrid.setAsset(asset.getDescription());
						groupKpiDataGrid.setAssetUri(asset.getUri());
						groupKpiDataGrid.setTag(entry.getKey());
						groupKpiDataGrid.setTagUri(assetTag.getTagUri());
						groupKpiDataGrid.setTag_isKpi(Boolean.TRUE);
						groupKpiDataGrid.setTag_isPM(Boolean.FALSE);
						if (assetTag.getTagDatasource().getMachineUri() != null
								&& !(assetTag.getTagDatasource().getMachineUri()).toString().isEmpty()) {// $NON-NLS-1$
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
		// GET Group
		List<Group> groups = this.groupFactory.getGroupsByFilter(null, PARENT_FILTER.toLowerCase(),
				"/" + GROUP_FILTER.toLowerCase()//$NON-NLS-1$
						+ "/" + id, //$NON-NLS-1$
				headers);

		// get current group as well
		groups.add(this.groupFactory.getGroup(id, headers));

		for (Group group : groups) {
			// get assets for this group

			List<Asset> groupAssets = getAssetFactory().getAssetsByFilter(null, GROUP_FILTER.toLowerCase(),
					group.getUri(), headers);

			if (CollectionUtils.isEmpty(groupAssets)) {
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
	private Collection<? extends Asset> getAssetChildren(Asset asset, List<Header> headers) {
		List<Asset> assetChildren = new ArrayList<Asset>();

		List<Asset> assets = getAssetFactory().getAssetsByFilter(null, PARENT_FILTER.toLowerCase(), asset.getUri(),
				headers);

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
