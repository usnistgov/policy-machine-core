package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.PolicyReader;
import gov.nist.csd.pm.policy.PolicySerializable;
import gov.nist.csd.pm.policy.author.*;
import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.PALExecutable;
import gov.nist.csd.pm.policy.author.pal.PALExecutor;
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
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;

public class PAP implements PolicySync, PolicyEventEmitter, Transactional, PolicyAuthor, PolicySerializable, PALExecutable {

    protected PolicyStore policyStore;

    protected List<PolicyEventListener> listeners;

    public PAP(PolicyStore policyStore) throws PMException {
        this.policyStore = policyStore;
        this.listeners = new ArrayList<>();

        SuperPolicy.verifySuperPolicy(this.policyStore);
    }

    @Override
    public synchronized void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
        listeners.add(listener);

        if (sync) {
            listener.handlePolicyEvent(policyStore.policySync());
        }
    }

    @Override
    public synchronized void removeEventListener(PolicyEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public synchronized void emitEvent(PolicyEvent event) throws PMException {
        for (PolicyEventListener listener : listeners) {
            listener.handlePolicyEvent(event);
        }
    }

    @Override
    public synchronized PolicySynchronizationEvent policySync() throws PMException {
        return this.policyStore.policySync();
    }

    @Override
    public synchronized void beginTx() throws PMException {
        policyStore.beginTx();

        emitEvent(new BeginTxEvent());
    }

    @Override
    public synchronized void commit() throws PMException {
        policyStore.commit();

        emitEvent(new CommitTxEvent());
    }

    @Override
    public synchronized void rollback() throws PMException {
        policyStore.rollback();

        emitEvent(new RollbackTxEvent(this));
    }

    @Override
    public synchronized AccessRightSet getResourceAccessRights() throws PMException {
        return policyStore.getResourceAccessRights();
    }

    @Override
    public synchronized boolean nodeExists(String name) throws PMException {
        return policyStore.nodeExists(name);
    }

    @Override
    public synchronized Node getNode(String name) throws PMException {
        if (!nodeExists(name)) {
            throw new NodeDoesNotExistException(name);
        }

        return policyStore.getNode(name);
    }

    @Override
    public synchronized List<String> search(NodeType type, Map<String, String> properties) throws PMException {
        return policyStore.search(type, properties);
    }

    @Override
    public synchronized List<String> getPolicyClasses() throws PMException {
        return policyStore.getPolicyClasses();
    }

    @Override
    public synchronized List<String> getChildren(String node) throws PMException {
        if (!nodeExists(node)) {
            throw new NodeDoesNotExistException(node);
        }

        return policyStore.getChildren(node);
    }

    @Override
    public synchronized List<String> getParents(String node) throws PMException {
        if (!nodeExists(node)) {
            throw new NodeDoesNotExistException(node);
        }

        return policyStore.getParents(node);
    }

    @Override
    public synchronized List<Association> getAssociationsWithSource(String ua) throws PMException {
        if (!nodeExists(ua)) {
            throw new NodeDoesNotExistException(ua);
        }

        return policyStore.getAssociationsWithSource(ua);
    }

    @Override
    public synchronized List<Association> getAssociationsWithTarget(String target) throws PMException {
        if (!nodeExists(target)) {
            throw new NodeDoesNotExistException(target);
        }

        return policyStore.getAssociationsWithTarget(target);
    }

    @Override
    public synchronized Map<String, List<Prohibition>> getProhibitions() throws PMException {
        return policyStore.getProhibitions();
    }

    @Override
    public synchronized List<Prohibition> getProhibitionsWithSubject(String subject) throws PMException {
        return policyStore.getProhibitionsWithSubject(subject);
    }

    @Override
    public synchronized Prohibition getProhibition(String label) throws PMException {
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
    public synchronized List<Obligation> getObligations() throws PMException {
        return policyStore.getObligations();
    }



    @Override
    public synchronized Obligation getObligation(String label) throws PMException {
        if (!obligationExists(label)) {
            throw new ObligationDoesNotExistException(label);
        }

        return policyStore.getObligation(label);
    }

    @Override
    public synchronized Map<String, FunctionDefinitionStatement> getPALFunctions() throws PMException {
        return policyStore.getPALFunctions();
    }

    @Override
    public synchronized Map<String, Value> getPALConstants() throws PMException {
        return policyStore.getPALConstants();
    }

    @Override
    public synchronized PALContext getPALContext() throws PMException {
        return policyStore.getPALContext();
    }

    @Override
    public synchronized void setResourceAccessRights(AccessRightSet accessRightSet) throws PMException {
        for (String ar : accessRightSet) {
            if (isAdminAccessRight(ar) || isWildcardAccessRight(ar)) {
                throw new AdminAccessRightExistsException(ar);
            }
        }

        policyStore.setResourceAccessRights(accessRightSet);

        // notify listeners of policy modification
        emitEvent(new SetResourceAccessRightsEvent(accessRightSet));
    }

    @Override
    public synchronized String createPolicyClass(String name, Map<String, String> properties) throws PMException {
        if (nodeExists(name)) {
            if (SuperPolicy.isSuperPolicyNode(name)) {
                return name;
            }

            throw new NodeNameExistsException(name);
        }

        List<PolicyEvent> events = SuperPolicy.createPolicyClass(policyStore, name, properties);
        for (PolicyEvent event : events) {
            emitEvent(event);
        }

        return name;
    }

    @Override
    public synchronized String createPolicyClass(String name) throws PMException {
        return createPolicyClass(name, noprops());
    }

    @Override
    public synchronized String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, UA, properties, parent, parents);
    }

    @Override
    public synchronized String createUserAttribute(String name, String parent, String... parents) throws PMException {
        return createUserAttribute(name, noprops(), parent, parents);
    }

    @Override
    public synchronized String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, OA, properties, parent, parents);
    }

    @Override
    public synchronized String createObjectAttribute(String name, String parent, String... parents) throws PMException {
        return createObjectAttribute(name, noprops(), parent, parents);
    }

    @Override
    public synchronized String createObject(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, O, properties, parent, parents);
    }

    @Override
    public synchronized String createObject(String name, String parent, String... parents) throws PMException {
        return createObject(name, noprops(), parent, parents);
    }

    @Override
    public synchronized String createUser(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, U, properties, parent, parents);
    }

    @Override
    public synchronized String createUser(String name, String parent, String... parents) throws PMException {
        return createUser(name, noprops(), parent, parents);
    }

    private String createNode(String name, NodeType type, Map<String, String> properties, String parent, String ... parents) throws PMException {
        if (nodeExists(name)) {
            if (SuperPolicy.isSuperPolicyNode(name)) {
                return name;
            }

            throw new NodeNameExistsException(name);
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
            List<PolicyEvent> events = SuperPolicy.assignedToPolicyClass(policyStore, name, pc);
            for (PolicyEvent e : events) {
                emitEvent(e);
            }
        }

        return name;
    }

    @Override
    public synchronized void setNodeProperties(String name, Map<String, String> properties) throws PMException {
        if (!nodeExists(name)) {
            throw new NodeDoesNotExistException(name);
        }

        policyStore.setNodeProperties(name, properties);

        emitEvent(new SetNodePropertiesEvent(name, properties));
    }

    @Override
    public synchronized void deleteNode(String name) throws PMException {
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
    public synchronized void assign(String child, String parent) throws PMException {
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
            List<PolicyEvent> events = SuperPolicy.assignedToPolicyClass(policyStore, child, parent);
            for (PolicyEvent e : events) {
                emitEvent(e);
            }
        }
    }

    @Override
    public synchronized void deassign(String child, String parent) throws PMException {
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
    public synchronized void associate(String ua, String target, AccessRightSet accessRights) throws PMException {
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
    public synchronized void dissociate(String ua, String target) throws PMException {
        if ((!nodeExists(ua) || !nodeExists(target))
            || (!getAssociationsWithSource(ua).contains(new Association(ua, target)))) {
            return;
        }

        policyStore.dissociate(ua, target);

        emitEvent(new DissociateEvent(ua, target));
    }

    @Override
    public synchronized void createProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
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
    public synchronized void updateProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
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
    public boolean prohibitionExists(String label) throws PMException {
        return policyStore.prohibitionExists(label);
    }

    @Override
    public synchronized void deleteProhibition(String label) throws PMException {
        if (!prohibitionExists(label)) {
            return;
        }

        Prohibition prohibition = policyStore.getProhibition(label);

        policyStore.deleteProhibition(label);

        emitEvent(new DeleteProhibitionEvent(prohibition));
    }

    @Override
    public synchronized void createObligation(UserContext author, String label, Rule... rules) throws PMException {
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

    @Override
    public boolean obligationExists(String label) throws PMException {
        return policyStore.obligationExists(label);
    }

    @Override
    public synchronized void updateObligation(UserContext author, String label, Rule... rules) throws PMException {
        if (!obligationExists(label)) {
            throw new ObligationDoesNotExistException(label);
        }

        checkAuthorExists(author);
        checkEventPatternAttributesExist(rules);

        policyStore.updateObligation(author, label, rules);

        emitEvent(new UpdateObligationEvent(author, label, List.of(rules)));
    }

    @Override
    public synchronized void deleteObligation(String label) throws PMException {
        if (!obligationExists(label)) {
            return;
        }

        policyStore.deleteObligation(label);

        emitEvent(new DeleteObligationEvent(label));
    }

    @Override
    public synchronized void addPALFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException {
        if (getPALFunctions().containsKey(functionDefinitionStatement.getFunctionName())) {
            throw new FunctionAlreadyDefinedException(functionDefinitionStatement.getFunctionName());
        }

        policyStore.addPALFunction(functionDefinitionStatement);

        emitEvent(new AddFunctionEvent(functionDefinitionStatement));
    }

    @Override
    public synchronized void removePALFunction(String functionName) throws PMException {
        policyStore.removePALFunction(functionName);

        emitEvent(new RemoveFunctionEvent(functionName));
    }

    @Override
    public synchronized void addPALConstant(String constantName, Value constantValue) throws PMException {
        if (getPALConstants().containsKey(constantName)) {
            throw new ConstantAlreadyDefinedException(constantName);
        }

        policyStore.addPALConstant(constantName, constantValue);

        emitEvent(new AddConstantEvent(constantName, constantValue));
    }

    @Override
    public synchronized void removePALConstant(String constName) throws PMException {
        policyStore.removePALConstant(constName);

        emitEvent(new RemoveConstantEvent(constName));
    }

    @Override
    public synchronized String toString(PolicySerializer policySerializer) throws PMException {
        return policySerializer.serialize(this);
    }

    @Override
    public synchronized void fromString(String s, PolicyDeserializer policyDeserializer) throws PMException {
        beginTx();

        // reset policy store
        policyStore.reset();

        // verify super policy
        SuperPolicy.verifySuperPolicy(this.policyStore);

        // deserialize using deserializer
        policyDeserializer.deserialize(this, s);

        commit();
    }

    @Override
    public void executePAL(UserContext userContext, String input, FunctionDefinitionStatement... functionDefinitionStatements) throws PMException {
        PALExecutor.compileAndExecutePAL(this, userContext, input, functionDefinitionStatements);
    }
}
