package com.tothenew.project.OnlineShopping.utils;

import java.util.Set;

public class StringToMapParser {

    public static String toCommaSeparatedString(Set<String> valueSet){
        String values = "";
        if(valueSet.isEmpty())
            return values;

        for(String value : valueSet){
            values = values + value + ",";
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

}
