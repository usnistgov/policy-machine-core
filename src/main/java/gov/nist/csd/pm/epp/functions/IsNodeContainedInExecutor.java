package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.dag.searcher.DepthFirstSearcher;
import gov.nist.csd.pm.pip.graph.dag.searcher.Direction;
import gov.nist.csd.pm.pip.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Boolean exec(Graph graph, Prohibitions prohibitions, Obligations obligations, EventContext eventCtx, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        List< Arg > args = function.getArgs();
        if (args.size() != numParams()) {
            throw new PMException(getFunctionName() + " expected " + numParams() + " parameters but got " + args.size());
        }

        Arg arg = args.get(0);
        Function f = arg.getFunction();
        if (f == null) {
            throw new PMException(getFunctionName() + " expects two functions as parameters");
        }

        Node childNode = functionEvaluator.evalNode(graph, prohibitions, obligations, eventCtx, f);
        if(childNode == null) {
            return false;
        }

        arg = args.get(1);
        f = arg.getFunction();
        if (f == null) {
            throw new PMException(getFunctionName() + " expects two functions as parameters");
        }

        Node parentNode = functionEvaluator.evalNode(graph, prohibitions, obligations, eventCtx, f);
        if(parentNode == null) {
            return false;
        }

        DepthFirstSearcher dfs = new DepthFirstSearcher(graph);
        Set<String> nodes = new HashSet<>();
        Visitor visitor = node -> {
            if (node.getName().equals(parentNode.getName())) {
                nodes.add(node.getName());
            }
        };
        dfs.traverse(childNode, (c, p) -> {}, visitor, Direction.PARENTS);

        return nodes.contains(parentNode.getName());
    }
}
