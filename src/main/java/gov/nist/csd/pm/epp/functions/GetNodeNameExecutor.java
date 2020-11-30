package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

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

    @Override
    public String exec(Graph graph, Prohibitions prohibitions, Obligations obligations, EventContext eventCtx, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        List<Arg> args = function.getArgs();
        if (args.size() != numParams()) {
            throw new PMException(getFunctionName() + " expected " + numParams() + " arg but got " + args.size());
        }

        Arg arg = args.get(0);
        Function argFunction = arg.getFunction();
        if (argFunction == null) {
            throw new PMException(getFunctionName() + " expected the first argument to be a function but it was null");
        }

        Node node = functionEvaluator.evalNode(graph, prohibitions, obligations, eventCtx, argFunction);
        return node.getName();
    }
}
