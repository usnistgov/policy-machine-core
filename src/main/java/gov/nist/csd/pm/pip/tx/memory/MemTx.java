package gov.nist.csd.pm.pip.tx.memory;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.MemObligations;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import gov.nist.csd.pm.pip.tx.Tx;
import gov.nist.csd.pm.pip.tx.TxRunner;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.PC;

public class MemTx implements Tx {

    private MemGraph graph;
    private MemProhibitions prohibitions;
    private MemObligations obligations;
    private TxGraph txGraph;
    private TxProhibitions txProhibitions;
    private TxObligations txObligations;

    public MemTx(MemGraph graph, MemProhibitions prohibitions, MemObligations obligations) {
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.txGraph = new TxGraph(graph);
        this.txProhibitions = new TxProhibitions();
        this.txObligations = new TxObligations();
    }

    @Override
    public void runTx(TxRunner runner) throws PMException {
        try {
            this.graph.lock();
            this.prohibitions.lock();
            this.obligations.lock();
            runner.run(txGraph, txProhibitions, txObligations);
        } finally {
            this.graph.unlock();
            this.prohibitions.unlock();
            this.obligations.unlock();
        }
    }

    @Override
    public void commit() throws PMException {
        // commit the graph
        txGraph.commit();

        // commit the prohibitions
        txProhibitions.commit();

        // commit the obligations
        txProhibitions.commit();
    }

    public void rollback() {
        // rollback graph
        this.txGraph = new TxGraph(graph);

        // rollback prohibitions
        this.txProhibitions = new TxProhibitions();

        // rollback obligations
        this.txObligations = new TxObligations();
    }
}
