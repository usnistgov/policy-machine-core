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
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
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
        ActualArgs actualArgs = op.actualArgs(name);

        return executeOp(op, actualArgs);
    }

    @Override
    public long createUserAttribute(String name, Collection<Long> descendants) throws PMException {
        CreateUserAttributeOp op = new CreateUserAttributeOp();
        ActualArgs actualArgs = op.actualArgs(name, new LongArrayList(descendants));

        return executeOp(op, actualArgs);
    }

    @Override
    public long createObjectAttribute(String name, Collection<Long> descendants) throws PMException {
        CreateObjectAttributeOp op = new CreateObjectAttributeOp();
        ActualArgs actualArgs = op.actualArgs(name, new LongArrayList(descendants));

        return executeOp(op, actualArgs);
    }

    @Override
    public long createObject(String name, Collection<Long> descendants) throws PMException {
        CreateObjectOp op = new CreateObjectOp();
        ActualArgs actualArgs = op.actualArgs(name, new LongArrayList(descendants));

        return executeOp(op, actualArgs);
    }

    @Override
    public long createUser(String name, Collection<Long> descendants) throws PMException {
        CreateUserOp op = new CreateUserOp();
        ActualArgs actualArgs = op.actualArgs(name, new LongArrayList(descendants));

        return executeOp(op, actualArgs);
    }

    @Override
    public void setNodeProperties(long id, Map<String, String> properties) throws PMException {
        SetNodePropertiesOp op = new SetNodePropertiesOp();
        ActualArgs actualArgs = op.actualArgs(id, new Properties(properties));

        executeOp(op, actualArgs);
    }

    @Override
    public void deleteNode(long id) throws PMException {
        Node node = pap.query().graph().getNodeById(id);
        LongArrayList descendants = new LongArrayList(pap.query().graph().getAdjacentDescendants(id));

        DeleteNodeOp op = new DeleteNodeOp();
        ActualArgs actualArgs = op.actualArgs(id, node.getType(), descendants);

        // build event context before executing or else the node will not exist when the util
        // tries to convert the id to the name
        EventContext eventContext = buildEventContext(pap, userCtx, op.getName(), actualArgs);

        executeOp(op, actualArgs, eventContext);
    }

    @Override
    public void assign(long ascId, Collection<Long> descendants) throws PMException {
        AssignOp op = new AssignOp();
        ActualArgs actualArgs = op.actualArgs(ascId, new LongArrayList(descendants));

        executeOp(op, actualArgs);
    }

    @Override
    public void deassign(long ascendant, Collection<Long> descendants) throws PMException {
        DeassignOp op = new DeassignOp();
        ActualArgs actualArgs = op.actualArgs(ascendant, new LongArrayList(descendants));

        executeOp(op, actualArgs);
    }

    @Override
    public void associate(long ua, long target, AccessRightSet accessRights) throws PMException {
        AssociateOp op = new AssociateOp();
        ActualArgs actualArgs = op.actualArgs(ua, target, accessRights);

        executeOp(op, actualArgs);
    }

    @Override
    public void dissociate(long ua, long target) throws PMException {
        DissociateOp op = new DissociateOp();
        ActualArgs actualArgs = op.actualArgs(ua, target);

        executeOp(op, actualArgs);
    }

    private <T> void executeOp(Operation<T> op, ActualArgs args, EventContext eventContext) throws PMException {
        op.canExecute(privilegeChecker, userCtx, args);
        op.execute(pap, args);

        eventPublisher.publishEvent(eventContext);
    }

    private <T> T executeOp(Operation<T> op, ActualArgs args) throws PMException {
        op.canExecute(privilegeChecker, userCtx, args);
        T ret = op.execute(pap, args);

        eventPublisher.publishEvent(buildEventContext(pap, userCtx, op.getName(), args));

        return ret;
    }
}
