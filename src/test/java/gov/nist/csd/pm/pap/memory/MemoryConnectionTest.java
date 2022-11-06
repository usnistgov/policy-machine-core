package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.exceptions.PMException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class MemoryConnectionTest {

    @Test
    void testTx() throws PMException {
        MemoryPolicyStore store = new MemoryPolicyStore();
        MemoryConnection conn = new MemoryConnection(store);
        conn.graph().createPolicyClass("pc1");
        conn.beginTx();
        conn.graph().createObjectAttribute("oa1", "pc1");
        assertTrue(store.graph().nodeExists("oa1"));
        conn.rollback();
        assertFalse(store.graph().nodeExists("oa1"));
        conn.commit();
        assertFalse(store.graph().nodeExists("oa1"));
    }
}