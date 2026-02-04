package gov.nist.csd.pm.core.pap.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.util.SamplePolicy;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

            pap.modify().prohibitions().createProhibition("label1", new ProhibitionSubject(subject), new AccessRightSet("read"),
		            true, List.of(
		                    new ContainerCondition(id("oa1"), true),
		                    new ContainerCondition(id("oa2"), false)));
            pap.modify().prohibitions().createProhibition("label2", new ProhibitionSubject(subject), new AccessRightSet("read"),
		            true, List.of(
		                    new ContainerCondition(id("oa3"), true),
		                    new ContainerCondition(id("oa4"), false)));

            Collection<Prohibition> prohibitions = pap.query().prohibitions().getProhibitions();
            assertEquals(2, prohibitions.size());
            checkProhibitions(prohibitions);
        }

        private void checkProhibitions(Collection<Prohibition> prohibitions) throws PMException {
            for (Prohibition p : prohibitions) {
                if (p.getName().equals("label1")) {
                    assertEquals("label1", p.getName());
                    assertEquals(id("subject"), p.getSubject().getNodeId());
                    assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                    assertTrue(p.isIntersection());
                    assertEquals(2, p.getContainers().size());
                    List<ContainerCondition> expected = List.of(
                            new ContainerCondition(id("oa1"), true),
                            new ContainerCondition(id("oa2"), false)
                    );
                    assertTrue(expected.containsAll(p.getContainers()) && p.getContainers().containsAll(expected));
                } else if (p.getName().equals("label2")) {
                    assertEquals("label2", p.getName());
                    assertEquals(id("subject"), p.getSubject().getNodeId());
                    assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                    assertTrue(p.isIntersection());
                    assertEquals(2, p.getContainers().size());
                    List<ContainerCondition> expected = List.of(
                            new ContainerCondition(id("oa3"), true),
                            new ContainerCondition(id("oa4"), false)
                    );
                    assertTrue(expected.containsAll(p.getContainers()) && p.getContainers().containsAll(expected));
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

            pap.modify().prohibitions().createProhibition("label1", new ProhibitionSubject(subject1), new AccessRightSet("read"),
		            true, List.of(new ContainerCondition(id("oa1"), true),
		                    new ContainerCondition(id("oa2"), false)));
            pap.modify().prohibitions().createProhibition("label2", new ProhibitionSubject(subject2), new AccessRightSet("read"),
		            true, List.of(new ContainerCondition(id("oa3"), true),
		                    new ContainerCondition(id("oa4"), false)));

            Collection<Prohibition> pros = pap.query().prohibitions().getProhibitionsWithSubject(new ProhibitionSubject(subject1));
            assertEquals(1, pros.size());

            Prohibition p = pros.iterator().next();

            assertEquals("label1", p.getName());
            assertEquals(subject1, p.getSubject().getNodeId());
            assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
            assertTrue(p.isIntersection());
            assertEquals(2, p.getContainers().size());
            List<ContainerCondition> expected = List.of(
                    new ContainerCondition(id("oa1"), true),
                    new ContainerCondition(id("oa2"), false)
            );
            assertTrue(expected.containsAll(p.getContainers()) && p.getContainers().containsAll(expected));
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

            pap.modify().prohibitions().createProhibition("label1", new ProhibitionSubject(subject), new AccessRightSet("read"),
		            true, List.of(new ContainerCondition(id("oa1"), true),
		                    new ContainerCondition(id("oa2"), false)));
            pap.modify().prohibitions().createProhibition("label2", new ProhibitionSubject(subject), new AccessRightSet("read"),
		            true, List.of(new ContainerCondition(id("oa3"), true),
		                    new ContainerCondition(id("oa4"), false)));

            Prohibition p = pap.query().prohibitions().getProhibition("label1");
            assertEquals("label1", p.getName());
            assertEquals(subject, p.getSubject().getNodeId());
            assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
            assertTrue(p.isIntersection());
            assertEquals(2, p.getContainers().size());
            List<ContainerCondition> expected = List.of(
                    new ContainerCondition(id("oa1"), true),
                    new ContainerCondition(id("oa2"), false)
            );
            assertTrue(expected.containsAll(p.getContainers()) && p.getContainers().containsAll(expected));
        }
    }

    @Test
    void testProhibitionExists() throws PMException {
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long subject = pap.modify().graph().createUserAttribute("subject", List.of(pc1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));

        pap.modify().prohibitions().createProhibition("label1", new ProhibitionSubject(subject), new AccessRightSet("read"),
            true, List.of(new ContainerCondition(oa1, true)));

        assertTrue(pap.query().prohibitions().prohibitionExists("label1"));
        assertFalse(pap.query().prohibitions().prohibitionExists("label2"));
    }

    @Test
    void testGetInheritedProhibitionsFor() throws PMException, IOException {
        SamplePolicy.loadSamplePolicyFromPML(pap);

        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(id("ua1")));
        long ua3 = pap.modify().graph().createUserAttribute("ua3", List.of(ua2));
        pap.modify().graph().assign(id("u1"), Collections.singleton(ua3));

        pap.modify().prohibitions().createProhibition("label1", new ProhibitionSubject(ua2), new AccessRightSet("read"),
		        true, List.of(new ContainerCondition(id("US project"), true)));
        pap.modify().prohibitions().createProhibition("label2", new ProhibitionSubject(ua3), new AccessRightSet("read"),
		        true, List.of(new ContainerCondition(id("US project"), true)));

        Collection<Prohibition> prohibitions = pap.query().prohibitions().getInheritedProhibitionsFor(id("u1"));
        assertEquals(2, prohibitions.size());
    }

    @Test
    void testGetProhibitionsForContainer() throws PMException, IOException {
        SamplePolicy.loadSamplePolicyFromPML(pap);

        pap.modify().prohibitions().createProhibition("label1", new ProhibitionSubject(id("ua1")), new AccessRightSet("read"),
		        true, List.of(new ContainerCondition(id("US project"), true)));
        pap.modify().prohibitions().createProhibition("label2", new ProhibitionSubject(id("ua1")), new AccessRightSet("read"),
		        true, List.of(new ContainerCondition(id("US project"), true)));


        Collection<Prohibition> prohibitions = pap.query().prohibitions().getProhibitionsWithContainer(id("US project"));
        assertEquals(2, prohibitions.size());
    }

}