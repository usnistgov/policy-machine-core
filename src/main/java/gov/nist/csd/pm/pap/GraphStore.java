package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.memory.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.policy.Graph;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Assignment;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssignmentException;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssociationException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.wildcardAccessRights;

/**
 * GraphStore extends the {@link Graph} interface and outlines how a concrete implementation of the interface
 * at the Policy Administration Point (PAP) level of the Policy Machine should behave including input validation and
 * expected exceptions.
 */
public interface GraphStore extends Graph {

    /**
     * See {@link Graph#setResourceAccessRights(AccessRightSet)} <p>
     *
     * @throws AdminAccessRightExistsException If a provided access right is already defined as an administrative
     * access right.
     * @throws PMBackendException              If there is an error executing the command in the PIP.
     */
    @Override
    void setResourceAccessRights(AccessRightSet accessRightSet)
    throws AdminAccessRightExistsException, PMBackendException;

    /**
     * See {@link Graph#getResourceAccessRights()} <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    AccessRightSet getResourceAccessRights() throws PMBackendException;

    /**
     * Create a policy class in the graph. This method should also create an object attribute that represents the
     * policy class in {@link AdminPolicy#POLICY_CLASSES_OA}. This object attribute can be used in
     * the future to create associations with the policy class itself which is not a supported relation in NGAC.
     * If the provided name equals {@link AdminPolicy#ADMIN_POLICY} then this method should also create the
     * {@link AdminPolicy#POLICY_CLASSES_OA} node and assign it to ADMIN_POLICY before creating
     * {@link AdminPolicy#ADMIN_POLICY_TARGET} <p>
     *
     * See {@link Graph#createPolicyClass(String)} <p>
     *
     * @throws NodeNameExistsException If a node of any type already exists with the provided name.
     * @throws PMBackendException      If there is an error executing the command in the PIP.
     */
    @Override
    String createPolicyClass(String name, Map<String, String> properties)
    throws NodeNameExistsException, PMBackendException;

    /**
     * See {@link GraphStore#createPolicyClass(String, Map)}
     */
    @Override
    String createPolicyClass(String name) throws NodeNameExistsException, PMBackendException;

    /**
     * See {@link Graph#createUserAttribute(String, Map, String, String...)} <p>
     *
     * @throws NodeNameExistsException    If a node already exists with the provided name.
     * @throws NodeDoesNotExistException  If any of the provided parent nodes do not exist.
     * @throws InvalidAssignmentException If any of the parent nodes have a node type other than UA and PC.
     * @throws PMBackendException         If there is an error executing the command in the PIP.
     */
    @Override
    String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents)
    throws NodeNameExistsException, NodeDoesNotExistException, InvalidAssignmentException, PMBackendException, AssignmentCausesLoopException;

    /**
     * See {@link GraphStore#createUserAttribute(String, Map, String, String...)} <p>
     */
    @Override
    String createUserAttribute(String name, String parent, String... parents)
    throws NodeNameExistsException, NodeDoesNotExistException, InvalidAssignmentException, PMBackendException, AssignmentCausesLoopException;

    /**
     * See {@link Graph#createObjectAttribute(String, Map, String, String...)} <p>
     *
     * @throws NodeNameExistsException    If a node already exists with the provided name.
     * @throws NodeDoesNotExistException  If any of the provided parent nodes do not exist.
     * @throws InvalidAssignmentException If any of the parent nodes have a node type other than OA and PC.
     * @throws PMBackendException         If there is an error executing the command in the PIP.
     */
    @Override
    String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents)
    throws NodeNameExistsException, NodeDoesNotExistException, InvalidAssignmentException, PMBackendException, AssignmentCausesLoopException;

    /**
     * See {@link GraphStore#createObjectAttribute(String, Map, String, String...)}
     */
    @Override
    String createObjectAttribute(String name, String parent, String... parents)
    throws NodeNameExistsException, NodeDoesNotExistException, InvalidAssignmentException, PMBackendException, AssignmentCausesLoopException;

    /**
     * See {@link Graph#createObject(String, Map, String, String...)} <p>
     *
     * @throws NodeNameExistsException    If a node already exists with the provided name.
     * @throws NodeDoesNotExistException  If any of the provided parent nodes do not exist.
     * @throws InvalidAssignmentException If any of the parent nodes have a node type other than OA.
     * @throws PMBackendException         If there is an error executing the command in the PIP.
     */
    @Override
    String createObject(String name, Map<String, String> properties, String parent, String... parents)
    throws NodeNameExistsException, NodeDoesNotExistException, InvalidAssignmentException, PMBackendException, AssignmentCausesLoopException;

    /**
     * See {@link GraphStore#createObject(String, Map, String, String...)} <p>
     */
    @Override
    String createObject(String name, String parent, String... parents)
    throws NodeNameExistsException, NodeDoesNotExistException, InvalidAssignmentException, PMBackendException, AssignmentCausesLoopException;

    /**
     * See {@link Graph#createUser(String, Map, String, String...)} <p>
     *
     * @throws NodeNameExistsException    If a node already exists with the provided name.
     * @throws NodeDoesNotExistException  If any of the provided parent nodes do not exist.
     * @throws InvalidAssignmentException If any of the parent nodes have a node type other than UA.
     * @throws PMBackendException         If there is an error executing the command in the PIP.
     */
    @Override
    String createUser(String name, Map<String, String> properties, String parent, String... parents)
    throws NodeNameExistsException, NodeDoesNotExistException, InvalidAssignmentException, PMBackendException, AssignmentCausesLoopException;

    /**
     * See {@link GraphStore#createUser(String, Map, String, String...)}  <p>
     */
    @Override
    String createUser(String name, String parent, String... parents)
    throws NodeNameExistsException, NodeDoesNotExistException, InvalidAssignmentException, PMBackendException, AssignmentCausesLoopException;

    /**
     * See {@link Graph#setNodeProperties <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    void setNodeProperties(String name, Map<String, String> properties)
    throws NodeDoesNotExistException, PMBackendException;

    /**
     * See {@link Graph#nodeExists(String)}  <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    boolean nodeExists(String name) throws PMBackendException;

    /**
     * See {@link Graph#getNode(String)}  <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    Node getNode(String name) throws NodeDoesNotExistException, PMBackendException;

    /**
     * See {@link Graph#search(NodeType, Map)}  <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    List<String> search(NodeType type, Map<String, String> properties) throws PMBackendException;


    /**
     * See {@link Graph#getPolicyClasses()}  <p>
     *
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    @Override
    List<String> getPolicyClasses() throws PMBackendException;

    /**
     * Delete the node with the given name from the graph. If the node is a policy class this will also delete the
     * representative object attribute. An exception will be thrown if the node has any nodes assigned to it or if
     * the node is defined in a prohibition or an obligation event pattern. If the node does not exist, no exception
     * will be thrown as this is the desired state. <p>
     *
     * See {@link Graph#deleteNode(String)}  <p>
     *
     * @throws NodeHasChildrenException             If the node being deleted still has nodes assigned to it.
     * @throws NodeReferencedInProhibitionException If the node is specified in a prohibition.
     * @throws NodeReferencedInObligationException  If the node is specified in an obligation.
     * @throws PMBackendException                   If there is an error executing the command in the PIP.
     */
    @Override
    void deleteNode(String name) throws NodeHasChildrenException, NodeReferencedInProhibitionException,
                                        NodeReferencedInObligationException, PMBackendException;

    /**
     * The child and parent nodes must both already exist in the graph, and the types must make a valid assignment. If
     * the child is already assigned to the parent, no exception will be thrown as this is the desired state. <p>
     *
     * See {@link Graph#assign(String, String)}  <p>
     *
     * @throws NodeDoesNotExistException  If either node does not exist.
     * @throws InvalidAssignmentException If the type of the child and the parent do not make a valid assignment.
     * @throws PMBackendException         If there is an error executing the command in the PIP.
     */
    @Override
    void assign(String child, String parent)
    throws NodeDoesNotExistException, InvalidAssignmentException, PMBackendException, AssignmentCausesLoopException;

    /**
     * Delete an assignment. An exception will be thrown if either node does not exist. If the assignment doesn't exist,
     * no exception will be thrown as this is the desired state. <p>
     *
     * See {@link Graph#deassign(String, String)}  <p>
     *
     * @throws NodeDoesNotExistException If either node does not exist.
     * @throws DisconnectedNodeException If deassinging the child node from the parent would cause the child node to be
     *                                  disconnected from the graph.
     * @throws PMBackendException        If there is an error executing the command in the PIP.
     */
    @Override
    void deassign(String child, String parent) throws NodeDoesNotExistException, DisconnectedNodeException, PMBackendException;

    /**
     * See {@link Graph#getParents(String)}  <p>
     *
     * @throws NodeDoesNotExistException If the node does not exist.
     * @throws PMBackendException        If there is an error executing the command in the PIP.
     */
    @Override
    List<String> getParents(String node) throws NodeDoesNotExistException, PMBackendException;

    /**
     * See {@link Graph#getChildren(String)}  <p>
     *
     * @throws NodeDoesNotExistException If the node does not exist.
     * @throws PMBackendException        If there is an error executing the command in the PIP.
     */
    @Override
    List<String> getChildren(String node) throws NodeDoesNotExistException, PMBackendException;

    /**
     * Create an association between the user attribute and the target node with the provided access rights.
     * If an association already exists between these two nodes, overwrite the existing access rights with the ones
     * provided. Associations can only begin at a user attribute but can point to either an object or user attribute. If
     * either node does not exist or a provided access right is unknown to the policy an exception will be thrown. <p>
     *
     * See {@link Graph#associate(String, String, AccessRightSet)}  <p>
     *
     * @throws NodeDoesNotExistException   If either node does not exist.
     * @throws InvalidAssociationException If the node types create an invalid association relation.
     * @throws PMBackendException          If there is an error executing the command in the PIP.
     */
    @Override
    void associate(String ua, String target, AccessRightSet accessRights)
    throws NodeDoesNotExistException, InvalidAssociationException, PMBackendException, UnknownAccessRightException;

    /**
     * Delete the association between the user attribute and target node.  If either of the nodes does not exist an
     * exception will be thrown. If the association does not exist no exception will be thrown as this is the desired
     * state. <p>
     *
     * See {@link Graph#dissociate(String, String)}  <p>
     *
     * @throws NodeDoesNotExistException If either node does not exist.
     * @throws PMBackendException        If there is an error executing the command in the PIP.
     */
    @Override
    void dissociate(String ua, String target) throws NodeDoesNotExistException, PMBackendException;

    /**
     * See {@link Graph#getAssociationsWithSource(String)}  <p>
     *
     * @throws NodeDoesNotExistException If the node does not exist.
     * @throws PMBackendException        If there is an error executing the command in the PIP.
     */
    @Override
    List<Association> getAssociationsWithSource(String ua) throws NodeDoesNotExistException, PMBackendException;

    /**
     * See {@link Graph#getAssociationsWithTarget(String)}  <p>
     *
     * @throws NodeDoesNotExistException If the node does not exist.
     * @throws PMBackendException        If there is an error executing the command in the PIP.
     */
    @Override
    List<Association> getAssociationsWithTarget(String target) throws NodeDoesNotExistException, PMBackendException;

    /**
     * Check if a proposed assignment causes a loop.
     *
     * @param child  The child of the assignment.
     * @param parent The parent of the assignment.
     * @throws AssignmentCausesLoopException If the assignment will cause a loop.
     * @throws PMBackendException            If there is an error executing the command in the PIP.
     */
    default void checkAssignmentDoesNotCreateLoop(String child, String parent)
    throws AssignmentCausesLoopException, PMBackendException {
        AtomicBoolean loop = new AtomicBoolean(false);

        try {
            new DepthFirstGraphWalker(this)
                    .withVisitor((node -> {
                        if (!node.equals(child)) {
                            return;
                        }

                        loop.set(true);
                    }))
                    .withDirection(Direction.PARENTS)
                    .withAllPathShortCircuit(node -> node.equals(child))
                    .walk(parent);
        } catch (PMException e) {
            throw new PMBackendException(e);
        }

        if (loop.get()) {
            throw new AssignmentCausesLoopException(child, parent);
        }
    }

    /**
     * Check that the provided resource access rights are not existing admin access rights.
     *
     * @param accessRightSet The access right set to check.
     * @throws AdminAccessRightExistsException If a provided access right is already defined as an admin access right.
     */
    default void checkSetResourceAccessRightsInput(AccessRightSet accessRightSet)
    throws AdminAccessRightExistsException {
        for (String ar : accessRightSet) {
            if (isAdminAccessRight(ar) || isWildcardAccessRight(ar)) {
                throw new AdminAccessRightExistsException(ar);
            }
        }
    }

    /**
     * Check that the given policy class name is not taken by another node.
     *
     * @param name The name to check.
     * @throws PMBackendException      If there is an error in the backend implementation.
     * @throws NodeNameExistsException If the provided name already exists in the graph.
     */
    default void checkCreatePolicyClassInput(String name) throws PMBackendException, NodeNameExistsException {
        if (nodeExists(name)) {
            throw new NodeNameExistsException(name);
        }
    }

    /**
     * Check the node name does not already exist and ensure the give parent nodes exist and make up a valid assignment.
     *
     * @param name    The name of the new node.
     * @param type    The type of the new node.
     * @param parent  The initial parent to assign the new node to.
     * @param parents Additional parents to assign the new node to.
     * @throws PMBackendException         If there is an error in the backend implementation.
     * @throws NodeNameExistsException    If a node already exists with the given name.
     * @throws NodeDoesNotExistException  If a given parent node does not exist.
     * @throws InvalidAssignmentException If assigning the new node to a given parent node would make an invalid
     * assignment.
     */
    default void checkCreateNodeInput(String name, NodeType type, String parent, String... parents)
    throws PMBackendException, NodeNameExistsException, NodeDoesNotExistException, InvalidAssignmentException,
           AssignmentCausesLoopException {
        if (nodeExists(name)) {
            throw new NodeNameExistsException(name);
        }

        // when creating a node the only loop that can occur is to itself
        if (name.equals(parent)) {
            throw new AssignmentCausesLoopException(name, parent);
        }

        // check assign inputs
        // getNode will ensure parent node exists
        Node parentNode = getNode(parent);
        Assignment.checkAssignment(type, parentNode.getType());

        for (String p : parents) {
            if (name.equals(p)) {
                throw new AssignmentCausesLoopException(name, p);
            }

            parentNode = getNode(p);
            Assignment.checkAssignment(type, parentNode.getType());
        }
    }

    /**
     * Check if the given nodes exists.
     *
     * @param name The name of the node to check.
     * @throws PMBackendException        If there is an error in the backend implementation.
     * @throws NodeDoesNotExistException If the node does not exist.
     */
    default void checkSetNodePropertiesInput(String name) throws PMBackendException, NodeDoesNotExistException {
        if (!nodeExists(name)) {
            throw new NodeDoesNotExistException(name);
        }
    }

    /**
     * Check if the node exists.
     *
     * @param name The node to check.
     * @throws PMBackendException        If there is an error in the backend implementation.
     * @throws NodeDoesNotExistException If the given node does not exist.
     */
    default void checkGetNodeInput(String name) throws PMBackendException, NodeDoesNotExistException {
        if (!nodeExists(name)) {
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
     * @param prohibitionsStore The ProhibitionStore used to check if the node is referenced in a prohibition.
     * @param obligationsStore  The ObligationStore used to check if the node is referenced in an obligation event
     *                          pattern.
     * @return True if the execution should proceed, false otherwise.
     * @throws PMBackendException                   If there is an error in the backend implementation.
     * @throws NodeHasChildrenException             If the node still has other nodes assigned to it.
     * @throws NodeReferencedInProhibitionException If the node is referenced in a prohibition.
     * @throws NodeReferencedInObligationException  If the node is referenced in an obligation event pattern.
     */
    default boolean checkDeleteNodeInput(String name, ProhibitionsStore prohibitionsStore,
                                         ObligationsStore obligationsStore)
    throws PMBackendException, NodeHasChildrenException, NodeReferencedInProhibitionException,
           NodeReferencedInObligationException {
        List<String> children;
        try {
            children = getChildren(name);
        } catch (NodeDoesNotExistException e) {
            // quietly return if the nodes already does not exist as this is the desired state
            return false;
        }

        if (!children.isEmpty()) {
            throw new NodeHasChildrenException(name);
        }

        checkIfNodeInProhibition(name, prohibitionsStore);
        checkIfNodeInObligation(name, obligationsStore);

        return true;
    }

    /**
     * Helper method to check if a given node is referenced in any prohibitions. The default implementation loads all
     * prohibitions into memory and then searches through each one.
     *
     * @param name             The node to check for.
     * @param prohibitionStore The ProhibitionStore used to get the prohibitions.
     * @throws NodeReferencedInProhibitionException If the node is referenced in a prohibition.
     * @throws PMBackendException                   If there is an error in the backend implementation.
     */
    default void checkIfNodeInProhibition(String name, ProhibitionsStore prohibitionStore)
    throws NodeReferencedInProhibitionException, PMBackendException {
        Map<String, List<Prohibition>> allProhibitions = prohibitionStore.getAll();
        for (List<Prohibition> subjPros : allProhibitions.values()) {
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
     * @param obligationsStore The ObligationStore used to get the obligations.
     * @throws NodeReferencedInObligationException If the node is referenced in an obligation.
     * @throws PMBackendException                  If there is an error in the backend implementation.
     */
    default void checkIfNodeInObligation(String name, ObligationsStore obligationsStore)
    throws NodeReferencedInObligationException, PMBackendException {
        List<Obligation> obligations = obligationsStore.getAll();
        for (Obligation obligation : obligations) {
            // if the node is the author of the obligation or referenced in any rules throw an exception
            if (obligation.getAuthor().getUser().equals(name) || nodeInObligation(name, obligation)) {
                throw new NodeReferencedInObligationException(name, obligation.getName());
            }
        }
    }


    /**
     * Check if both nodes exist and make a valid assignment. If the assignment already exists an error does not
     * occur but
     * return false to indicate to the caller that execution should not proceed.
     *
     * @param child  The child node.
     * @param parent The parent node.
     * @return True if the execution should proceed, false otherwise.
     * @throws NodeDoesNotExistException  If either the child or parent does not exist.
     * @throws PMBackendException         If there is an error in the backend implementation.
     * @throws InvalidAssignmentException If the types of the child and parent node do not make a valid assignment.
     */
    default boolean checkAssignInput(String child, String parent)
    throws NodeDoesNotExistException, PMBackendException, InvalidAssignmentException, AssignmentCausesLoopException {
        // ignore if assignment already exists
        if (getParents(child).contains(parent)) {
            return false;
        }

        // getting both nodes will check if they exist
        Node childNode = getNode(child);
        Node parentNode = getNode(parent);

        // check node types make a valid assignment relation
        Assignment.checkAssignment(childNode.getType(), parentNode.getType());

        // check the assignment won't create a loop
        checkAssignmentDoesNotCreateLoop(child, parent);

        return true;
    }

    /**
     * Check if both nodes exist. If the assignment does not exist an error does not occur but return false to indicate
     * to the caller that execution should not proceed.
     *
     * @param child  The child node.
     * @param parent The parent node.
     * @return True if the execution should proceed, false otherwise.
     * @throws NodeDoesNotExistException If either the child or parent does not exist.
     * @throws PMBackendException        If there is an error in the backend implementation.
     */
    default boolean checkDeassignInput(String child, String parent)
    throws PMBackendException, NodeDoesNotExistException, DisconnectedNodeException {
        if (!nodeExists(child)) {
            throw new NodeDoesNotExistException(child);
        } else if (!nodeExists(parent)) {
            throw new NodeDoesNotExistException(parent);
        }

        List<String> parents = getParents(child);
        if (!parents.contains(parent)) {
            return false;
        }

        if (parents.size() == 1) {
            throw new DisconnectedNodeException(child, parent);
        }

        return true;
    }

    /**
     * Check that the provided node exists.
     *
     * @param node The node to check.
     * @throws PMBackendException        If there is an error in the backend implementation.
     * @throws NodeDoesNotExistException If the node does not exist.
     */
    default void checkGetParentsInput(String node) throws PMBackendException, NodeDoesNotExistException {
        if (!nodeExists(node)) {
            throw new NodeDoesNotExistException(node);
        }
    }

    /**
     * Check that the provided node exists.
     *
     * @param node The node to check.
     * @throws PMBackendException        If there is an error in the backend implementation.
     * @throws NodeDoesNotExistException If the node does not exist.
     */
    default void checkGetChildrenInput(String node) throws PMBackendException, NodeDoesNotExistException {
        if (!nodeExists(node)) {
            throw new NodeDoesNotExistException(node);
        }
    }

    /**
     * Check if the user attribute and target nodes exist and make up a valid association and that the given access
     * rights are allowed.
     *
     * @param ua           The user attribute.
     * @param target       The target node.
     * @param accessRights The access rights.
     * @throws NodeDoesNotExistException   If the user attribute or target node do not exist.
     * @throws PMBackendException          If there is an error in the backend implementation.
     * @throws UnknownAccessRightException If a given access right has not been previously defined.
     * @throws InvalidAssociationException If the provided user attribute and target node do not make a valid
     * association.
     */
    default void checkAssociateInput(String ua, String target, AccessRightSet accessRights)
    throws NodeDoesNotExistException, PMBackendException, UnknownAccessRightException, InvalidAssociationException {
        Node uaNode = getNode(ua);
        Node targetNode = getNode(target);

        // check the access rights are valid
        checkAccessRightsValid(getResourceAccessRights(), accessRights);

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
     * @throws NodeDoesNotExistException If either the user attribute or target does not exist.
     * @throws PMBackendException        If there is an error in the backend implementation.
     */
    default boolean checkDissociateInput(String ua, String target)
    throws PMBackendException, NodeDoesNotExistException {
        if (!nodeExists(ua)) {
            throw new NodeDoesNotExistException(ua);
        } else if (!nodeExists(target)) {
            throw new NodeDoesNotExistException(target);
        }

        if (!getAssociationsWithSource(ua).contains(new Association(ua, target))) {
            return false;
        }

        return true;
    }

    /**
     * Check the source node to get associations for.
     *
     * @param ua The source node.
     * @throws PMBackendException        If there is an error in the backend implementation.
     * @throws NodeDoesNotExistException If the source node does not exist.
     */
    default void checkGetAssociationsWithSourceInput(String ua) throws PMBackendException, NodeDoesNotExistException {
        if (!nodeExists(ua)) {
            throw new NodeDoesNotExistException(ua);
        }
    }

    /**
     * Check the target node to get associations for.
     *
     * @param target The target node.
     * @throws PMBackendException        If there is an error in the backend implementation.
     * @throws NodeDoesNotExistException If the target node does not exist.
     */
    default void checkGetAssociationsWithTargetInput(String target) throws PMBackendException, NodeDoesNotExistException {
        if (!nodeExists(target)) {
            throw new NodeDoesNotExistException(target);
        }
    }

    static void checkAccessRightsValid(AccessRightSet resourceAccessRights, AccessRightSet accessRightSet)
    throws UnknownAccessRightException, PMBackendException {
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
            if (containerCondition.name().equals(name)) {
                return true;
            }
        }

        return false;
    }

    private static boolean nodeInObligation(String name, Obligation obligation) {
        for (Rule rule : obligation.getRules()) {
            if (nodeInEvent(name, rule.getEventPattern())) {
                return true;
            }
        }

        return false;
    }

    private static boolean nodeInEvent(String name, EventPattern event) {
        // check subject
        EventSubject subject = event.getSubject();
        if ((subject.getType() == EventSubject.Type.ANY_USER_WITH_ATTRIBUTE && subject.anyUserWithAttribute().equals(name))
                || (subject.getType() == EventSubject.Type.USERS && subject.users().contains(name))) {
            return true;
        }

        // check the target
        Target target = event.getTarget();
        return (target.getType() == Target.Type.ANY_CONTAINED_IN && target.anyContainedIn().equals(name))
                || (target.getType() == Target.Type.ANY_OF_SET && target.anyOfSet().contains(name))
                || (target.getType() == Target.Type.POLICY_ELEMENT && target.policyElement().equals(name));
    }
}
