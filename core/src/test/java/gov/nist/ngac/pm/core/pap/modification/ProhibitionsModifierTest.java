/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.modification;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.exception.ProhibitionDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.ProhibitionExistsException;
import gov.nist.ngac.pm.core.common.exception.UnknownAccessRightException;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.PAPTestInitializer;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.util.SamplePolicy;
import java.io.IOException;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

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
                tx.executePML(NodeUserContext.of(id("u1")), """
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
                tx.executePML(NodeUserContext.of(id("u1")), """
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
                tx.executePML(NodeUserContext.of(id("u1")), """
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
                tx.executePML(NodeUserContext.of(id("u1")), """
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
