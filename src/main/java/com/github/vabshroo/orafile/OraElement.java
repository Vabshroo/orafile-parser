package com.github.vabshroo.orafile;

/**
 * Created by IntelliJ IDEA
 * Interface for all element type in .ora file
 * @author chenlei
 * @date 2017/12/4
 * @time 20:27
 * @desc OraElement
 */
public interface OraElement {

    /**
     * return key of key-value pair
     * @return
     */
    String getKey();

    /**
     * return value of key-value pair
     * if value is string,return itself
     * if value is {@link OraElement},return toString()
     * @return
     */
    String getStringValue();

}
