package gov.nist.csd.pm.policy.events.graph;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

import java.util.Map;

public class CreateUserEvent extends CreateNodeEvent{
    public CreateUserEvent(String name, Map<String, String> properties, String initialParent, String... parents) {
        super(name, NodeType.U, properties, initialParent, parents);
    }

    @Override
    public String getEventName() {
        return "create_user";
    }

    @Override
    public void apply(Policy policy) throws PMException {
        policy.graph().createUser(name, properties, initialParent, additionalParents);
    }
}
