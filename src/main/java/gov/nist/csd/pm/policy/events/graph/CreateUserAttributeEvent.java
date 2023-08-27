package gov.nist.csd.pm.policy.events.graph;

import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

import java.util.Map;

public class CreateUserAttributeEvent extends CreateNodeEvent{
    public CreateUserAttributeEvent(String name, Map<String, String> properties, String initialParent, String... parents) {
        super(name, NodeType.UA, properties, initialParent, parents);
    }

    @Override
    public String getEventName() {
        return "create_user_attribute";
    }

}
