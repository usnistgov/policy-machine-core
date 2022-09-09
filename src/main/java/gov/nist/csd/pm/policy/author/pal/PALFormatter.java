package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALLexer;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.compiler.visitor.*;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import java.util.List;
import java.util.Scanner;

public class PALFormatter extends PALBaseVisitor<String> {

    private static final String SPACES = "    ";

    int indentLevel;

    private PALFormatter() {
        indentLevel = 0;
    }

    public static String format(String pal) {
        PALLexer lexer = new PALLexer(CharStreams.fromString(pal));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PALParser parser = new PALParser(tokens);

        return new PALFormatter().visitPal(parser.pal());
    }

    public static String getText(ParserRuleContext ctx) {
        int startIndex = ctx.start.getStartIndex();
        int stopIndex = ctx.stop.getStopIndex();
        Interval interval = new Interval(startIndex, stopIndex);
        return ctx.start.getInputStream().getText(interval);
    }

    public static String statementsToString(List<? extends PALStatement> stmts) {
        StringBuilder s = new StringBuilder();

        for (PALStatement stmt : stmts) {
            s.append(stmt);
        }

        return s.toString();
    }

    private String statementCtxsToString(List<PALParser.StmtContext> stmts) {
        StringBuilder s = new StringBuilder();
        for (PALParser.StmtContext stmt : stmts) {
            if (!s.isEmpty()) {
                s.append("\n");
            }
            s.append(visitStmt(stmt));
        }
        return s.toString();
    }

    private String blockString(List<PALParser.StmtContext> stmts) {
        indentLevel++;
        String stmtsStr = statementCtxsToString(stmts);
        indentLevel--;
        return String.format("{\n%s\n%s}", stmtsStr, applyIndent());
    }

    private String applyIndent() {
        return SPACES.repeat(indentLevel);
    }

    @Override
    public String visitPal(PALParser.PalContext ctx) {
        return visitStmts(ctx.stmts());
    }

    @Override
    public String visitStmts(PALParser.StmtsContext ctx) {
        StringBuilder s = new StringBuilder();
        List<PALParser.StmtContext> stmts = ctx.stmt();
        for (PALParser.StmtContext stmt : stmts) {
            s.append(visitStmt(stmt)).append("\n");
        }
        return s.toString();
    }

    @Override
    public String visitStmt(PALParser.StmtContext ctx) {
        if (ctx.varStmt() != null) {
            return visitVarStmt(ctx.varStmt());
        } else if (ctx.funcDefStmt() != null) {
            return visitFuncDefStmt(ctx.funcDefStmt());
        } else if (ctx.foreachStmt() != null) {
            return visitForeachStmt(ctx.foreachStmt());
        } else if (ctx.funcCallStmt() != null) {
            return visitFuncCallStmt(ctx.funcCallStmt());
        } else if (ctx.ifStmt() != null) {
            return visitIfStmt(ctx.ifStmt());
        } else if (ctx.createStmt() != null) {
            return visitCreateStmt(ctx.createStmt());
        } else if (ctx.setNodePropsStmt() != null) {
            return visitSetNodePropsStmt(ctx.setNodePropsStmt());
        } else if (ctx.assignStmt() != null) {
            return visitAssignStmt(ctx.assignStmt());
        } else if (ctx.deassignStmt() != null) {
            return visitDeassignStmt(ctx.deassignStmt());
        } else if (ctx.deleteStmt() != null) {
            return visitDeleteStmt(ctx.deleteStmt());
        } else if (ctx.associateStmt() != null) {
            return visitAssociateStmt(ctx.associateStmt());
        } else if (ctx.dissociateStmt() != null) {
            return visitDissociateStmt(ctx.dissociateStmt());
        } else if (ctx.funcReturnStmt() != null) {
            return visitFuncReturnStmt(ctx.funcReturnStmt());
        } else if (ctx.breakStmt() != null) {
            return visitBreakStmt(ctx.breakStmt());
        } else if (ctx.continueStmt() != null) {
            return visitContinueStmt(ctx.continueStmt());
        } else if (ctx.setResourceAccessRightsStmt() != null) {
            return visitSetResourceAccessRightsStmt(ctx.setResourceAccessRightsStmt());
        } else if (ctx.deleteRuleStmt() != null) {
            return visitDeleteRuleStmt(ctx.deleteRuleStmt());
        }

        return getText(ctx);
    }

    @Override
    public String visitVarStmt(PALParser.VarStmtContext ctx) {
        return applyIndent() + getText(ctx);
    }

    @Override
    public String visitFuncDefStmt(PALParser.FuncDefStmtContext ctx) {
        String funcStr = String.format("%sfunction %s(%s) %s ",
                applyIndent(), ctx.IDENTIFIER().getText(), visitFormalArgList(ctx.formalArgList()), ctx.funcReturnType().getText());
        String body = visitFuncBody(ctx.funcBody());

        return String.format("%s%s", funcStr, body);
    }

    @Override
    public String visitFormalArgList(PALParser.FormalArgListContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitFormalArg(PALParser.FormalArgContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitFormalArgType(PALParser.FormalArgTypeContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitFuncReturnStmt(PALParser.FuncReturnStmtContext ctx) {
        // todo
        return getText(ctx);
    }

    @Override
    public String visitVarReturnType(PALParser.VarReturnTypeContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitVoidReturnType(PALParser.VoidReturnTypeContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitFuncBody(PALParser.FuncBodyContext ctx) {
        String body = blockString(ctx.stmt());

        return body;
    }

    @Override
    public String visitForeachStmt(PALParser.ForeachStmtContext ctx) {
        String foreach = String.format("%sforeach %s%s in %s ",
                applyIndent(), ctx.key.getText(), ctx.mapValue == null ? "" : ", " + ctx.mapValue.getText(), ctx.expression().getText());

        String body = blockString(ctx.stmtBlock().stmt());

        return String.format("%s%s", foreach, body);
    }

    @Override
    public String visitBreakStmt(PALParser.BreakStmtContext ctx) {
        return applyIndent() + getText(ctx);
    }

    @Override
    public String visitContinueStmt(PALParser.ContinueStmtContext ctx) {
        return applyIndent() + getText(ctx);
    }

    @Override
    public String visitFuncCallStmt(PALParser.FuncCallStmtContext ctx) {
        return applyIndent() + getText(ctx);
    }

    @Override
    public String visitIfStmt(PALParser.IfStmtContext ctx) {
        // todo
        return super.visitIfStmt(ctx);
    }

    @Override
    public String visitElseIfStmt(PALParser.ElseIfStmtContext ctx) {
        return super.visitElseIfStmt(ctx);
    }

    @Override
    public String visitElseStmt(PALParser.ElseStmtContext ctx) {
        return super.visitElseStmt(ctx);
    }

    @Override
    public String visitStringType(PALParser.StringTypeContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitBooleanType(PALParser.BooleanTypeContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitArrayVarType(PALParser.ArrayVarTypeContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitMapVarType(PALParser.MapVarTypeContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitAnyType(PALParser.AnyTypeContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitMapType(PALParser.MapTypeContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitArrayType(PALParser.ArrayTypeContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitStmtBlock(PALParser.StmtBlockContext ctx) {
        return statementCtxsToString(ctx.stmt());
    }

    @Override
    public String visitDeleteNode(PALParser.DeleteNodeContext ctx) {
        return applyIndent() + getText(ctx);
    }

    @Override
    public String visitDeleteObligation(PALParser.DeleteObligationContext ctx) {
        return applyIndent() + getText(ctx);
    }

    @Override
    public String visitDeleteProhibition(PALParser.DeleteProhibitionContext ctx) {
        return applyIndent() + getText(ctx);
    }

    @Override
    public String visitNodeType(PALParser.NodeTypeContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitCreateStmt(PALParser.CreateStmtContext ctx) {
        if (ctx.createAttrStmt() != null) {
            return visitCreateAttrStmt(ctx.createAttrStmt());
        } else if (ctx.createPolicyStmt() != null) {
            return visitCreatePolicyStmt(ctx.createPolicyStmt());
        } else if (ctx.createUserOrObjectStmt() != null) {
            return visitCreateUserOrObjectStmt(ctx.createUserOrObjectStmt());
        } else if (ctx.createProhibitionStmt() != null) {
            return visitCreateProhibitionStmt(ctx.createProhibitionStmt());
        } else if (ctx.createObligationStmt() != null) {
            return visitCreateObligationStmt(ctx.createObligationStmt());
        }

        return "";
    }

    @Override
    public String visitCreatePolicyStmt(PALParser.CreatePolicyStmtContext ctx) {
        return applyIndent() + "create " + getText(ctx);
    }

    @Override
    public String visitCreateAttrStmt(PALParser.CreateAttrStmtContext ctx) {
        return applyIndent() + "create " + getText(ctx);
    }

    @Override
    public String visitCreateUserOrObjectStmt(PALParser.CreateUserOrObjectStmtContext ctx) {
        return applyIndent() + "create " + getText(ctx);
    }

    @Override
    public String visitSetNodePropsStmt(PALParser.SetNodePropsStmtContext ctx) {
        return applyIndent() + getText(ctx);
    }

    @Override
    public String visitAssignStmt(PALParser.AssignStmtContext ctx) {
        return applyIndent() + getText(ctx);
    }

    @Override
    public String visitDeassignStmt(PALParser.DeassignStmtContext ctx) {
        return applyIndent() + getText(ctx);
    }

    @Override
    public String visitAssociateStmt(PALParser.AssociateStmtContext ctx) {
        return applyIndent() + getText(ctx);
    }

    @Override
    public String visitDissociateStmt(PALParser.DissociateStmtContext ctx) {
        return applyIndent() + getText(ctx);
    }

    @Override
    public String visitDeleteStmt(PALParser.DeleteStmtContext ctx) {
        return applyIndent() + getText(ctx);
    }

    @Override
    public String visitCreateObligationStmt(PALParser.CreateObligationStmtContext ctx) {
        String create = String.format("%screate obligation %s", applyIndent(), getText(ctx.label));

        StringBuilder s = new StringBuilder();
        for (PALParser.CreateRuleStmtContext r : ctx.createRuleStmt()) {
            s.append(visitCreateRuleStmt(r));
        }

        return String.format("%s {\n%s\n%s}", create, s, applyIndent());
    }

    @Override
    public String visitCreateRuleStmt(PALParser.CreateRuleStmtContext ctx) {
        indentLevel++;
        String create = String.format("%screate rule %s", applyIndent(), getText(ctx.label));
        String subject = String.format("%swhen %s", applyIndent(), getText(ctx.subjectClause()));
        String performs = String.format("%sperforms %s", applyIndent(), getText(ctx.performsClause));
        String on = ctx.onClause() == null ? "" : String.format("\n%son %s", applyIndent(), getText(ctx.onClause()));
        String reseponse = String.format("%sdo")
        indentLevel--;

        return String.format("%s\n%s\n%s%s", create, subject, performs, on);
    }

    @Override
    public String visitAnyUserSubject(PALParser.AnyUserSubjectContext ctx) {
        return super.visitAnyUserSubject(ctx);
    }

    @Override
    public String visitUserSubject(PALParser.UserSubjectContext ctx) {
        return super.visitUserSubject(ctx);
    }

    @Override
    public String visitUsersListSubject(PALParser.UsersListSubjectContext ctx) {
        return super.visitUsersListSubject(ctx);
    }

    @Override
    public String visitUserAttrSubject(PALParser.UserAttrSubjectContext ctx) {
        return super.visitUserAttrSubject(ctx);
    }

    @Override
    public String visitProcessSubject(PALParser.ProcessSubjectContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitPolicyElement(PALParser.PolicyElementContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitAnyPolicyElement(PALParser.AnyPolicyElementContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitAnyContainedIn(PALParser.AnyContainedInContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitAnyOfSet(PALParser.AnyOfSetContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitAnyPe(PALParser.AnyPeContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitResponse(PALParser.ResponseContext ctx) {
        // TODO
        return super.visitResponse(ctx);
    }

    @Override
    public String visitResponseBlock(PALParser.ResponseBlockContext ctx) {
        // TODO
        return super.visitResponseBlock(ctx);
    }

    @Override
    public String visitResponseStmts(PALParser.ResponseStmtsContext ctx) {
        // TODO
        return super.visitResponseStmts(ctx);
    }

    @Override
    public String visitResponseStmt(PALParser.ResponseStmtContext ctx) {
        return visitStmt(ctx.stmt());
    }

    @Override
    public String visitDeleteRuleStmt(PALParser.DeleteRuleStmtContext ctx) {
        return applyIndent() + getText(ctx);
    }

    @Override
    public String visitCreateProhibitionStmt(PALParser.CreateProhibitionStmtContext ctx) {
        // TODO
        return applyIndent() + "create " + getText(ctx);
    }

    @Override
    public String visitProhibitionContainerList(PALParser.ProhibitionContainerListContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitProhibitionContainerExpression(PALParser.ProhibitionContainerExpressionContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitSetResourceAccessRightsStmt(PALParser.SetResourceAccessRightsStmtContext ctx) {
        return super.visitSetResourceAccessRightsStmt(ctx);
    }

    @Override
    public String visitVariableReference(PALParser.VariableReferenceContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitFunctionCall(PALParser.FunctionCallContext ctx) {
        // expression not function invocation
        return getText(ctx);
    }

    @Override
    public String visitLiteralExpr(PALParser.LiteralExprContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitArray(PALParser.ArrayContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitAccessRightArray(PALParser.AccessRightArrayContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitAccessRight(PALParser.AccessRightContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitMap(PALParser.MapContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitMapEntry(PALParser.MapEntryContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitMapEntryRef(PALParser.MapEntryRefContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitStringLiteral(PALParser.StringLiteralContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitBooleanLiteral(PALParser.BooleanLiteralContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitArrayLiteral(PALParser.ArrayLiteralContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitMapLiteral(PALParser.MapLiteralContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitReferenceByID(PALParser.ReferenceByIDContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitMapEntryReference(PALParser.MapEntryReferenceContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitFuncCall(PALParser.FuncCallContext ctx) {
        return getText(ctx);
    }

    @Override
    public String visitFuncCallArgs(PALParser.FuncCallArgsContext ctx) {
        return getText(ctx);
    }
}
