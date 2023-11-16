package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.reviewer.GraphReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.scope.Scope;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.List;

public class EPP {

    private final EPPEventProcessor eventListener;

    public EPP(PDP pdp, PAP pap, FunctionDefinitionStatement ... customFunctions) throws PMException {
        eventListener = new EPPEventProcessor(pdp, pap, customFunctions);

        pdp.addEventListener(eventListener);
    }

    public EPPEventProcessor getEventProcessor() {
        return eventListener;
    }

    public static class EPPEventProcessor implements EventProcessor {

        private PDP pdp;
        private PAP pap;
        private FunctionDefinitionStatement[] customFunctions;

        public EPPEventProcessor(PDP pdp, PAP pap, FunctionDefinitionStatement... customFunctions) {
            this.pdp = pdp;
            this.pap = pap;
            this.customFunctions = customFunctions;
        }

        @Override
        public void processEvent(EventContext eventCtx) throws PMException {
            GlobalScope<Value, FunctionDefinitionStatement> globalScope = GlobalScope.withValuesAndDefinitions(pap, customFunctions);
            List<Obligation> obligations = pap.obligations().getAll();
            for(Obligation obligation : obligations) {
                UserContext author = obligation.getAuthor();
                ExecutionContext executionCtx = new ExecutionContext(author, new Scope<>(globalScope));

                List<Rule> rules = obligation.getRules();
                for(Rule rule : rules) {
                    if(!eventCtx.matchesPattern(rule.getEventPattern(), new GraphReviewer(pap))) {
                        continue;
                    }

                    Response response = rule.getResponse();

                    // need to run pdp tx as author
                    pdp.runTx(author, txPDP -> response.execute(executionCtx, txPDP, eventCtx));
                }
            }
        }
    }
}
