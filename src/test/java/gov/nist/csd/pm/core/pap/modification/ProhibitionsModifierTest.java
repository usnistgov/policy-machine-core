package gov.nist.csd.pm.core.pap.modification;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.ProhibitionExistsException;
import gov.nist.csd.pm.core.common.exception.UnknownAccessRightException;
import gov.nist.csd.pm.core.common.prohibition.NodeProhibition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.SamplePolicy;
import java.io.IOException;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class ProhibitionsModifierTest extends PAPTestInitializer {

    @Nested
    class CreateProhibitionTest {

        @Test
        void testProhibitionExistsException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("subject", ids("pc1"));

            pap.modify().prohibitions().createNodeProhibition("pro1", id("subject"), new AccessRightSet(), Set.of(), Set.of(), false);

            assertThrows(
                    ProhibitionExistsException.class,
                    () -> pap.modify().prohibitions().createNodeProhibition("pro1", id("subject"), new AccessRightSet(), Set.of(), Set.of(), false));
        }

        @Test
        void testProhibitionSubjectDoesNotExistException() {
            assertThrows(
                    NodeDoesNotExistException.class,
                    () -> pap.modify().prohibitions().createNodeProhibition("pro1", id("subject"), new AccessRightSet("admin:*"), Set.of(), Set.of(), false));
        }


        @Test
        void testUnknownAccessRightException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("subject", ids("pc1"));

            assertThrows(
                    UnknownAccessRightException.class,
                    () -> pap.modify().prohibitions().createNodeProhibition("pro1", id("subject"), new AccessRightSet("read"), Set.of(), Set.of(), false));
        }

        @Test
        void testProhibitionContainerDoesNotExistException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("subject", ids("pc1"));
            pap.modify().operations().setResourceAccessRights(new AccessRightSet("read"));
            assertThrows(
                    NodeDoesNotExistException.class,
                    () -> pap.modify().prohibitions().createNodeProhibition("pro1", id("subject"), new AccessRightSet("read"),
                            Set.of(), Set.of(id("oa1")), false));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("subject", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", ids("pc1"));
            pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));

            pap.modify().prohibitions().createNodeProhibition("pro1", id("subject"), new AccessRightSet("read"),
                    Set.of(id("oa1")),
                    Set.of(id("oa2")),
                    true);

            Prohibition p = pap.query().prohibitions().getProhibition("pro1");
            assertEquals("pro1", p.getName());
            assertTrue(p instanceof NodeProhibition);
            assertEquals(id("subject"), ((NodeProhibition) p).getNodeId());
            assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
            assertTrue(p.isConjunctive());
            assertEquals(Set.of(id("oa1")), p.getInclusionSet());
            assertEquals(Set.of(id("oa2")), p.getExclusionSet());
        }

        @Test
        void testTx() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.runTx(tx -> {
                tx.executePML(new UserContext(id("u1")), """
                    create conj node prohibition "p1"
                    deny "ua1"
                    arset ["read"]
                    include ["US project"]

                    create conj node prohibition "p2"
                    deny "ua1"
                    arset ["read"]
                    include ["US project"]
                    """);
            });

            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                tx.executePML(new UserContext(id("u1")), """
                    create conj node prohibition "p3"
                    deny "ua1"
                    arset ["read"]
                    include ["US project"]

                    create conj node prohibition "p4"
                    deny "ua1"
                    arset ["read"]
                    include ["US project"]
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
            pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));
            pap.modify().prohibitions().createNodeProhibition("pro1", id("subject"), new AccessRightSet("read"),
                    Set.of(id("oa1")),
                    Set.of(id("oa2")),
                    true);

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
                    create conj node prohibition "p1"
                    deny "ua1"
                    arset ["read"]
                    include ["US project"]

                    create conj node prohibition "p2"
                    deny "ua1"
                    arset ["read"]
                    include ["US project"]
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
