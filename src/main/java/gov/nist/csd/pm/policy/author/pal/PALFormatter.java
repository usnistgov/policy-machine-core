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

    private PALFormatter() {

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
        String text = getText(ctx);
        text = formatNewLines(text);
        text = formatIndents(text);
        return text;
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
