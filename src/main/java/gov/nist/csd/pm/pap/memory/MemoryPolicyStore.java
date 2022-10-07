package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.*;
import gov.nist.csd.pm.policy.events.PolicySynchronizationEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.TransactionNotStartedException;
import gov.nist.csd.pm.policy.model.graph.Graph;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.author.pal.PALContext;

import java.util.List;
import java.util.Map;

public class MemoryPolicyStore extends PolicyStore {

    private MemoryGraphStore graph;
    private MemoryProhibitionsStore prohibitions;
    private MemoryObligationsStore obligations;
    private MemoryPALStore pal;

    public MemoryPolicyStore() {
        graph = new MemoryGraphStore();
        prohibitions = new MemoryProhibitionsStore();
        obligations = new MemoryObligationsStore();
        pal = new MemoryPALStore();
    }

    public MemoryPolicyStore(PolicySynchronizationEvent event) {
        this.graph = new MemoryGraphStore(event.getGraph());
        this.prohibitions = new MemoryProhibitionsStore(event.getProhibitions());
        this.obligations = new MemoryObligationsStore(event.getObligations());
        this.pal = new MemoryPALStore(event.getPALContext());
    }

    void setGraph(MemoryGraphStore graph) {
        this.graph = graph;
    }

    void setProhibitions(MemoryProhibitionsStore prohibitions) {
        this.prohibitions = prohibitions;
    }

    void setObligations(MemoryObligationsStore obligations) {
        this.obligations = obligations;
    }

    void setPAL(MemoryPALStore pal) {
        this.pal = pal;
    }

    MemoryGraphStore getGraph() {
        return graph;
    }

    MemoryProhibitionsStore getProhibitions() {
        return prohibitions;
    }

    MemoryObligationsStore getObligations() {
        return obligations;
    }

    MemoryPALStore getPAL() {
        return pal;
    }

    @Override
    public synchronized PolicySynchronizationEvent policySync() {
        return new PolicySynchronizationEvent(
                new MemoryGraphStore(graph).getGraph(),
                new MemoryProhibitionsStore(prohibitions.getAll()).getAll(),
                new MemoryObligationsStore(obligations.getAll()).getAll(),
                new MemoryPALStore().getContext()
        );
    }

    @Override
    public synchronized void beginTx() throws PMException {
        graph.beginTx();
        prohibitions.beginTx();
        obligations.beginTx();
        pal.beginTx();
    }

    @Override
    public synchronized void commit() throws PMException {
        graph.commit();
        prohibitions.commit();
        obligations.commit();
        pal.commit();
    }

    @Override
    public synchronized void rollback() throws TransactionNotStartedException {
        graph.rollback();
        prohibitions.rollback();
        obligations.rollback();
        pal.rollback();
    }

    @Override
    public GraphStore graph() {
        return graph;
    }

    @Override
    public ProhibitionsStore prohibitions() {
        return prohibitions;
    }

    @Override
    public ObligationsStore obligations() {
        return obligations;
    }

    @Override
    public PALStore pal() {
        return pal;
    }
}
