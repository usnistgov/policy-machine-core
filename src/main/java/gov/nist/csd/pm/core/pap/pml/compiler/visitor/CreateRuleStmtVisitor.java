package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.Type.STRING_TYPE;


import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.AnyArgPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.ArgPatternExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.InArgPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.LogicalArgPatternExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.NegateArgPatternExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.NodeArgPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.ParenArgPatternExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.*;
import gov.nist.csd.pm.core.pap.pml.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateRuleStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateRuleStmtVisitor extends PMLBaseVisitor<CreateRuleStatement> {

    public CreateRuleStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreateRuleStatement visitCreateRuleStatement(PMLParser.CreateRuleStatementContext ctx) {
        SubjectPatternVisitor subjectPatternVisitor = new SubjectPatternVisitor(visitorCtx);
        OperationPatternVisitor operationPatternVisitor = new OperationPatternVisitor(visitorCtx);
        ArgMapVisitor argMapVisitor = new ArgMapVisitor(visitorCtx);
        ResponseVisitor responseVisitor = new ResponseVisitor(visitorCtx);

        Expression<String> name = ExpressionVisitor.compile(visitorCtx, ctx.ruleName, STRING_TYPE);

        return new CreateRuleStatement(
                name,
                subjectPatternVisitor.visit(ctx.subjectPattern()),
                operationPatternVisitor.visit(ctx.operationPattern()),
                argMapVisitor.visitArgPattern(ctx.argPattern()),
                responseVisitor.visitResponse(ctx.response())
        );
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
            return new InSubjectPattern(ExpressionVisitor.removeQuotes(ctx.stringLit().getText()));
        }

        @Override
        public SubjectPatternExpression visitUsernameSubject(PMLParser.UsernameSubjectContext ctx) {
            return new UsernamePattern(ExpressionVisitor.removeQuotes(ctx.stringLit().getText()));
        }

        @Override
        public SubjectPatternExpression visitProcessSubject(PMLParser.ProcessSubjectContext ctx) {
            return new ProcessSubjectPattern(ExpressionVisitor.removeQuotes(ctx.stringLit().getText()));
        }
    }

    static class OperationPatternVisitor extends PMLBaseVisitor<OperationPattern> {

        public OperationPatternVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);
        }

        @Override
        public OperationPattern visitAnyOperation(PMLParser.AnyOperationContext ctx) {
            return new OperationPattern();
        }

        @Override
        public OperationPattern visitIDOperation(PMLParser.IDOperationContext ctx) {
            return new OperationPattern(ExpressionVisitor.removeQuotes(ctx.stringLit().getText()));
        }
    }

    static class ArgPatternVisitor extends PMLBaseVisitor<ArgPatternExpression> {

        public ArgPatternVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);
        }

        @Override
        public ArgPatternExpression visitParenArgPatternExpression(PMLParser.ParenArgPatternExpressionContext ctx) {
            return new ParenArgPatternExpression(visit(ctx.argPatternExpression()));
        }

        @Override
        public ArgPatternExpression visitNegateArgPatternExpression(PMLParser.NegateArgPatternExpressionContext ctx) {
            return new NegateArgPatternExpression(visit(ctx.argPatternExpression()));
        }

        @Override
        public ArgPatternExpression visitBasicArgPatternExpression(PMLParser.BasicArgPatternExpressionContext ctx) {
            return visit(ctx.basicArgPatternExpr());
        }

        @Override
        public ArgPatternExpression visitLogicalArgPatternExpression(PMLParser.LogicalArgPatternExpressionContext ctx) {
            return new LogicalArgPatternExpression(
                    visit(ctx.left),
                    visit(ctx.right),
                    ctx.LOGICAL_AND() != null
            );
        }

        @Override
        public ArgPatternExpression visitAnyPolicyElement(PMLParser.AnyPolicyElementContext ctx) {
            return new AnyArgPattern();
        }

        @Override
        public ArgPatternExpression visitInPolicyElement(PMLParser.InPolicyElementContext ctx) {
            return new InArgPattern(ExpressionVisitor.removeQuotes(ctx.stringLit().getText()));
        }

        @Override
        public ArgPatternExpression visitPolicyElement(PMLParser.PolicyElementContext ctx) {
            return new NodeArgPattern(ExpressionVisitor.removeQuotes(ctx.stringLit().getText()));
        }
    }

    static class ArgMapVisitor extends PMLBaseVisitor<Map<String, List<ArgPatternExpression>>> {

        private ArgPatternVisitor argPatternVisitor = new ArgPatternVisitor(visitorCtx);

        public ArgMapVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);

            this.argPatternVisitor = new ArgPatternVisitor(visitorCtx);
        }

        @Override
        public Map<String, List<ArgPatternExpression>> visitArgPattern(PMLParser.ArgPatternContext ctx) {
            Map<String, List<ArgPatternExpression>> map = new HashMap<>();

            if (ctx == null) {
                return new HashMap<>();
            }

            for (PMLParser.ArgPatternElementContext argPatternElementContext : ctx.argPatternElement()) {
                Map<String, List<ArgPatternExpression>> elementMap = visitArgPatternElement(argPatternElementContext);
                map.putAll(elementMap);
            }

            return map;
        }

        @Override
        public Map<String, List<ArgPatternExpression>> visitArgPatternElement(PMLParser.ArgPatternElementContext ctx) {
            String key = ctx.key.getText();

            List<ArgPatternExpression> expressions = new ArrayList<>();
            if (ctx.single != null) {
                ArgPatternExpression pattern = argPatternVisitor.visit(ctx.single);
                expressions.add(pattern);
            } else if (ctx.multiple != null) {
                for (PMLParser.ArgPatternExpressionContext argCtx : ctx.multiple.argPatternExpression()) {
                    expressions.add(argPatternVisitor.visit(argCtx));
                }
            } else {
                throw new PMLCompilationRuntimeException(ctx, "Invalid arg pattern expression: " + ctx.getText());
            }

            return Map.of(key, expressions);
        }
    }

    static class ResponseVisitor extends PMLBaseVisitor<CreateRuleStatement.ResponseBlock> {

        public ResponseVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);
        }

        @Override
        public CreateRuleStatement.ResponseBlock visitResponse(PMLParser.ResponseContext ctx) {
            String evtVar = ctx.ID().getText();

            // create a new local parser scope for the response block
            // add the event name and event context map to the local parser scope
            VisitorContext localVisitorCtx = visitorCtx.copy();
            try {
                localVisitorCtx.scope().addVariable(evtVar, new Variable(evtVar, MapType.of(STRING_TYPE, ANY_TYPE), true));
            } catch (VariableAlreadyDefinedInScopeException e) {
                throw new PMLCompilationRuntimeException(e);
            }

            PMLParser.ResponseBlockContext responseBlockCtx = ctx.responseBlock();
            List<PMLParser.ResponseStatementContext> responseStmtsCtx = responseBlockCtx.responseStatement();

            StatementVisitor statementVisitor = new StatementVisitor(localVisitorCtx);
            CreateRuleStmtVisitor createRuleStmtVisitor = new CreateRuleStmtVisitor(localVisitorCtx);
            DeleteRuleStmtVisitor deleteRuleStmtVisitor = new DeleteRuleStmtVisitor(localVisitorCtx);

            List<PMLStatement<?>> stmts = new ArrayList<>();
            for (PMLParser.ResponseStatementContext responseStmtCtx : responseStmtsCtx) {
                PMLStatement<?> stmt = null;

                if (responseStmtCtx.statement() != null) {
                    stmt = statementVisitor.visitStatement(responseStmtCtx.statement());
                } else if (responseStmtCtx.createRuleStatement() != null) {
                    stmt = createRuleStmtVisitor.visitCreateRuleStatement(responseStmtCtx.createRuleStatement());
                } else if (responseStmtCtx.deleteRuleStatement() != null) {
                    stmt = deleteRuleStmtVisitor.visitDeleteRuleStatement(responseStmtCtx.deleteRuleStatement());
                }

                stmts.add(stmt);
            }

            CreateRuleStatement.ResponseBlock responseBlock = new CreateRuleStatement.ResponseBlock(evtVar, stmts);

            return responseBlock;
        }
    }
}
