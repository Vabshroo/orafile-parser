package com.github.vabshroo.orafile.sup;

import com.github.vabshroo.orafile.AbstractOraElement;
import com.github.vabshroo.orafile.OraElement;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 * Like (PROTOCOL=tcp)
 * @author chenlei
 * @date 2017/12/4
 * @time 20:35
 * @desc SimpleOraElement
 */
public class SimpleOraElement extends AbstractOraElement {

    private String value;

    public SimpleOraElement(String key,String value){
        super(key);
        this.value = value;
    }

    public SimpleOraElement setValue(String value) {
        this.value = value;
        return this;
    }

    public String getStringValue() {
        return value;
    }

    @Override
    public String toString() {
        return LEFT_BRACKET + key + EQUAL + value + RIGHT_BRACKET;
    }

    @Override
    public OraElement get(String key) {
        return null;
    }

    @Override
    public List<OraElement> getList(String key) {
        return null;
    }

    @Override
    public List<ListOraElement> getListElements(String key) {
        return null;
    }

    @Override
    public ListOraElement getListElement(String key) {
        return null;
    }
}
