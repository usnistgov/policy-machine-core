package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.pml.function.builtin.*;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.HashMap;
import java.util.Map;

public class PMLBuiltinFunctions {

    // util functions
    private static final FunctionDefinitionStatement concat = new Concat();
    private static final FunctionDefinitionStatement equals = new Equals();
    private static final FunctionDefinitionStatement contains = new Contains();
    private static final FunctionDefinitionStatement containsKey = new ContainsKey();

    // policy functions
    private static final FunctionDefinitionStatement getAssociationsWithSource = new GetAssociationsWithSource();
    private static final FunctionDefinitionStatement getAssociationsWithTarget = new GetAssociationsWithTarget();
    private static final FunctionDefinitionStatement getChildren = new GetChildren();
    private static final FunctionDefinitionStatement getParents = new GetParents();
    private static final FunctionDefinitionStatement getNodeProperties = new GetNodeProperties();
    private static final FunctionDefinitionStatement getNodeType = new GetNodeType();
    private static final FunctionDefinitionStatement getProhibitionsFor = new GetProhibitionsFor();
    private static final FunctionDefinitionStatement hasPropertyKey = new HasPropertyKey();
    private static final FunctionDefinitionStatement hasPropertyValue = new HasPropertyValue();
    private static final FunctionDefinitionStatement nodeExists = new NodeExists();
    private static final FunctionDefinitionStatement getNode = new GetNode();
    private static final FunctionDefinitionStatement search = new Search();
    private static final FunctionDefinitionStatement append = new Append();
    private static final FunctionDefinitionStatement appendAll = new AppendAll();

    private static final Map<String, FunctionDefinitionStatement> BUILTIN_FUNCTIONS = new HashMap<>();

    static {
        BUILTIN_FUNCTIONS.put(concat.signature().getFunctionName(), concat);
        BUILTIN_FUNCTIONS.put(equals.signature().getFunctionName(), equals);
        BUILTIN_FUNCTIONS.put(contains.signature().getFunctionName(), contains);
        BUILTIN_FUNCTIONS.put(containsKey.signature().getFunctionName(), containsKey);

        BUILTIN_FUNCTIONS.put(getAssociationsWithSource.signature().getFunctionName(), getAssociationsWithSource);
        BUILTIN_FUNCTIONS.put(getAssociationsWithTarget.signature().getFunctionName(), getAssociationsWithTarget);
        BUILTIN_FUNCTIONS.put(getChildren.signature().getFunctionName(), getChildren);
        BUILTIN_FUNCTIONS.put(getParents.signature().getFunctionName(), getParents);
        BUILTIN_FUNCTIONS.put(getNodeProperties.signature().getFunctionName(), getNodeProperties);
        BUILTIN_FUNCTIONS.put(getNodeType.signature().getFunctionName(), getNodeType);
        BUILTIN_FUNCTIONS.put(getProhibitionsFor.signature().getFunctionName(), getProhibitionsFor);
        BUILTIN_FUNCTIONS.put(hasPropertyKey.signature().getFunctionName(), hasPropertyKey);
        BUILTIN_FUNCTIONS.put(hasPropertyValue.signature().getFunctionName(), hasPropertyValue);
        BUILTIN_FUNCTIONS.put(nodeExists.signature().getFunctionName(), nodeExists);
        BUILTIN_FUNCTIONS.put(getNode.signature().getFunctionName(), getNode);
        BUILTIN_FUNCTIONS.put(search.signature().getFunctionName(), search);
        BUILTIN_FUNCTIONS.put(append.signature().getFunctionName(), append);
        BUILTIN_FUNCTIONS.put(appendAll.signature().getFunctionName(), appendAll);
    }

    public static Map<String, FunctionDefinitionStatement> builtinFunctions() {
        return new HashMap<>(BUILTIN_FUNCTIONS);
    }

    public static boolean isBuiltinFunction(String functionName) {
        return BUILTIN_FUNCTIONS.containsKey(functionName);
    }

    private PMLBuiltinFunctions() {}
}
