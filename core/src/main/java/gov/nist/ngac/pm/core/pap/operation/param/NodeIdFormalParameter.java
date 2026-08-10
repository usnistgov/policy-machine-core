package gov.nist.ngac.pm.core.pap.operation.param;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;

/**
 * A formal parameter referencing a single node by id.
 */
public final class NodeIdFormalParameter extends NodeFormalParameter<Long> {

    public NodeIdFormalParameter(String name) {
        super(name, LONG_TYPE);
    }
}
