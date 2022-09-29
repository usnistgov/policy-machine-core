package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.exceptions.PMException;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static org.junit.jupiter.api.Assertions.*;

class MemoryConnectionTest {

    @Test
    void testTwoConnections() throws PMException {
        MemoryPolicyStore policyStore = new MemoryPolicyStore();

        MemoryConnection c1 = new MemoryConnection(policyStore);
        MemoryConnection c2 = new MemoryConnection(policyStore);

        c1.beginTx();
        c2.beginTx();

        c1.graph().createPolicyClass("pc1", null);
        assertTrue(c1.graph().nodeExists("pc1"));
        assertFalse(policyStore.graph().nodeExists("pc1"));

        c2.graph().createPolicyClass("pc2", null);
        assertTrue(c2.graph().nodeExists("pc2"));
        assertFalse(policyStore.graph().nodeExists("pc2"));

        c1.commit();

        assertTrue(policyStore.graph().nodeExists("pc1"));
        assertTrue(c1.graph().nodeExists("pc1"));
        assertFalse(c2.graph().nodeExists("pc1"));

        c2.commit();

        assertTrue(policyStore.graph().nodeExists("pc2"));
        assertTrue(c1.graph().nodeExists("pc2"));
        System.out.println(c2.graph().getPolicyClasses());
        assertTrue(c2.graph().nodeExists("pc1"));
    }

    @Test
    void testTwoConcurrentConnections() throws InterruptedException {
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();

        Thread t1 = new Thread(() -> {
            try {
                MemoryConnection c1 = new MemoryConnection(memoryPolicyStore);
                c1.graph().createPolicyClass("pc1", noprops());
                c1.graph().createObjectAttribute("oa1", noprops(), "pc1");
            } catch (PMException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                MemoryConnection c1 = new MemoryConnection(memoryPolicyStore);
                c1.graph().createUserAttribute("ua1", noprops(), "pc1");
            } catch (PMException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertTrue(memoryPolicyStore.graph().nodeExists("pc1"));
        assertTrue(memoryPolicyStore.graph().nodeExists("ua1"));
        assertTrue(memoryPolicyStore.graph().nodeExists("oa1"));

    }

}