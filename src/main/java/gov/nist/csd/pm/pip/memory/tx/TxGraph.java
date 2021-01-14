package gov.nist.csd.pm.pip.memory.tx;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.memory.tx.cmd.*;
import gov.nist.csd.pm.pip.memory.tx.cmd.graph.*;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.PC;

public class TxGraph implements Graph {
    private Graph targetGraph;
    private Map<String, Node> nodes;
    private Set<Node> pcs;
    private Map<String, Set<String>> assignments;
    private Map<String, Map<String, OperationSet>> associations;
    private List<TxCmd> cmds;

    public TxGraph(Graph graph) {
        targetGraph = graph;
        nodes = new HashMap<>();
        pcs = new HashSet<>();
        assignments = new HashMap<>();
        associations = new HashMap<>();
        cmds = new ArrayList<>();
    }

    private List<TxCmd> getCmdsOfType(Class c) {
        List<TxCmd> txCmds = new ArrayList<>();
        for (TxCmd txCmd : this.cmds) {
            if (txCmd.getClass().equals(c)) {
                txCmds.add(txCmd);
            }
        }
        return txCmds;
    }

    @Override
    public Node createPolicyClass(String name, Map<String, String> properties) throws PMException {
        Node pc = new Node(name, PC, properties);
        pcs.add(pc);
        nodes.put(name, pc);

        cmds.add(new CreatePolicyClassTxCmd(targetGraph, name, properties));

        return pc;
    }

    @Override
    public Node createNode(String name, NodeType type, Map<String, String> properties, String initialParent, String... additionalParents) throws PMException {
        Node node = new Node(name, type, properties);
        nodes.put(name, node);

        Set<String> parents = new HashSet<>();
        parents.add(initialParent);
        parents.addAll(Arrays.asList(additionalParents));

        // check that the parents exist in the tx or target graph
        for (String parent : parents) {
            if (!(nodes.containsKey(parent) || targetGraph.exists(parent))) {
                throw new PMException("parent " + parent + " does not exist");
            }
        }

        cmds.add(new CreateNodeTxCmd(targetGraph, name, type, properties, parents));

        return node;
    }

    @Override
    public void updateNode(String name, Map<String, String> properties) throws PMException {
        Node node;
        if (nodes.containsKey(name)) {
            node = nodes.get(name);
        } else if (targetGraph.exists(name)) {
            node = targetGraph.getNode(name);
        } else {
            throw new PMException("node " + name + " does not exist");
        }

        node.setProperties(properties);
        if (node.getType() == PC) {
            pcs.add(node);
        }

        nodes.put(name, node);

        cmds.add(new UpdateNodeTxCmd(targetGraph, name, properties));
    }

    @Override
    public void deleteNode(String name) throws PMException {
        if (nodes.containsKey(name)) {
            nodes.remove(name);
            pcs.remove(new Node(name, PC));
        }

        cmds.add(new DeleteNodeTxCmd(targetGraph, name));
    }

    @Override
    public boolean exists(String name) throws PMException {
        return nodes.containsKey(name) || targetGraph.exists(name);
    }

    @Override
    public Set<String> getPolicyClasses() throws PMException {
        Set<String> pcs = new HashSet<>();
        for (Node pc : this.pcs) {
            pcs.add(pc.getName());
        }

        pcs.addAll(targetGraph.getPolicyClasses());

        return pcs;
    }

    @Override
    public Set<Node> getNodes() throws PMException {
        Collection<Node> nodes = this.nodes.values();
        Set<Node> nodeSet = new HashSet<>();
        for (Node node : nodes) {
            nodeSet.add(new Node(node));
        }

        nodes = targetGraph.getNodes();
        for (Node node : nodes) {
            nodeSet.add(new Node(node));
        }

        return nodeSet;
    }

    @Override
    public Node getNode(String name) throws PMException {
        if (nodes.containsKey(name)) {
            return new Node(nodes.get(name));
        } else {
            return new Node(targetGraph.getNode(name));
        }
    }

    @Override
    public Node getNode(NodeType type, Map<String, String> properties) throws PMException {
        Set<Node> search = search(type, properties);
        if (search.isEmpty()) {
            throw new PMException("node with type (" + type + ") with properties " + properties + " does not exist");
        }

        return new Node(search.iterator().next());
    }

    @Override
    public Set<Node> search(NodeType type, Map<String, String> properties) throws PMException {
        // check tx first
        Map<String, Node> txNodes = txSearch(type, properties);
        Set<Node> search = targetGraph.search(type, properties);
        for (Node node : search) {
            if (txNodes.containsKey(node.getName())) {
                continue;
            }

            txNodes.put(node.getName(), new Node(node));
        }

        return new HashSet<>(txNodes.values());
    }

    private Map<String, Node> txSearch(NodeType type, Map<String, String> properties) {
        if (properties == null) {
            properties = new HashMap<>();
        }

        Collection<Node> txNodes = nodes.values();
        Map<String, Node> results = new HashMap<>();
        // iterate over the nodes to find ones that match the search parameters
        for (Node node : txNodes) {
            // if the type parameter is not null and the current node type does not equal the type parameter, do not add
            if (type != null && !node.getType().equals(type)) {
                continue;
            }

            boolean add = true;
            for (String key : properties.keySet()) {
                String checkValue = properties.get(key);
                String foundValue = node.getProperties().get(key);
                // if the property provided in the search parameters is null or *, continue to the next property
                if (!(checkValue == null || checkValue.equals("*")) &&
                        (foundValue == null || !foundValue.equals(checkValue))) {
                    add = false;
                    break;
                }
            }

            if (add) {
                results.put(node.getName(), new Node(node));
            }
        }

        return results;
    }

    @Override
    public Set<String> getChildren(String name) throws PMException {
        // get children from the target graph
        Set<String> children = new HashSet<>();
        if (targetGraph.exists(name)) {
            children.addAll(targetGraph.getChildren(name));
        }

        // add the children from the tx
        for (String child : assignments.keySet()) {
            Set<String> parents = assignments.getOrDefault(child, new HashSet<>());
            if (!parents.contains(name)) {
                continue;
            }

            children.add(child);
        }

        // remove and deassigns
        List<TxCmd> cmds = getCmdsOfType(DeassignTxCmd.class);
        for (TxCmd txCmd : cmds) {
            DeassignTxCmd deassignTxCmd = (DeassignTxCmd)txCmd;
            if (!deassignTxCmd.getParent().equals(name)) {
                continue;
            }

            children.remove(deassignTxCmd.getChild());
        }

        return children;
    }

    @Override
    public Set<String> getParents(String name) throws PMException {
        // get children from the target graph
        Set<String> parents = new HashSet<>();
        if (targetGraph.exists(name)) {
            parents.addAll(targetGraph.getParents(name));
        }

        // add the children from the tx
        parents.addAll(assignments.getOrDefault(name, new HashSet<>()));
        // remove and deassigns
        List<TxCmd> cmds = getCmdsOfType(DeassignTxCmd.class);
        for (TxCmd txCmd : cmds) {
            DeassignTxCmd deassignTxCmd = (DeassignTxCmd)txCmd;
            if (!deassignTxCmd.getChild().equals(name)) {
                continue;
            }

            parents.remove(deassignTxCmd.getChild());
        }

        return parents;
    }

    @Override
    public void assign(String child, String parent) throws PMException {
        Set<String> parents = assignments.getOrDefault(child, new HashSet<>());
        parents.add(parent);
        assignments.put(child, parents);
        cmds.add(new AssignTxCmd(targetGraph, child, parent));
    }

    @Override
    public void deassign(String child, String parent) throws PMException {
        Set<String> parents = assignments.getOrDefault(child, new HashSet<>());
        parents.remove(parent);
        assignments.put(child, parents);
        cmds.add(new DeassignTxCmd(targetGraph, child, parent));
    }

    @Override
    public boolean isAssigned(String child, String parent) throws PMException {
        return assignments.getOrDefault(child, new HashSet<>()).contains(parent) ||
                targetGraph.isAssigned(child, parent);
    }

    @Override
    public void associate(String ua, String target, OperationSet operations) throws PMException {
        Map<String, OperationSet> assocs = associations.getOrDefault(ua, new HashMap<>());
        assocs.put(target, operations);
        associations.put(ua, assocs);
        cmds.add(new AssociateTxCmd(targetGraph, ua, target, operations));
    }

    @Override
    public void dissociate(String ua, String target) throws PMException {
        Map<String, OperationSet> assocs = associations.getOrDefault(ua, new HashMap<>());
        assocs.remove(target);
        associations.put(ua, assocs);
        cmds.add(new DissociateTxCmd(targetGraph, ua, target));
    }

    @Override
    public Map<String, OperationSet> getSourceAssociations(String source) throws PMException {
        // get target graph associations
        Map<String, OperationSet> sourceAssociations = targetGraph.getSourceAssociations(source);
        // get tx associations
        sourceAssociations.putAll(associations.getOrDefault(source, new HashMap<>()));
        // remove any dissociates
        List<TxCmd> cmds = getCmdsOfType(DissociateTxCmd.class);
        for (TxCmd cmd : cmds) {
            DissociateTxCmd dissociateTxCmd = (DissociateTxCmd)cmd;
            if (!dissociateTxCmd.getUa().equals(source)) {
                continue;
            }

            sourceAssociations.remove(dissociateTxCmd.getTarget());
        }

        return sourceAssociations;
    }

    @Override
    public Map<String, OperationSet> getTargetAssociations(String target) throws PMException {
        // get target graph associations
        Map<String, OperationSet> targetAssociations = targetGraph.getTargetAssociations(target);
        // get tx associations
        for (String source : associations.keySet()) {
            Map<String, OperationSet> assocs = associations.getOrDefault(source, new HashMap<>());
            if (!assocs.containsKey(target)) {
                continue;
            }

            OperationSet os = assocs.getOrDefault(target, new OperationSet());
            targetAssociations.put(target, os);
        }


        // remove any dissociates
        List<TxCmd> cmds = getCmdsOfType(DissociateTxCmd.class);
        for (TxCmd cmd : cmds) {
            DissociateTxCmd dissociateTxCmd = (DissociateTxCmd)cmd;
            if (!dissociateTxCmd.getUa().equals(target)) {
                continue;
            }

            targetAssociations.remove(dissociateTxCmd.getTarget());
        }

        return targetAssociations;
    }

    @Override
    public String toJson() throws PMException {
        throw new PMException("toJson not implemented for transaction");
    }

    @Override
    public void fromJson(String s) throws PMException {
        throw new PMException("fromJson not implemented for transaction");
    }

    public void commit() throws PMException {
        for (TxCmd txCmd : cmds) {
            txCmd.commit();
        }
    }
}
