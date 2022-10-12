package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

import java.util.Map;

public abstract class CreateNodeEvent implements PolicyEvent {

    private final String name;
    private final NodeType type;
    private final Map<String, String> properties;
    private String initialParent;
    private String[] additionalParents;

    protected CreateNodeEvent(String name, NodeType type, Map<String, String> properties, String initialParent, String ... parents) {
        this.name = name;
        this.type = type;
        this.properties = properties;
        this.initialParent = initialParent;
        this.additionalParents = parents;
    }

    protected CreateNodeEvent(String name, NodeType type, Map<String, String> properties) {
        this.name = name;
        this.type = type;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public NodeType getType() {
        return type;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getInitialParent() {
        return initialParent;
    }

    public String[] getAdditionalParents() {
        return additionalParents;
    }
}
