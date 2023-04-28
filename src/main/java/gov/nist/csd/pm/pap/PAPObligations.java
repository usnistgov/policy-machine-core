package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.NodeDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.ObligationDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.ObligationExistsException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;

import java.util.List;

class PAPObligations implements Obligations, PolicyEventEmitter {
    protected PolicyStore policyStore;

    protected PolicyEventListener listener;

    public PAPObligations(PolicyStore policyStore, PolicyEventListener listener) throws PMException {
        this.policyStore = policyStore;
        this.listener = listener;
    }

    @Override
    public synchronized void create(UserContext author, String label, Rule... rules) throws PMException {
        if (exists(label)) {
            throw new ObligationExistsException(label);
        }

        checkAuthorExists(author);
        checkEventPatternAttributesExist(rules);

        policyStore.obligations().create(author, label, rules);

        emitEvent(new CreateObligationEvent(author, label, List.of(rules)));
    }

    private void checkAuthorExists(UserContext author) throws PMException {
        if (!policyStore.graph().nodeExists(author.getUser())) {
            throw new NodeDoesNotExistException(author.getUser());
        }
    }

    private void checkEventPatternAttributesExist(Rule ... rules) throws PMException {
        for (Rule rule : rules) {
            EventPattern event = rule.getEventPattern();

            // check subject
            EventSubject subject = event.getSubject();
            switch (subject.getType()) {
                case USERS -> {
                    for (String user : subject.users()) {
                        if (!policyStore.graph().nodeExists(user)) {
                            throw new NodeDoesNotExistException(user);
                        }
                    }
                }
                case ANY_USER_WITH_ATTRIBUTE -> {
                    if (!policyStore.graph().nodeExists(subject.anyUserWithAttribute())) {
                        throw new NodeDoesNotExistException(subject.anyUserWithAttribute());
                    }
                }
            }

            // check target
            Target target = event.getTarget();
            switch (target.getType()) {
                case ANY_OF_SET -> {
                    for (String pe : target.anyOfSet()) {
                        if (!policyStore.graph().nodeExists(pe)) {
                            throw new NodeDoesNotExistException(pe);
                        }
                    }
                }
                case POLICY_ELEMENT -> {
                    if (!policyStore.graph().nodeExists(target.policyElement())) {
                        throw new NodeDoesNotExistException(target.policyElement());
                    }
                }
                case ANY_CONTAINED_IN -> {
                    if (!policyStore.graph().nodeExists(target.anyContainedIn())) {
                        throw new NodeDoesNotExistException(target.anyContainedIn());
                    }
                }
            }
        }
    }

    @Override
    public boolean exists(String label) throws PMException {
        return policyStore.obligations().exists(label);
    }

    @Override
    public synchronized void update(UserContext author, String label, Rule... rules) throws PMException {
        if (!exists(label)) {
            throw new ObligationDoesNotExistException(label);
        }

        checkAuthorExists(author);
        checkEventPatternAttributesExist(rules);

        policyStore.obligations().update(author, label, rules);

        emitEvent(new UpdateObligationEvent(author, label, List.of(rules)));
    }

    @Override
    public synchronized void delete(String label) throws PMException {
        if (!exists(label)) {
            return;
        }

        Obligation obligation = policyStore.obligations().get(label);

        policyStore.obligations().delete(label);

        emitEvent(new DeleteObligationEvent(obligation));
    }

    @Override
    public synchronized List<Obligation> getAll() throws PMException {
        return policyStore.obligations().getAll();
    }

    @Override
    public synchronized Obligation get(String label) throws PMException {
        if (!exists(label)) {
            throw new ObligationDoesNotExistException(label);
        }

        return policyStore.obligations().get(label);
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {

    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {

    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        listener.handlePolicyEvent(event);
    }
}
