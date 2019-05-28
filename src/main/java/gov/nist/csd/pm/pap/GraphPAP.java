package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.util.*;

public class GraphPAP implements Graph {

    private Graph                 graph;
    private HashMap<String, Long> namespaces;

    public GraphPAP(Graph graph) throws PMException {
        this.graph = graph;

        // check the super configuration is loaded in the graph
        SuperGraph.check(this.graph);

        this.namespaces = new HashMap<>();
    }

    @Override
    public Node createNode(long id, String name, NodeType nodeType, Map<String, String> properties) throws PMException {
        // check that the node namespace, name, and type do not already exist
        String namespace = toNamespaceString(name, nodeType, properties);
        if(namespaces.get(namespace) != null) {
            throw new PMException(String.format("a node with the name %s and type %s already exists in the name space %s",
                     name, nodeType, getNodeNamespace(properties)));
        }

        // create the node
        Node node = graph.createNode(id, name, nodeType, properties);

        // add the node's namespace
        namespaces.put(toNamespaceString(name, nodeType, properties), node.getID());

        return node;
    }

    private String toNamespaceString(String name, NodeType type, Map<String, String> properties) {
        return String.format("%s:%s:%s", name, type, getNodeNamespace(properties));
    }

    private String getNodeNamespace(Map<String, String> properties) {
        if(properties == null || !properties.containsKey("namespace")) {
            return "default";
        }

        return properties.get("namespace");
    }

    @Override
    public void updateNode(long id, String name, Map<String, String> properties) throws PMException {
        graph.updateNode(id, name, properties);
    }

    @Override
    public void deleteNode(long nodeID) throws PMException {
        // retrieve the node from the graph
        Node node = graph.getNode(nodeID);

        // remove the node frm the namespaces map
        namespaces.remove(toNamespaceString(node.getName(), node.getType(), node.getProperties()));

        // delete the node
        graph.deleteNode(nodeID);
    }

    @Override
    public boolean exists(long nodeID) throws PMException {
        return graph.exists(nodeID);
    }

    @Override
    public Collection<Node> getNodes() throws PMException {
        return graph.getNodes();
    }

    @Override
    public Set<Long> getPolicies() throws PMException {
        return graph.getPolicies();
    }

    @Override
    public Set<Long> getChildren(long nodeID) throws PMException {
        return graph.getChildren(nodeID);
    }

    @Override
    public Set<Long> getParents(long nodeID) throws PMException {
        return graph.getParents(nodeID);
    }

    @Override
    public void assign(long childID, long parentID) throws PMException {
        graph.assign(childID, parentID);
    }

    @Override
    public void deassign(long childID, long parentID) throws PMException {
        graph.deassign(childID, parentID);
    }

    @Override
    public void associate(long uaID, long targetID, Set<String> operations) throws PMException {
        graph.associate(uaID, targetID, operations);
    }

    @Override
    public void dissociate(long uaID, long targetID) throws PMException {
        graph.dissociate(uaID, targetID);
    }

    @Override
    public Map<Long, Set<String>> getSourceAssociations(long sourceID) throws PMException {
        return graph.getSourceAssociations(sourceID);
    }

    @Override
    public Map<Long, Set<String>> getTargetAssociations(long targetID) throws PMException {
        return graph.getTargetAssociations(targetID);
    }

    @Override
    public Set<Node> search(String name, String type, Map<String, String> properties) throws PMException {
        return graph.search(name, type, properties);
    }

    @Override
    public Node getNode(long id) throws PMException {
        return graph.getNode(id);
    }
}
