package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.adjudicator.Adjudicator;
import gov.nist.csd.pm.policy.PolicySerializable;
import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.PALExecutable;
import gov.nist.csd.pm.policy.author.pal.PALExecutor;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
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
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static gov.nist.csd.pm.policy.model.obligation.event.EventSubject.Type.*;
import static gov.nist.csd.pm.policy.model.obligation.event.EventSubject.Type.PROCESS;
import static gov.nist.csd.pm.policy.model.obligation.event.Target.Type.*;
import static gov.nist.csd.pm.policy.model.obligation.event.Target.Type.ANY_OF_SET;

public abstract class PDP implements PolicyEventEmitter {

    protected final PAP pap;
    protected final List<PolicyEventListener> eventListeners;

    protected PDP(PAP pap) {
        this.pap = pap;
        this.eventListeners = new ArrayList<>();
    }

    public abstract PolicyReviewer policyReviewer() throws PMException;

    public abstract void runTx(UserContext userCtx, PDPTxRunner txRunner) throws PMException;

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
        eventListeners.add(listener);

        if (sync) {
            listener.handlePolicyEvent(pap.policySync());
        }
    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {
        eventListeners.remove(listener);
    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        for (PolicyEventListener listener : eventListeners) {
            listener.handlePolicyEvent(event);
        }
    }

    public interface PDPTxRunner {
        void run(PDPTx policy) throws PMException;
    }

    public static class PDPTx implements PolicyAuthor, PolicyEventEmitter, PolicySerializable, PALExecutable {

        private final UserContext userCtx;
        private final Adjudicator adjudicator;
        private final PAP pap;
        private List<PolicyEventListener> epps;

        public PDPTx(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer, List<PolicyEventListener> epps) {
            this.userCtx = userCtx;
            this.adjudicator = new Adjudicator(userCtx, pap, policyReviewer);
            this.pap = pap;
            this.epps = epps;
        }

        @Override
        public String toString(PolicySerializer policySerializer) throws PMException {
            adjudicator.toString(policySerializer);

            return pap.toString(policySerializer);
        }

        @Override
        public void fromString(String s, PolicyDeserializer policyDeserializer) throws PMException {
            adjudicator.fromString(s, policyDeserializer);

            pap.fromString(s, policyDeserializer);
        }

        @Override
        public AccessRightSet getResourceAccessRights() throws PMException {
            return pap.getResourceAccessRights();
        }

        @Override
        public boolean nodeExists(String name) throws PMException {
            return adjudicator.nodeExists(name);
        }

        @Override
        public Node getNode(String name) throws PMException {
            return adjudicator.getNode(name);
        }

        @Override
        public List<String> search(NodeType type, Map<String, String> properties) throws PMException {
            return adjudicator.search(type, properties);
        }

        @Override
        public List<String> getPolicyClasses() throws PMException {
            return pap.getPolicyClasses();
        }

        @Override
        public List<String> getChildren(String node) throws PMException {
            return adjudicator.getChildren(node);
        }

        @Override
        public List<String> getParents(String node) throws PMException {
            return adjudicator.getParents(node);
        }

        @Override
        public List<Association> getAssociationsWithSource(String ua) throws PMException {
            return adjudicator.getAssociationsWithSource(ua);
        }

        @Override
        public List<Association> getAssociationsWithTarget(String target) throws PMException {
            return adjudicator.getAssociationsWithTarget(target);
        }

        @Override
        public Map<String, List<Prohibition>> getProhibitions() throws PMException {
            return adjudicator.getProhibitions();
        }

        @Override
        public List<Prohibition> getProhibitionsWithSubject(String subject) throws PMException {
            return adjudicator.getProhibitionsWithSubject(subject);
        }

        @Override
        public Prohibition getProhibition(String label) throws PMException {
            return adjudicator.getProhibition(label);
        }

        @Override
        public List<Obligation> getObligations() throws PMException {
            return adjudicator.getObligations();
        }

        @Override
        public Obligation getObligation(String label) throws PMException {
            return adjudicator.getObligation(label);
        }

        @Override
        public Map<String, FunctionDefinitionStatement> getPALFunctions() throws PMException {
            return pap.getPALFunctions();
        }

        @Override
        public Map<String, Value> getPALConstants() throws PMException {
            return pap.getPALConstants();
        }

        @Override
        public PALContext getPALContext() throws PMException {
            return pap.getPALContext();
        }

        @Override
        public void setResourceAccessRights(AccessRightSet accessRightSet) throws PMException {
            adjudicator.setResourceAccessRights(accessRightSet);

            pap.setResourceAccessRights(accessRightSet);
        }

        @Override
        public String createPolicyClass(String name, Map<String, String> properties) throws PMException {
            adjudicator.createPolicyClass(name, properties);

            pap.createPolicyClass(name, properties);

            emitEvent(new EventContext(userCtx, name, new CreatePolicyClassEvent(name, new HashMap<>())));

            return name;
        }

        @Override
        public String createPolicyClass(String name) throws PMException {
            return createPolicyClass(name, noprops());
        }

        @Override
        public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
            adjudicator.createUserAttribute(name, properties, parent, parents);

            pap.createUserAttribute(name, properties, parent, parents);

            CreateUserAttributeEvent event =
                    new CreateUserAttributeEvent(name, new HashMap<>(), parent, parents);

            emitCreateNodeEvent(event, name, parent, parents);

            return name;
        }

        @Override
        public String createUserAttribute(String name, String parent, String... parents) throws PMException {
            return createUserAttribute(name, noprops(), parent, parents);
        }

        @Override
        public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
            adjudicator.createObjectAttribute(name, properties, parent, parents);

            pap.createObjectAttribute(name, properties, parent, parents);

            CreateObjectAttributeEvent event =
                    new CreateObjectAttributeEvent(name, new HashMap<>(), parent, parents);

            emitCreateNodeEvent(event, name, parent, parents);


            return name;        }

        @Override
        public String createObjectAttribute(String name, String parent, String... parents) throws PMException {
            return createObjectAttribute(name, noprops(), parent, parents);
        }

        @Override
        public String createObject(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
            adjudicator.createObject(name, properties, parent, parents);

            pap.createObject(name, properties, parent, parents);

            CreateObjectEvent event =
                    new CreateObjectEvent(name, new HashMap<>(), parent, parents);

            emitCreateNodeEvent(event, name, parent, parents);

            return name;        }

        @Override
        public String createObject(String name, String parent, String... parents) throws PMException {
            return createObject(name, noprops(), parent, parents);
        }

        @Override
        public String createUser(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
            adjudicator.createUser(name, properties, parent, parents);

            pap.createUser(name, properties, parent, parents);

            CreateUserEvent event = new CreateUserEvent(name, new HashMap<>(), parent, parents);

            emitCreateNodeEvent(event, name, parent, parents);

            return name;
        }

        @Override
        public String createUser(String name, String parent, String... parents) throws PMException {
            return createUser(name, noprops(), parent, parents);
        }

        private void emitCreateNodeEvent(PolicyEvent event, String name, String parent, String ... parents) throws PMException {
            // emit event for the new node
            emitEvent(new EventContext(userCtx, name, event));

            // emit event for creating a node in a parent
            emitEvent(new EventContext(userCtx, parent, event));

            // do the same for any additional parents
            for (String p : parents) {
                emitEvent(new EventContext(userCtx, p, event));
            }
        }

        @Override
        public void setNodeProperties(String name, Map<String, String> properties) throws PMException {
            adjudicator.setNodeProperties(name, properties);

            pap.setNodeProperties(name, properties);

            emitEvent(new EventContext(userCtx, name,
                    new SetNodePropertiesEvent(name, properties)));
        }

        @Override
        public void deleteNode(String name) throws PMException {
            adjudicator.deleteNode(name);

            // get parents of the deleted node before deleting to process event in the EPP
            List<String> parents = getParents(name);

            pap.deleteNode(name);

            emitDeleteNodeEvent(new DeleteNodeEvent(name), name, parents);
        }

        private void emitDeleteNodeEvent(PolicyEvent event, String name, List<String> parents) throws PMException {
            // emit delete node event on the deleted node
            emitEvent(new EventContext(userCtx, name, event));

            // emit delete node on each parent
            for (String parent : parents) {
                emitEvent(new EventContext(userCtx, parent, event));
            }
        }

        @Override
        public void assign(String child, String parent) throws PMException {
            adjudicator.assign(child, parent);

            pap.assign(child, parent);

            emitEvent(new EventContext(userCtx, child,
                    new AssignEvent(child, parent)));
            emitEvent(new EventContext(userCtx, parent,
                    new AssignToEvent(child, parent)));
        }

        @Override
        public void deassign(String child, String parent) throws PMException {
            adjudicator.deassign(child, parent);

            pap.deassign(child, parent);

            emitEvent(new EventContext(userCtx, child,
                    new DeassignEvent(child, parent)));
            emitEvent(new EventContext(userCtx, parent,
                    new DeassignFromEvent(child, parent)));
        }

        @Override
        public void associate(String ua, String target, AccessRightSet accessRights) throws PMException {
            adjudicator.associate(ua, target, accessRights);

            pap.associate(ua, target, accessRights);

            emitEvent(new EventContext(userCtx, ua,
                    new AssociateEvent(ua, target, accessRights)));
            emitEvent(new EventContext(userCtx, target,
                    new AssociateEvent(ua, target, accessRights)));
        }

        @Override
        public void dissociate(String ua, String target) throws PMException {
            adjudicator.dissociate(ua, target);

            pap.dissociate(ua, target);

            emitEvent(new EventContext(userCtx, ua,
                    new DissociateEvent(ua, target)));
            emitEvent(new EventContext(userCtx, target,
                    new DissociateEvent(ua, target)));
        }

        @Override
        public void createProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
            adjudicator.createProhibition(label, subject, accessRightSet, intersection, containerConditions);

            pap.createProhibition(label, subject, accessRightSet, intersection, containerConditions);

            CreateProhibitionEvent createProhibitionEvent = new CreateProhibitionEvent(
                    label, subject, accessRightSet, intersection, List.of(containerConditions)
            );

            // emit event for subject
            emitEvent(new EventContext(userCtx, subject.name(), createProhibitionEvent));

            // emit event for each container specified
            for (ContainerCondition containerCondition : containerConditions) {
                emitEvent(new EventContext(userCtx, containerCondition.name(), createProhibitionEvent));
            }
        }

        @Override
        public void updateProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
            adjudicator.updateProhibition(label, subject, accessRightSet, intersection, containerConditions);

            pap.updateProhibition(label, subject, accessRightSet, intersection, containerConditions);

            UpdateProhibitionEvent updateProhibitionEvent = new UpdateProhibitionEvent(
                    label, subject, accessRightSet, intersection, List.of(containerConditions)
            );

            // emit event for subject
            emitEvent(new EventContext(userCtx, subject.name(), updateProhibitionEvent));

            // emit event for each container specified
            for (ContainerCondition containerCondition : containerConditions) {
                emitEvent(new EventContext(userCtx, containerCondition.name(), updateProhibitionEvent));
            }
        }

        @Override
        public void deleteProhibition(String label) throws PMException {
            adjudicator.deleteProhibition(label);

            pap.deleteProhibition(label);

            emitDeleteProhibitionEvent(label);
        }

        private void emitDeleteProhibitionEvent(String label) throws PMException {
            Prohibition prohibition;
            try {
                prohibition = pap.getProhibition(label);
            } catch (PMException e) {
                throw new PMException("error getting prohibition " + label + ": " + e.getMessage());
            }

            ProhibitionSubject subject = prohibition.getSubject();
            List<ContainerCondition> containerConditions = prohibition.getContainers();

            DeleteProhibitionEvent deleteProhibitionEvent = new DeleteProhibitionEvent(prohibition);

            // emit event for subject
            emitEvent(new EventContext(userCtx, subject.name(), deleteProhibitionEvent));

            // emit event for each container specified
            for (ContainerCondition containerCondition : containerConditions) {
                emitEvent(new EventContext(userCtx, containerCondition.name(), deleteProhibitionEvent));
            }
        }

        @Override
        public void createObligation(UserContext author, String label, Rule... rules) throws PMException {
            adjudicator.createObligation(author, label, rules);

            pap.createObligation(author, label, rules);

            emitObligationEvent(new CreateObligationEvent(author, label, List.of(rules)), rules);
        }

        private void emitObligationEvent(PolicyEvent event, Rule... rules) throws PMException {
            // emit events for each rule
            for (Rule rule : rules) {
                // emit event for the subject
                EventSubject subject = rule.getEvent().getSubject();
                if (subject.getType() == ANY_USER) {
                    emitEvent(new EventContext(userCtx, "", event));
                } else if (subject.getType() == ANY_USER_WITH_ATTRIBUTE) {
                    emitEvent(new EventContext(userCtx, subject.anyUserWithAttribute(), event));
                } else if (subject.getType() == USERS) {
                    for (String user : subject.users()) {
                        emitEvent(new EventContext(userCtx, user, event));
                    }
                } else if (subject.getType() == PROCESS) {
                    emitEvent(new EventContext(userCtx, subject.process(), event));
                }

                // emit event for each target
                Target target = rule.getEvent().getTarget();
                if (target.getType() == POLICY_ELEMENT) {
                    emitEvent(new EventContext(userCtx, target.policyElement(), event));
                } else if (target.getType() == ANY_POLICY_ELEMENT) {
                    emitEvent(new EventContext(userCtx, "", event));
                } else if (target.getType() == ANY_CONTAINED_IN) {
                    emitEvent(new EventContext(userCtx, target.anyContainedIn(), event));
                } else if (target.getType() == ANY_OF_SET) {
                    for (String policyElement : target.anyOfSet()) {
                        emitEvent(new EventContext(userCtx, policyElement, event));
                    }
                }
            }
        }

        @Override
        public void updateObligation(UserContext author, String label, Rule... rules) throws PMException {
            adjudicator.updateObligation(author, label, rules);

            pap.updateObligation(author, label, rules);

            emitObligationEvent(
                    new UpdateObligationEvent(author, label, List.of(rules)),
                    rules
            );
        }

        @Override
        public void deleteObligation(String label) throws PMException {
            adjudicator.deleteObligation(label);

            // get the obligation to use in the EPP before it is deleted
            Obligation obligation = getObligation(label);

            pap.deleteObligation(label);

            emitDeleteObligationEvent(label, obligation);
        }

        private void emitDeleteObligationEvent(String label, Obligation obligation) throws PMException {
            emitObligationEvent(
                    new DeleteObligationEvent(label),
                    obligation.getRules().toArray(Rule[]::new)
            );
        }

        @Override
        public void addPALFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException {
            adjudicator.addPALFunction(functionDefinitionStatement);

            pap.addPALFunction(functionDefinitionStatement);

            emitEvent(new EventContext(userCtx, new AddFunctionEvent(functionDefinitionStatement)));
        }

        @Override
        public void removePALFunction(String functionName) throws PMException {
            adjudicator.removePALFunction(functionName);

            pap.removePALFunction(functionName);

            emitEvent(new EventContext(userCtx, new RemoveFunctionEvent(functionName)));
        }

        @Override
        public void addPALConstant(String constantName, Value constantValue) throws PMException {
            adjudicator.addPALConstant(constantName, constantValue);

            pap.addPALConstant(constantName, constantValue);

            emitEvent(new EventContext(userCtx, new AddConstantEvent(constantName, constantValue)));
        }

        @Override
        public void removePALConstant(String constName) throws PMException {
            adjudicator.removePALConstant(constName);

            pap.removePALConstant(constName);

            emitEvent(new EventContext(userCtx, new RemoveConstantEvent(constName)));
        }

        @Override
        public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
            epps.add(listener);
            
            if (sync) {
                listener.handlePolicyEvent(pap.policySync());
            }
        }

        @Override
        public void removeEventListener(PolicyEventListener listener) {
            epps.remove(listener);
        }

        @Override
        public void emitEvent(PolicyEvent event) throws PMException {
            for (PolicyEventListener epp : epps) {
                epp.handlePolicyEvent(event);
            }
        }

        @Override
        public void executePAL(UserContext userContext, String input, FunctionDefinitionStatement... functionDefinitionStatements) throws PMException {
            // TODO does not use pap
            PALExecutor.compileAndExecutePAL(this, userContext, input, functionDefinitionStatements);
        }
    }
}
