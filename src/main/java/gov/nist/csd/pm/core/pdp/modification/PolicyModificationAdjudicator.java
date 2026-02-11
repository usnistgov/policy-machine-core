package gov.nist.csd.pm.core.pdp.modification;

import gov.nist.csd.pm.core.common.event.EventPublisher;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.modification.PolicyModification;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDPEventPublisher;

public class PolicyModificationAdjudicator implements PolicyModification {

    private final GraphModificationAdjudicator graph;
    private final ProhibitionsModificationAdjudicator prohibitions;
    private final ObligationsModificationAdjudicator obligations;
    private final OperationsModificationAdjudicator operations;

    public PolicyModificationAdjudicator(UserContext userCtx, PAP pap, PDPEventPublisher eventPublisher) {
        this.graph = new GraphModificationAdjudicator(userCtx, pap, eventPublisher);
        this.prohibitions = new ProhibitionsModificationAdjudicator(userCtx, pap);
        this.obligations = new ObligationsModificationAdjudicator(userCtx, pap);
        this.operations = new OperationsModificationAdjudicator(userCtx, pap);
    }

    public PolicyModificationAdjudicator(GraphModificationAdjudicator graph,
                                         ProhibitionsModificationAdjudicator prohibitions,
                                         ObligationsModificationAdjudicator obligations,
                                         OperationsModificationAdjudicator operations) {
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.operations = operations;
    }

    @Override
    public GraphModificationAdjudicator graph() {
        return graph;
    }

    @Override
    public ProhibitionsModificationAdjudicator prohibitions() {
        return prohibitions;
    }

    @Override
    public ObligationsModificationAdjudicator obligations() {
        return obligations;
    }

    @Override
    public OperationsModificationAdjudicator operations() {
        return operations;
    }

}
