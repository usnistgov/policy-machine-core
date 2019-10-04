package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CreateNodeExecutor implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "create_node";
    }

    @Override
    public int numParams() {
        return 0;
    }

    @Override
    public Node exec(EventContext eventCtx, long userID, long processID, PDP pdp, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        List<Arg> args = function.getArgs();

        // first arg is the name, can be function
        Arg nameArg = args.get(0);
        String name = nameArg.getValue();
        if(nameArg.getFunction() != null) {
            name = functionEvaluator.evalString(eventCtx, userID, processID, pdp, nameArg.getFunction());
        }

        // second arg is the type, can be function
        Arg typeArg = args.get(1);
        String type = typeArg.getValue();
        if(typeArg.getFunction() != null) {
            type = functionEvaluator.evalString(eventCtx, userID, processID, pdp, typeArg.getFunction());
        }

        // third arg is the properties which is a map that has to come from a function
        Map<String, String> props = new HashMap<>();
        if(args.size() > 2) {
            Arg propsArg = args.get(2);
            if (propsArg.getFunction() != null) {
                props = (Map) functionEvaluator.evalMap(eventCtx, userID, processID, pdp, propsArg.getFunction());
            }
        }

        long id = new Random().nextLong();
        Graph graph = pdp.getPAP().getGraphPAP();
        return graph.createNode(id, name, NodeType.toNodeType(type), props);
    }
}
