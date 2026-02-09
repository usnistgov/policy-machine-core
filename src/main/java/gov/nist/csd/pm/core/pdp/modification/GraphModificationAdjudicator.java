package gov.nist.csd.pm.core.pdp.modification;

import static gov.nist.csd.pm.core.pap.operation.Operation.ARSET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.operation.Operation.PROPERTIES_PARAM;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventPublisher;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.Properties;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.modification.GraphModification;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.graph.AssignOp;
import gov.nist.csd.pm.core.pap.operation.graph.AssociateOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateObjectAttributeOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateObjectOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreatePolicyClassOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateUserAttributeOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateUserOp;
import gov.nist.csd.pm.core.pap.operation.graph.DeassignOp;
import gov.nist.csd.pm.core.pap.operation.graph.DeleteNodeOp;
import gov.nist.csd.pm.core.pap.operation.graph.DissociateOp;
import gov.nist.csd.pm.core.pap.operation.graph.SetNodePropertiesOp;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;
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
            .put(CreateUserAttributeOp.CREATE_NODE_DESCENDANTS_PARAM, new ArrayList<>(descendants));

        return executeOp(op, args);
    }

    @Override
    public long createObjectAttribute(String name, Collection<Long> descendants) throws PMException {
        CreateObjectAttributeOp op = new CreateObjectAttributeOp();
        Args args = new Args()
            .put(NAME_PARAM, name)
            .put(CreateObjectAttributeOp.CREATE_NODE_DESCENDANTS_PARAM, new ArrayList<>(descendants));

        return executeOp(op, args);
    }

    @Override
    public long createObject(String name, Collection<Long> descendants) throws PMException {
        CreateObjectOp op = new CreateObjectOp();
        Args args = new Args()
            .put(NAME_PARAM, name)
            .put(CreateObjectOp.CREATE_NODE_DESCENDANTS_PARAM, new ArrayList<>(descendants));

        return executeOp(op, args);
    }

    @Override
    public long createUser(String name, Collection<Long> descendants) throws PMException {
        CreateUserOp op = new CreateUserOp();
        Args args = new Args()
            .put(NAME_PARAM, name)
            .put(CreateUserOp.CREATE_NODE_DESCENDANTS_PARAM, new ArrayList<>(descendants));

        return executeOp(op, args);
    }

    @Override
    public void setNodeProperties(long id, Map<String, String> properties) throws PMException {
        SetNodePropertiesOp op = new SetNodePropertiesOp();
        Args args = new Args()
            .put(SetNodePropertiesOp.SET_NODE_PROPS_NODE_ID_PARAM, id)
            .put(PROPERTIES_PARAM, new Properties(properties));

        executeOp(op, args);
    }

    @Override
    public void deleteNode(long id) throws PMException {
        Node node = pap.query().graph().getNodeById(id);
        Collection<Long> descendants = pap.query().graph().getAdjacentDescendants(id);

        DeleteNodeOp op = new DeleteNodeOp();
        Args args = new Args()
            .put(DeleteNodeOp.DELETE_NODE_NODE_ID_PARAM, id)
            .put(Operation.TYPE_PARAM, node.getType().toString())
            .put(DeleteNodeOp.DELETE_NODE_DESCENDANTS_PARAM, new ArrayList<>(descendants));

        // build event context before executing or else the node will not exist when the util
        // tries to convert the id to the name
        EventContext eventContext = new EventContext(pap, userCtx, op.getName(), args.toMap());

        executeOp(op, args, eventContext);
    }

    @Override
    public void assign(long ascId, Collection<Long> descendants) throws PMException {
        AssignOp op = new AssignOp();
        Args args = new Args()
            .put(AssignOp.ASSIGN_ASCENDANT_PARAM,ascId)
            .put(AssignOp.ASSIGN_DESCENDANTS_PARAM, new ArrayList<>(descendants));

        executeOp(op, args);
    }

    @Override
    public void deassign(long ascendant, Collection<Long> descendants) throws PMException {
        DeassignOp op = new DeassignOp();
        Args args = new Args()
            .put(DeassignOp.DEASSIGN_ASCENDANT_PARAM, ascendant)
            .put(DeassignOp.DEASSIGN_DESCENDANTS_PARAM, new ArrayList<>(descendants));

        executeOp(op, args);
    }

    @Override
    public void associate(long ua, long target, AccessRightSet accessRights) throws PMException {
        AssociateOp op = new AssociateOp();
        Args args = new Args()
            .put(AssociateOp.ASSOCIATE_UA_PARAM, ua)
            .put(AssociateOp.ASSOCIATE_TARGET_PARAM, target)
            .put(ARSET_PARAM, new ArrayList<>(accessRights));

        executeOp(op, args);
    }

    @Override
    public void dissociate(long ua, long target) throws PMException {
        DissociateOp op = new DissociateOp();
        Args args = new Args()
            .put(DissociateOp.DISSOCIATE_UA_PARAM, ua)
            .put(DissociateOp.DISSOCIATE_TARGET_PARAM, target);

        executeOp(op, args);
    }

    private <R> void executeOp(AdminOperation<R> op, Args args, EventContext eventContext) throws PMException {
        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);

        eventPublisher.publishEvent(eventContext);
    }

    private <R> R executeOp(AdminOperation<R> op, Args args) throws PMException {
        op.canExecute(pap, userCtx, args);
        R ret = op.execute(pap, args);

        eventPublisher.publishEvent(new EventContext(pap, userCtx, op.getName(), args.toMap()));

        return ret;
    }
}
