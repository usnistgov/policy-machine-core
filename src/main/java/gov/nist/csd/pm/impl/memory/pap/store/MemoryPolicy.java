package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.routine.Routine;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.*;

public class MemoryPolicy {

    protected Map<String, Vertex> graph;
    protected AccessRightSet resourceOperations;
    protected Set<String> pcs;
    protected Map<String, Collection<Prohibition>> prohibitions;
    protected List<Obligation> obligations;
    protected Map<String, Operation<?>> operations;
    protected Map<String, Routine<?>> routines;

    public MemoryPolicy() {
        reset();
    }

    public void reset() {
        this.graph = new Object2ObjectOpenHashMap<>();
        this.pcs = new ObjectOpenHashSet<>();
        this.resourceOperations = new AccessRightSet();
        this.prohibitions = new HashMap<>();
        this.obligations = new ArrayList<>();
        this.operations = new HashMap<>();
        this.routines = new HashMap<>();
    }
}
