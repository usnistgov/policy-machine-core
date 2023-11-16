package gov.nist.csd.pm.policy.pml.exception;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.compiler.error.CompileError;
import gov.nist.csd.pm.policy.pml.compiler.error.ErrorLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PMLCompilationException extends PMException {

    private final List<CompileError> errors;

    public PMLCompilationException(ErrorLog errorLog) {
        super(errorLog.toString());
        this.errors = new ArrayList<>(errorLog.getErrors());
    }

    public PMLCompilationException(CompileError error) {
        super(error.errorMessage());
        this.errors = Arrays.asList(error);
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
