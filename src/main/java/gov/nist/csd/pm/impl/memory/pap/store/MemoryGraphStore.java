package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.dag.Direction;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.query.model.subgraph.AscendantSubgraph;
import gov.nist.csd.pm.pap.query.model.subgraph.DescendantSubgraph;
import gov.nist.csd.pm.pap.store.GraphStore;
import gov.nist.csd.pm.pap.store.GraphStoreDFS;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static gov.nist.csd.pm.common.graph.node.NodeType.*;
import static gov.nist.csd.pm.common.graph.node.Properties.WILDCARD;

public class MemoryGraphStore extends MemoryStore implements GraphStore {

    public MemoryGraphStore(MemoryPolicy policy, MemoryTx tx, TxCmdTracker txCmdTracker) {
        super(policy, tx, txCmdTracker);
    }

    @Override
    public void createNode(long id, String name, NodeType type) throws PMException {
        if (type == PC) {
            VertexPolicyClass vertexPolicyClass = new VertexPolicyClass(name);
            policy.addNode(vertexPolicyClass);
        } else if (type == OA || type == UA) {
            VertexAttribute vertexAttribute = new VertexAttribute(name, type);
            policy.addNode(vertexAttribute);
        } else if (type == O || type == U) {
            VertexLeaf vertexLeaf = new VertexLeaf(name, type);
            policy.addNode(vertexLeaf);
        }

        txCmdTracker.trackOp(tx, new TxCmd.CreateNodeTxCmd(name));
    }

    @Override
    public void deleteNode(long id) throws PMException {
        Vertex vertex = policy.graph.get(id);

        if (vertex == null) {
            return;
        }

        long[] descs = vertex.getAdjacentDescendants();
        Association[] incomingAssociations = vertex.getIncomingAssociations();
        Association[] outgoingAssociations = vertex.getOutgoingAssociations();

        for (long desc : descs) {
            deleteAssignment(id, desc);
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

        policy.graph.remove(id);

        if (vertex.getType() == PC) {
            policy.pcs.remove(id);
        }

        txCmdTracker.trackOp(tx, new TxCmd.DeleteNodeTxCmd(
                id,
                new Node(vertex.name, vertex.type, vertex.getProperties()),
                vertex.getAdjacentDescendants()
        ));
    }

    @Override
    public void setNodeProperties(long id, Map<String, String> newProperties) throws PMException {
        Vertex vertex = policy.graph.get(id);
        Map<String, String> oldProperties = vertex.getProperties();

        if (oldProperties.isEmpty() && newProperties.isEmpty()) {
            return;
        }

        if (vertex instanceof VertexWithProps vertexWithProps) {
            if (newProperties.isEmpty()) {
                policy.graph.put(id, vertexWithProps.getVertex());
            } else {
                policy.graph.put(id, new VertexWithProps(vertexWithProps.getVertex(), newProperties));
            }
        } else {
            policy.graph.put(id, new VertexWithProps(vertex, newProperties));
        }

        txCmdTracker.trackOp(tx, new TxCmd.SetNodePropertiesTxCmd(id, oldProperties, newProperties));
    }

    @Override
    public void createAssignment(long start, long end) throws PMException {
        policy.graph.get(start).addAssignment(start, end);
        policy.graph.get(end).addAssignment(start, end);

        txCmdTracker.trackOp(tx, new TxCmd.CreateAssignmentTxCmd(start, end));
    }

    @Override
    public void deleteAssignment(long start, long end) throws PMException {
        policy.graph.get(start).deleteAssignment(start, end);
        policy.graph.get(end).deleteAssignment(start, end);

        txCmdTracker.trackOp(tx, new TxCmd.DeleteAssignmentTxCmd(start, end));
    }

    @Override
    public void createAssociation(long ua, long target, AccessRightSet arset) throws PMException {
        deleteAssociation(ua, target);
        policy.graph.get(ua).addAssociation(ua, target, arset);
        policy.graph.get(target).addAssociation(ua, target, arset);

        txCmdTracker.trackOp(tx, new TxCmd.CreateAssociationTxCmd(ua, target));
    }

    @Override
    public void deleteAssociation(long ua, long target) throws PMException {
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
    public Node getNodeById(long id) throws PMException {
        Vertex vertex = policy.graph.get(id);
        return new Node(vertex.getName(), vertex.getType(), vertex.getProperties());
    }

    @Override
    public Node getNodeByName(String name) throws PMException {
        return policy.graph.get();
    }

    @Override
    public boolean nodeExists(long id) throws PMException {
        return false;
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        return policy.graph.containsKey(name);
    }

    @Override
    public long[] search(NodeType type, Map<String, String> properties) throws PMException {
        List<String> nodes = filterByType(type);
        return filterByProperties(nodes, properties);
    }

    @Override
    public long[] getPolicyClasses() throws PMException {
        return new ArrayList<>(policy.pcs);
    }

    @Override
    public long[] getAdjacentDescendants(long id) throws PMException {
        return policy.graph.get(id).getAdjacentDescendants();
    }

    @Override
    public long[] getAdjacentAscendants(long id) throws PMException {
        return policy.graph.get(id).getAdjacentAscendants();
    }

    @Override
    public Association[] getAssociationsWithSource(long uaId) throws PMException {
        return policy.graph.get(uaId).getOutgoingAssociations();
    }

    @Override
    public Collection<Association> getAssociationsWithTarget(long targetId) throws PMException {
        return policy.graph.get(targetId).getIncomingAssociations();
    }

    @Override
    public Collection<String> getPolicyClassDescendants(long id) throws PMException {
        Set<String> pcs = new HashSet<>();

        new GraphStoreDFS(this)
                .withDirection(Direction.DESCENDANTS)
                .withVisitor((n) -> {
                    Node visitedNode;
                    visitedNode = getNodeById(n);
                    if (visitedNode.getType().equals(PC)) {
                        pcs.add(n);
                    }
                })
                .walk(id);

        pcs.remove(id);

        return pcs;
    }

    @Override
    public Collection<String> getAttributeDescendants(long id) throws PMException {
        Set<String> attrs = new HashSet<>();

        new GraphStoreDFS(this)
                .withDirection(Direction.DESCENDANTS)
                .withVisitor((n) -> {
                    Node visitedNode;
                    visitedNode = getNodeById(n);
                    if (visitedNode.getType().equals(UA) ||
                            visitedNode.getType().equals(OA)) {
                        attrs.add(n);
                    }
                })
                .walk(id);

        attrs.remove(id);

        return attrs;
    }

    @Override
    public DescendantSubgraph getDescendantSubgraph(long id) throws PMException {
        List<DescendantSubgraph> adjacentSubgraphs = new ArrayList<>();

        Collection<String> adjacentDescendants = getAdjacentDescendants(id);
        for (String adjacent : adjacentDescendants) {
            adjacentSubgraphs.add(getDescendantSubgraph(adjacent));
        }

        return new DescendantSubgraph(id, adjacentSubgraphs);
    }

    @Override
    public AscendantSubgraph getAscendantSubgraph(long id) throws PMException {
        List<AscendantSubgraph> adjacentSubgraphs = new ArrayList<>();

        Collection<String> adjacentAscendants = getAdjacentAscendants(id);
        for (String adjacent : adjacentAscendants) {
            adjacentSubgraphs.add(getAscendantSubgraph(adjacent));
        }

        return new AscendantSubgraph(id, adjacentSubgraphs);
    }

    @Override
    public boolean isAscendant(long asc, long dsc) throws PMException {
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
    public boolean isDescendant(long asc, long dsc) throws PMException {
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
                Map<String, String> nodeProperties = getNodeById(n).getProperties();

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