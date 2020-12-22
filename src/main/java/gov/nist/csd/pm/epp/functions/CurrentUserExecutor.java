package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

public class CurrentUserExecutor implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "current_user";
    }

    @Override
    public int numParams() {
        return 0;
    }

    @Override
    public Node exec(Graph graph, Prohibitions prohibitions, Obligations obligations, EventContext eventCtx, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        return graph.getNode(eventCtx.getUserCtx().getUser());
    }
}
