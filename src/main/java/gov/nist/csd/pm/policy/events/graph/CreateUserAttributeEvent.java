package gov.nist.csd.pm.policy.events.graph;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
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

    @Override
    public void apply(Policy policy) throws PMException {
        policy.graph().createUserAttribute(name, properties, initialParent, additionalParents);
    }
}
