package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.modification.*;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pdp.Adjudicator;

public class PolicyModificationAdjudicator extends Adjudicator implements PolicyModification {

    private final GraphModificationAdjudicator graph;
    private final ProhibitionsModificationAdjudicator prohibitions;
    private final ObligationsModificationAdjudicator obligations;
    private final OperationsModificationAdjudicator operations;
    private final RoutinesModificationAdjudicator routines;

    public PolicyModificationAdjudicator(UserContext userCtx, PAP pap, EventEmitter eventEmitter, PrivilegeChecker privilegeChecker) throws PMException {
        super(privilegeChecker);
        this.graph = new GraphModificationAdjudicator(userCtx, pap, eventEmitter, privilegeChecker);
        this.prohibitions = new ProhibitionsModificationAdjudicator(userCtx, pap, eventEmitter, privilegeChecker);
        this.obligations = new ObligationsModificationAdjudicator(userCtx, pap, eventEmitter, privilegeChecker);
        this.operations = new OperationsModificationAdjudicator(userCtx, pap, eventEmitter, privilegeChecker);
        this.routines = new RoutinesModificationAdjudicator(userCtx, pap, eventEmitter, privilegeChecker);
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

    @Override
    public RoutinesModificationAdjudicator routines() {
        return routines;
    }

}
