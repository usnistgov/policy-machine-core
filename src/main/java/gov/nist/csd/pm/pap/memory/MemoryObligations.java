package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.ObligationsStore;
import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.tx.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MemoryObligations extends MemoryStore<TxObligations> implements ObligationsStore, Transactional, BaseMemoryTx {

    protected MemoryTx<TxObligations> tx;
    private List<Obligation> obligations;
    private MemoryGraph graph;

    public MemoryObligations() {
        this.obligations = new ArrayList<>();
    }

    public MemoryObligations(List<Obligation> obligations) {
        this.obligations = obligations;
    }

    public MemoryObligations(Obligations obligations) throws PMException {
        this.obligations = obligations.getAll();
    }

    public void setMemoryGraph(MemoryGraph graph) {
        this.graph = graph;
    }

    public void clear() {
        this.obligations.clear();
    }

    @Override
    public void beginTx() throws PMException {
        if (tx == null) {
            tx = new MemoryTx<>(false, 0, new TxObligations(new TxPolicyEventTracker(), this));
        }

        tx.beginTx();
    }

    @Override
    public void commit() throws PMException {
        tx.commit();
    }

    @Override
    public void rollback() throws PMException {
        tx.getStore().rollback();

        tx.rollback();
    }

    @Override
    public void create(UserContext author, String name, Rule... rules) throws ObligationIdExistsException, NodeDoesNotExistException, PMBackendException {
        checkCreateInput(graph, author, name, rules);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.create(author, name, rules));

        obligations.add(new Obligation(author, name, Arrays.asList(rules)));
    }

    @Override
    public void update(UserContext author, String name, Rule... rules) throws ObligationDoesNotExistException, NodeDoesNotExistException, PMBackendException {
        checkUpdateInput(graph, author, name, rules);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.update(author, name, rules));

        for (Obligation o : obligations) {
            if (o.getId().equals(name)) {
                o.setAuthor(author);
                o.setId(name);
                o.setRules(List.of(rules));
            }
        }
    }

    @Override
    public void delete(String name) throws PMBackendException {
        if (!checkDeleteInput(name)) {
            return;
        }

        // log the command if in a tx
        handleTxIfActive(tx -> tx.delete(name));

        this.obligations.removeIf(o -> o.getId().equals(name));
    }

    @Override
    public List<Obligation> getAll() {
        return new ArrayList<>(obligations);
    }

    @Override
    public boolean exists(String name) {
        for (Obligation o : obligations) {
            if (o.getId().equals(name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Obligation get(String name) throws ObligationDoesNotExistException, PMBackendException {
        checkGetInput(name);

        for (Obligation obligation : obligations) {
            if (obligation.getId().equals(name)) {
                return obligation.clone();
            }
        }

        // this shouldn't be reached due to the checkGet call, but just to be safe
        throw new ObligationDoesNotExistException(name);
    }
}
