package gov.nist.csd.pm.policy.serialization.json;

import java.util.List;
import java.util.Map;

public class JSONPolicyClass {

    private String name;
    private Map<String, String> properties;
    private List<JSONNode> userAttributes;
    private List<JSONNode> objectAttributes;
    private Map<String, List<JSONAssociation>> associations;

    public JSONPolicyClass() {

    }

    public JSONPolicyClass(String name, Map<String, String> properties, List<JSONNode> userAttributes,
                           List<JSONNode> objectAttributes, Map<String, List<JSONAssociation>> associations) {
        this.name = name;
        this.properties = properties;
        this.userAttributes = userAttributes;
        this.objectAttributes = objectAttributes;
        this.associations = associations;
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

    public List<JSONNode> getUserAttributes() {
        return userAttributes;
    }

    public void setUserAttributes(List<JSONNode> userAttributes) {
        this.userAttributes = userAttributes;
    }

    public List<JSONNode> getObjectAttributes() {
        return objectAttributes;
    }

    public void setObjectAttributes(List<JSONNode> objectAttributes) {
        this.objectAttributes = objectAttributes;
    }

    public Map<String, List<JSONAssociation>> getAssociations() {
        return associations;
    }

    public void setAssociations(
            Map<String, List<JSONAssociation>> associations) {
        this.associations = associations;
    }
}
