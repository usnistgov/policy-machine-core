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
        CreatePolicyClassOp op = new CreatePolicyClassOp();

        Map<String, Object> operands = Map.of(NAME_OPERAND, name);
        op.canExecute(privilegeChecker, userCtx, operands);
        Long id = op.execute(pap, operands);

        EventContext eventContext = op.toEventContext(pap, userCtx, operands);
        eventPublisher.publishEvent(eventContext);

        return id;
    }

    @Override
    public long createUserAttribute(String name, Collection<Long> descendants) throws PMException {
        CreateUserAttributeOp op = new CreateUserAttributeOp();

        Map<String, Object> operands = Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants);

        op.canExecute(privilegeChecker, userCtx, operands);
        Long id = op.execute(pap, operands);

        EventContext eventContext = op.toEventContext(pap, userCtx, operands);
        eventPublisher.publishEvent(eventContext);

        return id;
    }

    @Override
    public long createObjectAttribute(String name, Collection<Long> descendants) throws PMException {
        CreateObjectAttributeOp op = new CreateObjectAttributeOp();

        Map<String, Object> operands = Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants);
        op.canExecute(privilegeChecker, userCtx, operands);
        Long id = op.execute(pap, operands);

        EventContext eventContext = op.toEventContext(pap, userCtx, operands);
        eventPublisher.publishEvent(eventContext);

        return id;
    }

    @Override
    public long createObject(String name, Collection<Long> descendants) throws PMException {
        CreateObjectOp op = new CreateObjectOp();

        Map<String, Object> operands = Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants);
        op.canExecute(privilegeChecker, userCtx, operands);
        Long id = op.execute(pap, operands);

        EventContext eventContext = op.toEventContext(pap, userCtx, operands);
        eventPublisher.publishEvent(eventContext);

        return id;
    }

    @Override
    public long createUser(String name, Collection<Long> descendants) throws PMException {
        CreateUserOp op = new CreateUserOp();

        Map<String, Object> operands = Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants);
        op.canExecute(privilegeChecker, userCtx, operands);
        Long id = op.execute(pap, operands);

        EventContext eventContext = op.toEventContext(pap, userCtx, operands);
        eventPublisher.publishEvent(eventContext);

        return id;
    }

    @Override
    public void setNodeProperties(long id, Map<String, String> properties) throws PMException {
        SetNodePropertiesOp op = new SetNodePropertiesOp();

        Map<String, Object> operands = Map.of(NAME_OPERAND, id, PROPERTIES_OPERAND, properties);
        op.canExecute(privilegeChecker, userCtx, operands);
        op.execute(pap, operands);

        EventContext eventContext = op.toEventContext(pap, userCtx, operands);
        eventPublisher.publishEvent(eventContext);
    }

    @Override
    public void deleteNode(long id) throws PMException {
        NodeType nodeType = pap.query().graph().getNodeById(id).getType();
        long[] descendants = pap.query().graph().getAdjacentDescendants(id);

        DeleteNodeOp op = new DeleteNodeOp();

        Map<String, Object> operands = Map.of(NAME_OPERAND, id, TYPE_OPERAND, nodeType, DESCENDANTS_OPERAND, descendants);
        op.canExecute(privilegeChecker, userCtx, operands);
        op.execute(pap, operands);

        EventContext eventContext = op.toEventContext(pap, userCtx, operands);
        eventPublisher.publishEvent(eventContext);
    }

    @Override
    public void assign(long ascId, Collection<Long> descendants) throws PMException {
        AssignOp op = new AssignOp();

        Map<String, Object> operands = Map.of(ASCENDANT_OPERAND, ascId, DESCENDANTS_OPERAND, descendants);
        op.canExecute(privilegeChecker, userCtx, operands);
        op.execute(pap, operands);

        EventContext eventContext = op.toEventContext(pap, userCtx, operands);
        eventPublisher.publishEvent(eventContext);
    }

    @Override
    public void deassign(long ascendant, Collection<Long> descendants) throws PMException {
        DeassignOp op = new DeassignOp();

        Map<String, Object> operands = Map.of(ASCENDANT_OPERAND, ascendant, DESCENDANTS_OPERAND, descendants);
        op.canExecute(privilegeChecker, userCtx, operands);
        op.execute(pap, operands);

        EventContext eventContext = op.toEventContext(pap, userCtx, operands);
        eventPublisher.publishEvent(eventContext);
    }

    @Override
    public void associate(long ua, long target, AccessRightSet accessRights) throws PMException {
        AssociateOp op = new AssociateOp();

        Map<String, Object> operands = Map.of(UA_OPERAND, ua, TARGET_OPERAND, target, ARSET_OPERAND, accessRights);
        op.canExecute(privilegeChecker, userCtx, operands);
        op.execute(pap, operands);

        EventContext eventContext = op.toEventContext(pap, userCtx, operands);
        eventPublisher.publishEvent(eventContext);
    }

    @Override
    public void dissociate(long ua, long target) throws PMException {
        DissociateOp op = new DissociateOp();

        Map<String, Object> operands = Map.of(UA_OPERAND, ua, TARGET_OPERAND, target);
        op.canExecute(privilegeChecker, userCtx, operands);
        op.execute(pap, operands);

        EventContext eventContext = op.toEventContext(pap, userCtx, operands);
        eventPublisher.publishEvent(eventContext);

    }
}
