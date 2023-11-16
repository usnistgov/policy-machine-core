package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class CreateNonPCStatementTest {

    @Test
    void testSuccess() throws PMException {
        CreateNonPCStatement stmt1 = new CreateNonPCStatement(new StringLiteral("ua1"), NodeType.UA, buildArrayLiteral("pc1"));
        CreateNonPCStatement stmt2 = new CreateNonPCStatement(new StringLiteral("oa1"), NodeType.OA, buildArrayLiteral("pc1"));
        CreateNonPCStatement stmt3 = new CreateNonPCStatement(new StringLiteral("u1"), NodeType.U, buildArrayLiteral("ua1"));
        CreateNonPCStatement stmt4 = new CreateNonPCStatement(new StringLiteral("o1"), NodeType.O, buildArrayLiteral("oa1"));

        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc1");
        store.graph().createUserAttribute("ua2", "pc1");
        store.graph().createUser("u2", "ua2");
        ExecutionContext execCtx = new ExecutionContext(new UserContext("u2"), GlobalScope.withValuesAndDefinitions(new MemoryPolicyStore()));

        stmt1.execute(execCtx, store);
        stmt2.execute(execCtx, store);
        stmt3.execute(execCtx, store);
        stmt4.execute(execCtx, store);

        assertTrue(store.graph().nodeExists("ua1"));
        assertTrue(store.graph().nodeExists("oa1"));
        assertTrue(store.graph().nodeExists("u1"));
        assertTrue(store.graph().nodeExists("o1"));
        
        assertTrue(store.graph().getParents("ua1").contains("pc1"));
        assertTrue(store.graph().getParents("oa1").contains("pc1"));
        assertTrue(store.graph().getParents("u1").contains("ua1"));
        assertTrue(store.graph().getParents("o1").contains("oa1"));
    }

    @Test
    void testWithProperties() throws PMException {
        CreateNonPCStatement stmt1 = new CreateNonPCStatement(new StringLiteral("ua1"), NodeType.UA, buildArrayLiteral("pc1"),
                                                              buildMapLiteral("a", "b", "c", "d"));

        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc1");
        store.graph().createUserAttribute("ua2", "pc1");
        store.graph().createUser("u1", "ua2");
        ExecutionContext execCtx = new ExecutionContext(new UserContext("u1"), GlobalScope.withValuesAndDefinitions(new MemoryPolicyStore()));

        stmt1.execute(execCtx, store);

       assertEquals(Map.of("a", "b", "c", "d"), store.graph().getNode("ua1").getProperties());
    }

    @Test
    void testToFormattedString() {
        CreateNonPCStatement stmt = new CreateNonPCStatement(
                new StringLiteral("ua1"),
                NodeType.UA,
                buildArrayLiteral("ua2"),
                buildMapLiteral("a", "b")
        );
        assertEquals(
                "create UA \"ua1\" with properties {\"a\": \"b\"} assign to [\"ua2\"]",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    create UA \"ua1\" with properties {\"a\": \"b\"} assign to [\"ua2\"]",
                stmt.toFormattedString(1)
        );
    }

}