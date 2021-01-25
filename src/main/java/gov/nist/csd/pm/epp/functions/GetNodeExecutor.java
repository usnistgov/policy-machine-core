package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetNodeExecutor implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "get_node";
    }

    @Override
    public int numParams() {
        return 2;
    }

    @Override
    public Node exec(Graph graph, Prohibitions prohibitions, Obligations obligations, EventContext eventCtx, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        List<Arg> args = function.getArgs();
        if (args == null || args.size() < numParams() || args.size() > numParams()) {
            throw new PMException(getFunctionName() + " expected at least two arguments (name and type) but found none");
        }

        // first arg should be a string or a function tht returns a string
        Arg arg = args.get(0);
        String name = arg.getValue();
        if(arg.getFunction() != null) {
            name = functionEvaluator.evalString(graph, prohibitions, obligations, eventCtx, arg.getFunction());
        }

        // second arg should be the type of the node to search for
        arg = args.get(1);
        String type = arg.getValue();
        if(arg.getFunction() != null) {
            type = functionEvaluator.evalString(graph, prohibitions, obligations, eventCtx, arg.getFunction());
        }

        Map<String, String> props = new HashMap<>();
        if(args.size() > 2) {
            arg = args.get(2);
            if (arg.getFunction() != null) {
                props = (Map) functionEvaluator.evalMap(graph, prohibitions, obligations, eventCtx, arg.getFunction());
            }
        }

        if (name != null) {
            return graph.getNode(name);
        }

        return graph.getNode(NodeType.toNodeType(type), props);
    }
}
