package gov.nist.csd.pm.pip.graph;

import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MySQLConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/policydb_core";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pmAdmin";


    public static void main(String[] args){

        try{
            Class.forName(DB_DRIVER);
            MySQLGraph mySQLGraph = new MySQLGraph();

            Map<String, String> map = new HashMap<>();
            map.put("name", "valueName");
            map.put("exempleJson", "valueJSON");
            map.put("update1", "value Update 1");

            //mySQLGraph.createNode(11,"node User Attribute", NodeType.PC, map);
            //mySQLGraph.updateNode(10, "Update name node user attribute", map);
            //mySQLGraph.deleteNode(11);
            //mySQLGraph.exists(10);
            //mySQLGraph.getPolicies();
            //mySQLGraph.getNodes();
            //mySQLGraph.getNode(10);
            //mySQLGraph.search("name2", "UA", map);
            //mySQLGraph.getChildren(4);
            //mySQLGraph.getParents(8);
            //mySQLGraph.assign(8,2);
            //mySQLGraph.deassign(8,2);
            //mySQLGraph.associate(3,8,new HashSet<>(Arrays.asList( "write")));
            //mySQLGraph.dissociate(3,8);

            //mySQLGraph.getSourceAssociations(3);
            mySQLGraph.getTargetAssociations(10);

        } catch (Exception e){
            System.out.println(e);
        }
    }

    public static Connection getConnection() {
        Connection con = null;

        try {
            con = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            return con;
        } catch (SQLException ex) {
            return null;
        }
    }

}