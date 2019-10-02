package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.epp.events.*;
import gov.nist.csd.pm.epp.functions.FunctionExecutor;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.evr.EVRException;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class FunctionEvaluator {

    private Map<String, FunctionExecutor> funExecs;

    public FunctionEvaluator() throws PMException {
        funExecs = new HashMap<>();

        Reflections reflections = new Reflections("");
        Set<Class<? extends FunctionExecutor>> classes = reflections.getSubTypesOf(FunctionExecutor.class);
        for (Class c : classes) {
            Constructor constructor = null;
            try {
                constructor = c.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new PMException("invalid function definition in " + c.getCanonicalName() + ". Expected a default constructor.");
            }

            Object o = null;
            try {
                o = constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new PMException("error initializing FunctionEvaluator: " + e.getMessage());
            }
            if (!(o instanceof FunctionExecutor)) {
                continue;
            }

            FunctionExecutor f = (FunctionExecutor) o;
            funExecs.put(f.getFunctionName(), f);
            System.out.println("loading function " + f.getFunctionName());
        }
    }

    public FunctionExecutor getFunctionExecutor(String name) {
        return funExecs.get(name);
    }

    public boolean evalBool(EventContext eventCtx, long userID, long processID, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = this.funExecs.get(functionName);
        return (boolean)functionExecutor.exec(eventCtx, userID, processID, pdp, function, this);
    }

    public List evalNodeList(EventContext eventCtx, long userID, long processID, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = this.funExecs.get(functionName);
        return (List) functionExecutor.exec(eventCtx, userID, processID, pdp, function, this);
    }

    public Node evalNode(EventContext eventCtx, long userID, long processID, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = this.funExecs.get(functionName);
        return (Node)functionExecutor.exec(eventCtx, userID, processID, pdp, function, this);
    }

    public String evalString(EventContext eventCtx, long userID, long processID, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = this.funExecs.get(functionName);
        return (String)functionExecutor.exec(eventCtx, userID, processID, pdp, function, this);
    }

    public long evalLong(EventContext eventCtx, long userID, long processID, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = this.funExecs.get(functionName);
        return (long)functionExecutor.exec(eventCtx, userID, processID, pdp, function, this);
    }

    public Prohibition.Subject evalProhibitionSubject(EventContext eventCtx, long userID, long processID, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = this.funExecs.get(functionName);
        return (Prohibition.Subject) functionExecutor.exec(eventCtx, userID, processID, pdp, function, this);
    }

    public Map evalMap(EventContext eventCtx, long userID, long processID, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = this.funExecs.get(functionName);
        return (Map)functionExecutor.exec(eventCtx, userID, processID, pdp, function, this);
    }

    public Object evalObject(EventContext eventCtx, long userID, long processID, PDP pdp, Function function) throws PMException {
        String functionName = function.getName();
        FunctionExecutor functionExecutor = this.funExecs.get(functionName);
        return functionExecutor.exec(eventCtx, userID, processID, pdp, function, this);
    }
}
