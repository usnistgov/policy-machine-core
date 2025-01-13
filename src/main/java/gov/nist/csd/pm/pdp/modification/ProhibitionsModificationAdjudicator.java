package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.modification.ProhibitionsModification;
import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.event.EventPublisher;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.common.op.prohibition.CreateProhibitionOp;
import gov.nist.csd.pm.common.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.Collection;
import java.util.Map;

import static gov.nist.csd.pm.common.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.common.op.prohibition.ProhibitionOp.*;

public class ProhibitionsModificationAdjudicator extends Adjudicator implements ProhibitionsModification {
    private final UserContext userCtx;
    private final PAP pap;
    private final EventPublisher eventPublisher;

    public ProhibitionsModificationAdjudicator(UserContext userCtx, PAP pap, EventPublisher eventPublisher, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
        this.eventPublisher = eventPublisher;
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

        eventPublisher.publishEvent(event);
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

        eventPublisher.publishEvent(event);
    }
}
