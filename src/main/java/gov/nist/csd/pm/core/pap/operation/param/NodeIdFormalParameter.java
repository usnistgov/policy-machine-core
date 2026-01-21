package gov.nist.csd.pm.core.pap.operation.param;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;

import gov.nist.csd.pm.core.pap.operation.RequiredCapabilities;

public final class NodeIdFormalParameter extends NodeFormalParameter<Long> {

    public NodeIdFormalParameter(String name, RequiredCapabilities reqCap) {
        super(name, LONG_TYPE, reqCap);
    }

    public NodeIdFormalParameter(String name, String... reqCaps) {
        super(name, LONG_TYPE, reqCaps);
    }
}
