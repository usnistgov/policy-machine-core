package gov.nist.csd.pm.core.pap.function.op.arg;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.function.RequiredCapabilities;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import java.util.List;

public final class NodeNameListFormalParameter extends NodeFormalParameter<List<String>>{

    public NodeNameListFormalParameter(String name, RequiredCapabilities reqCap) {
        super(name, ListType.of(STRING_TYPE), reqCap);
    }

    public NodeNameListFormalParameter(String name, String... reqCaps) {
        super(name, ListType.of(STRING_TYPE), reqCaps);
    }
}
