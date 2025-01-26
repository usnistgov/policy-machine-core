package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.event.EventPublisher;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.modification.*;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

public class PolicyModificationAdjudicator extends Adjudicator implements PolicyModification {

    private final GraphModificationAdjudicator graph;
    private final ProhibitionsModificationAdjudicator prohibitions;
    private final ObligationsModificationAdjudicator obligations;
    private final OperationsModificationAdjudicator operations;
    private final RoutinesModificationAdjudicator routines;

    public PolicyModificationAdjudicator(UserContext userCtx, PAP pap, EventPublisher eventPublisher, PrivilegeChecker privilegeChecker) throws PMException {
        super(privilegeChecker);
        this.graph = new GraphModificationAdjudicator(userCtx, pap, eventPublisher, privilegeChecker);
        this.prohibitions = new ProhibitionsModificationAdjudicator(userCtx, pap, privilegeChecker);
        this.obligations = new ObligationsModificationAdjudicator(userCtx, pap, privilegeChecker);
        this.operations = new OperationsModificationAdjudicator(userCtx, pap, privilegeChecker);
        this.routines = new RoutinesModificationAdjudicator(userCtx, pap, privilegeChecker);
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
