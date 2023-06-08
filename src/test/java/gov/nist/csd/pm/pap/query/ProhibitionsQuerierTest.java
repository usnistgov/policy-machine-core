package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.PAPTestInitializer;
import gov.nist.csd.pm.pap.exception.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.util.SamplePolicy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ProhibitionsQuerierTest extends PAPTestInitializer {


    @Nested
    class GetAll {

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("subject", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa3", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa4", List.of("pc1"));

            pap.modify().operations().setResourceOperations(new AccessRightSet("read", "write"));

            pap.modify().prohibitions().createProhibition("label1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                    List.of(
                            new ContainerCondition("oa1", true),
                            new ContainerCondition("oa2", false)));
            pap.modify().prohibitions().createProhibition("label2", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                    List.of(
                            new ContainerCondition("oa3", true),
                            new ContainerCondition("oa4", false)));

            Map<String, Collection<Prohibition>> prohibitions = pap.query().prohibitions().getProhibitions();
            assertEquals(1, prohibitions.size());
            assertEquals(2, prohibitions.get("subject").size());
            checkProhibitions(prohibitions.get("subject"));
        }

        private void checkProhibitions(Collection<Prohibition> prohibitions) {
            for (Prohibition p : prohibitions) {
                if (p.getName().equals("label1")) {
                    assertEquals("label1", p.getName());
                    assertEquals("subject", p.getSubject().getName());
                    assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                    assertTrue(p.isIntersection());
                    assertEquals(2, p.getContainers().size());
                    List<ContainerCondition> expected = List.of(
                            new ContainerCondition("oa1", true),
                            new ContainerCondition("oa2", false)
                    );
                    assertTrue(expected.containsAll(p.getContainers()) && p.getContainers().containsAll(expected));
                } else if (p.getName().equals("label2")) {
                    assertEquals("label2", p.getName());
                    assertEquals("subject", p.getSubject().getName());
                    assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                    assertTrue(p.isIntersection());
                    assertEquals(2, p.getContainers().size());
                    List<ContainerCondition> expected = List.of(
                            new ContainerCondition("oa3", true),
                            new ContainerCondition("oa4", false)
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
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("subject1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("subject2", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa3", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa4", List.of("pc1"));
            pap.modify().operations().setResourceOperations(new AccessRightSet("read", "write"));

            pap.modify().prohibitions().createProhibition("label1", ProhibitionSubject.userAttribute("subject1"), new AccessRightSet("read"), true,
                    List.of(new ContainerCondition("oa1", true),
                            new ContainerCondition("oa2", false)));
            pap.modify().prohibitions().createProhibition("label2", ProhibitionSubject.userAttribute("subject2"), new AccessRightSet("read"), true,
                    List.of(new ContainerCondition("oa3", true),
                            new ContainerCondition("oa4", false)));

            Collection<Prohibition> pros = pap.query().prohibitions().getProhibitionsWithSubject("subject1");
            assertEquals(1, pros.size());

            Prohibition p = pros.iterator().next();

            assertEquals("label1", p.getName());
            assertEquals("subject1", p.getSubject().getName());
            assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
            assertTrue(p.isIntersection());
            assertEquals(2, p.getContainers().size());
            List<ContainerCondition> expected = List.of(
                    new ContainerCondition("oa1", true),
                    new ContainerCondition("oa2", false)
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

            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("subject", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa3", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa4", List.of("pc1"));
            pap.modify().operations().setResourceOperations(new AccessRightSet("read", "write"));

            pap.modify().prohibitions().createProhibition("label1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                    List.of(new ContainerCondition("oa1", true),
                            new ContainerCondition("oa2", false)));
            pap.modify().prohibitions().createProhibition("label2", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                    List.of(new ContainerCondition("oa3", true),
                            new ContainerCondition("oa4", false)));

            Prohibition p = pap.query().prohibitions().getProhibition("label1");
            assertEquals("label1", p.getName());
            assertEquals("subject", p.getSubject().getName());
            assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
            assertTrue(p.isIntersection());
            assertEquals(2, p.getContainers().size());
            List<ContainerCondition> expected = List.of(
                    new ContainerCondition("oa1", true),
                    new ContainerCondition("oa2", false)
            );
            assertTrue(expected.containsAll(p.getContainers()) && p.getContainers().containsAll(expected));
        }
    }

    @Test
    void testGetInheritedProhibitionsFor() throws PMException, IOException {
        SamplePolicy.loadSamplePolicyFromPML(pap);

        pap.modify().graph().createUserAttribute("ua2", List.of("ua1"));
        pap.modify().graph().createUserAttribute("ua3", List.of("ua2"));
        pap.modify().graph().assign("u1", Collections.singleton("ua3"));

        pap.modify().prohibitions().createProhibition("label1", ProhibitionSubject.userAttribute("ua2"), new AccessRightSet("read"), true,
                List.of(new ContainerCondition("US project", true)));
        pap.modify().prohibitions().createProhibition("label2", ProhibitionSubject.userAttribute("ua3"), new AccessRightSet("read"), true,
                List.of(new ContainerCondition("US project", true)));

        Collection<Prohibition> prohibitions = pap.query().prohibitions().getInheritedProhibitionsFor("u1");
        assertEquals(2, prohibitions.size());
    }

    @Test
    void testGetProhibitionsForContainer() throws PMException, IOException {
        SamplePolicy.loadSamplePolicyFromPML(pap);

        pap.modify().prohibitions().createProhibition("label1", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet("read"), true,
                List.of(new ContainerCondition("US project", true)));
        pap.modify().prohibitions().createProhibition("label2", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet("read"), true,
                List.of(new ContainerCondition("US project", true)));


        Collection<Prohibition> prohibitions = pap.query().prohibitions().getProhibitionsWithContainer("US project");
        assertEquals(2, prohibitions.size());
    }

}