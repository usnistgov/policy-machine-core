package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.epp.EventProcessor;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.events.obligations.CreateObligationEvent;
import gov.nist.csd.pm.policy.events.obligations.DeleteObligationEvent;
import gov.nist.csd.pm.policy.events.obligations.UpdateObligationEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.subject.Subject;
import gov.nist.csd.pm.policy.model.obligation.event.target.Target;

import java.util.List;

class PDPObligations implements Obligations, EventEmitter {
    private UserContext userCtx;
    private AdjudicatorObligations adjudicator;
    private PAP pap;
    private EventProcessor listener;

    public PDPObligations(UserContext userCtx, AdjudicatorObligations adjudicator, PAP pap, EventProcessor listener) {
        this.userCtx = userCtx;
        this.adjudicator = adjudicator;
        this.pap = pap;
        this.listener = listener;
    }

    @Override
    public void create(UserContext author, String name, Rule... rules) throws PMException {
        adjudicator.create(author, name, rules);

        pap.obligations().create(author, name, rules);

        emitObligationEvent(new CreateObligationEvent(author, name, List.of(rules)), rules);
    }

    private void emitObligationEvent(PolicyEvent event, Rule... rules) throws PMException {
        // emit events for each rule
        for (Rule rule : rules) {
            // emit event for the subject
            Subject subject = rule.getEventPattern().getSubject();
            for (String user : subject.getSubjects()) {
                emitEvent(new EventContext(userCtx, user, event));
            }

            // emit event for each target
            Target target = rule.getEventPattern().getTarget();
            for (String policyElement : target.getTargets()) {
                emitEvent(new EventContext(userCtx, policyElement, event));
            }
        }
    }

    @Override
    public void update(UserContext author, String name, Rule... rules) throws PMException {
        adjudicator.update(author, name, rules);

        pap.obligations().update(author, name, rules);

        emitObligationEvent(
                new UpdateObligationEvent(author, name, List.of(rules)),
                rules
        );
    }

    @Override
    public void delete(String name) throws PMException {
        if (!exists(name)) {
            return;
        }

        adjudicator.delete(name);

        // get the obligation to use in the EPP before it is deleted
        Obligation obligation = get(name);

        pap.obligations().delete(name);

        emitDeleteObligationEvent(obligation);
    }

    private void emitDeleteObligationEvent(Obligation obligation) throws PMException {
        emitObligationEvent(
                new DeleteObligationEvent(obligation.getName()),
                obligation.getRules().toArray(Rule[]::new)
        );
    }

    @Override
    public List<Obligation> getAll() throws PMException {
        return adjudicator.getAll();
    }

    @Override
    public boolean exists(String name) throws PMException {
        return adjudicator.exists(name);
    }

    @Override
    public Obligation get(String name) throws PMException {
        return adjudicator.get(name);
    }

    @Override
    public void addEventListener(EventProcessor listener) {

    }

    @Override
    public void removeEventListener(EventProcessor listener) {

    }

    @Override
    public void emitEvent(EventContext event) throws PMException {
        this.listener.processEvent(event);
    }
}
