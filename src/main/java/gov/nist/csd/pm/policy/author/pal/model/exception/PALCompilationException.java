package gov.nist.csd.pm.policy.author.pal.model.exception;

import gov.nist.csd.pm.policy.author.pal.compiler.error.CompileError;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;

public class PALCompilationException extends PMException {

    private final List<CompileError> errors;

    public PALCompilationException(List<CompileError> errors) {
        super(errors.toString());
        this.errors = errors;
    }

    public List<CompileError> getErrors() {
        return errors;
    }

    @Override
    public String getMessage() {
        StringBuilder s = new StringBuilder();
        for (CompileError e : errors) {
            s.append(e.toString()).append("\n");
        }
        return s.toString();
    }
}
