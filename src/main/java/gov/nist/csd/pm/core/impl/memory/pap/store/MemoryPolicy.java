package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.operation.Operation;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryPolicy {

    protected Map<Long, Vertex> graph;
    protected Map<String, Long> nameToIds;
    protected AccessRightSet resourceAccessRights;
    protected LongArraySet pcs;
    protected Map<Long, Collection<Prohibition>> nodeProhibitions;
    protected Map<String, Collection<Prohibition>> processProhibitions;
    protected List<Obligation> obligations;
    protected Map<String, Operation<?>> operations;

    public MemoryPolicy() {
        reset();
    }

    public void reset() {
        this.graph = new Long2ObjectOpenHashMap<>();
        this.nameToIds = new Object2LongOpenHashMap<>();
        this.pcs = new LongArraySet();
        this.resourceAccessRights = new AccessRightSet();
        this.nodeProhibitions = new HashMap<>();
        this.processProhibitions = new HashMap<>();
        this.obligations = new ArrayList<>();
        this.operations = new HashMap<>();
    }

    public void addNode(Vertex vertex) {
        graph.put(vertex.getId(), vertex);
        nameToIds.put(vertex.getName(), vertex.getId());

        if (vertex.getType() == NodeType.PC) {
            pcs.add(vertex.getId());
        }
    }

    public void addProhibition(Prohibition prohibition) {
        ProhibitionSubject subject = prohibition.getSubject();

        if (subject.isNode()) {
            nodeProhibitions.computeIfAbsent(subject.getNodeId(), k -> new ArrayList<>()).add(prohibition);
        } else {
            processProhibitions.computeIfAbsent(subject.getProcess(), k -> new ArrayList<>()).add(prohibition);
        }
    }

    public void deleteProhibition(Prohibition prohibition) {
        ProhibitionSubject subject = prohibition.getSubject();
        if (subject.isNode()) {
            removeProhibitionFromMap(nodeProhibitions, subject.getNodeId(), prohibition);
        } else {
            removeProhibitionFromMap(processProhibitions, subject.getProcess(), prohibition);
        }
    }

    private void removeProhibitionFromMap(Map<?, Collection<Prohibition>> map, Object key, Prohibition value) {
        if (map.containsKey(key)) {
            Collection<Prohibition> list = map.get(key);
            list.remove(value);

            if (list.isEmpty()) {
                map.remove(key);
            }
        }
    }
}
