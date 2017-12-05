package com.github.vabshroo.orafile;

import com.github.vabshroo.orafile.exception.OrafileParseException;
import com.github.vabshroo.orafile.sup.ListOraElement;
import com.github.vabshroo.orafile.sup.Orafile;
import com.github.vabshroo.orafile.sup.SimpleOraElement;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/12/4
 * @time 20:57
 * @desc OrafileParser
 */
public class OrafileParser {

    private final static String LINE = "([a-z]|[A-Z])+([a-z]|[A-Z]|_|[0-9])*\\s*=\\s*.*";
    private final static String LIST_LINE = "\\(([a-z]|[A-Z])+([a-z]|[A-Z]|_)*\\s*=\\s*.*\\){2,}";
    private final static Set<Character> NETWORK_CHAR_SET = new HashSet<>(Arrays.asList(
            '(',')','<','>','/','\\',
            ',','.',':',';','\'','"','=','-','_',
            '$','+','*','#','&','!','%','?','@',
            '0','1','2','3','4','5','6','7','8','9',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'));
    private final static Set<Character> INVALID_VALUE_CHAR_SET = new HashSet<>(Arrays.asList(' ','\t','\n','\r'));

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

    /**
     * one property one line
     * @param orafileContent
     * @return
     */
    private static String trimOrafileContent(String orafileContent) {
        StringBuilder stringBuilder = new StringBuilder();

        String[] lines = orafileContent.split("\n");
        for (String line : lines) {
            if(StringUtils.isNotBlank(line) &&  StringUtils.isNotBlank(line.trim()) && !line.startsWith("#")){
                line = line.trim();
                Integer quoteD = 0;
                Integer quoteS = 0;
                char[] lineCharArray = line.toCharArray();

                out:
                for (int i = 0; i < lineCharArray.length; ) {
                    switch (lineCharArray[i]){
                        case '\'':
                            quoteS++;
                            i++;
                            break;
                        case '"':
                            quoteD++;
                            i++;
                            break;
                        case '\\':
                            i+=2;
                            break;
                        case '#':
                            if(quoteD % 2 == 0 && quoteS % 2 == 0){
                                line = line.substring(0,i).trim();
                                break out;
                            }
                            i++;
                            break;
                        default:
                            i++;
                            break;

                    }
                }

                if(line.startsWith(" ") || stringBuilder.length() == 0){
                    stringBuilder.append(line.trim());
                }else{
                    stringBuilder.append("\n").append(line.trim());
                }
            }
        }

        return stringBuilder.toString();
    }

    /**
     * parse each line
     * @param line
     * @param oraElements
     * @throws OrafileParseException
     */
    private static void generateFromLine(String line, List<OraElement> oraElements) throws OrafileParseException {
        if(StringUtils.isBlank(line)){
            return;
        }

        if(!line.matches(LINE) && !line.matches(LIST_LINE)){
            throw new OrafileParseException("Illegal input: " + line);
        }

        Integer index = line.indexOf("=");

        String key = line.substring(0,index).trim();
        String stringValue = line.substring(index + 1).trim();

        generateOraElement(key,stringValue,oraElements);
    }

    /**
     * parse element
     * @param key
     * @param stringValue
     * @param oraElements
     * @throws OrafileParseException
     */
    private static void generateOraElement(String key, String stringValue, List<OraElement> oraElements) throws OrafileParseException {
        List<String> stringList = new ArrayList<>();
        char[] charArray = stringValue.toCharArray();

        Integer sum = 0;
        Integer start = 0;
        Integer end = 0;
        Integer quoteD = 0;
        Integer quoteS = 0;
        for (int i = 0; i < charArray.length;) {
            switch (charArray[i]){
                case '\\':
                    i+=2;
                    break;
                case '\'':
                    quoteS ++;
                    i++;
                    break;
                case '"':
                    quoteD ++;
                    i++;
                    break;
                case '(':
                    if(sum == 0){
                        start = i;
                    }
                    sum -= 1;
                    i++;
                    break;
                case ')':
                    sum +=1;
                    if(sum == 0){
                        end = i + 1;
                    }
                    i++;
                    break;
                default:
                    i++;
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

        if(quoteD % 2 > 0 || quoteS % 2 > 0){
            throw new OrafileParseException("quote not match: " + stringValue);
        }

        if(start == 0 && start == end){
            checkStringValue(key,stringValue);
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

    /**
     * check value
     * @param key
     * @param stringValue
     * @throws OrafileParseException
     * @return
     */
    private static String  checkStringValue(String key, String stringValue) throws OrafileParseException {
        if(StringUtils.isBlank(stringValue)){
            throw new OrafileParseException("null value: " + key + "=" + stringValue);
        }

        for (char c : stringValue.toCharArray()) {
            if(INVALID_VALUE_CHAR_SET.contains(c) || !NETWORK_CHAR_SET.contains(c)){
                throw new OrafileParseException("invalid value: " + stringValue);
            }
        }

        return  stringValue;
    }

}
