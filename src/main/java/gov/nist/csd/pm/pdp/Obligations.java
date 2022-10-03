package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.adjudicator.Adjudicator;
import gov.nist.csd.pm.pdp.reviewer.PolicyReviewer;
import gov.nist.csd.pm.policy.author.ObligationsAuthor;
import gov.nist.csd.pm.policy.events.PolicyEventEmitter;
import gov.nist.csd.pm.policy.events.PolicyEventListener;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.review.PolicyReview;

import java.util.List;

import static gov.nist.csd.pm.policy.model.obligation.event.EventSubject.Type.*;
import static gov.nist.csd.pm.policy.model.obligation.event.EventSubject.Type.PROCESS;
import static gov.nist.csd.pm.policy.model.obligation.event.Target.Type.*;
import static gov.nist.csd.pm.policy.model.obligation.event.Target.Type.ANY_OF_SET;

class Obligations extends ObligationsAuthor implements PolicyEventEmitter {

    private UserContext userCtx;
    private PAP pap;
    private Adjudicator adjudicator;
    private List<PolicyEventListener> epps;

    public Obligations(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer, List<PolicyEventListener> epps) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.adjudicator = new Adjudicator(userCtx, pap, policyReviewer);
        this.epps = epps;
    }

    @Override
    public void create(UserContext author, String label, Rule... rules) throws PMException {
        adjudicator.obligations().create(author, label, rules);

        pap.obligations().create(author, label, rules);

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
    public void update(UserContext author, String label, Rule... rules) throws PMException {
        adjudicator.obligations().update(author, label, rules);

        pap.obligations().update(author, label, rules);

        emitObligationEvent(
                new UpdateObligationEvent(author, label, List.of(rules)),
                rules
        );
    }

    @Override
    public void delete(String label) throws PMException {
        adjudicator.obligations().delete(label);

        // get the obligation to use in the EPP before it is deleted
        Obligation obligation = get(label);

        pap.obligations().delete(label);

        emitDeleteObligationEvent(label, obligation);
    }

    @Override
    public List<Obligation> getAll() throws PMException {
        return adjudicator.obligations().getAll();
    }

    @Override
    public Obligation get(String label) throws PMException {
        return adjudicator.obligations().get(label);
    }

    private void emitDeleteObligationEvent(String label, Obligation obligation) throws PMException {
        emitObligationEvent(
                new DeleteObligationEvent(label),
                obligation.getRules().toArray(Rule[]::new)
        );
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
        // adding event listeners is done by the PDP class
    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {
        // removing event listeners is done by the PDP class
    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        for (PolicyEventListener epp : epps) {
            epp.handlePolicyEvent(event);
        }
    }
}
