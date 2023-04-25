package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.adjudicator.Adjudicator;
import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.pml.PMLContext;
import gov.nist.csd.pm.policy.pml.PMLExecutable;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
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
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;
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

    public abstract PolicyReviewer reviewer() throws PMException;

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

    public static class PDPTx implements PolicyEventEmitter, PolicyEventListener, PolicySerializable, PMLExecutable, Policy {

        private final UserContext userCtx;
        private final Adjudicator adjudicator;
        private final PAP pap;
        private List<PolicyEventListener> epps;

        private PDPGraph pdpGraph;
        private PDPProhibitions pdpProhibitions;
        private PDPObligations pdpObligations;
        private PDPUserDefinedPML pdpUserDefinedPML;

        public PDPTx(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer, List<PolicyEventListener> epps) throws PMException {
            this.userCtx = userCtx;
            this.adjudicator = new Adjudicator(userCtx, pap, policyReviewer);
            this.pap = pap;
            this.epps = epps;

            this.pdpGraph = new PDPGraph(userCtx, adjudicator.graph(), pap, this);
            this.pdpProhibitions = new PDPProhibitions(userCtx, adjudicator.prohibitions(), pap, this);
            this.pdpObligations = new PDPObligations(userCtx, adjudicator.obligations(), pap, this);
            this.pdpUserDefinedPML = new PDPUserDefinedPML(userCtx, adjudicator.userDefinedPML(), pap, this);
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
        public void executePML(UserContext userContext, String input, FunctionDefinitionStatement... functionDefinitionStatements) throws PMException {
            PMLExecutor.compileAndExecutePML(this, userContext, input, functionDefinitionStatements);
        }

        @Override
        public Graph graph() {
            return pdpGraph;
        }

        @Override
        public Prohibitions prohibitions() {
            return pdpProhibitions;
        }

        @Override
        public Obligations obligations() {
            return pdpObligations;
        }

        @Override
        public UserDefinedPML userDefinedPML() {
            return pdpUserDefinedPML;
        }

        @Override
        public void handlePolicyEvent(PolicyEvent event) throws PMException {
            for (PolicyEventListener listener : epps) {
                listener.handlePolicyEvent(event);
            }
        }
    }
}
