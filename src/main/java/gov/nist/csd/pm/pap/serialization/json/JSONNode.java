package gov.nist.csd.pm.pap.serialization.json;

import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class JSONNode {

    private Map<String, String> properties;
    private Collection<String> assignments;
    private Map<String, AccessRightSet> associations;

    public JSONNode(Map<String, String> properties, Collection<String> assignments) {
        if (!properties.isEmpty()) {
            this.properties = properties;
        }

        if (!assignments.isEmpty()) {
            this.assignments = assignments;
        }
    }

    public JSONNode(Map<String, String> properties,
                    Collection<String> assignments,
                    Map<String, AccessRightSet> associations) {
        this(properties, assignments);
        this.associations = associations;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Collection<String> getAssignments() {
        return assignments;
    }

    public void setAssignments(Collection<String> assignments) {
        this.assignments = assignments;
    }

    public Map<String, AccessRightSet> getAssociations() {
        return associations;
    }

    public void setAssociations(Map<String, AccessRightSet> associations) {
        this.associations = associations;
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
        return Objects.equals(properties, jsonNode.properties) && Objects.equals(
                assignments,
                jsonNode.assignments
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties, assignments);
    }
}
