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

        assertEquals("function func1(string a, bool b, []string c) string {\n" +
                             "    return \"test\"\n" +
                             "}",
                     stmt.toFormattedString(0));

        assertEquals("    function func1(string a, bool b, []string c) string {\n" +
                             "        return \"test\"\n" +
                             "    }",
                     stmt.toFormattedString(1));
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

        assertEquals("function func1(string a, bool b, []string c) {\n" +
                             "    return\n" +
                             "}",
                     stmt.toFormattedString(0));

        assertEquals("    function func1(string a, bool b, []string c) {\n" +
                             "        return\n" +
                             "    }",
                     stmt.toFormattedString(1));
    }

    @Test
    void testFormalArgOverwritesVariable()
            throws PMException {
        String pml = "var a = \"test\"\n" +
                "                var b = \"test2\"\n" +
                "                func1(a, b)\n" +
                "                \n" +
                "                function func1(string a, string b) {\n" +
                "                    create policy class a\n" +
                "                    create policy class b\n" +
                "                }";
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext(""), pml);

        assertTrue(memoryPolicyStore.graph().nodeExists("test"));
        assertTrue(memoryPolicyStore.graph().nodeExists("test2"));
    }

    @Test
    void testInvokeFromDefinition() throws PMException {
        String pml = "function f1(string a) {\n" +
                "                    create policy class a\n" +
                "                }\n" +
                "                \n" +
                "                function f2() {\n" +
                "                    a := \"test\"\n" +
                "                    f1(a)\n" +
                "                }\n" +
                "                \n" +
                "                f2()";
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext(""), pml);

        assertTrue(memoryPolicyStore.graph().nodeExists("test"));
    }

    @Test
    void testInvokeFromFunctionUsingConstant() throws PMException {
        String pml =
                "const x = \"x\"\n" +
                "\n" +
                "func1()\n" +
                "\n" +
                "function func1() {\n" +
                "    func2()\n" +
                "}\n" +
                "\n" +
                "function func2() {\n" +
                "    create policy class x\n" +
                "}";
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext(""), pml);
        assertTrue(memoryPolicyStore.graph().nodeExists("x"));
    }
}