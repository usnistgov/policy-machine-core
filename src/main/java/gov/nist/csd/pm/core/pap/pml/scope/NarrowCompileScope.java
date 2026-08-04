package gov.nist.csd.pm.core.pap.pml.scope;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperations;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.builtin.PMLBuiltinOperations;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Scope} used to recompile a single already-persisted PML definition (an operation or an obligation)
 * read back from a {@code Store}, without the cost/recursion risk of {@link CompileScope}'s eager
 * {@code pap.query().operations().getOperations()} symbol-table seed: a {@code Store}'s read path for a
 * PML-kind row drives this scope, and if that scope itself called {@code getOperations()} to seed, it would
 * recurse straight back into every other stored PML row.
 * <p>
 * A persisted definition was already valid PML when it was first created, so it doesn't need eager
 * re-validation against every sibling operation. Only the fixed, cheap sets (builtins, protected admin
 * operations) are seeded eagerly; any other name referenced in the body (a cross-reference to another
 * user-defined operation/function) is resolved lazily, one name at a time, via {@code pap.query().operations()}
 * only when actually looked up.
 */
public class NarrowCompileScope extends Scope<Variable, PMLOperationSignature> {

    private enum Visibility {
        ALL,
        FUNCTIONS_AND_QUERIES,
        FUNCTIONS_ONLY
    }

    private final Visibility visibility;

    public NarrowCompileScope(PAP pap) {
        this(pap, ScopeSeeds.loadConstants(), new HashMap<>(), seedOperations(), null, Visibility.ALL);
    }

    private NarrowCompileScope(PAP pap,
                               Map<String, Variable> constants,
                               Map<String, Variable> variables,
                               Map<String, PMLOperationSignature> operations,
                               Scope<Variable, PMLOperationSignature> parentScope,
                               Visibility visibility) {
        super(pap, constants, variables, operations, parentScope);
        this.visibility = visibility;
    }

    private static Map<String, PMLOperationSignature> seedOperations() {
        Map<String, PMLOperationSignature> operationSignatures = new HashMap<>();

        for (Operation<?> op : PMLBuiltinOperations.builtinOperations().values()) {
            operationSignatures.put(op.getName(), ScopeSeeds.createOperationSignature(op));
        }

        for (Operation<?> op : AdminOperations.ADMIN_OPERATIONS) {
            operationSignatures.put(op.getName(), ScopeSeeds.createOperationSignature(op));
        }

        return operationSignatures;
    }

    @Override
    public PMLOperationSignature getOperation(String name) throws UnknownOperationInScopeException {
        PMLOperationSignature signature = getOperations().get(name);
        if (signature != null) {
            return signature;
        }

        signature = resolveFromPap(name);
        if (signature == null) {
            throw new UnknownOperationInScopeException(name);
        }

        getOperations().put(name, signature);
        return signature;
    }

    @Override
    public boolean operationExists(String name) {
        return getOperations().containsKey(name) || resolveFromPap(name) != null;
    }

    private PMLOperationSignature resolveFromPap(String name) {
        try {
            Operation<?> op = getPap().query().operations().getOperation(name);
            PMLOperationSignature signature = ScopeSeeds.createOperationSignature(op);
            return isVisible(signature) ? signature : null;
        } catch (PMException e) {
            return null;
        }
    }

    @Override
    public NarrowCompileScope copy() {
        return new NarrowCompileScope(
            getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            new HashMap<>(getOperations()),
            getParentScope() != null ? getParentScope().copy() : null,
            visibility
        );
    }

    @Override
    public NarrowCompileScope copyFunctionsOnly() {
        return restrictedCopy(Visibility.FUNCTIONS_ONLY);
    }

    @Override
    public NarrowCompileScope copyFunctionsAndQueriesOnly() {
        return restrictedCopy(Visibility.FUNCTIONS_AND_QUERIES);
    }

    private NarrowCompileScope restrictedCopy(Visibility newVisibility) {
        Map<String, PMLOperationSignature> filtered = new HashMap<>();
        for (PMLOperationSignature signature : getOperations().values()) {
            if (isVisibleUnder(newVisibility, signature)) {
                filtered.put(signature.getName(), signature);
            }
        }

        return new NarrowCompileScope(
            getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            filtered,
            getParentScope() != null ? getParentScope().copy() : null,
            newVisibility
        );
    }

    private boolean isVisible(PMLOperationSignature signature) {
        return isVisibleUnder(visibility, signature);
    }

    private static boolean isVisibleUnder(Visibility v, PMLOperationSignature signature) {
        return switch (v) {
            case ALL -> true;
            case FUNCTIONS_AND_QUERIES -> ScopeSeeds.isFunctionOrQuery(signature);
            case FUNCTIONS_ONLY -> ScopeSeeds.isFunction(signature);
        };
    }
}
