package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.store.PolicyStore;
import gov.nist.csd.pm.policy.author.ProhibitionsAuthor;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.Graph.checkAccessRightsValid;

class Prohibitions extends ProhibitionsAuthor implements PolicyEventEmitter {

    private final PolicyStore store;
    private final List<PolicyEventListener> listeners;

    Prohibitions(PolicyStore store) {
        this.store = store;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void create(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        if (prohibitionExists(label)) {
            throw new ProhibitionExistsException(label);
        }

        checkProhibitionParameters(subject, accessRightSet, containerConditions);

        store.prohibitions().create(label, subject, accessRightSet, intersection, containerConditions);

        emitEvent(new CreateProhibitionEvent(
                label, subject, accessRightSet, intersection, List.of(containerConditions)
        ));
    }

    @Override
    public void update(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        if (!prohibitionExists(label)) {
            throw new ProhibitionDoesNotExistException(label);
        }

        checkProhibitionParameters(subject, accessRightSet, containerConditions);

        store.prohibitions().update(label, subject, accessRightSet, intersection, containerConditions);

        emitEvent(new UpdateProhibitionEvent(
                label, subject, accessRightSet, intersection, List.of(containerConditions))
        );
    }

    private void checkProhibitionParameters(ProhibitionSubject subject, AccessRightSet accessRightSet, ContainerCondition ... containerConditions) throws PMException {
        checkAccessRightsValid(store.graph(), accessRightSet);
        checkProhibitionSubjectExists(subject);
        checkProhibitionContainersExist(containerConditions);
    }

    private void checkProhibitionSubjectExists(ProhibitionSubject subject) throws PMException {
        if (subject.type() != ProhibitionSubject.Type.PROCESS) {
            if (!store.graph().nodeExists(subject.name())) {
                throw new ProhibitionSubjectDoesNotExistException(subject.name());
            }
        }
    }

    private void checkProhibitionContainersExist(ContainerCondition ... containerConditions) throws PMException {
        for (ContainerCondition container : containerConditions) {
            if (!store.graph().nodeExists(container.name())) {
                throw new ProhibitionContainerDoesNotExistException(container.name());
            }
        }
    }

    @Override
    public void delete(String label) throws PMException {
        if (!prohibitionExists(label)) {
            return;
        }

        Prohibition prohibition = store.prohibitions().get(label);

        store.prohibitions().delete(label);

        emitEvent(new DeleteProhibitionEvent(prohibition));
    }

    private boolean prohibitionExists(String label) throws PMException {
        return getProhibitionOrNull(label) != null;
    }

    private Prohibition getProhibitionOrNull(String label) throws PMException {
        for (List<Prohibition> prohibitions : store.prohibitions().getAll().values()) {
            for (Prohibition p : prohibitions) {
                if (p.getLabel().equals(label)) {
                    return p;
                }
            }
        }

        return null;
    }

    @Override
    public Map<String, List<Prohibition>> getAll() throws PMException {
        return store.prohibitions().getAll();
    }

    @Override
    public List<Prohibition> getWithSubject(String subject) throws PMException {
        return store.prohibitions().getWithSubject(subject);
    }

    @Override
    public Prohibition get(String label) throws PMException {
        Prohibition prohibition = getProhibitionOrNull(label);
        if (prohibition == null) {
            throw new ProhibitionDoesNotExistException(label);
        }

        return prohibition;
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
