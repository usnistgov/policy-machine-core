package gov.nist.csd.pm.core.pap.function.op.arg;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.LONG_TYPE;

import gov.nist.csd.pm.core.pap.function.RequiredCapabilities;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import java.util.List;

public final class NodeIdListFormalParameter extends NodeFormalParameter<List<Long>> {

    public NodeIdListFormalParameter(String name, RequiredCapabilities reqCap) {
        super(name, ListType.of(LONG_TYPE), reqCap);
    }

    public NodeIdListFormalParameter(String name, String... reqCaps) {
        super(name,  ListType.of(LONG_TYPE), reqCaps);
    }
}
