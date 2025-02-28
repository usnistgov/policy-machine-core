package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.expression.literal.LiteralVisitor;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.operand.*;
import gov.nist.csd.pm.pap.pml.pattern.subject.*;
import gov.nist.csd.pm.pap.pml.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.CreateExecutableStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateRuleStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

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
        OperandMapVisitor operandMapVisitor = new OperandMapVisitor(visitorCtx);
        ResponseVisitor responseVisitor = new ResponseVisitor(visitorCtx);

        Expression name = Expression.compile(visitorCtx, ctx.ruleName, Type.string());

        return new CreateRuleStatement(
                name,
                subjectPatternVisitor.visit(ctx.subjectPattern()),
                operationPatternVisitor.visit(ctx.operationPattern()),
                operandMapVisitor.visitOperandPattern(ctx.operandPattern()),
                responseVisitor.visitResponse(ctx.response())
        );
    }

    static class SubjectPatternVisitor extends PMLBaseVisitor<SubjectPattern> {

        private final LiteralVisitor literalVisitor;
        private final SubjectPatternExpressionVisitor subjectPatternExpressionVisitor;

        public SubjectPatternVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);

            this.literalVisitor = new LiteralVisitor(visitorCtx);
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
        private final LiteralVisitor literalVisitor;
        public SubjectPatternExpressionVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);
            this.literalVisitor = new LiteralVisitor(visitorCtx);
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
            return new InSubjectPattern(literalVisitor.visitStringLit(ctx.stringLit()));
        }

        @Override
        public SubjectPatternExpression visitUsernameSubject(PMLParser.UsernameSubjectContext ctx) {
            return new UsernamePattern(literalVisitor.visitStringLit(ctx.stringLit()));
        }

        @Override
        public SubjectPatternExpression visitProcessSubject(PMLParser.ProcessSubjectContext ctx) {
            return new ProcessSubjectPattern(literalVisitor.visitStringLit(ctx.stringLit()));
        }
    }

    static class OperationPatternVisitor extends PMLBaseVisitor<OperationPattern> {

        private final LiteralVisitor literalVisitor;

        public OperationPatternVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);

            this.literalVisitor = new LiteralVisitor(visitorCtx);
        }

        @Override
        public OperationPattern visitAnyOperation(PMLParser.AnyOperationContext ctx) {
            return new OperationPattern();
        }

        @Override
        public OperationPattern visitIDOperation(PMLParser.IDOperationContext ctx) {
            return new OperationPattern(literalVisitor.visitStringLit(ctx.stringLit()).getValue());
        }
    }

    static class OperandPatternVisitor extends PMLBaseVisitor<OperandPatternExpression> {

        private final LiteralVisitor literalVisitor;

        public OperandPatternVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);

            this.literalVisitor = new LiteralVisitor(visitorCtx);
        }

        @Override
        public OperandPatternExpression visitParenOperandPatternExpression(PMLParser.ParenOperandPatternExpressionContext ctx) {
            return new ParenOperandPatternExpression(visit(ctx.operandPatternExpression()));
        }

        @Override
        public OperandPatternExpression visitNegateOperandPatternExpression(PMLParser.NegateOperandPatternExpressionContext ctx) {
            return new NegateOperandPatternExpression(visit(ctx.operandPatternExpression()));
        }

        @Override
        public OperandPatternExpression visitBasicOperandPatternExpression(PMLParser.BasicOperandPatternExpressionContext ctx) {
            return visit(ctx.basicOperandPatternExpr());
        }

        @Override
        public OperandPatternExpression visitLogicalOperandPatternExpression(PMLParser.LogicalOperandPatternExpressionContext ctx) {
            return new LogicalOperandPatternExpression(
                    visit(ctx.left),
                    visit(ctx.right),
                    ctx.LOGICAL_AND() != null
            );
        }

        @Override
        public OperandPatternExpression visitAnyPolicyElement(PMLParser.AnyPolicyElementContext ctx) {
            return new AnyOperandPattern();
        }

        @Override
        public OperandPatternExpression visitInPolicyElement(PMLParser.InPolicyElementContext ctx) {
            return new InOperandPattern(literalVisitor.visitStringLit(ctx.stringLit()));
        }

        @Override
        public OperandPatternExpression visitPolicyElement(PMLParser.PolicyElementContext ctx) {
            return new NodeOperandPattern(literalVisitor.visitStringLit(ctx.stringLit()));
        }
    }

    static class OperandMapVisitor extends PMLBaseVisitor<Map<String, List<OperandPatternExpression>>> {

        private OperandPatternVisitor operandPatternVisitor = new OperandPatternVisitor(visitorCtx);

        public OperandMapVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);

            this.operandPatternVisitor = new OperandPatternVisitor(visitorCtx);
        }

        @Override
        public Map<String, List<OperandPatternExpression>> visitOperandPattern(PMLParser.OperandPatternContext ctx) {
            Map<String, List<OperandPatternExpression>> map = new HashMap<>();

            if (ctx == null) {
                return new HashMap<>();
            }

            for (PMLParser.OperandPatternElementContext operandPatternElementContext : ctx.operandPatternElement()) {
                Map<String, List<OperandPatternExpression>> elementMap = visitOperandPatternElement(operandPatternElementContext);
                map.putAll(elementMap);
            }

            return map;
        }

        @Override
        public Map<String, List<OperandPatternExpression>> visitOperandPatternElement(PMLParser.OperandPatternElementContext ctx) {
            String key = ctx.key.getText();

            List<OperandPatternExpression> expressions = new ArrayList<>();
            if (ctx.single != null) {
                OperandPatternExpression pattern = operandPatternVisitor.visit(ctx.single);
                expressions.add(pattern);
            } else if (ctx.multiple != null) {
                for (PMLParser.OperandPatternExpressionContext operandCtx : ctx.multiple.operandPatternExpression()) {
                    expressions.add(operandPatternVisitor.visit(operandCtx));
                }
            } else {
                throw new PMLCompilationRuntimeException(ctx, "Invalid operand pattern expression: " + ctx.getText());
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
                localVisitorCtx.scope().addVariable(evtVar, new Variable(evtVar, Type.map(Type.string(), Type.any()), true));
            } catch (VariableAlreadyDefinedInScopeException e) {
                throw new PMLCompilationRuntimeException(e);
            }

            PMLParser.ResponseBlockContext responseBlockCtx = ctx.responseBlock();
            List<PMLParser.ResponseStatementContext> responseStmtsCtx = responseBlockCtx.responseStatement();

            StatementVisitor statementVisitor = new StatementVisitor(localVisitorCtx);
            CreateRuleStmtVisitor createRuleStmtVisitor = new CreateRuleStmtVisitor(localVisitorCtx);
            DeleteRuleStmtVisitor deleteRuleStmtVisitor = new DeleteRuleStmtVisitor(localVisitorCtx);

            List<PMLStatement> stmts = new ArrayList<>();
            for (PMLParser.ResponseStatementContext responseStmtCtx : responseStmtsCtx) {
                PMLStatement stmt = null;

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
