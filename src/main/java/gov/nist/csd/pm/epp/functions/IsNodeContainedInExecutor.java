package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;

import java.util.List;

public class IsNodeContainedInExecutor implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "is_node_contained_in";
    }

    @Override
    public int numParams() {
        return 2;
    }

    @Override
    public Boolean exec(EventContext eventCtx, String user, String process, PDP pdp, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        List< Arg > args = function.getArgs();
        if (args.size() != numParams()) {
            throw new PMException(getFunctionName() + " expected " + numParams() + " parameters but got " + args.size());
        }

        Arg arg = args.get(0);
        Function f = arg.getFunction();
        if (f == null) {
            throw new PMException(getFunctionName() + " expects two functions as parameters");
        }

        Node childNode = functionEvaluator.evalNode(eventCtx, user, process, pdp, f);
        if(childNode == null) {
            return false;
        }

        arg = args.get(1);
        f = arg.getFunction();
        if (f == null) {
            throw new PMException(getFunctionName() + " expects two functions as parameters");
        }

        Node parentNode = functionEvaluator.evalNode(eventCtx, user, process, pdp, f);
        if(parentNode == null) {
            return false;
        }

        return pdp.getPAP().getGraphPAP().getChildren(parentNode.getName()).contains(childNode.getName());
    }
}
