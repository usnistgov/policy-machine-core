package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pdp.memory.MemoryPDP;
import gov.nist.csd.pm.pdp.memory.MemoryPolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.BootstrapExistingPolicyException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.PAPTest.testAdminPolicy;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.CREATE_OBJECT_ATTRIBUTE;
import static org.junit.jupiter.api.Assertions.*;

class PDPTest {

    @Test
    void testRunTx() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        PDP pdp = new MemoryPDP(pap);

        pap.runTx((policy) -> {
            policy.graph().createPolicyClass("pc1");
            policy.graph().createUserAttribute("ua1", "pc1");
            policy.graph().createObjectAttribute("oa1", "pc1");
            policy.graph().createObjectAttribute("oa2", "pc1");
            policy.graph().createUser("u1", "ua1");
            policy.graph().createObject("o1", "oa1");
        });

        assertThrows(PMException.class, () -> pdp.runTx(new UserContext("u1"), ((policy) ->
                policy.graph().associate("ua1", "oa1", new AccessRightSet(CREATE_OBJECT_ATTRIBUTE)))));

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("oa1"));
    }


    @Test
    void testBootstrapWithAdminPolicyOnly() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        PDP pdp = new MemoryPDP(pap);

        pdp.bootstrap(p -> {
            p.graph().createPolicyClass("pc1");
        });

        testAdminPolicy(pap, 2);
        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists(AdminPolicy.policyClassTargetName("pc1")));
    }

    @Test
    void testBootstrapWithExistingPolicyThrowsException() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        PDP pdp = new MemoryPDP(pap);
        pap.graph().createPolicyClass("pc1");
        assertThrows(BootstrapExistingPolicyException.class, () -> {
            pdp.bootstrap((policy) -> {});
        });

        pap.reset();

        pap.graph().setResourceAccessRights(new AccessRightSet("read"));
        pap.graph().createPolicyClass("pc1");
        pap.graph().createUserAttribute("ua1", "pc1");
        pap.graph().createUser("u1", "ua1");
        pap.graph().createObjectAttribute("oa1", "pc1");
        pap.graph().createObject("o1", "oa1");

        pap.prohibitions().create("pro1", new ProhibitionSubject("u1", ProhibitionSubject.Type.USER),
                                  new AccessRightSet("read"), true, new ContainerCondition("oa1", false));

        assertThrows(BootstrapExistingPolicyException.class, () -> {
            pdp.bootstrap((policy) -> {});
        });

        pap.obligations().create(new UserContext("u1"), "obl1");

        assertThrows(BootstrapExistingPolicyException.class, () -> {
            pdp.bootstrap((policy) -> {});
        });
    }
}