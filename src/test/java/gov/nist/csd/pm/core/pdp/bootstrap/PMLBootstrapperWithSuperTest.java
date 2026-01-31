package gov.nist.csd.pm.core.pdp.bootstrap;

import static org.junit.jupiter.api.Assertions.*;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.graph.relationship.Association;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.util.TestIdGenerator;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class PMLBootstrapperWithSuperTest {

    @Test
    void bootstrap_createsAssociationsAndObligations_WHenDeleteSuperIsFalse() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        pap.bootstrap(new PMLBootstrapperWithSuper(""));

        assertEquals(3, pap.query().obligations().getObligations().size());
        assertEquals(2, pap.query().graph().getAssociationsWithSource(pap.query().graph().getNodeByName("@super").getId()).size());
    }

    @Test
    void bootstrap_associationCreated_whenUAOrOAOrAssignmentIsCreated() throws PMException {
        PAP pap = new TestPAP();
        pap.bootstrap(new PMLBootstrapperWithSuper("""
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create oa "oa1" in ["pc1"]
            create oa "oa3" in ["pc1"]
            """));
        PDP pdp = new PDP(pap);

        long pcId = pap.query().graph().getNodeId("pc1");
        long oa1Id = pap.query().graph().getNodeId("oa1");
        long oa3Id = pap.query().graph().getNodeId("oa3");
        long superId = pap.query().graph().getNodeId("super");

        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        pdp.runTx(new UserContext(superId), pdpTx -> {
            long oa2 = pdpTx.modify().graph().createObjectAttribute("oa2", List.of(pcId));
            long ua2 = pdpTx.modify().graph().createUserAttribute("ua2", List.of(pcId));
            pdpTx.modify().graph().assign(oa3Id, List.of(oa1Id));

            return null;
        });

        long superUaId = pap.query().graph().getNodeId("@super");
        Collection<Association> actual = pap.query().graph().getAssociationsWithSource(superUaId);
        assertEquals(4, actual.size());
        assertTrue(actual.contains(new Association(TestIdGenerator.id("@super"), AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("*"))));
        assertTrue(actual.contains(new Association(TestIdGenerator.id("@super"), TestIdGenerator.id("@pm_admin_users"), new AccessRightSet("*"))));
        assertTrue(actual.contains(new Association(TestIdGenerator.id("@super"), TestIdGenerator.id("oa2"), new AccessRightSet("*"))));
        assertTrue(actual.contains(new Association(TestIdGenerator.id("@super"), TestIdGenerator.id("ua2"), new AccessRightSet("*"))));
    }

    @Test
    void bootstrap_ok_whenSuperNodeUsedInProvidedPML() throws PMException {
        PAP pap = new TestPAP();
        assertDoesNotThrow(() -> pap.bootstrap(new PMLBootstrapperWithSuper("""
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            assign "super" to ["ua1"]
            """)));
        assertTrue(pap.query().graph().getAdjacentDescendants(TestIdGenerator.id("super")).contains(TestIdGenerator.id("ua1")));
    }

}