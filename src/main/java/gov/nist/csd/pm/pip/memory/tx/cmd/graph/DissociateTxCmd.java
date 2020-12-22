package gov.nist.csd.pm.pip.memory.tx.cmd.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.tx.cmd.TxCmd;

import java.util.Map;

public class DissociateTxCmd implements TxCmd {

    private Graph graph;
    private String ua;
    private String target;
    private OperationSet operations;

    public DissociateTxCmd(Graph graph, String ua, String target) {
        this.graph = graph;
        this.ua = ua;
        this.target = target;
    }

    public String getUa() {
        return ua;
    }

    public String getTarget() {
        return target;
    }

    public OperationSet getOperations() {
        return operations;
    }

    @Override
    public void commit() throws PMException {
        Map<String, OperationSet> assocs = graph.getSourceAssociations(ua);
        operations = assocs.get(target);
        graph.dissociate(ua, target);
    }
}
