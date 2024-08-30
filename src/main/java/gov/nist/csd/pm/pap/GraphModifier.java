package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.dag.Direction;
import gov.nist.csd.pm.pap.graph.node.Node;
import gov.nist.csd.pm.pap.graph.node.NodeType;
import gov.nist.csd.pm.pap.graph.relationship.Assignment;
import gov.nist.csd.pm.pap.graph.relationship.Association;
import gov.nist.csd.pm.pap.modification.GraphModification;
import gov.nist.csd.pm.pap.obligation.EventPattern;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pap.prohibition.ContainerCondition;
import gov.nist.csd.pm.pap.prohibition.Prohibition;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.exception.*;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.pattern.operand.OperandPatternExpression;
import gov.nist.csd.pm.pap.store.GraphStoreDFS;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static gov.nist.csd.pm.pap.graph.node.NodeType.*;
import static gov.nist.csd.pm.pap.op.AdminAccessRights.*;
import static gov.nist.csd.pm.pap.op.AdminAccessRights.wildcardAccessRights;

public class GraphModifier extends Modifier implements GraphModification {

    public GraphModifier(PolicyStore store) {
        super(store);
    }

    public GraphModifier(Modifier modifier) {
        super(modifier);
    }

    @Override
    public String createPolicyClass(String name) throws PMException {
        if (!checkCreatePolicyClassInput(name)) {
            return name;
        }

        // create pc node
        store.graph().createNode(name, PC);

        return name;
    }

    @Override
    public String createUserAttribute(String name, Collection<String> assignments)
            throws PMException {
        return createNonPolicyClassNode(name, UA, assignments);
    }

    @Override
    public String createObjectAttribute(String name, Collection<String> assignments)
            throws PMException {
        return createNonPolicyClassNode(name, OA, assignments);
    }

    @Override
    public String createObject(String name, Collection<String> assignments) throws PMException {
        return createNonPolicyClassNode(name, O, assignments);
    }

    @Override
    public String createUser(String name, Collection<String> assignments) throws PMException {
        return createNonPolicyClassNode(name, U, assignments);
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties) throws PMException {
        checkSetNodePropertiesInput(name);

        store.graph().setNodeProperties(name, properties);
    }

    @Override
    public void deleteNode(String name) throws PMException {
        if(!checkDeleteNodeInput(name)) {
            return;
        }

        store.graph().deleteNode(name);
    }

    @Override
    public void assign(String ascendant, Collection<String> descendants) throws PMException {
        if(!checkAssignInput(ascendant, descendants)) {
            return;
        }

        for (String descendant : descendants) {
            store.graph().createAssignment(ascendant, descendant);
        }
    }

    @Override
    public void deassign(String ascendant, Collection<String> descendants) throws PMException {
        if(!checkDeassignInput(ascendant, descendants)) {
            return;
        }

        for (String descendant : descendants) {
            store.graph().deleteAssignment(ascendant, descendant);
        }
    }

    @Override
    public void associate(String ua, String target, AccessRightSet accessRights) throws PMException {
        checkAssociateInput(ua, target, accessRights);

        store.graph().createAssociation(ua, target, accessRights);
    }

    @Override
    public void dissociate(String ua, String target) throws PMException {
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
    protected void checkAssignmentDoesNotCreateLoop(String ascendant, String descendant) throws PMException {
        AtomicBoolean loop = new AtomicBoolean(false);

        new GraphStoreDFS(store.graph())
                .withVisitor((node -> {
                    if (!node.equals(ascendant)) {
                        return;
                    }

                    loop.set(true);
                }))
                .withDirection(Direction.DESCENDANTS)
                .withAllPathShortCircuit(node -> node.equals(ascendant))
                .walk(descendant);

        if (loop.get()) {
            throw new AssignmentCausesLoopException(ascendant, descendant);
        }
    }

    /**
     * Check that the given policy class name is not taken by another node.
     *
     * @param name The name to check.
     * @return True if execution should proceed, false otherwise
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected boolean checkCreatePolicyClassInput(String name) throws PMException {
        if (name.equals(AdminPolicyNode.ADMIN_POLICY.nodeName())) {
            return false;
        } else if (store.graph().nodeExists(name)) {
            throw new NodeNameExistsException(name);
        }

        return true;
    }

    /**
     * Check the node name does not already exist and ensure the given descendant nodes exist and form a valid assignment.
     *
     * @param name    The name of the new node.
     * @param type    The type of the new node.
     * @param descendants Nodes to assign the new node to.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     * assignment.
     */
    protected boolean checkCreateNodeInput(String name, NodeType type, Collection<String> descendants) throws PMException {
        if (name.equals(AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName())) {
            return false;
        } else if (store.graph().nodeExists(name)) {
            throw new NodeNameExistsException(name);
        }

        // when creating a node the only loop that can occur is to itself
        if (descendants.contains(name)) {
            throw new AssignmentCausesLoopException(name, name);
        }

        // need to be assigned to at least one node to avoid a disconnected graph
        if (descendants.isEmpty()) {
            throw new DisconnectedNodeException(name, type);
        }

        // check assign inputs
        for (String assignment : descendants) {
            if (name.equals(assignment)) {
                throw new AssignmentCausesLoopException(name, assignment);
            }

            if (!store.graph().nodeExists(assignment)) {
                throw new NodeDoesNotExistException(assignment);
            }

            Node assignNode = store.graph().getNode(assignment);
            Assignment.checkAssignment(type, assignNode.getType());
        }

        return true;
    }

    /**
     * Check if the given nodes exists.
     *
     * @param name The name of the node to check.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected void checkSetNodePropertiesInput(String name) throws PMException {
        if (!store.graph().nodeExists(name)) {
            throw new NodeDoesNotExistException(name);
        }
    }

    /**
     * Check if the given node can be deleted. If the node is referenced in a prohibition or event pattern then it
     * cannot
     * be deleted. If the node does not exist an error does not occur but return false to indicate to the caller that
     * execution should not proceed.
     *
     * @param name              The name of the node being deleted.
     *                          pattern.
     * @return True if the execution should proceed, false otherwise.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected boolean checkDeleteNodeInput(String name) throws PMException {
        if (!store.graph().nodeExists(name)) {
            return false;
        }

        Collection<String> ascendants;
        try {
            ascendants = store.graph().getAdjacentAscendants(name);
        } catch (NodeDoesNotExistException e) {
            // quietly return if the nodes already does not exist as this is the desired state
            return false;
        }

        if (!ascendants.isEmpty()) {
            throw new NodeHasAscendantsException(name);
        }

        checkIfNodeInProhibition(name);
        checkIfNodeInObligation(name);

        return true;
    }

    /**
     * Helper method to check if a given node is referenced in any prohibitions. The default implementation loads all
     * prohibitions into memory and then searches through each one.
     *
     * @param name             The node to check for.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected void checkIfNodeInProhibition(String name) throws PMException {
        Map<String, Collection<Prohibition>> allProhibitions = store.prohibitions().getProhibitions();
        for (Collection<Prohibition> subjPros : allProhibitions.values()) {
            for (Prohibition p : subjPros) {
                if (nodeInProhibition(name, p)) {
                    throw new NodeReferencedInProhibitionException(name, p.getName());
                }
            }
        }
    }

    /**
     * Helper method to check if a given node is referenced in any obligations. The default implementation loads all
     * obligations into memory and then searches through each one.
     *
     * @param name             The node to check for.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected void checkIfNodeInObligation(String name) throws PMException {
        Collection<Obligation> obligations = store.obligations().getObligations();
        for (Obligation obligation : obligations) {
            // if the node is the author of the obligation or referenced in any rules throw an exception
            if (obligation.getAuthor().equals(name)) {
                throw new NodeReferencedInObligationException(name, obligation.getName());
            }

            // check if node referenced in pattern
            for (Rule rule : obligation.getRules()) {
                EventPattern eventPattern = rule.getEventPattern();

                // check subject and operation patterns
                boolean referenced = checkPatternForNode(name, eventPattern.getSubjectPattern());

                // check operand patterns
                for (List<OperandPatternExpression> pattern : eventPattern.getOperandPatterns().values()) {
                    for (OperandPatternExpression operandPatternExpression : pattern) {
                        if (checkPatternForNode(name, operandPatternExpression)) {
                            referenced = true;
                        }
                    }
                }

                if (referenced) {
                    throw new NodeReferencedInObligationException(name, obligation.getName());
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
     * @param descendants The descendant nodes.
     * @return True if the execution should proceed, false otherwise.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected boolean checkAssignInput(String ascendant, Collection<String> descendants) throws PMException {
        for (String descendant : descendants) {
            // getting both nodes will check if they exist
            if (!store.graph().nodeExists(ascendant)) {
                throw new NodeDoesNotExistException(ascendant);
            } else if (!store.graph().nodeExists(descendant)) {
                throw new NodeDoesNotExistException(descendant);
            }

            // ignore if assignment already exists
            if (store.graph().getAdjacentDescendants(ascendant).contains(descendant)) {
                return false;
            }

            Node ascNode = store.graph().getNode(ascendant);
            Node descNode = store.graph().getNode(descendant);

            // check node types make a valid assignment relation
            Assignment.checkAssignment(ascNode.getType(), descNode.getType());

            // check the assignment won't create a loop
            checkAssignmentDoesNotCreateLoop(ascendant, descendant);
        }

        return true;
    }

    /**
     * Check if the ascendants and descendants exist. If the assignment does not exist an error does not occur but return
     * false to indicate to the caller that execution should not proceed. If trying to delete the admin policy config,
     * an error will occur.
     *
     * @param ascendant  The ascendant node.
     * @param descendants The descendant nodes.
     * @return True if the execution should proceed, false otherwise.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected boolean checkDeassignInput(String ascendant, Collection<String> descendants) throws PMException {
        for (String descendant : descendants) {
            if (!store.graph().nodeExists(ascendant)) {
                throw new NodeDoesNotExistException(ascendant);
            } else if (!store.graph().nodeExists(descendant)) {
                throw new NodeDoesNotExistException(descendant);
            } else if (ascendant.equals(AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName()) &&
                    descendant.equals(AdminPolicyNode.ADMIN_POLICY.nodeName())) {
                throw new CannotDeleteAdminPolicyConfigException();
            }

            Collection<String> descs = store.graph().getAdjacentDescendants(ascendant);
            if (!descs.contains(descendant)) {
                return false;
            }

            if (descs.size() == 1) {
                throw new DisconnectedNodeException(ascendant, descendant);
            }
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
    protected void checkAssociateInput(String ua, String target, AccessRightSet accessRights) throws PMException {
        if (!store.graph().nodeExists(ua)) {
            throw new NodeDoesNotExistException(ua);
        } else if (!store.graph().nodeExists(target)) {
            throw new NodeDoesNotExistException(target);
        }

        Node uaNode = store.graph().getNode(ua);
        Node targetNode = store.graph().getNode(target);

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
    protected boolean checkDissociateInput(String ua, String target) throws PMException {
        if (!store.graph().nodeExists(ua)) {
            throw new NodeDoesNotExistException(ua);
        } else if (!store.graph().nodeExists(target)) {
            throw new NodeDoesNotExistException(target);
        }

        Collection<Association> associations = store.graph().getAssociationsWithSource(ua);
        for (Association a : associations) {
            if (a.getSource().equals(ua) && a.getTarget().equals(target)) {
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

    private static boolean nodeInProhibition(String name, Prohibition prohibition) {
        if (prohibition.getSubject().getName().equals(name)) {
            return true;
        }

        for (ContainerCondition containerCondition : prohibition.getContainers()) {
            if (containerCondition.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    private String createNonPolicyClassNode(String name, NodeType type, Collection<String> assignments)
            throws PMException {
        return runTx(() -> {
            if (!checkCreateNodeInput(name, type, assignments)) {
                return name;
            }

            store.graph().createNode(name, type);

            for (String assignmentNode : assignments) {
                store.graph().createAssignment(name, assignmentNode);
            }

            return name;
        });

    }
}
