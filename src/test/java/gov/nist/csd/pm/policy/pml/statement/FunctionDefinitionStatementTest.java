package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FunctionDefinitionStatementTest {

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

    @Test
    void testFormalArgOverwritesVariable()
            throws PMException {
        String pml = """
                var a = "test"
                var b = "test2"
                func1(a, b)
                
                function func1(string a, string b) {
                    create policy class a
                    create policy class b
                }
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext(""), pml);

        assertTrue(memoryPolicyStore.graph().nodeExists("test"));
        assertTrue(memoryPolicyStore.graph().nodeExists("test2"));
    }

    @Test
    void testInvokeFromDefinition() throws PMException {
        String pml = """
                function f1(string a) {
                    create policy class a
                }
                
                function f2() {
                    a := "test"
                    f1(a)
                }
                
                f2()
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext(""), pml);

        assertTrue(memoryPolicyStore.graph().nodeExists("test"));
    }

    @Test
    void testInvokeFromFunctionUsingConstant() throws PMException {
        String pml = """
                const x = "x"
                
                func1()
                
                function func1() {
                    func2()
                }
                
                function func2() {
                    create policy class x
                }
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext(""), pml);
        assertTrue(memoryPolicyStore.graph().nodeExists("x"));
    }
}