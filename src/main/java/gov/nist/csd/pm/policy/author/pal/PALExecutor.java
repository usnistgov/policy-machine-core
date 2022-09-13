package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALLexer;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.compiler.error.ErrorLog;
import gov.nist.csd.pm.policy.author.pal.compiler.VisitorScope;
import gov.nist.csd.pm.policy.author.pal.compiler.visitor.PolicyVisitor;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.exception.PALCompilationException;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AdminAccessRights;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.antlr.v4.runtime.*;

import java.util.*;

import static gov.nist.csd.pm.pap.policies.SuperPolicy.*;
import static gov.nist.csd.pm.policy.author.pal.PALBuiltinFunctions.getBuiltinFunctions;

public class PALExecutor implements PALExecutable{

    private final PolicyAuthor policy;

    public PALExecutor(PolicyAuthor policy) {
        this.policy = policy;
    }

    @Override
    public List<PALStatement> compilePAL(String input) throws PMException {
        PALLexer lexer = new PALLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PALParser parser = new PALParser(tokens);

        ErrorLog errorLog = new ErrorLog();
        VisitorScope visitorScope = new VisitorScope(errorLog);

        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                errorLog.addError(line, charPositionInLine, offendingSymbol.toString().length(), msg);
            }
        });

        for (FunctionDefinitionStatement functionDefinitionStmt : getBuiltinFunctions()) {
            visitorScope.addFunction(functionDefinitionStmt);
        }

        for (String adminAccessRight : AdminAccessRights.ALL_ADMIN_ACCESS_RIGHTS_SET) {
            visitorScope.addVariable(adminAccessRight, Type.string(), true);
        }

        PALContext palContext = policy.pal().getContext();
        Map<String, FunctionDefinitionStatement> functions = palContext.getFunctions();
        for (FunctionDefinitionStatement customFunction : functions.values()) {
            visitorScope.addFunction(customFunction);
        }

        Map<String, Value> constants = palContext.getConstants();
        for (String constName : constants.keySet()) {
            visitorScope.addVariable(constName, constants.get(constName).getType(), true);
        }

        PolicyVisitor policyVisitor = new PolicyVisitor(new VisitorContext(visitorScope, errorLog));
        List<PALStatement> stmts = policyVisitor.visitPal(parser.pal());

        // throw an exception if there are any errors from parsing
        if (!errorLog.getErrors().isEmpty()) {
            throw new PALCompilationException(errorLog.getErrors());
        }

        return stmts;
    }

    @Override
    public void compileAndExecutePAL(UserContext author, String input) throws PMException {
        // compile the PAL into statements
        List<PALStatement> statements = compilePAL(input);

        // initialize the execution context
        ExecutionContext predefinedCtx = prepareExecutionCtx(author);
        ExecutionContext ctx = predefinedCtx.copy();

        // execute each statement
        for (PALStatement stmt : statements) {
            stmt.execute(ctx, policy);
        }

        // save any top level functions and constants to be used later
        saveTopLevelFunctionsAndConstants(predefinedCtx, ctx);
    }

    private void saveTopLevelFunctionsAndConstants(ExecutionContext predefinedCtx, ExecutionContext ctx) throws PMException {
        Map<String, FunctionDefinitionStatement> predefinedFunctions = predefinedCtx.getFunctions();
        Map<String, Value> predefinedConstants = predefinedCtx.getConstants();

        Map<String, FunctionDefinitionStatement> topLevelFunctions = ctx.getFunctions();
        for (String funcName : topLevelFunctions.keySet()) {
            if (predefinedFunctions.containsKey(funcName)) {
                continue;
            }

            FunctionDefinitionStatement funcDef = topLevelFunctions.get(funcName);
            policy.pal().addFunction(funcDef);
        }

        Map<String, Value> topLevelConstants = ctx.getConstants();
        for (String name : topLevelConstants.keySet()) {
            if (predefinedConstants.containsKey(name)) {
                continue;
            }

            Value value = topLevelConstants.get(name);
            policy.pal().addConstant(name, value);
        }
    }

    private ExecutionContext prepareExecutionCtx(UserContext author) throws PMException {
        ExecutionContext ctx = new ExecutionContext(author);

        // add builtin functions
        for (FunctionDefinitionStatement functionDefinitionStmt : getBuiltinFunctions()) {
            ctx.addFunction(functionDefinitionStmt);
        }

        // add admin access rights which are constants
        for (String adminAccessRight : AdminAccessRights.ALL_ADMIN_ACCESS_RIGHTS_SET) {
            ctx.addVariable(adminAccessRight, new Value(adminAccessRight), true);
        }

        // add super constants to ctx
        ctx.addVariable("SUPER_USER", new Value(SUPER_USER), true);
        ctx.addVariable("SUPER_PC", new Value(SUPER_PC), true);
        ctx.addVariable("SUPER_UA", new Value(SUPER_UA), true);
        ctx.addVariable("SUPER_OA", new Value(SUPER_OA), true);
        ctx.addVariable("SUPER_OBJECT", new Value(SUPER_OBJECT), true);

        PALContext palContext = policy.pal().getContext();
        Map<String, FunctionDefinitionStatement> customFunctions = palContext.getFunctions();
        for (FunctionDefinitionStatement customFunction : customFunctions.values()) {
            ctx.addFunction(customFunction);
        }

        Map<String, Value> customConstants = palContext.getConstants();
        for (String constName : customConstants.keySet()) {
            ctx.addVariable(constName, customConstants.get(constName), true);
        }

        return ctx;
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
        System.out.println(pal);
        return PALFormatter.format(pal);
    }
}
