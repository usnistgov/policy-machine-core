package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.modification.ProhibitionsModification;
import gov.nist.csd.pm.pap.obligation.EventContext;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.modification.ProhibitionsModifier;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.op.prohibition.CreateProhibitionOp;
import gov.nist.csd.pm.pap.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.prohibition.ContainerCondition;
import gov.nist.csd.pm.pap.prohibition.Prohibition;
import gov.nist.csd.pm.pap.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pdp.Adjudicator;

import java.util.Collection;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.pap.op.prohibition.ProhibitionOp.*;

public class ProhibitionsModificationAdjudicator extends Adjudicator implements ProhibitionsModification {
    private final UserContext userCtx;
    private final PAP pap;
    private final EventEmitter eventEmitter;

    public ProhibitionsModificationAdjudicator(UserContext userCtx, PAP pap, EventEmitter eventEmitter, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
        this.eventEmitter = eventEmitter;
    }

    @Override
    public void createProhibition(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, Collection<ContainerCondition> containerConditions) throws PMException {
        EventContext event = new CreateProhibitionOp()
                .withOperands(Map.of(
                        NAME_OPERAND, name,
                        SUBJECT_OPERAND, subject,
                        ARSET_OPERAND, accessRightSet,
                        INTERSECTION_OPERAND, intersection,
                        CONTAINERS_OPERAND, containerConditions
                ))
                .execute(pap, userCtx, privilegeChecker);

        eventEmitter.emitEvent(event);
    }

    @Override
    public void deleteProhibition(String name) throws PMException {
        Prohibition prohibition = pap.query().prohibitions().getProhibition(name);

        EventContext event = new DeleteProhibitionOp()
                .withOperands(Map.of(
                        NAME_OPERAND, prohibition.getName(),
                        SUBJECT_OPERAND, prohibition.getSubject(),
                        ARSET_OPERAND, prohibition.getAccessRightSet(),
                        INTERSECTION_OPERAND, prohibition.isIntersection(),
                        CONTAINERS_OPERAND, prohibition.getContainers()
                ))
                .execute(pap, userCtx, privilegeChecker);

        eventEmitter.emitEvent(event);
    }
}
