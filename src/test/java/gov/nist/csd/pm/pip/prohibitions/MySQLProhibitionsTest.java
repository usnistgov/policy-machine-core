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

        mySQLProhibitions.add(prohibition);

        Prohibition prohibition_test = mySQLProhibitions.get("prohibition 1");
        assertEquals("prohibition 1", prohibition_test.getName());
        assertEquals("super_u", prohibition_test.getSubject());
        assertFalse(prohibition_test.isIntersection());
        assertEquals(new OperationSet("read"), prohibition_test.getOperations());
        assertTrue(prohibition.getContainers().containsKey("new PC"));
        assertTrue(prohibition.getContainers().get("2"));
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
        mySQLProhibitions.add(p);
        list_prohibitions = mySQLProhibitions.getAll();
        assertEquals(sizeTot+1, list_prohibitions.size());
    }

    @Test
    void get() throws PIPException{
        assertThrows(PIPException.class, () -> mySQLProhibitions.get("unknown prohibition"));

        Prohibition prohibition = mySQLProhibitions.get("prohibition 1");
        assertEquals("prohibition 1", prohibition.getName());
        assertEquals("super_u", prohibition.getSubject());
        assertFalse(prohibition.isIntersection());
        assertEquals(prohibition.getOperations(), new OperationSet("read"));

        assertTrue(prohibition.getContainers().containsKey("2"));
        assertTrue(prohibition.getContainers().get("2"));
    }

    @Test
    void getProhibitionsFor() throws PIPException{
        MySQLProhibitions prohibitions = new MySQLProhibitions(new MySQLConnection());

        Prohibition prohibitionTest = new Prohibition.Builder("new prohibition test 106", "2", new OperationSet("read"))
                .setIntersection(false)
                .addContainer("3", true)
                .build();
        prohibitions.add(prohibitionTest);
        assertEquals(1, prohibitions.getProhibitionsFor("2").size());

    }

    @Test
    void update() throws PIPException{
        Prohibition prohibitionTest = new Prohibition.Builder("new prohibition test", "newSubject test", new OperationSet("read"))
                .addContainer("5", true)
                .build();
        mySQLProhibitions.add(prohibitionTest);

        Prohibition prohibition = mySQLProhibitions.get("new prohibition test");

        Prohibition newProhibition = new Prohibition.Builder("new prohibition update", "newSubject update", new OperationSet("new op"))
                .setIntersection(true)
                .addContainer("4", false)
                .build();

        mySQLProhibitions.update(prohibition.getName(), newProhibition);

        prohibition = mySQLProhibitions.get("new prohibition update");
        assertTrue(prohibition.isIntersection());
        assertEquals("new prohibition update", prohibition.getName());
        assertEquals("newSubject update", prohibition.getSubject());
        assertEquals(new OperationSet("new op"), prohibition.getOperations());
        assertTrue(prohibition.getContainers().containsKey("4"));
        assertFalse(prohibition.getContainers().get("4"));
    }

    @Test
    void delete() throws PIPException{
        Prohibition p = new Prohibition.Builder("new prohibition to be deleted", "new subject", new OperationSet("read", "write"))
                .build();

        mySQLProhibitions.add(p);
        List<Prohibition> list_prohibitions = mySQLProhibitions.getAll();

        int sizeTot = list_prohibitions.size();

        mySQLProhibitions.delete("new prohibition to be deleted");
        assertEquals(sizeTot -1, mySQLProhibitions.getAll().size());
    }

}
