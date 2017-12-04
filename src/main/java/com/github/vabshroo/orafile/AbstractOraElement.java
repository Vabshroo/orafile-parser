package com.github.vabshroo.orafile;

import com.github.vabshroo.orafile.sup.ListOraElement;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/12/4
 * @time 20:34
 * @desc AbstractOraElement
 */
public abstract class AbstractOraElement implements OraElement {

    protected String LEFT_BRACKET = "(";
    protected String RIGHT_BRACKET = ")";
    protected String EQUAL = "=";

    protected String key;

    public AbstractOraElement(String key){
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    protected abstract OraElement get(String key);
    protected abstract List<OraElement> getList(String key);
    protected abstract List<ListOraElement> getListElements(String key);
    protected abstract ListOraElement getListElement(String key);
}
