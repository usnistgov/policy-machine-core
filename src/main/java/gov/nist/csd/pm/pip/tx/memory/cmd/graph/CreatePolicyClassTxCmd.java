package gov.nist.csd.pm.pip.tx.memory.cmd.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.tx.memory.cmd.TxCmd;

import java.util.Map;

public class CreatePolicyClassTxCmd implements TxCmd {

    private Graph graph;
    private String name;
    private Map<String, String> properties;

    public CreatePolicyClassTxCmd(Graph graph, String name, Map<String, String> properties) {
        this.graph = graph;
        this.name = name;
        this.properties = properties;
    }

    @Override
    public void commit() throws PMException {
        graph.createPolicyClass(name, properties);
    }

    @Override
    public void rollback() throws PMException {
        graph.deleteNode(name);
    }
}