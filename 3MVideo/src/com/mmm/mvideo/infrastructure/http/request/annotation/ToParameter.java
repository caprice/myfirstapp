package com.mmm.mvideo.infrastructure.http.request.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO: Auto-generated Javadoc
/**
 * The Interface ToParameter.
 * @author a37wczz
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ToParameter {

    /**
     * Name.
     * 
     * @return the string
     */
    String name();

    /**
     * Skip if null.
     * 
     * @return true, if successful
     */
    boolean skipIfNull() default true;

    /**
     * Sign if null.
     * 
     * @return true, if successful
     */
    boolean signIfNull() default false;
}
