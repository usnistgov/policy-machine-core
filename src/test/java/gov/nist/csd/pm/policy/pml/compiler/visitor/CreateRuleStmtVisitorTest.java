package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.CompiledPML;
import gov.nist.csd.pm.policy.pml.PMLCompiler;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.policy.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.policy.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.statement.CreateObligationStatement;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.pml.statement.CreateRuleStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateRuleStmtVisitorTest {

    @Test
    void testInvalidExpressionTypes() throws PMException {
        PMLParser.CreateObligationStatementContext ctx = PMLContextVisitor.toCtx(
                """
                create obligation "test" {
                    create rule ["test"]
                    when any user
                    performs ["e1"]
                    do(ctx) {}
                }
                """,
                PMLParser.CreateObligationStatementContext.class
        );
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new CreateObligationStmtVisitor(visitorCtx).visitCreateObligationStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type string, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                create obligation "test" {
                    create rule "test"
                    when users "u1"
                    performs ["e1"]
                    do(ctx) {}
                }
                """,
                PMLParser.CreateObligationStatementContext.class
        );
        visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new CreateObligationStmtVisitor(visitorCtx).visitCreateObligationStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []string, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                create obligation "test" {
                    create rule "test"
                    when users in union of "u1"
                    performs ["e1"]
                    do(ctx) {}
                }
                """,
                PMLParser.CreateObligationStatementContext.class
        );
        visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new CreateObligationStmtVisitor(visitorCtx).visitCreateObligationStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []string, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                create obligation "test" {
                    create rule "test"
                    when users in intersection of "u1"
                    performs ["e1"]
                    do(ctx) {}
                }
                """,
                PMLParser.CreateObligationStatementContext.class
        );
        visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new CreateObligationStmtVisitor(visitorCtx).visitCreateObligationStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []string, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                create obligation "test" {
                    create rule "test"
                    when processes "u1"
                    performs ["e1"]
                    do(ctx) {}
                }
                """,
                PMLParser.CreateObligationStatementContext.class
        );
        visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new CreateObligationStmtVisitor(visitorCtx).visitCreateObligationStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []string, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                create obligation "test" {
                    create rule "test"
                    when any user
                    performs ["e1"]
                    on union of "oa1"
                    do(ctx) {}
                }
                """,
                PMLParser.CreateObligationStatementContext.class
        );
        visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new CreateObligationStmtVisitor(visitorCtx).visitCreateObligationStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []string, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                create obligation "test" {
                    create rule "test"
                    when any user
                    performs ["e1"]
                    on intersection of "oa1"
                    do(ctx) {}
                }
                """,
                PMLParser.CreateObligationStatementContext.class
        );
        visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new CreateObligationStmtVisitor(visitorCtx).visitCreateObligationStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []string, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                create obligation "test" {
                    create rule "test"
                    when any user
                    performs ["e1"]
                    on "oa1"
                    do(ctx) {}
                }
                """,
                PMLParser.CreateObligationStatementContext.class
        );
        visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new CreateObligationStmtVisitor(visitorCtx).visitCreateObligationStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []string, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );


    }

    @Test
    void testSubjectClause() throws PMException {
        String pml = """
                    create obligation "obligation1" {
                        create rule "any user"
                        when any user
                        performs ["test_event"]
                        on any
                        do(ctx) {}
                        
                        create rule "users"
                        when users ["u1"]
                        performs ["test_event"]
                        on any
                        do(ctx) {}
                        
                        create rule "users union"
                        when users in union of ["ua1", "ua2"]
                        performs ["test_event"]
                        on any
                        do(ctx) {}
                        
                        create rule "users intersection"
                        when users in intersection of ["ua1", "ua2"]
                        performs ["test_event"]
                        on any
                        do(ctx) {}
                        
                        create rule "processes"
                        when processes ["123"]
                        performs ["test_event"]
                        on any
                        do(ctx) {}
                    }
                    """;
        CompiledPML compiledPML = PMLCompiler.compilePML(new MemoryPolicyStore(), pml);
        assertEquals(1, compiledPML.stmts().size());

        CreateObligationStatement stmt = (CreateObligationStatement)compiledPML.stmts().get(0);
        assertEquals(
                new CreateObligationStatement(
                        new StringLiteral("obligation1"),
                        List.of(
                                new CreateRuleStatement(
                                        new StringLiteral("any user"),
                                        new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.ANY_USER, null),
                                        new CreateRuleStatement.PerformsClause(buildArrayLiteral("test_event")),
                                        new CreateRuleStatement.OnClause(null, CreateRuleStatement.TargetType.ANY_TARGET),
                                        new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())

                                ),
                                new CreateRuleStatement(
                                        new StringLiteral("users"),
                                        new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.USERS, buildArrayLiteral("u1")),
                                        new CreateRuleStatement.PerformsClause(buildArrayLiteral("test_event")),
                                        new CreateRuleStatement.OnClause(null, CreateRuleStatement.TargetType.ANY_TARGET),
                                        new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())

                                ),
                                new CreateRuleStatement(
                                        new StringLiteral("users union"),
                                        new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.USERS_IN_UNION, buildArrayLiteral("ua1", "ua2")),
                                        new CreateRuleStatement.PerformsClause(buildArrayLiteral("test_event")),
                                        new CreateRuleStatement.OnClause(null, CreateRuleStatement.TargetType.ANY_TARGET),
                                        new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())

                                ),
                                new CreateRuleStatement(
                                        new StringLiteral("users intersection"),
                                        new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.USERS_IN_INTERSECTION, buildArrayLiteral("ua1", "ua2")),
                                        new CreateRuleStatement.PerformsClause(buildArrayLiteral("test_event")),
                                        new CreateRuleStatement.OnClause(null, CreateRuleStatement.TargetType.ANY_TARGET),
                                        new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())

                                ),
                                new CreateRuleStatement(
                                        new StringLiteral("processes"),
                                        new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.PROCESSES, buildArrayLiteral("123")),
                                        new CreateRuleStatement.PerformsClause(buildArrayLiteral("test_event")),
                                        new CreateRuleStatement.OnClause(null, CreateRuleStatement.TargetType.ANY_TARGET),
                                        new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())

                                )
                        )
                ),
                stmt
        );
    }

    @Test
    void testOnClause() throws PMException {
        String pml = """
                    create obligation "obligation1" {
                        create rule "any target"
                        when any user
                        performs ["test_event"]
                        on any
                        do(ctx) {}
                        
                        create rule "no target is any"
                        when any user
                        performs ["test_event"]
                        do(ctx) {}
                        
                        create rule "any in union"
                        when any user
                        performs ["test_event"]
                        on union of ["oa1", "oa2"]
                        do(ctx) {}
                        
                        create rule "any in intersection"
                        when any user
                        performs ["test_event"]
                        on intersection of ["oa1", "oa2"]
                        do(ctx) {}
                        
                        create rule "on targets"
                        when any user
                        performs ["test_event"]
                        on ["oa1", "oa2"]
                        do(ctx) {}
                    }
                    """;
        CompiledPML compiledPML = PMLCompiler.compilePML(new MemoryPolicyStore(), pml);
        assertEquals(1, compiledPML.stmts().size());

        CreateObligationStatement stmt = (CreateObligationStatement)compiledPML.stmts().get(0);
        CreateObligationStatement expected = new CreateObligationStatement(
                new StringLiteral("obligation1"),
                List.of(
                        new CreateRuleStatement(
                                new StringLiteral("any target"),
                                new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.ANY_USER, null),
                                new CreateRuleStatement.PerformsClause(buildArrayLiteral("test_event")),
                                new CreateRuleStatement.OnClause(null, CreateRuleStatement.TargetType.ANY_TARGET),
                                new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())

                        ),
                        new CreateRuleStatement(
                                new StringLiteral("no target is any"),
                                new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.ANY_USER, null),
                                new CreateRuleStatement.PerformsClause(buildArrayLiteral("test_event")),
                                new CreateRuleStatement.OnClause(null, CreateRuleStatement.TargetType.ANY_TARGET),
                                new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())

                        ),
                        new CreateRuleStatement(
                                new StringLiteral("any in union"),
                                new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.ANY_USER, null),
                                new CreateRuleStatement.PerformsClause(buildArrayLiteral("test_event")),
                                new CreateRuleStatement.OnClause(buildArrayLiteral("oa1", "oa2"), CreateRuleStatement.TargetType.ANY_IN_UNION),
                                new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())

                        ),
                        new CreateRuleStatement(
                                new StringLiteral("any in intersection"),
                                new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.ANY_USER, null),
                                new CreateRuleStatement.PerformsClause(buildArrayLiteral("test_event")),
                                new CreateRuleStatement.OnClause(buildArrayLiteral("oa1", "oa2"), CreateRuleStatement.TargetType.ANY_IN_INTERSECTION),
                                new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())

                        ),
                        new CreateRuleStatement(
                                new StringLiteral("on targets"),
                                new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.ANY_USER, null),
                                new CreateRuleStatement.PerformsClause(buildArrayLiteral("test_event")),
                                new CreateRuleStatement.OnClause(buildArrayLiteral("oa1", "oa2"), CreateRuleStatement.TargetType.ON_TARGETS),
                                new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())

                        )
                )
        );

        assertEquals(expected, stmt);
    }

    @Test
    void testPerformsClause() throws PMException {
        String pml = """
                    e2 := "e2"
                    events := ["e1", "e2"]
                    create obligation "obligation1" {
                        create rule "e1 and e2"
                        when any user
                        performs ["e1", e2]
                        do(ctx) {}
                        
                        create rule "var events"
                        when any user
                        performs events
                        do(ctx) {}
                    }
                    """;
        CompiledPML compiledPML = PMLCompiler.compilePML(new MemoryPolicyStore(), pml);
        assertEquals(3, compiledPML.stmts().size());

        CreateObligationStatement stmt = (CreateObligationStatement)compiledPML.stmts().get(2);
        CreateObligationStatement expected = new CreateObligationStatement(
                new StringLiteral("obligation1"),
                List.of(
                        new CreateRuleStatement(
                                new StringLiteral("e1 and e2"),
                                new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.ANY_USER, null),
                                new CreateRuleStatement.PerformsClause(new ArrayLiteral(Type.string(), new StringLiteral("e1"), new ReferenceByID("e2"))),
                                new CreateRuleStatement.OnClause(null, CreateRuleStatement.TargetType.ANY_TARGET),
                                new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())
                        ),
                        new CreateRuleStatement(
                                new StringLiteral("var events"),
                                new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.ANY_USER, null),
                                new CreateRuleStatement.PerformsClause(new ReferenceByID("events")),
                                new CreateRuleStatement.OnClause(null, CreateRuleStatement.TargetType.ANY_TARGET),
                                new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())
                        )
                )
        );

        assertEquals(expected, stmt);
    }

    @Test
    void testResponse() throws PMException {
        String pml = """
                    create obligation "obligation1" {
                        create rule "e1 and e2"
                        when any user
                        performs ["e1"]
                        do(ctx) {
                            create policy class "pc1"
                            create policy class "pc2"
                        }
                    }
                    """;
        CompiledPML compiledPML = PMLCompiler.compilePML(new MemoryPolicyStore(), pml);
        assertEquals(1, compiledPML.stmts().size());

        CreateObligationStatement stmt = (CreateObligationStatement)compiledPML.stmts().get(0);
        CreateObligationStatement expected = new CreateObligationStatement(
                new StringLiteral("obligation1"),
                List.of(
                        new CreateRuleStatement(
                                new StringLiteral("e1 and e2"),
                                new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.ANY_USER, null),
                                new CreateRuleStatement.PerformsClause(buildArrayLiteral("e1", "e2")),
                                new CreateRuleStatement.OnClause(null, CreateRuleStatement.TargetType.ANY_TARGET),
                                new CreateRuleStatement.ResponseBlock("ctx", List.of(
                                        new CreatePolicyStatement(new StringLiteral("pc1")),
                                        new CreatePolicyStatement(new StringLiteral("pc2"))
                                ))
                        )
                )
        );
    }

    @Test
    void testFunctionInResponseReturnsError() throws PMException {
        String pml = """
                    create obligation "obligation1" {
                        create rule "e1 and e2"
                        when any user
                        performs ["e1"]
                        do(ctx) {
                            function f1() {}
                        }
                    }
                    """;
        PMLCompilationException e =
                assertThrows(PMLCompilationException.class, () -> PMLCompiler.compilePML(new MemoryPolicyStore(), pml));
        assertEquals("functions are not allowed inside response blocks", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testReturnValueInResponseThrowsException() {
        String pml = """
                    create obligation "obligation1" {
                        create rule "any user"
                        when any user
                        performs ["test_event"]
                        on any
                        do(ctx) {
                            return "test"
                        }                        
                    }
                    """;
        assertThrows(PMLCompilationException.class, () -> PMLCompiler.compilePML(new MemoryPolicyStore(), pml));
    }

}