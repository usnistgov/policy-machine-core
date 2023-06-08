package gov.nist.csd.pm.pap.pml.scope;

import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.HashMap;
import java.util.Map;

import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.*;
import static gov.nist.csd.pm.pap.pml.executable.operation.PMLBuiltinOperations.builtinFunctions;

public class CompileGlobalScope extends GlobalScope<Variable, PMLExecutableSignature> {

    public CompileGlobalScope() {
        loadBuiltinConstantsAndFunctions();
    }

    public CompileGlobalScope(Map<String, Variable> constants, Map<String, PMLExecutableSignature> functions) {
        super(constants, functions);

        loadBuiltinConstantsAndFunctions();
    }

    private void loadBuiltinConstantsAndFunctions() {
        Map<String, Variable> builtinConstants = new HashMap<>();

        // admin policy nodes constants
        builtinConstants.put(ADMIN_POLICY.constantName(), new Variable(ADMIN_POLICY.constantName(), Type.string(), true));
        builtinConstants.put(ADMIN_POLICY_OBJECT.constantName(), new Variable(ADMIN_POLICY_OBJECT.constantName(), Type.string(), true));

        // add builtin operations
        addConstants(builtinConstants);

        Map<String, PMLOperation> funcs = builtinFunctions();
        for (Map.Entry<String, PMLOperation> func : funcs.entrySet()) {
            addExecutable(func.getKey(), func.getValue().getSignature());
        }
    }
}
