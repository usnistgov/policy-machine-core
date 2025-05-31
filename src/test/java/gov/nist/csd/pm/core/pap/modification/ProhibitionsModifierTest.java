package gov.nist.csd.pm.core.pap.modification;

import gov.nist.csd.pm.core.common.exception.*;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.pap.admin.AdminAccessRights;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.SamplePolicy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ProhibitionsModifierTest extends PAPTestInitializer {

    @Nested
    class CreateProhibitionTest {

        @Test
        void testProhibitionExistsException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("subject", ids("pc1"));

            pap.modify().prohibitions().createProhibition("pro1", new ProhibitionSubject(id("subject")), new AccessRightSet(), false, List.of());

            assertThrows(
                    ProhibitionExistsException.class,
                    () -> pap.modify().prohibitions().createProhibition("pro1", new ProhibitionSubject(id("subject")), new AccessRightSet(), false, List.of()));
        }

        @Test
        void testProhibitionSubjectDoesNotExistException() {
            assertThrows(
                    NodeDoesNotExistException.class,
                    () -> pap.modify().prohibitions().createProhibition("pro1", new ProhibitionSubject(id("subject")), new AccessRightSet(
                        AdminAccessRights.WC_ALL), false, List.of()));
        }


        @Test
        void testUnknownAccessRightException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("subject", ids("pc1"));

            assertThrows(
                    UnknownAccessRightException.class,
                    () -> pap.modify().prohibitions().createProhibition("pro1", new ProhibitionSubject(id("subject")), new AccessRightSet("read"), false, List.of()));
        }

        @Test
        void testProhibitionContainerDoesNotExistException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("subject", ids("pc1"));
            pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
            assertThrows(
                    NodeDoesNotExistException.class,
                    () -> pap.modify().prohibitions().createProhibition("pro1", new ProhibitionSubject(id("subject")), new AccessRightSet("read"),
                            false,
                            Collections.singleton(new ContainerCondition(id("oa1"), true))));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("subject", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", ids("pc1"));
            pap.modify().operations().setResourceOperations(new AccessRightSet("read", "write"));

            pap.modify().prohibitions().createProhibition("pro1", new ProhibitionSubject(id("subject")), new AccessRightSet("read"),
                    true,
                    List.of(
                            new ContainerCondition(id("oa1"), true),
                            new ContainerCondition(id("oa2"), false)
                    ));

            Prohibition p = pap.query().prohibitions().getProhibition("pro1");
            assertEquals("pro1", p.getName());
            assertEquals(id("subject"), p.getSubject().getNodeId());
            assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
            assertTrue(p.isIntersection());
            assertEquals(2, p.getContainers().size());
            List<ContainerCondition> expected = List.of(
                    new ContainerCondition(id("oa1"), true),
                    new ContainerCondition(id("oa2"), false)
            );
            assertTrue(expected.containsAll(p.getContainers()) && p.getContainers().containsAll(expected));
        }

        @Test
        void testTx() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.runTx(tx -> {
                tx.executePML(new UserContext(id("u1")), """
                    create prohibition "p1"
                    deny user attribute "ua1"
                    access rights ["read"]
                    on union of {"US project": false}
                    
                    create prohibition "p2"
                    deny user attribute "ua1"
                    access rights ["read"]
                    on union of {"US project": false}
                    """);
            });

            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                tx.executePML(new UserContext(id("u1")), """
                    create prohibition "p3"
                    deny user attribute "ua1"
                    access rights ["read"]
                    on union of {"US project": false}
                    
                    create prohibition "p4"
                    deny user attribute "ua1"
                    access rights ["read"]
                    on union of {"US project": false}
                    """);
                throw new PMException("");
            }));

            assertDoesNotThrow(() -> pap.query().prohibitions().getProhibition("p1"));
            assertDoesNotThrow(() -> pap.query().prohibitions().getProhibition("p2"));
            assertThrows(ProhibitionDoesNotExistException.class,
                    () -> pap.query().prohibitions().getProhibition("p3"));
            assertThrows(ProhibitionDoesNotExistException.class,
                    () -> pap.query().prohibitions().getProhibition("p4"));

        }
    }

    @Nested
    class DeleteProhibitionTest {

        @Test
        void testNonExistingProhibitionDoesNotThrowException() {
            assertDoesNotThrow(() -> pap.modify().prohibitions().deleteProhibition("pro1"));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("subject", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", ids("pc1"));
            pap.modify().operations().setResourceOperations(new AccessRightSet("read", "write"));

            pap.modify().prohibitions().createProhibition("pro1", new ProhibitionSubject(id("subject")), new AccessRightSet("read"),
                    true, List.of(
                            new ContainerCondition(id("oa1"), true),
                            new ContainerCondition(id("oa2"), false)));

            assertDoesNotThrow(() -> pap.query().prohibitions().getProhibition("pro1"));

            pap.modify().prohibitions().deleteProhibition("pro1");

            assertThrows(ProhibitionDoesNotExistException.class,
                    () -> pap.query().prohibitions().getProhibition("pro1"));
        }

        @Test
        void testTx() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.runTx(tx -> {
                tx.executePML(new UserContext(id("u1")), """
                    create prohibition "p1"
                    deny user attribute "ua1"
                    access rights ["read"]
                    on union of {"US project": false}
                    
                    create prohibition "p2"
                    deny user attribute "ua1"
                    access rights ["read"]
                    on union of {"US project": false}
                    """);
            });

            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                tx.executePML(new UserContext(id("u1")), """
                    delete prohibition "p1"
                    delete prohibition "p2"
                    """);
                throw new PMException("");
            }));

            assertDoesNotThrow(() -> pap.query().prohibitions().getProhibition("p1"));
            assertDoesNotThrow(() -> pap.query().prohibitions().getProhibition("p2"));
        }
    }
}