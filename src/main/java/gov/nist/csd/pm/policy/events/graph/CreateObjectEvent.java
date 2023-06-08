package gov.nist.csd.pm.policy.events.graph;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

import java.util.Map;

public class CreateObjectEvent extends CreateNodeEvent{
    public CreateObjectEvent(String name, Map<String, String> properties, String initialParent, String... parents) {
        super(name, NodeType.O, properties, initialParent, parents);
    }

    @Override
    public String getEventName() {
        return "create_object";
    }

    @Override
    public void apply(Policy policy) throws PMException {
        policy.graph().createObject(name, properties, initialParent, additionalParents);
    }
}
