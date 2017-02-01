/**
 * 
 */
package com.ge.predix.solsvc.experience.datasource.handlers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ge.predix.entity.asset.Asset;
import com.ge.predix.entity.asset.AssetTag;
import com.ge.predix.solsvc.bootstrap.ams.dto.Tag;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.AssetKpiDataGrid;
import com.ge.predix.solsvc.experience.datasource.datagrid.dto.BaseKpiDataGrid;

/**
 * Component to get Data for Asset Based on Group
 * 
 * @author 212421693
 *
 */
@Component
public class AssetDataSourceHandler extends DataSourceHandler
{

    private static Logger log = LoggerFactory.getLogger(AssetDataSourceHandler.class);

    @SuppressWarnings("nls")
    @Override
    public List<BaseKpiDataGrid> getWidgetData(String id, String start_time, String end_time, String authorization)
    {

        List<Asset> allAsset = getAssetWithKpi(id, authorization);

        // call timeseries to get the currentValue

        List<BaseKpiDataGrid> groupList = new ArrayList<BaseKpiDataGrid>();

        for (Asset asset : allAsset)
        {

            LinkedHashMap<String, AssetTag> tags = asset.getAssetTag();
            if ( tags != null )
            {
                for (Entry<String, AssetTag> entry : tags.entrySet())
                {
                    entry.getKey();
                    AssetTag assetTag = entry.getValue();
                   
                    AssetKpiDataGrid kpiDataGrid = getAnalyticsDrivenAssetDataGrid(entry.getKey(), assetTag,
                            authorization);

                    // check for tagExtensions and if properties are set return kpiDataGrid
                    if ( kpiDataGrid == null )
                    {
                    	log.debug("getAnalyticsDrivenAssetDataGrid not found calling time series Current Value");
                        kpiDataGrid = new AssetKpiDataGrid();
                        List<Double> dataPoint = getCurrentValue(entry.getKey(), assetTag, authorization);
                        if ( dataPoint != null && dataPoint.size() >= 2 )
                        {
                        	kpiDataGrid.setLastTagReading(dataPoint.get(DATAPOINT_TS).longValue());
                            kpiDataGrid.setCurrentValue(dataPoint.get(DATAPOINT_VALUE).doubleValue());                          
                            kpiDataGrid.setAlertStatus(getTagAlertStatus(dataPoint.get(DATAPOINT_VALUE), assetTag));
                        }
                        else
                        {
                            kpiDataGrid.setCurrentValue(new Double(0));
                            kpiDataGrid.setLastTagReading(new Long(0));
                            kpiDataGrid.setAlertStatus(getTagAlertStatus(new Double(0), assetTag));
                        }

                    }

                    kpiDataGrid.setTag(entry.getKey());
                    kpiDataGrid.setTagUri(assetTag.getTagUri());
                    kpiDataGrid.setThresholdMax(assetTag.getOutputMaximum());
                    kpiDataGrid.setThresholdMin(assetTag.getOutputMinimum());
                    setDeltaFromThreshold(kpiDataGrid, assetTag);

                    if ( assetTag.getTagDatasource().getIsKpi() != null
                            && StringUtils.containsIgnoreCase(assetTag.getTagDatasource().getIsKpi().toString(), "TRUE") )
                    {
                        kpiDataGrid.setTag_isKpi(Boolean.TRUE);
                    }
                    else
                    {
                        kpiDataGrid.setTag_isKpi(Boolean.FALSE);
                    }
                    if ( assetTag.getTagDatasource().getMachineUri() != null
                            && !assetTag.getTagDatasource().getMachineUri().toString().isEmpty() )
                    {
                        kpiDataGrid.setTag_isPM(Boolean.TRUE);
                    }
                    else
                    {
                        kpiDataGrid.setTag_isPM(Boolean.FALSE);
                    }

                    // tag call
                    Tag tag = getTag(assetTag.getTagUri(), authorization);
                    if ( tag != null )
                    {
                        kpiDataGrid.setUnit(tag.getUom());
                    }

                    groupList.add(kpiDataGrid);
                }

            }

        }

        return groupList;
    }

    /**
     * Method to calculate the Threshold value
     * 
     * @param kpiDataGrid
     * @param assetTag
     */
    private void setDeltaFromThreshold(AssetKpiDataGrid kpiDataGrid, AssetTag assetTag)
    {
        Double currentValue = kpiDataGrid.getCurrentValue();
        Double thresholdMin = assetTag.getLoAlarmThreshold();
        Double thresholdMax = assetTag.getOutputMaximum();

        log.debug("currentValue = " + currentValue + " thresholdMin = " + thresholdMin + " thresholdMax = "   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                + thresholdMax);

        if ( currentValue.compareTo(thresholdMin) >= 0 && currentValue.compareTo(thresholdMax) <= 0 )
        {
            Double midRange = (thresholdMax - thresholdMin) / 2;
            if ( currentValue.compareTo(midRange) >= 0 )
            {
                Double value = ((thresholdMax - currentValue) / (thresholdMax - thresholdMin)) * 100;
                kpiDataGrid.setDeltaThresholdColor("GREEN"); //$NON-NLS-1$
                kpiDataGrid.setDeltaThresholdLevel("HIGH"); //$NON-NLS-1$
                kpiDataGrid.setDeltaThreshold(value);
                log.debug("In range ^" + value); //$NON-NLS-1$
            }
            else
            {
                Double value = ((currentValue - thresholdMin) / (thresholdMax - thresholdMin)) * 100;
                kpiDataGrid.setDeltaThresholdColor("GREEN"); //$NON-NLS-1$
                kpiDataGrid.setDeltaThresholdLevel("LOW"); //$NON-NLS-1$
                kpiDataGrid.setDeltaThreshold(value);
                log.debug("In range v" + value); //$NON-NLS-1$
            }

        }
        else if ( currentValue.compareTo(thresholdMin) < 0 )
        {
            Double value = ((currentValue - thresholdMin) / thresholdMin) * 100;
            kpiDataGrid.setDeltaThresholdColor("RED"); //$NON-NLS-1$
            kpiDataGrid.setDeltaThresholdLevel("LOW"); //$NON-NLS-1$
            kpiDataGrid.setDeltaThreshold(value);
            log.debug("RED range v" + value); //$NON-NLS-1$

        }
        else if ( currentValue.compareTo(thresholdMax) > 0 )
        {
            Double value = ((thresholdMax - currentValue) / thresholdMax) * 100;
            kpiDataGrid.setDeltaThresholdColor("RED"); //$NON-NLS-1$
            kpiDataGrid.setDeltaThresholdLevel("HIGH"); //$NON-NLS-1$
            kpiDataGrid.setDeltaThreshold(value);
            log.debug("RED range ^ " + value); //$NON-NLS-1$
        }

    }

    /***
     * 
     * @param id
     * @param authorization
     * @return
     */
    private List<Asset> getAssetWithKpi(String id, String authorization)
    {
        List<Asset> allAsset = new ArrayList<Asset>();
        Asset assets = getSummaryAsset(id, authorization);
        if ( assets != null ) allAsset.add(assets);
        return allAsset;
    }


    @Override
    public Asset getSummaryAsset(String id, String authorization)
    {
    	log.debug("Calling Summary Asset with id "+id); //$NON-NLS-1$
        return getAsset(id, authorization);
    }

}
