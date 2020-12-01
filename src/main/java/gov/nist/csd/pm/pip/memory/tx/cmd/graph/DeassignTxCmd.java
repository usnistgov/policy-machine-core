package gov.nist.csd.pm.pip.memory.tx.cmd.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.tx.cmd.TxCmd;

public class DeassignTxCmd implements TxCmd {

    private Graph graph;
    private String child;
    private String parent;

    public DeassignTxCmd(Graph graph, String child, String parent) {
        this.graph = graph;
        this.child = child;
        this.parent = parent;
    }

    public String getChild() {
        return child;
    }

    public String getParent() {
        return parent;
    }

    @Override
    public void commit() throws PMException {
        graph.deassign(child, parent);
    }
}
