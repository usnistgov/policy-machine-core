package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.exception.PMLExecutionException;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.BoolValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FunctionInvocationStatementTest {

    @Test
    void testSuccess() throws PMException {
        FunctionInvocationStatement stmt = new FunctionInvocationStatement("equals", List.of(
                new StringLiteral("a"),
                new StringLiteral("a")
        ));

        Value value = stmt.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());

        assertEquals(
                new BoolValue(true),
                value
        );
    }

    @Test
    void testWrongArgNumber() {
        FunctionInvocationStatement stmt = new FunctionInvocationStatement("equals", List.of(
                new StringLiteral("a"),
                new StringLiteral("a"),
                new StringLiteral("a")
        ));

        PMLExecutionException e = assertThrows(
                PMLExecutionException.class,
                () -> stmt.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore())
        );
        assertEquals(
                "expected 2 args for function \"equals\", got 3",
                e.getMessage()
        );
    }

    @Test
    void testWrongArgType() {
        FunctionInvocationStatement stmt = new FunctionInvocationStatement("concat", List.of(
                new StringLiteral("a")
        ));

        PMLExecutionException e = assertThrows(PMLExecutionException.class,
                                               () -> stmt.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore()));
        assertEquals(
                "expected []string for arg 0 for function \"concat\", got string",
                e.getMessage()
        );
    }

    @Test
    void testToFormattedStringVoidReturn() {
        FunctionInvocationStatement stmt = new FunctionInvocationStatement("equals", List.of(
                new StringLiteral("a"),
                new StringLiteral("a")
        ));

        assertEquals("""
                             equals("a", "a")""",
                     stmt.toFormattedString(0));

        assertEquals("equals(\"a\", \"a\")",
                     stmt.toFormattedString(1));
    }
}