package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.SuperPolicy;
import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.PolicySerializable;
import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.NodeDoesNotExistException;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssignmentException;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssociationException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.serializer.PolicyDeserializer;
import gov.nist.csd.pm.policy.serializer.PolicySerializer;
import gov.nist.csd.pm.policy.author.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.UnauthorizedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_PC_REP;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.GET_ASSOCIATIONS;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.PC;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;

public class Adjudicator implements PolicyAuthor, PolicySerializable {

    private final UserContext userCtx;
    private final PAP pap;
    private final AccessRightChecker accessRightChecker;

    public Adjudicator(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.accessRightChecker = new AccessRightChecker(pap, policyReviewer);
    }

    @Override
    public String toString(PolicySerializer policySerializer) throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, TO_STRING);

        return null;
    }

    @Override
    public void fromString(String s, PolicyDeserializer policyDeserializer) throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, FROM_STRING);
    }

    @Override
    public AccessRightSet getResourceAccessRights() throws PMException {
        return null;
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        boolean exists = pap.nodeExists(name);
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
        Node node = pap.getNode(name);

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
        List<String> search = pap.search(type, properties);
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
        List<String> policyClasses = pap.getPolicyClasses();
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
    public List<String> getChildren(String node) throws PMException {
        List<String> children = pap.getChildren(node);
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
    public List<String> getParents(String node) throws PMException {
        List<String> parents = pap.getParents(node);
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
    public List<Association> getAssociationsWithSource(String ua) throws PMException {
        return getAssociations(pap.getAssociationsWithSource(ua));
    }

    @Override
    public List<Association> getAssociationsWithTarget(String target) throws PMException {
        return getAssociations(pap.getAssociationsWithTarget(target));
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

    @Override
    public Map<String, List<Prohibition>> getProhibitions() throws PMException {
        Map<String, List<Prohibition>> prohibitions = pap.getProhibitions();
        Map<String, List<Prohibition>> retProhibitions = new HashMap<>();
        for (String subject : prohibitions.keySet()) {
            List<Prohibition> subjectPros = filterProhibitions(prohibitions.get(subject));
            retProhibitions.put(subject, subjectPros);
        }

        return retProhibitions;
    }

    @Override
    public boolean prohibitionExists(String label) throws PMException {
        boolean exists = pap.prohibitionExists(label);
        if (!exists) {
            return false;
        }

        try {
            getProhibition(label);
        } catch (UnauthorizedException e) {
            return false;
        }


        return true;
    }

    @Override
    public List<Prohibition> getProhibitionsWithSubject(String subject) throws PMException {
        return filterProhibitions(pap.getProhibitionsWithSubject(subject));
    }

    @Override
    public Prohibition getProhibition(String label) throws PMException {
        Prohibition prohibition = pap.getProhibition(label);

        // check user has access to subject prohibitions
        if (prohibition.getSubject().type() == ProhibitionSubject.Type.PROCESS) {
            accessRightChecker.check(userCtx, SUPER_PC_REP, GET_PROCESS_PROHIBITIONS);
        } else {
            accessRightChecker.check(userCtx, prohibition.getSubject().name(), GET_PROHIBITIONS);
        }

        // check user has access to each target prohibitions
        for (ContainerCondition containerCondition : prohibition.getContainers()) {
            accessRightChecker.check(userCtx, containerCondition.name(), GET_PROHIBITIONS);
        }

        return prohibition;
    }

    private List<Prohibition> filterProhibitions(List<Prohibition> prohibitions) {
        prohibitions.removeIf(prohibition -> {
            try {
                // check user has access to subject prohibitions
                if (prohibition.getSubject().type() == ProhibitionSubject.Type.PROCESS) {
                    accessRightChecker.check(userCtx, SUPER_PC_REP, GET_PROCESS_PROHIBITIONS);
                } else {
                    accessRightChecker.check(userCtx, prohibition.getSubject().name(), GET_PROHIBITIONS);
                }

                // check user has access to each target prohibitions
                for (ContainerCondition containerCondition : prohibition.getContainers()) {
                    accessRightChecker.check(userCtx, containerCondition.name(), GET_PROHIBITIONS);
                }

                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return prohibitions;
    }

    @Override
    public List<Obligation> getObligations() throws PMException {
        List<Obligation> obligations = pap.getObligations();
        obligations.removeIf(obligation -> {
            try {
                for (Rule rule : obligation.getRules()) {
                    EventSubject subject = rule.getEvent().getSubject();
                    checkSubject(subject, GET_OBLIGATION);

                    Target target = rule.getEvent().getTarget();
                    checkTarget(target, GET_OBLIGATION);
                }
                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return obligations;
    }

    @Override
    public boolean obligationExists(String label) throws PMException {
        boolean exists = pap.obligationExists(label);
        if (!exists) {
            return false;
        }

        try {
            getObligation(label);
        } catch (UnauthorizedException e) {
            return false;
        }

        return true;
    }

    @Override
    public Obligation getObligation(String label) throws PMException {
        Obligation obligation = pap.getObligation(label);
        for (Rule rule : obligation.getRules()) {
            EventSubject subject = rule.getEvent().getSubject();
            checkSubject(subject, GET_OBLIGATION);

            Target target = rule.getEvent().getTarget();
            checkTarget(target, GET_OBLIGATION);
        }

        return obligation;
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getPALFunctions() throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, GET_FUNCTIONS);

        return null;
    }

    @Override
    public Map<String, Value> getPALConstants() throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, GET_CONSTANTS);

        return null;
    }

    @Override
    public PALContext getPALContext() throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, GET_CONTEXT);

        return null;
    }

    @Override
    public void setResourceAccessRights(AccessRightSet accessRightSet) throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, SET_RESOURCE_ACCESS_RIGHTS);
    }

    @Override
    public String createPolicyClass(String name, Map<String, String> properties) throws PMException {
        // check that the user has the CREATE_POLICY_CLASS right on the super policy object
        accessRightChecker.check(userCtx, SUPER_PC_REP, CREATE_POLICY_CLASS);

        return null;
    }

    @Override
    public String createPolicyClass(String name) throws PMException {
        return createPolicyClass(name, noprops());
    }

    @Override
    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        checkParents(CREATE_USER_ATTRIBUTE, parent);
        checkParents(CREATE_USER_ATTRIBUTE, parents);

        return null;
    }

    @Override
    public String createUserAttribute(String name, String parent, String... parents) throws PMException {
        return createUserAttribute(name, noprops(), parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        checkParents(CREATE_OBJECT_ATTRIBUTE, parent);
        checkParents(CREATE_OBJECT_ATTRIBUTE, parents);

        return null;
    }

    @Override
    public String createObjectAttribute(String name, String parent, String... parents) throws PMException {
        return createObjectAttribute(name, noprops(), parent, parents);
    }

    @Override
    public String createObject(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        checkParents(CREATE_OBJECT, parent);
        checkParents(CREATE_OBJECT, parents);

        return null;
    }

    @Override
    public String createObject(String name, String parent, String... parents) throws PMException {
        return createObject(name, noprops(), parent, parents);
    }

    @Override
    public String createUser(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        checkParents(CREATE_USER, parent);
        checkParents(CREATE_USER, parents);

        return null;
    }

    @Override
    public String createUser(String name, String parent, String... parents) throws PMException {
        return createUser(name, noprops(), parent, parents);
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
    public void deleteNode(String name) throws PMException {
        NodeType nodeType = pap.getNode(name).getType();

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
        List<String> parents = pap.getParents(name);

        for(String parent : parents) {
            accessRightChecker.check(userCtx, parent, op);
        }
    }

    @Override
    public void assign(String child, String parent) throws PMException {
        Node childNode = pap.getNode(child);
        Node parentNode = pap.getNode(parent);

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
            default -> throw new IllegalArgumentException("cannot assign to a node of type " + childNode.getType());
        };

        //check the user can assign the child
        accessRightChecker.check(userCtx, child, childAR);

        // check that the user can assign to the parent node
        accessRightChecker.check(userCtx, parent, parentAR);
    }

    @Override
    public void deassign(String child, String parent) throws PMException {
        Node childNode = pap.getNode(child);
        Node parentNode = pap.getNode(parent);

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
            default -> throw new InvalidAssignmentException("cannot deassign from a node of type " + childNode.getType());
        };

        //check the user can deassign the child
        accessRightChecker.check(userCtx, child, childAR);

        // check that the user can deassign from the parent node
        accessRightChecker.check(userCtx, parent, parentAR);
    }

    @Override
    public void associate(String ua, String target, AccessRightSet accessRights) throws PMException {
        Node targetNode = pap.getNode(target);

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
        Node targetNode = pap.getNode(target);

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
    public void createProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        if (subject.type() == ProhibitionSubject.Type.PROCESS) {
            accessRightChecker.check(userCtx, SUPER_PC_REP, CREATE_PROCESS_PROHIBITION);
        } else {
            accessRightChecker.check(userCtx, subject.name(), CREATE_PROHIBITION);
        }


        // check that the user can create a prohibition for each container in the condition
        for (ContainerCondition contCond : containerConditions) {
            accessRightChecker.check(userCtx, contCond.name(), ADD_CONTAINER_TO_PROHIBITION);

            // there is another access right needed if the condition is a complement
            if (contCond.complement()) {
                accessRightChecker.check(userCtx, SUPER_PC_REP, ADD_CONTAINER_COMPLEMENT_TO_PROHIBITION);
            }
        }
    }

    @Override
    public void updateProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        createProhibition(label, subject, accessRightSet, intersection, containerConditions);
    }

    @Override
    public void deleteProhibition(String label) throws PMException {
        if (!prohibitionExists(label)) {
            return;
        }

        Prohibition prohibition = pap.getProhibition(label);

        // check that the user can create a prohibition for the subject
        if (prohibition.getSubject().type() == ProhibitionSubject.Type.PROCESS) {
            accessRightChecker.check(userCtx, SUPER_PC_REP, DELETE_PROCESS_PROHIBITION);
        } else {
            accessRightChecker.check(userCtx, prohibition.getSubject().name(), DELETE_PROHIBITION);
        }

        // check that the user can create a prohibition for each container in the condition
        for (ContainerCondition contCond : prohibition.getContainers()) {
            accessRightChecker.check(userCtx, contCond.name(), REMOVE_CONTAINER_FROM_PROHIBITION);

            // there is another access right needed if the condition is a complement
            if (contCond.complement()) {
                accessRightChecker.check(userCtx, SUPER_PC_REP, REMOVE_CONTAINER_COMPLEMENT_FROM_PROHIBITION);
            }
        }
    }

    @Override
    public void createObligation(UserContext author, String label, Rule... rules) throws PMException {
        for (Rule rule : rules) {
            EventSubject subject = rule.getEvent().getSubject();
            checkSubject(subject, CREATE_OBLIGATION);

            Target target = rule.getEvent().getTarget();
            checkTarget(target, CREATE_OBLIGATION);
        }
    }

    private void checkTarget(Target target, String accessRight) throws PMException {
        if (target.getType() == Target.Type.POLICY_ELEMENT) {
            accessRightChecker.check(userCtx, target.policyElement(), accessRight);
        } else if (target.getType() == Target.Type.ANY_POLICY_ELEMENT) {
            accessRightChecker.check(userCtx, SUPER_PC_REP, accessRight);
        } else if (target.getType() == Target.Type.ANY_CONTAINED_IN) {
            accessRightChecker.check(userCtx, target.anyContainedIn(), accessRight);
        } else if (target.getType() == Target.Type.ANY_OF_SET) {
            for (String policyElement : target.anyOfSet()) {
                accessRightChecker.check(userCtx, policyElement, accessRight);
            }
        }
    }

    private void checkSubject(EventSubject subject, String accessRight) throws PMException {
        if (subject.getType() == EventSubject.Type.ANY_USER) {
            accessRightChecker.check(userCtx, SUPER_PC_REP, accessRight);
        } else if (subject.getType() == EventSubject.Type.ANY_USER_WITH_ATTRIBUTE) {
            accessRightChecker.check(userCtx, subject.anyUserWithAttribute(), accessRight);
        } else if (subject.getType() == EventSubject.Type.PROCESS) {
            // need permissions on super object create a process obligation
            accessRightChecker.check(userCtx, SUPER_PC_REP, accessRight);
        } else if (subject.getType() == EventSubject.Type.USERS) {
            for (String user : subject.users()) {
                accessRightChecker.check(userCtx, user, accessRight);
            }
        }
    }

    @Override
    public void updateObligation(UserContext author, String label, Rule... rules) throws PMException {
        createObligation(author, label, rules);
    }

    @Override
    public void deleteObligation(String label) throws PMException {
        if (!obligationExists(label)) {
            return;
        }

        Obligation obligation = pap.getObligation(label);
        for (Rule rule : obligation.getRules()) {
            EventSubject subject = rule.getEvent().getSubject();
            checkSubject(subject, DELETE_OBLIGATION);

            Target target = rule.getEvent().getTarget();
            checkTarget(target, DELETE_OBLIGATION);
        }
    }

    @Override
    public void addPALFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, ADD_FUNCTION);
    }

    @Override
    public void removePALFunction(String functionName) throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, REMOVE_FUNCTION);
    }

    @Override
    public void addPALConstant(String constantName, Value constantValue) throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, ADD_CONSTANT);
    }

    @Override
    public void removePALConstant(String constName) throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, REMOVE_CONSTANT);
    }
}
