package gov.nist.csd.pm.pap.serialization.json;

import java.util.List;
import java.util.Map;

public class JSONPolicyClass {

    private Map<String, String> properties;

    public JSONPolicyClass() {

    }

    public JSONPolicyClass(Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
