package gov.nist.csd.pm.pip.memory.tx.cmd.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.memory.tx.cmd.TxCmd;

import java.util.Set;

public class DeleteNodeTxCmd implements TxCmd {

    private Graph graph;
    private String name;
    private Node node;
    private Set<String> parents;

    public DeleteNodeTxCmd(Graph graph, String name) {
        this.graph = graph;
        this.name = name;
    }

    @Override
    public void commit() throws PMException {
        node = graph.getNode(name);
        parents = graph.getParents(name);
        graph.deleteNode(name);
    }
}
