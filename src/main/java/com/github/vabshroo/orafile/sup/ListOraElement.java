package com.github.vabshroo.orafile.sup;

import com.github.vabshroo.orafile.AbstractOraElement;
import com.github.vabshroo.orafile.OraElement;
import com.github.vabshroo.orafile.exception.OrafileEditException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/12/4
 * @time 20:40
 * @desc ListOraElement
 */
public class ListOraElement extends AbstractOraElement {

    List<OraElement> value;

    public ListOraElement(String key,List<OraElement> value) {
        super(key);
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public ListOraElement setValue(List<OraElement> value) {
        this.value = value;
        return this;
    }

    public synchronized void add(OraElement oraElement) throws OrafileEditException {
        value.add(oraElement);
    }

    public synchronized void add(int i,OraElement oraElement) throws OrafileEditException {
        value.add(i,oraElement);
    }

    public synchronized List<OraElement> deleteByKey(String key){
        List<OraElement> toRemove = new ArrayList<>();
        for (OraElement oraElement : value) {
            if(key.equals(oraElement.getKey())){
                toRemove.add(oraElement);
            }
        }

        if(toRemove.isEmpty()){
            return null;
        }

        value.removeAll(toRemove);
        return toRemove;
    }

    public synchronized List<OraElement> set(String key,OraElement newOraElement){
        List<OraElement> toUpdate = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            if(key.equals(value.get(i).getKey())){
                toUpdate.add(value.set(i,newOraElement));
            }
        }

        if(toUpdate.isEmpty()){
            return null;
        }

        return toUpdate;
    }

    public  List<OraElement> getList(String key){
        List<OraElement> toReturn = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            if(key.equals(value.get(i).getKey())){
                toReturn.add(value.get(i));
            }
        }

        if(toReturn.isEmpty()){
            return null;
        }

        return toReturn;
    }

    @Override
    public List<ListOraElement> getListElements(String key) {
        List<OraElement> list = getList(key);
        List<ListOraElement> result = null;
        if(list != null){
            result = new ArrayList<>();
            for (OraElement oraElement : list) {
                result.add((ListOraElement) oraElement);
            }
        }
        return result;
    }

    @Override
    public ListOraElement getListElement(String key) {
        return (ListOraElement) get(key);
    }

    public  OraElement get(String key){
        List<OraElement> toReturn = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            if(key.equals(value.get(i).getKey())){
                toReturn.add(value.get(i));
            }
        }

        if(toReturn.isEmpty()){
            return null;
        }

        return toReturn.get(0);
    }


    public String getStringValue() {
        StringBuilder stringBuilder = new StringBuilder();

        if(value != null && !value.isEmpty()){
            for (OraElement oraElement : value) {
                stringBuilder.append(oraElement.toString());
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        if(value != null && !value.isEmpty()){
            stringBuilder.append(LEFT_BRACKET).append(key).append(EQUAL);
            for (OraElement oraElement : value) {
                stringBuilder.append(oraElement.toString());
            }
            stringBuilder.append(RIGHT_BRACKET);
        }

        return stringBuilder.toString();
    }
}
