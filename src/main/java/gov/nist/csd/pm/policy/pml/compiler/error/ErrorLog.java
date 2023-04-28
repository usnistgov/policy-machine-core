package gov.nist.csd.pm.policy.pml.compiler.error;

import gov.nist.csd.pm.policy.pml.compiler.Position;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class ErrorLog {

    private final List<CompileError> errors;

    public ErrorLog() {
        this.errors = new ArrayList<>();
    }

    public ErrorLog addError(ParserRuleContext ctx, String message) {
        CompileError compileError = CompileError.fromParserRuleContext(ctx, message);

        addError(compileError);

        return this;
    }

    public ErrorLog addError(int line, int charPos, int end, String msg) {
        CompileError compileError = new CompileError(new Position(line, charPos, end), msg);

        addError(compileError);

        return this;
    }

    private void addError(CompileError error) {
        if (this.errors.contains(error)) {
            return;
        }

        this.errors.add(error);
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
