package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.exception.ObligationDoesNotExistException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.store.ObligationsStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class MemoryObligationsStore extends MemoryStore implements ObligationsStore {

    public MemoryObligationsStore(MemoryPolicy policy, MemoryTx tx, TxCmdTracker txCmdTracker) {
        super(policy, tx, txCmdTracker);
    }

    @Override
    public void createObligation(long authorId, String name, List<Rule> rules) throws PMException {
        Obligation obligation = new Obligation(authorId, name, rules.stream().toList());

        policy.obligations.add(obligation);

        txCmdTracker.trackOp(tx, new TxCmd.CreateObligationTxCmd(obligation));
    }

    @Override
    public Collection<Obligation> getObligations() throws PMException {
        return policy.obligations;
    }

    @Override
    public boolean obligationExists(String name) throws PMException {
        for (Obligation o : policy.obligations) {
            if (o.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Obligation getObligation(String name) throws ObligationDoesNotExistException {
        for (Obligation obligation : policy.obligations) {
            if (obligation.getName().equals(name)) {
                return obligation;
            }
        }

        throw new ObligationDoesNotExistException(name);
    }

    @Override
    public Collection<Obligation> getObligationsWithAuthor(long userId) throws PMException {
        List<Obligation> obls = new ArrayList<>();
        for (Obligation obligation : getObligations()) {
            if (obligation.getAuthorId() == userId) {
                obls.add(obligation);
            }
        }

        return obls;
    }

    @Override
    public void deleteObligation(String name) throws PMException {
        Obligation old = getObligation(name);
        policy.obligations.removeIf(o -> o.getName().equals(name));
        txCmdTracker.trackOp(tx, new TxCmd.DeleteObligationTxCmd(old));
    }

}
