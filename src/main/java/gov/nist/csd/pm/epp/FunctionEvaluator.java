package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.epp.events.*;
import gov.nist.csd.pm.epp.functions.*;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
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

    public boolean evalBool(UserContext obligationUser, EventContext eventCtx, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);

        return (boolean)functionExecutor.exec(obligationUser, eventCtx, pdp, function, this);
    }

    public List evalNodeList(UserContext obligationUser, EventContext eventCtx, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (List) functionExecutor.exec(obligationUser, eventCtx, pdp, function, this);
    }

    public Node evalNode(UserContext obligationUser, EventContext eventCtx, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (Node)functionExecutor.exec(obligationUser, eventCtx, pdp, function, this);
    }

    public String evalString(UserContext obligationUser, EventContext eventCtx, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (String)functionExecutor.exec(obligationUser, eventCtx, pdp, function, this);
    }

    public String evalLong(UserContext obligationUser, EventContext eventCtx, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (String)functionExecutor.exec(obligationUser, eventCtx, pdp, function, this);
    }

    public Map evalMap(UserContext obligationUser, EventContext eventCtx, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return (Map)functionExecutor.exec(obligationUser, eventCtx, pdp, function, this);
    }

    public Object evalObject(UserContext obligationUser, EventContext eventCtx, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = getFunctionExecutor(functionName);
        return functionExecutor.exec(obligationUser, eventCtx, pdp, function, this);
    }
}
