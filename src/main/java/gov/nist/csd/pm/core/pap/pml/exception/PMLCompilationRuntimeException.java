package gov.nist.csd.pm.core.pap.pml.exception;

import gov.nist.csd.pm.core.pap.pml.compiler.Position;
import gov.nist.csd.pm.core.pap.pml.compiler.error.CompileError;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class PMLCompilationRuntimeException extends RuntimeException {

    private List<CompileError> errors;

    public PMLCompilationRuntimeException(ParserRuleContext ctx, String message) {
        super(message);
        this.errors = List.of(CompileError.fromParserRuleContext(ctx, message));
    }

    public PMLCompilationRuntimeException(String message) {
        super(message);
        this.errors = new ArrayList<>();
        this.errors.add(new CompileError(new Position(0, 0, 0), message));
    }

    public PMLCompilationRuntimeException(List<CompileError> errors) {
        this.errors = errors;
    }

    public PMLCompilationRuntimeException(Throwable cause) {
        super(cause);
    }

    public List<CompileError> getErrors() {
        return errors;
    }
}
