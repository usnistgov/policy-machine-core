package gov.nist.csd.pm.core.pap.modification;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.O;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.OA;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.PC;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.U;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.UA;
import static gov.nist.csd.pm.core.pap.operation.accessright.AccessRightValidator.validateAccessRights;

import gov.nist.csd.pm.core.common.exception.AssignmentCausesLoopException;
import gov.nist.csd.pm.core.common.exception.CannotDeleteAdminPolicyConfigException;
import gov.nist.csd.pm.core.common.exception.DisconnectedNodeException;
import gov.nist.csd.pm.core.common.exception.InvalidAssignmentException;
import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.NodeHasAscendantsException;
import gov.nist.csd.pm.core.common.exception.NodeIdExistsException;
import gov.nist.csd.pm.core.common.exception.NodeNameExistsException;
import gov.nist.csd.pm.core.common.exception.NodeReferencedInObligationException;
import gov.nist.csd.pm.core.common.exception.NodeReferencedInProhibitionException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.UnknownAccessRightException;
import gov.nist.csd.pm.core.common.graph.dag.Direction;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.common.exception.InvalidAssociationException;
import gov.nist.csd.pm.core.pap.admin.AdminPolicy;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.accessright.WildcardAccessRight;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.id.IdGenerator;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.store.GraphStoreDFS;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class GraphModifier extends Modifier implements GraphModification {

    private IdGenerator idGenerator;

    public GraphModifier(PolicyStore store, IdGenerator idGenerator) {
        super(store);
        this.idGenerator = idGenerator;
    }

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public long createPolicyClass(String name) throws PMException {
        long id = idGenerator.generateId(name, PC);

        if (name.equals(AdminPolicyNode.PM_ADMIN_PC.nodeName())) {
            return AdminPolicyNode.PM_ADMIN_PC.nodeId();
        } else if (policyStore.graph().nodeExists(name)) {
            throw new NodeNameExistsException(name);
        }

        // create pc node
        policyStore.graph().createNode(id, name, PC);

        return id;
    }

    @Override
    public long createUserAttribute(String name, Collection<Long> assignments)
    throws PMException {
        return createNonPolicyClassNode(name, UA, assignments);
    }

    @Override
    public long createObjectAttribute(String name, Collection<Long> assignments)
    throws PMException {
        return createNonPolicyClassNode(name, OA, assignments);
    }

    @Override
    public long createObject(String name, Collection<Long> assignments) throws PMException {
        return createNonPolicyClassNode(name, O, assignments);
    }

    @Override
    public long createUser(String name, Collection<Long> assignments) throws PMException {
        return createNonPolicyClassNode(name, U, assignments);
    }

    @Override
    public void setNodeProperties(long id, Map<String, String> properties) throws PMException {
        checkSetNodePropertiesInput(id);

        policyStore.graph().setNodeProperties(id, properties);
    }

    @Override
    public void deleteNode(long id) throws PMException {
        if(!checkDeleteNodeInput(id)) {
            return;
        }

        policyStore.graph().deleteNode(id);
    }

    @Override
    public void assign(long ascId, Collection<Long> descendants) throws PMException {
        for (long descendant : descendants) {
            if(!checkAssignInput(ascId, descendant)) {
                continue;
            }

            policyStore.graph().createAssignment(ascId, descendant);
        }
    }

    @Override
    public void deassign(long ascendant, Collection<Long> descendants) throws PMException {
        for (long descendant : descendants) {
            if(!checkDeassignInput(ascendant, descendant)) {
                continue;
            }

            policyStore.graph().deleteAssignment(ascendant, descendant);
        }
    }

    @Override
    public void associate(long ua, long target, AccessRightSet accessRights) throws PMException {
        checkAssociateInput(ua, target, accessRights);

        policyStore.graph().createAssociation(ua, target, accessRights);
    }

    @Override
    public void dissociate(long ua, long target) throws PMException {
        if(!checkDissociateInput(ua, target)) {
            return;
        }

        policyStore.graph().deleteAssociation(ua, target);
    }

    /**
     * Check if a proposed assignment causes a loop.
     *
     * @param ascendant  The ascendant of the assignment.
     * @param descendant The descendant of the assignment.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected void checkAssignmentDoesNotCreateLoop(long ascendant, long descendant) throws PMException {
        AtomicBoolean loop = new AtomicBoolean(false);

        new GraphStoreDFS(policyStore.graph())
            .withVisitor((node -> {
                if (node != ascendant) {
                    return;
                }

                loop.set(true);
            }))
            .withDirection(Direction.DESCENDANTS)
            .withAllPathShortCircuit(node -> node == ascendant)
            .walk(descendant);

        if (loop.get()) {
            Node aNode = policyStore.graph().getNodeById(ascendant);
            Node dNode = policyStore.graph().getNodeById(descendant);
            throw new AssignmentCausesLoopException(aNode.getName(), dNode.getName());
        }
    }

    /**
     * Check if the given nodes exists.
     *
     * @param id The id of the node to check.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected void checkSetNodePropertiesInput(long id) throws PMException {
        if (!policyStore.graph().nodeExists(id)) {
            throw new NodeDoesNotExistException(id);
        }
    }

    /**
     * Check if the given node can be deleted. If the node is referenced in a prohibition or event pattern then it
     * cannot be deleted. If the node does not exist an error does not occur but return false to indicate to the caller
     * that execution should not proceed.
     *
     * @param id              The id of the node being deleted.
     * @return True if the execution should proceed, false otherwise.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected boolean checkDeleteNodeInput(long id) throws PMException {
        if (AdminPolicyNode.isAdminPolicyNode(id)) {
            throw new CannotDeleteAdminPolicyConfigException();
        } else if (!policyStore.graph().nodeExists(id)) {
            return false;
        }

        Collection<Long> ascendants;
        try {
            ascendants = policyStore.graph().getAdjacentAscendants(id);
        } catch (NodeDoesNotExistException e) {
            // quietly return if the nodes already does not exist as this is the desired state
            return false;
        }

        if (!ascendants.isEmpty()) {
            Node node = policyStore.graph().getNodeById(id);
            throw new NodeHasAscendantsException(node.getName());
        }

        checkIfNodeInProhibition(id);
        checkIfNodeIsObligationAuthor(id);

        return true;
    }

    /**
     * Helper method to check if a given node is referenced in any prohibitions. The default implementation loads all
     * prohibitions into memory and then searches through each one.
     *
     * @param id The node to check for.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected void checkIfNodeInProhibition(long id) throws PMException {
        Map<Long, Collection<Prohibition>> allProhibitions = policyStore.prohibitions().getNodeProhibitions();
        for (Collection<Prohibition> subjPros : allProhibitions.values()) {
            for (Prohibition p : subjPros) {
                if (nodeInProhibition(id, p)) {
                    Node node = policyStore.graph().getNodeById(id);
                    throw new NodeReferencedInProhibitionException(node.getName(), p.getName());
                }
            }
        }
    }

    /**
     * Helper method to check if a given node is referenced in any obligations as the author.
     * @param id The node to check for.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected void checkIfNodeIsObligationAuthor(long id) throws PMException {
        Collection<Obligation> obligationsWithAuthor = policyStore.obligations().getObligationsWithAuthor(id);
        if (obligationsWithAuthor.isEmpty()) {
            return;
        }

        throw new NodeReferencedInObligationException(
            policyStore.graph().getNodeById(id).getName(),
            obligationsWithAuthor.stream().map(Obligation::getName).collect(Collectors.toSet()));
    }

    /**
     * Check if both nodes exist and make a valid assignment. If the assignment already exists an error does not
     * occur but
     * return false to indicate to the caller that execution should not proceed.
     *
     * @param ascendant  The ascendant node.
     * @param descendant The descendant node.
     * @return True if the execution should proceed, false otherwise.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected boolean checkAssignInput(long ascendant, long descendant) throws PMException {
        // getting both nodes will check if they exist
        if (!policyStore.graph().nodeExists(ascendant)) {
            throw new NodeDoesNotExistException(ascendant);
        } else if (!policyStore.graph().nodeExists(descendant)) {
            throw new NodeDoesNotExistException(descendant);
        }

        // ignore if assignment already exists
        Collection<Long> descendants = policyStore.graph().getAdjacentDescendants(ascendant);
        if (new HashSet<>(descendants).contains(descendant)) {
            return false;
        }

        Node ascNode = policyStore.graph().getNodeById(ascendant);
        Node descNode = policyStore.graph().getNodeById(descendant);

        // check node types make a valid assignment relation
        checkAssignment(ascNode.getType(), descNode.getType());

        // check the assignment won't create a loop
        checkAssignmentDoesNotCreateLoop(ascendant, descendant);

        return true;
    }

    /**
     * Check if the ascendants and descendants exist. If the assignment does not exist an error does not occur but return
     * false to indicate to the caller that execution should not proceed. If trying to delete the admin policy config,
     * an error will occur.
     *
     * @param ascendant  The ascendant node.
     * @param descendant The descendant node.
     * @return True if the execution should proceed, false otherwise.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected boolean checkDeassignInput(long ascendant, long descendant) throws PMException {
        if (!policyStore.graph().nodeExists(ascendant)) {
            throw new NodeDoesNotExistException(ascendant);
        } else if (!policyStore.graph().nodeExists(descendant)) {
            throw new NodeDoesNotExistException(descendant);
        } else if (AdminPolicy.isAdminPolicyAssignment(ascendant, descendant)) {
            throw new CannotDeleteAdminPolicyConfigException();
        }

        Collection<Long> descs = policyStore.graph().getAdjacentDescendants(ascendant);
        if (!new HashSet<>(descs).contains(descendant)) {
            return false;
        }

        if (descs.size() == 1) {
            Node aNode = policyStore.graph().getNodeById(ascendant);
            Node dNode = policyStore.graph().getNodeById(descendant);
            throw new DisconnectedNodeException(aNode.getName(), dNode.getName());
        }

        return true;
    }

    /**
     * Check if the user attribute and target nodes exist and make up a valid association and that the given access
     * rights are allowed.
     *
     * @param ua           The user attribute.
     * @param target       The target node.
     * @param accessRights The access rights.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     * association.
     */
    protected void checkAssociateInput(long ua, long target, AccessRightSet accessRights) throws PMException {
        if (!policyStore.graph().nodeExists(ua)) {
            throw new NodeDoesNotExistException(ua);
        } else if (!policyStore.graph().nodeExists(target)) {
            throw new NodeDoesNotExistException(target);
        }

        Node uaNode = policyStore.graph().getNodeById(ua);
        Node targetNode = policyStore.graph().getNodeById(target);

        // check the access rights are valid
        validateAccessRights(policyStore.operations().getResourceAccessRights(), accessRights);

        // check the types of each node make a valid association
        checkAssociation(uaNode.getType(), targetNode.getType());
    }

    /**
     * Check if both nodes exist. If the association does not exist an error does not occur but return false to indicate
     * to the caller that execution should not proceed.
     *
     * @param ua     The user attribute.
     * @param target The target node.
     * @return True if the execution should proceed, false otherwise.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected boolean checkDissociateInput(long ua, long target) throws PMException {
        if (!policyStore.graph().nodeExists(ua)) {
            throw new NodeDoesNotExistException(ua);
        } else if (!policyStore.graph().nodeExists(target)) {
            throw new NodeDoesNotExistException(target);
        }

        Collection<Association> associations = policyStore.graph().getAssociationsWithSource(ua);
        for (Association a : associations) {
            if (a.source() == ua && a.target() == target) {
                return true;
            }
        }

        return false;
    }

    private static boolean nodeInProhibition(long id, Prohibition prohibition) {
        if (prohibition.getSubject().getNodeId() == id) {
            return true;
        }

        for (ContainerCondition containerCondition : prohibition.getContainers()) {
            if (containerCondition.getId() == id) {
                return true;
            }
        }

        return false;
    }

    private long createNonPolicyClassNode(String name, NodeType type, Collection<Long> descendants)
    throws PMException {
        long id = idGenerator.generateId(name, type);

        for (AdminPolicyNode adminNode : AdminPolicyNode.values()) {
            if (adminNode != AdminPolicyNode.PM_ADMIN_PC && name.equals(adminNode.nodeName())) {
                return adminNode.nodeId();
            }
        }

        if (policyStore.graph().nodeExists(name)) {
            throw new NodeNameExistsException(name);
        } else if (policyStore.graph().nodeExists(id)) {
            throw new NodeIdExistsException(id);
        }

        // need to be assigned to at least one node to avoid a disconnected graph
        if (descendants.isEmpty()) {
            throw new DisconnectedNodeException(name, type);
        }

        // check assign inputs
        for (long assignment : descendants) {
            String assignmentName = policyStore.graph().getNodeById(assignment).getName();
            if (name.equals(assignmentName)) {
                throw new AssignmentCausesLoopException(name, assignmentName);
            }

            Node assignNode = policyStore.graph().getNodeById(assignment);
            checkAssignment(type, assignNode.getType());
        }

        return runTx(() -> {
            policyStore.graph().createNode(id, name, type);

            for (long desc : descendants) {
                policyStore.graph().createAssignment(id, desc);
            }

            return id;
        });
    }

    private static final Map<NodeType, Set<NodeType>> validAssociations = Map.of(
        PC, Set.of(),
        OA, Set.of(),
        O, Set.of(),
        UA, Set.of(UA, OA, O),
        U, Set.of()
    );

    /**
     * Check if the provided types create a valid association.
     *
     * @param uaType     the type of the source node in the association. This should always be a user Attribute,
     *                   so an InvalidAssociationException will be thrown if it's not.
     * @param targetType the type of the target node. This can be either an Object Attribute or a user attribute.
     * @throws InvalidAssociationException if the provided types do not make a valid Association under NGAC
     */
    public static void checkAssociation(NodeType uaType, NodeType targetType) throws InvalidAssociationException {
        Set<NodeType> check = validAssociations.get(uaType);
        if (!check.contains(targetType)) {
            throw new InvalidAssociationException(String.format("cannot associate a node of type %s to a node of type %s", uaType, targetType));
        }
    }

    private static final Map<NodeType, Set<NodeType>> validAssignments = Map.of(
        NodeType.PC, Set.of(),
        NodeType.OA, Set.of(NodeType.OA, NodeType.PC),
        NodeType.O, Set.of(NodeType.OA, NodeType.PC),
        NodeType.UA, Set.of(NodeType.UA, NodeType.PC),
        NodeType.U, Set.of(NodeType.UA)
    );

    /**
     * Check if the assignment provided, is valid under NGAC.
     *
     * @param ascType The type of the ascendant node.
     * @param dscType The type of the descendant node.
     * @throws InvalidAssignmentException if the ascendant type is not allowed to be assigned to the descendant type.
     */
    public static void checkAssignment(NodeType ascType, NodeType dscType) throws InvalidAssignmentException {
        Set<NodeType> check = validAssignments.get(ascType);
        if (!check.contains(dscType)) {
            throw new InvalidAssignmentException(String.format("cannot assign a node of type %s to a node of type %s",
                ascType,
                dscType
            ));
        }
    }
}
