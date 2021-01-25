package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.policies.SuperPolicy;
import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.graph.model.relationships.Assignment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.operations.Operations.ALL_OPS;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.NAMESPACE_PROPERTY;
import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.REP_PROPERTY;

public class GraphAdmin implements Graph {

    private FunctionalEntity pip;
    private Graph graph;
    private SuperPolicy superPolicy;

    public GraphAdmin(FunctionalEntity pip) throws PMException {
        this.pip = pip;
        this.graph = pip.getGraph();
        this.superPolicy = new SuperPolicy();
        this.superPolicy.configure(this.graph);
    }

    public SuperPolicy getSuperPolicy() {
        return superPolicy;
    }

    public static String getPolicyClassDefault(String pcName, NodeType type) {
        return pcName + "_default_" + type.toString();
    }

    @Override
    public Node createPolicyClass(String name, Map<String, String> properties) throws PMException {
        Map<String, String> nodeProps = new HashMap<>();
        if (properties != null) {
            nodeProps.putAll(properties);
        }

        String rep = name + "_rep";
        String defaultUA = getPolicyClassDefault(name, UA);
        String defaultOA = getPolicyClassDefault(name, OA);

        nodeProps.putAll(Node.toProperties("default_ua", defaultUA, "default_oa", defaultOA,
                REP_PROPERTY, rep));

        Node pcNode = new Node();
        pip.runTx((g, p, o) -> {
            // create the pc node
            Node node = g.createPolicyClass(name, nodeProps);
            pcNode.setName(node.getName());
            pcNode.setType(node.getType());
            pcNode.setProperties(node.getProperties());

            // create the PC UA node
            Node pcUANode = g.createNode(defaultUA, UA, Node.toProperties(NAMESPACE_PROPERTY, name), pcNode.getName());
            // create the PC OA node
            Node pcOANode = g.createNode(defaultOA, OA, Node.toProperties(NAMESPACE_PROPERTY, name), pcNode.getName());

            // assign Super U to PC UA
            // getPAP().getGraphPAP().assign(superPolicy.getSuperU().getID(), pcUANode.getID());
            // assign superUA and superUA2 to PC
            g.assign(superPolicy.getSuperUserAttribute().getName(), pcNode.getName());
            g.assign(superPolicy.getSuperUserAttribute2().getName(), pcNode.getName());
            // associate Super UA and PC UA
            g.associate(superPolicy.getSuperUserAttribute().getName(), pcUANode.getName(), new OperationSet(ALL_OPS));
            // associate Super UA and PC OA
            g.associate(superPolicy.getSuperUserAttribute().getName(), pcOANode.getName(), new OperationSet(ALL_OPS));

            // create an OA that will represent the pc
            g.createNode(rep, OA, Node.toProperties("pc", String.valueOf(name)),
                    superPolicy.getSuperObjectAttribute().getName());
        });

        return pcNode;
    }

    @Override
    public Node createNode(String name, NodeType type, Map<String, String> properties, String initialParent, String... additionalParents) throws PMException {
        if (name == null) {
            throw new IllegalArgumentException("a node cannot have a null name");
        } else if (type == null) {
            throw new IllegalArgumentException("a node cannot have a null type");
        } else if (type == PC) {
            throw new IllegalArgumentException("use cretePolicyClass to crete policy classes");
        }

        // instantiate the properties map if it's null
        // if this node is a user, hash the password if present in the properties
        Map<String, String> nodeProps = new HashMap<>();
        if (properties != null) {
            nodeProps.putAll(properties);
        }

        NodeType defaultType = (type == OA || type == O) ? OA : UA;

        Node node = new Node();
        pip.runTx((g, p, o) -> {
            // if the parent is a PC get the PC default
            Node parentNode = g.getNode(initialParent);
            String pcInitParent = null;
            if (parentNode.getType().equals(PC)) {
                pcInitParent = getPolicyClassDefault(parentNode.getName(), defaultType);
            }

            for (int i = 0; i < additionalParents.length; i++) {
                String parent = additionalParents[i];

                // if the parent is a PC get the PC default attribute
                Node additionalParentNode = g.getNode(parent);
                if (additionalParentNode.getType().equals(PC)) {
                    additionalParents[i] = getPolicyClassDefault(additionalParentNode.getName(), defaultType);
                }
            }

            Node n = g.createNode(name, type, properties, (pcInitParent == null) ? initialParent : pcInitParent, additionalParents);
            node.setName(n.getName());
            node.setType(n.getType());
            node.setProperties(n.getProperties());
        });

        return node;
    }

    @Override
    public synchronized void updateNode(String name, Map<String, String> properties) throws PMException {
        graph.updateNode(name, properties);
    }

    @Override
    public void deleteNode(String name) throws PMException {
        if (graph.getChildren(name).size() != 0) {
            throw new PMException("cannot delete " + name + ", nodes are still assigned to it");
        }
        graph.deleteNode(name);
    }

    @Override
    public boolean exists(String name) throws PMException {
        return graph.exists(name);
    }

    @Override
    public Set<String> getPolicyClasses() throws PMException {
        return graph.getPolicyClasses();
    }

    @Override
    public Set<Node> getNodes() throws PMException {
        return graph.getNodes();
    }

    @Override
    public Node getNode(String name) throws PMException {
        if(!exists(name)) {
            throw new PMException(String.format("node %s could not be found", name));
        }

        return graph.getNode(name);
    }

    @Override
    public Node getNode(NodeType type, Map<String, String> properties) throws PMException {
        return graph.getNode(type, properties);
    }

    @Override
    public Set<Node> search(NodeType type, Map<String, String> properties) throws PMException {
        return graph.search(type, properties);
    }

    @Override
    public Set<String> getChildren(String name) throws PMException {
        return graph.getChildren(name);
    }

    @Override
    public Set<String> getParents(String name) throws PMException {
        return graph.getParents(name);
    }

    @Override
    public void assign(String child, String parent) throws PMException {
        // check that the nodes are not null
        if(child == null) {
            throw new IllegalArgumentException("the child node name cannot be null when creating an assignment");
        } else if(parent == null) {
            throw new IllegalArgumentException("the parent node name cannot be null when creating an assignment");
        } else if(!exists(child)) {
            throw new PMException(String.format("child node %s does not exist", child));
        } else if(!exists(parent)) {
            throw new PMException(String.format("parent node %s does not exist", parent));
        }

        //check if the assignment is valid
        Node childNode = getNode(child);
        Node parentNode = getNode(parent);
        Assignment.checkAssignment(childNode.getType(), parentNode.getType());

        if (parentNode.getType().equals(PC)) {
            parent = getPolicyClassDefault(parentNode.getName(), childNode.getType());
        }

        graph.assign(child, parent);
    }

    @Override
    public void deassign(String child, String parent) throws PMException {
        // check that the parameters are correct
        if(child == null) {
            throw new IllegalArgumentException("the child node cannot be null when deassigning");
        } else if(parent == null) {
            throw new IllegalArgumentException("the parent node cannot be null when deassigning");
        } else if(!exists(child)) {
            throw new PMException(String.format("child node %s could not be found when deassigning", child));
        } else if(!exists(parent)) {
            throw new PMException(String.format("parent node %s could not be found when deassigning", parent));
        }

        graph.deassign(child, parent);
    }

    @Override
    public boolean isAssigned(String child, String parent) throws PMException {
        return graph.isAssigned(child, parent);
    }

    @Override
    public void associate(String ua, String target, OperationSet operations) throws PMException {
        if(ua == null) {
            throw new IllegalArgumentException("the user attribute cannot be null when creating an association");
        } else if(target == null) {
            throw new IllegalArgumentException("the target node cannot be null when creating an association");
        } else if(!exists(ua)) {
            throw new PMException(String.format("node %s could not be found when creating an association", ua));
        } else if(!exists(target)) {
            throw new PMException(String.format("node %s could not be found when creating an association", target));
        }

        graph.associate(ua, target, operations);
    }

    @Override
    public void dissociate(String ua, String target) throws PMException {
        if(ua == null) {
            throw new IllegalArgumentException("the user attribute cannot be null when creating an association");
        } else if(target == null) {
            throw new IllegalArgumentException("the target cannot be null when creating an association");
        } else if(!exists(ua)) {
            throw new PMException(String.format("node %s could not be found when deleting an association", ua));
        } else if(!exists(target)) {
            throw new PMException(String.format("node %s could not be found when deleting an association", target));
        }

        graph.dissociate(ua, target);
    }

    @Override
    public Map<String, OperationSet> getSourceAssociations(String source) throws PMException {
        if(!exists(source)) {
            throw new PMException(String.format("node %s could not be found", source));
        }

        return graph.getSourceAssociations(source);
    }

    @Override
    public Map<String, OperationSet> getTargetAssociations(String target) throws PMException {
        if(!exists(target)) {
            throw new PMException(String.format("node %s could not be found", target));
        }

        return graph.getTargetAssociations(target);
    }

    @Override
    public String toJson() throws PMException {
        return graph.toJson();
    }

    @Override
    public void fromJson(String s) throws PMException {
        graph.fromJson(s);
    }
}
