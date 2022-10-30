package gov.nist.csd.pm.policy.model.graph.nodes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores information needed for a node.
 */
public class Node implements Serializable {
    private String              name;
    private NodeType            type;
    private Map<String, String> properties;

    public Node() {
        this.properties = new HashMap<>();
    }

    public Node(Node node) {
        this.name = node.getName();
        this.type = node.getType();
        this.properties = node.getProperties() == null ? new HashMap<>() : new HashMap<>(node.getProperties());
    }

    public Node(String name, NodeType type, Map<String, String> properties) {
        this.name = name;
        this.type = type;
        this.properties = properties == null ? new HashMap<>() : properties;
    }

    public Node(String name, NodeType type) {
        this.name = name;
        this.type = type;
        this.properties = new HashMap<>();
    }

    public Node(String name) {
        this.name = name;
    }

    public Node addProperty(String key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("a node cannot have a property with a null key or value");
        }

        this.properties.put(key, value);
        return this;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    /**
     * Two nodes are equal if their IDs are the same.
     *
     * @param o The object to check for equality.
     * @return true if the two objects are the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Node n) {
            return this.name.equals(n.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name + ":" + type + ":" + properties;
    }

}
