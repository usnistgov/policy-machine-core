package gov.nist.csd.pm.core.pap.operation.param;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import java.util.List;

public final class NodeNameListFormalParameter extends NodeFormalParameter<List<String>>{

    public NodeNameListFormalParameter(String name, AccessRightSet reqCaps) {
        super(name, ListType.of(STRING_TYPE), reqCaps);
    }

    public NodeNameListFormalParameter(String name, String... reqCaps) {
        super(name, ListType.of(STRING_TYPE), reqCaps);
    }
}
