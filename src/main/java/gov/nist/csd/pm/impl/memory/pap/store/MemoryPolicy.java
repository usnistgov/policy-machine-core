package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.common.routine.Routine;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import java.util.*;

public class MemoryPolicy {

    protected Long2ObjectOpenHashMap<Vertex> graph;
    protected Object2LongOpenHashMap<String> nameToIds;
    protected AccessRightSet resourceOperations;
    protected LongArraySet pcs;
    protected Map<Node, Collection<Prohibition>> nodeProhibitions;
    protected Map<String, Collection<Prohibition>> processProhibitions;
    protected List<Obligation> obligations;
    protected Map<String, Operation<?>> operations;
    protected Map<String, Routine<?>> routines;

    public MemoryPolicy() {
        reset();
    }

    public void reset() {
        this.graph = new Long2ObjectOpenHashMap<>();
        this.nameToIds = new Object2LongOpenHashMap<>();
        this.pcs = new LongArraySet();
        this.resourceOperations = new AccessRightSet();
        this.nodeProhibitions = new HashMap<>();
        this.obligations = new ArrayList<>();
        this.operations = new HashMap<>();
        this.routines = new HashMap<>();
    }

    public void addNode(Vertex vertex) {
        graph.put(vertex.gteId(), vertex);
        nameToIds.put(vertex.getName(), vertex.gteId());

        if (vertex.getType() == NodeType.PC) {
            pcs.add(vertex.gteId());
        }
    }

    public void addProhibition(Prohibition prohibition) {
        ProhibitionSubject subject = prohibition.getSubject();

        if (subject.isNode()) {
            Node node = vertexToNode(graph.get(subject.getNodeId()));
            nodeProhibitions.computeIfAbsent(node, k -> new ArrayList<>()).add(prohibition);
        } else {
            processProhibitions.computeIfAbsent(subject.getProcess(), k -> new ArrayList<>()).add(prohibition);
        }
    }

    public void deleteProhibition(Prohibition prohibition) {
        ProhibitionSubject subject = prohibition.getSubject();
        if (subject.isNode()) {
            Node node = vertexToNode(graph.get(subject.getNodeId()));
            removeProhibitionFromMap(nodeProhibitions, node, prohibition);
        } else {
            removeProhibitionFromMap(processProhibitions, subject.getProcess(), prohibition);
        }
    }

    private void removeProhibitionFromMap(Map<?, Collection<Prohibition>> map, Object key, Prohibition value) {
        // Check if the key exists and has a list
        if (map.containsKey(key)) {
            Collection<Prohibition> list = map.get(key);

            // Remove the specific string if it exists in the list
            list.remove(value);

            // Optional: Remove key if list is empty
            if (list.isEmpty()) {
                map.remove(key);
            }
        }
    }

    private Node vertexToNode(Vertex vertex) {
        return new Node(vertex.getName(), vertex.getType(), vertex.getProperties());
    }
}
