package com.stackroute.datamunger.query.parser;

/* This class is used for storing name of field, aggregate function for 
 * each aggregate function
 * */
public class AggregateFunction {

	public String field;
	public String function;


	public String getField(){
		return this.field;
	}

	public void setField(String field){
		this.field = field;
	}
	public String getFunction(){
		return this.function;
	}

	public void setFunction(String function){
		this.function = function;
	}

	@Override
	public String toString() {
		return String.format(this.field+" "+this.function);
	}


}