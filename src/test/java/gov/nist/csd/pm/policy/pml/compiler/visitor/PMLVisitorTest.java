package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionReturnStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.ArrayValue;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import gov.nist.csd.pm.policy.pml.value.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class PMLVisitorTest {

    @Test
    void testConstantAndFunctionSignatureCompilationHappensBeforeOtherStatements() throws PMException {
        String pml = """
                test2()
                
                const b = "b"

                function test2() {
                    create pc b
                    create pc c
                    
                    test1()
                }               
                
                const c = "c"
                
                function test1() {
                    create pc "a"
                }
                
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext("u1"), pml);

        assertTrue(memoryPolicyStore.graph().nodeExists("a"));
        assertTrue(memoryPolicyStore.graph().nodeExists("b"));
        assertTrue(memoryPolicyStore.graph().nodeExists("c"));
    }

    @Test
    void testDuplicateFunctionNames() throws PMException {
        String pml = """
                function test1() {
                
                }              
                                
                function test1() {
                }
                
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLCompilationException e = assertThrows(
                PMLCompilationException.class, () -> PMLExecutor.compileAndExecutePML(memoryPolicyStore,
                                                                                      new UserContext("u1"), pml
                ));
        assertEquals(1, e.getErrors().size());
        assertEquals("function 'test1' already defined in scope", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testFunctionReferencesUnknownConst() throws PMException {
        String pml = """
                function test1() {
                    create policy class a
                }
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLCompilationException e = assertThrows(
                PMLCompilationException.class, () -> PMLExecutor.compileAndExecutePML(memoryPolicyStore,
                                                                                      new UserContext("u1"), pml
                ));
        assertEquals(1, e.getErrors().size());
        assertEquals("unknown variable 'a' in scope", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testDuplicateConstantNames() throws PMException {
        String pml = """
                const a = "a"
                const a = "a"
                
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLCompilationException e = assertThrows(
                PMLCompilationException.class, () -> PMLExecutor.compileAndExecutePML(memoryPolicyStore,
                                                                                      new UserContext("u1"), pml
                ));
        assertEquals(1, e.getErrors().size());
        assertEquals("const 'a' already defined in scope", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testConstClashesWithFunctionArgThrowsException() throws PMException {
        String pml = """
                const a = "a"
                
                function f1(string a) {}
                
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLCompilationException e = assertThrows(
                PMLCompilationException.class,
                () -> PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext("u1"), pml)
        );
        assertEquals(1, e.getErrors().size());
        assertEquals("formal arg 'a' already defined as a constant in scope", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testDuplicateFunctionNameReturnsError() throws PMException {
        String pml = """
                function f1(string a, string b) string {
                    return ""
                }
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        memoryPolicyStore.userDefinedPML().createFunction(new FunctionDefinitionStatement.Builder("f1")
                                                                  .returns(Type.voidType())
                                                                  .build());
        PMLCompilationException e = assertThrows(
                PMLCompilationException.class,
                () -> PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext("u1"), pml)
        );

        assertEquals(1, e.getErrors().size());
        assertEquals("function 'f1' already defined in scope", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testConstantOverwritesExistingUserDefinedConstant() throws PMException {
        String pml = """
                const x = ["x"]
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        ArrayValue expected = new ArrayValue(List.of(new StringValue("x2")), Type.string());
        memoryPolicyStore.userDefinedPML().createConstant("x", expected);
        PMLCompilationException e = assertThrows(
                PMLCompilationException.class,
                () -> PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext("u1"), pml)
        );
        assertEquals(1, e.getErrors().size());
        assertEquals("const 'x' already defined in scope", e.getErrors().get(0).errorMessage());
    }
}