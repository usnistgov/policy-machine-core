/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.serialization.json;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * JSON DTO for a graph node: its id, name, properties, descendant assignments, and outgoing associations.
 */
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
