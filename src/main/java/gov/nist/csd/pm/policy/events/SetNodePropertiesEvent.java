package gov.nist.csd.pm.policy.events;

import java.util.Map;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SetNodePropertiesEvent that = (SetNodePropertiesEvent) o;
        return Objects.equals(name, that.name) && Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, properties);
    }
}
