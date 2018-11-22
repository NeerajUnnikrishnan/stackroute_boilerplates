package com.stackroute.datamunger.query.parser;

/*
 * This class is used for storing name of field, condition and value for
 * each conditions
 * generate getter and setter for this class,
 * Also override toString method
 * */

import java.util.List;

public class Restriction {

	public String propertyName;
	public String propertyValue;
	public String condition;

	// Write logic for constructor
	public Restriction(String name,	 String value, String condition) {
		this.propertyName = name;
		this.propertyValue = value;
		this.condition = condition;
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	public void setPropertyName( String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return this.propertyValue;
	}

	public void setPropertyValue( String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public String getCondition() {
		return this.condition;
	}

	public void setCondition( String condition) {
		this.condition = condition;
	}
	@Override
	public String toString() {
		return String.format(this.propertyName+" "+ this.propertyValue+" "+	this.condition);
	}

}