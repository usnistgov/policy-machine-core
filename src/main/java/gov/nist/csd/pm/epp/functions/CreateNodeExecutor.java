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

public class CreateNodeExecutor implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "create_node";
    }

    /**
     * parent name, parent type, parent properties, name, type, properties
     * @return
     */
    @Override
    public int numParams() {
        return 6;
    }

    @Override
    public Node exec(Graph graph, Prohibitions prohibitions, Obligations obligations, EventContext eventCtx, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        List<Arg> args = function.getArgs();

        // first arg is the name, can be function that returns a string
        Arg parentNameArg = args.get(0);
        String parentName = parentNameArg.getValue();
        if(parentNameArg.getFunction() != null) {
            parentName = functionEvaluator.evalString(graph, prohibitions, obligations, eventCtx, parentNameArg.getFunction());
        }

        // second arg is the type, can be function
        Arg parentTypeArg = args.get(1);
        String parentType = parentTypeArg.getValue();
        if(parentTypeArg.getFunction() != null) {
            parentType = functionEvaluator.evalString(graph, prohibitions, obligations, eventCtx, parentTypeArg.getFunction());
        }

        // fourth arg is the name, can be function
        Arg nameArg = args.get(2);
        String name = nameArg.getValue();
        if(nameArg.getFunction() != null) {
            name = functionEvaluator.evalString(graph, prohibitions, obligations, eventCtx, nameArg.getFunction());
        }

        // fifth arg is the type, can be function
        Arg typeArg = args.get(3);
        String type = typeArg.getValue();
        if(typeArg.getFunction() != null) {
            type = functionEvaluator.evalString(graph, prohibitions, obligations, eventCtx, typeArg.getFunction());
        }

        // sixth arg is the properties which is a map that has to come from a function
        Map<String, String> props = new HashMap<>();
        if(args.size() > 4) {
            Arg propsArg = args.get(4);
            if (propsArg.getFunction() != null) {
                props = (Map) functionEvaluator.evalMap(graph, prohibitions, obligations, eventCtx, propsArg.getFunction());
            }
        }

        Node parentNode;
        if (parentName != null) {
            parentNode = graph.getNode(parentName);
        } else {
            parentNode = graph.getNode(NodeType.toNodeType(parentType), new HashMap<>());
        }

        return graph.createNode(name, NodeType.toNodeType(type), props, parentNode.getName());
    }
}
