package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GetChildrenExecutor implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "get_children";
    }

    @Override
    public int numParams() {
        return 1;
    }

    @Override
    public List<String> exec(EventContext eventCtx, String user, String process, PDP pdp, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        FunctionExecutor getNodeExecutor = functionEvaluator.getFunctionExecutor("get_node");
        Node node = (Node)getNodeExecutor.exec(eventCtx, user, process, pdp, function, functionEvaluator);
        Set<String> children = pdp.getPAP().getGraphPAP().getChildren(node.getName());
        return new ArrayList<>(children);
    }
}
