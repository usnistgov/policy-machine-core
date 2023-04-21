package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.pml.model.expression.Literal;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.PALStatement;
import gov.nist.csd.pm.policy.pml.statement.VarStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.serializer.PALDeserializer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static org.junit.jupiter.api.Assertions.*;

class PALExecutorTest {

    @Test
    void testCustomBuiltinFunctions() throws PMException {
        FunctionDefinitionStatement test1 = new FunctionDefinitionStatement.Builder("test1")
                .returns(Type.voidType())
                .args(
                        new FormalArgument("a1", Type.string())
                )
                .executor((ctx, policy) -> {
                    return new Value("hello world");
                })
                .build();
        FunctionDefinitionStatement test2 = new FunctionDefinitionStatement.Builder("test2")
                .returns(Type.voidType())
                .args(
                        new FormalArgument("a1", Type.string())
                )
                .body(
                        new VarStatement("test123", new Expression(new Literal("hello world")), true)
                )
                .build();

        PAP pap = new PAP(new MemoryPolicyStore());

        String pal = """
                
                test1('')
                test2('')
                
                """;

        List<PALStatement> statements = PALCompiler.compilePAL(pap, pal, test1, test2);
        assertEquals(2, statements.size());

        Map<String, FunctionDefinitionStatement> functions = pap.getPALFunctions();
        assertTrue(functions.isEmpty());

        pap.fromString(pal, new PALDeserializer(new UserContext(SUPER_USER), test1, test2));
        assertEquals(2, statements.size());

        functions = pap.getPALFunctions();
        assertTrue(functions.isEmpty());
    }

}