package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.prohibition.ContainerConditionsList;
import gov.nist.csd.pm.pap.executable.op.prohibition.CreateProhibitionOp;
import gov.nist.csd.pm.pap.executable.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.modification.ProhibitionsModification;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.Collection;

public class ProhibitionsModificationAdjudicator extends Adjudicator implements ProhibitionsModification {

    private final UserContext userCtx;
    private final PAP pap;

    public ProhibitionsModificationAdjudicator(UserContext userCtx, PAP pap, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public void createProhibition(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, Collection<ContainerCondition> containerConditions) throws PMException {
        CreateProhibitionOp op = new CreateProhibitionOp();
        ActualArgs args = op.actualArgs(name, subject, accessRightSet, intersection,
            new ContainerConditionsList(containerConditions));

        op.canExecute(privilegeChecker, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void deleteProhibition(String name) throws PMException {
        Prohibition prohibition = pap.query().prohibitions().getProhibition(name);

        DeleteProhibitionOp op = new DeleteProhibitionOp();
        ActualArgs args = op.actualArgs(name, prohibition.getSubject(), prohibition.getAccessRightSet(),
            prohibition.isIntersection(), new ContainerConditionsList(prohibition.getContainers()));

        op.canExecute(privilegeChecker, userCtx, args);
        op.execute(pap, args);
    }
}
