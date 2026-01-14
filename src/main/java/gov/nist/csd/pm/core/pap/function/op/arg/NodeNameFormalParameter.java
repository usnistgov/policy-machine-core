package gov.nist.csd.pm.core.pap.function.op.arg;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.function.RequiredCapabilities;

public class NodeNameFormalParameter extends NodeFormalParameter<String> {

    public NodeNameFormalParameter(String name, RequiredCapabilities reqCap) {
        super(name, STRING_TYPE, reqCap);
    }

    public NodeNameFormalParameter(String name, String... reqCaps) {
        super(name, STRING_TYPE, reqCaps);
    }
}
