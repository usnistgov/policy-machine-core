package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLLexer;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.statement.PALStatement;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import java.util.List;
import java.util.Scanner;

public class PMLFormatter extends PMLBaseVisitor<String> {

    private static final String SPACES = "    ";
    public static final String NEW_LINE = "\n";

    private int indentLevel;

    private PMLFormatter() {
        indentLevel = 0;
    }

    private String indent() {
        return SPACES.repeat(indentLevel);
    }

    public static String format(String pal) {
        PMLLexer lexer = new PMLLexer(CharStreams.fromString(pal));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);

        String formatted = new PMLFormatter().visitPal(parser.pal());

        return removeEmptyLines(formatted);
    }

    private static String removeEmptyLines(String formatted) {
        StringBuilder ret = new StringBuilder();
        Scanner sc = new Scanner(formatted);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.isEmpty()) {
                continue;
            }

            ret.append(line).append(NEW_LINE);
        }

        return ret.toString();
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
    public String visitPal(PMLParser.PalContext ctx) {
        StringBuilder s = new StringBuilder();
        for (PMLParser.StmtContext stmtCtx : ctx.stmt()) {
            s.append(visitStmt(stmtCtx));
        }
        return s.toString();
    }

    @Override
    public String visitStmt(PMLParser.StmtContext ctx) {
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
        } else if (ctx.createAttrStmt() != null) {
            return visitCreateAttrStmt(ctx.createAttrStmt());
        } else if (ctx.createPolicyStmt() != null) {
            return visitCreatePolicyStmt(ctx.createPolicyStmt());
        } else if (ctx.createUserOrObjectStmt() != null) {
            return visitCreateUserOrObjectStmt(ctx.createUserOrObjectStmt());
        } else if (ctx.createProhibitionStmt() != null) {
            return visitCreateProhibitionStmt(ctx.createProhibitionStmt());
        } else if (ctx.createObligationStmt() != null) {
            return visitCreateObligationStmt(ctx.createObligationStmt());
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

    private String formatStmt(ParserRuleContext ctx) {
        String text = getText(ctx);
        return indent() + text + NEW_LINE;
    }

    @Override
    public String visitVarStmt(PMLParser.VarStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitFuncDefStmt(PMLParser.FuncDefStmtContext ctx) {
        String text = getText(ctx);
        int stmtStartIndex = ctx.start.getStartIndex();
        PMLParser.FuncBodyContext funcBodyCtx = ctx.funcBody();
        int openCurlyIndex = funcBodyCtx.OPEN_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        int closeCurlyIndex = funcBodyCtx.CLOSE_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        String signature = indent() + (text.substring(0, openCurlyIndex + 1).trim()) + NEW_LINE;

        String body = visitStmts(funcBodyCtx.stmt());

        String close = "\n" + indent() + text.substring(closeCurlyIndex).trim() + NEW_LINE;

        return signature + body + close;
    }

    private String visitStmts(List<PMLParser.StmtContext> stmts) {
        indentLevel++;
        StringBuilder body = new StringBuilder();
        for (PMLParser.StmtContext stmtCtx : stmts) {
            body.append(visitStmt(stmtCtx));
        }
        indentLevel--;

        return body.toString();
    }

    @Override
    public String visitFuncReturnStmt(PMLParser.FuncReturnStmtContext ctx) {
        return formatStmt(ctx);
    }
    
    @Override
    public String visitForeachStmt(PMLParser.ForeachStmtContext ctx) {
        String text = getText(ctx);
        int stmtStartIndex = ctx.start.getStartIndex();
        PMLParser.StmtBlockContext forStmtsCtx = ctx.stmtBlock();
        int openCurlyIndex = forStmtsCtx.OPEN_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        int closeCurlyIndex = forStmtsCtx.CLOSE_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        String forloop = indent() + (text.substring(0, openCurlyIndex + 1).trim()) + NEW_LINE;

        String body = visitStmts(ctx.stmtBlock().stmt());

        String close = "\n" + indent() + text.substring(closeCurlyIndex).trim() + NEW_LINE;

        return forloop + body + close;
    }

    @Override
    public String visitBreakStmt(PMLParser.BreakStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitContinueStmt(PMLParser.ContinueStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitFuncCallStmt(PMLParser.FuncCallStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitIfStmt(PMLParser.IfStmtContext ctx) {
        String text = getText(ctx);
        int startIndex = ctx.start.getStartIndex();
        int openCurlyIndex = (ctx.stmtBlock().OPEN_CURLY().getSymbol().getStartIndex()) - startIndex;
        String ifStr = indent() + text.substring(0, openCurlyIndex).trim() + " ";

        String ifStmtBlock = visitStmtBlock(ctx.stmtBlock());

        StringBuilder elseIfStmtBlock = new StringBuilder();
        for (PMLParser.ElseIfStmtContext elseIfStmtCtx : ctx.elseIfStmt()) {
            elseIfStmtBlock.append(visitElseIfStmt(elseIfStmtCtx));
        }

        String elseStmtBlock = visitElseStmt(ctx.elseStmt());

        return ifStr + ifStmtBlock + elseIfStmtBlock + elseStmtBlock;
    }

    @Override
    public String visitElseIfStmt(PMLParser.ElseIfStmtContext ctx) {
        if (ctx == null) {
            return "";
        }

        String text = getText(ctx);
        int startIndex = ctx.start.getStartIndex();
        int openCurlyIndex = (ctx.stmtBlock().OPEN_CURLY().getSymbol().getStartIndex()) - startIndex;
        String ifStr = " " + text.substring(0, openCurlyIndex).trim() + " ";

        String stmtBlock = visitStmtBlock(ctx.stmtBlock());

        return ifStr + stmtBlock;
    }

    @Override
    public String visitElseStmt(PMLParser.ElseStmtContext ctx) {
        if (ctx == null) {
            return "";
        }

        String text = getText(ctx);
        int startIndex = ctx.start.getStartIndex();
        int openCurlyIndex = (ctx.stmtBlock().OPEN_CURLY().getSymbol().getStartIndex()) - startIndex;
        String elseStr = " " + text.substring(0, openCurlyIndex).trim();

        String stmtBlock = visitStmtBlock(ctx.stmtBlock());

        return elseStr + stmtBlock + NEW_LINE;
    }

    @Override
    public String visitStmtBlock(PMLParser.StmtBlockContext ctx) {
        StringBuilder block = new StringBuilder();

        indentLevel++;
        for (PMLParser.StmtContext stmtCtx : ctx.stmt()) {
            block.append(visitStmt(stmtCtx));
        }
        indentLevel--;

        return "{" + NEW_LINE + block + indent() + "}";
    }

    @Override
    public String visitCreatePolicyStmt(PMLParser.CreatePolicyStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitCreateAttrStmt(PMLParser.CreateAttrStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitCreateUserOrObjectStmt(PMLParser.CreateUserOrObjectStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitSetNodePropsStmt(PMLParser.SetNodePropsStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitAssignStmt(PMLParser.AssignStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitDeassignStmt(PMLParser.DeassignStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitAssociateStmt(PMLParser.AssociateStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitDissociateStmt(PMLParser.DissociateStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitDeleteStmt(PMLParser.DeleteStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitCreateObligationStmt(PMLParser.CreateObligationStmtContext ctx) {
        String text = getText(ctx);
        int stmtStartIndex = ctx.start.getStartIndex();
        int openCurlyIndex = ctx.OPEN_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        int closeCurlyIndex = ctx.CLOSE_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        String create = indent() + (text.substring(0, openCurlyIndex + 1).trim()) + NEW_LINE;

        StringBuilder body = new StringBuilder();
        List<PMLParser.CreateRuleStmtContext> createRuleStmts = ctx.createRuleStmt();
        indentLevel++;
        for (PMLParser.CreateRuleStmtContext createRuleStmtCtx : createRuleStmts) {
            body.append(visitCreateRuleStmt(createRuleStmtCtx));
        }
        indentLevel--;

        String close = "\n" + indent() + text.substring(closeCurlyIndex).trim() + NEW_LINE;

        return create + body + close;
    }

    @Override
    public String visitCreateRuleStmt(PMLParser.CreateRuleStmtContext ctx) {
        String text = getText(ctx);
        int stmtStartIndex = ctx.start.getStartIndex();
        int whenIndex = ctx.WHEN().getSymbol().getStartIndex() - stmtStartIndex;
        String create = indent() + (text.substring(0, whenIndex).trim()) + NEW_LINE;

        int performsIndex = ctx.PERFORMS().getSymbol().getStartIndex() - stmtStartIndex;
        String when = indent() + (text.substring(whenIndex, performsIndex).trim()) + NEW_LINE;

        int doIndex = ctx.response().DO().getSymbol().getStartIndex() - stmtStartIndex;
        String performs = "";
        String on = "";
        int performsEndIndex = 0;
        if (ctx.ON() != null) {
            int onIndex = ctx.ON().getSymbol().getStartIndex() - stmtStartIndex;
            performsEndIndex = onIndex;
            on = indent() + (text.substring(onIndex, doIndex).trim()) + NEW_LINE;
        } else {
            performsEndIndex = doIndex;
        }

        performs = indent() + (text.substring(performsIndex, performsEndIndex).trim()) + NEW_LINE;

        String response = visitResponse(ctx.response());

        return create + when + performs + on + response;
    }

    @Override
    public String visitResponse(PMLParser.ResponseContext ctx) {
        String text = getText(ctx);

        int start = ctx.start.getStartIndex();
        int closeParenIndex = (ctx.CLOSE_PAREN().getSymbol().getStopIndex() + 1) - start;
        String doStr = indent() + text.substring(0, closeParenIndex).trim() + " ";

        String response = visitResponseBlock(ctx.responseBlock());

        return doStr + response;
    }

    @Override
    public String visitResponseBlock(PMLParser.ResponseBlockContext ctx) {
        indentLevel++;
        String responseBlock = visitResponseStmts(ctx.responseStmt());
        indentLevel--;

        return "{" + NEW_LINE + responseBlock + indent() + "}" + NEW_LINE;
    }

    public String visitResponseStmts(List<PMLParser.ResponseStmtContext> ctx) {
        StringBuilder stmts = new StringBuilder();
        for (PMLParser.ResponseStmtContext stmtCtx : ctx) {
            stmts.append(visitResponseStmt(stmtCtx)).append(NEW_LINE);
        }
        return stmts.toString();
    }

    @Override
    public String visitResponseStmt(PMLParser.ResponseStmtContext ctx) {
        if (ctx.stmt() != null) {
            return visitStmt(ctx.stmt());
        } else if (ctx.createRuleStmt() != null){
            return visitCreateRuleStmt(ctx.createRuleStmt());
        } else {
            return visitDeleteRuleStmt(ctx.deleteRuleStmt());
        }
    }

    @Override
    public String visitDeleteRuleStmt(PMLParser.DeleteRuleStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitCreateProhibitionStmt(PMLParser.CreateProhibitionStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitSetResourceAccessRightsStmt(PMLParser.SetResourceAccessRightsStmtContext ctx) {
        return formatStmt(ctx);
    }
}
