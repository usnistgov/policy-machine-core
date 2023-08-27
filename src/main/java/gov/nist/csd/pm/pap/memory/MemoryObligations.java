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
    public void create(UserContext author, String id, Rule... rules) throws ObligationIdExistsException, NodeDoesNotExistException, PMBackendException {
        checkCreateInput(graph, author, id, rules);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.create(author, id, rules));

        obligations.add(new Obligation(author, id, Arrays.asList(rules)));
    }

    @Override
    public void update(UserContext author, String id, Rule... rules) throws ObligationDoesNotExistException, NodeDoesNotExistException, PMBackendException {
        checkUpdateInput(graph, author, id, rules);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.update(author, id, rules));

        for (Obligation o : obligations) {
            if (o.getId().equals(id)) {
                o.setAuthor(author);
                o.setId(id);
                o.setRules(List.of(rules));
            }
        }
    }

    @Override
    public void delete(String id) throws PMBackendException {
        if (!checkDeleteInput(id)) {
            return;
        }

        // log the command if in a tx
        handleTxIfActive(tx -> tx.delete(id));

        this.obligations.removeIf(o -> o.getId().equals(id));
    }

    @Override
    public List<Obligation> getAll() {
        return new ArrayList<>(obligations);
    }

    @Override
    public boolean exists(String id) {
        for (Obligation o : obligations) {
            if (o.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Obligation get(String id) throws ObligationDoesNotExistException, PMBackendException {
        checkGetInput(id);

        for (Obligation obligation : obligations) {
            if (obligation.getId().equals(id)) {
                return obligation.clone();
            }
        }

        // this shouldn't be reached due to the checkGet call, but just to be safe
        throw new ObligationDoesNotExistException(id);
    }
}
