package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.List;
import java.util.Map;

public interface PolicyWriter {

    /**
     * Set the resource access rights recognized in this policy
     * @param accessRightSet the operations to set as the resource access rights
     */
    void setResourceAccessRights(AccessRightSet accessRightSet) throws PMException;

    /**
     * Create a policy class in the graph.
     *
     * @param name the name of the policy class.
     * @return the node representing the new policy class.
     * @throws PMException if there is an error creating the policy class node in the graph.
     */
    String createPolicyClass(String name, Map<String, String> properties) throws PMException;
    String createPolicyClass(String name) throws PMException;

    /**
     * Create a new user attribute. User attributes do not have to be connected to the graph, therefore
     * the parents argument is optional.
     * @param name the name of the suer attribute
     * @param parent is the parent to initially assign the new node to.  A user attribute needs to be connected
     *                      to the graph when created.
     * @param parents is a list of 0 or more additional parents to assign the new node to.
     * @return the user attribute node
     * @throws PMException
     */
    String createUserAttribute(String name, Map<String, String> properties, String parent, String ... parents) throws PMException;
    String createUserAttribute(String name, String parent, String ... parents) throws PMException;

    /**
     * Create a new object attribute with the given name, and assign it to the given parents.
     * Note: Node names must be unique.
     *
     * @param name the unique name of the node.
     * @param parent is the parent to initially assign the new node to.  An object attribute needs to be connected
     *                      to the graph when created.
     * @param parents is a list of 0 or more additional parents to assign the new node to.
     * @return the object attribute node
     * @throws PMException if there is an error creating the node in the graph.
     */
    String createObjectAttribute(String name, Map<String, String> properties, String parent, String ... parents) throws PMException;
    String createObjectAttribute(String name, String parent, String ... parents) throws PMException;

    /**
     * Create a new object with the given name, and assign it to the given parents.
     * Note: Node names must be unique.
     *
     * @param name the unique name of the node.
     * @param parent is the parent to initially assign the new node to.  An object needs to be connected to the
     *                      graph when created.
     * @param parents is a list of 0 or more additional parents to assign the new node to.
     * @return the object node
     * @throws PMException if there is an error creating the node in the graph.
     */
    String createObject(String name, Map<String, String> properties, String parent, String ... parents) throws PMException;
    String createObject(String name, String parent, String ... parents) throws PMException;

    /**
     * Create a new user with the given name, and assign it to the given parents.
     * Note: Node names must be unique.
     *
     * @param name the unique name of the node.
     * @param parent is the parent to initially assign the new node to.  A user needs to be connected to the
     *                      graph when created.
     * @param parents is a list of 0 or more additional parents to assign the new node to.
     * @return the user node
     * @throws PMException if there is an error creating the node in the graph.
     */
    String createUser(String name, Map<String, String> properties, String parent, String ... parents) throws PMException;
    String createUser(String name, String parent, String ... parents) throws PMException;

    /**
     * Update the properties of the node with the given name. The given properties overwrite any existing properties.
     *
     * @param name the name of the node to update.
     * @param properties the properties to give the node.
     * @throws PMException if there is an error updating the node in the graph.
     */
    void setNodeProperties(String name, Map<String, String> properties) throws PMException;

    /**
     * Delete the node with the given name from the graph. The node must not have any other nodes assigned to it.
     * If the node does not exist, no exception will be thrown as this is the desired state.
     *
     * @param name the name of the node to delete.
     * @throws PMException if the node being deleted still has other nodes assigned to it.
     * @throws PMException if there is an error deleting the node from the graph.
     */
    void deleteNode(String name) throws PMException;

    /**
     * Assign the child node to the parent node. The child and parent nodes must both already exist in the graph,
     * and the types must make a valid assignment. An example of a valid assignment is assigning o1, an object, to oa1,
     * an object attribute.  o1 is the child (objects can never be the parent in an assignment), and oa1 is the parent.
     * If the child is already assigned to the parent, no exception will be thrown as this is the desired state.
     *
     * @param child  the name of the child node.
     * @param parent the name of the parent node.
     * @throws PMException if there is an error assigning the two nodes.
     */
    void assign(String child, String parent) throws PMException;

    /**
     * Remove the assignment between the child and parent nodes. An exception will be thrown if either node
     * does not exist. If the assignment doesn't exist, no exception will be thrown as this is the desired state.
     *
     * @param child  the name of the child node.
     * @param parent the name of the parent node.
     * @throws PMException if there is an error deassigning the two nodes.
     */
    void deassign(String child, String parent) throws PMException;

    /**
     * Assign the children of the given attribute to the target attribute of the same type.
     * @param children The children to assign to the target.
     * @param target The target attribute to assign the children to.
     * @throws PMException
     */
    void assignAll(List<String> children, String target) throws PMException;

    /**
     * Deassign the children of the given attribute from the target attribute of the same type.
     * @param children The children to deassign from the given target.
     * @param target The target attribute to deassign the children from.
     * @throws PMException
     */
    void deassignAll(List<String> children, String target) throws PMException;

    /**
     * Deassign all nodes assigned to the given node and delete the given node.
     * from the rest of the graph.
     * @param target The node to deassign all children from.
     * @throws PMException
     */
    void deassignAllFromAndDelete(String target) throws PMException;

    /**
     * Create an Association between the user attribute and the Target node with the provided access rights. If an association
     * already exists between these two nodes, overwrite the existing access rights with the ones provided.  Associations
     * can only begin at a user attribute but can point to either an object or user attribute
     *
     * @param ua the name of the user attribute.
     * @param target the name of the target attribute.
     * @param accessRights a set of access rights to add to the association.
     * @throws PMException if there is an error associating the two nodes.
     */
    void associate(String ua, String target, AccessRightSet accessRights) throws PMException;

    /**
     * Delete the Association between the user attribute and Target node.
     *
     * @param ua     the name of the user attribute.
     * @param target the name of the target attribute.
     * @throws PMException if there is an error dissociating the two nodes.
     */
    void dissociate(String ua, String target) throws PMException;

    /**
     * Create a new prohibition.
     *
     * @param label the identifier of this prohibition.
     * @param subject ths subject of the prohibition (user, user attribute, or process).
     * @param accessRightSet the access rights to be denied
     * @param intersection a boolean flag that determines if the intersection of the containers should be denied or not.
     * @param containerConditions the containers to deny the subject access to.
     * @throws PMException
     */
    void createProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet,
                boolean intersection, ContainerCondition... containerConditions) throws PMException;

    /**
     * Update an existing prohibition.
     *
     * @param label the identifier of this prohibition.
     * @param subject ths subject of the prohibition (user, user attribute, or process).
     * @param accessRightSet the access rights to be denied
     * @param intersection a boolean flag that determines if the intersection of the containers should be denied or not.
     * @param containerConditions the containers to deny the subject access to.
     * @throws PMException
     */
    void updateProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet,
                boolean intersection, ContainerCondition ... containerConditions) throws PMException;

    /**
     * Delete the prohibition, and remove it from the data structure.
     *
     * @param label The name of the prohibition to delete.
     * @throws PMException if there is an error deleting the prohibition.
     */
    void deleteProhibition(String label) throws PMException;

    /**
     * Create a new obligation.
     *
     * @param author the user/process that is creating the obligation.
     * @param label the label of the obligation.
     * @param rules the rules of the obligation.
     * @throws PMException
     */
    void createObligation(UserContext author, String label, Rule... rules) throws PMException;

    /**
     * Update the obligation with the given label.  If the label in the provided object is not null and different from
     * the label parameter, the label will also be updated.
     * @param author the user/process that created the obligation
     * @param label the label of the obligation to update
     * @param rules the updated obligation rules
     * @throws PMException
     */
    void updateObligation(UserContext author, String label, Rule... rules) throws PMException;

    /**
     * Delete the obligation with the given label.
     * @param label the label of the obligation to delete
     * @throws PMException
     */
    void deleteObligation(String label) throws PMException;
    void addPALFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException;
    void removePALFunction(String functionName) throws PMException;
    void addPALConstant(String constantName, Value constantValue) throws PMException;
    void removePALConstant(String constName) throws PMException;

}
