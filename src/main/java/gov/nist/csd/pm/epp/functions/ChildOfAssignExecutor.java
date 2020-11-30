package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.*;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.evr.EVRException;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

public class ChildOfAssignExecutor implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "child_of_assign";
    }

    @Override
    public int numParams() {
        return 0;
    }

    @Override
    public Node exec(Graph graph, Prohibitions prohibitions, Obligations obligations, EventContext eventCtx, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        Node child;
        if(eventCtx instanceof AssignToEvent) {
            child = ((AssignToEvent) eventCtx).getChildNode();
        } else if (eventCtx instanceof AssignEvent) {
            child = eventCtx.getTarget();
        } else if (eventCtx instanceof DeassignFromEvent) {
            child = ((DeassignFromEvent) eventCtx).getChildNode();
        } else if (eventCtx instanceof DeassignEvent) {
            child = eventCtx.getTarget();
        } else {
            throw new EVRException("invalid event context for function child_of_assign. Valid event contexts are AssignTo, " +
                    "Assign, DeassignFrom, and Deassign");
        }

        return child;
    }
}
