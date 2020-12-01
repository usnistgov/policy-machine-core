package gov.nist.csd.pm.pip.memory.tx.cmd.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.memory.tx.cmd.TxCmd;

import java.util.Map;
import java.util.Set;

public class CreateNodeTxCmd implements TxCmd {

    private Graph graph;
    private String name;
    private NodeType type;
    private Map<String, String> properties;
    private Set<String> parents;

    public CreateNodeTxCmd(Graph graph, String name, NodeType type, Map<String, String> properties, Set<String> parents) {
        this.graph = graph;
        this.name = name;
        this.type = type;
        this.properties = properties;
        this.parents = parents;
    }

    @Override
    public void commit() throws PMException {
        String initialParent = parents.iterator().next();
        parents.remove(initialParent);
        graph.createNode(name, type, properties, initialParent, parents.toArray(new String[0]));
    }

}