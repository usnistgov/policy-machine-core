package gov.nist.csd.pm.core.pap.operation.param;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;

public final class NodeIdFormalParameter extends NodeFormalParameter<Long> {

    public NodeIdFormalParameter(String name) {
        super(name, LONG_TYPE);
    }
}
