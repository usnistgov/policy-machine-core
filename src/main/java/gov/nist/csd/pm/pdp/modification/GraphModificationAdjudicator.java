package gov.nist.csd.pm.pdp.modification;

import static gov.nist.csd.pm.pdp.event.EventContextUtil.buildEventContext;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.event.EventPublisher;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.Properties;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.op.graph.*;
import gov.nist.csd.pm.pap.modification.GraphModification;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.Collection;
import java.util.Map;

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
        Args args = op.actualArgs(name);

        return executeOp(op, args);
    }

    @Override
    public long createUserAttribute(String name, Collection<Long> descendants) throws PMException {
        CreateUserAttributeOp op = new CreateUserAttributeOp();
        Args args = op.actualArgs(name, new LongArrayList(descendants));

        return executeOp(op, args);
    }

    @Override
    public long createObjectAttribute(String name, Collection<Long> descendants) throws PMException {
        CreateObjectAttributeOp op = new CreateObjectAttributeOp();
        Args args = op.actualArgs(name, new LongArrayList(descendants));

        return executeOp(op, args);
    }

    @Override
    public long createObject(String name, Collection<Long> descendants) throws PMException {
        CreateObjectOp op = new CreateObjectOp();
        Args args = op.actualArgs(name, new LongArrayList(descendants));

        return executeOp(op, args);
    }

    @Override
    public long createUser(String name, Collection<Long> descendants) throws PMException {
        CreateUserOp op = new CreateUserOp();
        Args args = op.actualArgs(name, new LongArrayList(descendants));

        return executeOp(op, args);
    }

    @Override
    public void setNodeProperties(long id, Map<String, String> properties) throws PMException {
        SetNodePropertiesOp op = new SetNodePropertiesOp();
        Args args = op.actualArgs(id, new Properties(properties));

        executeOp(op, args);
    }

    @Override
    public void deleteNode(long id) throws PMException {
        Node node = pap.query().graph().getNodeById(id);
        LongArrayList descendants = new LongArrayList(pap.query().graph().getAdjacentDescendants(id));

        DeleteNodeOp op = new DeleteNodeOp();
        Args args = op.actualArgs(id, node.getType(), descendants);

        // build event context before executing or else the node will not exist when the util
        // tries to convert the id to the name
        EventContext eventContext = buildEventContext(pap, userCtx, op.getName(), args);

        executeOp(op, args, eventContext);
    }

    @Override
    public void assign(long ascId, Collection<Long> descendants) throws PMException {
        AssignOp op = new AssignOp();
        Args args = op.actualArgs(ascId, new LongArrayList(descendants));

        executeOp(op, args);
    }

    @Override
    public void deassign(long ascendant, Collection<Long> descendants) throws PMException {
        DeassignOp op = new DeassignOp();
        Args args = op.actualArgs(ascendant, new LongArrayList(descendants));

        executeOp(op, args);
    }

    @Override
    public void associate(long ua, long target, AccessRightSet accessRights) throws PMException {
        AssociateOp op = new AssociateOp();
        Args args = op.actualArgs(ua, target, accessRights);

        executeOp(op, args);
    }

    @Override
    public void dissociate(long ua, long target) throws PMException {
        DissociateOp op = new DissociateOp();
        Args args = op.actualArgs(ua, target);

        executeOp(op, args);
    }

    private <T> void executeOp(Operation<T> op, Args args, EventContext eventContext) throws PMException {
        op.canExecute(privilegeChecker, userCtx, args);
        op.execute(pap, args);

        eventPublisher.publishEvent(eventContext);
    }

    private <T> T executeOp(Operation<T> op, Args args) throws PMException {
        op.canExecute(privilegeChecker, userCtx, args);
        T ret = op.execute(pap, args);

        eventPublisher.publishEvent(buildEventContext(pap, userCtx, op.getName(), args));

        return ret;
    }
}
