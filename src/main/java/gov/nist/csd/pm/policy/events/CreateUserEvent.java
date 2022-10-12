package gov.nist.csd.pm.policy.events;

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
}
