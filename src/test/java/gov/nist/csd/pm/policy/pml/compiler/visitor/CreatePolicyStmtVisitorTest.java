package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLCompiler;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.ErrorExpression;
import gov.nist.csd.pm.policy.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.exception.PMLCompilationException;
import gov.nist.csd.pm.policy.pml.statement.AssociateStatement;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement.CreateOrAssignAttributeStatement;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.OA;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.UA;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class CreatePolicyStmtVisitorTest {

    @Test
    void testSuccess() {
        PMLParser.CreatePolicyStatementContext ctx = PMLContextVisitor.toCtx(
                """
                create policy class "test"
                """,
                PMLParser.CreatePolicyStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        PMLStatement stmt = new CreatePolicyStmtVisitor(visitorCtx).visitCreatePolicyStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new CreatePolicyStatement(new StringLiteral("test")),
                stmt
        );
    }


    @Test
    void testSuccessWithProperties() {
        PMLParser.CreatePolicyStatementContext ctx = PMLContextVisitor.toCtx(
                """
                create policy class "test" with properties {"a": "b"}
                """,
                PMLParser.CreatePolicyStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        PMLStatement stmt = new CreatePolicyStmtVisitor(visitorCtx).visitCreatePolicyStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new CreatePolicyStatement(new StringLiteral("test"), buildMapLiteral("a", "b")),
                stmt
        );
    }

    @Test
    void testInvalidNameExpression() {
        PMLParser.CreatePolicyStatementContext ctx = PMLContextVisitor.toCtx(
                """
                create policy class ["test"]
                """,
                PMLParser.CreatePolicyStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        new CreatePolicyStmtVisitor(visitorCtx).visitCreatePolicyStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type string, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testHierarchyUserAttributesOnly() throws PMException {
        String pml = """
                create policy class "test" {
                    user attributes {
                        "a"
                            "a1"
                            "a2"
                                "a21"
                            "a3"
                        "b"
                            "b1"
                    }
                }
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        List<PMLStatement> statements = PMLCompiler.compilePML(memoryPolicyStore, pml);
        assertEquals(1, statements.size());
        CreatePolicyStatement stmt = (CreatePolicyStatement) statements.get(0);
        assertEquals(
                new CreatePolicyStatement(
                        new StringLiteral("test"),
                        null,
                        List.of(
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a"), UA, new StringLiteral("test")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a1"), UA, new StringLiteral("a")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a2"), UA, new StringLiteral("a")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a21"), UA, new StringLiteral("a2")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a3"), UA, new StringLiteral("a")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("b"), UA, new StringLiteral("test")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("b1"), UA, new StringLiteral("b"))
                        ),
                        List.of(),
                        List.of()
                ),
                stmt
        );
    }

    @Test
    void testHierarchyObjectAttributesOnly() throws PMException {
        String pml = """
                create policy class "test" {
                    object attributes {
                        "a"
                            "a1"
                            "a2"
                                "a21"
                            "a3"
                        "b"
                            "b1"
                    }
                }
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        List<PMLStatement> statements = PMLCompiler.compilePML(memoryPolicyStore, pml);
        assertEquals(1, statements.size());
        CreatePolicyStatement stmt = (CreatePolicyStatement) statements.get(0);
        assertEquals(
                new CreatePolicyStatement(
                        new StringLiteral("test"),
                        null,
                        List.of(),
                        List.of(
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a"), OA, new StringLiteral("test")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a1"), OA, new StringLiteral("a")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a2"), OA, new StringLiteral("a")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a21"), OA, new StringLiteral("a2")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a3"), OA, new StringLiteral("a")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("b"), OA, new StringLiteral("test")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("b1"), OA, new StringLiteral("b"))
                        ),
                        List.of()
                ),
                stmt
        );
    }

    @Test
    void testHierarchyUsingReferenceByID() throws PMException {
        String pml = """
                const a = "a"
                const b = "b"
                create policy class "test" {
                    object attributes {
                        a
                            "a1"
                            "a2"
                                "a21"
                            "a3"
                        b
                            "a21"
                    }
                }
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        List<PMLStatement> statements = PMLCompiler.compilePML(memoryPolicyStore, pml);
        assertEquals(3, statements.size());
        CreatePolicyStatement stmt = (CreatePolicyStatement) statements.get(2);
        assertEquals(
                new CreatePolicyStatement(
                        new StringLiteral("test"),
                        null,
                        List.of(),
                        List.of(
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new ReferenceByID("a"), OA, new StringLiteral("test")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a1"), OA, new ReferenceByID("a")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a2"), OA, new ReferenceByID("a")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a21"), OA, new StringLiteral("a2")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a3"), OA, new ReferenceByID("a")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new ReferenceByID("b"), OA, new StringLiteral("test")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("a21"), OA, new ReferenceByID("b"))
                        ),
                        List.of()
                ),
                stmt
        );
    }

    @Test
    void testAssociationsOnly() throws PMException {
        String pml = """
                const a = "a"
                create policy class "test" {
                    associations {
                        a and "b" with ["read", "write"]
                        a and "c" with ["read"]
                        a and "d" with ["read"]
                    }
                }
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        List<PMLStatement> statements = PMLCompiler.compilePML(memoryPolicyStore, pml);
        assertEquals(2, statements.size());
        CreatePolicyStatement stmt = (CreatePolicyStatement) statements.get(1);
        assertEquals(
                new CreatePolicyStatement(
                        new StringLiteral("test"),
                        null,
                        List.of(),
                        List.of(),
                        List.of(
                                new AssociateStatement(new ReferenceByID("a"), new StringLiteral("b"), buildArrayLiteral("read", "write")),
                                new AssociateStatement(new ReferenceByID("a"), new StringLiteral("c"), buildArrayLiteral("read")),
                                new AssociateStatement(new ReferenceByID("a"), new StringLiteral("d"), buildArrayLiteral("read"))
                        )
                ),
                stmt
        );
    }

    @Test
    void testUserAndObjectAttributeWithAssociationsAndPropertiesHierarchy() throws PMException {
        String pml = """
                create policy class "test" with properties {"a": "b"} {
                    user attributes {
                        "ua1"
                            "ua1-1"
                            "ua1-2"
                                "ua1-2-1"
                            "ua1-3"
                        "ua2"
                            "ua2-1"
                    }
                    object attributes {
                        "oa1"
                            "oa1-1"
                            "oa1-2"
                                "oa1-2-1"
                            "oa1-3"
                        "oa2"
                            "oa2-1"
                    }
                    associations {
                        "ua1" and "oa1" with ["read", "write"]
                        "ua1" and "oa1" with [create_policy_class]
                    }
                }
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        List<PMLStatement> statements = PMLCompiler.compilePML(memoryPolicyStore, pml);
        assertEquals(1, statements.size());
        CreatePolicyStatement stmt = (CreatePolicyStatement) statements.get(0);
        assertEquals(
                new CreatePolicyStatement(
                        new StringLiteral("test"),
                        buildMapLiteral("a", "b"),
                        new ArrayList<>(List.of(
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua1"), UA, new StringLiteral("test")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua1-1"), UA, new StringLiteral("ua1")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua1-2"), UA, new StringLiteral("ua1")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua1-2-1"), UA, new StringLiteral("ua1-2")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua1-3"), UA, new StringLiteral("ua1")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua2"), UA, new StringLiteral("test")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua2-1"), UA, new StringLiteral("ua2"))
                        )),
                        new ArrayList<>(List.of(
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa1"), OA, new StringLiteral("test")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa1-1"), OA, new StringLiteral("oa1")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa1-2"), OA, new StringLiteral("oa1")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa1-2-1"), OA, new StringLiteral("oa1-2")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa1-3"), OA, new StringLiteral("oa1")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa2"), OA, new StringLiteral("test")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa2-1"), OA, new StringLiteral("oa2"))
                        )),
                        new ArrayList<>(List.of(
                                new AssociateStatement(new StringLiteral("ua1"), new StringLiteral("oa1"), buildArrayLiteral("read", "write")),
                                new AssociateStatement(new StringLiteral("ua1"), new StringLiteral("oa1"),
                                                       new ArrayLiteral(Type.string(), new ReferenceByID("create_policy_class")))
                        ))
                ),
                stmt
        );
    }

    @Test
    void testAttributeInMultipleAttributesAndPolicyClasses() throws PMException {
        String pml = """
                create policy class "test" with properties {"a": "b"} {
                    user attributes {
                        "ua1"
                            "ua3"
                        "ua2"
                            "ua3"
                    }
                    object attributes {
                        "oa1"
                    }
                    associations {
                        "ua1" and "oa1" with ["read", "write"]
                    }
                }
                
                create policy class "test2" with properties {"a": "b"} {
                    user attributes {
                        "ua1"
                            "ua3"
                        "ua2"
                            "ua3"
                    }
                    object attributes {
                        "oa1"
                    }
                    associations {
                        "ua1" and "oa1" with ["read", "write"]
                    }
                }
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        List<PMLStatement> statements = PMLCompiler.compilePML(memoryPolicyStore, pml);
        assertEquals(2, statements.size());
        CreatePolicyStatement stmt = (CreatePolicyStatement) statements.get(0);
        assertEquals(
                new CreatePolicyStatement(
                        new StringLiteral("test"),
                        buildMapLiteral("a", "b"),
                        new ArrayList<>(List.of(
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua1"), UA, new StringLiteral("test")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua3"), UA, new StringLiteral("ua1")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua2"), UA, new StringLiteral("test")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua3"), UA, new StringLiteral("ua2"))
                        )),
                        new ArrayList<>(List.of(
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa1"), OA, new StringLiteral("test"))
                        )),
                        new ArrayList<>(List.of(
                                new AssociateStatement(new StringLiteral("ua1"), new StringLiteral("oa1"), buildArrayLiteral("read", "write"))
                        ))
                ),
                stmt
        );

        stmt = (CreatePolicyStatement) statements.get(1);
        assertEquals(
                new CreatePolicyStatement(
                        new StringLiteral("test2"),
                        buildMapLiteral("a", "b"),
                        new ArrayList<>(List.of(
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua1"), UA, new StringLiteral("test2")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua3"), UA, new StringLiteral("ua1")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua2"), UA, new StringLiteral("test2")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua3"), UA, new StringLiteral("ua2"))
                        )),
                        new ArrayList<>(List.of(
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa1"), OA, new StringLiteral("test2"))
                        )),
                        new ArrayList<>(List.of(
                                new AssociateStatement(new StringLiteral("ua1"), new StringLiteral("oa1"), buildArrayLiteral("read", "write"))
                        ))
                ),
                stmt
        );
    }

    @Test
    void testAttributePropertiesInHierarchy() throws PMException {
        String pml = """
                create policy class "test" with properties {"a": "b"} {
                    user attributes {
                        "ua1" {"k": "v"}
                            "ua1-1" {"k1": "v1", "k2": "v2"}
                            "ua1-2"
                                "ua1-2-1"
                            "ua1-3"
                        "ua2"
                            "ua2-1"
                    }
                    object attributes {
                        "oa1"
                            "oa1-1"
                            "oa1-2"
                                "oa1-2-1" {"k1": "v1", "k2": "v2"}
                            "oa1-3"
                        "oa2" {"k1": "v1", "k2": "v2"}
                            "oa2-1" {"k1": "v1", "k2": "v2"}
                    }
                }
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        List<PMLStatement> statements = PMLCompiler.compilePML(memoryPolicyStore, pml);
        assertEquals(1, statements.size());
        CreatePolicyStatement stmt = (CreatePolicyStatement) statements.get(0);
        assertEquals(
                new CreatePolicyStatement(
                        new StringLiteral("test"),
                        buildMapLiteral("a", "b"),
                        new ArrayList<>(List.of(
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua1"), UA, new StringLiteral("test"), buildMapLiteral("k", "v")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua1-1"), UA, new StringLiteral("ua1"), buildMapLiteral("k1", "v1", "k2", "v2")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua1-2"), UA, new StringLiteral("ua1")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua1-2-1"), UA, new StringLiteral("ua1-2")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua1-3"), UA, new StringLiteral("ua1")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua2"), UA, new StringLiteral("test")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("ua2-1"), UA, new StringLiteral("ua2"))
                        )),
                        new ArrayList<>(List.of(
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa1"), OA, new StringLiteral("test")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa1-1"), OA, new StringLiteral("oa1")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa1-2"), OA, new StringLiteral("oa1")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa1-2-1"), OA, new StringLiteral("oa1-2"), buildMapLiteral("k1", "v1", "k2", "v2")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa1-3"), OA, new StringLiteral("oa1")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa2"), OA, new StringLiteral("test"), buildMapLiteral("k1", "v1", "k2", "v2")),
                                new CreatePolicyStatement.CreateOrAssignAttributeStatement(new StringLiteral("oa2-1"), OA, new StringLiteral("oa2"), buildMapLiteral("k1", "v1", "k2", "v2"))
                        )),
                        List.of()
                ),
                stmt
        );
    }

    @Test
    void testMalformedProperty() throws PMException {
        String pml = """
                create policy class "test" {
                    user attributes {
                        "a" {"a"}
                    }
                }
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLCompilationException e = assertThrows(
                PMLCompilationException.class, () -> PMLCompiler.compilePML(memoryPolicyStore, pml));

        assertEquals(1, e.getErrors().size());
        assertEquals(
                "CompileError{position=Position{line=3, start=16, end=0}, errorMessage='mismatched input '}' expecting {':', '||', '&&', '==', '!=', '+'}'}\n",
                e.getMessage()
        );
    }


    @Test
    void testHierarchyWithInvalidIndentation() throws PMException {
        String pml = """
                create policy class "test" {
                    user attributes {
                    "a"
                    "b"
                    }
                }
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLCompilationException e = assertThrows(
                PMLCompilationException.class, () -> PMLCompiler.compilePML(memoryPolicyStore, pml));

        assertEquals(1, e.getErrors().size());
        assertEquals(
                "CompileError{position=Position{line=2, start=21, end=0}, errorMessage='invalid indentation'}\n",
                e.getMessage()
        );


        String pml2 = """
                create policy class "test" {
                    user attributes {
                        "a"
                    "b"
                        "c"
                    }
                }
                """;
        e = assertThrows(
                PMLCompilationException.class, () -> PMLCompiler.compilePML(memoryPolicyStore, pml2));

        assertEquals(1, e.getErrors().size());
        assertEquals(
                "CompileError{position=Position{line=3, start=11, end=0}, errorMessage='invalid indentation'}\n",
                e.getMessage()
        );


        String pml3 = """
                create policy class "test" {
                    user attributes {
                        "a"
                        "b"
                         "c"
                    }
                }
                """;
        e = assertThrows(
                PMLCompilationException.class, () -> PMLCompiler.compilePML(memoryPolicyStore, pml3));

        assertEquals(1, e.getErrors().size());
        assertEquals(
                "CompileError{position=Position{line=4, start=11, end=0}, errorMessage='invalid indentation'}\n",
                e.getMessage()
        );
    }

}