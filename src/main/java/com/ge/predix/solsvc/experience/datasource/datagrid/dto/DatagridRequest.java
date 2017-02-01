package com.ge.predix.solsvc.experience.datasource.datagrid.dto;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
/**
 * 
 * 
 * @author 212421693
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "dataSet" })
public class DatagridRequest {

	/**
	 * Data Set
	 * <p>
	 * 
	 * 
	 */
	@JsonProperty("dataSet")
	private DatagridRequest.DataSet dataSet;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * Data Set
	 * <p>
	 * 
	 * 
	 * @return The dataSet
	 */
	@JsonProperty("dataSet")
	public DatagridRequest.DataSet getDataSet() {
		return this.dataSet;
	}

	/**
	 * Data Set
	 * <p>
	 * 
	 * 
	 * @param dataSet
	 *            The dataSet
	 */
	@JsonProperty("dataSet")
	public void setDataSet(DatagridRequest.DataSet dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * @return : Map
	 */
	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	/**
	 * 
	 * @param key : name
	 * @param value : value
	 */
	@JsonAnySetter
	public void setAdditionalProperty(String key, Object value) {
		this.additionalProperties.put(key, value);
	}

	/**
	 * 
	 * 
	 * @author 212421693
	 */
	@Generated("org.jsonschema2pojo")
	public static enum DataSet {
		/**
		 * 
		 */
		_1("1"),  //$NON-NLS-1$
		/**
		 * 
		 */
		_2("2"); //$NON-NLS-1$
		private final String value;
		private static Map<String, DatagridRequest.DataSet> constants = new HashMap<String, DatagridRequest.DataSet>();

		static {
			for (DatagridRequest.DataSet c : values()) {
				constants.put(c.value, c);
			}
		}

		private DataSet(String value) {
			this.value = value;
		}

		@JsonValue
		@Override
		public String toString() {
			return this.value;
		}
		/**
		 * 
		 * @param value : value
		 * @return  DatagridRequest.DataSet
		 */
		@JsonCreator
		public static DatagridRequest.DataSet fromValue(String value) {
			DatagridRequest.DataSet constant = constants.get(value);
			if (constant == null) {
				throw new IllegalArgumentException(value);
			} 
			return constant;
			
		}

	}

}