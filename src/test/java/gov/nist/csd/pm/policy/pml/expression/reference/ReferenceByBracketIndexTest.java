package gov.nist.csd.pm.policy.pml.expression.reference;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.expression.literal.BoolLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.exception.PMLCompilationException;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class ReferenceByBracketIndexTest {

    @Test
    void testGetType() throws PMLScopeException {
        ReferenceByBracketIndex a = new ReferenceByBracketIndex(new ReferenceByID("a"), new StringLiteral("b"));
        VisitorContext visitorContext = new VisitorContext();
        Type expected =  Type.array(Type.string());
        visitorContext.scope().addVariable("a", Type.map(Type.string(), expected), false);

        assertEquals(
                expected,
                a.getType(visitorContext.scope())
        );
    }

    @Test
    void testExecute() throws PMException {
        ReferenceByBracketIndex a = new ReferenceByBracketIndex(new ReferenceByID("a"),  new StringLiteral("b"));
        ExecutionContext executionContext = new ExecutionContext(new UserContext(""));
        ArrayValue expected = new ArrayValue(List.of(new StringValue("1"), new StringValue("2")), Type.string());
        MapValue mapValue = new MapValue(Map.of(new StringValue("b"), expected), Type.string(), Type.array(Type.string()));
        executionContext.scope().addValue("a", mapValue);

        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        Value actual = a.execute(executionContext, memoryPolicyStore);
        assertEquals(expected, actual);
    }

    @Test
    void testIndexChain() throws PMException {
        String pml = """
                a := {
                    "b": {
                        "c": {
                            "d": "e"
                        }  
                    }
                }
                
                create policy class a["b"]["c"]["d"]
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext("u1"), pml);

        assertTrue(memoryPolicyStore.graph().nodeExists("e"));
    }

    @Test
    void testWrongKeyType() throws PMException {
        String pml = """
                a := {
                    "b": {
                        "c": {
                            "d": "e"
                        }  
                    }
                }
                
                create policy class a[true]["c"]["d"]
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        memoryPolicyStore.graph().createPolicyClass("pc1");
        memoryPolicyStore.graph().createUserAttribute("ua1", "pc1");
        memoryPolicyStore.graph().createUserAttribute("u1", "ua1");
        PMLCompilationException e = assertThrows(PMLCompilationException.class,
                                                 () -> PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext("u1"), pml));
        assertEquals("expected expression type string, got bool", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testKeyDoesNotExist() {
        String pml = """
                a := {
                    "b": {
                        "c": {
                            "d": "e"
                        }  
                    }
                }
                
                create policy class a["z"]["c"]["d"]
                """;
        assertThrows(NullPointerException.class,
                     () -> PMLExecutor.compileAndExecutePML(new MemoryPolicyStore(), new UserContext("u1"), pml));
    }

    @Test
    void testArrayKey() throws PMException {
        String pml = """
                a := {
                    ["a"]: "test"
                }
                
                create policy class a[["a"]]
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext("u1"), pml);

        assertTrue(memoryPolicyStore.graph().nodeExists("test"));
    }

}