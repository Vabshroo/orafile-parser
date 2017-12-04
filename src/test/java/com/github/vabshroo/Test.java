package com.github.vabshroo;

import com.github.vabshroo.orafile.OrafileParser;
import com.github.vabshroo.orafile.exception.OrafileEditException;
import com.github.vabshroo.orafile.exception.OrafileParseException;
import com.github.vabshroo.orafile.sup.ListOraElement;
import com.github.vabshroo.orafile.sup.Orafile;
import com.github.vabshroo.orafile.sup.SimpleOraElement;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/12/4
 * @time 22:06
 * @desc Test
 */
public class Test {

    public static void main(String[] args) throws OrafileParseException, OrafileEditException {
        Orafile orafile = OrafileParser.parse("LISTENER=  \n" +
                "  (DESCRIPTION=  \n" +
                "    (ADDRESS_LIST=  \n" +
                "      (ADDRESS=(PROTOCOL=tcp)(HOST=sale-server)(PORT=1521))  \n" +
                "      (ADDRESS=(PROTOCOL=ipc)(KEY=extproc))))  \n" +
                "SID_LIST_LISTENER=  \n" +
                "  (SID_LIST=  \n" +
                "    (SID_DESC=  \n" +
                "      (GLOBAL_DBNAME=sales.us.acme.com)  \n" +
                "      (ORACLE_HOME=/oracle10g)  \n" +
                "      (SID_NAME=sales))  \n" +
                "    (SID_DESC=  \n" +
                "      (SID_NAME=plsextproc)  \n" +
                "      (ORACLE_HOME=/oracle10g)  \n" +
                "      (PROGRAM=extproc))) ");
        //orafile.add(new SimpleOraElement("SID_LIST_LISTENER","qq"));//error
        orafile.getListElement("SID_LIST_LISTENER")
                .getListElement("SID_LIST")
                .add(new ListOraElement("SID_DESC",Arrays.asList(new SimpleOraElement("SID_NAME","sidname"),new SimpleOraElement("ORACLE_HOME","/orahome"),new SimpleOraElement("PROGRAM","program"))));
        System.out.println(orafile);
    }

}
