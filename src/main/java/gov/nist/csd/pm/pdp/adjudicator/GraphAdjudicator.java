package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.SuperPolicy;
import gov.nist.csd.pm.policy.Graph;
import gov.nist.csd.pm.policy.exceptions.NodeDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.UnauthorizedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssignmentException;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssociationException;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_PC_REP;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.PC;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;

public class GraphAdjudicator implements Graph {

    private final UserContext userCtx;
    private final PAP pap;
    private final AccessRightChecker accessRightChecker;

    public GraphAdjudicator(UserContext userCtx, PAP pap, AccessRightChecker accessRightChecker) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.accessRightChecker = accessRightChecker;
    }

    @Override
    public void setResourceAccessRights(AccessRightSet accessRightSet) throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, SET_RESOURCE_ACCESS_RIGHTS);
    }

    @Override
    public AccessRightSet getResourceAccessRights() throws PMException {
        return null;
    }

    @Override
    public String createPolicyClass(String name, Map<String, String> properties) throws PMException {
        // check that the user has the CREATE_POLICY_CLASS right on the super policy object
        accessRightChecker.check(userCtx, SUPER_PC_REP, CREATE_POLICY_CLASS);

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
            accessRightChecker.check(userCtx, parent, accessRight);
        }
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties) throws PMException {
        accessRightChecker.check(userCtx, name, SET_NODE_PROPERTIES);
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        boolean exists = pap.graph().nodeExists(name);
        if (!exists) {
            return false;
        }

        try {
            accessRightChecker.check(userCtx, name);
            return true;
        } catch (UnauthorizedException e) {
            return false;
        }
    }

    @Override
    public Node getNode(String name) throws PMException {
        // get node
        Node node = pap.graph().getNode(name);

        // check user has permissions on the node
        try {
            accessRightChecker.check(userCtx, name);
        } catch (UnauthorizedException e) {
            // if no permissions, the user shouldn't know it exists
            throw new NodeDoesNotExistException(name);
        }

        return node;
    }

    @Override
    public List<String> search(NodeType type, Map<String, String> properties) throws PMException {
        List<String> search = pap.graph().search(type, properties);
        search.removeIf(node -> {
            try {
                accessRightChecker.check(userCtx, node);
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
                accessRightChecker.check(userCtx, SuperPolicy.pcRepObjectAttribute(pc));
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
            accessRightChecker.check(userCtx, SuperPolicy.pcRepObjectAttribute(name), DELETE_POLICY_CLASS);
            return;
        }

        String op = switch (nodeType) {
            case OA -> DELETE_OBJECT_ATTRIBUTE;
            case UA -> DELETE_USER_ATTRIBUTE;
            case O -> DELETE_OBJECT;
            case U -> DELETE_USER;
            default -> DELETE_POLICY_CLASS;
        };

        // check the user can delete the node
        accessRightChecker.check(userCtx, name, op);

        // check that the user can delete the node from the node's parents
        List<String> parents = pap.graph().getParents(name);

        for(String parent : parents) {
            accessRightChecker.check(userCtx, parent, op);
        }
    }

    @Override
    public void assign(String child, String parent) throws PMException {
        Node childNode = pap.graph().getNode(child);
        Node parentNode = pap.graph().getNode(parent);

        String childAR = switch (childNode.getType()) {
            case OA -> ASSIGN_OBJECT_ATTRIBUTE;
            case UA -> ASSIGN_USER_ATTRIBUTE;
            case O -> ASSIGN_OBJECT;
            case U -> ASSIGN_USER;
            default -> throw new IllegalArgumentException("cannot assign node of type " + childNode.getType());
        };

        String parentAR = switch (parentNode.getType()) {
            case OA -> ASSIGN_TO_OBJECT_ATTRIBUTE;
            case UA -> ASSIGN_TO_USER_ATTRIBUTE;
            case PC -> ASSIGN_TO_POLICY_CLASS;
            default -> throw new IllegalArgumentException("cannot assign to a node of type " + parentNode.getType());
        };

        //check the user can assign the child
        accessRightChecker.check(userCtx, child, childAR);

        // check that the user can assign to the parent node
        accessRightChecker.check(userCtx, parent, parentAR);
    }

    @Override
    public void deassign(String child, String parent) throws PMException {
        Node childNode = pap.graph().getNode(child);
        Node parentNode = pap.graph().getNode(parent);

        String childAR = switch (childNode.getType()) {
            case OA -> DEASSIGN_OBJECT_ATTRIBUTE;
            case UA -> DEASSIGN_USER_ATTRIBUTE;
            case O -> DEASSIGN_OBJECT;
            case U -> DEASSIGN_USER;
            default -> throw new InvalidAssignmentException("cannot deassign node of type " + childNode.getType());
        };

        String parentAR = switch (parentNode.getType()) {
            case OA -> DEASSIGN_FROM_OBJECT_ATTRIBUTE;
            case UA -> DEASSIGN_FROM_USER_ATTRIBUTE;
            case PC -> DEASSIGN_FROM_POLICY_CLASS;
            default -> throw new InvalidAssignmentException("cannot deassign from a node of type " + parentNode.getType());
        };

        //check the user can deassign the child
        accessRightChecker.check(userCtx, child, childAR);

        // check that the user can deassign from the parent node
        accessRightChecker.check(userCtx, parent, parentAR);
    }

    @Override
    public void assignAll(List<String> children, String target) throws PMException {
        Node parentNode = pap.graph().getNode(target);

        String parentAR = switch (parentNode.getType()) {
            case OA -> ASSIGN_TO_OBJECT_ATTRIBUTE;
            case UA -> ASSIGN_TO_USER_ATTRIBUTE;
            case PC -> ASSIGN_TO_POLICY_CLASS;
            default -> throw new IllegalArgumentException("cannot assign to a node of type " + parentNode.getType());
        };

        // check user can assign to parent
        accessRightChecker.check(userCtx, target, parentAR);

        // check the user can assign each child
        for (String child : children) {
            Node childNode = pap.graph().getNode(child);

            String childAR = switch (childNode.getType()) {
                case OA -> ASSIGN_OBJECT_ATTRIBUTE;
                case UA -> ASSIGN_USER_ATTRIBUTE;
                case O -> ASSIGN_OBJECT;
                case U -> ASSIGN_USER;
                default -> throw new IllegalArgumentException("cannot assign node of type " + childNode.getType());
            };

            accessRightChecker.check(userCtx, child, childAR);
        }
    }

    @Override
    public void deassignAll(List<String> children, String target) throws PMException {
        Node parentNode = pap.graph().getNode(target);

        String parentAR = switch (parentNode.getType()) {
            case OA -> DEASSIGN_FROM_OBJECT_ATTRIBUTE;
            case UA -> DEASSIGN_FROM_USER_ATTRIBUTE;
            case PC -> DEASSIGN_FROM_POLICY_CLASS;
            default -> throw new InvalidAssignmentException("cannot deassign from a node of type " + parentNode.getType());
        };

        // check user can deassign from parent
        accessRightChecker.check(userCtx, target, parentAR);

        // check the user can deassign each child
        for (String child : children) {
            Node childNode = pap.graph().getNode(child);

            String childAR = switch (childNode.getType()) {
                case OA -> DEASSIGN_OBJECT_ATTRIBUTE;
                case UA -> DEASSIGN_USER_ATTRIBUTE;
                case O -> DEASSIGN_OBJECT;
                case U -> DEASSIGN_USER;
                default -> throw new InvalidAssignmentException("cannot deassign node of type " + childNode.getType());
            };

            accessRightChecker.check(userCtx, child, childAR);
        }
    }

    @Override
    public void deassignAllFromAndDelete(String target) throws PMException {
        Node parentNode = pap.graph().getNode(target);

        String parentAR = switch (parentNode.getType()) {
            case OA -> DEASSIGN_FROM_OBJECT_ATTRIBUTE;
            case UA -> DEASSIGN_FROM_USER_ATTRIBUTE;
            case PC -> DEASSIGN_FROM_POLICY_CLASS;
            default -> throw new InvalidAssignmentException("cannot deassign from a node of type " + parentNode.getType());
        };

        // check user can deassign from parent
        accessRightChecker.check(userCtx, target, parentAR);

        parentAR = switch (parentNode.getType()) {
            case OA -> DELETE_OBJECT_ATTRIBUTE;
            case UA -> DELETE_USER_ATTRIBUTE;
            case O -> DELETE_OBJECT;
            case U -> DELETE_USER;
            default -> DELETE_POLICY_CLASS;
        };

        // check the user can delete the target
        accessRightChecker.check(userCtx, target, parentAR);

        // check the user can deassign each child
        for (String child : pap.graph().getChildren(target)) {
            Node childNode = pap.graph().getNode(child);

            String childAR = switch (childNode.getType()) {
                case OA -> DEASSIGN_OBJECT_ATTRIBUTE;
                case UA -> DEASSIGN_USER_ATTRIBUTE;
                case O -> DEASSIGN_OBJECT;
                case U -> DEASSIGN_USER;
                default -> throw new InvalidAssignmentException("cannot deassign node of type " + childNode.getType());
            };

            accessRightChecker.check(userCtx, child, childAR);
        }
    }

    @Override
    public List<String> getParents(String node) throws PMException {
        List<String> parents = pap.graph().getParents(node);
        parents.removeIf(parent -> {
            try {
                accessRightChecker.check(userCtx, parent);
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
                accessRightChecker.check(userCtx, child);
                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return children;
    }

    @Override
    public void associate(String ua, String target, AccessRightSet accessRights) throws PMException {
        Node targetNode = pap.graph().getNode(target);

        String targetAR = switch (targetNode.getType()) {
            case OA -> DISSOCIATE_OBJECT_ATTRIBUTE;
            case UA -> DISSOCIATE_USER_ATTRIBUTE;
            default -> throw new InvalidAssociationException("cannot associate a target node of type " + targetNode.getType());
        };

        //check the user can associate the source and target nodes
        accessRightChecker.check(userCtx, ua, ASSOCIATE_USER_ATTRIBUTE);
        accessRightChecker.check(userCtx, target, targetAR);
    }

    @Override
    public void dissociate(String ua, String target) throws PMException {
        Node targetNode = pap.graph().getNode(target);

        String targetAR = switch (targetNode.getType()) {
            case OA -> DISSOCIATE_OBJECT_ATTRIBUTE;
            case UA -> DISSOCIATE_USER_ATTRIBUTE;
            default -> throw new InvalidAssociationException("cannot dissociate a target node of type " + targetNode.getType());
        };

        //check the user can dissociate the source and target nodes
        accessRightChecker.check(userCtx, ua, DISSOCIATE_USER_ATTRIBUTE);
        accessRightChecker.check(userCtx, target, targetAR);
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
                accessRightChecker.check(userCtx, association.getSource(), GET_ASSOCIATIONS);
                accessRightChecker.check(userCtx, association.getTarget(), GET_ASSOCIATIONS);
                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return associations;
    }
}
