package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.Graph;
import gov.nist.csd.pm.policy.exceptions.NodeDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.UnauthorizedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.PC;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;

public class AdjudicatorGraph implements Graph {

    private final UserContext userCtx;
    private final PAP pap;
    private final PrivilegeChecker privilegeChecker;

    public AdjudicatorGraph(UserContext userCtx, PAP pap, PrivilegeChecker privilegeChecker) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.privilegeChecker = privilegeChecker;
    }

    @Override
    public void setResourceAccessRights(AccessRightSet accessRightSet) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.ADMIN_POLICY_TARGET.nodeName(), SET_RESOURCE_ACCESS_RIGHTS);
    }

    @Override
    public AccessRightSet getResourceAccessRights() throws PMException {
        return null;
    }

    @Override
    public String createPolicyClass(String name, Map<String, String> properties) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.POLICY_CLASS_TARGETS.nodeName(), CREATE_POLICY_CLASS);

        return null;
    }

    @Override
    public String createPolicyClass(String name) throws PMException {
        return createPolicyClass(name, NO_PROPERTIES);
    }

    @Override
    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        checkParents(CREATE_USER_ATTRIBUTE, parent);
        checkParents(CREATE_USER_ATTRIBUTE, parents);

        return null;
    }

    @Override
    public String createUserAttribute(String name, String parent, String... parents) throws PMException {
        return createUserAttribute(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        checkParents(CREATE_OBJECT_ATTRIBUTE, parent);
        checkParents(CREATE_OBJECT_ATTRIBUTE, parents);

        return null;
    }

    @Override
    public String createObjectAttribute(String name, String parent, String... parents) throws PMException {
        return createObjectAttribute(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createObject(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        checkParents(CREATE_OBJECT, parent);
        checkParents(CREATE_OBJECT, parents);

        return null;
    }

    @Override
    public String createObject(String name, String parent, String... parents) throws PMException {
        return createObject(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createUser(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        checkParents(CREATE_USER, parent);
        checkParents(CREATE_USER, parents);

        return null;
    }

    @Override
    public String createUser(String name, String parent, String... parents) throws PMException {
        return createUser(name, NO_PROPERTIES, parent, parents);
    }

    private void checkParents(String accessRight, String ... parents) throws PMException {
        for (String parent : parents) {
            privilegeChecker.check(userCtx, parent, accessRight);
        }
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties) throws PMException {
        privilegeChecker.check(userCtx, name, SET_NODE_PROPERTIES);
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        boolean exists = pap.graph().nodeExists(name);
        if (!exists) {
            return false;
        }

        // check user has permissions on the node
        privilegeChecker.check(userCtx, name);

        return true;
    }

    @Override
    public Node getNode(String name) throws PMException {
        // get node
        Node node = pap.graph().getNode(name);

        // check user has permissions on the node
        privilegeChecker.check(userCtx, name);

        return node;
    }

    @Override
    public List<String> search(NodeType type, Map<String, String> properties) throws PMException {
        List<String> search = pap.graph().search(type, properties);
        search.removeIf(node -> {
            try {
                privilegeChecker.check(userCtx, node);
                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return search;
    }

    @Override
    public List<String> getPolicyClasses() throws PMException {
        List<String> policyClasses = pap.graph().getPolicyClasses();
        policyClasses.removeIf(pc -> {
            try {
                privilegeChecker.check(userCtx, AdminPolicy.policyClassTargetName(pc));
                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return policyClasses;
    }

    @Override
    public void deleteNode(String name) throws PMException {
        NodeType nodeType = pap.graph().getNode(name).getType();

        if (nodeType == PC) {
            privilegeChecker.check(userCtx, AdminPolicy.policyClassTargetName(name), DELETE_POLICY_CLASS);
            return;
        }

        String op = "";
        switch (nodeType) {
            case OA: op = DELETE_OBJECT_ATTRIBUTE; break;
            case UA: op = DELETE_USER_ATTRIBUTE; break;
            case O: op = DELETE_OBJECT; break;
            case U: op = DELETE_USER; break;
            default: op = DELETE_POLICY_CLASS; break;
        };

        // check the user can delete the node
        privilegeChecker.check(userCtx, name, op);

        // check that the user can delete the node from the node's parents
        List<String> parents = pap.graph().getParents(name);

        for(String parent : parents) {
            privilegeChecker.check(userCtx, parent, op);
        }
    }

    @Override
    public void assign(String child, String parent) throws PMException {
        Node childNode = pap.graph().getNode(child);
        Node parentNode = pap.graph().getNode(parent);

        //check the user can assign the child
        privilegeChecker.check(userCtx, child, ASSIGN);

        // check that the user can assign to the parent node
        privilegeChecker.check(userCtx, parent, ASSIGN_TO);
    }

    @Override
    public void deassign(String child, String parent) throws PMException {
        Node childNode = pap.graph().getNode(child);
        Node parentNode = pap.graph().getNode(parent);

        //check the user can deassign the child
        privilegeChecker.check(userCtx, child, DEASSIGN);

        // check that the user can deassign from the parent node
        privilegeChecker.check(userCtx, parent, DEASSIGN_FROM);
    }

    @Override
    public List<String> getParents(String node) throws PMException {
        List<String> parents = pap.graph().getParents(node);
        parents.removeIf(parent -> {
            try {
                privilegeChecker.check(userCtx, parent);
                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return parents;
    }

    @Override
    public List<String> getChildren(String node) throws PMException {
        List<String> children = pap.graph().getChildren(node);
        children.removeIf(child -> {
            try {
                privilegeChecker.check(userCtx, child);
                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return children;
    }

    @Override
    public void associate(String ua, String target, AccessRightSet accessRights) throws PMException {
        privilegeChecker.check(userCtx, ua, ASSOCIATE);
        privilegeChecker.check(userCtx, target, ASSOCIATE_TO);
    }

    @Override
    public void dissociate(String ua, String target) throws PMException {
        privilegeChecker.check(userCtx, ua, DISSOCIATE);
        privilegeChecker.check(userCtx, target, DISSOCIATE_FROM);
    }

    @Override
    public List<Association> getAssociationsWithSource(String ua) throws PMException {
        return getAssociations(pap.graph().getAssociationsWithSource(ua));
    }

    @Override
    public List<Association> getAssociationsWithTarget(String target) throws PMException {
        return getAssociations(pap.graph().getAssociationsWithTarget(target));
    }

    private List<Association> getAssociations(List<Association> associations) {
        associations.removeIf(association -> {
            try {
                privilegeChecker.check(userCtx, association.getSource(), GET_ASSOCIATIONS);
                privilegeChecker.check(userCtx, association.getTarget(), GET_ASSOCIATIONS);
                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return associations;
    }
}
