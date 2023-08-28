package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.AdminPolicy.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminPolicyTest {

    @Test
    void testEnable() throws PMException {
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        memoryPolicyStore.graph().createPolicyClass("pc1");

        AdminPolicy.enable(memoryPolicyStore);

        assertTrue(memoryPolicyStore.graph().nodeExists("pc1"));
        assertTrue(memoryPolicyStore.graph().nodeExists(AdminPolicy.policyClassObjectAttributeName("pc1")));
        assertTrue(memoryPolicyStore.graph().nodeExists(ADMIN_POLICY));
        assertTrue(memoryPolicyStore.graph().nodeExists(POLICY_CLASSES_OA));
        assertTrue(memoryPolicyStore.graph().getParents(POLICY_CLASSES_OA).contains(ADMIN_POLICY));
        assertTrue(memoryPolicyStore.graph().nodeExists(PML_FUNCTIONS_TARGET));
        assertTrue(memoryPolicyStore.graph().getParents(PML_FUNCTIONS_TARGET).contains(ADMIN_POLICY));
        assertTrue(memoryPolicyStore.graph().nodeExists(PML_CONSTANTS_TARGET));
        assertTrue(memoryPolicyStore.graph().getParents(PML_CONSTANTS_TARGET).contains(ADMIN_POLICY));
        assertTrue(memoryPolicyStore.graph().nodeExists(ADMIN_POLICY_TARGET));
        assertTrue(memoryPolicyStore.graph().getParents(ADMIN_POLICY_TARGET).contains(ADMIN_POLICY));
    }
}
