package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.epp.events.*;
import gov.nist.csd.pm.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pdp.audit.Auditor;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pap.policies.SuperPolicy;
import gov.nist.csd.pm.pdp.services.guard.GraphGuard;
import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.REP_PROPERTY;

/**
 * GraphService provides methods to maintain an NGAC graph, while also ensuring any user interacting with the graph,
 * has the correct permissions to do so.
 */
public class GraphService extends Service implements Graph {

    private Graph graph;
    private GraphGuard guard;

    public GraphService(UserContext userCtx, FunctionalEntity pap, EPP epp, Decider decider, Auditor auditor) throws PMException {
        super(userCtx, pap, epp, decider, auditor);
        this.graph = pap.getGraph();
        this.guard = new GraphGuard(pap, decider);
    }

    @Override
    public Node createPolicyClass(String name, Map<String, String> properties) throws PMException {
        // check user has permission to create a policy class
        guard.checkCreatePolicyClass(userCtx);

        // create and return the new policy class
        return graph.createPolicyClass(name, properties);
    }

    /**
     * Create a node and assign it to the provided parent(s). The name and type must not be null.
     * This method is needed because if a node is created without an initial assignment, it will be impossible
     * to assign the node in the future since no user will have permissions on a node not connected to the graph.
     * In this method we can check the user has the permission to assign to the given parent node and ignore if
     * the user can assign the newly created node.
     *
     * When creating a policy class, a parent node is not required.  The user must have the "create policy class" permission
     * on the super object.  By default the super user will always have this permission. A configuration will be created
     * that grants the user permissions on the policy class' default UA and OA, which will allow the user to delegate admin
     * permissions to other users.
     *
     * @param name the name of the node to create.
     * @param type the type of the node.
     * @param properties properties to add to the node.
     * @param initialParent the name of the node to assign the new node to.
     * @param additionalParents 0 or more node names to assign the new node to.
     * @return the new node.
     */
    @Override
    public Node createNode(String name, NodeType type, Map<String, String> properties, String initialParent, String ... additionalParents) throws PMException {
        // check that the user can create the node in each of the parents
        guard.checkCreateNode(userCtx, type, initialParent, additionalParents);

        //create the node
        Node node = graph.createNode(name, type, properties, initialParent, additionalParents);

        // process the event
        getEPP().processEvent(new CreateNodeEvent(userCtx, node, initialParent, additionalParents));

        return node;
    }

    /**
     * Update the node in the database and in the in-memory graph.  If the name is null or empty it is ignored, likewise
     * for properties.
     *
     * @param name the name to give the node.
     * @param properties the properties of the node.
     * @throws PMException if the given node does not exist in the graph.
     * @throws PMAuthorizationException if the user is not authorized to update the node.
     */
    public void updateNode(String name, Map<String, String> properties) throws PMException {
        // check that the user can update the node
        guard.checkUpdateNode(userCtx, name);

        //update node in the PAP
        graph.updateNode(name, properties);
    }

    /**
     * Delete the node with the given name from the PAP.  First check that the current user
     * has the correct permissions to do so. Do this by checking that the user has the permission to deassign from each
     * of the node's parents, and that the user can delete the node.
     * @param name the name of the node to delete.
     * @throws PMException if there is an error accessing the graph through the PAP.
     * @throws PMAuthorizationException if the user is not authorized to delete the node.
     */
    public void deleteNode(String name) throws PMException {
        Node node = graph.getNode(name);

        // check that the user can delete the node
        guard.checkDeleteNode(userCtx, node.getType(), name);

        // check that the node does not have any children
        if (graph.getChildren(name).size() != 0) {
            throw new PMException("cannot delete " + name + ", nodes are still assigned to it");
        }

        // if it's a PC, delete the rep
        if (node.getType().equals(PC)) {
            if (node.getProperties().containsKey(REP_PROPERTY)) {
                graph.deleteNode(node.getProperties().get(REP_PROPERTY));
            }
        }

        // get the
        Set<String> parents = graph.getParents(name);

        // delete the node
        graph.deleteNode(name);

        // process the delete node event
        getEPP().processEvent(new DeleteNodeEvent(userCtx, node, parents));
/*        // process the delete event
        Set<String> parents = graph.getParents(name);
        for(String parent : parents) {
            Node parentNode = graph.getNode(parent);

            getEPP().processEvent(new DeassignEvent(userCtx, node, parentNode));
            getEPP().processEvent(new DeassignFromEvent(userCtx, parentNode, node));
        }

        // delete the node
        graph.deleteNode(name);*/
    }

    /**
     * Check that a node with the given name exists. This method will return false if the user does not have access to
     * the node.
     * @param name the name of the node to check for.
     * @return true if a node with the given name exists, false otherwise.
     * @throws PMException if there is an error checking if the node exists in the graph through the PAP.
     */
    public boolean exists(String name) throws PMException {
        boolean exists = graph.exists(name);
        if (!exists) {
            return false;
        }

        return guard.checkExists(userCtx, name);
    }

    /**
     * Retrieve the list of all nodes in the graph.  Go to the database to do this, since it is more likely to have
     * all of the node information.
     * @return the set of all nodes in the graph.
     * @throws PMException if there is an error getting the nodes from the PAP.
     */
    public Set<Node> getNodes() throws PMException {
        Set<Node> nodes = new HashSet<>(graph.getNodes());
        guard.filterNodes(userCtx, nodes);
        return new HashSet<>(nodes);
    }

    /**
     * Get the set of policy classes. This can be performed by the in-memory graph.
     * @return the set of names for the policy classes in the graph.
     * @throws PMException if there is an error getting the policy classes from the PAP.
     */
    public Set<String> getPolicyClasses() throws PMException {
        return graph.getPolicyClasses();
    }

    public String getPolicyClassDefault(String pc, NodeType type) {
        return pc + "_default_" + type.toString();
    }

    /**
     * Get the children of the node from the graph.  Get the children from the database to ensure all node information
     * is present.  Before returning the set of nodes, filter out any nodes that the user has no permissions on.
     *
     * @param name the name of the node to get the children of.
     * @return a set of Node objects, representing the children of the target node.
     * @throws PMException if the target node does not exist.
     * @throws PMException if there is an error getting the children from the PAP.

     */
    public Set<String> getChildren(String name) throws PMException {
        if(!exists(name)) {
            throw new PMException(String.format("node %s could not be found", name));
        }

        Set<String> children = graph.getChildren(name);
        guard.filter(userCtx, children);
        return children;
    }

    /**
     * Get the parents of the node from the graph.  Before returning the set of nodes, filter out any nodes that the user
     * has no permissions on.
     * @param name the name of the node to get the parents of.
     * @return a set of Node objects, representing the parents of the target node.
     * @throws PMException if the target node does not exist.
     * @throws PMException if there is an error getting the parents from the PAP.
     */
    public Set<String> getParents(String name) throws PMException {
        if(!exists(name)) {
            throw new PMException(String.format("node %s could not be found", name));
        }

        Set<String> parents = graph.getParents(name);
        guard.filter(userCtx, parents);
        return parents;
    }

    /**
     * Create the assignment in both the db and in-memory graphs. First check that the user is allowed to assign the child,
     * and allowed to assign something to the parent.
     * @param child the name of the child node.
     * @param parent the name of the parent node.
     * @throws IllegalArgumentException if the child name is null.
     * @throws IllegalArgumentException if the parent name is null.
     * @throws PMException if the child or parent node does not exist.
     * @throws PMException if the assignment is invalid.
     * @throws PMAuthorizationException if the current user does not have permission to create the assignment.
     */
    public void assign(String child, String parent) throws PMException {
        // check that the user can make the assignment
        guard.checkAssign(userCtx, child, parent);

        // assign in the PAP
        graph.assign(child, parent);

        // process the assignment as to events - assign and assign to
        Node childNode = getNode(child);
        Node parentNode = getNode(parent);
        getEPP().processEvent(new AssignEvent(userCtx, childNode, parentNode));
        getEPP().processEvent(new AssignToEvent(userCtx, parentNode, childNode));
    }

    /**
     * Create the assignment in both the db and in-memory graphs. First check that the user is allowed to assign the child,
     * and allowed to assign something to the parent.
     * @param child the name of the child of the assignment to delete.
     * @param parent the name of the parent of the assignment to delete.
     * @throws IllegalArgumentException if the child name is null.
     * @throws IllegalArgumentException if the parent name is null.
     * @throws PMException if the child or parent node does not exist.
     * @throws PMAuthorizationException if the current user does not have permission to delete the assignment.
     */
    public void deassign(String child, String parent) throws PMException {
        // check the user can delete the assignment
        guard.checkDeassign(userCtx, child, parent);

        //delete assignment in PAP
        graph.deassign(child, parent);

        // process the deassign as two events - deassign and deassign from
        Node parentNode = getNode(parent);
        Node childNode = getNode(child);
        getEPP().processEvent(new DeassignEvent(userCtx, childNode, parentNode));
        getEPP().processEvent(new DeassignFromEvent(userCtx, parentNode, childNode));
    }

    @Override
    public boolean isAssigned(String child, String parent) throws PMException {
        Node parentNode = getNode(parent);
        Node childNode = getNode(child);

        return graph.isAssigned(childNode.getName(), parentNode.getName());

    }

    /**
     * Create an association between the user attribute and the target node with the given operations. First, check that
     * the user has the permissions to associate the user attribute and target nodes.  If an association already exists
     * between the two nodes than update the existing association with the provided operations (overwrite).
     *
     * @param ua the name of the user attribute.
     * @param target the name of the target node.
     * @param operations a Set of operations to add to the Association.
     * @throws IllegalArgumentException if the user attribute is null.
     * @throws IllegalArgumentException if the target is null.
     * @throws PMException if the user attribute node does not exist.
     * @throws PMException if the target node does not exist.
     * @throws PMException if the association is invalid.
     * @throws PMAuthorizationException if the current user does not have permission to create the association.
     */
    public void associate(String ua, String target, OperationSet operations) throws PMException {
        // check that this user can create the association
        guard.checkAssociate(userCtx, ua, target);

        //create association in PAP
        graph.associate(ua, target, operations);

        getEPP().processEvent(new AssociationEvent(userCtx, graph.getNode(ua), graph.getNode(target)));
    }

    /**
     * Delete the association between the user attribute and the target node.  First, check that the user has the
     * permission to delete the association.
     *
     * @param ua The name of the user attribute.
     * @param target The name of the target node.
     * @throws IllegalArgumentException If the user attribute is null.
     * @throws IllegalArgumentException If the target node is null.
     * @throws PMException If the user attribute node does not exist.
     * @throws PMException If the target node does not exist.
     * @throws PMAuthorizationException If the current user does not have permission to delete the association.
     */
    public void dissociate(String ua, String target) throws PMException {
        // check that the user can delete the association
        guard.checkDissociate(userCtx, ua, target);

        //create association in PAP
        graph.dissociate(ua, target);

        getEPP().processEvent(new DeleteAssociationEvent(userCtx, graph.getNode(ua), graph.getNode(target)));
    }

    /**
     * Get the associations the given node is the source node of. First, check if the user is allowed to retrieve this
     * information.
     *
     * @param source The name of the source node.
     * @return a map of the target and operations for each association the given node is the source of.
     * @throws PMException If the given node does not exist.
     * @throws PMAuthorizationException If the current user does not have permission to get hte node's associations.
     */
    public Map<String, OperationSet> getSourceAssociations(String source) throws PMException {
        // check that this user can get the associations of the source node
        guard.checkGetAssociations(userCtx, source);

        // get the associations for the source node
        Map<String, OperationSet> sourceAssociations = graph.getSourceAssociations(source);

        // filter out any associations in which the user does not have access to the target attribute
        guard.filter(userCtx, sourceAssociations);

        return sourceAssociations;
    }

    /**
     * Get the associations the given node is the target node of. First, check if the user is allowed to retrieve this
     * information.
     *
     * @param target The name of the source node.
     * @return a map of the source name and operations for each association the given node is the target of.
     * @throws PMException If the given node does not exist.
     * @throws PMAuthorizationException If the current user does not have permission to get hte node's associations.
     */
    public Map<String, OperationSet> getTargetAssociations(String target) throws PMException {
        // check that this user can get the associations of the target node
        guard.checkGetAssociations(userCtx, target);

        // get the associations for the target node
        Map<String, OperationSet> targetAssociations = graph.getTargetAssociations(target);

        // filter out any associations in which the user does not have access to the source attribute
        guard.filter(userCtx, targetAssociations);

        return targetAssociations;
    }

    @Override
    public String toJson() throws PMException {
        // check that the user can serialize to json
        guard.checkToJson(userCtx);

        return graph.toJson();
    }

    @Override
    public void fromJson(String s) throws PMException {
        // check that the user can deserialize a json string to the graph
        guard.checkFromJson(userCtx);

        graph.fromJson(s);
    }

    /**
     * Search the NGAC graph for nodes that match the given parameters. A node must match all non null parameters to be
     * returned in the search.
     *
     * @param type The type of the nodes to search for.
     * @param properties The properties of the nodes to search for.
     * @return a Response with the nodes that match the given search criteria.
     * @throws PMException If the PAP encounters an error with the graph.
     * @throws PMAuthorizationException If the current user does not have permission to get hte node's associations.
     */
    @Override
    public Set<Node> search(NodeType type, Map<String, String> properties) throws PMException {
        Set<Node> search = graph.search(type, properties);
        //System.out.println(search);
        guard.filterNodes(userCtx, search);
        return search;
    }

    /**
     * Retrieve the node from the graph with the given name.
     *
     * @param name the name of the node to get.
     * @return the Node retrieved from the graph with the given name.
     * @throws PMException If the node does not exist in the graph.
     * @throws PMAuthorizationException if the current user is not authorized to access this node.
     */
    public Node getNode(String name) throws PMException {
        // get node
        Node node = graph.getNode(name);

        // check user has permissions on the node
        try {
            guard.checkExists(userCtx, name);
        } catch (PMException e) {
            // if no permissions, the user shouldn't know it exists
            throw new PMException(String.format("node %s could not be found", name));
        }

        return node;
    }

    @Override
    public Node getNode(NodeType type, Map<String, String> properties) throws PMException {
        Node node = graph.getNode(type, properties);

        // check user has permissions on the node
        try {
            guard.checkExists(userCtx, node.getName());
        } catch (PMException e) {
            // if no permissions, the user shouldn't know it exists
            throw new PMException(String.format("node (%s, %s) could not be found", type, properties.toString()));
        }

        return node;
    }

    /**
     * Deletes all nodes in the graph
     *
     * @throws PMException if something goes wrong in the deletion process
     */
    public void reset(UserContext userCtx) throws PMException {
        guard.checkReset(userCtx);

        Collection<Node> nodes = graph.getNodes();
        Set<String> names = new HashSet<>();
        Set<String> prohibitions_name = new HashSet<>();
        for (Node node: nodes) {
            names.add(node.getName());
            if (node.getType() == UA || node.getType() == OA) {
                graph.getTargetAssociations(node.getName()).keySet().forEach(el -> {
                    try {
                        graph.dissociate(el, node.getName());
                    } catch (PMException pmException) {
                        pmException.printStackTrace();
                    }
                });
                if (node.getType() == UA) {
                    graph.getSourceAssociations(node.getName()).keySet().forEach(el -> {
                        try {
                            graph.dissociate(node.getName(), el);
                        } catch (PMException pmException) {
                            pmException.printStackTrace();
                        }
                    });
                }
            }
            graph.getChildren(node.getName()).forEach(el -> {
                try {
                    if (graph.isAssigned(el, node.getName())) {
                        graph.deassign(el, node.getName());
                    }
                } catch (PMException pmException) {
                    pmException.printStackTrace();
                }
            });
            graph.getParents(node.getName()).forEach(el -> {
                try {
                    if (graph.isAssigned(node.getName(), el)) {
                        graph.deassign(node.getName(), el);
                    }
                } catch (PMException pmException) {
                    pmException.printStackTrace();
                }
            });
            getProhibitionsAdmin().getProhibitionsFor(node.getName()).forEach( el -> {
                prohibitions_name.add(el.getName());
            });
        }
        for (String prohibition: prohibitions_name) {
            getProhibitionsAdmin().delete(prohibition);
        }

        for (String name : names) {
            graph.deleteNode(name);
        }
        //setup Super policy in GraphAdmin + copy graph and graph copy
        superPolicy = new SuperPolicy();
        superPolicy.configure(graph);
    }
}
