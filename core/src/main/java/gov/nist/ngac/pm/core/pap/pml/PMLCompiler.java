/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLLexer;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.error.ErrorLog;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.PMLVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * Lexes, parses, and compiles a PML source string into its list of top-level statements.
 */
public class PMLCompiler {

    public PMLCompiler() {
    }

    /**
     * Compiles the given PML source against a fresh {@link CompileScope} built from the PAP's current
     * policy state.
     *
     * @throws PMException if compilation fails
     */
    public List<PMLStatement<?>> compilePML(PAP pap, String input) throws PMException {
        CompileScope scope = new CompileScope(pap);
        return compilePMLWithScope(scope, input);
    }

    /**
     * Compiles the given PML source against the provided scope, letting the caller seed or reuse a scope
     * rather than building a fresh one from the PAP.
     *
     * @throws PMException if compilation fails
     */
    public List<PMLStatement<?>> compilePML(PAP pap, CompileScope scope, String input) throws PMException {
        return compilePMLWithScope(scope, input);
    }

    private List<PMLStatement<?>> compilePMLWithScope(CompileScope scope, String input) throws PMException {
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
        List<PMLStatement<?>> compiled = pmlVisitor.visitPml(pmlCtx);

        // check for errors encountered during compilation
        if (!errorLog.getErrors().isEmpty()) {
            throw new PMLCompilationException(errorLog.getErrors());
        }

        return compiled;
    }
}
