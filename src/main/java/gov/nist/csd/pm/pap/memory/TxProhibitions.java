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
    private final MemoryProhibitions memoryProhibitions;

    public TxProhibitions(TxPolicyEventTracker txPolicyEventTracker, MemoryProhibitions memoryProhibitions) {
        this.txPolicyEventTracker = txPolicyEventTracker;
        this.memoryProhibitions = memoryProhibitions;
    }

    @Override
    public void rollback() throws PMException {
        List<PolicyEvent> events = txPolicyEventTracker.getEvents();
        for (PolicyEvent event : events) {
            TxCmd<MemoryProhibitions> txCmd = (TxCmd<MemoryProhibitions>) TxCmd.eventToCmd(event);
            txCmd.rollback(memoryProhibitions);
        }
    }

    @Override
    public void create(String id, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) {
        txPolicyEventTracker.trackPolicyEvent(new CreateProhibitionEvent(id, subject, accessRightSet, intersection, List.of(containerConditions)));
    }

    @Override
    public void update(String id, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        txPolicyEventTracker.trackPolicyEvent(new TxEvents.MemoryUpdateProhibitionEvent(
                new Prohibition(id, subject, accessRightSet, intersection, List.of(containerConditions)),
                memoryProhibitions.get(id)
        ));
    }

    @Override
    public void delete(String id) throws PMException {
        txPolicyEventTracker.trackPolicyEvent(new TxEvents.MemoryDeleteProhibitionEvent(memoryProhibitions.get(id)));
    }

    @Override
    public Map<String, List<Prohibition>> getAll() {
        return null;
    }

    @Override
    public boolean exists(String id) {
        return false;
    }

    @Override
    public List<Prohibition> getWithSubject(String subject) {
        return null;
    }

    @Override
    public Prohibition get(String id) {
        return null;
    }
}
