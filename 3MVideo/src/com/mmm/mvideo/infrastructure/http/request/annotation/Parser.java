package com.mmm.mvideo.infrastructure.http.request.annotation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

// TODO: Auto-generated Javadoc
/**
 * The Class Parser.
 * @author a37wczz
 */
public class Parser {

    /**
     * .
     * 
     * @param obj
     *            the obj
     * @return the string
     * @throws IllegalArgumentException
     *             the illegal argument exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    public static String buildGetParam(Object obj)
            throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAnnotationPresent(ToParameter.class)) {
                ToParameter toParameter = field
                        .getAnnotation(ToParameter.class);
                Object value = field.get(obj);
                if (value == null) {
                    if (toParameter.skipIfNull()) {
                        continue;
                    } else {
                        sb.append(toParameter.name()).append("=");
                    }
                } else {
                    sb.append(toParameter.name()).append("=").append(value);
                }
            }
            if (i != fields.length - 1) {
                sb.append("&");
            }
        }
        return sb.toString();
    }
}
