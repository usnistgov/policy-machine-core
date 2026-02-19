package gov.nist.csd.pm.core.pdp;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.epp.EventContext;
import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.modification.GraphModification;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.WildcardAccessRight;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.serialization.json.JSONSerializer;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class PDPTxTest {

    @Test
    void testReset() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                associate "ua1" and PM_ADMIN_BASE_OA with ["admin:*"]
                """);
        PDPTx u2 = new PDPTx(new UserContext(id("u2")), pap, List.of());
        assertThrows(UnauthorizedException.class, u2::reset);

        PDPTx u1 = new PDPTx(new TestUserContext("u1"), pap, List.of());
        assertDoesNotThrow(u1::reset);
    }

    @Test
    void testSerialize() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                associate "ua1" and PM_ADMIN_BASE_OA with ["admin:*"]
                """);
        PDPTx u2 = new PDPTx(new UserContext(id("u2")), pap, List.of());
        assertThrows(UnauthorizedException.class, () -> u2.serialize(new JSONSerializer()));

        PDPTx u1 = new PDPTx(new TestUserContext("u1"), pap, List.of());
        assertDoesNotThrow(() -> u1.serialize(new JSONSerializer()));
    }

    @Test
    void testDeserialize() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                associate "ua1" and PM_ADMIN_BASE_OA with ["admin:*"]
                """);

        String serialize = "create pc \"test\"";

        PDPTx u2 = new PDPTx(new UserContext(id("u2")), pap, List.of());
        assertThrows(UnauthorizedException.class, () -> u2.executePML(serialize));

        PDPTx u1 = new PDPTx(new TestUserContext("u1"), pap, List.of());
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

        PDPTx pdpTx = new PDPTx(new UserContext(u1), pap, List.of());
        long oa1 = pdpTx.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        assertDoesNotThrow(() -> pdpTx.modify().graph().createObjectAttribute("oa2", List.of(oa1)));
    }

    @Test
    void testObligationAuthorNoPrivilegesOnEventContextUser() throws PMException {
        String pml = """
            resourceop read_file(@node string n) { }
            
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create oa "oa1" in ["pc1"]
            create u "u1" in ["ua1"]
            create u "u2" in ["ua1"]
            """;

        TestPAP testPAP = new TestPAP();
        testPAP.executePML(null, pml);

        testPAP.executePML(new TestUserContext("u1"), """
            create obligation "o1"
            when any user 
            performs any operation
            do(ctx) {
            
            }
            """);

        PDP pdp = new PDP(testPAP);
        EPP epp = new EPP(pdp, testPAP);
        assertThrows(
            UnauthorizedException.class,
            () -> epp.processEvent(new EventContext(new EventContextUser("u2"), "read_file", Map.of("n", "oa1")))
        );
    }

    @Test
    void testObligationAuthorNoPrivilegesOnEventContextArgs() throws PMException {
        String pml = """
            resourceop read_file(@node string n) { }
            
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create ua "ua2" in ["pc1"]
            create oa "oa1" in ["pc1"]
            create u "u1" in ["ua1"]
            create u "u2" in ["ua1", "ua2"]
            associate "ua1" and "ua2" with ["*"]
            """;

        TestPAP testPAP = new TestPAP();
        testPAP.executePML(null, pml);

        testPAP.executePML(new TestUserContext("u1"), """
            create obligation "o1"
            when any user 
            performs any operation
            do(ctx) {
            
            }
            """);

        PDP pdp = new PDP(testPAP);
        EPP epp = new EPP(pdp, testPAP);
        assertThrows(
            UnauthorizedException.class,
            () -> epp.processEvent(new EventContext(new EventContextUser("u2"), "read_file", Map.of("n", "oa1")))
        );
    }
}