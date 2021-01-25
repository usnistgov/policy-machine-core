package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.epp.functions.*;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionEvaluator {

    private Map<String, FunctionExecutor> funExecs;

    public FunctionEvaluator() throws PMException {
        funExecs = new HashMap<>();

        // add the build in functions
        addFunctionExecutor(new ChildOfAssignExecutor());
        addFunctionExecutor(new CreateNodeExecutor());
        addFunctionExecutor(new CurrentProcessExecutor());
        addFunctionExecutor(new CurrentTargetExecutor());
        addFunctionExecutor(new CurrentUserExecutor());
        addFunctionExecutor(new GetChildrenExecutor());
        addFunctionExecutor(new GetNodeExecutor());
        addFunctionExecutor(new GetNodeNameExecutor());
        addFunctionExecutor(new IsNodeContainedInExecutor());
        addFunctionExecutor(new ParentOfAssignExecutor());
        addFunctionExecutor(new ToPropertiesExecutor());
    }

    public void addFunctionExecutor(FunctionExecutor executor) {
        this.funExecs.put(executor.getFunctionName(), executor);
    }

    public void removeFunctionExecutor(FunctionExecutor executor) {
        this.funExecs.remove(executor.getFunctionName());
    }

    public FunctionExecutor getFunctionExecutor(String name) throws PMException {
        if (!funExecs.containsKey(name)) {
            throw new PMException(name + " is not a recognized function");
        }
        return funExecs.get(name);
    }

    public boolean evalBool(Graph graph, Prohibitions prohibitions, Obligations obligations,
                            EventContext eventCtx, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);

        return (boolean)functionExecutor.exec(graph, prohibitions, obligations, eventCtx, function, this);
    }

    public List evalNodeList(Graph graph, Prohibitions prohibitions, Obligations obligations,
                             EventContext eventCtx, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (List) functionExecutor.exec(graph, prohibitions, obligations, eventCtx, function, this);
    }

    public Node evalNode(Graph graph, Prohibitions prohibitions, Obligations obligations,
                         EventContext eventCtx, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (Node)functionExecutor.exec(graph, prohibitions, obligations, eventCtx, function, this);
    }

    public String evalString(Graph graph, Prohibitions prohibitions, Obligations obligations,
                             EventContext eventCtx, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (String)functionExecutor.exec(graph, prohibitions, obligations, eventCtx, function, this);
    }

    public String evalLong(Graph graph, Prohibitions prohibitions, Obligations obligations,
                           EventContext eventCtx, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (String)functionExecutor.exec(graph, prohibitions, obligations, eventCtx, function, this);
    }

    public Map evalMap(Graph graph, Prohibitions prohibitions, Obligations obligations,
                       EventContext eventCtx, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (Map)functionExecutor.exec(graph, prohibitions, obligations, eventCtx, function, this);
    }

    public Object evalObject(Graph graph, Prohibitions prohibitions, Obligations obligations,
                             EventContext eventCtx, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return functionExecutor.exec(graph, prohibitions, obligations, eventCtx, function, this);
    }
}
