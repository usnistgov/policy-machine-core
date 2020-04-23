package gov.nist.csd.pm.pip.prohibitions;

import gov.nist.csd.pm.exceptions.PIPException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.mysql.MySQLConnection;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import gov.nist.csd.pm.pip.prohibitions.mysql.MySQLProhibitions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MySQLProhibitionsTest {

    @Test
    void add() throws PIPException {

        Prohibition prohibition = new Prohibition.Builder("prohibition 1", "super_u", new OperationSet("read"))
                .setIntersection(false)
                .addContainer("2", true)
                .build();

        MySQLProhibitions prohibitions = new MySQLProhibitions(new MySQLConnection());
        prohibitions.add(prohibition);

        Prohibition prohibition_test = prohibitions.get("prohibition 1");
        assertEquals("prohibition 1", prohibition_test.getName());
        assertEquals("super_u", prohibition_test.getSubject());
        assertFalse(prohibition_test.isIntersection());
        assertEquals(new OperationSet("read"), prohibition_test.getOperations());
        assertTrue(prohibition.getContainers().containsKey("2"));
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
        MySQLProhibitions prohibitions = new MySQLProhibitions(new MySQLConnection());


        assertThrows(IllegalArgumentException.class, () -> prohibitions.add(p));
        assertThrows(IllegalArgumentException.class, () -> prohibitions.add(p1));
        assertThrows(IllegalArgumentException.class, () -> prohibitions.add(p2));
        assertThrows(PIPException.class, () -> prohibitions.add(p3));

    }

    @Test
    void getAll() throws PIPException{
        MySQLProhibitions prohibitions = new MySQLProhibitions(new MySQLConnection());

        List<Prohibition> list_prohibitions = prohibitions.getAll();
        int sizeTot = list_prohibitions.size();

        Prohibition p = new Prohibition.Builder("new prohibition", "test subject", new OperationSet("read", "write"))
                .build();
        prohibitions.add(p);
        list_prohibitions = prohibitions.getAll();
        assertEquals(sizeTot+1, list_prohibitions.size());
    }

    @Test
    void get() throws PIPException{
        MySQLProhibitions prohibitions = new MySQLProhibitions(new MySQLConnection());

        assertThrows(PIPException.class, () -> prohibitions.get("unknown prohibition"));

        Prohibition prohibition = prohibitions.get("prohibition 1");
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
        MySQLProhibitions prohibitions = new MySQLProhibitions(new MySQLConnection());
        Prohibition prohibitionTest = new Prohibition.Builder("new prohibition test", "newSubject test", new OperationSet("read"))
                .addContainer("5", true)
                .build();
        prohibitions.add(prohibitionTest);

        Prohibition prohibition = prohibitions.get("new prohibition test");

        Prohibition newProhibition = new Prohibition.Builder("new prohibition update", "newSubject update", new OperationSet("new op"))
                .setIntersection(true)
                .addContainer("4", false)
                .build();

        prohibitions.update(prohibition.getName(), newProhibition);

        prohibition = prohibitions.get("new prohibition update");
        assertTrue(prohibition.isIntersection());
        assertEquals("new prohibition update", prohibition.getName());
        assertEquals("newSubject update", prohibition.getSubject());
        assertEquals(new OperationSet("new op"), prohibition.getOperations());
        assertTrue(prohibition.getContainers().containsKey("4"));
        assertFalse(prohibition.getContainers().get("4"));
    }

    @Test
    void delete() throws PIPException{
        MySQLProhibitions prohibitions = new MySQLProhibitions(new MySQLConnection());

        Prohibition p = new Prohibition.Builder("new prohibition to be deleted", "new subject", new OperationSet("read", "write"))
                .build();
        prohibitions.add(p);
        List<Prohibition> list_prohibitions = prohibitions.getAll();

        int sizeTot = list_prohibitions.size();

        prohibitions.delete("new prohibition to be deleted");
        assertEquals(sizeTot -1, prohibitions.getAll().size());
    }

}
