package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;

public class CurrentUserExecutor implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "current_user";
    }

    @Override
    public int numParams() {
        return 0;
    }

    /**
     * @return the Node with the given userID
     */
    @Override
    public Node exec(EventContext eventCtx, long userID, long processID, PDP pdp, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        return pdp.getPAP().getGraphPAP().getNode(userID);
    }
}
