package gov.nist.csd.pm.core.impl.memory.pap.store;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.ANY;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.O;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.OA;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.PC;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.U;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.UA;
import static gov.nist.csd.pm.core.common.graph.node.Properties.WILDCARD;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.Direction;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.operation.accessrights.AccessRightSet;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.pap.query.model.subgraph.Subgraph;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import gov.nist.csd.pm.core.pap.store.GraphStoreDFS;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class MemoryGraphStore extends MemoryStore implements GraphStore {

    public MemoryGraphStore(MemoryPolicy policy, MemoryTx tx, TxCmdTracker txCmdTracker) {
        super(policy, tx, txCmdTracker);
    }

    @Override
    public void createNode(long id, String name, NodeType type) throws PMException {
        if (type == PC) {
            VertexPolicyClass vertexPolicyClass = new VertexPolicyClass(id, name);
            policy.addNode(vertexPolicyClass);
        } else if (type == OA || type == UA) {
            VertexAttribute vertexAttribute = new VertexAttribute(id, name, type);
            policy.addNode(vertexAttribute);
        } else if (type == O || type == U) {
            VertexLeaf vertexLeaf = new VertexLeaf(id, name, type);
            policy.addNode(vertexLeaf);
        }

        txCmdTracker.trackOp(tx, new TxCmd.CreateNodeTxCmd(id));
    }

    @Override
    public void deleteNode(long id) throws PMException {
        Vertex vertex = policy.graph.get(id);

        if (vertex == null) {
            return;
        }

        Collection<Long> descs = new ArrayList<>(vertex.getAdjacentDescendants());
        Collection<Association> incomingAssociations = new ArrayList<>(vertex.getIncomingAssociations());
        Collection<Association> outgoingAssociations = new ArrayList<>(vertex.getOutgoingAssociations());

        for (long desc : descs) {
            deleteAssignment(id, desc);
        }

        for (Association association : incomingAssociations) {
            Vertex v = policy.graph.get(association.source());
            if(v == null) {
                continue;
            }

            deleteAssociation(association.source(), association.target());
        }

        for (Association association : outgoingAssociations) {
            Vertex v = policy.graph.get(association.target());
            if(v == null) {
                continue;
            }

            deleteAssociation(association.source(), association.target());
        }

        policy.graph.remove(id);
        policy.nameToIds.remove(vertex.getName());

        if (vertex.getType() == PC) {
            policy.pcs.remove(id);
        }

        txCmdTracker.trackOp(tx, new TxCmd.DeleteNodeTxCmd(
                id,
                new Node(id, vertex.name, vertex.type, vertex.getProperties()),
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
                policy.graph.put(id, new VertexWithProps(id, vertexWithProps.getVertex(), newProperties));
            }
        } else {
            policy.graph.put(id, new VertexWithProps(id, vertex, newProperties));
        }

        txCmdTracker.trackOp(tx, new TxCmd.SetNodePropertiesTxCmd(id, oldProperties));
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
            if (association.target() == target) {
                accessRightSet = association.arset();
            }
        }

        policy.graph.get(ua).deleteAssociation(ua, target);
        policy.graph.get(target).deleteAssociation(ua, target);

        txCmdTracker.trackOp(tx, new TxCmd.DeleteAssociationTxCmd(ua, target, accessRightSet));
    }

    @Override
    public Node getNodeById(long id) throws PMException {
        Vertex vertex = policy.graph.get(id);
        return new Node(vertex.getId(), vertex.getName(), vertex.getType(), vertex.getProperties());
    }

    @Override
    public Node getNodeByName(String name) throws PMException {
        Vertex vertex = policy.graph.get(policy.nameToIds.get(name));
        return new Node(vertex.getId(), vertex.getName(), vertex.getType(), vertex.getProperties());
    }

    @Override
    public boolean nodeExists(long id) throws PMException {
        return policy.graph.containsKey(id);
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        return policy.nameToIds.containsKey(name);
    }

    @Override
    public Collection<Long> search(NodeType type, Map<String, String> properties) throws PMException {
        LongOpenHashSet nodes = filterByType(type);
        return filterByProperties(nodes, properties);
    }

    @Override
    public Collection<Long> getPolicyClasses() throws PMException {
        return policy.pcs;
    }

    @Override
    public Collection<Long> getAdjacentDescendants(long id) throws PMException {
        return policy.graph.get(id).getAdjacentDescendants();
    }

    @Override
    public Collection<Long> getAdjacentAscendants(long id) throws PMException {
        return policy.graph.get(id).getAdjacentAscendants();
    }

    @Override
    public Collection<Association> getAssociationsWithSource(long uaId) throws PMException {
        return policy.graph.get(uaId).getOutgoingAssociations();
    }

    @Override
    public Collection<Association> getAssociationsWithTarget(long targetId) throws PMException {
        return policy.graph.get(targetId).getIncomingAssociations();
    }

    @Override
    public Collection<Long> getPolicyClassDescendants(long id) throws PMException {
        LongOpenHashSet pcs = new LongOpenHashSet();

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
    public Collection<Long> getAttributeDescendants(long id) throws PMException {
        LongOpenHashSet attrs = new LongOpenHashSet();

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
    public Subgraph getDescendantSubgraph(long id) throws PMException {
        List<Subgraph> adjacentSubgraphs = new ArrayList<>();

        Collection<Long> adjacentDescendants = getAdjacentDescendants(id);
        for (long adjacent : adjacentDescendants) {
            adjacentSubgraphs.add(getDescendantSubgraph(adjacent));
        }

        return new Subgraph(getNodeById(id), adjacentSubgraphs);
    }

    @Override
    public Subgraph getAscendantSubgraph(long id) throws PMException {
        List<Subgraph> adjacentSubgraphs = new ArrayList<>();

        Collection<Long> adjacentAscendants = getAdjacentAscendants(id);
        for (long adjacent : adjacentAscendants) {
            adjacentSubgraphs.add(getAscendantSubgraph(adjacent));
        }

        return new Subgraph(getNodeById(id), adjacentSubgraphs);
    }

    @Override
    public boolean isAscendant(long asc, long dsc) throws PMException {
        AtomicBoolean found = new AtomicBoolean(false);

        new GraphStoreDFS(this)
                .withDirection(Direction.ASCENDANTS)
                .withVisitor((n) -> {
                    if (n == asc) {
                        found.set(true);
                    }
                })
                .withAllPathShortCircuit(n -> n == asc)
                .walk(dsc);

        return found.get();
    }

    @Override
    public boolean isDescendant(long asc, long dsc) throws PMException {
        AtomicBoolean found = new AtomicBoolean(false);

        new GraphStoreDFS(this)
                .withDirection(Direction.DESCENDANTS)
                .withVisitor((n) -> {
                    if (n == dsc) {
                        found.set(true);
                    }
                })
                .withAllPathShortCircuit(n -> n == dsc)
                .walk(asc);

        return found.get();
    }

    private LongOpenHashSet filterByProperties(LongOpenHashSet nodes, Map<String, String> properties) throws PMException {
        LongOpenHashSet results = new LongOpenHashSet();

        if (properties.isEmpty()) {
            results.addAll(nodes);
        } else {
            for (long n : nodes) {
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

    private LongOpenHashSet filterByType(NodeType type) {
        LongOpenHashSet nodes = new LongOpenHashSet();

        // return all nodes if type is ANY
        if (type == ANY) {
            Set<Long> ids = policy.graph.keySet();

            for (long id : ids) {
                nodes.add(id);
            }

            return nodes;
        }

        // return pcs from separate set if type is PC
        if (type == PC) {
            nodes.addAll(policy.pcs);

            return nodes;
        }

        // get other node types that match
        for (Map.Entry<Long, Vertex> node : policy.graph.entrySet()) {
            Vertex vertex = node.getValue();
            if (vertex.type == type) {
                nodes.add(node.getKey().longValue());
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