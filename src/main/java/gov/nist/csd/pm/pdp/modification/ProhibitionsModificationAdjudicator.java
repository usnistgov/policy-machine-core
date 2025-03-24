package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.executable.op.prohibition.CreateProhibitionOp;
import gov.nist.csd.pm.pap.executable.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.modification.ProhibitionsModification;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.Collection;
import java.util.Map;

import static gov.nist.csd.pm.pap.executable.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.pap.executable.op.graph.GraphOp.ARSET_OPERAND;

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
        new CreateProhibitionOp()
                .withOperands(Map.of(
                        NAME_OPERAND, name,
                        SUBJECT_OPERAND, subject,
                        ARSET_OPERAND, accessRightSet,
                        INTERSECTION_OPERAND, intersection,
                        CONTAINERS_OPERAND, containerConditions
                ))
                .execute(pap, userCtx, privilegeChecker);
    }

    @Override
    public void deleteProhibition(String name) throws PMException {
        Prohibition prohibition = pap.query().prohibitions().getProhibition(name);

        new DeleteProhibitionOp()
                .withOperands(Map.of(
                        NAME_OPERAND, prohibition.getName(),
                        SUBJECT_OPERAND, prohibition.getSubject(),
                        ARSET_OPERAND, prohibition.getAccessRightSet(),
                        INTERSECTION_OPERAND, prohibition.isIntersection(),
                        CONTAINERS_OPERAND, prohibition.getContainers()
                ))
                .execute(pap, userCtx, privilegeChecker);
    }
}
