package gov.nist.csd.pm.core.pap.operation.param;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;

import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;

public final class NodeIdFormalParameter extends NodeFormalParameter<Long> {

    public NodeIdFormalParameter(String name) {
        super(name, LONG_TYPE);
    }
}
