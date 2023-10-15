package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class ForeachStatementTest {

    @Test
    void testSuccess() throws PMException {
        // array
        ForeachStatement stmt = new ForeachStatement("x", null, buildArrayLiteral("a", "b", "c"),
                                                     List.of(new CreatePolicyStatement(new ReferenceByID("x")))
        );

        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc1");
        store.graph().createUserAttribute("ua1", "pc1");
        store.graph().createUser("u1", "ua1");
        UserContext userContext = new UserContext("u1");

        stmt.execute(new ExecutionContext(userContext), store);

        assertEquals(5, store.graph().getPolicyClasses().size());
        assertTrue(store.graph().getPolicyClasses().containsAll(List.of("a", "b", "c")));

        // map with key and value vars
        stmt = new ForeachStatement("x", "y", buildMapLiteral("a", "b", "c", "d"), List.of(
                new CreatePolicyStatement(new ReferenceByID("x")),
                new CreatePolicyStatement(new ReferenceByID("y"))
        ));

        store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc1");
        store.graph().createUserAttribute("ua1", "pc1");
        store.graph().createUser("u1", "ua1");

        stmt.execute(new ExecutionContext(userContext), store);

        assertEquals(6, store.graph().getPolicyClasses().size());
        assertTrue(store.graph().getPolicyClasses().containsAll(List.of("a", "b", "c", "d")));

        // map with key only
        stmt = new ForeachStatement("x", null, buildMapLiteral("a", "b", "c", "d"), List.of(
                new CreatePolicyStatement(new ReferenceByID("x"))
        ));

        store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc1");
        store.graph().createUserAttribute("ua1", "pc1");
        store.graph().createUser("u1", "ua1");

        stmt.execute(new ExecutionContext(userContext), store);

        assertEquals(4, store.graph().getPolicyClasses().size());
        assertTrue(store.graph().getPolicyClasses().containsAll(List.of("a", "c")));
    }

    @Test
    void testOverwriteValues() throws PMException, UnknownVariableInScopeException {
        ForeachStatement stmt = new ForeachStatement("x", null, buildArrayLiteral("a", "b", "c"), List.of(
                new VariableAssignmentStatement("test", false, new ReferenceByID("x"))
        ));

        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc1");
        store.graph().createUserAttribute("ua1", "pc1");
        store.graph().createUser("u1", "ua1");
        UserContext userContext = new UserContext("u1");

        ExecutionContext executionContext = new ExecutionContext(userContext);
        executionContext.scope().addValue("test", new StringValue("test"));
        stmt.execute(executionContext, store);

        assertEquals(
                "c",
                executionContext.scope().getValue("test").getStringValue()
        );
    }

    @Test
    void testArrayToFormattedString() {
        ForeachStatement stmt = new ForeachStatement("x", null, buildArrayLiteral("a", "b", "c"),
                                                     List.of(
                                                             new CreatePolicyStatement(new ReferenceByID("x"))
                                                     )
        );

        assertEquals("""
                             foreach x in ["a", "b", "c"] {
                                 create PC x
                             }""",
                     stmt.toFormattedString(0));

        assertEquals("""
                                 foreach x in ["a", "b", "c"] {
                                     create PC x
                                 }
                             """,
                     stmt.toFormattedString(1) + "\n");
    }

    @Test
    void testMapToFormattedString() {
        ForeachStatement stmt = new ForeachStatement("x", "y", buildMapLiteral("a", "b", "c", "d"),
                                                     List.of(
                                                             new CreatePolicyStatement(new ReferenceByID("x"))
                                                     )
        );

        assertEquals("""
                             foreach x, y in {"a": "b", "c": "d"} {
                                 create PC x
                             }""",
                     stmt.toFormattedString(0));

        assertEquals("""
                                 foreach x, y in {"a": "b", "c": "d"} {
                                     create PC x
                                 }
                             """,
                     stmt.toFormattedString(1) + "\n");
    }
}