package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.BasicFunctionDefinitionStatementContext;
import gov.nist.csd.pm.pap.pml.compiler.visitor.function.CheckStatementVisitor;
import gov.nist.csd.pm.pap.pml.compiler.visitor.function.FunctionDefinitionVisitor;
import gov.nist.csd.pm.pap.pml.compiler.visitor.function.FunctionSignatureVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;

public class StatementVisitor extends PMLBaseVisitor<PMLStatement> {

    public StatementVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public PMLStatement visitCreatePolicyStatement(PMLParser.CreatePolicyStatementContext ctx) {
        return new CreatePolicyStmtVisitor(visitorCtx).visitCreatePolicyStatement(ctx);
    }

    @Override
    public PMLStatement visitCreateNonPCStatement(PMLParser.CreateNonPCStatementContext ctx) {
        return new CreateNonPCStmtVisitor(visitorCtx).visitCreateNonPCStatement(ctx);
    }

    @Override
    public PMLStatement visitCreateObligationStatement(PMLParser.CreateObligationStatementContext ctx) {
        return new CreateObligationStmtVisitor(visitorCtx).visitCreateObligationStatement(ctx);
    }

    @Override
    public PMLStatement visitCreateProhibitionStatement(PMLParser.CreateProhibitionStatementContext ctx) {
        return new CreateProhibitionStmtVisitor(visitorCtx).visitCreateProhibitionStatement(ctx);
    }

    @Override
    public PMLStatement visitSetNodePropertiesStatement(PMLParser.SetNodePropertiesStatementContext ctx) {
        return new SetNodePropertiesStmtVisitor(visitorCtx).visitSetNodePropertiesStatement(ctx);
    }

    @Override
    public PMLStatement visitAssignStatement(PMLParser.AssignStatementContext ctx) {
        return new AssignStmtVisitor(visitorCtx).visitAssignStatement(ctx);
    }

    @Override
    public PMLStatement visitDeassignStatement(PMLParser.DeassignStatementContext ctx) {
        return new DeassignStmtVisitor(visitorCtx).visitDeassignStatement(ctx);
    }

    @Override
    public PMLStatement visitAssociateStatement(PMLParser.AssociateStatementContext ctx) {
        return new AssociateStmtVisitor(visitorCtx).visitAssociateStatement(ctx);
    }

    @Override
    public PMLStatement visitDissociateStatement(PMLParser.DissociateStatementContext ctx) {
        return new DissociateStmtVisitor(visitorCtx).visitDissociateStatement(ctx);
    }

    @Override
    public PMLStatement visitSetResourceOperationsStatement(PMLParser.SetResourceOperationsStatementContext ctx) {
        return new SetResourceOperationsStmtVisitor(visitorCtx).visitSetResourceOperationsStatement(ctx);
    }

    @Override
    public PMLStatement visitDeleteStatement(PMLParser.DeleteStatementContext ctx) {
        return new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
    }

    @Override
    public PMLStatement visitDeleteRuleStatement(PMLParser.DeleteRuleStatementContext ctx) {
        return new DeleteRuleStmtVisitor(visitorCtx).visitDeleteRuleStatement(ctx);
    }

    @Override
    public PMLStatement visitVariableAssignmentStatement(PMLParser.VariableAssignmentStatementContext ctx) {
        return new VarStmtVisitor(visitorCtx).visitVariableAssignmentStatement(ctx);
    }

    @Override
    public PMLStatement visitVarDeclaration(PMLParser.VarDeclarationContext ctx) {
        return new VarStmtVisitor(visitorCtx).visitVarDeclaration(ctx);
    }

    @Override
    public PMLStatement visitShortDeclaration(PMLParser.ShortDeclarationContext ctx) {
        return new VarStmtVisitor(visitorCtx).visitShortDeclaration(ctx);
    }

    @Override
    public PMLStatement visitOperationDefinitionStatement(PMLParser.OperationDefinitionStatementContext ctx) {
        return new FunctionDefinitionVisitor(visitorCtx, new FunctionSignatureVisitor(visitorCtx, true)).visitOperationDefinitionStatement(ctx);
    }

    @Override
    public PMLStatement visitRoutineDefinitionStatement(PMLParser.RoutineDefinitionStatementContext ctx) {
        return new FunctionDefinitionVisitor(visitorCtx, new FunctionSignatureVisitor(visitorCtx, true)).visitRoutineDefinitionStatement(ctx);
    }

    @Override
    public PMLStatement visitBasicFunctionDefinitionStatement(BasicFunctionDefinitionStatementContext ctx) {
        return new FunctionDefinitionVisitor(visitorCtx, new FunctionSignatureVisitor(visitorCtx, true)).visitBasicFunctionDefinitionStatement(ctx);
    }

    @Override
    public PMLStatement visitReturnStatement(PMLParser.ReturnStatementContext ctx) {
        return new FunctionReturnStmtVisitor(visitorCtx).visitReturnStatement(ctx);
    }

    @Override
    public PMLStatement visitFunctionInvokeStatement(PMLParser.FunctionInvokeStatementContext ctx) {
        return new FunctionInvokeStmtVisitor(visitorCtx).visitFunctionInvokeStatement(ctx);
    }

    @Override
    public PMLStatement visitForeachStatement(PMLParser.ForeachStatementContext ctx) {
        return new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
    }

    @Override
    public PMLStatement visitBreakStatement(PMLParser.BreakStatementContext ctx) {
        return new BreakStmtVisitor(visitorCtx).visitBreakStatement(ctx);
    }

    @Override
    public PMLStatement visitContinueStatement(PMLParser.ContinueStatementContext ctx) {
        return new ContinueStmtVisitor(visitorCtx).visitContinueStatement(ctx);
    }

    @Override
    public PMLStatement visitIfStatement(PMLParser.IfStatementContext ctx) {
        return new IfStmtVisitor(visitorCtx).visitIfStatement(ctx);
    }

    @Override
    public PMLStatement visitCheckStatement(PMLParser.CheckStatementContext ctx) {
        return new CheckStatementVisitor(visitorCtx).visitCheckStatement(ctx);
    }
}
