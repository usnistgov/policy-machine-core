package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.epp.events.*;
import gov.nist.csd.pm.epp.functions.*;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.*;

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

    public FunctionExecutor getFunctionExecutor(String name) throws PMException {
        if (!funExecs.containsKey(name)) {
            throw new PMException(name + " is not a recognized function");
        }
        return funExecs.get(name);
    }

    public boolean evalBool(EventContext eventCtx, String user, String process, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);

        return (boolean)functionExecutor.exec(eventCtx, user, process, pdp, function, this);
    }

    public List evalNodeList(EventContext eventCtx, String user, String process, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (List) functionExecutor.exec(eventCtx, user, process, pdp, function, this);
    }

    public Node evalNode(EventContext eventCtx, String user, String process, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (Node)functionExecutor.exec(eventCtx, user, process, pdp, function, this);
    }

    public String evalString(EventContext eventCtx, String user, String process, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (String)functionExecutor.exec(eventCtx, user, process, pdp, function, this);
    }

    public String evalLong(EventContext eventCtx, String user, String process, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (String)functionExecutor.exec(eventCtx, user, process, pdp, function, this);
    }

    public Map evalMap(EventContext eventCtx, String user, String process, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (Map)functionExecutor.exec(eventCtx, user, process, pdp, function, this);
    }

    public Object evalObject(EventContext eventCtx, String user, String process, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return functionExecutor.exec(eventCtx, user, process, pdp, function, this);
    }
}
