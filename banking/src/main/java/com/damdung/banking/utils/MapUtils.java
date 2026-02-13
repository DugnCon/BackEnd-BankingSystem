package com.damdung.banking.utils;

import org.springframework.stereotype.Component;

import java.util.Map;

public class MapUtils {
    public static <T> T getObject(Map<String,Object> maps, String key, Class<T> TClass) {
        Object object = maps.getOrDefault(key, null);
        if(object != null) {
            if(TClass.getTypeName().equals("java.lang.String")) {
                object = object.toString();
            } else if(TClass.getTypeName().equals("java.lang.Long")) {
                object = Long.parseLong(object.toString());
            } else if(TClass.getTypeName().equals("java.lang.Double")) {
                object = Double.parseDouble(object.toString());
            } else {
                object = Integer.parseInt(object.toString());
            }
            return TClass.cast(object);
        }
        return null;
    }
}
