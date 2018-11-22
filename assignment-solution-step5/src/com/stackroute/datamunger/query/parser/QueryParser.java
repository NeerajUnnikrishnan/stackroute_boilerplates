package com.stackroute.datamunger.query.parser;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.*;
import java.lang.*;


/*There are total 4 DataMungerTest file:
 *
 * 1)DataMungerTestTask1.java file is for testing following 4 methods
 * a)getBaseQuery()  b)getFileName()  c)getOrderByClause()  d)getGroupByFields()
 *
 * Once you implement the above 4 methods,run DataMungerTestTask1.java
 *
 * 2)DataMungerTestTask2.java file is for testing following 2 methods
 * a)getFields() b) getAggregateFunctions()
 *
 * Once you implement the above 2 methods,run DataMungerTestTask2.java
 *
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getRestrictions()  b)getLogicalOperators()
 *
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 *
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class QueryParser {

	private QueryParameter queryParameter = new QueryParameter();

	/*
	 * This method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {

		queryParameter.setQueryString(queryString);
		queryParameter.setFileName(getFileName(queryString));
		queryParameter.setBaseQuery(getBaseQuery(queryString));
		queryParameter.setFields(getFields(queryString));
		queryParameter.setOrderByFields(getOrderByFields(queryString));
		queryParameter.setGroupByFields(getGroupByFields(queryString));
		queryParameter.setRestrictions(getRestrictions(queryString));
		queryParameter.setLogicalOperators(getLogicalOperators(queryString));
		queryParameter.setAggregateFunctions(getAggregateFunctions(queryString));

		return queryParameter;
	}

	public String[] getSplitStrings(String queryString) {

		String lowCaseString = queryString.toLowerCase();

		String[] splitStringArray = lowCaseString.split(" ");

		return splitStringArray;
	}

	/*
	 * Extract the name of the file from the query. File name can be found after the
	 * "from" clause.
	 */
	public String getFileName(String queryString) {

		String[] splitStringArray = getSplitStrings(queryString);

		int fromIndex = Arrays.asList(splitStringArray).indexOf("from");

		return splitStringArray[fromIndex+1];
	}


	/*
	 *
	 * Extract the baseQuery from the query.This method is used to extract the
	 * baseQuery from the query string. BaseQuery contains from the beginning of the
	 * query till the where clause
	 */
	public String getBaseQuery(String queryString) {

		String[] splitStringArray = queryString.split("where|group by|order by");

		if(splitStringArray == null){
			return null;
		}

		return splitStringArray[0].trim();
	}

	/*
	 * extract the order by fields from the query string. Please note that we will
	 * need to extract the field(s) after "order by" clause in the query, if at all
	 * the order by clause exists. For eg: select city,winner,team1,team2 from
	 * data/ipl.csv order by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one order by fields.
	 */
	public List<String> getOrderByFields(String queryString) {

		String[] orderFieldSplit = queryString.split("order by");

		if(orderFieldSplit.length ==1){
			return null;
		}

		String[] orderFields=  orderFieldSplit[1].split(",");

		List<String> orderByFieldsList = new ArrayList<String>();

		for (String s: orderFields) {
			orderByFieldsList.add(s.trim());
		}

		return orderByFieldsList;
	}


	/*
	 * Extract the group by fields from the query string. Please note that we will
	 * need to extract the field(s) after "group by" clause in the query, if at all
	 * the group by clause exists. For eg: select city,max(win_by_runs) from
	 * data/ipl.csv group by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one group by fields.
	 */
	public List<String> getGroupByFields(String queryString) {

		String[] orderByFieldString = queryString.split("order by");

		String[] groupByFieldString = orderByFieldString[0].split("group by");

		if(groupByFieldString.length == 1){
			return null;
		}

		String[] trimmedGroupByFields = new String[groupByFieldString.length];

		for (int i = 0; i < groupByFieldString.length; i++)
			trimmedGroupByFields[i] = groupByFieldString[i].trim();

		String[] groupByFields = trimmedGroupByFields[1].split(",");

		List<String> groupByFieldsList = Arrays.asList(groupByFields);
		return groupByFieldsList;
	}


	/*
	 * Extract the selected fields from the query string. Please note that we will
	 * need to extract the field(s) after "select" clause followed by a space from
	 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
	 * query mentioned above, we need to extract "city" and "win_by_runs". Please
	 * note that we might have a field containing name "from_date" or "from_hrs".
	 * Hence, consider this while parsing.
	 */
	public List<String> getFields(String queryString) {

		String[] splitStringArray = queryString.split("select | from");

		if(splitStringArray[1] == "*") {
			List<String> fullFieldList = new ArrayList<>();
			fullFieldList.add("*");
			return fullFieldList;
		}

		List<String> fieldList = Arrays.asList(splitStringArray[1].trim().split(","));

		List<String> trimmedFieldList = new ArrayList<>();
		for(int i=0;i< fieldList.size();i++){
			trimmedFieldList.add(fieldList.get(i).trim());
		}
		return trimmedFieldList;
	}

	/*
	 * Extract the conditions from the query string(if exists). for each condition,
	 * we need to capture the following: 1. Name of field 2. condition 3. value
	 *
	 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
	 * where season >= 2008 or toss_decision != bat
	 *
	 * here, for the first condition, "season>=2008" we need to capture: 1. Name of
	 * field: season 2. condition: >= 3. value: 2008
	 *
	 * the query might contain multiple conditions separated by OR/AND operators.
	 * Please consider this while parsing the conditions.
	 *
	 */
	public String getConditionsPartQuery(String queryString) {


		String[] splitStringArray = queryString.split("where");

		if(splitStringArray.length == 1) {
			return null;
		}

		String[] conditionPart = splitStringArray[1].split("group by");

		if (conditionPart.length == 1) {

			conditionPart  = splitStringArray[1].split("order by");

		}

		return conditionPart[0].trim();
	}
	public List<Restriction> getRestrictions(String queryString) {

		String ConditionPart = getConditionsPartQuery(queryString);


		if (ConditionPart==null)
			return null;

		String[] splitConditions = ConditionPart.split("and | or");

		for (int i = 0; i < splitConditions.length; i++)
			splitConditions[i] = splitConditions[i].trim();

		List<Restriction> splitConditionClass = new ArrayList<>();

		for(String s:splitConditions){
			s = s.replace("'"," ");
			String[] conditionPartArray = s.split(" ");
			if(conditionPartArray.length == 3) {

				Restriction element = new Restriction(conditionPartArray[0], conditionPartArray[2], conditionPartArray[1]);
				splitConditionClass.add(element);
			}
			else if(!Character.isLetter(conditionPartArray[0].charAt(conditionPartArray[0].length()-1))) {
				Restriction element = new Restriction(conditionPartArray[0].substring(0, conditionPartArray[0].length() - 1),  conditionPartArray[1],conditionPartArray[0].substring(conditionPartArray[0].length() - 1));
				splitConditionClass.add(element);
				System.out.println(element.getPropertyName());
			}
		}

		return splitConditionClass;
	}

	/*
	 * Extract the logical operators(AND/OR) from the query, if at all it is
	 * present. For eg: select city,winner,team1,team2,player_of_match from
	 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
	 * bangalore
	 *
	 * The query mentioned above in the example should return a List of Strings
	 * containing [or,and]
	 */
	public List<String> getLogicalOperators(String queryString) {

		queryString=queryString.toLowerCase();
		List<String> logicalOperators =new ArrayList<>();

		if(queryString.contains("where"))
		{
			String whereCondition=queryString.split("order by")[0].trim().split("group by")[0].trim().split("where")[1].trim();

			String[] conditions=whereCondition.split("\\s+");
			for(String word : conditions)
			{
				if(word.equals("and"))
				{
					logicalOperators.add("and");
				}
				else if(word.equals("or"))
				{
					logicalOperators.add("or");
				}
				else if(word.equals("not"))
				{
					logicalOperators.add("not");
				}
			}

			return logicalOperators;
		}
		else
		{
			return null;
		}
	}

	/*
	 * Extract the aggregate functions from the query. The presence of the aggregate
	 * functions can determined if we have either "min" or "max" or "sum" or "count"
	 * or "avg" followed by opening braces"(" after "select" clause in the query
	 * string. in case it is present, then we will have to extract the same. For
	 * each aggregate functions, we need to know the following: 1. type of aggregate
	 * function(min/max/count/sum/avg) 2. field on which the aggregate function is
	 * being applied.
	 *
	 * Please note that more than one aggregate function can be present in a query.
	 *
	 *
	 */
	public List<AggregateFunction> getAggregateFunctions(String queryString) {

		String[] splitString = getSplitStrings(queryString);
		String[] aggregateString = splitString[1].split(",");

		List<AggregateFunction> aggregateList=new ArrayList<>();

		for(String element:aggregateString) {
			if (element.contains("(") && element.contains(")")) {
				int bracketStartIndex = element.indexOf("(");
				int bracketFinalIndex = element.indexOf(")");

				AggregateFunction aggregateListElement = new AggregateFunction();
				aggregateListElement.setField(element.substring(bracketStartIndex+1, bracketFinalIndex));
				aggregateListElement.setFunction(element.substring(0, bracketStartIndex));

				if (element.contains("count")) {
					aggregateList.add(aggregateListElement);
				} else if (element.contains("avg")) {
					aggregateList.add(aggregateListElement);
				} else if (element.contains("min")) {
					aggregateList.add(aggregateListElement);
				} else if (element.contains("max")) {
					aggregateList.add(aggregateListElement);
				} else if (element.contains("sum")) {
					aggregateList.add(aggregateListElement);
				}
			}
		}
		if(aggregateList.size()==0){
			return null;
		}

		return aggregateList;
	}

}