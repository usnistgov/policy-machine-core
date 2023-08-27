package gov.nist.csd.pm.policy.events.graph;

import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

import java.util.Map;

public class CreatePolicyClassEvent extends CreateNodeEvent{
    public CreatePolicyClassEvent(String name, Map<String, String> properties) {
        super(name, NodeType.PC, properties);
    }

    @Override
    public String getEventName() {
        return "create_policy_class";
    }

}
