package com.github.vabshroo.orafile.sup;

import com.github.vabshroo.orafile.OraElement;
import com.github.vabshroo.orafile.exception.OrafileEditException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/12/4
 * @time 20:53
 * @desc Orafile
 */
public class Orafile extends ListOraElement{

    List<OraElement> value;

    public Orafile(List<OraElement> value){
        super(null,value);
        this.value = value;
    }

    public synchronized void add(OraElement oraElement) throws OrafileEditException {
        if(get(oraElement.getKey()) != null){
            throw new OrafileEditException("Key " + oraElement.getKey() + " already exist!");
        }
        value.add(oraElement);
    }

    public synchronized void add(int i,OraElement oraElement) throws OrafileEditException {
        if(get(oraElement.getKey()) != null){
            throw new OrafileEditException("Key " + oraElement.getKey() + " already exist!");
        }
        value.add(i,oraElement);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        if(value != null && !value.isEmpty()){
            for (OraElement oraElement : value) {
                stringBuilder.append(oraElement.getKey()).append("=").append(oraElement.getStringValue()).append("\n");
            }
        }

        return stringBuilder.toString();
    }
}
