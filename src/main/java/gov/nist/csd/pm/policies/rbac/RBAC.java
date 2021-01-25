package gov.nist.csd.pm.policies.rbac;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.operations.Operations;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.util.Map;
import java.util.Set;

/**
 * Utilities for any operation relating to the RBAC NGAC concept
 */
public class RBAC {

    public static String RBAC_PC_NAME = "RBAC";
    public static Node RBAC_USERS_NODE = null;
    public static Node RBAC_OBJECTS_NODE = null;
    public static Node RBAC_PC_NODE = null;

    /**
     * This sets the RBAC PC for any of the methods in this class.
     * If the given PC already exists it will mark it as the RBAC PC,
     * otherwise it will create and mark it.
     *
     * This will likely be the first call in any method of this class.
     *
     * @param RBACname the name of the RBAC PC, null if you want to use the default name
     * @param pdp PDP of the existing graph
     * @param superUserContext UserContext of the super user
     * @return the RBAC PC
     * @throws PMException
     */
    public static Node configure (String RBACname, PDP pdp, UserContext superUserContext) throws PMException {
        Graph graph = pdp.withUser(superUserContext).getGraph();

        // todo: on-boarding methods.

        if (RBACname != null) {
            RBAC_PC_NAME = RBACname;
        }

        // DAC PC todo: find default pc node's properties
        RBAC_PC_NODE = checkAndCreateRBACNode(graph, RBAC_PC_NAME, NodeType.PC);
        Set<String> children = graph.getChildren(RBAC_PC_NAME);
        for (String child: children) {
            Node childNode = graph.getNode(child);
            if (childNode.getType().equals(NodeType.UA)) {
                RBAC_USERS_NODE = childNode;
            } else {
                RBAC_OBJECTS_NODE = childNode;
            }
        }

        return RBAC_PC_NODE;
    }

    // create user and assign role
    public static Node createUserAndAssignRole (PDP pdp, UserContext superUserContext,
                                                String userName, String roleName) throws PMException {

        Graph graph = pdp.withUser(superUserContext).getGraph();
        if (roleName == null || !graph.exists(roleName)) throw new PMException("Role must exist.");

        // create user
        // choose a role and assign the user to that role
        Node user = graph.createNode(userName, NodeType.U, null, roleName);
        return user;
    }

    // assign role
    public static void assignRole (PDP pdp, UserContext superUserContext,
                                   String userName, String roleName) throws PMException {

        Graph graph = pdp.withUser(superUserContext).getGraph();
        if (roleName == null || !graph.exists(roleName)) throw new IllegalArgumentException("Role must exist.");
        if (userName == null || !graph.exists(userName)) throw new IllegalArgumentException("User must exist.");

        // assign user to corresponding role node
        graph.assign(userName, roleName);
    }

    // remove role
    public static void deassignRole (PDP pdp, UserContext superUserContext,
                                     String userName, String roleName) throws PMException {

        Graph graph = pdp.withUser(superUserContext).getGraph();
        if (roleName == null || !graph.exists(roleName)) throw new IllegalArgumentException("Role must exist.");
        if (userName == null || !graph.exists(userName)) throw new IllegalArgumentException("User must exist.");

        // de-assign user from corresponding role node
        graph.deassign(userName, roleName);
    }

    // create role
    public static Node createRole (PDP pdp, UserContext superUserContext,
                                   String roleName) throws PMException {
        Graph graph = pdp.withUser(superUserContext).getGraph();
        if (roleName == null) throw new IllegalArgumentException("Role must exist.");

        // create role node and assign to roles (default ua)
        graph.createNode(roleName, NodeType.UA, null, RBAC_USERS_NODE.getName());

        return null;
    }

    // create role
    public static void deleteRole (PDP pdp, UserContext superUserContext,
                                   String roleName) throws PMException {
        // find role node
        // todo: how to reassign children
    }

    // get user roles
    public static Set<String> getUserRoles (PDP pdp, UserContext superUserContext,
                                            String userName) throws PMException {
        Graph graph = pdp.withUser(superUserContext).getGraph();
        if (userName == null || !graph.exists(userName)) throw new IllegalArgumentException("User must exist.");

        // get parents in the RBAC PC
        Set<String> parents = graph.getParents(userName);
        return parents;
    }

    // set_role_permissions
    public static void setRolePermissions (PDP pdp, UserContext superUserContext,
                                           String roleName, OperationSet ops, String targetName) throws PMException {

        Graph graph = pdp.withUser(superUserContext).getGraph();
        if (roleName == null || !graph.exists(roleName)) throw new IllegalArgumentException("Role must exist.");
        if (targetName == null || !graph.exists(targetName)) throw new IllegalArgumentException("Target must exist.");
        if (ops == null || ops.isEmpty()) throw new IllegalArgumentException("Ops must exist.");

        // create association from role to give objects with given permissions
        graph.associate(roleName, targetName, ops);
    }

    // get_role_permissions
    public static Map<String, OperationSet> getRolePermissions (PDP pdp, UserContext superUserContext,
                                                                String roleName) throws PMException {

        Graph graph = pdp.withUser(superUserContext).getGraph();
        if (roleName == null || !graph.exists(roleName)) throw new IllegalArgumentException("Role must exist.");


        // get associations from role
        return graph.getSourceAssociations(roleName);
    }

    /********************
     * Helper Functions *
     ********************/


    /**
     * Helper Method to check if a RBAC node exists, and other wise create it.
     * It will also set the corresponding property for that DAC node.
     *
     * This methods is specifically for RBAC nodes, and not meant to be used elsewhere
     */
    private static Node checkAndCreateRBACNode(Graph graph, String name, NodeType type) throws PMException {
        Node RBAC;
        if (!graph.exists(name)) {
            if (type == NodeType.PC) {
                return graph.createPolicyClass(name, Node.toProperties("ngac_type", "RBAC"));
            } else {
                return graph.createNode (
                        name,
                        type,
                        Node.toProperties("ngac_type", "RBAC"),
                        RBAC_PC_NAME
                );
            }
        } else {
            RBAC = graph.getNode(name);

            // add ngac_type=RBAC to properties
            String typeValue = RBAC.getProperties().get("ngac_type");
            if (typeValue == null) {
                RBAC.addProperty("ngac_type", "RBAC");
            } else if (!typeValue.equals("RBAC")) {
                throw new PMException("Node cannot have property key of ngac_type");
            }
        }
        return RBAC;
    }
}
