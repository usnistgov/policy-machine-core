package gov.nist.csd.pm.core.pap.serialization.json;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class JSONNode {

    private long id;
    private String name;
    private List<JSONProperty> properties;
    private Collection<Long> assignments;
    private List<JSONAssociation> associations;

    public JSONNode() {
    }

    public JSONNode(long id, String name, List<JSONProperty> properties, Collection<Long> assignments, List<JSONAssociation> associations) {
        this.id = id;
        this.name = name;
        this.properties = properties;
        this.assignments = assignments;
        this.associations = associations;
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

    public List<JSONProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<JSONProperty> properties) {
        this.properties = properties;
    }

    public Collection<Long> getAssignments() {
        return assignments;
    }

    public void setAssignments(Collection<Long> assignments) {
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
