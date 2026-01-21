package gov.nist.csd.pm.core.pap.operation.param;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.operation.RequiredCapabilities;

public final class NodeNameFormalParameter extends NodeFormalParameter<String> {

    public NodeNameFormalParameter(String name, RequiredCapabilities reqCap) {
        super(name, STRING_TYPE, reqCap);
    }

    public NodeNameFormalParameter(String name, String... reqCaps) {
        super(name, STRING_TYPE, reqCaps);
    }
}
