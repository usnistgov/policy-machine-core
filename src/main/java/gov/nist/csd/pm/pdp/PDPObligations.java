package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.adjudicator.GraphAdjudicator;
import gov.nist.csd.pm.pdp.adjudicator.ObligationsAdjudicator;
import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;

import java.util.List;

import static gov.nist.csd.pm.policy.model.obligation.event.EventSubject.Type.*;
import static gov.nist.csd.pm.policy.model.obligation.event.EventSubject.Type.PROCESS;
import static gov.nist.csd.pm.policy.model.obligation.event.Target.Type.*;
import static gov.nist.csd.pm.policy.model.obligation.event.Target.Type.ANY_OF_SET;

class PDPObligations implements Obligations, PolicyEventEmitter {
    private UserContext userCtx;
    private ObligationsAdjudicator adjudicator;
    private PAP pap;
    private PolicyEventListener listener;

    public PDPObligations(UserContext userCtx, ObligationsAdjudicator adjudicator, PAP pap, PolicyEventListener listener) {
        this.userCtx = userCtx;
        this.adjudicator = adjudicator;
        this.pap = pap;
        this.listener = listener;
    }

    @Override
    public void createObligation(UserContext author, String label, Rule... rules) throws PMException {
        adjudicator.createObligation(author, label, rules);

        pap.obligations().createObligation(author, label, rules);

        emitObligationEvent(new CreateObligationEvent(author, label, List.of(rules)), rules);
    }

    private void emitObligationEvent(PolicyEvent event, Rule... rules) throws PMException {
        // emit events for each rule
        for (Rule rule : rules) {
            // emit event for the subject
            EventSubject subject = rule.getEvent().getSubject();
            if (subject.getType() == ANY_USER) {
                emitEvent(new EventContext(userCtx, "", event));
            } else if (subject.getType() == ANY_USER_WITH_ATTRIBUTE) {
                emitEvent(new EventContext(userCtx, subject.anyUserWithAttribute(), event));
            } else if (subject.getType() == USERS) {
                for (String user : subject.users()) {
                    emitEvent(new EventContext(userCtx, user, event));
                }
            } else if (subject.getType() == PROCESS) {
                emitEvent(new EventContext(userCtx, subject.process(), event));
            }

            // emit event for each target
            Target target = rule.getEvent().getTarget();
            if (target.getType() == POLICY_ELEMENT) {
                emitEvent(new EventContext(userCtx, target.policyElement(), event));
            } else if (target.getType() == ANY_POLICY_ELEMENT) {
                emitEvent(new EventContext(userCtx, "", event));
            } else if (target.getType() == ANY_CONTAINED_IN) {
                emitEvent(new EventContext(userCtx, target.anyContainedIn(), event));
            } else if (target.getType() == ANY_OF_SET) {
                for (String policyElement : target.anyOfSet()) {
                    emitEvent(new EventContext(userCtx, policyElement, event));
                }
            }
        }
    }

    @Override
    public void updateObligation(UserContext author, String label, Rule... rules) throws PMException {
        adjudicator.updateObligation(author, label, rules);

        pap.obligations().updateObligation(author, label, rules);

        emitObligationEvent(
                new UpdateObligationEvent(author, label, List.of(rules)),
                rules
        );
    }

    @Override
    public void deleteObligation(String label) throws PMException {
        if (!obligationExists(label)) {
            return;
        }

        adjudicator.deleteObligation(label);

        // get the obligation to use in the EPP before it is deleted
        Obligation obligation = getObligation(label);

        pap.obligations().deleteObligation(label);

        emitDeleteObligationEvent(obligation);
    }

    private void emitDeleteObligationEvent(Obligation obligation) throws PMException {
        emitObligationEvent(
                new DeleteObligationEvent(obligation),
                obligation.getRules().toArray(Rule[]::new)
        );
    }

    @Override
    public List<Obligation> getObligations() throws PMException {
        return adjudicator.getObligations();
    }

    @Override
    public boolean obligationExists(String label) throws PMException {
        return adjudicator.obligationExists(label);
    }

    @Override
    public Obligation getObligation(String label) throws PMException {
        return adjudicator.getObligation(label);
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {

    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {

    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        this.listener.handlePolicyEvent(event);
    }
}
