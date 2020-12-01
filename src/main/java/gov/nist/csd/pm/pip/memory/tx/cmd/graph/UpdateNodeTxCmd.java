package gov.nist.csd.pm.pip.memory.tx.cmd.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.memory.tx.cmd.TxCmd;

import java.util.HashMap;
import java.util.Map;

public class UpdateNodeTxCmd implements TxCmd {

    private Graph graph;
    private String name;
    private Map<String, String> properties;
    private Map<String, String> originalProperties;

    public UpdateNodeTxCmd(Graph graph, String name, Map<String, String> properties) {
        this.graph = graph;
        this.name = name;
        this.properties = properties;
        this.originalProperties = new HashMap<>();
    }

    @Override
    public void commit() throws PMException {
        Node node = graph.getNode(name);
        originalProperties = node.getProperties();
        graph.updateNode(name, properties);
    }
}
