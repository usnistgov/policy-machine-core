package gov.nist.csd.pm.core.pap.operation.param;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import java.util.List;

public final class NodeNameListFormalParameter extends NodeFormalParameter<List<String>>{

    public NodeNameListFormalParameter(String name) {
        super(name, ListType.of(STRING_TYPE));
    }
}
