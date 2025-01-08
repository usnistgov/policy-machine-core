package gov.nist.csd.pm.pap.serialization.json;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class JSONNode {

    private String name;
    private List<JSONProperty> properties;
    private Collection<String> assignments;
    private List<JSONAssociation> associations;

    public JSONNode(String name, List<JSONProperty> properties, Collection<String> assignments, List<JSONAssociation> associations) {
        this.name = name;

        if (properties != null && !properties.isEmpty()) {
            this.properties = properties;
        }

        if (assignments != null && !assignments.isEmpty()) {
            this.assignments = assignments;
        }

        if (associations != null && !associations.isEmpty()) {
            this.associations = associations;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JSONProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<JSONProperty> properties) {
        this.properties = properties;
    }

    public Collection<String> getAssignments() {
        return assignments;
    }

    public void setAssignments(Collection<String> assignments) {
        this.assignments = assignments;
    }

    public List<JSONAssociation> getAssociations() {
        return associations;
    }

    public void setAssociations(List<JSONAssociation> associations) {
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
