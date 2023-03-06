package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.tx.TxRunner.runTx;
import static org.junit.jupiter.api.Assertions.*;

class MemoryPolicyStoreTest {

    MemoryPolicyStore memoryPolicyStore;

    @BeforeEach
    void setUp() {
        memoryPolicyStore = new MemoryPolicyStore();
    }

    @Test
    void getResourceAccessRights() throws PMException {
        memoryPolicyStore.setResourceAccessRights(new AccessRightSet("read", "write"));
        AccessRightSet resourceAccessRights = memoryPolicyStore.getResourceAccessRights();
        resourceAccessRights.add("test");

        assertFalse(memoryPolicyStore.getResourceAccessRights().contains("test"));
    }

    @Test
    void getNode() throws PMException {
        memoryPolicyStore.createPolicyClass("pc1", null);
        Node pc1 = memoryPolicyStore.getNode("pc1");
        pc1.getProperties().put("test", "test");

        assertFalse(memoryPolicyStore.getNode("pc1").getProperties().containsKey("test"));
    }

    @Test
    void getPolicyClasses() throws PMException {
        memoryPolicyStore.createPolicyClass("pc1", null);
        memoryPolicyStore.createPolicyClass("pc2", null);
        List<String> policyClasses = memoryPolicyStore.getPolicyClasses();
        policyClasses.add("test");
        assertFalse(memoryPolicyStore.getPolicyClasses().contains("test"));
    }

    @Test
    void getChildren() throws PMException {
        memoryPolicyStore.createPolicyClass("pc1", null);
        memoryPolicyStore.createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.createObjectAttribute("oa2", "pc1");
        memoryPolicyStore.createObjectAttribute("oa3", "pc1");
        List<String> children = memoryPolicyStore.getChildren("pc1");
        children.add("test");
        assertFalse(memoryPolicyStore.getChildren("pc1").contains("test"));
    }

    @Test
    void getParents() throws PMException {
        memoryPolicyStore.createPolicyClass("pc1", null);
        memoryPolicyStore.createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.createObjectAttribute("oa2", "pc1");
        memoryPolicyStore.createObjectAttribute("oa3", "pc1");
        memoryPolicyStore.createObject("o1", "oa1", "oa2", "oa3");
        List<String> parents = memoryPolicyStore.getParents("o1");
        parents.add("test");
        assertFalse(memoryPolicyStore.getParents("o1").contains("test"));
    }

    @Test
    void getAssociationsWithSource() throws PMException {
        memoryPolicyStore.createPolicyClass("pc1", null);
        memoryPolicyStore.createUserAttribute("ua1", "pc1");
        memoryPolicyStore.createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.associate("ua1", "oa1", new AccessRightSet());
        List<Association> assocs = memoryPolicyStore.getAssociationsWithSource("ua1");
        assocs.clear();
        assertFalse(memoryPolicyStore.getAssociationsWithSource("ua1").isEmpty());
    }

    @Test
    void getAssociationsWithTarget() throws PMException {
        memoryPolicyStore.createPolicyClass("pc1", null);
        memoryPolicyStore.createUserAttribute("ua1", "pc1");
        memoryPolicyStore.createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.associate("ua1", "oa1", new AccessRightSet());
        List<Association> assocs = memoryPolicyStore.getAssociationsWithTarget("oa1");
        assocs.clear();
        assertFalse(memoryPolicyStore.getAssociationsWithTarget("oa1").isEmpty());
    }

    @Test
    void getProhibitions() throws PMException {
        memoryPolicyStore.createPolicyClass("pc1", null);
        memoryPolicyStore.createUserAttribute("ua1", "pc1");
        memoryPolicyStore.createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.createProhibition("label", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet(), true, new ContainerCondition("oa1", false));
        Map<String, List<Prohibition>> prohibitions = memoryPolicyStore.getProhibitions();
        prohibitions.clear();
        assertEquals(1, memoryPolicyStore.getProhibitions().size());
        prohibitions = memoryPolicyStore.getProhibitions();
        Prohibition p = prohibitions.get("ua1").get(0);
        p = new Prohibition("test", ProhibitionSubject.userAttribute("ua2"), new AccessRightSet("read"), false, Collections.singletonList(new ContainerCondition("oa2", true)));
        Prohibition actual = memoryPolicyStore.getProhibitionsWithSubject("ua1").get(0);
        assertEquals("label", actual.getLabel());
        assertEquals("ua1", actual.getSubject().name());
        assertEquals(ProhibitionSubject.Type.USER_ATTRIBUTE, actual.getSubject().type());
        assertEquals(new AccessRightSet(), actual.getAccessRightSet());
        assertTrue(actual.isIntersection());
        assertEquals(1, actual.getContainers().size());
        assertEquals(new ContainerCondition("oa1", false), actual.getContainers().get(0));
    }

    @Test
    void getProhibitionsFor() throws PMException {
        memoryPolicyStore.createPolicyClass("pc1", null);
        memoryPolicyStore.createUserAttribute("ua1", "pc1");
        memoryPolicyStore.createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.createProhibition("label", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet(), true, new ContainerCondition("oa1", false));
        List<Prohibition> prohibitions = memoryPolicyStore.getProhibitionsWithSubject("ua1");
        prohibitions.clear();
        assertEquals(1, memoryPolicyStore.getProhibitions().size());
        prohibitions = memoryPolicyStore.getProhibitionsWithSubject("ua1");
        Prohibition p = prohibitions.get(0);
        p = new Prohibition("test", ProhibitionSubject.userAttribute("ua2"), new AccessRightSet("read"), false, Collections.singletonList(new ContainerCondition("oa2", true)));
        Prohibition actual = memoryPolicyStore.getProhibitionsWithSubject("ua1").get(0);
        assertEquals("label", actual.getLabel());
        assertEquals("ua1", actual.getSubject().name());
        assertEquals(ProhibitionSubject.Type.USER_ATTRIBUTE, actual.getSubject().type());
        assertEquals(new AccessRightSet(), actual.getAccessRightSet());
        assertTrue(actual.isIntersection());
        assertEquals(1, actual.getContainers().size());
        assertEquals(new ContainerCondition("oa1", false), actual.getContainers().get(0));
    }

    @Test
    void getProhibition() throws PMException {
        memoryPolicyStore.createPolicyClass("pc1", null);
        memoryPolicyStore.createUserAttribute("ua1", "pc1");
        memoryPolicyStore.createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.createProhibition("label", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet(), true, new ContainerCondition("oa1", false));
        Prohibition p = memoryPolicyStore.getProhibition("label");
        p = new Prohibition("test", ProhibitionSubject.userAttribute("ua2"), new AccessRightSet("read"), false, Collections.singletonList(new ContainerCondition("oa2", true)));
        Prohibition actual = memoryPolicyStore.getProhibition("label");
        assertEquals("label", actual.getLabel());
        assertEquals("ua1", actual.getSubject().name());
        assertEquals(ProhibitionSubject.Type.USER_ATTRIBUTE, actual.getSubject().type());
        assertEquals(new AccessRightSet(), actual.getAccessRightSet());
        assertTrue(actual.isIntersection());
        assertEquals(1, actual.getContainers().size());
        assertEquals(new ContainerCondition("oa1", false), actual.getContainers().get(0));
    }

    @Test
    void getObligations() throws PMException {
        memoryPolicyStore.createObligation(
                new UserContext("test"),
                "label",
                new Rule(
                        "rule1",
                        new EventPattern(
                                EventSubject.anyUser(),
                                Performs.events("test_event")
                        ),
                        new Response(
                                new UserContext("test")
                        )
                )
        );
        List<Obligation> obligations = memoryPolicyStore.getObligations();
        obligations.clear();
        assertEquals(1, memoryPolicyStore.getObligations().size());
    }

    @Test
    void getObligation() throws PMException {
        Rule rule1 = new Rule(
                "rule1",
                new EventPattern(
                        EventSubject.anyUser(),
                        Performs.events("test_event")
                ),
                new Response(
                        new UserContext("test")
                )
        );

        memoryPolicyStore.createObligation(
                new UserContext("test"),
                "label",
                rule1
        );

        Obligation obligation = memoryPolicyStore.getObligation("label");
        assertEquals("label", obligation.getLabel());
        assertEquals(new UserContext("test"), obligation.getAuthor());
        assertEquals(1, obligation.getRules().size());
        assertEquals(rule1, obligation.getRules().get(0));
    }

    @Test
    void testTx() throws PMException {
        MemoryPolicyStore store = new MemoryPolicyStore();
        store.createPolicyClass("pc1");
        try {
            runTx(store, () -> {
                store.createObjectAttribute("oa1", "pc1");
                throw new PMException("test");
            });
        } catch (PMException e) { }
        assertFalse(store.nodeExists("oa1"));
    }

    @Test
    void testTx2() throws PMException {
        MemoryPolicyStore store = new MemoryPolicyStore();
        store.createPolicyClass("pc1");
        store.beginTx();
        store.createObjectAttribute("oa1", "pc1");
        assertTrue(store.nodeExists("oa1"));
        store.rollback();
        assertFalse(store.nodeExists("oa1"));
        store.commit();
        assertFalse(store.nodeExists("oa1"));
    }
}