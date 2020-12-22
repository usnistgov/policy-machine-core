package gov.nist.csd.pm.pip.memory.tx.cmd.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.tx.cmd.TxCmd;

public class AssociateTxCmd implements TxCmd {

    private Graph graph;
    private String ua;
    private String target;
    private OperationSet operations;

    public AssociateTxCmd(Graph graph, String ua, String target, OperationSet operations) {
        this.graph = graph;
        this.ua = ua;
        this.target = target;
        this.operations = operations;
    }

    @Override
    public void commit() throws PMException {
        graph.associate(ua, target, operations);
    }

}
