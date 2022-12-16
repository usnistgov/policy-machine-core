package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.policy.author.pal.model.expression.Literal;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.author.pal.statement.VarStatement;
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

        MemoryPAP memoryPAP = new MemoryPAP();

        String pal = """
                
                test1('');
                test2('');
                
                """;

        List<PALStatement> statements = PALCompiler.compilePAL(memoryPAP, pal, test1, test2);
        assertEquals(2, statements.size());

        Map<String, FunctionDefinitionStatement> functions = memoryPAP.pal().getFunctions();
        assertTrue(functions.isEmpty());

        memoryPAP.fromString(pal, new PALDeserializer(new UserContext(SUPER_USER), test1, test2));
        assertEquals(2, statements.size());

        functions = memoryPAP.pal().getFunctions();
        assertTrue(functions.isEmpty());

    }

}