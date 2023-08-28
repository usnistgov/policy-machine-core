package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.Prohibitions;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.events.prohibitions.CreateProhibitionEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.List;
import java.util.Map;

public class TxProhibitions implements Prohibitions, BaseMemoryTx {

    private final TxPolicyEventTracker txPolicyEventTracker;
    private final MemoryProhibitionsStore memoryProhibitionsStore;

    public TxProhibitions(TxPolicyEventTracker txPolicyEventTracker, MemoryProhibitionsStore memoryProhibitionsStore) {
        this.txPolicyEventTracker = txPolicyEventTracker;
        this.memoryProhibitionsStore = memoryProhibitionsStore;
    }

    @Override
    public void rollback() throws PMException {
        List<PolicyEvent> events = txPolicyEventTracker.getEvents();
        for (PolicyEvent event : events) {
            TxCmd<MemoryProhibitionsStore> txCmd = (TxCmd<MemoryProhibitionsStore>) TxCmd.eventToCmd(event);
            txCmd.rollback(memoryProhibitionsStore);
        }
    }

    @Override
    public void create(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) {
        txPolicyEventTracker.trackPolicyEvent(new CreateProhibitionEvent(name, subject, accessRightSet, intersection, List.of(containerConditions)));
    }

    @Override
    public void update(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        txPolicyEventTracker.trackPolicyEvent(new TxEvents.MemoryUpdateProhibitionEvent(
                new Prohibition(name, subject, accessRightSet, intersection, List.of(containerConditions)),
                memoryProhibitionsStore.get(name)
        ));
    }

    @Override
    public void delete(String name) throws PMException {
        txPolicyEventTracker.trackPolicyEvent(new TxEvents.MemoryDeleteProhibitionEvent(memoryProhibitionsStore.get(name)));
    }

    @Override
    public Map<String, List<Prohibition>> getAll() {
        return null;
    }

    @Override
    public boolean exists(String name) {
        return false;
    }

    @Override
    public List<Prohibition> getWithSubject(String subject) {
        return null;
    }

    @Override
    public Prohibition get(String name) {
        return null;
    }
}
