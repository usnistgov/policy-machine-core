package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.model.graph.MemoryGraph;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.author.pal.PALContext;

import java.util.List;
import java.util.Map;

public class PolicySynchronizationEvent implements PolicyEvent {

    private final MemoryGraph graph;
    private final Map<String, List<Prohibition>> prohibitions;
    private final List<Obligation> obligations;
    private final PALContext palCtx;

    public PolicySynchronizationEvent(MemoryGraph graph, Map<String, List<Prohibition>> prohibitions,
                                      List<Obligation> obligations, PALContext palCtx) {
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.palCtx = palCtx;
    }

    public MemoryGraph getGraph() {
        return graph;
    }

    public Map<String, List<Prohibition>> getProhibitions() {
        return prohibitions;
    }

    public List<Obligation> getObligations() {
        return obligations;
    }

    public PALContext getPALContext() {
        return palCtx;
    }

    @Override
    public String getEventName() {
        return "policy_sync";
    }
}
