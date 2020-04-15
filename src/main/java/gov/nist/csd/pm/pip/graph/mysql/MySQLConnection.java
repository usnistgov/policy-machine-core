package gov.nist.csd.pm.pip.graph.mysql;

import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import gov.nist.csd.pm.pip.prohibitions.mysqlProhibition.MySQLProhibitions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;

public class MySQLConnection {

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/policydb_core";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pmAdmin";


    public static void main(String[] args){

        try{
/*            MySQLGraph mySQLGraph = new MySQLGraph(new MySQLConnection());
            Map<String, String> map = new HashMap<>();
            map.put("key", "value");
            map.put("key2", "value 2");

            Node parentNode = mySQLGraph.createPolicyClass("parent",null);
            Node child1Node = mySQLGraph.createNode("child1", UA, null, "parent");
            Node child2Node = mySQLGraph.createNode("child2", OA, map, "parent");

            mySQLGraph.getSourceAssociations("child1");*/


            MySQLProhibitions prohibitions = new MySQLProhibitions(new MySQLConnection());

            Prohibition prohibition = new Prohibition.Builder("prohibition1", "123", new OperationSet("read"))
                    .addContainer("1", true)
                    .build();
            prohibitions.add(prohibition);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            return con;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
