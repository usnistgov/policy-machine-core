package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;

import java.util.List;

public class GetNodeNameExecutor implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "get_node_name";
    }

    @Override
    public int numParams() {
        return 1;
    }

    /**
     * A function that returns a Node is the only expected parameter in the Function parameter.
     * @return the name of the node returned from this function.
     */
    @Override
    public String exec(EventContext eventCtx, String user, String process, PDP pdp, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        List<Arg> args = function.getArgs();
        if (args.size() != numParams()) {
            throw new PMException(getFunctionName() + " expected " + numParams() + " arg but got " + args.size());
        }

        Arg arg = args.get(0);
        Function argFunction = arg.getFunction();
        if (argFunction == null) {
            throw new PMException(getFunctionName() + " expected the first argument to be a function but it was null");
        }

        Node node = functionEvaluator.evalNode(eventCtx, user, process, pdp, argFunction);
        return node.getName();
    }
}
