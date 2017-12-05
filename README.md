# orafile-parser
Oracle configuration file parser.One simple parser and writer for .ora file.

![jdk-1.8](https://img.shields.io/badge/JDK-1.8-blue.svg?style=for-the-badge)
![test-passing](https://img.shields.io/badge/test-passing-green.svg?style=for-the-badge)

# Syntax Rules for Configuration Files
[Syntax Rules for Configuration Files](https://docs.oracle.com/cd/E11882_01/network.112/e10835/syntax.htm#NETRF005)

# Usage
```java
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
```