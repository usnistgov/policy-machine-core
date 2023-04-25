package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.antlr.PMLLexer;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.compiler.error.ErrorLog;
import gov.nist.csd.pm.policy.pml.compiler.visitor.PolicyVisitor;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.exception.PMLCompilationException;
import gov.nist.csd.pm.policy.pml.model.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.Scope;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import org.antlr.v4.runtime.*;

import java.util.ArrayList;
import java.util.List;

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

        PMLLexer lexer = new PMLLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                errorLog.addError(line, charPositionInLine, offendingSymbol.toString().length(), msg);
            }
        });

        PolicyVisitor policyVisitor = new PolicyVisitor(new VisitorContext(scope, errorLog));
        List<PMLStatement> stmts = new ArrayList<>();
        try {
            stmts = policyVisitor.visitPml(parser.pml());
        } catch (Exception e) {
            errorLog.addError(parser.pml(), e.getMessage());
        }

        // throw an exception if there are any errors from parsing
        if (!errorLog.getErrors().isEmpty()) {
            throw new PMLCompilationException(errorLog);
        }

        return stmts;
    }

}
