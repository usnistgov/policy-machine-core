package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLLexer;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
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

    public static String format(String pml) {
        PMLLexer lexer = new PMLLexer(CharStreams.fromString(pml));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);

        String formatted = new PMLFormatter().visitPml(parser.pml());

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

    public static String statementsToString(List<? extends PMLStatement> stmts) {
        StringBuilder s = new StringBuilder();

        for (PMLStatement stmt : stmts) {
            s.append(stmt);
        }

        return s.toString();
    }

    @Override
    public String visitPml(PMLParser.PmlContext ctx) {
        StringBuilder s = new StringBuilder();
        for (PMLParser.StatementContext stmtCtx : ctx.statement()) {
            s.append(visitStatement(stmtCtx));
        }
        return s.toString();
    }

    @Override
    public String visitStatement(PMLParser.StatementContext ctx) {
        if (ctx.variableDeclarationStatement() != null) {
            return visitVarStmt(ctx.variableDeclarationStatement());
        } else if (ctx.functionDefinitionStatement() != null) {
            return visitFuncDefStmt(ctx.functionDefinitionStatement());
        } else if (ctx.foreachStatement() != null) {
            return visitForeachStmt(ctx.foreachStatement());
        } else if (ctx.functionInvokeStatement() != null) {
            return visitFuncCallStmt(ctx.functionInvokeStatement());
        } else if (ctx.ifStatement() != null) {
            return visitIfStmt(ctx.ifStatement());
        } else if (ctx.createAttributeStatement() != null) {
            return visitCreateAttributeStatement(ctx.createAttributeStatement());
        } else if (ctx.createPolicyStatement() != null) {
            return visitCreatePolicyStatement(ctx.createPolicyStatement());
        } else if (ctx.createUserOrObjectStatement() != null) {
            return visitCreateUserOrObjectStatement(ctx.createUserOrObjectStatement());
        } else if (ctx.createProhibitionStatement() != null) {
            return visitCreateProhibitionStatement(ctx.createProhibitionStatement());
        } else if (ctx.createObligationStatement() != null) {
            return visitCreateObligationStatement(ctx.createObligationStatement());
        } else if (ctx.setNodePropertiesStatement() != null) {
            return visitSetNodePropertiesStatement(ctx.setNodePropertiesStatement());
        } else if (ctx.assignStatement() != null) {
            return visitAssignStatement(ctx.assignStatement());
        } else if (ctx.deassignStatement() != null) {
            return visitDeassignStatement(ctx.deassignStatement());
        } else if (ctx.deleteStatement() != null) {
            return visitDeleteStatement(ctx.deleteStatement());
        } else if (ctx.associateStatement() != null) {
            return visitAssociateStatement(ctx.associateStatement());
        } else if (ctx.dissociateStatement() != null) {
            return visitDissociateStatement(ctx.dissociateStatement());
        } else if (ctx.functionReturnStatement() != null) {
            return visitFuncReturnStmt(ctx.functionReturnStatement());
        } else if (ctx.breakStatement() != null) {
            return visitBreakStmt(ctx.breakStatement());
        } else if (ctx.continueStatement() != null) {
            return visitContinueStmt(ctx.continueStatement());
        } else if (ctx.setResourceAccessRightsStatement() != null) {
            return visitSetResourceAccessRightsStatement(ctx.setResourceAccessRightsStatement());
        } else if (ctx.deleteRuleStatement() != null) {
            return visitDeleteRuleStatement(ctx.deleteRuleStatement());
        }

        return getText(ctx);
    }

    private String formatStmt(ParserRuleContext ctx) {
        String text = getText(ctx);
        return indent() + text + NEW_LINE;
    }

    public String visitVarStmt(PMLParser.VariableDeclarationStatementContext ctx) {
        return formatStmt(ctx);
    }

    public String visitFuncDefStmt(PMLParser.FunctionDefinitionStatementContext ctx) {
        String text = getText(ctx);
        int stmtStartIndex = ctx.start.getStartIndex();
        PMLParser.FuncBodyContext funcBodyCtx = ctx.funcBody();
        int openCurlyIndex = funcBodyCtx.OPEN_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        int closeCurlyIndex = funcBodyCtx.CLOSE_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        String signature = indent() + (text.substring(0, openCurlyIndex + 1).trim()) + NEW_LINE;

        String body = visitStatements(funcBodyCtx.statement());

        String close = "\n" + indent() + text.substring(closeCurlyIndex).trim() + NEW_LINE;

        return signature + body + close;
    }

    private String visitStatements(List<PMLParser.StatementContext> stmts) {
        indentLevel++;
        StringBuilder body = new StringBuilder();
        for (PMLParser.StatementContext stmtCtx : stmts) {
            body.append(visitStatement(stmtCtx));
        }
        indentLevel--;

        return body.toString();
    }

    public String visitFuncReturnStmt(PMLParser.FunctionReturnStatementContext ctx) {
        return formatStmt(ctx);
    }
    
    public String visitForeachStmt(PMLParser.ForeachStatementContext ctx) {
        String text = getText(ctx);
        int stmtStartIndex = ctx.start.getStartIndex();
        PMLParser.StatementBlockContext forStmtsCtx = ctx.statementBlock();
        int openCurlyIndex = forStmtsCtx.OPEN_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        int closeCurlyIndex = forStmtsCtx.CLOSE_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        String forloop = indent() + (text.substring(0, openCurlyIndex + 1).trim()) + NEW_LINE;

        String body = visitStatements(ctx.statementBlock().statement());

        String close = "\n" + indent() + text.substring(closeCurlyIndex).trim() + NEW_LINE;

        return forloop + body + close;
    }

    public String visitBreakStmt(PMLParser.BreakStatementContext ctx) {
        return formatStmt(ctx);
    }

    public String visitContinueStmt(PMLParser.ContinueStatementContext ctx) {
        return formatStmt(ctx);
    }

    public String visitFuncCallStmt(PMLParser.FunctionInvokeStatementContext ctx) {
        return formatStmt(ctx);
    }

    public String visitIfStmt(PMLParser.IfStatementContext ctx) {
        String text = getText(ctx);
        int startIndex = ctx.start.getStartIndex();
        int openCurlyIndex = (ctx.statementBlock().OPEN_CURLY().getSymbol().getStartIndex()) - startIndex;
        String ifStr = indent() + text.substring(0, openCurlyIndex).trim() + " ";

        String ifStmtBlock = visitStatementBlock(ctx.statementBlock());

        StringBuilder elseIfStmtBlock = new StringBuilder();
        for (PMLParser.ElseIfStatementContext elseIfStmtCtx : ctx.elseIfStatement()) {
            elseIfStmtBlock.append(visitElseIfStatement(elseIfStmtCtx));
        }

        String elseStmtBlock = visitElseStatement(ctx.elseStatement());

        return ifStr + ifStmtBlock + elseIfStmtBlock + elseStmtBlock;
    }

    @Override
    public String visitElseIfStatement(PMLParser.ElseIfStatementContext ctx) {
        if (ctx == null) {
            return "";
        }

        String text = getText(ctx);
        int startIndex = ctx.start.getStartIndex();
        int openCurlyIndex = (ctx.statementBlock().OPEN_CURLY().getSymbol().getStartIndex()) - startIndex;
        String ifStr = " " + text.substring(0, openCurlyIndex).trim() + " ";

        String stmtBlock = visitStatementBlock(ctx.statementBlock());

        return ifStr + stmtBlock;
    }

    @Override
    public String visitElseStatement(PMLParser.ElseStatementContext ctx) {
        if (ctx == null) {
            return "";
        }

        String text = getText(ctx);
        int startIndex = ctx.start.getStartIndex();
        int openCurlyIndex = (ctx.statementBlock().OPEN_CURLY().getSymbol().getStartIndex()) - startIndex;
        String elseStr = " " + text.substring(0, openCurlyIndex).trim();

        String stmtBlock = visitStatementBlock(ctx.statementBlock());

        return elseStr + stmtBlock + NEW_LINE;
    }

    @Override
    public String visitStatementBlock(PMLParser.StatementBlockContext ctx) {
        StringBuilder block = new StringBuilder();

        indentLevel++;
        for (PMLParser.StatementContext stmtCtx : ctx.statement()) {
            block.append(visitStatement(stmtCtx));
        }
        indentLevel--;

        return "{" + NEW_LINE + block + indent() + "}";
    }

    @Override
    public String visitCreatePolicyStatement(PMLParser.CreatePolicyStatementContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitCreateAttributeStatement(PMLParser.CreateAttributeStatementContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitCreateUserOrObjectStatement(PMLParser.CreateUserOrObjectStatementContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitSetNodePropertiesStatement(PMLParser.SetNodePropertiesStatementContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitAssignStatement(PMLParser.AssignStatementContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitDeassignStatement(PMLParser.DeassignStatementContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitAssociateStatement(PMLParser.AssociateStatementContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitDissociateStatement(PMLParser.DissociateStatementContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitDeleteStatement(PMLParser.DeleteStatementContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitCreateObligationStatement(PMLParser.CreateObligationStatementContext ctx) {
        String text = getText(ctx);
        int stmtStartIndex = ctx.start.getStartIndex();
        int openCurlyIndex = ctx.OPEN_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        int closeCurlyIndex = ctx.CLOSE_CURLY().getSymbol().getStartIndex() - stmtStartIndex;
        String create = indent() + (text.substring(0, openCurlyIndex + 1).trim()) + NEW_LINE;

        StringBuilder body = new StringBuilder();
        List<PMLParser.CreateRuleStatementContext> createRuleStmts = ctx.createRuleStatement();
        indentLevel++;
        for (PMLParser.CreateRuleStatementContext createRuleStmtCtx : createRuleStmts) {
            body.append(visitCreateRuleStatement(createRuleStmtCtx));
        }
        indentLevel--;

        String close = "\n" + indent() + text.substring(closeCurlyIndex).trim() + NEW_LINE;

        return create + body + close;
    }

    @Override
    public String visitCreateRuleStatement(PMLParser.CreateRuleStatementContext ctx) {
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
        String responseBlock = visitResponseStatements(ctx.responseStatement());
        indentLevel--;

        return "{" + NEW_LINE + responseBlock + indent() + "}" + NEW_LINE;
    }

    public String visitResponseStatements(List<PMLParser.ResponseStatementContext> ctx) {
        StringBuilder stmts = new StringBuilder();
        for (PMLParser.ResponseStatementContext stmtCtx : ctx) {
            stmts.append(visitResponseStatement(stmtCtx)).append(NEW_LINE);
        }
        return stmts.toString();
    }

    @Override
    public String visitResponseStatement(PMLParser.ResponseStatementContext ctx) {
        if (ctx.statement() != null) {
            return visitStatement(ctx.statement());
        } else if (ctx.createRuleStatement() != null){
            return visitCreateRuleStatement(ctx.createRuleStatement());
        } else {
            return visitDeleteRuleStatement(ctx.deleteRuleStatement());
        }
    }

    @Override
    public String visitDeleteRuleStatement(PMLParser.DeleteRuleStatementContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitCreateProhibitionStatement(PMLParser.CreateProhibitionStatementContext ctx) {
        return formatStmt(ctx);
    }

    @Override
    public String visitSetResourceAccessRightsStatement(PMLParser.SetResourceAccessRightsStatementContext ctx) {
        return formatStmt(ctx);
    }
}
