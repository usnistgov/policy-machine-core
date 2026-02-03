package gov.nist.csd.pm.core.pap.operation.param;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;

import gov.nist.csd.pm.core.pap.operation.accessrights.AccessRightSet;

public final class NodeIdFormalParameter extends NodeFormalParameter<Long> {

    public NodeIdFormalParameter(String name, AccessRightSet reqCaps) {
        super(name, LONG_TYPE, reqCaps);
    }

    public NodeIdFormalParameter(String name, String... reqCaps) {
        super(name, LONG_TYPE, reqCaps);
    }
}
