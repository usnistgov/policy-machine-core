package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.Prohibitions;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.pap.PAPGraph.checkAccessRightsValid;

class PAPProhibitions implements Prohibitions, PolicyEventEmitter {
    protected PolicyStore policyStore;

    protected PolicyEventListener listener;

    public PAPProhibitions(PolicyStore policyStore, PolicyEventListener listener) throws PMException {
        this.policyStore = policyStore;
        this.listener = listener;
    }

    @Override
    public synchronized void createProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        if (prohibitionExists(label)) {
            throw new ProhibitionExistsException(label);
        }

        checkProhibitionParameters(subject, accessRightSet, containerConditions);

        policyStore.prohibitions().createProhibition(label, subject, accessRightSet, intersection, containerConditions);

        emitEvent(new CreateProhibitionEvent(
                label, subject, accessRightSet, intersection, List.of(containerConditions)
        ));
    }

    @Override
    public synchronized void updateProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        checkProhibitionParameters(subject, accessRightSet, containerConditions);

        policyStore.prohibitions().updateProhibition(label, subject, accessRightSet, intersection, containerConditions);

        emitEvent(new UpdateProhibitionEvent(
                label, subject, accessRightSet, intersection, List.of(containerConditions))
        );
    }

    private void checkProhibitionParameters(ProhibitionSubject subject, AccessRightSet accessRightSet, ContainerCondition ... containerConditions) throws PMException {
        checkAccessRightsValid(policyStore.graph(), accessRightSet);
        checkProhibitionSubjectExists(subject);
        checkProhibitionContainersExist(containerConditions);
    }

    private void checkProhibitionSubjectExists(ProhibitionSubject subject) throws PMException {
        if (subject.type() != ProhibitionSubject.Type.PROCESS) {
            if (!policyStore.graph().nodeExists(subject.name())) {
                throw new ProhibitionSubjectDoesNotExistException(subject.name());
            }
        }
    }

    private void checkProhibitionContainersExist(ContainerCondition ... containerConditions) throws PMException {
        for (ContainerCondition container : containerConditions) {
            if (!policyStore.graph().nodeExists(container.name())) {
                throw new ProhibitionContainerDoesNotExistException(container.name());
            }
        }
    }

    @Override
    public synchronized void deleteProhibition(String label) throws PMException {
        if (!prohibitionExists(label)) {
            return;
        }

        Prohibition prohibition = policyStore.prohibitions().getProhibition(label);

        policyStore.prohibitions().deleteProhibition(label);

        emitEvent(new DeleteProhibitionEvent(prohibition));
    }

    @Override
    public synchronized Map<String, List<Prohibition>> getProhibitions() throws PMException {
        return policyStore.prohibitions().getProhibitions();
    }

    @Override
    public boolean prohibitionExists(String label) throws PMException {
        return policyStore.prohibitions().prohibitionExists(label);
    }

    @Override
    public synchronized List<Prohibition> getProhibitionsWithSubject(String subject) throws PMException {
        return policyStore.prohibitions().getProhibitionsWithSubject(subject);
    }

    @Override
    public synchronized Prohibition getProhibition(String label) throws PMException {
        Prohibition prohibition = getProhibitionOrNull(label);
        if (prohibition == null) {
            throw new ProhibitionDoesNotExistException(label);
        }

        return prohibition;
    }

    private Prohibition getProhibitionOrNull(String label) throws PMException {
        for (List<Prohibition> prohibitions : policyStore.prohibitions().getProhibitions().values()) {
            for (Prohibition p : prohibitions) {
                if (p.getLabel().equals(label)) {
                    return p;
                }
            }
        }

        return null;
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
