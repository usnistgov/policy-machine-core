package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.antlr.PMLLexer;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.compiler.error.ErrorLog;
import gov.nist.csd.pm.policy.pml.compiler.visitor.PMLVisitor;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.exception.PMLCompilationException;
import gov.nist.csd.pm.policy.pml.model.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.Scope;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.antlr.v4.runtime.*;

import java.util.ArrayList;
import java.util.List;

import static org.antlr.v4.runtime.Lexer.HIDDEN;

public class PMLCompiler {

    public static List<PMLStatement> compilePML(Policy policy, String input, FunctionDefinitionStatement... customBuiltinFunctions) throws PMException {
        ErrorLog errorLog = new ErrorLog();
        Scope scope = new Scope(Scope.Mode.COMPILE);
        scope.loadFromPMLContext(PMLContext.fromPolicy(policy));

        // add custom builtin functions to scope
        for (FunctionDefinitionStatement func : customBuiltinFunctions) {
            try {
                scope.addFunction(func);
            } catch (FunctionAlreadyDefinedInScopeException e) {
                errorLog.addError(0, 0, 0, e.getMessage());
            }
        }

        PMLErrorHandler pmlErrorHandler = new PMLErrorHandler();

        PMLLexer lexer = new PMLLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(pmlErrorHandler);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(pmlErrorHandler);

        PMLVisitor pmlVisitor = new PMLVisitor(new VisitorContext(tokens, scope, errorLog));
        List<PMLStatement> stmts = new ArrayList<>();
        try {
            stmts = pmlVisitor.visitPml(parser.pml());
        } catch (Exception e) {
            errorLog.addError(parser.pml(), e.getMessage());
        }

        errorLog.addErrors(pmlErrorHandler.getErrors());

        // throw an exception if there are any errors from parsing
        if (!errorLog.getErrors().isEmpty()) {
            throw new PMLCompilationException(errorLog);
        }

        return stmts;
    }
}
