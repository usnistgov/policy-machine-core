package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.modification.*;
import gov.nist.csd.pm.pap.query.UserContext;

public class PolicyModificationAdjudicator extends PolicyModifier {

    private final GraphModificationAdjudicator graph;
    private final ProhibitionsModificationAdjudicator prohibitions;
    private final ObligationsModificationAdjudicator obligations;
    private final OperationsModificationAdjudicator operations;
    private final RoutinesModificationAdjudicator routines;

    public PolicyModificationAdjudicator(UserContext userCtx, PAP pap, EventEmitter eventEmitter) throws PMException {
        super(pap.modify());
        this.graph = new GraphModificationAdjudicator(userCtx, pap, eventEmitter);
        this.prohibitions = new ProhibitionsModificationAdjudicator(userCtx, pap, eventEmitter);
        this.obligations = new ObligationsModificationAdjudicator(userCtx, pap, eventEmitter);
        this.operations = new OperationsModificationAdjudicator(userCtx, pap, eventEmitter);
        this.routines = new RoutinesModificationAdjudicator(userCtx, pap, eventEmitter);
    }

    @Override
    public GraphModifier graph() {
        return graph;
    }

    @Override
    public ProhibitionsModifier prohibitions() {
        return prohibitions;
    }

    @Override
    public ObligationsModifier obligations() {
        return obligations;
    }

    @Override
    public OperationsModifier operations() {
        return operations;
    }

    @Override
    public RoutinesModifier routines() {
        return routines;
    }

}
