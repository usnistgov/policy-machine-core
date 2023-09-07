package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.events.obligations.CreateObligationEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.PMRuntimeException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;

import java.util.List;

public class TxObligations implements Obligations, BaseMemoryTx {

    private final TxPolicyEventTracker txPolicyEventTracker;
    private final MemoryObligationsStore memoryObligationsStore;

    public TxObligations(TxPolicyEventTracker txPolicyEventTracker, MemoryObligationsStore memoryObligationsStore) {
        this.txPolicyEventTracker = txPolicyEventTracker;
        this.memoryObligationsStore = memoryObligationsStore;
    }
    @Override
    public void create(UserContext author, String name, Rule... rules) {
        txPolicyEventTracker.trackPolicyEvent(new CreateObligationEvent(author, name, List.of(rules)));
    }

    @Override
    public void rollback() {
        List<PolicyEvent> events = txPolicyEventTracker.getEvents();
        for (PolicyEvent event : events) {
            try {
                TxCmd<MemoryObligationsStore> txCmd = (TxCmd<MemoryObligationsStore>) TxCmd.eventToCmd(event);
                txCmd.rollback(memoryObligationsStore);
            } catch (PMException e) {
                // throw runtime exception because there is noway back if the rollback fails
                throw new PMRuntimeException("", e);
            }
        }
    }

    @Override
    public void update(UserContext author, String name, Rule... rules) throws PMException {
        txPolicyEventTracker.trackPolicyEvent(new TxEvents.MemoryUpdateObligationEvent(
                new Obligation(author, name, List.of(rules)),
                memoryObligationsStore.get(name)
        ));
    }

    @Override
    public void delete(String name) throws PMException {
        txPolicyEventTracker.trackPolicyEvent(new TxEvents.MemoryDeleteObligationEvent(memoryObligationsStore.get(name)));
    }

    @Override
    public List<Obligation> getAll() {
        return null;
    }

    @Override
    public boolean exists(String name) {
        return false;
    }

    @Override
    public Obligation get(String name) {
        return null;
    }

}
