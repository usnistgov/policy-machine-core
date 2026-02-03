package gov.nist.csd.pm.core.pap.operation.param;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;

import gov.nist.csd.pm.core.pap.operation.accessrights.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import java.util.List;

public final class NodeIdListFormalParameter extends NodeFormalParameter<List<Long>> {

    public NodeIdListFormalParameter(String name, AccessRightSet reqCaps) {
        super(name, ListType.of(LONG_TYPE), reqCaps);
    }

    public NodeIdListFormalParameter(String name, String... reqCaps) {
        super(name,  ListType.of(LONG_TYPE), reqCaps);
    }
}
