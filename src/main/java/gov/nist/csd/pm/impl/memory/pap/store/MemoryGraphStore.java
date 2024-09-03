package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.dag.Direction;
import gov.nist.csd.pm.pap.graph.node.Node;
import gov.nist.csd.pm.pap.graph.node.NodeType;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.graph.relationship.Association;
import gov.nist.csd.pm.pap.store.GraphStore;
import gov.nist.csd.pm.pap.store.GraphStoreDFS;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static gov.nist.csd.pm.pap.graph.node.NodeType.*;
import static gov.nist.csd.pm.pap.graph.node.Properties.WILDCARD;

public class MemoryGraphStore extends MemoryStore implements GraphStore {

    public MemoryGraphStore(MemoryPolicy policy, MemoryTx tx, TxCmdTracker txCmdTracker) {
        super(policy, tx, txCmdTracker);
    }

    @Override
    public void createNode(String name, NodeType type) throws PMException {

        if (type == PC) {
            policy.pcs.add(name);
            VertexPolicyClass vertexPolicyClass = new VertexPolicyClass(name);
            policy.graph.put(name, vertexPolicyClass);
        } else if (type == OA || type == UA) {
            VertexAttribute vertexAttribute = new VertexAttribute(name, type);
            policy.graph.put(name, vertexAttribute);
        } else if (type == O || type == U) {
            VertexLeaf vertexLeaf = new VertexLeaf(name, type);
            policy.graph.put(name, vertexLeaf);
        }

        txCmdTracker.trackOp(tx, new TxCmd.CreateNodeTxCmd(name));
    }

    @Override
    public void deleteNode(String name) throws PMException {
        Vertex vertex = policy.graph.get(name);

        if (vertex == null) {
            return;
        }

        Collection<String> descs = vertex.getAdjacentDescendants();
        Collection<Association> incomingAssociations = vertex.getIncomingAssociations();
        Collection<Association> outgoingAssociations = vertex.getOutgoingAssociations();

        for (String desc : descs) {
            deleteAssignment(name, desc);
        }

        for (Association association : incomingAssociations) {
            Vertex v = policy.graph.get(association.getSource());
            if(v == null) {
                continue;
            }

            deleteAssociation(association.getSource(), association.getTarget());
        }

        for (Association association : outgoingAssociations) {
            Vertex v = policy.graph.get(association.getTarget());
            if(v == null) {
                continue;
            }

            deleteAssociation(association.getSource(), association.getTarget());
        }

        policy.graph.remove(name);

        if (vertex.getType() == PC) {
            policy.pcs.remove(name);
        }

        txCmdTracker.trackOp(tx, new TxCmd.DeleteNodeTxCmd(
                name,
                new Node(vertex.name, vertex.type, vertex.getProperties()),
                vertex.getAdjacentDescendants()
        ));
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> newProperties) throws PMException {
        Vertex vertex = policy.graph.get(name);
        Map<String, String> oldProperties = vertex.getProperties();

        if (oldProperties.isEmpty() && newProperties.isEmpty()) {
            return;
        }

        if (vertex instanceof VertexWithProps) {
            VertexWithProps vertexWithProps = (VertexWithProps) vertex;
            if (newProperties.isEmpty()) {
                policy.graph.put(name, vertexWithProps.getVertex());
            } else {
                policy.graph.put(name, new VertexWithProps(vertexWithProps.getVertex(), newProperties));
            }
        } else {
            policy.graph.put(name, new VertexWithProps(vertex, newProperties));
        }

        txCmdTracker.trackOp(tx, new TxCmd.SetNodePropertiesTxCmd(name, oldProperties, newProperties));
    }

    @Override
    public void createAssignment(String start, String end) throws PMException {
        policy.graph.get(start).addAssignment(start, end);
        policy.graph.get(end).addAssignment(start, end);

        txCmdTracker.trackOp(tx, new TxCmd.CreateAssignmentTxCmd(start, end));
    }

    @Override
    public void deleteAssignment(String start, String end) throws PMException {
        policy.graph.get(start).deleteAssignment(start, end);
        policy.graph.get(end).deleteAssignment(start, end);

        txCmdTracker.trackOp(tx, new TxCmd.DeleteAssignmentTxCmd(start, end));
    }

    @Override
    public void createAssociation(String ua, String target, AccessRightSet arset) throws PMException {
        deleteAssociation(ua, target);
        policy.graph.get(ua).addAssociation(ua, target, arset);
        policy.graph.get(target).addAssociation(ua, target, arset);

        txCmdTracker.trackOp(tx, new TxCmd.CreateAssociationTxCmd(ua, target));
    }

    @Override
    public void deleteAssociation(String ua, String target) throws PMException {
        Vertex vertex = policy.graph.get(ua);

        AccessRightSet accessRightSet = new AccessRightSet();
        for (Association association : vertex.getOutgoingAssociations()) {
            if (association.getTarget().equals(target)) {
                accessRightSet = association.getAccessRightSet();
            }
        }

        policy.graph.get(ua).deleteAssociation(ua, target);
        policy.graph.get(target).deleteAssociation(ua, target);

        txCmdTracker.trackOp(tx, new TxCmd.DeleteAssociationTxCmd(ua, target, accessRightSet));
    }

    @Override
    public Node getNode(String name) throws PMException {
        Vertex vertex = policy.graph.get(name);
        return new Node(vertex.getName(), vertex.getType(), vertex.getProperties());
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        return policy.graph.containsKey(name);
    }

    @Override
    public Collection<String> search(NodeType type, Map<String, String> properties) throws PMException {
        List<String> nodes = filterByType(type);
        return filterByProperties(nodes, properties);
    }

    @Override
    public Collection<String> getPolicyClasses() throws PMException {
        return new ArrayList<>(policy.pcs);
    }

    @Override
    public Collection<String> getAdjacentDescendants(String name) throws PMException {
        return policy.graph.get(name).getAdjacentDescendants();
    }

    @Override
    public Collection<String> getAdjacentAscendants(String name) throws PMException {
        return policy.graph.get(name).getAdjacentAscendants();
    }

    @Override
    public Collection<Association> getAssociationsWithSource(String ua) throws PMException {
        return policy.graph.get(ua).getOutgoingAssociations();
    }

    @Override
    public Collection<Association> getAssociationsWithTarget(String target) throws PMException {
        return policy.graph.get(target).getIncomingAssociations();
    }

    @Override
    public Collection<String> getAscendants(String node) throws PMException {
        Set<String> ascs = new HashSet<>();

        new GraphStoreDFS(this)
                .withDirection(Direction.ASCENDANTS)
                .withVisitor(n -> {
                    ascs.add(n);
                })
                .walk(node);

        ascs.remove(node);

        return ascs;
    }

    @Override
    public Collection<String> getPolicyClassDescendants(String node) throws PMException {
        Set<String> pcs = new HashSet<>();

        new GraphStoreDFS(this)
                .withDirection(Direction.DESCENDANTS)
                .withVisitor((n) -> {
                    Node visitedNode;
                    visitedNode = getNode(n);
                    if (visitedNode.getType().equals(PC)) {
                        pcs.add(n);
                    }
                })
                .walk(node);

        pcs.remove(node);

        return pcs;
    }

    @Override
    public Collection<String> getAttributeDescendants(String node) throws PMException {
        Set<String> attrs = new HashSet<>();

        new GraphStoreDFS(this)
                .withDirection(Direction.DESCENDANTS)
                .withVisitor((n) -> {
                    Node visitedNode;
                    visitedNode = getNode(n);
                    if (visitedNode.getType().equals(UA) ||
                            visitedNode.getType().equals(OA)) {
                        attrs.add(n);
                    }
                })
                .walk(node);

        attrs.remove(node);

        return attrs;
    }

    @Override
    public Collection<String> getDescendants(String node) throws PMException {
        Set<String> descs = new HashSet<>();

        new GraphStoreDFS(this)
                .withDirection(Direction.DESCENDANTS)
                .withVisitor(descs::add)
                .walk(node);

        descs.remove(node);

        return descs;
    }

    @Override
    public boolean isAscendant(String asc, String dsc) throws PMException {
        AtomicBoolean found = new AtomicBoolean(false);

        new GraphStoreDFS(this)
                .withDirection(Direction.ASCENDANTS)
                .withVisitor((n) -> {
                    if (n.equals(asc)) {
                        found.set(true);
                    }
                })
                .withAllPathShortCircuit(n -> n.equals(asc))
                .walk(dsc);

        return found.get();
    }

    @Override
    public boolean isDescendant(String asc, String dsc) throws PMException {
        AtomicBoolean found = new AtomicBoolean(false);

        new GraphStoreDFS(this)
                .withDirection(Direction.DESCENDANTS)
                .withVisitor((n) -> {
                    if (n.equals(dsc)) {
                        found.set(true);
                    }
                })
                .withAllPathShortCircuit(n -> n.equals(dsc))
                .walk(asc);

        return found.get();
    }

    private List<String> filterByProperties(List<String> nodes, Map<String, String> properties) throws PMException {
        List<String> results = new ArrayList<>();
        if (properties.isEmpty()) {
            results.addAll(nodes);
        } else {
            for (String n : nodes) {
                Map<String, String> nodeProperties = getNode(n).getProperties();

                if (!hasAllKeys(nodeProperties, properties)
                        || !valuesMatch(nodeProperties, properties)) {
                    continue;
                }

                results.add(n);
            }
        }

        return results;
    }

    private List<String> filterByType(NodeType type) {
        List<String> nodes = new ArrayList<>();

        // return all nodes if type is ANY
        if (type == ANY) {
            for (Map.Entry<String, Vertex> node : policy.graph.entrySet()) {
                nodes.add(node.getKey());
            }

            return nodes;
        }

        // return pcs from separate set if type is PC
        if (type == PC) {
            nodes.addAll(policy.pcs);

            return nodes;
        }

        // get other node types that match
        for (Map.Entry<String, Vertex> node : policy.graph.entrySet()) {
            Vertex vertex = node.getValue();
            if (vertex.type == type) {
                nodes.add(node.getKey());
            }
        }

        return nodes;
    }

    private boolean valuesMatch(Map<String, String> nodeProperties, Map<String, String> checkProperties) {
        for (Map.Entry<String, String> entry : checkProperties.entrySet()) {
            String checkKey = entry.getKey();
            String checkValue = entry.getValue();
            if (!checkValue.equals(nodeProperties.get(checkKey))
                    && !checkValue.equals(WILDCARD)) {
                return false;
            }
        }

        return true;
    }

    private boolean hasAllKeys(Map<String, String> nodeProperties, Map<String, String> checkProperties) {
        for (String key : checkProperties.keySet()) {
            if (!nodeProperties.containsKey(key)) {
                return false;
            }
        }

        return true;
    }
}