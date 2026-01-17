package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.AnyOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.MatchesOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.OnPattern;
import gov.nist.csd.pm.core.pap.obligation.response.PMLObligationResponse;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.AnyOperationContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.EventPatternContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.OnPatternContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.OperationPatternFuncContext;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.function.StatementBlockParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.obligation.event.operation.OperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.subject.InSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.LogicalSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.NegateSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.ParenSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.ProcessSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.obligation.event.subject.SubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.UsernamePatternExpression;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.function.query.PMLStmtsQueryFunction;
import gov.nist.csd.pm.core.pap.pml.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.core.pap.pml.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateObligationStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.tree.TerminalNode;

public class CreateObligationStmtVisitor extends PMLBaseVisitor<CreateObligationStatement> {

    public CreateObligationStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreateObligationStatement visitCreateObligationStatement(PMLParser.CreateObligationStatementContext ctx) {
        Expression<String> name = ExpressionVisitor.compile(visitorCtx, ctx.name, STRING_TYPE);
        EventPattern eventPattern = new EventPatternVisitor(visitorCtx).visit(ctx.eventPattern());
        PMLObligationResponse response = new ResponseVisitor(visitorCtx).visitResponse(ctx.response());

        return new CreateObligationStatement(name, eventPattern, response);
    }

    static class EventPatternVisitor extends PMLBaseVisitor<EventPattern> {

        public EventPatternVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);
        }

        @Override
        public EventPattern visitEventPattern(EventPatternContext ctx) {
            SubjectPattern subjectPattern = new SubjectPatternVisitor(visitorCtx)
                .visit(ctx.subjectPattern());
            OperationPattern operationPattern = new OperationPatternVisitor(visitorCtx)
                .visit(ctx.operationPattern());

            return new EventPattern(subjectPattern, operationPattern);
        }
    }

    static class SubjectPatternVisitor extends PMLBaseVisitor<SubjectPattern> {

        private final SubjectPatternExpressionVisitor subjectPatternExpressionVisitor;

        public SubjectPatternVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);

            this.subjectPatternExpressionVisitor = new SubjectPatternExpressionVisitor(visitorCtx);
        }

        @Override
        public SubjectPattern visitAnyUserPattern(PMLParser.AnyUserPatternContext ctx) {
            return new SubjectPattern();
        }

        @Override
        public SubjectPattern visitUserPattern(PMLParser.UserPatternContext ctx) {
            SubjectPatternExpression expr = subjectPatternExpressionVisitor.visit(ctx.subjectPatternExpression());
            return new SubjectPattern(expr);
        }
    }

    static class SubjectPatternExpressionVisitor extends PMLBaseVisitor<SubjectPatternExpression> {
        public SubjectPatternExpressionVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);
        }

        @Override
        public SubjectPatternExpression visitBasicSubjectPatternExpression(PMLParser.BasicSubjectPatternExpressionContext ctx) {
            return visit(ctx.basicSubjectPatternExpr());
        }

        @Override
        public SubjectPatternExpression visitParenSubjectPatternExpression(PMLParser.ParenSubjectPatternExpressionContext ctx) {
            return new ParenSubjectPatternExpression(visit(ctx.subjectPatternExpression()));
        }

        @Override
        public SubjectPatternExpression visitNegateSubjectPatternExpression(PMLParser.NegateSubjectPatternExpressionContext ctx) {
            return new NegateSubjectPatternExpression(visit(ctx.subjectPatternExpression()));
        }

        @Override
        public SubjectPatternExpression visitLogicalSubjectPatternExpression(PMLParser.LogicalSubjectPatternExpressionContext ctx) {
            return new LogicalSubjectPatternExpression(
                visit(ctx.left),
                visit(ctx.right),
                ctx.LOGICAL_AND() != null
            );
        }

        @Override
        public SubjectPatternExpression visitInSubject(PMLParser.InSubjectContext ctx) {
            return new InSubjectPatternExpression(ExpressionVisitor.removeQuotes(ctx.stringLit().getText()));
        }

        @Override
        public SubjectPatternExpression visitUsernameSubject(PMLParser.UsernameSubjectContext ctx) {
            return new UsernamePatternExpression(ExpressionVisitor.removeQuotes(ctx.stringLit().getText()));
        }

        @Override
        public SubjectPatternExpression visitProcessSubject(PMLParser.ProcessSubjectContext ctx) {
            return new ProcessSubjectPatternExpression(ExpressionVisitor.removeQuotes(ctx.stringLit().getText()));
        }
    }

    static class OperationPatternVisitor extends PMLBaseVisitor<OperationPattern> {

        public OperationPatternVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);
        }

        @Override
        public OperationPattern visitAnyOperation(AnyOperationContext ctx) {
            return new AnyOperationPattern();
        }

        @Override
        public OperationPattern visitOperationPatternFunc(OperationPatternFuncContext ctx) {
            String opName = ctx.opName.getText();

            OnPatternContext onPatternContext = ctx.onPattern();
            if (onPatternContext == null) {
                return new MatchesOperationPattern("opName");
            }

            Set<String> argNames = onPatternContext.argNames().ID().stream().map(TerminalNode::getText).collect(Collectors.toSet());

            // get FormalParameters of the operation, keep only the ones defined in argNames
            List<FormalParameter<?>> patternParams = new ArrayList<>();
            try {
                PMLFunctionSignature function = visitorCtx.scope().getFunction(opName);

                List<FormalParameter<?>> formalParameters = function.getFormalParameters();

                // check that all of the argNames are defined as formal params for the operation
                Set<String> formalParamNames = formalParameters.stream().map(FormalParameter::getName).collect(Collectors.toSet());
                if (!formalParamNames.containsAll(argNames)) {
                    throw new PMLCompilationRuntimeException(onPatternContext,
                        "expected arg names " + formalParamNames + " but found " + argNames);
                }

                for (FormalParameter<?> formalParam : formalParameters) {
                    if (argNames.contains(formalParam.getName())) {
                        patternParams.add(formalParam);
                    }
                }

                // remove non query functions from visitor ctx
                VisitorContext copy = visitorCtx.copyBasicAndQueryOnly();

                PMLStatementBlock pmlStatementBlock = StatementBlockParser.parseOnStatementBlock(copy,
                    onPatternContext.onPatternBlock(), BOOLEAN_TYPE, patternParams);

                return new MatchesOperationPattern(
                    opName,
                    new OnPattern(
                        argNames,
                        new PMLStmtsQueryFunction<>(opName, BOOLEAN_TYPE, patternParams, pmlStatementBlock)
                    )
                );

            } catch (UnknownFunctionInScopeException e) {
                throw new PMLCompilationRuntimeException(ctx, e.getMessage());
            }
        }
    }

    static class ResponseVisitor extends PMLBaseVisitor<PMLObligationResponse> {

        public ResponseVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);
        }

        @Override
        public PMLObligationResponse visitResponse(PMLParser.ResponseContext ctx) {
            String evtVar = ctx.ID().getText();

            // create a new local parser scope for the response block
            // add the event name and event context map to the local parser scope
            VisitorContext localVisitorCtx = visitorCtx.copy();
            try {
                localVisitorCtx.scope().addVariable(evtVar, new Variable(evtVar, MapType.of(STRING_TYPE, ANY_TYPE), true));
            } catch (VariableAlreadyDefinedInScopeException e) {
                visitorCtx.errorLog().addError(ctx, e.getMessage());
            }

            PMLParser.ResponseBlockContext responseBlockCtx = ctx.responseBlock();
            List<PMLParser.StatementContext> responseStmtsCtx = responseBlockCtx.statement();

            StatementVisitor statementVisitor = new StatementVisitor(localVisitorCtx);

            List<PMLStatement<?>> stmts = new ArrayList<>();
            for (PMLParser.StatementContext responseStmtCtx : responseStmtsCtx) {
                PMLStatement<?> stmt = statementVisitor.visitStatement(responseStmtCtx);
                stmts.add(stmt);
            }

            return new PMLObligationResponse(evtVar, stmts);
        }
    }


}
