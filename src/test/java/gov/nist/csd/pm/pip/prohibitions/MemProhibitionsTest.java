package gov.nist.csd.pm.pip.prohibitions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.memory.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemProhibitionsTest {

    private Prohibitions prohibitions;

    @BeforeEach
    void setUp() throws PMException {
        prohibitions = new MemProhibitions();

        Prohibition prohibition = new Prohibition.Builder("prohibition1", "123", new OperationSet("read"))
                .addContainer("1234", true)
                .build();

        prohibitions.add(prohibition);
    }

    @Test
    void createProhibition() throws PMException {
        Prohibition p = null;
        assertThrows(IllegalArgumentException.class, () -> prohibitions.add(p));

        Prohibition prohibition = new Prohibition.Builder("p123", "sub", new OperationSet("read"))
                .setIntersection(false)
                .addContainer("1234", true)
                .build();

        prohibitions.add(prohibition);

        Prohibition p123 = prohibitions.get("p123");
        assertEquals("p123", p123.getName());
        assertEquals("sub", p123.getSubject());
        assertFalse(p123.isIntersection());
        assertEquals(new OperationSet("read"), p123.getOperations());
        assertTrue(prohibition.getContainers().containsKey("1234"));
        assertTrue(prohibition.getContainers().get("1234"));
    }

    @Test
    void getProhibitions() throws PMException {
        List<Prohibition> prohibitions = this.prohibitions.getAll();
        assertEquals(1, prohibitions.size());
    }

    @Test
    void getProhibition() throws PMException {
        assertThrows(PMException.class, () -> prohibitions.get("abcd"));

        Prohibition prohibition = prohibitions.get("prohibition1");
        assertEquals("prohibition1", prohibition.getName());
        assertEquals("123", prohibition.getSubject());
        assertFalse(prohibition.isIntersection());
        assertEquals(prohibition.getOperations(), new OperationSet("read"));

        assertTrue(prohibition.getContainers().containsKey("1234"));
        assertTrue(prohibition.getContainers().get("1234"));
    }

    @Test
    void updateProhibition() throws PMException {
        assertThrows(IllegalArgumentException.class, () -> prohibitions.update(null, null));

        Prohibition prohibition = prohibitions.get("prohibition1");

        Prohibition newPro = new Prohibition.Builder("new prohibition", "newSubject", new OperationSet("new op"))
                .setIntersection(true)
                .addContainer("newCont", false)
                .build();

        prohibitions.update(prohibition.getName(), newPro);

        prohibition = prohibitions.get("prohibition1");
        assertTrue(prohibition.isIntersection());
        assertEquals("prohibition1", prohibition.getName());
        assertEquals("newSubject", prohibition.getSubject());
        assertEquals(new OperationSet("new op"), prohibition.getOperations());
        assertTrue(prohibition.getContainers().containsKey("newCont"));
        assertFalse(prohibition.getContainers().get("newCont"));
    }

    @Test
    void deleteProhibition() throws PMException {
        prohibitions.delete("prohibition1");
        assertTrue(prohibitions.getAll().isEmpty());
    }
}
