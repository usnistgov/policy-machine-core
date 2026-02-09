package gov.nist.csd.pm.core.pap.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.core.common.prohibition.NodeProhibition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.util.SamplePolicy;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class ProhibitionsQuerierTest extends PAPTestInitializer {


    @Nested
    class GetAll {

        @Test
        void testSuccess() throws PMException {
            long pc1 = pap.modify().graph().createPolicyClass("pc1");
            long subject = pap.modify().graph().createUserAttribute("subject", List.of(pc1));
            long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
            long oa3 = pap.modify().graph().createObjectAttribute("oa3", List.of(pc1));
            long oa4 = pap.modify().graph().createObjectAttribute("oa4", List.of(pc1));

            pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));

            pap.modify().prohibitions().createNodeProhibition("label1", subject, new AccessRightSet("read"),
                    Set.of(id("oa1")),
                    Set.of(id("oa2")),
                    true);
            pap.modify().prohibitions().createNodeProhibition("label2", subject, new AccessRightSet("read"),
                    Set.of(id("oa3")),
                    Set.of(id("oa4")),
                    true);

            Collection<Prohibition> prohibitions = pap.query().prohibitions().getProhibitions();
            assertEquals(2, prohibitions.size());
            checkProhibitions(prohibitions);
        }

        private void checkProhibitions(Collection<Prohibition> prohibitions) throws PMException {
            for (Prohibition p : prohibitions) {
                if (p.getName().equals("label1")) {
                    assertEquals("label1", p.getName());
                    assertTrue(p instanceof NodeProhibition);
                    assertEquals(id("subject"), ((NodeProhibition) p).getNodeId());
                    assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                    assertTrue(p.isConjunctive());
                    assertEquals(Set.of(id("oa1")), p.getInclusionSet());
                    assertEquals(Set.of(id("oa2")), p.getExclusionSet());
                } else if (p.getName().equals("label2")) {
                    assertEquals("label2", p.getName());
                    assertTrue(p instanceof NodeProhibition);
                    assertEquals(id("subject"), ((NodeProhibition) p).getNodeId());
                    assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                    assertTrue(p.isConjunctive());
                    assertEquals(Set.of(id("oa3")), p.getInclusionSet());
                    assertEquals(Set.of(id("oa4")), p.getExclusionSet());
                } else {
                    fail("unexpected prohibition label " + p.getName());
                }
            }
        }
    }

    @Nested
    class GetProhibitionsWithSubject {

        @Test
        void testSuccess() throws PMException {
            long pc1 = pap.modify().graph().createPolicyClass("pc1");
            long subject1 = pap.modify().graph().createUserAttribute("subject1", List.of(pc1));
            long subject2 = pap.modify().graph().createUserAttribute("subject2", List.of(pc1));
            long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
            long oa3 = pap.modify().graph().createObjectAttribute("oa3", List.of(pc1));
            long oa4 = pap.modify().graph().createObjectAttribute("oa4", List.of(pc1));
            pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));

            pap.modify().prohibitions().createNodeProhibition("label1", subject1, new AccessRightSet("read"),
                    Set.of(id("oa1")),
                    Set.of(id("oa2")),
                    true);
            pap.modify().prohibitions().createNodeProhibition("label2", subject2, new AccessRightSet("read"),
                    Set.of(id("oa3")),
                    Set.of(id("oa4")),
                    true);

            Collection<Prohibition> pros = pap.query().prohibitions().getNodeProhibitions(subject1);
            assertEquals(1, pros.size());

            Prohibition p = pros.iterator().next();

            assertEquals("label1", p.getName());
            assertTrue(p instanceof NodeProhibition);
            assertEquals(subject1, ((NodeProhibition) p).getNodeId());
            assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
            assertTrue(p.isConjunctive());
            assertEquals(Set.of(id("oa1")), p.getInclusionSet());
            assertEquals(Set.of(id("oa2")), p.getExclusionSet());
        }
    }

    @Nested
    class GetProhibition {

        @Test
        void testSuccess() throws PMException {
            assertThrows(ProhibitionDoesNotExistException.class,
                    () -> pap.query().prohibitions().getProhibition("pro1"));

            long pc1 = pap.modify().graph().createPolicyClass("pc1");
            long subject = pap.modify().graph().createUserAttribute("subject", List.of(pc1));
            long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
            long oa3 = pap.modify().graph().createObjectAttribute("oa3", List.of(pc1));
            long oa4 = pap.modify().graph().createObjectAttribute("oa4", List.of(pc1));
            pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));

            pap.modify().prohibitions().createNodeProhibition("label1", subject, new AccessRightSet("read"),
                    Set.of(id("oa1")),
                    Set.of(id("oa2")),
                    true);
            pap.modify().prohibitions().createNodeProhibition("label2", subject, new AccessRightSet("read"),
                    Set.of(id("oa3")),
                    Set.of(id("oa4")),
                    true);

            Prohibition p = pap.query().prohibitions().getProhibition("label1");
            assertEquals("label1", p.getName());
            assertTrue(p instanceof NodeProhibition);
            assertEquals(subject, ((NodeProhibition) p).getNodeId());
            assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
            assertTrue(p.isConjunctive());
            assertEquals(Set.of(id("oa1")), p.getInclusionSet());
            assertEquals(Set.of(id("oa2")), p.getExclusionSet());
        }
    }

    @Test
    void testProhibitionExists() throws PMException {
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long subject = pap.modify().graph().createUserAttribute("subject", List.of(pc1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));

        pap.modify().prohibitions().createNodeProhibition("label1", subject, new AccessRightSet("read"),
            Set.of(oa1), Set.of(), true);

        assertTrue(pap.query().prohibitions().prohibitionExists("label1"));
        assertFalse(pap.query().prohibitions().prohibitionExists("label2"));
    }

    @Test
    void testGetInheritedProhibitionsFor() throws PMException, IOException {
        SamplePolicy.loadSamplePolicyFromPML(pap);

        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(id("ua1")));
        long ua3 = pap.modify().graph().createUserAttribute("ua3", List.of(ua2));
        pap.modify().graph().assign(id("u1"), Set.of(ua3));

        pap.modify().prohibitions().createNodeProhibition("label1", ua2, new AccessRightSet("read"),
                Set.of(id("US project")), Set.of(), true);
        pap.modify().prohibitions().createNodeProhibition("label2", ua3, new AccessRightSet("read"),
                Set.of(id("US project")), Set.of(), true);

        Collection<Prohibition> prohibitions = pap.query().prohibitions().getInheritedProhibitionsFor(id("u1"));
        assertEquals(2, prohibitions.size());
    }

    @Test
    void testGetProhibitionsForContainer() throws PMException, IOException {
        SamplePolicy.loadSamplePolicyFromPML(pap);

        pap.modify().prohibitions().createNodeProhibition("label1", id("ua1"), new AccessRightSet("read"),
                Set.of(id("US project")), Set.of(), true);
        pap.modify().prohibitions().createNodeProhibition("label2", id("ua1"), new AccessRightSet("read"),
                Set.of(id("US project")), Set.of(), true);


        Collection<Prohibition> prohibitions = pap.query().prohibitions().getProhibitionsWithContainer(id("US project"));
        assertEquals(2, prohibitions.size());
    }

}
