package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class ObjectAttribute implements Vertex {

    private final Node node;
    private final List<String> parents;
    private final List<String> children;
    private final List<Association> associations;

    public ObjectAttribute(String name, Map<String, String> properties) {
        this.node = new Node(name, NodeType.OA, properties);
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.associations = new ArrayList<>();
    }

    private ObjectAttribute(Node node, List<String> parents, List<String> children, List<Association> associations) {
        this.node = new Node(node);
        this.parents = new ArrayList<>(parents);
        this.children = new ArrayList<>(children);
        this.associations = new ArrayList<>(associations);
    }

    @Override
    public Vertex copy() {
        return new ObjectAttribute(node, parents, children, associations);
    }

    @Override
    public void setProperties(Map<String, String> properties) {
        node.setProperties(properties);
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public List<String> getParents() {
        return parents;
    }

    @Override
    public List<String> getChildren() {
        return children;
    }

    @Override
    public List<Association> getOutgoingAssociations() {
        return Collections.emptyList();
    }

    @Override
    public List<Association> getIncomingAssociations() {
        return associations;
    }

    @Override
    public void addAssignment(String child, String parent) {
        if (child.equals(node.getName())) {
            parents.add(parent);
        } else {
            children.add(child);
        }
    }

    @Override
    public void removeAssignment(String child, String parent) {
        if (child.equals(node.getName())) {
            parents.remove(parent);
        } else {
            children.remove(child);
        }
    }

    @Override
    public void addAssociation(String ua, String target, AccessRightSet accessRightSet) {
        associations.add(new Association(ua, target, accessRightSet));
    }

    @Override
    public void removeAssociation(String ua, String target) {
        associations.remove(new Association(ua, target));
    }
}
