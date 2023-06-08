package gov.nist.csd.pm.pap.pml.compiler.error;

import gov.nist.csd.pm.pap.pml.compiler.Position;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import java.util.Objects;

public record CompileError(Position position, String errorMessage) {

    public static String getText(ParserRuleContext ctx) {
        int startIndex = ctx.start.getStartIndex();
        int stopIndex = ctx.stop.getStopIndex();
        Interval interval = new Interval(startIndex, stopIndex);
        return ctx.start.getInputStream().getText(interval);
    }

    public static CompileError fromParserRuleContext(ParserRuleContext ctx, String message) {
        return new CompileError(
                new Position(ctx),
                message
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompileError that = (CompileError) o;
        return Objects.equals(position, that.position) && Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, errorMessage);
    }

    @Override
    public String toString() {
        return "CompileError{" +
                "position=" + position +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
