package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.epp.events.AssignEvent;
import gov.nist.csd.pm.epp.events.AssignToEvent;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.epp.events.DeassignEvent;
import gov.nist.csd.pm.epp.events.DeassignFromEvent;
import gov.nist.csd.pm.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.policy.SuperPolicy;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.graph.model.relationships.Assignment;
import gov.nist.csd.pm.pip.graph.model.relationships.Association;

import java.util.*;

import static gov.nist.csd.pm.operations.Operations.*;
import static gov.nist.csd.pm.pdp.decider.PReviewDecider.ALL_OPERATIONS;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.*;

/**
 * GraphService provides methods to maintain an NGAC graph, while also ensuring any user interacting with the graph,
 * has the correct permissions to do so.
 */
public class GraphService extends Service implements Graph {

    private Graph graph;

    public GraphService(PAP pap, EPP epp) throws PMException {
        super(pap, epp);

        this.graph = pap.getGraphPAP();
    }

    public SuperPolicy configureSuperPolicy() throws PMException {
        superPolicy = new SuperPolicy();
        superPolicy.configure(graph);
        return superPolicy;
    }

    @Override
    public Node createPolicyClass(String name, Map<String, String> properties) throws PMException {
        // check that the user can create a policy class
        if (!hasPermissions(userCtx, superPolicy.getSuperPolicyClassRep().getName(), CREATE_POLICY_CLASS)) {
            throw new PMAuthorizationException("unauthorized permissions to create a policy class");
        }

        if (properties == null) {
            properties = new HashMap<>();
        }

        // create the PC node
        String rep = name + "_rep";
        String defaultUA = name + "_default_UA";
        String defaultOA = name + "_default_OA";

        properties.putAll(Node.toProperties("default_ua", defaultUA, "default_oa", defaultOA,
                REP_PROPERTY, rep));
        Node pcNode = getPAP().getGraphPAP().createPolicyClass(name, properties);
        // create the PC UA node
        Node pcUANode = getPAP().getGraphPAP().createNode(defaultUA, UA, Node.toProperties(NAMESPACE_PROPERTY, name), pcNode.getName());
        // create the PC OA node
        Node pcOANode = getPAP().getGraphPAP().createNode(defaultOA, OA, Node.toProperties(NAMESPACE_PROPERTY, name), pcNode.getName());

        // assign Super U to PC UA
        // getPAP().getGraphPAP().assign(superPolicy.getSuperU().getID(), pcUANode.getID());
        // assign superUA and superUA2 to PC
        getPAP().getGraphPAP().assign(superPolicy.getSuperUserAttribute().getName(), pcNode.getName());
        getPAP().getGraphPAP().assign(superPolicy.getSuperUserAttribute2().getName(), pcNode.getName());
        // associate Super UA and PC UA
        getPAP().getGraphPAP().associate(superPolicy.getSuperUserAttribute().getName(), pcUANode.getName(), new OperationSet(ALL_OPERATIONS));
        // associate Super UA and PC OA
        getPAP().getGraphPAP().associate(superPolicy.getSuperUserAttribute().getName(), pcOANode.getName(), new OperationSet(ALL_OPERATIONS));

        // create an OA that will represent the pc
        getPAP().getGraphPAP().createNode(rep, OA, Node.toProperties("pc", String.valueOf(name)),
                superPolicy.getSuperObjectAttribute().getName());

        return pcNode;
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
     * @throws IllegalArgumentException if the name is null or empty.
     * @throws IllegalArgumentException if the type is null.
     */
    @Override
    public Node createNode(String name, NodeType type, Map<String, String> properties, String initialParent, String ... additionalParents) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if (name == null) {
            throw new IllegalArgumentException("a node cannot have a null name");
        } else if (type == null) {
            throw new IllegalArgumentException("a node cannot have a null type");
        }

        // instantiate the properties map if it's null
        // if this node is a user, hash the password if present in the properties
        if(properties == null) {
            properties = new HashMap<>();
        }
        
        // check that the user has the permission to assign to the parent node
        if (!hasPermissions(userCtx, initialParent, ASSIGN_TO)) {
            // if the user cannot assign to the parent node, delete the newly created node
            throw new PMAuthorizationException(String.format("unauthorized permission \"%s\" on node %s", ASSIGN_TO, initialParent));
        }
        // if the parent is a PC get the PC default
        Node parentNode = graph.getNode(initialParent);
        if (parentNode.getType().equals(PC)) {
            initialParent = getPolicyClassDefault(parentNode.getName(), type);
            parentNode = getNode(initialParent);
        }

        // check any additional parents before assigning
        for (int i = 0; i < additionalParents.length; i++) {
            String parent = additionalParents[i];

            if (!hasPermissions(userCtx, parent, ASSIGN_TO)) {
                // if the user cannot assign to the parent node, delete the newly created node
                throw new PMAuthorizationException(String.format("unauthorized permission \"%s\" on %s", ASSIGN_TO, parent));
            }

            // if the parent is a PC get the PC default
            Node additionalParentNode = graph.getNode(parent);
            if (additionalParentNode.getType().equals(PC)) {
               additionalParents[i] = getPolicyClassDefault(additionalParentNode.getName(), type);
            }
        }

        //create the node
        Node node = getGraphPAP().createNode(name, type, properties, initialParent, additionalParents);

        // process event for initial parent
        getEPP().processEvent(new AssignToEvent(userCtx, parentNode, node));
        // process event for any additional parents
        for (String parent : additionalParents) {
            parentNode = graph.getNode(parent);
            getEPP().processEvent(new AssignToEvent(userCtx, parentNode, node));
        }

        return node;
    }

    public String getPolicyClassDefault(String pc, NodeType type) throws PMException {
        return pc + "_default_" + type.toString();
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
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        // check that the user can update the node
        if(!hasPermissions(userCtx, name, UPDATE_NODE)) {
            throw new PMAuthorizationException(String.format("unauthorized permission %s on node %s", UPDATE_NODE, name));
        }

        //update node in the PAP
        getGraphPAP().updateNode(name, properties);
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
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        Node node = getGraphPAP().getNode(name);

        // check the user can deassign the node
        if (!hasPermissions(userCtx, name, DEASSIGN)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", name, DEASSIGN));
        }

        // check that the user can deassign from the node's parents
        Set<String> parents = getGraphPAP().getParents(name);
        for(String parent : parents) {
            if(!hasPermissions(userCtx, parent, DEASSIGN_FROM)) {
                throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", parent, DEASSIGN_FROM));
            }

            Node parentNode = getGraphPAP().getNode(parent);

            getEPP().processEvent(new DeassignEvent(userCtx, node, parentNode));
            getEPP().processEvent(new DeassignFromEvent(userCtx, parentNode, node));
        }

        // if it's a PC, delete the rep
        if (node.getType().equals(PC)) {
            if (node.getProperties().containsKey(REP_PROPERTY)) {
                getGraphPAP().deleteNode(node.getProperties().get(REP_PROPERTY));
            }
        }

        getGraphPAP().deleteNode(name);
    }

    /**
     * Check that a node with the given name exists.  Just checking the in-memory graph is faster.
     * @param name the name of the node to check for.
     * @return true if a node with the given name exists, false otherwise.
     * @throws PMException if there is an error checking if the node exists in the graph through the PAP.
     */
    public boolean exists(String name) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        boolean exists = getGraphPAP().exists(name);
        if (!exists) {
            return false;
        }

        Node node = getGraphPAP().getNode(name);

        // if the node is a pc the user must have permission on the rep OA of the PC
        if (node.getType().equals(PC)) {
            String pcRep = node.getProperties().get(REP_PROPERTY);
            node = getGraphPAP().getNode(pcRep);
            return hasPermissions(userCtx, node.getName(), ANY_OPERATIONS);
        }

        // node exists, return false if the user does not have access to it.
        return hasPermissions(userCtx, name, ANY_OPERATIONS);
    }

    /**
     * Retrieve the list of all nodes in the graph.  Go to the database to do this, since it is more likely to have
     * all of the node information.
     * @return the set of all nodes in the graph.
     * @throws PMException if there is an error getting the nodes from the PAP.
     */
    public Set<Node> getNodes() throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        Set<Node> nodes = new HashSet<>(getGraphPAP().getNodes());
        nodes.removeIf((node) -> {
            try {
                return !hasPermissions(userCtx, node.getName(), ANY_OPERATIONS);
            }
            catch (PMException e) {
                e.printStackTrace();
                return true;
            }
        });

        return new HashSet<>(nodes);
    }

    /**
     * Get the set of policy classes. This can be performed by the in-memory graph.
     * @return the set of names for the policy classes in the graph.
     * @throws PMException if there is an error getting the policy classes from the PAP.
     */
    public Set<String> getPolicyClasses() throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        return getGraphPAP().getPolicyClasses();
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
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(!exists(name)) {
            throw new PMException(String.format("node %s could not be found", name));
        }

        Set<String> children = getGraphPAP().getChildren(name);
        children.removeIf((node) -> {
            try {
                return !hasPermissions(userCtx, node, ANY_OPERATIONS);
            }
            catch (PMException e) {
                e.printStackTrace();
                return true;
            }
        });

        return children;
    }

    /**
     * Get the parents of the node from the graph.  Get the parents from the database to ensure all node information
     * is present.  Before returning the set of nodes, filter out any nodes that the user has no permissions on.
     * @param name the name of the node to get the parents of.
     * @return a set of Node objects, representing the parents of the target node.
     * @throws PMException if the target node does not exist.
     * @throws PMException if there is an error getting the parents from the PAP.
     */
    public Set<String> getParents(String name) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(!exists(name)) {
            throw new PMException(String.format("node %s could not be found", name));
        }

        Set<String> parents = getGraphPAP().getParents(name);
        parents.removeIf((node) -> {
            try {
                return !hasPermissions(userCtx, node, ANY_OPERATIONS);
            }
            catch (PMException e) {
                e.printStackTrace();
                return true;
            }
        });

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
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        // check that the nodes are not null
        if(child == null) {
            throw new IllegalArgumentException("the child node name cannot be null when creating an assignment");
        } else if(parent == null) {
            throw new IllegalArgumentException("the parent node name cannot be null when creating an assignment");
        } else if(!exists(child)) {
            throw new PMException(String.format("child node %s does not exist", child));
        } else if(!exists(parent)) {
            throw new PMException(String.format("parent node %s does not exist", parent));
        }

        //check the user can assign the child
        if(!hasPermissions(userCtx, child, ASSIGN)) {
            throw new PMAuthorizationException(String.format("unauthorized permission %s on node %s", ASSIGN, child));
        }

        //check if the assignment is valid
        Node childNode = getNode(child);
        Node parentNode = getNode(parent);
        Assignment.checkAssignment(childNode.getType(), parentNode.getType());

        // check that the user can assign to the parent node
        if (!hasPermissions(userCtx, parent, ASSIGN_TO)) {
            throw new PMAuthorizationException(String.format("unauthorized permission %s on node %s", ASSIGN_TO, parent));
        }

        if (parentNode.getType().equals(PC)) {
            parent = getPolicyClassDefault(parentNode.getName(), childNode.getType());
        }

        // assign in the PAP
        getGraphPAP().assign(child, parent);

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
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        // check that the parameters are correct
        if(child == null) {
            throw new IllegalArgumentException("the child node cannot be null when deassigning");
        } else if(parent == null) {
            throw new IllegalArgumentException("the parent node cannot be null when deassigning");
        } else if(!exists(child)) {
            throw new PMException(String.format("child node %s could not be found when deassigning", child));
        } else if(!exists(parent)) {
            throw new PMException(String.format("parent node %s could not be found when deassigning", parent));
        }

        //check the user can deassign the child
        if(!hasPermissions(userCtx, child, DEASSIGN)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", child, DEASSIGN));
        }

        //check that the user can deassign from the parent
        if (!hasPermissions(userCtx, parent, DEASSIGN_FROM)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", parent, DEASSIGN_FROM));
        }

        //delete assignment in PAP
        getGraphPAP().deassign(child, parent);

        Node parentNode = getNode(parent);
        Node childNode = getNode(child);

        getEPP().processEvent(new DeassignEvent(userCtx, childNode, parentNode));
        getEPP().processEvent(new DeassignFromEvent(userCtx, parentNode, childNode));
    }

    @Override
    public boolean isAssigned(String child, String parent) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        Node parentNode = getNode(parent);
        Node childNode = getNode(child);

        return getGraphPAP().isAssigned(childNode.getName(), parentNode.getName());

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
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(ua == null) {
            throw new IllegalArgumentException("the user attribute cannot be null when creating an association");
        } else if(target == null) {
            throw new IllegalArgumentException("the target node cannot be null when creating an association");
        }

        Node sourceNode = getNode(ua);
        Node targetNode = getNode(target);

        Association.checkAssociation(sourceNode.getType(), targetNode.getType());

        //check the user can associate the source and target nodes
        if(!hasPermissions(userCtx, ua, ASSOCIATE)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", sourceNode.getName(), ASSOCIATE));
        }
        if (!hasPermissions(userCtx, target, ASSOCIATE)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", targetNode.getName(), ASSOCIATE));
        }

        //create association in PAP
        getGraphPAP().associate(ua, target, operations);
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
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(ua == null) {
            throw new IllegalArgumentException("the user attribute cannot be null when creating an association");
        } else if(target == null) {
            throw new IllegalArgumentException("the target cannot be null when creating an association");
        } else if(!exists(ua)) {
            throw new PMException(String.format("node %s could not be found when creating an association", ua));
        } else if(!exists(target)) {
            throw new PMException(String.format("node %s could not be found when creating an association", target));
        }

        //check the user can associate the source and target nodes
        if(!hasPermissions(userCtx, ua, DISASSOCIATE)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", ua, DISASSOCIATE));
        }
        if (!hasPermissions(userCtx, target, DISASSOCIATE)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", target, DISASSOCIATE));
        }

        //create association in PAP
        getGraphPAP().dissociate(ua, target);
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
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(!exists(source)) {
            throw new PMException(String.format("node %s could not be found", source));
        }

        //check the user can get the associations of the source node
        if(!hasPermissions(userCtx, source, GET_ASSOCIATIONS)){
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", source, GET_ASSOCIATIONS));
        }

        return getGraphPAP().getSourceAssociations(source);
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
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(!exists(target)) {
            throw new PMException(String.format("node %s could not be found", target));
        }

        //check the user can get the associations of the source node
        if(!hasPermissions(userCtx, target, GET_ASSOCIATIONS)){
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", target, GET_ASSOCIATIONS));
        }

        return getGraphPAP().getTargetAssociations(target);
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
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        Set<Node> search = getGraphPAP().search(type, properties);
        search.removeIf(x -> {
            try {
                return !hasPermissions(userCtx, x.getName(), ANY_OPERATIONS);
            }
            catch (PMException e) {
                return true;
            }
        });
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
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        if(!exists(name)) {
            throw new PMException(String.format("node %s could not be found", name));
        }

        if(!hasPermissions(userCtx, name, ANY_OPERATIONS)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", name, ANY_OPERATIONS));
        }

        return getGraphPAP().getNode(name);
    }

    @Override
    public Node getNode(NodeType type, Map<String, String> properties) throws PMException {
        Node node = getGraphPAP().getNode(type, properties);
        if (!hasPermissions(userCtx, node.getName(), ANY_OPERATIONS)) {
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
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        // check that the user can reset the graph
        if (!hasPermissions(userCtx, superPolicy.getSuperPolicyClassRep().getName(), RESET)) {
            throw new PMAuthorizationException("unauthorized permissions to reset the graph");
        }

        Collection<Node> nodes = getGraphPAP().getNodes();
        Set<String> names = new HashSet<>();
        for (Node node: nodes) {
            names.add(node.getName());
        }
        for (String name : names) {
            getGraphPAP().deleteNode(name);
        }
    }
}
