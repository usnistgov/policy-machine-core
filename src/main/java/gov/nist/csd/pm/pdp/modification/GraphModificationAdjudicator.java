package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.obligation.EventContext;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.modification.GraphModifier;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.graph.*;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.common.graph.node.NodeType;

import java.util.Collection;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.pap.op.graph.GraphOp.*;

public class GraphModificationAdjudicator extends GraphModifier {

    private final UserContext userCtx;
    private final PAP pap;
    private final EventEmitter eventEmitter;

    public GraphModificationAdjudicator(UserContext userCtx, PAP pap, EventEmitter eventEmitter) {
        super(pap.modify());
        this.userCtx = userCtx;
        this.pap = pap;
        this.eventEmitter = eventEmitter;
    }

    @Override
    public String createPolicyClass(String name) throws PMException {
        EventContext event = new CreatePolicyClassOp()
                .withOperands(Map.of(NAME_OPERAND, name))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);

        return name;
    }

    @Override
    public String createUserAttribute(String name, Collection<String> descendants) throws PMException {
        EventContext event = new CreateUserAttributeOp()
                .withOperands(Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);

        return name;
    }

    @Override
    public String createObjectAttribute(String name, Collection<String> descendants) throws PMException {
        EventContext event = new CreateObjectAttributeOp()
                .withOperands(Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);

        return name;
    }

    @Override
    public String createObject(String name, Collection<String> descendants) throws PMException {
        EventContext event = new CreateObjectOp()
                .withOperands(Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);

        return name;
    }

    @Override
    public String createUser(String name, Collection<String> descendants) throws PMException {
        EventContext event = new CreateUserOp()
                .withOperands(Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);

        return name;
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties) throws PMException {
        EventContext event = new SetNodePropertiesOp()
                .withOperands(Map.of(NAME_OPERAND, name, PROPERTIES_OPERAND, properties))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);
    }

    @Override
    public void deleteNode(String name) throws PMException {
        NodeType nodeType = pap.query().graph().getNode(name).getType();
        Collection<String> descendants = pap.query().graph().getAdjacentDescendants(name);

        Operation<?> op = new DeletePolicyClassOp();

        switch (nodeType) {
            case OA -> op = new DeleteObjectAttributeOp();
            case UA -> op = new DeleteUserAttributeOp();
            case O -> op = new DeleteObjectOp();
            case U -> op = new DeleteUserOp();
        }

        EventContext event = op.
                withOperands(Map.of(NAME_OPERAND, name, TYPE_OPERAND, nodeType, DESCENDANTS_OPERAND, descendants))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);
    }

    @Override
    public void assign(String ascendant, Collection<String> descendants) throws PMException {
        EventContext event = new AssignOp()
                .withOperands(Map.of(ASCENDANT_OPERAND, ascendant, DESCENDANTS_OPERAND, descendants))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);
    }

    @Override
    public void deassign(String ascendant, Collection<String> descendants) throws PMException {
        EventContext event = new DeassignOp()
                .withOperands(Map.of(ASCENDANT_OPERAND, ascendant, DESCENDANTS_OPERAND, descendants))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);
    }

    @Override
    public void associate(String ua, String target, AccessRightSet accessRights) throws PMException {
        EventContext event = new AssociateOp()
                .withOperands(Map.of(UA_OPERAND, ua, TARGET_OPERAND, target, ARSET_OPERAND, accessRights))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);
    }

    @Override
    public void dissociate(String ua, String target) throws PMException {
        EventContext event = new DissociateOp()
                .withOperands(Map.of(UA_OPERAND, ua, TARGET_OPERAND, target))
                .execute(pap, userCtx);

        eventEmitter.emitEvent(event);

    }
}
