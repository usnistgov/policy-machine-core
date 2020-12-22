package gov.nist.csd.pm.pip.memory.tx.cmd.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.tx.cmd.TxCmd;

public class AssignTxCmd implements TxCmd {

    private Graph graph;
    private String child;
    private String parent;

    public AssignTxCmd(Graph graph, String child, String parent) {
        this.graph = graph;
        this.child = child;
        this.parent = parent;
    }

    @Override
    public void commit() throws PMException {
        graph.assign(child, parent);
    }

}
