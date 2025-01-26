package gov.nist.csd.pm.common.graph.node;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores information needed for a node.
 */
public class Node implements Serializable {

    private long id;
    private String              name;
    private NodeType            type;
    private Map<String, String> properties;

    public Node() {
        this.properties = new HashMap<>();
    }

    public Node(long id, String name, NodeType type, Map<String, String> properties) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.properties = properties;
    }

    public Node(Node node) {
        this.id = node.id;
        this.name = node.name;
        this.type = node.type;
        this.properties = node.properties == null ? new HashMap<>() : new HashMap<>(node.properties);
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getIdAndName() {
        return id + ":" + name;
    }

    /**
     * Two nodes are equal if their IDs are the same.
     *
     * @param o The object to check for equality.
     * @return true if the two objects are the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node n)) {
            return false;
        }

        return this.name.equals(n.name)
                && this.type.equals(n.type)
                && this.properties.equals(n.properties);
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
