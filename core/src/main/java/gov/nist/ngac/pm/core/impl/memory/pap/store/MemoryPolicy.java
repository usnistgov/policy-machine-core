/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.impl.memory.pap.store;

import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds the in-memory policy state shared by the memory stores.
 */
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

    /**
     * Resets the policy state to empty.
     */
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

    /**
     * Adds a vertex to the graph, indexing it by id and name. Also tracks it as a policy class if its
     * type is PC.
     *
     * @param vertex the vertex to add
     */
    public void addNode(Vertex vertex) {
        graph.put(vertex.getId(), vertex);
        nameToIds.put(vertex.getName(), vertex.getId());

        if (vertex.getType() == NodeType.PC) {
            pcs.add(vertex.getId());
        }
    }

    /**
     * Indexes a prohibition by its node or process, depending on its type.
     *
     * @param prohibition the prohibition to add
     */
    public void addProhibition(Prohibition prohibition) {
        switch (prohibition) {
            case NodeProhibition nodeProhibition ->
                nodeProhibitions.computeIfAbsent(nodeProhibition.getNodeId(), k -> new ArrayList<>()).add(prohibition);
            case ProcessProhibition processProhibition ->
                processProhibitions.computeIfAbsent(processProhibition.getProcess(), k -> new ArrayList<>()).add(prohibition);
        }
    }

    /**
     * Removes a prohibition added by {@link #addProhibition}.
     *
     * @param prohibition the prohibition to remove
     */
    public void deleteProhibition(Prohibition prohibition) {
        switch (prohibition) {
            case NodeProhibition nodeProhibition ->
                removeProhibitionFromMap(nodeProhibitions, nodeProhibition.getNodeId(), prohibition);
            case ProcessProhibition processProhibition ->
                removeProhibitionFromMap(processProhibitions, processProhibition.getProcess(), prohibition);
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
