package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.exception.*;
import gov.nist.csd.pm.common.graph.dag.Direction;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Assignment;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.obligation.EventPattern;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.id.IdGenerator;
import gov.nist.csd.pm.pap.modification.GraphModification;
import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.pattern.arg.ArgPatternExpression;
import gov.nist.csd.pm.pap.store.GraphStoreDFS;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static gov.nist.csd.pm.common.graph.node.NodeType.*;
import static gov.nist.csd.pm.pap.AdminAccessRights.allAdminAccessRights;
import static gov.nist.csd.pm.pap.AdminAccessRights.wildcardAccessRights;

public class GraphModifier extends Modifier implements GraphModification {

    public GraphModifier(PolicyStore store, IdGenerator idGenerator) {
        super(store, idGenerator);
    }

    @Override
    public long createPolicyClass(String name) throws PMException {
        long id = idGenerator.generateId(name, PC);

        if (name.equals(AdminPolicyNode.PM_ADMIN_PC.nodeName())) {
            return AdminPolicyNode.PM_ADMIN_PC.nodeId();
        } else if (store.graph().nodeExists(name)) {
            throw new NodeNameExistsException(name);
        }

        // create pc node
        store.graph().createNode(id, name, PC);

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

        store.graph().setNodeProperties(id, properties);
    }

    @Override
    public void deleteNode(long id) throws PMException {
        if(!checkDeleteNodeInput(id)) {
            return;
        }

        store.graph().deleteNode(id);
    }

    @Override
    public void assign(long ascId, Collection<Long> descendants) throws PMException {
        for (long descendant : descendants) {
            if(!checkAssignInput(ascId, descendant)) {
                continue;
            }

            store.graph().createAssignment(ascId, descendant);
        }
    }

    @Override
    public void deassign(long ascendant, Collection<Long> descendants) throws PMException {
        for (long descendant : descendants) {
            if(!checkDeassignInput(ascendant, descendant)) {
                continue;
            }

            store.graph().deleteAssignment(ascendant, descendant);
        }
    }

    @Override
    public void associate(long ua, long target, AccessRightSet accessRights) throws PMException {
        checkAssociateInput(ua, target, accessRights);

        store.graph().createAssociation(ua, target, accessRights);
    }

    @Override
    public void dissociate(long ua, long target) throws PMException {
        if(!checkDissociateInput(ua, target)) {
            return;
        }

        store.graph().deleteAssociation(ua, target);
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

        new GraphStoreDFS(store.graph())
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
            Node aNode = store.graph().getNodeById(ascendant);
            Node dNode = store.graph().getNodeById(descendant);
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
        if (!store.graph().nodeExists(id)) {
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
        if (!store.graph().nodeExists(id)) {
            return false;
        }

        Collection<Long> ascendants;
        try {
            ascendants = store.graph().getAdjacentAscendants(id);
        } catch (NodeDoesNotExistException e) {
            // quietly return if the nodes already does not exist as this is the desired state
            return false;
        }

        if (!ascendants.isEmpty()) {
            Node node = store.graph().getNodeById(id);
            throw new NodeHasAscendantsException(node.nameAndId());
        }

        checkIfNodeInProhibition(id);
        checkIfNodeInObligation(id);

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
        Map<Long, Collection<Prohibition>> allProhibitions = store.prohibitions().getNodeProhibitions();
        for (Collection<Prohibition> subjPros : allProhibitions.values()) {
            for (Prohibition p : subjPros) {
                if (nodeInProhibition(id, p)) {
                    Node node = store.graph().getNodeById(id);
                    throw new NodeReferencedInProhibitionException(node.nameAndId(), p.getName());
                }
            }
        }
    }

    /**
     * Helper method to check if a given node is referenced in any obligations. The default implementation loads all
     * obligations into memory and then searches through each one.
     *
     * @param id             The node to check for.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected void checkIfNodeInObligation(long id) throws PMException {
        Node node = store.graph().getNodeById(id);

        Collection<Obligation> obligations = store.obligations().getObligations();
        for (Obligation obligation : obligations) {
            // if the node is the author of the obligation or referenced in any rules throw an exception
            if (obligation.getAuthorId() == id) {
                throw new NodeReferencedInObligationException(node.nameAndId(), obligation.getName());
            }

            // check if node referenced in pattern
            for (Rule rule : obligation.getRules()) {
                EventPattern eventPattern = rule.getEventPattern();

                // check subject and operation patterns
                boolean referenced = checkPatternForNode(node.getName(), eventPattern.getSubjectPattern());

                // check arg patterns
                for (List<ArgPatternExpression> pattern : eventPattern.getArgPatterns().values()) {
                    for (ArgPatternExpression argPatternExpression : pattern) {
                        if (checkPatternForNode(node.getName(), argPatternExpression)) {
                            referenced = true;
                        }
                    }
                }

                if (referenced) {
                    throw new NodeReferencedInObligationException(node.nameAndId(), obligation.getName());
                }
            }
        }
    }

    private boolean checkPatternForNode(String entity, Pattern pattern) {
        return pattern.getReferencedNodes().nodes().contains(entity);
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
        if (!store.graph().nodeExists(ascendant)) {
            throw new NodeDoesNotExistException(ascendant);
        } else if (!store.graph().nodeExists(descendant)) {
            throw new NodeDoesNotExistException(descendant);
        }

        // ignore if assignment already exists
        Collection<Long> descendants = store.graph().getAdjacentDescendants(ascendant);
        if (new HashSet<>(descendants).contains(descendant)) {
            return false;
        }

        Node ascNode = store.graph().getNodeById(ascendant);
        Node descNode = store.graph().getNodeById(descendant);

        // check node types make a valid assignment relation
        Assignment.checkAssignment(ascNode.getType(), descNode.getType());

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
        if (!store.graph().nodeExists(ascendant)) {
            throw new NodeDoesNotExistException(ascendant);
        } else if (!store.graph().nodeExists(descendant)) {
            throw new NodeDoesNotExistException(descendant);
        } else if (ascendant == AdminPolicyNode.PM_ADMIN_OBJECT.nodeId() &&
                descendant == AdminPolicyNode.PM_ADMIN_PC.nodeId()) {
            throw new CannotDeleteAdminPolicyConfigException();
        }

        Collection<Long> descs = store.graph().getAdjacentDescendants(ascendant);
        if (!new HashSet<>(descs).contains(descendant)) {
            return false;
        }

        if (descs.size() == 1) {
            Node aNode = store.graph().getNodeById(ascendant);
            Node dNode = store.graph().getNodeById(descendant);
            throw new DisconnectedNodeException(aNode.nameAndId(), dNode.nameAndId());
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
        if (!store.graph().nodeExists(ua)) {
            throw new NodeDoesNotExistException(ua);
        } else if (!store.graph().nodeExists(target)) {
            throw new NodeDoesNotExistException(target);
        }

        Node uaNode = store.graph().getNodeById(ua);
        Node targetNode = store.graph().getNodeById(target);

        // check the access rights are valid
        checkAccessRightsValid(store.operations().getResourceOperations(), accessRights);

        // check the types of each node make a valid association
        Association.checkAssociation(uaNode.getType(), targetNode.getType());
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
        if (!store.graph().nodeExists(ua)) {
            throw new NodeDoesNotExistException(ua);
        } else if (!store.graph().nodeExists(target)) {
            throw new NodeDoesNotExistException(target);
        }

        Collection<Association> associations = store.graph().getAssociationsWithSource(ua);
        for (Association a : associations) {
            if (a.getSource() == ua && a.getTarget() == target) {
                return true;
            }
        }

        return false;
    }

    static void checkAccessRightsValid(AccessRightSet resourceAccessRights, AccessRightSet accessRightSet) throws PMException {
        for (String ar : accessRightSet) {
            if (!resourceAccessRights.contains(ar)
                    && !allAdminAccessRights().contains(ar)
                    && !wildcardAccessRights().contains(ar)) {
                throw new UnknownAccessRightException(ar);
            }
        }
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

        if (name.equals(AdminPolicyNode.PM_ADMIN_OBJECT.nodeName())) {
            return AdminPolicyNode.PM_ADMIN_OBJECT.nodeId();
        } else if (store.graph().nodeExists(name)) {
            throw new NodeNameExistsException(name);
        } else if (store.graph().nodeExists(id)) {
            throw new NodeIdExistsException(id);
        }

        // need to be assigned to at least one node to avoid a disconnected graph
        if (descendants.isEmpty()) {
            throw new DisconnectedNodeException(name, type);
        }

        // check assign inputs
        for (long assignment : descendants) {
            String assignmentName = store.graph().getNodeById(assignment).getName();
            if (name.equals(assignmentName)) {
                throw new AssignmentCausesLoopException(name, assignmentName);
            }

            Node assignNode = store.graph().getNodeById(assignment);
            Assignment.checkAssignment(type, assignNode.getType());
        }

        return runTx(() -> {
            store.graph().createNode(id, name, type);

            for (long desc : descendants) {
                store.graph().createAssignment(id, desc);
            }

            return id;
        });
    }
}
