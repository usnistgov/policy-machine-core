package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.AnyOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.MatchesOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.OnPattern;
import gov.nist.csd.pm.core.pap.obligation.event.subject.InSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.LogicalSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.NegateSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.ParenSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.ProcessSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.obligation.event.subject.UsernamePatternExpression;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import gov.nist.csd.pm.core.pap.pml.operation.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.basic.ReturnStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateObligationStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CreateObligationStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                create obligation "test"
                when any user
                performs any operation
                do(ctx) {}
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new CreateObligationStatement(
                    new StringLiteralExpression("test"),
                    new EventPattern(
                        new SubjectPattern(),
                        new AnyOperationPattern()
                    ),
                    new ObligationResponse("ctx", List.of())
                ),
                stmt
        );
    }

    @Test
    void testInvalidNameExpression() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));

        testCompilationError(
                """
                create obligation ["test"] {}
                """, visitorCtx, 1,
                "expected expression type string, got []string"
                );
    }

    @Test
    void testVariableInProcess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when process test
            performs any operation
            do(ctx) {}
            """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        visitorCtx.scope().addVariable("test", new Variable("test", STRING_TYPE, false));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(new ProcessSubjectPatternExpression(new VariableReferenceExpression<>("test", STRING_TYPE))),
                    new AnyOperationPattern()
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }
    
    @Test
    void testUsernameSubjectLiteral() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when user "alice"
            performs any operation
            do(ctx) {}
            """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(new UsernamePatternExpression(new StringLiteralExpression("alice"))),
                    new AnyOperationPattern()
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    @Test
    void testInSubjectPattern() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when user in "ua1"
            performs any operation
            do(ctx) {}
            """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(new InSubjectPatternExpression(new StringLiteralExpression("ua1"))),
                    new AnyOperationPattern()
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    @Test
    void testNegateSubjectPattern() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when !user "alice"
            performs any operation
            do(ctx) {}
            """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(new NegateSubjectPatternExpression(
                        new UsernamePatternExpression(new StringLiteralExpression("alice"))
                    )),
                    new AnyOperationPattern()
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    @Test
    void testLogicalAndSubjectPattern() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when user "alice" && user in "ua1"
            performs any operation
            do(ctx) {}
            """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(new LogicalSubjectPatternExpression(
                        new UsernamePatternExpression(new StringLiteralExpression("alice")),
                        new InSubjectPatternExpression(new StringLiteralExpression("ua1")),
                        true
                    )),
                    new AnyOperationPattern()
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    @Test
    void testLogicalOrSubjectPattern() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when user "alice" || user in "ua1"
            performs any operation
            do(ctx) {}
            """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(new LogicalSubjectPatternExpression(
                        new UsernamePatternExpression(new StringLiteralExpression("alice")),
                        new InSubjectPatternExpression(new StringLiteralExpression("ua1")),
                        false
                    )),
                    new AnyOperationPattern()
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    @Test
    void testParenSubjectPattern() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when (user "alice")
            performs any operation
            do(ctx) {}
            """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(new ParenSubjectPatternExpression(
                        new UsernamePatternExpression(new StringLiteralExpression("alice"))
                    )),
                    new AnyOperationPattern()
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    @Test
    void testProcessSubjectLiteral() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when process "p1"
            performs any operation
            do(ctx) {}
            """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(new ProcessSubjectPatternExpression(new StringLiteralExpression("p1"))),
                    new AnyOperationPattern()
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }
    
    @Test
    void testMatchesOperationNoOnClause() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when any user
            performs "assign"
            do(ctx) {}
            """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(),
                    new MatchesOperationPattern("assign")
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    private static VisitorContext buildScopeWithTestOp() throws PMException {
        CompileScope scope = new CompileScope(new MemoryPAP());
        FormalParameter<String> argA = new FormalParameter<>("argA", STRING_TYPE);
        PMLOperationSignature opSig = new PMLOperationSignature(
            OperationType.RESOURCEOP, "testOp", VOID_TYPE,
            List.of(argA),
            List.of(argA),
            List.of()
        );
        scope.addOperation("testOp", opSig);
        return new VisitorContext(scope);
    }

    @Test
    void testMatchesOperationOnClauseEmptyArgs() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when any user
            performs "testOp" on() { return true }
            do(ctx) {}
            """);
        VisitorContext visitorCtx = buildScopeWithTestOp();
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        PMLStmtsRoutine<Boolean> returnTrueRoutine = new PMLStmtsRoutine<>(
            "", BOOLEAN_TYPE, List.of(),
            new PMLStatementBlock(List.of(new ReturnStatement(new BoolLiteralExpression(true))))
        );
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(),
                    new MatchesOperationPattern("testOp", new OnPattern(Set.of(), returnTrueRoutine))
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    @Test
    void testMatchesOperationOnClauseWithNamedArgs() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when any user
            performs "testOp" on(argA) { return true }
            do(ctx) {}
            """);
        VisitorContext visitorCtx = buildScopeWithTestOp();
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        PMLStmtsRoutine<Boolean> returnTrueRoutine = new PMLStmtsRoutine<>(
            "", BOOLEAN_TYPE, List.of(),
            new PMLStatementBlock(List.of(new ReturnStatement(new BoolLiteralExpression(true))))
        );
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(),
                    new MatchesOperationPattern("testOp", new OnPattern(Set.of("argA"), returnTrueRoutine))
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    @Test
    void testMatchesOperationUnknownOperation() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        testCompilationError(
            """
            create obligation "o1"
            when any user
            performs "unknownOp" on() { return true }
            do(ctx) {}
            """,
            visitorCtx, 1,
            "unknown operation 'unknownOp' in scope"
        );
    }

    @Test
    void testMatchesOperationInvalidArgName() throws PMException {
        VisitorContext visitorCtx = buildScopeWithTestOp();
        testCompilationError(
            """
            create obligation "o1"
            when any user
            performs "testOp" on(badArg) { return true }
            do(ctx) {}
            """,
            visitorCtx, 1,
            "expected arg names [argA] but found [badArg]"
        );
    }

    @Test
    void testResponseWithStatements() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when any user
            performs any operation
            do(ctx) {
                create PC "pc1"
            }
            """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(new SubjectPattern(), new AnyOperationPattern()),
                new ObligationResponse("ctx", List.of(
                    new CreatePolicyClassStatement(new StringLiteralExpression("pc1"))
                ))
            ),
            stmt
        );
    }

    @Test
    void testResponseEventCtxVariableUsage() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when any user
            performs any operation
            do(evtCtx) {
                create PC evtCtx.opName
            }
            """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        CreateObligationStatement obligStmt = (CreateObligationStatement) stmt;
        assertEquals("evtCtx", obligStmt.getResponse().getEventCtxVariable());
        assertEquals(1, obligStmt.getResponse().getStatements().size());
    }

    @Test
    void testNameVariableExpression() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation obligName
            when any user
            performs any operation
            do(ctx) {}
            """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        visitorCtx.scope().addVariable("obligName", new Variable("obligName", STRING_TYPE, false));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new CreateObligationStatement(
                new VariableReferenceExpression<>("obligName", STRING_TYPE),
                new EventPattern(new SubjectPattern(), new AnyOperationPattern()),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    @Test
    void testInvalidUsernameExpression() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        testCompilationError(
            """
            create obligation "o1"
            when user ["alice"]
            performs any operation
            do(ctx) {}
            """,
            visitorCtx, 1,
            "expected expression type string, got []string"
        );
    }

    @Test
    void testInvalidInSubjectExpression() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        testCompilationError(
            """
            create obligation "o1"
            when user in ["ua1"]
            performs any operation
            do(ctx) {}
            """,
            visitorCtx, 1,
            "expected expression type string, got []string"
        );
    }

    @Test
    void testInvalidProcessExpression() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        testCompilationError(
            """
            create obligation "o1"
            when process ["p1"]
            performs any operation
            do(ctx) {}
            """,
            visitorCtx, 1,
            "expected expression type string, got []string"
        );
    }

    @Test
    void testOnClauseWithEventOnlyParam() throws PMException {
        // eventOnly is an extra event param not present in formal params
        CompileScope scope = new CompileScope(new MemoryPAP());
        FormalParameter<String> argA = new FormalParameter<>("argA", STRING_TYPE);
        FormalParameter<String> eventOnly = new FormalParameter<>("eventOnly", STRING_TYPE);
        PMLOperationSignature opSig = new PMLOperationSignature(
            OperationType.RESOURCEOP, "testOp2", VOID_TYPE,
            List.of(argA),
            List.of(argA, eventOnly),
            List.of()
        );
        scope.addOperation("testOp2", opSig);
        VisitorContext visitorCtx = new VisitorContext(scope);

        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when any user
            performs "testOp2" on(eventOnly) { return true }
            do(ctx) {}
            """);
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        PMLStmtsRoutine<Boolean> routine = new PMLStmtsRoutine<>(
            "", BOOLEAN_TYPE, List.of(),
            new PMLStatementBlock(List.of(new ReturnStatement(new BoolLiteralExpression(true))))
        );
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(),
                    new MatchesOperationPattern("testOp2", new OnPattern(Set.of("eventOnly"), routine))
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    @Test
    void testOnClauseWithFormalAndEventOnlyParams() throws PMException {
        // on() clause references both the formal-mapped param and the event-only param
        CompileScope scope = new CompileScope(new MemoryPAP());
        FormalParameter<String> argA = new FormalParameter<>("argA", STRING_TYPE);
        FormalParameter<String> eventOnly = new FormalParameter<>("eventOnly", STRING_TYPE);
        PMLOperationSignature opSig = new PMLOperationSignature(
            OperationType.RESOURCEOP, "testOp2", VOID_TYPE,
            List.of(argA),
            List.of(argA, eventOnly),
            List.of()
        );
        scope.addOperation("testOp2", opSig);
        VisitorContext visitorCtx = new VisitorContext(scope);

        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when any user
            performs "testOp2" on(argA, eventOnly) { return true }
            do(ctx) {}
            """);
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        PMLStmtsRoutine<Boolean> routine = new PMLStmtsRoutine<>(
            "", BOOLEAN_TYPE, List.of(),
            new PMLStatementBlock(List.of(new ReturnStatement(new BoolLiteralExpression(true))))
        );
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(),
                    new MatchesOperationPattern("testOp2", new OnPattern(Set.of("argA", "eventOnly"), routine))
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    @Test
    void testOnClauseFormalOnlyParamNotInEventParams_Error() throws PMException {
        // argB is a formal param but is NOT in event params — referencing it in on() should fail
        CompileScope scope = new CompileScope(new MemoryPAP());
        FormalParameter<String> argA = new FormalParameter<>("argA", STRING_TYPE);
        FormalParameter<String> argB = new FormalParameter<>("argB", STRING_TYPE);
        PMLOperationSignature opSig = new PMLOperationSignature(
            OperationType.RESOURCEOP, "testOp3", VOID_TYPE,
            List.of(argA, argB),
            List.of(argA),
            List.of()
        );
        scope.addOperation("testOp3", opSig);
        VisitorContext visitorCtx = new VisitorContext(scope);

        testCompilationError(
            """
            create obligation "o1"
            when any user
            performs "testOp3" on(argB) { return true }
            do(ctx) {}
            """,
            visitorCtx, 1,
            "expected arg names [argA] but found [argB]"
        );
    }

    @Test
    void testOnClauseWithOptionalFormalParam() throws PMException {
        // Optional formal param can be referenced in on() clause
        // the compiled patternParam must have required=false
        CompileScope scope = new CompileScope(new MemoryPAP());
        FormalParameter<String> argA = new FormalParameter<>("argA", STRING_TYPE, true);
        FormalParameter<String> argB = new FormalParameter<>("argB", STRING_TYPE, false);
        PMLOperationSignature opSig = new PMLOperationSignature(
            OperationType.RESOURCEOP, "testOp4", VOID_TYPE,
            List.of(argA, argB),
            List.of(argA, argB),
            List.of()
        );
        scope.addOperation("testOp4", opSig);
        VisitorContext visitorCtx = new VisitorContext(scope);

        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when any user
            performs "testOp4" on(argB) { return true }
            do(ctx) {}
            """);
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        CreateObligationStatement obligStmt = (CreateObligationStatement) stmt;
        MatchesOperationPattern matchesOp =
            (MatchesOperationPattern) obligStmt.getEventPattern().getOperationPattern();
        assertEquals(Set.of("argB"), matchesOp.getOnPattern().patternArgs());

        List<FormalParameter<?>> patternParams = matchesOp.getOnPattern().func().getPmlFormalArgs();
        assertEquals(1, patternParams.size());
        assertEquals("argB", patternParams.get(0).getName());
        assertFalse(patternParams.get(0).isRequired());
    }

    @Test
    void testOnClauseWithOptionalEventOnlyParam() throws PMException {
        // Optional event-only param in on() clause
        // compiled patternParam must have required=false
        CompileScope scope = new CompileScope(new MemoryPAP());
        FormalParameter<String> argA = new FormalParameter<>("argA", STRING_TYPE);
        FormalParameter<String> optId = new FormalParameter<>("optId", STRING_TYPE, false);
        PMLOperationSignature opSig = new PMLOperationSignature(
            OperationType.RESOURCEOP, "testOp5", VOID_TYPE,
            List.of(argA),
            List.of(argA, optId),
            List.of()
        );
        scope.addOperation("testOp5", opSig);
        VisitorContext visitorCtx = new VisitorContext(scope);

        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when any user
            performs "testOp5" on(optId) { return true }
            do(ctx) {}
            """);
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        CreateObligationStatement obligStmt = (CreateObligationStatement) stmt;
        MatchesOperationPattern matchesOp =
            (MatchesOperationPattern) obligStmt.getEventPattern().getOperationPattern();
        assertEquals(Set.of("optId"), matchesOp.getOnPattern().patternArgs());

        List<FormalParameter<?>> patternParams = matchesOp.getOnPattern().func().getPmlFormalArgs();
        assertEquals(1, patternParams.size());
        assertEquals("optId", patternParams.get(0).getName());
        assertFalse(patternParams.get(0).isRequired());
    }

    @Test
    void testOnClauseNamedArgNoBlockBody() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when any user
            performs "testOp" on(argA)
            do(ctx) {}
            """);
        VisitorContext visitorCtx = buildScopeWithTestOp();
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        PMLStmtsRoutine<Boolean> returnTrueRoutine = new PMLStmtsRoutine<>(
            "", BOOLEAN_TYPE, List.of(),
            new PMLStatementBlock(List.of(new ReturnStatement(new BoolLiteralExpression(true))))
        );
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(),
                    new MatchesOperationPattern("testOp", new OnPattern(Set.of("argA"), returnTrueRoutine))
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    @Test
    void testOnClauseEmptyArgsNoBlockBody() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when any user
            performs "testOp" on()
            do(ctx) {}
            """);
        VisitorContext visitorCtx = buildScopeWithTestOp();
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        PMLStmtsRoutine<Boolean> returnTrueRoutine = new PMLStmtsRoutine<>(
            "", BOOLEAN_TYPE, List.of(),
            new PMLStatementBlock(List.of(new ReturnStatement(new BoolLiteralExpression(true))))
        );
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(),
                    new MatchesOperationPattern("testOp", new OnPattern(Set.of(), returnTrueRoutine))
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    @Test
    void testNestedLogicalSubjectPattern() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when (user "alice" && user in "ua1") || user "bob"
            performs any operation
            do(ctx) {}
            """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(new LogicalSubjectPatternExpression(
                        new ParenSubjectPatternExpression(new LogicalSubjectPatternExpression(
                            new UsernamePatternExpression(new StringLiteralExpression("alice")),
                            new InSubjectPatternExpression(new StringLiteralExpression("ua1")),
                            true
                        )),
                        new UsernamePatternExpression(new StringLiteralExpression("bob")),
                        false
                    )),
                    new AnyOperationPattern()
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }

    @Test
    void testOnClauseEventParamAccessibleAsVariable() throws PMException {
        // A param named in on(argA) must be available as a variable inside the block
        VisitorContext visitorCtx = buildScopeWithTestOp();
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when any user
            performs "testOp" on(argA) { return argA == "test" }
            do(c) {}
            """);
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        CreateObligationStatement obligStmt = (CreateObligationStatement) stmt;
        MatchesOperationPattern matchesOp =
            (MatchesOperationPattern) obligStmt.getEventPattern().getOperationPattern();
        assertEquals(Set.of("argA"), matchesOp.getOnPattern().patternArgs());
    }
}
