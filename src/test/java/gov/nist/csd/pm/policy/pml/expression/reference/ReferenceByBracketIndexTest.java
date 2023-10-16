package gov.nist.csd.pm.policy.pml.expression.reference;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReferenceByBracketIndexTest {

    @Test
    void testGetType() throws PMException {
        ReferenceByBracketIndex a = new ReferenceByBracketIndex(new ReferenceByID("a"), new StringLiteral("b"));
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        Type expected =  Type.array(Type.string());
        visitorContext.scope().addVariable("a", new Variable("a", Type.map(Type.string(), expected), false));

        assertEquals(
                expected,
                a.getType(visitorContext.scope())
        );
    }

    @Test
    void testExecute() throws PMException {
        ReferenceByBracketIndex a = new ReferenceByBracketIndex(new ReferenceByID("a"),  new StringLiteral("b"));
        ExecutionContext executionContext = new ExecutionContext(new UserContext(""), GlobalScope.withValuesAndDefinitions(new MemoryPolicyStore()));
        ArrayValue expected = new ArrayValue(List.of(new StringValue("1"), new StringValue("2")), Type.string());
        MapValue mapValue = new MapValue(Map.of(new StringValue("b"), expected), Type.string(), Type.array(Type.string()));
        executionContext.scope().addVariable("a", mapValue);

        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        Value actual = a.execute(executionContext, memoryPolicyStore);
        assertEquals(expected, actual);
    }

    @Test
    void testIndexChain() throws PMException {
        String pml = "a := {\n" +
                "                    \"b\": {\n" +
                "                        \"c\": {\n" +
                "                            \"d\": \"e\"\n" +
                "                        }  \n" +
                "                    }\n" +
                "                }\n" +
                "                \n" +
                "                create policy class a[\"b\"][\"c\"][\"d\"]";
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext("u1"), pml);

        assertTrue(memoryPolicyStore.graph().nodeExists("e"));
    }

    @Test
    void testWrongKeyType() throws PMException {
        String pml =
                "                a := {\n" +
                "                    \"b\": {\n" +
                "                        \"c\": {\n" +
                "                            \"d\": \"e\"\n" +
                "                        }  \n" +
                "                    }\n" +
                "                }\n" +
                "                \n" +
                "                create policy class a[true][\"c\"][\"d\"]";
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
        String pml =
                "                a := {\n" +
                "                    \"b\": {\n" +
                "                        \"c\": {\n" +
                "                            \"d\": \"e\"\n" +
                "                        }  \n" +
                "                    }\n" +
                "                }\n" +
                "                \n" +
                "                create policy class a[\"z\"][\"c\"][\"d\"]";
        assertThrows(NullPointerException.class,
                     () -> PMLExecutor.compileAndExecutePML(new MemoryPolicyStore(), new UserContext("u1"), pml));
    }

    @Test
    void testArrayKey() throws PMException {
        String pml =
                "                a := {\n" +
                "                    [\"a\"]: \"test\"\n" +
                "                }\n" +
                "                \n" +
                "                create policy class a[[\"a\"]]";
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext("u1"), pml);

        assertTrue(memoryPolicyStore.graph().nodeExists("test"));
    }

}