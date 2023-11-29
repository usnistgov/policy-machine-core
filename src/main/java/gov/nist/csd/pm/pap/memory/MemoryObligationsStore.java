package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.ObligationsStore;
import gov.nist.csd.pm.pap.memory.unmodifiable.UnmodifiableObligation;
import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.tx.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class MemoryObligationsStore extends MemoryStore<TxObligations> implements ObligationsStore, Transactional, BaseMemoryTx {

    private List<Obligation> obligations;
    private MemoryGraphStore graph;

    public MemoryObligationsStore() {
        this.obligations = Collections.unmodifiableList(new ArrayList<>());
    }

    public void setMemoryGraph(MemoryGraphStore graph) {
        this.graph = graph;
    }

    public void clear() {
        this.obligations = Collections.unmodifiableList(new ArrayList<>());
    }

    @Override
    public void beginTx() {
        if (tx == null) {
            tx = new MemoryTx<>(false, 0, new TxObligations(new TxPolicyEventTracker(), this));
        }

        tx.beginTx();
    }

    @Override
    public void commit() {
        tx.commit();
    }

    @Override
    public void rollback() {
        tx.getStore().rollback();

        tx.rollback();
    }

    @Override
    public void create(UserContext author, String name, Rule... rules) throws ObligationNameExistsException, NodeDoesNotExistException, PMBackendException {
        checkCreateInput(graph, author, name, rules);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.create(author, name, rules));

        createInternal(obligations.size()-1, author, name, rules);
    }

    @Override
    public void update(UserContext author, String name, Rule... rules)
    throws ObligationDoesNotExistException, NodeDoesNotExistException, PMBackendException, ObligationRuleNameExistsException {
        checkUpdateInput(graph, author, name, rules);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.update(author, name, rules));

        for (int i = 0; i < obligations.size(); i++) {
            if (obligations.get(i).getName().equals(name)) {
                deleteInternal(name);
                createInternal(i, author, name, rules);

                return;
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

        deleteInternal(name);
    }

    @Override
    public List<Obligation> getAll() {
        return obligations;
    }

    @Override
    public boolean exists(String name) {
        for (Obligation o : obligations) {
            if (o.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Obligation get(String name) throws ObligationDoesNotExistException, PMBackendException {
        checkGetInput(name);

        for (Obligation obligation : obligations) {
            if (obligation.getName().equals(name)) {
                return obligation;
            }
        }

        // this shouldn't be reached due to the checkGet call, but just to be safe
        throw new ObligationDoesNotExistException(name);
    }

    private void createInternal(int index, UserContext author, String name, Rule ... rules) {
        ArrayList<Obligation> copy = new ArrayList<>(obligations);
        copy.add(Math.max(index, 0), new UnmodifiableObligation(author, name, Arrays.asList(rules)));
        this.obligations = Collections.unmodifiableList(copy);
    }

    private void deleteInternal(String name) {
        ArrayList<Obligation> copy = new ArrayList<>(obligations);
        copy.removeIf(o -> o.getName().equals(name));
        this.obligations = Collections.unmodifiableList(copy);
    }
}
