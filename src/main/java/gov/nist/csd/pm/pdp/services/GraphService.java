package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.epp.events.AssignEvent;
import gov.nist.csd.pm.epp.events.AssignToEvent;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.epp.events.DeassignEvent;
import gov.nist.csd.pm.epp.events.DeassignFromEvent;
import gov.nist.csd.pm.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.SuperGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.graph.model.relationships.Assignment;
import gov.nist.csd.pm.pip.graph.model.relationships.Association;

import java.util.*;

import static gov.nist.csd.pm.common.Operations.*;
import static gov.nist.csd.pm.pdp.decider.PReviewDecider.ALL_OPERATIONS;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.PC;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;
import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.*;

/**
 * GraphService provides methods to maintain an NGAC graph, while also ensuring any user interacting with the graph,
 * has the correct permissions to do so.
 */
public class GraphService extends Service {

    public GraphService(PAP pap, EPP epp) {
        super(pap, epp);
    }

    /**
     * Create a node and assign it to the node with the given ID. The name and type must not be null.
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
     * @param parentID the ID of the parent node if creating a non policy class node.
     * @param name the name of the node to create.
     * @param type the type of the node.
     * @param properties properties to add to the node.
     * @return the new node created with it's ID.
     * @throws IllegalArgumentException if the name is null or empty.
     * @throws IllegalArgumentException if the type is null.
     */
    public Node createNode(UserContext userCtx, long parentID, String name, NodeType type, Map<String, String> properties) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("a node cannot have a null or empty name");
        } else if (type == null) {
            throw new IllegalArgumentException("a node cannot have a null type");
        }

        // instantiate the properties map if it's null
        // if this node is a user, hash the password if present in the properties
        if(properties == null) {
            properties = new HashMap<>();
        }

        checkNamespace(name, type, properties);

        long id = new Random().nextLong();
        // create the node
        if(type.equals(PC)) {
            return createPolicyClass(userCtx, id, name, properties);
        } else {
            return createNonPolicyClass(userCtx, parentID, id, name, type, properties);
        }
    }

    private void checkNamespace(String name, NodeType type, Map<String, String> properties) throws PMException {
        // check that the intended namespace does not already have the node name
        String namespace = properties.get(NAMESPACE_PROPERTY);
        if(namespace == null) {
            namespace = DEFAULT_NAMESPACE;
        }

        // search the graph for any node with the same name, type, and namespace
        Set<Node> search = getGraphPAP().search(name, type.toString(),
                Node.toProperties(NAMESPACE_PROPERTY, namespace));
        if(!search.isEmpty()) {
            throw new PMException(String.format("a node with the name \"%s\" and type %s already exists in the namespace \"%s\"",
                    name, type, namespace));
        }
    }

    private Node createNonPolicyClass(UserContext userCtx, long parentID, long id, String name, NodeType type, Map<String, String> properties) throws PMException {
        //check that the parent node exists
        if(!exists(userCtx, parentID)) {
            throw new PMException(String.format("the specified parent node with ID %d does not exist", parentID));
        }

        //get the parent node to make the assignment
        Node parentNodeCtx = getNode(userCtx, parentID);

        // check that the user has the permission to assign to the parent node
        if (!hasPermissions(userCtx, parentID, ASSIGN_TO)) {
            // if the user cannot assign to the parent node, delete the newly created node
            throw new PMAuthorizationException(String.format("unauthorized permission \"%s\" on node with ID %d", ASSIGN_TO, parentID));
        }

        //create the node
        Node node = getGraphPAP().createNode(id, name, type, properties);

        // assign the node to the specified parent node
        getGraphPAP().assign(id, parentID);

        getEPP().processEvent(new AssignToEvent(parentNodeCtx, node), userCtx.getUserID(), userCtx.getProcessID());

        return node;
    }

    private Node createPolicyClass(UserContext userCtx, long id, String name, Map<String, String> properties) throws PMException {
        // check that the user can create a policy class
        if (!hasPermissions(userCtx, SuperGraph.getSuperO().getID(), CREATE_POLICY_CLASS)) {
            throw new PMAuthorizationException("unauthorized permissions to create a policy class");
        }

        // create the PC node
        Random rand = new Random();
        long defaultUA = rand.nextLong();
        long defaultOA = rand.nextLong();
        properties.putAll(Node.toProperties("default_ua", String.valueOf(defaultUA), "default_oa", String.valueOf(defaultOA)));
        Node pcNode = getPAP().getGraphPAP().createNode(rand.nextLong(), name, PC, properties);
        // create the PC UA node
        Node pcUANode = getPAP().getGraphPAP().createNode(defaultUA, name, UA, Node.toProperties(NAMESPACE_PROPERTY, name));
        // create the PC OA node
        Node pcOANode = getPAP().getGraphPAP().createNode(defaultOA, name, OA, Node.toProperties(NAMESPACE_PROPERTY, name));
        // assign PC UA to PC
        getPAP().getGraphPAP().assign(pcUANode.getID(), pcNode.getID());
        // assign PC OA to PC
        getPAP().getGraphPAP().assign(pcOANode.getID(), pcNode.getID());
        // assign super UA to PC
        getPAP().getGraphPAP().assign(SuperGraph.getSuperUA1().getID(), pcNode.getID());
        // associate Super UA and PC UA
        getPAP().getGraphPAP().associate(SuperGraph.getSuperUA1().getID(), pcUANode.getID(), new HashSet<>(Arrays.asList(ALL_OPERATIONS)));
        // associate Super UA and PC OA
        getPAP().getGraphPAP().associate(SuperGraph.getSuperUA1().getID(), pcOANode.getID(), new HashSet<>(Arrays.asList(ALL_OPERATIONS)));

        return pcNode;
    }

    public long getPolicyClassDefault(long pcID, NodeType type) throws PMException {
        Node pcNode = getPAP().getGraphPAP().getNode(pcID);
        String defaultProp = "";
        if(type.equals(OA)) {
            defaultProp = pcNode.getProperties().get("default_oa");
            if(defaultProp == null) {
                throw new PMException("policy class " + pcNode.getName() + " does not have a default object attribute");
            }

            return Long.parseLong(defaultProp);
        } else {
            defaultProp = pcNode.getProperties().get("default_ua");
            if(defaultProp == null) {
                throw new PMException("policy class " + pcNode.getName() + " does not have a default user attribute");
            }

            return Long.parseLong(defaultProp);
        }
    }

    /**
     * Update the node in the database and in the in-memory graph.  If the name is null or empty it is ignored, likewise
     * for properties.
     *
     * @param id the ID of the node to update.
     * @param name the name to give the node.
     * @param properties the properties of the node.
     * @throws IllegalArgumentException if the given node id is 0.
     * @throws PMException if the given node does not exist in the graph.
     * @throws PMAuthorizationException if the user is not authorized to update the node.
     */
    public void updateNode(UserContext userCtx, long id, String name, Map<String, String> properties) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(id == 0) {
            throw new IllegalArgumentException("no ID was provided when updating the node");
        } else if (!exists(userCtx, id)) {
            throw new PMException(String.format("node with ID %d could not be found", id));
        }

        // check that the user can update the node
        if(!hasPermissions(userCtx, id, UPDATE_NODE)) {
            throw new PMAuthorizationException(String.format("unauthorized permission %s on node with ID %d", UPDATE_NODE, id));
        }

        //update node in the PAP
        getGraphPAP().updateNode(id, name, properties);
    }

    /**
     * Delete the node with the given ID from the db and in-memory graphs.  First check that the current user
     * has the correct permissions to do so. Do this by checking that the user has the permission to deassign from each
     * of the node's parents, and that the user can delete the node.
     * @param nodeID the ID of the node to delete.
     * @throws PMException if there is an error accessing the graph through the PAP.
     * @throws PMAuthorizationException if the user is not authorized to delete the node.
     */
    public void deleteNode(UserContext userCtx, long nodeID) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        Node node = getGraphPAP().getNode(nodeID);

        // check the user can deassign the node
        if (!hasPermissions(userCtx, nodeID, DEASSIGN)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", nodeID, DEASSIGN));
        }

        // check that the user can deassign from the node's parents
        Set<Long> parents = getGraphPAP().getParents(nodeID);
        for(long parentID : parents) {
            if(!hasPermissions(userCtx, parentID, DEASSIGN_FROM)) {
                throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", parentID, DEASSIGN_FROM));
            }

            Node parentNode = getGraphPAP().getNode(parentID);

            getEPP().processEvent(new DeassignEvent(node, parentNode), userCtx.getUserID(), userCtx.getProcessID());
            getEPP().processEvent(new DeassignFromEvent(parentNode, node), userCtx.getUserID(), userCtx.getProcessID());
        }

        getGraphPAP().deleteNode(nodeID);
    }

    /**
     * Check that a node with the given ID exists.  Just checking the in-memory graph is faster.
     * @param nodeID the ID of the node to check for.
     * @return true if a node with the given ID exists, false otherwise.
     * @throws PMException if there is an error checking if the node exists in the graph through the PAP.
     */
    public boolean exists(UserContext userCtx, long nodeID) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(!hasPermissions(userCtx, nodeID, ANY_OPERATIONS)) {
            // return false if the user does not have access to it.
            return false;
        }

        return getGraphPAP().exists(nodeID);
    }

    /**
     * Retrieve the list of all nodes in the graph.  Go to the database to do this, since it is more likely to have
     * all of the node information.
     * @return the set of all nodes in the graph.
     * @throws PMException if there is an error getting the nodes from the PAP.
     */
    public Set<Node> getNodes(UserContext userCtx) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        Set<Node> nodes = new HashSet<>(getGraphPAP().getNodes());
        nodes.removeIf((node) -> {
            try {
                return !hasPermissions(userCtx, node.getID(), ANY_OPERATIONS);
            }
            catch (PMException e) {
                e.printStackTrace();
                return true;
            }
        });

        return new HashSet<>(nodes);
    }

    /**
     * Get the set of policy class IDs. This can be performed by the in-memory graph.
     * @return the set of IDs for the policy classes in the graph.
     * @throws PMException if there is an error getting the policy classes from the PAP.
     */
    public Set<Long> getPolicies(UserContext userCtx) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        return getGraphPAP().getPolicies();
    }

    /**
     * Get the children of the node from the graph.  Get the children from the database to ensure all node information
     * is present.  Before returning the set of nodes, filter out any nodes that the user has no permissions on.
     * @param nodeID the ID of the node to get the children of.
     * @return a set of Node objects, representing the children of the target node.
     * @throws PMException if the target node does not exist.
     * @throws PMException if there is an error getting the children from the PAP.

     */
    public Set<Node> getChildren(UserContext userCtx, long nodeID) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(!exists(userCtx, nodeID)) {
            throw new PMException(String.format("node with ID %d could not be found", nodeID));
        }

        Set<Long> children = getGraphPAP().getChildren(nodeID);
        children.removeIf((node) -> {
            try {
                return !hasPermissions(userCtx, node, ANY_OPERATIONS);
            }
            catch (PMException e) {
                e.printStackTrace();
                return true;
            }
        });

        Set<Node> retChildren = new HashSet<>();
        for(long childID : children) {
            retChildren.add(getNode(userCtx, childID));
        }
        return retChildren;
    }

    /**
     * Get the parents of the node from the graph.  Get the parents from the database to ensure all node information
     * is present.  Before returning the set of nodes, filter out any nodes that the user has no permissions on.
     * @param nodeID the ID of the node to get the parents of.
     * @return a set of Node objects, representing the parents of the target node.
     * @throws PMException if the target node does not exist.
     * @throws PMException if there is an error getting the parents from the PAP.
     */
    public Set<Node> getParents(UserContext userCtx, long nodeID) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(!exists(userCtx, nodeID)) {
            throw new PMException(String.format("node with ID %d could not be found", nodeID));
        }

        Set<Long> parents = getGraphPAP().getParents(nodeID);
        parents.removeIf((node) -> {
            try {
                return !hasPermissions(userCtx, node, ANY_OPERATIONS);
            }
            catch (PMException e) {
                e.printStackTrace();
                return true;
            }
        });

        Set<Node> retParents = new HashSet<>();
        for(long parentID : parents) {
            retParents.add(getNode(userCtx, parentID));
        }
        return retParents;
    }

    /**
     * Create the assignment in both the db and in-memory graphs. First check that the user is allowed to assign the child,
     * and allowed to assign something to the parent. Both child and parent contexts must include the ID and type of the node.
     * @param childID the ID of the child node.
     * @param parentID the ID of the parent node.
     * @throws IllegalArgumentException if the child ID is 0.
     * @throws IllegalArgumentException if the parent ID is 0.
     * @throws PMException if the child or parent node does not exist.
     * @throws PMException if the assignment is invalid.
     * @throws PMAuthorizationException if the current user does not have permission to create the assignment.
     */
    public void assign(UserContext userCtx, long childID, long parentID) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        // check that the nodes are not null
        if(childID == 0) {
            throw new IllegalArgumentException("the child node ID cannot be 0 when creating an assignment");
        } else if(parentID == 0) {
            throw new IllegalArgumentException("the parent node ID cannot be 0 when creating an assignment");
        } else if(!exists(userCtx, childID)) {
            throw new PMException(String.format("child node with ID %d does not exist", childID));
        } else if(!exists(userCtx, parentID)) {
            throw new PMException(String.format("parent node with ID %d does not exist", parentID));
        }

        //check the user can assign the child
        if(!hasPermissions(userCtx, childID, ASSIGN)) {
            throw new PMAuthorizationException(String.format("unauthorized permission %s on node with ID %d", ASSIGN, childID));
        }

        //check if the assignment is valid
        Node child = getNode(userCtx, childID);
        Node parent = getNode(userCtx, parentID);
        Assignment.checkAssignment(child.getType(), parent.getType());

        // check that the user can assign to the parent node
        if (!hasPermissions(userCtx, parentID, ASSIGN_TO)) {
            throw new PMAuthorizationException(String.format("unauthorized permission %s on node with ID %d", ASSIGN_TO, parentID));
        }

        // assign in the PAP
        getGraphPAP().assign(childID, parentID);

        getEPP().processEvent(new AssignEvent(child, parent), userCtx.getUserID(), userCtx.getProcessID());
        getEPP().processEvent(new AssignToEvent(parent, child), userCtx.getUserID(), userCtx.getProcessID());
    }

    /**
     * Create the assignment in both the db and in-memory graphs. First check that the user is allowed to assign the child,
     * and allowed to assign something to the parent.
     * @param childID the ID of the child of the assignment to delete.
     * @param parentID the ID of the parent of the assignment to delete.
     * @throws IllegalArgumentException if the child ID is 0.
     * @throws IllegalArgumentException if the parent ID is 0.
     * @throws PMException if the child or parent node does not exist.
     * @throws PMAuthorizationException if the current user does not have permission to delete the assignment.
     */
    public void deassign(UserContext userCtx, long childID, long parentID) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        // check that the parameters are correct
        if(childID == 0) {
            throw new IllegalArgumentException("the child node ID cannot be 0 when deassigning");
        } else if(parentID == 0) {
            throw new IllegalArgumentException("the parent node ID cannot be 0 when deassigning");
        } else if(!exists(userCtx, childID)) {
            throw new PMException(String.format("child node with ID %d could not be found when deassigning", childID));
        } else if(!exists(userCtx, parentID)) {
            throw new PMException(String.format("parent node with ID %d could not be found when deassigning", parentID));
        }

        //check the user can deassign the child
        if(!hasPermissions(userCtx, childID, DEASSIGN)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", childID, DEASSIGN));
        }

        //check that the user can deassign from the parent
        if (!hasPermissions(userCtx, parentID, DEASSIGN_FROM)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", parentID, DEASSIGN_FROM));
        }

        //delete assignment in PAP
        getGraphPAP().deassign(childID, parentID);

        Node parentNode = getNode(userCtx, parentID);
        Node childNode = getNode(userCtx, childID);

        getEPP().processEvent(new DeassignEvent(childNode, parentNode), userCtx.getUserID(), userCtx.getProcessID());
        getEPP().processEvent(new DeassignFromEvent(parentNode, childNode), userCtx.getUserID(), userCtx.getProcessID());
    }

    /**
     * Create an association between the user attribute and the target node with the given operations. First, check that
     * the user has the permissions to associate the user attribute and target nodes.  If an association already exists
     * between the two nodes than update the existing association with the provided operations (overwrite).
     * @param uaID the ID of the user attribute.
     * @param targetID the ID of the target node.
     * @param operations a Set of operations to add to the Association.
     * @throws IllegalArgumentException if the user attribute ID is 0.
     * @throws IllegalArgumentException if the target node ID is 0.
     * @throws PMException if the user attribute node does not exist.
     * @throws PMException if the target node does not exist.
     * @throws PMException if the association is invalid.
     * @throws PMAuthorizationException if the current user does not have permission to create the association.
     */
    public void associate(UserContext userCtx, long uaID, long targetID, Set<String> operations) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(uaID == 0) {
            throw new IllegalArgumentException("the user attribute ID cannot be 0 when creating an association");
        } else if(targetID == 0) {
            throw new IllegalArgumentException("the target node ID cannot be 0 when creating an association");
        } else if(!exists(userCtx, uaID)) {
            throw new PMException(String.format("node with ID %d could not be found when creating an association", uaID));
        } else if(!exists(userCtx, targetID)) {
            throw new PMException(String.format("node with ID %d could not be found when creating an association", targetID));
        }

        Node sourceNode = getNode(userCtx, uaID);
        Node targetNode = getNode(userCtx, targetID);

        Association.checkAssociation(sourceNode.getType(), targetNode.getType());

        //check the user can associate the source and target nodes
        if(!hasPermissions(userCtx, uaID, ASSOCIATE)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", sourceNode.getName(), ASSOCIATE));
        }
        if (!hasPermissions(userCtx, targetID, ASSOCIATE)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", targetNode.getName(), ASSOCIATE));
        }

        //create association in PAP
        getGraphPAP().associate(uaID, targetID, operations);
    }

    /**
     * Delete the association between the user attribute and the target node.  First, check that the user has the
     * permission to delete the association.
     * @param uaID The ID of the user attribute.
     * @param targetID The ID of the target node.
     * @throws IllegalArgumentException If the user attribute ID is 0.
     * @throws IllegalArgumentException If the target node ID is 0.
     * @throws PMException If the user attribute node does not exist.
     * @throws PMException If the target node does not exist.
     * @throws PMAuthorizationException If the current user does not have permission to delete the association.
     */
    public void dissociate(UserContext userCtx, long uaID, long targetID) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(uaID == 0) {
            throw new IllegalArgumentException("the user attribute ID cannot be 0 when creating an association");
        } else if(targetID == 0) {
            throw new IllegalArgumentException("the target node ID cannot be 0 when creating an association");
        } else if(!exists(userCtx, uaID)) {
            throw new PMException(String.format("node with ID %d could not be found when creating an association", uaID));
        } else if(!exists(userCtx, targetID)) {
            throw new PMException(String.format("node with ID %d could not be found when creating an association", targetID));
        }

        //check the user can associate the source and target nodes
        if(!hasPermissions(userCtx, uaID, DISASSOCIATE)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", uaID, DISASSOCIATE));
        }
        if (!hasPermissions(userCtx, targetID, DISASSOCIATE)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", targetID, DISASSOCIATE));
        }

        //create association in PAP
        getGraphPAP().dissociate(uaID, targetID);
    }

    /**
     * Get the associations the given node is the source node of. First, check if the user is allowed to retrieve this
     * information.
     * @param sourceID The ID of the source node.
     * @return a map of the target ID and operations for each association the given node is the source of.
     * @throws PMException If the given node does not exist.
     * @throws PMAuthorizationException If the current user does not have permission to get hte node's associations.
     */
    public Map<Long, Set<String>> getSourceAssociations(UserContext userCtx, long sourceID) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(!exists(userCtx, sourceID)) {
            throw new PMException(String.format("node with ID %d could not be found", sourceID));
        }

        //check the user can get the associations of the source node
        if(!hasPermissions(userCtx, sourceID, GET_ASSOCIATIONS)){
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", sourceID, GET_ASSOCIATIONS));
        }

        return getGraphPAP().getSourceAssociations(sourceID);
    }

    /**
     * Get the associations the given node is the target node of. First, check if the user is allowed to retrieve this
     * information.
     * @param targetID The ID of the source node.
     * @return a map of the source ID and operations for each association the given node is the target of.
     * @throws PMException If the given node does not exist.
     * @throws PMAuthorizationException If the current user does not have permission to get hte node's associations.
     */
    public Map<Long, Set<String>> getTargetAssociations(UserContext userCtx, long targetID) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(!exists(userCtx, targetID)) {
            throw new PMException(String.format("node with ID %d could not be found", targetID));
        }

        //check the user can get the associations of the source node
        if(!hasPermissions(userCtx, targetID, GET_ASSOCIATIONS)){
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", targetID, GET_ASSOCIATIONS));
        }

        return getGraphPAP().getTargetAssociations(targetID);
    }

    /**
     * Search the NGAC graph for nodes that match the given parameters.  The given search parameters are provided in the
     * URI as query parameters.  The parameters name and type are extracted from the URI and passed as parameters to the
     * search function.  Any other query parameters found in the URI will be added to the search criteria as node properties.
     * A node must match all non null parameters to be returned in the search.
     *
     * @param name The name of the nodes to search for.
     * @param type The type of the nodes to search for.
     * @param properties The properties of the nodes to search for.
     * @return a Response with the nodes that match the given search criteria.
     * @throws PMException If the PAP encounters an error with the graph.
     * @throws PMAuthorizationException If the current user does not have permission to get hte node's associations.
     */
    public Set<Node> search(UserContext userCtx, String name, String type, Map<String, String> properties) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        // user the PAP searcher to search for the intended nodes
        Set<Node> nodes = getGraphPAP().search(name, type, properties);
        nodes.removeIf(x -> {
            try {
                return !hasPermissions(userCtx, x.getID(), ANY_OPERATIONS);
            }
            catch (PMException e) {
                return true;
            }
        });
        return nodes;
    }

    /**
     * Retrieve the node from the graph with the given ID.
     *
     * @param id the ID of the node to get.
     * @return the Node retrieved from the graph with the given ID.
     * @throws PMException If the node does not exist in the graph.
     * @throws PMAuthorizationException if the current user is not authorized to access this node.
     */
    public Node getNode(UserContext userCtx, long id) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(!exists(userCtx, id)) {
            throw new PMException(String.format("node with ID %d could not be found", id));
        }

        if(!hasPermissions(userCtx, id, ANY_OPERATIONS)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", id, ANY_OPERATIONS));
        }

        return getGraphPAP().getNode(id);
    }

    /**
     * Deletes all nodes in the graph
     *
     * @throws PMException if something goes wrong in the deletion process
     */
    public void reset(UserContext userCtx) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        Set<Node> nodes = getNodes(userCtx);
        Set<Long> ids = new HashSet<>();
        for (Node node: nodes) {
            ids.add(node.getID());
        }
        for (long id: ids) {
            deleteNode(userCtx, id);
        }
    }
}
