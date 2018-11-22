package com.stackroute.datamunger.query;

import java.util.HashMap;
import java.util.Map;

//Header class containing a Collection containing the headers
public class Header extends HashMap<String, Integer> {

    private static final long serialVersionUID = 1L;

    private HashMap<String,Integer> headerMap;

    public  void setHeader(String[] headerArray){
        HashMap<String,Integer> map = new HashMap<String,Integer>();


        for(int i =0;i<headerArray.length;i++){
            map.put(headerArray[i],i);
        }

        this.headerMap = map;

    }

    public HashMap<String, Integer> getHeader(){
        return this.headerMap;
    }

}