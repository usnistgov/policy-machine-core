package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExecutePALTest {

    @Test
    void testFunctionWithCustomFunction() throws PMException {
        FunctionDefinitionStatement functionDefinitionStatement = new FunctionDefinitionStatement(
                "testFunc",
                Type.string(),
                List.of(),
                (ctx, policy) -> new Value("create policy class 'pc1'")
        );
        String pal = """
                executePAL(testFunc())
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.addPALFunction(functionDefinitionStatement);
        pap.executePAL(new UserContext("super"), pal);
        assertTrue(pap.getPolicyClasses().contains("pc1"));
    }

}