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

package gov.nist.ngac.pm.core.pdp;

import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.epp.EPP;
import gov.nist.ngac.pm.core.epp.EventContext;
import gov.nist.ngac.pm.core.pap.obligation.event.EventContextUser;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.ngac.pm.core.pap.modification.GraphModification;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.accessright.WildcardAccessRight;
import gov.nist.ngac.pm.core.pap.serialization.json.JSONSerializer;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class PDPTxTest {

    @Test
    void testReset() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:*"]
                """);
        PDPTx u2 = new PDPTx(NodeUserContext.of(id("u2")), pap, List.of());
        assertThrows(UnauthorizedException.class, u2::reset);

        PDPTx u1 = new PDPTx(NodeUserContext.of("u1"), pap, List.of());
        assertDoesNotThrow(u1::reset);
    }

    @Test
    void testSerialize() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:*"]
                """);
        PDPTx u2 = new PDPTx(NodeUserContext.of(id("u2")), pap, List.of());
        assertThrows(UnauthorizedException.class, () -> u2.serialize(new JSONSerializer()));

        PDPTx u1 = new PDPTx(NodeUserContext.of("u1"), pap, List.of());
        assertDoesNotThrow(() -> u1.serialize(new JSONSerializer()));
    }

    @Test
    void testDeserialize() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:*"]
                """);

        String serialize = "create pc \"test\"";

        PDPTx u2 = new PDPTx(NodeUserContext.of(id("u2")), pap, List.of());
        assertThrows(UnauthorizedException.class, () -> u2.executePML(serialize));

        PDPTx u1 = new PDPTx(NodeUserContext.of("u1"), pap, List.of());
        assertDoesNotThrow(() -> u1.executePML(serialize));
    }

    @Test
    void testPrivilegesOnNodesCreatedInPC() throws PMException {
        PAP pap = new TestPAP();
        GraphModification graph = pap.modify().graph();
        long pc1 = graph.createPolicyClass("pc1");
        long ua1 = graph.createUserAttribute("ua1", List.of(pc1));
        long u1 = graph.createUser("u1", List.of(ua1));
        graph.associate(ua1, AdminPolicyNode.PM_ADMIN_POLICY_CLASSES.nodeId(), new AccessRightSet(WildcardAccessRight.ADMIN_GRAPH_WILDCARD.toString()));

        PDPTx pdpTx = new PDPTx(NodeUserContext.of(u1), pap, List.of());
        long oa1 = pdpTx.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        assertDoesNotThrow(() -> pdpTx.modify().graph().createObjectAttribute("oa2", List.of(oa1)));
    }

    @Test
    void testObligationAuthorNoPrivilegesOnEventContextUser() throws PMException {
        String pml = """
            resourceop read_file(@Node string n) { }
            
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create oa "oa1" in ["pc1"]
            create u "u1" in ["ua1"]
            create u "u2" in ["ua1"]
            """;

        TestPAP testPAP = new TestPAP();
        testPAP.executePML(null, pml);

        testPAP.executePML(NodeUserContext.of("u1"), """
            create obligation "o1"
            when any user 
            performs any operation
            do(ctx) {
            
            }
            """);

        PDP pdp = new PDP(testPAP);
        EPP epp = new EPP(pdp, testPAP);
        assertDoesNotThrow(
            () -> epp.processEvent(new EventContext(new EventContextUser("u2"), "read_file", Map.of("n", "oa1")))
        );
    }

    @Test
    void testObligationAuthorNoPrivilegesOnEventContextArgs() throws PMException {
        String pml = """
            resourceop read_file(@Node string n) { }
            
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create ua "ua2" in ["pc1"]
            create oa "oa1" in ["pc1"]
            create u "u1" in ["ua1"]
            create u "u2" in ["ua1", "ua2"]
            associate "ua1" to "ua2" with ["*"]
            """;

        TestPAP testPAP = new TestPAP();
        testPAP.executePML(null, pml);

        testPAP.executePML(NodeUserContext.of("u1"), """
            create obligation "o1"
            when any user 
            performs any operation
            do(ctx) {
            
            }
            """);

        PDP pdp = new PDP(testPAP);
        EPP epp = new EPP(pdp, testPAP);
        assertDoesNotThrow(
            () -> epp.processEvent(new EventContext(new EventContextUser("u2"), "read_file", Map.of("n", "oa1")))
        );
    }
}