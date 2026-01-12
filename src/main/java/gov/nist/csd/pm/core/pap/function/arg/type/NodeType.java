package gov.nist.csd.pm.core.pap.function.arg.type;

import gov.nist.csd.pm.core.pap.function.arg.NodeArg;

public final class NodeType extends Type<NodeArg<?>> {

    @Override
    public NodeArg<?> cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (obj instanceof NodeArg<?> nodeArg) {
            return nodeArg;
        }
        throw new IllegalArgumentException("cannot convert " + obj.getClass() + " to NodeArg");
    }
}
