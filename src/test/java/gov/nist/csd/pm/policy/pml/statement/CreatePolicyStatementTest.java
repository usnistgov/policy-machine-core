package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.pml.PMLCompiler;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import gov.nist.csd.pm.util.PolicyEquals;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.OA;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.UA;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class CreatePolicyStatementTest {

    @Test
    void testSuccess() throws PMException {
        CreatePolicyStatement stmt = new CreatePolicyStatement(new StringLiteral("pc1"));
        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc2");
        store.graph().createUserAttribute("ua2", "pc2");
        store.graph().createUser("u2", "ua2");
        ExecutionContext execCtx = new ExecutionContext(new UserContext("u2"));

        stmt.execute(execCtx, store);

        assertTrue(store.graph().nodeExists("pc1"));
    }

    @Test
    void testToFormattedString() {
        CreatePolicyStatement stmt = new CreatePolicyStatement(
                new StringLiteral("pc1")
        );
        assertEquals(
                "create PC \"pc1\"",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    create PC \"pc1\"",
                stmt.toFormattedString(1)
        );
    }

    CreatePolicyStatement stmt = new CreatePolicyStatement(
            new StringLiteral("test"),
            buildMapLiteral("a", "b"),
            new ArrayList<>(List.of(
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            new ReferenceByID("ua1"), UA, new StringLiteral("test"), buildMapLiteral("k", "v")),
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            new StringLiteral("ua1-1"), UA, new ReferenceByID("ua1")),
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            new StringLiteral("ua1-2"), UA, new ReferenceByID("ua1")),
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            new StringLiteral("ua1-2-1"), UA, new StringLiteral("ua1-2"), buildMapLiteral("k", "v")),
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            new StringLiteral("ua1-3"), UA, new ReferenceByID("ua1")),
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            new StringLiteral("ua2"), UA, new StringLiteral("test"), buildMapLiteral("k", "v")),
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            new StringLiteral("ua1-2-1"), UA, new StringLiteral("ua2"))
            )),
            new ArrayList<>(List.of(
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            new StringLiteral("oa1"), OA, new StringLiteral("test"), buildMapLiteral("k", "v")),
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            new StringLiteral("oa1-1"), OA, new StringLiteral("oa1")),
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            new StringLiteral("oa1-2"), OA, new StringLiteral("oa1")),
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            new StringLiteral("oa1-2-1"), OA, new StringLiteral("oa1-2"), buildMapLiteral("k", "v")),
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            new StringLiteral("oa1-3"), OA, new StringLiteral("oa1")),
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            new StringLiteral("oa2"), OA, new StringLiteral("test"), buildMapLiteral("k", "v")),
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            new StringLiteral("oa1-2-1"), OA, new StringLiteral("oa2"))
            )),
            new ArrayList<>(List.of(
                    new AssociateStatement(
                            new StringLiteral("ua1"), new StringLiteral("oa1"), buildArrayLiteral("read", "write"))
            ))
    );

    @Test
    void testHierarchy() throws PMException {
        ExecutionContext execCtx = new ExecutionContext(new UserContext("u2"));
        execCtx.scope().addValue("ua1", new StringValue("ua1"));

        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        memoryPolicyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
        stmt.execute(execCtx, memoryPolicyStore);

        MemoryPolicyStore expected = new MemoryPolicyStore();
        expected.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
        expected.graph().createPolicyClass("test", Map.of("a", "b"));
        expected.graph().createUserAttribute("ua1", Map.of("k", "v"), "test");
        expected.graph().createUserAttribute("ua1-1", "ua1");
        expected.graph().createUserAttribute("ua1-2", "ua1");
        expected.graph().createUserAttribute("ua1-2-1", Map.of("k", "v"), "ua1-2");
        expected.graph().createUserAttribute("ua1-3", "ua1");
        expected.graph().createUserAttribute("ua2", Map.of("k", "v"), "test");
        expected.graph().assign("ua1-2-1", "ua2");

        expected.graph().createObjectAttribute("oa1", Map.of("k", "v"), "test");
        expected.graph().createObjectAttribute("oa1-1", "oa1");
        expected.graph().createObjectAttribute("oa1-2", "oa1");
        expected.graph().createObjectAttribute("oa1-2-1", Map.of("k", "v"), "oa1-2");
        expected.graph().createObjectAttribute("oa1-3", "oa1");
        expected.graph().createObjectAttribute("oa2", Map.of("k", "v"), "test");
        expected.graph().assign("oa1-2-1", "oa2");

        expected.graph().associate("ua1", "oa1", new AccessRightSet("read", "write"));

        PolicyEquals.assertPolicyEquals(expected, memoryPolicyStore);
    }

    @Test
    void testFormattedString() {
        String s = stmt.toFormattedString(0);
        assertEquals("""
                             create PC "test" with properties {"a": "b"} {
                                 user attributes {
                                     ua1 {"k": "v"}
                                         "ua1-1"
                                         "ua1-2"
                                             "ua1-2-1" {"k": "v"}
                                         "ua1-3"
                                     "ua2" {"k": "v"}
                                         "ua1-2-1"
                                 }
                                 object attributes {
                                     "oa1" {"k": "v"}
                                         "oa1-1"
                                         "oa1-2"
                                             "oa1-2-1" {"k": "v"}
                                         "oa1-3"
                                     "oa2" {"k": "v"}
                                         "oa1-2-1"
                                 }
                                 associations {
                                     "ua1" and "oa1" with ["read", "write"]
                                 }
                             }""", s);
    }

    @Test
    void testFormattedStringWithIndent() {
        String s = stmt.toFormattedString(2);
        assertEquals("""
                             create PC "test" with properties {"a": "b"} {
                                 user attributes {
                                     ua1 {"k": "v"}
                                         "ua1-1"
                                         "ua1-2"
                                             "ua1-2-1" {"k": "v"}
                                         "ua1-3"
                                     "ua2" {"k": "v"}
                                         "ua1-2-1"
                                 }
                                 object attributes {
                                     "oa1" {"k": "v"}
                                         "oa1-1"
                                         "oa1-2"
                                             "oa1-2-1" {"k": "v"}
                                         "oa1-3"
                                     "oa2" {"k": "v"}
                                         "oa1-2-1"
                                 }
                                 associations {
                                     "ua1" and "oa1" with ["read", "write"]
                                 }
                             }
                     """.stripTrailing(), s);
    }

    @Test
    void testFormattedStringWithNoHierarchy() {
        CreatePolicyStatement s = new CreatePolicyStatement(new StringLiteral("a"));
        assertEquals("create PC \"a\"", s.toFormattedString(0));

        s = new CreatePolicyStatement(new StringLiteral("a"), buildMapLiteral("a", "b"));
        assertEquals("create PC \"a\" with properties {\"a\": \"b\"}", s.toFormattedString(0));

        s = new CreatePolicyStatement(new StringLiteral("a"));
        assertEquals("    create PC \"a\"", s.toFormattedString(1));

        s = new CreatePolicyStatement(new StringLiteral("a"), buildMapLiteral("a", "b"));
        assertEquals("    create PC \"a\" with properties {\"a\": \"b\"}", s.toFormattedString(1));
    }
}