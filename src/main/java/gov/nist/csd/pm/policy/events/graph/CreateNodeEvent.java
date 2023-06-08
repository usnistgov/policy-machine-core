package gov.nist.csd.pm.policy.events.graph;

import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public abstract class CreateNodeEvent extends GraphEvent {

    protected final String name;
    protected final NodeType type;
    protected final Map<String, String> properties;
    protected String initialParent;
    protected String[] additionalParents;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateNodeEvent that = (CreateNodeEvent) o;
        return Objects.equals(name, that.name) && type == that.type && Objects.equals(properties, that.properties) && Objects.equals(initialParent, that.initialParent) && Arrays.equals(additionalParents, that.additionalParents);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, type, properties, initialParent);
        result = 31 * result + Arrays.hashCode(additionalParents);
        return result;
    }
}
