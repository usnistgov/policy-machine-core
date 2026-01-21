package gov.nist.csd.pm.core.pap.pml.operation.builtin;

import gov.nist.csd.pm.core.pap.operation.Operation;
import java.util.HashMap;
import java.util.Map;

public class PMLBuiltinOperations {

    // util operations
    private static final Contains contains = new Contains();
    private static final ContainsKey containsKey = new ContainsKey();
    private static final Env env = new Env();

    // policy operations
    private static final GetAssociationsWithSource getAssociationsWithSource = new GetAssociationsWithSource();
    private static final GetAssociationsWithTarget getAssociationsWithTarget = new GetAssociationsWithTarget();
    private static final GetAdjacentAscendants getAdjacentAscendants = new GetAdjacentAscendants();
    private static final GetAdjacentDescendants getAdjacentDescendants = new GetAdjacentDescendants();
    private static final GetNodeProperties getNodeProperties = new GetNodeProperties();
    private static final GetNodeType getNodeType = new GetNodeType();
    private static final HasPropertyKey hasPropertyKey = new HasPropertyKey();
    private static final HasPropertyValue hasPropertyValue = new HasPropertyValue();
    private static final NodeExists nodeExists = new NodeExists();
    private static final GetNode getNode = new GetNode();
    private static final Search search = new Search();
    private static final Append append = new Append();
    private static final AppendAll appendAll = new AppendAll();
    private static final Name name = new Name();
    private static final Id id = new Id();

    private static final Map<String, Operation<?>> BUILTIN_OPERATIONS = new HashMap<>();

    static {
        BUILTIN_OPERATIONS.put(contains.getName(), contains);
        BUILTIN_OPERATIONS.put(containsKey.getName(), containsKey);
        BUILTIN_OPERATIONS.put(appendAll.getName(), appendAll);
        BUILTIN_OPERATIONS.put(append.getName(), append);
        BUILTIN_OPERATIONS.put(env.getName(), env);

        BUILTIN_OPERATIONS.put(getAssociationsWithSource.getName(), getAssociationsWithSource);
        BUILTIN_OPERATIONS.put(getAssociationsWithTarget.getName(), getAssociationsWithTarget);
        BUILTIN_OPERATIONS.put(getAdjacentAscendants.getName(), getAdjacentAscendants);
        BUILTIN_OPERATIONS.put(getAdjacentDescendants.getName(), getAdjacentDescendants);
        BUILTIN_OPERATIONS.put(getNodeProperties.getName(), getNodeProperties);
        BUILTIN_OPERATIONS.put(getNodeType.getName(), getNodeType);
        BUILTIN_OPERATIONS.put(hasPropertyKey.getName(), hasPropertyKey);
        BUILTIN_OPERATIONS.put(hasPropertyValue.getName(), hasPropertyValue);
        BUILTIN_OPERATIONS.put(nodeExists.getName(), nodeExists);
        BUILTIN_OPERATIONS.put(getNode.getName(), getNode);
        BUILTIN_OPERATIONS.put(search.getName(), search);
        BUILTIN_OPERATIONS.put(name.getName(), name);
        BUILTIN_OPERATIONS.put(id.getName(), id);
    }

    public static Map<String, Operation<?>> builtinOperations() {
        return new HashMap<>(BUILTIN_OPERATIONS);
    }

    public static boolean isBuiltinOperation(String operationName) {
        return BUILTIN_OPERATIONS.containsKey(operationName);
    }

    private PMLBuiltinOperations() {}
}
