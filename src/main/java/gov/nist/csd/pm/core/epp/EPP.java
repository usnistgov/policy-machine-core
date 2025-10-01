package gov.nist.csd.pm.core.epp;


import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventSubscriber;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.ObligationResponse;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;

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
        for (Obligation obligation : obligations) {
            long author = obligation.getAuthorId();
            List<Rule> rules = obligation.getRules();
            for (Rule rule : rules) {
                if (!rule.getEventPattern().matches(eventCtx, pap)) {
                    continue;
                }

                UserContext authorCtx = new UserContext(author);
                ObligationResponse obligationResponse = rule.getResponse();

                executeResponse(authorCtx, obligationResponse, eventCtx);
            }
        }
    }

    public void executeResponse(UserContext author, ObligationResponse obligationResponse, EventContext eventCtx) throws PMException {
        pdp.runTx(author, pdpTx -> {
            pdpTx.executeObligationResponse(eventCtx, obligationResponse);
            return null;
        });
    }
}
