package gov.nist.csd.pm.pap.pml;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.BasicFunctionDefinitionStatementContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParserBaseVisitor;
import org.antlr.v4.runtime.RuleContext;

public class PMLContextVisitor extends PMLParserBaseVisitor<RuleContext> {

    /*@Override
    public RuleContext visitPml(PMLParser.PmlContext ctx) {
        PMLParser.StatementContext statement = ctx.statement(0);
        return visitStatement(statement);
    }

    @Override
    public RuleContext visitCreatePolicyStatement(PMLParser.CreatePolicyStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitCreateNonPCStatement(PMLParser.CreateNonPCStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitCreateObligationStatement(PMLParser.CreateObligationStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitCreateProhibitionStatement(PMLParser.CreateProhibitionStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitSetNodePropertiesStatement(PMLParser.SetNodePropertiesStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitAssignStatement(PMLParser.AssignStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitDeassignStatement(PMLParser.DeassignStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitAssociateStatement(PMLParser.AssociateStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitDissociateStatement(PMLParser.DissociateStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitSetResourceOperationsStatement(PMLParser.SetResourceOperationsStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitDeleteStatement(PMLParser.DeleteStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitDeleteRuleStatement(PMLParser.DeleteRuleStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitVarDeclaration(PMLParser.VarDeclarationContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitShortDeclaration(PMLParser.ShortDeclarationContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitVariableAssignmentStatement(PMLParser.VariableAssignmentStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitBasicFunctionDefinitionStatement(BasicFunctionDefinitionStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitReturnStatement(PMLParser.ReturnStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitFunctionInvokeStatement(PMLParser.FunctionInvokeStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitForeachStatement(PMLParser.ForeachStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitBreakStatement(PMLParser.BreakStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitContinueStatement(PMLParser.ContinueStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitIfStatement(PMLParser.IfStatementContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitNegateExpression(PMLParser.NegateExpressionContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitLogicalExpression(PMLParser.LogicalExpressionContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitPlusExpression(PMLParser.PlusExpressionContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitFunctionInvokeExpression(PMLParser.FunctionInvokeExpressionContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitVariableReferenceExpression(PMLParser.VariableReferenceExpressionContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitLiteralExpression(PMLParser.ExpressionContext ctx) {
        return ctx;
    }

    @Override
    public RuleContext visitEqualsExpression(PMLParser.EqualsExpressionContext ctx) {
        return ctx;
    }*/
}
