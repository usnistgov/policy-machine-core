package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.ObligationsStore;
import gov.nist.csd.pm.policy.exceptions.TransactionNotStartedException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.exceptions.ObligationDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MemoryObligationsStore extends ObligationsStore {

    private List<Obligation> obligations;
    MemoryObligationsStore() {
        this.obligations = new ArrayList<>();
    }

    MemoryObligationsStore(List<Obligation> obligations) {
        this.obligations = obligations;
    }

    synchronized void setObligations(List<Obligation> obligations) {
        this.obligations = new ArrayList<>(obligations);
    }

    @Override
    public synchronized void create(UserContext author, String label, Rule... rules) {
        obligations.add(new Obligation(author, label, Arrays.asList(rules)));
    }

    @Override
    public synchronized void update(UserContext author, String label, Rule... rules) {
        for (Obligation o : obligations) {
            if (o.getLabel().equals(label)) {
                o.setAuthor(author);
                o.setLabel(label);
                o.setRules(List.of(rules));
            }
        }
    }

    @Override
    public synchronized void delete(String label) {
        this.obligations.removeIf(o -> o.getLabel().equals(label));
    }

    @Override
    public synchronized List<Obligation> getAll() {
        return new ArrayList<>(obligations);
    }

    @Override
    public synchronized Obligation get(String label) throws ObligationDoesNotExistException {
        for (Obligation obligation : obligations) {
            if (obligation.getLabel().equals(label)) {
                return obligation.clone();
            }
        }

        throw new ObligationDoesNotExistException(label);
    }

    @Override
    public synchronized void beginTx() throws PMException {

    }

    @Override
    public synchronized void commit() throws PMException {

    }

    @Override
    public synchronized void rollback() throws TransactionNotStartedException {

    }
}
