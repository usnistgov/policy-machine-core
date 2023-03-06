package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.PolicyReader;
import gov.nist.csd.pm.policy.author.*;
import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Assignment;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.serializer.PolicyDeserializer;
import gov.nist.csd.pm.policy.serializer.PolicySerializer;
import gov.nist.csd.pm.policy.tx.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.SuperPolicy.pcRepObjectAttribute;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.allAdminAccessRights;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.wildcardAccessRights;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;

public class PAP implements PolicySync, PolicyEventEmitter, Transactional, PolicyAuthor {

    protected PolicyStore policyStore;

    protected List<PolicyEventListener> listeners;

    public PAP(PolicyStore policyStore) throws PMException {
        init(policyStore, true);
    }

    protected void init(PolicyStore policyStore, boolean verifySuperPolicy) throws PMException {
        this.policyStore = policyStore;
        this.listeners = new ArrayList<>();

        if (verifySuperPolicy) {
            SuperPolicy.verifySuperPolicy(this.policyStore);
        }
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
        listeners.add(listener);

        if (sync) {
            listener.handlePolicyEvent(policyStore.policySync());
        }
    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        for (PolicyEventListener listener : listeners) {
            listener.handlePolicyEvent(event);
        }
    }

    @Override
    public PolicySynchronizationEvent policySync() throws PMException {
        return this.policyStore.policySync();
    }

    @Override
    public void beginTx() throws PMException {
        policyStore.beginTx();

        emitEvent(new BeginTxEvent());
    }

    @Override
    public void commit() throws PMException {
        policyStore.commit();

        emitEvent(new CommitTxEvent());
    }

    @Override
    public void rollback() throws PMException {
        policyStore.rollback();

        emitEvent(new RollbackTxEvent(this));
    }

    @Override
    public AccessRightSet getResourceAccessRights() throws PMException {
        return policyStore.getResourceAccessRights();
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        return policyStore.nodeExists(name);
    }

    @Override
    public Node getNode(String name) throws PMException {
        if (!nodeExists(name)) {
            throw new NodeDoesNotExistException(name);
        }

        return policyStore.getNode(name);
    }

    @Override
    public List<String> search(NodeType type, Map<String, String> properties) throws PMException {
        return policyStore.search(type, properties);
    }

    @Override
    public List<String> getPolicyClasses() throws PMException {
        return policyStore.getPolicyClasses();
    }

    @Override
    public List<String> getChildren(String node) throws PMException {
        if (!nodeExists(node)) {
            throw new NodeDoesNotExistException(node);
        }

        return policyStore.getChildren(node);
    }

    @Override
    public List<String> getParents(String node) throws PMException {
        if (!nodeExists(node)) {
            throw new NodeDoesNotExistException(node);
        }

        return policyStore.getParents(node);
    }

    @Override
    public List<Association> getAssociationsWithSource(String ua) throws PMException {
        if (!nodeExists(ua)) {
            throw new NodeDoesNotExistException(ua);
        }

        return policyStore.getAssociationsWithSource(ua);
    }

    @Override
    public List<Association> getAssociationsWithTarget(String target) throws PMException {
        if (!nodeExists(target)) {
            throw new NodeDoesNotExistException(target);
        }

        return policyStore.getAssociationsWithTarget(target);
    }

    @Override
    public Map<String, List<Prohibition>> getProhibitions() throws PMException {
        return policyStore.getProhibitions();
    }

    @Override
    public List<Prohibition> getProhibitionsWithSubject(String subject) throws PMException {
        return policyStore.getProhibitionsWithSubject(subject);
    }

    @Override
    public Prohibition getProhibition(String label) throws PMException {
        Prohibition prohibition = getProhibitionOrNull(label);
        if (prohibition == null) {
            throw new ProhibitionDoesNotExistException(label);
        }

        return prohibition;
    }

    private Prohibition getProhibitionOrNull(String label) throws PMException {
        for (List<Prohibition> prohibitions : policyStore.getProhibitions().values()) {
            for (Prohibition p : prohibitions) {
                if (p.getLabel().equals(label)) {
                    return p;
                }
            }
        }

        return null;
    }

    @Override
    public List<Obligation> getObligations() throws PMException {
        return policyStore.getObligations();
    }

    @Override
    public Obligation getObligation(String label) throws PMException {
        if (!obligationExists(label)) {
            throw new ObligationDoesNotExistException(label);
        }

        return policyStore.getObligation(label);
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getPALFunctions() throws PMException {
        return policyStore.getPALFunctions();
    }

    @Override
    public Map<String, Value> getPALConstants() throws PMException {
        return policyStore.getPALConstants();
    }

    @Override
    public PALContext getPALContext() throws PMException {
        return policyStore.getPALContext();
    }

    @Override
    public void setResourceAccessRights(AccessRightSet accessRightSet) throws PMException {
        policyStore.setResourceAccessRights(accessRightSet);
    }

    @Override
    public String createPolicyClass(String name, Map<String, String> properties) throws PMException {
        if (nodeExists(name)) {
            throw new NodeNameExistsException(name);
        }

        List<PolicyEvent> events = SuperPolicy.createPolicyClass(policyStore, name, properties);
        for (PolicyEvent event : events) {
            emitEvent(event);
        }

        return name;
    }

    @Override
    public String createPolicyClass(String name) throws PMException {
        return createPolicyClass(name, noprops());
    }

    @Override
    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, UA, properties, parent, parents);
    }

    @Override
    public String createUserAttribute(String name, String parent, String... parents) throws PMException {
        return createUserAttribute(name, noprops(), parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, OA, properties, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, String parent, String... parents) throws PMException {
        return createObjectAttribute(name, noprops(), parent, parents);
    }

    @Override
    public String createObject(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, O, properties, parent, parents);
    }

    @Override
    public String createObject(String name, String parent, String... parents) throws PMException {
        return createObject(name, noprops(), parent, parents);
    }

    @Override
    public String createUser(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, U, properties, parent, parents);
    }

    @Override
    public String createUser(String name, String parent, String... parents) throws PMException {
        return createUser(name, noprops(), parent, parents);
    }

    private String createNode(String name, NodeType type, Map<String, String> properties, String parent, String ... parents) throws PMException {
        if (nodeExists(name)) {
            throw new NodeNameExistsException(name);
        } else if (!nodeExists(parent)) {
            throw new NodeDoesNotExistException(parent);
        }

        // collect any parents that are of type PC
        // this will also check if the nodes exist by calling getNode
        List<String> pcParents = new ArrayList<>();
        List<String> parentsList = new ArrayList<>(Arrays.asList(parents));
        parentsList.add(parent);
        for (String p : parentsList) {
            Node parentNode = getNode(p);
            if (parentNode.getType() != PC) {
                continue;
            }

            pcParents.add(p);
        }

        switch (type) {
            case OA -> {
                policyStore.createObjectAttribute(name, properties, parent, parents);
                emitEvent(new CreateObjectAttributeEvent(name, properties, parent, parents));
            }
            case UA -> {
                policyStore.createUserAttribute(name, properties, parent, parents);
                emitEvent(new CreateUserAttributeEvent(name, properties, parent, parents));
            }
            case O -> {
                policyStore.createObject(name, properties, parent, parents);
                emitEvent(new CreateObjectEvent(name, properties, parent, parents));
            }
            case U -> {
                policyStore.createUser(name, properties, parent, parents);
                emitEvent(new CreateUserEvent(name, properties, parent, parents));
            }
            default -> { /* PC and ANY should not ever be passed to this private method */ }
        }

        // for any pc parents, create any necessary super policy configurations
        for (String pc : pcParents) {
            SuperPolicy.assignedToPolicyClass(policyStore, name, pc);
        }

        return name;
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties) throws PMException {
        if (!nodeExists(name)) {
            throw new NodeDoesNotExistException(name);
        }

        policyStore.setNodeProperties(name, properties);

        emitEvent(new SetNodePropertiesEvent(name, properties));
    }

    @Override
    public void deleteNode(String name) throws PMException {
        if (!nodeExists(name)) {
            return;
        }

        List<String> children = getChildren(name);
        if (!children.isEmpty()) {
            throw new NodeHasChildrenException(name);
        }

        checkIfNodeInProhibition(name);
        checkIfNodeInObligation(name);

        NodeType type = getNode(name).getType();

        // delete the rep node if node is a PC
        policyStore.beginTx();

        if (type == PC) {
            String rep = pcRepObjectAttribute(name);
            policyStore.deleteNode(rep);
            emitEvent(new DeleteNodeEvent(rep));
        }

        policyStore.deleteNode(name);
        emitEvent(new DeleteNodeEvent(name));

        policyStore.commit();
    }

    private void checkIfNodeInProhibition(String name) throws PMException {
        Map<String, List<Prohibition>> prohibitions = policyStore.getProhibitions();
        for (List<Prohibition> subjPros : prohibitions.values()) {
            for (Prohibition p : subjPros) {
                if (nodeInProhibition(name, p)) {
                    throw new NodeReferencedInProhibitionException(name, p.getLabel());
                }
            }
        }
    }

    private boolean nodeInProhibition(String name, Prohibition prohibition) {
        if (prohibition.getSubject().name().equals(name)) {
            return true;
        }

        for (ContainerCondition containerCondition : prohibition.getContainers()) {
            if (containerCondition.name().equals(name)) {
                return true;
            }
        }

        return false;
    }

    private void checkIfNodeInObligation(String name) throws PMException {
        List<Obligation> obligations = policyStore.getObligations();
        for (Obligation obligation : obligations) {
            // if the node is the author of the obligation or referenced in any rules throw an exception
            if (obligation.getAuthor().getUser().equals(name)
                || nodeInObligation(name, obligation)) {
                throw new NodeReferencedInObligationException(name, obligation.getLabel());
            }
        }
    }

    private boolean nodeInObligation(String name, Obligation obligation) {
        for (Rule rule : obligation.getRules()) {
            if (nodeInEvent(name, rule.getEvent())) {
                return true;
            }
        }

        return false;
    }

    private boolean nodeInEvent(String name, EventPattern event) {
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

    @Override
    public void assign(String child, String parent) throws PMException {
        Node childNode = getNode(child);
        Node parentNode = getNode(parent);

        // ignore if assignment already exists
        if (getParents(child).contains(parent)) {
            return;
        }

        // check node types make a valid assignment relation
        Assignment.checkAssignment(childNode.getType(), parentNode.getType());

        policyStore.assign(child, parent);
        emitEvent(new AssignEvent(child, parent));

        // if the parent is a policy class, need to associate the super ua with the child
        if (parentNode.getType() == PC) {
            SuperPolicy.assignedToPolicyClass(policyStore, child, parent);
        }
    }

    @Override
    public void deassign(String child, String parent) throws PMException {
        if ((!nodeExists(child) || !nodeExists(parent))
            || (!getParents(child).contains(parent))) {
            return;
        }

        List<String> parents = policyStore.getParents(child);
        if (parents.size() == 1) {
            throw new DisconnectedNodeException(child, parent);
        }

        policyStore.deassign(child, parent);

        emitEvent(new DeassignEvent(child, parent));
    }

    @Override
    public void associate(String ua, String target, AccessRightSet accessRights) throws PMException {
        Node uaNode = getNode(ua);
        Node targetNode = getNode(target);

        // check the access rights are valid
        checkAccessRightsValid(policyStore, accessRights);

        // check the types of each node make a valid association
        Association.checkAssociation(uaNode.getType(), targetNode.getType());

        // associate and emit event
        policyStore.associate(ua, target, accessRights);
        emitEvent(new AssociateEvent(ua, target, accessRights));
    }

    static void checkAccessRightsValid(PolicyReader policyReader, AccessRightSet accessRightSet) throws PMException {
        AccessRightSet resourceAccessRights = policyReader.getResourceAccessRights();

        for (String ar : accessRightSet) {
            if (!resourceAccessRights.contains(ar)
                && !allAdminAccessRights().contains(ar)
                && !wildcardAccessRights().contains(ar)) {
                throw new UnknownAccessRightException(ar);
            }
        }
    }

    @Override
    public void dissociate(String ua, String target) throws PMException {
        if ((!nodeExists(ua) || !nodeExists(target))
            || (!getAssociationsWithSource(ua).contains(new Association(ua, target)))) {
            return;
        }

        policyStore.dissociate(ua, target);

        emitEvent(new DissociateEvent(ua, target));
    }

    @Override
    public void createProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        if (prohibitionExists(label)) {
            throw new ProhibitionExistsException(label);
        }

        checkProhibitionParameters(subject, accessRightSet, containerConditions);

        policyStore.createProhibition(label, subject, accessRightSet, intersection, containerConditions);

        emitEvent(new CreateProhibitionEvent(
                label, subject, accessRightSet, intersection, List.of(containerConditions)
        ));
    }

    @Override
    public void updateProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        if (!prohibitionExists(label)) {
            throw new ProhibitionDoesNotExistException(label);
        }

        checkProhibitionParameters(subject, accessRightSet, containerConditions);

        policyStore.updateProhibition(label, subject, accessRightSet, intersection, containerConditions);

        emitEvent(new UpdateProhibitionEvent(
                label, subject, accessRightSet, intersection, List.of(containerConditions))
        );
    }

    private void checkProhibitionParameters(ProhibitionSubject subject, AccessRightSet accessRightSet, ContainerCondition ... containerConditions) throws PMException {
        checkAccessRightsValid(policyStore, accessRightSet);
        checkProhibitionSubjectExists(subject);
        checkProhibitionContainersExist(containerConditions);
    }

    private void checkProhibitionSubjectExists(ProhibitionSubject subject) throws PMException {
        if (subject.type() != ProhibitionSubject.Type.PROCESS) {
            if (!policyStore.nodeExists(subject.name())) {
                throw new ProhibitionSubjectDoesNotExistException(subject.name());
            }
        }
    }

    private void checkProhibitionContainersExist(ContainerCondition ... containerConditions) throws PMException {
        for (ContainerCondition container : containerConditions) {
            if (!policyStore.nodeExists(container.name())) {
                throw new ProhibitionContainerDoesNotExistException(container.name());
            }
        }
    }

    @Override
    public void deleteProhibition(String label) throws PMException {
        if (!prohibitionExists(label)) {
            return;
        }

        Prohibition prohibition = policyStore.getProhibition(label);

        policyStore.deleteProhibition(label);

        emitEvent(new DeleteProhibitionEvent(prohibition));
    }

    private boolean prohibitionExists(String label) throws PMException {
        return getProhibitionOrNull(label) != null;
    }

    @Override
    public void createObligation(UserContext author, String label, Rule... rules) throws PMException {
        if (obligationExists(label)) {
            throw new ObligationExistsException(label);
        }

        checkAuthorExists(author);
        checkEventPatternAttributesExist(rules);

        policyStore.createObligation(author, label, rules);

        emitEvent(new CreateObligationEvent(author, label, List.of(rules)));
    }

    private void checkAuthorExists(UserContext author) throws PMException {
        if (!policyStore.nodeExists(author.getUser())) {
            throw new NodeDoesNotExistException(author.getUser());
        }
    }

    private void checkEventPatternAttributesExist(Rule ... rules) throws PMException {
        for (Rule rule : rules) {
            EventPattern event = rule.getEvent();

            // check subject
            EventSubject subject = event.getSubject();
            switch (subject.getType()) {
                case USERS -> {
                    for (String user : subject.users()) {
                        if (!policyStore.nodeExists(user)) {
                            throw new NodeDoesNotExistException(user);
                        }
                    }
                }
                case ANY_USER_WITH_ATTRIBUTE -> {
                    if (!policyStore.nodeExists(subject.anyUserWithAttribute())) {
                        throw new NodeDoesNotExistException(subject.anyUserWithAttribute());
                    }
                }
            }

            // check target
            Target target = event.getTarget();
            switch (target.getType()) {
                case ANY_OF_SET -> {
                    for (String pe : target.anyOfSet()) {
                        if (!policyStore.nodeExists(pe)) {
                            throw new NodeDoesNotExistException(pe);
                        }
                    }
                }
                case POLICY_ELEMENT -> {
                    if (!policyStore.nodeExists(target.policyElement())) {
                        throw new NodeDoesNotExistException(target.policyElement());
                    }
                }
                case ANY_CONTAINED_IN -> {
                    if (!policyStore.nodeExists(target.anyContainedIn())) {
                        throw new NodeDoesNotExistException(target.anyContainedIn());
                    }
                }
            }
        }
    }

    private boolean obligationExists(String label) throws PMException {
        return getObligationOrNull(label) != null;
    }

    private Obligation getObligationOrNull(String label) throws PMException {
        for (Obligation obligation : policyStore.getObligations()) {
            if (obligation.getLabel().equals(label)) {
                return obligation;
            }
        }

        return null;
    }

    @Override
    public void updateObligation(UserContext author, String label, Rule... rules) throws PMException {
        if (!obligationExists(label)) {
            throw new ObligationDoesNotExistException(label);
        }

        checkAuthorExists(author);
        checkEventPatternAttributesExist(rules);

        policyStore.updateObligation(author, label, rules);

        emitEvent(new UpdateObligationEvent(author, label, List.of(rules)));
    }

    @Override
    public void deleteObligation(String label) throws PMException {
        if (!obligationExists(label)) {
            return;
        }

        policyStore.deleteObligation(label);

        emitEvent(new DeleteObligationEvent(label));
    }

    @Override
    public void addPALFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException {
        if (getPALFunctions().containsKey(functionDefinitionStatement.getFunctionName())) {
            throw new FunctionAlreadyDefinedException(functionDefinitionStatement.getFunctionName());
        }

        policyStore.addPALFunction(functionDefinitionStatement);

        emitEvent(new AddFunctionEvent(functionDefinitionStatement));
    }

    @Override
    public void removePALFunction(String functionName) throws PMException {
        policyStore.removePALFunction(functionName);

        emitEvent(new RemoveFunctionEvent(functionName));
    }

    @Override
    public void addPALConstant(String constantName, Value constantValue) throws PMException {
        if (getPALConstants().containsKey(constantName)) {
            throw new ConstantAlreadyDefinedException(constantName);
        }

        policyStore.addPALConstant(constantName, constantValue);

        emitEvent(new AddConstantEvent(constantName, constantValue));
    }

    @Override
    public void removePALConstant(String constName) throws PMException {
        policyStore.removePALConstant(constName);

        emitEvent(new RemoveConstantEvent(constName));
    }

    @Override
    public String toString(PolicySerializer policySerializer) throws PMException {
        return policyStore.toString(policySerializer);
    }

    @Override
    public void fromString(String s, PolicyDeserializer policyDeserializer) throws PMException {
        policyStore.fromString(s, policyDeserializer);
        init(policyStore, true);
    }
}
