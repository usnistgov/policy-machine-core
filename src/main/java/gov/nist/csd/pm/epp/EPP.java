package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.epp.functions.FunctionExecutor;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.pip.obligations.model.*;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.obligations.model.ResponsePattern;
import gov.nist.csd.pm.pip.obligations.model.Rule;

import java.util.List;

public class EPP {

    private FunctionalEntity pap;
    private PDP pdp;
    private FunctionEvaluator functionEvaluator;

    public EPP(FunctionalEntity pap, PDP pdp, EPPOptions eppOptions) throws PMException {
        this.pap = pap;
        this.pdp = pdp;
        this.functionEvaluator = new FunctionEvaluator();
        if (eppOptions != null) {
            for (FunctionExecutor executor : eppOptions.getExecutors()) {
                this.functionEvaluator.addFunctionExecutor(executor);
            }
        }
    }

    public void addFunctionExecutor(FunctionExecutor executor) {
        this.functionEvaluator.addFunctionExecutor(executor);
    }

    public void removeFunctionExecutor(FunctionExecutor executor) {
        this.functionEvaluator.removeFunctionExecutor(executor);
    }
    
    public PDP getPDP() {
        return pdp;
    }

    public synchronized void processEvent(EventContext eventCtx) throws PMException {
        List<Obligation> obligations = pap.getObligations().getAll();
        for(Obligation obligation : obligations) {
            if (!obligation.isEnabled()) {
                continue;
            }

            UserContext definingUser = new UserContext(obligation.getUser());

            pdp.withUser(definingUser).runTx((g, p, o) -> {
                List<Rule> rules = obligation.getRules();
                for(Rule rule : rules) {
                    if(!eventCtx.matchesPattern(rule.getEventPattern(), g)) {
                        continue;
                    }

                    ResponsePattern responsePattern = rule.getResponsePattern();
                    responsePattern.apply(g, p, o, functionEvaluator, eventCtx, rule, obligation.getLabel());
                }
            });
        }
    }
}
