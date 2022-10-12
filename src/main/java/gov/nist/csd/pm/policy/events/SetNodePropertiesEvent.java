package gov.nist.csd.pm.policy.events;

import java.util.Map;

public class SetNodePropertiesEvent implements PolicyEvent {

    private final String name;
    private final Map<String, String> properties;

    public SetNodePropertiesEvent(String name, Map<String, String> properties) {
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String getEventName() {
        return "set_node_properties";
    }
}
