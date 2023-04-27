package gov.nist.csd.pm.policy.json;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;

import java.util.List;
import java.util.Map;

public class JSONGraph {
    AccessRightSet resourceAccessRights;
    List<Node> nodes;
    List<String[]> assignments;
    Map<String, Map<String, AccessRightSet>> associations;

    public JSONGraph() {
    }

    public JSONGraph(AccessRightSet resourceAccessRights, List<Node> nodes, List<String[]> assignments, Map<String, Map<String, AccessRightSet>> associations) {
        this.resourceAccessRights = resourceAccessRights;
        this.nodes = nodes;
        this.assignments = assignments;
        this.associations = associations;
    }

    public AccessRightSet getResourceAccessRights() {
        return resourceAccessRights;
    }

    public void setResourceAccessRights(AccessRightSet resourceAccessRights) {
        this.resourceAccessRights = resourceAccessRights;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<String[]> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<String[]> assignments) {
        this.assignments = assignments;
    }

    public Map<String, Map<String, AccessRightSet>> getAssociations() {
        return associations;
    }

    public void setAssociations(Map<String, Map<String, AccessRightSet>> associations) {
        this.associations = associations;
    }
}
