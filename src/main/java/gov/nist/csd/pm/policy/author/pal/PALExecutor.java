package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALLexer;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.compiler.error.ErrorLog;
import gov.nist.csd.pm.policy.author.pal.compiler.visitor.PolicyVisitor;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.exception.PALCompilationException;
import gov.nist.csd.pm.policy.author.pal.model.exception.PALExecutionException;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.scope.*;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.antlr.v4.runtime.*;

import java.util.*;

import static gov.nist.csd.pm.policy.author.pal.PALBuiltinFunctions.BUILTIN_FUNCTIONS;

public class PALExecutor implements PALExecutable{

    private final PolicyAuthor policy;

    public PALExecutor(PolicyAuthor policy) {
        this.policy = policy;
    }

    @Override
    public List<PALStatement> compilePAL(String input, FunctionDefinitionStatement ... customBuiltinFunctions) throws PMException {
        ErrorLog errorLog = new ErrorLog();
        Scope scope = new Scope(Scope.Mode.COMPILE);
        scope.loadFromPALContext(policy.pal().getContext());

        // add custom builtin functions to scope
        for (FunctionDefinitionStatement func : customBuiltinFunctions) {
            try {
                scope.addFunction(func);
            } catch (FunctionAlreadyDefinedInScopeException e) {
                errorLog.addError(0, 0, 0, e.getMessage());
            }
        }

        PALLexer lexer = new PALLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PALParser parser = new PALParser(tokens);
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                errorLog.addError(line, charPositionInLine, offendingSymbol.toString().length(), msg);
            }
        });

        PolicyVisitor policyVisitor = new PolicyVisitor(new VisitorContext(scope, errorLog));
        List<PALStatement> stmts = new ArrayList<>();
        try {
            stmts = policyVisitor.visitPal(parser.pal());
        } catch (Exception e) {
            errorLog.addError(parser.pal(), e.getMessage());
        }

        // throw an exception if there are any errors from parsing
        if (!errorLog.getErrors().isEmpty()) {
            throw new PALCompilationException(errorLog);
        }

        return stmts;
    }

    @Override
    public void compileAndExecutePAL(UserContext author, String input, FunctionDefinitionStatement ... customBuiltinFunctions) throws PMException {
        // compile the PAL into statements
        List<PALStatement> compiledStatements = compilePAL(input);

        // initialize the execution context
        ExecutionContext ctx = new ExecutionContext(author);
        ctx.scope().loadFromPALContext(policy.pal().getContext());

        ExecutionContext predefined;
        try {
            // add custom builtin functions to scope
            for (FunctionDefinitionStatement func : customBuiltinFunctions) {
                ctx.scope().addFunction(func);
            }

            // store the predefined ctx to avoid adding again at the end of execution
            predefined = ctx.copy();
        } catch (PALScopeException e) {
            throw new PALExecutionException(e.getMessage());
        }

        // execute each statement
        for (PALStatement stmt : compiledStatements) {
            stmt.execute(ctx, policy);
        }

        // save any top level functions and constants to be used later
        saveTopLevelFunctionsAndConstants(predefined, ctx);
    }

    private void saveTopLevelFunctionsAndConstants(ExecutionContext predefinedCtx, ExecutionContext ctx) throws PMException {
        Map<String, FunctionDefinitionStatement> predefinedFunctions = predefinedCtx.scope().functions();
        Map<String, Value> predefinedConstants = predefinedCtx.scope().values();

        Map<String, FunctionDefinitionStatement> topLevelFunctions = ctx.scope().functions();
        for (String funcName : topLevelFunctions.keySet()) {
            if (predefinedFunctions.containsKey(funcName)) {
                continue;
            }

            FunctionDefinitionStatement funcDef = topLevelFunctions.get(funcName);
            policy.pal().addFunction(funcDef);
        }

        Map<String, Value> topLevelConstants = ctx.scope().values();
        for (String name : topLevelConstants.keySet()) {
            if (predefinedConstants.containsKey(name)) {
                continue;
            }

            Value value = topLevelConstants.get(name);
            policy.pal().addConstant(name, value);
        }
    }

    public static Value executeStatementBlock(ExecutionContext executionCtx, PolicyAuthor policyAuthor, List<PALStatement> statements) throws PMException {
        for (PALStatement statement : statements) {
            Value value = statement.execute(executionCtx, policyAuthor);
            if (value.isReturn() || value.isBreak() || value.isContinue()) {
                return value;
            }
        }

        return new Value();
    }

    @Override
    public String toPAL() throws PMException {
        String pal = new PALSerializer(policy).toPAL();
        return PALFormatter.format(pal);
    }
}
