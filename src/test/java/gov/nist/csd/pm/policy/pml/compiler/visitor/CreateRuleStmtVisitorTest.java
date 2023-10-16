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
                "create obligation \"test\" {\n" +
                        "                    create rule [\"test\"]\n" +
                        "                    when any user\n" +
                        "                    performs [\"e1\"]\n" +
                        "                    do(ctx) {}\n" +
                        "                }",
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
                "create obligation \"test\" {\n" +
                        "                    create rule \"test\"\n" +
                        "                    when users \"u1\"\n" +
                        "                    performs [\"e1\"]\n" +
                        "                    do(ctx) {}\n" +
                        "                }",
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
                "create obligation \"test\" {\n" +
                        "                    create rule \"test\"\n" +
                        "                    when users in union of \"u1\"\n" +
                        "                    performs [\"e1\"]\n" +
                        "                    do(ctx) {}\n" +
                        "                }",
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
                "create obligation \"test\" {\n" +
                        "                    create rule \"test\"\n" +
                        "                    when users in intersection of \"u1\"\n" +
                        "                    performs [\"e1\"]\n" +
                        "                    do(ctx) {}\n" +
                        "                }",
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
                "create obligation \"test\" {\n" +
                        "                    create rule \"test\"\n" +
                        "                    when processes \"u1\"\n" +
                        "                    performs [\"e1\"]\n" +
                        "                    do(ctx) {}\n" +
                        "                }",
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
                "create obligation \"test\" {\n" +
                        "                    create rule \"test\"\n" +
                        "                    when any user\n" +
                        "                    performs [\"e1\"]\n" +
                        "                    on union of \"oa1\"\n" +
                        "                    do(ctx) {}\n" +
                        "                }",
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
                "create obligation \"test\" {\n" +
                        "                    create rule \"test\"\n" +
                        "                    when any user\n" +
                        "                    performs [\"e1\"]\n" +
                        "                    on intersection of \"oa1\"\n" +
                        "                    do(ctx) {}\n" +
                        "                }",
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
                "create obligation \"test\" {\n" +
                        "                    create rule \"test\"\n" +
                        "                    when any user\n" +
                        "                    performs [\"e1\"]\n" +
                        "                    on \"oa1\"\n" +
                        "                    do(ctx) {}\n" +
                        "                }",
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
        String pml =
                "create obligation \"obligation1\" {\n" +
                "                        create rule \"any user\"\n" +
                "                        when any user\n" +
                "                        performs [\"test_event\"]\n" +
                "                        on any\n" +
                "                        do(ctx) {}\n" +
                "                        \n" +
                "                        create rule \"users\"\n" +
                "                        when users [\"u1\"]\n" +
                "                        performs [\"test_event\"]\n" +
                "                        on any\n" +
                "                        do(ctx) {}\n" +
                "                        \n" +
                "                        create rule \"users union\"\n" +
                "                        when users in union of [\"ua1\", \"ua2\"]\n" +
                "                        performs [\"test_event\"]\n" +
                "                        on any\n" +
                "                        do(ctx) {}\n" +
                "                        \n" +
                "                        create rule \"users intersection\"\n" +
                "                        when users in intersection of [\"ua1\", \"ua2\"]\n" +
                "                        performs [\"test_event\"]\n" +
                "                        on any\n" +
                "                        do(ctx) {}\n" +
                "                        \n" +
                "                        create rule \"processes\"\n" +
                "                        when processes [\"123\"]\n" +
                "                        performs [\"test_event\"]\n" +
                "                        on any\n" +
                "                        do(ctx) {}\n" +
                "                    }";

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
        String pml = "create obligation \"obligation1\" {\n" +
                "                        create rule \"any target\"\n" +
                "                        when any user\n" +
                "                        performs [\"test_event\"]\n" +
                "                        on any\n" +
                "                        do(ctx) {}\n" +
                "                        \n" +
                "                        create rule \"no target is any\"\n" +
                "                        when any user\n" +
                "                        performs [\"test_event\"]\n" +
                "                        do(ctx) {}\n" +
                "                        \n" +
                "                        create rule \"any in union\"\n" +
                "                        when any user\n" +
                "                        performs [\"test_event\"]\n" +
                "                        on union of [\"oa1\", \"oa2\"]\n" +
                "                        do(ctx) {}\n" +
                "                        \n" +
                "                        create rule \"any in intersection\"\n" +
                "                        when any user\n" +
                "                        performs [\"test_event\"]\n" +
                "                        on intersection of [\"oa1\", \"oa2\"]\n" +
                "                        do(ctx) {}\n" +
                "                        \n" +
                "                        create rule \"on targets\"\n" +
                "                        when any user\n" +
                "                        performs [\"test_event\"]\n" +
                "                        on [\"oa1\", \"oa2\"]\n" +
                "                        do(ctx) {}\n" +
                "                    }";
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
        String pml = "e2 := \"e2\"\n" +
                "                    events := [\"e1\", \"e2\"]\n" +
                "                    create obligation \"obligation1\" {\n" +
                "                        create rule \"e1 and e2\"\n" +
                "                        when any user\n" +
                "                        performs [\"e1\", e2]\n" +
                "                        do(ctx) {}\n" +
                "                        \n" +
                "                        create rule \"var events\"\n" +
                "                        when any user\n" +
                "                        performs events\n" +
                "                        do(ctx) {}\n" +
                "                    }";
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
        String pml = "create obligation \"obligation1\" {\n" +
                "                        create rule \"e1 and e2\"\n" +
                "                        when any user\n" +
                "                        performs [\"e1\"]\n" +
                "                        do(ctx) {\n" +
                "                            create policy class \"pc1\"\n" +
                "                            create policy class \"pc2\"\n" +
                "                        }\n" +
                "                    }";
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
        String pml =
                "create obligation \"obligation1\" {\n" +
                "    create rule \"e1 and e2\"\n" +
                "    when any user\n" +
                "    performs [\"e1\"]\n" +
                "    do(ctx) {\n" +
                "        function f1() {}\n" +
                "    }\n" +
                "}";
        PMLCompilationException e =
                assertThrows(PMLCompilationException.class, () -> PMLCompiler.compilePML(new MemoryPolicyStore(), pml));
        assertEquals("functions are not allowed inside response blocks", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testReturnValueInResponseThrowsException() {
        String pml =
                "create obligation \"obligation1\" {\n" +
                "    create rule \"any user\"\n" +
                "    when any user\n" +
                "    performs [\"test_event\"]\n" +
                "    on any\n" +
                "    do(ctx) {\n" +
                "        return \"test\"\n" +
                "    }                        \n" +
                "}";
        assertThrows(PMLCompilationException.class, () -> PMLCompiler.compilePML(new MemoryPolicyStore(), pml));
    }

}