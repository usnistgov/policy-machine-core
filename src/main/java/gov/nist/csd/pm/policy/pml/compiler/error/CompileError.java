package gov.nist.csd.pm.policy.pml.compiler.error;

import gov.nist.csd.pm.policy.pml.PMLFormatter;
import gov.nist.csd.pm.policy.pml.compiler.Position;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

public record CompileError(Position position, String errorMessage) {

    public static String getText(ParserRuleContext ctx) {
        return PMLFormatter.getText(ctx);
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
}
