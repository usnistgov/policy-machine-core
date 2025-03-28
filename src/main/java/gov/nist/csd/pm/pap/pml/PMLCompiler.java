package gov.nist.csd.pm.pap.pml;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.antlr.PMLLexer;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.compiler.error.ErrorLog;
import gov.nist.csd.pm.pap.pml.compiler.visitor.PMLVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.List;

public class PMLCompiler {

    public PMLCompiler() {
    }

    public List<PMLStatement> compilePML(String input) throws PMException {
        Scope<Variable, PMLFunctionSignature> scope = new CompileScope();
        return compilePMLWithScope(scope, input);
    }

    public List<PMLStatement> compilePML(PAP pap, String input) throws PMException {
        Scope<Variable, PMLFunctionSignature> scope = new CompileScope(pap);
        return compilePMLWithScope(scope, input);
    }

    private List<PMLStatement> compilePMLWithScope(Scope<Variable, PMLFunctionSignature> scope, String input) throws PMException {
        PMLErrorHandler pmlErrorHandler = new PMLErrorHandler();

        PMLLexer lexer = new PMLLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(pmlErrorHandler);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(pmlErrorHandler);

        // check for syntax errors
        ErrorLog errorLog = new ErrorLog();
        PMLVisitor pmlVisitor = new PMLVisitor(new VisitorContext(tokens, scope, errorLog, pmlErrorHandler));
        PMLParser.PmlContext pmlCtx = parser.pml();
        if (!pmlErrorHandler.getErrors().isEmpty()) {
            throw new PMLCompilationException(pmlErrorHandler.getErrors());
        }

        // compile
        List<PMLStatement> compiled = pmlVisitor.visitPml(pmlCtx);

        // check for errors encountered during compilation
        if (!errorLog.getErrors().isEmpty()) {
            throw new PMLCompilationException(errorLog.getErrors());
        }

        return compiled;
    }
}
