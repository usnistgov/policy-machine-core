package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class FunctionDefinitionStatementTest {

    @Test
    void testSuccess() throws PMException, UnknownFunctionInScopeException {
        FunctionDefinitionStatement stmt = new FunctionDefinitionStatement.Builder("func1")
                .returns(Type.voidType())
                .args(
                        new FormalArgument("a", Type.string()),
                        new FormalArgument("b", Type.bool()),
                        new FormalArgument("c", Type.array(Type.string()))
                )
                .body()
                .build();

        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc1");
        store.graph().createUserAttribute("ua1", "pc1");
        store.graph().createUserAttribute("ua2", "pc1");
        store.graph().createUserAttribute("ua3", "pc1");
        store.graph().createUser("u1", "ua1");

        ExecutionContext execCtx = new ExecutionContext(new UserContext("u1"));
        stmt.execute(execCtx, store);

        assertTrue(execCtx.scope().functionExists("func1"));
        assertEquals(
                stmt,
                execCtx.scope().getFunction("func1")
        );

        stmt = new FunctionDefinitionStatement.Builder("func1")
                .returns(Type.string())
                .args(
                        new FormalArgument("a", Type.string()),
                        new FormalArgument("b", Type.bool()),
                        new FormalArgument("c", Type.array(Type.string()))
                )
                .body(
                        new FunctionReturnStatement(new StringLiteral("test"))
                )
                .build();

        execCtx = new ExecutionContext(new UserContext("u1"));
        stmt.execute(execCtx, store);

        assertTrue(execCtx.scope().functionExists("func1"));
        assertEquals(
                stmt,
                execCtx.scope().getFunction("func1")
        );
    }

    @Test
    void testToFormattedString() {
        FunctionDefinitionStatement stmt = new FunctionDefinitionStatement.Builder("func1")
                .returns(Type.string())
                .args(
                        new FormalArgument("a", Type.string()),
                        new FormalArgument("b", Type.bool()),
                        new FormalArgument("c", Type.array(Type.string()))
                )
                .body(
                        new FunctionReturnStatement(new StringLiteral("test"))
                )
                .build();

        assertEquals("""
                             function func1(string a, bool b, []string c) string {
                                 return "test"
                             }""",
                     stmt.toFormattedString(0));

        assertEquals("""
                                 function func1(string a, bool b, []string c) string {
                                     return "test"
                                 }
                             """,
                     stmt.toFormattedString(1) + "\n");
    }

    @Test
    void testToFormattedStringVoidReturn() {
        FunctionDefinitionStatement stmt = new FunctionDefinitionStatement.Builder("func1")
                .returns(Type.voidType())
                .args(
                        new FormalArgument("a", Type.string()),
                        new FormalArgument("b", Type.bool()),
                        new FormalArgument("c", Type.array(Type.string()))
                )
                .body(
                        new FunctionReturnStatement()
                )
                .build();

        assertEquals("""
                             function func1(string a, bool b, []string c) {
                                 return
                             }""",
                     stmt.toFormattedString(0));

        assertEquals("""
                                 function func1(string a, bool b, []string c) {
                                     return
                                 }
                             """,
                     stmt.toFormattedString(1) + "\n");
    }
}