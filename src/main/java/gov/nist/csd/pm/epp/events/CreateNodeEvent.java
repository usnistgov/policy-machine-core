package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CreateNodeEvent extends EventContext {

    private Set<String> parents;

    public CreateNodeEvent(UserContext userCtx, Node deletedNode,
                           String initialParent, String ... additionalParents) {
        super(userCtx, CREATE_NODE_EVENT, deletedNode);
        this.parents = new HashSet<>(Arrays.asList(additionalParents));
        this.parents.add(initialParent);
    }

    public Set<String> getParents() {
        return parents;
    }
}
