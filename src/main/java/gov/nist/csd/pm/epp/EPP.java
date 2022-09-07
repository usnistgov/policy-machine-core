package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.policy.events.PolicyEventListener;
import gov.nist.csd.pm.policy.events.EventContext;
import gov.nist.csd.pm.policy.events.PolicyEvent;

public class EPP implements PolicyEventListener{

    private final EventListener eventListener;

    public EPP(PDP pdp, PAP pap) throws PMException {
        eventListener = new EventListener(new EventProcessor(pdp, pap));

        pdp.addEventListener(eventListener, false);
    }

    @Override
    public void handlePolicyEvent(PolicyEvent event) throws PMException {
         eventListener.handlePolicyEvent(event);
    }
}
