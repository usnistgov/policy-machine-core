package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

import java.util.Set;

public class DeleteNodeEvent extends EventContext {

    private Set<String> parents;

    public DeleteNodeEvent(UserContext userCtx, Node deletedNode, Set<String> parents) {
        super(userCtx, DELETE_NODE_EVENT, deletedNode);
        this.parents = parents;
    }

    public Set<String> getParents() {
        return parents;
    }
}
