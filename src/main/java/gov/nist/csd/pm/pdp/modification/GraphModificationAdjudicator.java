package gov.nist.csd.pm.pdp.modification;

import static gov.nist.csd.pm.pdp.event.EventContextUtil.buildEventContext;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.event.EventPublisher;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.Properties;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.op.graph.*;
import gov.nist.csd.pm.pap.function.op.graph.AssignOp.AssignOpArgs;
import gov.nist.csd.pm.pap.function.op.graph.AssociateOp.AssociateOpArgs;
import gov.nist.csd.pm.pap.function.op.graph.CreateNodeOp.CreateNodeOpArgs;
import gov.nist.csd.pm.pap.function.op.graph.DeassignOp.DeassignOpArgs;
import gov.nist.csd.pm.pap.function.op.graph.DeleteNodeOp.DeleteNodeOpArgs;
import gov.nist.csd.pm.pap.function.op.graph.DissociateOp.DissociateOpArgs;
import gov.nist.csd.pm.pap.function.op.graph.SetNodePropertiesOp.SetNodePropertiesOpArgs;
import gov.nist.csd.pm.pap.modification.GraphModification;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GraphModificationAdjudicator extends Adjudicator implements GraphModification {

    private final PAP pap;
    private final EventPublisher eventPublisher;

    public GraphModificationAdjudicator(UserContext userCtx, PAP pap, EventPublisher eventPublisher) {
        super(pap, userCtx);
        this.userCtx = userCtx;
        this.pap = pap;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public long createPolicyClass(String name) throws PMException {
        CreatePolicyClassOp op = new CreatePolicyClassOp();
        CreateNodeOpArgs args = new CreateNodeOpArgs(name, List.of());

        return executeOp(op, args);
    }

    @Override
    public long createUserAttribute(String name, Collection<Long> descendants) throws PMException {
        CreateUserAttributeOp op = new CreateUserAttributeOp();
        CreateNodeOpArgs args = new CreateNodeOpArgs(name, new ArrayList<>(descendants));

        return executeOp(op, args);
    }

    @Override
    public long createObjectAttribute(String name, Collection<Long> descendants) throws PMException {
        CreateObjectAttributeOp op = new CreateObjectAttributeOp();
        CreateNodeOpArgs args = new CreateNodeOpArgs(name, new ArrayList<>(descendants));

        return executeOp(op, args);
    }

    @Override
    public long createObject(String name, Collection<Long> descendants) throws PMException {
        CreateObjectOp op = new CreateObjectOp();
        CreateNodeOpArgs args = new CreateNodeOpArgs(name, new ArrayList<>(descendants));

        return executeOp(op, args);
    }

    @Override
    public long createUser(String name, Collection<Long> descendants) throws PMException {
        CreateUserOp op = new CreateUserOp();
        CreateNodeOpArgs args = new CreateNodeOpArgs(name, new ArrayList<>(descendants));

        return executeOp(op, args);
    }

    @Override
    public void setNodeProperties(long id, Map<String, String> properties) throws PMException {
        SetNodePropertiesOp op = new SetNodePropertiesOp();
        SetNodePropertiesOpArgs args = new SetNodePropertiesOpArgs(id, new Properties(properties));

        executeOp(op, args);
    }

    @Override
    public void deleteNode(long id) throws PMException {
        Node node = pap.query().graph().getNodeById(id);
        LongArrayList descendants = new LongArrayList(pap.query().graph().getAdjacentDescendants(id));

        DeleteNodeOp op = new DeleteNodeOp();
        DeleteNodeOpArgs args = new DeleteNodeOpArgs(id, node.getType(), descendants);

        // build event context before executing or else the node will not exist when the util
        // tries to convert the id to the name
        EventContext eventContext = buildEventContext(pap, userCtx, op.getName(), args);

        executeOp(op, args, eventContext);
    }

    @Override
    public void assign(long ascId, Collection<Long> descendants) throws PMException {
        AssignOp op = new AssignOp();
        AssignOpArgs args = new AssignOpArgs(ascId, new LongArrayList(descendants));

        executeOp(op, args);
    }

    @Override
    public void deassign(long ascendant, Collection<Long> descendants) throws PMException {
        DeassignOp op = new DeassignOp();
        DeassignOpArgs args = new DeassignOpArgs(ascendant, new LongArrayList(descendants));

        executeOp(op, args);
    }

    @Override
    public void associate(long ua, long target, AccessRightSet accessRights) throws PMException {
        AssociateOp op = new AssociateOp();
        AssociateOpArgs args = new AssociateOpArgs(ua, target, accessRights);

        executeOp(op, args);
    }

    @Override
    public void dissociate(long ua, long target) throws PMException {
        DissociateOp op = new DissociateOp();
        DissociateOpArgs args = new DissociateOpArgs(ua, target);

        executeOp(op, args);
    }

    private <R, A extends Args> void executeOp(Operation<R, A> op, A args, EventContext eventContext) throws PMException {
        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);

        eventPublisher.publishEvent(eventContext);
    }

    private <R, A extends Args> R executeOp(Operation<R, A> op, A args) throws PMException {
        op.canExecute(pap, userCtx, args);
        R ret = op.execute(pap, args);

        eventPublisher.publishEvent(buildEventContext(pap, userCtx, op.getName(), args));

        return ret;
    }
}
