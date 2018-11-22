package com.stackroute.datamunger.query;

import java.util.HashMap;

//This class will be used to store the column data types as columnIndex/DataType
public class RowDataTypeDefinitions extends HashMap<Integer, String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HashMap<Integer,String> dataType;

	public  void setDataType(String[] dataTypeArray){

		for(int i =0;i<dataTypeArray.length;i++){
			this.dataType.put(i,dataTypeArray[i]);
		}

	}

	public HashMap<Integer,String > getDataType(){
		return this.dataType;
	}
}
