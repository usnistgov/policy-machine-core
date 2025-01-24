package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.op.graph.*;
import gov.nist.csd.pm.pap.modification.GraphModification;
import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.event.EventPublisher;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.Collection;
import java.util.Map;

import static gov.nist.csd.pm.common.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.common.op.graph.GraphOp.*;

public class GraphModificationAdjudicator extends Adjudicator implements GraphModification {

    private final UserContext userCtx;
    private final PAP pap;
    private final EventPublisher eventPublisher;

    public GraphModificationAdjudicator(UserContext userCtx, PAP pap, EventPublisher eventPublisher, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public long createPolicyClass(String name) throws PMException {
        EventContext event = new CreatePolicyClassOp()
                .withOperands(Map.of(NAME_OPERAND, name))
                .execute(pap, userCtx, privilegeChecker);

        eventPublisher.publishEvent(event);

        return name;
    }

    @Override
    public long createUserAttribute(String name, Collection<Long> descendants) throws PMException {
        EventContext event = new CreateUserAttributeOp()
                .withOperands(Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants))
                .execute(pap, userCtx, privilegeChecker);

        eventPublisher.publishEvent(event);

        return name;
    }

    @Override
    public long createObjectAttribute(String name, Collection<Long> descendants) throws PMException {
        EventContext event = new CreateObjectAttributeOp()
                .withOperands(Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants))
                .execute(pap, userCtx, privilegeChecker);

        eventPublisher.publishEvent(event);

        return name;
    }

    @Override
    public long createObject(String name, Collection<Long> descendants) throws PMException {
        EventContext event = new CreateObjectOp()
                .withOperands(Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants))
                .execute(pap, userCtx, privilegeChecker);

        eventPublisher.publishEvent(event);

        return name;
    }

    @Override
    public long createUser(String name, Collection<Long> descendants) throws PMException {
        EventContext event = new CreateUserOp()
                .withOperands(Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants))
                .execute(pap, userCtx, privilegeChecker);

        eventPublisher.publishEvent(event);

        return name;
    }

    @Override
    public void setNodeProperties(long id, Map<String, String> properties) throws PMException {
        EventContext event = new SetNodePropertiesOp()
                .withOperands(Map.of(NAME_OPERAND, id, PROPERTIES_OPERAND, properties))
                .execute(pap, userCtx, privilegeChecker);

        eventPublisher.publishEvent(event);
    }

    @Override
    public void deleteNode(long id) throws PMException {
        NodeType nodeType = pap.query().graph().getNodeByName(id).getType();
        Collection<String> descendants = pap.query().graph().getAdjacentDescendants(id);

        Operation<?> op = new DeletePolicyClassOp();

        switch (nodeType) {
            case OA -> op = new DeleteObjectAttributeOp();
            case UA -> op = new DeleteUserAttributeOp();
            case O -> op = new DeleteObjectOp();
            case U -> op = new DeleteUserOp();
        }

        EventContext event = op.
                withOperands(Map.of(NAME_OPERAND, id, TYPE_OPERAND, nodeType, DESCENDANTS_OPERAND, descendants))
                .execute(pap, userCtx, privilegeChecker);

        eventPublisher.publishEvent(event);
    }

    @Override
    public void assign(long ascId, Collection<Long> descendants) throws PMException {
        EventContext event = new AssignOp()
                .withOperands(Map.of(ASCENDANT_OPERAND, ascId, DESCENDANTS_OPERAND, descendants))
                .execute(pap, userCtx, privilegeChecker);

        eventPublisher.publishEvent(event);
    }

    @Override
    public void deassign(long ascendant, Collection<Long> descendants) throws PMException {
        EventContext event = new DeassignOp()
                .withOperands(Map.of(ASCENDANT_OPERAND, ascendant, DESCENDANTS_OPERAND, descendants))
                .execute(pap, userCtx, privilegeChecker);

        eventPublisher.publishEvent(event);
    }

    @Override
    public void associate(long ua, long target, AccessRightSet accessRights) throws PMException {
        EventContext event = new AssociateOp()
                .withOperands(Map.of(UA_OPERAND, ua, TARGET_OPERAND, target, ARSET_OPERAND, accessRights))
                .execute(pap, userCtx, privilegeChecker);

        eventPublisher.publishEvent(event);
    }

    @Override
    public void dissociate(long ua, long target) throws PMException {
        EventContext event = new DissociateOp()
                .withOperands(Map.of(UA_OPERAND, ua, TARGET_OPERAND, target))
                .execute(pap, userCtx, privilegeChecker);

        eventPublisher.publishEvent(event);

    }
}
