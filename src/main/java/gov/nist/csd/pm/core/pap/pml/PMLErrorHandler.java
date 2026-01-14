package gov.nist.csd.pm.core.pap.pml;

import gov.nist.csd.pm.core.pap.pml.compiler.Position;
import gov.nist.csd.pm.core.pap.pml.compiler.error.CompileError;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

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
