package gov.nist.csd.pm.common.graph.node;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public Node(long id, String name, NodeType type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.properties = new HashMap<>();
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

    public String nameAndId() {
        return name + ":" + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node node)) return false;
	    return id == node.id && Objects.equals(name, node.name) && type == node.type && Objects.equals(properties, node.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, properties);
    }

    @Override
    public String toString() {
        return name + ":" + id + ":" + type + ":" + properties;
    }

}
