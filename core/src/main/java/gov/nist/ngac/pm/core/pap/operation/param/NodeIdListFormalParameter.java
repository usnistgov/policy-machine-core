package gov.nist.ngac.pm.core.pap.operation.param;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;

import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import java.util.List;

public final class NodeIdListFormalParameter extends NodeFormalParameter<List<Long>> {

    public NodeIdListFormalParameter(String name) {
        super(name, ListType.of(LONG_TYPE));
    }
}
