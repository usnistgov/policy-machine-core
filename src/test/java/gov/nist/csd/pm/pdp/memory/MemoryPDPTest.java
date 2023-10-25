package gov.nist.csd.pm.pdp.memory;

import gov.nist.csd.pm.pap.AdminPolicyNode;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.VoidValue;
import gov.nist.csd.pm.util.SamplePolicy;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.policy.pml.model.exception.PMLExecutionException;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.NodeNameExistsException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.UnauthorizedException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MemoryPDPTest {

    @Test
    void testRollback() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.graph().createPolicyClass("pc1");
        pap.graph().createObjectAttribute("oa1", "pc1");
        pap.graph().createUserAttribute("ua1", "pc1");
        pap.graph().createUser("u1", "ua1");
        pap.graph().associate("ua1", AdminPolicyNode.POLICY_CLASSES_OA.nodeName(), new AccessRightSet("*"));

        PDP pdp = new MemoryPDP(pap);
        assertThrows(NodeNameExistsException.class, () -> {
            pdp.runTx(new UserContext("u1"), policy -> {
                policy.graph().createPolicyClass("pc2");
                // expect error and rollback
                policy.graph().createObjectAttribute("oa1", "pc2");
            });
        });

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("ua1"));
        assertTrue(pap.graph().nodeExists("oa1"));
        assertFalse(pap.graph().nodeExists("pc2"));
    }

    @Test
    void testExecutePML() throws PMException {
        try {
            PAP pap = new PAP(new MemoryPolicyStore());
            SamplePolicy.loadSamplePolicyFromPML(pap);

            FunctionDefinitionStatement functionDefinitionStatement = new FunctionDefinitionStatement.Builder("testfunc")
                    .returns(Type.voidType())
                    .args()
                    .executor((ctx, policy) -> {
                        policy.graph().createPolicyClass("pc3");
                        return new VoidValue();
                    })
                    .build();

            MemoryPDP memoryPDP = new MemoryPDP(pap);
            memoryPDP.runTx(new UserContext("u1"), policy -> {
                policy.userDefinedPML().createFunction(functionDefinitionStatement);
                policy.executePML(new UserContext("u1"), "create ua \"ua3\" assign to [\"pc2\"]");
            });

            assertTrue(pap.graph().nodeExists("ua3"));

            PMLExecutionException e = assertThrows(PMLExecutionException.class, () -> {
                memoryPDP.runTx(new UserContext("u1"), policy -> {
                    policy.executePML(new UserContext("u1"), "testfunc()");
                });
            });
            assertEquals(UnauthorizedException.class, e.getCause().getClass());

            assertFalse(pap.graph().nodeExists("pc3"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
