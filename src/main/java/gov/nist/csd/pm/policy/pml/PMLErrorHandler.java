package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.pml.compiler.Position;
import gov.nist.csd.pm.policy.pml.compiler.error.CompileError;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

public class PMLErrorHandler extends BaseErrorListener {

    private final List<CompileError> errors = new ArrayList<>();

    public List<CompileError> getErrors() {
        return errors;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
                            String msg, RecognitionException e) {
        errors.add(new CompileError(new Position(line, charPositionInLine, 0), msg));
    }




}
