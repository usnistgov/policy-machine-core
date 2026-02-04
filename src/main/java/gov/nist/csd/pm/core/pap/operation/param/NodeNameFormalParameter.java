package gov.nist.csd.pm.core.pap.operation.param;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;

public final class NodeNameFormalParameter extends NodeFormalParameter<String> {

    public NodeNameFormalParameter(String name, AccessRightSet reqCaps) {
        super(name, STRING_TYPE, reqCaps);
    }

    public NodeNameFormalParameter(String name, AdminAccessRight... accessRights) {
        super(name, STRING_TYPE, accessRights);
    }
}
