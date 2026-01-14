package gov.nist.csd.pm.core.pap.function.op.arg;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.LONG_TYPE;

import gov.nist.csd.pm.core.pap.function.RequiredCapabilities;

public class NodeIdFormalParameter extends NodeFormalParameter<Long> {

    public NodeIdFormalParameter(String name, RequiredCapabilities reqCap) {
        super(name, LONG_TYPE, reqCap);
    }

    public NodeIdFormalParameter(String name, String... reqCaps) {
        super(name, LONG_TYPE, reqCaps);
    }
}
