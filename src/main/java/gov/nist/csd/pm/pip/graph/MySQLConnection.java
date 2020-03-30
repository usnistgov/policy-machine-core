package gov.nist.csd.pm.pip.graph;

import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;

public class MySQLConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/policydb_core";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pmAdmin";


    public static void main(String[] args){

        try{
            Class.forName(DB_DRIVER);
            MySQLGraph mySQLGraph = new MySQLGraph(new MySQLConnection());

            Map<String, String> map = new HashMap<>();
            map.put("key", "value");
            map.put("key2", "value 2");

            /*Node parentNode = mySQLGraph.createPolicyClass("parent",null);
            Node child1Node = mySQLGraph.createNode("child1", UA, null, "parent");
            Node child2Node = mySQLGraph.createNode("child2", OA, map, "parent");*/
            //mySQLGraph.isAssigned(3,6);
            //mySQLGraph.updateNode(10, "Update name2 node user attribute", map);
            //mySQLGraph.deleteNode(11);
            //mySQLGraph.exists(10);
            //mySQLGraph.getPolicies();
            //mySQLGraph.getNodes();
            //mySQLGraph.getNode(10);
            //mySQLGraph.search("name2", "UA", map);
            //mySQLGraph.getChildren(4);
            //mySQLGraph.getParents(8);
            //mySQLGraph.assign(8,3);
            //mySQLGraph.deassign(9,8);
            //mySQLGraph.associate("child1","child2", new OperationSet("read", "write"));
            //mySQLGraph.dissociate(3,8);

            mySQLGraph.getSourceAssociations("child1");
            //mySQLGraph.getTargetAssociations(10);

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