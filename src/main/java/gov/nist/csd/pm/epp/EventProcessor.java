package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.policy.author.pal.PALExecutor;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.policy.events.EventContext;

import java.util.List;

class EventProcessor {

    private final PDP pdp;
    private final PAP pap;

    public EventProcessor(PDP pdp, PAP pap) {
        this.pdp = pdp;
        this.pap = pap;
    }

    public void processEvent(EventContext eventCtx) throws PMException {
        List<Obligation> obligations = pap.obligations().getAll();
        for(Obligation obligation : obligations) {
            UserContext author = obligation.getAuthor();
            List<Rule> rules = obligation.getRules();
            for(Rule rule : rules) {
                if(!eventCtx.matchesPattern(rule.getEvent(), pdp.policyReviewer())) {
                    continue;
                }

                Response response = rule.getResponse();

                // need to run pdp tx as author
                pdp.runTx(author, (txPDP) -> {
                    /*PALExecutor.executeStatementBlock(executionCtx, txPDP, response.getStatements());*/
                    response.execute(txPDP, eventCtx);
                });
            }
        }
    }
}
