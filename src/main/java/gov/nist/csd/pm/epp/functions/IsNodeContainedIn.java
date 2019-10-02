package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;

import java.util.List;

public class IsNodeContainedIn implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "is_node_contained_in";
    }

    @Override
    public int numParams() {
        return 2;
    }

    @Override
    public Object exec(EventContext eventCtx, long userID, long processID, PDP pdp, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        List< Arg > args = function.getArgs();
        if (args.size() != numParams()) {
            throw new PMException(getFunctionName() + " expected " + numParams() + " parameters but got " + args.size());
        }

        Arg arg = args.get(0);
        Function f = arg.getFunction();
        Node childNode = functionEvaluator.evalNode(eventCtx, userID, processID, pdp, f);
        if(childNode == null) {
            return false;
        }

        arg = args.get(1);
        f = arg.getFunction();
        Node parentNode = functionEvaluator.evalNode(eventCtx, userID, processID, pdp, f);
        if(parentNode == null) {
            return false;
        }

        return pdp.getPAP().getGraphPAP().getChildren(parentNode.getID()).contains(childNode.getID());
    }
}
