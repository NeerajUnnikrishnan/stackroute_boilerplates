package com.stackroute.datamunger.query.parser;

import java.util.List;
/* 
 * This class will contain the elements of the parsed Query String such as conditions,
 * logical operators,aggregate functions, file name, fields group by fields, order by
 * fields, Query Type
 * */

public class QueryParameter {

	private String queryString;
	private String file;
	private String baseQuery;
	private List<Restriction> restrictions;
	private List<String> fields;
	private String QUERY_TYPE;
	private List<AggregateFunction> aggregateFunctions;
	private List<String> logicalOperators;
	private List<String> orderByField;
	private List<String> groupByField;

	public void setQueryString(String queryString){
		this.queryString = queryString;
	}

	public String getQueryString(){
		return queryString;
	}

	public void setFileName(String file){
		this.file = file;
	}

	public String getFileName() {
		return file;
	}

	public void setBaseQuery(String baseQuery){
		this.baseQuery = baseQuery;
	}

	public String getBaseQuery() {
		return baseQuery;
	}

	public void setRestrictions(List<Restriction> restrictions){
		this.restrictions = restrictions;
	}

	public List<Restriction> getRestrictions() {
		return restrictions;
	}

	public void setFields(List<String> fields){
		this.fields = fields;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setQUERY_TYPE(String QUERY_TYPE){
		this.QUERY_TYPE = QUERY_TYPE;
	}

	public String getQUERY_TYPE() {
		return QUERY_TYPE;
	}

	public void setAggregateFunctions(List<AggregateFunction> aggregateFunctions) {
		this.aggregateFunctions = aggregateFunctions;
	}

	public List<AggregateFunction> getAggregateFunctions() {
		return aggregateFunctions;
	}

	public void setLogicalOperators(List<String> logicalOperators){
		this.logicalOperators = logicalOperators;
	}

	public List<String> getLogicalOperators() {
		return logicalOperators;
	}

	public void setOrderByFields( List<String> orderByField) {
		this.orderByField = orderByField;
	}

	public List<String> getOrderByFields() {
		return orderByField;
	}

	public void setGroupByFields(List<String> groupByField) {
		this.groupByField = groupByField;
	}

	public List<String> getGroupByFields() {
		return groupByField;
	}

}