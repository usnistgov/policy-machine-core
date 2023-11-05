package gov.nist.csd.pm.policy.serialization.json;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JSONNode {

    private String name;
    private Map<String, String> properties;
    private List<JSONNode> children;

    public JSONNode() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public List<JSONNode> getChildren() {
        return children;
    }

    public void setChildren(List<JSONNode> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JSONNode jsonNode = (JSONNode) o;
        return Objects.equals(name, jsonNode.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
