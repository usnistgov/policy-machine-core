package gov.nist.ngac.pm.core.impl.memory.pap.store;

import gov.nist.ngac.pm.core.common.exception.ObligationDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.store.ObligationsStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemoryObligationsStore extends MemoryStore implements ObligationsStore {

    public MemoryObligationsStore(MemoryPolicy policy, MemoryTx tx, TxCmdTracker txCmdTracker) {
        super(policy, tx, txCmdTracker);
    }

    @Override
    public void createObligation(Obligation obligation) throws PMException {
        policy.obligations.add(obligation);

        txCmdTracker.trackOp(tx, new TxCmd.CreateObligationTxCmd(obligation));
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
    public ObligationPml getObligationPml(String name) throws PMException {
        Obligation obligation = findObligation(name);
        if (obligation == null) {
            return null;
        }

        return new ObligationPml(obligation.getName(), obligation.toString(), obligation.getAuthor());
    }

    @Override
    public Collection<ObligationPml> getObligationPmls() throws PMException {
        List<ObligationPml> rows = new ArrayList<>();
        for (Obligation obligation : policy.obligations) {
            rows.add(new ObligationPml(obligation.getName(), obligation.toString(), obligation.getAuthor()));
        }

        return rows;
    }

    @Override
    public Collection<String> getObligationNamesWithAuthor(NodeUserContext author) throws PMException {
        List<String> names = new ArrayList<>();
        for (Obligation obligation : policy.obligations) {
            if (author.equals(obligation.getAuthor())) {
                names.add(obligation.getName());
            }
        }

        return names;
    }

    @Override
    public void deleteObligation(String name) throws PMException {
        Obligation old = requireObligation(name);
        policy.obligations.removeIf(o -> o.getName().equals(name));
        txCmdTracker.trackOp(tx, new TxCmd.DeleteObligationTxCmd(old));
    }

    private Obligation findObligation(String name) {
        for (Obligation obligation : policy.obligations) {
            if (obligation.getName().equals(name)) {
                return obligation;
            }
        }

        return null;
    }

    private Obligation requireObligation(String name) throws ObligationDoesNotExistException {
        Obligation obligation = findObligation(name);
        if (obligation == null) {
            throw new ObligationDoesNotExistException(name);
        }

        return obligation;
    }

}
