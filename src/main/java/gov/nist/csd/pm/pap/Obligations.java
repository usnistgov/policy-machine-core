package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.store.PolicyStore;
import gov.nist.csd.pm.policy.author.ObligationsAuthor;
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

import java.util.ArrayList;
import java.util.List;

class Obligations extends ObligationsAuthor implements PolicyEventEmitter {

    private final PolicyStore store;
    private final List<PolicyEventListener> listeners;


    public Obligations(PolicyStore store) {
        this.store = store;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void create(UserContext author, String label, Rule... rules) throws PMException {
        if (obligationExists(label)) {
            throw new ObligationExistsException(label);
        }

        checkAuthorExists(author);
        checkEventPatternAttributesExist(rules);

        store.obligations().create(author, label, rules);

        emitEvent(new CreateObligationEvent(author, label, List.of(rules)));
    }

    private void checkAuthorExists(UserContext author) throws PMException {
        if (!store.graph().nodeExists(author.getUser())) {
            throw new NodeDoesNotExistException(author.getUser());
        }
    }

    private void checkEventPatternAttributesExist(Rule ... rules) throws PMException {
        for (Rule rule : rules) {
            EventPattern event = rule.getEvent();

            // check subject
            EventSubject subject = event.getSubject();
            switch (subject.getType()) {
                case USERS -> {
                    for (String user : subject.users()) {
                        if (!store.graph().nodeExists(user)) {
                            throw new NodeDoesNotExistException(user);
                        }
                    }
                }
                case ANY_USER_WITH_ATTRIBUTE -> {
                    if (!store.graph().nodeExists(subject.anyUserWithAttribute())) {
                        throw new NodeDoesNotExistException(subject.anyUserWithAttribute());
                    }
                }
            }

            // check target
            Target target = event.getTarget();
            switch (target.getType()) {
                case ANY_OF_SET -> {
                    for (String pe : target.anyOfSet()) {
                        if (!store.graph().nodeExists(pe)) {
                            throw new NodeDoesNotExistException(pe);
                        }
                    }
                }
                case POLICY_ELEMENT -> {
                    if (!store.graph().nodeExists(target.policyElement())) {
                        throw new NodeDoesNotExistException(target.policyElement());
                    }
                }
                case ANY_CONTAINED_IN -> {
                    if (!store.graph().nodeExists(target.anyContainedIn())) {
                        throw new NodeDoesNotExistException(target.anyContainedIn());
                    }
                }
            }
        }
    }

    private boolean obligationExists(String label) throws PMException {
        return getObligationOrNull(label) != null;
    }

    private Obligation getObligationOrNull(String label) throws PMException {
        for (Obligation obligation : store.obligations().getAll()) {
            if (obligation.getLabel().equals(label)) {
                return obligation;
            }
        }

        return null;
    }

    @Override
    public void update(UserContext author, String label, Rule... rules) throws PMException {
        if (!obligationExists(label)) {
            throw new ObligationDoesNotExistException(label);
        }

        checkAuthorExists(author);
        checkEventPatternAttributesExist(rules);

        store.obligations().update(author, label, rules);

        emitEvent(new UpdateObligationEvent(author, label, List.of(rules)));
    }

    @Override
    public void delete(String label) throws PMException {
        if (!obligationExists(label)) {
            return;
        }

        store.obligations().delete(label);

        emitEvent(new DeleteObligationEvent(label));
    }

    @Override
    public List<Obligation> getAll() throws PMException {
        return store.obligations().getAll();
    }

    @Override
    public Obligation get(String label) throws PMException {
        if (!obligationExists(label)) {
            throw new ObligationDoesNotExistException(label);
        }

        return store.obligations().get(label);
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) {
        listeners.add(listener);
    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        for (PolicyEventListener listener : listeners) {
            listener.handlePolicyEvent(event);
        }
    }
}
