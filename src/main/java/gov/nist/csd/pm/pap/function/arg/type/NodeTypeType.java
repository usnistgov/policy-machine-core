package gov.nist.csd.pm.pap.function.arg.type;

import gov.nist.csd.pm.common.graph.node.NodeType;

public final class NodeTypeType extends ArgType<NodeType> {

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
} 