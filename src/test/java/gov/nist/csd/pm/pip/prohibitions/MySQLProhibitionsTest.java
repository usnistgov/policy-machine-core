package gov.nist.csd.pm.pip.prohibitions;

import gov.nist.csd.pm.exceptions.PIPException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.policies.SuperPolicy;
import gov.nist.csd.pm.pip.graph.mysql.MySQLConnection;
import gov.nist.csd.pm.pip.graph.mysql.MySQLGraph;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import gov.nist.csd.pm.pip.prohibitions.mysql.MySQLProhibitions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLProhibitionsTest {

    private MySQLGraph graph = new MySQLGraph(new MySQLConnection());
    private MySQLProhibitions mySQLProhibitions = new MySQLProhibitions(new MySQLConnection());

    @BeforeEach
    void setup() throws PMException {
        //clear the prohibitions
        for (Prohibition p: mySQLProhibitions.getAll()) {
            mySQLProhibitions.delete(p.getName());
        }

        //clear the graph and add setup configuration
        graph.deleteAll();
        SuperPolicy superPolicy = new SuperPolicy();
        superPolicy.configure(graph);
        System.out.println(graph.getNodes());
    }

    public MySQLProhibitionsTest() throws PIPException {
    }

    @Test
    void add() throws PIPException {

        Prohibition p1 = new Prohibition.Builder("prohibition 1", "super_u", new OperationSet("read"))
                .setIntersection(false)
                .addContainer("super_pc", true)
                .build();

        mySQLProhibitions.add(p1);

        Prohibition prohibition = mySQLProhibitions.get(p1.getName());
        assertEquals(p1.getName(), prohibition.getName());
        assertEquals(p1.getSubject(), prohibition.getSubject());
        assertEquals(p1.isIntersection(), prohibition.isIntersection());
        assertEquals(p1.getOperations(), prohibition.getOperations());
        assertEquals(p1.getContainers(), prohibition.getContainers());
    }

    @Test
    void addException() throws PIPException {
        Prohibition p3 = new Prohibition.Builder("prohibition 1", "super", new OperationSet("read"))
                .build();
        mySQLProhibitions.add(p3);

        //a null prohibition - a null or empty name - a null or empty subject
        Prohibition p = null;
        Prohibition p1 = new Prohibition.Builder("", "subject", new OperationSet("read"))
                .build();
        Prohibition p2 = new Prohibition.Builder("nameRandom", "", new OperationSet("read"))
                .build();
        //Another prohibition with the same name already exists
        Prohibition p3_sameName = new Prohibition.Builder("prohibition 1", "super_u", new OperationSet("read"))
                .addContainer("10", true)
                .build();

        assertThrows(IllegalArgumentException.class, () -> mySQLProhibitions.add(p));
        assertThrows(IllegalArgumentException.class, () -> mySQLProhibitions.add(p1));
        assertThrows(IllegalArgumentException.class, () -> mySQLProhibitions.add(p2));
        assertThrows(PIPException.class, () -> mySQLProhibitions.add(p3_sameName));

    }

    @Test
    void getAll() throws PIPException{
        //sizeTot is 0 since we cleared all the prohibitions
        int sizeTot = mySQLProhibitions.getAll().size();

        Prohibition p = new Prohibition.Builder("prohibition1", "subject", new OperationSet("read"))
                .build();
            mySQLProhibitions.add(p);

            assertEquals(sizeTot+1, mySQLProhibitions.getAll().size());
    }

    @Test
    void get() throws PIPException{
        Prohibition p = new Prohibition.Builder("new prohibition", "subject", new OperationSet("read", "write"))
                .addContainer("super_pc", true)
                .build();

        mySQLProhibitions.add(p);
        Prohibition prohibition = mySQLProhibitions.get(p.getName());
        assertEquals(p.getName(), prohibition.getName());
        assertEquals(p.getSubject(), prohibition.getSubject());
        assertEquals(p.getOperations(), prohibition.getOperations());
        assertEquals(p.getContainers(), prohibition.getContainers());
        assertEquals(p.isIntersection(), prohibition.isIntersection());
    }

    @Test
    void getException() throws PIPException{
        assertThrows(PIPException.class, () -> mySQLProhibitions.get("unknown prohibition"));
    }

    @Test
    void getProhibitionsFor() throws PIPException{

        Prohibition prohibition = new Prohibition.Builder("prohibition", "subject", new OperationSet("read"))
                .setIntersection(false)
                .addContainer("super_ua1", true)
                .build();

        Prohibition prohibition2 = new Prohibition.Builder("prohibition2", "subject", new OperationSet("read"))
                .setIntersection(false)
                .build();

        mySQLProhibitions.add(prohibition);
        mySQLProhibitions.add(prohibition2);
        System.out.println(prohibition.getSubject());
        assertEquals(2, mySQLProhibitions.getProhibitionsFor("subject").size());

    }

    @Test
    void update() throws PIPException{
        Prohibition prohibition = new Prohibition.Builder("prohibition", "subject", new OperationSet("read"))
                .setIntersection(false)
                .build();

        mySQLProhibitions.add(prohibition);
        Prohibition prohibition_get = mySQLProhibitions.get(prohibition.getName());

        //prohibition2 is used in order to update prohibition
        Prohibition prohibition2 = new Prohibition.Builder("prohibition update", "super_oa", new OperationSet("read", "write"))
                .setIntersection(true)
                .addContainer("super_pc", true)
                .build();

        //prohibition has now the same values as prohibition2
        mySQLProhibitions.update(prohibition.getName(), prohibition2);

        prohibition = mySQLProhibitions.get(prohibition2.getName());
        assertEquals(prohibition.isIntersection(), prohibition2.isIntersection());
        assertEquals(prohibition.getName(), prohibition2.getName());
        assertEquals(prohibition.getSubject(), prohibition2.getSubject());
        assertEquals(prohibition.getOperations(), prohibition2.getOperations());
        assertEquals(prohibition.getContainers(), prohibition2.getContainers());

    }

    @Test
    void updateException() throws PIPException {
        Prohibition prohibition = new Prohibition.Builder("prohibition", "subject", new OperationSet("read"))
                .setIntersection(false)
                .build();

        mySQLProhibitions.add(prohibition);
        //a null prohibition - a null or empty name - an already existing name
        Prohibition p = null;
        Prohibition p1 = new Prohibition.Builder("", "subject", new OperationSet("read"))
                .build();
        //Another prohibition with the same name already exists
        Prohibition p2 = new Prohibition.Builder("prohibition", "super_u", new OperationSet("read"))
                .build();

        assertThrows(IllegalArgumentException.class,() -> mySQLProhibitions.update(prohibition.getName(), p));
        assertThrows(IllegalArgumentException.class,() -> mySQLProhibitions.update(prohibition.getName(), p1));
        assertThrows(PIPException.class,() -> mySQLProhibitions.update(prohibition.getName(), p2));
    }

    @Test
    void delete() throws PIPException {
        Prohibition p = new Prohibition.Builder("new prohibition to be deleted", "new subject", new OperationSet("read"))
                .build();

        mySQLProhibitions.add(p);
        int sizeTot = mySQLProhibitions.getAll().size();
        mySQLProhibitions.delete(p.getName());

        assertEquals(sizeTot-1, mySQLProhibitions.getAll().size());
    }

    @Test
    void deleteException() throws PIPException {
        assertThrows(PIPException.class, () -> mySQLProhibitions.delete("a prohibition that do not exist"));
    }

}
