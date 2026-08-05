package gov.nist.ngac.pm.core.pap.operation.param;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

public final class NodeNameFormalParameter extends NodeFormalParameter<String> {

    public NodeNameFormalParameter(String name) {
        super(name, STRING_TYPE);
    }
}
