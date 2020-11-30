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

public class ParentOfAssignExecutor implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "parent_of_assign";
    }

    @Override
    public int numParams() {
        return 0;
    }

    @Override
    public Node exec(Graph graph, Prohibitions prohibitions, Obligations obligations, EventContext eventCtx, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        Node parent;
        if(eventCtx instanceof AssignToEvent) {
            parent = eventCtx.getTarget();
        } else if (eventCtx instanceof AssignEvent) {
            parent = ((AssignEvent)eventCtx).getParentNode();
        } else if (eventCtx instanceof DeassignFromEvent) {
            parent = eventCtx.getTarget();
        } else if (eventCtx instanceof DeassignEvent) {
            parent = ((DeassignEvent)eventCtx).getParentNode();
        } else {
            throw new EVRException("invalid event context for function parent_of_assign. Valid event contexts are AssignTo, " +
                    "Assign, DeassignFrom, and Deassign");
        }

        return parent;
    }
}
