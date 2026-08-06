package gov.nist.ngac.pm.core.pap.operation.graph;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import java.util.List;

/**
 * Admin operation "create_object": creates a new object node.
 */
public class CreateObjectOp extends CreateNodeOp {

    public CreateObjectOp() {
        super("create_object");
    }

    @Override
    protected long createNode(PAP pap, String name, List<Long> descs) throws PMException {
        return pap.modify().graph().createObject(name, descs);
    }

}
