package gov.nist.csd.pm.policy.author.pal.compiler.error;

import gov.nist.csd.pm.policy.author.pal.compiler.Position;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;


public class ErrorLog {

    private final List<CompileError> errors;

    public ErrorLog() {
        this.errors = new ArrayList<>();
    }

    public ErrorLog addError(ParserRuleContext ctx, String message) {
        this.errors.add(CompileError.fromParserRuleContext(ctx, message));
        return this;
    }

    public ErrorLog addError(int line, int charPos, int end, String msg) {
        this.errors.add(new CompileError(new Position(line, charPos, end), msg));
        return this;
    }

    public List<CompileError> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("errors: \n");
        for (CompileError error : errors) {
            s.append(error.toString()).append("\n");
        }
        return s.toString();
    }
}
