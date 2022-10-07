package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStoreEventHandler;
import gov.nist.csd.pm.policy.author.*;
import gov.nist.csd.pm.policy.author.pal.PALExecutable;
import gov.nist.csd.pm.policy.author.pal.PALExecutor;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.tx.TxRunner;

import java.util.ArrayList;
import java.util.List;

public abstract class PDP implements PolicyEventEmitter {

    protected final PAP pap;
    protected final List<PolicyEventListener> eventListeners;

    public PDP(PAP pap) throws PMException {
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

    public static class PDPTx extends PolicyAuthor implements PALExecutable {

        private final Graph graph;
        private final Prohibitions prohibitions;
        private final Obligations obligations;
        private final PAL pal;

        public PDPTx(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer, List<PolicyEventListener> epps) {
            this.graph = new Graph(userCtx, pap, policyReviewer, epps);
            this.prohibitions = new Prohibitions(userCtx, pap, policyReviewer, epps);
            this.obligations = new Obligations(userCtx, pap, policyReviewer, epps);
            this.pal = new PAL(userCtx, pap, policyReviewer, epps);
        }

        @Override
        public GraphAuthor graph() {
            return graph;
        }

        @Override
        public ProhibitionsAuthor prohibitions() {
            return prohibitions;
        }

        @Override
        public ObligationsAuthor obligations() {
            return obligations;
        }

        @Override
        public PALAuthor pal() {
            return pal;
        }

        @Override
        public List<PALStatement> compilePAL(String input, FunctionDefinitionStatement ... customBuiltinFunctions) throws PMException {
            return new PALExecutor(this).compilePAL(input, customBuiltinFunctions);
        }

        @Override
        public void compileAndExecutePAL(UserContext author, String input, FunctionDefinitionStatement ... customBuiltinFunctions) throws PMException {
            new PALExecutor(this).compileAndExecutePAL(author, input, customBuiltinFunctions);
        }

        @Override
        public String toPAL() throws PMException {
            return new PALExecutor(this).toPAL();
        }
    }
}
