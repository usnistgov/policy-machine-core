package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.function.arg.type.Type;

final class NodeTypeType extends Type<NodeType> {

    @Override
    public NodeType cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof NodeType nodeType)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to NodeType");
        }

        return nodeType;
    }

    @Override
    public Class<NodeType> getExpectedClass() {
        return NodeType.class;
    }
}