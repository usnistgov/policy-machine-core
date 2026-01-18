package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;

import gov.nist.csd.pm.core.pap.function.Function;
import java.util.HashMap;
import java.util.Map;

public class PMLBuiltinFunctions {

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

    private static final Map<String, Function<?>> BUILTIN_FUNCTIONS = new HashMap<>();

    static {
        BUILTIN_FUNCTIONS.put(contains.getName(), contains);
        BUILTIN_FUNCTIONS.put(containsKey.getName(), containsKey);
        BUILTIN_FUNCTIONS.put(appendAll.getName(), appendAll);
        BUILTIN_FUNCTIONS.put(append.getName(), append);
        BUILTIN_FUNCTIONS.put(env.getName(), env);

        BUILTIN_FUNCTIONS.put(getAssociationsWithSource.getName(), getAssociationsWithSource);
        BUILTIN_FUNCTIONS.put(getAssociationsWithTarget.getName(), getAssociationsWithTarget);
        BUILTIN_FUNCTIONS.put(getAdjacentAscendants.getName(), getAdjacentAscendants);
        BUILTIN_FUNCTIONS.put(getAdjacentDescendants.getName(), getAdjacentDescendants);
        BUILTIN_FUNCTIONS.put(getNodeProperties.getName(), getNodeProperties);
        BUILTIN_FUNCTIONS.put(getNodeType.getName(), getNodeType);
        BUILTIN_FUNCTIONS.put(hasPropertyKey.getName(), hasPropertyKey);
        BUILTIN_FUNCTIONS.put(hasPropertyValue.getName(), hasPropertyValue);
        BUILTIN_FUNCTIONS.put(nodeExists.getName(), nodeExists);
        BUILTIN_FUNCTIONS.put(getNode.getName(), getNode);
        BUILTIN_FUNCTIONS.put(search.getName(), search);
        BUILTIN_FUNCTIONS.put(name.getName(), name);
        BUILTIN_FUNCTIONS.put(id.getName(), id);
    }

    public static Map<String, Function<?>> builtinFunctions() {
        return new HashMap<>(BUILTIN_FUNCTIONS);
    }

    public static boolean isBuiltinFunction(String functionName) {
        return BUILTIN_FUNCTIONS.containsKey(functionName);
    }

    private PMLBuiltinFunctions() {}
}
