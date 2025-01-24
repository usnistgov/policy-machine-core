package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.common.event.EventSubscriber;
import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.obligation.Response;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pdp.PDPExecutionContext;

import java.util.Collection;
import java.util.List;

public class EPP implements EventSubscriber {

    private PDP pdp;
    private PAP pap;

    public EPP(PDP pdp, PAP pap) throws PMException {
        this.pdp = pdp;
        this.pap = pap;
    }

    public EPP subscribeToPDP() {
        pdp.addEventSubscriber(this);
        return this;
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
                UserContext userContext = new UserContext(author);

                // need to run pdp tx as author
                pdp.runTx(userContext, txPDP -> response.execute(new PDPExecutionContext(userContext, txPDP), eventCtx));
            }
        }
    }
}
