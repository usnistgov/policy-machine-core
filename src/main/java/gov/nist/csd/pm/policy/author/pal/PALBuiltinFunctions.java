package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.policy.author.pal.function.*;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;

import java.util.HashMap;
import java.util.Map;

public class PALBuiltinFunctions {

    // util functions
    private static final FunctionDefinitionStatement concat = new Concat();
    private static final FunctionDefinitionStatement equals = new Equals();
    private static final FunctionDefinitionStatement contains = new Contains();
    private static final FunctionDefinitionStatement containsKey = new ContainsKey();
    private static final FunctionDefinitionStatement numToStr = new NumToStr();

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

    public static final Map<String, FunctionDefinitionStatement> BUILTIN_FUNCTIONS = new HashMap<>();

    static {
        BUILTIN_FUNCTIONS.put(concat.getFunctionName(), concat);
        BUILTIN_FUNCTIONS.put(equals.getFunctionName(), equals);
        BUILTIN_FUNCTIONS.put(contains.getFunctionName(), contains);
        BUILTIN_FUNCTIONS.put(containsKey.getFunctionName(), containsKey);
        BUILTIN_FUNCTIONS.put(numToStr.getFunctionName(), numToStr);

        BUILTIN_FUNCTIONS.put(getAssociationsWithSource.getFunctionName(), getAssociationsWithSource);
        BUILTIN_FUNCTIONS.put(getAssociationsWithTarget.getFunctionName(), getAssociationsWithTarget);
        BUILTIN_FUNCTIONS.put(getChildren.getFunctionName(), getChildren);
        BUILTIN_FUNCTIONS.put(getParents.getFunctionName(), getParents);
        BUILTIN_FUNCTIONS.put(getNodeProperties.getFunctionName(), getNodeProperties);
        BUILTIN_FUNCTIONS.put(getNodeType.getFunctionName(), getNodeType);
        BUILTIN_FUNCTIONS.put(getProhibitionsFor.getFunctionName(), getProhibitionsFor);
        BUILTIN_FUNCTIONS.put(hasPropertyKey.getFunctionName(), hasPropertyKey);
        BUILTIN_FUNCTIONS.put(hasPropertyValue.getFunctionName(), hasPropertyValue);
        BUILTIN_FUNCTIONS.put(nodeExists.getFunctionName(), nodeExists);
        BUILTIN_FUNCTIONS.put(getNode.getFunctionName(), getNode);
        BUILTIN_FUNCTIONS.put(search.getFunctionName(), search);
    }

    public static boolean isBuiltinFunction(String functionName) {
        return BUILTIN_FUNCTIONS.containsKey(functionName);
    }

}
