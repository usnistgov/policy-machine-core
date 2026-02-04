package gov.nist.csd.pm.core.pap.operation.param;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;

import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import java.util.List;

public final class NodeIdListFormalParameter extends NodeFormalParameter<List<Long>> {

    public NodeIdListFormalParameter(String name, AccessRightSet reqCaps) {
        super(name, ListType.of(LONG_TYPE), reqCaps);
    }

    public NodeIdListFormalParameter(String name, AdminAccessRight... accessRights) {
        super(name, ListType.of(LONG_TYPE), accessRights);
    }
}
