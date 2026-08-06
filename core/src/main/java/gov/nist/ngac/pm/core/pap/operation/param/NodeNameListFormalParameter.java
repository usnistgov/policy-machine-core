package gov.nist.ngac.pm.core.pap.operation.param;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import java.util.List;

/**
 * A formal parameter referencing a list of nodes by name.
 */
public final class NodeNameListFormalParameter extends NodeFormalParameter<List<String>>{

    public NodeNameListFormalParameter(String name) {
        super(name, ListType.of(STRING_TYPE));
    }
}
