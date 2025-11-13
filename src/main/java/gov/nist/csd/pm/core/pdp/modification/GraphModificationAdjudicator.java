package gov.nist.csd.pm.core.pdp.modification;

import static gov.nist.csd.pm.core.pap.function.op.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.Operation.NODE_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.ARSET_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.ASCENDANT_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.DESCENDANTS_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.PROPERTIES_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.TARGET_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.TYPE_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.UA_PARAM;
import static gov.nist.csd.pm.core.pdp.event.EventContextUtil.buildEventContext;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventPublisher;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.Properties;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.op.graph.*;
import gov.nist.csd.pm.core.pap.modification.GraphModification;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.ArrayList;
import java.util.Collection;
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
        Args args = new Args()
            .put(NAME_PARAM, name);

        return executeOp(op, args);
    }

    @Override
    public long createUserAttribute(String name, Collection<Long> descendants) throws PMException {
        CreateUserAttributeOp op = new CreateUserAttributeOp();
        Args args = new Args()
            .put(NAME_PARAM, name)
            .put(DESCENDANTS_PARAM, new ArrayList<>(descendants));

        return executeOp(op, args);
    }

    @Override
    public long createObjectAttribute(String name, Collection<Long> descendants) throws PMException {
        CreateObjectAttributeOp op = new CreateObjectAttributeOp();
        Args args = new Args()
            .put(NAME_PARAM, name)
            .put(DESCENDANTS_PARAM, new ArrayList<>(descendants));

        return executeOp(op, args);
    }

    @Override
    public long createObject(String name, Collection<Long> descendants) throws PMException {
        CreateObjectOp op = new CreateObjectOp();
        Args args = new Args()
            .put(NAME_PARAM, name)
            .put(DESCENDANTS_PARAM, new ArrayList<>(descendants));

        return executeOp(op, args);
    }

    @Override
    public long createUser(String name, Collection<Long> descendants) throws PMException {
        CreateUserOp op = new CreateUserOp();
        Args args = new Args()
            .put(NAME_PARAM, name)
            .put(DESCENDANTS_PARAM, new ArrayList<>(descendants));

        return executeOp(op, args);
    }

    @Override
    public void setNodeProperties(long id, Map<String, String> properties) throws PMException {
        SetNodePropertiesOp op = new SetNodePropertiesOp();
        Args args = new Args()
            .put(NODE_PARAM, id)
            .put(PROPERTIES_PARAM, new Properties(properties));

        executeOp(op, args);
    }

    @Override
    public void deleteNode(long id) throws PMException {
        Node node = pap.query().graph().getNodeById(id);
        LongArrayList descendants = new LongArrayList(pap.query().graph().getAdjacentDescendants(id));

        DeleteNodeOp op = new DeleteNodeOp();
        Args args = new Args()
            .put(NODE_PARAM, id)
            .put(TYPE_PARAM, node.getType().toString())
            .put(DESCENDANTS_PARAM, descendants);

        // build event context before executing or else the node will not exist when the util
        // tries to convert the id to the name
        EventContext eventContext = buildEventContext(pap, userCtx, op.getName(), args);

        executeOp(op, args, eventContext);
    }

    @Override
    public void assign(long ascId, Collection<Long> descendants) throws PMException {
        AssignOp op = new AssignOp();
        Args args = new Args()
            .put(ASCENDANT_PARAM, ascId)
            .put(DESCENDANTS_PARAM, new ArrayList<>(descendants));

        executeOp(op, args);
    }

    @Override
    public void deassign(long ascendant, Collection<Long> descendants) throws PMException {
        DeassignOp op = new DeassignOp();
        Args args = new Args()
            .put(ASCENDANT_PARAM, ascendant)
            .put(DESCENDANTS_PARAM, new ArrayList<>(descendants));

        executeOp(op, args);
    }

    @Override
    public void associate(long ua, long target, AccessRightSet accessRights) throws PMException {
        AssociateOp op = new AssociateOp();
        Args args = new Args()
            .put(UA_PARAM, ua)
            .put(TARGET_PARAM, target)
            .put(ARSET_PARAM, new ArrayList<>(accessRights));

        executeOp(op, args);
    }

    @Override
    public void dissociate(long ua, long target) throws PMException {
        DissociateOp op = new DissociateOp();
        Args args = new Args()
            .put(UA_PARAM, ua)
            .put(TARGET_PARAM, target);

        executeOp(op, args);
    }

    private <R> void executeOp(Operation<R> op, Args args, EventContext eventContext) throws PMException {
        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);

        eventPublisher.publishEvent(eventContext);
    }

    private <R> R executeOp(Operation<R> op, Args args) throws PMException {
        op.canExecute(pap, userCtx, args);
        R ret = op.execute(pap, args);

        eventPublisher.publishEvent(buildEventContext(pap, userCtx, op.getName(), args));

        return ret;
    }
}
