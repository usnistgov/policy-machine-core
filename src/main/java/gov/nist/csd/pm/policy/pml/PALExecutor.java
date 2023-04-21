package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.exception.PALExecutionException;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.scope.*;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.PALStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.model.scope.PALScopeException;

import java.util.*;

public class PALExecutor {

    public static void compileAndExecutePAL(PolicyAuthor policy, UserContext author, String input,
                                            FunctionDefinitionStatement ... customFunctions) throws PMException {
        // compile the PAL into statements
        List<PALStatement> compiledStatements = PALCompiler.compilePAL(policy, input, customFunctions);

        // initialize the execution context
        ExecutionContext ctx = new ExecutionContext(author);
        ctx.scope().loadFromPALContext(policy.getPALContext());

        ExecutionContext predefined;
        try {
            // add custom builtin functions to scope
            for (FunctionDefinitionStatement func : customFunctions) {
                ctx.scope().addFunction(func);
            }

            // store the predefined ctx to avoid adding again at the end of execution
            predefined = ctx.copy();
        } catch (PALScopeException e) {
            throw new PALExecutionException(e.getMessage());
        }

        // execute each statement
        for (PALStatement stmt : compiledStatements) {
            try {
                stmt.execute(ctx, policy);
            } catch (PMException e) {
                System.err.println(e.getMessage() + ": " + stmt);
                throw e;
            }
        }

        // save any top level functions and constants to be used later
        saveTopLevelFunctionsAndConstants(policy, predefined, ctx);
    }

    private static void saveTopLevelFunctionsAndConstants(PolicyAuthor policy, ExecutionContext predefinedCtx, ExecutionContext ctx) throws PMException {
        Map<String, FunctionDefinitionStatement> predefinedFunctions = predefinedCtx.scope().functions();
        Map<String, Value> predefinedConstants = predefinedCtx.scope().values();

        Map<String, FunctionDefinitionStatement> topLevelFunctions = ctx.scope().functions();
        for (String funcName : topLevelFunctions.keySet()) {
            if (predefinedFunctions.containsKey(funcName)) {
                continue;
            }

            FunctionDefinitionStatement funcDef = topLevelFunctions.get(funcName);
            policy.addPALFunction(funcDef);
        }

        Map<String, Value> topLevelConstants = ctx.scope().values();
        for (String name : topLevelConstants.keySet()) {
            if (predefinedConstants.containsKey(name)) {
                continue;
            }

            Value value = topLevelConstants.get(name);
            policy.addPALConstant(name, value);
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
}
