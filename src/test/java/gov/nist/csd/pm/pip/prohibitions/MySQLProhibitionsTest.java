package gov.nist.csd.pm.pip.prohibitions;

import gov.nist.csd.pm.exceptions.PIPException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.graph.mysql.MySQLConnection;
import gov.nist.csd.pm.pip.graph.mysql.MySQLGraph;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import gov.nist.csd.pm.pip.prohibitions.mysql.MySQLProhibitions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLProhibitionsTest {

    private MySQLGraph graph = new MySQLGraph(new MySQLConnection());
    private MySQLProhibitions mySQLProhibitions = new MySQLProhibitions(new MySQLConnection());

    public MySQLProhibitionsTest() throws PIPException {
    }

    @Test
    void add() throws PIPException {
        if (!graph.exists("new PC")) {
            graph.createPolicyClass("new PC", null);
            graph.createNode("new node", NodeType.OA, null, "new PC");
        }

        Prohibition prohibition = new Prohibition.Builder("prohibition 2", "super_u", new OperationSet("read"))
                .setIntersection(false)
                .addContainer("new PC", true)
                .build();

        if (!mySQLProhibitions.exists(prohibition.getName())) {
            mySQLProhibitions.add(prohibition);
        }

        Prohibition prohibition_test = mySQLProhibitions.get("prohibition 1");
        assertEquals("prohibition 1", prohibition_test.getName());
        assertEquals("super_u", prohibition_test.getSubject());
        assertFalse(prohibition_test.isIntersection());
        assertEquals(new OperationSet("read"), prohibition_test.getOperations());
        assertTrue(prohibition.getContainers().containsKey("new PC"));
        assertTrue(prohibition.getContainers().get("new PC"));
    }

    @Test
    void addException() throws PIPException {
        Prohibition p = null;
        Prohibition p1 = new Prohibition.Builder("", "subject", new OperationSet("read"))
                .build();
        Prohibition p2 = new Prohibition.Builder("nameRandom", "", new OperationSet("read"))
                .build();
        //prohibition1 is already an existing prohibition
        Prohibition p3 = new Prohibition.Builder("prohibition 1", "super", new OperationSet("read"))
                .addContainer("10", true)
                .build();

        assertThrows(IllegalArgumentException.class, () -> mySQLProhibitions.add(p));
        assertThrows(IllegalArgumentException.class, () -> mySQLProhibitions.add(p1));
        assertThrows(IllegalArgumentException.class, () -> mySQLProhibitions.add(p2));
        assertThrows(PIPException.class, () -> mySQLProhibitions.add(p3));

    }

    @Test
    void getAll() throws PIPException{

        List<Prohibition> list_prohibitions = mySQLProhibitions.getAll();
        int sizeTot = list_prohibitions.size();

        Prohibition p = new Prohibition.Builder("new prohibition", "test subject", new OperationSet("read", "write"))
                .build();
        if (!mySQLProhibitions.exists(p.getName())) {
            mySQLProhibitions.add(p);
            list_prohibitions = mySQLProhibitions.getAll();
            assertEquals(sizeTot+1, list_prohibitions.size());
        } else {
            assertEquals(sizeTot, list_prohibitions.size());
        }

    }

    @Test
    void get() throws PIPException{
        assertThrows(PIPException.class, () -> mySQLProhibitions.get("unknown prohibition"));

        Prohibition prohibition = mySQLProhibitions.get("new prohibition");
        assertEquals("new prohibition", prohibition.getName());
        assertEquals("test subject", prohibition.getSubject());

        assertFalse(prohibition.isIntersection());
        assertEquals(prohibition.getOperations(), new OperationSet("read", "write"));
    }

    @Test
    void getProhibitionsFor() throws PIPException{

        Prohibition prohibitionTest = new Prohibition.Builder("new prohibition test 106", "2", new OperationSet("read"))
                .setIntersection(false)
                .addContainer("super_ua1", true)
                .build();
        if (!mySQLProhibitions.exists(prohibitionTest.getName())) {
            mySQLProhibitions.add(prohibitionTest);
        }
        assertEquals(1, mySQLProhibitions.getProhibitionsFor("2").size());

    }

    @Test
    void update() throws PIPException{
        Prohibition prohibitionTest = new Prohibition.Builder("new prohibition test", "newSubject test", new OperationSet("read"))
                .addContainer("super_ua2", true)
                .build();
        if (!mySQLProhibitions.exists(prohibitionTest.getName())) {
            mySQLProhibitions.add(prohibitionTest);
        }
        Prohibition prohibition = mySQLProhibitions.get("new prohibition test");

        Prohibition prohibition2 = new Prohibition.Builder("new prohibition update2", "newSubject update", new OperationSet("new op"))
                .setIntersection(true)
                .addContainer("super_default_UA", false)
                .build();

        if (!mySQLProhibitions.exists(prohibition2.getName())) {
            mySQLProhibitions.update(prohibition.getName(), prohibition2);

            prohibition = mySQLProhibitions.get("new prohibition update");
            assertTrue(prohibition.isIntersection());
            assertEquals("new prohibition update2", prohibition.getName());
            assertEquals("newSubject update", prohibition.getSubject());
            assertEquals(new OperationSet("new op"), prohibition.getOperations());
            assertTrue(prohibition.getContainers().containsKey("super_default_UA"));
            assertFalse(prohibition.getContainers().get("super_default_UA"));
        }
    }

    @Test
    void delete() throws PIPException {
        Prohibition p = new Prohibition.Builder("new prohibition to be deleted", "new subject", new OperationSet("read", "write"))
                .build();

        List<Prohibition> list_prohibitions = mySQLProhibitions.getAll();
        int sizeTot = list_prohibitions.size();

        if (!mySQLProhibitions.exists(p.getName())) {
            mySQLProhibitions.add(p);

            list_prohibitions = mySQLProhibitions.getAll();
            sizeTot = list_prohibitions.size();

            mySQLProhibitions.delete(p.getName());
            assertEquals(sizeTot-1, mySQLProhibitions.getAll().size());
        } else {
            assertEquals(sizeTot, mySQLProhibitions.getAll().size());
        }
    }

}
