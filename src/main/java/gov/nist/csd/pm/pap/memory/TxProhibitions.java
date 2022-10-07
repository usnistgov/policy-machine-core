package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.ProhibitionsStore;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.tx.TxPolicyEventListener;

import java.util.List;
import java.util.Map;

class TxProhibitions extends ProhibitionsStore implements PolicyEventEmitter {

    private final ProhibitionsStore store;
    private final TxPolicyEventListener txPolicyEventListener;

    public TxProhibitions(ProhibitionsStore store, TxPolicyEventListener txPolicyEventListener) {
        this.store = store;
        this.txPolicyEventListener = txPolicyEventListener;
    }

    @Override
    public void create(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        emitEvent(new CreateProhibitionEvent(label, subject, accessRightSet, intersection, List.of(containerConditions)));
        store.create(label, subject, accessRightSet, intersection, containerConditions);
    }

    @Override
    public void update(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        emitEvent(new UpdateProhibitionEvent(label, subject, accessRightSet, intersection, List.of(containerConditions)));
        store.update(label, subject, accessRightSet, intersection, containerConditions);
    }

    @Override
    public void delete(String label) throws PMException {
        emitEvent(new DeleteProhibitionEvent(label));
        store.delete(label);
    }

    @Override
    public Map<String, List<Prohibition>> getAll() throws PMException {
        return store.getAll();
    }

    @Override
    public List<Prohibition> getWithSubject(String subject) throws PMException {
        return store.getWithSubject(subject);
    }

    @Override
    public Prohibition get(String label) throws PMException {
        return store.get(label);
    }

    @Override
    public void beginTx() throws PMException {

    }

    @Override
    public void commit() throws PMException {

    }

    @Override
    public void rollback() throws PMException {

    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {

    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {

    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        txPolicyEventListener.handlePolicyEvent(event);
    }
}
