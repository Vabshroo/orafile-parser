package com.github.vabshroo.orafile;

import com.github.vabshroo.orafile.exception.OrafileParseException;
import com.github.vabshroo.orafile.sup.ListOraElement;
import com.github.vabshroo.orafile.sup.Orafile;
import com.github.vabshroo.orafile.sup.SimpleOraElement;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/12/4
 * @time 20:57
 * @desc OrafileParser
 */
public class OrafileParser {

    private final static String LINE = "([a-z]|[A-Z])+([a-z]|[A-Z]|_|[0-9])*=.*";
    private final static String LIST_LINE = "\\(([a-z]|[A-Z])+([a-z]|[A-Z]|_)*=.*\\){2,}";

    public static Orafile parse(String orafileContent) throws OrafileParseException {

        if(StringUtils.isBlank(orafileContent)){
            return null;
        }

        orafileContent = trimOrafileContent(orafileContent);

        String[] lines = orafileContent.split("\n");
        if(lines.length > 0){
            List<OraElement> oraElements = new ArrayList<>();

            for (String line : lines) {
                generateFromLine(line,oraElements);
            }

            return new Orafile(oraElements);
        }

        return null;
    }

    public static Orafile parse(InputStream inputStream) throws IOException, OrafileParseException {
        StringBuilder orafileContent = new StringBuilder();

        InputStreamReader isr = new InputStreamReader(inputStream);
        char[] buffer = new char[1024];
        while (isr.read(buffer) != -1){
            orafileContent.append(String.valueOf(buffer));
        }

        return parse(orafileContent.toString());
    }

    private static String trimOrafileContent(String orafileContent) {
        StringBuilder stringBuilder = new StringBuilder();

        String[] lines = orafileContent.split("\n");
        for (String line : lines) {
            if(StringUtils.isNotBlank(line) &&  StringUtils.isNotBlank(line.trim()) && !line.startsWith("#")){
                if(line.startsWith(" ") || stringBuilder.length() == 0){
                    stringBuilder.append(line);
                }else{
                    stringBuilder.append("\n").append(line);
                }
            }
        }

        return stringBuilder.toString();
    }

    private static void generateFromLine(String line, List<OraElement> oraElements) throws OrafileParseException {
        if(StringUtils.isBlank(line)){
            return;
        }

        if(!line.matches(LINE) && !line.matches(LIST_LINE)){
            throw new OrafileParseException("Illegal input: " + line);
        }

        Integer index = line.indexOf("=");
        String key = line.substring(0,index);
        String stringValue = line.substring(index + 1);

        generateOraElement(key,stringValue,oraElements);
    }

    private static void generateOraElement(String key, String stringValue, List<OraElement> oraElements) throws OrafileParseException {
        List<String> stringList = new ArrayList<>();
        char[] charArray = stringValue.toCharArray();

        Integer sum = 0;
        Integer start = 0;
        Integer end = 0;
        for (int i = 0; i < charArray.length; i++) {
            switch (charArray[i]){
                case '(':
                    sum -= 1;
                    break;
                case ')':
                    sum +=1;
                    end = i + 1;
                default:
                    break;
            }
            if(sum == 0 && start < end){
                stringList.add(stringValue.substring(start,end));
                start = end;
                end = 0;
            }
        }

        if(sum != 0){
            throw new OrafileParseException("Brackets not match: " + stringValue);
        }

        if(start == 0 && start == end){
            oraElements.add(new SimpleOraElement(key,stringValue));
        }else{
            List<OraElement> oraElementsChild = new ArrayList<>();
            ListOraElement listOraElement = new ListOraElement(key,oraElementsChild);
            oraElements.add(listOraElement);
            for (String s : stringList) {
                String line = s.substring(s.indexOf("(") + 1,s.lastIndexOf(")"));
                generateFromLine(line,oraElementsChild);
            }
        }

    }

}
