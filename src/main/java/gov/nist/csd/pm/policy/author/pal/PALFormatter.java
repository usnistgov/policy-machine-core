package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALLexer;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.Scanner;

public class PALFormatter extends PALBaseVisitor<String> {

    private static final String SPACES = "    ";

    private int indentLevel;

    private PALFormatter() {
        indentLevel = 0;
    }

    private String indent() {
        return SPACES.repeat(indentLevel);
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

    @Override
    public String visitPal(PALParser.PalContext ctx) {
        return visitStmts(ctx.stmts());
    }

    @Override
    public String visitStmts(PALParser.StmtsContext ctx) {
        StringBuilder s = new StringBuilder();
        for (PALParser.StmtContext stmtCtx : ctx.stmt()) {
            s.append(visitStmt(stmtCtx));
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

    private String newLineAfterSemiColon(ParserRuleContext ctx) {
        String text = getText(ctx);
        return text + "\n";
    }

    @Override
    public String visitVarStmt(PALParser.VarStmtContext ctx) {
        return indent() + newLineAfterSemiColon(ctx);
    }

    @Override
    public String visitFuncDefStmt(PALParser.FuncDefStmtContext ctx) {
        String text = getText(ctx);
        int stmtStartIndex = ctx.start.getStartIndex();
        PALParser.FuncBodyContext funcBodyCtx = ctx.funcBody();
        int openCurlyIndex = funcBodyCtx.OPEN_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        int closeCurlyIndex = funcBodyCtx.CLOSE_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        String signature = indent() + (text.substring(0, openCurlyIndex + 1)) + "\n";

        indentLevel++;
        StringBuilder body = new StringBuilder();
        List<PALParser.StmtContext> bodyStmtCtxs = funcBodyCtx.stmt();
        for (PALParser.StmtContext stmtCtx : bodyStmtCtxs) {
            body.append(visitStmt(stmtCtx));
        }
        indentLevel--;

        String close = "\n" + indent() + text.substring(closeCurlyIndex) + "\n";

        return signature + body + close;
    }

    @Override
    public String visitFormalArgList(PALParser.FormalArgListContext ctx) {
        return super.visitFormalArgList(ctx);
    }

    @Override
    public String visitFormalArg(PALParser.FormalArgContext ctx) {
        return super.visitFormalArg(ctx);
    }

    @Override
    public String visitFormalArgType(PALParser.FormalArgTypeContext ctx) {
        return super.visitFormalArgType(ctx);
    }

    @Override
    public String visitFuncReturnStmt(PALParser.FuncReturnStmtContext ctx) {
        return super.visitFuncReturnStmt(ctx);
    }

    @Override
    public String visitVarReturnType(PALParser.VarReturnTypeContext ctx) {
        return super.visitVarReturnType(ctx);
    }

    @Override
    public String visitVoidReturnType(PALParser.VoidReturnTypeContext ctx) {
        return super.visitVoidReturnType(ctx);
    }

    @Override
    public String visitFuncBody(PALParser.FuncBodyContext ctx) {
        return super.visitFuncBody(ctx);
    }

    @Override
    public String visitForeachStmt(PALParser.ForeachStmtContext ctx) {
        return super.visitForeachStmt(ctx);
    }

    @Override
    public String visitBreakStmt(PALParser.BreakStmtContext ctx) {
        return super.visitBreakStmt(ctx);
    }

    @Override
    public String visitContinueStmt(PALParser.ContinueStmtContext ctx) {
        return super.visitContinueStmt(ctx);
    }

    @Override
    public String visitFuncCallStmt(PALParser.FuncCallStmtContext ctx) {
        return super.visitFuncCallStmt(ctx);
    }

    @Override
    public String visitIfStmt(PALParser.IfStmtContext ctx) {
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
        return super.visitStringType(ctx);
    }

    @Override
    public String visitBooleanType(PALParser.BooleanTypeContext ctx) {
        return super.visitBooleanType(ctx);
    }

    @Override
    public String visitArrayVarType(PALParser.ArrayVarTypeContext ctx) {
        return super.visitArrayVarType(ctx);
    }

    @Override
    public String visitMapVarType(PALParser.MapVarTypeContext ctx) {
        return super.visitMapVarType(ctx);
    }

    @Override
    public String visitAnyType(PALParser.AnyTypeContext ctx) {
        return super.visitAnyType(ctx);
    }

    @Override
    public String visitMapType(PALParser.MapTypeContext ctx) {
        return super.visitMapType(ctx);
    }

    @Override
    public String visitArrayType(PALParser.ArrayTypeContext ctx) {
        return super.visitArrayType(ctx);
    }

    @Override
    public String visitStmtBlock(PALParser.StmtBlockContext ctx) {
        return super.visitStmtBlock(ctx);
    }

    @Override
    public String visitDeleteNode(PALParser.DeleteNodeContext ctx) {
        return super.visitDeleteNode(ctx);
    }

    @Override
    public String visitDeleteObligation(PALParser.DeleteObligationContext ctx) {
        return super.visitDeleteObligation(ctx);
    }

    @Override
    public String visitDeleteProhibition(PALParser.DeleteProhibitionContext ctx) {
        return super.visitDeleteProhibition(ctx);
    }

    @Override
    public String visitNodeType(PALParser.NodeTypeContext ctx) {
        return super.visitNodeType(ctx);
    }

    @Override
    public String visitCreateStmt(PALParser.CreateStmtContext ctx) {
        return super.visitCreateStmt(ctx);
    }

    @Override
    public String visitCreatePolicyStmt(PALParser.CreatePolicyStmtContext ctx) {
        return super.visitCreatePolicyStmt(ctx);
    }

    @Override
    public String visitCreateAttrStmt(PALParser.CreateAttrStmtContext ctx) {
        return super.visitCreateAttrStmt(ctx);
    }

    @Override
    public String visitCreateUserOrObjectStmt(PALParser.CreateUserOrObjectStmtContext ctx) {
        return super.visitCreateUserOrObjectStmt(ctx);
    }

    @Override
    public String visitSetNodePropsStmt(PALParser.SetNodePropsStmtContext ctx) {
        return super.visitSetNodePropsStmt(ctx);
    }

    @Override
    public String visitAssignStmt(PALParser.AssignStmtContext ctx) {
        return super.visitAssignStmt(ctx);
    }

    @Override
    public String visitDeassignStmt(PALParser.DeassignStmtContext ctx) {
        return super.visitDeassignStmt(ctx);
    }

    @Override
    public String visitAssociateStmt(PALParser.AssociateStmtContext ctx) {
        return super.visitAssociateStmt(ctx);
    }

    @Override
    public String visitDissociateStmt(PALParser.DissociateStmtContext ctx) {
        return super.visitDissociateStmt(ctx);
    }

    @Override
    public String visitDeleteStmt(PALParser.DeleteStmtContext ctx) {
        return super.visitDeleteStmt(ctx);
    }

    @Override
    public String visitCreateObligationStmt(PALParser.CreateObligationStmtContext ctx) {
        return super.visitCreateObligationStmt(ctx);
    }

    @Override
    public String visitCreateRuleStmt(PALParser.CreateRuleStmtContext ctx) {
        return super.visitCreateRuleStmt(ctx);
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
        return super.visitProcessSubject(ctx);
    }

    @Override
    public String visitPolicyElement(PALParser.PolicyElementContext ctx) {
        return super.visitPolicyElement(ctx);
    }

    @Override
    public String visitAnyPolicyElement(PALParser.AnyPolicyElementContext ctx) {
        return super.visitAnyPolicyElement(ctx);
    }

    @Override
    public String visitAnyContainedIn(PALParser.AnyContainedInContext ctx) {
        return super.visitAnyContainedIn(ctx);
    }

    @Override
    public String visitAnyOfSet(PALParser.AnyOfSetContext ctx) {
        return super.visitAnyOfSet(ctx);
    }

    @Override
    public String visitAnyPe(PALParser.AnyPeContext ctx) {
        return super.visitAnyPe(ctx);
    }

    @Override
    public String visitResponse(PALParser.ResponseContext ctx) {
        return super.visitResponse(ctx);
    }

    @Override
    public String visitResponseBlock(PALParser.ResponseBlockContext ctx) {
        return super.visitResponseBlock(ctx);
    }

    @Override
    public String visitResponseStmts(PALParser.ResponseStmtsContext ctx) {
        return super.visitResponseStmts(ctx);
    }

    @Override
    public String visitResponseStmt(PALParser.ResponseStmtContext ctx) {
        return super.visitResponseStmt(ctx);
    }

    @Override
    public String visitDeleteRuleStmt(PALParser.DeleteRuleStmtContext ctx) {
        return super.visitDeleteRuleStmt(ctx);
    }

    @Override
    public String visitCreateProhibitionStmt(PALParser.CreateProhibitionStmtContext ctx) {
        return super.visitCreateProhibitionStmt(ctx);
    }

    @Override
    public String visitProhibitionContainerList(PALParser.ProhibitionContainerListContext ctx) {
        return super.visitProhibitionContainerList(ctx);
    }

    @Override
    public String visitProhibitionContainerExpression(PALParser.ProhibitionContainerExpressionContext ctx) {
        return super.visitProhibitionContainerExpression(ctx);
    }

    @Override
    public String visitSetResourceAccessRightsStmt(PALParser.SetResourceAccessRightsStmtContext ctx) {
        return super.visitSetResourceAccessRightsStmt(ctx);
    }

    @Override
    public String visitVariableReference(PALParser.VariableReferenceContext ctx) {
        return super.visitVariableReference(ctx);
    }

    @Override
    public String visitFunctionCall(PALParser.FunctionCallContext ctx) {
        return super.visitFunctionCall(ctx);
    }

    @Override
    public String visitLiteralExpr(PALParser.LiteralExprContext ctx) {
        return super.visitLiteralExpr(ctx);
    }

    @Override
    public String visitArray(PALParser.ArrayContext ctx) {
        return super.visitArray(ctx);
    }

    @Override
    public String visitAccessRightArray(PALParser.AccessRightArrayContext ctx) {
        return super.visitAccessRightArray(ctx);
    }

    @Override
    public String visitAccessRight(PALParser.AccessRightContext ctx) {
        return super.visitAccessRight(ctx);
    }

    @Override
    public String visitMap(PALParser.MapContext ctx) {
        return super.visitMap(ctx);
    }

    @Override
    public String visitMapEntry(PALParser.MapEntryContext ctx) {
        return super.visitMapEntry(ctx);
    }

    @Override
    public String visitMapEntryRef(PALParser.MapEntryRefContext ctx) {
        return super.visitMapEntryRef(ctx);
    }

    @Override
    public String visitStringLiteral(PALParser.StringLiteralContext ctx) {
        return super.visitStringLiteral(ctx);
    }

    @Override
    public String visitBooleanLiteral(PALParser.BooleanLiteralContext ctx) {
        return super.visitBooleanLiteral(ctx);
    }

    @Override
    public String visitArrayLiteral(PALParser.ArrayLiteralContext ctx) {
        return super.visitArrayLiteral(ctx);
    }

    @Override
    public String visitMapLiteral(PALParser.MapLiteralContext ctx) {
        return super.visitMapLiteral(ctx);
    }

    @Override
    public String visitReferenceByID(PALParser.ReferenceByIDContext ctx) {
        return super.visitReferenceByID(ctx);
    }

    @Override
    public String visitMapEntryReference(PALParser.MapEntryReferenceContext ctx) {
        return super.visitMapEntryReference(ctx);
    }

    @Override
    public String visitFuncCall(PALParser.FuncCallContext ctx) {
        return super.visitFuncCall(ctx);
    }

    @Override
    public String visitFuncCallArgs(PALParser.FuncCallArgsContext ctx) {
        return super.visitFuncCallArgs(ctx);
    }

    private String formatNewLines(String text) {
        text = text.replaceAll("\\{", "\\{\n");
        text = text.replaceAll("}", "}\n");
        text = text.replaceAll(";", ";\n");
        return text;
    }

    private String formatIndents(String text) {
        StringBuilder formatted = new StringBuilder();
        int indentCount = 0;
        Scanner sc = new Scanner(text);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            if (line.endsWith("{")) {
                formatted.append(SPACES.repeat(indentCount));
                indentCount++;
            } else if (line.startsWith("}")){
                indentCount--;
                formatted.append(SPACES.repeat(indentCount));
            } else {
                formatted.append(SPACES.repeat(indentCount));
            }

            formatted.append(line).append("\n");
        }

        return formatted.toString();
    }
}
