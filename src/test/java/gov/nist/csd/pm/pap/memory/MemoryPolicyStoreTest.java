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

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;
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
        memoryPolicyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
        AccessRightSet resourceAccessRights = memoryPolicyStore.graph().getResourceAccessRights();
        resourceAccessRights.add("test");

        assertFalse(memoryPolicyStore.graph().getResourceAccessRights().contains("test"));
    }

    @Test
    void getNode() throws PMException {
        memoryPolicyStore.graph().createPolicyClass("pc1", NO_PROPERTIES);
        Node pc1 = memoryPolicyStore.graph().getNode("pc1");
        pc1.getProperties().put("test", "test");

        assertFalse(memoryPolicyStore.graph().getNode("pc1").getProperties().containsKey("test"));
    }

    @Test
    void getPolicyClasses() throws PMException {
        memoryPolicyStore.graph().createPolicyClass("pc1", null);
        memoryPolicyStore.graph().createPolicyClass("pc2", null);
        List<String> policyClasses = memoryPolicyStore.graph().getPolicyClasses();
        policyClasses.add("test");
        assertFalse(memoryPolicyStore.graph().getPolicyClasses().contains("test"));
    }

    @Test
    void getChildren() throws PMException {
        memoryPolicyStore.graph().createPolicyClass("pc1", null);
        memoryPolicyStore.graph().createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.graph().createObjectAttribute("oa2", "pc1");
        memoryPolicyStore.graph().createObjectAttribute("oa3", "pc1");
        List<String> children = memoryPolicyStore.graph().getChildren("pc1");
        children.add("test");
        assertFalse(memoryPolicyStore.graph().getChildren("pc1").contains("test"));
    }

    @Test
    void getParents() throws PMException {
        memoryPolicyStore.graph().createPolicyClass("pc1", null);
        memoryPolicyStore.graph().createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.graph().createObjectAttribute("oa2", "pc1");
        memoryPolicyStore.graph().createObjectAttribute("oa3", "pc1");
        memoryPolicyStore.graph().createObject("o1", "oa1", "oa2", "oa3");
        List<String> parents = memoryPolicyStore.graph().getParents("o1");
        parents.add("test");
        assertFalse(memoryPolicyStore.graph().getParents("o1").contains("test"));
    }

    @Test
    void getAssociationsWithSource() throws PMException {
        memoryPolicyStore.graph().createPolicyClass("pc1", null);
        memoryPolicyStore.graph().createUserAttribute("ua1", "pc1");
        memoryPolicyStore.graph().createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.graph().associate("ua1", "oa1", new AccessRightSet());
        List<Association> assocs = memoryPolicyStore.graph().getAssociationsWithSource("ua1");
        assocs.clear();
        assertFalse(memoryPolicyStore.graph().getAssociationsWithSource("ua1").isEmpty());
    }

    @Test
    void getAssociationsWithTarget() throws PMException {
        memoryPolicyStore.graph().createPolicyClass("pc1", null);
        memoryPolicyStore.graph().createUserAttribute("ua1", "pc1");
        memoryPolicyStore.graph().createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.graph().associate("ua1", "oa1", new AccessRightSet());
        List<Association> assocs = memoryPolicyStore.graph().getAssociationsWithTarget("oa1");
        assocs.clear();
        assertFalse(memoryPolicyStore.graph().getAssociationsWithTarget("oa1").isEmpty());
    }

    @Test
    void getProhibitions() throws PMException {
        memoryPolicyStore.graph().createPolicyClass("pc1", null);
        memoryPolicyStore.graph().createUserAttribute("ua1", "pc1");
        memoryPolicyStore.graph().createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.prohibitions().createProhibition("label", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet(), true, new ContainerCondition("oa1", false));
        Map<String, List<Prohibition>> prohibitions = memoryPolicyStore.prohibitions().getProhibitions();
        prohibitions.clear();
        assertEquals(1, memoryPolicyStore.prohibitions().getProhibitions().size());
        prohibitions = memoryPolicyStore.prohibitions().getProhibitions();
        Prohibition p = prohibitions.get("ua1").get(0);
        p = new Prohibition("test", ProhibitionSubject.userAttribute("ua2"), new AccessRightSet("read"), false, Collections.singletonList(new ContainerCondition("oa2", true)));
        Prohibition actual = memoryPolicyStore.prohibitions().getProhibitionsWithSubject("ua1").get(0);
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
        memoryPolicyStore.graph().createPolicyClass("pc1", null);
        memoryPolicyStore.graph().createUserAttribute("ua1", "pc1");
        memoryPolicyStore.graph().createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.prohibitions().createProhibition("label", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet(), true, new ContainerCondition("oa1", false));
        List<Prohibition> prohibitions = memoryPolicyStore.prohibitions().getProhibitionsWithSubject("ua1");
        prohibitions.clear();
        assertEquals(1, memoryPolicyStore.prohibitions().getProhibitions().size());
        prohibitions = memoryPolicyStore.prohibitions().getProhibitionsWithSubject("ua1");
        Prohibition p = prohibitions.get(0);
        p = new Prohibition("test", ProhibitionSubject.userAttribute("ua2"), new AccessRightSet("read"), false, Collections.singletonList(new ContainerCondition("oa2", true)));
        Prohibition actual = memoryPolicyStore.prohibitions().getProhibitionsWithSubject("ua1").get(0);
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
        memoryPolicyStore.graph().createPolicyClass("pc1", null);
        memoryPolicyStore.graph().createUserAttribute("ua1", "pc1");
        memoryPolicyStore.graph().createObjectAttribute("oa1", "pc1");
        memoryPolicyStore.prohibitions().createProhibition("label", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet(), true, new ContainerCondition("oa1", false));
        Prohibition p = memoryPolicyStore.prohibitions().getProhibition("label");
        p = new Prohibition("test", ProhibitionSubject.userAttribute("ua2"), new AccessRightSet("read"), false, Collections.singletonList(new ContainerCondition("oa2", true)));
        Prohibition actual = memoryPolicyStore.prohibitions().getProhibition("label");
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
        memoryPolicyStore.obligations().createObligation(
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
        List<Obligation> obligations = memoryPolicyStore.obligations().getObligations();
        obligations.clear();
        assertEquals(1, memoryPolicyStore.obligations().getObligations().size());
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

        memoryPolicyStore.obligations().createObligation(
                new UserContext("test"),
                "label",
                rule1
        );

        Obligation obligation = memoryPolicyStore.obligations().getObligation("label");
        assertEquals("label", obligation.getLabel());
        assertEquals(new UserContext("test"), obligation.getAuthor());
        assertEquals(1, obligation.getRules().size());
        assertEquals(rule1, obligation.getRules().get(0));
    }

    @Test
    void testTx() throws PMException {
        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc1");
        try {
            runTx(store, () -> {
                store.graph().createObjectAttribute("oa1", "pc1");
                throw new PMException("test");
            });
        } catch (PMException e) { }
        assertFalse(store.graph().nodeExists("oa1"));
    }

    @Test
    void testTx2() throws PMException {
        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc1");
        store.beginTx();
        store.graph().createObjectAttribute("oa1", "pc1");
        assertTrue(store.graph().nodeExists("oa1"));
        store.rollback();
        assertFalse(store.graph().nodeExists("oa1"));
        store.commit();
        assertFalse(store.graph().nodeExists("oa1"));
    }
}