package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AdminAccessRights;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.scope.Scope;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminPolicyNode.*;
import static gov.nist.csd.pm.pap.AdminPolicyNode.OBLIGATIONS_TARGET;
import static gov.nist.csd.pm.policy.pml.statement.PMLStatement.execute;

public class PMLExecutor {

    public static void compileAndExecutePML(Policy policy, UserContext author, String input,
                                            FunctionDefinitionStatement ... customFunctions) throws PMException {
        // compile the PML into statements
        CompiledPML compiledPML = PMLCompiler.compilePML(policy, input, customFunctions);

        // build a global scope from the policy
        GlobalScope<Value, FunctionDefinitionStatement> globalScope = GlobalScope.withValuesAndDefinitions(policy, customFunctions);

        // evaluate the constants and functions from the compiled PML
        Map<String, Value> persistedConstants = evaluateConstantStmts(policy, compiledPML.constants());

        // add constants and functions to policy
        Map<String, Value> udConstants = policy.userDefinedPML().getConstants();
        for (Map.Entry<String, Value> c : persistedConstants.entrySet()) {
            // if a persisted constant from the PML has the same name as a user defined constant, overwrite the user defined constant
            if (udConstants.containsKey(c.getKey())) {
                policy.userDefinedPML().deleteConstant(c.getKey());
            }

            policy.userDefinedPML().createConstant(c.getKey(), c.getValue());
        }

        Map<String, FunctionDefinitionStatement> udFunctions = policy.userDefinedPML().getFunctions();
        for (Map.Entry<String, FunctionDefinitionStatement> f : compiledPML.functions().entrySet()) {
            // if a persisted constant from the PML has the same name as a user defined constant, overwrite the user defined constant
            if (udFunctions.containsKey(f.getKey())) {
                policy.userDefinedPML().deleteFunction(f.getKey());
            }

            policy.userDefinedPML().createFunction(f.getValue());
        }

        // add the constants and functions to the persisted scope
        globalScope.getPersistedConstants().putAll(persistedConstants);
        globalScope.getPersistedFunctions().putAll(compiledPML.functions());

        // execute other statements
        ExecutionContext ctx = new ExecutionContext(author, new Scope<>(globalScope));

        for (PMLStatement pmlStatement : compiledPML.stmts()) {
            PMLStatement.execute(ctx, policy, pmlStatement);
        }
    }

    private static Map<String, Value> evaluateConstantStmts(Policy policy, Map<String, Expression> constants)
            throws PMException {
        Map<String, Value> constantsMap = new HashMap<>();

        // create empty exec ctx for constant value evaluation as all constants are literals
        ExecutionContext ctx = new ExecutionContext(new UserContext(), new Scope<>(GlobalScope.withValuesAndDefinitions(policy)));

        for (Map.Entry<String, Expression> e : constants.entrySet()) {
            constantsMap.put(e.getKey(), execute(ctx, policy, e.getValue()));
        }

        return constantsMap;
    }


    public static Value executeStatementBlock(ExecutionContext executionCtx, Policy policy, List<PMLStatement> statements) throws PMException {
        for (PMLStatement statement : statements) {
            Value value = execute(executionCtx, policy, statement);
            if (value instanceof ReturnValue || value instanceof BreakValue || value instanceof ContinueValue) {
                return value;
            }
        }

        return new VoidValue();
    }
}
