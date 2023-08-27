package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.events.obligations.CreateObligationEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;

import java.util.List;

public class TxObligations implements Obligations, BaseMemoryTx {

    private final TxPolicyEventTracker txPolicyEventTracker;
    private final MemoryObligations memoryObligations;

    public TxObligations(TxPolicyEventTracker txPolicyEventTracker, MemoryObligations memoryObligations) {
        this.txPolicyEventTracker = txPolicyEventTracker;
        this.memoryObligations = memoryObligations;
    }
    @Override
    public void create(UserContext author, String id, Rule... rules) {
        txPolicyEventTracker.trackPolicyEvent(new CreateObligationEvent(author, id, List.of(rules)));
    }

    @Override
    public void rollback() throws PMException {
        List<PolicyEvent> events = txPolicyEventTracker.getEvents();
        for (PolicyEvent event : events) {
            TxCmd<MemoryObligations> txCmd = (TxCmd<MemoryObligations>) TxCmd.eventToCmd(event);
            txCmd.rollback(memoryObligations);
        }
    }
    @Override
    public void update(UserContext author, String id, Rule... rules) throws PMException {
        txPolicyEventTracker.trackPolicyEvent(new TxEvents.MemoryUpdateObligationEvent(
                new Obligation(author, id, List.of(rules)),
                memoryObligations.get(id)
        ));
    }

    @Override
    public void delete(String id) throws PMException {
        txPolicyEventTracker.trackPolicyEvent(new TxEvents.MemoryDeleteObligationEvent(memoryObligations.get(id)));
    }

    @Override
    public List<Obligation> getAll() {
        return null;
    }

    @Override
    public boolean exists(String id) {
        return false;
    }

    @Override
    public Obligation get(String id) {
        return null;
    }

}
