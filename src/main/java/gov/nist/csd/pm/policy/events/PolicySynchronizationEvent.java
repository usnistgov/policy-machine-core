package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.model.graph.Graph;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.author.pal.PALContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicySynchronizationEvent extends PolicyEvent {

    private final Graph graph;
    private final Map<String, List<Prohibition>> prohibitions;
    private final List<Obligation> obligations;
    private final PALContext palCtx;

    public PolicySynchronizationEvent(Graph graph, Map<String, List<Prohibition>> prohibitions,
                                      List<Obligation> obligations, PALContext palCtx) {
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.palCtx = palCtx;
    }

    public Graph getGraph() {
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
}
