package gov.nist.csd.pm.pap.memory.unmodifiable;

import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

import java.util.Map;

public class UnmodifiableNode extends Node {

    public UnmodifiableNode() {
        super();
    }

    public UnmodifiableNode(Node node) {
        super(node);
    }

    public UnmodifiableNode(String name, NodeType type, Map<String, String> properties) {
        super(name, type, properties);
    }

    public UnmodifiableNode(String name, NodeType type) {
        super(name, type);
    }

    public UnmodifiableNode(String name) {
        super(name);
    }

    @Override
    public Node addProperty(String key, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setType(NodeType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setProperties(Map<String, String> properties) {
        throw new UnsupportedOperationException();
    }
}
