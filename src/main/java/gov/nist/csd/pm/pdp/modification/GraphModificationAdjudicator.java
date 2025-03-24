package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.event.EventPublisher;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.executable.op.graph.*;
import gov.nist.csd.pm.pap.modification.GraphModification;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.Collection;
import java.util.Map;

import static gov.nist.csd.pm.pap.executable.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.pap.executable.op.Operation.NODE_OPERAND;

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
        long id = op.execute(pap, operands);

        eventPublisher.publishEvent(new CreatePolicyClassOp.EventCtx(
                pap.query().graph().getNodeById(userCtx.getUser()).getName(),
                userCtx.getProcess(),
                name
        ));

        return id;
    }

    @Override
    public long createUserAttribute(String name, Collection<Long> descendants) throws PMException {
        CreateUserAttributeOp op = new CreateUserAttributeOp();

        Map<String, Object> operands = Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants);
        op.canExecute(privilegeChecker, userCtx, operands);
        long id = op.execute(pap, operands);

        eventPublisher.publishEvent(new CreateUserAttributeOp.EventCtx(
                pap.query().graph().getNodeById(userCtx.getUser()).getName(),
                userCtx.getProcess(),
                name,
                descendants.stream().map(descId -> {
                    try {
                        return pap.query().graph().getNodeById(descId).getName();
                    } catch (PMException e) {
                        throw new RuntimeException(e);
                    }
                }).toList()
        ));

        return id;
    }

    @Override
    public long createObjectAttribute(String name, Collection<Long> descendants) throws PMException {
        CreateObjectAttributeOp op = new CreateObjectAttributeOp();

        Map<String, Object> operands = Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants);
        op.canExecute(privilegeChecker, userCtx, operands);
        long id = op.execute(pap, operands);

        eventPublisher.publishEvent(new CreateObjectAttributeOp.EventCtx(
                pap.query().graph().getNodeById(userCtx.getUser()).getName(),
                userCtx.getProcess(),
                name,
                descendants.stream().map(descId -> {
                    try {
                        return pap.query().graph().getNodeById(descId).getName();
                    } catch (PMException e) {
                        throw new RuntimeException(e);
                    }
                }).toList()
        ));

        return id;
    }

    @Override
    public long createObject(String name, Collection<Long> descendants) throws PMException {
        CreateObjectOp op = new CreateObjectOp();

        Map<String, Object> operands = Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants);
        op.canExecute(privilegeChecker, userCtx, operands);
        long id = op.execute(pap, operands);

        eventPublisher.publishEvent(new CreateObjectOp.EventCtx(
                pap.query().graph().getNodeById(userCtx.getUser()).getName(),
                userCtx.getProcess(),
                name,
                descendants.stream().map(descId -> {
                    try {
                        return pap.query().graph().getNodeById(descId).getName();
                    } catch (PMException e) {
                        throw new RuntimeException(e);
                    }
                }).toList()
        ));

        return id;
    }

    @Override
    public long createUser(String name, Collection<Long> descendants) throws PMException {
        CreateUserOp op = new CreateUserOp();

        Map<String, Object> operands = Map.of(NAME_OPERAND, name, DESCENDANTS_OPERAND, descendants);
        op.canExecute(privilegeChecker, userCtx, operands);
        long id = op.execute(pap, operands);

        eventPublisher.publishEvent(new CreateUserOp.EventCtx(
                pap.query().graph().getNodeById(userCtx.getUser()).getName(),
                userCtx.getProcess(),
                name,
                descendants.stream().map(descId -> {
                    try {
                        return pap.query().graph().getNodeById(descId).getName();
                    } catch (PMException e) {
                        throw new RuntimeException(e);
                    }
                }).toList()
        ));

        return id;
    }

    @Override
    public void setNodeProperties(long id, Map<String, String> properties) throws PMException {
        SetNodePropertiesOp op = new SetNodePropertiesOp();

        Map<String, Object> operands = Map.of(NODE_OPERAND, id, PROPERTIES_OPERAND, properties);
        op.canExecute(privilegeChecker, userCtx, operands);
        op.execute(pap, operands);

        eventPublisher.publishEvent(new SetNodePropertiesOp.EventCtx(
                pap.query().graph().getNodeById(userCtx.getUser()).getName(),
                userCtx.getProcess(),
                pap.query().graph().getNodeById(id).getName()
        ));
    }

    @Override
    public void deleteNode(long id) throws PMException {
        Node node = pap.query().graph().getNodeById(id);
        Collection<Long> descendants = new LongArrayList(pap.query().graph().getAdjacentDescendants(id));

        DeleteNodeOp op = new DeleteNodeOp();

        Map<String, Object> operands = Map.of(NODE_OPERAND, id, TYPE_OPERAND, node.getType(), DESCENDANTS_OPERAND, descendants);
        op.canExecute(privilegeChecker, userCtx, operands);
        op.execute(pap, operands);

        eventPublisher.publishEvent(new DeleteNodeOp.EventCtx(
                pap.query().graph().getNodeById(userCtx.getUser()).getName(),
                userCtx.getProcess(),
                node.getName(),
                descendants.stream().map(descId -> {
                    try {
                        return pap.query().graph().getNodeById(descId).getName();
                    } catch (PMException e) {
                        throw new RuntimeException(e);
                    }
                }).toList()
        ));
    }

    @Override
    public void assign(long ascId, Collection<Long> descendants) throws PMException {
        AssignOp op = new AssignOp();

        Map<String, Object> operands = Map.of(ASCENDANT_OPERAND, ascId, DESCENDANTS_OPERAND, descendants);
        op.canExecute(privilegeChecker, userCtx, operands);
        op.execute(pap, operands);

        eventPublisher.publishEvent(new AssignOp.EventCtx(
                pap.query().graph().getNodeById(userCtx.getUser()).getName(),
                userCtx.getProcess(),
                pap.query().graph().getNodeById(ascId).getName(),
                descendants.stream().map(id -> {
	                try {
		                return pap.query().graph().getNodeById(id).getName();
	                } catch (PMException e) {
		                throw new RuntimeException(e);
	                }
                }).toList()
        ));
    }

    @Override
    public void deassign(long ascendant, Collection<Long> descendants) throws PMException {
        DeassignOp op = new DeassignOp();

        Map<String, Object> operands = Map.of(ASCENDANT_OPERAND, ascendant, DESCENDANTS_OPERAND, descendants);
        op.canExecute(privilegeChecker, userCtx, operands);
        op.execute(pap, operands);

        eventPublisher.publishEvent(new DeassignOp.EventCtx(
                pap.query().graph().getNodeById(userCtx.getUser()).getName(),
                userCtx.getProcess(),
                pap.query().graph().getNodeById(ascendant).getName(),
                descendants.stream().map(id -> {
                    try {
                        return pap.query().graph().getNodeById(id).getName();
                    } catch (PMException e) {
                        throw new RuntimeException(e);
                    }
                }).toList()
        ));
    }

    @Override
    public void associate(long ua, long target, AccessRightSet accessRights) throws PMException {
        AssociateOp op = new AssociateOp();

        Map<String, Object> operands = Map.of(UA_OPERAND, ua, TARGET_OPERAND, target, ARSET_OPERAND, accessRights);
        op.canExecute(privilegeChecker, userCtx, operands);
        op.execute(pap, operands);

        eventPublisher.publishEvent(new AssociateOp.EventCtx(
                pap.query().graph().getNodeById(userCtx.getUser()).getName(),
                userCtx.getProcess(),
                pap.query().graph().getNodeById(ua).getName(),
                pap.query().graph().getNodeById(target).getName()
        ));
    }

    @Override
    public void dissociate(long ua, long target) throws PMException {
        DissociateOp op = new DissociateOp();

        Map<String, Object> operands = Map.of(UA_OPERAND, ua, TARGET_OPERAND, target);
        op.canExecute(privilegeChecker, userCtx, operands);
        op.execute(pap, operands);

        eventPublisher.publishEvent(new DissociateOp.EventCtx(
                pap.query().graph().getNodeById(userCtx.getUser()).getName(),
                userCtx.getProcess(),
                pap.query().graph().getNodeById(ua).getName(),
                pap.query().graph().getNodeById(target).getName()
        ));

    }
}
