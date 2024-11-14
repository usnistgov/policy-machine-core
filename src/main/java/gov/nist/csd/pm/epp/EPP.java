package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.pap.obligation.EventContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pap.obligation.Response;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pdp.PDPExecutionContext;

import java.util.Collection;
import java.util.List;

public class EPP {

    private final EPPEventProcessor eventListener;

    public EPP(PDP pdp, PAP pap) throws PMException {
        eventListener = new EPPEventProcessor(pdp, pap);

        pdp.addEventListener(eventListener);
    }

    public EPPEventProcessor getEventProcessor() {
        return eventListener;
    }

    public static class EPPEventProcessor implements EventProcessor {

        private PDP pdp;
        private PAP pap;

        public EPPEventProcessor(PDP pdp, PAP pap) {
            this.pdp = pdp;
            this.pap = pap;
        }

        @Override
        public void processEvent(EventContext eventCtx) throws PMException {
            Collection<Obligation> obligations = pap.query().obligations().getObligations();
            for(Obligation obligation : obligations) {
                String author = obligation.getAuthor();
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
}
