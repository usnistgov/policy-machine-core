package gov.nist.csd.pm.pip.tx.memory.cmd.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.tx.memory.cmd.TxCmd;

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

    @Override
    public void rollback() throws PMException {
        String initialParent = parents.iterator().next();
        parents.remove(initialParent);
        graph.createNode(node.getName(), node.getType(), node.getProperties(), initialParent, parents.toArray(new String[0]));
    }
}
