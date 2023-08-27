package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;

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
                if(!eventCtx.matchesPattern(rule.getEventPattern(), pdp.reviewer())) {
                    continue;
                }

                Response response = rule.getResponse();

                /*check that the author can "trigger_event" on the evetnt context
                        probably add method to event context to make a decision on an impl basis*/

                // need to run pdp tx as author
                pdp.runTx(author, txPDP -> response.execute(txPDP, eventCtx));
            }
        }
    }
}
