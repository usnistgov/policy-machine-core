package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.event.EventSubscriber;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pap.obligation.Response;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.PDP;

import java.util.Collection;
import java.util.List;

public class EPP implements EventSubscriber {

    private final PAP pap;
    private final PDP pdp;

    public EPP(PDP pdp, PAP pap) {
        this.pap = pap;
        this.pdp = pdp;
    }

    @Override
    public void processEvent(EventContext eventCtx) throws PMException {
        Collection<Obligation> obligations = pap.query().obligations().getObligations();
        for(Obligation obligation : obligations) {
            long author = obligation.getAuthorId();
            List<Rule> rules = obligation.getRules();
            for(Rule rule : rules) {
                if(!rule.getEventPattern().matches(eventCtx, pap)) {
                    continue;
                }

                Response response = rule.getResponse();
                UserContext authorCtx = new UserContext(author);

                // need to run pdp tx as author of obligation
                pdp.runTx(authorCtx, txPDP -> response.execute(txPDP, authorCtx, eventCtx));
            }
        }
    }
}
