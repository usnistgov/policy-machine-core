package gov.nist.csd.pm.policy.author.pal.compiler.error;

import gov.nist.csd.pm.policy.author.pal.compiler.Position;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

public record CompileError(Position position, String errorMessage) {

    public static String getText(ParserRuleContext ctx) {
        int startIndex = ctx.start.getStartIndex();
        int stopIndex = ctx.stop.getStopIndex();
        Interval interval = new Interval(startIndex, stopIndex);
        return interval.toString();
    }

    public static CompileError fromParserRuleContext(ParserRuleContext ctx, String message) {
        return new CompileError(
                new Position(
                        ctx.start.getLine(),
                        ctx.start.getStartIndex(),
                        ctx.stop.getStopIndex()
                ),
                message
        );
    }

}
