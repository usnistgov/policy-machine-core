package gov.nist.csd.pm.pdp.memory;

import gov.nist.csd.pm.SamplePolicy;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.NodeNameExistsException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.UnauthorizedException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static org.junit.jupiter.api.Assertions.*;

class MemoryPDPTest {

    @Test
    void testRollback() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.graph().createPolicyClass("pc1");
        pap.graph().createObjectAttribute("oa1", "pc1");
        pap.graph().createUserAttribute("ua1", "pc1");

        PDP pdp = new MemoryPDP(pap, false);
        assertThrows(NodeNameExistsException.class, () -> {
            pdp.runTx(new UserContext(SUPER_USER), policy -> {
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

            FunctionDefinitionStatement functionDefinitionStatement = new FunctionDefinitionStatement(
                    "testfunc",
                    Type.voidType(),
                    List.of(),
                    (ctx, policy) -> {
                        policy.graph().createPolicyClass("pc3");
                        return new Value();
                    }
            );

            MemoryPDP memoryPDP = new MemoryPDP(pap, false);
            memoryPDP.runTx(new UserContext(SUPER_USER), policy -> {
                policy.userDefinedPML().addFunction(functionDefinitionStatement);
                policy.executePML(new UserContext(SUPER_USER), "create ua 'ua3' in ['pc2']");
            });

            assertTrue(pap.graph().nodeExists("ua3"));

            assertThrows(UnauthorizedException.class, () -> {
                memoryPDP.runTx(new UserContext("u1"), policy -> {
                    policy.executePML(new UserContext("u1"), "testfunc()");
                });
            });

            assertFalse(pap.graph().nodeExists("pc3"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
