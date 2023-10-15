package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.subject.AnyUserSubject;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.policy.tx.TxRunner.runTx;
import static org.junit.jupiter.api.Assertions.*;

class MemoryPolicyStoreTest {

    MemoryPolicyStore policyStore = new MemoryPolicyStore();

    MemoryPolicyStoreTest() throws PMException {
    }

    @Test
    void getResourceAccessRights() throws PMException {
        policyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
        AccessRightSet resourceAccessRights = policyStore.graph().getResourceAccessRights();
        resourceAccessRights.add("test");

        assertFalse(policyStore.graph().getResourceAccessRights().contains("test"));
    }

    @Test
    void getNode() throws PMException {
        policyStore.graph().createPolicyClass("pc1", NO_PROPERTIES);
        Node pc1 = policyStore.graph().getNode("pc1");
        pc1.getProperties().put("test", "test");

        assertFalse(policyStore.graph().getNode("pc1").getProperties().containsKey("test"));
    }

    @Test
    void getPolicyClasses() throws PMException {
        policyStore.graph().createPolicyClass("pc1", null);
        policyStore.graph().createPolicyClass("pc2", null);
        List<String> policyClasses = policyStore.graph().getPolicyClasses();
        policyClasses.add("test");
        assertFalse(policyStore.graph().getPolicyClasses().contains("test"));
    }

    @Test
    void getChildren() throws PMException {
        policyStore.graph().createPolicyClass("pc1", null);
        policyStore.graph().createObjectAttribute("oa1", "pc1");
        policyStore.graph().createObjectAttribute("oa2", "pc1");
        policyStore.graph().createObjectAttribute("oa3", "pc1");
        List<String> children = policyStore.graph().getChildren("pc1");
        children.add("test");
        assertFalse(policyStore.graph().getChildren("pc1").contains("test"));
    }

    @Test
    void getParents() throws PMException {
        policyStore.graph().createPolicyClass("pc1", null);
        policyStore.graph().createObjectAttribute("oa1", "pc1");
        policyStore.graph().createObjectAttribute("oa2", "pc1");
        policyStore.graph().createObjectAttribute("oa3", "pc1");
        policyStore.graph().createObject("o1", "oa1", "oa2", "oa3");
        List<String> parents = policyStore.graph().getParents("o1");
        parents.add("test");
        assertFalse(policyStore.graph().getParents("o1").contains("test"));
    }

    @Test
    void getAssociationsWithSource() throws PMException {
        policyStore.graph().createPolicyClass("pc1", null);
        policyStore.graph().createUserAttribute("ua1", "pc1");
        policyStore.graph().createObjectAttribute("oa1", "pc1");
        policyStore.graph().associate("ua1", "oa1", new AccessRightSet());
        List<Association> assocs = policyStore.graph().getAssociationsWithSource("ua1");
        assocs.clear();
        assertFalse(policyStore.graph().getAssociationsWithSource("ua1").isEmpty());
    }

    @Test
    void getAssociationsWithTarget() throws PMException {
        policyStore.graph().createPolicyClass("pc1", null);
        policyStore.graph().createUserAttribute("ua1", "pc1");
        policyStore.graph().createObjectAttribute("oa1", "pc1");
        policyStore.graph().associate("ua1", "oa1", new AccessRightSet());
        List<Association> assocs = policyStore.graph().getAssociationsWithTarget("oa1");
        assocs.clear();
        assertFalse(policyStore.graph().getAssociationsWithTarget("oa1").isEmpty());
    }

    @Test
    void getProhibitions() throws PMException {
        policyStore.graph().createPolicyClass("pc1", null);
        policyStore.graph().createUserAttribute("ua1", "pc1");
        policyStore.graph().createObjectAttribute("oa1", "pc1");
        policyStore.prohibitions().create("pro1", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet(), true, new ContainerCondition("oa1", false));
        Map<String, List<Prohibition>> prohibitions = policyStore.prohibitions().getAll();
        prohibitions.clear();
        assertEquals(1, policyStore.prohibitions().getAll().size());
        prohibitions = policyStore.prohibitions().getAll();
        Prohibition p = prohibitions.get("ua1").get(0);
        p = new Prohibition("test", ProhibitionSubject.userAttribute("ua2"), new AccessRightSet("read"), false, Collections.singletonList(new ContainerCondition("oa2", true)));
        Prohibition actual = policyStore.prohibitions().getWithSubject("ua1").get(0);
        assertEquals("pro1", actual.getName());
        assertEquals("ua1", actual.getSubject().getName());
        assertEquals(ProhibitionSubject.Type.USER_ATTRIBUTE, actual.getSubject().getType());
        assertEquals(new AccessRightSet(), actual.getAccessRightSet());
        assertTrue(actual.isIntersection());
        assertEquals(1, actual.getContainers().size());
        assertEquals(new ContainerCondition("oa1", false), actual.getContainers().get(0));
    }

    @Test
    void getProhibitionsFor() throws PMException {
        policyStore.graph().createPolicyClass("pc1", null);
        policyStore.graph().createUserAttribute("ua1", "pc1");
        policyStore.graph().createObjectAttribute("oa1", "pc1");
        policyStore.prohibitions().create("pro1", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet(), true, new ContainerCondition("oa1", false));
        List<Prohibition> prohibitions = policyStore.prohibitions().getWithSubject("ua1");
        prohibitions.clear();
        assertEquals(1, policyStore.prohibitions().getAll().size());
        prohibitions = policyStore.prohibitions().getWithSubject("ua1");
        Prohibition p = prohibitions.get(0);
        Prohibition actual = policyStore.prohibitions().getWithSubject("ua1").get(0);
        assertEquals("pro1", actual.getName());
        assertEquals("ua1", actual.getSubject().getName());
        assertEquals(ProhibitionSubject.Type.USER_ATTRIBUTE, actual.getSubject().getType());
        assertEquals(new AccessRightSet(), actual.getAccessRightSet());
        assertTrue(actual.isIntersection());
        assertEquals(1, actual.getContainers().size());
        assertEquals(new ContainerCondition("oa1", false), actual.getContainers().get(0));
    }

    @Test
    void getProhibition() throws PMException {
        policyStore.graph().createPolicyClass("pc1", null);
        policyStore.graph().createUserAttribute("ua1", "pc1");
        policyStore.graph().createObjectAttribute("oa1", "pc1");
        policyStore.prohibitions().create("pro1", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet(), true, new ContainerCondition("oa1", false));
        Prohibition p = policyStore.prohibitions().get("pro1");
        Prohibition actual = policyStore.prohibitions().get("pro1");
        assertEquals("pro1", actual.getName());
        assertEquals("ua1", actual.getSubject().getName());
        assertEquals(ProhibitionSubject.Type.USER_ATTRIBUTE, actual.getSubject().getType());
        assertEquals(new AccessRightSet(), actual.getAccessRightSet());
        assertTrue(actual.isIntersection());
        assertEquals(1, actual.getContainers().size());
        assertEquals(new ContainerCondition("oa1", false), actual.getContainers().get(0));
    }

    @Test
    void getObligations() throws PMException {
        policyStore.graph().createPolicyClass("pc1");
        policyStore.graph().createUserAttribute("ua1", "pc1");
        policyStore.graph().createUser("u1", "ua1");
        policyStore.obligations().create(
                new UserContext("u1"),
                "obl1",
                new Rule(
                        "rule1",
                        new EventPattern(
                                new AnyUserSubject(),
                                Performs.events("test_event")
                        ),
                        new Response(
                                new UserContext("test")
                        )
                )
        );
        List<Obligation> obligations = policyStore.obligations().getAll();
        obligations.clear();
        assertEquals(1, policyStore.obligations().getAll().size());
    }

    @Test
    void getObligation() throws PMException {
        Rule rule1 = new Rule(
                "rule1",
                new EventPattern(
                        new AnyUserSubject(),
                        Performs.events("test_event")
                ),
                new Response(
                        new UserContext("test")
                )
        );

        policyStore.graph().createPolicyClass("pc1");
        policyStore.graph().createUserAttribute("ua1", "pc1");
        policyStore.graph().createUser("u1", "ua1");
        policyStore.obligations().create(
                new UserContext("u1"),
                "obl1",
                rule1
        );

        Obligation obligation = policyStore.obligations().get("obl1");
        assertEquals("obl1", obligation.getName());
        assertEquals(new UserContext("u1"), obligation.getAuthor());
        assertEquals(1, obligation.getRules().size());
        assertEquals(rule1, obligation.getRules().get(0));
    }

    @Test
    void testTx() throws PMException {
        PolicyStore store = new MemoryPolicyStore();
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
        PolicyStore store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc1");
        store.beginTx();
        store.graph().createObjectAttribute("oa1", "pc1");
        assertTrue(store.graph().nodeExists("oa1"));
        store.rollback();
        assertFalse(store.graph().nodeExists("oa1"));
        store.commit();
        assertFalse(store.graph().nodeExists("oa1"));
    }

    @Test
    void testSetGraph() throws PMException {
        MemoryPolicyStore policyStore = new MemoryPolicyStore();

        MemoryPolicyStore policyStore1 = new MemoryPolicyStore();
        policyStore1.graph().createPolicyClass("pc1");

        policyStore.setGraph(policyStore1.graph());
        assertTrue(policyStore.graph().nodeExists("pc1"));
    }
}