package gov.nist.csd.pm.pip.graph.mysql;

import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.epp.EPPOptions;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.*;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.audit.PReviewAuditor;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemDBGraph;
import gov.nist.csd.pm.pip.memory.MemObligations;
import gov.nist.csd.pm.pip.memory.MemPIP;
import gov.nist.csd.pm.pip.memory.MySQLPIP;
import gov.nist.csd.pm.pip.prohibitions.MemDBProhibitions;
import gov.nist.csd.pm.pip.prohibitions.mysql.MySQLProhibitions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MySQLConnection {

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/policydb_core";
    //private static final String DB_URL = "jdbc:mysql://localhost:3306/policydb_core?profileSQL=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pmAdmin";

    public static void main(String[] args){

        try{
            MySQLConnection connection = new MySQLConnection();
            MySQLGraph mySQLGraph = new MySQLGraph(connection);
            MySQLProhibitions prohibitions = new MySQLProhibitions(connection);
            long start2 = System.currentTimeMillis();

            NumberFormat formatter = new DecimalFormat("#0.00");
            System.out.println("==============Setup PAP ==============");
            MySQLPIP pip = new MySQLPIP(mySQLGraph, prohibitions, new MemObligations());
            MySQLPAP pap = new MySQLPAP(pip);
            OperationSet ops = new OperationSet("read", "write", "execute");


            //PAP pap = new PAP(new GraphAdmin(new MemDBGraph(mySQLGraph)), new ProhibitionsAdmin(new MemDBProhibitions(prohibitions)), new ObligationsAdmin(new MemObligations()));
            PDP pdp = PDP.newPDP(pap, new EPPOptions(), new PReviewDecider(new MemDBGraph(mySQLGraph), new MemDBProhibitions(prohibitions), ops), new PReviewAuditor(mySQLGraph, ops));
            System.out.println("==============Main==============");

            Graph graph = pdp.withUser(new UserContext("super")).getGraph();
            long end2 = System.currentTimeMillis();
            long start3 = System.currentTimeMillis();

            System.out.println(graph.getChildren("super_ua2"));
            System.out.println(graph.getNodes());
            System.out.print("retrieving graph data takes " + formatter.format((end2 - start2) / 1000d) + " seconds"+ "\n");
            System.out.println(graph.getNode("not an node"));
            System.out.println(graph.getChildren("super_pc"));
            System.out.println(graph.getNodes());
            long end3 = System.currentTimeMillis();
            System.out.print("retrieving graph service " + formatter.format((end3 - start3) / 1000d) + " seconds"+ "\n");

            //prohibitions.
/*
            Node alice = graph.getNode("Alice");
            long start2 = System.currentTimeMillis();
            System.out.println(graph.getChildren("super_ua2"));
            long end2 = System.currentTimeMillis();
            System.out.println("\n" + "\n");
            System.out.print("retrieving graph data takes " + formatter.format((end2 - start2) / 1000d) + " seconds"+ "\n");

            Prohibition prohibition = new Prohibition.Builder("new deny ", "super_ua2", new OperationSet("read", "write"))
                    .setIntersection(true)
                    .addContainer(String.valueOf(alice.getId()), true)
                    .build();

            pdp.getProhibitionsService(new UserContext("super")).add(prohibition);

            System.out.println(pap.getProhibitionsAdmin());
            System.out.println(graph.getNodes());*/

           /* MySQLProhibitions prohibitions = new MySQLProhibitions(connection);
           Prohibition prohibition = new Prohibition.Builder("prohibition90 new", "super_oa", new OperationSet("read", "write"))
                    .setIntersection(true)
                    .addContainer("5", true)
                    .build();
           prohibitions.add(prohibition);
           prohibitions.update("prohibition90", prohibition);
           prohibitions.getProhibitionsFor("super_u");*/

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
