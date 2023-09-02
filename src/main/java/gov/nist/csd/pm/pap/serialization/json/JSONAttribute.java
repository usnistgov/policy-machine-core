package gov.nist.csd.pm.pap.serialization.json;

import java.util.Map;

public class JSONAttribute {

    private Map<String, String> properties;
    private Map<String, JSONAttribute> children;

    public JSONAttribute() {
    }

    public JSONAttribute(Map<String, String> properties, Map<String, JSONAttribute> children) {
        this.properties = properties;
        this.children = children;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, JSONAttribute> getChildren() {
        return children;
    }

    public void setChildren(Map<String, JSONAttribute> children) {
        this.children = children;
    }
}
