package gov.nist.csd.pm.policy.events.graph;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

import java.util.Map;

public class CreateObjectAttributeEvent extends CreateNodeEvent{
    public CreateObjectAttributeEvent(String name, Map<String, String> properties, String initialParent, String... parents) {
        super(name, NodeType.OA, properties, initialParent, parents);
    }

    @Override
    public String getEventName() {
        return "create_object_attribute";
    }

    @Override
    public void apply(Policy policy) throws PMException {
        policy.graph().createObjectAttribute(name, properties, initialParent, additionalParents);
    }
}
