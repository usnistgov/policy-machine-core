package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.epp.EventListener;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.events.obligations.CreateObligationEvent;
import gov.nist.csd.pm.policy.events.obligations.DeleteObligationEvent;
import gov.nist.csd.pm.policy.events.obligations.UpdateObligationEvent;
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

class PDPObligations implements Obligations, EventEmitter {
    private UserContext userCtx;
    private AdjudicatorObligations adjudicator;
    private PAP pap;
    private EventListener listener;

    public PDPObligations(UserContext userCtx, AdjudicatorObligations adjudicator, PAP pap, gov.nist.csd.pm.epp.EventListener listener) {
        this.userCtx = userCtx;
        this.adjudicator = adjudicator;
        this.pap = pap;
        this.listener = listener;
    }

    @Override
    public void create(UserContext author, String id, Rule... rules) throws PMException {
        adjudicator.create(author, id, rules);

        pap.obligations().create(author, id, rules);

        emitObligationEvent(new CreateObligationEvent(author, id, List.of(rules)), rules);
    }

    private void emitObligationEvent(PolicyEvent event, Rule... rules) throws PMException {
        // emit events for each rule
        for (Rule rule : rules) {
            // emit event for the subject
            EventSubject subject = rule.getEventPattern().getSubject();
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
            Target target = rule.getEventPattern().getTarget();
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
    public void update(UserContext author, String id, Rule... rules) throws PMException {
        adjudicator.update(author, id, rules);

        pap.obligations().update(author, id, rules);

        emitObligationEvent(
                new UpdateObligationEvent(author, id, List.of(rules)),
                rules
        );
    }

    @Override
    public void delete(String id) throws PMException {
        if (!exists(id)) {
            return;
        }

        adjudicator.delete(id);

        // get the obligation to use in the EPP before it is deleted
        Obligation obligation = get(id);

        pap.obligations().delete(id);

        emitDeleteObligationEvent(obligation);
    }

    private void emitDeleteObligationEvent(Obligation obligation) throws PMException {
        emitObligationEvent(
                new DeleteObligationEvent(obligation.getId()),
                obligation.getRules().toArray(Rule[]::new)
        );
    }

    @Override
    public List<Obligation> getAll() throws PMException {
        return adjudicator.getAll();
    }

    @Override
    public boolean exists(String id) throws PMException {
        return adjudicator.exists(id);
    }

    @Override
    public Obligation get(String id) throws PMException {
        return adjudicator.get(id);
    }

    @Override
    public void addEventListener(EventListener listener) {

    }

    @Override
    public void removeEventListener(EventListener listener) {

    }

    @Override
    public void emitEvent(EventContext event) throws PMException {
        this.listener.processEvent(event);
    }
}
