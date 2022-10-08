package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALLexer;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import java.util.List;
import java.util.Scanner;

public class PALFormatter extends PALBaseVisitor<String> {

    private static final String SPACES = "    ";
    public static final String NEW_LINE = "\n";

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

        String formatted = new PALFormatter().visitPal(parser.pal());

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
    public String visitVarStmt(PALParser.VarStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitFuncDefStmt(PALParser.FuncDefStmtContext ctx) {
        String text = getText(ctx);
        int stmtStartIndex = ctx.start.getStartIndex();
        PALParser.FuncBodyContext funcBodyCtx = ctx.funcBody();
        int openCurlyIndex = funcBodyCtx.OPEN_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        int closeCurlyIndex = funcBodyCtx.CLOSE_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        String signature = indent() + (text.substring(0, openCurlyIndex + 1).trim()) + NEW_LINE;

        String body = visitStmts(funcBodyCtx.stmt());

        String close = "\n" + indent() + text.substring(closeCurlyIndex).trim() + NEW_LINE;

        return signature + body + close;
    }

    private String visitStmts(List<PALParser.StmtContext> stmts) {
        indentLevel++;
        StringBuilder body = new StringBuilder();
        for (PALParser.StmtContext stmtCtx : stmts) {
            body.append(visitStmt(stmtCtx));
        }
        indentLevel--;

        return body.toString();
    }

    @Override
    public String visitFuncReturnStmt(PALParser.FuncReturnStmtContext ctx) {
        return formatStmt(ctx);
    }
    
    @Override
    public String visitForeachStmt(PALParser.ForeachStmtContext ctx) {
        String text = getText(ctx);
        int stmtStartIndex = ctx.start.getStartIndex();
        PALParser.StmtBlockContext forStmtsCtx = ctx.stmtBlock();
        int openCurlyIndex = forStmtsCtx.OPEN_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        int closeCurlyIndex = forStmtsCtx.CLOSE_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        String forloop = indent() + (text.substring(0, openCurlyIndex + 1).trim()) + NEW_LINE;

        String body = visitStmts(ctx.stmtBlock().stmt());

        String close = "\n" + indent() + text.substring(closeCurlyIndex).trim() + NEW_LINE;

        return forloop + body + close;
    }

    @Override
    public String visitBreakStmt(PALParser.BreakStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitContinueStmt(PALParser.ContinueStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitFuncCallStmt(PALParser.FuncCallStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitIfStmt(PALParser.IfStmtContext ctx) {
        String text = getText(ctx);
        int startIndex = ctx.start.getStartIndex();
        int openCurlyIndex = (ctx.stmtBlock().OPEN_CURLY().getSymbol().getStartIndex()) - startIndex;
        String ifStr = indent() + text.substring(0, openCurlyIndex).trim();

        String ifStmtBlock = visitStmtBlock(ctx.stmtBlock());

        StringBuilder elseIfStmtBlock = new StringBuilder();
        for (PALParser.ElseIfStmtContext elseIfStmtCtx : ctx.elseIfStmt()) {
            elseIfStmtBlock.append(visitElseIfStmt(elseIfStmtCtx));
        }

        String elseStmtBlock = visitElseStmt(ctx.elseStmt());

        return ifStr + ifStmtBlock + elseIfStmtBlock + elseStmtBlock;
    }

    @Override
    public String visitElseIfStmt(PALParser.ElseIfStmtContext ctx) {
        if (ctx == null) {
            return "";
        }

        String text = getText(ctx);
        int startIndex = ctx.start.getStartIndex();
        int openCurlyIndex = (ctx.stmtBlock().OPEN_CURLY().getSymbol().getStartIndex()) - startIndex;
        String ifStr = " " + text.substring(0, openCurlyIndex).trim();

        String stmtBlock = visitStmtBlock(ctx.stmtBlock());

        return ifStr + stmtBlock;
    }

    @Override
    public String visitElseStmt(PALParser.ElseStmtContext ctx) {
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
    public String visitStmtBlock(PALParser.StmtBlockContext ctx) {
        StringBuilder block = new StringBuilder();

        indentLevel++;
        for (PALParser.StmtContext stmtCtx : ctx.stmt()) {
            block.append(visitStmt(stmtCtx));
        }
        indentLevel--;

        return "{" + NEW_LINE + block + indent() + "}";
    }

    @Override
    public String visitCreatePolicyStmt(PALParser.CreatePolicyStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitCreateAttrStmt(PALParser.CreateAttrStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitCreateUserOrObjectStmt(PALParser.CreateUserOrObjectStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitSetNodePropsStmt(PALParser.SetNodePropsStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitAssignStmt(PALParser.AssignStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitDeassignStmt(PALParser.DeassignStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitAssociateStmt(PALParser.AssociateStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitDissociateStmt(PALParser.DissociateStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitDeleteStmt(PALParser.DeleteStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitCreateObligationStmt(PALParser.CreateObligationStmtContext ctx) {
        String text = getText(ctx);
        int stmtStartIndex = ctx.start.getStartIndex();
        int openCurlyIndex = ctx.OPEN_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        int closeCurlyIndex = ctx.CLOSE_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        String create = indent() + (text.substring(0, openCurlyIndex + 1).trim()) + NEW_LINE;

        StringBuilder body = new StringBuilder();
        List<PALParser.CreateRuleStmtContext> createRuleStmts = ctx.createRuleStmt();
        indentLevel++;
        for (PALParser.CreateRuleStmtContext createRuleStmtCtx : createRuleStmts) {
            body.append(visitCreateRuleStmt(createRuleStmtCtx));
        }
        indentLevel--;

        String close = "\n" + indent() + text.substring(closeCurlyIndex).trim() + NEW_LINE;

        return create + body + close;
    }

    @Override
    public String visitCreateRuleStmt(PALParser.CreateRuleStmtContext ctx) {
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
    public String visitResponse(PALParser.ResponseContext ctx) {
        String text = getText(ctx);

        int start = ctx.start.getStartIndex();
        int closeParenIndex = (ctx.DO().getSymbol().getStopIndex() + 1) - start;
        String doStr = indent() + text.substring(0, closeParenIndex).trim() + " ";

        String response = visitResponseBlock(ctx.responseBlock());

        return doStr + response;
    }

    @Override
    public String visitResponseBlock(PALParser.ResponseBlockContext ctx) {
        indentLevel++;
        String responseBlock = visitResponseStmts(ctx.responseStmt());
        indentLevel--;

        return "{" + NEW_LINE + responseBlock + indent() + "}" + NEW_LINE;
    }

    public String visitResponseStmts(List<PALParser.ResponseStmtContext> ctx) {
        StringBuilder stmts = new StringBuilder();
        for (PALParser.ResponseStmtContext stmtCtx : ctx) {
            stmts.append(visitResponseStmt(stmtCtx)).append(NEW_LINE);
        }
        return stmts.toString();
    }

    @Override
    public String visitResponseStmt(PALParser.ResponseStmtContext ctx) {
        if (ctx.stmt() != null) {
            return visitStmt(ctx.stmt());
        } else if (ctx.createRuleStmt() != null){
            return visitCreateRuleStmt(ctx.createRuleStmt());
        } else if (ctx.deleteRuleStmt() != null) {
            return visitDeleteRuleStmt(ctx.deleteRuleStmt());
        } else {
            return visitEventSpecificResponse(ctx.eventSpecificResponse());
        }
    }

    @Override
    public String visitDeleteRuleStmt(PALParser.DeleteRuleStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitCreateProhibitionStmt(PALParser.CreateProhibitionStmtContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitSetResourceAccessRightsStmt(PALParser.SetResourceAccessRightsStmtContext ctx) {
        return formatStmt(ctx);
    }
    @Override
    public String visitEventSpecificResponse(PALParser.EventSpecificResponseContext ctx) {
        String text = getText(ctx);
        int stmtStartIndex = ctx.start.getStartIndex();
        int openCurlyIndex = ctx.OPEN_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        int closeCurlyIndex = ctx.CLOSE_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        String event = indent() + (text.substring(0, openCurlyIndex + 1).trim()) + NEW_LINE;

        indentLevel++;
        String body = visitResponseStmts(ctx.responseStmt());
        indentLevel--;

        String close = "\n" + indent() + text.substring(closeCurlyIndex).trim() + NEW_LINE;

        return event + body + close;


        //return event + " {" + NEW_LINE + responseBlock + indent() + "}" + NEW_LINE;
    }
}
