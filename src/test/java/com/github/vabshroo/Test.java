package com.github.vabshroo;

import com.github.vabshroo.orafile.OrafileParser;
import com.github.vabshroo.orafile.exception.OrafileEditException;
import com.github.vabshroo.orafile.exception.OrafileParseException;
import com.github.vabshroo.orafile.sup.ListOraElement;
import com.github.vabshroo.orafile.sup.Orafile;
import com.github.vabshroo.orafile.sup.SimpleOraElement;

import java.io.FileInputStream;
import java.util.ArrayList;
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
        //parse from String or InputStream
        //Orafile orafile = OrafileParser.parse(new FileInputStream("/u01/app/oracle/product/11.2.0.4/dbhome_1/network/admin/listener.ora"));
        //Orafile orafile = new Orafile(new ArrayList<>());
        /*Orafile orafile = OrafileParser.parse("LISTENER=  \n" +
                "  (DESCRIPTION=  \n" +
                "    (ADDRESS_LIST=  \n" +
                "      (ADDRESS=(PROTOCOL=tcp)(HOST=sale-server)(PORT=1521))  \n" +
                "      (ADDRESS=(PROTOCOL=ipc)(KEY='extproc'))))  \n" +
                "SID_LIST_LISTENER=  \n" +
                "  (SID_LIST=  \n" +
                "    (SID_DESC=  \n" +
                "      (GLOBAL_DBNAME=sales.us.acme.com)  \n" +
                "      (ORACLE_HOME=/oracle10g)  \n" +
                "      (SID_NAME=sales))  \n" +
                "    (SID_DESC=  \n" +
                "      (SID_NAME=plsextproc)  \n" +
                "      (ORACLE_HOME=/oracle10g)  \n" +
                "      (PROGRAM=extproc))) ");*/
        //orafile.add(new SimpleOraElement("SID_LIST_LISTENER","qq"));//error for duplicated key
        /*orafile.getListElement("SID_LIST_LISTENER")
                .getListElement("SID_LIST")
                .add(new ListOraElement("SID_DESC",Arrays.asList(new SimpleOraElement("SID_NAME","sidname"),new SimpleOraElement("ORACLE_HOME","/orahome"),new SimpleOraElement("PROGRAM","program"))));*/
        Orafile orafile = OrafileParser.parse("LISTENER_DG=(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=IPC)(KEY=LISTENER_DG))))\t\t# line added by Agent\n" +
                "LISTENER=(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=IPC)(KEY=LISTENER))))\t\t# line added by Agent\n" +
                "LISTENER_SCAN1=(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=IPC)(KEY=LISTENER_SCAN1))))\t\t# line added by Agent\n" +
                "ENABLE_GLOBAL_DYNAMIC_ENDPOINT_LISTENER_SCAN1=ON\t\t# line added by Agent\n" +
                "ENABLE_GLOBAL_DYNAMIC_ENDPOINT_LISTENER='ON# line added by Agent'\n" +
                "ENABLE_GLOBAL_DYNAMIC_ENDPOINT_LISTENER_DG=ON\t\t# line added by Agent\n" +
                "INBOUND_CONNECT_TIMEOUT_listener = 0\n" +
                "DIAG_ADR_ENABLED_listener = OFF\n" +
                "INBOUND_CONNECT_TIMEOUT_listener_dg = 0\n" +
                "DIAG_ADR_ENABLED_listener_dg = OFForacle");
        System.out.println(orafile);
    }

}
