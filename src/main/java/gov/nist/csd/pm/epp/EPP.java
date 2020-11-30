package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.epp.functions.FunctionExecutor;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.GraphService;
import gov.nist.csd.pm.pdp.services.ObligationsService;
import gov.nist.csd.pm.pdp.services.ProhibitionsService;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.Features;
import gov.nist.csd.pm.pip.obligations.model.*;

import java.util.*;

public class EPP {

    private Features pap;
    private PDP pdp;
    private FunctionEvaluator functionEvaluator;

    public EPP(Features pap, PDP pdp, EPPOptions eppOptions) throws PMException {
        this.pap = pap;
        this.pdp = pdp;
        this.functionEvaluator = new FunctionEvaluator();
        if (eppOptions != null) {
            for (FunctionExecutor executor : eppOptions.getExecutors()) {
                this.functionEvaluator.addFunctionExecutor(executor);
            }
        }
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

            pdp.runTx(definingUser, (g, p, o) -> {
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
